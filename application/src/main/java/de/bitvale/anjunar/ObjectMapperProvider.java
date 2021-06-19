package de.bitvale.anjunar;

import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import de.bitvale.anjunar.jackson.UTCLocalDateTimeDeserializer;
import de.bitvale.anjunar.jackson.UTCLocalDateTimeSerializer;

import javax.ws.rs.Consumes;
import javax.ws.rs.Produces;
import javax.ws.rs.ext.ContextResolver;
import javax.ws.rs.ext.Provider;
import java.time.LocalDateTime;

/**
 * @author Patrick Bittner on 25.05.2015.
 */
@Provider
@Consumes("application/json")
@Produces("application/json")
public class ObjectMapperProvider implements ContextResolver<ObjectMapper> {

    @Override
    public ObjectMapper getContext(Class<?> type) {
        ObjectMapper objectMapper = new ObjectMapper()
                .configure(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS, false)
                .configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);

        JavaTimeModule module = new JavaTimeModule();

        module.addSerializer(LocalDateTime.class, new UTCLocalDateTimeSerializer());
        module.addDeserializer(LocalDateTime.class, new UTCLocalDateTimeDeserializer());

        objectMapper.registerModule(module);

        return objectMapper;
    }
}


