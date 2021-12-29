package com.denfop.tiles.solidmatter;

import com.denfop.IUItem;
import com.denfop.tiles.base.TileMatterGenerator;
import net.minecraft.item.ItemStack;

public class TileEntityEndSolidMatter extends TileMatterGenerator {

    static final ItemStack itemstack = new ItemStack(IUItem.matter, 1, 6);

    public TileEntityEndSolidMatter() {
        super(itemstack, "GenEnd_matter.name");


    }

}
