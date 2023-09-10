package com.denfop.tiles.base;

import com.denfop.IUCore;
import com.denfop.Localization;
import com.denfop.api.recipe.FluidHandlerRecipe;
import com.denfop.api.recipe.InvSlotOutput;
import com.denfop.api.recipe.RecipeOutput;
import com.denfop.api.upgrades.IUpgradableBlock;
import com.denfop.audio.EnumSound;
import com.denfop.componets.Fluids;
import com.denfop.container.ContainerObsidianGenerator;
import com.denfop.invslot.InvSlotFluidByList;
import com.denfop.invslot.InvSlotUpgrade;
import com.denfop.network.DecoderHandler;
import com.denfop.network.EncoderHandler;
import com.denfop.network.packet.CustomPacketBuffer;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.SoundEvent;
import net.minecraftforge.fluids.Fluid;
import net.minecraftforge.fluids.FluidRegistry;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.fluids.FluidTank;
import net.minecraftforge.fluids.capability.IFluidHandler;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import org.apache.commons.lang3.mutable.MutableObject;
import org.lwjgl.input.Keyboard;

import java.io.IOException;
import java.util.List;

public abstract class TileBaseObsidianGenerator extends TileElectricMachine
        implements IUpgradableBlock, IFluidHandler {

    public final InvSlotOutput outputSlot1;
    public final InvSlotFluidByList fluidSlot1;
    public final InvSlotFluidByList fluidSlot2;
    public final double defaultEnergyConsume;
    public final int defaultOperationLength;
    public final int defaultTier;
    public final double defaultEnergyStorage;
    public final InvSlotUpgrade upgradeSlot;
    public final FluidTank fluidTank1;
    public final FluidTank fluidTank2;
    private final FluidHandlerRecipe fluid_handler;
    public double energyConsume;
    public int operationLength;
    public int operationsPerTick;
    protected short progress;
    protected double guiProgress;

    public TileBaseObsidianGenerator(int energyPerTick, int length, int outputSlots) {
        this(energyPerTick, length, outputSlots, 1);
    }

    public TileBaseObsidianGenerator(int energyPerTick, int length, int outputSlots, int aDefaultTier) {
        super(energyPerTick * length, 1, outputSlots);
        this.progress = 0;
        this.defaultEnergyConsume = this.energyConsume = energyPerTick;
        this.defaultOperationLength = this.operationLength = length;
        this.defaultTier = aDefaultTier;
        this.defaultEnergyStorage = energyPerTick * length;
        this.upgradeSlot = new com.denfop.invslot.InvSlotUpgrade(this, 4);
        this.outputSlot1 = new InvSlotOutput(this, "output1", 1);

        this.fluidSlot1 = new InvSlotFluidByList(this, 1, FluidRegistry.WATER);
        this.fluidSlot2 = new InvSlotFluidByList(this, 1, FluidRegistry.LAVA);
        Fluids fluids = this.addComponent(new Fluids(this));
        this.fluidTank1 = fluids.addTank("fluidTank1", 12 * 1000, Fluids.fluidPredicate(FluidRegistry.WATER)

        );
        this.fluidTank2 = fluids.addTank("fluidTank2", 12 * 1000, Fluids.fluidPredicate(FluidRegistry.LAVA)

        );
        this.fluid_handler = new FluidHandlerRecipe("obsidian", fluids);
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
        if (IUCore.proxy.isSimulating()) {
            setOverclockRates();
            this.fluid_handler.load();
        }
    }

    public void onUnloaded() {
        super.onUnloaded();

    }


    public void updateEntityServer() {
        super.updateEntityServer();
        MutableObject<ItemStack> output1 = new MutableObject<>();
        boolean check = false;
        if (this.fluidTank1.getFluidAmount() + 1000 <= this.fluidTank1.getCapacity() && this.fluidSlot1.transferToTank(
                this.fluidTank1,
                output1,
                true
        ) && (output1.getValue() == null || this.outputSlot1.canAdd(output1.getValue()))) {
            this.fluidSlot1.transferToTank(this.fluidTank1, output1, false);
            if (output1.getValue() != null) {
                this.outputSlot1.add(output1.getValue());
            }
            check = true;
        }
        if (this.fluidTank2.getFluidAmount() + 1000 <= this.fluidTank2.getCapacity() && this.fluidSlot2.transferToTank(
                this.fluidTank2,
                output1,
                true
        ) && (output1.getValue() == null || this.outputSlot1.canAdd(output1.getValue()))) {
            this.fluidSlot2.transferToTank(this.fluidTank2, output1, false);
            if (output1.getValue() != null) {
                this.outputSlot1.add(output1.getValue());
            }
            check = true;
        }
        if (check || (this.fluid_handler.output() == null && this.fluidTank2.getFluidAmount() >= 1000 && this.fluidTank1.getFluidAmount() >= 1000)) {
            this.fluid_handler.getOutput();
        }


        if (this.fluid_handler.output() != null && this.energy.canUseEnergy(energyConsume)) {
            if (!this.getActive()) {
                setActive(true);
            }
            if (this.progress == 0) {
                initiate(0);
            }
            this.progress = (short) (this.progress + 1);
            this.energy.useEnergy(energyConsume);
            double k = this.progress;

            this.guiProgress = (k / this.operationLength);
            if (this.progress >= this.operationLength) {
                this.guiProgress = 0;
                operate(this.fluid_handler.getOutput().getOutput());
                this.progress = 0;
                initiate(2);
            }
        } else {
            if (this.progress != 0 && getActive()) {
                initiate(0);
            }
            if (this.fluid_handler.output() == null) {
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
            operateOnce(processResult);

            this.fluid_handler.checkOutput();
            if (this.fluid_handler.output() == null) {
                break;
            }
        }
    }

    public void operateOnce(List<ItemStack> processResult) {
        this.fluid_handler.consume();
        this.outputSlot.add(processResult);
    }


    public abstract String getInventoryName();

    public ContainerObsidianGenerator getGuiContainer(EntityPlayer entityPlayer) {
        return new ContainerObsidianGenerator(entityPlayer, this);
    }

    @Override
    public SoundEvent getSound() {
        return EnumSound.gen_obsidiant.getSoundEvent();
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
