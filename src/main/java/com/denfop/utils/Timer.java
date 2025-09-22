package com.denfop.utils;

import com.denfop.Localization;
import com.denfop.network.packet.CustomPacketBuffer;
import net.minecraft.nbt.NBTTagCompound;

import java.io.IOException;

public class Timer {

    boolean canWork = true;
    private int max;
    private int hour;
    private int minute;
    private int seconds;

    public Timer(int hour, int minute, int seconds) {
        this.hour = hour;
        this.minute = minute;
        this.seconds = seconds;
        this.max = hour * 3600 + minute * 60 + seconds;
    }

    public Timer(int time) {
        int hour = time / 3600;
        time = time - hour * 3600;
        int minute = time / 60;
        int seconds = time - minute * 60;
        this.hour = hour;
        this.minute = minute;
        this.seconds = seconds;
        this.max = hour * 3600 + minute * 60 + seconds;
    }

    public Timer(NBTTagCompound tagCompound) {
        this.hour = tagCompound.getInteger("hour");
        this.minute = tagCompound.getByte("minute");
        this.seconds = tagCompound.getByte("seconds");
        this.canWork = tagCompound.getBoolean("canWork");
        this.max = tagCompound.getInteger("max");
    }

    public Timer(CustomPacketBuffer is) {
        this.hour = is.readInt();
        this.minute = is.readByte();
        this.seconds = is.readByte();
        this.canWork = is.readBoolean();
        max = is.readInt();
    }

    public void readBuffer(CustomPacketBuffer is) throws IOException {
        this.hour = is.readInt();
        this.minute = is.readByte();
        this.seconds = is.readByte();
        this.canWork = is.readBoolean();
        max = is.readInt();
    }

    public void writeBuffer(CustomPacketBuffer buffer) {
        buffer.writeInt(this.hour);
        buffer.writeByte(this.minute);
        buffer.writeByte(this.seconds);
        buffer.writeBoolean(this.canWork);
        buffer.writeInt(this.max);
    }

    public NBTTagCompound writeNBT(NBTTagCompound tagCompound) {
        tagCompound.setInteger("hour", this.hour);
        tagCompound.setByte("minute", (byte) this.minute);
        tagCompound.setByte("seconds", (byte) this.seconds);
        tagCompound.setBoolean("canWork", this.canWork);
        tagCompound.setInteger("max", this.max);
        return tagCompound;
    }


    public String getDisplay() {
        String time = "";
        if (hour > 0) {
            time += this.hour + Localization.translate("iu.hour");
        }
        if (minute > 0) {
            time += this.minute + Localization.translate("iu.minutes");
        }
        if (seconds > 0) {
            time += this.seconds + Localization.translate("iu.seconds");
        }
        return time;
    }

    public int getMax() {
        return max;
    }

    public double getProgressBar() {
        return Math.min(1, (max - getBar()) / (max * 1d));
    }

    public int getBar() {
        return (this.hour * 3600 + this.minute * 60 + this.seconds);
    }

    public int getTime() {
        return this.seconds + this.minute * 60 + this.hour * 3600;
    }

    public void work(double percent) {
        for (int i = 0; i < percent; i++) {
            if (seconds != 0) {
                seconds--;
            } else if (minute != 0) {
                seconds = 59;
                minute--;
            } else if (hour != 0) {
                hour--;
                this.minute = 59;
                this.seconds = 59;
            } else {
                canWork = false;
            }
        }
    }

    public void work() {
        for (int i = 0; i < 1; i++) {
            if (seconds != 0) {
                seconds--;
            } else if (minute != 0) {
                seconds = 59;
                minute--;
            } else if (hour != 0) {
                hour--;
                this.minute = 59;
                this.seconds = 59;
            } else {
                canWork = false;
            }
        }
    }

    public void rework() {
        seconds++;
        if (seconds == 60) {
            seconds = 0;
            this.minute++;
            if (minute == 60) {
                minute = 0;
                this.hour++;
            }
        }
    }

    public boolean canWork() {
        return canWork;
    }

    public void setCanWork(final boolean canWork) {
        this.canWork = canWork;
    }

    public void readNBT(NBTTagCompound tagCompound) {
        this.hour = tagCompound.getInteger("hour");
        this.minute = tagCompound.getByte("minute");
        this.seconds = tagCompound.getByte("seconds");
        this.canWork = tagCompound.getBoolean("canWork");
    }

    public void readTimer(Timer timer) {
        this.hour = timer.hour;
        this.seconds = timer.seconds;
        this.minute = timer.minute;
        this.canWork = timer.canWork;
    }

    public Timer cloning() {
        Timer timer = new Timer(this.hour, this.minute, this.seconds);
        timer.canWork = this.canWork;
        return timer;
    }

    public boolean getMinute(int minute) {
        return this.minute == minute;
    }

    public void getTimeFromTimerRemove(Timer timerTo) {
        this.hour = this.hour - timerTo.hour;
        this.minute = this.minute - timerTo.minute;
        this.seconds = this.seconds - timerTo.seconds;
        if (this.seconds < 0) {
            this.seconds = Math.abs(seconds);
        }
        this.max = hour * 3600 + minute * 60 + seconds;
        ;
    }

}
