package com.denfop.tiles.solidmatter;

import com.denfop.IUItem;
import com.denfop.tiles.base.TileMatterGenerator;
import net.minecraft.item.ItemStack;

public class TileEntitySolidMatter extends TileMatterGenerator {

    static final ItemStack itemstack = new ItemStack(IUItem.matter, 1, 0);

    public TileEntitySolidMatter() {
        super(itemstack, "GenMatter_matter.name");


    }

}
