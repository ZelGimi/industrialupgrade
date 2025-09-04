package com.denfop.items.bags;

import com.denfop.containermenu.ContainerMenuBase;
import com.denfop.containermenu.ContainerMenuLeadBox;
import com.denfop.items.ItemStackInventory;
import com.denfop.screen.ScreenIndustrialUpgrade;
import com.denfop.screen.ScreenLeadBox;
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


    @OnlyIn(Dist.CLIENT)
    @Override
    public ScreenIndustrialUpgrade<ContainerMenuBase<?>> getGui(Player var1, ContainerMenuBase<?> menu) {
        ContainerMenuLeadBox containerLeadBox = (ContainerMenuLeadBox) menu;
        return new ScreenLeadBox(containerLeadBox, this.itemStack1);
    }

    public ContainerMenuLeadBox getGuiContainer(Player player) {
        return new ContainerMenuLeadBox(player, this);
    }


}
