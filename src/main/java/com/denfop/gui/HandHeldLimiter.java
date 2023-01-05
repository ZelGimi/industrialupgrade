package com.denfop.gui;

import com.denfop.container.ContainerLimiter;
import ic2.core.ContainerBase;
import ic2.core.item.tool.HandHeldInventory;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public class HandHeldLimiter extends HandHeldInventory {

    public HandHeldLimiter(EntityPlayer player, ItemStack stack) {
        super(player, stack, 0);
    }

    public ContainerBase<?> getGuiContainer(EntityPlayer player) {
        return new ContainerLimiter(player, this);
    }

    @SideOnly(Side.CLIENT)
    public GuiScreen getGui(EntityPlayer player, boolean isAdmin) {
        return new GuiToolLimiter(new ContainerLimiter(player, this));
    }

    public String getName() {
        return "iu.limiter";
    }

    public boolean hasCustomName() {
        return false;
    }

    public void closeGUI() {
        this.player.closeScreen();
    }

}
