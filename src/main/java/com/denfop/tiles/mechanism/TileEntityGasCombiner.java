package com.denfop.tiles.mechanism;

import com.denfop.IUItem;
import com.denfop.Localization;
import com.denfop.api.Recipes;
import com.denfop.api.inv.IAdvInventory;
import com.denfop.api.recipe.BaseFluidMachineRecipe;
import com.denfop.api.recipe.FluidHandlerRecipe;
import com.denfop.api.recipe.IHasRecipe;
import com.denfop.api.recipe.InputFluid;
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
import com.denfop.container.ContainerGasCombiner;
import com.denfop.gui.GuiCore;
import com.denfop.gui.GuiGasCombiner;
import com.denfop.invslot.InvSlot;
import com.denfop.invslot.InvSlotFluid;
import com.denfop.invslot.InvSlotFluidByList;
import com.denfop.invslot.InvSlotUpgrade;
import com.denfop.network.DecoderHandler;
import com.denfop.network.EncoderHandler;
import com.denfop.network.packet.CustomPacketBuffer;
import com.denfop.tiles.base.TileElectricMachine;
import com.denfop.utils.Keyboard;
import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.sounds.SoundEvent;
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

public class TileEntityGasCombiner extends TileElectricMachine implements IUpgradableBlock, IHasRecipe {

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
    public final InvSlotUpgrade upgradeSlot;
    public final InvSlotFluidByList fluidSlot3;
    public double energyConsume;
    public int operationLength;
    public int operationsPerTick;
    protected short progress;
    protected double guiProgress;

    public TileEntityGasCombiner(BlockPos pos, BlockState state) {
        super(100, 1, 3,BlockBaseMachine3.gas_combiner,pos,state);
        this.progress = 0;
        this.defaultEnergyConsume = this.energyConsume = 1;
        this.defaultOperationLength = this.operationLength = 100;
        this.defaultTier = 1;
        this.defaultEnergyStorage = 100;

        Fluids fluids = this.addComponent(new Fluids(this));
        this.fluidTank1 = fluids.addTankInsert("fluidTank1", 12 * 1000);


        this.fluidTank2 = fluids.addTankInsert("fluidTank2", 12 * 1000);


        this.fluidTank3 = fluids.addTank("fluidTank3", 12 * 1000, InvSlot.TypeItemSlot.OUTPUT);
        this.fluid_handler = new FluidHandlerRecipe("gas_combiner", fluids);
        this.fluidTank1.setAcceptedFluids(Fluids.fluidPredicate(this.fluid_handler.getFluids(0)));
        this.fluidTank2.setAcceptedFluids(Fluids.fluidPredicate(this.fluid_handler.getFluids(1)));
        this.fluidTank3.setAcceptedFluids(Fluids.fluidPredicate(this.fluid_handler.getOutputFluids(0)));
        this.upgradeSlot = new InvSlotUpgrade(this, 4);

        this.fluidSlot1 = new InvSlotFluidByList(this, 1, this.fluid_handler.getFluids(0));
        this.fluidSlot2 = new InvSlotFluidByList(this, 1, this.fluid_handler.getFluids(1));
        this.fluidSlot3 = new InvSlotFluidByList(this, 1, this.fluid_handler.getOutputFluids(0));
        this.fluidSlot3.setTypeFluidSlot(InvSlotFluid.TypeFluidSlot.OUTPUT);
        Recipes.recipes.getRecipeFluid().addInitRecipes(this);
        this.pollutionSoil = this.addComponent(new SoilPollutionComponent(this, 0.2));
        this.pollutionAir = this.addComponent(new AirPollutionComponent(this, 0.2));

    }

    public static int applyModifier(int base, int extra, double multiplier) {
        double ret = Math.round((base + extra) * multiplier);
        return (ret > 2.147483647E9D) ? Integer.MAX_VALUE : (int) ret;
    }

    @Override
    public SoundEvent getSound() {
        return EnumSound.air_collector.getSoundEvent();
    }

