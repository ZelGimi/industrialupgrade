package com.denfop.items;

import com.denfop.container.ContainerBase;
import com.denfop.container.ContainerEFReader;
import com.denfop.gui.GUIEFReader;
import com.denfop.gui.GuiCore;
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

    public ContainerBase<EFReaderInventory> getGuiContainer(Player player) {
        return new ContainerEFReader(this, this.itemStack1, player);
    }


    @OnlyIn(Dist.CLIENT)
    public GuiCore<ContainerBase<?>> getGui(Player player, ContainerBase<?> isAdmin) {
        return new GUIEFReader((ContainerEFReader) isAdmin, this.itemStack1);
    }

    @Override
    public ItemStackInventory getParent() {
        return this;
    }


    public boolean isItemValidForSlot(int i, ItemStack itemstack) {
        return false;
    }

}
