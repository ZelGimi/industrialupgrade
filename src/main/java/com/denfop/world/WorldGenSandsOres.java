package com.denfop.world;

import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.init.Blocks;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraft.world.gen.feature.WorldGenerator;

import java.util.Random;

public class WorldGenSandsOres extends WorldGenerator {

    private final IBlockState block;
    private final int numberOfBlocks;

    public WorldGenSandsOres(int p_i2011_1_, IBlockState state) {
        this.block = state;
        this.numberOfBlocks = p_i2011_1_;
    }

    public boolean generate(World p_180709_1_, Random p_180709_2_, BlockPos p_180709_3_) {
        if (p_180709_1_.getBlockState(p_180709_3_).getMaterial() != Material.WATER) {
            return false;
        } else {
            int lvt_4_1_ = p_180709_2_.nextInt(this.numberOfBlocks - 2) + 2;


            for (int lvt_6_1_ = p_180709_3_.getX() - lvt_4_1_; lvt_6_1_ <= p_180709_3_.getX() + lvt_4_1_; ++lvt_6_1_) {
                for (int lvt_7_1_ = p_180709_3_.getZ() - lvt_4_1_; lvt_7_1_ <= p_180709_3_.getZ() + lvt_4_1_; ++lvt_7_1_) {
                    int lvt_8_1_ = lvt_6_1_ - p_180709_3_.getX();
                    int lvt_9_1_ = lvt_7_1_ - p_180709_3_.getZ();
                    if (lvt_8_1_ * lvt_8_1_ + lvt_9_1_ * lvt_9_1_ <= lvt_4_1_ * lvt_4_1_) {
                        for (int lvt_10_1_ = p_180709_3_.getY() - 1; lvt_10_1_ <= p_180709_3_.getY() + 1; ++lvt_10_1_) {
                            BlockPos lvt_11_1_ = new BlockPos(lvt_6_1_, lvt_10_1_, lvt_7_1_);
                            IBlockState state = p_180709_1_.getBlockState(lvt_11_1_);
                            Block lvt_12_1_ = p_180709_1_.getBlockState(lvt_11_1_).getBlock();
                            if (lvt_12_1_ == Blocks.DIRT || state == this.block) {
                                p_180709_1_.setBlockState(lvt_11_1_, this.block, 2);
                            }
                        }
                    }
                }
            }

            return true;
        }
    }

}
