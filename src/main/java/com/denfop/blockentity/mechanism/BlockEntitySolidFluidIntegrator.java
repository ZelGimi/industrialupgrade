package com.denfop.blockentity.mechanism;

import com.denfop.IUItem;
import com.denfop.api.Recipes;
import com.denfop.api.blockentity.MultiBlockEntity;
import com.denfop.api.container.CustomWorldContainer;
import com.denfop.api.recipe.*;
import com.denfop.api.upgrades.BlockEntityUpgrade;
import com.denfop.api.upgrades.EnumBlockEntityUpgrade;
import com.denfop.blockentity.base.BlockEntityElectricMachine;
import com.denfop.blocks.BlockTileEntity;
import com.denfop.blocks.FluidName;
import com.denfop.blocks.mechanism.BlockBaseMachine3Entity;
import com.denfop.componets.AirPollutionComponent;
import com.denfop.componets.Fluids;
import com.denfop.componets.SoilPollutionComponent;
import com.denfop.containermenu.ContainerMenuBase;
import com.denfop.containermenu.ContainerMenuSolidFluidIntegrator;
import com.denfop.inventory.Inventory;
import com.denfop.inventory.InventoryFluid;
import com.denfop.inventory.InventoryFluidByList;
import com.denfop.inventory.InventoryUpgrade;
import com.denfop.network.DecoderHandler;
import com.denfop.network.EncoderHandler;
import com.denfop.network.IUpdatableTileEvent;
import com.denfop.network.packet.CustomPacketBuffer;
import com.denfop.recipe.IInputHandler;
import com.denfop.screen.ScreenIndustrialUpgrade;
import com.denfop.screen.ScreenSolidFluidIntegrator;
import com.denfop.utils.Keyboard;
import com.denfop.utils.Localization;
import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
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

