package com.example.config;

import com.example.common.JacksonObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurationSupport;

import java.util.List;

@Slf4j
@Configuration
public class WebMVCConfig extends WebMvcConfigurationSupport {
    //設置靜態資源映射(如果靜態資源放在resources資料夾下時需要此操作)
    @Override
    protected void addResourceHandlers(ResourceHandlerRegistry registry) {
        log.info("開始進行靜態資源映射...");
        registry.addResourceHandler("/backend/**").addResourceLocations("classpath:/backend/");
        registry.addResourceHandler("/front/**").addResourceLocations("classpath:/front/");
    }

    //擴展MVC框架的消息轉換器
    @Override
    protected void extendMessageConverters(List<HttpMessageConverter<?>> converters) {
        log.info("擴展消息轉換器...");
        //創建消息轉換器對象
        MappingJackson2HttpMessageConverter messageConverter=new MappingJackson2HttpMessageConverter();
        //設置對象轉換器，底層使用Jackson將Java對象轉為JSON
        messageConverter.setObjectMapper(new JacksonObjectMapper());
        //將上面的消息轉換器對象追加到MVC框架的轉換器容器
        converters.add(0,messageConverter);
    }
}
