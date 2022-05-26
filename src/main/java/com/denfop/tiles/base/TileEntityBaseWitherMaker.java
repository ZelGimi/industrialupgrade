package com.denfop.tiles.base;

import com.denfop.IUCore;
import com.denfop.audio.AudioSource;
import com.denfop.container.ContainerBaseWitherMaker;
import com.denfop.invslot.InvSlotProcessableWitherMaker;
import ic2.api.network.INetworkTileEntityEventListener;
import ic2.api.recipe.RecipeOutput;
import ic2.api.upgrade.IUpgradableBlock;
import ic2.api.upgrade.IUpgradeItem;
import ic2.core.ContainerBase;
import ic2.core.IC2;
import ic2.core.IHasGui;
import ic2.core.block.invslot.InvSlotUpgrade;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;

import java.util.List;
import java.util.Random;

public abstract class TileEntityBaseWitherMaker extends TileEntityElectricMachine
        implements IHasGui, INetworkTileEntityEventListener, IUpgradableBlock {

    public final int defaultEnergyConsume;
    public final int defaultOperationLength;
    public final int defaultTier;
    public final int defaultEnergyStorage;
    public final InvSlotUpgrade upgradeSlot;
    public int energyConsume;
    public int operationLength;
    public int operationsPerTick;
    public AudioSource audioSource;


    public InvSlotProcessableWitherMaker inputSlotA;
    protected short progress;
    protected double guiProgress;

    public TileEntityBaseWitherMaker(int energyPerTick, int length, int outputSlots) {
        this(energyPerTick, length, outputSlots, 1);
    }

    public TileEntityBaseWitherMaker(int energyPerTick, int length, int outputSlots, int aDefaultTier) {
        super("", energyPerTick * length, 1, outputSlots);
        this.progress = 0;
        this.defaultEnergyConsume = this.energyConsume = energyPerTick;
        this.defaultOperationLength = this.operationLength = length;
        this.defaultTier = aDefaultTier;
        this.defaultEnergyStorage = energyPerTick * length;

        this.upgradeSlot = new InvSlotUpgrade(this, "upgrade", 4);
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
            IUCore.audioManager.removeSources(this);
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
        RecipeOutput output = getOutput();
        if (this.getWorld().provider.getWorldTime() % 20 == 0) {
            if (!this.inputSlotA.isEmpty()) {
                for (int i = 0; i < 3; i++) {


                            if (!this.inputSlotA.get(i).isEmpty() && this.inputSlotA.get(i).getCount() > 1 && this.inputSlotA
                                    .get(i)
                                    .getItem() == Items.SKULL && this.inputSlotA.get(i).getItemDamage() == 1) {
                                for (int j = 0; j < 3; j++) {
                                if (this.inputSlotA.get(j).isEmpty()) {
                                    this.inputSlotA.consume(i, 1);
                                    ItemStack stack = new ItemStack(Items.SKULL,1,1);
                                    this.inputSlotA.put(j, stack);
                                }
                            }

                    }
                }
                for (int i = 3; i < 7; i++) {
                    for (int j = 3; j < 7; j++) {
                        if (i != j) {
                            if (!this.inputSlotA.get(i).isEmpty() && this.inputSlotA.get(i).getCount() > 1 && (this.inputSlotA
                                    .get(i)
                                    .getItem() == Item.getItemFromBlock(Blocks.SOUL_SAND))) {
                                if (this.inputSlotA.get(j).isEmpty()) {
                                    this.inputSlotA.consume(i, 1);

                                    ItemStack stack =  new ItemStack(Blocks.SOUL_SAND,1);
                                    this.inputSlotA.put(j, stack);
                                }
                            }
                        }
                    }
                }
            }
        }
        if (output != null && this.energy.canUseEnergy(energyConsume)) {
            setActive(true);
            if (this.progress == 0) {
                IC2.network.get(true).initiateTileEntityEvent(this, 0, true);
            }
            this.progress = (short) (this.progress + 1);
            this.energy.useEnergy(energyConsume);
            double k = this.progress;

            this.guiProgress = (k / this.operationLength);
            if (this.getWorld().provider.getWorldTime() % 20 == 0) {
                IC2.network.get(true).initiateTileEntityEvent(this, 3, true);
            }
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
        if (this.inputSlotA.isEmpty()) {
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

    public ContainerBase<? extends TileEntityBaseWitherMaker> getGuiContainer(EntityPlayer entityPlayer) {
        return new ContainerBaseWitherMaker(
                entityPlayer, this);
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
                    this.audioSource.stop();
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
                    IUCore.audioManager.playOnce(this, getInterruptSoundFile1());

                }
                break;
            case 3:
                if (this.audioSource != null) {
                    this.audioSource.stop();
                    final Random rand = this.getWorld().rand;
                    IUCore.audioManager.playOnce(this, rand.nextInt(2) == 0 ? getInterruptSoundFile2() :
                            getInterruptSoundFile3());

                }
                break;
        }
    }

    public String getInterruptSoundFile1() {
        return "Machines/WitherDeath1.ogg";
    }

    public String getInterruptSoundFile2() {
        return "Machines/WitherHurt3.ogg";
    }

    public String getInterruptSoundFile3() {
        return "Machines/WitherIdle1.ogg";
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

}
