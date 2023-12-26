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
import com.denfop.container.ContainerBattery;
import com.denfop.gui.GuiBattery;
import com.denfop.invslot.InvSlotUpgrade;
import com.denfop.network.IUpdatableTileEvent;
import com.denfop.recipe.IInputHandler;
import com.denfop.tiles.base.TileElectricMachine;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import java.util.EnumSet;
import java.util.Set;

public class TileEntityBatteryFactory extends TileElectricMachine implements
        IUpgradableBlock, IUpdateTick, IUpdatableTileEvent, IHasRecipe {

    public final InvSlotUpgrade upgradeSlot;
    public final ComponentUpgradeSlots componentUpgrade;
    public final ComponentProgress componentProgress;
    public final ComponentProcess componentProcess;
    public final InvSlotRecipes inputSlotA;
    public final ComponentUpgrade componentUpgrades;
    public MachineRecipe output;

    public TileEntityBatteryFactory() {
        super(400, 1, 1);
        Recipes.recipes.addInitRecipes(this);
        this.upgradeSlot = new com.denfop.invslot.InvSlotUpgrade(this, 4);
        this.componentUpgrade = this.addComponent(new ComponentUpgradeSlots(this, upgradeSlot));
        this.componentProgress = this.addComponent(new ComponentProgress(this, 1,
                (short) 200
        ));
        this.inputSlotA = new InvSlotRecipes(this, "battery_factory", this);
        this.componentProcess = this.addComponent(new ComponentProcess(this, 200, 1));
        this.componentProcess.setHasAudio(false);
        this.componentProcess.setSlotOutput(outputSlot);
        this.componentProcess.setInvSlotRecipes(this.inputSlotA);
    
        this.componentUpgrades = this.addComponent(new ComponentUpgrade(this, TypeUpgrade.INSTANT, TypeUpgrade.STACK));
    }
    @Override
    public void init() {
        addRecipe(423,421,0);
        addRecipe(395,311,1);
        addRecipe(313,399,2);
        addRecipe(348,346,3);
        addRecipe(304,302,4);
        addRecipe(409,407,5);
        addRecipe(388,389,6);
        addRecipe(332,330,7);
        addRecipe(432,430,8);
        addRecipe(361,359,9);
        addRecipe(309,307,10);
        addRecipe(304,302,11);
        addRecipe(318,316,12);
        addRecipe(353,350,13);
    }

    @Override
    public ContainerBattery getGuiContainer(final EntityPlayer var1) {
        return new ContainerBattery(this,var1);
    }

    @Override
    @SideOnly(Side.CLIENT)
    public GuiScreen getGui(final EntityPlayer var1, final boolean var2) {
        return new GuiBattery(getGuiContainer(var1));
    }


    public static void addRecipe(int container, int fill1, int output) {
        final IInputHandler input = com.denfop.api.Recipes.inputFactory;
        Recipes.recipes.addRecipe(
                "battery_factory",
                new BaseMachineRecipe(
                        new Input(
                                input.getInput(new ItemStack(IUItem.crafting_elements,1,340)),
                                input.getInput(new ItemStack(IUItem.crafting_elements,1,433)),  input.getInput(new ItemStack(IUItem.crafting_elements,1,340))
                                , input.getInput(new ItemStack(IUItem.crafting_elements,1,340)), input.getInput(new ItemStack(IUItem.crafting_elements,1,container)), input.getInput(new ItemStack(IUItem.crafting_elements,1,340))
                                , input.getInput(new ItemStack(IUItem.crafting_elements,1,fill1)),
                                input.getInput(new ItemStack(IUItem.crafting_elements,1,355)),
                                input.getInput(new ItemStack(IUItem.crafting_elements,1,fill1))),
                        new RecipeOutput(null, new ItemStack(IUItem.solar_battery,1,output))
                )
        );
    }
    @Override
    public BlockTileEntity getBlock() {
        return IUItem.basemachine2;
    }

    @Override
    public IMultiTileBlock getTeBlock() {
        return BlockBaseMachine3.battery_factory;
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
