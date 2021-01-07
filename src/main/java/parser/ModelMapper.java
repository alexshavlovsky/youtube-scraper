package parser;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.util.HashMap;
import java.util.Map;

class ModelMapper {

    private static ObjectMapper objectMapper = new ObjectMapper();

    @SuppressWarnings("unchecked")
    static Map<String, Object> parseJsSoup(String json) {
        try {
            return new ObjectMapper().readValue(json, HashMap.class);
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }
    }
}
