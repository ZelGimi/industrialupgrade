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
import com.denfop.container.ContainerFluidMixer;
import com.denfop.gui.GuiFluidMixer;
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

public class TileEntityFluidMixer extends TileElectricMachine implements IUpgradableBlock, IHasRecipe {

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
    public final Fluids.InternalFluidTank fluidTank4;
    public final InvSlotFluidByList fluidSlot4;
    public final InvSlotUpgrade upgradeSlot;
    public final InvSlotFluidByList fluidSlot3;
    public double energyConsume;
    public int operationLength;
    public int operationsPerTick;
    protected short progress;
    protected double guiProgress;

    public TileEntityFluidMixer() {
        super(100, 1, 4);
        this.progress = 0;
        this.defaultEnergyConsume = this.energyConsume = 1;
        this.defaultOperationLength = this.operationLength = 100;
        this.defaultTier = 1;
        this.defaultEnergyStorage = 100;

        this.addComponent(new SoilPollutionComponent(this, 0.1));
        this.addComponent(new AirPollutionComponent(this, 0.1));
        Fluids fluids = this.addComponent(new Fluids(this));
        this.fluidTank1 = fluids.addTankInsert("fluidTank1", 12 * 1000);


        this.fluidTank2 = fluids.addTankInsert("fluidTank2", 12 * 1000);


        this.fluidTank3 = fluids.addTank("fluidTank3", 12 * 1000, InvSlot.TypeItemSlot.OUTPUT);
        this.fluidTank4 = fluids.addTank("fluidTank4", 12 * 1000, InvSlot.TypeItemSlot.OUTPUT);
        this.fluid_handler = new FluidHandlerRecipe("fluid_mixer", fluids);
        this.fluidTank1.setAcceptedFluids(Fluids.fluidPredicate(this.fluid_handler.getFluids(0)));
        this.fluidTank2.setAcceptedFluids(Fluids.fluidPredicate(this.fluid_handler.getFluids(1)));
        this.fluidTank3.setAcceptedFluids(Fluids.fluidPredicate(this.fluid_handler.getOutputFluids(0)));
        this.fluidTank4.setAcceptedFluids(Fluids.fluidPredicate(this.fluid_handler.getOutputFluids(1)));
        this.upgradeSlot = new InvSlotUpgrade(this, 4);

        this.fluidSlot1 = new InvSlotFluidByList(this, 1, this.fluid_handler.getFluids(0));
        this.fluidSlot2 = new InvSlotFluidByList(this, 1, this.fluid_handler.getFluids(1));
        this.fluidSlot3 = new InvSlotFluidByList(this, 1, this.fluid_handler.getOutputFluids(0));
        this.fluidSlot4 = new InvSlotFluidByList(this, 1, this.fluid_handler.getOutputFluids(1));
        this.fluidSlot3.setTypeFluidSlot(InvSlotFluid.TypeFluidSlot.OUTPUT);
        this.fluidSlot4.setTypeFluidSlot(InvSlotFluid.TypeFluidSlot.OUTPUT);
        Recipes.recipes.getRecipeFluid().addInitRecipes(this);

    }

    public static int applyModifier(int base, int extra, double multiplier) {
        double ret = Math.round((base + extra) * multiplier);
        return (ret > 2.147483647E9D) ? Integer.MAX_VALUE : (int) ret;
    }

    @Override
    public SoundEvent getSound() {
        return EnumSound.fluid_mixer.getSoundEvent();
    }

