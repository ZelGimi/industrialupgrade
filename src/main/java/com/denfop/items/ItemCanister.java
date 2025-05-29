package com.denfop.items;

import com.denfop.IItemTab;
import com.denfop.IUCore;
import com.denfop.blocks.FluidName;
import net.minecraft.core.NonNullList;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.material.Fluid;

public class ItemCanister extends ItemFluidContainer implements IItemTab {

    public ItemCanister() {
        super(1000, 1);
    }

    @Override
    public CreativeModeTab getItemCategory() {
        return IUCore.ItemTab;
    }
    @Override
    public void fillItemCategory(CreativeModeTab p_41391_, NonNullList<ItemStack> p_41392_) {
        if (this.allowedIn(p_41391_)) {
            p_41392_.add(new ItemStack(this));
            p_41392_.add(this.getItemStack(FluidName.fluidmotoroil.getInstance().get()));
            p_41392_.add(this.getItemStack(FluidName.fluidsteam_oil.getInstance().get()));
        }
    }


    public boolean canfill(Fluid fluid) {
        return fluid == FluidName.fluidmotoroil.getInstance().get() || fluid == FluidName.fluidsteam_oil.getInstance().get() ;
    }

}
