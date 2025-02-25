package com.denfop.tiles.mechanism.steam;

import com.denfop.IUCore;
import com.denfop.IUItem;
import com.denfop.Localization;
import com.denfop.api.Recipes;
import com.denfop.api.recipe.BaseMachineRecipe;
import com.denfop.api.recipe.IHasRecipe;
import com.denfop.api.recipe.IUpdateTick;
import com.denfop.api.recipe.Input;
import com.denfop.api.recipe.InvSlotRecipes;
import com.denfop.api.recipe.MachineRecipe;
import com.denfop.api.recipe.RecipeOutput;
import com.denfop.api.sytem.EnergyType;
import com.denfop.api.tile.IMultiTileBlock;
import com.denfop.api.upgrades.IUpgradableBlock;
import com.denfop.api.upgrades.UpgradableProperty;
import com.denfop.audio.EnumSound;
import com.denfop.blocks.BlockTileEntity;
import com.denfop.blocks.FluidName;
import com.denfop.blocks.mechanism.BlockBaseMachine3;
import com.denfop.componets.ComponentBaseEnergy;
import com.denfop.componets.ComponentSteamEnergy;
import com.denfop.componets.Fluids;
import com.denfop.container.ContainerSteamCharger;
import com.denfop.gui.GuiSteamCharger;
import com.denfop.invslot.InvSlot;
import com.denfop.network.DecoderHandler;
import com.denfop.network.EncoderHandler;
import com.denfop.network.IUpdatableTileEvent;
import com.denfop.network.packet.CustomPacketBuffer;
import com.denfop.recipe.IInputHandler;
import com.denfop.tiles.base.TileElectricMachine;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.SoundEvent;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import org.lwjgl.input.Keyboard;

import java.io.IOException;
import java.util.EnumSet;
import java.util.List;
import java.util.Set;

public class TileSteamCrystalCharge extends TileElectricMachine implements
        IUpdateTick, IUpdatableTileEvent, IHasRecipe {


    public final InvSlotRecipes inputSlotA;
    public final ComponentSteamEnergy steam;
    public final double defaultEnergyConsume;
    public final int defaultOperationLength;
    public final int defaultTier;
    public final double defaultEnergyStorage;
    private final Fluids fluids;
    private final Fluids.InternalFluidTank fluidTank;
    public final ComponentBaseEnergy ampere;
    public MachineRecipe output;
    public double energyConsume;
    public int operationLength;
    public int operationsPerTick;
    public double guiProgress;
    protected short progress;

    public TileSteamCrystalCharge() {
        super(0, 1, 1);
        Recipes.recipes.addInitRecipes(this);

        this.progress = 0;
        this.defaultEnergyConsume = this.energyConsume = 1;
        this.defaultOperationLength = this.operationLength = 200;
        this.defaultTier = 1;
        this.defaultEnergyStorage = 100;
        fluids = this.addComponent(new Fluids(this));
        this.inputSlotA = new InvSlotRecipes(this, "charger", this);
        this.ampere = this.addComponent(ComponentBaseEnergy.asBasicSink(EnergyType.AMPERE, this, 4000));

     
        this.fluidTank = fluids.addTank("fluidTank6", 4000, InvSlot.TypeItemSlot.NONE, Fluids.fluidPredicate(
                FluidName.fluidsteam.getInstance()
        ));
        this.steam = this.addComponent(ComponentSteamEnergy.asBasicSink(this, 4000));
        this.steam.setFluidTank(fluidTank);

    }

    public static void addRecipe(ItemStack container, ItemStack output) {
        final IInputHandler input = com.denfop.api.Recipes.inputFactory;
        Recipes.recipes.addRecipe(
                "charger",
                new BaseMachineRecipe(
                        new Input(input.getInput(container)),
                        new RecipeOutput(null, output)
                )
        );


    }

    @Override
    public SoundEvent getSound() {
        return EnumSound.steam.getSoundEvent();
    }

    @SideOnly(Side.CLIENT)
    public void addInformation(ItemStack stack, List<String> tooltip) {
        if (!Keyboard.isKeyDown(Keyboard.KEY_LSHIFT)) {
            tooltip.add(Localization.translate("press.lshift"));
        }
        if (Keyboard.isKeyDown(Keyboard.KEY_LSHIFT)) {

        }
        super.addInformation(stack, tooltip);

    }

    public ContainerSteamCharger getGuiContainer(final EntityPlayer var1) {
        return new ContainerSteamCharger(var1, this);

    }

    @Override
    @SideOnly(Side.CLIENT)
    public GuiScreen getGui(final EntityPlayer var1, final boolean var2) {

        return new GuiSteamCharger(getGuiContainer(var1));
    }

    @Override
    public void init() {
        addRecipe(new ItemStack(Items.REDSTONE, 1), new ItemStack(IUItem.charged_redstone, 1));
        addRecipe(new ItemStack(Items.QUARTZ, 1), new ItemStack(IUItem.charged_quartz, 1));

    }

    @Override
    public BlockTileEntity getBlock() {
        return IUItem.basemachine2;
    }

    @Override
    public IMultiTileBlock getTeBlock() {
        return BlockBaseMachine3.steam_crystal_charge;
    }

    @Override
    public void onUpdate() {

    }

    public void onLoaded() {
        super.onLoaded();
        if (IUCore.proxy.isSimulating()) {
            inputSlotA.load();
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
    }

    public MachineRecipe getOutput() {
        this.output = this.inputSlotA.process();
        return this.output;
    }

    public void updateEntityServer() {
        super.updateEntityServer();


        if (this.output != null && !this.inputSlotA.isEmpty() && this.outputSlot.canAdd(this.output
                .getRecipe()
                .getOutput().items)&&ampere.canUseEnergy(20) && this.inputSlotA.continue_process(this.output) && this.steam.canUseEnergy(energyConsume) ) {
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
                ampere.useEnergy(20);
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
    }



    public double getProgress() {
        return this.guiProgress;
    }

}
