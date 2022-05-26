package com.denfop.tiles.base;

import com.denfop.container.ContainerTransformer;
import com.denfop.gui.GuiTransformer;
import ic2.api.energy.EnergyNet;
import ic2.api.network.INetworkClientTileEntityEventListener;
import ic2.core.ContainerBase;
import ic2.core.IHasGui;
import ic2.core.block.TileEntityInventory;
import ic2.core.block.comp.Energy;
import ic2.core.init.Localization;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.EnumFacing;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import java.util.Collections;
import java.util.EnumSet;
import java.util.List;

public abstract class TileEntityTransformer extends TileEntityInventory implements IHasGui,
        INetworkClientTileEntityEventListener {

    private static final TileEntityTransformer.Mode defaultMode;

    static {
        defaultMode = TileEntityTransformer.Mode.redstone;
    }

    protected final Energy energy;
    private final int defaultTier;
    private double inputFlow = 0.0D;
    private double outputFlow = 0.0D;
    private TileEntityTransformer.Mode configuredMode;
    private TileEntityTransformer.Mode transformMode;

    public TileEntityTransformer(int tier) {
        this.configuredMode = defaultMode;
        this.transformMode = null;
        this.defaultTier = tier;
        this.energy = this.addComponent((new Energy(
                this,
                EnergyNet.instance.getPowerFromTier(tier) * 8.0D,
                Collections.emptySet(),
                Collections.emptySet(),
                tier,
                tier,
                true
        )).setMultiSource(true));
    }

    public String getType() {
        switch (this.energy.getSourceTier()) {
            case 5:
                return "UMV";
            case 6:
                return "UHV";
            case 7:
                return "UEV";
            case 8:
                return "UMHV";
            case 9:
                return "UMEV";
            case 10:
                return "UHEV";
            case 11:
                return "HEEV";
        }
        return "";
    }

    public void readFromNBT(NBTTagCompound nbt) {
        super.readFromNBT(nbt);
        int mode = nbt.getInteger("mode");
        if (mode >= 0 && mode < TileEntityTransformer.Mode.VALUES.length) {
            this.configuredMode = TileEntityTransformer.Mode.VALUES[mode];
        } else {
            this.configuredMode = defaultMode;
        }

    }

    public NBTTagCompound writeToNBT(NBTTagCompound nbt) {
        super.writeToNBT(nbt);
        nbt.setInteger("mode", this.configuredMode.ordinal());
        return nbt;
    }

    protected void onLoaded() {
        super.onLoaded();
        if (!this.getWorld().isRemote) {
            this.updateRedstone(true);
        }

    }

    public TileEntityTransformer.Mode getMode() {
        return this.configuredMode;
    }

    public void onNetworkEvent(EntityPlayer player, int event) {
        if (event >= 0 && event < TileEntityTransformer.Mode.VALUES.length) {
            this.configuredMode = TileEntityTransformer.Mode.VALUES[event];
            this.updateRedstone(false);
        } else if (event == 3) {
            this.outputFlow = EnergyNet.instance.getPowerFromTier(this.energy.getSinkTier());
            this.inputFlow = EnergyNet.instance.getPowerFromTier(this.energy.getSinkTier());


        }

    }

    protected void updateEntityServer() {
        super.updateEntityServer();
        this.updateRedstone(false);
    }

    private void updateRedstone(boolean force) {
        assert !this.getWorld().isRemote;

        TileEntityTransformer.Mode newMode;
        switch (this.configuredMode) {
            case redstone:
                newMode = this.getWorld().isBlockPowered(this.pos)
                        ? TileEntityTransformer.Mode.stepup
                        : TileEntityTransformer.Mode.stepdown;
                break;
            case stepdown:
            case stepup:
                newMode = this.configuredMode;
                break;
            default:
                throw new RuntimeException("invalid mode: " + this.configuredMode);
        }

        if (newMode != TileEntityTransformer.Mode.stepup && newMode != TileEntityTransformer.Mode.stepdown) {
            throw new RuntimeException("invalid mode: " + newMode);
        } else {
            this.energy.setEnabled(true);
            if (force || this.transformMode != newMode) {
                this.transformMode = newMode;
                this.setActive(this.isStepUp());
                if (this.isStepUp()) {
                    this.energy.setSourceTier(this.defaultTier + 1);
                    this.energy.setSinkTier(this.defaultTier);
                    this.energy.setPacketOutput(1);
                    this.energy.setDirections(EnumSet.complementOf(EnumSet.of(this.getFacing())), EnumSet.of(this.getFacing()));
                } else {
                    this.energy.setSourceTier(this.defaultTier);
                    this.energy.setSinkTier(this.defaultTier + 1);
                    this.energy.setPacketOutput(4);
                    this.energy.setDirections(EnumSet.of(this.getFacing()), EnumSet.complementOf(EnumSet.of(this.getFacing())));
                }

                this.outputFlow = EnergyNet.instance.getPowerFromTier(this.energy.getSourceTier());
                this.inputFlow = EnergyNet.instance.getPowerFromTier(this.energy.getSinkTier());
            }

        }
    }

    public void setFacing(EnumFacing facing) {
        super.setFacing(facing);
        if (!this.getWorld().isRemote) {
            this.updateRedstone(true);
        }

    }

    @SideOnly(Side.CLIENT)
    public void addInformation(ItemStack stack, List<String> tooltip, ITooltipFlag advanced) {
        super.addInformation(stack, tooltip, advanced);
        tooltip.add(String.format(
                "%s %.0f %s %s %.0f %s",
                Localization.translate("ic2.item.tooltip.Low"),
                EnergyNet.instance.getPowerFromTier(this.energy.getSinkTier()),
                Localization.translate("ic2.generic.text.EUt"),
                Localization.translate("ic2.item.tooltip.High"),
                EnergyNet.instance.getPowerFromTier(this.energy.getSourceTier() + 1),
                Localization.translate("ic2.generic.text.EUt")
        ));
    }

    public ContainerBase<TileEntityTransformer> getGuiContainer(EntityPlayer player) {
        return new ContainerTransformer(player, this, 219);
    }

    @SideOnly(Side.CLIENT)
    public GuiScreen getGui(EntityPlayer player, boolean isAdmin) {
        return new GuiTransformer(new ContainerTransformer(player, this, 219));
    }

    public void onGuiClosed(EntityPlayer player) {
    }

    public double getinputflow() {
        return !this.isStepUp() ? this.inputFlow : this.outputFlow;
    }

    public double getoutputflow() {
        return this.isStepUp() ? this.inputFlow : this.outputFlow;
    }

    public boolean isStepUp() {
        return this.transformMode == TileEntityTransformer.Mode.stepup;
    }

    public enum Mode {
        redstone,
        stepdown,
        stepup;

        static final TileEntityTransformer.Mode[] VALUES = values();

        Mode() {
        }
    }

}
