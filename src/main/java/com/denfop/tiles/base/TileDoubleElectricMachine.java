package com.denfop.tiles.base;

import com.denfop.IUCore;
import com.denfop.Localization;
import com.denfop.api.audio.EnumTypeAudio;
import com.denfop.api.audio.IAudioFixer;
import com.denfop.api.recipe.IUpdateTick;
import com.denfop.api.recipe.InventoryOutput;
import com.denfop.api.recipe.InventoryRecipes;
import com.denfop.api.recipe.MachineRecipe;
import com.denfop.api.upgrades.IUpgradableBlock;
import com.denfop.api.upgrades.UpgradableProperty;
import com.denfop.audio.EnumSound;
import com.denfop.componets.ComponentProcess;
import com.denfop.componets.ComponentProgress;
import com.denfop.componets.ComponentUpgrade;
import com.denfop.componets.ComponentUpgradeSlots;
import com.denfop.componets.Energy;
import com.denfop.componets.HeatComponent;
import com.denfop.componets.TypeUpgrade;
import com.denfop.container.ContainerDoubleElectricMachine;
import com.denfop.invslot.InventoryUpgrade;
import com.denfop.invslot.Inventory;
import com.denfop.invslot.InventoryDischarge;
import com.denfop.network.DecoderHandler;
import com.denfop.network.EncoderHandler;
import com.denfop.network.IUpdatableTileEvent;
import com.denfop.network.packet.CustomPacketBuffer;
import com.denfop.network.packet.PacketStopSound;
import com.denfop.network.packet.PacketUpdateFieldTile;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.SoundCategory;
import org.lwjgl.input.Keyboard;

import java.io.IOException;
import java.util.EnumSet;
import java.util.List;
import java.util.Set;

