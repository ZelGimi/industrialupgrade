package com.denfop.items.relocator;

import com.denfop.network.packet.CustomPacketBuffer;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.math.BlockPos;

public class Point {

    private final String name;
    private final BlockPos pos;

    public Point(String name, BlockPos pos) {
        this.name = name;
        this.pos = pos;
    }

    public Point(CustomPacketBuffer buffer) {
        name = buffer.readString();
        pos = buffer.readBlockPos();
    }

    public Point(NBTTagCompound compound) {
        this.name = compound.getString("Name");
        this.pos = BlockPos.fromLong(compound.getLong("Pos"));
    }

    public BlockPos getPos() {
        return pos;
    }

    public String getName() {
        return name;
    }

    public void writeToBuffer(CustomPacketBuffer buffer) {
        buffer.writeString(name);
        buffer.writeBlockPos(pos);
    }

    public NBTTagCompound writeToNBT(NBTTagCompound compound) {
        compound.setString("Name", this.name);
        compound.setLong("Pos", this.pos.toLong());
        return compound;
    }

}
