package com.denfop.tiles.mechanism;

import com.denfop.api.inv.IHasGui;
import com.denfop.api.windsystem.IWindRotor;
import com.denfop.api.windsystem.IWindUpgradeBlock;
import com.denfop.container.ContainerWaterRotorUpgrade;
import com.denfop.gui.GuiWaterRotorUpgrade;
import com.denfop.invslot.InvSlotRotorWater;
import com.denfop.invslot.InvSlotWaterUpgrade;
import com.denfop.tiles.base.TileEntityInventory;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.BlockPos;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public class TileEntityWaterRotorModifier extends TileEntityInventory implements IWindUpgradeBlock, IHasGui {

    public final InvSlotWaterUpgrade slot;
    public final InvSlotRotorWater rotor_slot;

    public TileEntityWaterRotorModifier() {
        slot = new InvSlotWaterUpgrade(this);
        rotor_slot = new InvSlotRotorWater(slot);
    }


    @SideOnly(Side.CLIENT)
    protected boolean shouldSideBeRendered(EnumFacing side, BlockPos otherPos) {
        return false;
    }

    protected boolean isNormalCube() {
        return false;
    }

    protected boolean doesSideBlockRendering(EnumFacing side) {
        return false;
    }

    protected boolean isSideSolid(EnumFacing side) {
        return false;
    }

    protected boolean clientNeedsExtraModelInfo() {
        return true;
    }

    public boolean shouldRenderInPass(int pass) {
        return true;
    }

    @Override
    public IWindRotor getRotor() {
        if (!this.rotor_slot.get().isEmpty()) {
            return (IWindRotor) this.rotor_slot.get().getItem();
        }
        return null;
    }

    @Override
    public ItemStack getItemStack() {
        return this.rotor_slot.get();
    }

    @Override
    public ContainerWaterRotorUpgrade getGuiContainer(final EntityPlayer entityPlayer) {
        return new ContainerWaterRotorUpgrade(this, entityPlayer);
    }

    @Override
    @SideOnly(Side.CLIENT)
    public GuiScreen getGui(final EntityPlayer entityPlayer, final boolean b) {
        return new GuiWaterRotorUpgrade(getGuiContainer(entityPlayer));
    }

    @Override
    public void onGuiClosed(final EntityPlayer entityPlayer) {
    }

}
