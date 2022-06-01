package com.denfop.tiles.solidmatter;

import com.denfop.IUItem;
import com.denfop.tiles.base.TileEntityMatterGenerator;
import net.minecraft.item.ItemStack;

public class TileEntityAquaSolidEntityMatter extends TileEntityMatterGenerator {

    static final ItemStack itemstack = new ItemStack(IUItem.matter, 1, 2);

    public TileEntityAquaSolidEntityMatter() {
        super(itemstack, "GenAqua_matter.name");


    }

}
