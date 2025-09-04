package com.denfop.blockentity.mechanism;

import com.denfop.IUItem;
import com.denfop.api.Recipes;
import com.denfop.api.blockentity.MultiBlockEntity;
import com.denfop.api.container.CustomWorldContainer;
import com.denfop.api.recipe.*;
import com.denfop.api.upgrades.BlockEntityUpgrade;
import com.denfop.api.upgrades.EnumBlockEntityUpgrade;
import com.denfop.api.widget.EnumTypeSlot;
import com.denfop.blockentity.base.BlockEntityElectricMachine;
import com.denfop.blocks.BlockTileEntity;
import com.denfop.blocks.mechanism.BlockBaseMachine3Entity;
import com.denfop.componets.*;
import com.denfop.containermenu.ContainerMenuBase;
import com.denfop.containermenu.ContainerMenuBrewingPlant;
import com.denfop.inventory.Inventory;
import com.denfop.inventory.InventoryUpgrade;
import com.denfop.network.IUpdatableTileEvent;
import com.denfop.recipe.IInputHandler;
import com.denfop.screen.ScreenBrewingPlant;
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

public class BlockEntityBrewingPlant extends BlockEntityElectricMachine implements
        BlockEntityUpgrade, IUpdateTick, IUpdatableTileEvent, IHasRecipe {

    public final InventoryUpgrade upgradeSlot;
    public final ComponentUpgradeSlots componentUpgrade;
    public final ComponentProgress componentProgress;
    public final ComponentProcess componentProcess;
    public final InventoryRecipes inputSlotA;
    public final ComponentUpgrade componentUpgrades;
    public final Inventory input_slot;
    private final SoilPollutionComponent pollutionSoil;
    private final AirPollutionComponent pollutionAir;
    public MachineRecipe output;

    public BlockEntityBrewingPlant(BlockPos pos, BlockState state) {
        super(200, 1, 1, BlockBaseMachine3Entity.electric_brewing, pos, state);
        this.upgradeSlot = new InventoryUpgrade(this, 4);
        this.componentUpgrade = this.addComponent(new ComponentUpgradeSlots(this, upgradeSlot));
        this.componentProgress = this.addComponent(new ComponentProgress(this, 1,
                (short) 100
        ));
        this.inputSlotA = new InventoryRecipes(this, "brewing", this);
        this.componentProcess = this.addComponent(new ComponentProcess(this, 200, 1));
        this.componentProcess.setHasAudio(false);
        this.componentProcess.setSlotOutput(outputSlot);
        this.componentProcess.setInvSlotRecipes(this.inputSlotA);
        this.componentUpgrades = this.addComponent(new ComponentUpgrade(this, TypeUpgrade.INSTANT, TypeUpgrade.STACK));
        this.pollutionSoil = this.addComponent(new SoilPollutionComponent(this, 0.1));
        this.pollutionAir = this.addComponent(new AirPollutionComponent(this, 0.2));
        this.input_slot = new Inventory(this, Inventory.TypeItemSlot.INPUT, 1) {
            @Override
            public ItemStack set(final int index, final ItemStack content) {
                super.set(index, content);
                if (this.get(0).isEmpty()) {
                    ((BlockEntityBrewingPlant) this.base).inputSlotA.changeAccepts(ItemStack.EMPTY);
                } else {
                    ((BlockEntityBrewingPlant) this.base).inputSlotA.changeAccepts(this.get(0));
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

    public static void addRecipe(int container, ItemStack output) {
        final IInputHandler input = com.denfop.api.Recipes.inputFactory;
        Recipes.recipes.addRecipe(
                "laser",
                new BaseMachineRecipe(
                        new Input(
                                input.getInput(new ItemStack(IUItem.crafting_elements.getStack(container), 4))),
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
    public ContainerMenuBrewingPlant getGuiContainer(final Player var1) {
        return new ContainerMenuBrewingPlant(var1, this);
    }


    @Override
    @OnlyIn(Dist.CLIENT)
    public ScreenIndustrialUpgrade<ContainerMenuBase<? extends CustomWorldContainer>> getGui(Player var1, ContainerMenuBase<? extends CustomWorldContainer> menu) {

        return new ScreenBrewingPlant((ContainerMenuBrewingPlant) menu);
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
        return BlockBaseMachine3Entity.electric_brewing;
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
