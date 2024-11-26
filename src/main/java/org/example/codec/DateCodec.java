package org.example.codec;

import org.bson.BsonReader;
import org.bson.BsonWriter;
import org.bson.codecs.Codec;
import org.bson.codecs.DecoderContext;
import org.bson.codecs.EncoderContext;

import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.util.Date;

public class DateCodec implements Codec<LocalDateTime> {

    @Override
    public void encode(BsonWriter writer, LocalDateTime value, EncoderContext encoderContext) {
        writer.writeDateTime(value.toInstant(ZoneOffset.UTC).toEpochMilli());
    }

    @Override
    public LocalDateTime decode(BsonReader reader, DecoderContext decoderContext) {
        return LocalDateTime.ofEpochSecond(reader.readDateTime() / 1000, 0, ZoneOffset.UTC);
    }

    @Override
    public Class<LocalDateTime> getEncoderClass() {
        return LocalDateTime.class;
    }
}
