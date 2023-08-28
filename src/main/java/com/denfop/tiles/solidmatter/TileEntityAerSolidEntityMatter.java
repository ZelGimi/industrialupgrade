package com.denfop.tiles.solidmatter;

import com.denfop.IUItem;
import com.denfop.api.tile.IMultiTileBlock;
import com.denfop.blocks.BlockTileEntity;
import com.denfop.blocks.mechanism.BlockSolidMatter;
import com.denfop.tiles.base.TileEntityMatterGenerator;
import net.minecraft.item.ItemStack;

public class TileEntityAerSolidEntityMatter extends TileEntityMatterGenerator {

    static final ItemStack itemstack = new ItemStack(IUItem.matter, 1, 7);

    public TileEntityAerSolidEntityMatter() {
        super(itemstack, "GenAer_matter.name");


    }

    public IMultiTileBlock getTeBlock() {
        return BlockSolidMatter.aer_solidmatter;
    }

    public BlockTileEntity getBlock() {
        return IUItem.solidmatter;
    }

}
