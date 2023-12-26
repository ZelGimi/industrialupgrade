package com.denfop.tiles.mechanism;

import com.denfop.IUCore;
import com.denfop.IUItem;
import com.denfop.api.Recipes;
import com.denfop.api.energy.EnergyNetGlobal;
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
import com.denfop.blocks.BlockTileEntity;
import com.denfop.blocks.mechanism.BlockBaseMachine3;
import com.denfop.componets.ComponentBaseEnergy;
import com.denfop.componets.ComponentProcess;
import com.denfop.componets.ComponentProgress;
import com.denfop.componets.ComponentUpgradeSlots;
import com.denfop.container.ContainerBase;
import com.denfop.container.ContainerNuclearWasteRecycler;
import com.denfop.gui.GuiNuclearWasteRecycler;
import com.denfop.invslot.InvSlotUpgrade;
import com.denfop.network.IUpdatableTileEvent;
import com.denfop.recipe.IInputHandler;
import com.denfop.tiles.base.TileElectricMachine;
import com.denfop.utils.ModUtils;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumFacing;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.EnumSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

public class TileEntityNuclearWasteRecycler extends TileElectricMachine implements IUpgradableBlock, IUpdateTick,
        IUpdatableTileEvent, IHasRecipe {

    public final InvSlotUpgrade upgradeSlot;
    public final ComponentUpgradeSlots componentUpgrade;
    public final ComponentProgress componentProgress;
    public final InvSlotRecipes inputSlotA;
    public final ComponentProcess componentProcess;
    public final ComponentBaseEnergy rad;
    public MachineRecipe output;


    public TileEntityNuclearWasteRecycler() {
        super(500, 8, 1);
        this.upgradeSlot = new com.denfop.invslot.InvSlotUpgrade(this, 4);
        this.componentUpgrade = this.addComponent(new ComponentUpgradeSlots(this, upgradeSlot){
            @Override
            public void onLoaded() {
                super.onLoaded();
                this.componentProcess = ((TileEntityNuclearWasteRecycler)this.getParent()).componentProcess;
            }
        });
        this.componentProgress = this.addComponent(new ComponentProgress(this, 1,
                (short) 100
        ));
        this.inputSlotA = new InvSlotRecipes(this, "waste_recycler", this);
        this.componentProcess = this.addComponent(new ComponentProcess(this, 100, 1) {
                                                      @Override
                                                      public void operateOnce(final List<ItemStack> processResult) {
                                                          super.operateOnce(processResult);
                                                          ((TileEntityNuclearWasteRecycler)this.getParent()).rad.addEnergy(100);
                                                      }
                                                  }
        );
        this.componentProcess.setHasAudio(false);
        this.componentProcess.setSlotOutput(outputSlot);
        this.componentProcess.setInvSlotRecipes(this.inputSlotA);
        this.rad = this.addComponent((new ComponentBaseEnergy(
                EnergyType.RADIATION, this, 10000,

                new ArrayList<>(ModUtils.noFacings),
                Arrays.asList(EnumFacing.values()),
                0,
                EnergyNetGlobal.instance.getTierFromPower(14), false
        )));
        Recipes.recipes.addInitRecipes(this);
    }

    @Override
    public BlockTileEntity getBlock() {
        return IUItem.basemachine2;
    }

    @Override
    public IMultiTileBlock getTeBlock() {
        return BlockBaseMachine3.nuclear_waste_recycler;
    }

    @Override
    public ContainerNuclearWasteRecycler getGuiContainer(final EntityPlayer var1) {
        return new ContainerNuclearWasteRecycler(this,var1);
    }

    @Override
    @SideOnly(Side.CLIENT)
    public GuiScreen getGui(final EntityPlayer var1, final boolean var2) {
        return new GuiNuclearWasteRecycler(getGuiContainer(var1));
    }

    @Override
    public void init() {
        final IInputHandler input = com.denfop.api.Recipes.inputFactory;

        Recipes.recipes.addRecipe(
                "waste_recycler",
                new BaseMachineRecipe(
                        new Input(
                                input.getInput(new ItemStack(IUItem.crafting_elements,1,443))),
                        new RecipeOutput(null, ItemStack.EMPTY)
                )
        );
    }

    @Override
    public void onLoaded() {
        super.onLoaded();
        if (IUCore.proxy.isSimulating()) {
            inputSlotA.load();
            this.getOutput();


        }
    }

    @Override
    public void onUpdate() {

    }

    @Override
    public MachineRecipe getRecipeOutput() {
        if(this.rad.getEnergy() + 100 <= 10000) {
            return this.output;
        }else{
            return null;
        }
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
