package com.denfop.tiles.mechanism;

import com.denfop.IUItem;
import com.denfop.Localization;
import com.denfop.api.Recipes;
import com.denfop.api.inv.IAdvInventory;
import com.denfop.api.recipe.*;
import com.denfop.api.tile.IMultiTileBlock;
import com.denfop.api.upgrades.IUpgradableBlock;
import com.denfop.api.upgrades.UpgradableProperty;
import com.denfop.blocks.BlockTileEntity;
import com.denfop.blocks.FluidName;
import com.denfop.blocks.mechanism.BlockBaseMachine3;
import com.denfop.componets.AirPollutionComponent;
import com.denfop.componets.Fluids;
import com.denfop.componets.SoilPollutionComponent;
import com.denfop.container.ContainerBase;
import com.denfop.container.ContainerItemDividerFluids;
import com.denfop.gui.GuiCore;
import com.denfop.gui.GuiItemDividerFluids;
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
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.api.distmarker.OnlyIn;
import net.neoforged.neoforge.fluids.FluidStack;
import org.apache.commons.lang3.mutable.MutableObject;

import java.io.IOException;
import java.util.Arrays;
import java.util.EnumSet;
import java.util.List;
import java.util.Set;

public class TileEntityItemDividerFluids extends TileElectricMachine implements
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

    public TileEntityItemDividerFluids(BlockPos pos, BlockState state) {
        super(200, 1, 0, BlockBaseMachine3.item_divider_to_fluid, pos, state);
        Recipes.recipes.addInitRecipes(this);
        this.progress = 0;
        this.defaultEnergyConsume = this.energyConsume = 1;
        this.defaultOperationLength = this.operationLength = 100;
        this.defaultTier = 1;
        this.defaultEnergyStorage = 100;

        this.addComponent(new SoilPollutionComponent(this, 0.1));
        this.addComponent(new AirPollutionComponent(this, 0.1));
        this.inputSlotA = new InvSlotRecipes(this, "item_divider_fluid", this);
        Fluids fluids = this.addComponent(new Fluids(this));
        this.fluidTank1 = fluids.addTank("fluidTank1", 12 * 1000, InvSlot.TypeItemSlot.OUTPUT);
        this.output1 = new InvSlotOutput(this, 2);

        this.fluidTank2 = fluids.addTank("fluidTank2", 12 * 1000, InvSlot.TypeItemSlot.OUTPUT);


        this.fluid_handler = new FluidHandlerRecipe("item_divider_fluid", fluids);
        this.fluidTank1.setAcceptedFluids(Fluids.fluidPredicate(this.fluid_handler.getOutputFluids(0)));
        this.fluidTank2.setAcceptedFluids(Fluids.fluidPredicate(this.fluid_handler.getOutputFluids(1)));
        this.fluidSlot1 = new InvSlotFluidByList(this, 1, this.fluid_handler.getOutputFluids(0));
        this.fluidSlot2 = new InvSlotFluidByList(this, 1, this.fluid_handler.getOutputFluids(1));
        this.fluidSlot1.setTypeFluidSlot(InvSlotFluid.TypeFluidSlot.OUTPUT);
        this.fluidSlot2.setTypeFluidSlot(InvSlotFluid.TypeFluidSlot.OUTPUT);

        this.upgradeSlot = new com.denfop.invslot.InvSlotUpgrade(this, 4);

    }

    public static void addRecipe(ItemStack container, FluidStack... fluidStack) {
        final IInputHandler input = com.denfop.api.Recipes.inputFactory;
        Recipes.recipes.addRecipe(
                "item_divider_fluid",
                new BaseMachineRecipe(
                        new Input(
                                input.getInput(container)),
                        new RecipeOutput(null, container)
                )
        );
        Recipes.recipes.getRecipeFluid().addRecipe("item_divider_fluid", new BaseFluidMachineRecipe(new InputFluid(
                container), Arrays.asList(
                fluidStack)));

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

    public ContainerItemDividerFluids getGuiContainer(final Player var1) {
        return new ContainerItemDividerFluids(var1, this);

    }

    @Override
    @OnlyIn(Dist.CLIENT)
    public GuiCore<ContainerBase<? extends IAdvInventory>> getGui(Player var1, ContainerBase<? extends IAdvInventory> menu) {

        return new GuiItemDividerFluids((ContainerItemDividerFluids) menu);
    }

    @Override
    public void init() {
        addRecipe(
                new ItemStack(IUItem.basalts.getItem()),
                new FluidStack(FluidName.fluidpahoehoe_lava.getInstance().get(), 125),
                new FluidStack(FluidName.fluidfluorhyd.getInstance().get(), 50)
        );
        addRecipe(
                new ItemStack(IUItem.basalts.getItem(9), 1),
                new FluidStack(FluidName.fluidpahoehoe_lava.getInstance().get(), 125),
                new FluidStack(FluidName.fluidfluorhyd.getInstance().get(), 100)
        );
        addRecipe(
                new ItemStack(IUItem.basalts.getItem(4), 1),
                new FluidStack(FluidName.fluidpahoehoe_lava.getInstance().get(), 250),
                new FluidStack(FluidName.fluidfluorhyd.getInstance().get(), 50)
        );
        addRecipe(
                new ItemStack(Blocks.OBSIDIAN),
                new FluidStack(net.minecraft.world.level.material.Fluids.LAVA, 1000),
                new FluidStack(FluidName.fluidoxy.getInstance().get(), 10)
        );
        addRecipe(
                new ItemStack(IUItem.crafting_elements.getStack(454), 1),
                new FluidStack(FluidName.fluidnitricoxide.getInstance().get(), 200),
                new FluidStack(net.minecraft.world.level.material.Fluids.WATER, 400)
        );

        addRecipe(
                new ItemStack(IUItem.honeycomb.getItem()),
                new FluidStack(FluidName.fluidbeeswax.getInstance().get(), 250),
                new FluidStack(FluidName.fluidbiomass.getInstance().get(), 50)
        );
        addRecipe(
                new ItemStack(Items.QUARTZ),
                new FluidStack(FluidName.fluidquartz.getInstance().get(), 144),
                new FluidStack(FluidName.fluidoxy.getInstance().get(), 10)
        );
        addRecipe(
                new ItemStack(Blocks.GLASS, 4),
                new FluidStack(FluidName.fluidquartz.getInstance().get(), 144),
                new FluidStack(FluidName.fluidoxy.getInstance().get(), 10)
        );
        addRecipe(
                new ItemStack(Blocks.MAGMA_BLOCK, 1),
                new FluidStack(net.minecraft.world.level.material.Fluids.LAVA, 250),
                new FluidStack(FluidName.fluidoxy.getInstance().get(), 50)
        );
        addRecipe(
                IUItem.biochaff,
                new FluidStack(FluidName.fluidbiomass.getInstance().get(), 100),
                new FluidStack(FluidName.fluidoxy.getInstance().get(), 20)
        );
        addRecipe(
                IUItem.plantBall,
                new FluidStack(FluidName.fluidbiomass.getInstance().get(), 50),
                new FluidStack(FluidName.fluidoxy.getInstance().get(), 10)
        );
        addRecipe(
                new ItemStack(IUItem.space_stone.getItem(1), 1),
                new FluidStack(FluidName.fluidazot.getInstance().get(), 50),
                new FluidStack(FluidName.fluidhyd.getInstance().get(), 100)
        );
        addRecipe(
                new ItemStack(IUItem.space_stone.getItem(7), 1),
                new FluidStack(FluidName.fluidacetylene.getInstance().get(), 100),
                new FluidStack(FluidName.fluidHelium.getInstance().get(), 50)
        );
        addRecipe(
                new ItemStack(IUItem.space_stone.getItem(8), 1),
                new FluidStack(FluidName.fluidiodine.getInstance().get(), 200),
                new FluidStack(FluidName.fluidchlorum.getInstance().get(), 200)
        );
        addRecipe(
                new ItemStack(IUItem.space_stone.getItem(11), 1),
                new FluidStack(FluidName.fluiddecane.getInstance().get(), 200),
                new FluidStack(FluidName.fluidxenon.getInstance().get(), 200)
        );
        addRecipe(
                new ItemStack(IUItem.space_stone.getItem(13), 1),
                new FluidStack(FluidName.fluiddecane.getInstance().get(), 100),
                new FluidStack(FluidName.fluidsulfuricacid.getInstance().get(), 150)
        );
        addRecipe(
                new ItemStack(IUItem.space_stone1.getItem(0), 1),
                new FluidStack(FluidName.fluidpropane.getInstance().get(), 100),
                new FluidStack(FluidName.fluidbutene.getInstance().get(), 150)
        );
        addRecipe(
                new ItemStack(IUItem.space_stone1.getItem(1), 1),
                new FluidStack(FluidName.fluidnitricoxide.getInstance().get(), 100),
                new FluidStack(FluidName.fluidsulfurtrioxide.getInstance().get(), 150)
        );
        addRecipe(
                new ItemStack(IUItem.space_stone1.getItem(12), 1),
                new FluidStack(FluidName.fluidmethane.getInstance().get(), 100),
                new FluidStack(FluidName.fluidfluor.getInstance().get(), 150)
        );
    }

    @Override
    public BlockTileEntity getBlock() {
        return IUItem.basemachine2.getBlock(getTeBlock());
    }

    @Override
    public IMultiTileBlock getTeBlock() {
        return BlockBaseMachine3.item_divider_to_fluid;
    }

    @Override
    public void onUpdate() {

    }

    public void onLoaded() {
        super.onLoaded();
        if (!level.isClientSide) {
            inputSlotA.load();
            this.fluid_handler.load(this.inputSlotA.get(0));
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
        if (this.fluidTank1.getFluidAmount() >= 1000 && this.fluidSlot1.transferFromTank(
                this.fluidTank1,
                output1,
                true
        ) && (output1.getValue() == null || this.output1.canAdd(output1.getValue()))) {
            this.fluidSlot1.transferFromTank(this.fluidTank1, output1, false);
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

        if (check || (this.fluid_handler.output() == null && this.output != null)) {
            this.fluid_handler.getOutput(this.inputSlotA.get(0));
        } else {
            if (this.fluid_handler.output() != null && this.output == null) {
                this.fluid_handler.setOutput(null);
            }
        }


        if (this.output != null && !this.inputSlotA.isEmpty() && this.inputSlotA.continue_process(this.output) && this.fluid_handler.output() != null && this.fluid_handler.canFillFluid() && this.energy.canUseEnergy(
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
                UpgradableProperty.FluidExtract
        );
    }


    public double getProgress() {
        return this.guiProgress;
    }

}
