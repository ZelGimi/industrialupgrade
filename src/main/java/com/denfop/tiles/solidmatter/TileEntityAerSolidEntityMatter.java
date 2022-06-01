package com.denfop.tiles.solidmatter;

import com.denfop.IUItem;
import com.denfop.tiles.base.TileEntityMatterGenerator;
import net.minecraft.item.ItemStack;

public class TileEntityAerSolidEntityMatter extends TileEntityMatterGenerator {

    static final ItemStack itemstack = new ItemStack(IUItem.matter, 1, 7);

    public TileEntityAerSolidEntityMatter() {
        super(itemstack, "GenAer_matter.name");


    }

}
