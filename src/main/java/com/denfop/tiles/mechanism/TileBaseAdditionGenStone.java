package com.denfop.tiles.mechanism;

import com.denfop.IUCore;
import com.denfop.Localization;
import com.denfop.api.recipe.IUpdateTick;
import com.denfop.api.recipe.InventoryRecipes;
import com.denfop.api.recipe.MachineRecipe;
import com.denfop.api.upgrades.IUpgradableBlock;
import com.denfop.api.upgrades.UpgradableProperty;
import com.denfop.container.ContainerAdditionGenStone;
import com.denfop.gui.GuiAdditionGenStone;
import com.denfop.invslot.InventoryUpgrade;
import com.denfop.network.DecoderHandler;
import com.denfop.network.EncoderHandler;
import com.denfop.network.IUpdatableTileEvent;
import com.denfop.network.packet.CustomPacketBuffer;
import com.denfop.tiles.base.TileElectricMachine;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import org.lwjgl.input.Keyboard;

import java.io.IOException;
import java.util.EnumSet;
import java.util.List;
import java.util.Set;

public abstract class TileBaseAdditionGenStone extends TileElectricMachine implements
        IUpgradableBlock, IUpdateTick, IUpdatableTileEvent {

    public final double defaultEnergyConsume;
    public final int defaultOperationLength;
    public final int defaultTier;
    public final double defaultEnergyStorage;
    public final InventoryUpgrade upgradeSlot;
    public final ItemStack diorite;
    public final ItemStack andesite;
    public final ItemStack granite;
    public double energyConsume;
    public Mode mode;
    public int operationLength;

    public int operationsPerTick;
    public InventoryRecipes inputSlotA;
    public MachineRecipe output;
    protected short progress;
    protected double guiProgress;

    public TileBaseAdditionGenStone(int energyPerTick, int length, int outputSlots) {
        this(energyPerTick, length, outputSlots, 1);
    }

    public TileBaseAdditionGenStone(int energyPerTick, int length, int outputSlots, int aDefaultTier) {
        super((double) energyPerTick * length, 1, outputSlots);
        this.progress = 0;
        this.defaultEnergyConsume = this.energyConsume = energyPerTick;
        this.defaultOperationLength = this.operationLength = length;
        this.defaultTier = aDefaultTier;
        this.defaultEnergyStorage = energyPerTick * length;
        this.upgradeSlot = new InventoryUpgrade(this, 4);
        this.output = null;
        this.mode = Mode.GRANITE;
        this.granite = new ItemStack(Blocks.STONE, 8, 1);
        this.diorite = new ItemStack(Blocks.STONE, 8, 3);
        this.andesite = new ItemStack(Blocks.STONE, 8, 5);

    }

    public static int applyModifier(int base, int extra, double multiplier) {
        double ret = Math.round((base + extra) * multiplier);
        return (ret > 2.147483647E9D) ? Integer.MAX_VALUE : (int) ret;
    }

    public void readFromNBT(NBTTagCompound nbttagcompound) {
        super.readFromNBT(nbttagcompound);
        this.progress = nbttagcompound.getShort("progress");
        this.mode = Mode.values()[nbttagcompound.getInteger("mode")];
    }

    @Override
    public void onUpdate() {

    }

    @Override
    public void readContainerPacket(final CustomPacketBuffer customPacketBuffer) {
        super.readContainerPacket(customPacketBuffer);
        try {
            guiProgress = (double) DecoderHandler.decode(customPacketBuffer);
            mode = Mode.values()[(int) DecoderHandler.decode(customPacketBuffer)];
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

    }

    @Override
    public CustomPacketBuffer writeContainerPacket() {
        final CustomPacketBuffer packet = super.writeContainerPacket();
        try {
            EncoderHandler.encode(packet, guiProgress);
            EncoderHandler.encode(packet, mode);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        return packet;
    }

    public Mode getMode() {
        return mode;
    }

    @Override
    public MachineRecipe getRecipeOutput() {
        return this.output;
    }

    @Override
    public void setRecipeOutput(final MachineRecipe output) {
        this.output = output;
    }


    @SideOnly(Side.CLIENT)
    public GuiAdditionGenStone getGui(EntityPlayer entityPlayer, boolean isAdmin) {
        return new GuiAdditionGenStone(getGuiContainer(entityPlayer));
    }

    public String getStartSoundFile() {
        return "Machines/gen_cobblectone.ogg";
    }

    public String getInterruptSoundFile() {
        return "Machines/InterruptOne.ogg";
    }


    public Set<UpgradableProperty> getUpgradableProperties() {
        return EnumSet.of(
                UpgradableProperty.Processing,
                UpgradableProperty.Transformer,
                UpgradableProperty.EnergyStorage,
                UpgradableProperty.ItemExtract
        );
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

    public NBTTagCompound writeToNBT(NBTTagCompound nbttagcompound) {
        super.writeToNBT(nbttagcompound);
        nbttagcompound.setShort("progress", this.progress);
        nbttagcompound.setInteger("mode", this.mode.ordinal());

        return nbttagcompound;
    }

    public double getProgress() {
        return this.guiProgress;
    }

    public void onLoaded() {
        super.onLoaded();
        if (IUCore.proxy.isSimulating()) {
            this.setOverclockRates();
            inputSlotA.load();
            this.getOutput();
        }


    }


    public void updateEntityServer() {
        super.updateEntityServer();

        MachineRecipe output = this.output;
        if (output != null && canAdd() && this.energy.getEnergy() >= this.energyConsume) {
            if (!this.getActive()) {
                setActive(true);
                if (this.operationLength > this.defaultOperationLength * 0.1) {
                    initiate(0);
                }
            }
            this.progress = (short) (this.progress + 1);
            this.energy.useEnergy(this.energyConsume);
            double k = this.progress;

            this.guiProgress = (k / this.operationLength);
            if (this.progress >= this.operationLength) {
                this.guiProgress = 0;
                operate(output);
                this.progress = 0;
                if (this.operationLength > this.defaultOperationLength * 0.1 || (this.getTypeAudio() != valuesAudio[2 % valuesAudio.length])) {
                    initiate(2);
                }
            }
        } else {
            if (output == null && this.getActive()) {
                this.progress = 0;
                if (this.operationLength > this.defaultOperationLength * 0.1 || (this.getTypeAudio() != valuesAudio[1 % valuesAudio.length])) {
                    initiate(1);
                }
            }
            if (this.getActive()) {
                setActive(false);
            }
        }
        if (this.upgradeSlot.tickNoMark()) {
            setOverclockRates();
        }

    }

    private boolean canAdd() {
        switch (this.getMode()) {
            default:
                return this.outputSlot.canAdd(this.output.getRecipe().output.items);
            case DIORITE:
                return this.outputSlot.canAdd(this.diorite);

            case ANDESITE:
                return this.outputSlot.canAdd(this.andesite);

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
        this.operationsPerTick = Math.min(1, this.operationsPerTick);
        dischargeSlot.setTier(tier);
    }

    public void operate(MachineRecipe output) {

        List<ItemStack> processResult = output.getRecipe().output.items;
        operateOnce(output, processResult);


    }

    public void operateOnce(MachineRecipe output, List<ItemStack> processResult) {
        switch (this.getMode()) {
            default:
                this.outputSlot.addAll(processResult);
                break;
            case DIORITE:
                this.outputSlot.add(this.diorite);
                break;
            case ANDESITE:
                this.outputSlot.add(this.andesite);
                break;
        }
    }

    public MachineRecipe getOutput() {
        this.output = this.inputSlotA.process();
        return this.output;
    }

    @Override
    public void updateTileServer(final EntityPlayer entityPlayer, final double i) {
        if (i == 10) {
            super.updateTileServer(entityPlayer, i);
        } else {
            this.mode = Mode.values()[(this.mode.ordinal() + 1) % Mode.values().length];
        }
    }

    public ContainerAdditionGenStone getGuiContainer(EntityPlayer entityPlayer) {
        return new ContainerAdditionGenStone(entityPlayer, this);
    }


    public void onGuiClosed(EntityPlayer entityPlayer) {
    }

    public enum Mode {
        GRANITE,
        DIORITE,
        ANDESITE
    }

}

