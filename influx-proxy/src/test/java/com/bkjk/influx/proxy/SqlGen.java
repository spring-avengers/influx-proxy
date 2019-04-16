package com.bkjk.influx.proxy;

import org.springframework.util.StringUtils;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * @Program: influx-proxy
 * @Description:
 * @Author: shaoze.wang
 * @Create: 2019/3/19 17:48
 **/
public class SqlGen {
    public static void main(String[] args) {
//        dev(args);
        prod(args);
    }
    public static void dev(String[] args) {
        String d="cache_evictions\n" +
                "cache_gets\n" +
                "cache_local_disk_size\n" +
                "cache_local_heap_size\n" +
                "cache_local_offheap_size\n" +
                "cache_misses\n" +
                "cache_puts\n" +
                "cache_puts_added\n" +
                "cache_remoteSize\n" +
                "cache_removals\n" +
                "cache_size\n" +
                "cache_xa_commits\n" +
                "cache_xa_recoveries\n" +
                "cache_xa_rollbacks\n" +
                "code_execute_count\n" +
                "code_execute_timer\n" +
                "code_execute_timer_percentile\n" +
                "dataSource_connections_active\n" +
                "dataSource_connections_max\n" +
                "dataSource_connections_min\n" +
                "disk.free\n" +
                "disk.total\n" +
                "disk_free\n" +
                "disk_total\n" +
                "exchange_order_GoldenKey\n" +
                "health\n" +
                "health.discoverycomposite\n" +
                "health.diskspace\n" +
                "health.hystrix\n" +
                "health.refreshscope\n" +
                "health_composite\n" +
                "health_configserver\n" +
                "health_datasource\n" +
                "health_discoverycomposite\n" +
                "health_diskspace\n" +
                "health_elasticsearch\n" +
                "health_hystrix\n" +
                "health_mail\n" +
                "health_mongo\n" +
                "health_rabbit\n" +
                "health_redis\n" +
                "health_refreshscope\n" +
                "hikaricp_connections\n" +
                "hikaricp_connections_acquire\n" +
                "hikaricp_connections_acquire_percentile\n" +
                "hikaricp_connections_active\n" +
                "hikaricp_connections_creation\n" +
                "hikaricp_connections_creation_percentile\n" +
                "hikaricp_connections_idle\n" +
                "hikaricp_connections_max\n" +
                "hikaricp_connections_min\n" +
                "hikaricp_connections_pending\n" +
                "hikaricp_connections_timeout\n" +
                "hikaricp_connections_usage\n" +
                "hikaricp_connections_usage_percentile\n" +
                "http_server_requests\n" +
                "http_server_requests_percentile\n" +
                "hystrix_circuit_breaker_open\n" +
                "hystrix_command_other\n" +
                "hystrix_concurrent_execution_current\n" +
                "hystrix_concurrent_execution_rolling_max\n" +
                "hystrix_errors\n" +
                "hystrix_execution\n" +
                "hystrix_execution_terminal\n" +
                "hystrix_fallback\n" +
                "hystrix_latency_execution\n" +
                "hystrix_latency_execution_percentile\n" +
                "hystrix_latency_total\n" +
                "hystrix_latency_total_percentile\n" +
                "hystrix_requests\n" +
                "hystrix_threadpool_concurrent_execution_current\n" +
                "hystrix_threadpool_concurrent_execution_rolling_max\n" +
                "issue_GoldenKey\n" +
                "jdbc_connections_active\n" +
                "jdbc_connections_max\n" +
                "jdbc_connections_min\n" +
                "jvm.buffer.count\n" +
                "jvm.buffer.memory.used\n" +
                "jvm.buffer.total.capacity\n" +
                "jvm.classes.loaded\n" +
                "jvm.classes.unloaded\n" +
                "jvm.gc.live.data.size\n" +
                "jvm.gc.max.data.size\n" +
                "jvm.gc.memory.allocated\n" +
                "jvm.gc.memory.promoted\n" +
                "jvm.gc.pause\n" +
                "jvm.gc.pause.percentile\n" +
                "jvm.memory.committed\n" +
                "jvm.memory.max\n" +
                "jvm.memory.used\n" +
                "jvm.threads.daemon\n" +
                "jvm.threads.live\n" +
                "jvm.threads.peak\n" +
                "jvm_buffer_count\n" +
                "jvm_buffer_memory_used\n" +
                "jvm_buffer_total_capacity\n" +
                "jvm_classes_loaded\n" +
                "jvm_classes_unloaded\n" +
                "jvm_gc_concurrent_phase_time\n" +
                "jvm_gc_concurrent_phase_time_percentile\n" +
                "jvm_gc_live_data_size\n" +
                "jvm_gc_max_data_size\n" +
                "jvm_gc_memory_allocated\n" +
                "jvm_gc_memory_promoted\n" +
                "jvm_gc_pause\n" +
                "jvm_gc_pause_percentile\n" +
                "jvm_memory_committed\n" +
                "jvm_memory_max\n" +
                "jvm_memory_used\n" +
                "jvm_threads_daemon\n" +
                "jvm_threads_live\n" +
                "jvm_threads_peak\n" +
                "jvm_threads_states\n" +
                "kafka_consumer_assigned_partitions\n" +
                "kafka_consumer_bytes_consumed_total\n" +
                "kafka_consumer_commit_latency_avg\n" +
                "kafka_consumer_commit_rate\n" +
                "kafka_consumer_connection_count\n" +
                "kafka_consumer_fetch_latency_avg\n" +
                "kafka_consumer_fetch_latency_max\n" +
                "kafka_consumer_fetch_size_avg\n" +
                "kafka_consumer_fetch_size_max\n" +
                "kafka_consumer_fetch_throttle_time_avg\n" +
                "kafka_consumer_fetch_throttle_time_max\n" +
                "kafka_consumer_fetch_total\n" +
                "kafka_consumer_heartbeat_rate\n" +
                "kafka_consumer_io_ratio\n" +
                "kafka_consumer_io_wait_ratio\n" +
                "kafka_consumer_io-time-avg\n" +
                "kafka_consumer_io-wait-time-avg\n" +
                "kafka_consumer_join_rate\n" +
                "kafka_consumer_join_time_avg\n" +
                "kafka_consumer_join_time_max\n" +
                "kafka_consumer_last-heartbeat\n" +
                "kafka_consumer_records_consumed_total\n" +
                "kafka_consumer_records_per_request_avg\n" +
                "kafka_consumer_sync_rate\n" +
                "kafka_consumer_sync_time_avg\n" +
                "kafka_consumer_sync_time_max\n" +
                "kafka_producer_connection_count\n" +
                "kafka_producer_io_ratio\n" +
                "kafka_producer_io-time-avg\n" +
                "kafka_producer_io-wait-time-avg\n" +
                "kafka_producer_select_total\n" +
                "log4j2_events\n" +
                "logback.events\n" +
                "logback_events\n" +
                "method_timed\n" +
                "method_timed_percentile\n" +
                "method_with_url_count\n" +
                "method_with_url_timer\n" +
                "method_with_url_timer_percentile\n" +
                "micormeter.meters.size\n" +
                "micormeter_meters_size\n" +
                "mymeas\n" +
                "openfeign\n" +
                "openfeign_percentile\n" +
                "order_goldenKey_success\n" +
                "primary-_connections_active\n" +
                "primary-_connections_max\n" +
                "primary-_connections_min\n" +
                "process.cpu.usage\n" +
                "process.start.time\n" +
                "process.uptime\n" +
                "process_cpu_usage\n" +
                "process_files_max\n" +
                "process_files_open\n" +
                "process_start_time\n" +
                "process_uptime\n" +
                "rabbit_acknowledged\n" +
                "rabbit_cacheConnectionProperties_channelCacheSize\n" +
                "rabbit_cacheConnectionProperties_idleChannelsNotTx\n" +
                "rabbit_cacheConnectionProperties_idleChannelsNotTxHighWater\n" +
                "rabbit_cacheConnectionProperties_idleChannelsTx\n" +
                "rabbit_cacheConnectionProperties_idleChannelsTxHighWater\n" +
                "rabbit_channels\n" +
                "rabbit_connections\n" +
                "rabbit_consumed\n" +
                "rabbit_published\n" +
                "rabbit_rejected\n" +
                "rabbitmq_acknowledged\n" +
                "rabbitmq_acknowledged_published\n" +
                "rabbitmq_channels\n" +
                "rabbitmq_connections\n" +
                "rabbitmq_consumed\n" +
                "rabbitmq_failed_to_publish\n" +
                "rabbitmq_not_acknowledged_published\n" +
                "rabbitmq_published\n" +
                "rabbitmq_rejected\n" +
                "rabbitmq_unrouted_published\n" +
                "redis_pool_active\n" +
                "redis_pool_borrowed\n" +
                "redis_pool_created\n" +
                "redis_pool_destroyed\n" +
                "redis_pool_idle\n" +
                "redis_pool_total\n" +
                "redis_pool_waiters\n" +
                "rest_template_timer\n" +
                "rest_template_timer_percentile\n" +
                "spring_integration_channels\n" +
                "spring_integration_handlers\n" +
                "spring_integration_send\n" +
                "spring_integration_send_percentile\n" +
                "spring_integration_sources\n" +
                "sql_execute_time\n" +
                "sql_execute_time_percentile\n" +
                "system.cpu.count\n" +
                "system.cpu.usage\n" +
                "system_cpu_count\n" +
                "system_cpu_usage\n" +
                "system_load_average_1m\n" +
                "tesla_inprogress_requests\n" +
                "tesla_request_size\n" +
                "tesla_request_size_percentile\n" +
                "tesla_requests_forward_latency\n" +
                "tesla_requests_forward_latency_histogram\n" +
                "tesla_requests_forward_latency_percentile\n" +
                "tesla_requests_latency\n" +
                "tesla_requests_latency_histogram\n" +
                "tesla_requests_latency_percentile\n" +
                "tesla_requests_total\n" +
                "tesla_response_size\n" +
                "tesla_response_size_percentile\n" +
                "thirdParty-facade-invoke\n" +
                "thirdParty-facade-invoke_percentile\n" +
                "ThreadPoolTaskExecutor_active\n" +
                "ThreadPoolTaskExecutor_completed\n" +
                "ThreadPoolTaskExecutor_pool\n" +
                "ThreadPoolTaskExecutor_queued\n" +
                "ThreadPoolTaskScheduler_active\n" +
                "ThreadPoolTaskScheduler_completed\n" +
                "ThreadPoolTaskScheduler_pool\n" +
                "ThreadPoolTaskScheduler_queued\n" +
                "tomcat.cache.access\n" +
                "tomcat.cache.hit\n" +
                "tomcat.global.error\n" +
                "tomcat.global.received\n" +
                "tomcat.global.request\n" +
                "tomcat.global.request.max\n" +
                "tomcat.global.sent\n" +
                "tomcat.servlet.error\n" +
                "tomcat.servlet.request\n" +
                "tomcat.servlet.request.max\n" +
                "tomcat.sessions.active.current\n" +
                "tomcat.sessions.active.max\n" +
                "tomcat.sessions.alive.max\n" +
                "tomcat.sessions.created\n" +
                "tomcat.sessions.expired\n" +
                "tomcat.sessions.rejected\n" +
                "tomcat.threads.busy\n" +
                "tomcat.threads.config.max\n" +
                "tomcat.threads.current\n" +
                "tomcat_cache_access\n" +
                "tomcat_cache_hit\n" +
                "tomcat_global_error\n" +
                "tomcat_global_received\n" +
                "tomcat_global_request\n" +
                "tomcat_global_request_max\n" +
                "tomcat_global_sent\n" +
                "tomcat_servlet_error\n" +
                "tomcat_servlet_request\n" +
                "tomcat_servlet_request_max\n" +
                "tomcat_sessions_active_current\n" +
                "tomcat_sessions_active_max\n" +
                "tomcat_sessions_alive_max\n" +
                "tomcat_sessions_created\n" +
                "tomcat_sessions_expired\n" +
                "tomcat_sessions_rejected\n" +
                "tomcat_threads_busy\n" +
                "tomcat_threads_config_max\n" +
                "tomcat_threads_current\n" +
                "trace_event_count\n" +
                "transaction\n" +
                "transaction_percentile\n" +
                "uus_authservice_get_oauth_authorize\n" +
                "uus_authservice_request_uus\n" +
                "uus_authservice_token\n" +
                "uus_uus_create_account\n" +
                "uus_uus_login\n" +
                "uus_uus_register\n" +
                "uus_uus_send_message_code\n" +
                "uus_uus_silent_login";
        Set<String> set = Arrays.asList(d.split("\n")).stream()
                .filter(s -> !StringUtils.isEmpty(s))
                .map(s -> s.split("\\.")[0])
                .map(s -> s.split("_")[0])
                .map(String::toLowerCase)
                .collect(Collectors.toSet());
        ArrayList<String> list=new ArrayList<>();
        list.addAll(set);
        list.sort(String::compareTo);
        for (int i = 0; i < list.size(); i++) {
            String s=list.get(i);
            String node="";
            int step=0;
            if(s.substring(0,1).compareTo("k")<0){
                node="1";
                step=1000;
            }else if(s.substring(0,1).compareTo("r")<0){
                node="2";
                step=2000;
            }else {
                node="3";
                step=3000;
            }
            System.out.println(String.format("INSERT INTO `key_mapping` VALUES ('%s', NOW(), NOW(), 'monitor%s', '.*', '^(?i)%s.*$');",
                    step+i,node,s));
        }

        System.out.println(String.format("INSERT INTO `key_mapping` VALUES ('%s', NOW(), NOW(), 'monitor%s', '.*', '^(?i)%s.*$');",
                10000,3,""));


    }



