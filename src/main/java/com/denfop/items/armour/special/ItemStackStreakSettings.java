package com.denfop.items.armour.special;

import com.denfop.api.inv.IAdvInventory;
import com.denfop.container.ContainerBase;
import com.denfop.gui.GuiCore;
import com.denfop.items.ItemStackInventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.api.distmarker.OnlyIn;

public class ItemStackStreakSettings extends ItemStackInventory {

    public final ItemStack itemStack1;

    public ItemStackStreakSettings(Player player, ItemStack stack) {
        super(player, stack, 0);
        this.itemStack1 = stack;

    }

    public ContainerBase<ItemStackStreakSettings> getGuiContainer(Player player) {
        return new ContainerStreak(player, this);
    }


    @OnlyIn(Dist.CLIENT)
    public GuiCore<ContainerBase<? extends IAdvInventory>> getGui(Player player, ContainerBase<? extends IAdvInventory> isAdmin) {
        return new GuiColorPicker((ContainerStreak) isAdmin, itemStack1);
    }

    @Override
    public ItemStackInventory getParent() {
        return this;
    }


}
