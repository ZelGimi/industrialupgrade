package com.denfop.items;

import com.denfop.container.ContainerBase;
import com.denfop.container.ContainerEFReader;
import com.denfop.gui.GUIEFReader;
import com.denfop.gui.GuiCore;
import com.denfop.invslot.InvSlot;
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

    @Override
    public void addInventorySlot(final InvSlot var1) {

    }

    @Override
    public int getBaseIndex(final InvSlot var1) {
        return 0;
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
