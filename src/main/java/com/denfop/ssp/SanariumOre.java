package com.denfop.ssp;

import net.minecraft.block.Block;
import net.minecraft.block.SoundType;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;

import javax.annotation.Nonnull;
import java.util.Random;

public class SanariumOre extends Block {
	public SanariumOre(Material material, String name, float hardness, float resistance, String harvLevel, int level, SoundType soundType) {
		super(material);
		setRegistryName(SuperSolarPanels.MOD_ID, name);
		setUnlocalizedName(name);
		setHardness(hardness);
		setResistance(resistance);
		setHarvestLevel(harvLevel, level);
		setSoundType(soundType);
		setCreativeTab(SuperSolarPanels.SSPtab);
		setLightLevel(0.2F);
	}

	public boolean hasTileEntity() {
		return false;
	}


	public int quantityDropped(@Nonnull Random random) {
		return 1;
	}


	public int getExpDrop(@Nonnull IBlockState state, @Nonnull IBlockAccess world, @Nonnull BlockPos pos, int fortune) {
		Random rand = (world instanceof World) ? ((World) world).rand : new Random();
		return MathHelper.getInt(rand, 4, 7);
	}
}
