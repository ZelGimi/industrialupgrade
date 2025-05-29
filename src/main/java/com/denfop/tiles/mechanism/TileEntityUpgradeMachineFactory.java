package com.denfop.tiles.mechanism;

import com.denfop.IUItem;
import com.denfop.Localization;
import com.denfop.api.Recipes;
import com.denfop.api.crafting.BaseRecipe;
import com.denfop.api.inv.IAdvInventory;
import com.denfop.api.recipe.*;
import com.denfop.api.tile.IMultiTileBlock;
import com.denfop.api.upgrades.IUpgradableBlock;
import com.denfop.api.upgrades.UpgradableProperty;
import com.denfop.blocks.BlockTileEntity;
import com.denfop.blocks.mechanism.BlockBaseMachine3;
import com.denfop.componets.*;
import com.denfop.container.ContainerBase;
import com.denfop.container.ContainerUpgradeMachineFactory;
import com.denfop.gui.GuiCore;
import com.denfop.gui.GuiUpgradeMachineFactory;
import com.denfop.invslot.InvSlotUpgrade;
import com.denfop.network.IUpdatableTileEvent;
import com.denfop.recipe.IInputItemStack;
import com.denfop.recipe.InputItemStack;
import com.denfop.recipes.ItemStackHelper;
import com.denfop.tiles.base.TileElectricMachine;
import com.denfop.utils.Keyboard;
import com.denfop.utils.ModUtils;
import net.minecraft.core.BlockPos;
import net.minecraft.core.NonNullList;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

import java.util.ArrayList;
import java.util.EnumSet;
import java.util.List;
import java.util.Set;

import static com.denfop.recipes.BasicRecipeTwo.getBlockStack;

