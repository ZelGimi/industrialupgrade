package com.denfop.tiles.solidmatter;

import com.denfop.IUItem;
import com.denfop.tiles.base.TileMatterGenerator;
import net.minecraft.item.ItemStack;

public class TileEntityNetherSolidMatter extends TileMatterGenerator {

    static final ItemStack itemstack = new ItemStack(IUItem.matter, 1, 3);

    public TileEntityNetherSolidMatter() {
        super(itemstack, "GenNether_matter.name");


    }

}