public abstract class TileDoubleElectricMachine extends TileEntityInventory implements
        IAudioFixer, IUpgradableBlock, IUpdateTick, IUpdatableTileEvent {

    public final Energy energy;
    public final InventoryDischarge dischargeSlot;
    public final InventoryRecipes inputSlotA;
    public final InventoryOutput outputSlot;
    public final InventoryUpgrade upgradeSlot;
    public final HeatComponent heat;
    protected final EnumDoubleElectricMachine type;
    private final ComponentUpgrade componentUpgrades;
    public MachineRecipe output;
    public EnumTypeAudio typeAudio = EnumTypeAudio.OFF;
    public EnumTypeAudio[] valuesAudio = EnumTypeAudio.values();
    public ComponentUpgradeSlots componentUpgrade;
    public ComponentProgress componentProgress;
    public ComponentProcess componentProcess;
    private boolean sound = true;

    public TileDoubleElectricMachine(
            int energyPerTick,
            int length,
            int outputSlots,
            EnumDoubleElectricMachine type
    ) {
        this(energyPerTick, length, outputSlots, 1, type, true);
    }

    public TileDoubleElectricMachine(
            int energyPerTick,
            int length,
            int outputSlots,
            EnumDoubleElectricMachine type, boolean register
    ) {
        this(energyPerTick, length, outputSlots, 1, type, register);
    }

    public TileDoubleElectricMachine(
            int energyPerTick,
            int length,
            int outputSlots,
            int aDefaultTier,
            EnumDoubleElectricMachine type, boolean register
    ) {
        this.outputSlot = new InventoryOutput(this, outputSlots);
        this.upgradeSlot = new InventoryUpgrade(this, 4);
        this.dischargeSlot = new InventoryDischarge(this, Inventory.TypeItemSlot.INPUT, aDefaultTier, false);
        this.energy = this.addComponent(Energy
                .asBasicSink(this, (double) energyPerTick * length, aDefaultTier)
                .addManagedSlot(this.dischargeSlot));
        this.inputSlotA = new InventoryRecipes(this, type.recipe_name, this);
        this.type = type;
        this.output = null;
        if (type.heat) {
            this.heat = this.addComponent(HeatComponent
                    .asBasicSink(this, 5000));
        } else {
            this.heat = null;
        }
        if (register) {
            this.componentUpgrade = this.addComponent(new ComponentUpgradeSlots(this, upgradeSlot));
            this.componentProgress = this.addComponent(new ComponentProgress(this, 1,
                    (short) length
            ));
            this.componentProcess = this.addComponent(new ComponentProcess(this, length, energyPerTick));
            this.componentProcess.setHasAudio(true);
            this.componentProcess.setSlotOutput(outputSlot);
            this.componentProcess.setInvSlotRecipes(this.inputSlotA);
        }
        this.componentUpgrades = this.addComponent(new ComponentUpgrade(this, TypeUpgrade.INSTANT, TypeUpgrade.STACK));
    }

    public void updateTileServer(EntityPlayer var1, double var2) {
        sound = !sound;
        new PacketUpdateFieldTile(this, "sound", this.sound);

        if (!sound) {
            if (this.getTypeAudio() == EnumTypeAudio.ON) {
                setType(EnumTypeAudio.OFF);
                new PacketStopSound(getWorld(), this.pos);


            }
        }
    }

    public void updateField(String name, CustomPacketBuffer is) {

        if (name.equals("sound")) {
            try {
                this.sound = (boolean) DecoderHandler.decode(is);
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }
        super.updateField(name, is);
    }

    @Override
    public void readContainerPacket(final CustomPacketBuffer customPacketBuffer) {
        super.readContainerPacket(customPacketBuffer);
        try {
            sound = (boolean) DecoderHandler.decode(customPacketBuffer);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

    }

    @Override
    public CustomPacketBuffer writeContainerPacket() {
        final CustomPacketBuffer packet = super.writeContainerPacket();
        try {
            EncoderHandler.encode(packet, sound);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        return packet;
    }

    @Override
    public void onNetworkEvent(final int var1) {

    }

    public void addInformation(ItemStack stack, List<String> tooltip) {
        if (stack.getItemDamage() == 4 && type == EnumDoubleElectricMachine.ALLOY_SMELTER) {
            if (!Keyboard.isKeyDown(Keyboard.KEY_LSHIFT)) {
                tooltip.add(Localization.translate("press.lshift"));
            }
            if (Keyboard.isKeyDown(Keyboard.KEY_LSHIFT)) {
                tooltip.add(Localization.translate("iu.heatmachine.info"));
                tooltip.add(Localization.translate("iu.machines_work_energy") + this.componentProcess.getEnergyConsume() + Localization.translate(
                        "iu.machines_work_energy_type_eu"));
                tooltip.add(Localization.translate("iu.machines_work_length") + this.componentProgress.getMaxValue());

            }
        } else if (stack.getItemDamage() == 0 && EnumDoubleElectricMachine.SUNNARIUM_PANEL == type) {
            if (!Keyboard.isKeyDown(Keyboard.KEY_LSHIFT)) {
                tooltip.add(Localization.translate("press.lshift"));
            }
            if (Keyboard.isKeyDown(Keyboard.KEY_LSHIFT)) {
                tooltip.add(Localization.translate("iu.solarium_energy_sink.info"));
                tooltip.add(Localization.translate("iu.machines_work_energy") + this.componentProcess.getEnergyConsume() + Localization.translate(
                        "iu.machines_work_energy_type_eu"));
                tooltip.add(Localization.translate("iu.machines_work_energy") + 5 + Localization.translate("iu" +
                        ".machines_work_energy_type_se"));
                tooltip.add(Localization.translate("iu.machines_work_length") + this.componentProgress.getMaxValue());

            }
        } else {
            if (!Keyboard.isKeyDown(Keyboard.KEY_LSHIFT)) {
                tooltip.add(Localization.translate("press.lshift"));
            }
            if (Keyboard.isKeyDown(Keyboard.KEY_LSHIFT)) {
                tooltip.add(Localization.translate("iu.machines_work_energy") + this.componentProcess.getEnergyConsume() + Localization.translate(
                        "iu.machines_work_energy_type_eu"));
                tooltip.add(Localization.translate("iu.machines_work_length") + this.componentProcess.getOperationsPerTick());
            }
        }
        if (this.getComp(Energy.class) != null) {
            Energy energy = this.getComp(Energy.class);
            if (!energy.getSourceDirs().isEmpty()) {
                tooltip.add(Localization.translate("iu.item.tooltip.PowerTier", energy.getSourceTier()));
            } else if (!energy.getSinkDirs().isEmpty()) {
                tooltip.add(Localization.translate("iu.item.tooltip.PowerTier", energy.getSinkTier()));
            }
        }
        super.addInformation(stack, tooltip);
    }

    public EnumTypeAudio getTypeAudio() {
        return typeAudio;
    }

    public void setType(EnumTypeAudio type) {
        typeAudio = type;
    }

    @Override
    public boolean getEnable() {
        return this.sound;
    }

    public void initiate(int soundEvent) {
        if (this.getTypeAudio() == valuesAudio[soundEvent % valuesAudio.length]) {
            return;
        }
        setType(valuesAudio[soundEvent % valuesAudio.length]);

        if (sound) {
            if (getSound() == null) {
                return;
            }
            if (soundEvent == 0) {
                this.getWorld().playSound(null, this.pos, getSound(), SoundCategory.BLOCKS, 1F, 1);
            } else if (soundEvent == 1) {
                new PacketStopSound(getWorld(), this.pos);
                this.getWorld().playSound(null, this.pos, EnumSound.InterruptOne.getSoundEvent(), SoundCategory.BLOCKS, 1F, 1);
            } else {
                new PacketStopSound(getWorld(), this.pos);
            }
        }
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

    public void onUnloaded() {
        super.onUnloaded();


    }


    public MachineRecipe getOutput() {
        this.output = this.inputSlotA.process();
        return this.output;
    }

    public ContainerDoubleElectricMachine getGuiContainer(EntityPlayer entityPlayer) {
        return new ContainerDoubleElectricMachine(entityPlayer, this, type);
    }


    @Override
    public Set<UpgradableProperty> getUpgradableProperties() {
        return EnumSet.of(
                UpgradableProperty.Processing,
                UpgradableProperty.Transformer,
                UpgradableProperty.EnergyStorage,
                UpgradableProperty.ItemExtract,
                UpgradableProperty.ItemInput
        );
    }


    public final float getChargeLevel() {
        return Math.min((float) this.energy.getEnergy() / (float) this.energy.getCapacity(), 1);
    }

    public double getProgress() {
        return componentProgress.getBar();
    }

    public int getMode() {
        return 0;
    }


}