public class TileEntityUpgradeMachineFactory extends TileElectricMachine implements
        IUpgradableBlock, IUpdateTick, IUpdatableTileEvent, IHasRecipe {

    public final InvSlotUpgrade upgradeSlot;
    public final ComponentUpgradeSlots componentUpgrade;
    public final ComponentProgress componentProgress;
    public final ComponentProcess componentProcess;
    public final InvSlotRecipes inputSlotA;
    public final ComponentUpgrade componentUpgrades;
    public MachineRecipe output;

    public TileEntityUpgradeMachineFactory(BlockPos pos, BlockState state) {
        super(400, 1, 1, BlockBaseMachine3.upgrade_machine, pos, state);
        Recipes.recipes.addInitRecipes(this);
        this.upgradeSlot = new com.denfop.invslot.InvSlotUpgrade(this, 4);
        this.componentUpgrade = this.addComponent(new ComponentUpgradeSlots(this, upgradeSlot));
        this.componentProgress = this.addComponent(new ComponentProgress(this, 1,
                (short) 200
        ));
        this.inputSlotA = new InvSlotRecipes(this, "upgrade_machine", this);
        inputSlotA.setStackSizeLimit(1);
        this.componentProcess = this.addComponent(new ComponentProcess(this, 200, 1));
        this.componentProcess.setHasAudio(false);
        this.componentProcess.setSlotOutput(outputSlot);
        this.componentProcess.setInvSlotRecipes(this.inputSlotA);

        this.componentUpgrades = this.addComponent(new ComponentUpgrade(this, TypeUpgrade.INSTANT));
        this.pollutionSoil = this.addComponent(new SoilPollutionComponent(this, 0.1));
        this.pollutionAir = this.addComponent(new AirPollutionComponent(this, 0.1));

    }

    public static void addRecipe(BaseRecipe baseRecipe, int upgrade) {
        try {


            final NonNullList<IInputItemStack> ingredients = baseRecipe.getListInput();
            final ItemStack outputStack = baseRecipe.getOutput();
            ItemStack input_mechanism = ingredients.get(4).getInputs().get(0);
            List<IInputItemStack> inputs = new ArrayList<>();
            ItemStack upgradeStack = new ItemStack(IUItem.machinekit.getStack(upgrade), 1);
            for (IInputItemStack ingredient : ingredients) {
                if (ingredient == InputItemStack.EMPTY) {
                    inputs.add(InputItemStack.EMPTY);
                    continue;
                }
                final ItemStack input = ingredient.getInputs().get(0);
                if (input.is(input_mechanism.getItem())) {
                    inputs.add(new InputItemStack(upgradeStack.copy()));
                    continue;
                }
                inputs.add(ingredient);
            }

            CompoundTag tagCompound = new CompoundTag();
            outputStack.save(tagCompound);
            CompoundTag tagCompound2 = new CompoundTag();
            input_mechanism.save(tagCompound2);
            CompoundTag tagCompound1 = ModUtils.nbt(upgradeStack);
            tagCompound1.put("output", tagCompound);
            tagCompound1.put("input", tagCompound2);
            Recipes.recipes.addRecipe(
                    "upgrade_machine",
                    new BaseMachineRecipe(
                            new Input(inputs),
                            new RecipeOutput(null, upgradeStack)
                    )
            );
        } catch (Exception e) {

        }
    }

    @Override
    public void init() {
        TileEntityUpgradeMachineFactory.addRecipe(Recipes.recipe.addRecipe(
                ItemStackHelper.fromData(IUItem.machines, 1, 1),
                "DED",
                "BCB",
                "AAA",

                ('E'),
                ItemStackHelper.fromData(IUItem.core, 1, 5),

                ('D'),
                ("forge:doubleplate/Alumel"),
                ('B'),
                TileGenerationMicrochip.getLevelCircuit(IUItem.cirsuitQuantum, 7),

                ('C'),
                ItemStackHelper.fromData(IUItem.simplemachine, 1, 6),

                ('A'),
                ItemStackHelper.fromData(IUItem.quantumtool)
        ), 0);
        TileEntityUpgradeMachineFactory.addRecipe(Recipes.recipe.addRecipe(
                ItemStackHelper.fromData(IUItem.machines, 1, 2),
                "DED",
                "BCB",
                "AAA",

                ('E'),
                ItemStackHelper.fromData(IUItem.core, 1, 7),

                ('D'),
                ("forge:doubleplate/Vitalium"),
                ('B'),
                TileGenerationMicrochip.getLevelCircuit(IUItem.cirsuitQuantum, 7),

                ('C'),
                ItemStackHelper.fromData(IUItem.machines, 1, 1),

                ('A'),
                ItemStackHelper.fromData(IUItem.advQuantumtool)
        ), 1);
        TileEntityUpgradeMachineFactory.addRecipe(Recipes.recipe.addRecipe(
                ItemStackHelper.fromData(IUItem.machines, 1, 3),
                "DED",
                "BCB",
                "AFA",

                ('F'),
                ItemStackHelper.fromData(IUItem.doublecompressIridiumplate),

                ('E'),
                ItemStackHelper.fromData(IUItem.core, 1, 8),

                ('D'),
                ("forge:doubleplate/Duralumin"),
                ('B'),
                TileGenerationMicrochip.getLevelCircuit(IUItem.circuitSpectral, 9),

                ('C'),
                ItemStackHelper.fromData(IUItem.machines, 1, 2),

                ('A'),
                ItemStackHelper.fromData(IUItem.advQuantumtool)
        ), 2);
        TileEntityUpgradeMachineFactory.addRecipe(Recipes.recipe.addRecipe(ItemStackHelper.fromData(IUItem.machines_base, 1, 0), "A A",
                "DBE", "ACA",

                ('A'), ItemStackHelper.fromData(IUItem.crafting_elements, 1, 138),
                ('C'), ItemStackHelper.fromData(IUItem.crafting_elements, 1, 20),
                ('D'), ItemStackHelper.fromData(IUItem.crafting_elements, 1, 47),
                ('E'),
                ItemStackHelper.fromData(IUItem.crafting_elements, 1, 2),
                ('B'), ItemStackHelper.fromData(IUItem.simplemachine, 1, 0)
        ), 0);
        TileEntityUpgradeMachineFactory.addRecipe(Recipes.recipe.addRecipe(
                ItemStackHelper.fromData(IUItem.machines_base, 1, 1),
                "A A",
                "DBE",
                "ACA",

                ('A'),
                ItemStackHelper.fromData(IUItem.crafting_elements, 1, 139),
                ('C'),
                ItemStackHelper.fromData(IUItem.crafting_elements, 1, 96),
                ('D'),
                ItemStackHelper.fromData(IUItem.crafting_elements, 1, 49),
                ('E'),
                ItemStackHelper.fromData(IUItem.crafting_elements, 1, 78),
                ('B'),
                ItemStackHelper.fromData(IUItem.machines_base, 1, 0)
        ), 1);
        TileEntityUpgradeMachineFactory.addRecipe(Recipes.recipe.addRecipe(
                ItemStackHelper.fromData(IUItem.machines_base, 1, 2),
                "A A",
                "DBE",
                "ACA",

                ('A'),
                ItemStackHelper.fromData(IUItem.crafting_elements, 1, 140),
                ('C'),
                ItemStackHelper.fromData(IUItem.crafting_elements, 1, 120),
                ('D'),
                ItemStackHelper.fromData(IUItem.crafting_elements, 1, 51),
                ('E'),
                ItemStackHelper.fromData(IUItem.crafting_elements, 1, 103),
                ('B'),
                ItemStackHelper.fromData(IUItem.machines_base, 1, 1)
        ), 2);
        TileEntityUpgradeMachineFactory.addRecipe(Recipes.recipe.addRecipe(
                ItemStackHelper.fromData(IUItem.machines_base, 1, 6),
                "A A",
                "DBE",
                "ACA",

                ('A'),
                ItemStackHelper.fromData(IUItem.crafting_elements, 1, 138),
                ('C'),
                ItemStackHelper.fromData(IUItem.crafting_elements, 1, 20),
                ('D'),
                ItemStackHelper.fromData(IUItem.crafting_elements, 1, 47),
                ('E'),
                ItemStackHelper.fromData(IUItem.crafting_elements, 1, 225),
                ('B'),
                ItemStackHelper.fromData(IUItem.simplemachine, 1, 2)
        ), 0);
        TileEntityUpgradeMachineFactory.addRecipe(Recipes.recipe.addRecipe(
                ItemStackHelper.fromData(IUItem.machines_base, 1, 7),
                "A A",
                "DBE",
                "ACA",

                ('A'),
                ItemStackHelper.fromData(IUItem.crafting_elements, 1, 139),
                ('C'),
                ItemStackHelper.fromData(IUItem.crafting_elements, 1, 96),
                ('D'),
                ItemStackHelper.fromData(IUItem.crafting_elements, 1, 49),
                ('E'),
                ItemStackHelper.fromData(IUItem.crafting_elements, 1, 221),
                ('B'),
                ItemStackHelper.fromData(IUItem.machines_base, 1, 6)
        ), 1);
        TileEntityUpgradeMachineFactory.addRecipe(Recipes.recipe.addRecipe(
                ItemStackHelper.fromData(IUItem.machines_base, 1, 8),
                "A A",
                "DBE",
                "ACA",

                ('A'),
                ItemStackHelper.fromData(IUItem.crafting_elements, 1, 140),
                ('C'),
                ItemStackHelper.fromData(IUItem.crafting_elements, 1, 120),
                ('D'),
                ItemStackHelper.fromData(IUItem.crafting_elements, 1, 51),
                ('E'),
                ItemStackHelper.fromData(IUItem.crafting_elements, 1, 223),
                ('B'),
                ItemStackHelper.fromData(IUItem.machines_base, 1, 7)
        ), 2);
        TileEntityUpgradeMachineFactory.addRecipe(Recipes.recipe.addRecipe(
                ItemStackHelper.fromData(IUItem.machines_base, 1, 9),
                "A A",
                "DBE",
                "ACA",

                ('A'),
                ItemStackHelper.fromData(IUItem.crafting_elements, 1, 138),
                ('C'),
                ItemStackHelper.fromData(IUItem.crafting_elements, 1, 20),
                ('D'),
                ItemStackHelper.fromData(IUItem.crafting_elements, 1, 47),
                ('E'),
                ItemStackHelper.fromData(IUItem.crafting_elements, 1, 160),
                ('B'),
                ItemStackHelper.fromData(IUItem.simplemachine, 1, 3)
        ), 0);
        TileEntityUpgradeMachineFactory.addRecipe(Recipes.recipe.addRecipe(
                ItemStackHelper.fromData(IUItem.machines_base, 1, 10),
                "A A",
                "DBE",
                "ACA",

                ('A'),
                ItemStackHelper.fromData(IUItem.crafting_elements, 1, 139),
                ('C'),
                ItemStackHelper.fromData(IUItem.crafting_elements, 1, 96),
                ('D'),
                ItemStackHelper.fromData(IUItem.crafting_elements, 1, 49),
                ('E'),
                ItemStackHelper.fromData(IUItem.crafting_elements, 1, 161),
                ('B'),
                ItemStackHelper.fromData(IUItem.machines_base, 1, 9)
        ), 1);
        TileEntityUpgradeMachineFactory.addRecipe(Recipes.recipe.addRecipe(
                ItemStackHelper.fromData(IUItem.machines_base, 1, 11),
                "A A",
                "DBE",
                "ACA",

                ('A'),
                ItemStackHelper.fromData(IUItem.crafting_elements, 1, 140),
                ('C'),
                ItemStackHelper.fromData(IUItem.crafting_elements, 1, 120),
                ('D'),
                ItemStackHelper.fromData(IUItem.crafting_elements, 1, 51),
                ('E'),
                ItemStackHelper.fromData(IUItem.crafting_elements, 1, 162),
                ('B'),
                ItemStackHelper.fromData(IUItem.machines_base, 1, 10)
        ), 2);
        TileEntityUpgradeMachineFactory.addRecipe(Recipes.recipe.addRecipe(
                ItemStackHelper.fromData(IUItem.machines_base, 1, 3),
                "A A",
                "DBE",
                "ACA",

                ('A'),
                ItemStackHelper.fromData(IUItem.crafting_elements, 1, 138),
                ('C'),
                ItemStackHelper.fromData(IUItem.crafting_elements, 1, 20),
                ('D'),
                ItemStackHelper.fromData(IUItem.crafting_elements, 1, 47),
                ('E'),
                ItemStackHelper.fromData(IUItem.crafting_elements, 1, 1),
                ('B'),
                ItemStackHelper.fromData(IUItem.simplemachine, 1, 1)
        ), 0);
        TileEntityUpgradeMachineFactory.addRecipe(Recipes.recipe.addRecipe(
                ItemStackHelper.fromData(IUItem.machines_base, 1, 4),
                "A A",
                "DBE",
                "ACA",

                ('A'),
                ItemStackHelper.fromData(IUItem.crafting_elements, 1, 139),
                ('C'),
                ItemStackHelper.fromData(IUItem.crafting_elements, 1, 96),
                ('D'),
                ItemStackHelper.fromData(IUItem.crafting_elements, 1, 49),
                ('E'),
                ItemStackHelper.fromData(IUItem.crafting_elements, 1, 77),
                ('B'),
                ItemStackHelper.fromData(IUItem.machines_base, 1, 3)
        ), 1);
        TileEntityUpgradeMachineFactory.addRecipe(Recipes.recipe.addRecipe(
                ItemStackHelper.fromData(IUItem.machines_base, 1, 5),
                "A A",
                "DBE",
                "ACA",

                ('A'),
                ItemStackHelper.fromData(IUItem.crafting_elements, 1, 140),
                ('C'),
                ItemStackHelper.fromData(IUItem.crafting_elements, 1, 120),
                ('D'),
                ItemStackHelper.fromData(IUItem.crafting_elements, 1, 51),
                ('E'),
                ItemStackHelper.fromData(IUItem.crafting_elements, 1, 102),
                ('B'),
                ItemStackHelper.fromData(IUItem.machines_base, 1, 4)
        ), 2);
        TileEntityUpgradeMachineFactory.addRecipe(Recipes.recipe.addRecipe(
                ItemStackHelper.fromData(IUItem.machines_base1, 1, 0),
                "A A",
                "DBE",
                "ACA",

                ('A'),
                ItemStackHelper.fromData(IUItem.crafting_elements, 1, 138),
                ('C'),
                ItemStackHelper.fromData(IUItem.crafting_elements, 1, 20),
                ('D'),
                ItemStackHelper.fromData(IUItem.crafting_elements, 1, 47),
                ('E'),
                ItemStackHelper.fromData(IUItem.crafting_elements, 1, 9),
                ('B'),
                ItemStackHelper.fromData(IUItem.simplemachine, 1, 5)
        ), 0);
        TileEntityUpgradeMachineFactory.addRecipe(Recipes.recipe.addRecipe(
                ItemStackHelper.fromData(IUItem.machines_base1, 1, 1),
                "A A",
                "DBE",
                "ACA",

                ('A'),
                ItemStackHelper.fromData(IUItem.crafting_elements, 1, 139),
                ('C'),
                ItemStackHelper.fromData(IUItem.crafting_elements, 1, 96),
                ('D'),
                ItemStackHelper.fromData(IUItem.crafting_elements, 1, 49),
                ('E'),
                ItemStackHelper.fromData(IUItem.crafting_elements, 1, 86),
                ('B'),
                ItemStackHelper.fromData(IUItem.machines_base1, 1, 0)
        ), 1);
        TileEntityUpgradeMachineFactory.addRecipe(Recipes.recipe.addRecipe(
                ItemStackHelper.fromData(IUItem.machines_base1, 1, 2),
                "A A",
                "DBE",
                "ACA",

                ('A'),
                ItemStackHelper.fromData(IUItem.crafting_elements, 1, 140),
                ('C'),
                ItemStackHelper.fromData(IUItem.crafting_elements, 1, 120),
                ('D'),
                ItemStackHelper.fromData(IUItem.crafting_elements, 1, 51),
                ('E'),
                ItemStackHelper.fromData(IUItem.crafting_elements, 1, 110),
                ('B'),
                ItemStackHelper.fromData(IUItem.machines_base1, 1, 1)
        ), 2);
        TileEntityUpgradeMachineFactory.addRecipe(Recipes.recipe.addRecipe(
                ItemStackHelper.fromData(IUItem.machines_base2, 1, 1),
                "A A",
                "DBE",
                "ACA",

                ('A'),
                ItemStackHelper.fromData(IUItem.crafting_elements, 1, 138),
                ('C'),
                ItemStackHelper.fromData(IUItem.crafting_elements, 1, 20),
                ('D'),
                ItemStackHelper.fromData(IUItem.crafting_elements, 1, 47),
                ('E'),
                ItemStackHelper.fromData(IUItem.crafting_elements, 1, 166),
                ('B'),
                ItemStackHelper.fromData(IUItem.machines_base2, 1, 0)
        ), 0);
        TileEntityUpgradeMachineFactory.addRecipe(Recipes.recipe.addRecipe(
                ItemStackHelper.fromData(IUItem.machines_base2, 1, 2),
                "A A",
                "DBE",
                "ACA",

                ('A'),
                ItemStackHelper.fromData(IUItem.crafting_elements, 1, 139),
                ('C'),
                ItemStackHelper.fromData(IUItem.crafting_elements, 1, 96),
                ('D'),
                ItemStackHelper.fromData(IUItem.crafting_elements, 1, 49),
                ('E'),
                ItemStackHelper.fromData(IUItem.crafting_elements, 1, 167),
                ('B'),
                ItemStackHelper.fromData(IUItem.machines_base2, 1, 1)
        ), 1);
        TileEntityUpgradeMachineFactory.addRecipe(Recipes.recipe.addRecipe(
                ItemStackHelper.fromData(IUItem.machines_base2, 1, 3),
                "A A",
                "DBE",
                "ACA",

                ('A'),
                ItemStackHelper.fromData(IUItem.crafting_elements, 1, 140),
                ('C'),
                ItemStackHelper.fromData(IUItem.crafting_elements, 1, 120),
                ('D'),
                ItemStackHelper.fromData(IUItem.crafting_elements, 1, 51),
                ('E'),
                ItemStackHelper.fromData(IUItem.crafting_elements, 1, 168),
                ('B'),
                ItemStackHelper.fromData(IUItem.machines_base2, 1, 2)
        ), 2);
        TileEntityUpgradeMachineFactory.addRecipe(Recipes.recipe.addRecipe(
                ItemStackHelper.fromData(IUItem.machines_base2, 1, 5),
                "A A",
                "DBE",
                "ACA",

                ('A'),
                ItemStackHelper.fromData(IUItem.crafting_elements, 1, 138),
                ('C'),
                ItemStackHelper.fromData(IUItem.crafting_elements, 1, 20),
                ('D'),
                ItemStackHelper.fromData(IUItem.crafting_elements, 1, 47),
                ('E'),
                ItemStackHelper.fromData(IUItem.crafting_elements, 1, 125),
                ('B'),
                ItemStackHelper.fromData(IUItem.machines_base2, 1, 4)
        ), 0);
        TileEntityUpgradeMachineFactory.addRecipe(Recipes.recipe.addRecipe(
                ItemStackHelper.fromData(IUItem.machines_base2, 1, 6),
                "A A",
                "DBE",
                "ACA",

                ('A'),
                ItemStackHelper.fromData(IUItem.crafting_elements, 1, 139),
                ('C'),
                ItemStackHelper.fromData(IUItem.crafting_elements, 1, 96),
                ('D'),
                ItemStackHelper.fromData(IUItem.crafting_elements, 1, 49),
                ('E'),
                ItemStackHelper.fromData(IUItem.crafting_elements, 1, 126),
                ('B'),
                ItemStackHelper.fromData(IUItem.machines_base2, 1, 5)
        ), 1);
        TileEntityUpgradeMachineFactory.addRecipe(Recipes.recipe.addRecipe(
                ItemStackHelper.fromData(IUItem.machines_base2, 1, 7),
                "A A",
                "DBE",
                "ACA",

                ('A'),
                ItemStackHelper.fromData(IUItem.crafting_elements, 1, 140),
                ('C'),
                ItemStackHelper.fromData(IUItem.crafting_elements, 1, 120),
                ('D'),
                ItemStackHelper.fromData(IUItem.crafting_elements, 1, 51),
                ('E'),
                ItemStackHelper.fromData(IUItem.crafting_elements, 1, 127),
                ('B'),
                ItemStackHelper.fromData(IUItem.machines_base2, 1, 6)
        ), 2);
        TileEntityUpgradeMachineFactory.addRecipe(Recipes.recipe.addRecipe(
                ItemStackHelper.fromData(IUItem.machines_base2, 1, 9),
                "A A",
                "DBE",
                "ACA",

                ('A'),
                ItemStackHelper.fromData(IUItem.crafting_elements, 1, 138),
                ('C'),
                ItemStackHelper.fromData(IUItem.crafting_elements, 1, 20),
                ('D'),
                ItemStackHelper.fromData(IUItem.crafting_elements, 1, 47),
                ('E'),
                ItemStackHelper.fromData(IUItem.crafting_elements, 1, 133),
                ('B'),
                ItemStackHelper.fromData(IUItem.machines_base2, 1, 8)
        ), 0);
        TileEntityUpgradeMachineFactory.addRecipe(Recipes.recipe.addRecipe(
                ItemStackHelper.fromData(IUItem.machines_base2, 1, 10),
                "A A",
                "DBE",
                "ACA",

                ('A'),
                ItemStackHelper.fromData(IUItem.crafting_elements, 1, 139),
                ('C'),
                ItemStackHelper.fromData(IUItem.crafting_elements, 1, 96),
                ('D'),
                ItemStackHelper.fromData(IUItem.crafting_elements, 1, 49),
                ('E'),
                ItemStackHelper.fromData(IUItem.crafting_elements, 1, 134),
                ('B'),
                ItemStackHelper.fromData(IUItem.machines_base2, 1, 9)
        ), 1);
        TileEntityUpgradeMachineFactory.addRecipe(Recipes.recipe.addRecipe(
                ItemStackHelper.fromData(IUItem.machines_base2, 1, 11),
                "A A",
                "DBE",
                "ACA",

                ('A'),
                ItemStackHelper.fromData(IUItem.crafting_elements, 1, 140),
                ('C'),
                ItemStackHelper.fromData(IUItem.crafting_elements, 1, 120),
                ('D'),
                ItemStackHelper.fromData(IUItem.crafting_elements, 1, 51),
                ('E'),
                ItemStackHelper.fromData(IUItem.crafting_elements, 1, 136),
                ('B'),
                ItemStackHelper.fromData(IUItem.machines_base2, 1, 10)
        ), 2);
        TileEntityUpgradeMachineFactory.addRecipe(Recipes.recipe.addRecipe(
                ItemStackHelper.fromData(IUItem.machines_base3, 1, 17),
                "A A",
                "DBE",
                "ACA",

                ('A'),
                ItemStackHelper.fromData(IUItem.crafting_elements, 1, 138),
                ('C'),
                ItemStackHelper.fromData(IUItem.crafting_elements, 1, 20),
                ('D'),
                ItemStackHelper.fromData(IUItem.crafting_elements, 1, 47),
                ('E'),
                ItemStackHelper.fromData(IUItem.crafting_elements, 1, 224),
                ('B'),
                ItemStackHelper.fromData(IUItem.machines_base3, 1, 16)
        ), 0);
        TileEntityUpgradeMachineFactory.addRecipe(Recipes.recipe.addRecipe(
                ItemStackHelper.fromData(IUItem.machines_base3, 1, 18),
                "A A",
                "DBE",
                "ACA",

                ('A'),
                ItemStackHelper.fromData(IUItem.crafting_elements, 1, 139),
                ('C'),
                ItemStackHelper.fromData(IUItem.crafting_elements, 1, 96),
                ('D'),
                ItemStackHelper.fromData(IUItem.crafting_elements, 1, 49),
                ('E'),
                ItemStackHelper.fromData(IUItem.crafting_elements, 1, 220),
                ('B'),
                ItemStackHelper.fromData(IUItem.machines_base3, 1, 17)
        ), 1);
        TileEntityUpgradeMachineFactory.addRecipe(Recipes.recipe.addRecipe(
                ItemStackHelper.fromData(IUItem.machines_base3, 1, 19),
                "A A",
                "DBE",
                "ACA",

                ('A'),
                ItemStackHelper.fromData(IUItem.crafting_elements, 1, 140),
                ('C'),
                ItemStackHelper.fromData(IUItem.crafting_elements, 1, 120),
                ('D'),
                ItemStackHelper.fromData(IUItem.crafting_elements, 1, 51),
                ('E'),
                ItemStackHelper.fromData(IUItem.crafting_elements, 1, 222),
                ('B'),
                ItemStackHelper.fromData(IUItem.machines_base3, 1, 18)
        ), 2);
        TileEntityUpgradeMachineFactory.addRecipe(Recipes.recipe.addRecipe(
                ItemStackHelper.fromData(IUItem.machines_base3, 1, 1),
                "AGA",
                "DBE",
                "ACA",

                ('A'),
                ItemStackHelper.fromData(IUItem.crafting_elements, 1, 138),
                ('C'),
                ItemStackHelper.fromData(IUItem.crafting_elements, 1, 20),
                ('D'),
                ItemStackHelper.fromData(IUItem.crafting_elements, 1, 47),
                ('E'),
                ItemStackHelper.fromData(IUItem.crafting_elements, 1, 19),
                ('B'),
                ItemStackHelper.fromData(IUItem.machines_base3, 1, 0),
                ('G'),
                ItemStackHelper.fromData(IUItem.crafting_elements, 1, 18)
        ), 0);
        TileEntityUpgradeMachineFactory.addRecipe(Recipes.recipe.addRecipe(
                ItemStackHelper.fromData(IUItem.machines_base3, 1, 2),
                "AGA",
                "DBE",
                "ACA",

                ('A'),
                ItemStackHelper.fromData(IUItem.crafting_elements, 1, 139),
                ('C'),
                ItemStackHelper.fromData(IUItem.crafting_elements, 1, 96),
                ('D'),
                ItemStackHelper.fromData(IUItem.crafting_elements, 1, 49),
                ('E'),
                ItemStackHelper.fromData(IUItem.crafting_elements, 1, 95),
                ('B'),
                ItemStackHelper.fromData(IUItem.machines_base3, 1, 1),
                ('G'),
                ItemStackHelper.fromData(IUItem.crafting_elements, 1, 94)
        ), 1);
        TileEntityUpgradeMachineFactory.addRecipe(Recipes.recipe.addRecipe(
                ItemStackHelper.fromData(IUItem.machines_base3, 1, 3),
                "AGA",
                "DBE",
                "ACA",

                ('A'),
                ItemStackHelper.fromData(IUItem.crafting_elements, 1, 140),
                ('C'),
                ItemStackHelper.fromData(IUItem.crafting_elements, 1, 120),
                ('D'),
                ItemStackHelper.fromData(IUItem.crafting_elements, 1, 51),
                ('E'),
                ItemStackHelper.fromData(IUItem.crafting_elements, 1, 119),
                ('B'),
                ItemStackHelper.fromData(IUItem.machines_base3, 1, 2),
                ('G'),
                ItemStackHelper.fromData(IUItem.crafting_elements, 1, 118)
        ), 2);
        TileEntityUpgradeMachineFactory.addRecipe(Recipes.recipe.addRecipe(
                ItemStackHelper.fromData(IUItem.machines_base3, 1, 5),
                "AGA",
                "DBE",
                "ACA",

                ('A'),
                ItemStackHelper.fromData(IUItem.crafting_elements, 1, 138),
                ('C'),
                ItemStackHelper.fromData(IUItem.crafting_elements, 1, 20),
                ('D'),
                ItemStackHelper.fromData(IUItem.crafting_elements, 1, 47),
                ('E'),
                ItemStackHelper.fromData(IUItem.crafting_elements, 1, 9),
                ('B'),
                ItemStackHelper.fromData(IUItem.machines_base3, 1, 4),
                ('G'),
                ItemStackHelper.fromData(IUItem.crafting_elements, 1, 129)
        ), 0);
        TileEntityUpgradeMachineFactory.addRecipe(Recipes.recipe.addRecipe(
                ItemStackHelper.fromData(IUItem.machines_base3, 1, 6),
                "AGA",
                "DBE",
                "ACA",

                ('A'),
                ItemStackHelper.fromData(IUItem.crafting_elements, 1, 139),
                ('C'),
                ItemStackHelper.fromData(IUItem.crafting_elements, 1, 96),
                ('D'),
                ItemStackHelper.fromData(IUItem.crafting_elements, 1, 49),
                ('E'),
                ItemStackHelper.fromData(IUItem.crafting_elements, 1, 86),
                ('B'),
                ItemStackHelper.fromData(IUItem.machines_base3, 1, 5),
                ('G'),
                ItemStackHelper.fromData(IUItem.crafting_elements, 1, 130)
        ), 1);
        TileEntityUpgradeMachineFactory.addRecipe(Recipes.recipe.addRecipe(
                ItemStackHelper.fromData(IUItem.machines_base3, 1, 7),
                "AGA",
                "DBE",
                "ACA",

                ('A'),
                ItemStackHelper.fromData(IUItem.crafting_elements, 1, 140),
                ('C'),
                ItemStackHelper.fromData(IUItem.crafting_elements, 1, 120),
                ('D'),
                ItemStackHelper.fromData(IUItem.crafting_elements, 1, 51),
                ('E'),
                ItemStackHelper.fromData(IUItem.crafting_elements, 1, 110),
                ('B'),
                ItemStackHelper.fromData(IUItem.machines_base3, 1, 6),
                ('G'),
                ItemStackHelper.fromData(IUItem.crafting_elements, 1, 131)
        ), 2);
        TileEntityUpgradeMachineFactory.addRecipe(Recipes.recipe.addRecipe(
                ItemStackHelper.fromData(IUItem.machines_base3, 1, 13),
                "A A",
                "DBE",
                "ACA",

                ('A'),
                ItemStackHelper.fromData(IUItem.crafting_elements, 1, 138),
                ('C'),
                ItemStackHelper.fromData(IUItem.crafting_elements, 1, 20),
                ('D'),
                ItemStackHelper.fromData(IUItem.crafting_elements, 1, 47),
                ('E'),
                ItemStackHelper.fromData(IUItem.crafting_elements, 1, 15),
                ('B'),
                ItemStackHelper.fromData(IUItem.machines_base3, 1, 12)
        ), 0);
        TileEntityUpgradeMachineFactory.addRecipe(Recipes.recipe.addRecipe(
                ItemStackHelper.fromData(IUItem.machines_base3, 1, 14),
                "A A",
                "DBE",
                "ACA",

                ('A'),
                ItemStackHelper.fromData(IUItem.crafting_elements, 1, 139),
                ('C'),
                ItemStackHelper.fromData(IUItem.crafting_elements, 1, 96),
                ('D'),
                ItemStackHelper.fromData(IUItem.crafting_elements, 1, 49),
                ('E'),
                ItemStackHelper.fromData(IUItem.crafting_elements, 1, 91),
                ('B'),
                ItemStackHelper.fromData(IUItem.machines_base3, 1, 13)
        ), 1);
        TileEntityUpgradeMachineFactory.addRecipe(Recipes.recipe.addRecipe(
                ItemStackHelper.fromData(IUItem.machines_base3, 1, 15),
                "A A",
                "DBE",
                "ACA",

                ('A'),
                ItemStackHelper.fromData(IUItem.crafting_elements, 1, 140),
                ('C'),
                ItemStackHelper.fromData(IUItem.crafting_elements, 1, 120),
                ('D'),
                ItemStackHelper.fromData(IUItem.crafting_elements, 1, 51),
                ('E'),
                ItemStackHelper.fromData(IUItem.crafting_elements, 1, 115),
                ('B'),
                ItemStackHelper.fromData(IUItem.machines_base3, 1, 14)
        ), 2);
        TileEntityUpgradeMachineFactory.addRecipe(Recipes.recipe.addRecipe(
                ItemStackHelper.fromData(IUItem.machines_base3, 1, 9),
                "AGA",
                "DBE",
                "ACA",

                ('A'),
                ItemStackHelper.fromData(IUItem.crafting_elements, 1, 138),
                ('C'),
                ItemStackHelper.fromData(IUItem.crafting_elements, 1, 20),
                ('D'),
                ItemStackHelper.fromData(IUItem.crafting_elements, 1, 47),
                ('E'),
                ItemStackHelper.fromData(IUItem.crafting_elements, 1, 12),
                ('B'),
                ItemStackHelper.fromData(IUItem.machines_base3, 1, 8),
                ('G'),
                ItemStackHelper.fromData(IUItem.crafting_elements, 1, 6)
        ), 0);
        TileEntityUpgradeMachineFactory.addRecipe(Recipes.recipe.addRecipe(
                ItemStackHelper.fromData(IUItem.machines_base3, 1, 10),
                "AGA",
                "DBE",
                "ACA",

                ('A'),
                ItemStackHelper.fromData(IUItem.crafting_elements, 1, 139),
                ('C'),
                ItemStackHelper.fromData(IUItem.crafting_elements, 1, 96),
                ('D'),
                ItemStackHelper.fromData(IUItem.crafting_elements, 1, 49),
                ('E'),
                ItemStackHelper.fromData(IUItem.crafting_elements, 1, 87),
                ('B'),
                ItemStackHelper.fromData(IUItem.machines_base3, 1, 9),
                ('G'),
                ItemStackHelper.fromData(IUItem.crafting_elements, 1, 83)
        ), 1);
        TileEntityUpgradeMachineFactory.addRecipe(Recipes.recipe.addRecipe(
                ItemStackHelper.fromData(IUItem.machines_base3, 1, 11),
                "AGA",
                "DBE",
                "ACA",

                ('A'),
                ItemStackHelper.fromData(IUItem.crafting_elements, 1, 140),
                ('C'),
                ItemStackHelper.fromData(IUItem.crafting_elements, 1, 120),
                ('D'),
                ItemStackHelper.fromData(IUItem.crafting_elements, 1, 51),
                ('E'),
                ItemStackHelper.fromData(IUItem.crafting_elements, 1, 111),
                ('B'),
                ItemStackHelper.fromData(IUItem.machines_base3, 1, 10),
                ('G'),
                ItemStackHelper.fromData(IUItem.crafting_elements, 1, 107)
        ), 2);
        TileEntityUpgradeMachineFactory.addRecipe(Recipes.recipe.addRecipe(
                ItemStackHelper.fromData(IUItem.machines_base1, 1, 3),
                "A A",
                "DBE",
                "ACA",

                ('A'),
                ItemStackHelper.fromData(IUItem.crafting_elements, 1, 138),
                ('C'),
                ItemStackHelper.fromData(IUItem.crafting_elements, 1, 20),
                ('D'),
                ItemStackHelper.fromData(IUItem.crafting_elements, 1, 47),
                ('E'),
                ItemStackHelper.fromData(IUItem.crafting_elements, 1, 135),
                ('B'),
                ItemStackHelper.fromData(IUItem.machines_base1, 1, 2)
        ), 0);
        TileEntityUpgradeMachineFactory.addRecipe(Recipes.recipe.addRecipe(
                ItemStackHelper.fromData(IUItem.machines_base1, 1, 4),
                "A A",
                "DBE",
                "ACA",

                ('A'),
                ItemStackHelper.fromData(IUItem.crafting_elements, 1, 139),
                ('C'),
                ItemStackHelper.fromData(IUItem.crafting_elements, 1, 96),
                ('D'),
                ItemStackHelper.fromData(IUItem.crafting_elements, 1, 49),
                ('E'),
                ItemStackHelper.fromData(IUItem.crafting_elements, 1, 146),
                ('B'),
                ItemStackHelper.fromData(IUItem.machines_base1, 1, 3)
        ), 1);
        TileEntityUpgradeMachineFactory.addRecipe(Recipes.recipe.addRecipe(
                ItemStackHelper.fromData(IUItem.machines_base1, 1, 5),
                "A A",
                "DBE",
                "ACA",

                ('A'),
                ItemStackHelper.fromData(IUItem.crafting_elements, 1, 140),
                ('C'),
                ItemStackHelper.fromData(IUItem.crafting_elements, 1, 120),
                ('D'),
                ItemStackHelper.fromData(IUItem.crafting_elements, 1, 51),
                ('E'),
                ItemStackHelper.fromData(IUItem.crafting_elements, 1, 157),
                ('B'),
                ItemStackHelper.fromData(IUItem.machines_base1, 1, 4)
        ), 2);
        TileEntityUpgradeMachineFactory.addRecipe(Recipes.recipe.addRecipe(
                ItemStackHelper.fromData(IUItem.machines_base1, 1, 7),
                "A A",
                "DBE",
                "ACA",

                ('A'),
                ItemStackHelper.fromData(IUItem.crafting_elements, 1, 138),
                ('C'),
                ItemStackHelper.fromData(IUItem.crafting_elements, 1, 20),
                ('D'),
                ItemStackHelper.fromData(IUItem.crafting_elements, 1, 47),
                ('E'),
                ItemStackHelper.fromData(IUItem.crafting_elements, 1, 135),
                ('B'),
                ItemStackHelper.fromData(IUItem.machines_base1, 1, 6)
        ), 0);
        TileEntityUpgradeMachineFactory.addRecipe(Recipes.recipe.addRecipe(
                ItemStackHelper.fromData(IUItem.machines_base1, 1, 8),
                "A A",
                "DBE",
                "ACA",

                ('A'),
                ItemStackHelper.fromData(IUItem.crafting_elements, 1, 139),
                ('C'),
                ItemStackHelper.fromData(IUItem.crafting_elements, 1, 96),
                ('D'),
                ItemStackHelper.fromData(IUItem.crafting_elements, 1, 49),
                ('E'),
                ItemStackHelper.fromData(IUItem.crafting_elements, 1, 146),
                ('B'),
                ItemStackHelper.fromData(IUItem.machines_base1, 1, 7)
        ), 1);
        TileEntityUpgradeMachineFactory.addRecipe(Recipes.recipe.addRecipe(
                ItemStackHelper.fromData(IUItem.machines_base1, 1, 9),
                "A A",
                "DBE",
                "ACA",

                ('A'),
                ItemStackHelper.fromData(IUItem.crafting_elements, 1, 140),
                ('C'),
                ItemStackHelper.fromData(IUItem.crafting_elements, 1, 120),
                ('D'),
                ItemStackHelper.fromData(IUItem.crafting_elements, 1, 51),
                ('E'),
                ItemStackHelper.fromData(IUItem.crafting_elements, 1, 157),
                ('B'),
                ItemStackHelper.fromData(IUItem.machines_base1, 1, 8)
        ), 2);
        TileEntityUpgradeMachineFactory.addRecipe(Recipes.recipe.addRecipe(
                ItemStackHelper.fromData(IUItem.basemachine1, 1, 6),
                "F F",
                "CAE",
                "FBF",

                ('A'),
                getBlockStack(BlockBaseMachine3.pump_iu),
                ('B'),
                ItemStackHelper.fromData(IUItem.crafting_elements, 1, 20),

                ('C'),
                ItemStackHelper.fromData(IUItem.crafting_elements, 1, 6),

                ('E'),
                ItemStackHelper.fromData(IUItem.crafting_elements, 1, 248),
                ('F'),
                ItemStackHelper.fromData(IUItem.crafting_elements, 1, 138)
        ), 0);
        TileEntityUpgradeMachineFactory.addRecipe(Recipes.recipe.addRecipe(
                ItemStackHelper.fromData(IUItem.oiladvrefiner),
                " A ",
                " B ",
                " C ",

                ('A'),
                ItemStackHelper.fromData(IUItem.crafting_elements, 1, 9),

                ('B'),
                IUItem.oilrefiner,

                ('C'),
                ItemStackHelper.fromData(IUItem.crafting_elements, 1, 20)
        ), 0);
        TileEntityUpgradeMachineFactory.addRecipe(Recipes.recipe.addRecipe(
                ItemStackHelper.fromData(IUItem.machines, 1, 10),
                "BAB",
                " D ",
                "B B",

                ('D'),
                getBlockStack(BlockBaseMachine3.generator_iu),
                ('A'),
                ItemStackHelper.fromData(IUItem.crafting_elements, 1, 234),

                ('B'),
                ItemStackHelper.fromData(IUItem.crafting_elements, 1, 138)
        ), 0);
        TileEntityUpgradeMachineFactory.addRecipe(Recipes.recipe.addRecipe(
                ItemStackHelper.fromData(IUItem.machines, 1, 11),
                "BAB",
                " D ",
                "B B",

                ('D'),
                ItemStackHelper.fromData(IUItem.machines, 1, 10),

                ('A'),
                ItemStackHelper.fromData(IUItem.crafting_elements, 1, 232),

                ('B'),
                ItemStackHelper.fromData(IUItem.crafting_elements, 1, 139)
        ), 1);
        TileEntityUpgradeMachineFactory.addRecipe(Recipes.recipe.addRecipe(
                ItemStackHelper.fromData(IUItem.machines, 1, 12),
                "BAB",
                " D ",
                "B B",

                ('D'),
                ItemStackHelper.fromData(IUItem.machines, 1, 11),

                ('A'),
                ItemStackHelper.fromData(IUItem.crafting_elements, 1, 233),

                ('B'),
                ItemStackHelper.fromData(IUItem.crafting_elements, 1, 140)
        ), 2);
        TileEntityUpgradeMachineFactory.addRecipe(Recipes.recipe.addRecipe(
                ItemStackHelper.fromData(IUItem.basemachine, 1, 4),
                "B B",
                "ADC",
                "B B",

                ('D'),
                getBlockStack(BlockBaseMachine3.geogenerator_iu),
                ('A'),
                ItemStackHelper.fromData(IUItem.crafting_elements, 1, 234),

                ('C'),
                ItemStackHelper.fromData(IUItem.crafting_elements, 1, 5),

                ('B'),
                ItemStackHelper.fromData(IUItem.crafting_elements, 1, 138)
        ), 0);
        TileEntityUpgradeMachineFactory.addRecipe(Recipes.recipe.addRecipe(
                ItemStackHelper.fromData(IUItem.basemachine, 1, 5),
                "B B",
                "ADC",
                "B B",

                ('D'),
                ItemStackHelper.fromData(IUItem.basemachine, 1, 4),

                ('A'),
                ItemStackHelper.fromData(IUItem.crafting_elements, 1, 232),

                ('C'),
                ItemStackHelper.fromData(IUItem.crafting_elements, 1, 82),

                ('B'),
                ItemStackHelper.fromData(IUItem.crafting_elements, 1, 139)
        ), 1);
        TileEntityUpgradeMachineFactory.addRecipe(Recipes.recipe.addRecipe(
                ItemStackHelper.fromData(IUItem.basemachine, 1, 6),
                "B B",
                "ADC",
                "B B",

                ('D'),
                ItemStackHelper.fromData(IUItem.basemachine, 1, 5),

                ('A'),
                ItemStackHelper.fromData(IUItem.crafting_elements, 1, 233),

                ('C'),
                ItemStackHelper.fromData(IUItem.crafting_elements, 1, 106),

                ('B'),
                ItemStackHelper.fromData(IUItem.crafting_elements, 1, 140)
        ), 2);
        TileEntityUpgradeMachineFactory.addRecipe(Recipes.recipe.addRecipe(ItemStackHelper.fromData(IUItem.machines, 1, 13), "C C", " B ",
                "CAC",

                ('A'), ItemStackHelper.fromData(IUItem.crafting_elements, 1, 257),
                ('B'), ItemStackHelper.fromData(IUItem.machines, 1, 8),
                ('C'), ItemStackHelper.fromData(IUItem.crafting_elements, 1, 138)
        ), 0);
        TileEntityUpgradeMachineFactory.addRecipe(Recipes.recipe.addRecipe(ItemStackHelper.fromData(IUItem.machines, 1, 14), "C C", " B "
                , "CAC",

                ('A'), ItemStackHelper.fromData(IUItem.crafting_elements, 1, 253),
                ('B'), ItemStackHelper.fromData(IUItem.machines, 1, 13),
                ('C'), ItemStackHelper.fromData(IUItem.crafting_elements, 1, 139)
        ), 1);
        TileEntityUpgradeMachineFactory.addRecipe(Recipes.recipe.addRecipe(
                ItemStackHelper.fromData(IUItem.machines, 1, 15),
                "C C",
                " B ",
                "CAC",

                ('A'),
                ItemStackHelper.fromData(IUItem.crafting_elements, 1, 255),
                ('B'),
                ItemStackHelper.fromData(IUItem.machines, 1, 14),
                ('C'),
                ItemStackHelper.fromData(IUItem.crafting_elements, 1, 140)
        ), 2);
        TileEntityUpgradeMachineFactory.addRecipe(Recipes.recipe.addRecipe(
                ItemStackHelper.fromData(IUItem.basemachine1, 1, 7),
                "F F",
                "CAE",
                "FBF",

                ('A'),
                ItemStackHelper.fromData(IUItem.basemachine1, 1, 6),

                ('B'),
                ItemStackHelper.fromData(IUItem.crafting_elements, 1, 96),

                ('C'),
                ItemStackHelper.fromData(IUItem.crafting_elements, 1, 83),

                ('E'),
                ItemStackHelper.fromData(IUItem.crafting_elements, 1, 245),
                ('F'),
                ItemStackHelper.fromData(IUItem.crafting_elements, 1, 139)
        ), 1);
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
    public ContainerUpgradeMachineFactory getGuiContainer(final Player var1) {
        return new ContainerUpgradeMachineFactory(this, var1);
    }

    @Override
    @OnlyIn(Dist.CLIENT)
    public GuiCore<ContainerBase<? extends IAdvInventory>> getGui(Player var1, ContainerBase<? extends IAdvInventory> menu) {
        return new GuiUpgradeMachineFactory((ContainerUpgradeMachineFactory) menu);
    }

    @Override
    public BlockTileEntity getBlock() {
        return IUItem.basemachine2.getBlock(getTeBlock());
    }

    @Override
    public IMultiTileBlock getTeBlock() {
        return BlockBaseMachine3.upgrade_machine;
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