    @Override
    public void init() {
        Recipes.recipes.getRecipeFluid().addRecipe("fluid_mixer", new BaseFluidMachineRecipe(new InputFluid(
                new FluidStack(FluidName.fluidnitricoxide.getInstance(), 100), new FluidStack(
                FluidName.fluidhyd.getInstance(),
                100
        )), Arrays.asList(new FluidStack(FluidName.fluidazot.getInstance(), 100), new FluidStack(
                FluidRegistry.WATER,
                100
        ))));
        Recipes.recipes.getRecipeFluid().addRecipe("fluid_mixer", new BaseFluidMachineRecipe(new InputFluid(
                new FluidStack(FluidName.fluidnitrogenhydride.getInstance(), 400),
                new FluidStack(
                        FluidName.fluidoxy.getInstance(),
                        400
                )
        ), Arrays.asList(new FluidStack(FluidName.fluidnitrogenoxy.getInstance(), 400), new FluidStack(
                FluidRegistry.WATER,
                600
        ))));

        Recipes.recipes.getRecipeFluid().addRecipe("fluid_mixer", new BaseFluidMachineRecipe(new InputFluid(
                new FluidStack(FluidName.fluidmethane.getInstance(), 100), new FluidStack(
                FluidName.fluidbromine.getInstance(),
                100
        )), Arrays.asList(
                new FluidStack(FluidName.fluidhydrogenbromide.getInstance(), 100),
                new FluidStack(
                        FluidName.fluidmethylbromide.getInstance(),
                        100
                )
        )));
        Recipes.recipes.getRecipeFluid().addRecipe("fluid_mixer", new BaseFluidMachineRecipe(new InputFluid(
                new FluidStack(FluidName.fluidpropionic_acid.getInstance(), 250), new FluidStack(
                FluidRegistry.WATER,
                500
        )), Arrays.asList(
                new FluidStack(FluidName.fluidhyd.getInstance(), 500),
                new FluidStack(
                        FluidName.fluidacetic_acid.getInstance(),
                        250
                )
        )));
        Recipes.recipes.getRecipeFluid().addRecipe("fluid_mixer", new BaseFluidMachineRecipe(new InputFluid(
                new FluidStack(FluidName.fluidaniline.getInstance(), 100), new FluidStack(
                FluidName.fluidchlorum.getInstance(),
                300
        )), Arrays.asList(
                new FluidStack(FluidName.fluidtrichloroaniline.getInstance(), 100),
                new FluidStack(
                        FluidName.fluidhydrogenchloride.getInstance(),
                        150
                )
        )));
        Recipes.recipes.getRecipeFluid().addRecipe("fluid_mixer", new BaseFluidMachineRecipe(new InputFluid(
                new FluidStack(FluidName.fluidmethylsulfate.getInstance(), 250), new FluidStack(
                FluidName.fluidtrichloroaniline.getInstance(),
                100
        )), Arrays.asList(
                new FluidStack(FluidName.fluidmethyltrichloroaniline.getInstance(), 100),
                new FluidStack(
                        FluidName.fluidsulfuricacid.getInstance(),
                        200
                )
        )));

        Recipes.recipes.getRecipeFluid().addRecipe("fluid_mixer", new BaseFluidMachineRecipe(new InputFluid(
                new FluidStack(FluidName.fluidmethyltrichloroaniline.getInstance(), 200), new FluidStack(
                FluidName.fluidmethanol.getInstance(),
                100
        )), Arrays.asList(
                new FluidStack(FluidName.fluidweed_ex.getInstance(), 100),
                new FluidStack(
                        FluidName.fluidmethylchloride.getInstance(),
                        100
                )
        )));

        Recipes.recipes.getRecipeFluid().addRecipe("fluid_mixer", new BaseFluidMachineRecipe(new InputFluid(
                new FluidStack(FluidName.fluidmethanol.getInstance(), 250), new FluidStack(
                FluidName.fluidsulfuricacid.getInstance(),
                250
        )), Arrays.asList(
                new FluidStack(FluidName.fluidmethylsulfate.getInstance(), 250),
                new FluidStack(
                        FluidRegistry.WATER,
                        1000
                )
        )));
        Recipes.recipes.getRecipeFluid().addRecipe("fluid_mixer", new BaseFluidMachineRecipe(new InputFluid(
                new FluidStack(FluidName.fluidbenzene.getInstance(), 100), new FluidStack(
                FluidName.fluidmethylbromide.getInstance(),
                100
        )), Arrays.asList(
                new FluidStack(FluidName.fluidhydrogenbromide.getInstance(), 100),
                new FluidStack(
                        FluidName.fluidtoluene.getInstance(),
                        100
                )
        )));

        Recipes.recipes.getRecipeFluid().addRecipe("fluid_mixer", new BaseFluidMachineRecipe(new InputFluid(
                new FluidStack(FluidName.fluidpolyprop.getInstance(), 400),
                new FluidStack(
                        FluidName.fluidnitrogenoxy.getInstance(),
                        600
                )
        ), Arrays.asList(
                new FluidStack(FluidName.fluidacrylonitrile.getInstance(), 400),
                new FluidStack(
                        FluidName.fluidazot.getInstance(),
                        100
                )
        )));

        Recipes.recipes.getRecipeFluid().addRecipe("fluid_mixer", new BaseFluidMachineRecipe(new InputFluid(
                new FluidStack(FluidName.fluidtertbutylsulfuricacid.getInstance(), 400),
                new FluidStack(
                        FluidRegistry.WATER,
                        400
                )
        ), Arrays.asList(
                new FluidStack(FluidName.fluidtertbutylalcohol.getInstance(), 400),
                new FluidStack(
                        FluidName.fluidsulfuricacid.getInstance(),
                        400
                )
        )));

        Recipes.recipes.getRecipeFluid().addRecipe("fluid_mixer", new BaseFluidMachineRecipe(new InputFluid(
                new FluidStack(FluidName.fluidethylhexanol.getInstance(), 400),
                new FluidStack(
                        FluidName.fluidnitricacid.getInstance(),
                        400
                )
        ), Arrays.asList(
                new FluidStack(FluidName.fluidethylhexylnitrate.getInstance(), 400),
                new FluidStack(
                        FluidRegistry.WATER,
                        400
                )
        )));

        Recipes.recipes.getRecipeFluid().addRecipe("fluid_mixer", new BaseFluidMachineRecipe(new InputFluid(
                new FluidStack(FluidName.fluidcyclohexane.getInstance(), 400),
                new FluidStack(
                        FluidName.fluidmethylbromide.getInstance(),
                        400
                )
        ), Arrays.asList(
                new FluidStack(FluidName.fluidmethylcyclohexane.getInstance(), 400),
                new FluidStack(
                        FluidName.fluidhydrogenbromide.getInstance(),
                        400
                )
        )));

        Recipes.recipes.getRecipeFluid().addRecipe("fluid_mixer", new BaseFluidMachineRecipe(new InputFluid(
                new FluidStack(FluidName.fluidsodiumhydroxide.getInstance(), 500),
                new FluidStack(
                        FluidName.fluidchlorum.getInstance(),
                        250
                )
        ), Arrays.asList(
                new FluidStack(FluidName.fluidsodium_hypochlorite.getInstance(), 500),
                new FluidStack(
                        FluidRegistry.WATER,
                        250
                )
        )));
        Recipes.recipes.getRecipeFluid().addRecipe("fluid_mixer", new BaseFluidMachineRecipe(new InputFluid(
                new FluidStack(FluidName.fluidnitrogenhydride.getInstance(), 500),
                new FluidStack(
                        FluidName.fluidsodium_hypochlorite.getInstance(),
                        250
                )
        ), Arrays.asList(
                new FluidStack(FluidName.fluidhydrazine.getInstance(), 250),
                new FluidStack(
                        FluidRegistry.WATER,
                        250
                )
        )));
        Recipes.recipes.getRecipeFluid().addRecipe("fluid_mixer", new BaseFluidMachineRecipe(new InputFluid(
                new FluidStack(FluidName.fluidmethanol.getInstance(), 250),
                new FluidStack(
                        FluidName.fluidhydrazine.getInstance(),
                        250
                )
        ), Arrays.asList(
                new FluidStack(FluidName.fluiddimethylhydrazine.getInstance(), 250),
                new FluidStack(
                        FluidRegistry.WATER,
                        200
                )
        )));
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
        if (this.fluidTank4.getFluidAmount() - 1000 >= 0 && this.fluidSlot4.transferFromTank(
                this.fluidTank4,
                output1,
                true
        ) && (output1.getValue() == null || this.outputSlot.canAdd(output1.getValue()))) {
            this.fluidSlot4.transferFromTank(this.fluidTank4, output1, false);
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
        return BlockBaseMachine3.fluid_mixer;
    }

    public BlockTileEntity getBlock() {
        return IUItem.basemachine2;
    }

    public ContainerFluidMixer getGuiContainer(EntityPlayer entityPlayer) {
        return new ContainerFluidMixer(entityPlayer, this);
    }

    @SideOnly(Side.CLIENT)
    public GuiFluidMixer getGui(EntityPlayer entityPlayer, boolean isAdmin) {
        return new GuiFluidMixer(getGuiContainer(entityPlayer));
    }

    public Set<UpgradableProperty> getUpgradableProperties() {
        return EnumSet.of(UpgradableProperty.Processing, UpgradableProperty.Transformer,
                UpgradableProperty.EnergyStorage, UpgradableProperty.FluidExtract,
                UpgradableProperty.FluidInput
        );
    }


}
