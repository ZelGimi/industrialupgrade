package com.denfop.blockentity.mechanism.worlcollector;

import com.denfop.IUItem;
import com.denfop.api.blockentity.MultiBlockEntity;
import com.denfop.blockentity.base.BlockEntityBaseWorldCollector;
import com.denfop.blockentity.base.EnumTypeCollector;
import com.denfop.blocks.BlockTileEntity;
import com.denfop.blocks.mechanism.BlockBaseMachine3Entity;
import net.minecraft.core.BlockPos;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.block.state.BlockState;

public class BlockEntityEnderAssembler extends BlockEntityBaseWorldCollector {

    public BlockEntityEnderAssembler(BlockPos pos, BlockState state) {
        super(EnumTypeCollector.END, BlockBaseMachine3Entity.ender_assembler, pos, state);
    }

    public void init() {

        addRecipe(new ItemStack(Items.MAGMA_CREAM), 32, new ItemStack(Items.ENDER_PEARL));
        addRecipe(new ItemStack(Items.APPLE), 64, new ItemStack(Items.CHORUS_FRUIT));
        addRecipe(new ItemStack(Items.SKELETON_SKULL), 64, new ItemStack(Items.DRAGON_HEAD));
        addRecipe(new ItemStack(Items.GLASS_BOTTLE), 64, new ItemStack(Items.EXPERIENCE_BOTTLE));

    }

    public MultiBlockEntity getTeBlock() {
        return BlockBaseMachine3Entity.ender_assembler;
    }

    public BlockTileEntity getBlock() {
        return IUItem.basemachine2.getBlock(getTeBlock());
    }


}
