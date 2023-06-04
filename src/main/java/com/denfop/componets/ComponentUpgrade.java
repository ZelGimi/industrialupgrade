package com.denfop.componets;

import com.denfop.IUItem;
import com.denfop.tiles.base.TileEntityInventory;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumHand;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

enum TypeUpgrade {
    STACK(new ItemStack(IUItem.module_stack)),
    INSTANT(new ItemStack(IUItem.module_quickly)),
    SORTING(new ItemStack(IUItem.module_storage));
    private final ItemStack stack;

    TypeUpgrade(ItemStack stack) {
        this.stack = stack;
    }

    public ItemStack getStack() {
        return stack;
    }
}

public class ComponentUpgrade extends AbstractComponent {

    private final List<TypeUpgrade> listUpgrade;
    private final List<TypeUpgrade> listActiveUpgrade = new ArrayList<>();
    private boolean change = true;
    private int maxUpgrade;

    public ComponentUpgrade(final TileEntityInventory parent, TypeUpgrade... typeUpgrades) {
        super(parent);
        this.listUpgrade = Arrays.asList(typeUpgrades);
        this.maxUpgrade = this.listUpgrade.size();
    }

    public boolean isChange() {
        return change;
    }

    public void setChange(final boolean change) {
        this.change = change;
    }

    public void setMaxUpgrade(final int maxUpgrade) {
        this.maxUpgrade = maxUpgrade;
    }

    public boolean hasUpgrade(TypeUpgrade typeUpgrade) {
        return listActiveUpgrade.contains(typeUpgrade);
    }

    @Override
    public boolean canUsePurifier(final EntityPlayer player) {
        return !this.listActiveUpgrade.isEmpty();
    }

    @Override
    public boolean onBlockActivated(final EntityPlayer player, final EnumHand hand) {
        final ItemStack stack = player.getHeldItem(hand);
        for (TypeUpgrade upgrade : this.listUpgrade) {
            if (upgrade.getStack().isItemEqual(stack)) {
                if (!this.listActiveUpgrade.contains(upgrade)) {
                    this.listActiveUpgrade.add(upgrade);
                    stack.shrink(1);
                    this.change = true;
                    return true;
                }
            }
        }
        return false;
    }

}
