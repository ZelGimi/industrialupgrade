package com.denfop.tiles.base;

import com.denfop.IUCore;
import com.denfop.api.recipe.IUpdateTick;
import com.denfop.api.recipe.InvSlotRecipes;
import com.denfop.api.recipe.MachineRecipe;
import com.denfop.audio.AudioSource;
import com.denfop.audio.PositionSpec;
import com.denfop.componets.AdvEnergy;
import com.denfop.container.ContainerDoubleElectricMachine;
import com.denfop.tiles.mechanism.TileEntityAlloySmelter;
import ic2.api.network.INetworkTileEntityEventListener;
import ic2.api.upgrade.IUpgradableBlock;
import ic2.api.upgrade.IUpgradeItem;
import ic2.api.upgrade.UpgradableProperty;
import ic2.core.ContainerBase;
import ic2.core.IC2;
import ic2.core.IHasGui;
import ic2.core.block.TileEntityInventory;
import ic2.core.block.invslot.InvSlot;
import ic2.core.block.invslot.InvSlotDischarge;
import ic2.core.block.invslot.InvSlotOutput;
import ic2.core.block.invslot.InvSlotUpgrade;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;

import java.util.EnumSet;
import java.util.List;
import java.util.Set;

public abstract class TileEntityDoubleElectricMachine extends TileEntityInventory implements IHasGui,
        INetworkTileEntityEventListener, IUpgradableBlock, IUpdateTick {

    public final AdvEnergy energy;
    public final InvSlotDischarge dischargeSlot;
    public final int defaultEnergyConsume;
    public final int defaultOperationLength;
    public final int defaultTier;
    public final int defaultEnergyStorage;
    public final InvSlotRecipes inputSlotA;
    public final InvSlotOutput outputSlot;
    public final InvSlotUpgrade upgradeSlot;
    protected final String name;
    protected final EnumDoubleElectricMachine type;
    public int energyConsume;
    public int operationLength;
    public int operationsPerTick;
    public AudioSource audioSource;
    public short temperature;
    public MachineRecipe output;
    protected short progress;
    protected double guiProgress;

    public TileEntityDoubleElectricMachine(
            int energyPerTick,
            int length,
            int outputSlots,
            String name,
            EnumDoubleElectricMachine type
    ) {
        this(energyPerTick, length, outputSlots, 1, name, type);
    }


    public TileEntityDoubleElectricMachine(
            int energyPerTick,
            int length,
            int outputSlots,
            int aDefaultTier,
            String name,
            EnumDoubleElectricMachine type
    ) {
        this.progress = 0;
        this.defaultEnergyConsume = this.energyConsume = energyPerTick;
        this.defaultOperationLength = this.operationLength = length;
        this.defaultTier = aDefaultTier;
        this.defaultEnergyStorage = energyPerTick * length;
        this.outputSlot = new InvSlotOutput(this, "output", outputSlots);
        this.upgradeSlot = new InvSlotUpgrade(this, "upgrade", 4);
        this.name = name;
        this.dischargeSlot = new InvSlotDischarge(this, InvSlot.Access.NONE, aDefaultTier, false, InvSlot.InvSide.ANY);
        this.energy = this.addComponent(AdvEnergy
                .asBasicSink(this, (double) energyPerTick * length, aDefaultTier)
                .addManagedSlot(this.dischargeSlot));
        this.inputSlotA = new InvSlotRecipes(this, type.recipe_name, this);
        this.type = type;
        this.output = null;
    }

    public MachineRecipe getRecipeOutput() {
        return this.output;
    }

    public void setRecipeOutput(MachineRecipe output) {
        this.output = output;
    }

    public void onUpdate() {
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

    protected void initiate(int soundEvent) {
        IC2.network.get(true).initiateTileEntityEvent(this, soundEvent, true);
    }

    protected void onLoaded() {
        super.onLoaded();
        if (IC2.platform.isSimulating()) {
            this.setOverclockRates();
            inputSlotA.load();
            this.getOutput();
        }


    }

    protected void onUnloaded() {
        super.onUnloaded();
        if (IC2.platform.isRendering() && this.audioSource != null) {
            IUCore.audioManager.removeSources(this);
            this.audioSource = null;
        }

    }

    public void markDirty() {
        super.markDirty();
        if (IC2.platform.isSimulating()) {
            this.setOverclockRates();
        }

    }

    protected void updateEntityServer() {
        super.updateEntityServer();
        boolean needsInvUpdate = false;


        MachineRecipe output = this.output;
        if (output != null && this.outputSlot.canAdd(output.getRecipe().output.items) && this.energy.getEnergy() >= this.energyConsume) {
            if (this.type.equals(EnumDoubleElectricMachine.ALLOY_SMELTER)) {
                if (output.getRecipe().output.metadata.getShort("temperature") == 0 || output.getRecipe().output.metadata.getInteger(
                        "temperature") > ((TileEntityAlloySmelter) this).temperature) {
                    return;
                }
            }
            setActive(true);
            if (this.progress == 0) {
                IC2.network.get(true).initiateTileEntityEvent(this, 0, true);
            }
            this.progress = (short) (this.progress + 1);
            this.energy.useEnergy(this.energyConsume);
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
        this.operationsPerTick = this.upgradeSlot.getOperationsPerTick(this.defaultOperationLength);
        this.operationLength = this.upgradeSlot.getOperationLength(this.defaultOperationLength);
        this.energyConsume = this.upgradeSlot.getEnergyDemand(this.defaultEnergyConsume);
        int tier = this.upgradeSlot.getTier(this.defaultTier);
        this.energy.setSinkTier(tier);
        this.energy.setCapacity(this.upgradeSlot.getEnergyStorage(
                this.defaultEnergyStorage,
                this.defaultOperationLength,
                this.defaultEnergyConsume
        ));
        dischargeSlot.setTier(tier);
    }

    public void operate(MachineRecipe output) {
        for (int i = 0; i < this.operationsPerTick; i++) {
            List<ItemStack> processResult = output.getRecipe().output.items;
            operateOnce(output, processResult);
            if (!this.inputSlotA.continue_process(this.output)) {
                getOutput();
            }
            if (this.output == null) {
                break;
            }
        }
    }

    public abstract void operateOnce(MachineRecipe output, List<ItemStack> processResult);


    public MachineRecipe getOutput() {
        this.output = this.inputSlotA.process();
        return this.output;
    }

    public ContainerBase<? extends TileEntityDoubleElectricMachine> getGuiContainer(EntityPlayer entityPlayer) {
        return new ContainerDoubleElectricMachine(entityPlayer, this, type);
    }


    public String getStartSoundFile() {
        return null;
    }

    public String getInterruptSoundFile() {
        return null;
    }

    public void onNetworkEvent(int event) {
        if (this.audioSource == null && this.getStartSoundFile() != null) {
            this.audioSource = IUCore.audioManager.createSource(this, this.getStartSoundFile());
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
                    if (this.getInterruptSoundFile() != null) {
                        IUCore.audioManager.playOnce(
                                this,
                                PositionSpec.Center,
                                this.getInterruptSoundFile(),
                                false,
                                IUCore.audioManager.getDefaultVolume()
                        );
                    }
                }
                break;
            case 2:
                if (this.audioSource != null) {
                    this.audioSource.stop();
                }
            case 3:
        }

    }

    public double getEnergy() {
        return this.energy.getEnergy();
    }

    public boolean useEnergy(double amount) {
        return this.energy.useEnergy(amount);
    }

    @Override
    public Set<UpgradableProperty> getUpgradableProperties() {
        return EnumSet.of(
                UpgradableProperty.Processing,
                UpgradableProperty.Transformer,
                UpgradableProperty.EnergyStorage,
                UpgradableProperty.ItemConsuming,
                UpgradableProperty.ItemProducing
        );
    }

    public void onGuiClosed(EntityPlayer player) {
    }


    public final float getChargeLevel() {
        return Math.min((float) this.energy.getEnergy() / (float) this.energy.getCapacity(), 1);
    }

    public double getProgress() {
        return this.guiProgress;
    }

    public int getMode() {
        return 0;
    }

    public String getInventoryName() {
        return this.name;
    }


}
