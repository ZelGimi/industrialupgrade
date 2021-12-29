package com.denfop.tiles.base;

import com.denfop.Config;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;

public class TileEntityVein extends TileEntity {


    public int meta;
    public int number;
    public final int max;
    public boolean change;

    public TileEntityVein() {
        this.number = Config.maxVein;
        this.max = Config.maxVein;
        this.meta = 0;

    }

    public void readFromNBT(final NBTTagCompound nbttagcompound) {
        super.readFromNBT(nbttagcompound);
        number = nbttagcompound.getInteger("number");
        change = nbttagcompound.getBoolean("change");
        meta = nbttagcompound.getInteger("meta");
    }

    public NBTTagCompound writeToNBT(final NBTTagCompound nbttagcompound) {
        super.writeToNBT(nbttagcompound);
        nbttagcompound.setInteger("number", number);
        nbttagcompound.setInteger("meta", meta);
        nbttagcompound.setBoolean("change", change);
        return nbttagcompound;
    }

}
