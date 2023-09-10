package com.denfop.tiles.base;

import com.denfop.IUCore;
import com.denfop.Localization;
import com.denfop.api.recipe.IUpdateTick;
import com.denfop.api.recipe.InvSlotRecipes;
import com.denfop.api.recipe.MachineRecipe;
import com.denfop.api.upgrades.IUpgradableBlock;
import com.denfop.api.upgrades.UpgradableProperty;
import com.denfop.audio.EnumSound;
import com.denfop.componets.*;
import com.denfop.container.ContainerTripleElectricMachine;
import com.denfop.invslot.InvSlot;
import com.denfop.invslot.InvSlotDischarge;
import com.denfop.invslot.InvSlotUpgrade;
import com.denfop.network.packet.PacketStopSound;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.SoundCategory;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import org.lwjgl.input.Keyboard;

import java.util.EnumSet;
import java.util.List;
import java.util.Set;

public abstract class TileTripleElectricMachine extends TileStandartMachine
        implements IUpgradableBlock, IUpdateTick {

    public final InvSlotDischarge dischargeSlot;
    public final InvSlotRecipes inputSlotA;
    public final InvSlotUpgrade upgradeSlot;
    public final ComponentUpgradeSlots componentUpgrade;
    public final ComponentProgress componentProgress;
    public final ComponentProcess componentProcess;
    protected final String name;
    protected final EnumTripleElectricMachine type;
    private final ComponentUpgrade componentUpgrades;
    public double energyConsume;
    public int operationLength;
    public int operationsPerTick;
    public MachineRecipe output;
    protected boolean sound = true;

    public TileTripleElectricMachine(
            int energyPerTick,
            int length,
            int outputSlots,
            String name,
            EnumTripleElectricMachine type
    ) {
        this(energyPerTick, length, outputSlots, 1, name, type);
    }

    public TileTripleElectricMachine(
            int energyPerTick, int length, int outputSlots, int aDefaultTier, String name,
            EnumTripleElectricMachine type
    ) {
        super(outputSlots);
        this.upgradeSlot = new com.denfop.invslot.InvSlotUpgrade(this, 4);
        this.name = name;
        this.inputSlotA = new InvSlotRecipes(this, type.recipe_name, this);
        this.type = type;
        this.dischargeSlot = new InvSlotDischarge(this, InvSlot.TypeItemSlot.INPUT, aDefaultTier, false);
        this.componentUpgrade = this.addComponent(new ComponentUpgradeSlots(this, upgradeSlot));
        this.componentProgress = this.addComponent(new ComponentProgress(this, 1,
                (short) length
        ));
        this.componentProcess = this.addComponent(new ComponentProcess(this, length, energyPerTick));
        this.componentProcess.setHasAudio(true);
        this.componentProcess.setSlotOutput(outputSlot);
        this.componentProcess.setInvSlotRecipes(inputSlotA);
        this.energy = this.addComponent(AdvEnergy
                .asBasicSink(this, (double) energyPerTick * length, aDefaultTier)
                .addManagedSlot(this.dischargeSlot));
        this.componentUpgrades = this.addComponent(new ComponentUpgrade(this, TypeUpgrade.INSTANT, TypeUpgrade.STACK));

    }

    @Override
    public boolean getEnable() {
        return this.sound;
    }

    @SideOnly(Side.CLIENT)
    public void addInformation(ItemStack stack, List<String> tooltip, ITooltipFlag advanced) {
        if (stack.getItemDamage() == 3 && type == EnumTripleElectricMachine.ADV_ALLOY_SMELTER) {
            if (!Keyboard.isKeyDown(Keyboard.KEY_LSHIFT)) {
                tooltip.add(Localization.translate("press.lshift"));
            }
            if (Keyboard.isKeyDown(Keyboard.KEY_LSHIFT)) {
                tooltip.add(Localization.translate("iu.heatmachine.info"));
                tooltip.add(Localization.translate("iu.machines_work_energy") + this.componentProcess.getDefaultEnergyConsume() + Localization.translate(
                        "iu.machines_work_energy_type_eu"));
                tooltip.add(Localization.translate("iu.machines_work_length") + this.componentProcess.getDefaultOperationLength());

            }
        } else {
            if (!Keyboard.isKeyDown(Keyboard.KEY_LSHIFT)) {
                tooltip.add(Localization.translate("press.lshift"));
            }
            if (Keyboard.isKeyDown(Keyboard.KEY_LSHIFT)) {
                tooltip.add(Localization.translate("iu.machines_work_energy") + this.componentProcess.getDefaultEnergyConsume() + Localization.translate(
                        "iu.machines_work_energy_type_eu"));
                tooltip.add(Localization.translate("iu.machines_work_length") + this.componentProcess.getDefaultOperationLength());
            }
        }
        if (this.getComp(AdvEnergy.class) != null) {
            AdvEnergy energy = this.getComp(AdvEnergy.class);
            if (!energy.getSourceDirs().isEmpty()) {
                tooltip.add(Localization.translate("iu.item.tooltip.PowerTier", energy.getSourceTier()));
            } else if (!energy.getSinkDirs().isEmpty()) {
                tooltip.add(Localization.translate("iu.item.tooltip.PowerTier", energy.getSinkTier()));
            }
        }
    }

    @Override
    public void onUpdate() {

    }

    @Override
    public MachineRecipe getRecipeOutput() {
        return this.output;
    }

    @Override
    public void setRecipeOutput(final MachineRecipe output) {
        this.output = output;
    }

    public void readFromNBT(NBTTagCompound nbttagcompound) {
        super.readFromNBT(nbttagcompound);
        this.sound = nbttagcompound.getBoolean("sound");

    }

    public NBTTagCompound writeToNBT(NBTTagCompound nbttagcompound) {
        super.writeToNBT(nbttagcompound);
        nbttagcompound.setBoolean("sound", this.sound);
        return nbttagcompound;
    }


    public void onLoaded() {
        super.onLoaded();
        if (IUCore.proxy.isSimulating()) {
            inputSlotA.load();
            this.getOutput();

        }

    }


    public MachineRecipe getOutput() {


        this.output = this.inputSlotA.process();

        return this.output;
    }

    public ContainerTripleElectricMachine getGuiContainer(EntityPlayer entityPlayer) {
        return new ContainerTripleElectricMachine(entityPlayer, this, type);
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

    public void initiate(int soundEvent) {
        if (this.getType() == valuesAudio[soundEvent % valuesAudio.length]) {
            return;
        }

        setType(valuesAudio[soundEvent % valuesAudio.length]);
        if (!getEnable()) {
            return;
        }
        if (getSound() == null) {
            return;
        }
        if (soundEvent == 0) {
            this.getWorld().playSound(null, this.pos, getSound(), SoundCategory.MASTER, 1F, 1);
        } else if (soundEvent == 1) {
            new PacketStopSound(getWorld(), this.pos);
            this.getWorld().playSound(null, this.pos, EnumSound.InterruptOne.getSoundEvent(), SoundCategory.MASTER, 1F, 1);
        } else {
            new PacketStopSound(getWorld(), this.pos);
        }
    }

    public final float getChargeLevel() {
        return Math.min((float) this.energy.getEnergy() / (float) this.energy.getCapacity(), 1);
    }


    public String getInventoryName() {
        return this.name;
    }


}
