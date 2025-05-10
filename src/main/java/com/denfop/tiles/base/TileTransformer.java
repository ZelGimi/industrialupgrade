package com.denfop.tiles.base;

import com.denfop.Localization;
import com.denfop.api.energy.EnergyNetGlobal;
import com.denfop.api.energy.ITransformer;
import com.denfop.api.energy.Mode;
import com.denfop.api.tile.IMultiTileBlock;
import com.denfop.componets.Energy;
import com.denfop.componets.Redstone;
import com.denfop.componets.RedstoneHandler;
import com.denfop.container.ContainerBase;
import com.denfop.container.ContainerTransformer;
import com.denfop.gui.GuiCore;
import com.denfop.gui.GuiTransformer;
import com.denfop.network.DecoderHandler;
import com.denfop.network.EncoderHandler;
import com.denfop.network.IUpdatableTileEvent;
import com.denfop.network.packet.CustomPacketBuffer;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

import java.io.IOException;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

public abstract class TileTransformer extends TileEntityInventory implements
        IUpdatableTileEvent, ITransformer {

    private static final Mode defaultMode;

    static {
        defaultMode = Mode.redstone;
    }

    protected final Energy energy;
    private final int defaultTier;
    private final Redstone redstone;
    private boolean hasRedstone = false;
    private double inputFlow = 0.0D;
    private double outputFlow = 0.0D;
    private Mode configuredMode;
    private Mode transformMode;

    public TileTransformer(int tier, IMultiTileBlock tileBlock, BlockPos pos, BlockState state) {
        super(tileBlock,pos,state);
        this.configuredMode = defaultMode;
        this.transformMode = null;
        this.defaultTier = tier;
        this.energy = this.addComponent(new Energy(
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

    public void readFromNBT(CompoundTag nbt) {
        super.readFromNBT(nbt);
        int mode = nbt.getInt("mode");
        if (mode >= 0 && mode < Mode.values().length) {
            this.configuredMode = Mode.values()[mode];
        } else {
            this.configuredMode = defaultMode;
        }

    }

    public CompoundTag writeToNBT(CompoundTag nbt) {
        super.writeToNBT(nbt);
        nbt.putInt("mode", this.configuredMode.ordinal());
        return nbt;
    }

    public void onLoaded() {
        super.onLoaded();
        if (!this.getWorld().isClientSide) {
            this.updateRedstone(true);
        }

    }

    public Mode getMode() {
        return this.configuredMode;
    }

    public void updateTileServer(Player player, double event) {
        if (event >= 0 && event < Mode.values().length) {
            this.configuredMode = Mode.values()[(int) event];
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
        assert !this.getWorld().isClientSide;

        Mode newMode;
        switch (this.configuredMode) {
            case redstone:
                newMode = this.hasRedstone
                        ? Mode.stepup
                        : Mode.stepdown;
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
                        Arrays
                                .asList(Direction.values())
                                .stream()
                                .filter(facing -> facing != this.getFacing())
                                .collect(Collectors.toList()),
                        Collections.singletonList(this.getFacing())

                );
            } else {
                this.energy.setSourceTier(this.defaultTier);
                this.energy.setSinkTier(this.defaultTier + 1);
                this.energy.setPacketOutput(4);
                this.energy.setDirections(
                        Collections.singletonList(this.getFacing()),
                        Arrays.stream(Direction.values())
                                .filter(facing -> facing != this.getFacing())
                                .collect(Collectors.toList())
                );

            }

            this.outputFlow = EnergyNetGlobal.instance.getPowerFromTier(this.energy.getSourceTier());
            this.inputFlow = EnergyNetGlobal.instance.getPowerFromTier(this.energy.getSinkTier());
        }

    }

    public void setFacing(Direction facing) {
        super.setFacing(facing);
        if (!this.getWorld().isClientSide) {
            this.updateRedstone(true);
        }

    }

    public void addInformation(ItemStack stack, List<String> tooltip) {
        super.addInformation(stack, tooltip);
        tooltip.add(Localization.translate("iu.item.tooltip.High") + " " + +(int) EnergyNetGlobal.instance.getPowerFromTier(this.energy.getSinkTier()) + " "
                + Localization.translate("iu.generic.text.EUt") + " " + Localization.translate("iu.item.tooltip.Low") + " " +
                +(int) EnergyNetGlobal.instance.getPowerFromTier(this.energy.getSourceTier()) + " " + Localization.translate(
                "iu" +
                        ".generic.text.EUt"));

    }

    public ContainerTransformer getGuiContainer(Player player) {
        return new ContainerTransformer(player, this, 202);
    }

    @OnlyIn(Dist.CLIENT)
    public GuiCore<ContainerBase<?>> getGui(Player player, ContainerBase<?> isAdmin) {
        return new GuiTransformer((ContainerTransformer) isAdmin);
    }



    public double getinputflow() {
        return !this.isStepUp() ? this.inputFlow : this.outputFlow;
    }

    public double getoutputflow() {
        return this.isStepUp() ? this.inputFlow : this.outputFlow;
    }

    public boolean isStepUp() {
        return this.transformMode == Mode.stepup;
    }



}
