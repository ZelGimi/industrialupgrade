package com.Denfop.ssp;


import com.Denfop.ssp.tiles.SSPBlock;

import ic2.api.item.IC2Items;
import ic2.core.block.ITeBlock;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraftforge.fml.common.Loader;

public class SSPSourceTab extends CreativeTabs {
  public SSPSourceTab(String label) {
    super("SSPSourceTab");
  }
  
  public ItemStack getTabIconItem() {
   
    return  SuperSolarPanels.machines.getItemStack((ITeBlock)SSPBlock.admin_solar_panel);
  }
}
