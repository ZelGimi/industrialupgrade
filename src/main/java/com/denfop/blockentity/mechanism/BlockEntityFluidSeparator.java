package com.denfop.blockentity.mechanism;

import com.denfop.IUItem;
import com.denfop.api.Recipes;
import com.denfop.api.blockentity.MultiBlockEntity;
import com.denfop.api.container.CustomWorldContainer;
import com.denfop.api.otherenergies.common.EnergyType;
import com.denfop.api.recipe.BaseFluidMachineRecipe;
import com.denfop.api.recipe.FluidHandlerRecipe;
import com.denfop.api.recipe.IHasRecipe;
import com.denfop.api.recipe.InputFluid;
import com.denfop.api.upgrades.BlockEntityUpgrade;
import com.denfop.api.upgrades.EnumBlockEntityUpgrade;
import com.denfop.blockentity.base.BlockEntityElectricMachine;
import com.denfop.blocks.BlockTileEntity;
import com.denfop.blocks.FluidName;
import com.denfop.blocks.mechanism.BlockBaseMachine3Entity;
import com.denfop.componets.AirPollutionComponent;
import com.denfop.componets.ComponentBaseEnergy;
import com.denfop.componets.Fluids;
import com.denfop.componets.SoilPollutionComponent;
import com.denfop.containermenu.ContainerMenuBase;
import com.denfop.containermenu.ContainerMenuFluidSeparator;
import com.denfop.inventory.Inventory;
import com.denfop.inventory.InventoryFluid;
import com.denfop.inventory.InventoryFluidByList;
import com.denfop.inventory.InventoryUpgrade;
import com.denfop.network.DecoderHandler;
import com.denfop.network.EncoderHandler;
import com.denfop.network.packet.CustomPacketBuffer;
import com.denfop.screen.ScreenFluidSeparator;
import com.denfop.screen.ScreenIndustrialUpgrade;
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
import java.util.Arrays;
import java.util.EnumSet;
import java.util.List;
import java.util.Set;

public class BlockEntityFluidSeparator extends BlockEntityElectricMachine implements BlockEntityUpgrade, IHasRecipe {

    public final FluidHandlerRecipe fluid_handler;
    public final Fluids.InternalFluidTank fluidTank2;
    public final Fluids.InternalFluidTank fluidTank1;
    public final InventoryFluidByList fluidSlot1;
    public final InventoryFluidByList fluidSlot2;
    public final Fluids.InternalFluidTank fluidTank3;
    public final double defaultEnergyConsume;
    public final int defaultOperationLength;
    public final int defaultTier;
    public final double defaultEnergyStorage;
    public final ComponentBaseEnergy se;
    public final InventoryUpgrade upgradeSlot;
    public final InventoryFluidByList fluidSlot3;
    public double energyConsume;
    public int operationLength;
    public int operationsPerTick;
    protected short progress;
    protected double guiProgress;

