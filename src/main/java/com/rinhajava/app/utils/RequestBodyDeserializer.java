package com.rinhajava.app.utils;

import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;
import com.fasterxml.jackson.databind.JsonNode;
import com.rinhajava.app.domain.Pessoa;

import java.io.IOException;

public class RequestBodyDeserializer extends JsonDeserializer<String> {

    @Override
    public String deserialize(JsonParser p, DeserializationContext context) throws IOException {
        JsonNode node = p.getCodec().readTree(p);
        if (!node.isTextual()) {
            throw new JsonParseException(context.getParser(), "Field 'nome' precisa ser String");
        }

        return node.textValue();
    }
}
