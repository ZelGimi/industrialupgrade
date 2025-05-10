package com.denfop.tiles.solidmatter;

import com.denfop.IUItem;
import com.denfop.api.tile.IMultiTileBlock;
import com.denfop.blocks.BlockTileEntity;
import com.denfop.blocks.mechanism.BlockSolidMatter;
import com.denfop.tiles.base.TileEntityMatterGenerator;
import net.minecraft.core.BlockPos;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.state.BlockState;

public class TileEntityEndSolidEntityMatter extends TileEntityMatterGenerator {

    static final ItemStack itemstack = new ItemStack(IUItem.matter.getStack(6));

    public TileEntityEndSolidEntityMatter(BlockPos pos, BlockState state) {
        super(itemstack, "GenEnd_matter.name", BlockSolidMatter.end_solidmatter, pos, state);


    }

    public IMultiTileBlock getTeBlock() {
        return BlockSolidMatter.end_solidmatter;
    }

    public BlockTileEntity getBlock() {
        return IUItem.solidmatter.getBlock(getTeBlock().getId());
    }

}
