package com.sky.config;

import com.sky.interceptor.JwtTokenAdminInterceptor;
import com.sky.json.JacksonObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurationSupport;
import springfox.documentation.builders.ApiInfoBuilder;
import springfox.documentation.builders.PathSelectors;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.service.ApiInfo;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spring.web.plugins.Docket;

import java.util.List;


/**
 * 配置类，注册 web 层相关组件
 */
@Configuration
@Slf4j
public class WebMvcConfiguration extends WebMvcConfigurationSupport {

    @Autowired
    private JwtTokenAdminInterceptor jwtTokenAdminInterceptor;

    /**
     * 注册自定义拦截器
     *
     * @param registry
     */
    protected void addInterceptors(InterceptorRegistry registry) {
        log.info("[Config] 开始注册自定义拦截器...");
        registry.addInterceptor(jwtTokenAdminInterceptor)
                .addPathPatterns("/admin/**")
                .excludePathPatterns("/admin/employee/login");
    }

    /**
     * 通过 knife4j 生成接口文档
     * @return
     */
    @Bean
    public Docket docket() {
        log.info("[Config] 进行生成接口文档...");
        ApiInfo apiInfo = new ApiInfoBuilder()
                .title("苍穹外卖接口文档")
                .version("2.0")
                .description("苍穹外卖接口文档")
                .build();
        Docket docket = new Docket(DocumentationType.SWAGGER_2)
                .apiInfo(apiInfo)
                .select()
                // 指定要扫描生成接口的包
                .apis(RequestHandlerSelectors.basePackage("com.sky.controller"))
                .paths(PathSelectors.any())
                .build();
        return docket;
    }

    /**
     * 设置静态资源映射
     * @param registry
     */
    protected void addResourceHandlers(ResourceHandlerRegistry registry) {
        log.info("[Config] 设置静态资源映射...");

        String pathPatterns = "/doc.html";
        registry.addResourceHandler(pathPatterns).addResourceLocations("classpath:/META-INF/resources/");
        registry.addResourceHandler("/webjars/**").addResourceLocations("classpath:/META-INF/resources/webjars/");

        log.info("[Config] 设置完成，接口文档访问 URL - http://localhost:8080" + pathPatterns);
    }

    /**
     * 扩展 SpringMVC 的消息转换器，添加一个 FastJson 转换器
     * 预制 JacksonObjectMapper 继承自 ObjectMapper
     * @param converters 既有的转换器列表
     */
    protected void extendMessageConverters(List<HttpMessageConverter<?>> converters) {
        log.info("[Config] 扩展消息转换器...");

        // 创建一个消息转换器对象
        MappingJackson2HttpMessageConverter messageConverter = new MappingJackson2HttpMessageConverter();

        // 设置自定义对象转换器，底层使用 Jackson 将 Java 对象转为 json（即序列化）
        messageConverter.setObjectMapper(new JacksonObjectMapper());

        // 将自定义消息转换器，追加到 SpringMVC 框架的转换器集合中
        converters.add(0, messageConverter);    // 放在索引为 0 的位置，优先使用
    }
}
