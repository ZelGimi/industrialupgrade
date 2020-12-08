package com.denfop.ssp;

import net.minecraft.block.Block;
import net.minecraft.block.SoundType;
import net.minecraft.block.material.Material;
import net.minecraft.creativetab.CreativeTabs;

public class SSPB extends Block {
  public SSPB(Material material, String name, float hardness, float resistance, String harvLevel, int level, SoundType soundType) {
    super(material);
    setRegistryName("super_solar_panels", name);
    setUnlocalizedName(name);
    setHardness(hardness);
    setResistance(resistance);
    setHarvestLevel(harvLevel, level);
    setSoundType(soundType);
    setCreativeTab(SuperSolarPanels.SSPtab);
  }
}
