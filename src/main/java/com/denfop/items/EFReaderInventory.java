package com.denfop.items;

import com.denfop.containermenu.ContainerMenuBase;
import com.denfop.containermenu.ContainerMenuEFReader;
import com.denfop.inventory.Inventory;
import com.denfop.screen.ScreenEFReader;
import com.denfop.screen.ScreenIndustrialUpgrade;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

import javax.annotation.Nonnull;

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

    @Override
    public void addInventorySlot(final Inventory var1) {

    }


    @Nonnull
    public String getName() {
        return "book";
    }

    public boolean hasCustomName() {
        return false;
    }

    public boolean isItemValidForSlot(int i, ItemStack itemstack) {
        return false;
    }

}
