package com.denfop.tiles.mechanism;

import com.denfop.IUItem;
import com.denfop.api.Recipes;
import com.denfop.api.recipe.*;
import com.denfop.api.recipe.InventoryRecipes;
import com.denfop.api.tile.IMultiTileBlock;
import com.denfop.api.upgrades.IUpgradableBlock;
import com.denfop.api.upgrades.UpgradableProperty;
import com.denfop.blocks.BlockTileEntity;
import com.denfop.blocks.FluidName;
import com.denfop.blocks.mechanism.BlockBaseMachine3;
import com.denfop.componets.AirPollutionComponent;
import com.denfop.componets.ComponentProcess;
import com.denfop.componets.ComponentProgress;
import com.denfop.componets.ComponentUpgrade;
import com.denfop.componets.ComponentUpgradeSlots;
import com.denfop.componets.Fluids;
import com.denfop.componets.SoilPollutionComponent;
import com.denfop.componets.TypeUpgrade;
import com.denfop.container.ContainerGeneticPolymerizer;
import com.denfop.gui.GuiGeneticPolymerizer;
import com.denfop.invslot.Inventory;
import com.denfop.invslot.InventoryFluidByList;
import com.denfop.invslot.InventoryUpgrade;
import com.denfop.recipe.IInputHandler;
import com.denfop.tiles.base.TileElectricLiquidTankInventory;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import java.util.EnumSet;
import java.util.Set;

