package com.denfop.ssp.items.api;

import net.minecraft.entity.EntityLivingBase;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagInt;

public class RechargeHelper {
	public static int getCharge(ItemStack is) {
		if (is == null || is.isEmpty() || !(is.getItem() instanceof IRechargable))
			return -1;
		if (is.hasTagCompound() && is.getTagCompound() != null) {
			return is.getTagCompound().getInteger("tc.charge");
		}
		return 0;
	}

	public static boolean consumeCharge(ItemStack is, EntityLivingBase player, int amt) {
		if (is == null || is.isEmpty() || !(is.getItem() instanceof IRechargable))
			return false;
		if (is.hasTagCompound()) {
			int charge = 0;
			if (is.getTagCompound() != null)
				charge = is.getTagCompound().getInteger("tc.charge");
			if (charge >= amt) {
				charge -= amt;
				is.setTagInfo("tc.charge", new NBTTagInt(charge));
				return true;
			}
		}
		return false;
	}
}
