package com.annkee.base.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurerAdapter;
import springfox.documentation.builders.ApiInfoBuilder;
import springfox.documentation.builders.PathSelectors;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.service.ApiInfo;
import springfox.documentation.service.Contact;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spring.web.plugins.Docket;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

/**
 * swagger配置
 *
 * @author wangan
 * @date 2018/01/05
 */
@Configuration
@EnableSwagger2
public class SwaggerConfig extends WebMvcConfigurerAdapter {
    
    @Bean
    public Docket restApi() {
        return new Docket(DocumentationType.SWAGGER_2)
                .apiInfo(apiInfo())
                .select()
                .apis(RequestHandlerSelectors.basePackage("com.annkee.applications.controller"))
                .paths(PathSelectors.any()).build();
    }
    
    public ApiInfo apiInfo() {
        return new ApiInfoBuilder()
                .contact(new Contact("wangan", "", ""))
                .title("small-applications")
                .version("1.0")
                .description("<strong>small-applications开放API接口说明</strong><br/><br/>" +
                        "<strong>接口返回成功码：</strong><br/>" +
                        "200 成功<br/><br/>" +
                        "200003 该app已不可用<br/>" +
                        "200004 该app在此设备已不可用<br/>" +
                        "200005 应用已存在<br/>" +
                        "200006 应用不存在,无法创建版本<br/><br/>" +
                        "400 客户端请求有误<br/>" +
                        "401 授权权限不足<br/>" +
                        "401003 无效的应用授权令牌<br/>" +
                        "404 路径有误<br/>" +
                        "415 请求头有误<br/><br/>"
                )
                .build();
    }
}
