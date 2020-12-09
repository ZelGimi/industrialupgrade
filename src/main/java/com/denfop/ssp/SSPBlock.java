package com.denfop.ssp;

import net.minecraft.block.Block;
import net.minecraft.block.SoundType;
import net.minecraft.block.material.Material;

public class SSPBlock extends Block {
	public SSPBlock(Material material, String name, float hardness, float resistance, String harvLevel, int level, SoundType soundType) {
		super(material);
		setRegistryName(SuperSolarPanels.MOD_ID, name);
		setUnlocalizedName(name);
		setHardness(hardness);
		setResistance(resistance);
		setHarvestLevel(harvLevel, level);
		setSoundType(soundType);
		setCreativeTab(SuperSolarPanels.SSPtab);
	}
}
