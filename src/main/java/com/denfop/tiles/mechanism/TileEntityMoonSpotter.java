package com.denfop.tiles.mechanism;

import com.denfop.IUCore;
import com.denfop.IUItem;
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
import com.denfop.blocks.BlockTileEntity;
import com.denfop.blocks.mechanism.BlockBaseMachine3;
import com.denfop.componets.ComponentTimer;
import com.denfop.container.ContainerMoonSpotter;
import com.denfop.gui.GuiMoonSpotter;
import com.denfop.network.IUpdatableTileEvent;
import com.denfop.recipe.IInputHandler;
import com.denfop.tiles.base.TileElectricMachine;
import com.denfop.utils.Timer;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import java.util.EnumSet;
import java.util.Set;

public class TileEntityMoonSpotter extends TileElectricMachine implements
        IUpgradableBlock, IUpdateTick, IUpdatableTileEvent, IHasRecipe {

    public final ComponentTimer timer;
    public final InvSlotRecipes inputSlotA;
    public MachineRecipe output;

    public TileEntityMoonSpotter() {
        super(0, 14, 1);
        Recipes.recipes.addInitRecipes(this);
        inputSlotA = new InvSlotRecipes(this, "solar_glass_recipe", this);
        inputSlotA.setStackSizeLimit(1);
        this.timer = this.addComponent(new ComponentTimer(this, new Timer(0, 5, 0)));
    }

    @Override
    public void init() {
        for (int i = 0; i < 14; i++) {
            addRecipe(i);
        }
    }

    public static void addRecipe(int container) {
        final IInputHandler input = com.denfop.api.Recipes.inputFactory;
        Recipes.recipes.addRecipe(
                "solar_glass_recipe",
                new BaseMachineRecipe(
                        new Input(
                                input.getInput(new ItemStack(IUItem.solar_day_glass, 1, container))),
                        new RecipeOutput(null, new ItemStack(IUItem.solar_night_glass, 1, container))
                )
        );
    }


    @Override
    public BlockTileEntity getBlock() {
        return IUItem.basemachine2;
    }

    @Override
    public IMultiTileBlock getTeBlock() {
        return BlockBaseMachine3.moon_spotter;
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

    @Override
    public void updateEntityServer() {
        super.updateEntityServer();
        if (this.getWorld().provider.isDaytime()) {
            this.timer.setCanWorkWithOut(false);
            this.setActive(false);
            return;
        }
        if (this.inputSlotA.get().isEmpty() || this.output == null || this.outputSlot.get().getCount() >= 64) {
            this.timer.setCanWork(false);
            this.setActive(false);
            return;
        }
        this.setActive(true);
        if (!this.timer.isCanWork()) {
            this.timer.setCanWork(true);
        }
        if (this.timer.getTimers().get(0).getTime() <= 0) {
            this.inputSlotA.consume();
            this.outputSlot.add(this.output.getRecipe().output.items.get(0));
            getOutput();
        }
    }

    @Override
    public MachineRecipe getRecipeOutput() {
        return this.output;
    }

    public MachineRecipe getOutput() {
        this.output = this.inputSlotA.process();
        this.timer.resetTime();
        return this.output;
    }
    @Override
    public ContainerMoonSpotter getGuiContainer(final EntityPlayer var1) {
        return new ContainerMoonSpotter(this,var1);
    }

    @Override
    @SideOnly(Side.CLIENT)
    public GuiScreen getGui(final EntityPlayer var1, final boolean var2) {
        return new GuiMoonSpotter(getGuiContainer(var1));
    }

    @Override
    public void setRecipeOutput(final MachineRecipe output) {
        this.output = output;
    }

    @Override
    public Set<UpgradableProperty> getUpgradableProperties() {
        return EnumSet.of(
                UpgradableProperty.Processing,
                UpgradableProperty.Transformer,
                UpgradableProperty.EnergyStorage,
                UpgradableProperty.ItemConsuming,
                UpgradableProperty.ItemProducing
        );
    }


}
