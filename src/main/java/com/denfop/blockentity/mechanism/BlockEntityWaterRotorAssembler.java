package com.denfop.blockentity.mechanism;

import com.denfop.IUItem;
import com.denfop.api.Recipes;
import com.denfop.api.blockentity.MultiBlockEntity;
import com.denfop.api.container.CustomWorldContainer;
import com.denfop.api.recipe.*;
import com.denfop.blockentity.base.BlockEntityInventory;
import com.denfop.blocks.BlockTileEntity;
import com.denfop.blocks.mechanism.BlockBaseMachine3Entity;
import com.denfop.componets.AirPollutionComponent;
import com.denfop.componets.Energy;
import com.denfop.componets.SoilPollutionComponent;
import com.denfop.containermenu.ContainerMenuBase;
import com.denfop.containermenu.ContainerMenuWaterRotorAssembler;
import com.denfop.items.reactors.ItemDamage;
import com.denfop.network.DecoderHandler;
import com.denfop.network.EncoderHandler;
import com.denfop.network.packet.CustomPacketBuffer;
import com.denfop.recipe.IInputHandler;
import com.denfop.screen.ScreenIndustrialUpgrade;
import com.denfop.screen.ScreenWaterRotorAssembler;
import com.denfop.utils.Keyboard;
import com.denfop.utils.Localization;
import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

import java.io.IOException;
import java.util.List;

public class BlockEntityWaterRotorAssembler extends BlockEntityInventory implements IUpdateTick, IHasRecipe {

    public final InventoryRecipes inputSlotA;
    public final Energy energy;
    public final int defaultEnergyConsume;
    public final int defaultOperationLength;
    public final int defaultTier;
    public final int defaultEnergyStorage;
    public final InventoryOutput outputSlot;
    private final SoilPollutionComponent pollutionSoil;
    private final AirPollutionComponent pollutionAir;
    public MachineRecipe recipe;
    public short progress;
    public double guiProgress;
    public int energyConsume;
    public int operationLength;
    public int operationsPerTick = 1;

    public BlockEntityWaterRotorAssembler(BlockPos pos, BlockState state) {
        super(BlockBaseMachine3Entity.water_rotor_assembler, pos, state);
        this.inputSlotA = new InventoryRecipes(this, "water_rotor_assembler", this);
        inputSlotA.setStackSizeLimit(1);
        this.defaultEnergyConsume = this.energyConsume = 2;
        this.defaultOperationLength = this.operationLength = 100;
        this.defaultTier = 14;
        this.defaultEnergyStorage = 2 * 100;
        this.recipe = null;
        this.outputSlot = new InventoryOutput(this, 1);
        this.energy = this.addComponent(Energy.asBasicSink(this, defaultEnergyStorage, defaultTier));
        Recipes.recipes.addInitRecipes(this);
        this.pollutionSoil = this.addComponent(new SoilPollutionComponent(this, 0.05));
        this.pollutionAir = this.addComponent(new AirPollutionComponent(this, 0.05));
    }

    public static void addRecipe(int meta, int meta1, ItemStack stack) {
        final IInputHandler input = com.denfop.api.Recipes.inputFactory;
        ((ItemDamage) stack.getItem()).setCustomDamage(stack, 0);
        Recipes.recipes.addRecipe("water_rotor_assembler", new BaseMachineRecipe(
                new Input(
                        input.getInput(new ItemStack(IUItem.water_rod.getStack(meta))),
                        input.getInput(new ItemStack(IUItem.water_rod.getStack(meta))),
                        input.getInput(new ItemStack(IUItem.water_rod.getStack(meta))),
                        input.getInput(new ItemStack(IUItem.water_rod.getStack(meta))),
                        input.getInput(new ItemStack(IUItem.corewater.getStack(meta1), 1))

                ),
                new RecipeOutput(null, stack)
        ));
    }


    public MultiBlockEntity getTeBlock() {
        return BlockBaseMachine3Entity.water_rotor_assembler;
    }

    public BlockTileEntity getBlock() {
        return IUItem.basemachine2.getBlock(getTeBlock());
    }

