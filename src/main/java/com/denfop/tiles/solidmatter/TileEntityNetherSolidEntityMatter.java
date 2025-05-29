package com.denfop.tiles.solidmatter;

import com.denfop.IUItem;
import com.denfop.api.tile.IMultiTileBlock;
import com.denfop.blocks.BlockTileEntity;
import com.denfop.blocks.mechanism.BlockSolidMatter;
import com.denfop.tiles.base.TileEntityMatterGenerator;
import net.minecraft.core.BlockPos;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.state.BlockState;

public class TileEntityNetherSolidEntityMatter extends TileEntityMatterGenerator {

    static final ItemStack itemstack = new ItemStack(IUItem.matter.getStack(3));

    public TileEntityNetherSolidEntityMatter(BlockPos pos, BlockState state) {
        super(itemstack, "GenNether_matter.name", BlockSolidMatter.nether_solidmatter, pos, state);


    }

    public IMultiTileBlock getTeBlock() {
        return BlockSolidMatter.nether_solidmatter;
    }

    public BlockTileEntity getBlock() {
        return IUItem.solidmatter.getBlock(getTeBlock().getId());
    }

}
