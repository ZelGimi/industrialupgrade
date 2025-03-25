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
import com.denfop.componets.ComponentBioFuelEnergy;
import com.denfop.componets.ComponentSteamEnergy;
import com.denfop.componets.Energy;
import com.denfop.componets.Fluids;
import com.denfop.componets.PressureComponent;
import com.denfop.container.ContainerBioGenerator;
import com.denfop.container.ContainerElectricBioGenerator;
import com.denfop.gui.GuiElectricBioGenerator;
import com.denfop.gui.GuiSteamBioGenerator;
import com.denfop.invslot.InvSlot;
import com.denfop.network.DecoderHandler;
import com.denfop.network.EncoderHandler;
import com.denfop.network.packet.CustomPacketBuffer;
import com.denfop.recipe.IInputHandler;
import com.denfop.tiles.base.TileElectricMachine;
import com.denfop.utils.ModUtils;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.SoundEvent;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.fluids.capability.CapabilityFluidHandler;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import org.lwjgl.input.Keyboard;

import java.io.IOException;
import java.util.Collections;
import java.util.EnumSet;
import java.util.List;
import java.util.Set;

public class TileEntityBioGenerator extends TileElectricMachine implements  IHasRecipe, IUpdateTick {

    public final FluidHandlerRecipe fluid_handler;
    public final Fluids.InternalFluidTank fluidTank1;
    public final double defaultEnergyConsume;
    public final int defaultOperationLength;
    public final int defaultTier;
    public final double defaultEnergyStorage;
    public final Energy steam;
    public final InvSlotRecipes inputSlotA;
    private final Fluids fluids;
    public final ComponentBioFuelEnergy bioEnergy;
    public double energyConsume;
    public int operationLength;
    public int operationsPerTick;
    protected short progress;
    protected double guiProgress;
    private MachineRecipe output;

    public TileEntityBioGenerator() {
        super(0, 1, 0);
        this.progress = 0;
        this.inputSlotA = new InvSlotRecipes(this, "biomass", this);
        this.defaultEnergyConsume = this.energyConsume = 1;
        this.defaultOperationLength = this.operationLength = 20;
        operationsPerTick = 1;
        this.defaultTier = 1;
        this.defaultEnergyStorage = 20;
        this.fluids = this.addComponent(new Fluids(this));
        this.fluidTank1 = fluids.addTank("fluidTank1", 4000, InvSlot.TypeItemSlot.NONE,
                Fluids.fluidPredicate(FluidName.fluidbiomass.getInstance()));
        this.fluid_handler = new FluidHandlerRecipe("biomass", fluids);

        this.steam = this.addComponent(Energy.asBasicSink(this, 100));
        this.bioEnergy = this.addComponent(ComponentBioFuelEnergy.asBasicSource(this, 4000));
        this.bioEnergy.setFluidTank(fluidTank1);
        this.fluidTank1.setAcceptedFluids(Fluids.fluidPredicate(this.fluid_handler.getOutputFluids(0)));
        Recipes.recipes.getRecipeFluid().addInitRecipes(this);

    }
    public static void addRecipe(ItemStack container, FluidStack fluidStack) {
        final IInputHandler input = com.denfop.api.Recipes.inputFactory;
        Recipes.recipes.addRecipe(
                "biomass",
                new BaseMachineRecipe(
                        new Input(
                                input.getInput(container)),
                        new RecipeOutput(null, container)
                )
        );
        Recipes.recipes.getRecipeFluid().addRecipe("biomass", new BaseFluidMachineRecipe(new InputFluid(
                container), Collections.singletonList(
                fluidStack)));

    }



    @Override
    public void init() {
        addRecipe(
                IUItem.biochaff,
                new FluidStack(FluidName.fluidbiomass.getInstance(), 800)
        );
        addRecipe(
                IUItem.plantBall,
                new FluidStack(FluidName.fluidbiomass.getInstance(), 400)
        );

    }
    public static int applyModifier(int base, int extra, double multiplier) {
        double ret = Math.round((base + extra) * multiplier);
        return (ret > 2.147483647E9D) ? Integer.MAX_VALUE : (int) ret;
    }



