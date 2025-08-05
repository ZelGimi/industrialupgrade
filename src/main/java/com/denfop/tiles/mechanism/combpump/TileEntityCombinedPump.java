package com.denfop.tiles.mechanism.combpump;

import com.denfop.Localization;
import com.denfop.api.gui.IType;
import com.denfop.api.inv.IAdvInventory;
import com.denfop.api.recipe.InvSlotOutput;
import com.denfop.api.sytem.EnergyType;
import com.denfop.api.tile.IMultiTileBlock;
import com.denfop.api.upgrades.IUpgradableBlock;
import com.denfop.api.upgrades.UpgradableProperty;
import com.denfop.audio.EnumSound;
import com.denfop.componets.ComponentBaseEnergy;
import com.denfop.componets.EnumTypeStyle;
import com.denfop.container.ContainerBase;
import com.denfop.container.ContainerCombPump;
import com.denfop.gui.GuiCompPump;
import com.denfop.gui.GuiCore;
import com.denfop.invslot.InvSlot;
import com.denfop.invslot.InvSlotFluid;
import com.denfop.invslot.InvSlotUpgrade;
import com.denfop.network.DecoderHandler;
import com.denfop.network.EncoderHandler;
import com.denfop.network.packet.CustomPacketBuffer;
import com.denfop.tiles.base.TileElectricLiquidTankInventory;
import com.denfop.utils.Keyboard;
import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.material.FluidState;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.api.distmarker.OnlyIn;
import net.neoforged.neoforge.fluids.FluidStack;
import net.neoforged.neoforge.fluids.capability.IFluidHandler;
import org.apache.commons.lang3.mutable.MutableObject;

import java.io.IOException;
import java.util.EnumSet;
import java.util.List;
import java.util.Set;

public class TileEntityCombinedPump extends TileElectricLiquidTankInventory implements IUpgradableBlock, IType {

    public final int defaultTier;
    public final double defaultEnergyStorage;
    public final double defaultEnergyConsume;
    public final int defaultOperationLength;
    public final InvSlotFluid containerSlot;
    public final InvSlotOutput outputSlot;
    public final InvSlotUpgrade upgradeSlot;
    public final ComponentBaseEnergy energyQe;
    public final EnumTypePump typePump;
    public double energyConsume;
    public int operationsPerTick;
    public short progress = 0;
    public int operationLength;
    public float guiProgress;
    public int x;
    public int z;
    public int y;
    boolean canWork = true;

