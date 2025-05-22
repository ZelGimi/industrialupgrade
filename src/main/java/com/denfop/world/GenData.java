package com.denfop.world;

import net.minecraft.nbt.NBTTagCompound;

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

    public GenData(NBTTagCompound nbtTagCompound) {
        this.y = nbtTagCompound.getShort("y");
        this.x = nbtTagCompound.getInteger("x");
        this.z = nbtTagCompound.getInteger("z");
        this.typeGas = TypeGas.values()[nbtTagCompound.getByte("type")];
    }

    public NBTTagCompound writeNBT() {
        NBTTagCompound nbtTagCompound = new NBTTagCompound();
        nbtTagCompound.setShort("y", (short) y);
        nbtTagCompound.setInteger("x", (short) x);
        nbtTagCompound.setInteger("z", (short) z);
        nbtTagCompound.setByte("type", (byte) typeGas.ordinal());
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
