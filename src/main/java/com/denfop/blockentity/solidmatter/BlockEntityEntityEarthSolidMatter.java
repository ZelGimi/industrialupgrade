package com.denfop.blockentity.solidmatter;

import com.denfop.IUItem;
import com.denfop.api.blockentity.MultiBlockEntity;
import com.denfop.blockentity.base.BlockEntityMatterGenerator;
import com.denfop.blocks.BlockTileEntity;
import com.denfop.blocks.mechanism.BlockSolidMatterEntity;
import net.minecraft.core.BlockPos;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.state.BlockState;

public class BlockEntityEntityEarthSolidMatter extends BlockEntityMatterGenerator {

    static final ItemStack itemstack = new ItemStack(IUItem.matter.getStack(5));

    public BlockEntityEntityEarthSolidMatter(BlockPos pos, BlockState state) {
        super(itemstack, "GenEarth_matter.name", BlockSolidMatterEntity.earth_solidmatter, pos, state);


    }

    public MultiBlockEntity getTeBlock() {
        return BlockSolidMatterEntity.earth_solidmatter;
    }

    public BlockTileEntity getBlock() {
        return IUItem.solidmatter.getBlock(getTeBlock().getId());
    }

}
