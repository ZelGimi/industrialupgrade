package com.denfop.items.relocator;

import com.denfop.network.packet.CustomPacketBuffer;
import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;

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

    public Point(CompoundTag compound) {
        this.name = compound.getString("Name");
        this.pos = BlockPos.of(compound.getLong("Pos"));
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

    public CompoundTag writeToNBT(CompoundTag compound) {
        compound.putString("Name", this.name);
        compound.putLong("Pos", this.pos.asLong());
        return compound;
    }
}
