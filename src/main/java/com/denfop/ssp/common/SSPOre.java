package com.denfop.ssp.common;

import com.denfop.ssp.SuperSolarPanels;
import net.minecraft.block.Block;
import net.minecraft.block.SoundType;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.item.Item;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.world.World;

import javax.annotation.ParametersAreNonnullByDefault;
import java.util.Random;

public class SSPOre extends Block {

    public SSPOre(
            Material material,
            String name,
            float hardness,
            float resistance,
            String harvLevel,
            int level,
            SoundType soundType
    ) {
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

    @Override
    @ParametersAreNonnullByDefault
    public int quantityDroppedWithBonus(int fortune, Random random) {

        if (fortune > 0 && Item.getItemFromBlock(this) != this.getItemDropped(this
                .getBlockState()
                .getValidStates()
                .iterator()
                .next(), random, fortune)) {

            int i = Math.max(0, random.nextInt(fortune + 2) - 1);

            return this.quantityDropped(random) * (i + 1);

        } else {
            return this.quantityDropped(random);
        }

    }

    @Override
    @ParametersAreNonnullByDefault
    public int getExpDrop(IBlockState state, net.minecraft.world.IBlockAccess world, BlockPos pos, int fortune) {
        Random rand = world instanceof World ? ((World) world).rand : new Random();
        if (this.getItemDropped(state, rand, fortune) != Item.getItemFromBlock(this)) {
            return MathHelper.getInt(rand, 0, 2);
        }
        return 0;
    }

}
