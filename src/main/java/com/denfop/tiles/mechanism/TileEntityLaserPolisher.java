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
import com.denfop.componets.ComponentProcess;
import com.denfop.componets.ComponentProgress;
import com.denfop.componets.ComponentUpgrade;
import com.denfop.componets.ComponentUpgradeSlots;
import com.denfop.componets.TypeUpgrade;
import com.denfop.container.ContainerLaserPolisher;
import com.denfop.gui.GuiLaserPolisher;
import com.denfop.invslot.InvSlotUpgrade;
import com.denfop.network.IUpdatableTileEvent;
import com.denfop.recipe.IInputHandler;
import com.denfop.tiles.base.TileElectricMachine;
import com.denfop.utils.ModUtils;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import java.util.EnumSet;
import java.util.Set;

public class TileEntityLaserPolisher extends TileElectricMachine implements
        IUpgradableBlock, IUpdateTick, IUpdatableTileEvent, IHasRecipe {

    public final InvSlotUpgrade upgradeSlot;
    public final ComponentUpgradeSlots componentUpgrade;
    public final ComponentProgress componentProgress;
    public final ComponentProcess componentProcess;
    public final InvSlotRecipes inputSlotA;
    public final ComponentUpgrade componentUpgrades;
    public MachineRecipe output;

    public TileEntityLaserPolisher() {
        super(200, 1, 1);
        Recipes.recipes.addInitRecipes(this);
        this.upgradeSlot = new com.denfop.invslot.InvSlotUpgrade(this, 4);
        this.componentUpgrade = this.addComponent(new ComponentUpgradeSlots(this, upgradeSlot));
        this.componentProgress = this.addComponent(new ComponentProgress(this, 1,
                (short) 100
        ));
        this.inputSlotA = new InvSlotRecipes(this, "laser", this);
        this.componentProcess = this.addComponent(new ComponentProcess(this, 200, 1));
        this.componentProcess.setHasAudio(false);
        this.componentProcess.setSlotOutput(outputSlot);
        this.componentProcess.setInvSlotRecipes(this.inputSlotA);

        this.componentUpgrades = this.addComponent(new ComponentUpgrade(this, TypeUpgrade.INSTANT, TypeUpgrade.STACK));
    }
    @Override
    public ContainerLaserPolisher getGuiContainer(final EntityPlayer var1) {
        return new ContainerLaserPolisher(var1, this);
    }

    @Override
    @SideOnly(Side.CLIENT)
    public GuiScreen getGui(final EntityPlayer var1, final boolean var2) {
        return new GuiLaserPolisher(getGuiContainer(var1));
    }
    @Override
    public void init() {
        addRecipe("oreDiamond",new ItemStack(Items.DIAMOND,4));
        addRecipe("oreCoal",new ItemStack(Items.COAL,5));
        addRecipe("oreQuartz",new ItemStack(Items.QUARTZ,4));
        addRecipe("oreLapis",new ItemStack(Items.DYE,5,4));
        addRecipe("oreRedstone",new ItemStack(Items.REDSTONE,5));
        addRecipe(319,new ItemStack(IUItem.crafting_elements,1,357));
        addRecipe(ModUtils.setSize(IUItem.UranFuel,8),new ItemStack(IUItem.crafting_elements,1,443));

        addRecipe(new ItemStack(IUItem.ore2),new ItemStack(IUItem.crafting_elements,1,447));
        addRecipe(new ItemStack(IUItem.ore2,1,2),new ItemStack(IUItem.crafting_elements,1,448));
        addRecipe(new ItemStack(IUItem.ore2,1,1),new ItemStack(IUItem.crafting_elements,1,449));
    }
    public static void addRecipe(int container, ItemStack output) {
        final IInputHandler input = com.denfop.api.Recipes.inputFactory;
        Recipes.recipes.addRecipe(
                "laser",
                new BaseMachineRecipe(
                        new Input(
                                input.getInput(new ItemStack(IUItem.crafting_elements,4,container))),
                        new RecipeOutput(null, output)
                )
        );
    }
    public static void addRecipe(ItemStack container, ItemStack output) {
        final IInputHandler input = com.denfop.api.Recipes.inputFactory;
        Recipes.recipes.addRecipe(
                "laser",
                new BaseMachineRecipe(
                        new Input(
                                input.getInput(container)),
                        new RecipeOutput(null, output)
                )
        );
    }
    public static void addRecipe(String container, ItemStack output) {
        final IInputHandler input = com.denfop.api.Recipes.inputFactory;
        Recipes.recipes.addRecipe(
                "laser",
                new BaseMachineRecipe(
                        new Input(
                                input.getInput(container)),
                        new RecipeOutput(null, output)
                )
        );
    }
    @Override
    public BlockTileEntity getBlock() {
        return IUItem.basemachine2;
    }

    @Override
    public IMultiTileBlock getTeBlock() {
        return BlockBaseMachine3.laser_polisher;
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
    public MachineRecipe getRecipeOutput() {
        return this.output;
    }
    public MachineRecipe getOutput() {
        this.output = this.inputSlotA.process();
        return this.output;
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
