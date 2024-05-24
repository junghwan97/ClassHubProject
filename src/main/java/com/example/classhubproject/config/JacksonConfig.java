package com.example.classhubproject.config;

import com.example.classhubproject.data.payment.PrepareDeserializer;
import com.example.classhubproject.data.payment.PrepareSerializer;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.module.SimpleModule;
import com.siot.IamportRestClient.response.Prepare;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.converter.json.Jackson2ObjectMapperBuilder;

@Configuration
public class JacksonConfig {
    @Bean
    public ObjectMapper objectMapper(Jackson2ObjectMapperBuilder builder) {
        ObjectMapper objectMapper = builder.createXmlMapper(false).build();
        SimpleModule module = new SimpleModule();
        module.addSerializer(Prepare.class, new PrepareSerializer());
        module.addDeserializer(Prepare.class, new PrepareDeserializer());
        objectMapper.registerModule(module);
        return objectMapper;
    }
}
