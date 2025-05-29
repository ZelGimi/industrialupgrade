package com.denfop.world;


import net.minecraft.nbt.CompoundTag;

public class GenData {

    private final int y;
    private final int x;
    private final int z;

    private final TypeGas typeGas;

    public GenData(int y, int x, int z, TypeGas typeGas) {

        this.y = y;
        this.x = x;
        this.z = z;

        this.typeGas = typeGas;
    }

    public GenData(CompoundTag nbtTagCompound) {
        this.y = nbtTagCompound.getShort("y");
        this.x = nbtTagCompound.getInt("x");
        this.z = nbtTagCompound.getInt("z");
        this.typeGas = TypeGas.values()[nbtTagCompound.getByte("type")];
    }

    public CompoundTag writeNBT() {
        CompoundTag nbtTagCompound = new CompoundTag();
        nbtTagCompound.putShort("y", (short) y);
        nbtTagCompound.putInt("x", (short) x);
        nbtTagCompound.putInt("z", (short) z);
        nbtTagCompound.putByte("type", (byte) typeGas.ordinal());
        return nbtTagCompound;
    }

    public TypeGas getTypeGas() {
        return typeGas;
    }

    public int getY() {
        return y;
    }

    public int getX() {
        return x;
    }

    public int getZ() {
        return z;
    }

}
