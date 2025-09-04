package com.denfop.items.armour.special;

import com.denfop.api.container.CustomWorldContainer;
import com.denfop.containermenu.ContainerMenuBase;
import com.denfop.items.ItemStackInventory;
import com.denfop.screen.ScreenIndustrialUpgrade;
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

    public ContainerMenuBase<ItemStackStreakSettings> getGuiContainer(Player player) {
        return new ContainerMenuStreak(player, this);
    }


    @OnlyIn(Dist.CLIENT)
    public ScreenIndustrialUpgrade<ContainerMenuBase<? extends CustomWorldContainer>> getGui(Player player, ContainerMenuBase<? extends CustomWorldContainer> isAdmin) {
        return new ScreenColorPicker((ContainerMenuStreak) isAdmin, itemStack1);
    }


}
