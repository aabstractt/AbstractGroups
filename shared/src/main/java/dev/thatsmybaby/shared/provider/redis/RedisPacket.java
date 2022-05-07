package dev.thatsmybaby.shared.provider.redis;

import com.google.common.io.ByteArrayDataInput;
import com.google.common.io.ByteArrayDataOutput;

public abstract class RedisPacket {

    public abstract int pid();

    public abstract void decode(ByteArrayDataInput stream);

    public abstract void encode(ByteArrayDataOutput stream);

    public abstract void handle();
}