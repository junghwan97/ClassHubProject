package com.example.classhubproject.data.payment;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;
import com.fasterxml.jackson.databind.JsonNode;
import com.siot.IamportRestClient.response.Prepare;

import java.io.IOException;

// 역직렬화
public class PrepareDeserializer extends JsonDeserializer<Prepare> {

    @Override
    public Prepare deserialize(JsonParser p, DeserializationContext ctxt) throws IOException, JsonProcessingException {
        JsonNode node = p.getCodec().readTree(p);
        Prepare prepare = new Prepare();
        return prepare;
    }

}
