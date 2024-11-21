package me.vifez.core.util;

import me.vifez.core.kCore;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.scheduler.BukkitTask;

public class Timer {

    private long currentTime;
    private long startTime;
    private boolean countdown;
    private boolean async = false;
    private boolean paused;
    private BukkitTask task;

    public Timer(long time, boolean countdown) {
        this(time, time, countdown);
    }

    public Timer(long currentTime, long startTime, boolean countdown) {
        this.currentTime = currentTime;
        this.startTime = startTime;
        this.countdown = countdown;
    }

    public final void reset() {
        currentTime = startTime;
        paused = false;

        if (task == null) {
            onStart();
            BukkitRunnable runnable = new BukkitRunnable() {
                @Override
                public void run() {
                    if (paused) {
                        return;
                    }

                    if (countdown) {
                        if (currentTime == 0L) {
                            stop(false);
                            onExpire();
                            currentTime = -1L;
                            return;
                        }

                        currentTime--;
                    } else {
                        currentTime++;
                    }

                    onTick();
                }
            };
            if (async) {
                task = runnable.runTaskTimerAsynchronously(kCore.getInstance(), 20L, 20L);
                return;
            }
            task = runnable.runTaskTimer(kCore.getInstance(), 20L, 20L);
        }

    }

    public void onStart() {
    }

    public void onTick() {
    }

    public void onExpire() {
    }

    public void onStop() {
    }

    public final void stop() {
        stop(true);
    }

    private void stop(boolean onStop) {
        if (task != null) {
            task.cancel();
        }
        task = null;
        currentTime = -1L;
        if (onStop) {
            onStop();
        }
    }

    public long getCurrentTime() {
        return currentTime;
    }

    public void setCurrentTime(long currentTime) {
        this.currentTime = currentTime;
    }

    public long getStartTime() {
        return startTime;
    }

    public void setStartTime(long startTime) {
        this.startTime = startTime;
    }

    public boolean isCountdown() {
        return countdown;
    }

    public void setCountdown(boolean countdown) {
        this.countdown = countdown;
    }

    public boolean isAsync() {
        return async;
    }

    public void setAsync(boolean async) {
        this.async = async;
    }

    public boolean isPaused() {
        return paused;
    }

    public void setPaused(boolean paused) {
        this.paused = paused;
    }

}