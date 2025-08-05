package com.denfop.items.bags;

import com.denfop.container.ContainerBase;
import com.denfop.container.ContainerLeadBox;
import com.denfop.gui.GuiCore;
import com.denfop.gui.GuiLeadBox;
import com.denfop.items.ItemStackInventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.api.distmarker.OnlyIn;

public class ItemStackLeadBox extends ItemStackInventory {


    public final ItemStack itemStack1;


    public ItemStackLeadBox(Player player, ItemStack stack, int inventorySize) {
        super(player, stack, inventorySize);
        this.itemStack1 = stack;
    }

    @Override
    public ItemStackInventory getParent() {
        return this;
    }


    @OnlyIn(Dist.CLIENT)
    @Override
    public GuiCore<ContainerBase<?>> getGui(Player var1, ContainerBase<?> menu) {
        ContainerLeadBox containerLeadBox = (ContainerLeadBox) menu;
        return new GuiLeadBox(containerLeadBox, this.itemStack1);
    }

    public ContainerLeadBox getGuiContainer(Player player) {
        return new ContainerLeadBox(player, this);
    }


}
