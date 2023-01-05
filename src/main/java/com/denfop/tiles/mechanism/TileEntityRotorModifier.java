package com.denfop.tiles.mechanism;

import com.denfop.api.windsystem.IWindRotor;
import com.denfop.api.windsystem.IWindUpgradeBlock;
import com.denfop.api.windsystem.InvSlotUpgrade;
import com.denfop.api.windsystem.upgrade.InvSlotRotor;
import com.denfop.componets.AdvEnergy;
import com.denfop.container.ContainerRotorUpgrade;
import com.denfop.gui.GuiRotorUpgrade;
import com.denfop.tiles.base.TileEntityInventory;
import ic2.core.IHasGui;
import ic2.core.init.Localization;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.BlockPos;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import java.util.List;

public class TileEntityRotorModifier extends TileEntityInventory implements IWindUpgradeBlock, IHasGui {

    public final InvSlotUpgrade slot;
    public final InvSlotRotor rotor_slot;

    public TileEntityRotorModifier() {
        slot = new InvSlotUpgrade(this);
        rotor_slot = new InvSlotRotor(slot);
    }

    @SideOnly(Side.CLIENT)
    public void addInformation(ItemStack stack, List<String> tooltip, ITooltipFlag advanced) {
        if (this.hasComponent(AdvEnergy.class)) {
            AdvEnergy energy = this.getComponent(AdvEnergy.class);
            if (!energy.getSourceDirs().isEmpty()) {
                tooltip.add(Localization.translate("ic2.item.tooltip.PowerTier", energy.getSourceTier()));
            } else if (!energy.getSinkDirs().isEmpty()) {
                tooltip.add(Localization.translate("ic2.item.tooltip.PowerTier", energy.getSinkTier()));
            }
        }

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
    public ContainerRotorUpgrade getGuiContainer(final EntityPlayer entityPlayer) {
        return new ContainerRotorUpgrade(this, entityPlayer);
    }

    @Override
    @SideOnly(Side.CLIENT)
    public GuiScreen getGui(final EntityPlayer entityPlayer, final boolean b) {
        return new GuiRotorUpgrade(getGuiContainer(entityPlayer));
    }

    @Override
    public void onGuiClosed(final EntityPlayer entityPlayer) {

    }

}
