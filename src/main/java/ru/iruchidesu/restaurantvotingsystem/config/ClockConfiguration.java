package ru.iruchidesu.restaurantvotingsystem.config;

import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.time.Clock;

@Configuration
public class ClockConfiguration {
    @Bean
    @ConditionalOnMissingBean
    Clock getSystemDefaultZoneClock() {
        return Clock.systemDefaultZone();
    }
}
