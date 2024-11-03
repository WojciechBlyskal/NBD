package org.example.codec;

import org.bson.codecs.Codec;
import org.bson.codecs.configuration.CodecProvider;
import org.bson.codecs.configuration.CodecRegistry;

import org.example.simpleMgdTypes.UniqueIdMgd;

public class UniqueIdCodecProvider implements CodecProvider {

    public UniqueIdCodecProvider() {
    }

    @Override
    @SuppressWarnings("unchecked")
    public <T> Codec<T> get(Class<T> aClass,
                            CodecRegistry codecRegistry) {
        if (aClass == UniqueIdMgd.class){
            return (Codec<T>) new UniqueIdCodec(codecRegistry);
        }
        return null;
    }
}
