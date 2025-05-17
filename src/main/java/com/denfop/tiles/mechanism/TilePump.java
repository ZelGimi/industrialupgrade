package com.denfop.tiles.mechanism;

import com.denfop.Localization;
import com.denfop.api.gui.IType;
import com.denfop.api.inv.IAdvInventory;
import com.denfop.api.recipe.InvSlotOutput;
import com.denfop.api.tile.IMultiTileBlock;
import com.denfop.api.upgrades.IUpgradableBlock;
import com.denfop.api.upgrades.UpgradableProperty;
import com.denfop.audio.EnumSound;
import com.denfop.componets.ComponentProgress;
import com.denfop.componets.EnumTypeStyle;
import com.denfop.container.ContainerBase;
import com.denfop.container.ContainerPump;
import com.denfop.gui.GuiCore;
import com.denfop.gui.GuiPump;
import com.denfop.invslot.InvSlot;
import com.denfop.invslot.InvSlotFluid;
import com.denfop.invslot.InvSlotUpgrade;
import com.denfop.network.packet.CustomPacketBuffer;
import com.denfop.tiles.base.TileElectricLiquidTankInventory;
import com.denfop.utils.Keyboard;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.material.FluidState;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.fluids.capability.IFluidHandler;
import org.apache.commons.lang3.mutable.MutableObject;

import java.util.EnumSet;
import java.util.List;
import java.util.Set;

public class TilePump extends TileElectricLiquidTankInventory implements IUpgradableBlock, IType {

    public final int defaultTier;
    public final double defaultEnergyStorage;
    public final double defaultEnergyConsume;
    public final int defaultOperationLength;
    public final InvSlotFluid containerSlot;
    public final InvSlotOutput outputSlot;
    public final InvSlotUpgrade upgradeSlot;
    public double energyConsume;
    public int operationsPerTick;
    public int operationLength;
    public ComponentProgress componentProgress;

    public TilePump(int size, int operationLength, IMultiTileBlock block, BlockPos pos, BlockState state) {
        super(20, 1, size, block, pos, state);
        this.containerSlot = new InvSlotFluid(
                this,
                InvSlot.TypeItemSlot.INPUT,
                1,
                InvSlotFluid.TypeFluidSlot.OUTPUT
        );
        this.outputSlot = new InvSlotOutput(this, 1);
        this.upgradeSlot = new com.denfop.invslot.InvSlotUpgrade(this, 4);
        this.defaultEnergyConsume = this.energyConsume = 1;
        this.defaultOperationLength = this.operationLength = operationLength;
        this.defaultTier = 1;
        this.defaultEnergyStorage = this.operationLength;
        componentProgress = this.addComponent(new ComponentProgress(this, 1, (short) operationLength));

        this.fluidTank.setTypeItemSlot(InvSlot.TypeItemSlot.OUTPUT);
    }

    private static int applyModifier(int base, int extra, double multiplier) {
        double ret = (double) Math.round(((double) base + (double) extra) * multiplier);
        return ret > 2.147483647E9D ? 2147483647 : (int) ret;
    }

    private static double applyModifier(double base, double extra, double multiplier) {
        return (double) Math.round((base + extra) * multiplier);
    }

    @Override
    public void readContainerPacket(final CustomPacketBuffer customPacketBuffer) {
        super.readContainerPacket(customPacketBuffer);


    }

    @Override
    public CustomPacketBuffer writeContainerPacket() {
        final CustomPacketBuffer packet = super.writeContainerPacket();

        return packet;
    }

    public void addInformation(ItemStack stack, List<String> tooltip) {
        if (!Keyboard.isKeyDown(Keyboard.KEY_LSHIFT)) {
            tooltip.add(Localization.translate("press.lshift"));
        }
        if (Keyboard.isKeyDown(Keyboard.KEY_LSHIFT)) {
            tooltip.add(Localization.translate("iu.machines_work_energy") + this.defaultEnergyConsume + Localization.translate(
                    "iu.machines_work_energy_type_eu"));
            tooltip.add(Localization.translate("iu.machines_work_length") + this.defaultOperationLength);
        }
        super.addInformation(stack, tooltip);

    }


    public void updateEntityServer() {
        super.updateEntityServer();
        if (this.energy.canUseEnergy((this.energyConsume))) {

            if (componentProgress.getProgress() < componentProgress.getMaxValue()) {
                componentProgress.addProgress(0);
                this.energy.useEnergy(energyConsume);
            } else {
                if (this.canoperate()) {
                    componentProgress.setProgress((short) 0);
                }
            }
        }

        MutableObject<ItemStack> output = new MutableObject<>();
        if (this.containerSlot.transferFromTank(
                this.fluidTank,
                output,
                true
        ) && (output.getValue() == null || this.outputSlot.canAdd(output.getValue()))) {
            this.containerSlot.transferFromTank(this.fluidTank, output, false);
            if (output.getValue() != null) {
                this.outputSlot.add(output.getValue());
            }
        }


        if (this.upgradeSlot.tickNoMark()) {
            setUpgradestat();
        }


    }


