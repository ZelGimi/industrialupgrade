package com.denfop.blockentity.base;

import com.denfop.api.blockentity.MultiBlockEntity;
import com.denfop.api.recipe.FluidHandlerRecipe;
import com.denfop.api.recipe.InventoryOutput;
import com.denfop.api.recipe.RecipeOutput;
import com.denfop.api.upgrades.IUpgradableBlock;
import com.denfop.componets.Fluids;
import com.denfop.containermenu.ContainerMenuObsidianGenerator;
import com.denfop.inventory.Inventory;
import com.denfop.inventory.InventoryFluidByList;
import com.denfop.inventory.InventoryUpgrade;
import com.denfop.network.DecoderHandler;
import com.denfop.network.EncoderHandler;
import com.denfop.network.packet.CustomPacketBuffer;
import com.denfop.sound.EnumSound;
import com.denfop.utils.Keyboard;
import com.denfop.utils.Localization;
import com.denfop.utils.ParticleUtils;
import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraftforge.fluids.capability.templates.FluidTank;
import org.apache.commons.lang3.mutable.MutableObject;

import java.io.IOException;
import java.util.List;

public abstract class BlockEntityBaseObsidianGenerator extends BlockEntityElectricMachine
        implements IUpgradableBlock {

    public final InventoryOutput outputSlot1;
    public final InventoryFluidByList fluidSlot1;
    public final InventoryFluidByList fluidSlot2;
    public final double defaultEnergyConsume;
    public final int defaultOperationLength;
    public final int defaultTier;
    public final double defaultEnergyStorage;
    public final InventoryUpgrade upgradeSlot;
    public final FluidTank fluidTank1;
    public final FluidTank fluidTank2;
    private final FluidHandlerRecipe fluid_handler;
    public double energyConsume;
    public int operationLength;
    public int operationsPerTick;
    protected short progress;
    protected double guiProgress;


    public BlockEntityBaseObsidianGenerator(int energyPerTick, int length, int outputSlots, MultiBlockEntity block, BlockPos pos, BlockState state) {
        super(energyPerTick * length, 1, outputSlots, block, pos, state);
        this.progress = 0;
        this.defaultEnergyConsume = this.energyConsume = energyPerTick;
        this.defaultOperationLength = this.operationLength = length;
        this.defaultTier = 1;
        this.defaultEnergyStorage = energyPerTick * length;
        this.upgradeSlot = new InventoryUpgrade(this, 4);
        this.outputSlot1 = new InventoryOutput(this, 1);

        this.fluidSlot1 = new InventoryFluidByList(this, 1, net.minecraft.world.level.material.Fluids.WATER);
        this.fluidSlot2 = new InventoryFluidByList(this, 1, net.minecraft.world.level.material.Fluids.LAVA);
        Fluids fluids = this.addComponent(new Fluids(this));
        this.fluidTank1 = fluids.addTank(
                "fluidTank1",
                12 * 1000,
                Fluids.fluidPredicate(net.minecraft.world.level.material.Fluids.WATER),
                Inventory.TypeItemSlot.INPUT

        );
        this.fluidTank2 = fluids.addTank(
                "fluidTank2",
                12 * 1000,
                Fluids.fluidPredicate(net.minecraft.world.level.material.Fluids.LAVA),
                Inventory.TypeItemSlot.INPUT

        );
        this.fluid_handler = new FluidHandlerRecipe("obsidian", fluids);
    }

    public static int applyModifier(int base, int extra, double multiplier) {
        double ret = Math.round((base + extra) * multiplier);
        return (ret > 2.147483647E9D) ? Integer.MAX_VALUE : (int) ret;
    }


    public void readFromNBT(CompoundTag nbttagcompound) {
        super.readFromNBT(nbttagcompound);
        this.progress = nbttagcompound.getShort("progress");

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

    public CompoundTag writeToNBT(CompoundTag nbttagcompound) {
        super.writeToNBT(nbttagcompound);
        nbttagcompound.putShort("progress", this.progress);
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
        if (!this.getWorld().isClientSide) {
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
        if (check || (this.fluid_handler.output() == null && this.fluidTank2.getFluidAmount() > 0 && this.fluidTank1.getFluidAmount() > 0)) {
            this.fluid_handler.getOutput();
        } else {
            if (this.fluid_handler.output() != null && !this.fluid_handler.checkFluids()) {
                this.fluid_handler.setOutput(null);
            }
        }
        if (this.getActive() && this.level.getGameTime() % 5 == 0) {
            ParticleUtils.spawnObsidianGeneratorParticles(level, pos, level.random);
        }

        if (this.fluid_handler.output() != null && this.fluid_handler.canOperate() && this.energy.canUseEnergy(energyConsume) && this.outputSlot.canAdd(
                this.fluid_handler.output().getOutput().items.get(0))) {
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
        this.outputSlot.addAll(processResult);
    }


    public abstract String getInventoryName();

    public ContainerMenuObsidianGenerator getGuiContainer(Player entityPlayer) {
        return new ContainerMenuObsidianGenerator(entityPlayer, this);
    }

    @Override
    public SoundEvent getSound() {
        return EnumSound.gen_obsidiant.getSoundEvent();
    }


}
