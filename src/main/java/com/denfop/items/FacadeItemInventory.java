package com.denfop.items;

import com.denfop.container.ContainerBase;
import com.denfop.gui.GuiCore;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.api.distmarker.OnlyIn;

public class FacadeItemInventory extends ItemStackInventory {

    public final int inventorySize;
    public final ItemStack itemStack1;


    public FacadeItemInventory(Player player, ItemStack stack, int inventorySize) {
        super(player, stack, inventorySize);
        this.inventorySize = inventorySize;
        this.itemStack1 = stack;
    }


    @Override
    public ItemStackInventory getParent() {
        return this;
    }


    @OnlyIn(Dist.CLIENT)
    @Override
    public GuiCore<ContainerBase<?>> getGui(Player var1, ContainerBase<?> menu) {
        ContainerFacadeItem containerLeadBox = (ContainerFacadeItem) menu;
        return new GuiFacadeItem(containerLeadBox, this.itemStack1);
    }

    public ContainerFacadeItem getGuiContainer(Player player) {
        return new ContainerFacadeItem(player, this);
    }


}
