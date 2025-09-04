package com.denfop.blockentity.mechanism;

import com.denfop.IUItem;
import com.denfop.api.Recipes;
import com.denfop.api.blockentity.MultiBlockEntity;
import com.denfop.api.container.CustomWorldContainer;
import com.denfop.api.recipe.*;
import com.denfop.api.upgrades.BlockEntityUpgrade;
import com.denfop.api.upgrades.EnumBlockEntityUpgrade;
import com.denfop.blockentity.base.BlockEntityElectricMachine;
import com.denfop.blocks.BlockTileEntity;
import com.denfop.blocks.mechanism.BlockBaseMachine3Entity;
import com.denfop.componets.*;
import com.denfop.containermenu.ContainerMenuBase;
import com.denfop.containermenu.ContainerMenuBattery;
import com.denfop.inventory.InventoryUpgrade;
import com.denfop.network.IUpdatableTileEvent;
import com.denfop.recipe.IInputHandler;
import com.denfop.screen.ScreenBattery;
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

public class BlockEntityBatteryFactory extends BlockEntityElectricMachine implements
        BlockEntityUpgrade, IUpdateTick, IUpdatableTileEvent, IHasRecipe {

    public final InventoryUpgrade upgradeSlot;
    public final ComponentUpgradeSlots componentUpgrade;
    public final ComponentProgress componentProgress;
    public final ComponentProcess componentProcess;
    public final InventoryRecipes inputSlotA;
    public final ComponentUpgrade componentUpgrades;
    private final SoilPollutionComponent pollutionSoil;
    private final AirPollutionComponent pollutionAir;
    public MachineRecipe output;

    public BlockEntityBatteryFactory(BlockPos pos, BlockState state) {
        super(400, 1, 1, BlockBaseMachine3Entity.battery_factory, pos, state);
        Recipes.recipes.addInitRecipes(this);
        this.upgradeSlot = new InventoryUpgrade(this, 4);
        this.componentUpgrade = this.addComponent(new ComponentUpgradeSlots(this, upgradeSlot));
        this.componentProgress = this.addComponent(new ComponentProgress(this, 1,
                (short) 200
        ));
        this.inputSlotA = new InventoryRecipes(this, "battery_factory", this);
        this.componentProcess = this.addComponent(new ComponentProcess(this, 200, 1));
        this.componentProcess.setHasAudio(false);
        this.componentProcess.setSlotOutput(outputSlot);
        this.componentProcess.setInvSlotRecipes(this.inputSlotA);

        this.componentUpgrades = this.addComponent(new ComponentUpgrade(this, TypeUpgrade.INSTANT, TypeUpgrade.STACK));
        this.pollutionSoil = this.addComponent(new SoilPollutionComponent(this, 0.1));
        this.pollutionAir = this.addComponent(new AirPollutionComponent(this, 0.1));
    }

    public static void addRecipe(int container, int fill1, int output) {
        final IInputHandler input = com.denfop.api.Recipes.inputFactory;
        Recipes.recipes.addRecipe(
                "battery_factory",
                new BaseMachineRecipe(
                        new Input(
                                input.getInput(new ItemStack(IUItem.crafting_elements.getStack(340), 1)),
                                input.getInput(new ItemStack(IUItem.crafting_elements.getStack(433), 1)),
                                input.getInput(new ItemStack(IUItem.crafting_elements.getStack(340), 1))
                                ,
                                input.getInput(new ItemStack(IUItem.crafting_elements.getStack(340), 1)),
                                input.getInput(new ItemStack(IUItem.crafting_elements.getStack(container), 1)),
                                input.getInput(new ItemStack(IUItem.crafting_elements.getStack(340), 1))
                                ,
                                input.getInput(new ItemStack(IUItem.crafting_elements.getStack(fill1), 1)),
                                input.getInput(new ItemStack(IUItem.crafting_elements.getStack(355), 1)),
                                input.getInput(new ItemStack(IUItem.crafting_elements.getStack(fill1), 1))
                        ),
                        new RecipeOutput(null, new ItemStack(IUItem.solar_battery.getStack(output), 1))
                )
        );
    }

    @Override
    public void init() {
        addRecipe(423, 421, 0);
        addRecipe(395, 311, 1);
        addRecipe(313, 399, 2);
        addRecipe(348, 346, 3);
        addRecipe(409, 407, 4);
        addRecipe(384, 382, 5);
        addRecipe(388, 389, 6);
        addRecipe(332, 330, 7);
        addRecipe(432, 430, 8);
        addRecipe(361, 359, 9);
        addRecipe(309, 307, 10);
        addRecipe(304, 302, 11);
        addRecipe(318, 316, 12);
        addRecipe(353, 350, 13);
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
    public ContainerMenuBattery getGuiContainer(final Player var1) {
        return new ContainerMenuBattery(this, var1);
    }

    @Override
    @OnlyIn(Dist.CLIENT)
    public ScreenIndustrialUpgrade<ContainerMenuBase<? extends CustomWorldContainer>> getGui(Player var1, ContainerMenuBase<? extends CustomWorldContainer> menu) {

        return new ScreenBattery((ContainerMenuBattery) menu);
    }

    @Override
    public BlockTileEntity getBlock() {
        return IUItem.basemachine2.getBlock(getTeBlock());
    }

    @Override
    public MultiBlockEntity getTeBlock() {
        return BlockBaseMachine3Entity.battery_factory;
    }

    @Override
    public void onUpdate() {

    }

    public void onLoaded() {
        super.onLoaded();
        if (!level.isClientSide) {
            inputSlotA.load();
            this.getOutput();
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