    public void init() {
        addRecipe(0, 0, new ItemStack(IUItem.water_rotor_wood.getItem()));
        addRecipe(1, 1, new ItemStack(IUItem.water_rotor_bronze.getItem()));
        addRecipe(2, 2, new ItemStack(IUItem.water_rotor_iron.getItem()));
        addRecipe(3, 3, new ItemStack(IUItem.water_rotor_steel.getItem()));
        addRecipe(4, 4, new ItemStack(IUItem.water_rotor_carbon.getItem()));

        addRecipe(5, 5, IUItem.water_iridium.getItemStack());
        addRecipe(6, 6, IUItem.water_compressiridium.getItemStack());
        addRecipe(7, 7, IUItem.water_spectral.getItemStack());
        addRecipe(8, 8, IUItem.water_myphical.getItemStack());
        addRecipe(10, 10, IUItem.water_photon.getItemStack());
        addRecipe(9, 9, IUItem.water_neutron.getItemStack());
        addRecipe(11, 11, IUItem.water_barionrotor.getItemStack());
        addRecipe(12, 12, IUItem.water_adronrotor.getItemStack());
        addRecipe(13, 13, IUItem.water_ultramarinerotor.getItemStack());
    }

    @Override
    public void readContainerPacket(final CustomPacketBuffer customPacketBuffer) {
        super.readContainerPacket(customPacketBuffer);
        try {
            inputSlotA.readFromNbt(getNBTFromSlot(customPacketBuffer));
            progress = (short) DecoderHandler.decode(customPacketBuffer);
            guiProgress = (double) DecoderHandler.decode(customPacketBuffer);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

    }

    @Override
    public CustomPacketBuffer writeContainerPacket() {
        final CustomPacketBuffer packet = super.writeContainerPacket();
        try {
            EncoderHandler.encode(packet, inputSlotA);
            EncoderHandler.encode(packet, progress);
            EncoderHandler.encode(packet, guiProgress);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        return packet;
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

    public void readFromNBT(CompoundTag nbttagcompound) {
        super.readFromNBT(nbttagcompound);
        this.progress = nbttagcompound.getShort("progress");

    }

    public CompoundTag writeToNBT(CompoundTag nbttagcompound) {
        super.writeToNBT(nbttagcompound);
        nbttagcompound.putShort("progress", this.progress);
        return nbttagcompound;
    }

    public void onLoaded() {
        super.onLoaded();
        inputSlotA.load();
        this.getOutput();
    }

    public void operate(MachineRecipe output) {
        for (int i = 0; i < this.operationsPerTick; i++) {
            List<ItemStack> processResult = output.getRecipe().output.items;
            operateOnce(processResult);
            if (!this.inputSlotA.continue_process(this.recipe) || !this.outputSlot.canAdd(output.getRecipe().output.items)) {
                getOutput();
                break;
            }

            if (this.recipe == null) {
                break;
            }
        }


    }

    public MachineRecipe getOutput() {
        this.recipe = this.inputSlotA.process();


        return this.recipe;
    }

    public void operateOnce(List<ItemStack> processResult) {

        this.inputSlotA.consume();

        this.outputSlot.addAll(processResult);
    }

    @Override
    public void updateEntityServer() {
        super.updateEntityServer();
        if (this.recipe != null && this.energy.canUseEnergy(energyConsume) && !this.inputSlotA.isEmpty() && this.outputSlot.canAdd(
                this.recipe.getRecipe().getOutput().items)) {


            this.progress += 1;
            this.energy.useEnergy(energyConsume);
            double k = this.progress;

            this.guiProgress = (k / this.operationLength);
            if (this.progress >= this.operationLength) {
                this.guiProgress = 0;
                operate(this.recipe);
                this.progress = 0;


            }

        } else {
            if (this.recipe == null) {
                this.progress = 0;

            }


        }
    }


    @Override
    public ContainerMenuWaterRotorAssembler getGuiContainer(final Player entityPlayer) {
        return new ContainerMenuWaterRotorAssembler(this, entityPlayer);
    }

    @Override
    @OnlyIn(Dist.CLIENT)
    public ScreenIndustrialUpgrade<ContainerMenuBase<? extends CustomWorldContainer>> getGui(Player var1, ContainerMenuBase<? extends CustomWorldContainer> menu) {
        return new ScreenWaterRotorAssembler((ContainerMenuWaterRotorAssembler) menu);
    }


    @Override
    public void onUpdate() {

    }

    @Override
    public int getInventoryStackLimit() {
        return 1;
    }

    @Override
    public MachineRecipe getRecipeOutput() {
        return this.recipe;
    }

    @Override
    public void setRecipeOutput(final MachineRecipe output) {
        this.recipe = output;
    }

}
