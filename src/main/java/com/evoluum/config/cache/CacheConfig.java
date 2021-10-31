package com.evoluum.config.cache;

import com.hazelcast.config.Config;
import com.hazelcast.config.MapConfig;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
@EnableCaching
public class CacheConfig {

    @Bean
    Config config() {
        final Config config = new Config();

        final MapConfig mapConfig = new MapConfig();
        mapConfig.setTimeToLiveSeconds(900); // 15 minutos
        config.getMapConfigs().put("cities", mapConfig);

        return config;
    }
}
