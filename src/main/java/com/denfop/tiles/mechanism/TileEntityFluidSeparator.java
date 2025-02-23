package com.denfop.tiles.mechanism;

import com.denfop.IUCore;
import com.denfop.IUItem;
import com.denfop.Localization;
import com.denfop.api.Recipes;
import com.denfop.api.recipe.BaseFluidMachineRecipe;
import com.denfop.api.recipe.FluidHandlerRecipe;
import com.denfop.api.recipe.IHasRecipe;
import com.denfop.api.recipe.InputFluid;
import com.denfop.api.sytem.EnergyType;
import com.denfop.api.tile.IMultiTileBlock;
import com.denfop.api.upgrades.IUpgradableBlock;
import com.denfop.api.upgrades.UpgradableProperty;
import com.denfop.blocks.BlockTileEntity;
import com.denfop.blocks.FluidName;
import com.denfop.blocks.mechanism.BlockBaseMachine3;
import com.denfop.componets.AirPollutionComponent;
import com.denfop.componets.ComponentBaseEnergy;
import com.denfop.componets.Fluids;
import com.denfop.componets.SoilPollutionComponent;
import com.denfop.container.ContainerFluidSeparator;
import com.denfop.gui.GuiFluidSeparator;
import com.denfop.invslot.InvSlot;
import com.denfop.invslot.InvSlotFluid;
import com.denfop.invslot.InvSlotFluidByList;
import com.denfop.invslot.InvSlotUpgrade;
import com.denfop.network.DecoderHandler;
import com.denfop.network.EncoderHandler;
import com.denfop.network.packet.CustomPacketBuffer;
import com.denfop.tiles.base.TileElectricMachine;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
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

public class TileEntityFluidSeparator extends TileElectricMachine implements IUpgradableBlock, IHasRecipe {

    public final FluidHandlerRecipe fluid_handler;
    public final Fluids.InternalFluidTank fluidTank2;
    public final Fluids.InternalFluidTank fluidTank1;
    public final InvSlotFluidByList fluidSlot1;
    public final InvSlotFluidByList fluidSlot2;
    public final Fluids.InternalFluidTank fluidTank3;
    public final double defaultEnergyConsume;
    public final int defaultOperationLength;
    public final int defaultTier;
    public final double defaultEnergyStorage;
    public final ComponentBaseEnergy se;
    public final InvSlotUpgrade upgradeSlot;
    public final InvSlotFluidByList fluidSlot3;
    public double energyConsume;
    public int operationLength;
    public int operationsPerTick;
    protected short progress;
    protected double guiProgress;

    public TileEntityFluidSeparator() {
        super(100, 1, 3);
        this.progress = 0;
        this.defaultEnergyConsume = this.energyConsume = 1;
        this.defaultOperationLength = this.operationLength = 100;
        this.defaultTier = 1;
        this.defaultEnergyStorage = 100;

        this.addComponent(new SoilPollutionComponent(this, 0.1));
        this.addComponent(new AirPollutionComponent(this, 0.1));
        Fluids fluids = this.addComponent(new Fluids(this));
        this.fluidTank1 = fluids.addTank("fluidTank1", 12 * 1000, InvSlot.TypeItemSlot.INPUT);


        this.fluidTank2 = fluids.addTank("fluidTank2", 12 * 1000, InvSlot.TypeItemSlot.OUTPUT);


        this.fluidTank3 = fluids.addTank("fluidTank3", 12 * 1000, InvSlot.TypeItemSlot.OUTPUT);
        this.fluid_handler = new FluidHandlerRecipe("fluid_separator", fluids);
        this.fluidTank1.setAcceptedFluids(Fluids.fluidPredicate(this.fluid_handler.getFluids(0)));
        this.fluidTank2.setAcceptedFluids(Fluids.fluidPredicate(this.fluid_handler.getOutputFluids(0)));
        this.fluidTank3.setAcceptedFluids(Fluids.fluidPredicate(this.fluid_handler.getOutputFluids(1)));
        this.upgradeSlot = new InvSlotUpgrade(this, 4);

        this.fluidSlot1 = new InvSlotFluidByList(this, 1, this.fluid_handler.getFluids(0));
        this.fluidSlot2 = new InvSlotFluidByList(this, 1, this.fluid_handler.getOutputFluids(0));
        this.fluidSlot3 = new InvSlotFluidByList(this, 1, this.fluid_handler.getOutputFluids(1));
        this.se = this.addComponent(ComponentBaseEnergy.asBasicSink(EnergyType.SOLARIUM, this, 1000));
        this.fluidSlot2.setTypeFluidSlot(InvSlotFluid.TypeFluidSlot.OUTPUT);
        this.fluidSlot3.setTypeFluidSlot(InvSlotFluid.TypeFluidSlot.OUTPUT);
        Recipes.recipes.getRecipeFluid().addInitRecipes(this);

    }

    public static int applyModifier(int base, int extra, double multiplier) {
        double ret = Math.round((base + extra) * multiplier);
        return (ret > 2.147483647E9D) ? Integer.MAX_VALUE : (int) ret;
    }

