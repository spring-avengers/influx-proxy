package com.bkjk.influx.proxy.common.micrometer;

import io.micrometer.core.instrument.Meter;
import io.micrometer.core.instrument.MeterRegistry;
import io.micrometer.core.instrument.Tag;
import io.micrometer.core.instrument.config.MeterFilter;
import io.micrometer.core.instrument.distribution.DistributionStatisticConfig;
import org.springframework.boot.actuate.autoconfigure.metrics.MeterRegistryCustomizer;
import org.springframework.util.StringUtils;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import static java.util.stream.Collectors.toList;
import static java.util.stream.StreamSupport.stream;

public class MyRegistryCustomizer implements MeterRegistryCustomizer {


    private static class TagIgnoreMeterFilter implements MeterFilter {
        private List<String> tagKeys = new ArrayList<>();

        @Override
        public Meter.Id map(Meter.Id id) {
            if (null == tagKeys || tagKeys.size() == 0) {
                return id;
            }
            List<Tag> tags = stream(id.getTagsAsIterable().spliterator(), false).filter(t -> {
                for (String tagKey : tagKeys) {
                    if (t.getKey().equals(tagKey))
                        return false;
                }
                return true;
            }).collect(toList());

            return id.replaceTags(tags);
        }

    }

    private static class TimerMeterFilter implements MeterFilter {
        private static final String DEFAULT_PERCENTILES = "0.8, 0.9, 0.95, 0.99";
        double[] percentiles;

        Set<Meter.Type> types =
            Arrays.asList(Meter.Type.TIMER, Meter.Type.LONG_TASK_TIMER, Meter.Type.DISTRIBUTION_SUMMARY).stream()
                .collect(Collectors.toSet());

        @Override
        public DistributionStatisticConfig configure(Meter.Id id, DistributionStatisticConfig config) {
            if (types.contains(id.getType())) {
                return DistributionStatisticConfig.builder().percentiles(getPercentiles()).build().merge(config);
            }
            return config;
        }

        public double[] getPercentiles() {
            if (percentiles == null) {
                String percentileString="";
                if (StringUtils.isEmpty(percentileString)) {
                    percentileString = DEFAULT_PERCENTILES;
                }
                percentiles = Arrays.asList(percentileString.split(",")).stream().mapToDouble(s -> {
                    return Double.valueOf(s).doubleValue();
                }).toArray();
            }
            return percentiles;
        }
    }

    private final PlatformTag platformTag;


    public MyRegistryCustomizer(PlatformTag platformTag) {
        this.platformTag = platformTag;
    }

    @Override
    public void customize(MeterRegistry registry) {
        registry.config().commonTags(platformTag.getTags()).meterFilter(new TimerMeterFilter())
            .meterFilter(new TagIgnoreMeterFilter())
            .meterFilter(MeterFilter.denyNameStartsWith("http.client.requests"));
    }

}