    @Override
    public void init() {
        Recipes.recipes.getRecipeFluid().addRecipe("gas_combiner", new BaseFluidMachineRecipe(new InputFluid(
                new FluidStack(FluidName.fluidcreosote.getInstance().get(), 1000), new FluidStack(
                FluidName.fluidneft.getInstance().get(),
                1000
        )), Collections.singletonList(new FluidStack(FluidName.fluidindustrialoil.getInstance().get(), 1000))));
        Recipes.recipes.getRecipeFluid().addRecipe("gas_combiner", new BaseFluidMachineRecipe(new InputFluid(
                new FluidStack(FluidName.fluidcreosote.getInstance().get(), 1000), new FluidStack(
                FluidName.fluidsweet_medium_oil.getInstance().get(),
                800
        )), Collections.singletonList(new FluidStack(FluidName.fluidindustrialoil.getInstance().get(), 1000))));
        Recipes.recipes.getRecipeFluid().addRecipe("gas_combiner", new BaseFluidMachineRecipe(new InputFluid(
                new FluidStack(FluidName.fluidcreosote.getInstance().get(), 1000), new FluidStack(
                FluidName.fluidsweet_heavy_oil.getInstance().get(),
                600
        )), Collections.singletonList(new FluidStack(FluidName.fluidindustrialoil.getInstance().get(), 1000))));
        Recipes.recipes.getRecipeFluid().addRecipe("gas_combiner", new BaseFluidMachineRecipe(new InputFluid(
                new FluidStack(FluidName.fluidcreosote.getInstance().get(), 1000), new FluidStack(
                FluidName.fluidsour_light_oil.getInstance().get(),
                1100
        )), Collections.singletonList(new FluidStack(FluidName.fluidindustrialoil.getInstance().get(), 1000))));
        Recipes.recipes.getRecipeFluid().addRecipe("gas_combiner", new BaseFluidMachineRecipe(new InputFluid(
                new FluidStack(FluidName.fluidcreosote.getInstance().get(), 1000),
                new FluidStack(
                        FluidName.fluidsour_medium_oil.getInstance().get(),
                        1300
                )
        ), Collections.singletonList(new FluidStack(FluidName.fluidindustrialoil.getInstance().get(), 1000))));
        Recipes.recipes.getRecipeFluid().addRecipe("gas_combiner", new BaseFluidMachineRecipe(new InputFluid(
                new FluidStack(
                        FluidName.fluidfluor.getInstance().get(),
                        200
                ),
                new FluidStack(FluidName.fluidhyd.getInstance().get(), 1000)
        ), Collections.singletonList(new FluidStack(FluidName.fluidfluorhyd.getInstance().get(), 200))));

        Recipes.recipes.getRecipeFluid().addRecipe("gas_combiner", new BaseFluidMachineRecipe(new InputFluid(
                new FluidStack(FluidName.fluidcreosote.getInstance().get(), 1000),
                new FluidStack(
                        FluidName.fluidsour_heavy_oil.getInstance().get(),
                        1500
                )
        ), Collections.singletonList(new FluidStack(FluidName.fluidindustrialoil.getInstance().get(), 1000))));

        Recipes.recipes.getRecipeFluid().addRecipe("gas_combiner", new BaseFluidMachineRecipe(new InputFluid(
                new FluidStack(FluidName.fluidbiomass.getInstance().get(), 500),
                new FluidStack(
                        net.minecraft.world.level.material.Fluids.WATER,
                        500
                )
        ), Collections.singletonList(new FluidStack(FluidName.fluidglucose.getInstance().get(), 1000))));

        Recipes.recipes.getRecipeFluid().addRecipe("gas_combiner", new BaseFluidMachineRecipe(new InputFluid(
                new FluidStack(FluidName.fluidpropane.getInstance().get(), 400), new FluidStack(
                FluidName.fluidbromine.getInstance().get(),
                800
        )), Collections.singletonList(new FluidStack(FluidName.fluiddibromopropane.getInstance().get(), 400))));
        Recipes.recipes.getRecipeFluid().addRecipe("gas_combiner", new BaseFluidMachineRecipe(new InputFluid(
                new FluidStack(FluidName.fluidacetylene.getInstance().get(), 400), new FluidStack(
                FluidName.fluidhyd.getInstance().get(),
                1000
        )), Collections.singletonList(new FluidStack(FluidName.fluidethylene.getInstance().get(), 400))));

        Recipes.recipes.getRecipeFluid().addRecipe("gas_combiner", new BaseFluidMachineRecipe(new InputFluid(
                new FluidStack(FluidName.fluidhydrogensulfide.getInstance().get(), 200),
                new FluidStack(
                        FluidName.fluidoxy.getInstance().get(),
                        300
                )
        ), Collections.singletonList(new FluidStack(FluidName.fluidsulfuroxide.getInstance().get(), 200))));


        Recipes.recipes.getRecipeFluid().addRecipe("gas_combiner", new BaseFluidMachineRecipe(new InputFluid(
                new FluidStack(FluidName.fluidsulfuroxide.getInstance().get(), 200),
                new FluidStack(
                        FluidName.fluidoxy.getInstance().get(),
                        100
                )
        ), Collections.singletonList(new FluidStack(FluidName.fluidsulfurtrioxide.getInstance().get(), 200))));

        Recipes.recipes.getRecipeFluid().addRecipe("gas_combiner", new BaseFluidMachineRecipe(new InputFluid(
                new FluidStack(FluidName.fluidsulfurtrioxide.getInstance().get(), 500),
                new FluidStack(
                        net.minecraft.world.level.material.Fluids.WATER,
                        500
                )
        ), Collections.singletonList(new FluidStack(FluidName.fluidsulfuricacid.getInstance().get(), 500))));

        Recipes.recipes.getRecipeFluid().addRecipe("gas_combiner", new BaseFluidMachineRecipe(new InputFluid(
                new FluidStack(FluidName.fluidazot.getInstance().get(), 100), new FluidStack(
                FluidName.fluidhyd.getInstance().get(),
                300
        )), Collections.singletonList(new FluidStack(FluidName.fluidnitrogenhydride.getInstance().get(), 200))));
        Recipes.recipes.getRecipeFluid().addRecipe("gas_combiner", new BaseFluidMachineRecipe(new InputFluid(
                new FluidStack(FluidName.fluidnitrogenoxy.getInstance().get(), 200), new FluidStack(
                FluidName.fluidoxy.getInstance().get(),
                100
        )), Collections.singletonList(new FluidStack(FluidName.fluidnitrogendioxide.getInstance().get(), 200))));

        Recipes.recipes.getRecipeFluid().addRecipe("gas_combiner", new BaseFluidMachineRecipe(new InputFluid(
                new FluidStack(FluidName.fluidnitrogendioxide.getInstance().get(), 400), new FluidStack(
                net.minecraft.world.level.material.Fluids.WATER,
                300
        )), Collections.singletonList(new FluidStack(FluidName.fluidnitricacid.getInstance().get(), 400))));

        Recipes.recipes.getRecipeFluid().addRecipe("gas_combiner", new BaseFluidMachineRecipe(new InputFluid(
                new FluidStack(FluidName.fluidtoluene.getInstance().get(), 400), new FluidStack(
                FluidName.fluidnitricacid.getInstance().get(),
                600
        )), Collections.singletonList(new FluidStack(FluidName.fluidtrinitrotoluene.getInstance().get(), 200))));

        Recipes.recipes.getRecipeFluid().addRecipe("gas_combiner", new BaseFluidMachineRecipe(new InputFluid(
                new FluidStack(FluidName.fluidethylene.getInstance().get(), 200), new FluidStack(
                FluidName.fluidhyd.getInstance().get(),
                200
        )), Collections.singletonList(new FluidStack(FluidName.fluidethane.getInstance().get(), 200))));

        Recipes.recipes.getRecipeFluid().addRecipe("gas_combiner", new BaseFluidMachineRecipe(new InputFluid(
                new FluidStack(FluidName.fluidethylene.getInstance().get(), 200), new FluidStack(
                net.minecraft.world.level.material.Fluids.WATER,
                200
        )), Collections.singletonList(new FluidStack(FluidName.fluidethanol.getInstance().get(), 200))));

        Recipes.recipes.getRecipeFluid().addRecipe("gas_combiner", new BaseFluidMachineRecipe(new InputFluid(
                new FluidStack(FluidName.fluidpolybutadiene.getInstance().get(), 200), new FluidStack(
                FluidName.fluidpolyacrylonitrile.getInstance().get(),
                200
        )), Collections.singletonList(new FluidStack(FluidName.fluidbutadiene_nitrile.getInstance().get(), 200))));

        Recipes.recipes.getRecipeFluid().addRecipe("gas_combiner", new BaseFluidMachineRecipe(new InputFluid(
                new FluidStack(FluidName.fluidethylene.getInstance().get(), 200), new FluidStack(
                FluidName.fluidhydrogenchloride.getInstance().get(),
                200
        )), Collections.singletonList(new FluidStack(FluidName.fluidchloroethane.getInstance().get(), 200))));

        Recipes.recipes.getRecipeFluid().addRecipe("gas_combiner", new BaseFluidMachineRecipe(new InputFluid(new FluidStack(
                FluidName.fluidchlorum.getInstance().get(),
                200
        ),   new FluidStack(FluidName.fluidhyd.getInstance().get(), 200)), Collections.singletonList(new FluidStack(FluidName.fluidhydrogenchloride.getInstance().get(), 400))));

        Recipes.recipes.getRecipeFluid().addRecipe("gas_combiner", new BaseFluidMachineRecipe(new InputFluid(
                new FluidStack(FluidName.fluidbenz.getInstance().get(), 500), new FluidStack(
                FluidName.fluidtetraethyllead.getInstance().get(),
                500
        )), Collections.singletonList(new FluidStack(FluidName.fluidpetrol90.getInstance().get(), 500))));

        Recipes.recipes.getRecipeFluid().addRecipe("gas_combiner", new BaseFluidMachineRecipe(new InputFluid(
                new FluidStack(FluidName.fluidpetrol90.getInstance().get(), 500), new FluidStack(
                FluidName.fluidtetraethyllead.getInstance().get(),
                500
        )), Collections.singletonList(new FluidStack(FluidName.fluidpetrol95.getInstance().get(), 500))));


        Recipes.recipes.getRecipeFluid().addRecipe("gas_combiner", new BaseFluidMachineRecipe(new InputFluid(
                new FluidStack(FluidName.fluidcarbonmonoxide.getInstance().get(), 200), new FluidStack(
                FluidName.fluidhyd.getInstance().get(),
                400
        )), Collections.singletonList(new FluidStack(FluidName.fluidmethanol.getInstance().get(), 200))));

        Recipes.recipes.getRecipeFluid().addRecipe("gas_combiner", new BaseFluidMachineRecipe(new InputFluid(
                new FluidStack(FluidName.fluidbutadiene.getInstance().get(), 200), new FluidStack(
                FluidName.fluidhyd.getInstance().get(),
                200
        )), Collections.singletonList(new FluidStack(FluidName.fluidbutene.getInstance().get(), 200))));


        Recipes.recipes.getRecipeFluid().addRecipe("gas_combiner", new BaseFluidMachineRecipe(new InputFluid(
                new FluidStack(FluidName.fluidbutene.getInstance().get(), 200), new FluidStack(
                FluidName.fluidsulfuricacid.getInstance().get(),
                200
        )), Collections.singletonList(new FluidStack(FluidName.fluidtertbutylsulfuricacid.getInstance().get(), 200))));

        Recipes.recipes.getRecipeFluid().addRecipe("gas_combiner", new BaseFluidMachineRecipe(new InputFluid(
                new FluidStack(FluidName.fluidisobutylene.getInstance().get(), 200), new FluidStack(
                FluidName.fluidmethanol.getInstance().get(),
                100
        )), Collections.singletonList(new FluidStack(FluidName.fluidtertbutylmethylether.getInstance().get(), 100))));

        Recipes.recipes.getRecipeFluid().addRecipe("gas_combiner", new BaseFluidMachineRecipe(new InputFluid(
                new FluidStack(FluidName.fluidpetrol95.getInstance().get(), 500),
                new FluidStack(
                        FluidName.fluidtertbutylmethylether.getInstance().get(),
                        500
                )
        ), Collections.singletonList(new FluidStack(FluidName.fluidpetrol100.getInstance().get(), 500))));

        Recipes.recipes.getRecipeFluid().addRecipe("gas_combiner", new BaseFluidMachineRecipe(new InputFluid(
                new FluidStack(FluidName.fluidpetrol100.getInstance().get(), 500),
                new FluidStack(
                        FluidName.fluidtertbutylmethylether.getInstance().get(),
                        500
                )
        ), Collections.singletonList(new FluidStack(FluidName.fluidpetrol105.getInstance().get(), 500))));

        Recipes.recipes.getRecipeFluid().addRecipe("gas_combiner", new BaseFluidMachineRecipe(new InputFluid(
                new FluidStack(FluidName.fluidmethylpentanal.getInstance().get(), 500),
                new FluidStack(
                        FluidName.fluidhyd.getInstance().get(),
                        1000
                )
        ), Collections.singletonList(new FluidStack(FluidName.fluidethylhexanol.getInstance().get(), 500))));

        Recipes.recipes.getRecipeFluid().addRecipe("gas_combiner", new BaseFluidMachineRecipe(new InputFluid(
                new FluidStack(FluidName.fluidmethylcyclohexylnitrate.getInstance().get(), 500),
                new FluidStack(
                        FluidName.fluiddizel.getInstance().get(),
                        500
                )
        ), Collections.singletonList(new FluidStack(FluidName.fluida_diesel.getInstance().get(), 500))));

        Recipes.recipes.getRecipeFluid().addRecipe("gas_combiner", new BaseFluidMachineRecipe(new InputFluid(
                new FluidStack(FluidName.fluidmethylcyclohexylnitrate.getInstance().get(), 500),
                new FluidStack(
                        FluidName.fluida_diesel.getInstance().get(),
                        500
                )
        ), Collections.singletonList(new FluidStack(FluidName.fluidaa_diesel.getInstance().get(), 500))));


        Recipes.recipes.getRecipeFluid().addRecipe("gas_combiner", new BaseFluidMachineRecipe(new InputFluid(
                new FluidStack(FluidName.fluidbenzene.getInstance().get(), 300),
                new FluidStack(
                        FluidName.fluidhyd.getInstance().get(),
                        900
                )
        ), Collections.singletonList(new FluidStack(FluidName.fluidcyclohexane.getInstance().get(), 300))));

        Recipes.recipes.getRecipeFluid().addRecipe("gas_combiner", new BaseFluidMachineRecipe(new InputFluid(
                new FluidStack(FluidName.fluidcyclohexane.getInstance().get(), 300),
                new FluidStack(
                        FluidName.fluidnitricacid.getInstance().get(),
                        300
                )
        ), Collections.singletonList(new FluidStack(FluidName.fluidmethylcyclohexylnitrate.getInstance().get(), 300))));


        Recipes.recipes.getRecipeFluid().addRecipe("gas_combiner", new BaseFluidMachineRecipe(new InputFluid(
                new FluidStack(FluidName.fluidethylhexylnitrate.getInstance().get(), 500),
                new FluidStack(
                        FluidName.fluidaa_diesel.getInstance().get(),
                        500
                )
        ), Collections.singletonList(new FluidStack(FluidName.fluidaaa_diesel.getInstance().get(), 500))));

        Recipes.recipes.getRecipeFluid().addRecipe("gas_combiner", new BaseFluidMachineRecipe(new InputFluid(
                new FluidStack(FluidName.fluidethylhexylnitrate.getInstance().get(), 500),
                new FluidStack(
                        FluidName.fluidaaa_diesel.getInstance().get(),
                        500
                )
        ), Collections.singletonList(new FluidStack(FluidName.fluidaaaa_diesel.getInstance().get(), 500))));

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
        if (this.fluidTank2.getFluidAmount() + 1000 <= this.fluidTank2.getCapacity() && this.fluidSlot2.transferToTank(
                this.fluidTank2,
                output1,
                true
        ) && (output1.getValue() == null || this.outputSlot.canAdd(output1.getValue()))) {
            this.fluidSlot2.transferToTank(this.fluidTank2, output1, false);
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
        if (check || (this.fluid_handler.output() == null && this.fluidTank2.getFluidAmount() >= 1 && this.fluidTank1.getFluidAmount() >= 1)) {
            this.fluid_handler.getOutput();
        } else {
            if (this.fluid_handler.output() != null && !this.fluid_handler.checkFluids()) {
                this.fluid_handler.setOutput(null);
            }
        }


        if (this.fluid_handler.output() != null && this.fluid_handler.canOperate() && this.fluid_handler.canFillFluid() && this.energy.canUseEnergy(
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
        return BlockBaseMachine3.gas_combiner;
    }

    public BlockTileEntity getBlock() {
        return IUItem.basemachine2.getBlock(getTeBlock());
    }

    public ContainerGasCombiner getGuiContainer(Player entityPlayer) {
        return new ContainerGasCombiner(entityPlayer, this);
    }

    @Override
    @OnlyIn(Dist.CLIENT)
    public GuiCore<ContainerBase<? extends IAdvInventory>> getGui(Player var1, ContainerBase<? extends IAdvInventory> menu) {

        return new GuiGasCombiner((ContainerGasCombiner) menu);
    }

    public Set<UpgradableProperty> getUpgradableProperties() {
        return EnumSet.of(UpgradableProperty.Processing, UpgradableProperty.Transformer,
                UpgradableProperty.EnergyStorage, UpgradableProperty.FluidExtract,
                UpgradableProperty.FluidInput
        );
    }


}
