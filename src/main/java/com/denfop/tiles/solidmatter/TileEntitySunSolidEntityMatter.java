package com.denfop.tiles.solidmatter;

import com.denfop.IUItem;
import com.denfop.tiles.base.TileEntityMatterGenerator;
import net.minecraft.item.ItemStack;

public class TileEntitySunSolidEntityMatter extends TileEntityMatterGenerator {

    static final ItemStack itemstack = new ItemStack(IUItem.matter, 1, 1);

    public TileEntitySunSolidEntityMatter() {
        super(itemstack, "GenSun_matter.name");


    }

}