    public boolean canoperate() {
        return this.operate(true);
    }

    public boolean operate(boolean sim) {
        if (this.fluidTank.getFluidAmount() >= this.fluidTank.getCapacity()) {
            return false;
        }
        FluidStack liquid;
        boolean canOperate = false;
        for (int i = this.pos.getX() - 5; i <= this.pos.getX() + 5; i++) {
            for (int j = this.pos.getZ() - 5; j <= this.pos.getZ() + 5; j++) {
                for (int k = this.pos.getY() - 5; k <= this.pos.getY() + 5; k++) {
                    for (Direction dir : Direction.values()) {
                        if (this.fluidTank.getFluidAmount() >= this.fluidTank.getCapacity()) {
                            return false;
                        }
                        liquid = this.pump(new BlockPos(i + dir.getStepX(), k + dir.getStepY(),
                                j + dir.getStepZ()
                        ), false);
                        if (this.getFluidTank().fill(liquid, IFluidHandler.FluidAction.SIMULATE) > 0) {
                            this.getFluidTank().fill(liquid, IFluidHandler.FluidAction.EXECUTE);
                            canOperate = true;
                        }
                    }
                }
            }
        }


        return canOperate;
    }

    public FluidStack pump(BlockPos pos, boolean sim) {
        FluidStack ret = FluidStack.EMPTY;
        int freespace = this.fluidTank.getCapacity() - this.fluidTank.getFluidAmount();

        if (freespace >= 1000) {
            BlockState block = this.getWorld().getBlockState(pos);
            if (block.getMaterial().isLiquid()) {
                FluidState fluidState = block.getBlock().getFluidState(block);

                if (!fluidState.isSource()) {
                    return FluidStack.EMPTY;
                }

                ret = new FluidStack(fluidState.getType(), 1000);
                if (this.fluidTank.getFluid().isEmpty() || this.fluidTank
                        .getFluid()
                        .getFluid() == ret.getFluid()) {
                    if (!sim) {
                        this.getWorld().setBlock(pos, Blocks.AIR.defaultBlockState(), 3);
                    }
                }
            }
        }
        return ret;
    }


    public void onLoaded() {
        super.onLoaded();
        if (!getWorld().isClientSide) {
            this.setUpgradestat();
        }

    }


    public void setUpgradestat() {
        double previousProgress = componentProgress.getBar();
        double stackOpLen = ((double) this.defaultOperationLength + (double) this.upgradeSlot.extraProcessTime) * 64.0D * this.upgradeSlot.processTimeMultiplier;
        this.operationsPerTick = (int) Math.min(Math.ceil(64.0D / stackOpLen), 2.147483647E9D);
        this.operationLength = (int) Math.round(stackOpLen * (double) this.operationsPerTick / 64.0D);
        this.energyConsume = applyModifier(this.defaultEnergyConsume, this.upgradeSlot.extraEnergyDemand, 1);
        this.energy.setSinkTier(applyModifier(this.defaultTier, this.upgradeSlot.extraTier, 1.0D));
        this.energy.setCapacity(applyModifier(
                this.defaultEnergyStorage,
                this.upgradeSlot.extraEnergyStorage + this.operationLength * this.energyConsume,
                this.upgradeSlot.energyStorageMultiplier
        ));
        if (this.operationLength < 1) {
            this.operationLength = 1;
        }
        componentProgress.setMaxValue((short) operationLength);
        componentProgress.setProgress((short) ((int) Math.floor(previousProgress * (double) this.operationLength + 0.1D)));

    }

    public double getEnergy() {
        return this.energy.getEnergy();
    }

    public boolean useEnergy(double amount) {
        if (this.energy.canUseEnergy(amount)) {
            this.energy.useEnergy(amount);
            return true;
        } else {
            return false;
        }
    }


    public ContainerPump getGuiContainer(Player entityPlayer) {
        return new ContainerPump(entityPlayer, this);
    }

    @OnlyIn(Dist.CLIENT)
    public GuiCore<ContainerBase<? extends IAdvInventory>> getGui(Player entityPlayer, ContainerBase<? extends IAdvInventory> isAdmin) {
        return new GuiPump((ContainerPump) isAdmin);
    }


    public Set<UpgradableProperty> getUpgradableProperties() {
        return EnumSet.of(
                UpgradableProperty.Processing,
                UpgradableProperty.Transformer,
                UpgradableProperty.EnergyStorage,
                UpgradableProperty.FluidExtract
        );
    }


    @Override
    public EnumTypeStyle getStyle() {
        return null;
    }

    @Override
    public SoundEvent getSound() {
        return EnumSound.PumpOp.getSoundEvent();
    }

}
