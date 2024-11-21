package me.vifez.core.util.redis;

public abstract class Packet {

    private boolean async;

    public abstract void onReceive();

    public boolean isAsync() {
        return async;
    }

    public void setAsync(boolean async) {
        this.async = async;
    }

    public final void send() {
        RedisHandler.sendPacket(this);
    }

}