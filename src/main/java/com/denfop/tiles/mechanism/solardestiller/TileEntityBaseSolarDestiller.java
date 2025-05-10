package com.denfop.tiles.mechanism.solardestiller;

import com.denfop.api.gui.IType;
import com.denfop.api.inv.IAdvInventory;
import com.denfop.api.recipe.InvSlotOutput;
import com.denfop.api.tile.IMultiTileBlock;
import com.denfop.api.upgrades.IUpgradableBlock;
import com.denfop.api.upgrades.UpgradableProperty;
import com.denfop.blocks.FluidName;
import com.denfop.componets.EnumTypeStyle;
import com.denfop.componets.Fluids;
import com.denfop.container.ContainerBase;
import com.denfop.container.ContainerSolarDestiller;
import com.denfop.gui.GuiCore;
import com.denfop.gui.GuiSolarDestiller;
import com.denfop.invslot.*;
import com.denfop.network.DecoderHandler;
import com.denfop.network.EncoderHandler;
import com.denfop.network.packet.CustomPacketBuffer;
import com.denfop.tiles.base.TileEntityInventory;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Holder;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.biome.Biome;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.material.MaterialColor;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.common.Tags;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.fluids.capability.IFluidHandler;
import net.minecraftforge.fluids.capability.templates.FluidTank;

import java.io.IOException;
import java.util.EnumSet;
import java.util.Set;

public class TileEntityBaseSolarDestiller extends TileEntityInventory implements IUpgradableBlock, IType {

    public final FluidTank inputTank;
    public final FluidTank outputTank;
    public final InvSlotOutput wateroutputSlot;
    public final InvSlotOutput destiwateroutputSlott;
    public final InvSlotFluidByList waterinputSlot;
    public final InvSlotTank destiwaterinputSlot;
    public final InvSlotUpgrade upgradeSlot;
    protected final Fluids fluids = this.addComponent(new Fluids(this));
    private final EnumTypeStyle style;
    private int tickrate;
    private boolean skyLight;
    private boolean hasSky;

    public TileEntityBaseSolarDestiller(EnumTypeStyle style, IMultiTileBlock block, BlockPos pos, BlockState state) {
        super(block, pos, state);
        this.style = style;
        this.inputTank = this.fluids.addTankInsert("inputTank", 10000, Fluids.fluidPredicate(net.minecraft.world.level.material.Fluids.WATER));
        this.outputTank = this.fluids.addTankExtract(
                "outputTank",
                10000,
                Fluids.fluidPredicate(FluidName.fluiddistilled_water.getInstance().get())
        );
        this.waterinputSlot = new InvSlotFluidByList(
                this,
                InvSlot.TypeItemSlot.INPUT,
                1,
                InvSlotFluid.TypeFluidSlot.INPUT,
                net.minecraft.world.level.material.Fluids.WATER
        );
        this.destiwaterinputSlot = new InvSlotTank(
                this,
                InvSlot.TypeItemSlot.INPUT,
                1,
                InvSlotFluid.TypeFluidSlot.OUTPUT,
                this.outputTank
        );
        this.wateroutputSlot = new InvSlotOutput(this, 1);
        this.destiwateroutputSlott = new InvSlotOutput(this, 1);
        this.upgradeSlot = new InvSlotUpgrade(this, 3);
    }

    public void onLoaded() {
        super.onLoaded();
        this.tickrate = this.getTickRate();
        this.hasSky = !(this.getLevel().dimension() == Level.NETHER);
        updateVisibility();
    }

    public void updateVisibility() {
        this.skyLight = this.level.canSeeSky(this.pos.above()) &&
                (this.level.getBlockState(this.pos.above()).getMapColor(this.level, this.pos.above()) ==
                        MaterialColor.NONE) && this.hasSky && this.level.isDay();
    }

    @Override
    public void readContainerPacket(final CustomPacketBuffer customPacketBuffer) {
        super.readContainerPacket(customPacketBuffer);
        try {
            skyLight = (boolean) DecoderHandler.decode(customPacketBuffer);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

    }

    @Override
    public CustomPacketBuffer writeContainerPacket() {
        final CustomPacketBuffer packet = super.writeContainerPacket();
        try {
            EncoderHandler.encode(packet, skyLight);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        return packet;
    }

    public void updateEntityServer() {
        super.updateEntityServer();
        this.waterinputSlot.processIntoTank(this.inputTank, this.wateroutputSlot);
        if (this.getWorld().getGameTime() % 40 == 0) {
            updateVisibility();

        }
        this.destiwaterinputSlot.processFromTank(this.outputTank, this.destiwateroutputSlott);
        if (!this.skyLight) {
            return;
        }
        if (this.getWorld().getGameTime() % this.tickrate == 0) {

            if (this.canWork()) {
                this.inputTank.drain(getAmountWater(), IFluidHandler.FluidAction.EXECUTE);
                this.outputTank.fill(new FluidStack(FluidName.fluiddistilled_water.getInstance().get(), 1), IFluidHandler.FluidAction.EXECUTE);
            }


        }


        this.upgradeSlot.tickNoMark();
    }


    public ContainerSolarDestiller getGuiContainer(Player player) {
        return new ContainerSolarDestiller(player, this);
    }

    @OnlyIn(Dist.CLIENT)
    public GuiCore<ContainerBase<? extends IAdvInventory>> getGui(Player player, ContainerBase<? extends IAdvInventory> isAdmin) {
        return new GuiSolarDestiller((ContainerSolarDestiller) isAdmin);
    }

    public int getAmountWater() {
        switch (style) {
            case DEFAULT:
                return 4;
            case ADVANCED:
                return 4;
            case IMPROVED:
                return 3;
            case PERFECT:
                return 2;
            case PHOTONIC:
                return 1;
        }
        return 4;
    }

    public int getTickRate() {
        Holder<Biome> biome = level.getBiome(pos);

        if (biome.is(Tags.Biomes.IS_HOT)) {
            switch (style) {
                case DEFAULT:
                    return 36;
                case ADVANCED:
                    return 20;
                case IMPROVED:
                    return 11;
                case PERFECT:
                    return 6;
                case PHOTONIC:
                    return 4;
            }
        } else {
            boolean isCold = biome.is(Tags.Biomes.IS_COLD);
            switch (style) {
                case DEFAULT:
                    return isCold ? 144 : 72;
                case ADVANCED:
                    return isCold ? 80 : 40;
                case IMPROVED:
                    return isCold ? 44 : 22;
                case PERFECT:
                    return isCold ? 24 : 12;
                case PHOTONIC:
                    return isCold ? 16 : 8;
            }
        }
        return 0;
    }

    public int gaugeLiquidScaled(int i, int tank) {
        switch (tank) {
            case 0:
                if (this.inputTank.getFluidAmount() <= 0) {
                    return 0;
                }

                return this.inputTank.getFluidAmount() * i / this.inputTank.getCapacity();
            case 1:
                if (this.outputTank.getFluidAmount() <= 0) {
                    return 0;
                }

                return this.outputTank.getFluidAmount() * i / this.outputTank.getCapacity();
            default:
                return 0;
        }
    }

    public boolean canWork() {
        return this.inputTank.getFluidAmount() >= getAmountWater() && this.outputTank.getFluidAmount() < this.outputTank.getCapacity() && this.skyLight;
    }

    public Set<UpgradableProperty> getUpgradableProperties() {
        return EnumSet.of(UpgradableProperty.FluidInput
                , UpgradableProperty.FluidExtract);
    }

    @Override
    public EnumTypeStyle getStyle() {
        return style;
    }

}
