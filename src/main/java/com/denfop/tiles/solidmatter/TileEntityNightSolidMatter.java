package com.denfop.tiles.solidmatter;

import com.denfop.IUItem;
import com.denfop.tiles.base.TileMatterGenerator;
import net.minecraft.item.ItemStack;

public class TileEntityNightSolidMatter extends TileMatterGenerator {

    static final ItemStack itemstack = new ItemStack(IUItem.matter, 1, 4);

    public TileEntityNightSolidMatter() {
        super(itemstack, "GenNight_matter.name");


    }

}