public class BlockEntitySolidFluidIntegrator extends BlockEntityElectricMachine implements
        BlockEntityUpgrade, IUpdatableTileEvent, IHasRecipe {

    public final InventoryUpgrade upgradeSlot;
    public final Fluids.InternalFluidTank fluidTank1;
    public final Fluids.InternalFluidTank fluidTank2;
    public final InventoryFluidByList fluidSlot1;
    public final InventoryFluidByList fluidSlot2;
    public final InventoryOutput output1;
    public final Fluids.InternalFluidTank fluidTank3;
    public final InventoryFluidByList fluidSlot3;
    public final FluidHandlerRecipe fluid_handler;
    public final double defaultEnergyConsume;
    public final int defaultOperationLength;
    public final int defaultTier;
    public final double defaultEnergyStorage;
    public double energyConsume;
    public int operationLength;
    public int operationsPerTick;
    public double guiProgress;
    protected short progress;

    public BlockEntitySolidFluidIntegrator(BlockPos pos, BlockState state) {
        super(200, 1, 1, BlockBaseMachine3Entity.solid_fluid_integrator, pos, state);
        Recipes.recipes.addInitRecipes(this);

        this.progress = 0;
        this.defaultEnergyConsume = this.energyConsume = 1;
        this.defaultOperationLength = this.operationLength = 100;
        this.defaultTier = 1;
        this.defaultEnergyStorage = 100;
        Fluids fluids = this.addComponent(new Fluids(this));
        this.fluidTank1 = fluids.addTankInsert("fluidTank1", 12 * 1000);
        this.output1 = new InventoryOutput(this, 3);
        this.fluidTank2 = fluids.addTank("fluidTank2", 12 * 1000, Inventory.TypeItemSlot.OUTPUT);
        this.fluidTank3 = fluids.addTankInsert("fluidTank3", 12 * 1000);


        this.fluid_handler = new FluidHandlerRecipe("solid_fluid_integrator", fluids);
        this.fluidTank1.setAcceptedFluids(Fluids.fluidPredicate(this.fluid_handler.getFluids(0)));
        this.fluidTank3.setAcceptedFluids(Fluids.fluidPredicate(this.fluid_handler.getFluids(1)));
        this.fluidTank2.setAcceptedFluids(Fluids.fluidPredicate(this.fluid_handler.getOutputFluids(0)));
        this.fluidSlot1 = new InventoryFluidByList(this, 1, this.fluid_handler.getFluids(0));
        this.fluidSlot3 = new InventoryFluidByList(this, 1, this.fluid_handler.getFluids(1));
        this.fluidSlot2 = new InventoryFluidByList(this, 1, this.fluid_handler.getOutputFluids(0));
        this.fluidSlot2.setTypeFluidSlot(InventoryFluid.TypeFluidSlot.OUTPUT);
        this.upgradeSlot = new InventoryUpgrade(this, 4);
        this.addComponent(new SoilPollutionComponent(this, 0.1));
        this.addComponent(new AirPollutionComponent(this, 0.1));

    }

    public static void addRecipe(FluidStack fluidStack, FluidStack fluidStack1, ItemStack output, FluidStack outputfluidStack) {
        final IInputHandler input = com.denfop.api.Recipes.inputFactory;

        Recipes.recipes.getRecipeFluid().addRecipe("solid_fluid_integrator", new BaseFluidMachineRecipe(new InputFluid(
                fluidStack, fluidStack1), new RecipeOutput(null, output), Collections.singletonList(
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

    public ContainerMenuSolidFluidIntegrator getGuiContainer(final Player var1) {
        return new ContainerMenuSolidFluidIntegrator(var1, this);

    }

    @Override
    @OnlyIn(Dist.CLIENT)
    public ScreenIndustrialUpgrade<ContainerMenuBase<? extends CustomWorldContainer>> getGui(Player var1, ContainerMenuBase<? extends CustomWorldContainer> menu) {

        return new ScreenSolidFluidIntegrator((ContainerMenuSolidFluidIntegrator) menu);
    }

    @Override
    public void init() {
        addRecipe(new FluidStack(FluidName.fluidnitrogenhydride.getInstance().get(), 135), new FluidStack(
                FluidName.fluidchlorum.getInstance().get(),
                100
        ), new ItemStack(IUItem.iudust.getStack(61), 1), new FluidStack(FluidName.fluidnitrogen.getInstance().get(), 10));

        addRecipe(new FluidStack(FluidName.fluidmonochlorobenzene.getInstance().get(), 200), new FluidStack(
                FluidName.fluidnitrogenhydride.getInstance().get(),
                400
        ), new ItemStack(IUItem.iudust.getStack(61), 1), new FluidStack(FluidName.fluidaniline.getInstance().get(), 200));

        addRecipe(new FluidStack(FluidName.fluidmotoroil.getInstance().get(), 500), new FluidStack(
                FluidName.fluidcoolant.getInstance().get(),
                500
        ), new ItemStack(IUItem.cooling_mixture.getItem()), new FluidStack(FluidName.fluidnitrogen.getInstance().get(), 100));

        addRecipe(

                new FluidStack(FluidName.fluidoxygen.getInstance().get(), 100),
                new FluidStack(FluidName.fluidpolyprop.getInstance().get(), 250),

                new ItemStack(IUItem.crafting_elements.getStack(484), 1), new FluidStack(FluidName.fluidcarbonmonoxide.getInstance().get(), 50)
        );
        addRecipe(

                new FluidStack(FluidName.fluidoxygen.getInstance().get(), 100),
                new FluidStack(FluidName.fluidpolyeth.getInstance().get(), 250),

                new ItemStack(IUItem.crafting_elements.getStack(483), 1), new FluidStack(FluidName.fluidcarbonmonoxide.getInstance().get()
                        , 50)
        );
    }


    @Override
    public BlockTileEntity getBlock() {
        return IUItem.basemachine2.getBlock(getTeBlock());
    }

    @Override
    public MultiBlockEntity getTeBlock() {
        return BlockBaseMachine3Entity.solid_fluid_integrator;
    }

    public void onLoaded() {
        super.onLoaded();
        if (!level.isClientSide) {
            this.fluid_handler.load();
            this.fluid_handler.checkOutput();
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
        if (this.fluidTank3.getCapacity() - this.fluidTank3.getFluidAmount() >= 1000 && this.fluidSlot3.transferToTank(
                this.fluidTank3,
                output1,
                true
        ) && (output1.getValue() == null || this.output1.canAdd(output1.getValue()))) {
            this.fluidSlot3.transferToTank(this.fluidTank3, output1, false);
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

        if (check || (this.fluid_handler.output() == null && this.fluidTank1.getFluidAmount() > 0 && this.fluidTank3.getFluidAmount() > 0)) {
            this.fluid_handler.getOutput();
        } else {
            if (this.fluid_handler.output() != null && (this.fluidTank1.getFluidAmount() ==
                    0 || this.fluidTank3.getFluidAmount() == 0)) {
                this.fluid_handler.setOutput(null);
            }
        }


        if (this.fluid_handler.output() != null && this.outputSlot.canAdd(this.fluid_handler
                .output()
                .getOutput().items) && fluid_handler.canOperate() && this.fluid_handler.canFillFluid() && this.energy.canUseEnergy(
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
            if (this.fluid_handler.output() == null) {
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

            this.fluid_handler.checkOutput();
            if (this.fluid_handler.output() == null) {
                break;
            }
        }
    }

    public void operateOnce() {
        this.fluid_handler.consume();
        this.outputSlot.add(this.fluid_handler.output().getOutput().items);
        this.fluid_handler.fillFluid();
    }

    @Override
    public Set<EnumBlockEntityUpgrade> getUpgradableProperties() {
        return EnumSet.of(
                EnumBlockEntityUpgrade.Processing,
                EnumBlockEntityUpgrade.Transformer,
                EnumBlockEntityUpgrade.EnergyStorage,
                EnumBlockEntityUpgrade.ItemInput,
                EnumBlockEntityUpgrade.FluidExtract, EnumBlockEntityUpgrade.ItemExtract
        );
    }


    public double getProgress() {
        return this.guiProgress;
    }

}