    public TileEntityCombinedPump(int size, int operationLength, EnumTypePump typePump, IMultiTileBlock block, BlockPos pos, BlockState state) {
        super(0, 0, size, block, pos, state);
        this.containerSlot = new InvSlotFluid(
                this,
                InvSlot.TypeItemSlot.INPUT,
                1,
                InvSlotFluid.TypeFluidSlot.OUTPUT
        );
        this.typePump = typePump;
        this.energyQe = this.addComponent(ComponentBaseEnergy.asBasicSink(EnergyType.QUANTUM, this, 100));
        this.outputSlot = new InvSlotOutput(this, 1);
        this.upgradeSlot = new com.denfop.invslot.InvSlotUpgrade(this, 4);
        this.defaultEnergyConsume = this.energyConsume = (this.getStyle().ordinal() + 1);
        this.defaultOperationLength = this.operationLength = operationLength;
        this.defaultTier = 1;
        this.defaultEnergyStorage = this.operationLength;
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
        try {
            guiProgress = (float) DecoderHandler.decode(customPacketBuffer);
            x = customPacketBuffer.readInt();
            y = customPacketBuffer.readInt();
            z = customPacketBuffer.readInt();
            canWork = customPacketBuffer.readBoolean();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

    }

    @Override
    public ContainerCombPump getGuiContainer(final Player var1) {
        return new ContainerCombPump(var1, this);
    }

    @Override
    @OnlyIn(Dist.CLIENT)
    public GuiCore<ContainerBase<? extends IAdvInventory>> getGui(final Player var1, final ContainerBase<? extends IAdvInventory> var2) {
        return new GuiCompPump((ContainerCombPump) var2);
    }

    @Override
    public CustomPacketBuffer writeContainerPacket() {
        final CustomPacketBuffer packet = super.writeContainerPacket();
        try {
            EncoderHandler.encode(packet, guiProgress);
            packet.writeInt(this.x);
            packet.writeInt(this.y);
            packet.writeInt(this.z);
            packet.writeBoolean(this.canWork);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        return packet;
    }

    public void updateEntityServer() {
        super.updateEntityServer();
        if (canWork && this.energyQe.canUseEnergy((this.energyConsume))) {

            if (this.progress < this.operationLength) {
                ++this.progress;
                this.energyQe.useEnergy(energyConsume);
            } else {
                for (int i = 0; i < operationsPerTick; i++) {
                    if (x < this.getBlockPos().getX() + this.typePump.getXz()) {
                        x++;
                    } else if (z < this.getBlockPos().getZ() + this.typePump.getXz()) {
                        z++;
                        x = this.getBlockPos().getX();
                    } else if (y >= this.getBlockPos().getY() - this.typePump.getY()) {
                        z = this.getBlockPos().getZ();
                        x = this.getBlockPos().getX();
                        y--;
                    } else {
                        canWork = false;
                    }
                    if (this.operate(x, y, z)) {
                        this.progress = 0;
                    }
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


        this.guiProgress = (float) this.progress / (float) this.operationLength;
        if (this.upgradeSlot.tickNoMark()) {
            setUpgradestat();
        }


    }

    public boolean operate(int x, int y, int z) {
        if (this.fluidTank.getFluidAmount() >= this.fluidTank.getCapacity()) {
            return false;
        }
        FluidStack liquid;
        boolean canOperate = false;


        if (this.fluidTank.getFluidAmount() >= this.fluidTank.getCapacity()) {
            return false;
        }
        liquid = this.pump(new BlockPos(x, y, z
        ), false);
        if (this.getFluidTank().fill(liquid, IFluidHandler.FluidAction.SIMULATE) > 0) {
            this.getFluidTank().fill(liquid, IFluidHandler.FluidAction.EXECUTE);
            canOperate = true;


        }


        return canOperate;
    }

    public FluidStack pump(BlockPos pos, boolean sim) {
        FluidStack ret = FluidStack.EMPTY;
        int freespace = this.fluidTank.getCapacity() - this.fluidTank.getFluidAmount();

        if (freespace >= 1000) {
            BlockState block = this.getWorld().getBlockState(pos);
            if (block.liquid()) {
                FluidState fluidState = block.getFluidState();

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
        if (!level.isClientSide) {
            this.setUpgradestat();
            x = this.pos.getX();
            y = this.pos.getY() - 1;
            z = this.pos.getZ();
        }

    }

    public void setUpgradestat() {
        double previousProgress = (double) this.progress / (double) this.operationLength;
        double stackOpLen = ((double) this.defaultOperationLength + (double) this.upgradeSlot.extraProcessTime) * 64.0D * this.upgradeSlot.processTimeMultiplier;
        this.operationsPerTick = (int) Math.min(Math.ceil(64.0D / stackOpLen), 2.147483647E9D);
        this.operationLength = (int) Math.round(stackOpLen * (double) this.operationsPerTick / 64.0D);
        this.energyConsume = applyModifier(this.defaultEnergyConsume, this.upgradeSlot.extraEnergyDemand, 1);
        this.energyQe.setSinkTier(applyModifier(this.defaultTier, this.upgradeSlot.extraTier, 1.0D));
        this.energyQe.setCapacity(applyModifier(
                this.defaultEnergyStorage,
                this.upgradeSlot.extraEnergyStorage + this.operationLength * this.energyConsume,
                this.upgradeSlot.energyStorageMultiplier
        ));
        if (this.operationLength < 1) {
            this.operationLength = 1;
        }

        this.progress = (short) ((int) Math.floor(previousProgress * (double) this.operationLength + 0.1D));
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

    public double getEnergy() {
        return this.energyQe.getEnergy();
    }

    public boolean useEnergy(double amount) {
        if (this.energyQe.canUseEnergy(amount)) {
            this.energyQe.useEnergy(amount);
            return true;
        } else {
            return false;
        }
    }

    public void readFromNBT(CompoundTag nbttagcompound) {
        super.readFromNBT(nbttagcompound);
        this.progress = nbttagcompound.getShort("progress");
        this.x = nbttagcompound.getInt("x_pump");
        this.y = nbttagcompound.getInt("y_pump");
        this.z = nbttagcompound.getInt("z_pump");
        this.canWork = nbttagcompound.getBoolean("canWork");
    }

    public CompoundTag writeToNBT(CompoundTag nbttagcompound) {
        super.writeToNBT(nbttagcompound);
        nbttagcompound.putShort("progress", this.progress);
        nbttagcompound.putInt("x_pump", this.x);
        nbttagcompound.putInt("y_pump", this.y);
        nbttagcompound.putInt("z_pump", this.z);
        nbttagcompound.putBoolean("canWork", this.canWork);
        return nbttagcompound;
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
