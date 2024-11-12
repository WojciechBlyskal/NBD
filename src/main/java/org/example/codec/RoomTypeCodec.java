package org.example.codec;
import org.bson.BsonSerializationException;
import org.bson.BsonType;
import org.bson.types.ObjectId;
import org.example.Mgd.MicroSuiteMgd;
import org.example.Mgd.RoomMgd;
import org.bson.BsonReader;
import org.bson.BsonWriter;
import org.bson.codecs.Codec;
import org.bson.codecs.DecoderContext;
import org.bson.codecs.EncoderContext;
import org.bson.codecs.configuration.CodecRegistry;
import org.example.Mgd.StudioMgd;


public class RoomTypeCodec implements Codec<RoomMgd> {

    private final CodecRegistry registry;

    public RoomTypeCodec(CodecRegistry registry) {
        this.registry = registry;
    }

    /*@Override
    public void encode(BsonWriter writer, RoomMgd roomMgd, EncoderContext encoderContext) {
        writer.writeStartDocument();
        writer.writeString("_room", roomMgd.getClass().getSimpleName().toLowerCase());
        writer.writeInt32("number", roomMgd.getNumber());
        writer.writeInt32("floor", roomMgd.getFloor());
        writer.writeDouble("surface", roomMgd.getSurface());
        writer.writeDouble("price", roomMgd.getPrice());
        writer.writeInt32("rented", roomMgd.getRented());
        writer.writeEndDocument();
    }

    @Override
    public RoomMgd decode(BsonReader reader, DecoderContext decoderContext) {
        reader.readStartDocument();
        String type = reader.readString();
        int number = reader.readInt32();
        int floor = reader.readInt32();
        double surface = reader.readDouble();
        double price = reader.readDouble();
        int rented = reader.readInt32();
        reader.readEndDocument();

        if (type.equals("studio")) {
            return new StudioMgd();
        } else {
            return new MicroSuiteMgd();
        }
    }*/
    @Override
    public void encode(BsonWriter writer, RoomMgd roomMgd, EncoderContext encoderContext) {
        writer.writeStartDocument();
        writer.writeString("_room", roomMgd.getClass().getSimpleName().toLowerCase());
        writer.writeInt32("number", roomMgd.getNumber());
        writer.writeInt32("floor", roomMgd.getFloor());
        writer.writeDouble("surface", roomMgd.getSurface());
        writer.writeDouble("price", roomMgd.getPrice());
        writer.writeInt32("rented", roomMgd.getRented());
        writer.writeEndDocument();
    }

    @Override
    public RoomMgd decode(BsonReader reader, DecoderContext decoderContext) {
        reader.readStartDocument();

        String id = reader.readObjectId("_id").toString();
        String type = reader.readString("_room");
        int number = reader.readInt32("number");
        int floor = reader.readInt32("floor");
        double surface = reader.readDouble("surface");
        double price = reader.readDouble("price");
        int rented = reader.readInt32("rented");

        reader.readEndDocument(); // Ensure the document context is properly closed.

        RoomMgd room;
        if (type.equals("studio")) {
            room = new StudioMgd();
        } else {
            room = new MicroSuiteMgd();
        }

        room.setNumber(number);
        room.setFloor(floor);
        room.setSurface(surface);
        room.setPrice(price);
        room.setRented(rented);

        return room;
    }

    /*@Override
    public RoomMgd decode(BsonReader reader, DecoderContext decoderContext) {
        reader.readStartDocument();
        ObjectId typeId = reader.readObjectId();
        String id = typeId.toString();
        String type = reader.readString("_room");
        int number = reader.readInt32("number");
        int floor = reader.readInt32("floor");
        double surface = reader.readDouble("surface");
        double price = reader.readDouble("price");
        int rented = reader.readInt32("rented");

        RoomMgd room;
        if (type.equals("studio")) {
            room = new StudioMgd();
        } else {
            room = new MicroSuiteMgd();
        }
        room.setNumber(number);
        room.setFloor(floor);
        room.setSurface(surface);
        room.setPrice(price);
        room.setRented(rented);

        return room;
    }*/

    /*@Override
    public RoomMgd decode(BsonReader reader, DecoderContext decoderContext) {
        reader.readStartDocument();

        String type = null;
        while (reader.readBsonType() != BsonType.END_OF_DOCUMENT) {
            String fieldName = reader.readName();
            if (fieldName.equals("_room")) {
                if (reader.getCurrentBsonType() == BsonType.STRING) {
                    type = reader.readString();
                } else {
                    throw new BsonSerializationException("Expected '_room' to be a STRING but found: " + reader.getCurrentBsonType());
                }
            } else {
                reader.skipValue(); // Skip unknown fields
            }
        }

        if (type == null) {
            throw new BsonSerializationException("Missing discriminator key '_room'");
        }

        RoomMgd room;
        if (type.equals("studiomgd")) {
            room = new StudioMgd();
        } else if (type.equals("microsuitemgd")) {
            room = new MicroSuiteMgd();
        } else {
            throw new BsonSerializationException("Unknown room type: " + type);
        }

        reader.readEndDocument();
        return room;
    }*/

    @Override
    public Class<RoomMgd> getEncoderClass() {
        return RoomMgd.class;
    }
}
