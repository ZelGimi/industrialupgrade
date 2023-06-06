package com.denfop.utils;

import ic2.core.init.Localization;
import ic2.core.network.GrowingBuffer;
import net.minecraft.nbt.NBTTagCompound;

import java.io.DataInput;
import java.io.IOException;

public class Timer {

    boolean canWork = true;
    private int hour;
    private int minute;
    private int seconds;

    public Timer(int hour, int minute, int seconds) {
        this.hour = hour;
        this.minute = minute;
        this.seconds = seconds;
    }


    public void readBuffer(DataInput is) throws IOException {
        this.hour = is.readInt();
        this.minute = is.readInt();
        this.seconds = is.readInt();
        this.canWork = is.readBoolean();
    }

    public void writeBuffer(GrowingBuffer buffer) {
        buffer.writeInt(this.hour);
        buffer.writeInt(this.minute);
        buffer.writeInt(this.seconds);
        buffer.writeBoolean(this.canWork);
    }

    public NBTTagCompound writeNBT(NBTTagCompound tagCompound) {
        tagCompound.setInteger("hour", this.hour);
        tagCompound.setInteger("minute", this.minute);
        tagCompound.setInteger("seconds", this.seconds);
        tagCompound.setBoolean("canWork", this.canWork);
        return tagCompound;
    }


    public String getDisplay() {
        return this.hour + Localization.translate("iu.hour") + this.minute + Localization.translate("iu.minutes") + this.seconds + Localization.translate(
                "iu.seconds");
    }

    public void work() {
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

    public boolean canWork() {
        return canWork;
    }

    public void readNBT(NBTTagCompound tagCompound) {
        this.hour = tagCompound.getInteger("hour");
        this.minute = tagCompound.getInteger("minute");
        this.seconds = tagCompound.getInteger("seconds");
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

}
