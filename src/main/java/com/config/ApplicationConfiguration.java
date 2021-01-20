package com.config;

import com.github.xiaoymin.swaggerbootstrapui.annotations.EnableSwaggerBootstrapUI;
import org.hibernate.validator.constraints.URL;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import springfox.documentation.builders.ApiInfoBuilder;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.service.Contact;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spring.web.plugins.Docket;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

/**
 * Create on 2020/10/21 9:30
 *
 * @author Yang Shuolin
 */
@Configuration
@EnableSwagger2
@EnableSwaggerBootstrapUI
public class ApplicationConfiguration {
    @Bean
    public Docket customDocket() {
//        Contact contact = new Contact("Yang Shuolin", "", "yangshuolin@wisdomopen.com");
        Contact contact = new Contact("shan", "", "shanpengkun@wisdomopen.com");
        return new Docket(DocumentationType.SWAGGER_2)
                .apiInfo(
                        new ApiInfoBuilder()
                                .title("供应商系统API文档")
                                .description("供应商管理系统API文档")
                                .version("v1")
                                .contact(contact)
                                .build()
                )
                .select()
                .apis(RequestHandlerSelectors
                        .basePackage("com"))
                .build();
    }
}
