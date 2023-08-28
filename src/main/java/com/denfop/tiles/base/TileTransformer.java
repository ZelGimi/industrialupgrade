package com.denfop.tiles.base;

import com.denfop.Localization;
import com.denfop.api.energy.EnergyNetGlobal;
import com.denfop.api.energy.ITransformer;
import com.denfop.componets.AdvEnergy;
import com.denfop.componets.Redstone;
import com.denfop.componets.RedstoneHandler;
import com.denfop.container.ContainerTransformer;
import com.denfop.gui.GuiTransformer;
import com.denfop.network.DecoderHandler;
import com.denfop.network.EncoderHandler;
import com.denfop.network.IUpdatableTileEvent;
import com.denfop.network.packet.CustomPacketBuffer;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.EnumFacing;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import java.io.IOException;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

public abstract class TileTransformer extends TileEntityInventory implements
        IUpdatableTileEvent, ITransformer {

    private static final TileTransformer.Mode defaultMode;

    static {
        defaultMode = TileTransformer.Mode.redstone;
    }

    protected final AdvEnergy energy;
    private final int defaultTier;
    private final Redstone redstone;
    private boolean hasRedstone = false;
    private double inputFlow = 0.0D;
    private double outputFlow = 0.0D;
    private TileTransformer.Mode configuredMode;
    private TileTransformer.Mode transformMode;

    public TileTransformer(int tier) {
        this.configuredMode = defaultMode;
        this.transformMode = null;
        this.defaultTier = tier;
        this.energy = this.addComponent(new AdvEnergy(
                this,
                EnergyNetGlobal.instance.getPowerFromTier(tier) * 4.0D,
                Collections.emptyList(),
                Collections.emptyList(),
                tier,
                tier,
                true
        ).setMultiSource(true));
        this.redstone = this.addComponent(new Redstone(this));
        this.redstone.subscribe(new RedstoneHandler() {
            @Override
            public void action(final int input) {
                hasRedstone = input != 0;
            }
        });
    }

    public String getType() {
        switch (this.energy.getSourceTier()) {
            case 1:
                return "LV";
            case 2:
                return "MV";
            case 3:
                return "HV";
            case 4:
                return "EV";
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

    @Override
    public void readContainerPacket(final CustomPacketBuffer customPacketBuffer) {
        super.readContainerPacket(customPacketBuffer);
        try {
            configuredMode = Mode.values()[(int) DecoderHandler.decode(customPacketBuffer)];
            inputFlow = (double) DecoderHandler.decode(customPacketBuffer);
            outputFlow = (double) DecoderHandler.decode(customPacketBuffer);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

    }

    @Override
    public CustomPacketBuffer writeContainerPacket() {
        final CustomPacketBuffer packet = super.writeContainerPacket();
        try {
            EncoderHandler.encode(packet, configuredMode);
            EncoderHandler.encode(packet, inputFlow);
            EncoderHandler.encode(packet, outputFlow);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        return packet;
    }

    public void readFromNBT(NBTTagCompound nbt) {
        super.readFromNBT(nbt);
        int mode = nbt.getInteger("mode");
        if (mode >= 0 && mode < TileTransformer.Mode.VALUES.length) {
            this.configuredMode = TileTransformer.Mode.VALUES[mode];
        } else {
            this.configuredMode = defaultMode;
        }

    }

    public NBTTagCompound writeToNBT(NBTTagCompound nbt) {
        super.writeToNBT(nbt);
        nbt.setInteger("mode", this.configuredMode.ordinal());
        return nbt;
    }

    public void onLoaded() {
        super.onLoaded();
        if (!this.getWorld().isRemote) {
            this.updateRedstone(true);
        }

    }

    public TileTransformer.Mode getMode() {
        return this.configuredMode;
    }

    public void updateTileServer(EntityPlayer player, double event) {
        if (event >= 0 && event < TileTransformer.Mode.VALUES.length) {
            this.configuredMode = TileTransformer.Mode.VALUES[(int) event];
            this.updateRedstone(false);
        } else if (event == 3) {
            this.outputFlow = EnergyNetGlobal.instance.getPowerFromTier(this.energy.getSinkTier());
            this.inputFlow = EnergyNetGlobal.instance.getPowerFromTier(this.energy.getSinkTier());


        }

    }

    public void updateEntityServer() {
        super.updateEntityServer();
        this.updateRedstone(false);
    }

    private void updateRedstone(boolean force) {
        assert !this.getWorld().isRemote;

        TileTransformer.Mode newMode;
        switch (this.configuredMode) {
            case redstone:
                newMode = this.hasRedstone
                        ? TileTransformer.Mode.stepup
                        : TileTransformer.Mode.stepdown;
                break;
            case stepdown:
            case stepup:
                newMode = this.configuredMode;
                break;
            default:
                throw new RuntimeException("invalid mode: " + this.configuredMode);
        }

        this.energy.setEnabled(true);
        if (force || this.transformMode != newMode) {
            this.transformMode = newMode;
            this.setActive(this.isStepUp());
            if (this.isStepUp()) {
                this.energy.setSourceTier(this.defaultTier + 1);
                this.energy.setSinkTier(this.defaultTier);
                this.energy.setPacketOutput(1);
                this.energy.setDirections(
                        Collections.singletonList(this.getFacing()),
                        Arrays
                                .asList(EnumFacing.VALUES)
                                .stream()
                                .filter(facing -> facing != this.getFacing())
                                .collect(Collectors.toList())
                );
            } else {
                this.energy.setSourceTier(this.defaultTier);
                this.energy.setSinkTier(this.defaultTier + 1);
                this.energy.setPacketOutput(4);
                this.energy.setDirections(
                        Arrays
                                .asList(EnumFacing.VALUES)
                                .stream()
                                .filter(facing -> facing != this.getFacing())
                                .collect(Collectors.toList()), Collections.singletonList(this.getFacing()));

            }

            this.outputFlow = EnergyNetGlobal.instance.getPowerFromTier(this.energy.getSourceTier());
            this.inputFlow = EnergyNetGlobal.instance.getPowerFromTier(this.energy.getSinkTier());
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
        tooltip.add(Localization.translate("iu.item.tooltip.Low") + (int) EnergyNetGlobal.instance.getPowerFromTier(this.energy.getSinkTier()) + " "
                + Localization.translate("iu.generic.text.EUt") + " " + Localization.translate("iu.item.tooltip.High")
                + (int) EnergyNetGlobal.instance.getPowerFromTier(this.energy.getSourceTier() + 1) + " " + Localization.translate(
                "iu" +
                        ".generic.text.EUt"));

    }

    public ContainerTransformer getGuiContainer(EntityPlayer player) {
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
        return this.transformMode == TileTransformer.Mode.stepup;
    }

    public enum Mode {
        redstone,
        stepdown,
        stepup;

        static final TileTransformer.Mode[] VALUES = values();


    }

}
