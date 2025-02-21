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
import com.denfop.container.ContainerPositronsConverter;
import com.denfop.gui.GuiPositronsConverter;
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

public class TileEntityPositronConverter extends TileElectricMachine implements IUpgradableBlock, IUpdateTick,
        IUpdatableTileEvent, IHasRecipe {

    public final InvSlotUpgrade upgradeSlot;
    public final ComponentUpgradeSlots componentUpgrade;
    public final ComponentProgress componentProgress;
    public final InvSlotRecipes inputSlotA;
    public final ComponentProcess componentProcess;
    public final ComponentBaseEnergy positrons;
    public final ComponentBaseEnergy qe;
    public MachineRecipe output;


    public TileEntityPositronConverter() {
        super(500, 8, 1);
        this.upgradeSlot = new com.denfop.invslot.InvSlotUpgrade(this, 4);
        this.componentUpgrade = this.addComponent(new ComponentUpgradeSlots(this, upgradeSlot) {
            @Override
            public void onLoaded() {
                super.onLoaded();
                this.componentProcess = ((TileEntityPositronConverter) this.getParent()).componentProcess;
            }
        });
        this.componentProgress = this.addComponent(new ComponentProgress(this, 1,
                (short) 100
        ));
        this.inputSlotA = new InvSlotRecipes(this, "positrons", this);
        this.componentProcess = this.addComponent(new ComponentProcess(this, 100, 1) {
                                                      @Override
                                                      public boolean checkRadiation(final boolean consume) {
                                                          if (this.updateTick.getRecipeOutput() == null) {
                                                              return false;
                                                          } else {
                                                              final int amount = 5;
                                                              if (consume) {
                                                                  qe.useEnergy(amount);
                                                              }
                                                              return qe.getEnergy() >= amount;
                                                          }
                                                      }

                                                      @Override
                                                      public void consumeEnergy() {
                                                          super.consumeEnergy();
                                                          qe.useEnergy(5);
                                                      }

                                                      @Override
                                                      protected int getRadiationSize(final int size) {
                                                          final int amount = 5;

                                                          return (int) Math.min(size, qe.getEnergy() / amount);
                                                      }

                                                      @Override
                                                      public void operateWithMax(final MachineRecipe output) {
                                                          super.operateWithMax(output);
                                                          ((TileEntityPositronConverter) this.getParent()).positrons.addEnergy(2000);

                                                      }

                                                      @Override
                                                      public void operateWithMax(final MachineRecipe output, final int size) {
                                                          super.operateWithMax(output, size);
                                                          ((TileEntityPositronConverter) this.getParent()).positrons.addEnergy(2000);

                                                      }
                                                  }
        );
        this.componentProcess.setHasAudio(false);
        this.componentProcess.setSlotOutput(outputSlot);
        this.componentProcess.setInvSlotRecipes(this.inputSlotA);
        this.positrons = this.addComponent(ComponentBaseEnergy.asBasicSource(
                EnergyType.POSITRONS, this, 100000));
        this.qe = this.addComponent(ComponentBaseEnergy.asBasicSink(
                EnergyType.QUANTUM, this, 1000));
        Recipes.recipes.addInitRecipes(this);
    }

    @Override
    public BlockTileEntity getBlock() {
        return IUItem.basemachine2;
    }

    @Override
    public IMultiTileBlock getTeBlock() {
        return BlockBaseMachine3.positronconverter;
    }

    @Override
    public ContainerPositronsConverter getGuiContainer(final EntityPlayer var1) {
        return new ContainerPositronsConverter(this, var1);
    }

    @Override
    @SideOnly(Side.CLIENT)
    public GuiScreen getGui(final EntityPlayer var1, final boolean var2) {
        return new GuiPositronsConverter(getGuiContainer(var1));
    }

    @Override
    public void init() {
        final IInputHandler input = com.denfop.api.Recipes.inputFactory;

        Recipes.recipes.addRecipe(
                "positrons",
                new BaseMachineRecipe(
                        new Input(
                                input.getInput(new ItemStack(IUItem.crafting_elements, 1, 352)),
                                input.getInput(new ItemStack(IUItem.proton))
                        ),
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
        if (this.positrons.getEnergy() + 2000 <= 100000) {
            return this.output;
        } else {
            return null;
        }
    }

    @Override
    public void setRecipeOutput(final MachineRecipe output) {
        this.output = output;
    }

    public MachineRecipe getOutput() {
        this.output = this.inputSlotA.process();
        return this.output;
    }

    @Override
    public Set<UpgradableProperty> getUpgradableProperties() {
        return EnumSet.of(
                UpgradableProperty.Transformer,
                UpgradableProperty.EnergyStorage,
                UpgradableProperty.ItemExtract,
                UpgradableProperty.ItemInput
        );
    }

}
