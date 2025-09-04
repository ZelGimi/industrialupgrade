package com.denfop.blockentity.mechanism;

import com.denfop.IUItem;
import com.denfop.api.Recipes;
import com.denfop.api.blockentity.MultiBlockEntity;
import com.denfop.api.container.CustomWorldContainer;
import com.denfop.api.recipe.*;
import com.denfop.api.upgrades.IUpgradableBlock;
import com.denfop.api.upgrades.UpgradableProperty;
import com.denfop.blockentity.base.BlockEntityElectricMachine;
import com.denfop.blocks.BlockTileEntity;
import com.denfop.blocks.FluidName;
import com.denfop.blocks.mechanism.BlockBaseMachine3Entity;
import com.denfop.componets.AirPollutionComponent;
import com.denfop.componets.Fluids;
import com.denfop.componets.SoilPollutionComponent;
import com.denfop.containermenu.ContainerMenuBase;
import com.denfop.containermenu.ContainerMenuSingleFluidAdapter;
import com.denfop.inventory.Inventory;
import com.denfop.inventory.InventoryFluid;
import com.denfop.inventory.InventoryFluidByList;
import com.denfop.inventory.InventoryUpgrade;
import com.denfop.network.DecoderHandler;
import com.denfop.network.EncoderHandler;
import com.denfop.network.IUpdatableTileEvent;
import com.denfop.network.packet.CustomPacketBuffer;
import com.denfop.recipe.IInputHandler;
import com.denfop.recipe.IInputItemStack;
import com.denfop.screen.ScreenIndustrialUpgrade;
import com.denfop.screen.ScreenSingleFluidAdapter;
import com.denfop.utils.Keyboard;
import com.denfop.utils.Localization;
import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.fluids.FluidStack;
import org.apache.commons.lang3.mutable.MutableObject;

import java.io.IOException;
import java.util.Collections;
import java.util.EnumSet;
import java.util.List;
import java.util.Set;

