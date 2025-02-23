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
import com.denfop.api.tile.IMultiTileBlock;
import com.denfop.api.upgrades.IUpgradableBlock;
import com.denfop.api.upgrades.UpgradableProperty;
import com.denfop.audio.EnumSound;
import com.denfop.blocks.BlockTileEntity;
import com.denfop.blocks.FluidName;
import com.denfop.blocks.mechanism.BlockBaseMachine3;
import com.denfop.componets.ComponentSteamEnergy;
import com.denfop.componets.Fluids;
import com.denfop.componets.PressureComponent;
import com.denfop.container.ContainerSteamSharpener;
import com.denfop.gui.GuiSteamSharpener;
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

public class TileSteamSharpener extends TileElectricMachine implements
         IUpdateTick, IUpdatableTileEvent, IHasRecipe {


    public final InvSlotRecipes inputSlotA;
    public final PressureComponent pressure;
    public final ComponentSteamEnergy steam;
    public final double defaultEnergyConsume;
    public final int defaultOperationLength;
    public final int defaultTier;
    public final double defaultEnergyStorage;
    private final Fluids fluids;
    private final Fluids.InternalFluidTank fluidTank;
    public MachineRecipe output;
    public double energyConsume;
    public int operationLength;
    public int operationsPerTick;
    public double guiProgress;
    protected short progress;

    public TileSteamSharpener() {
        super(0, 1, 1);
        Recipes.recipes.addInitRecipes(this);

        this.progress = 0;
        this.defaultEnergyConsume = this.energyConsume = 1;
        this.defaultOperationLength = this.operationLength = 200;
        this.defaultTier = 1;
        this.defaultEnergyStorage = 100;
        fluids = this.addComponent(new Fluids(this));
        this.inputSlotA = new InvSlotRecipes(this, "sharpener", this);


        this.pressure = this.addComponent(PressureComponent.asBasicSink(this, 1));
        this.fluidTank = fluids.addTank("fluidTank6", 4000, InvSlot.TypeItemSlot.NONE, Fluids.fluidPredicate(
                FluidName.fluidsteam.getInstance()
        ));
        this.steam = this.addComponent(ComponentSteamEnergy.asBasicSink(this, 4000));
        this.steam.setFluidTank(fluidTank);

    }

    public static void addRecipe(ItemStack container, ItemStack output) {
        final IInputHandler input = com.denfop.api.Recipes.inputFactory;
        Recipes.recipes.addRecipe(
                "sharpener",
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

    public ContainerSteamSharpener getGuiContainer(final EntityPlayer var1) {
        return new ContainerSteamSharpener(var1, this);

    }

    @Override
    @SideOnly(Side.CLIENT)
    public GuiScreen getGui(final EntityPlayer var1, final boolean var2) {

        return new GuiSteamSharpener(getGuiContainer(var1));
    }

    @Override
    public void init() {
        addRecipe(new ItemStack(IUItem.crafting_elements, 1, 502), new ItemStack(IUItem.iuingot, 1, 23));
        addRecipe(new ItemStack(IUItem.crafting_elements, 1, 503), new ItemStack(IUItem.crafting_elements, 1, 504));
        addRecipe(IUItem.sulfurDust, new ItemStack(IUItem.crafting_elements, 1, 476));
        final IInputHandler input = com.denfop.api.Recipes.inputFactory;
        Recipes.recipes.addRecipe("sharpener", new BaseMachineRecipe(
                new Input(input.getInput(new ItemStack(IUItem.rawIngot, 1, 0))),
                new RecipeOutput(null, new ItemStack(IUItem.iuingot, 1, 0))
        ));
        Recipes.recipes.addRecipe("sharpener", new BaseMachineRecipe(
                new Input(input.getInput(new ItemStack(IUItem.rawIngot, 1, 1))),
                new RecipeOutput(null, new ItemStack(IUItem.iuingot, 1, 1))
        ));
        Recipes.recipes.addRecipe("sharpener", new BaseMachineRecipe(
                new Input(input.getInput(new ItemStack(IUItem.rawIngot, 1, 2))),
                new RecipeOutput(null, new ItemStack(IUItem.iuingot, 1, 2))
        ));
        Recipes.recipes.addRecipe("sharpener", new BaseMachineRecipe(
                new Input(input.getInput(new ItemStack(IUItem.rawIngot, 1, 3))),
                new RecipeOutput(null, new ItemStack(IUItem.iuingot, 1, 3))
        ));
        Recipes.recipes.addRecipe("sharpener", new BaseMachineRecipe(
                new Input(input.getInput(new ItemStack(IUItem.rawIngot, 1, 4))),
                new RecipeOutput(null, new ItemStack(IUItem.iuingot, 1, 6))
        ));
        Recipes.recipes.addRecipe("sharpener", new BaseMachineRecipe(
                new Input(input.getInput(new ItemStack(IUItem.rawIngot, 1, 5))),
                new RecipeOutput(null, new ItemStack(IUItem.iuingot, 1, 7))
        ));
        Recipes.recipes.addRecipe("sharpener", new BaseMachineRecipe(
                new Input(input.getInput(new ItemStack(IUItem.rawIngot, 1, 6))),
                new RecipeOutput(null, new ItemStack(IUItem.iuingot, 1, 8))
        ));

        Recipes.recipes.addRecipe("sharpener", new BaseMachineRecipe(
                new Input(input.getInput(new ItemStack(IUItem.rawIngot, 1, 7))),
                new RecipeOutput(null, new ItemStack(IUItem.iuingot, 1, 9))
        ));
        Recipes.recipes.addRecipe("sharpener", new BaseMachineRecipe(
                new Input(input.getInput(new ItemStack(IUItem.rawIngot, 1, 8))),
                new RecipeOutput(null, new ItemStack(IUItem.iuingot, 1, 10))
        ));
        Recipes.recipes.addRecipe("sharpener", new BaseMachineRecipe(
                new Input(input.getInput(new ItemStack(IUItem.rawIngot, 1, 9))),
                new RecipeOutput(null, new ItemStack(IUItem.iuingot, 1, 11))
        ));
        Recipes.recipes.addRecipe("sharpener", new BaseMachineRecipe(
                new Input(input.getInput(new ItemStack(IUItem.rawIngot, 1, 10))),
                new RecipeOutput(null, new ItemStack(IUItem.iuingot, 1, 12))
        ));
        Recipes.recipes.addRecipe("sharpener", new BaseMachineRecipe(
                new Input(input.getInput(new ItemStack(IUItem.rawIngot, 1, 10))),
                new RecipeOutput(null, new ItemStack(IUItem.iuingot, 1, 12))
        ));
        Recipes.recipes.addRecipe("sharpener", new BaseMachineRecipe(
                new Input(input.getInput(new ItemStack(IUItem.rawIngot, 1, 11))),
                new RecipeOutput(null, new ItemStack(IUItem.iuingot, 1, 14))
        ));
        Recipes.recipes.addRecipe("sharpener", new BaseMachineRecipe(
                new Input(input.getInput(new ItemStack(IUItem.rawIngot, 1, 12))),
                new RecipeOutput(null, new ItemStack(IUItem.iuingot, 1, 15))
        ));
        Recipes.recipes.addRecipe("sharpener", new BaseMachineRecipe(
                new Input(input.getInput(new ItemStack(IUItem.rawIngot, 1, 13))),
                new RecipeOutput(null, new ItemStack(IUItem.iuingot, 1, 16))
        ));
        Recipes.recipes.addRecipe("sharpener", new BaseMachineRecipe(
                new Input(input.getInput(new ItemStack(IUItem.rawIngot, 1, 14))),
                new RecipeOutput(null, new ItemStack(IUItem.iuingot, 1, 17))
        ));
        Recipes.recipes.addRecipe("sharpener", new BaseMachineRecipe(
                new Input(input.getInput(new ItemStack(IUItem.rawIngot, 1, 15))),
                new RecipeOutput(null, new ItemStack(IUItem.iuingot, 1, 18))
        ));
        Recipes.recipes.addRecipe("sharpener", new BaseMachineRecipe(
                new Input(input.getInput(new ItemStack(IUItem.rawIngot, 1, 16))),
                new RecipeOutput(null, new ItemStack(IUItem.iuingot, 1, 21))
        ));
        Recipes.recipes.addRecipe("sharpener", new BaseMachineRecipe(
                new Input(input.getInput(new ItemStack(IUItem.rawIngot, 1, 17))),
                new RecipeOutput(null, new ItemStack(Items.GOLD_INGOT, 1))
        ));
        Recipes.recipes.addRecipe("sharpener", new BaseMachineRecipe(
                new Input(input.getInput(new ItemStack(IUItem.rawIngot, 1, 18))),
                new RecipeOutput(null, new ItemStack(Items.IRON_INGOT, 1))
        ));
        Recipes.recipes.addRecipe("sharpener", new BaseMachineRecipe(
                new Input(input.getInput(new ItemStack(IUItem.rawIngot, 1, 19))),
                new RecipeOutput(null, new ItemStack(IUItem.iuingot, 1, 22))
        ));
        Recipes.recipes.addRecipe("sharpener", new BaseMachineRecipe(
                new Input(input.getInput(new ItemStack(IUItem.rawIngot, 1, 20))),
                new RecipeOutput(null, new ItemStack(IUItem.iuingot, 1, 24))
        ));
        Recipes.recipes.addRecipe("sharpener", new BaseMachineRecipe(
                new Input(input.getInput(new ItemStack(IUItem.rawIngot, 1, 22))),
                new RecipeOutput(null, new ItemStack(IUItem.iuingot, 1, 25))
        ));
        Recipes.recipes.addRecipe("sharpener", new BaseMachineRecipe(
                new Input(input.getInput(new ItemStack(IUItem.rawIngot, 1, 23))),
                new RecipeOutput(null, new ItemStack(IUItem.iuingot, 1, 26))
        ));
        Recipes.recipes.addRecipe("sharpener", new BaseMachineRecipe(
                new Input(input.getInput(new ItemStack(IUItem.rawIngot, 1, 24))),
                new RecipeOutput(null, new ItemStack(IUItem.iuingot, 1, 27))
        ));
        for (int i = 25; i < 40; i++) {
            Recipes.recipes.addRecipe("sharpener", new BaseMachineRecipe(
                    new Input(input.getInput(new ItemStack(IUItem.rawIngot, 1, i))),
                    new RecipeOutput(null, new ItemStack(IUItem.iuingot, 1, i+3))
            ));
        }
    }

    @Override
    public BlockTileEntity getBlock() {
        return IUItem.basemachine2;
    }

    @Override
    public IMultiTileBlock getTeBlock() {
        return BlockBaseMachine3.steam_sharpener;
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
                .getOutput().items) && this.inputSlotA.continue_process(this.output) && this.steam.canUseEnergy(energyConsume) && this.pressure.getEnergy() == 1) {
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
