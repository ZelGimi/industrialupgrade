package com.denfop.ssp;

import com.denfop.ssp.common.Constants;
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

public class SSPOre extends Block {
	public SSPOre(Material material, String name, float hardness, float resistance, String harvLevel, int level, SoundType soundType) {
		super(material);
		setRegistryName(Constants.MOD_ID, name);
		setUnlocalizedName(name);
		setHardness(hardness);
		setResistance(resistance);
		setHarvestLevel(harvLevel, level);
		setSoundType(soundType);
		setCreativeTab(SuperSolarPanels.SSPTab);
		setLightLevel(0.2F);
	}

	// public Item getItemDropped(IBlockState state, Random rand, int fortune) {
	// return ItemReg.LIRIUM;
	//}

	public int quantityDropped(@Nonnull Random random) {
		return 1;
	}

	public int quantityDroppedWithBonus(int fortune, @Nonnull Random random) {
		if (fortune > 0)
			return quantityDropped(random) + random.nextInt(fortune);
		return quantityDropped(random);
	}

	public int getExpDrop(@Nonnull IBlockState state, @Nonnull IBlockAccess world, @Nonnull BlockPos pos, int fortune) {
		Random rand = (world instanceof World) ? ((World) world).rand : new Random();
		return MathHelper.getInt(rand, 4, 7);
	}
}
