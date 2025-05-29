package com.denfop.tiles.mechanism;

import com.denfop.IUItem;
import com.denfop.Localization;
import com.denfop.api.Recipes;
import com.denfop.api.inv.IAdvInventory;
import com.denfop.api.recipe.*;
import com.denfop.api.tile.IMultiTileBlock;
import com.denfop.api.upgrades.IUpgradableBlock;
import com.denfop.api.upgrades.UpgradableProperty;
import com.denfop.audio.EnumSound;
import com.denfop.blocks.BlockTileEntity;
import com.denfop.blocks.FluidName;
import com.denfop.blocks.mechanism.BlockBaseMachine3;
import com.denfop.componets.AirPollutionComponent;
import com.denfop.componets.Fluids;
import com.denfop.componets.SoilPollutionComponent;
import com.denfop.container.ContainerBase;
import com.denfop.container.ContainerFluidIntegrator;
import com.denfop.gui.GuiCore;
import com.denfop.gui.GuiFluidIntegrator;
import com.denfop.invslot.InvSlot;
import com.denfop.invslot.InvSlotFluid;
import com.denfop.invslot.InvSlotFluidByList;
import com.denfop.invslot.InvSlotUpgrade;
import com.denfop.network.DecoderHandler;
import com.denfop.network.EncoderHandler;
import com.denfop.network.IUpdatableTileEvent;
import com.denfop.network.packet.CustomPacketBuffer;
import com.denfop.recipe.IInputHandler;
import com.denfop.tiles.base.TileElectricMachine;
import com.denfop.utils.Keyboard;
import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.Blocks;
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

