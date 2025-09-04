package com.denfop.blockentity.mechanism.solardestiller;

import com.denfop.api.blockentity.MultiBlockEntity;
import com.denfop.api.container.CustomWorldContainer;
import com.denfop.api.recipe.InventoryOutput;
import com.denfop.api.upgrades.IUpgradableBlock;
import com.denfop.api.upgrades.UpgradableProperty;
import com.denfop.api.widget.IType;
import com.denfop.blockentity.base.BlockEntityInventory;
import com.denfop.blocks.FluidName;
import com.denfop.componets.EnumTypeStyle;
import com.denfop.componets.Fluids;
import com.denfop.containermenu.ContainerMenuBase;
import com.denfop.containermenu.ContainerMenuSolarDestiller;
import com.denfop.inventory.*;
import com.denfop.network.DecoderHandler;
import com.denfop.network.EncoderHandler;
import com.denfop.network.packet.CustomPacketBuffer;
import com.denfop.screen.ScreenIndustrialUpgrade;
import com.denfop.screen.ScreenSolarDestiller;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Holder;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.biome.Biome;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.material.MapColor;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.api.distmarker.OnlyIn;
import net.neoforged.neoforge.common.Tags;
import net.neoforged.neoforge.fluids.FluidStack;
import net.neoforged.neoforge.fluids.capability.IFluidHandler;
import net.neoforged.neoforge.fluids.capability.templates.FluidTank;

import java.io.IOException;
import java.util.EnumSet;
import java.util.Set;

public class BlockEntityBaseSolarDestiller extends BlockEntityInventory implements IUpgradableBlock, IType {

    public final FluidTank inputTank;
    public final FluidTank outputTank;
    public final InventoryOutput wateroutputSlot;
    public final InventoryOutput destiwateroutputSlott;
    public final InventoryFluidByList waterinputSlot;
    public final InventoryTank destiwaterinputSlot;
    public final InventoryUpgrade upgradeSlot;
    protected final Fluids fluids = this.addComponent(new Fluids(this));
    private final EnumTypeStyle style;
    private int tickrate;
    private boolean skyLight;
    private boolean hasSky;

    public BlockEntityBaseSolarDestiller(EnumTypeStyle style, MultiBlockEntity block, BlockPos pos, BlockState state) {
        super(block, pos, state);
        this.style = style;
        this.inputTank = this.fluids.addTankInsert("inputTank", 10000, Fluids.fluidPredicate(net.minecraft.world.level.material.Fluids.WATER));
        this.outputTank = this.fluids.addTankExtract(
                "outputTank",
                10000,
                Fluids.fluidPredicate(FluidName.fluiddistilled_water.getInstance().get())
        );
        this.waterinputSlot = new InventoryFluidByList(
                this,
                Inventory.TypeItemSlot.INPUT,
                1,
                InventoryFluid.TypeFluidSlot.INPUT,
                net.minecraft.world.level.material.Fluids.WATER
        );
        this.destiwaterinputSlot = new InventoryTank(
                this,
                Inventory.TypeItemSlot.INPUT,
                1,
                InventoryFluid.TypeFluidSlot.OUTPUT,
                this.outputTank
        );
        this.wateroutputSlot = new InventoryOutput(this, 1);
        this.destiwateroutputSlott = new InventoryOutput(this, 1);
        this.upgradeSlot = new InventoryUpgrade(this, 3);
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
                        MapColor.NONE) && this.hasSky && this.level.isDay();
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


    public ContainerMenuSolarDestiller getGuiContainer(Player player) {
        return new ContainerMenuSolarDestiller(player, this);
    }

    @OnlyIn(Dist.CLIENT)
    public ScreenIndustrialUpgrade<ContainerMenuBase<? extends CustomWorldContainer>> getGui(Player player, ContainerMenuBase<? extends CustomWorldContainer> isAdmin) {
        return new ScreenSolarDestiller((ContainerMenuSolarDestiller) isAdmin);
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
