package com.denfop.tiles.base;

import com.denfop.IUCore;
import com.denfop.api.recipe.InvSlotOutput;
import com.denfop.api.recipe.RecipeOutput;
import com.denfop.audio.AudioSource;
import com.denfop.container.ContainerObsidianGenerator;
import com.denfop.invslot.InvSlotObsidianGenerator;
import com.denfop.invslot.InvSlotUpgrade;
import ic2.api.upgrade.IUpgradableBlock;
import ic2.api.upgrade.IUpgradeItem;
import ic2.core.ContainerBase;
import ic2.core.IC2;
import ic2.core.block.comp.Fluids;
import ic2.core.block.invslot.InvSlotConsumableLiquidByList;
import ic2.core.init.Localization;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraftforge.fluids.Fluid;
import net.minecraftforge.fluids.FluidRegistry;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.fluids.FluidTank;
import net.minecraftforge.fluids.capability.IFluidHandler;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import org.apache.commons.lang3.mutable.MutableObject;
import org.lwjgl.input.Keyboard;

import java.util.List;

public abstract class TileEntityBaseObsidianGenerator extends TileEntityElectricMachine
        implements IUpgradableBlock, IFluidHandler {

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
        super(energyPerTick * length, 1, outputSlots);
        this.progress = 0;
        this.defaultEnergyConsume = this.energyConsume = energyPerTick;
        this.defaultOperationLength = this.operationLength = length;
        this.defaultTier = aDefaultTier;
        this.defaultEnergyStorage = energyPerTick * length;
        this.upgradeSlot = new com.denfop.invslot.InvSlotUpgrade(this, "upgrade", 4);
        this.outputSlot1 = new InvSlotOutput(this, "output1", 1);

        this.fluidSlot1 = new InvSlotConsumableLiquidByList(this, "fluidSlot", 1, FluidRegistry.WATER);
        this.fluidSlot2 = new InvSlotConsumableLiquidByList(this, "fluidSlot1", 1, FluidRegistry.LAVA);
        Fluids fluids = this.addComponent(new Fluids(this));
        this.fluidTank1 = fluids.addTank("fluidTank1", 12 * 1000, Fluids.fluidPredicate(FluidRegistry.WATER)

        );
        this.fluidTank2 = fluids.addTank("fluidTank2", 12 * 1000, Fluids.fluidPredicate(FluidRegistry.LAVA)

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

    @SideOnly(Side.CLIENT)
    public void addInformation(ItemStack stack, List<String> tooltip, ITooltipFlag advanced) {
        if (!Keyboard.isKeyDown(Keyboard.KEY_LSHIFT)) {
            tooltip.add(Localization.translate("press.lshift"));
        }
        if (Keyboard.isKeyDown(Keyboard.KEY_LSHIFT)) {
            tooltip.add(Localization.translate("iu.machines_work_energy") + this.defaultEnergyConsume + Localization.translate(
                    "iu.machines_work_energy_type_eu"));
            tooltip.add(Localization.translate("iu.machines_work_length") + this.defaultOperationLength);
        }
        super.addInformation(stack, tooltip, advanced);

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
        MutableObject<ItemStack> output1 = new MutableObject<>();
        if (this.fluidSlot1.transferToTank(
                this.fluidTank1,
                output1,
                true
        ) && (output1.getValue() == null || this.outputSlot1.canAdd(output1.getValue()))) {
            this.fluidSlot1.transferToTank(this.fluidTank1, output1, false);
            if (output1.getValue() != null) {
                this.outputSlot1.add(output1.getValue());
            }
        }
        if (this.fluidSlot2.transferToTank(
                this.fluidTank2,
                output1,
                true
        ) && (output1.getValue() == null || this.outputSlot1.canAdd(output1.getValue()))) {
            this.fluidSlot2.transferToTank(this.fluidTank2, output1, false);
            if (output1.getValue() != null) {
                this.outputSlot1.add(output1.getValue());
            }
        }
        RecipeOutput output = getOutput();


        if (output != null && this.energy.canUseEnergy(energyConsume)) {
            if (!this.getActive()) {
                setActive(true);
            }
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
            if (this.getActive()) {
                setActive(false);
            }
        }
        if (this.upgradeSlot.tickNoMark()) {
            setOverclockRates();
        }

    }

    public void setOverclockRates() {
        this.operationsPerTick = this.upgradeSlot.getOperationsPerTick(this.defaultOperationLength);
        this.operationLength = this.upgradeSlot.getOperationLength(this.defaultOperationLength);
        this.energyConsume = this.upgradeSlot.getEnergyDemand(this.defaultEnergyConsume);
        int tier = this.upgradeSlot.getTier(this.defaultTier);
        this.energy.setSinkTier(tier);
        this.energy.setCapacity(this.upgradeSlot.getEnergyStorage(
                this.defaultEnergyStorage
        ));
        if (this.operationLength < 1) {
            this.operationLength = 1;
        }
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

    public boolean canDrain() {
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
            return !this.canDrain() ? null : this.getFluidTank1().drain(resource.amount, doDrain);
        } else if (resource != null && resource.isFluidEqual(this.getFluidTank2().getFluid())) {
            return !this.canDrain() ? null : this.getFluidTank2().drain(resource.amount, doDrain);
        } else {
            return null;
        }
    }

    public FluidStack drain(int maxDrain, boolean doDrain) {
        return !this.canDrain() ? null : this.getFluidTank2().drain(maxDrain, doDrain);
    }


    public FluidTank getFluidTank1() {
        return this.fluidTank1;
    }

    public FluidTank getFluidTank2() {
        return this.fluidTank2;
    }

}
