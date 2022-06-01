package com.denfop.tiles.base;

import com.denfop.api.ITemperature;
import com.denfop.api.heat.IHeatSink;
import com.denfop.api.recipe.IUpdateTick;
import com.denfop.api.recipe.InvSlotRecipes;
import com.denfop.api.recipe.MachineRecipe;
import com.denfop.audio.AudioSource;
import ic2.api.network.INetworkTileEntityEventListener;
import ic2.api.upgrade.IUpgradableBlock;
import ic2.api.upgrade.IUpgradeItem;
import ic2.core.IC2;
import ic2.core.IHasGui;
import ic2.core.block.invslot.InvSlotOutput;
import ic2.core.block.invslot.InvSlotUpgrade;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;

import java.util.List;
import java.util.Random;

public abstract class TileEntityBaseHandlerHeavyOre extends TileEntityElectricMachine
        implements IHasGui, INetworkTileEntityEventListener, IUpgradableBlock, ITemperature, IHeatSink, IUpdateTick {

    public final int defaultEnergyConsume;
    public final int defaultOperationLength;
    public final int defaultTier;
    public final int defaultEnergyStorage;
    public final short maxtemperature;
    public final InvSlotOutput outputSlot;
    public final InvSlotUpgrade upgradeSlot;
    public int energyConsume;
    public int operationLength;
    public int operationsPerTick;
    public short temperature;
    public AudioSource audioSource;
    public InvSlotRecipes inputSlotA;
    public MachineRecipe output;
    protected boolean needTemperature;
    protected short progress;
    protected double guiProgress;
    private ITemperature source;

    public TileEntityBaseHandlerHeavyOre(int energyPerTick, int length, int outputSlots) {
        this(energyPerTick, length, outputSlots, 1);
    }

    public TileEntityBaseHandlerHeavyOre(int energyPerTick, int length, int outputSlots, int aDefaultTier) {
        super(energyPerTick * length, 1, 1);
        this.progress = 0;
        this.defaultEnergyConsume = this.energyConsume = energyPerTick;
        this.defaultOperationLength = this.operationLength = length;
        this.defaultTier = aDefaultTier;
        this.defaultEnergyStorage = energyPerTick * length;
        this.outputSlot = new InvSlotOutput(this, "output", outputSlots);
        this.upgradeSlot = new InvSlotUpgrade(this, "upgrade", 4);
        this.temperature = 0;
        this.maxtemperature = 5000;
        this.needTemperature = false;
        this.inputSlotA = new InvSlotRecipes(this, "handlerho", this);

        this.source = null;
    }

    public static int applyModifier(int base, int extra, double multiplier) {
        double ret = Math.round((base + extra) * multiplier);
        return (ret > 2.147483647E9D) ? Integer.MAX_VALUE : (int) ret;
    }

    @Override
    public ITemperature getSource() {
        return this.source;
    }

    @Override
    public void setSource(final ITemperature source) {
        this.source = source;
    }


    public void readFromNBT(NBTTagCompound nbttagcompound) {
        super.readFromNBT(nbttagcompound);
        this.progress = nbttagcompound.getShort("progress");
        this.temperature = nbttagcompound.getShort("temperature");
    }

    public NBTTagCompound writeToNBT(NBTTagCompound nbttagcompound) {
        super.writeToNBT(nbttagcompound);
        nbttagcompound.setShort("progress", this.progress);
        nbttagcompound.setShort("temperature", this.temperature);
        return nbttagcompound;
    }

    public void markDirty() {
        super.markDirty();
        if (IC2.platform.isSimulating()) {
            setOverclockRates();
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

    public void operate(MachineRecipe output) {
        for (int i = 0; i < this.operationsPerTick; i++) {
            List<ItemStack> processResult = output.getRecipe().output.items;
            for (int j = 0; j < this.upgradeSlot.size(); j++) {
                ItemStack stack = this.upgradeSlot.get(j);
                if (stack != null && stack.getItem() instanceof IUpgradeItem) {
                    ((IUpgradeItem) stack.getItem()).onProcessEnd(stack, this, processResult);
                }
            }
            operateOnce(processResult, output);
            if (!this.inputSlotA.continue_process(this.output)) {
                getOutput();
            }
            if (this.output == null) {
                break;
            }
        }
    }

    public void operateOnce(List<ItemStack> processResult, final MachineRecipe output) {
        final NBTTagCompound nbt = output.getRecipe().output.metadata;
        int[] col = new int[processResult.size()];
        for (int i = 0; i < col.length; i++) {
            col[i] = nbt.getInteger(("input" + i));
            final Random rand = world.rand;
            if ((100 - col[i]) <= rand.nextInt(100)) {
                this.outputSlot.add(processResult.get(i));
            }
        }
        this.inputSlotA.consume();
    }

    public MachineRecipe getOutput() {

        this.output = this.inputSlotA.process();


        return this.output;
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


    public double getProgress() {
        return this.guiProgress;
    }


}