    public static void prod(String[] args) {
        String d="cache_evictions\n" +
                "cache_gets\n" +
                "cache_local_disk_size\n" +
                "cache_local_heap_size\n" +
                "cache_local_offheap_size\n" +
                "cache_misses\n" +
                "cache_puts\n" +
                "cache_puts_added\n" +
                "cache_remoteSize\n" +
                "cache_removals\n" +
                "cache_size\n" +
                "cache_xa_commits\n" +
                "cache_xa_recoveries\n" +
                "cache_xa_rollbacks\n" +
                "code_execute_count\n" +
                "code_execute_timer\n" +
                "code_execute_timer_percentile\n" +
                "dataSource_connections_active\n" +
                "dataSource_connections_max\n" +
                "dataSource_connections_min\n" +
                "disk.free\n" +
                "disk.total\n" +
                "disk_free\n" +
                "disk_total\n" +
                "exchange_order_GoldenKey\n" +
                "health\n" +
                "health.discoverycomposite\n" +
                "health.diskspace\n" +
                "health.hystrix\n" +
                "health.refreshscope\n" +
                "health_composite\n" +
                "health_configserver\n" +
                "health_datasource\n" +
                "health_discoverycomposite\n" +
                "health_diskspace\n" +
                "health_elasticsearch\n" +
                "health_hystrix\n" +
                "health_mail\n" +
                "health_mongo\n" +
                "health_rabbit\n" +
                "health_redis\n" +
                "health_refreshscope\n" +
                "hikaricp_connections\n" +
                "hikaricp_connections_acquire\n" +
                "hikaricp_connections_acquire_percentile\n" +
                "hikaricp_connections_active\n" +
                "hikaricp_connections_creation\n" +
                "hikaricp_connections_creation_percentile\n" +
                "hikaricp_connections_idle\n" +
                "hikaricp_connections_max\n" +
                "hikaricp_connections_min\n" +
                "hikaricp_connections_pending\n" +
                "hikaricp_connections_timeout\n" +
                "hikaricp_connections_usage\n" +
                "hikaricp_connections_usage_percentile\n" +
                "http_server_requests\n" +
                "http_server_requests_percentile\n" +
                "hystrix_circuit_breaker_open\n" +
                "hystrix_command_other\n" +
                "hystrix_concurrent_execution_current\n" +
                "hystrix_concurrent_execution_rolling_max\n" +
                "hystrix_errors\n" +
                "hystrix_execution\n" +
                "hystrix_execution_terminal\n" +
                "hystrix_fallback\n" +
                "hystrix_latency_execution\n" +
                "hystrix_latency_execution_percentile\n" +
                "hystrix_latency_total\n" +
                "hystrix_latency_total_percentile\n" +
                "hystrix_requests\n" +
                "hystrix_threadpool_concurrent_execution_current\n" +
                "hystrix_threadpool_concurrent_execution_rolling_max\n" +
                "issue_GoldenKey\n" +
                "jdbc_connections_active\n" +
                "jdbc_connections_max\n" +
                "jdbc_connections_min\n" +
                "jvm.buffer.count\n" +
                "jvm.buffer.memory.used\n" +
                "jvm.buffer.total.capacity\n" +
                "jvm.classes.loaded\n" +
                "jvm.classes.unloaded\n" +
                "jvm.gc.live.data.size\n" +
                "jvm.gc.max.data.size\n" +
                "jvm.gc.memory.allocated\n" +
                "jvm.gc.memory.promoted\n" +
                "jvm.gc.pause\n" +
                "jvm.gc.pause.percentile\n" +
                "jvm.memory.committed\n" +
                "jvm.memory.max\n" +
                "jvm.memory.used\n" +
                "jvm.threads.daemon\n" +
                "jvm.threads.live\n" +
                "jvm.threads.peak\n" +
                "jvm_buffer_count\n" +
                "jvm_buffer_memory_used\n" +
                "jvm_buffer_total_capacity\n" +
                "jvm_classes_loaded\n" +
                "jvm_classes_unloaded\n" +
                "jvm_gc_concurrent_phase_time\n" +
                "jvm_gc_concurrent_phase_time_percentile\n" +
                "jvm_gc_live_data_size\n" +
                "jvm_gc_max_data_size\n" +
                "jvm_gc_memory_allocated\n" +
                "jvm_gc_memory_promoted\n" +
                "jvm_gc_pause\n" +
                "jvm_gc_pause_percentile\n" +
                "jvm_memory_committed\n" +
                "jvm_memory_max\n" +
                "jvm_memory_used\n" +
                "jvm_threads_daemon\n" +
                "jvm_threads_live\n" +
                "jvm_threads_peak\n" +
                "jvm_threads_states\n" +
                "kafka_consumer_assigned_partitions\n" +
                "kafka_consumer_bytes_consumed_total\n" +
                "kafka_consumer_commit_latency_avg\n" +
                "kafka_consumer_commit_rate\n" +
                "kafka_consumer_connection_count\n" +
                "kafka_consumer_fetch_latency_avg\n" +
                "kafka_consumer_fetch_latency_max\n" +
                "kafka_consumer_fetch_size_avg\n" +
                "kafka_consumer_fetch_size_max\n" +
                "kafka_consumer_fetch_throttle_time_avg\n" +
                "kafka_consumer_fetch_throttle_time_max\n" +
                "kafka_consumer_fetch_total\n" +
                "kafka_consumer_heartbeat_rate\n" +
                "kafka_consumer_io_ratio\n" +
                "kafka_consumer_io_wait_ratio\n" +
                "kafka_consumer_io-time-avg\n" +
                "kafka_consumer_io-wait-time-avg\n" +
                "kafka_consumer_join_rate\n" +
                "kafka_consumer_join_time_avg\n" +
                "kafka_consumer_join_time_max\n" +
                "kafka_consumer_last-heartbeat\n" +
                "kafka_consumer_records_consumed_total\n" +
                "kafka_consumer_records_per_request_avg\n" +
                "kafka_consumer_sync_rate\n" +
                "kafka_consumer_sync_time_avg\n" +
                "kafka_consumer_sync_time_max\n" +
                "kafka_producer_connection_count\n" +
                "kafka_producer_io_ratio\n" +
                "kafka_producer_io-time-avg\n" +
                "kafka_producer_io-wait-time-avg\n" +
                "kafka_producer_select_total\n" +
                "log4j2_events\n" +
                "logback.events\n" +
                "logback_events\n" +
                "method_timed\n" +
                "method_timed_percentile\n" +
                "method_with_url_count\n" +
                "method_with_url_timer\n" +
                "method_with_url_timer_percentile\n" +
                "micormeter.meters.size\n" +
                "micormeter_meters_size\n" +
                "mymeas\n" +
                "openfeign\n" +
                "openfeign_percentile\n" +
                "order_goldenKey_success\n" +
                "primary-_connections_active\n" +
                "primary-_connections_max\n" +
                "primary-_connections_min\n" +
                "process.cpu.usage\n" +
                "process.start.time\n" +
                "process.uptime\n" +
                "process_cpu_usage\n" +
                "process_files_max\n" +
                "process_files_open\n" +
                "process_start_time\n" +
                "process_uptime\n" +
                "rabbit_acknowledged\n" +
                "rabbit_cacheConnectionProperties_channelCacheSize\n" +
                "rabbit_cacheConnectionProperties_idleChannelsNotTx\n" +
                "rabbit_cacheConnectionProperties_idleChannelsNotTxHighWater\n" +
                "rabbit_cacheConnectionProperties_idleChannelsTx\n" +
                "rabbit_cacheConnectionProperties_idleChannelsTxHighWater\n" +
                "rabbit_channels\n" +
                "rabbit_connections\n" +
                "rabbit_consumed\n" +
                "rabbit_published\n" +
                "rabbit_rejected\n" +
                "rabbitmq_acknowledged\n" +
                "rabbitmq_acknowledged_published\n" +
                "rabbitmq_channels\n" +
                "rabbitmq_connections\n" +
                "rabbitmq_consumed\n" +
                "rabbitmq_failed_to_publish\n" +
                "rabbitmq_not_acknowledged_published\n" +
                "rabbitmq_published\n" +
                "rabbitmq_rejected\n" +
                "rabbitmq_unrouted_published\n" +
                "redis_pool_active\n" +
                "redis_pool_borrowed\n" +
                "redis_pool_created\n" +
                "redis_pool_destroyed\n" +
                "redis_pool_idle\n" +
                "redis_pool_total\n" +
                "redis_pool_waiters\n" +
                "rest_template_timer\n" +
                "rest_template_timer_percentile\n" +
                "spring_integration_channels\n" +
                "spring_integration_handlers\n" +
                "spring_integration_send\n" +
                "spring_integration_send_percentile\n" +
                "spring_integration_sources\n" +
                "sql_execute_time\n" +
                "sql_execute_time_percentile\n" +
                "system.cpu.count\n" +
                "system.cpu.usage\n" +
                "system_cpu_count\n" +
                "system_cpu_usage\n" +
                "system_load_average_1m\n" +
                "tesla_inprogress_requests\n" +
                "tesla_request_size\n" +
                "tesla_request_size_percentile\n" +
                "tesla_requests_forward_latency\n" +
                "tesla_requests_forward_latency_histogram\n" +
                "tesla_requests_forward_latency_percentile\n" +
                "tesla_requests_latency\n" +
                "tesla_requests_latency_histogram\n" +
                "tesla_requests_latency_percentile\n" +
                "tesla_requests_total\n" +
                "tesla_response_size\n" +
                "tesla_response_size_percentile\n" +
                "thirdParty-facade-invoke\n" +
                "thirdParty-facade-invoke_percentile\n" +
                "ThreadPoolTaskExecutor_active\n" +
                "ThreadPoolTaskExecutor_completed\n" +
                "ThreadPoolTaskExecutor_pool\n" +
                "ThreadPoolTaskExecutor_queued\n" +
                "ThreadPoolTaskScheduler_active\n" +
                "ThreadPoolTaskScheduler_completed\n" +
                "ThreadPoolTaskScheduler_pool\n" +
                "ThreadPoolTaskScheduler_queued\n" +
                "tomcat.cache.access\n" +
                "tomcat.cache.hit\n" +
                "tomcat.global.error\n" +
                "tomcat.global.received\n" +
                "tomcat.global.request\n" +
                "tomcat.global.request.max\n" +
                "tomcat.global.sent\n" +
                "tomcat.servlet.error\n" +
                "tomcat.servlet.request\n" +
                "tomcat.servlet.request.max\n" +
                "tomcat.sessions.active.current\n" +
                "tomcat.sessions.active.max\n" +
                "tomcat.sessions.alive.max\n" +
                "tomcat.sessions.created\n" +
                "tomcat.sessions.expired\n" +
                "tomcat.sessions.rejected\n" +
                "tomcat.threads.busy\n" +
                "tomcat.threads.config.max\n" +
                "tomcat.threads.current\n" +
                "tomcat_cache_access\n" +
                "tomcat_cache_hit\n" +
                "tomcat_global_error\n" +
                "tomcat_global_received\n" +
                "tomcat_global_request\n" +
                "tomcat_global_request_max\n" +
                "tomcat_global_sent\n" +
                "tomcat_servlet_error\n" +
                "tomcat_servlet_request\n" +
                "tomcat_servlet_request_max\n" +
                "tomcat_sessions_active_current\n" +
                "tomcat_sessions_active_max\n" +
                "tomcat_sessions_alive_max\n" +
                "tomcat_sessions_created\n" +
                "tomcat_sessions_expired\n" +
                "tomcat_sessions_rejected\n" +
                "tomcat_threads_busy\n" +
                "tomcat_threads_config_max\n" +
                "tomcat_threads_current\n" +
                "trace_event_count\n" +
                "transaction\n" +
                "transaction_percentile\n" +
                "uus_authservice_get_oauth_authorize\n" +
                "uus_authservice_request_uus\n" +
                "uus_authservice_token\n" +
                "uus_uus_create_account\n" +
                "uus_uus_login\n" +
                "uus_uus_register\n" +
                "uus_uus_send_message_code\n" +
                "uus_uus_silent_login";
        Set<String> set = Arrays.asList(d.split("\n")).stream()
                .filter(s -> !StringUtils.isEmpty(s))
                .map(s -> s.split("\\.")[0])
                .map(s -> s.split("_")[0])
                .map(String::toLowerCase)
                .collect(Collectors.toSet());
        ArrayList<String> list=new ArrayList<>();
        list.addAll(set);
        list.sort(String::compareTo);
        for (int i = 0; i < list.size(); i++) {
            String s=list.get(i);
            String node="";
            /**
             * e
             * h
             * j
             * l
             * s
             */
            int step=0;
            if(s.substring(0,1).compareTo("e")<0){
                node="1";
                step=1000;
            }else if(s.substring(0,1).compareTo("h")<0){
                node="2";
                step=2000;
            }else if(s.substring(0,1).compareTo("j")<0){
                node="3";
                step=3000;
            }else if(s.substring(0,1).compareTo("l")<0){
                node="4";
                step=4000;
            }else if(s.substring(0,1).compareTo("s")<0){
                node="5";
                step=5000;
            }else {
                node="6";
                step=6000;
            }
            System.out.println(String.format("INSERT INTO `key_mapping` VALUES ('%s', NOW(), NOW(), 'monitor%s', '.*', '^(?i)%s.*$');",
                    step+i,node,s));
        }

        System.out.println(String.format("INSERT INTO `key_mapping` VALUES ('%s', NOW(), NOW(), 'monitor%s', '.*', '^(?i)%s.*$');",
                10000,6,""));


    }
}
