package com.denfop.world;

import net.minecraft.nbt.NBTTagCompound;

public class GenData {

    private final int y;
    private final TypeGas typeGas;

    public GenData(int y, TypeGas typeGas) {

        this.y = y;

        this.typeGas = typeGas;
    }

    public GenData(NBTTagCompound nbtTagCompound) {
        this.y = nbtTagCompound.getShort("y");
        this.typeGas = TypeGas.values()[nbtTagCompound.getByte("type")];
    }

    public NBTTagCompound writeNBT() {
        NBTTagCompound nbtTagCompound = new NBTTagCompound();
        nbtTagCompound.setShort("y", (short) y);
        nbtTagCompound.setByte("type", (byte) typeGas.ordinal());
        return nbtTagCompound;
    }

    public TypeGas getTypeGas() {
        return typeGas;
    }

    public int getY() {
        return y;
    }



}
