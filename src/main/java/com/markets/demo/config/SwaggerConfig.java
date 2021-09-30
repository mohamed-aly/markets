package com.markets.demo.config;

import org.springframework.boot.info.BuildProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import springfox.documentation.builders.ApiInfoBuilder;
import springfox.documentation.builders.PathSelectors;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.service.ApiInfo;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spring.web.plugins.Docket;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

@Configuration
@EnableSwagger2
public class SwaggerConfig {

    @Bean
    public Docket api(BuildProperties buildProperties) {
        return new Docket(DocumentationType.SWAGGER_2)
                .select()
                .apis(RequestHandlerSelectors.basePackage("com.markets.demo.business"))
                .paths(PathSelectors.any())
                .build()
                .apiInfo(apiInfo(buildProperties));
    }

    private ApiInfo apiInfo(BuildProperties buildProperties) {
        return new ApiInfoBuilder()
                .title(buildProperties.getName())
                .description("REST API documentation for Markets endpoints.")
                .version(buildProperties.getVersion())
                .build();
    }
}
