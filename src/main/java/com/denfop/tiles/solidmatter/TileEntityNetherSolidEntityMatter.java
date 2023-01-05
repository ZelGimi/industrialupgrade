package com.denfop.tiles.solidmatter;

import com.denfop.IUItem;
import com.denfop.tiles.base.TileEntityMatterGenerator;
import net.minecraft.item.ItemStack;

public class TileEntityNetherSolidEntityMatter extends TileEntityMatterGenerator {

    static final ItemStack itemstack = new ItemStack(IUItem.matter, 1, 3);

    public TileEntityNetherSolidEntityMatter() {
        super(itemstack, "GenNether_matter.name");


    }

}
