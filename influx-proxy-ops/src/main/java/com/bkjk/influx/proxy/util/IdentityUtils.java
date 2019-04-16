/**
 * =============================================================
 * Copyright 2018 Lianjia Group All Rights Reserved
 * CompanyName: 上海链家有限公司
 * SystemName: 贝壳
 * ClassName: IdentityUtils
 * version: 1.0.0
 * date: 2019/3/15
 * author: Tyson
 * =============================================================
 */
package com.bkjk.influx.proxy.util;

import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.ldap.userdetails.LdapUserDetailsImpl;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

/**
 * @author Tyson
 * @version V1.0
 * @Description: TODO
 * @date 2019/3/15上午11:07
 */
public class IdentityUtils {

    public static String getUserNameDN() {
        Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        if (principal instanceof LdapUserDetailsImpl) {
            List<String> name = Arrays.asList(((LdapUserDetailsImpl) principal).getDn().split(",")).stream().map(s -> s.split("=")[1]).limit(2).collect(Collectors.toList());
            return name.size()==1?name.get(0):String.format("%s(%s)", name.get(0),name.get(1));
        }
        return getUserName();
    }

    public static String getUserName() {
        Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        if (principal instanceof UserDetails) {
            return ((UserDetails) principal).getUsername();
        }
        return principal.toString();
    }
}
