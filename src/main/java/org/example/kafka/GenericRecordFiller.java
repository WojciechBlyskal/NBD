package org.example.kafka;

import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.dataformat.avro.AvroMapper;
import com.fasterxml.jackson.dataformat.avro.AvroSchema;
import org.example.Mgd.GuestMgd;
import org.example.Mgd.RentMgd;
import org.apache.avro.generic.GenericData;
import org.apache.avro.generic.GenericRecord;

public class GenericRecordFiller {

    public GenericRecordFiller() {
    }

    public GenericRecord fillGuest(RentMgd rentMgd) throws JsonMappingException {

        AvroMapper avroMapper = new AvroMapper();
        AvroSchema avroSchema = avroMapper.schemaFor(GuestMgd.class);

        GenericRecord genericRecord = new GenericData.Record(avroSchema.getAvroSchema());

        genericRecord.put("entityId",
                rentMgd.getEntityId());
        genericRecord.put("name",
                rentMgd.getEntityId());
        genericRecord.put("lastname",
//                rentMgd.getSeatNumber());
//        genericRecord.put("phonenumber",
//                rentMgd.getTripLengthByStations());
//        genericRecord.put("discount",
//                rentMgd.getGuest());
//        genericRecord.put("cardNumber",
//                rentMgd.getRoom());
//        genericRecord.put("disabilityType",
                rentMgd.getRoom());

        return genericRecord;
    }

    public GenericRecord fillRent(RentMgd rentMgd) throws JsonMappingException {

        AvroMapper avroMapper = new AvroMapper();
        AvroSchema avroSchema = avroMapper.schemaFor(RentMgd.class);
        String topic = KafkaConf.Topic.RENTS.toString().toLowerCase();

        GenericRecord genericRecord = new GenericData.Record(avroSchema.getAvroSchema());

        genericRecord.put("entityId",
                rentMgd.getEntityId());
//        genericRecord.put("seatNumber",
//                rentMgd.getSeatNumber());
//        genericRecord.put("tripLengthByStations",
//                rentMgd.getTripLengthByStations());
        genericRecord.put("guest",
                rentMgd.getGuest());
        genericRecord.put("room",
                rentMgd.getRoom());

        return genericRecord;
    }
}
