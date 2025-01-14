package org.example.kafka;

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
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class AvroFileCreator {

    public static void main(String[] args)
            throws Exception {

        generateFiles();
    }

    private static void generateFiles() throws IOException {

        AvroMapper avroMapper = new AvroMapper();

        HashMap<AvroSchema, String> avroSchemas = new HashMap<>();

        avroSchemas.put(avroMapper.schemaFor(AbstractEntityMgd.class), AbstractEntityMgd.class.getSimpleName());
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
                .type(LogicalTypes.timestampMillis().addToSchema(Schema.create(Schema.Type.LONG)))
                .noDefault()
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
                .endRecord();
        return schema;
    }

    private static Schema getUniqueIdMgdSchema(){

        Schema schema = SchemaBuilder.record("RoomMgd")
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
