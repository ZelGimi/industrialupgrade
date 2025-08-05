package com.denfop.items.armour.special;

import com.denfop.ElectricItem;
import com.denfop.api.inv.IAdvInventory;
import com.denfop.api.upgrade.UpgradeSystem;
import com.denfop.container.ContainerBase;
import com.denfop.datacomponent.DataComponentsInit;
import com.denfop.gui.GuiCore;
import com.denfop.items.EnumInfoUpgradeModules;
import com.denfop.items.ItemStackInventory;
import com.denfop.items.bags.BagsDescription;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.api.distmarker.OnlyIn;

import java.util.ArrayList;
import java.util.List;

public class ItemStackLegsBags extends ItemStackInventory {

    public final int inventorySize;
    public final ItemStack itemStack1;
    private final double coef;

    public ItemStackLegsBags(Player player, ItemStack stack) {
        super(player, stack, 45);
        this.inventorySize = 45;
        this.itemStack1 = stack;
        this.coef = 1D - (UpgradeSystem.system.hasModules(EnumInfoUpgradeModules.ENERGY, stack) ?
                UpgradeSystem.system.getModules(EnumInfoUpgradeModules.ENERGY, stack).number * 0.25D : 0);
        this.updatelist();
    }

    public ContainerBase<ItemStackLegsBags> getGuiContainer(Player player) {
        return new ContainerLegsBags(player, this);
    }

    @OnlyIn(Dist.CLIENT)
    public GuiCore<ContainerBase<? extends IAdvInventory>> getGui(Player player, ContainerBase<? extends IAdvInventory> isAdmin) {
        return new GuiLegsBags((ContainerLegsBags) isAdmin, itemStack1);
    }

    @Override
    public ItemStackInventory getParent() {
        return this;
    }


    @Override
    public void setItem(int slot, ItemStack stack) {
        super.setItem(slot, stack);

        this.updatelist();
    }


    private void updatelist() {
        List<BagsDescription> list = new ArrayList<>();
        for (ItemStack stack : this.inventory) {
            if (stack == null) {
                continue;
            }
            if (stack.isEmpty()) {
                continue;
            }
            if (list.isEmpty()) {
                list.add(new BagsDescription(stack));
            } else if (list.contains(new BagsDescription(stack))) {
                for (BagsDescription bagsDescription : list) {
                    if (bagsDescription.equals(new BagsDescription(stack))) {
                        bagsDescription.addCount(stack.getCount());
                    }
                }
            } else {
                list.add(new BagsDescription(stack));
            }
        }
        this.itemStack1.set(DataComponentsInit.DESCRIPTIONS_CONTAINER, list);
    }


    public boolean isItemValidForSlot(int i, ItemStack itemstack) {


        if (ElectricItem.manager.canUse(itemStack1, 50 * coef)) {
            return !itemstack.isEmpty();
        } else {
            return false;
        }
    }

}