    @Override
    public SoundEvent getSound() {
        return EnumSound.steam.getSoundEvent();
    }

    public void readFromNBT(NBTTagCompound nbttagcompound) {
        super.readFromNBT(nbttagcompound);
        this.progress = nbttagcompound.getShort("progress");

    }

    @SideOnly(Side.CLIENT)
    public void addInformation(ItemStack stack, List<String> tooltip) {
        if (!Keyboard.isKeyDown(Keyboard.KEY_LSHIFT)) {
            tooltip.add(Localization.translate("press.lshift"));
        }
        if (Keyboard.isKeyDown(Keyboard.KEY_LSHIFT)) {
            tooltip.add(Localization.translate("iu.machines_work_length") + this.defaultOperationLength);
        }
        tooltip.add(Localization.translate("iu.biomachine_generator"));
        super.addInformation(stack, tooltip);

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

    public double getProgress() {
        return this.guiProgress;
    }

    public void onLoaded() {
        super.onLoaded();
        if (IUCore.proxy.isSimulating()) {
            inputSlotA.load();
            this.fluid_handler.load(this.inputSlotA.get());
            this.getOutput();
        }
    }

    public void onUnloaded() {
        super.onUnloaded();

    }

    @Override
    public void onUpdate() {

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

        if ((this.fluid_handler.output() == null && !this.inputSlotA.isEmpty())) {
            this.fluid_handler.getOutput(this.inputSlotA.get());
        } else {
            if (this.fluid_handler.output() != null && this.inputSlotA.isEmpty()) {
                this.fluid_handler.setOutput(null);
            }
        }


        if (this.output != null && this.fluid_handler.output() != null &&  this.fluid_handler.canFillFluid() && this.steam.canUseEnergy(
                energyConsume)) {

            if (!this.getActive()) {
                setActive(true);
            }
            if (this.progress == 0) {
                initiate(0);
            }
            this.progress = (short) (this.progress + 1);
            this.steam.useEnergy(energyConsume);
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


    }

    public void setOverclockRates() {
        if (this.operationLength < 1) {
            this.operationLength = 1;
        }
    }

    public void operate() {
        for (int i = 0; i < this.operationsPerTick; i++) {
            operateOnce();

            this.getOutput();
            if (this.fluid_handler.output() == null) {
                break;
            }
        }
    }

    public void operateOnce() {
        this.bioEnergy.addEnergy(this.fluid_handler.output().output_fluid.get(0).amount);
        this.inputSlotA.consume();
    }


    public IMultiTileBlock getTeBlock() {
        return BlockBaseMachine3.bio_generator;
    }

    public BlockTileEntity getBlock() {
        return IUItem.basemachine2;
    }

    public ContainerElectricBioGenerator getGuiContainer(EntityPlayer entityPlayer) {
        return new ContainerElectricBioGenerator(entityPlayer, this);
    }

    @SideOnly(Side.CLIENT)
    public GuiElectricBioGenerator getGui(EntityPlayer entityPlayer, boolean isAdmin) {
        return new GuiElectricBioGenerator(getGuiContainer(entityPlayer));
    }

    public Set<UpgradableProperty> getUpgradableProperties() {
        return EnumSet.of(UpgradableProperty.Processing, UpgradableProperty.Transformer,
                UpgradableProperty.EnergyStorage,
                UpgradableProperty.FluidInput, UpgradableProperty.ItemExtract
        );
    }

    @Override
    public boolean onActivated(
            final EntityPlayer player,
            final EnumHand hand,
            final EnumFacing side,
            final float hitX,
            final float hitY,
            final float hitZ
    ) {
        if (!this.getWorld().isRemote && player
                .getHeldItem(hand)
                .hasCapability(CapabilityFluidHandler.FLUID_HANDLER_ITEM_CAPABILITY, null)) {

            return ModUtils.interactWithFluidHandler(player, hand,
                    fluids.getCapability(CapabilityFluidHandler.FLUID_HANDLER_CAPABILITY, side)
            );
        } else {

            return super.onActivated(player, hand, side, hitX, hitY, hitZ);
        }
    }


}
