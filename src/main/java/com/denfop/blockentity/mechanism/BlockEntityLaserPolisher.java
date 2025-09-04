package com.denfop.blockentity.mechanism;

import com.denfop.IUItem;
import com.denfop.api.Recipes;
import com.denfop.api.blockentity.MultiBlockEntity;
import com.denfop.api.container.CustomWorldContainer;
import com.denfop.api.recipe.*;
import com.denfop.api.upgrades.IUpgradableBlock;
import com.denfop.api.upgrades.UpgradableProperty;
import com.denfop.api.widget.EnumTypeSlot;
import com.denfop.blockentity.base.BlockEntityElectricMachine;
import com.denfop.blocks.BlockTileEntity;
import com.denfop.blocks.mechanism.BlockBaseMachine3Entity;
import com.denfop.componets.*;
import com.denfop.containermenu.ContainerMenuBase;
import com.denfop.containermenu.ContainerMenuLaserPolisher;
import com.denfop.inventory.Inventory;
import com.denfop.inventory.InventoryUpgrade;
import com.denfop.network.IUpdatableTileEvent;
import com.denfop.recipe.IInputHandler;
import com.denfop.screen.ScreenIndustrialUpgrade;
import com.denfop.screen.ScreenLaserPolisher;
import com.denfop.utils.Keyboard;
import com.denfop.utils.Localization;
import com.denfop.utils.ModUtils;
import net.minecraft.core.BlockPos;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

import java.util.EnumSet;
import java.util.List;
import java.util.Set;

