package org.example.kafka;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.JsonDeserializer;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.module.SimpleModule;
import com.fasterxml.jackson.dataformat.avro.AvroMapper;
import com.fasterxml.jackson.dataformat.avro.AvroSchema;
import org.apache.avro.LogicalTypes;
import org.example.Mgd.*;
import org.apache.avro.SchemaBuilder;

import org.apache.avro.Schema;
import org.apache.avro.generic.GenericData;
//import org.apache.kafka.common.serialization.Serdes;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class AvroFileCreator {
    public static void main(String[] args) throws Exception {
        generateFiles();
    }

    private static void generateFiles() throws IOException {

        AvroMapper avroMapper = new AvroMapper();
        avroMapper.registerModule(createCustomModule());
        HashMap<AvroSchema, String> avroSchemas = new HashMap<>();
        /*AvroMapper avroMapper = new AvroMapper();
        avroMapper.registerModule(createCustomModule());*/
        avroMapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);

        //avroSchemas.put(avroMapper.schemaFor(AbstractEntityMgd.class), AbstractEntityMgd.class.getSimpleName());
        avroSchemas.put(avroMapper.schemaFor(GuestMgd.class), GuestMgd.class.getSimpleName());
        avroSchemas.put(avroMapper.schemaFor(MicroSuiteMgd.class), MicroSuiteMgd.class.getSimpleName());
        avroSchemas.put(avroMapper.schemaFor(StudioMgd.class), StudioMgd.class.getSimpleName());
        avroSchemas.put(avroMapper.schemaFor(RentMgd.class), RentMgd.class.getSimpleName());

        for (Map.Entry<AvroSchema, String> schema : avroSchemas.entrySet()){

            try (BufferedWriter writer = new BufferedWriter(
                    new FileWriter(String.format("src/main/avro/%s-self.avsc", schema.getValue())))){

                String msg = schema.getKey().getAvroSchema().toString();
                writer.write(msg);
            }
        }
    }

    /*private static com.fasterxml.jackson.databind.Module createCustomModule() {
        SimpleModule module = new SimpleModule();

        module.addSerializer(LocalDateTime.class, new JsonSerializer<>() {
            @Override
            public void serialize(LocalDateTime value, JsonGenerator gen, com.fasterxml.jackson.databind.SerializerProvider serializers) throws IOException {
                gen.writeNumber(value.toInstant(ZoneOffset.UTC).toEpochMilli());
            }
        });

        module.addDeserializer(LocalDateTime.class, new JsonDeserializer<>() {
            @Override
            public LocalDateTime deserialize(JsonParser p, DeserializationContext ctxt) throws IOException {
                long epochMillis = p.getLongValue();
                return LocalDateTime.ofEpochSecond(epochMillis / 1000, (int) (epochMillis % 1000) * 1_000_000, ZoneOffset.UTC);
            }
        });

        return module;
    }*/

    private static com.fasterxml.jackson.databind.Module createCustomModule() {
        SimpleModule module = new SimpleModule();

        // Serializer: Convert LocalDateTime to milliseconds since epoch
        module.addSerializer(LocalDateTime.class, new JsonSerializer<>() {
            @Override
            public void serialize(LocalDateTime value, JsonGenerator gen, com.fasterxml.jackson.databind.SerializerProvider serializers) throws IOException {
                gen.writeNumber(value.toInstant(ZoneOffset.UTC).toEpochMilli());
            }
        });

        // Deserializer: Convert milliseconds since epoch to LocalDateTime
        module.addDeserializer(LocalDateTime.class, new JsonDeserializer<>() {
            @Override
            public LocalDateTime deserialize(JsonParser p, DeserializationContext ctxt) throws IOException {
                long epochMillis = p.getLongValue();
                return Instant.ofEpochMilli(epochMillis).atZone(ZoneOffset.UTC).toLocalDateTime();
            }
        });

        return module;
    }

    private static Schema getRentSchema(){

        Schema schema = SchemaBuilder.record("RentMgd")
                .namespace("model.avro")
                .fields()
                .name("entityId")
                .type(getUniqueIdMgdSchema())
                .noDefault()
                .requiredString("rentNumber")
                .name("startTime")
                .type(LogicalTypes.timestampMillis().addToSchema(Schema.create(Schema.Type.LONG)))
                .noDefault()
                .name("endTime")
                .type(Schema.createUnion(
                        Arrays.asList(
                                Schema.create(Schema.Type.NULL),
                                LogicalTypes.timestampMillis().addToSchema(Schema.create(Schema.Type.LONG))
                        )
                ))
                .withDefault(null)
                //.type(LogicalTypes.timestampMillis().addToSchema(Schema.create(Schema.Type.LONG)))
                //.noDefault()
                .name("guest")
                .type(getGuestMgdSchema())
                .noDefault()
                .name("room")
                .type(getRoomMgdSchema())
                .noDefault()
                .endRecord();
        return schema;
    }

    private static Schema getGuestMgdSchema(){

        Schema schema = SchemaBuilder.record("GuestMgd")
                .namespace("model.avro")
                .fields()
                .name("entityId")
                .type(getUniqueIdMgdSchema())
                .noDefault()
                .requiredString("name")
                .requiredString("lastname")
                .requiredString("phonenumber")
                .endRecord();
        return schema;
    }

    private static Schema getRoomMgdSchema(){

        Schema schema = SchemaBuilder.record("RoomMgd")
                .namespace("model.avro")
                .fields()
                .name("entityId")
                .type(getUniqueIdMgdSchema())
                .noDefault()
                .requiredInt("number")
                .requiredInt("floor")
                .requiredDouble("surface")
                .requiredDouble("price")
                .requiredInt("rented")
                .optionalBoolean("balcony")
                .endRecord();
        return schema;
    }

    private static Schema getUniqueIdMgdSchema(){

        Schema schema = SchemaBuilder.record("UniqueIdMgd")
                .namespace("model.avro")
                .fields()
                .name("uuid")
                .type(Schema.createFixed("UUID",
                        "UUID",
                        "fixed",
                        500))
                .noDefault()
                .endRecord();
        return schema;
    }
}
