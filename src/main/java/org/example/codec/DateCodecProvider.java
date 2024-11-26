package org.example.codec;

import org.bson.codecs.Codec;
import org.bson.codecs.configuration.CodecProvider;
import org.bson.codecs.configuration.CodecRegistry;
import org.bson.codecs.jsr310.LocalDateTimeCodec;

import java.time.LocalDateTime;

public class DateCodecProvider implements CodecProvider {
    @Override
    public <T> Codec<T> get(Class<T> clazz, CodecRegistry registry) {
        if (clazz.equals(LocalDateTime.class)) {
            return (Codec<T>) new LocalDateTimeCodec();
        }
        return null;
    }
}
