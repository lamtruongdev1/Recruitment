package com.poly.Recruitment;

import java.util.List;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.client.SimpleClientHttpRequestFactory;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.web.client.RestTemplate;

import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;

@Configuration
public class AppConfig {

    @Bean
    public RestTemplate restTemplate() {
        SimpleClientHttpRequestFactory factory = new SimpleClientHttpRequestFactory();
        factory.setConnectTimeout(3000); // Connection timeout (ms)
        factory.setReadTimeout(3000); // Read timeout (ms)

        RestTemplate restTemplate = new RestTemplate(factory);

        // Add Jackson converter for JSON
        List<HttpMessageConverter<?>> converters = restTemplate.getMessageConverters();
        converters.add(new MappingJackson2HttpMessageConverter());

        return restTemplate;
    }

}
