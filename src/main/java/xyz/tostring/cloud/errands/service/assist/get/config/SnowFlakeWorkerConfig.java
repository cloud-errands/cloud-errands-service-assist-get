package xyz.tostring.cloud.errands.service.assist.get.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import xyz.tostring.cloud.errands.common.service.util.SnowflakeIdWorker;

@Configuration
public class SnowFlakeWorkerConfig {

    @Bean
    public SnowflakeIdWorker snowflakeIdWorker() {
        return new SnowflakeIdWorker(0, 0);
    }
}
