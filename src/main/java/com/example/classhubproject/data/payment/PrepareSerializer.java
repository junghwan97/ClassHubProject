package com.example.classhubproject.data.payment;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.SerializerProvider;

import java.io.IOException;

// 직렬화
public class PrepareSerializer<Prepare> extends JsonSerializer<Prepare> {

    @Override
    public void serialize(Prepare prepare, JsonGenerator gen, SerializerProvider serializers) throws IOException {
        gen.writeStartObject();
        gen.writeEndObject();
    }

}