public class BlockEntityLaserPolisher extends BlockEntityElectricMachine implements
        IUpgradableBlock, IUpdateTick, IUpdatableTileEvent, IHasRecipe {

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

    public BlockEntityLaserPolisher(BlockPos pos, BlockState state) {
        super(200, 1, 1, BlockBaseMachine3Entity.laser_polisher, pos, state);
        Recipes.recipes.addInitRecipes(this);
        this.upgradeSlot = new InventoryUpgrade(this, 4);
        this.componentUpgrade = this.addComponent(new ComponentUpgradeSlots(this, upgradeSlot));
        this.componentProgress = this.addComponent(new ComponentProgress(this, 1,
                (short) 100
        ));
        this.inputSlotA = new InventoryRecipes(this, "laser", this);
        this.componentProcess = this.addComponent(new ComponentProcess(this, 200, 1));
        this.componentProcess.setHasAudio(false);
        this.componentProcess.setSlotOutput(outputSlot);
        this.componentProcess.setInvSlotRecipes(this.inputSlotA);
        this.componentUpgrades = this.addComponent(new ComponentUpgrade(this, TypeUpgrade.INSTANT, TypeUpgrade.STACK));
        this.pollutionSoil = this.addComponent(new SoilPollutionComponent(this, 0.1));
        this.pollutionAir = this.addComponent(new AirPollutionComponent(this, 0.1));
        this.input_slot = new Inventory(this, Inventory.TypeItemSlot.INPUT, 1) {
            @Override
            public ItemStack set(final int index, final ItemStack content) {
                super.set(index, content);
                if (this.get(0).isEmpty()) {
                    ((BlockEntityLaserPolisher) this.base).inputSlotA.changeAccepts(ItemStack.EMPTY);
                } else {
                    ((BlockEntityLaserPolisher) this.base).inputSlotA.changeAccepts(this.get(0));
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
    public ContainerMenuLaserPolisher getGuiContainer(final Player var1) {
        return new ContainerMenuLaserPolisher(var1, this);
    }

    public ContainerMenuLaserPolisher getGuiContainer1(final Player var1) {
        return new ContainerMenuLaserPolisher(var1, this, false);
    }

    @Override
    @OnlyIn(Dist.CLIENT)
    public ScreenIndustrialUpgrade<ContainerMenuBase<? extends CustomWorldContainer>> getGui(Player var1, ContainerMenuBase<? extends CustomWorldContainer> menu) {
        return new ScreenLaserPolisher((ContainerMenuLaserPolisher) menu);
    }

    @Override
    public void init() {
        addRecipe(new ItemStack(IUItem.crafting_elements.getStack(502)), IUItem.advIronIngot);
        addRecipe("forge:ores/Diamond", new ItemStack(Items.DIAMOND, 4));
        addRecipe("forge:ores/Coal", new ItemStack(Items.COAL, 5));
        addRecipe("forge:ores/Quartz", new ItemStack(Items.QUARTZ, 4));
        addRecipe("forge:ores/Lapis", new ItemStack(Items.LAPIS_LAZULI, 5));
        addRecipe("forge:ores/Sulfur", new ItemStack(IUItem.iudust.getStack(31), 4));
        addRecipe("forge:ores/Bor", new ItemStack(IUItem.crafting_elements.getStack(448)));
        addRecipe("forge:ores/Boron", new ItemStack(IUItem.crafting_elements.getStack(448)));
        addRecipe("forge:ores/Redstone", new ItemStack(Items.REDSTONE, 5));
        addRecipe("forge:dusts/Quartz", new ItemStack(Items.QUARTZ));
        addRecipe(319, new ItemStack(IUItem.crafting_elements.getStack(357)));
        addRecipe(new ItemStack(IUItem.crafting_elements.getStack(493)), new ItemStack(IUItem.crafting_elements.getStack(495)));
        addRecipe(IUItem.sulfurDust, new ItemStack(IUItem.crafting_elements.getStack(476)));
        addRecipe(ModUtils.setSize(IUItem.UranFuel, 8), new ItemStack(IUItem.crafting_elements.getStack(443)));

        addRecipe(new ItemStack(IUItem.basalts.getItem(2)), new ItemStack(Blocks.OBSIDIAN));

        addRecipe(new ItemStack(IUItem.crafting_elements.getStack(503)), new ItemStack(IUItem.crafting_elements.getStack(504)));
        addRecipe(new ItemStack(IUItem.ore2.getItem()), new ItemStack(IUItem.crafting_elements.getStack(447)));
        addRecipe("forge:ores/Lithium", new ItemStack(IUItem.crafting_elements.getStack(447)));
        addRecipe(new ItemStack(IUItem.ore2.getItem(2)), new ItemStack(IUItem.crafting_elements.getStack(448)));
        addRecipe(new ItemStack(IUItem.ore2.getItem(1)), new ItemStack(IUItem.crafting_elements.getStack(449)));
        addRecipe("forge:ores/Beryllium", new ItemStack(IUItem.crafting_elements.getStack(449)));
        addRecipe(
                IUItem.coal_chunk,
                new ItemStack(Items.DIAMOND)
        );
        addRecipe(new ItemStack(IUItem.raw_apatite.getItem()), new ItemStack(IUItem.apatite_cube.getItem()));


        for (int i = 0; i < 14; i++) {
            addRecipe(new ItemStack(IUItem.photonglass.getStack(i)), new ItemStack(IUItem.solar_day_glass.getStack(i)));
        }
        final IInputHandler input = com.denfop.api.Recipes.inputFactory;
        Recipes.recipes.addRecipe("laser", new BaseMachineRecipe(
                new Input(input.getInput(new ItemStack(IUItem.rawIngot.getStack(0)))),
                new RecipeOutput(null, new ItemStack(IUItem.iuingot.getStack(0)))
        ));
        Recipes.recipes.addRecipe("laser", new BaseMachineRecipe(
                new Input(input.getInput(new ItemStack(IUItem.rawIngot.getStack(1)))),
                new RecipeOutput(null, new ItemStack(IUItem.iuingot.getStack(1)))
        ));
        Recipes.recipes.addRecipe("laser", new BaseMachineRecipe(
                new Input(input.getInput(new ItemStack(IUItem.rawIngot.getStack(2)))),
                new RecipeOutput(null, new ItemStack(IUItem.iuingot.getStack(2)))
        ));
        Recipes.recipes.addRecipe("laser", new BaseMachineRecipe(
                new Input(input.getInput(new ItemStack(IUItem.rawIngot.getStack(3)))),
                new RecipeOutput(null, new ItemStack(IUItem.iuingot.getStack(3)))
        ));
        Recipes.recipes.addRecipe("laser", new BaseMachineRecipe(
                new Input(input.getInput(new ItemStack(IUItem.rawIngot.getStack(4)))),
                new RecipeOutput(null, new ItemStack(IUItem.iuingot.getStack(6)))
        ));
        Recipes.recipes.addRecipe("laser", new BaseMachineRecipe(
                new Input(input.getInput(new ItemStack(IUItem.rawIngot.getStack(5)))),
                new RecipeOutput(null, new ItemStack(IUItem.iuingot.getStack(7)))
        ));
        Recipes.recipes.addRecipe("laser", new BaseMachineRecipe(
                new Input(input.getInput(new ItemStack(IUItem.rawIngot.getStack(6)))),
                new RecipeOutput(null, new ItemStack(IUItem.iuingot.getStack(8)))
        ));

        Recipes.recipes.addRecipe("laser", new BaseMachineRecipe(
                new Input(input.getInput(new ItemStack(IUItem.rawIngot.getStack(7)))),
                new RecipeOutput(null, new ItemStack(IUItem.iuingot.getStack(9)))
        ));
        Recipes.recipes.addRecipe("laser", new BaseMachineRecipe(
                new Input(input.getInput(new ItemStack(IUItem.rawIngot.getStack(8)))),
                new RecipeOutput(null, new ItemStack(IUItem.iuingot.getStack(10)))
        ));
        Recipes.recipes.addRecipe("laser", new BaseMachineRecipe(
                new Input(input.getInput(new ItemStack(IUItem.rawIngot.getStack(9)))),
                new RecipeOutput(null, new ItemStack(IUItem.iuingot.getStack(11)))
        ));
        Recipes.recipes.addRecipe("laser", new BaseMachineRecipe(
                new Input(input.getInput(new ItemStack(IUItem.rawIngot.getStack(10)))),
                new RecipeOutput(null, new ItemStack(IUItem.iuingot.getStack(12)))
        ));
        Recipes.recipes.addRecipe("laser", new BaseMachineRecipe(
                new Input(input.getInput(new ItemStack(IUItem.rawIngot.getStack(10)))),
                new RecipeOutput(null, new ItemStack(IUItem.iuingot.getStack(12)))
        ));
        Recipes.recipes.addRecipe("laser", new BaseMachineRecipe(
                new Input(input.getInput(new ItemStack(IUItem.rawIngot.getStack(11)))),
                new RecipeOutput(null, new ItemStack(IUItem.iuingot.getStack(14)))
        ));
        Recipes.recipes.addRecipe("laser", new BaseMachineRecipe(
                new Input(input.getInput(new ItemStack(IUItem.rawIngot.getStack(12)))),
                new RecipeOutput(null, new ItemStack(IUItem.iuingot.getStack(15)))
        ));
        Recipes.recipes.addRecipe("laser", new BaseMachineRecipe(
                new Input(input.getInput(new ItemStack(IUItem.rawIngot.getStack(13)))),
                new RecipeOutput(null, new ItemStack(IUItem.iuingot.getStack(16)))
        ));
        Recipes.recipes.addRecipe("laser", new BaseMachineRecipe(
                new Input(input.getInput(new ItemStack(IUItem.rawIngot.getStack(14)))),
                new RecipeOutput(null, new ItemStack(IUItem.iuingot.getStack(17)))
        ));
        Recipes.recipes.addRecipe("laser", new BaseMachineRecipe(
                new Input(input.getInput(new ItemStack(IUItem.rawIngot.getStack(15)))),
                new RecipeOutput(null, new ItemStack(IUItem.iuingot.getStack(18)))
        ));
        Recipes.recipes.addRecipe("laser", new BaseMachineRecipe(
                new Input(input.getInput(new ItemStack(IUItem.rawIngot.getStack(16)))),
                new RecipeOutput(null, new ItemStack(Items.COPPER_INGOT))
        ));
        Recipes.recipes.addRecipe("laser", new BaseMachineRecipe(
                new Input(input.getInput(new ItemStack(IUItem.rawIngot.getStack(17)))),
                new RecipeOutput(null, new ItemStack(Items.GOLD_INGOT, 1)))
        );
        Recipes.recipes.addRecipe("laser", new BaseMachineRecipe(
                new Input(input.getInput(new ItemStack(IUItem.rawIngot.getStack(18)))),
                new RecipeOutput(null, new ItemStack(Items.IRON_INGOT, 1)))
        );
        Recipes.recipes.addRecipe("laser", new BaseMachineRecipe(
                new Input(input.getInput(new ItemStack(IUItem.rawIngot.getStack(19)))),
                new RecipeOutput(null, new ItemStack(IUItem.iuingot.getStack(22)))
        ));
        Recipes.recipes.addRecipe("laser", new BaseMachineRecipe(
                new Input(input.getInput(new ItemStack(IUItem.rawIngot.getStack(20)))),
                new RecipeOutput(null, new ItemStack(IUItem.iuingot.getStack(24)))
        ));
        Recipes.recipes.addRecipe("laser", new BaseMachineRecipe(
                new Input(input.getInput(new ItemStack(IUItem.rawIngot.getStack(22)))),
                new RecipeOutput(null, new ItemStack(IUItem.iuingot.getStack(25)))
        ));
        Recipes.recipes.addRecipe("laser", new BaseMachineRecipe(
                new Input(input.getInput(new ItemStack(IUItem.rawIngot.getStack(23)))),
                new RecipeOutput(null, new ItemStack(IUItem.iuingot.getStack(26)))
        ));
        Recipes.recipes.addRecipe("laser", new BaseMachineRecipe(
                new Input(input.getInput(new ItemStack(IUItem.rawIngot.getStack(24)))),
                new RecipeOutput(null, new ItemStack(IUItem.iuingot.getStack(27)))
        ));
        for (int i = 25; i < 40; i++) {
            Recipes.recipes.addRecipe("laser", new BaseMachineRecipe(
                    new Input(input.getInput(new ItemStack(IUItem.rawIngot.getStack(i)))),
                    new RecipeOutput(null, new ItemStack(IUItem.iuingot.getStack(i + 3)))
            ));
        }
    }

    @Override
    public BlockTileEntity getBlock() {
        return IUItem.basemachine2.getBlock(getTeBlock());
    }

    @Override
    public MultiBlockEntity getTeBlock() {
        return BlockBaseMachine3Entity.laser_polisher;
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
