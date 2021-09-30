package com.markets.demo.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import springfox.documentation.swagger2.mappers.ModelMapper;
import springfox.documentation.swagger2.mappers.ModelMapperImpl;

@Configuration
public class BeansConfiguration {

    @Bean
    public ModelMapper modelMapper() {
        return new ModelMapperImpl();
    }
}
