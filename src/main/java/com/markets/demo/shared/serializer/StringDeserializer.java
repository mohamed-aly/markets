package com.markets.demo.shared.serializer;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;
import org.springframework.boot.jackson.JsonComponent;
import org.springframework.web.util.HtmlUtils;

import java.io.IOException;

@JsonComponent
public class StringDeserializer extends JsonDeserializer<String> {


    @Override
    public String deserialize(JsonParser p, DeserializationContext ctxt) throws IOException {
        return HtmlUtils.htmlEscape(p.getText());
    }
}
