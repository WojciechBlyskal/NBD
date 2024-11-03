package org.example.codecTest;

import com.mongodb.MongoClientSettings;
import org.bson.codecs.configuration.CodecRegistry;
import org.example.codec.UniqueIdCodec;
import org.junit.jupiter.api.Test;
import org.example.simpleMgdTypes.UniqueIdMgd;

import static junit.framework.Assert.assertEquals;

public class UniqueIdCodecTest {

    @Test
    public void getEncoderClassTest(){

        CodecRegistry pojoCodecRegistry =
                MongoClientSettings.getDefaultCodecRegistry();

        UniqueIdCodec uniqueIdCodec =
                new UniqueIdCodec(pojoCodecRegistry);
        assertEquals(uniqueIdCodec.getEncoderClass(),
                UniqueIdMgd.class);
    }
}
