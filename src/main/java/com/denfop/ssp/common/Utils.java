package com.denfop.ssp.common;

import com.denfop.ssp.SuperSolarPanels;
import ic2.core.gui.dynamic.GuiParser;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.ResourceLocation;

public class Utils {

    public static GuiParser.GuiNode parse(String teBlock) {
        ResourceLocation loc = SuperSolarPanels.getIdentifier("guidef/" + teBlock + ".xml");
        try {
            return GuiParser.parse(loc, SuperSolarPanels.class);
        } catch (Exception var3) {
            throw new RuntimeException(var3);
        }
    }

    public static NBTTagCompound getOrCreateNbtData(final ItemStack itemstack) {
        NBTTagCompound nbttagcompound = itemstack.getTagCompound();
        if (nbttagcompound == null) {
            nbttagcompound = new NBTTagCompound();
            itemstack.setTagCompound(nbttagcompound);
            nbttagcompound.setInteger("charge", 0);
            nbttagcompound.setInteger("Fly", 0);
            nbttagcompound.setInteger("solarType", 0);
            nbttagcompound.setInteger("energy", 0);
            nbttagcompound.setInteger("energy2", 0);
            nbttagcompound.setBoolean("isFlyActive", false);
            nbttagcompound.setBoolean("EnableWirelles", false);
            nbttagcompound.setInteger("World", 0);
            nbttagcompound.setInteger("Xcoord", 0);
            nbttagcompound.setInteger("Ycoord", 0);
            nbttagcompound.setInteger("Zcoord", 0);
            nbttagcompound.setInteger("tier", 0);
            nbttagcompound.setString("Name", "Name electical block");

        }
        return nbttagcompound;
    }

    public static NBTTagCompound getOrCreateNbtData(final EntityPlayer player) {
        NBTTagCompound nbttagcompound = player.getEntityData();

        if (nbttagcompound.getSize() == 0) {
            nbttagcompound = new NBTTagCompound();
            nbttagcompound.setBoolean("isFlyActive", false);
            nbttagcompound.setBoolean("isNightVision", false);
            nbttagcompound.setBoolean("isEffect", false);
            nbttagcompound.setBoolean("isEffect1", false);
            nbttagcompound.setBoolean("isEffect2", false);
            nbttagcompound.setBoolean("isEffect3", false);
        }
        return nbttagcompound;
    }

}
