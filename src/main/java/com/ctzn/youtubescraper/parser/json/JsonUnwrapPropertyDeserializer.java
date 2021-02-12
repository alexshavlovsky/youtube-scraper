package com.ctzn.youtubescraper.parser.json;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.TreeNode;
import com.fasterxml.jackson.databind.BeanProperty;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.JsonDeserializer;
import com.fasterxml.jackson.databind.deser.ContextualDeserializer;

import java.io.IOException;

@Deprecated
public class JsonUnwrapPropertyDeserializer extends JsonDeserializer<Object> implements ContextualDeserializer {

    private JavaType unwrappedJavaType;
    private String propertyName;

    @Override
    public JsonDeserializer<?> createContextual(final DeserializationContext deserializationContext, final BeanProperty beanProperty) {
        JsonUnwrapProperty skipWrapperObject = beanProperty.getAnnotation(JsonUnwrapProperty.class);
        propertyName = skipWrapperObject.value();
        unwrappedJavaType = beanProperty.getType();
        return this;
    }

    @Override
    public Object deserialize(final JsonParser jsonParser, final DeserializationContext deserializationContext) throws IOException {
        final TreeNode targetObjectNode = jsonParser.readValueAsTree().get(propertyName);
        return jsonParser.getCodec().readValue(targetObjectNode.traverse(), unwrappedJavaType);
    }
}