public class TileEntityGeneticPolymerizer extends TileElectricLiquidTankInventory implements IHasRecipe, IUpdateTick,
        IUpgradableBlock {


    public final InventoryFluidByList fluidSlot;
    public final InventoryOutput outputSlot1;
    public final InventoryUpgrade upgradeSlot;
    public final ComponentUpgradeSlots componentUpgrade;
    public final ComponentProgress componentProgress;
    public final ComponentProcess componentProcess;
    private final SoilPollutionComponent pollutionSoil;
    private final AirPollutionComponent pollutionAir;
    private final ComponentUpgrade componentUpgrades;

    public InventoryRecipes inputSlotA;
    public MachineRecipe output;

    public TileEntityGeneticPolymerizer() {
        super(
                300,
                1,
                12,
                Fluids.fluidPredicate(FluidName.fluidbeegenetic.getInstance(), FluidName.fluidcropgenetic.getInstance())
        );
        this.outputSlot1 = new InventoryOutput(this, 1);
        this.fluidSlot = new InventoryFluidByList(
                this,
                1,
                FluidName.fluidbeegenetic.getInstance(),
                FluidName.fluidcropgenetic.getInstance()
        );
        this.upgradeSlot = new InventoryUpgrade(this, 4);
        this.output = null;
        this.componentUpgrade = this.addComponent(new ComponentUpgradeSlots(this, upgradeSlot));
        this.componentProgress = this.addComponent(new ComponentProgress(this, 1,
                (short) 1
        ));
        this.componentProcess = this.addComponent(new ComponentProcess(this, 1, 300));
        this.componentProcess.setHasAudio(true);
        this.componentProcess.setHasTank(true);
        this.componentProcess.setSlotOutput(outputSlot);
        this.componentUpgrades = this.addComponent(new ComponentUpgrade(this, TypeUpgrade.INSTANT, TypeUpgrade.STACK));

        this.inputSlotA = new InventoryRecipes(this, "genetic_polymerizer", this, this.fluidTank);
        this.componentProcess.setInvSlotRecipes(inputSlotA);
        this.inputSlotA.setInvSlotConsumableLiquidByList(this.fluidSlot);
        fluidTank.setTypeItemSlot(Inventory.TypeItemSlot.INPUT);
        this.pollutionSoil = this.addComponent(new SoilPollutionComponent(this, 0.1));
        this.pollutionAir = this.addComponent(new AirPollutionComponent(this, 0.25));
        Recipes.recipes.addInitRecipes(this);
    }

    public ContainerGeneticPolymerizer getGuiContainer(EntityPlayer entityPlayer) {
        return new ContainerGeneticPolymerizer(entityPlayer, this);

    }


    @Override
    public void onLoaded() {
        super.onLoaded();
        inputSlotA.load();
        this.getOutput();
    }

    public MachineRecipe getOutput() {
        this.output = this.inputSlotA.process();
        return this.output;
    }

    @Override
    public void onUpdate() {

    }

    @Override
    public MachineRecipe getRecipeOutput() {
        return this.output;
    }

    @Override
    public void setRecipeOutput(final MachineRecipe output) {
        this.output = output;
    }

    public IMultiTileBlock getTeBlock() {
        return BlockBaseMachine3.genetic_polymerizer;
    }

    public BlockTileEntity getBlock() {
        return IUItem.basemachine2;
    }

    public void init() {
        final IInputHandler input = com.denfop.api.Recipes.inputFactory;
        Recipes.recipes.addRecipe("genetic_polymerizer", new BaseMachineRecipe(
                new Input(
                        new FluidStack(FluidName.fluidbeegenetic.getInstance(), 500),
                        input.getInput(new ItemStack(IUItem.genome_bee, 1, 0)),
                        input.getInput(new ItemStack(Items.GLASS_BOTTLE)),
                        input.getInput(new ItemStack(Items.NETHER_STAR, 1)),
                        input.getInput(new ItemStack(IUItem.crafting_elements, 8, 445)),
                        input.getInput(new ItemStack(IUItem.toriy, 2))
                ),
                new RecipeOutput(null, new ItemStack(IUItem.genome_bee, 1, 1))
        ));
        Recipes.recipes.addRecipe("genetic_polymerizer", new BaseMachineRecipe(
                new Input(
                        new FluidStack(FluidName.fluidbeegenetic.getInstance(), 1000),
                        input.getInput(new ItemStack(IUItem.genome_bee, 1, 1)),
                        input.getInput(new ItemStack(Items.GLASS_BOTTLE)),
                        input.getInput(new ItemStack(Items.NETHER_STAR, 2)),
                        input.getInput(new ItemStack(IUItem.crafting_elements, 10, 445)),
                        input.getInput(new ItemStack(IUItem.toriy, 2))
                ),
                new RecipeOutput(null, new ItemStack(IUItem.genome_bee, 1, 2))
        ));

        Recipes.recipes.addRecipe("genetic_polymerizer", new BaseMachineRecipe(
                new Input(
                        new FluidStack(FluidName.fluidbeegenetic.getInstance(), 500),
                        input.getInput(new ItemStack(IUItem.genome_bee, 1, 3)),
                        input.getInput(new ItemStack(Items.GLASS_BOTTLE)),
                        input.getInput(new ItemStack(Items.NETHER_STAR, 1)),
                        input.getInput(new ItemStack(IUItem.crafting_elements, 8, 445)),
                        input.getInput(new ItemStack(IUItem.radiationresources, 2, 4))
                ),
                new RecipeOutput(null, new ItemStack(IUItem.genome_bee, 1, 4))
        ));
        Recipes.recipes.addRecipe("genetic_polymerizer", new BaseMachineRecipe(
                new Input(
                        new FluidStack(FluidName.fluidbeegenetic.getInstance(), 1000),
                        input.getInput(new ItemStack(IUItem.genome_bee, 1, 4)),
                        input.getInput(new ItemStack(Items.GLASS_BOTTLE)),
                        input.getInput(new ItemStack(Items.NETHER_STAR, 2)),
                        input.getInput(new ItemStack(IUItem.crafting_elements, 10, 445)),
                        input.getInput(new ItemStack(IUItem.radiationresources, 2, 4))
                ),
                new RecipeOutput(null, new ItemStack(IUItem.genome_bee, 1, 5))
        ));

        Recipes.recipes.addRecipe("genetic_polymerizer", new BaseMachineRecipe(
                new Input(
                        new FluidStack(FluidName.fluidbeegenetic.getInstance(), 500),
                        input.getInput(new ItemStack(IUItem.genome_bee, 1, 7)),
                        input.getInput(new ItemStack(Items.GLASS_BOTTLE)),
                        input.getInput(new ItemStack(Items.NETHER_STAR, 1)),
                        input.getInput(new ItemStack(IUItem.crafting_elements, 8, 445)),
                        input.getInput(new ItemStack(IUItem.proton, 2))
                ),
                new RecipeOutput(null, new ItemStack(IUItem.genome_bee, 1, 8))
        ));
        Recipes.recipes.addRecipe("genetic_polymerizer", new BaseMachineRecipe(
                new Input(
                        new FluidStack(FluidName.fluidbeegenetic.getInstance(), 1000),
                        input.getInput(new ItemStack(IUItem.genome_bee, 1, 8)),
                        input.getInput(new ItemStack(Items.GLASS_BOTTLE)),
                        input.getInput(new ItemStack(Items.NETHER_STAR, 2)),
                        input.getInput(new ItemStack(IUItem.crafting_elements, 10, 445)),
                        input.getInput(new ItemStack(IUItem.proton, 2))
                ),
                new RecipeOutput(null, new ItemStack(IUItem.genome_bee, 1, 9))
        ));
        Recipes.recipes.addRecipe("genetic_polymerizer", new BaseMachineRecipe(
                new Input(
                        new FluidStack(FluidName.fluidbeegenetic.getInstance(), 500),
                        input.getInput(new ItemStack(IUItem.genome_bee, 1, 10)),
                        input.getInput(new ItemStack(Items.GLASS_BOTTLE)),
                        input.getInput(new ItemStack(Items.NETHER_STAR, 1)),
                        input.getInput(new ItemStack(IUItem.crafting_elements, 8, 445)),
                        input.getInput(new ItemStack(IUItem.proton, 2))
                ),
                new RecipeOutput(null, new ItemStack(IUItem.genome_bee, 1, 11))
        ));
        Recipes.recipes.addRecipe("genetic_polymerizer", new BaseMachineRecipe(
                new Input(
                        new FluidStack(FluidName.fluidbeegenetic.getInstance(), 500),
                        input.getInput(new ItemStack(IUItem.genome_bee, 1, 12)),
                        input.getInput(new ItemStack(Items.GLASS_BOTTLE)),
                        input.getInput(new ItemStack(Items.NETHER_STAR, 1)),
                        input.getInput(new ItemStack(IUItem.crafting_elements, 8, 446)),
                        input.getInput(new ItemStack(IUItem.radiationresources, 2, 2))
                ),
                new RecipeOutput(null, new ItemStack(IUItem.genome_bee, 1, 13))
        ));
        Recipes.recipes.addRecipe("genetic_polymerizer", new BaseMachineRecipe(
                new Input(
                        new FluidStack(FluidName.fluidbeegenetic.getInstance(), 1000),
                        input.getInput(new ItemStack(IUItem.genome_bee, 1, 13)),
                        input.getInput(new ItemStack(Items.GLASS_BOTTLE)),
                        input.getInput(new ItemStack(Items.NETHER_STAR, 2)),
                        input.getInput(new ItemStack(IUItem.crafting_elements, 10, 446)),
                        input.getInput(new ItemStack(IUItem.radiationresources, 2, 2))
                ),
                new RecipeOutput(null, new ItemStack(IUItem.genome_bee, 1, 14))
        ));

        Recipes.recipes.addRecipe("genetic_polymerizer", new BaseMachineRecipe(
                new Input(
                        new FluidStack(FluidName.fluidbeegenetic.getInstance(), 500),
                        input.getInput(new ItemStack(IUItem.genome_bee, 1, 15)),
                        input.getInput(new ItemStack(Items.GLASS_BOTTLE)),
                        input.getInput(new ItemStack(Items.NETHER_STAR, 1)),
                        input.getInput(new ItemStack(IUItem.crafting_elements, 8, 446)),
                        input.getInput(new ItemStack(IUItem.nuclear_res, 2, 2))
                ),
                new RecipeOutput(null, new ItemStack(IUItem.genome_bee, 1, 16))
        ));
        Recipes.recipes.addRecipe("genetic_polymerizer", new BaseMachineRecipe(
                new Input(
                        new FluidStack(FluidName.fluidbeegenetic.getInstance(), 1000),
                        input.getInput(new ItemStack(IUItem.genome_bee, 1, 16)),
                        input.getInput(new ItemStack(Items.GLASS_BOTTLE)),
                        input.getInput(new ItemStack(Items.NETHER_STAR, 2)),
                        input.getInput(new ItemStack(IUItem.crafting_elements, 10, 446)),
                        input.getInput(new ItemStack(IUItem.nuclear_res, 2, 2))
                ),
                new RecipeOutput(null, new ItemStack(IUItem.genome_bee, 1, 17))
        ));

        Recipes.recipes.addRecipe("genetic_polymerizer", new BaseMachineRecipe(
                new Input(
                        new FluidStack(FluidName.fluidbeegenetic.getInstance(), 500),
                        input.getInput(new ItemStack(IUItem.genome_bee, 1, 18)),
                        input.getInput(new ItemStack(Items.GLASS_BOTTLE)),
                        input.getInput(new ItemStack(Items.NETHER_STAR, 1)),
                        input.getInput(new ItemStack(IUItem.crafting_elements, 8, 446)),
                        input.getInput(new ItemStack(IUItem.radiationresources, 2, 0))
                ),
                new RecipeOutput(null, new ItemStack(IUItem.genome_bee, 1, 19))
        ));
        Recipes.recipes.addRecipe("genetic_polymerizer", new BaseMachineRecipe(
                new Input(
                        new FluidStack(FluidName.fluidbeegenetic.getInstance(), 1000),
                        input.getInput(new ItemStack(IUItem.genome_bee, 1, 19)),
                        input.getInput(new ItemStack(Items.GLASS_BOTTLE)),
                        input.getInput(new ItemStack(Items.NETHER_STAR, 2)),
                        input.getInput(new ItemStack(IUItem.crafting_elements, 10, 446)),
                        input.getInput(new ItemStack(IUItem.radiationresources, 2, 0))
                ),
                new RecipeOutput(null, new ItemStack(IUItem.genome_bee, 1, 20))
        ));

        Recipes.recipes.addRecipe("genetic_polymerizer", new BaseMachineRecipe(
                new Input(
                        new FluidStack(FluidName.fluidbeegenetic.getInstance(), 500),
                        input.getInput(new ItemStack(IUItem.genome_bee, 1, 21)),
                        input.getInput(new ItemStack(Items.GLASS_BOTTLE)),
                        input.getInput(new ItemStack(Items.NETHER_STAR, 1)),
                        input.getInput(new ItemStack(IUItem.crafting_elements, 8, 446)),
                        input.getInput(new ItemStack(IUItem.radiationresources, 2, 4))
                ),
                new RecipeOutput(null, new ItemStack(IUItem.genome_bee, 1, 22))
        ));
        Recipes.recipes.addRecipe("genetic_polymerizer", new BaseMachineRecipe(
                new Input(
                        new FluidStack(FluidName.fluidbeegenetic.getInstance(), 1000),
                        input.getInput(new ItemStack(IUItem.genome_bee, 1, 22)),
                        input.getInput(new ItemStack(Items.GLASS_BOTTLE)),
                        input.getInput(new ItemStack(Items.NETHER_STAR, 2)),
                        input.getInput(new ItemStack(IUItem.crafting_elements, 10, 446)),
                        input.getInput(new ItemStack(IUItem.radiationresources, 2, 4))
                ),
                new RecipeOutput(null, new ItemStack(IUItem.genome_bee, 1, 23))
        ));

        Recipes.recipes.addRecipe("genetic_polymerizer", new BaseMachineRecipe(
                new Input(
                        new FluidStack(FluidName.fluidbeegenetic.getInstance(), 500),
                        input.getInput(new ItemStack(IUItem.genome_bee, 1, 24)),
                        input.getInput(new ItemStack(Items.GLASS_BOTTLE)),
                        input.getInput(new ItemStack(Items.NETHER_STAR, 1)),
                        input.getInput(new ItemStack(IUItem.crafting_elements, 8, 445)),
                        input.getInput(new ItemStack(IUItem.radiationresources, 2, 2))
                ),
                new RecipeOutput(null, new ItemStack(IUItem.genome_bee, 1, 25))
        ));
        Recipes.recipes.addRecipe("genetic_polymerizer", new BaseMachineRecipe(
                new Input(
                        new FluidStack(FluidName.fluidbeegenetic.getInstance(), 1000),
                        input.getInput(new ItemStack(IUItem.genome_bee, 1, 25)),
                        input.getInput(new ItemStack(Items.GLASS_BOTTLE)),
                        input.getInput(new ItemStack(Items.NETHER_STAR, 2)),
                        input.getInput(new ItemStack(IUItem.crafting_elements, 10, 445)),
                        input.getInput(new ItemStack(IUItem.radiationresources, 2, 2))
                ),
                new RecipeOutput(null, new ItemStack(IUItem.genome_bee, 1, 26))
        ));

        Recipes.recipes.addRecipe("genetic_polymerizer", new BaseMachineRecipe(
                new Input(
                        new FluidStack(FluidName.fluidbeegenetic.getInstance(), 500),
                        input.getInput(new ItemStack(IUItem.genome_bee, 1, 27)),
                        input.getInput(new ItemStack(Items.GLASS_BOTTLE)),
                        input.getInput(new ItemStack(Items.NETHER_STAR, 1)),
                        input.getInput(new ItemStack(IUItem.crafting_elements, 8, 446)),
                        input.getInput(new ItemStack(IUItem.nuclear_res, 2, 2))
                ),
                new RecipeOutput(null, new ItemStack(IUItem.genome_bee, 1, 28))
        ));
        Recipes.recipes.addRecipe("genetic_polymerizer", new BaseMachineRecipe(
                new Input(
                        new FluidStack(FluidName.fluidbeegenetic.getInstance(), 1000),
                        input.getInput(new ItemStack(IUItem.genome_bee, 1, 28)),
                        input.getInput(new ItemStack(Items.GLASS_BOTTLE)),
                        input.getInput(new ItemStack(Items.NETHER_STAR, 2)),
                        input.getInput(new ItemStack(IUItem.crafting_elements, 10, 446)),
                        input.getInput(new ItemStack(IUItem.nuclear_res, 2, 2))
                ),
                new RecipeOutput(null, new ItemStack(IUItem.genome_bee, 1, 29))
        ));
        Recipes.recipes.addRecipe("genetic_polymerizer", new BaseMachineRecipe(
                new Input(
                        new FluidStack(FluidName.fluidbeegenetic.getInstance(), 500),
                        input.getInput(new ItemStack(IUItem.genome_bee, 1, 30)),
                        input.getInput(new ItemStack(Items.GLASS_BOTTLE)),
                        input.getInput(new ItemStack(Items.NETHER_STAR, 1)),
                        input.getInput(new ItemStack(IUItem.crafting_elements, 8, 446)),
                        input.getInput(new ItemStack(IUItem.toriy, 2))
                ),
                new RecipeOutput(null, new ItemStack(IUItem.genome_bee, 1, 31))
        ));
        Recipes.recipes.addRecipe("genetic_polymerizer", new BaseMachineRecipe(
                new Input(
                        new FluidStack(FluidName.fluidbeegenetic.getInstance(), 1000),
                        input.getInput(new ItemStack(IUItem.genome_bee, 1, 31)),
                        input.getInput(new ItemStack(Items.GLASS_BOTTLE)),
                        input.getInput(new ItemStack(Items.NETHER_STAR, 2)),
                        input.getInput(new ItemStack(IUItem.crafting_elements, 10, 446)),
                        input.getInput(new ItemStack(IUItem.toriy, 2))
                ),
                new RecipeOutput(null, new ItemStack(IUItem.genome_bee, 1, 32))
        ));
        Recipes.recipes.addRecipe("genetic_polymerizer", new BaseMachineRecipe(
                new Input(
                        new FluidStack(FluidName.fluidbeegenetic.getInstance(), 500),
                        input.getInput(new ItemStack(IUItem.genome_bee, 1, 33)),
                        input.getInput(new ItemStack(Items.GLASS_BOTTLE)),
                        input.getInput(new ItemStack(Items.NETHER_STAR, 1)),
                        input.getInput(new ItemStack(IUItem.crafting_elements, 8, 445)),
                        input.getInput(new ItemStack(IUItem.radiationresources, 2, 1))
                ),
                new RecipeOutput(null, new ItemStack(IUItem.genome_bee, 1, 34))
        ));
        Recipes.recipes.addRecipe("genetic_polymerizer", new BaseMachineRecipe(
                new Input(
                        new FluidStack(FluidName.fluidbeegenetic.getInstance(), 1000),
                        input.getInput(new ItemStack(IUItem.genome_bee, 1, 34)),
                        input.getInput(new ItemStack(Items.GLASS_BOTTLE)),
                        input.getInput(new ItemStack(Items.NETHER_STAR, 2)),
                        input.getInput(new ItemStack(IUItem.crafting_elements, 10, 445)),
                        input.getInput(new ItemStack(IUItem.radiationresources, 2, 1))
                ),
                new RecipeOutput(null, new ItemStack(IUItem.genome_bee, 1, 35))
        ));

        Recipes.recipes.addRecipe("genetic_polymerizer", new BaseMachineRecipe(
                new Input(
                        new FluidStack(FluidName.fluidbeegenetic.getInstance(), 500),
                        input.getInput(new ItemStack(IUItem.genome_bee, 1, 38)),
                        input.getInput(new ItemStack(Items.GLASS_BOTTLE)),
                        input.getInput(new ItemStack(Items.NETHER_STAR, 1)),
                        input.getInput(new ItemStack(IUItem.crafting_elements, 8, 445)),
                        input.getInput(new ItemStack(IUItem.nuclear_res, 2, 2))
                ),
                new RecipeOutput(null, new ItemStack(IUItem.genome_bee, 1, 39))
        ));
        Recipes.recipes.addRecipe("genetic_polymerizer", new BaseMachineRecipe(
                new Input(
                        new FluidStack(FluidName.fluidbeegenetic.getInstance(), 1000),
                        input.getInput(new ItemStack(IUItem.genome_bee, 1, 39)),
                        input.getInput(new ItemStack(Items.GLASS_BOTTLE)),
                        input.getInput(new ItemStack(Items.NETHER_STAR, 2)),
                        input.getInput(new ItemStack(IUItem.crafting_elements, 10, 445)),
                        input.getInput(new ItemStack(IUItem.nuclear_res, 2, 2))
                ),
                new RecipeOutput(null, new ItemStack(IUItem.genome_bee, 1, 40))
        ));

        Recipes.recipes.addRecipe("genetic_polymerizer", new BaseMachineRecipe(
                new Input(
                        new FluidStack(FluidName.fluidbeegenetic.getInstance(), 500),
                        input.getInput(new ItemStack(IUItem.genome_bee, 1, 41)),
                        input.getInput(new ItemStack(Items.GLASS_BOTTLE)),
                        input.getInput(new ItemStack(Items.NETHER_STAR, 1)),
                        input.getInput(new ItemStack(IUItem.crafting_elements, 8, 445)),
                        input.getInput(new ItemStack(IUItem.radiationresources, 2, 3))
                ),
                new RecipeOutput(null, new ItemStack(IUItem.genome_bee, 1, 42))
        ));
        Recipes.recipes.addRecipe("genetic_polymerizer", new BaseMachineRecipe(
                new Input(
                        new FluidStack(FluidName.fluidbeegenetic.getInstance(), 1000),
                        input.getInput(new ItemStack(IUItem.genome_bee, 1, 42)),
                        input.getInput(new ItemStack(Items.GLASS_BOTTLE)),
                        input.getInput(new ItemStack(Items.NETHER_STAR, 2)),
                        input.getInput(new ItemStack(IUItem.crafting_elements, 10, 445)),
                        input.getInput(new ItemStack(IUItem.radiationresources, 2, 3))
                ),
                new RecipeOutput(null, new ItemStack(IUItem.genome_bee, 1, 43))
        ));
        Recipes.recipes.addRecipe("genetic_polymerizer", new BaseMachineRecipe(
                new Input(
                        new FluidStack(FluidName.fluidbeegenetic.getInstance(), 500),
                        input.getInput(new ItemStack(IUItem.genome_bee, 1, 44)),
                        input.getInput(new ItemStack(Items.GLASS_BOTTLE)),
                        input.getInput(new ItemStack(Items.NETHER_STAR, 1)),
                        input.getInput(new ItemStack(IUItem.crafting_elements, 8, 446)),
                        input.getInput(new ItemStack(IUItem.nuclear_res, 2, 3))
                ),
                new RecipeOutput(null, new ItemStack(IUItem.genome_bee, 1, 45))
        ));
        Recipes.recipes.addRecipe("genetic_polymerizer", new BaseMachineRecipe(
                new Input(
                        new FluidStack(FluidName.fluidbeegenetic.getInstance(), 1000),
                        input.getInput(new ItemStack(IUItem.genome_bee, 1, 45)),
                        input.getInput(new ItemStack(Items.GLASS_BOTTLE)),
                        input.getInput(new ItemStack(Items.NETHER_STAR, 2)),
                        input.getInput(new ItemStack(IUItem.crafting_elements, 10, 446)),
                        input.getInput(new ItemStack(IUItem.nuclear_res, 2, 3))
                ),
                new RecipeOutput(null, new ItemStack(IUItem.genome_bee, 1, 46))
        ));

        Recipes.recipes.addRecipe("genetic_polymerizer", new BaseMachineRecipe(
                new Input(
                        new FluidStack(FluidName.fluidbeegenetic.getInstance(), 500),
                        input.getInput(new ItemStack(IUItem.genome_bee, 1, 47)),
                        input.getInput(new ItemStack(Items.GLASS_BOTTLE)),
                        input.getInput(new ItemStack(Items.NETHER_STAR, 1)),
                        input.getInput(new ItemStack(IUItem.crafting_elements, 8, 446)),
                        input.getInput(new ItemStack(IUItem.radiationresources, 2, 3))
                ),
                new RecipeOutput(null, new ItemStack(IUItem.genome_bee, 1, 48))
        ));
        Recipes.recipes.addRecipe("genetic_polymerizer", new BaseMachineRecipe(
                new Input(
                        new FluidStack(FluidName.fluidbeegenetic.getInstance(), 1000),
                        input.getInput(new ItemStack(IUItem.genome_bee, 1, 48)),
                        input.getInput(new ItemStack(Items.GLASS_BOTTLE)),
                        input.getInput(new ItemStack(Items.NETHER_STAR, 2)),
                        input.getInput(new ItemStack(IUItem.crafting_elements, 10, 446)),
                        input.getInput(new ItemStack(IUItem.radiationresources, 2, 3))
                ),
                new RecipeOutput(null, new ItemStack(IUItem.genome_bee, 1, 49))
        ));


        Recipes.recipes.addRecipe("genetic_polymerizer", new BaseMachineRecipe(
                new Input(
                        new FluidStack(FluidName.fluidcropgenetic.getInstance(), 500),
                        input.getInput(new ItemStack(IUItem.genome_crop, 1, 0)),
                        input.getInput(new ItemStack(Items.GLASS_BOTTLE)),
                        input.getInput(new ItemStack(IUItem.nether_star_ingot, 1)),
                        input.getInput(new ItemStack(IUItem.crafting_elements, 8, 445)),
                        input.getInput(new ItemStack(IUItem.toriy, 2))
                ),
                new RecipeOutput(null, new ItemStack(IUItem.genome_crop, 1, 1))
        ));
        Recipes.recipes.addRecipe("genetic_polymerizer", new BaseMachineRecipe(
                new Input(
                        new FluidStack(FluidName.fluidcropgenetic.getInstance(), 1000),
                        input.getInput(new ItemStack(IUItem.genome_crop, 1, 1)),
                        input.getInput(new ItemStack(Items.GLASS_BOTTLE)),
                        input.getInput(new ItemStack(IUItem.nether_star_ingot, 2)),
                        input.getInput(new ItemStack(IUItem.crafting_elements, 10, 445)),
                        input.getInput(new ItemStack(IUItem.toriy, 2))
                ),
                new RecipeOutput(null, new ItemStack(IUItem.genome_crop, 1, 2))
        ));

        Recipes.recipes.addRecipe("genetic_polymerizer", new BaseMachineRecipe(
                new Input(
                        new FluidStack(FluidName.fluidcropgenetic.getInstance(), 500),
                        input.getInput(new ItemStack(IUItem.genome_crop, 1, 3)),
                        input.getInput(new ItemStack(Items.GLASS_BOTTLE)),
                        input.getInput(new ItemStack(IUItem.nether_star_ingot, 1)),
                        input.getInput(new ItemStack(IUItem.crafting_elements, 8, 445)),
                        input.getInput(new ItemStack(IUItem.radiationresources, 2, 4))
                ),
                new RecipeOutput(null, new ItemStack(IUItem.genome_crop, 1, 4))
        ));
        Recipes.recipes.addRecipe("genetic_polymerizer", new BaseMachineRecipe(
                new Input(
                        new FluidStack(FluidName.fluidcropgenetic.getInstance(), 1000),
                        input.getInput(new ItemStack(IUItem.genome_crop, 1, 4)),
                        input.getInput(new ItemStack(Items.GLASS_BOTTLE)),
                        input.getInput(new ItemStack(IUItem.nether_star_ingot, 2)),
                        input.getInput(new ItemStack(IUItem.crafting_elements, 10, 445)),
                        input.getInput(new ItemStack(IUItem.radiationresources, 2, 4))
                ),
                new RecipeOutput(null, new ItemStack(IUItem.genome_crop, 1, 5))
        ));

        Recipes.recipes.addRecipe("genetic_polymerizer", new BaseMachineRecipe(
                new Input(
                        new FluidStack(FluidName.fluidcropgenetic.getInstance(), 500),
                        input.getInput(new ItemStack(IUItem.genome_crop, 1, 7)),
                        input.getInput(new ItemStack(Items.GLASS_BOTTLE)),
                        input.getInput(new ItemStack(IUItem.nether_star_ingot, 1)),
                        input.getInput(new ItemStack(IUItem.crafting_elements, 8, 445)),
                        input.getInput(new ItemStack(IUItem.proton, 2))
                ),
                new RecipeOutput(null, new ItemStack(IUItem.genome_crop, 1, 8))
        ));
        Recipes.recipes.addRecipe("genetic_polymerizer", new BaseMachineRecipe(
                new Input(
                        new FluidStack(FluidName.fluidcropgenetic.getInstance(), 1000),
                        input.getInput(new ItemStack(IUItem.genome_crop, 1, 8)),
                        input.getInput(new ItemStack(Items.GLASS_BOTTLE)),
                        input.getInput(new ItemStack(IUItem.nether_star_ingot, 2)),
                        input.getInput(new ItemStack(IUItem.crafting_elements, 10, 445)),
                        input.getInput(new ItemStack(IUItem.proton, 2))
                ),
                new RecipeOutput(null, new ItemStack(IUItem.genome_crop, 1, 9))
        ));
        Recipes.recipes.addRecipe("genetic_polymerizer", new BaseMachineRecipe(
                new Input(
                        new FluidStack(FluidName.fluidcropgenetic.getInstance(), 500),
                        input.getInput(new ItemStack(IUItem.genome_crop, 1, 10)),
                        input.getInput(new ItemStack(Items.GLASS_BOTTLE)),
                        input.getInput(new ItemStack(IUItem.nether_star_ingot, 1)),
                        input.getInput(new ItemStack(IUItem.crafting_elements, 8, 446)),
                        input.getInput(new ItemStack(IUItem.proton, 2))
                ),
                new RecipeOutput(null, new ItemStack(IUItem.genome_crop, 1, 11))
        ));


        Recipes.recipes.addRecipe("genetic_polymerizer", new BaseMachineRecipe(
                new Input(
                        new FluidStack(FluidName.fluidcropgenetic.getInstance(), 500),
                        input.getInput(new ItemStack(IUItem.genome_crop, 1, 13)),
                        input.getInput(new ItemStack(Items.GLASS_BOTTLE)),
                        input.getInput(new ItemStack(IUItem.nether_star_ingot, 1)),
                        input.getInput(new ItemStack(IUItem.crafting_elements, 8, 445)),
                        input.getInput(new ItemStack(IUItem.radiationresources, 2, 1))
                ),
                new RecipeOutput(null, new ItemStack(IUItem.genome_crop, 1, 14))
        ));
        Recipes.recipes.addRecipe("genetic_polymerizer", new BaseMachineRecipe(
                new Input(
                        new FluidStack(FluidName.fluidcropgenetic.getInstance(), 1000),
                        input.getInput(new ItemStack(IUItem.genome_crop, 1, 14)),
                        input.getInput(new ItemStack(Items.GLASS_BOTTLE)),
                        input.getInput(new ItemStack(IUItem.nether_star_ingot, 2)),
                        input.getInput(new ItemStack(IUItem.crafting_elements, 10, 445)),
                        input.getInput(new ItemStack(IUItem.radiationresources, 2, 1))
                ),
                new RecipeOutput(null, new ItemStack(IUItem.genome_crop, 1, 15))
        ));
        Recipes.recipes.addRecipe("genetic_polymerizer", new BaseMachineRecipe(
                new Input(
                        new FluidStack(FluidName.fluidcropgenetic.getInstance(), 1000),
                        input.getInput(new ItemStack(IUItem.genome_crop, 1, 15)),
                        input.getInput(new ItemStack(Items.GLASS_BOTTLE)),
                        input.getInput(new ItemStack(IUItem.nether_star_ingot, 4)),
                        input.getInput(new ItemStack(IUItem.crafting_elements, 10, 445)),
                        input.getInput(new ItemStack(IUItem.radiationresources, 2, 1))
                ),
                new RecipeOutput(null, new ItemStack(IUItem.genome_crop, 1, 16))
        ));


        Recipes.recipes.addRecipe("genetic_polymerizer", new BaseMachineRecipe(
                new Input(
                        new FluidStack(FluidName.fluidcropgenetic.getInstance(), 500),
                        input.getInput(new ItemStack(IUItem.genome_crop, 1, 22)),
                        input.getInput(new ItemStack(Items.GLASS_BOTTLE)),
                        input.getInput(new ItemStack(IUItem.nether_star_ingot, 1)),
                        input.getInput(new ItemStack(IUItem.crafting_elements, 8, 446)),
                        input.getInput(new ItemStack(IUItem.radiationresources, 2, 2))
                ),
                new RecipeOutput(null, new ItemStack(IUItem.genome_crop, 1, 23))
        ));
        Recipes.recipes.addRecipe("genetic_polymerizer", new BaseMachineRecipe(
                new Input(
                        new FluidStack(FluidName.fluidcropgenetic.getInstance(), 1000),
                        input.getInput(new ItemStack(IUItem.genome_crop, 1, 23)),
                        input.getInput(new ItemStack(Items.GLASS_BOTTLE)),
                        input.getInput(new ItemStack(IUItem.nether_star_ingot, 2)),
                        input.getInput(new ItemStack(IUItem.crafting_elements, 10, 446)),
                        input.getInput(new ItemStack(IUItem.radiationresources, 2, 2))
                ),
                new RecipeOutput(null, new ItemStack(IUItem.genome_crop, 1, 24))
        ));

        Recipes.recipes.addRecipe("genetic_polymerizer", new BaseMachineRecipe(
                new Input(
                        new FluidStack(FluidName.fluidcropgenetic.getInstance(), 500),
                        input.getInput(new ItemStack(IUItem.genome_crop, 1, 25)),
                        input.getInput(new ItemStack(Items.GLASS_BOTTLE)),
                        input.getInput(new ItemStack(IUItem.nether_star_ingot, 1)),
                        input.getInput(new ItemStack(IUItem.crafting_elements, 8, 446)),
                        input.getInput(new ItemStack(IUItem.toriy, 2))
                ),
                new RecipeOutput(null, new ItemStack(IUItem.genome_crop, 1, 26))
        ));
        Recipes.recipes.addRecipe("genetic_polymerizer", new BaseMachineRecipe(
                new Input(
                        new FluidStack(FluidName.fluidcropgenetic.getInstance(), 1000),
                        input.getInput(new ItemStack(IUItem.genome_crop, 1, 26)),
                        input.getInput(new ItemStack(Items.GLASS_BOTTLE)),
                        input.getInput(new ItemStack(IUItem.nether_star_ingot, 2)),
                        input.getInput(new ItemStack(IUItem.crafting_elements, 10, 446)),
                        input.getInput(new ItemStack(IUItem.toriy, 2))
                ),
                new RecipeOutput(null, new ItemStack(IUItem.genome_crop, 1, 27))
        ));


        Recipes.recipes.addRecipe("genetic_polymerizer", new BaseMachineRecipe(
                new Input(
                        new FluidStack(FluidName.fluidcropgenetic.getInstance(), 500),
                        input.getInput(new ItemStack(IUItem.genome_crop, 1, 28)),
                        input.getInput(new ItemStack(Items.GLASS_BOTTLE)),
                        input.getInput(new ItemStack(IUItem.nether_star_ingot, 1)),
                        input.getInput(new ItemStack(IUItem.crafting_elements, 8, 445)),
                        input.getInput(new ItemStack(IUItem.radiationresources, 2, 2))
                ),
                new RecipeOutput(null, new ItemStack(IUItem.genome_crop, 1, 29))
        ));
        Recipes.recipes.addRecipe("genetic_polymerizer", new BaseMachineRecipe(
                new Input(
                        new FluidStack(FluidName.fluidcropgenetic.getInstance(), 1000),
                        input.getInput(new ItemStack(IUItem.genome_crop, 1, 29)),
                        input.getInput(new ItemStack(Items.GLASS_BOTTLE)),
                        input.getInput(new ItemStack(IUItem.nether_star_ingot, 2)),
                        input.getInput(new ItemStack(IUItem.crafting_elements, 10, 445)),
                        input.getInput(new ItemStack(IUItem.radiationresources, 2, 2))
                ),
                new RecipeOutput(null, new ItemStack(IUItem.genome_crop, 1, 30))
        ));

        Recipes.recipes.addRecipe("genetic_polymerizer", new BaseMachineRecipe(
                new Input(
                        new FluidStack(FluidName.fluidcropgenetic.getInstance(), 500),
                        input.getInput(new ItemStack(IUItem.genome_crop, 1, 31)),
                        input.getInput(new ItemStack(Items.GLASS_BOTTLE)),
                        input.getInput(new ItemStack(IUItem.nether_star_ingot, 1)),
                        input.getInput(new ItemStack(IUItem.crafting_elements, 8, 446)),
                        input.getInput(new ItemStack(IUItem.radiationresources, 2, 4))
                ),
                new RecipeOutput(null, new ItemStack(IUItem.genome_crop, 1, 32))
        ));
        Recipes.recipes.addRecipe("genetic_polymerizer", new BaseMachineRecipe(
                new Input(
                        new FluidStack(FluidName.fluidcropgenetic.getInstance(), 1000),
                        input.getInput(new ItemStack(IUItem.genome_crop, 1, 32)),
                        input.getInput(new ItemStack(Items.GLASS_BOTTLE)),
                        input.getInput(new ItemStack(IUItem.nether_star_ingot, 2)),
                        input.getInput(new ItemStack(IUItem.crafting_elements, 10, 446)),
                        input.getInput(new ItemStack(IUItem.radiationresources, 2, 4))
                ),
                new RecipeOutput(null, new ItemStack(IUItem.genome_crop, 1, 33))
        ));

        Recipes.recipes.addRecipe("genetic_polymerizer", new BaseMachineRecipe(
                new Input(
                        new FluidStack(FluidName.fluidcropgenetic.getInstance(), 500),
                        input.getInput(new ItemStack(IUItem.genome_crop, 1, 37)),
                        input.getInput(new ItemStack(Items.GLASS_BOTTLE)),
                        input.getInput(new ItemStack(IUItem.nether_star_ingot, 1)),
                        input.getInput(new ItemStack(IUItem.crafting_elements, 8, 445)),
                        input.getInput(new ItemStack(IUItem.nuclear_res, 2, 2))
                ),
                new RecipeOutput(null, new ItemStack(IUItem.genome_crop, 1, 38))
        ));
        Recipes.recipes.addRecipe("genetic_polymerizer", new BaseMachineRecipe(
                new Input(
                        new FluidStack(FluidName.fluidcropgenetic.getInstance(), 1000),
                        input.getInput(new ItemStack(IUItem.genome_crop, 1, 38)),
                        input.getInput(new ItemStack(Items.GLASS_BOTTLE)),
                        input.getInput(new ItemStack(IUItem.nether_star_ingot, 2)),
                        input.getInput(new ItemStack(IUItem.crafting_elements, 10, 445)),
                        input.getInput(new ItemStack(IUItem.nuclear_res, 2, 2))
                ),
                new RecipeOutput(null, new ItemStack(IUItem.genome_crop, 1, 39))
        ));

        Recipes.recipes.addRecipe("genetic_polymerizer", new BaseMachineRecipe(
                new Input(
                        new FluidStack(FluidName.fluidcropgenetic.getInstance(), 500),
                        input.getInput(new ItemStack(IUItem.genome_crop, 1, 40)),
                        input.getInput(new ItemStack(Items.GLASS_BOTTLE)),
                        input.getInput(new ItemStack(IUItem.nether_star_ingot, 1)),
                        input.getInput(new ItemStack(IUItem.crafting_elements, 8, 445)),
                        input.getInput(new ItemStack(IUItem.radiationresources, 2, 3))
                ),
                new RecipeOutput(null, new ItemStack(IUItem.genome_crop, 1, 41))
        ));
        Recipes.recipes.addRecipe("genetic_polymerizer", new BaseMachineRecipe(
                new Input(
                        new FluidStack(FluidName.fluidcropgenetic.getInstance(), 1000),
                        input.getInput(new ItemStack(IUItem.genome_crop, 1, 41)),
                        input.getInput(new ItemStack(Items.GLASS_BOTTLE)),
                        input.getInput(new ItemStack(IUItem.nether_star_ingot, 2)),
                        input.getInput(new ItemStack(IUItem.crafting_elements, 10, 445)),
                        input.getInput(new ItemStack(IUItem.radiationresources, 2, 3))
                ),
                new RecipeOutput(null, new ItemStack(IUItem.genome_crop, 1, 42))
        ));

        Recipes.recipes.addRecipe("genetic_polymerizer", new BaseMachineRecipe(
                new Input(
                        new FluidStack(FluidName.fluidcropgenetic.getInstance(), 500),
                        input.getInput(new ItemStack(IUItem.genome_crop, 1, 43)),
                        input.getInput(new ItemStack(Items.GLASS_BOTTLE)),
                        input.getInput(new ItemStack(IUItem.nether_star_ingot, 1)),
                        input.getInput(new ItemStack(IUItem.crafting_elements, 8, 446)),
                        input.getInput(new ItemStack(IUItem.nuclear_res, 2, 3))
                ),
                new RecipeOutput(null, new ItemStack(IUItem.genome_crop, 1, 44))
        ));
        Recipes.recipes.addRecipe("genetic_polymerizer", new BaseMachineRecipe(
                new Input(
                        new FluidStack(FluidName.fluidcropgenetic.getInstance(), 1000),
                        input.getInput(new ItemStack(IUItem.genome_crop, 1, 44)),
                        input.getInput(new ItemStack(Items.GLASS_BOTTLE)),
                        input.getInput(new ItemStack(IUItem.nether_star_ingot, 2)),
                        input.getInput(new ItemStack(IUItem.crafting_elements, 10, 446)),
                        input.getInput(new ItemStack(IUItem.nuclear_res, 2, 3))
                ),
                new RecipeOutput(null, new ItemStack(IUItem.genome_crop, 1, 45))
        ));

        Recipes.recipes.addRecipe("genetic_polymerizer", new BaseMachineRecipe(
                new Input(
                        new FluidStack(FluidName.fluidcropgenetic.getInstance(), 500),
                        input.getInput(new ItemStack(IUItem.genome_crop, 1, 46)),
                        input.getInput(new ItemStack(Items.GLASS_BOTTLE)),
                        input.getInput(new ItemStack(IUItem.nether_star_ingot, 1)),
                        input.getInput(new ItemStack(IUItem.crafting_elements, 8, 446)),
                        input.getInput(new ItemStack(IUItem.radiationresources, 2, 3))
                ),
                new RecipeOutput(null, new ItemStack(IUItem.genome_crop, 1, 47))
        ));
        Recipes.recipes.addRecipe("genetic_polymerizer", new BaseMachineRecipe(
                new Input(
                        new FluidStack(FluidName.fluidcropgenetic.getInstance(), 1000),
                        input.getInput(new ItemStack(IUItem.genome_crop, 1, 47)),
                        input.getInput(new ItemStack(Items.GLASS_BOTTLE)),
                        input.getInput(new ItemStack(IUItem.nether_star_ingot, 2)),
                        input.getInput(new ItemStack(IUItem.crafting_elements, 10, 446)),
                        input.getInput(new ItemStack(IUItem.radiationresources, 2, 3))
                ),
                new RecipeOutput(null, new ItemStack(IUItem.genome_crop, 1, 48))
        ));


        Recipes.recipes.addRecipe("genetic_polymerizer", new BaseMachineRecipe(
                new Input(
                        new FluidStack(FluidName.fluidcropgenetic.getInstance(), 1000),
                        input.getInput(new ItemStack(IUItem.genome_crop, 1, 19)),
                        input.getInput(new ItemStack(Items.GLASS_BOTTLE)),
                        input.getInput(new ItemStack(IUItem.nether_star_ingot, 2)),
                        input.getInput(new ItemStack(IUItem.crafting_elements, 10, 446)),
                        input.getInput(new ItemStack(IUItem.radiationresources, 2, 3))
                ),
                new RecipeOutput(null, new ItemStack(IUItem.genome_crop, 1, 17))
        ));

        Recipes.recipes.addRecipe("genetic_polymerizer", new BaseMachineRecipe(
                new Input(
                        new FluidStack(FluidName.fluidcropgenetic.getInstance(), 1000),
                        input.getInput(new ItemStack(IUItem.genome_crop, 1, 19)),
                        input.getInput(new ItemStack(Items.GLASS_BOTTLE)),
                        input.getInput(new ItemStack(IUItem.nether_star_ingot, 2)),
                        input.getInput(new ItemStack(IUItem.crafting_elements, 10, 446)),
                        input.getInput(new ItemStack(IUItem.radiationresources, 2, 1))
                ),
                new RecipeOutput(null, new ItemStack(IUItem.genome_crop, 1, 18))
        ));

        Recipes.recipes.addRecipe("genetic_polymerizer", new BaseMachineRecipe(
                new Input(
                        new FluidStack(FluidName.fluidcropgenetic.getInstance(), 1000),
                        input.getInput(new ItemStack(IUItem.genome_crop, 1, 19)),
                        input.getInput(new ItemStack(Items.GLASS_BOTTLE)),
                        input.getInput(new ItemStack(IUItem.nether_star_ingot, 2)),
                        input.getInput(new ItemStack(IUItem.crafting_elements, 10, 446)),
                        input.getInput(new ItemStack(IUItem.radiationresources, 2, 2))
                ),
                new RecipeOutput(null, new ItemStack(IUItem.genome_crop, 1, 20))
        ));
        Recipes.recipes.addRecipe("genetic_polymerizer", new BaseMachineRecipe(
                new Input(
                        new FluidStack(FluidName.fluidcropgenetic.getInstance(), 1000),
                        input.getInput(new ItemStack(IUItem.genome_crop, 1, 19)),
                        input.getInput(new ItemStack(Items.GLASS_BOTTLE)),
                        input.getInput(new ItemStack(IUItem.nether_star_ingot, 2)),
                        input.getInput(new ItemStack(IUItem.crafting_elements, 10, 446)),
                        input.getInput(new ItemStack(IUItem.radiationresources, 2, 0))
                ),
                new RecipeOutput(null, new ItemStack(IUItem.genome_crop, 1, 21))
        ));
    }


    public int gaugeLiquidScaled(int i) {
        return this.getFluidTank().getFluidAmount() <= 0
                ? 0
                : this.getFluidTank().getFluidAmount() * i / this.getFluidTank().getCapacity();
    }


    @SideOnly(Side.CLIENT)
    public GuiScreen getGui(EntityPlayer entityPlayer, boolean isAdmin) {
        return new GuiGeneticPolymerizer(getGuiContainer(entityPlayer));

    }


    public Set<UpgradableProperty> getUpgradableProperties() {
        return EnumSet.of(
                UpgradableProperty.Processing,
                UpgradableProperty.Transformer,
                UpgradableProperty.EnergyStorage,
                UpgradableProperty.ItemExtract,
                UpgradableProperty.ItemInput,
                UpgradableProperty.FluidExtract
        );
    }

}
