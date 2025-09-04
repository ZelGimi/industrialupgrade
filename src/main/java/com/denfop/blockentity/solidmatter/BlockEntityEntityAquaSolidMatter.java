package com.denfop.blockentity.solidmatter;

import com.denfop.IUItem;
import com.denfop.api.blockentity.MultiBlockEntity;
import com.denfop.blockentity.base.BlockEntityMatterGenerator;
import com.denfop.blocks.BlockTileEntity;
import com.denfop.blocks.mechanism.BlockSolidMatterEntity;
import net.minecraft.core.BlockPos;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.state.BlockState;

public class BlockEntityEntityAquaSolidMatter extends BlockEntityMatterGenerator {

    static final ItemStack itemstack = new ItemStack(IUItem.matter.getStack(2));

    public BlockEntityEntityAquaSolidMatter(BlockPos pos, BlockState state) {
        super(itemstack, "GenAqua_matter.name", BlockSolidMatterEntity.aqua_solidmatter, pos, state);


    }

    public MultiBlockEntity getTeBlock() {
        return BlockSolidMatterEntity.aqua_solidmatter;
    }

    public BlockTileEntity getBlock() {
        return IUItem.solidmatter.getBlock(getTeBlock().getId());
    }

}