    @Override
    public void init() {
        Recipes.recipes.getRecipeFluid().addRecipe("fluid_separator", new BaseFluidMachineRecipe(
                new InputFluid(
                        new FluidStack(FluidName.fluidgas.getInstance(), 1000)),
                Arrays.asList(new FluidStack(
                        FluidName.fluidmethane.getInstance(),
                        500
                ), new FluidStack(FluidName.fluidpropane.getInstance(), 200))
        ));

        Recipes.recipes.getRecipeFluid().addRecipe("fluid_separator", new BaseFluidMachineRecipe(
                new InputFluid(
                        new FluidStack(FluidName.fluidmethane.getInstance(), 1000)),
                Arrays.asList(new FluidStack(
                        FluidName.fluidacetylene.getInstance(),
                        500
                ), new FluidStack(FluidName.fluidhyd.getInstance(), 1500))
        ));
        Recipes.recipes.getRecipeFluid().addRecipe("fluid_separator", new BaseFluidMachineRecipe(
                new InputFluid(
                        new FluidStack(FluidName.fluidglucose.getInstance(), 1000)),
                Arrays.asList(new FluidStack(
                        FluidName.fluidpropionic_acid.getInstance(),
                        850
                ), new FluidStack(FluidName.fluidco2.getInstance(), 150))
        ));
        Recipes.recipes.getRecipeFluid().addRecipe("fluid_separator", new BaseFluidMachineRecipe(
                new InputFluid(
                        new FluidStack(FluidName.fluidacetic_acid.getInstance(), 500)),
                Arrays.asList(new FluidStack(
                        FluidName.fluidbiogas.getInstance(),
                        350
                ), new FluidStack(FluidRegistry.WATER, 150))
        ));

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


    public double getProgress() {
        return this.guiProgress;
    }

    public void onLoaded() {
        super.onLoaded();
        if (IUCore.proxy.isSimulating()) {
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
        ) && (output1.getValue() == null || this.outputSlot.canAdd(output1.getValue()))) {
            this.fluidSlot1.transferToTank(this.fluidTank1, output1, false);
            if (output1.getValue() != null) {
                this.outputSlot.add(output1.getValue());
            }
            check = true;
        }
        if (this.fluidTank2.getFluidAmount() - 1000 >= 0 && this.fluidSlot2.transferFromTank(
                this.fluidTank2,
                output1,
                true
        ) && (output1.getValue() == null || this.outputSlot.canAdd(output1.getValue()))) {
            this.fluidSlot2.transferFromTank(this.fluidTank2, output1, false);
            if (output1.getValue() != null) {
                this.outputSlot.add(output1.getValue());
            }
            check = true;
        }
        if (this.fluidTank3.getFluidAmount() - 1000 >= 0 && this.fluidSlot3.transferFromTank(
                this.fluidTank3,
                output1,
                true
        ) && (output1.getValue() == null || this.outputSlot.canAdd(output1.getValue()))) {
            this.fluidSlot3.transferFromTank(this.fluidTank3, output1, false);
            if (output1.getValue() != null) {
                this.outputSlot.add(output1.getValue());
            }
        }
        if (check || (this.fluid_handler.output() == null && this.fluidTank1.getFluidAmount() >= 1)) {
            this.fluid_handler.getOutput();
        } else {
            if (this.fluid_handler.output() != null && !this.fluid_handler.checkFluids()) {
                this.fluid_handler.setOutput(null);
            }
        }


        if (this.fluid_handler.output() != null && this.se.getEnergy() >= 5 && this.fluid_handler.canOperate() && this.fluid_handler.canFillFluid() && this.energy.canUseEnergy(
                energyConsume)) {
            if (!this.getActive()) {
                setActive(true);
            }
            if (this.progress == 0) {
                initiate(0);
            }
            this.se.useEnergy(5);
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
        for (int i = 0; i < this.operationsPerTick; i++) {
            operateOnce();

            this.fluid_handler.checkOutput();
            if (this.fluid_handler.output() == null) {
                break;
            }
        }
    }

    public void operateOnce() {
        this.fluid_handler.consume();
        this.fluid_handler.fillFluid();
    }


    public IMultiTileBlock getTeBlock() {
        return BlockBaseMachine3.fluid_separator;
    }

    public BlockTileEntity getBlock() {
        return IUItem.basemachine2;
    }

    public ContainerFluidSeparator getGuiContainer(EntityPlayer entityPlayer) {
        return new ContainerFluidSeparator(entityPlayer, this);
    }

    @SideOnly(Side.CLIENT)
    public GuiFluidSeparator getGui(EntityPlayer entityPlayer, boolean isAdmin) {
        return new GuiFluidSeparator(getGuiContainer(entityPlayer));
    }

    public Set<UpgradableProperty> getUpgradableProperties() {
        return EnumSet.of(UpgradableProperty.Processing, UpgradableProperty.Transformer,
                UpgradableProperty.EnergyStorage, UpgradableProperty.FluidExtract,
                UpgradableProperty.FluidInput
        );
    }


}
