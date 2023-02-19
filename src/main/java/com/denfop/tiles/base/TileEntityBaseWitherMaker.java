package com.denfop.tiles.base;

import com.denfop.IUCore;
import com.denfop.api.recipe.IUpdateTick;
import com.denfop.api.recipe.InvSlotRecipes;
import com.denfop.api.recipe.MachineRecipe;
import com.denfop.audio.AudioSource;
import com.denfop.container.ContainerBaseWitherMaker;
import com.denfop.invslot.InvSlotUpgrade;
import ic2.api.upgrade.IUpgradableBlock;
import ic2.core.ContainerBase;
import ic2.core.IC2;
import ic2.core.init.Localization;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import org.lwjgl.input.Keyboard;

import java.util.List;
import java.util.Random;

public abstract class TileEntityBaseWitherMaker extends TileEntityElectricMachine
        implements IUpgradableBlock, IUpdateTick {

    public final int defaultEnergyConsume;
    public final int defaultOperationLength;
    public final int defaultTier;
    public final int defaultEnergyStorage;
    public final InvSlotUpgrade upgradeSlot;
    public int energyConsume;
    public int operationLength;
    public int operationsPerTick;
    public AudioSource audioSource;
    public MachineRecipe output;

    public InvSlotRecipes inputSlotA;
    protected short progress;
    protected double guiProgress;

    public TileEntityBaseWitherMaker(int energyPerTick, int length, int outputSlots) {
        this(energyPerTick, length, outputSlots, 1);
    }

    public TileEntityBaseWitherMaker(int energyPerTick, int length, int outputSlots, int aDefaultTier) {
        super(energyPerTick * length, 1, outputSlots);
        this.progress = 0;
        this.defaultEnergyConsume = this.energyConsume = energyPerTick;
        this.defaultOperationLength = this.operationLength = length;
        this.defaultTier = aDefaultTier;
        this.defaultEnergyStorage = energyPerTick * length;

        this.upgradeSlot = new com.denfop.invslot.InvSlotUpgrade(this, "upgrade", 4);
        this.output = null;
    }

    public static int applyModifier(int base, int extra, double multiplier) {
        double ret = Math.round((base + extra) * multiplier);
        return (ret > 2.147483647E9D) ? Integer.MAX_VALUE : (int) ret;
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
        inputSlotA.load();
        if (IC2.platform.isSimulating()) {
            setOverclockRates();
            this.output = this.getOutput();
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
        MachineRecipe output = this.output;
        if (this.getWorld().provider.getWorldTime() % 20 == 0) {
            if (!this.inputSlotA.isEmpty()) {
                for (int i = 0; i < 3; i++) {


                    if (!this.inputSlotA.get(i).isEmpty() && this.inputSlotA.get(i).getCount() > 1 && this.inputSlotA
                            .get(i)
                            .getItem() == Items.SKULL && this.inputSlotA.get(i).getItemDamage() == 1) {
                        for (int j = 0; j < 3; j++) {
                            if (this.inputSlotA.get(j).isEmpty() && !this.inputSlotA.get(i).isEmpty() && this.inputSlotA
                                    .get(i)
                                    .getCount() > 1) {
                                this.inputSlotA.consume(i, 1);
                                ItemStack stack = new ItemStack(Items.SKULL, 1, 1);
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

                                    ItemStack stack = new ItemStack(Blocks.SOUL_SAND, 1);
                                    this.inputSlotA.put(j, stack);
                                }
                            }
                        }
                    }
                }
            }
        }
        if (output != null && this.outputSlot.canAdd(output.getRecipe().output.items) && this.energy.canUseEnergy(energyConsume)) {
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
            if (this.getWorld().provider.getWorldTime() % 20 == 0) {
                if (sound) {
                    IC2.network.get(true).initiateTileEntityEvent(this, 3, true);
                }
            }
            if (this.progress >= this.operationLength) {
                this.guiProgress = 0;
                operate(output);
                this.progress = 0;
                initiate(2);
            }
        } else {
            if (this.progress != 0 && getActive()) {
                initiate(1);
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

    public void operate(MachineRecipe output) {
        for (int i = 0; i < this.operationsPerTick; i++) {
            List<ItemStack> processResult = output.getRecipe().output.items;
            operateOnce(processResult);
            if (!this.inputSlotA.continue_process(this.output) || !this.outputSlot.canAdd(output.getRecipe().output.items)) {
                getOutput();
                break;
            }
            if (this.output == null) {
                break;
            }
        }
    }

    public void operateOnce(List<ItemStack> processResult) {

        this.inputSlotA.consume();
        this.outputSlot.add(processResult);
    }

    public MachineRecipe getOutput() {

        this.output = this.inputSlotA.process();


        return this.output;
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