public class TileEntityFluidIntegrator extends TileElectricMachine implements
        IUpgradableBlock, IUpdateTick, IUpdatableTileEvent, IHasRecipe {

    public final InvSlotUpgrade upgradeSlot;
    public final InvSlotRecipes inputSlotA;
    public final Fluids.InternalFluidTank fluidTank1;
    public final Fluids.InternalFluidTank fluidTank2;
    public final InvSlotFluidByList fluidSlot1;
    public final InvSlotFluidByList fluidSlot2;
    public final InvSlotOutput output1;
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

    public TileEntityFluidIntegrator(BlockPos pos, BlockState state) {
        super(200, 1, 1,BlockBaseMachine3.fluid_integrator,pos,state);
        Recipes.recipes.addInitRecipes(this);

        this.addComponent(new SoilPollutionComponent(this, 0.1));
        this.addComponent(new AirPollutionComponent(this, 0.1));
        this.progress = 0;
        this.defaultEnergyConsume = this.energyConsume = 1;
        this.defaultOperationLength = this.operationLength = 100;
        this.defaultTier = 1;
        this.defaultEnergyStorage = 100;
        Fluids fluids = this.addComponent(new Fluids(this));
        this.fluidTank1 = fluids.addTankInsert("fluidTank1", 12 * 1000);
        this.output1 = new InvSlotOutput(this, 2);
        this.fluidTank2 = fluids.addTank("fluidTank2", 12 * 1000, InvSlot.TypeItemSlot.OUTPUT);
        this.inputSlotA = new InvSlotRecipes(this, "fluid_integrator", this, this.fluidTank1);


        this.fluid_handler = new FluidHandlerRecipe("fluid_integrator", fluids);
        this.fluidTank1.setAcceptedFluids(Fluids.fluidPredicate(this.fluid_handler.getFluids(0)));
        this.fluidTank2.setAcceptedFluids(Fluids.fluidPredicate(this.fluid_handler.getOutputFluids(0)));
        this.fluidSlot1 = new InvSlotFluidByList(this, 1, this.fluid_handler.getFluids(0));
        this.fluidSlot2 = new InvSlotFluidByList(this, 1, this.fluid_handler.getOutputFluids(0));
        this.fluidSlot2.setTypeFluidSlot(InvSlotFluid.TypeFluidSlot.OUTPUT);
        this.upgradeSlot = new com.denfop.invslot.InvSlotUpgrade(this, 4);

    }

    public static void addRecipe(ItemStack container, ItemStack output, FluidStack fluidStack, FluidStack outputfluidStack) {
        final IInputHandler input = com.denfop.api.Recipes.inputFactory;
        Recipes.recipes.addRecipe(
                "fluid_integrator",
                new BaseMachineRecipe(
                        new Input(fluidStack, input.getInput(container)),
                        new RecipeOutput(null, output)
                )
        );
        Recipes.recipes.getRecipeFluid().addRecipe("fluid_integrator", new BaseFluidMachineRecipe(new InputFluid(
                container, fluidStack), Collections.singletonList(
                outputfluidStack)));

    }

    @Override
    public SoundEvent getSound() {
        return EnumSound.mixer.getSoundEvent();
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

    public ContainerFluidIntegrator getGuiContainer(final Player var1) {
        return new ContainerFluidIntegrator(var1, this);

    }

    @Override
    @OnlyIn(Dist.CLIENT)
    public GuiCore<ContainerBase<? extends IAdvInventory>> getGui(Player var1, ContainerBase<? extends IAdvInventory> menu) {

        return new GuiFluidIntegrator((ContainerFluidIntegrator) menu);
    }

    @Override
    public void init() {
        addRecipe(new ItemStack(IUItem.iuingot.getStack(15)), new ItemStack(IUItem.iudust.getStack(42)),
                new FluidStack(FluidName.fluiddibromopropane.getInstance().get(), 200),
                new FluidStack(FluidName.fluidpropylene.getInstance().get()
                        , 200)
        );
        addRecipe(new ItemStack(IUItem.crafting_elements.getStack(482)), new ItemStack(IUItem.iudust.getStack(43)),
                new FluidStack(net.minecraft.world.level.material.Fluids.WATER, 200),
                new FluidStack(FluidName.fluidacetylene.getInstance().get()
                        , 200)
        );
        addRecipe(new ItemStack(Blocks.STONE), new ItemStack(IUItem.blockResource.getItem(15)),
                new FluidStack(FluidName.fluidblackoil.getInstance().get(), 100),
                new FluidStack(FluidName.fluidcarbonmonoxide.getInstance().get()
                        , 20)
        );
        addRecipe(new ItemStack(IUItem.iudust.getStack(21)), new ItemStack(IUItem.crafting_elements.getStack(498)),
                new FluidStack(FluidName.fluidhyd.getInstance().get(), 200),
                new FluidStack(net.minecraft.world.level.material.Fluids.WATER
                        , 100)
        );
        addRecipe(new ItemStack(IUItem.iudust.getStack(59)), new ItemStack(IUItem.smalldust.getStack(1), 4),
                new FluidStack(net.minecraft.world.level.material.Fluids.WATER, 300),
                new FluidStack(FluidName.fluidhydrogensulfide.getInstance().get()
                        , 150)
        );
        addRecipe(new ItemStack(IUItem.wolframite.getItem()), new ItemStack(IUItem.blockResource.getItem(13)),
                new FluidStack(FluidName.fluidquartz.getInstance().get(), 144),
                new FluidStack(FluidName.fluidoxy.getInstance().get()
                        , 20)
        );
        addRecipe(new ItemStack(IUItem.heavyore.getItem(4), 4), new ItemStack(IUItem.smalldust.getStack(22), 10),
                new FluidStack(FluidName.fluidoxy.getInstance().get(), 1100),
                new FluidStack(FluidName.fluidsulfuroxide.getInstance().get()
                        , 800)
        );

        addRecipe(new ItemStack(Blocks.SAND), IUItem.reinforcedStone,
                new FluidStack(FluidName.fluidconstruction_foam.getInstance().get(), 500),
                new FluidStack(FluidName.fluidhot_water.getInstance().get()
                        , 100)
        );

        addRecipe(new ItemStack(IUItem.crafting_elements.getStack(489)), new ItemStack(IUItem.crafting_elements.getStack(487)),
                new FluidStack(FluidName.fluidcoppersulfate.getInstance().get(), 250),
                new FluidStack(FluidName.fluidwastesulfuricacid.getInstance().get()
                        , 250)
        );


        addRecipe(new ItemStack(IUItem.crafting_elements.getStack(492)), new ItemStack(IUItem.crafting_elements.getStack(493)),
                new FluidStack(net.minecraft.world.level.material.Fluids.WATER, 250),
                new FluidStack(FluidName.fluidhyd.getInstance().get()
                        , 50)
        );
        addRecipe(new ItemStack(Blocks.DIRT), new ItemStack(Blocks.MYCELIUM),
                new FluidStack(FluidName.fluidhoney.getInstance().get(), 1000),
                new FluidStack(FluidName.fluidoxy.getInstance().get()
                        , 75)
        );
        addRecipe(new ItemStack(IUItem.iuingot.getStack(14), 4), new ItemStack(IUItem.iudust.getStack(62)),
                new FluidStack(FluidName.fluidnitricacid.getInstance().get(), 200),
                new FluidStack(FluidName.fluidnitrogendioxide.getInstance().get()
                        , 100)
        );

        addRecipe(new ItemStack(IUItem.iudust.getStack(65)), new ItemStack(IUItem.iudust.getStack(67)),
                new FluidStack(FluidName.fluidsulfuricacid.getInstance().get(), 200),
                new FluidStack(FluidName.fluidfluorhyd.getInstance().get()
                        , 200)
        );

        addRecipe(new ItemStack(IUItem.iudust.getStack(69)), new ItemStack(IUItem.iudust.getStack(64)),
                new FluidStack(net.minecraft.world.level.material.Fluids.WATER, 100),
                new FluidStack(FluidName.fluidorthophosphoricacid.getInstance().get()
                        , 100)
        );

        addRecipe(new ItemStack(IUItem.iudust.getStack(70)), new ItemStack(IUItem.iudust.getStack(37)),
                new FluidStack(net.minecraft.world.level.material.Fluids.WATER, 100),
                new FluidStack(FluidName.fluidorthophosphoricacid.getInstance().get()
                        , 100)
        );

        addRecipe(new ItemStack(IUItem.iudust.getStack(66)), new ItemStack(IUItem.iudust.getStack(41)),
                new FluidStack(net.minecraft.world.level.material.Fluids.WATER, 100),
                new FluidStack(FluidName.fluidorthophosphoricacid.getInstance().get()
                        , 100)
        );
    }

    @Override
    public BlockTileEntity getBlock() {
        return IUItem.basemachine2.getBlock(getTeBlock());
    }

    @Override
    public IMultiTileBlock getTeBlock() {
        return BlockBaseMachine3.fluid_integrator;
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


        if (this.output != null && !this.inputSlotA.isEmpty() && this.outputSlot.canAdd(this.output
                .getRecipe()
                .getOutput().items) && this.inputSlotA.continue_process(this.output) && this.fluid_handler.output() != null && fluid_handler.canOperate() && this.fluid_handler.canFillFluid() && this.energy.canUseEnergy(
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
        this.outputSlot.add(this.output.getRecipe().getOutput().items);
        this.fluid_handler.fillFluid();
    }

    @Override
    public Set<UpgradableProperty> getUpgradableProperties() {
        return EnumSet.of(
                UpgradableProperty.Processing,
                UpgradableProperty.Transformer,
                UpgradableProperty.EnergyStorage,
                UpgradableProperty.ItemInput,
                UpgradableProperty.ItemExtract,
                UpgradableProperty.FluidExtract
        );
    }


    public double getProgress() {
        return this.guiProgress;
    }

}
