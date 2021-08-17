package ru.iruchidesu.restaurantvotingsystem.util.json;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.ObjectCodec;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;
import com.fasterxml.jackson.databind.JsonNode;
import ru.iruchidesu.restaurantvotingsystem.util.UserUtil;

import java.io.IOException;

public class JsonDeserializers {

    private JsonDeserializers() {
    }

    // https://stackoverflow.com/a/60995048/548473
    public static class PasswordDeserializer extends JsonDeserializer<String> {
        public String deserialize(JsonParser jsonParser, DeserializationContext deserializationContext) throws IOException {
            ObjectCodec oc = jsonParser.getCodec();
            JsonNode node = oc.readTree(jsonParser);
            String rawPassword = node.asText();
            return UserUtil.PASSWORD_ENCODER.encode(rawPassword);
        }
    }
}
