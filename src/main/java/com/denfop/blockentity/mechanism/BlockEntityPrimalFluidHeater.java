package com.denfop.blockentity.mechanism;

import com.denfop.IUItem;
import com.denfop.api.blockentity.MultiBlockEntity;
import com.denfop.api.recipe.FluidHandlerRecipe;
import com.denfop.api.recipe.IHasRecipe;
import com.denfop.api.upgrades.BlockEntityUpgrade;
import com.denfop.api.upgrades.EnumBlockEntityUpgrade;
import com.denfop.blockentity.base.BlockEntityElectricMachine;
import com.denfop.blocks.BlockTileEntity;
import com.denfop.blocks.mechanism.BlockPrimalFluidHeaterEntity;
import com.denfop.componets.Fluids;
import com.denfop.componets.HeatComponent;
import com.denfop.inventory.Inventory;
import com.denfop.network.DecoderHandler;
import com.denfop.network.EncoderHandler;
import com.denfop.network.packet.CustomPacketBuffer;
import com.denfop.network.packet.PacketUpdateFieldTile;
import com.denfop.utils.Localization;
import com.denfop.utils.ModUtils;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.material.Material;
import net.minecraft.world.phys.Vec3;
import net.minecraftforge.common.capabilities.ForgeCapabilities;
import net.minecraftforge.fluids.capability.IFluidHandlerItem;
import net.minecraftforge.fluids.capability.templates.FluidTank;

import java.io.IOException;
import java.util.EnumSet;
import java.util.List;
import java.util.Set;

public class BlockEntityPrimalFluidHeater extends BlockEntityElectricMachine implements BlockEntityUpgrade, IHasRecipe {

    public final FluidHandlerRecipe fluid_handler;
    public final Fluids.InternalFluidTank fluidTank2;
    public final Fluids.InternalFluidTank fluidTank1;
    public final int defaultOperationLength;
    public final HeatComponent heat;
    private final Fluids fluids;
    public double energyConsume;
    public int operationLength;
    public int operationsPerTick;
    protected short progress;
    protected double guiProgress;
    boolean work;
    private int prevAmount;
    private int prevAmount1;

    public BlockEntityPrimalFluidHeater(BlockPos pos, BlockState state) {
        super(0, 0, 0, BlockPrimalFluidHeaterEntity.primal_fluid_heater, pos, state);
        this.progress = 0;
        this.defaultOperationLength = this.operationLength = 200;

        this.fluids = this.addComponent(new Fluids(this));
        this.fluidTank1 = fluids.addTank("fluidTank1", 12 * 1000, Inventory.TypeItemSlot.INPUT);


        this.fluidTank2 = fluids.addTank("fluidTank2", 12 * 1000, Inventory.TypeItemSlot.OUTPUT);


        this.fluid_handler = new FluidHandlerRecipe("heat", fluids);
        this.fluidTank1.setAcceptedFluids(Fluids.fluidPredicate(this.fluid_handler.getFluids(0)));
        this.fluidTank2.setAcceptedFluids(Fluids.fluidPredicate(this.fluid_handler.getOutputFluids(0)));
        this.heat = this.addComponent(HeatComponent.asBasicSink(this, 1000));

    }

    public static int applyModifier(int base, int extra, double multiplier) {
        double ret = Math.round((base + extra) * multiplier);
        return (ret > 2.147483647E9D) ? Integer.MAX_VALUE : (int) ret;
    }


    @Override
    public void init() {

    }

    public void readFromNBT(CompoundTag nbttagcompound) {
        super.readFromNBT(nbttagcompound);
        this.progress = nbttagcompound.getShort("progress");

    }

