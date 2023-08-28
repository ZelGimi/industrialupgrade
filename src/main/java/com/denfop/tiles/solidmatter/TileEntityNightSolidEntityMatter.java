package com.denfop.tiles.solidmatter;

import com.denfop.IUItem;
import com.denfop.api.tile.IMultiTileBlock;
import com.denfop.blocks.BlockTileEntity;
import com.denfop.blocks.mechanism.BlockSolidMatter;
import com.denfop.tiles.base.TileEntityMatterGenerator;
import net.minecraft.item.ItemStack;

public class TileEntityNightSolidEntityMatter extends TileEntityMatterGenerator {

    static final ItemStack itemstack = new ItemStack(IUItem.matter, 1, 4);

    public TileEntityNightSolidEntityMatter() {
        super(itemstack, "GenNight_matter.name");


    }

    public IMultiTileBlock getTeBlock() {
        return BlockSolidMatter.night_solidmatter;
    }

    public BlockTileEntity getBlock() {
        return IUItem.solidmatter;
    }

}