    public BlockEntityFluidSeparator(BlockPos pos, BlockState state) {
        super(100, 1, 3, BlockBaseMachine3Entity.fluid_separator, pos, state);
        this.progress = 0;
        this.defaultEnergyConsume = this.energyConsume = 1;
        this.defaultOperationLength = this.operationLength = 100;
        this.defaultTier = 1;
        this.defaultEnergyStorage = 100;

        this.addComponent(new SoilPollutionComponent(this, 0.1));
        this.addComponent(new AirPollutionComponent(this, 0.1));
        Fluids fluids = this.addComponent(new Fluids(this));
        this.fluidTank1 = fluids.addTank("fluidTank1", 12 * 1000, Inventory.TypeItemSlot.INPUT);


        this.fluidTank2 = fluids.addTank("fluidTank2", 12 * 1000, Inventory.TypeItemSlot.OUTPUT);


        this.fluidTank3 = fluids.addTank("fluidTank3", 12 * 1000, Inventory.TypeItemSlot.OUTPUT);
        this.fluid_handler = new FluidHandlerRecipe("fluid_separator", fluids);
        this.fluidTank1.setAcceptedFluids(Fluids.fluidPredicate(this.fluid_handler.getFluids(0)));
        this.fluidTank2.setAcceptedFluids(Fluids.fluidPredicate(this.fluid_handler.getOutputFluids(0)));
        this.fluidTank3.setAcceptedFluids(Fluids.fluidPredicate(this.fluid_handler.getOutputFluids(1)));
        this.upgradeSlot = new InventoryUpgrade(this, 4);

        this.fluidSlot1 = new InventoryFluidByList(this, 1, this.fluid_handler.getFluids(0));
        this.fluidSlot2 = new InventoryFluidByList(this, 1, this.fluid_handler.getOutputFluids(0));
        this.fluidSlot3 = new InventoryFluidByList(this, 1, this.fluid_handler.getOutputFluids(1));
        this.se = this.addComponent(ComponentBaseEnergy.asBasicSink(EnergyType.SOLARIUM, this, 1000));
        this.fluidSlot2.setTypeFluidSlot(InventoryFluid.TypeFluidSlot.OUTPUT);
        this.fluidSlot3.setTypeFluidSlot(InventoryFluid.TypeFluidSlot.OUTPUT);
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
                        new FluidStack(FluidName.fluidgas.getInstance().get(), 1000)),
                Arrays.asList(new FluidStack(
                        FluidName.fluidmethane.getInstance().get(),
                        500
                ), new FluidStack(FluidName.fluidpropane.getInstance().get(), 200))
        ));

        Recipes.recipes.getRecipeFluid().addRecipe("fluid_separator", new BaseFluidMachineRecipe(
                new InputFluid(
                        new FluidStack(FluidName.fluidmethane.getInstance().get(), 1000)),
                Arrays.asList(new FluidStack(
                        FluidName.fluidacetylene.getInstance().get(),
                        500
                ), new FluidStack(FluidName.fluidhydrogen.getInstance().get(), 1500))
        ));
        Recipes.recipes.getRecipeFluid().addRecipe("fluid_separator", new BaseFluidMachineRecipe(
                new InputFluid(
                        new FluidStack(FluidName.fluidglucose.getInstance().get(), 1000)),
                Arrays.asList(new FluidStack(
                        FluidName.fluidpropionic_acid.getInstance().get(),
                        850
                ), new FluidStack(FluidName.fluidcarbondioxide.getInstance().get(), 150))
        ));
        Recipes.recipes.getRecipeFluid().addRecipe("fluid_separator", new BaseFluidMachineRecipe(
                new InputFluid(
                        new FluidStack(FluidName.fluidacetic_acid.getInstance().get(), 500)),
                Arrays.asList(new FluidStack(
                        FluidName.fluidbiogas.getInstance().get(),
                        350
                ), new FluidStack(net.minecraft.world.level.material.Fluids.WATER, 150))
        ));

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
        if (!level.isClientSide) {
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


    public MultiBlockEntity getTeBlock() {
        return BlockBaseMachine3Entity.fluid_separator;
    }

    public BlockTileEntity getBlock() {
        return IUItem.basemachine2.getBlock(getTeBlock());
    }

    public ContainerMenuFluidSeparator getGuiContainer(Player entityPlayer) {
        return new ContainerMenuFluidSeparator(entityPlayer, this);
    }

    @Override
    @OnlyIn(Dist.CLIENT)
    public ScreenIndustrialUpgrade<ContainerMenuBase<? extends CustomWorldContainer>> getGui(Player var1, ContainerMenuBase<? extends CustomWorldContainer> menu) {
        return new ScreenFluidSeparator((ContainerMenuFluidSeparator) menu);
    }

    public Set<EnumBlockEntityUpgrade> getUpgradableProperties() {
        return EnumSet.of(EnumBlockEntityUpgrade.Processing, EnumBlockEntityUpgrade.Transformer,
                EnumBlockEntityUpgrade.EnergyStorage, EnumBlockEntityUpgrade.FluidExtract,
                EnumBlockEntityUpgrade.FluidInput
        );
    }


}
