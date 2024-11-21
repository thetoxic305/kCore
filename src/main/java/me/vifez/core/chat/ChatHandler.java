package me.vifez.core.chat;

import me.vifez.core.kCore;
import me.vifez.core.handler.Handler;

public class ChatHandler extends Handler {

    private boolean muted = false;
    private long delay = -1L;

    public ChatHandler(kCore core) {
        super(core);
    }

    public boolean isMuted() {
        return muted;
    }

    public void setMuted(boolean muted) {
        this.muted = muted;
    }

    public boolean isDelay() {
        return delay != -1L;
    }

    public long getDelay() {
        return delay;
    }

    public void setDelay(long delay) {
        this.delay = delay;
    }

}