package com.denfop.blockentity.mechanism;

import com.denfop.IUItem;
import com.denfop.api.Recipes;
import com.denfop.api.blockentity.MultiBlockEntity;
import com.denfop.api.container.CustomWorldContainer;
import com.denfop.api.otherenergies.common.EnergyType;
import com.denfop.api.recipe.IHasRecipe;
import com.denfop.api.recipe.IUpdateTick;
import com.denfop.api.recipe.InventoryRecipes;
import com.denfop.api.recipe.MachineRecipe;
import com.denfop.api.upgrades.BlockEntityUpgrade;
import com.denfop.api.upgrades.EnumBlockEntityUpgrade;
import com.denfop.api.widget.EnumTypeSlot;
import com.denfop.blockentity.base.BlockEntityElectricMachine;
import com.denfop.blocks.BlockTileEntity;
import com.denfop.blocks.mechanism.BlockBaseMachine3Entity;
import com.denfop.componets.*;
import com.denfop.containermenu.ContainerMenuBase;
import com.denfop.containermenu.ContainerMenuCrystalCharger;
import com.denfop.inventory.Inventory;
import com.denfop.inventory.InventoryUpgrade;
import com.denfop.network.IUpdatableTileEvent;
import com.denfop.screen.ScreenCrystalCharger;
import com.denfop.screen.ScreenIndustrialUpgrade;
import com.denfop.utils.Keyboard;
import com.denfop.utils.Localization;
import net.minecraft.core.BlockPos;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

import java.util.EnumSet;
import java.util.List;
import java.util.Set;

public class BlockEntityCrystalCharger extends BlockEntityElectricMachine implements
        BlockEntityUpgrade, IUpdateTick, IUpdatableTileEvent, IHasRecipe {

    public final InventoryUpgrade upgradeSlot;
    public final ComponentUpgradeSlots componentUpgrade;
    public final ComponentProgress componentProgress;
    public final ComponentProcess componentProcess;
    public final InventoryRecipes inputSlotA;
    public final Inventory input_slot;
    public final ComponentBaseEnergy ampere;
    private final SoilPollutionComponent pollutionSoil;
    private final AirPollutionComponent pollutionAir;
    public MachineRecipe output;

    public BlockEntityCrystalCharger(BlockPos pos, BlockState state) {
        super(200, 1, 1, BlockBaseMachine3Entity.crystal_charge, pos, state);
        Recipes.recipes.addInitRecipes(this);
        this.upgradeSlot = new InventoryUpgrade(this, 4);
        this.componentUpgrade = this.addComponent(new ComponentUpgradeSlots(this, upgradeSlot) {
            @Override
            public void onLoaded() {
                super.onLoaded();
                this.componentProcess = ((BlockEntityCrystalCharger) this.getParent()).componentProcess;
            }
        });
        this.ampere = this.addComponent(ComponentBaseEnergy.asBasicSink(EnergyType.AMPERE, this, 4000));

        this.componentProgress = this.addComponent(new ComponentProgress(this, 1,
                (short) 100
        ));
        this.inputSlotA = new InventoryRecipes(this, "charger", this);
        this.componentProcess = this.addComponent(new ComponentProcess(this, 200, 1) {
            @Override
            public boolean checkRecipe() {
                return ampere.getEnergy() >= 2D * (this.defaultOperationLength * 1D / this.operationLength);
            }

            @Override
            public void consumeEnergy() {
                ampere.useEnergy(2D * (this.defaultOperationLength * 1D / this.operationLength));
            }
        });
        this.componentProcess.setHasAudio(false);
        this.componentProcess.setSlotOutput(outputSlot);
        this.componentProcess.setInvSlotRecipes(this.inputSlotA);
        this.pollutionSoil = this.addComponent(new SoilPollutionComponent(this, 0.05));
        this.pollutionAir = this.addComponent(new AirPollutionComponent(this, 0.05));
        this.input_slot = new Inventory(this, Inventory.TypeItemSlot.INPUT, 1) {
            @Override
            public ItemStack set(final int index, final ItemStack content) {
                super.set(index, content);
                if (this.get(0).isEmpty()) {
                    ((BlockEntityCrystalCharger) this.base).inputSlotA.changeAccepts(ItemStack.EMPTY);
                } else {
                    ((BlockEntityCrystalCharger) this.base).inputSlotA.changeAccepts(this.get(0));
                }
                return content;
            }

            @Override
            public boolean canPlaceItem(final int index, final ItemStack stack) {
                return stack.getItem() == IUItem.recipe_schedule.getItem();
            }

            @Override
            public EnumTypeSlot getTypeSlot() {
                return EnumTypeSlot.RECIPE_SCHEDULE;
            }
        };

    }


    public void addInformation(ItemStack stack, List<String> tooltip) {
        if (!Keyboard.isKeyDown(Keyboard.KEY_LSHIFT)) {
            tooltip.add(Localization.translate("press.lshift"));
        }
        if (Keyboard.isKeyDown(Keyboard.KEY_LSHIFT)) {
            tooltip.add(Localization.translate("iu.machines_work_energy") + this.componentProcess.getEnergyConsume() + Localization.translate(
                    "iu.machines_work_energy_type_eu"));
            tooltip.add(Localization.translate("iu.machines_work_length") + this.componentProcess.getOperationsPerTick());
        }
        super.addInformation(stack, tooltip);

    }

    @Override
    public ContainerMenuCrystalCharger getGuiContainer(final Player var1) {
        return new ContainerMenuCrystalCharger(var1, this);
    }


    @Override
    @OnlyIn(Dist.CLIENT)
    public ScreenIndustrialUpgrade<ContainerMenuBase<? extends CustomWorldContainer>> getGui(Player var1, ContainerMenuBase<? extends CustomWorldContainer> menu) {

        return new ScreenCrystalCharger((ContainerMenuCrystalCharger) menu);
    }

    @Override
    public void init() {

    }

    @Override
    public BlockTileEntity getBlock() {
        return IUItem.basemachine2.getBlock(getTeBlock());
    }

    @Override
    public MultiBlockEntity getTeBlock() {
        return BlockBaseMachine3Entity.crystal_charge;
    }

    @Override
    public void onUpdate() {

    }

    public void onLoaded() {
        super.onLoaded();
        if (!level.isClientSide) {
            inputSlotA.load();
            this.getOutput();
            if (this.input_slot.isEmpty()) {
                (this).inputSlotA.changeAccepts(ItemStack.EMPTY);
            } else {
                (this).inputSlotA.changeAccepts(this.input_slot.get(0));
            }
        }


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

    @Override
    public Set<EnumBlockEntityUpgrade> getUpgradableProperties() {
        return EnumSet.of(
                EnumBlockEntityUpgrade.Processing,
                EnumBlockEntityUpgrade.Transformer,
                EnumBlockEntityUpgrade.EnergyStorage,
                EnumBlockEntityUpgrade.ItemExtract,
                EnumBlockEntityUpgrade.ItemInput
        );
    }


}
