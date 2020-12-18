package com.denfop.ssp.common;


import com.denfop.ssp.SuperSolarPanels;
import com.denfop.ssp.tiles.SSPBlock;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.item.ItemStack;

import javax.annotation.Nonnull;

public class SSPSourceTab extends CreativeTabs {
	public SSPSourceTab(String label) {
		super(label);
	}

	@Nonnull
	public ItemStack getTabIconItem() {
		return SuperSolarPanels.machines.getItemStack(SSPBlock.admin_solar_panel);
	}
}
