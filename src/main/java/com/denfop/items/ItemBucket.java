package com.denfop.items;

import com.denfop.IItemTab;
import com.denfop.IUCore;
import com.denfop.blocks.FluidName;
import net.minecraft.Util;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.world.item.BucketItem;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.material.Fluid;
import net.neoforged.neoforge.fluids.capability.IFluidHandlerItem;
import net.neoforged.neoforge.fluids.capability.wrappers.FluidBucketWrapper;

public class ItemBucket extends BucketItem implements IItemTab, ItemFluidCapabilities {
    private final FluidName fluidName;
    private String nameItem;

    public ItemBucket(Fluid supplier, FluidName fluidName) {
        super(supplier, new Properties().setNoRepair().stacksTo(1));
        this.fluidName = fluidName;
    }

    @Override
    public CreativeModeTab getItemCategory() {
        return IUCore.fluidCellTab;
    }

    protected String getOrCreateDescriptionId() {
        if (this.nameItem == null) {
            StringBuilder pathBuilder = new StringBuilder(Util.makeDescriptionId("iu", BuiltInRegistries.ITEM.getKey(this)));
            String targetString = "industrialupgrade.";
            String replacement = "";
            if (replacement != null) {
                int index = pathBuilder.indexOf(targetString);
                while (index != -1) {
                    pathBuilder.replace(index, index + targetString.length(), replacement);
                    index = pathBuilder.indexOf(targetString, index + replacement.length());
                }
            }
            this.nameItem = pathBuilder.toString();
        }

        return "item.forge.bucketFilled." + fluidName.getName().toLowerCase();
    }

    @Override
    public IFluidHandlerItem initCapabilities(ItemStack stack) {
        return new FluidBucketWrapper(stack);
    }
}
