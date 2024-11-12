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
    }

    @Override
    public Class<RoomMgd> getEncoderClass() {
        return RoomMgd.class;
    }
}
