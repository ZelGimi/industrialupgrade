package com.denfop.tiles.mechanism;

import com.denfop.IUCore;
import com.denfop.IUItem;
import com.denfop.Localization;
import com.denfop.api.Recipes;
import com.denfop.api.gui.EnumTypeSlot;
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
import com.denfop.componets.AirPollutionComponent;
import com.denfop.componets.ComponentProcess;
import com.denfop.componets.ComponentProgress;
import com.denfop.componets.ComponentUpgrade;
import com.denfop.componets.ComponentUpgradeSlots;
import com.denfop.componets.SoilPollutionComponent;
import com.denfop.componets.TypeUpgrade;
import com.denfop.container.ContainerLaserPolisher;
import com.denfop.gui.GuiLaserPolisher;
import com.denfop.invslot.InvSlot;
import com.denfop.invslot.InvSlotUpgrade;
import com.denfop.network.IUpdatableTileEvent;
import com.denfop.recipe.IInputHandler;
import com.denfop.tiles.base.TileElectricMachine;
import com.denfop.utils.ModUtils;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import org.lwjgl.input.Keyboard;

import java.util.EnumSet;
import java.util.List;
import java.util.Set;

public class TileEntityLaserPolisher extends TileElectricMachine implements
        IUpgradableBlock, IUpdateTick, IUpdatableTileEvent, IHasRecipe {

    public final InvSlotUpgrade upgradeSlot;
    public final ComponentUpgradeSlots componentUpgrade;
    public final ComponentProgress componentProgress;
    public final ComponentProcess componentProcess;
    public final InvSlotRecipes inputSlotA;
    public final ComponentUpgrade componentUpgrades;
    public final InvSlot input_slot;
    private final SoilPollutionComponent pollutionSoil;
    private final AirPollutionComponent pollutionAir;
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
        this.pollutionSoil = this.addComponent(new SoilPollutionComponent(this, 0.1));
        this.pollutionAir = this.addComponent(new AirPollutionComponent(this, 0.1));
        this.input_slot = new InvSlot(this, InvSlot.TypeItemSlot.INPUT, 1) {
            @Override
            public void put(final int index, final ItemStack content) {
                super.put(index, content);
                if (this.get().isEmpty()) {
                    ((TileEntityLaserPolisher) this.base).inputSlotA.changeAccepts(ItemStack.EMPTY);
                } else {
                    ((TileEntityLaserPolisher) this.base).inputSlotA.changeAccepts(this.get());
                }
            }

            @Override
            public boolean accepts(final ItemStack stack, final int index) {
                return stack.getItem() == IUItem.recipe_schedule;
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
                                input.getInput(new ItemStack(IUItem.crafting_elements, 4, container))),
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
    public ContainerLaserPolisher getGuiContainer(final EntityPlayer var1) {
        return new ContainerLaserPolisher(var1, this);
    }

    public ContainerLaserPolisher getGuiContainer1(final EntityPlayer var1) {
        return new ContainerLaserPolisher(var1, this, false);
    }

    @Override
    @SideOnly(Side.CLIENT)
    public GuiScreen getGui(final EntityPlayer var1, final boolean var2) {
        return new GuiLaserPolisher(getGuiContainer(var1));
    }

    @Override
    public void init() {
        addRecipe("oreDiamond", new ItemStack(Items.DIAMOND, 4));
        addRecipe("oreCoal", new ItemStack(Items.COAL, 5));
        addRecipe("oreQuartz", new ItemStack(Items.QUARTZ, 4));
        addRecipe("oreLapis", new ItemStack(Items.DYE, 5, 4));
        addRecipe("oreSulfur", new ItemStack(IUItem.iudust, 4, 31));
        addRecipe("oreBoron", new ItemStack(IUItem.crafting_elements, 1, 448));
        addRecipe("oreRedstone", new ItemStack(Items.REDSTONE, 5));
        addRecipe("dustQuartz", new ItemStack(Items.QUARTZ));
        addRecipe(319, new ItemStack(IUItem.crafting_elements, 1, 357));
        addRecipe(new ItemStack(IUItem.crafting_elements, 1, 493), new ItemStack(IUItem.crafting_elements, 1, 495));
        addRecipe(IUItem.sulfurDust, new ItemStack(IUItem.crafting_elements, 1, 476));
        addRecipe(ModUtils.setSize(IUItem.UranFuel, 8), new ItemStack(IUItem.crafting_elements, 1, 443));

        addRecipe(new ItemStack(IUItem.basalts, 1, 2), new ItemStack(Blocks.OBSIDIAN));

        addRecipe(new ItemStack(IUItem.crafting_elements, 1, 503), new ItemStack(IUItem.crafting_elements, 1, 504));
        addRecipe(new ItemStack(IUItem.ore2), new ItemStack(IUItem.crafting_elements, 1, 447));
        addRecipe("oreLithium", new ItemStack(IUItem.crafting_elements, 1, 447));
        addRecipe(new ItemStack(IUItem.ore2, 1, 2), new ItemStack(IUItem.crafting_elements, 1, 448));
        addRecipe(new ItemStack(IUItem.ore2, 1, 1), new ItemStack(IUItem.crafting_elements, 1, 449));
        addRecipe("oreBeryllium", new ItemStack(IUItem.crafting_elements, 1, 449));
        addRecipe(
                IUItem.coal_chunk,
                new ItemStack(Items.DIAMOND)
        );
        addRecipe(new ItemStack(IUItem.raw_apatite), new ItemStack(IUItem.apatite_cube));


        for (int i = 0; i < 14; i++) {
            addRecipe(new ItemStack(IUItem.photonglass, 1, i), new ItemStack(IUItem.solar_day_glass, 1, i));
        }
        final IInputHandler input = com.denfop.api.Recipes.inputFactory;
        Recipes.recipes.addRecipe("laser", new BaseMachineRecipe(
                new Input(input.getInput(new ItemStack(IUItem.rawIngot, 1, 0))),
                new RecipeOutput(null, new ItemStack(IUItem.iuingot, 1, 0))
        ));
        Recipes.recipes.addRecipe("laser", new BaseMachineRecipe(
                new Input(input.getInput(new ItemStack(IUItem.rawIngot, 1, 1))),
                new RecipeOutput(null, new ItemStack(IUItem.iuingot, 1, 1))
        ));
        Recipes.recipes.addRecipe("laser", new BaseMachineRecipe(
                new Input(input.getInput(new ItemStack(IUItem.rawIngot, 1, 2))),
                new RecipeOutput(null, new ItemStack(IUItem.iuingot, 1, 2))
        ));
        Recipes.recipes.addRecipe("laser", new BaseMachineRecipe(
                new Input(input.getInput(new ItemStack(IUItem.rawIngot, 1, 3))),
                new RecipeOutput(null, new ItemStack(IUItem.iuingot, 1, 3))
        ));
        Recipes.recipes.addRecipe("laser", new BaseMachineRecipe(
                new Input(input.getInput(new ItemStack(IUItem.rawIngot, 1, 4))),
                new RecipeOutput(null, new ItemStack(IUItem.iuingot, 1, 6))
        ));
        Recipes.recipes.addRecipe("laser", new BaseMachineRecipe(
                new Input(input.getInput(new ItemStack(IUItem.rawIngot, 1, 5))),
                new RecipeOutput(null, new ItemStack(IUItem.iuingot, 1, 7))
        ));
        Recipes.recipes.addRecipe("laser", new BaseMachineRecipe(
                new Input(input.getInput(new ItemStack(IUItem.rawIngot, 1, 6))),
                new RecipeOutput(null, new ItemStack(IUItem.iuingot, 1, 8))
        ));

        Recipes.recipes.addRecipe("laser", new BaseMachineRecipe(
                new Input(input.getInput(new ItemStack(IUItem.rawIngot, 1, 7))),
                new RecipeOutput(null, new ItemStack(IUItem.iuingot, 1, 9))
        ));
        Recipes.recipes.addRecipe("laser", new BaseMachineRecipe(
                new Input(input.getInput(new ItemStack(IUItem.rawIngot, 1, 8))),
                new RecipeOutput(null, new ItemStack(IUItem.iuingot, 1, 10))
        ));
        Recipes.recipes.addRecipe("laser", new BaseMachineRecipe(
                new Input(input.getInput(new ItemStack(IUItem.rawIngot, 1, 9))),
                new RecipeOutput(null, new ItemStack(IUItem.iuingot, 1, 11))
        ));
        Recipes.recipes.addRecipe("laser", new BaseMachineRecipe(
                new Input(input.getInput(new ItemStack(IUItem.rawIngot, 1, 10))),
                new RecipeOutput(null, new ItemStack(IUItem.iuingot, 1, 12))
        ));
        Recipes.recipes.addRecipe("laser", new BaseMachineRecipe(
                new Input(input.getInput(new ItemStack(IUItem.rawIngot, 1, 10))),
                new RecipeOutput(null, new ItemStack(IUItem.iuingot, 1, 12))
        ));
        Recipes.recipes.addRecipe("laser", new BaseMachineRecipe(
                new Input(input.getInput(new ItemStack(IUItem.rawIngot, 1, 11))),
                new RecipeOutput(null, new ItemStack(IUItem.iuingot, 1, 14))
        ));
        Recipes.recipes.addRecipe("laser", new BaseMachineRecipe(
                new Input(input.getInput(new ItemStack(IUItem.rawIngot, 1, 12))),
                new RecipeOutput(null, new ItemStack(IUItem.iuingot, 1, 15))
        ));
        Recipes.recipes.addRecipe("laser", new BaseMachineRecipe(
                new Input(input.getInput(new ItemStack(IUItem.rawIngot, 1, 13))),
                new RecipeOutput(null, new ItemStack(IUItem.iuingot, 1, 16))
        ));
        Recipes.recipes.addRecipe("laser", new BaseMachineRecipe(
                new Input(input.getInput(new ItemStack(IUItem.rawIngot, 1, 14))),
                new RecipeOutput(null, new ItemStack(IUItem.iuingot, 1, 17))
        ));
        Recipes.recipes.addRecipe("laser", new BaseMachineRecipe(
                new Input(input.getInput(new ItemStack(IUItem.rawIngot, 1, 15))),
                new RecipeOutput(null, new ItemStack(IUItem.iuingot, 1, 18))
        ));
        Recipes.recipes.addRecipe("laser", new BaseMachineRecipe(
                new Input(input.getInput(new ItemStack(IUItem.rawIngot, 1, 16))),
                new RecipeOutput(null, new ItemStack(IUItem.iuingot, 1, 21))
        ));
        Recipes.recipes.addRecipe("laser", new BaseMachineRecipe(
                new Input(input.getInput(new ItemStack(IUItem.rawIngot, 1, 17))),
                new RecipeOutput(null, new ItemStack(Items.GOLD_INGOT, 1))
        ));
        Recipes.recipes.addRecipe("laser", new BaseMachineRecipe(
                new Input(input.getInput(new ItemStack(IUItem.rawIngot, 1, 18))),
                new RecipeOutput(null, new ItemStack(Items.IRON_INGOT, 1))
        ));
        Recipes.recipes.addRecipe("laser", new BaseMachineRecipe(
                new Input(input.getInput(new ItemStack(IUItem.rawIngot, 1, 19))),
                new RecipeOutput(null, new ItemStack(IUItem.iuingot, 1, 22))
        ));
        Recipes.recipes.addRecipe("laser", new BaseMachineRecipe(
                new Input(input.getInput(new ItemStack(IUItem.rawIngot, 1, 20))),
                new RecipeOutput(null, new ItemStack(IUItem.iuingot, 1, 24))
        ));
        Recipes.recipes.addRecipe("laser", new BaseMachineRecipe(
                new Input(input.getInput(new ItemStack(IUItem.rawIngot, 1, 22))),
                new RecipeOutput(null, new ItemStack(IUItem.iuingot, 1, 25))
        ));
        Recipes.recipes.addRecipe("laser", new BaseMachineRecipe(
                new Input(input.getInput(new ItemStack(IUItem.rawIngot, 1, 23))),
                new RecipeOutput(null, new ItemStack(IUItem.iuingot, 1, 26))
        ));
        Recipes.recipes.addRecipe("laser", new BaseMachineRecipe(
                new Input(input.getInput(new ItemStack(IUItem.rawIngot, 1, 24))),
                new RecipeOutput(null, new ItemStack(IUItem.iuingot, 1, 27))
        ));
        for (int i = 25; i < 40; i++) {
            Recipes.recipes.addRecipe("laser", new BaseMachineRecipe(
                    new Input(input.getInput(new ItemStack(IUItem.rawIngot, 1, i))),
                    new RecipeOutput(null, new ItemStack(IUItem.iuingot, 1, i+3))
            ));
        }
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
            if (this.input_slot.isEmpty()) {
                (this).inputSlotA.changeAccepts(ItemStack.EMPTY);
            } else {
                (this).inputSlotA.changeAccepts(this.input_slot.get());
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
