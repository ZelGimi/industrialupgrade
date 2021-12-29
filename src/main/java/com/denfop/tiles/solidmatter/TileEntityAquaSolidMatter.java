package com.denfop.tiles.solidmatter;

import com.denfop.IUItem;
import com.denfop.tiles.base.TileMatterGenerator;
import net.minecraft.item.ItemStack;

public class TileEntityAquaSolidMatter extends TileMatterGenerator {

    static final ItemStack itemstack = new ItemStack(IUItem.matter, 1, 2);

    public TileEntityAquaSolidMatter() {
        super(itemstack, "GenAqua_matter.name");


    }

}
