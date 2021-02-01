package com.config;

import com.github.xiaoymin.swaggerbootstrapui.annotations.EnableSwaggerBootstrapUI;
import org.springframework.boot.web.servlet.MultipartConfigFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.util.unit.DataSize;
import springfox.documentation.builders.ApiInfoBuilder;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.service.Contact;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spring.web.plugins.Docket;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

import javax.servlet.MultipartConfigElement;

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

    @Bean
    public MultipartConfigElement multipartConfigElement() {
        MultipartConfigFactory factory = new MultipartConfigFactory();
        // 限制上传文件大小
        factory.setMaxFileSize(DataSize.ofGigabytes(30));
        return factory.createMultipartConfig();
    }
}
