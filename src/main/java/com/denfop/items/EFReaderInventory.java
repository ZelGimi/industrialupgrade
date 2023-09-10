package com.denfop.items;

import com.denfop.container.ContainerBase;
import com.denfop.container.ContainerEFReader;
import com.denfop.gui.GUIEFReader;
import com.denfop.invslot.InvSlot;
import com.denfop.tiles.base.TileEntityInventory;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import javax.annotation.Nonnull;

public class EFReaderInventory extends ItemStackInventory {

    private final ItemStack itemStack1;

    public EFReaderInventory(EntityPlayer player, ItemStack stack) {
        super(player, stack, 0);
        this.itemStack1 = stack;
    }

    public ContainerBase<EFReaderInventory> getGuiContainer(EntityPlayer player) {
        return new ContainerEFReader(this, this.itemStack1);
    }


    @SideOnly(Side.CLIENT)
    public GuiScreen getGui(EntityPlayer player, boolean isAdmin) {
        return new GUIEFReader(new ContainerEFReader(this, this.itemStack1), this.itemStack1);
    }

    @Override
    public TileEntityInventory getParent() {
        return null;
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
