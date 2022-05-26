package com.denfop.tiles.base;

import com.denfop.Config;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;

public class TileEntityVein extends TileEntity {


    public final int max;
    public int meta;
    public int number;
    public boolean change;

    public TileEntityVein() {
        this.number = Config.maxVein;
        this.max = Config.maxVein;
        this.meta = 0;

    }

    public void readFromNBT(final NBTTagCompound nbttagcompound) {
        super.readFromNBT(nbttagcompound);
        this.number = nbttagcompound.getInteger("number");
        this.change = nbttagcompound.getBoolean("change");
        this.meta = nbttagcompound.getInteger("meta");
    }

    public NBTTagCompound writeToNBT(final NBTTagCompound nbttagcompound) {
        super.writeToNBT(nbttagcompound);
        nbttagcompound.setInteger("number", this.number);
        nbttagcompound.setInteger("meta", this.meta);
        nbttagcompound.setBoolean("change", this.change);
        return nbttagcompound;
    }

}
