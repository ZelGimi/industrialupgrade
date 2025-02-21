package com.denfop.tabs;


import com.denfop.IUItem;
import com.denfop.blocks.FluidName;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.item.ItemStack;

import javax.annotation.Nonnull;

public class TabCore extends CreativeTabs {

    private final int type;

    public TabCore(int type, String name) {
        super(name);
        this.type = type;
    }

    @Nonnull
    public ItemStack getTabIconItem() {
        switch (type) {
            case 0:
                return new ItemStack(IUItem.blockpanel);
            case 1:
                return new ItemStack(IUItem.basemodules);
            case 2:
                return new ItemStack(IUItem.basecircuit, 1, 11);
            case 3:
                return new ItemStack(IUItem.toriyore);
            case 4:
                return new ItemStack(IUItem.spectral_helmet);
            case 5:
                return new ItemStack(IUItem.block);
            case 6:
                return IUItem.reactormendeleviumQuad;
            case 7:
                return new ItemStack(IUItem.machinekit, 1, 3);
            case 8:
                return new ItemStack(IUItem.templates);
            case 9:
                return new ItemStack(IUItem.crafting_elements, 1, 21);
            case 10:
                return new ItemStack(IUItem.water_reactors_component, 1, 8);
            case 11:
                return new ItemStack(FluidName.fluiduu_matter.getInstance().getBlock(), 1);
            case 12:
                return new ItemStack(IUItem.crops);
            case 13:
                return new ItemStack(IUItem.jarBees);
            case 14:
                return new ItemStack(IUItem.genome_crop);
            case 15:
                return new ItemStack(IUItem.rocket);

        }
        return new ItemStack(IUItem.blockpanel);
    }

}
