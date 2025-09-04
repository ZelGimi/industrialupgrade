package com.denfop.api.item.upgrade;


import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.registries.RegistryObject;

public class UpgradeModificator {

    public final RegistryObject<? extends Item> itemstack;
    public final String type;

    public UpgradeModificator(RegistryObject<? extends Item> stack, String type) {
        this.itemstack = stack;
        this.type = type;
    }

    public boolean matches(ItemStack stack) {
        return this.itemstack.get() == stack.getItem();
    }

    public boolean matches(String type) {
        return this.type.equals(type);
    }

    @Override
    public boolean equals(final Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        UpgradeModificator that = (UpgradeModificator) o;
        return this.itemstack.get() == that.itemstack.get();
    }


}
