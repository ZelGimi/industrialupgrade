package com.denfop.items;

import com.denfop.containermenu.ContainerMenuBase;
import com.denfop.screen.ScreenIndustrialUpgrade;
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


    @OnlyIn(Dist.CLIENT)
    @Override
    public ScreenIndustrialUpgrade<ContainerMenuBase<?>> getGui(Player var1, ContainerMenuBase<?> menu) {
        ContainerMenuFacadeItem containerLeadBox = (ContainerMenuFacadeItem) menu;
        return new ScreenFacadeItem(containerLeadBox, this.itemStack1);
    }

    public ContainerMenuFacadeItem getGuiContainer(Player player) {
        return new ContainerMenuFacadeItem(player, this);
    }


}
