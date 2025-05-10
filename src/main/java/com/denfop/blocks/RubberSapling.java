package com.denfop.blocks;

import com.denfop.IUItem;
import com.denfop.world.RubTreeFeature;
import com.denfop.world.RubberTreeGrower;
import com.denfop.world.WorldBaseGen;
import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.util.RandomSource;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.SaplingBlock;
import net.minecraft.world.level.block.SoundType;
import net.minecraft.world.level.block.grower.AbstractTreeGrower;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.levelgen.feature.FeaturePlaceContext;
import net.minecraft.world.level.material.Material;
import net.minecraft.world.level.storage.loot.LootContext;

import java.util.List;
import java.util.Optional;

import static com.denfop.world.WorldBaseGen.RUB_TREE;
import static com.denfop.world.WorldBaseGen.RUB_TREE_GENERATOR;

public class RubberSapling extends SaplingBlock {
    public RubberSapling() {
        super(new RubberTreeGrower(), Properties.of(Material.PLANT).noCollission().randomTicks().strength(0.0F).instabreak().sound(SoundType.GRASS));
    }

    public List<ItemStack> getDrops(BlockState p_60537_, LootContext.Builder p_60538_) {
        if (!p_60538_.getLevel().isClientSide) {
            return List.of(new ItemStack(IUItem.rubberSapling.getItem()));
        }
        return super.getDrops(p_60537_, p_60538_);
    }

    public void advanceTree(ServerLevel pLevel, BlockPos pPos, BlockState pState, RandomSource pRandom) {
        if (pState.getValue(STAGE) == 0) {
            pLevel.setBlock(pPos, pState.cycle(STAGE), 4);
        } else {
            ((RubTreeFeature)RUB_TREE_GENERATOR.get()).placeInstantly(pLevel, pLevel.getChunkSource().getGenerator(),  pState, pRandom,pPos);
          }

    }
}
