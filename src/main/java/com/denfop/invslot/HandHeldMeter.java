package com.denfop.invslot;

import com.denfop.container.ContainerBase;
import com.denfop.container.ContainerMeter;
import com.denfop.gui.GuiToolMeter;
import com.denfop.items.HandHeldInventory;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public class HandHeldMeter extends HandHeldInventory {

    public HandHeldMeter(EntityPlayer player, ItemStack stack) {
        super(player, stack, 0);
    }

    public ContainerBase<?> getGuiContainer(EntityPlayer player) {
        return new ContainerMeter(player, this);
    }

    @SideOnly(Side.CLIENT)
    public GuiScreen getGui(EntityPlayer player, boolean isAdmin) {
        return new GuiToolMeter(new ContainerMeter(player, this));
    }

    public String getName() {
        return "iu.meter";
    }

    public boolean hasCustomName() {
        return false;
    }

    public void closeGUI() {
        this.player.closeScreen();
    }

}
