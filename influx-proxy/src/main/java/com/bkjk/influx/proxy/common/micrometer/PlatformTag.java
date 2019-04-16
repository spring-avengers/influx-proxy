package com.bkjk.influx.proxy.common.micrometer;

import io.micrometer.core.instrument.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cloud.commons.util.InetUtils;

import java.util.Arrays;
import java.util.List;

public class PlatformTag {

    @Value("${spring.profiles.active:none}")
    private String activeProfiles;
    @Value("${spring.application.name:none}")
    private String applicationName;
    @Value("${spring.application.group:none}")
    private String applicationGroup;
    @Value("${spring.application.version:none}")
    private String applicationVersion;
    @Value("${eureka.instance.metadata-map.zone:none}")
    private String zone;

    @Autowired
    private InetUtils inetUtils;

    private List<Tag> tags;

    public PlatformTag() {
    }

    private String getEurekaZone() {

        return zone;
    }

    public List<Tag> getTags() {
        if (tags == null) {
            tags = Arrays.asList(Tag.of("application.name", applicationName),
                Tag.of("application.group", applicationGroup), Tag.of("application.version", applicationVersion),
                Tag.of("application.profiles.active", activeProfiles), Tag.of("zone", getEurekaZone()),
                Tag.of("ip", inetUtils.findFirstNonLoopbackHostInfo().getIpAddress()));
        }
        return tags;
    }

}
