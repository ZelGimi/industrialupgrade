package com.denfop.blockentity.mechanism;

import com.denfop.IUItem;
import com.denfop.api.Recipes;
import com.denfop.api.blockentity.MultiBlockEntity;
import com.denfop.api.container.CustomWorldContainer;
import com.denfop.api.energy.networking.EnergyNetGlobal;
import com.denfop.api.otherenergies.common.EnergyType;
import com.denfop.api.recipe.*;
import com.denfop.api.upgrades.IUpgradableBlock;
import com.denfop.api.upgrades.UpgradableProperty;
import com.denfop.blockentity.base.BlockEntityElectricMachine;
import com.denfop.blocks.BlockTileEntity;
import com.denfop.blocks.mechanism.BlockBaseMachine3Entity;
import com.denfop.componets.*;
import com.denfop.containermenu.ContainerMenuBase;
import com.denfop.containermenu.ContainerMenuNuclearWasteRecycler;
import com.denfop.inventory.InventoryUpgrade;
import com.denfop.network.IUpdatableTileEvent;
import com.denfop.recipe.IInputHandler;
import com.denfop.screen.ScreenIndustrialUpgrade;
import com.denfop.screen.ScreenNuclearWasteRecycler;
import com.denfop.utils.ModUtils;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.state.BlockState;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.api.distmarker.OnlyIn;

import java.util.*;

public class BlockEntityNuclearWasteRecycler extends BlockEntityElectricMachine implements IUpgradableBlock, IUpdateTick,
        IUpdatableTileEvent, IHasRecipe {

    public final InventoryUpgrade upgradeSlot;
    public final ComponentUpgradeSlots componentUpgrade;
    public final ComponentProgress componentProgress;
    public final InventoryRecipes inputSlotA;
    public final ComponentProcess componentProcess;
    public final ComponentBaseEnergy rad;
    public MachineRecipe output;


    public BlockEntityNuclearWasteRecycler(BlockPos pos, BlockState state) {
        super(500, 8, 1, BlockBaseMachine3Entity.nuclear_waste_recycler, pos, state);
        this.upgradeSlot = new InventoryUpgrade(this, 4);
        this.componentUpgrade = this.addComponent(new ComponentUpgradeSlots(this, upgradeSlot) {
            @Override
            public void onLoaded() {
                super.onLoaded();
                this.componentProcess = ((BlockEntityNuclearWasteRecycler) this.getParent()).componentProcess;
            }
        });
        this.addComponent(new SoilPollutionComponent(this, 0.1));
        this.addComponent(new AirPollutionComponent(this, 0.1));
        this.componentProgress = this.addComponent(new ComponentProgress(this, 1,
                (short) 100
        ));
        this.inputSlotA = new InventoryRecipes(this, "waste_recycler", this);
        this.componentProcess = this.addComponent(new ComponentProcess(this, 100, 1) {
                                                      @Override
                                                      public void operateOnce(final List<ItemStack> processResult) {
                                                          super.operateOnce(processResult);
                                                          ((BlockEntityNuclearWasteRecycler) this.getParent()).rad.addEnergy(150);
                                                      }
                                                  }
        );
        this.componentProcess.setHasAudio(false);
        this.componentProcess.setSlotOutput(outputSlot);
        this.componentProcess.setInvSlotRecipes(this.inputSlotA);
        this.rad = this.addComponent((new ComponentBaseEnergy(
                EnergyType.RADIATION, this, 10000,

                new ArrayList<>(ModUtils.noFacings),
                Arrays.asList(Direction.values()),
                0,
                EnergyNetGlobal.instance.getTierFromPower(14)
        )));
        Recipes.recipes.addInitRecipes(this);
    }

    @Override
    public BlockTileEntity getBlock() {
        return IUItem.basemachine2.getBlock(getTeBlock());
    }

    @Override
    public MultiBlockEntity getTeBlock() {
        return BlockBaseMachine3Entity.nuclear_waste_recycler;
    }

    @Override
    public ContainerMenuNuclearWasteRecycler getGuiContainer(final Player var1) {
        return new ContainerMenuNuclearWasteRecycler(this, var1);
    }

    @Override
    @OnlyIn(Dist.CLIENT)
    public ScreenIndustrialUpgrade<ContainerMenuBase<? extends CustomWorldContainer>> getGui(Player var1, ContainerMenuBase<? extends CustomWorldContainer> menu) {
        return new ScreenNuclearWasteRecycler((ContainerMenuNuclearWasteRecycler) menu);
    }

    @Override
    public void init() {
        final IInputHandler input = com.denfop.api.Recipes.inputFactory;

        Recipes.recipes.addRecipe(
                "waste_recycler",
                new BaseMachineRecipe(
                        new Input(
                                input.getInput(new ItemStack(IUItem.crafting_elements.getStack(443), 1))),
                        new RecipeOutput(null, ItemStack.EMPTY)
                )
        );
    }

    @Override
    public void onLoaded() {
        super.onLoaded();
        if (!level.isClientSide) {
            inputSlotA.load();
            this.getOutput();


        }
    }

    @Override
    public void onUpdate() {

    }

    @Override
    public MachineRecipe getRecipeOutput() {
        if (this.rad.getEnergy() + 100 <= 10000) {
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
                UpgradableProperty.Processing,
                UpgradableProperty.Transformer,
                UpgradableProperty.EnergyStorage,
                UpgradableProperty.ItemExtract,
                UpgradableProperty.ItemInput
        );
    }

}
