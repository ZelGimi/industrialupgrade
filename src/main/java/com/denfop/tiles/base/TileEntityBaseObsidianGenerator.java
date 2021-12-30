package com.denfop.tiles.base;

import com.denfop.IUCore;
import com.denfop.audio.AudioSource;
import com.denfop.blocks.FluidName;
import com.denfop.container.ContainerObsidianGenerator;
import com.denfop.invslot.InvSlotObsidianGenerator;
import ic2.api.network.INetworkTileEntityEventListener;
import ic2.api.recipe.RecipeOutput;
import ic2.api.upgrade.IUpgradableBlock;
import ic2.api.upgrade.IUpgradeItem;
import ic2.core.ContainerBase;
import ic2.core.IC2;
import ic2.core.IHasGui;
import ic2.core.block.comp.Fluids;
import ic2.core.block.invslot.InvSlotConsumableLiquidByList;
import ic2.core.block.invslot.InvSlotOutput;
import ic2.core.block.invslot.InvSlotUpgrade;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraftforge.fluids.Fluid;
import net.minecraftforge.fluids.FluidRegistry;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.fluids.FluidTank;
import net.minecraftforge.fluids.capability.IFluidHandler;
import org.apache.commons.lang3.mutable.MutableObject;

import java.util.List;

public abstract class TileEntityBaseObsidianGenerator extends TileEntityElectricMachine
        implements IHasGui, INetworkTileEntityEventListener, IUpgradableBlock, IFluidHandler {

    public final InvSlotOutput outputSlot1;
    public final InvSlotConsumableLiquidByList fluidSlot1;
    public final InvSlotConsumableLiquidByList fluidSlot2;
    public final int defaultEnergyConsume;
    public final int defaultOperationLength;
    public final int defaultTier;
    public final int defaultEnergyStorage;
    public final InvSlotUpgrade upgradeSlot;
    public final FluidTank fluidTank1;
    public final FluidTank fluidTank2;
    private final Fluids fluids;
    public int energyConsume;
    public int operationLength;
    public int operationsPerTick;
    public AudioSource audioSource;
    public InvSlotObsidianGenerator inputSlotA;
    protected short progress;
    protected double guiProgress;

    public TileEntityBaseObsidianGenerator(int energyPerTick, int length, int outputSlots) {
        this(energyPerTick, length, outputSlots, 1);
    }

    public TileEntityBaseObsidianGenerator(int energyPerTick, int length, int outputSlots, int aDefaultTier) {
        super("", energyPerTick * length, 1, outputSlots);
        this.progress = 0;
        this.defaultEnergyConsume = this.energyConsume = energyPerTick;
        this.defaultOperationLength = this.operationLength = length;
        this.defaultTier = aDefaultTier;
        this.defaultEnergyStorage = energyPerTick * length;
        this.upgradeSlot = new InvSlotUpgrade(this, "upgrade", 4);
        this.outputSlot1 = new InvSlotOutput(this, "output1", 1);

        this.fluidSlot1 = new InvSlotConsumableLiquidByList(this, "fluidSlot", 1, FluidRegistry.WATER);
        this.fluidSlot2 = new InvSlotConsumableLiquidByList(this, "fluidSlot1", 1, FluidRegistry.LAVA);
        this.fluids = this.addComponent(new Fluids(this));
        this.fluidTank1 = this.fluids.addTank("fluidTank1", 12 * 1000

        );
        this.fluidTank2 = this.fluids.addTank("fluidTank2", 12 * 1000

        );
    }

    public static int applyModifier(int base, int extra, double multiplier) {
        double ret = Math.round((base + extra) * multiplier);
        return (ret > 2.147483647E9D) ? Integer.MAX_VALUE : (int) ret;
    }


    public void readFromNBT(NBTTagCompound nbttagcompound) {
        super.readFromNBT(nbttagcompound);
        this.progress = nbttagcompound.getShort("progress");

    }

    public NBTTagCompound writeToNBT(NBTTagCompound nbttagcompound) {
        super.writeToNBT(nbttagcompound);
        nbttagcompound.setShort("progress", this.progress);
        return nbttagcompound;
    }

    public double getProgress() {
        return this.guiProgress;
    }

    public void onLoaded() {
        super.onLoaded();
        if (IC2.platform.isSimulating()) {
            setOverclockRates();
        }
    }

    public void onUnloaded() {
        super.onUnloaded();
        if (IC2.platform.isRendering() && this.audioSource != null) {
            IC2.audioManager.removeSources(this);
            this.audioSource = null;
        }
    }

    public void markDirty() {
        super.markDirty();
        if (IC2.platform.isSimulating()) {
            setOverclockRates();
        }
    }

    public void updateEntityServer() {
        super.updateEntityServer();
        boolean needsInvUpdate = false;
        MutableObject<ItemStack> output1 = new MutableObject<>();
        if (this.fluidSlot1.transferToTank(
                this.fluidTank1,
                output1,
                true
        ) && (output1.getValue() == null || this.outputSlot1.canAdd(output1.getValue()))) {
            needsInvUpdate = this.fluidSlot1.transferToTank(this.fluidTank1, output1, false);
            if (output1.getValue() != null) {
                this.outputSlot1.add(output1.getValue());
            }
        }
        if (this.fluidSlot2.transferToTank(
                this.fluidTank2,
                output1,
                true
        ) && (output1.getValue() == null || this.outputSlot1.canAdd(output1.getValue()))) {
            needsInvUpdate = this.fluidSlot2.transferToTank(this.fluidTank2, output1, false);
            if (output1.getValue() != null) {
                this.outputSlot1.add(output1.getValue());
            }
        }
        RecipeOutput output = getOutput();


        if (output != null && this.energy.canUseEnergy(energyConsume)) {
            setActive(true);
            if (this.progress == 0) {
                IC2.network.get(true).initiateTileEntityEvent(this, 0, true);
            }
            this.progress = (short) (this.progress + 1);
            this.energy.useEnergy(energyConsume);
            double k = this.progress;

            this.guiProgress = (k / this.operationLength);
            if (this.progress >= this.operationLength) {
                this.guiProgress = 0;
                operate(output);
                needsInvUpdate = true;
                this.progress = 0;
                IC2.network.get(true).initiateTileEntityEvent(this, 2, true);
            }
        } else {
            if (this.progress != 0 && getActive()) {
                IC2.network.get(true).initiateTileEntityEvent(this, 1, true);
            }
            if (output == null) {
                this.progress = 0;
            }
            setActive(false);
        }
        for (int i = 0; i < this.upgradeSlot.size(); i++) {
            ItemStack stack = this.upgradeSlot.get(i);
            if (stack != null && stack.getItem() instanceof IUpgradeItem) {
                if (((IUpgradeItem) stack.getItem()).onTick(stack, this)) {
                    needsInvUpdate = true;
                }
            }
        }

        if (needsInvUpdate) {
            super.markDirty();
        }
    }

    public void setOverclockRates() {
        this.upgradeSlot.onChanged();
        double previousProgress = (double) this.progress / (double) this.operationLength;
        double stackOpLen = (this.defaultOperationLength + this.upgradeSlot.extraProcessTime) * 64.0D
                * this.upgradeSlot.processTimeMultiplier;
        this.operationsPerTick = (int) Math.min(Math.ceil(64.0D / stackOpLen), 2.147483647E9D);
        this.operationLength = (int) Math.round(stackOpLen * this.operationsPerTick / 64.0D);
        this.energyConsume = applyModifier(this.defaultEnergyConsume, this.upgradeSlot.extraEnergyDemand,
                this.upgradeSlot.energyDemandMultiplier
        );
        this.energy.setSinkTier(applyModifier(this.defaultTier, this.upgradeSlot.extraTier, 1.0D));
        this.energy.setCapacity(applyModifier(
                this.defaultEnergyStorage,
                this.upgradeSlot.extraEnergyStorage + this.operationLength * this.energyConsume,
                this.upgradeSlot.energyStorageMultiplier
        ));
        if (this.operationLength < 1) {
            this.operationLength = 1;
        }
        this.progress = (short) (int) Math.floor(previousProgress * this.operationLength + 0.1D);
    }

    public void operate(RecipeOutput output) {
        for (int i = 0; i < this.operationsPerTick; i++) {
            List<ItemStack> processResult = output.items;
            for (int j = 0; j < this.upgradeSlot.size(); j++) {
                ItemStack stack = this.upgradeSlot.get(j);
                if (stack != null && stack.getItem() instanceof IUpgradeItem) {
                    ((IUpgradeItem) stack.getItem()).onProcessEnd(stack, this, processResult);
                }
            }
            operateOnce(processResult);

            output = getOutput();
            if (output == null) {
                break;
            }
        }
    }

    public void operateOnce(List<ItemStack> processResult) {

        this.inputSlotA.consume();

        this.outputSlot.add(processResult);
    }

    public RecipeOutput getOutput() {
        if (this.fluidTank2.getFluid() == null) {
            return null;
        }
        if (this.fluidTank1.getFluid() == null) {
            return null;
        }
        if (this.fluidTank2.getFluid().amount < 1000) {
            return null;
        }

        RecipeOutput output = this.inputSlotA.process();

        if (output == null) {
            return null;
        }
        if (this.outputSlot.canAdd(output.items)) {
            return output;
        }

        return null;
    }

    public abstract String getInventoryName();

    public ContainerBase<? extends TileEntityBaseObsidianGenerator> getGuiContainer(EntityPlayer entityPlayer) {
        return new ContainerObsidianGenerator(entityPlayer, this);
    }

    public String getStartSoundFile() {
        return null;
    }

    public String getInterruptSoundFile() {
        return null;
    }

    public void onNetworkEvent(int event) {
        if (this.audioSource == null && getStartSoundFile() != null) {
            this.audioSource = IUCore.audioManager.createSource(this, getStartSoundFile());
        }
        switch (event) {
            case 0:
                if (this.audioSource != null) {
                    this.audioSource.play();
                }
                break;
            case 1:
                if (this.audioSource != null) {
                    this.audioSource.stop();
                    if (getInterruptSoundFile() != null) {
                        IUCore.audioManager.playOnce(this, getInterruptSoundFile());
                    }
                }
                break;
            case 2:
                if (this.audioSource != null) {
                    this.audioSource.stop();
                }
                break;
        }
    }

    public double getEnergy() {
        return this.energy.getEnergy();
    }

    public boolean useEnergy(double amount) {
        if (this.energy.canUseEnergy(amount)) {
            this.energy.useEnergy(amount);
            return true;
        }
        return false;
    }

    public void onGuiClosed(EntityPlayer entityPlayer) {
    }


    public boolean canFill(Fluid fluid) {
        return fluid == FluidRegistry.LAVA || fluid == FluidRegistry.WATER;
    }

    public boolean canDrain(Fluid fluid) {
        return true;
    }

    public int fill(FluidStack resource, boolean doFill) {
        if (this.getFluidTank1().getFluidAmount() < this.getFluidTank1().getCapacity() && resource
                .getFluid()
                .equals(FluidRegistry.WATER)) {
            return this.canFill(resource.getFluid()) ? this.getFluidTank1().fill(resource, doFill) : 0;
        }
        if (this.getFluidTank2().getFluidAmount() < this.getFluidTank2().getCapacity() && resource
                .getFluid()
                .equals(FluidRegistry.LAVA)) {
            return this.canFill(resource.getFluid()) ? this.getFluidTank2().fill(resource, doFill) : 0;

        }
        return 0;
    }

    public FluidStack drain(FluidStack resource, boolean doDrain) {
        if (resource != null && resource.isFluidEqual(this.getFluidTank1().getFluid())) {
            return !this.canDrain(resource.getFluid()) ? null : this.getFluidTank1().drain(resource.amount, doDrain);
        } else if (resource != null && resource.isFluidEqual(this.getFluidTank2().getFluid())) {
            return !this.canDrain(resource.getFluid()) ? null : this.getFluidTank2().drain(resource.amount, doDrain);
        } else {
            return null;
        }
    }

    public FluidStack drain(int maxDrain, boolean doDrain) {
        return !this.canDrain(null) ? null : this.getFluidTank2().drain(maxDrain, doDrain);
    }


    public int getTankAmount1() {
        return this.getFluidTank1().getFluidAmount();
    }

    public int getTankAmount2() {
        return this.getFluidTank2().getFluidAmount();
    }

    public int gaugeLiquidScaled1(int i) {
        return this.getFluidTank1().getFluidAmount() <= 0
                ? 0
                : this.getFluidTank1().getFluidAmount() * i / this.getFluidTank1().getCapacity();
    }

    public int gaugeLiquidScaled2(int i) {
        return this.getFluidTank2().getFluidAmount() <= 0
                ? 0
                : this.getFluidTank2().getFluidAmount() * i / this.getFluidTank2().getCapacity();
    }

    public FluidTank getFluidTank1() {
        return this.fluidTank1;
    }

    public FluidTank getFluidTank2() {
        return this.fluidTank2;
    }

}