public class BlockEntitySingleFluidAdapter extends BlockEntityElectricMachine implements
        IUpgradableBlock, IUpdateTick, IUpdatableTileEvent, IHasRecipe {

    public final InventoryUpgrade upgradeSlot;
    public final InventoryRecipes inputSlotA;
    public final Fluids.InternalFluidTank fluidTank1;
    public final Fluids.InternalFluidTank fluidTank2;
    public final InventoryFluidByList fluidSlot1;
    public final InventoryFluidByList fluidSlot2;
    public final InventoryOutput output1;
    public final FluidHandlerRecipe fluid_handler;
    public final double defaultEnergyConsume;
    public final int defaultOperationLength;
    public final int defaultTier;
    public final double defaultEnergyStorage;
    public MachineRecipe output;
    public double energyConsume;
    public int operationLength;
    public int operationsPerTick;
    public double guiProgress;
    protected short progress;

    public BlockEntitySingleFluidAdapter(BlockPos pos, BlockState state) {
        super(200, 1, 1, BlockBaseMachine3Entity.single_fluid_adapter, pos, state);
        Recipes.recipes.addInitRecipes(this);

        this.progress = 0;
        this.defaultEnergyConsume = this.energyConsume = 1;
        this.defaultOperationLength = this.operationLength = 200;
        this.defaultTier = 1;
        this.defaultEnergyStorage = 100;
        Fluids fluids = this.addComponent(new Fluids(this));
        this.fluidTank1 = fluids.addTankInsert("fluidTank1", 12 * 1000);
        this.output1 = new InventoryOutput(this, 2);
        this.fluidTank2 = fluids.addTank("fluidTank2", 12 * 1000, Inventory.TypeItemSlot.OUTPUT);
        this.inputSlotA = new InventoryRecipes(this, "single_fluid_adapter", this, fluidTank1);


        this.fluid_handler = new FluidHandlerRecipe("single_fluid_adapter", fluids);
        this.fluidTank1.setAcceptedFluids(Fluids.fluidPredicate(this.fluid_handler.getFluids(0)));
        this.fluidTank2.setAcceptedFluids(Fluids.fluidPredicate(this.fluid_handler.getOutputFluids(0)));
        this.fluidSlot1 = new InventoryFluidByList(this, 1, this.fluid_handler.getFluids(0));
        this.fluidSlot2 = new InventoryFluidByList(this, 1, this.fluid_handler.getOutputFluids(0));
        this.fluidSlot2.setTypeFluidSlot(InventoryFluid.TypeFluidSlot.OUTPUT);
        this.upgradeSlot = new InventoryUpgrade(this, 4);
        this.addComponent(new SoilPollutionComponent(this, 0.1));
        this.addComponent(new AirPollutionComponent(this, 0.1));

    }

    public static void addRecipe(ItemStack container, FluidStack fluidStack, FluidStack outputfluidStack) {
        final IInputHandler input = com.denfop.api.Recipes.inputFactory;
        Recipes.recipes.addRecipe(
                "single_fluid_adapter",
                new BaseMachineRecipe(
                        new Input(fluidStack, input.getInput(container)),
                        new RecipeOutput(null, container)
                )
        );
        Recipes.recipes.getRecipeFluid().addRecipe("single_fluid_adapter", new BaseFluidMachineRecipe(new InputFluid(
                container,
                fluidStack
        ), Collections.singletonList(
                outputfluidStack)));

    }

    public static void addRecipe(IInputItemStack container, FluidStack fluidStack, FluidStack outputfluidStack) {
        final IInputHandler input = com.denfop.api.Recipes.inputFactory;
        Recipes.recipes.addRecipe(
                "single_fluid_adapter",
                new BaseMachineRecipe(
                        new Input(fluidStack, input.getInput(container)),
                        new RecipeOutput(null, container.getInputs().get(0))
                )
        );
        Recipes.recipes.getRecipeFluid().addRecipe("single_fluid_adapter", new BaseFluidMachineRecipe(new InputFluid(
                container,
                fluidStack
        ), Collections.singletonList(
                outputfluidStack)));

    }

    public void addInformation(ItemStack stack, List<String> tooltip) {
        if (!Keyboard.isKeyDown(Keyboard.KEY_LSHIFT)) {
            tooltip.add(Localization.translate("press.lshift"));
        }
        if (Keyboard.isKeyDown(Keyboard.KEY_LSHIFT)) {
            tooltip.add(Localization.translate("iu.machines_work_energy") + 200 + Localization.translate(
                    "iu.machines_work_energy_type_eu"));
            tooltip.add(Localization.translate("iu.machines_work_length") + 1);
        }
        super.addInformation(stack, tooltip);

    }

    public ContainerMenuSingleFluidAdapter getGuiContainer(final Player var1) {
        return new ContainerMenuSingleFluidAdapter(var1, this);

    }

    @Override
    @OnlyIn(Dist.CLIENT)
    public ScreenIndustrialUpgrade<ContainerMenuBase<? extends CustomWorldContainer>> getGui(Player var1, ContainerMenuBase<? extends CustomWorldContainer> menu) {

        return new ScreenSingleFluidAdapter((ContainerMenuSingleFluidAdapter) menu);
    }

    @Override
    public void init() {
        addRecipe(new ItemStack(IUItem.phosphorus_oxide.getItem()),
                new FluidStack(Fluids.WATER, 300), new FluidStack(FluidName.fluidorthophosphoricacid.getInstance().get()
                        , 200)
        );

        addRecipe(
                new ItemStack(IUItem.iudust.getStack(73), 2),
                new FluidStack(FluidName.fluidchloroethane.getInstance().get(), 250),
                new FluidStack(FluidName.fluidtetraethyllead.getInstance().get()
                        , 250)
        );


        addRecipe(
                Recipes.inputFactory.getInput("forge:dusts/coal"),
                new FluidStack(FluidName.fluidoxygen.getInstance().get(), 400),
                new FluidStack(FluidName.fluidcarbonmonoxide.getInstance().get()
                        , 200)
        );
        addRecipe(
                new ItemStack(IUItem.iudust.getStack(1), 4),
                new FluidStack(FluidName.fluidpropane.getInstance().get(), 400),
                new FluidStack(FluidName.fluidmethylpentane.getInstance().get()
                        , 200)
        );
        addRecipe(
                new ItemStack(IUItem.wolframite.getItem()),
                new FluidStack(FluidName.fluidquartz.getInstance().get(), 144),
                new FluidStack(FluidName.fluidtemperedglass.getInstance().get()
                        , 144)
        );
        addRecipe(
                new ItemStack(IUItem.crafting_elements.getStack(465), 1),
                new FluidStack(Fluids.WATER, 50),
                new FluidStack(FluidName.fluidpetroleum.getInstance().get()
                        , 1000)
        );

        addRecipe(
                new ItemStack(Items.SUGAR),
                new FluidStack(FluidName.fluidroyaljelly.getInstance().get(), 200),
                new FluidStack(FluidName.fluidapianroyaljelly.getInstance().get()
                        , 200)
        );
        addRecipe(new ItemStack(Items.LAPIS_LAZULI, 16), new FluidStack(FluidName.fluiddistilled_water.getInstance().get(), 4000),
                new FluidStack(FluidName.fluidcoolant.getInstance().get(), 1000)
        );
        addRecipe(IUItem.cfPowder, new FluidStack(Fluids.WATER, 1000),
                new FluidStack(FluidName.fluidconstruction_foam.getInstance().get(), 1000)
        );

    }

    @Override
    public BlockTileEntity getBlock() {
        return IUItem.basemachine2.getBlock(getTeBlock());
    }

    @Override
    public MultiBlockEntity getTeBlock() {
        return BlockBaseMachine3Entity.single_fluid_adapter;
    }

    @Override
    public void onUpdate() {

    }

    public void onLoaded() {
        super.onLoaded();
        if (!level.isClientSide) {
            inputSlotA.load();
            this.fluid_handler.load();
            this.getOutput();
        }


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

    @Override
    public MachineRecipe getRecipeOutput() {
        return this.output;
    }

    @Override
    public void setRecipeOutput(final MachineRecipe output) {
        this.output = output;
        this.fluid_handler.setOutput(null);
    }

    public MachineRecipe getOutput() {
        this.output = this.inputSlotA.process();
        this.fluid_handler.setOutput(null);
        return this.output;
    }

    public void updateEntityServer() {
        super.updateEntityServer();
        MutableObject<ItemStack> output1 = new MutableObject<>();
        boolean check = false;
        if (this.fluidTank1.getCapacity() - this.fluidTank1.getFluidAmount() >= 1000 && this.fluidSlot1.transferToTank(
                this.fluidTank1,
                output1,
                true
        ) && (output1.getValue() == null || this.output1.canAdd(output1.getValue()))) {
            this.fluidSlot1.transferToTank(this.fluidTank1, output1, false);
            if (output1.getValue() != null) {
                this.output1.add(output1.getValue());
            }
            check = true;
        }
        if (this.fluidTank2.getFluidAmount() >= 1000 && this.fluidSlot2.transferFromTank(
                this.fluidTank2,
                output1,
                true
        ) && (output1.getValue() == null || this.output1.canAdd(output1.getValue()))) {
            this.fluidSlot2.transferFromTank(this.fluidTank2, output1, false);
            if (output1.getValue() != null) {
                this.output1.add(output1.getValue());
            }
            check = true;
        }

        if (check || (this.fluid_handler.output() == null && this.output != null && this.fluidTank1.getFluidAmount() > 0)) {
            this.fluid_handler.getOutput(this.inputSlotA.get(0));
        } else {
            if (this.fluid_handler.output() != null && this.output == null) {
                this.fluid_handler.setOutput(null);
            }
        }


        if (this.output != null && !this.inputSlotA.isEmpty() && this.inputSlotA.continue_process(this.output) && this.fluid_handler.output() != null && fluid_handler.canOperate() && this.fluid_handler.canFillFluid() && this.energy.canUseEnergy(
                energyConsume)) {
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
                operate();
                this.progress = 0;
                initiate(2);
            }
        } else {
            if (this.progress != 0 && getActive()) {
                initiate(0);
            }
            if (this.output == null) {
                this.progress = 0;
                guiProgress = 0;
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

    public void operate() {
        for (int i = 0; i < 1; i++) {
            operateOnce();

            this.getOutput();
            if (this.output == null) {
                break;
            }
        }
    }

    public void operateOnce() {
        this.inputSlotA.consume();
        this.fluid_handler.fillFluid();
    }

    @Override
    public Set<UpgradableProperty> getUpgradableProperties() {
        return EnumSet.of(
                UpgradableProperty.Processing,
                UpgradableProperty.Transformer,
                UpgradableProperty.EnergyStorage,
                UpgradableProperty.ItemInput,
                UpgradableProperty.FluidExtract,
                UpgradableProperty.ItemExtract
        );
    }


    public double getProgress() {
        return this.guiProgress;
    }

}
