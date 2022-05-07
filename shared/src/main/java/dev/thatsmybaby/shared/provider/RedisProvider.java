package dev.thatsmybaby.shared.provider;

import com.google.common.io.ByteArrayDataInput;
import com.google.common.io.ByteArrayDataOutput;
import com.google.common.io.ByteStreams;
import dev.thatsmybaby.shared.VersionInfo;
import dev.thatsmybaby.shared.provider.redis.RedisPacket;
import lombok.Getter;
import org.apache.logging.log4j.Level;
import redis.clients.jedis.*;

import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ForkJoinPool;
import java.util.function.Consumer;
import java.util.function.Function;

@SuppressWarnings("UnstableApiUsage")
public final class RedisProvider {

    @Getter private final static RedisProvider instance = new RedisProvider();

    private static final Map<Integer, Class<? extends RedisPacket>> packetPool = new HashMap<>();

    private JedisPool jedisPool = null;
    private Subscription jedisPubSub = null;

    private String password = null;

    public void init(String address, String password) {
        if (address == null) {
            return;
        }

        String[] addressSplit = address.split(":");
        String host = addressSplit[0];
        int port = addressSplit.length > 1 ? Integer.parseInt(addressSplit[1]) : Protocol.DEFAULT_PORT;

        this.password = password != null && password.length() > 0 ? password : null;

        this.jedisPool = new JedisPool(new JedisPoolConfig(), host, port, 30_000, this.password, 0, null);

        ForkJoinPool.commonPool().execute(() -> execute(jedis -> {
            jedis.subscribe(this.jedisPubSub = new Subscription(), "SurvivalSync".getBytes(StandardCharsets.UTF_8));
        }));

        /*if (this.connected()) {
            Bukkit.getLogger().info("Redis is now successfully connected and synchronization is ready!");
        } else {
            Bukkit.getLogger().info("Could not connect to redis, synchronization won't work with other servers, you will still be able to use VicnixCore normally on this server. (" + Bukkit.getName() + ")");
        }*/
    }

    public void redisMessage(RedisPacket pk) {
        CompletableFuture.runAsync(() -> execute(jedis -> {
            ByteArrayDataOutput stream = ByteStreams.newDataOutput();

            stream.writeInt(pk.pid());
            pk.encode(stream);

            jedis.publish("AbstractGroups".getBytes(StandardCharsets.UTF_8), stream.toByteArray());
        }));
    }

    public static <T> T execute(Function<Jedis, T> action) {
        if (!instance.connected()) {
            throw new RuntimeException("Jedis was disconnected");
        }

        try (Jedis jedis = instance.jedisPool.getResource()) {
            if (instance.password != null && !instance.password.isEmpty()) {
                jedis.auth(instance.password);
            }

            return action.apply(jedis);
        }
    }

    public static void execute(Consumer<Jedis> action) {
        if (!instance.connected()) {
            return;
        }

        try (Jedis jedis = instance.jedisPool.getResource()) {
            if (instance.password != null && !instance.password.isEmpty()) {
                jedis.auth(instance.password);
            }

            action.accept(jedis);
        }
    }

    public boolean connected() {
        return this.jedisPool != null && !this.jedisPool.isClosed();
    }

    public void close() throws InterruptedException {
        if (this.jedisPubSub != null) {
            this.jedisPubSub.unsubscribe();
        }

        if (this.jedisPool != null) {
            this.jedisPool.destroy();
        }
    }

    public void registerPacket(RedisPacket... pools) {
        for (RedisPacket pool : pools) packetPool.put(pool.pid(), pool.getClass());
    }

    private RedisPacket constructMessage(int pid) {
        Class<? extends RedisPacket> instance = packetPool.get(pid);

        if (instance == null) {
            return null;
        }

        try {
            return instance.getDeclaredConstructor().newInstance();
        } catch (Exception e) {
            e.printStackTrace();
        }

        return null;
    }

    protected class Subscription extends BinaryJedisPubSub {

        @Override
        public void onMessage(byte[] channel, byte[] message) {
            ByteArrayDataInput stream = ByteStreams.newDataInput(message);

            RedisPacket pk = constructMessage(stream.readInt());

            if (pk == null) {
                VersionInfo.getLogger().log(Level.FATAL, "Redis messenger received a invalid packet");

                return;
            }

            pk.decode(stream);

            pk.handle();

            VersionInfo.getLogger().log(Level.FATAL, "Packet " + pk.getClass().getName() + " decoded and handled!");
        }
    }
}