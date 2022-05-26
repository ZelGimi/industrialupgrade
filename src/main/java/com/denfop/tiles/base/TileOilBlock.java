package com.denfop.tiles.base;

import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;

public class TileOilBlock extends TileEntity {


    public int number;
    public int max;
    public boolean change;
    public boolean empty;

    public TileOilBlock() {

        this.empty = false;

    }

    public void readFromNBT(final NBTTagCompound nbttagcompound) {
        super.readFromNBT(nbttagcompound);
        this.number = nbttagcompound.getInteger("number");
        this.max = nbttagcompound.getInteger("max");
        this.change = nbttagcompound.getBoolean("change");
        this.empty = nbttagcompound.getBoolean("empty");
    }

    public NBTTagCompound writeToNBT(final NBTTagCompound nbttagcompound) {
        super.writeToNBT(nbttagcompound);
        nbttagcompound.setInteger("number", this.number);
        nbttagcompound.setBoolean("change", this.change);
        nbttagcompound.setBoolean("empty", this.empty);
        nbttagcompound.setInteger("max", this.max);
        return nbttagcompound;
    }

}
