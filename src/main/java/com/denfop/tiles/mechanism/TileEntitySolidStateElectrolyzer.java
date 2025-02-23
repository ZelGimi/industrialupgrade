package com.denfop.tiles.mechanism;

import com.denfop.IUCore;
import com.denfop.IUItem;
import com.denfop.Localization;
import com.denfop.api.Recipes;
import com.denfop.api.recipe.BaseFluidMachineRecipe;
import com.denfop.api.recipe.BaseMachineRecipe;
import com.denfop.api.recipe.FluidHandlerRecipe;
import com.denfop.api.recipe.IHasRecipe;
import com.denfop.api.recipe.IUpdateTick;
import com.denfop.api.recipe.Input;
import com.denfop.api.recipe.InputFluid;
import com.denfop.api.recipe.InvSlotOutput;
import com.denfop.api.recipe.InvSlotRecipes;
import com.denfop.api.recipe.MachineRecipe;
import com.denfop.api.recipe.RecipeOutput;
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
import com.denfop.container.ContainerSolidStateElectrolyzer;
import com.denfop.gui.GuiSolidStateElectrolyzer;
import com.denfop.invslot.InvSlot;
import com.denfop.invslot.InvSlotElectrolyzer;
import com.denfop.invslot.InvSlotFluid;
import com.denfop.invslot.InvSlotFluidByList;
import com.denfop.invslot.InvSlotUpgrade;
import com.denfop.network.DecoderHandler;
import com.denfop.network.EncoderHandler;
import com.denfop.network.IUpdatableTileEvent;
import com.denfop.network.packet.CustomPacketBuffer;
import com.denfop.recipe.IInputHandler;
import com.denfop.tiles.base.TileElectricMachine;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.SoundEvent;
import net.minecraftforge.fluids.FluidRegistry;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import org.apache.commons.lang3.mutable.MutableObject;
import org.lwjgl.input.Keyboard;

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

    public TileEntitySolidStateElectrolyzer() {
        super(200, 1, 1);
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

    public ContainerSolidStateElectrolyzer getGuiContainer(final EntityPlayer var1) {
        return new ContainerSolidStateElectrolyzer(var1, this);

    }

    @Override
    @SideOnly(Side.CLIENT)
    public GuiScreen getGui(final EntityPlayer var1, final boolean var2) {

        return new GuiSolidStateElectrolyzer(getGuiContainer(var1));
    }

    @Override
    public void init() {
        addRecipe(new ItemStack(IUItem.iudust, 1, 42), new ItemStack(IUItem.iudust, 1, 15),
                new FluidStack(FluidName.fluidbromine.getInstance(), 50)
        );

        addRecipe(new ItemStack(IUItem.iudust, 1, 43), new ItemStack(IUItem.iudust, 1, 41),
                new FluidStack(FluidRegistry.WATER, 20)
        );

        addRecipe(new ItemStack(Blocks.CLAY), new ItemStack(IUItem.smalldust, 1, 1),
                new FluidStack(FluidName.fluidco2.getInstance(), 10)
        );
        addRecipe(new ItemStack(IUItem.iudust, 1, 63), new ItemStack(IUItem.iudust, 3, 14),
                new FluidStack(FluidName.fluidchlorum.getInstance(), 80)
        );

        addRecipe(new ItemStack(IUItem.preciousgem, 1, 0), new ItemStack(IUItem.smalldust, 5, 11),
                new FluidStack(FluidName.fluidoxy.getInstance(), 20)
        );
        addRecipe(new ItemStack(IUItem.preciousgem, 1, 1), new ItemStack(IUItem.smalldust, 5, 2),
                new FluidStack(FluidName.fluidoxy.getInstance(), 20)
        );
        addRecipe(new ItemStack(IUItem.preciousgem, 1, 2), new ItemStack(IUItem.smalldust, 5, 10),
                new FluidStack(FluidName.fluidoxy.getInstance(), 20)
        );
        addRecipe(new ItemStack(IUItem.space_stone, 1, 0), new ItemStack(IUItem.spaceItem, 3, 30),
                new FluidStack(FluidName.fluidbromine.getInstance(), 50)
        );
        addRecipe(new ItemStack(IUItem.space_stone, 1, 5), new ItemStack(IUItem.spaceItem, 3, 35),
                new FluidStack(FluidName.fluidHelium.getInstance(), 50)
        );
        addRecipe(new ItemStack(IUItem.space_stone, 1, 6), new ItemStack(IUItem.spaceItem, 3, 36),
                new FluidStack(FluidName.fluidmethane.getInstance(), 50)
        );
        addRecipe(new ItemStack(IUItem.space_stone, 1, 10), new ItemStack(IUItem.spaceItem, 3, 40),
                new FluidStack(FluidName.fluidcarbonmonoxide.getInstance(), 50)
        );
        addRecipe(new ItemStack(IUItem.space_stone, 1, 14), new ItemStack(IUItem.spaceItem, 3, 44),
                new FluidStack(FluidName.fluidazot.getInstance(), 100)
        );
        addRecipe(new ItemStack(IUItem.space_stone1, 1, 2), new ItemStack(IUItem.spaceItem, 3, 48),
                new FluidStack(FluidName.fluidhyd.getInstance(), 100)
        );
        addRecipe(new ItemStack(IUItem.space_stone1, 1, 3), new ItemStack(IUItem.spaceItem, 3, 49),
                new FluidStack(FluidName.fluidethane.getInstance(), 100)
        );
        addRecipe(new ItemStack(IUItem.space_stone1, 1, 5), new ItemStack(IUItem.spaceItem, 3, 51),
                new FluidStack(FluidName.fluidnitricacid.getInstance(), 100)
        );
        addRecipe(new ItemStack(IUItem.space_stone1, 1, 6), new ItemStack(IUItem.spaceItem, 3, 52),
                new FluidStack(FluidName.fluidxenon.getInstance(), 80)
        );
        addRecipe(new ItemStack(IUItem.space_stone1, 1, 7), new ItemStack(IUItem.spaceItem, 3, 53),
                new FluidStack(FluidName.fluidnitrogenhydride.getInstance(), 100)
        );
        addRecipe(new ItemStack(IUItem.space_stone1, 1, 8), new ItemStack(IUItem.spaceItem, 3, 54),
                new FluidStack(FluidName.fluiddecane.getInstance(), 80)
        );
        addRecipe(new ItemStack(IUItem.space_stone1, 1, 10), new ItemStack(IUItem.spaceItem, 3, 56),
                new FluidStack(FluidName.fluidchlorum.getInstance(), 80)
        );
        addRecipe(new ItemStack(IUItem.space_stone1, 1, 11), new ItemStack(IUItem.spaceItem, 3, 57),
                new FluidStack(FluidName.fluidiodine.getInstance(), 80)
        );
    }

    @Override
    public SoundEvent getSound() {
        return EnumSound.solid_electrolyzer.getSoundEvent();
    }

    @Override
    public BlockTileEntity getBlock() {
        return IUItem.basemachine2;
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
        if (IUCore.proxy.isSimulating()) {
            inputSlotA.load();
            this.fluid_handler.load(this.inputSlotA.get());
            this.getOutput();
        }


    }

    public void readFromNBT(NBTTagCompound nbttagcompound) {
        super.readFromNBT(nbttagcompound);
        this.progress = nbttagcompound.getShort("progress");

    }

    public NBTTagCompound writeToNBT(NBTTagCompound nbttagcompound) {
        super.writeToNBT(nbttagcompound);
        nbttagcompound.setShort("progress", this.progress);
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
            this.fluid_handler.getOutput(this.inputSlotA.get());
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
            ItemStack cathode = this.cathodeslot.get();
            ItemStack anode = this.anodeslot.get();
            if (cathode.getItemDamage() < cathode.getMaxDamage()) {
                cathode.setItemDamage(cathode.getItemDamage() + 1);
            }
            if (anode.getItemDamage() < anode.getMaxDamage()) {
                anode.setItemDamage(anode.getItemDamage() + 1);
            }
            if (cathode.getItemDamage() == cathode.getMaxDamage()) {
                this.cathodeslot.consume(1);
            }
            if (anode.getItemDamage() == anode.getMaxDamage()) {
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
