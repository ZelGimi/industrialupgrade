package com.denfop.tiles.mechanism;

import com.denfop.IUCore;
import com.denfop.IUItem;
import com.denfop.Localization;
import com.denfop.api.Recipes;
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
import com.denfop.container.ContainerGasCombiner;
import com.denfop.gui.GuiGasCombiner;
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
import net.minecraft.util.EnumFacing;
import net.minecraft.util.SoundEvent;
import net.minecraft.util.math.BlockPos;
import net.minecraftforge.fluids.FluidRegistry;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import org.apache.commons.lang3.mutable.MutableObject;
import org.lwjgl.input.Keyboard;

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

    public TileEntityGasCombiner() {
        super(100, 1, 3);
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
                new FluidStack(FluidName.fluidcreosote.getInstance(), 2000), new FluidStack(
                FluidName.fluidneft.getInstance(),
                1000
        )), Collections.singletonList(new FluidStack(FluidName.fluidindustrialoil.getInstance(), 1000))));
        Recipes.recipes.getRecipeFluid().addRecipe("gas_combiner", new BaseFluidMachineRecipe(new InputFluid(
                new FluidStack(FluidName.fluidcreosote.getInstance(), 2000), new FluidStack(
                FluidName.fluidsweet_medium_oil.getInstance(),
                800
        )), Collections.singletonList(new FluidStack(FluidName.fluidindustrialoil.getInstance(), 1000))));
        Recipes.recipes.getRecipeFluid().addRecipe("gas_combiner", new BaseFluidMachineRecipe(new InputFluid(
                new FluidStack(FluidName.fluidcreosote.getInstance(), 2000), new FluidStack(
                FluidName.fluidsweet_heavy_oil.getInstance(),
                600
        )), Collections.singletonList(new FluidStack(FluidName.fluidindustrialoil.getInstance(), 1000))));
        Recipes.recipes.getRecipeFluid().addRecipe("gas_combiner", new BaseFluidMachineRecipe(new InputFluid(
                new FluidStack(FluidName.fluidcreosote.getInstance(), 2000), new FluidStack(
                FluidName.fluidsour_light_oil.getInstance(),
                1100
        )), Collections.singletonList(new FluidStack(FluidName.fluidindustrialoil.getInstance(), 1000))));
        Recipes.recipes.getRecipeFluid().addRecipe("gas_combiner", new BaseFluidMachineRecipe(new InputFluid(
                new FluidStack(FluidName.fluidcreosote.getInstance(), 2000),
                new FluidStack(
                        FluidName.fluidsour_medium_oil.getInstance(),
                        1300
                )
        ), Collections.singletonList(new FluidStack(FluidName.fluidindustrialoil.getInstance(), 1000))));
        Recipes.recipes.getRecipeFluid().addRecipe("gas_combiner", new BaseFluidMachineRecipe(new InputFluid(
                new FluidStack(FluidName.fluidhyd.getInstance(), 200),
                new FluidStack(
                        FluidName.fluidfluor.getInstance(),
                        200
                )
        ), Collections.singletonList(new FluidStack(FluidName.fluidfluorhyd.getInstance(), 200))));

        Recipes.recipes.getRecipeFluid().addRecipe("gas_combiner", new BaseFluidMachineRecipe(new InputFluid(
                new FluidStack(FluidName.fluidcreosote.getInstance(), 2000),
                new FluidStack(
                        FluidName.fluidsour_heavy_oil.getInstance(),
                        1500
                )
        ), Collections.singletonList(new FluidStack(FluidName.fluidindustrialoil.getInstance(), 1000))));

        Recipes.recipes.getRecipeFluid().addRecipe("gas_combiner", new BaseFluidMachineRecipe(new InputFluid(
                new FluidStack(FluidName.fluidbiomass.getInstance(), 500),
                new FluidStack(
                     FluidRegistry.WATER,
                        500
                )
        ), Collections.singletonList(new FluidStack(FluidName.fluidglucose.getInstance(), 1000))));

        Recipes.recipes.getRecipeFluid().addRecipe("gas_combiner", new BaseFluidMachineRecipe(new InputFluid(
                new FluidStack(FluidName.fluidpropane.getInstance(), 400), new FluidStack(
                FluidName.fluidbromine.getInstance(),
                800
        )), Collections.singletonList(new FluidStack(FluidName.fluiddibromopropane.getInstance(), 400))));
        Recipes.recipes.getRecipeFluid().addRecipe("gas_combiner", new BaseFluidMachineRecipe(new InputFluid(
                new FluidStack(FluidName.fluidacetylene.getInstance(), 400), new FluidStack(
                FluidName.fluidhyd.getInstance(),
                1000
        )), Collections.singletonList(new FluidStack(FluidName.fluidethylene.getInstance(), 400))));

        Recipes.recipes.getRecipeFluid().addRecipe("gas_combiner", new BaseFluidMachineRecipe(new InputFluid(
                new FluidStack(FluidName.fluidhydrogensulfide.getInstance(), 200),
                new FluidStack(
                        FluidName.fluidoxy.getInstance(),
                        300
                )
        ), Collections.singletonList(new FluidStack(FluidName.fluidsulfuroxide.getInstance(), 200))));


        Recipes.recipes.getRecipeFluid().addRecipe("gas_combiner", new BaseFluidMachineRecipe(new InputFluid(
                new FluidStack(FluidName.fluidsulfuroxide.getInstance(), 200),
                new FluidStack(
                        FluidName.fluidoxy.getInstance(),
                        100
                )
        ), Collections.singletonList(new FluidStack(FluidName.fluidsulfurtrioxide.getInstance(), 200))));

        Recipes.recipes.getRecipeFluid().addRecipe("gas_combiner", new BaseFluidMachineRecipe(new InputFluid(
                new FluidStack(FluidName.fluidsulfurtrioxide.getInstance(), 500),
                new FluidStack(
                        FluidRegistry.WATER,
                        500
                )
        ), Collections.singletonList(new FluidStack(FluidName.fluidsulfuricacid.getInstance(), 500))));

        Recipes.recipes.getRecipeFluid().addRecipe("gas_combiner", new BaseFluidMachineRecipe(new InputFluid(
                new FluidStack(FluidName.fluidazot.getInstance(), 100), new FluidStack(
                FluidName.fluidhyd.getInstance(),
                300
        )), Collections.singletonList(new FluidStack(FluidName.fluidnitrogenhydride.getInstance(), 200))));
        Recipes.recipes.getRecipeFluid().addRecipe("gas_combiner", new BaseFluidMachineRecipe(new InputFluid(
                new FluidStack(FluidName.fluidnitrogenoxy.getInstance(), 200), new FluidStack(
                FluidName.fluidoxy.getInstance(),
                100
        )), Collections.singletonList(new FluidStack(FluidName.fluidnitrogendioxide.getInstance(), 200))));

        Recipes.recipes.getRecipeFluid().addRecipe("gas_combiner", new BaseFluidMachineRecipe(new InputFluid(
                new FluidStack(FluidName.fluidnitrogendioxide.getInstance(), 400), new FluidStack(
                FluidRegistry.WATER,
                300
        )), Collections.singletonList(new FluidStack(FluidName.fluidnitricacid.getInstance(), 400))));

        Recipes.recipes.getRecipeFluid().addRecipe("gas_combiner", new BaseFluidMachineRecipe(new InputFluid(
                new FluidStack(FluidName.fluidtoluene.getInstance(), 400), new FluidStack(
                FluidName.fluidnitricacid.getInstance(),
                600
        )), Collections.singletonList(new FluidStack(FluidName.fluidtrinitrotoluene.getInstance(), 200))));

        Recipes.recipes.getRecipeFluid().addRecipe("gas_combiner", new BaseFluidMachineRecipe(new InputFluid(
                new FluidStack(FluidName.fluidethylene.getInstance(), 200), new FluidStack(
                FluidName.fluidhyd.getInstance(),
                200
        )), Collections.singletonList(new FluidStack(FluidName.fluidethane.getInstance(), 200))));

        Recipes.recipes.getRecipeFluid().addRecipe("gas_combiner", new BaseFluidMachineRecipe(new InputFluid(
                new FluidStack(FluidName.fluidethylene.getInstance(), 200), new FluidStack(
                FluidRegistry.WATER,
                200
        )), Collections.singletonList(new FluidStack(FluidName.fluidethanol.getInstance(), 200))));

        Recipes.recipes.getRecipeFluid().addRecipe("gas_combiner", new BaseFluidMachineRecipe(new InputFluid(
                new FluidStack(FluidName.fluidpolybutadiene.getInstance(), 200), new FluidStack(
                FluidName.fluidpolyacrylonitrile.getInstance(),
                200
        )), Collections.singletonList(new FluidStack(FluidName.fluidbutadiene_nitrile.getInstance(), 200))));

        Recipes.recipes.getRecipeFluid().addRecipe("gas_combiner", new BaseFluidMachineRecipe(new InputFluid(
                new FluidStack(FluidName.fluidethylene.getInstance(), 200), new FluidStack(
                FluidName.fluidhydrogenchloride.getInstance(),
                200
        )), Collections.singletonList(new FluidStack(FluidName.fluidchloroethane.getInstance(), 200))));

        Recipes.recipes.getRecipeFluid().addRecipe("gas_combiner", new BaseFluidMachineRecipe(new InputFluid(
                new FluidStack(FluidName.fluidhyd.getInstance(), 200), new FluidStack(
                FluidName.fluidchlorum.getInstance(),
                200
        )), Collections.singletonList(new FluidStack(FluidName.fluidhydrogenchloride.getInstance(), 400))));

        Recipes.recipes.getRecipeFluid().addRecipe("gas_combiner", new BaseFluidMachineRecipe(new InputFluid(
                new FluidStack(FluidName.fluidbenz.getInstance(), 500), new FluidStack(
                FluidName.fluidtetraethyllead.getInstance(),
                500
        )), Collections.singletonList(new FluidStack(FluidName.fluidpetrol90.getInstance(), 500))));

        Recipes.recipes.getRecipeFluid().addRecipe("gas_combiner", new BaseFluidMachineRecipe(new InputFluid(
                new FluidStack(FluidName.fluidpetrol90.getInstance(), 500), new FluidStack(
                FluidName.fluidtetraethyllead.getInstance(),
                500
        )), Collections.singletonList(new FluidStack(FluidName.fluidpetrol95.getInstance(), 500))));


        Recipes.recipes.getRecipeFluid().addRecipe("gas_combiner", new BaseFluidMachineRecipe(new InputFluid(
                new FluidStack(FluidName.fluidcarbonmonoxide.getInstance(), 200), new FluidStack(
                FluidName.fluidhyd.getInstance(),
                400
        )), Collections.singletonList(new FluidStack(FluidName.fluidmethanol.getInstance(), 200))));

        Recipes.recipes.getRecipeFluid().addRecipe("gas_combiner", new BaseFluidMachineRecipe(new InputFluid(
                new FluidStack(FluidName.fluidbutadiene.getInstance(), 200), new FluidStack(
                FluidName.fluidhyd.getInstance(),
                200
        )), Collections.singletonList(new FluidStack(FluidName.fluidbutene.getInstance(), 200))));


        Recipes.recipes.getRecipeFluid().addRecipe("gas_combiner", new BaseFluidMachineRecipe(new InputFluid(
                new FluidStack(FluidName.fluidbutene.getInstance(), 200), new FluidStack(
                FluidName.fluidsulfuricacid.getInstance(),
                200
        )), Collections.singletonList(new FluidStack(FluidName.fluidtertbutylsulfuricacid.getInstance(), 200))));

        Recipes.recipes.getRecipeFluid().addRecipe("gas_combiner", new BaseFluidMachineRecipe(new InputFluid(
                new FluidStack(FluidName.fluidisobutylene.getInstance(), 200), new FluidStack(
                FluidName.fluidmethanol.getInstance(),
                100
        )), Collections.singletonList(new FluidStack(FluidName.fluidtertbutylmethylether.getInstance(), 100))));

        Recipes.recipes.getRecipeFluid().addRecipe("gas_combiner", new BaseFluidMachineRecipe(new InputFluid(
                new FluidStack(FluidName.fluidpetrol95.getInstance(), 500),
                new FluidStack(
                        FluidName.fluidtertbutylmethylether.getInstance(),
                        500
                )
        ), Collections.singletonList(new FluidStack(FluidName.fluidpetrol100.getInstance(), 500))));

        Recipes.recipes.getRecipeFluid().addRecipe("gas_combiner", new BaseFluidMachineRecipe(new InputFluid(
                new FluidStack(FluidName.fluidpetrol100.getInstance(), 500),
                new FluidStack(
                        FluidName.fluidtertbutylmethylether.getInstance(),
                        500
                )
        ), Collections.singletonList(new FluidStack(FluidName.fluidpetrol105.getInstance(), 500))));

        Recipes.recipes.getRecipeFluid().addRecipe("gas_combiner", new BaseFluidMachineRecipe(new InputFluid(
                new FluidStack(FluidName.fluidmethylpentanal.getInstance(), 500),
                new FluidStack(
                        FluidName.fluidhyd.getInstance(),
                        1000
                )
        ), Collections.singletonList(new FluidStack(FluidName.fluidethylhexanol.getInstance(), 500))));

        Recipes.recipes.getRecipeFluid().addRecipe("gas_combiner", new BaseFluidMachineRecipe(new InputFluid(
                new FluidStack(FluidName.fluidmethylcyclohexylnitrate.getInstance(), 500),
                new FluidStack(
                        FluidName.fluiddizel.getInstance(),
                        500
                )
        ), Collections.singletonList(new FluidStack(FluidName.fluida_diesel.getInstance(), 500))));

        Recipes.recipes.getRecipeFluid().addRecipe("gas_combiner", new BaseFluidMachineRecipe(new InputFluid(
                new FluidStack(FluidName.fluidmethylcyclohexylnitrate.getInstance(), 500),
                new FluidStack(
                        FluidName.fluida_diesel.getInstance(),
                        500
                )
        ), Collections.singletonList(new FluidStack(FluidName.fluidaa_diesel.getInstance(), 500))));


        Recipes.recipes.getRecipeFluid().addRecipe("gas_combiner", new BaseFluidMachineRecipe(new InputFluid(
                new FluidStack(FluidName.fluidbenzene.getInstance(), 300),
                new FluidStack(
                        FluidName.fluidhyd.getInstance(),
                        900
                )
        ), Collections.singletonList(new FluidStack(FluidName.fluidcyclohexane.getInstance(), 300))));

        Recipes.recipes.getRecipeFluid().addRecipe("gas_combiner", new BaseFluidMachineRecipe(new InputFluid(
                new FluidStack(FluidName.fluidcyclohexane.getInstance(), 300),
                new FluidStack(
                        FluidName.fluidnitricacid.getInstance(),
                        300
                )
        ), Collections.singletonList(new FluidStack(FluidName.fluidmethylcyclohexylnitrate.getInstance(), 300))));


        Recipes.recipes.getRecipeFluid().addRecipe("gas_combiner", new BaseFluidMachineRecipe(new InputFluid(
                new FluidStack(FluidName.fluidethylhexylnitrate.getInstance(), 500),
                new FluidStack(
                        FluidName.fluidaa_diesel.getInstance(),
                        500
                )
        ), Collections.singletonList(new FluidStack(FluidName.fluidaaa_diesel.getInstance(), 500))));

        Recipes.recipes.getRecipeFluid().addRecipe("gas_combiner", new BaseFluidMachineRecipe(new InputFluid(
                new FluidStack(FluidName.fluidethylhexylnitrate.getInstance(), 500),
                new FluidStack(
                        FluidName.fluidaaa_diesel.getInstance(),
                        500
                )
        ), Collections.singletonList(new FluidStack(FluidName.fluidaaaa_diesel.getInstance(), 500))));

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

    @SideOnly(Side.CLIENT)
    public boolean shouldSideBeRendered(EnumFacing side, BlockPos otherPos) {
        return false;
    }

    public boolean isNormalCube() {
        return false;
    }

    public boolean doesSideBlockRendering(EnumFacing side) {
        return false;
    }

    public boolean isSideSolid(EnumFacing side) {
        return false;
    }

    public boolean clientNeedsExtraModelInfo() {
        return true;
    }

    public boolean shouldRenderInPass(int pass) {
        return true;
    }

    public IMultiTileBlock getTeBlock() {
        return BlockBaseMachine3.gas_combiner;
    }

    public BlockTileEntity getBlock() {
        return IUItem.basemachine2;
    }

    public ContainerGasCombiner getGuiContainer(EntityPlayer entityPlayer) {
        return new ContainerGasCombiner(entityPlayer, this);
    }

    @SideOnly(Side.CLIENT)
    public GuiGasCombiner getGui(EntityPlayer entityPlayer, boolean isAdmin) {
        return new GuiGasCombiner(getGuiContainer(entityPlayer));
    }

    public Set<UpgradableProperty> getUpgradableProperties() {
        return EnumSet.of(UpgradableProperty.Processing, UpgradableProperty.Transformer,
                UpgradableProperty.EnergyStorage, UpgradableProperty.FluidExtract,
                UpgradableProperty.FluidInput
        );
    }


}
