package com.denfop.render.streak;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.nbt.NBTTagCompound;

public class PlayerStreakInfo {

    private  RGB rgb;
    private  boolean rainbow;

    public PlayerStreakInfo(RGB rgb, boolean rainbow){
        this.rgb= rgb;
        this.rainbow = rainbow;
    }
    public PlayerStreakInfo(NBTTagCompound nbtTagCompound){
        this.rgb= new RGB(nbtTagCompound.getShort("red"),nbtTagCompound.getShort("green"),nbtTagCompound.getShort("blue"));
        this.rainbow = nbtTagCompound.getBoolean("rainbow");
    }
    public NBTTagCompound writeNBT(){
        final NBTTagCompound nbt = new NBTTagCompound();
        nbt.setShort("red",rgb.getRed());
        nbt.setShort("blue",rgb.getBlue());
        nbt.setShort("green",rgb.getGreen());
        nbt.setBoolean("rainbow",rainbow);
        return nbt;
    }
    public boolean isRainbow() {
        return rainbow;
    }

    public RGB getRgb() {
        return rgb;
    }



    public void setRainbow(final boolean rainbow) {
        this.rainbow = rainbow;
    }

    public void setRgb(final RGB rgb) {
        this.rgb = rgb;
    }

}
