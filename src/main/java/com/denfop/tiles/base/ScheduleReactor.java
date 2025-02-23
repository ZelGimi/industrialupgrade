package com.denfop.tiles.base;

import com.denfop.network.packet.CustomPacketBuffer;
import com.denfop.utils.Timer;
import net.minecraft.nbt.NBTTagCompound;

import java.io.IOException;

public class ScheduleReactor {

    private final Timer timer;
    private int minTemperature;

    public ScheduleReactor(int minTemperature, int timer) {
        this.minTemperature = minTemperature;
        this.timer = new Timer(timer);
    }

    public ScheduleReactor(NBTTagCompound tagCompound) {
        this.minTemperature = tagCompound.getInteger("minTemperature");
        this.timer = new Timer(0);
        this.timer.readNBT(tagCompound);
    }

    public NBTTagCompound writeNBT(NBTTagCompound tagCompound) {
        tagCompound.setInteger("minTemperature", minTemperature);
        this.timer.writeNBT(tagCompound);
        return tagCompound;
    }

    public void readBuffer(CustomPacketBuffer is) throws IOException {
        this.minTemperature = is.readInt();
        this.timer.readBuffer(is);
    }

    public void writeBuffer(CustomPacketBuffer buffer) {
        buffer.writeInt(this.minTemperature);
        this.timer.writeBuffer(buffer);
    }

}
