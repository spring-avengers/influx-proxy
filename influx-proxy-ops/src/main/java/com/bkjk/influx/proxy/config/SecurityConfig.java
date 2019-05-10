package com.bkjk.influx.proxy.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.security.servlet.PathRequest;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.ldap.authentication.ad.ActiveDirectoryLdapAuthenticationProvider;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@Configuration
public class SecurityConfig extends WebSecurityConfigurerAdapter {
    @Value("${ldap.url:}")
    private String url;
    @Value("${ldap.domain:}")
    private String domain;
    @Value("${ldap.rootDn:}")
    private String rootDn;
    @Value("${ldap.searchFilter:}")
    private String searchFilter;
    @Value("${ldap.adminUserName:shaoze.wang,admin}")
    private String adminUserName;

    @Value("${ldap.facker:false}")
    private boolean facker;

    @Autowired
    public PasswordEncoder passwordEncoder;

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http
                .authorizeRequests()
                .antMatchers(HttpMethod.GET).authenticated()
                .antMatchers(HttpMethod.OPTIONS).authenticated()
                .anyRequest().hasAnyRole("ADMIN")
                .and().headers().frameOptions().sameOrigin()
                .and().formLogin().loginPage("/login").permitAll()
                .and().csrf().disable();
    }

    @Override
    public void configure(WebSecurity web) throws Exception {
        List<String> ignore = Arrays.asList("/health", "/actuator/**");
        web.
                ignoring()
                .requestMatchers(PathRequest.toStaticResources().atCommonLocations())
                .antMatchers(ignore.toArray(new String[0]))
                .antMatchers("/api/**");
    }

    @Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {

        if (facker) {
            auth.authenticationProvider(new AuthenticationProvider() {
                @Override
                public Authentication authenticate(Authentication authentication) throws AuthenticationException {
                    ArrayList<GrantedAuthority> authorities = new ArrayList<>();
                    if (Arrays.asList(adminUserName.split(",")).stream().anyMatch(s -> s.equalsIgnoreCase(authentication.getName()))) {
                        authorities.add(new SimpleGrantedAuthority("ROLE_ADMIN"));
                    }
                    return new UsernamePasswordAuthenticationToken(
                            authentication.getPrincipal(),
                            authentication.getCredentials(),
                            authorities);
                }

                @Override
                public boolean supports(Class<?> authentication) {
                    return true;
                }
            });
        } else {
            ActiveDirectoryLdapAuthenticationProvider adProvider =
                    new ActiveDirectoryLdapAuthenticationProvider(domain,url,rootDn);
            adProvider.setConvertSubErrorCodesToExceptions(true);
            adProvider.setUseAuthenticationRequestCredentials(true);
            if (searchFilter != null && searchFilter.trim().length() > 0)
            {
                adProvider.setSearchFilter(searchFilter);
            }
            auth.authenticationProvider(new AuthenticationProvider(){

                @Override
                public Authentication authenticate(Authentication authentication) throws AuthenticationException {
                    ArrayList<GrantedAuthority> authorities=new ArrayList<>();
                    if(Arrays.asList(adminUserName.split(",")).stream().anyMatch(s->s.equalsIgnoreCase(authentication.getName()))){
                        authorities.add(new SimpleGrantedAuthority("ROLE_ADMIN"));
                    }
                    Authentication auth0 = adProvider.authenticate(authentication);
                    authorities.addAll(auth0.getAuthorities());
                    UsernamePasswordAuthenticationToken authenticationToken=new UsernamePasswordAuthenticationToken(
                            auth0.getPrincipal(),
                            auth0.getCredentials(),
                            authorities);
                    return authenticationToken;
                }

                @Override
                public boolean supports(Class<?> authentication) {
                    return adProvider.supports(authentication);
                }
            });

        }
        auth.eraseCredentials(false);
    }
}