    @Override
    public void readPacket(final CustomPacketBuffer customPacketBuffer) {
        super.readPacket(customPacketBuffer);
        try {
            FluidTank fluidTank1 = (FluidTank) DecoderHandler.decode(customPacketBuffer);
            if (fluidTank1 != null) {
                this.fluidTank1.readFromNBT(fluidTank1.writeToNBT(new CompoundTag()));
            }
            FluidTank fluidTank2 = (FluidTank) DecoderHandler.decode(customPacketBuffer);
            if (fluidTank2 != null) {
                this.fluidTank2.readFromNBT(fluidTank2.writeToNBT(new CompoundTag()));
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void updateField(final String name, final CustomPacketBuffer is) {
        super.updateField(name, is);
        if (name.equals("fluidtank")) {
            try {
                FluidTank fluidTank1 = (FluidTank) DecoderHandler.decode(is);
                if (fluidTank1 != null) {
                    this.fluidTank1.readFromNBT(fluidTank1.writeToNBT(new CompoundTag()));
                }
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }
        if (name.equals("guiProgress")) {
            try {
                guiProgress = (double) DecoderHandler.decode(is);

            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }
        if (name.equals("fluidtank1")) {
            try {
                FluidTank fluidTank2 = (FluidTank) DecoderHandler.decode(is);
                if (fluidTank2 != null) {
                    this.fluidTank2.readFromNBT(fluidTank2.writeToNBT(new CompoundTag()));
                }
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }
    }

    @Override
    public CustomPacketBuffer writePacket() {
        CustomPacketBuffer customPacketBuffer = super.writePacket();
        try {
            EncoderHandler.encode(customPacketBuffer, fluidTank1);
            EncoderHandler.encode(customPacketBuffer, fluidTank2);
            ;
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        return customPacketBuffer;
    }

    @Override
    public void addInformation(final ItemStack stack, final List<String> tooltip) {
        for (int i = 1; i < 5; i++) {
            tooltip.add(Localization.translate("fluid_heater.info" + i));
        }
    }

    public CompoundTag writeToNBT(CompoundTag nbttagcompound) {
        super.writeToNBT(nbttagcompound);
        nbttagcompound.putShort("progress", this.progress);
        return nbttagcompound;
    }

    @Override
    public void readContainerPacket(final CustomPacketBuffer customPacketBuffer) {
        super.readContainerPacket(customPacketBuffer);
        try {
            guiProgress = (double) DecoderHandler.decode(customPacketBuffer);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

    }

    @Override
    public void onNeighborChange(final BlockState neighbor, final BlockPos neighborPos) {
        super.onNeighborChange(neighbor, neighborPos);
        if (work) {
            if (this.pos.below().distSqr(neighborPos) == 0) {
                BlockState blockState = level.getBlockState(this.pos.below());
                if (blockState.getMaterial() != Material.AIR) {
                    this.work = blockState.getBlock() == Blocks.LAVA || blockState.getBlock() == Blocks.LAVA_CAULDRON;
                } else {
                    work = false;
                }
            }
        } else {
            if (this.pos.below().distSqr(neighborPos) == 0) {
                BlockState blockState = level.getBlockState(this.pos.below());
                if (blockState.getMaterial() != Material.AIR) {
                    this.work = blockState.getBlock() == Blocks.LAVA || blockState.getBlock() == Blocks.LAVA_CAULDRON;
                } else {
                    work = false;
                }
            }
        }
    }

    @Override
    public CustomPacketBuffer writeContainerPacket() {
        final CustomPacketBuffer packet = super.writeContainerPacket();
        try {
            EncoderHandler.encode(packet, guiProgress);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        return packet;
    }

    public double getProgress() {
        return this.guiProgress;
    }

    public void onLoaded() {
        super.onLoaded();
        if (!this.level.isClientSide) {
            setOverclockRates();
            this.fluid_handler.load();
            BlockState blockState = level.getBlockState(this.pos.below());
            if (blockState.getMaterial() != Material.AIR) {
                this.work = blockState.getBlock() == Blocks.LAVA || blockState.getBlock() == Blocks.LAVA_CAULDRON;
            } else {
                work = false;
            }
        }
    }

    public void onUnloaded() {
        super.onUnloaded();

    }

    @Override
    public boolean onActivated(Player player, InteractionHand hand, Direction side, Vec3 vec3) {
        if (!this.getWorld().isClientSide && player
                .getItemInHand(hand)
                .getCapability(
                        ForgeCapabilities.FLUID_HANDLER_ITEM,
                        null
                ).orElse((IFluidHandlerItem) player
                        .getItemInHand(hand).getItem().initCapabilities(player
                                .getItemInHand(hand), player
                                .getItemInHand(hand).getTag())) != null && this.fluidTank1.getFluidAmount() + 1000 <= this.fluidTank1.getCapacity()) {


            return ModUtils.interactWithFluidHandler(player, hand,
                    fluids.getCapability(ForgeCapabilities.FLUID_HANDLER, side)
            );
        } else {

            return super.onActivated(player, hand, side, vec3);
        }
    }


    public void updateEntityServer() {
        super.updateEntityServer();

        if (this.work) {
            this.heat.addEnergy(1);
        }
        if (this.fluid_handler.output() == null && this.fluidTank1.getFluidAmount() >= 1) {
            this.fluid_handler.getOutput();
        } else {
            if (this.fluid_handler.output() != null && !this.fluid_handler.checkFluids()) {
                this.fluid_handler.setOutput(null);
            }
        }
        if (this.prevAmount != this.fluidTank1.getFluidAmount()) {
            this.prevAmount = this.fluidTank1.getFluidAmount();
            new PacketUpdateFieldTile(this, "fluidtank", this.fluidTank1);
        }
        if (this.prevAmount1 != this.fluidTank2.getFluidAmount()) {
            this.prevAmount1 = this.fluidTank2.getFluidAmount();
            new PacketUpdateFieldTile(this, "fluidtank1", this.fluidTank2);
        }
        if (this.fluid_handler.output() != null && this.heat.getEnergy() > 500 && this.fluid_handler.canOperate() && this.fluid_handler.canFillFluid()) {

            if (!this.getActive()) {
                setActive(true);
            }
            if (this.progress == 0) {
                initiate(0);
            }
            this.progress = (short) (this.progress + 1);
            double k = this.progress;

            this.guiProgress = (k / this.operationLength);
            if (this.progress >= this.operationLength) {
                this.guiProgress = 0;
                operate();
                this.progress = 0;
                initiate(2);
            }
            if (this.level.getGameTime() % 20 == 0) {
                new PacketUpdateFieldTile(this, "guiProgress", this.guiProgress);
            }
        } else {
            if (this.progress != 0 && getActive()) {
                initiate(0);
                this.progress = 0;

            }
            if (this.fluid_handler.output() == null) {
                this.progress = 0;
            }
            if (this.getActive()) {
                setActive(false);
            }
        }
        if (this.level.getGameTime() % 20 == 0) {
            this.heat.useEnergy(1);
        }


    }

    public void setOverclockRates() {

        if (this.operationLength < 1) {
            this.operationLength = 1;
        }
    }

    public void operate() {
        for (int i = 0; i < 1; i++) {
            operateOnce();

            this.fluid_handler.checkOutput();
            if (this.fluid_handler.output() == null) {
                break;
            }
        }
    }

    public void operateOnce() {
        this.fluid_handler.consume();
        this.fluid_handler.fillFluid();
    }


    public MultiBlockEntity getTeBlock() {
        return BlockPrimalFluidHeaterEntity.primal_fluid_heater;
    }

    public BlockTileEntity getBlock() {
        return IUItem.primalFluidHeater.getBlock();
    }


    public Set<EnumBlockEntityUpgrade> getUpgradableProperties() {
        return EnumSet.of(EnumBlockEntityUpgrade.Processing, EnumBlockEntityUpgrade.Transformer,
                EnumBlockEntityUpgrade.EnergyStorage, EnumBlockEntityUpgrade.FluidExtract,
                EnumBlockEntityUpgrade.FluidInput
        );
    }


}
