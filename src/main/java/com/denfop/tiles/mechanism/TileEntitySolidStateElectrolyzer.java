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
import com.denfop.container.ContainerSolidStateElectrolyzer;
import com.denfop.gui.GuiCore;
import com.denfop.gui.GuiSolidStateElectrolyzer;
import com.denfop.invslot.*;
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
import java.util.Arrays;
import java.util.EnumSet;
import java.util.List;
import java.util.Set;

public class TileEntitySolidStateElectrolyzer extends TileElectricMachine implements
        IUpgradableBlock, IUpdateTick, IUpdatableTileEvent, IHasRecipe {

    public final InvSlotUpgrade upgradeSlot;
    public final InvSlotRecipes inputSlotA;
    public final Fluids.InternalFluidTank fluidTank1;
    public final InvSlotFluidByList fluidSlot1;
    public final InvSlotOutput output1;
    public final FluidHandlerRecipe fluid_handler;
    public final double defaultEnergyConsume;
    public final int defaultOperationLength;
    public final int defaultTier;
    public final double defaultEnergyStorage;
    public final InvSlotElectrolyzer cathodeslot;
    public final InvSlotElectrolyzer anodeslot;
    public MachineRecipe output;
    public double energyConsume;
    public int operationLength;
    public int operationsPerTick;
    public double guiProgress;
    protected short progress;

    public TileEntitySolidStateElectrolyzer(BlockPos pos, BlockState state) {
        super(200, 1, 1,BlockBaseMachine3.solid_state_electrolyzer,pos,state);
        Recipes.recipes.addInitRecipes(this);

        this.progress = 0;
        this.defaultEnergyConsume = this.energyConsume = 1;
        this.defaultOperationLength = this.operationLength = 100;
        this.defaultTier = 1;
        this.defaultEnergyStorage = 100;

        this.addComponent(new SoilPollutionComponent(this, 0.1));
        this.addComponent(new AirPollutionComponent(this, 0.1));
        this.inputSlotA = new InvSlotRecipes(this, "solid_electrolyzer", this);
        Fluids fluids = this.addComponent(new Fluids(this));
        this.fluidTank1 = fluids.addTank("fluidTank1", 12 * 1000, InvSlot.TypeItemSlot.OUTPUT);
        this.output1 = new InvSlotOutput(this, 1);


        this.fluid_handler = new FluidHandlerRecipe("solid_electrolyzer", fluids);
        this.fluidTank1.setAcceptedFluids(Fluids.fluidPredicate(this.fluid_handler.getOutputFluids(0)));
        this.fluidSlot1 = new InvSlotFluidByList(this, 1, this.fluid_handler.getOutputFluids(0));
        this.fluidSlot1.setTypeFluidSlot(InvSlotFluid.TypeFluidSlot.OUTPUT);
        this.upgradeSlot = new com.denfop.invslot.InvSlotUpgrade(this, 4);
        this.cathodeslot = new InvSlotElectrolyzer(this, 1);
        this.anodeslot = new InvSlotElectrolyzer(this, 0);

    }

    public static void addRecipe(ItemStack container, ItemStack output, FluidStack... fluidStack) {
        final IInputHandler input = com.denfop.api.Recipes.inputFactory;
        Recipes.recipes.addRecipe(
                "solid_electrolyzer",
                new BaseMachineRecipe(
                        new Input(
                                input.getInput(container)),
                        new RecipeOutput(null, output)
                )
        );
        Recipes.recipes.getRecipeFluid().addRecipe("solid_electrolyzer", new BaseFluidMachineRecipe(new InputFluid(
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

    public ContainerSolidStateElectrolyzer getGuiContainer(final Player var1) {
        return new ContainerSolidStateElectrolyzer(var1, this);

    }

    @Override
    @OnlyIn(Dist.CLIENT)
    public GuiCore<ContainerBase<? extends IAdvInventory>> getGui(Player var1, ContainerBase<? extends IAdvInventory> menu) {

        return new GuiSolidStateElectrolyzer((ContainerSolidStateElectrolyzer) menu);
    }

    @Override
    public void init() {
        addRecipe(new ItemStack(IUItem.iudust.getStack(42)), new ItemStack(IUItem.iudust.getStack(15)),
                new FluidStack(FluidName.fluidbromine.getInstance().get(), 50)
        );

        addRecipe(new ItemStack(IUItem.iudust.getStack(43)), new ItemStack(IUItem.iudust.getStack(41)),
                new FluidStack(net.minecraft.world.level.material.Fluids.WATER, 20)
        );

        addRecipe(new ItemStack(Blocks.CLAY), new ItemStack(IUItem.smalldust.getStack(1)),
                new FluidStack(FluidName.fluidco2.getInstance().get(), 10)
        );
        addRecipe(new ItemStack(IUItem.iudust.getStack(63)), new ItemStack(IUItem.iudust.getStack(14), 3),
                new FluidStack(FluidName.fluidchlorum.getInstance().get(), 80)
        );

        addRecipe(new ItemStack(IUItem.preciousgem.getStack(0)), new ItemStack(IUItem.smalldust.getStack(11), 5),
                new FluidStack(FluidName.fluidoxy.getInstance().get(), 20)
        );
        addRecipe(new ItemStack(IUItem.preciousgem.getStack(1)), new ItemStack(IUItem.smalldust.getStack(2), 5),
                new FluidStack(FluidName.fluidoxy.getInstance().get(), 20)
        );
        addRecipe(new ItemStack(IUItem.preciousgem.getStack(2)), new ItemStack(IUItem.smalldust.getStack(10), 5),
                new FluidStack(FluidName.fluidoxy.getInstance().get(), 20)
        );
        addRecipe(new ItemStack(IUItem.space_stone.getItem(0)), new ItemStack(IUItem.spaceItem.getStack(30), 3),
                new FluidStack(FluidName.fluidbromine.getInstance().get(), 50)
        );
        addRecipe(new ItemStack(IUItem.space_stone.getItem(5)), new ItemStack(IUItem.spaceItem.getStack(35), 3),
                new FluidStack(FluidName.fluidHelium.getInstance().get(), 50)
        );
        addRecipe(new ItemStack(IUItem.space_stone.getItem(6)), new ItemStack(IUItem.spaceItem.getStack(36), 3),
                new FluidStack(FluidName.fluidmethane.getInstance().get(), 50)
        );
        addRecipe(new ItemStack(IUItem.space_stone.getItem(10)), new ItemStack(IUItem.spaceItem.getStack(40), 3),
                new FluidStack(FluidName.fluidcarbonmonoxide.getInstance().get(), 50)
        );
        addRecipe(new ItemStack(IUItem.space_stone.getItem(14)), new ItemStack(IUItem.spaceItem.getStack(44), 3),
                new FluidStack(FluidName.fluidazot.getInstance().get(), 100)
        );
        addRecipe(new ItemStack(IUItem.space_stone1.getItem(2)), new ItemStack(IUItem.spaceItem.getStack(48), 3),
                new FluidStack(FluidName.fluidhyd.getInstance().get(), 100)
        );
        addRecipe(new ItemStack(IUItem.space_stone1.getItem(3)), new ItemStack(IUItem.spaceItem.getStack(49), 3),
                new FluidStack(FluidName.fluidethane.getInstance().get(), 100)
        );
        addRecipe(new ItemStack(IUItem.space_stone1.getItem(5)), new ItemStack(IUItem.spaceItem.getStack(51), 3),
                new FluidStack(FluidName.fluidnitricacid.getInstance().get(), 100)
        );
        addRecipe(new ItemStack(IUItem.space_stone1.getItem(6)), new ItemStack(IUItem.spaceItem.getStack(52), 3),
                new FluidStack(FluidName.fluidxenon.getInstance().get(), 80)
        );
        addRecipe(new ItemStack(IUItem.space_stone1.getItem(7)), new ItemStack(IUItem.spaceItem.getStack(53), 3),
                new FluidStack(FluidName.fluidnitrogenhydride.getInstance().get(), 100)
        );
        addRecipe(new ItemStack(IUItem.space_stone1.getItem(8)), new ItemStack(IUItem.spaceItem.getStack(54), 3),
                new FluidStack(FluidName.fluiddecane.getInstance().get(), 80)
        );
        addRecipe(new ItemStack(IUItem.space_stone1.getItem(10)), new ItemStack(IUItem.spaceItem.getStack(56), 3),
                new FluidStack(FluidName.fluidchlorum.getInstance().get(), 80)
        );
        addRecipe(new ItemStack(IUItem.space_stone1.getItem(11)), new ItemStack(IUItem.spaceItem.getStack(57), 3),
                new FluidStack(FluidName.fluidiodine.getInstance().get(), 80)
        );
    }

    @Override
    public SoundEvent getSound() {
        return EnumSound.solid_electrolyzer.getSoundEvent();
    }

    @Override
    public BlockTileEntity getBlock() {
        return IUItem.basemachine2.getBlock(getTeBlock());
    }

    @Override
    public IMultiTileBlock getTeBlock() {
        return BlockBaseMachine3.solid_state_electrolyzer;
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
        if (this.upgradeSlot.tickNoMark()) {
            setOverclockRates();
        }

        if (check || (this.fluid_handler.output() == null && this.output != null)) {
            this.fluid_handler.getOutput(this.inputSlotA.get(0));
        } else {
            if (this.fluid_handler.output() != null && this.output == null) {
                this.fluid_handler.setOutput(null);
            }
        }
        if (this.cathodeslot.isEmpty() || this.anodeslot.isEmpty()) {
            if (this.getActive()) {
                this.setActive(false);
                initiate(2);
            }
            return;
        }

        if (this.output != null && !this.inputSlotA.isEmpty() && this.outputSlot.canAdd(output
                .getRecipe()
                .getOutput().items) && this.inputSlotA.continue_process(this.output) && this.fluid_handler.output() != null && this.fluid_handler.canFillFluid() && this.energy.canUseEnergy(
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
            ItemStack cathode = this.cathodeslot.get(0);
            ItemStack anode = this.anodeslot.get(0);
            if (cathode.getDamageValue() < cathode.getMaxDamage()) {
                cathode.setDamageValue(cathode.getDamageValue() + 1);
            }
            if (anode.getDamageValue() < anode.getMaxDamage()) {
                anode.setDamageValue(anode.getDamageValue() + 1);
            }
            if (cathode.getDamageValue() == cathode.getMaxDamage()) {
                this.cathodeslot.consume(1);
            }
            if (anode.getDamageValue() == anode.getMaxDamage()) {
                this.anodeslot.consume(1);
            }
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
                UpgradableProperty.FluidExtract
        );
    }


    public double getProgress() {
        return this.guiProgress;
    }

}
