package com.denfop.items;

import com.denfop.containermenu.ContainerMenuBase;
import com.denfop.containermenu.ContainerMenuEFReader;
import com.denfop.screen.ScreenEFReader;
import com.denfop.screen.ScreenIndustrialUpgrade;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.api.distmarker.OnlyIn;

;

public class EFReaderInventory extends ItemStackInventory {

    private final ItemStack itemStack1;

    public EFReaderInventory(Player player, ItemStack stack) {
        super(player, stack, 0);
        this.itemStack1 = stack;
    }

    public ContainerMenuBase<EFReaderInventory> getGuiContainer(Player player) {
        return new ContainerMenuEFReader(this, this.itemStack1, player);
    }


    @OnlyIn(Dist.CLIENT)
    public ScreenIndustrialUpgrade<ContainerMenuBase<?>> getGui(Player player, ContainerMenuBase<?> isAdmin) {
        return new ScreenEFReader((ContainerMenuEFReader) isAdmin, this.itemStack1);
    }


    public boolean isItemValidForSlot(int i, ItemStack itemstack) {
        return false;
    }

}
