package com.denfop.tiles.mechanism;

import com.denfop.IUItem;
import com.denfop.api.Recipes;
import com.denfop.api.recipe.BaseMachineRecipe;
import com.denfop.api.recipe.IHasRecipe;
import com.denfop.api.recipe.IUpdateTick;
import com.denfop.api.recipe.Input;
import com.denfop.api.recipe.InvSlotOutput;
import com.denfop.api.recipe.InvSlotRecipes;
import com.denfop.api.recipe.MachineRecipe;
import com.denfop.api.recipe.RecipeOutput;
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
import com.denfop.container.ContainerGeneticTransposer;
import com.denfop.gui.GuiGeneticTransposer;
import com.denfop.invslot.InvSlot;
import com.denfop.invslot.InvSlotFluidByList;
import com.denfop.invslot.InvSlotUpgrade;
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

public class TileEntityGeneticTransposer extends TileElectricLiquidTankInventory implements IHasRecipe, IUpdateTick,
        IUpgradableBlock {


    public final InvSlotFluidByList fluidSlot;
    public final InvSlotOutput outputSlot1;
    public final InvSlotUpgrade upgradeSlot;
    public final ComponentUpgradeSlots componentUpgrade;
    public final ComponentProgress componentProgress;
    public final ComponentProcess componentProcess;
    private final SoilPollutionComponent pollutionSoil;
    private final AirPollutionComponent pollutionAir;
    private final ComponentUpgrade componentUpgrades;

    public InvSlotRecipes inputSlotA;
    public MachineRecipe output;

    public TileEntityGeneticTransposer() {
        super(
                300,
                1,
                12,
                Fluids.fluidPredicate(FluidName.fluidbeegenetic.getInstance(), FluidName.fluidcropgenetic.getInstance())
        );
        this.outputSlot1 = new InvSlotOutput(this, 1);
        this.fluidSlot = new InvSlotFluidByList(
                this,
                1,
                FluidName.fluidbeegenetic.getInstance(),
                FluidName.fluidcropgenetic.getInstance()
        );

        this.upgradeSlot = new com.denfop.invslot.InvSlotUpgrade(this, 4);
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

        this.inputSlotA = new InvSlotRecipes(this, "genetic_transposer", this, this.fluidTank);
        this.componentProcess.setInvSlotRecipes(inputSlotA);
        this.inputSlotA.setInvSlotConsumableLiquidByList(this.fluidSlot);
        fluidTank.setTypeItemSlot(InvSlot.TypeItemSlot.INPUT);
        this.pollutionSoil = this.addComponent(new SoilPollutionComponent(this, 0.1));
        this.pollutionAir = this.addComponent(new AirPollutionComponent(this, 0.25));
        Recipes.recipes.addInitRecipes(this);
    }

    public ContainerGeneticTransposer getGuiContainer(EntityPlayer entityPlayer) {
        return new ContainerGeneticTransposer(entityPlayer, this);

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
        return BlockBaseMachine3.genetic_transposer;
    }

    public BlockTileEntity getBlock() {
        return IUItem.basemachine2;
    }

    public void init() {
        final IInputHandler input = com.denfop.api.Recipes.inputFactory;
        Recipes.recipes.addRecipe("genetic_transposer", new BaseMachineRecipe(
                new Input(
                        new FluidStack(FluidName.fluidbeegenetic.getInstance(), 500),
                        input.getInput(new ItemStack(Items.GLASS_BOTTLE)),
                        input.getInput(new ItemStack(Items.NETHER_STAR, 1)),
                        input.getInput(new ItemStack(IUItem.crafting_elements, 4, 445)),
                        input.getInput(new ItemStack(IUItem.toriy))
                ),
                new RecipeOutput(null, new ItemStack(IUItem.genome_bee, 1, 0))
        ));
        Recipes.recipes.addRecipe("genetic_transposer", new BaseMachineRecipe(
                new Input(
                        new FluidStack(FluidName.fluidbeegenetic.getInstance(), 500),
                        input.getInput(new ItemStack(Items.GLASS_BOTTLE)),
                        input.getInput(new ItemStack(Items.NETHER_STAR, 1)),
                        input.getInput(new ItemStack(IUItem.crafting_elements, 4, 445)),
                        input.getInput(new ItemStack(IUItem.crafting_elements, 1, 446))
                ),
                new RecipeOutput(null, new ItemStack(IUItem.genome_bee, 1, 6))
        ));
        Recipes.recipes.addRecipe("genetic_transposer", new BaseMachineRecipe(
                new Input(
                        new FluidStack(FluidName.fluidbeegenetic.getInstance(), 500),
                        input.getInput(new ItemStack(Items.GLASS_BOTTLE)),
                        input.getInput(new ItemStack(Items.NETHER_STAR, 1)),
                        input.getInput(new ItemStack(IUItem.crafting_elements, 4, 445)),
                        input.getInput(new ItemStack(IUItem.radiationresources, 1, 4))
                ),
                new RecipeOutput(null, new ItemStack(IUItem.genome_bee, 1, 37))
        ));
        Recipes.recipes.addRecipe("genetic_transposer", new BaseMachineRecipe(
                new Input(
                        new FluidStack(FluidName.fluidbeegenetic.getInstance(), 500),
                        input.getInput(new ItemStack(Items.GLASS_BOTTLE)),
                        input.getInput(new ItemStack(Items.NETHER_STAR, 1)),
                        input.getInput(new ItemStack(IUItem.crafting_elements, 4, 445)),
                        input.getInput(new ItemStack(IUItem.nuclear_res, 1, 3))
                ),
                new RecipeOutput(null, new ItemStack(IUItem.genome_bee, 1, 36))
        ));
        Recipes.recipes.addRecipe("genetic_transposer", new BaseMachineRecipe(
                new Input(
                        new FluidStack(FluidName.fluidbeegenetic.getInstance(), 500),
                        input.getInput(new ItemStack(Items.GLASS_BOTTLE)),
                        input.getInput(new ItemStack(Items.NETHER_STAR, 1)),
                        input.getInput(new ItemStack(IUItem.crafting_elements, 4, 445)),
                        input.getInput(new ItemStack(IUItem.radiationresources, 1, 3))
                ),
                new RecipeOutput(null, new ItemStack(IUItem.genome_bee, 1, 41))
        ));
        Recipes.recipes.addRecipe("genetic_transposer", new BaseMachineRecipe(
                new Input(
                        new FluidStack(FluidName.fluidbeegenetic.getInstance(), 500),
                        input.getInput(new ItemStack(Items.GLASS_BOTTLE)),
                        input.getInput(new ItemStack(Items.NETHER_STAR, 1)),
                        input.getInput(new ItemStack(IUItem.crafting_elements, 4, 445)),
                        input.getInput(new ItemStack(IUItem.radiationresources, 1, 2))
                ),
                new RecipeOutput(null, new ItemStack(IUItem.genome_bee, 1, 24))
        ));
        Recipes.recipes.addRecipe("genetic_transposer", new BaseMachineRecipe(
                new Input(
                        new FluidStack(FluidName.fluidbeegenetic.getInstance(), 500),
                        input.getInput(new ItemStack(Items.GLASS_BOTTLE)),
                        input.getInput(new ItemStack(Items.NETHER_STAR, 1)),
                        input.getInput(new ItemStack(IUItem.crafting_elements, 4, 445)),
                        input.getInput(new ItemStack(IUItem.nuclear_res, 1, 2))
                ),
                new RecipeOutput(null, new ItemStack(IUItem.genome_bee, 1, 38))
        ));
        Recipes.recipes.addRecipe("genetic_transposer", new BaseMachineRecipe(
                new Input(
                        new FluidStack(FluidName.fluidbeegenetic.getInstance(), 500),
                        input.getInput(new ItemStack(Items.GLASS_BOTTLE)),
                        input.getInput(new ItemStack(Items.NETHER_STAR, 1)),
                        input.getInput(new ItemStack(IUItem.crafting_elements, 4, 445)),
                        input.getInput(new ItemStack(IUItem.radiationresources, 1, 1))
                ),
                new RecipeOutput(null, new ItemStack(IUItem.genome_bee, 1, 33))
        ));
        Recipes.recipes.addRecipe("genetic_transposer", new BaseMachineRecipe(
                new Input(
                        new FluidStack(FluidName.fluidbeegenetic.getInstance(), 500),
                        input.getInput(new ItemStack(Items.GLASS_BOTTLE)),
                        input.getInput(new ItemStack(Items.NETHER_STAR, 1)),
                        input.getInput(new ItemStack(IUItem.crafting_elements, 4, 445)),
                        input.getInput(new ItemStack(IUItem.proton))
                ),
                new RecipeOutput(null, new ItemStack(IUItem.genome_bee, 1, 7))
        ));
        Recipes.recipes.addRecipe("genetic_transposer", new BaseMachineRecipe(
                new Input(
                        new FluidStack(FluidName.fluidbeegenetic.getInstance(), 500),
                        input.getInput(new ItemStack(Items.GLASS_BOTTLE)),
                        input.getInput(new ItemStack(Items.NETHER_STAR, 1)),
                        input.getInput(new ItemStack(IUItem.crafting_elements, 4, 445)),
                        input.getInput(new ItemStack(IUItem.radiationresources, 1, 0))
                ),
                new RecipeOutput(null, new ItemStack(IUItem.genome_bee, 1, 3))
        ));


        Recipes.recipes.addRecipe("genetic_transposer", new BaseMachineRecipe(
                new Input(
                        new FluidStack(FluidName.fluidbeegenetic.getInstance(), 500),
                        input.getInput(new ItemStack(Items.GLASS_BOTTLE)),
                        input.getInput(new ItemStack(Items.NETHER_STAR, 1)),
                        input.getInput(new ItemStack(IUItem.crafting_elements, 4, 446)),
                        input.getInput(new ItemStack(IUItem.proton))
                ),
                new RecipeOutput(null, new ItemStack(IUItem.genome_bee, 1, 10))
        ));

        Recipes.recipes.addRecipe("genetic_transposer", new BaseMachineRecipe(
                new Input(
                        new FluidStack(FluidName.fluidbeegenetic.getInstance(), 500),
                        input.getInput(new ItemStack(Items.GLASS_BOTTLE)),
                        input.getInput(new ItemStack(Items.NETHER_STAR, 1)),
                        input.getInput(new ItemStack(IUItem.crafting_elements, 4, 446)),
                        input.getInput(new ItemStack(IUItem.radiationresources, 1, 3))
                ),
                new RecipeOutput(null, new ItemStack(IUItem.genome_bee, 1, 47))
        ));
        Recipes.recipes.addRecipe("genetic_transposer", new BaseMachineRecipe(
                new Input(
                        new FluidStack(FluidName.fluidbeegenetic.getInstance(), 500),
                        input.getInput(new ItemStack(Items.GLASS_BOTTLE)),
                        input.getInput(new ItemStack(Items.NETHER_STAR, 1)),
                        input.getInput(new ItemStack(IUItem.crafting_elements, 4, 446)),
                        input.getInput(new ItemStack(IUItem.nuclear_res, 1, 3))
                ),
                new RecipeOutput(null, new ItemStack(IUItem.genome_bee, 1, 44))
        ));
        Recipes.recipes.addRecipe("genetic_transposer", new BaseMachineRecipe(
                new Input(
                        new FluidStack(FluidName.fluidbeegenetic.getInstance(), 500),
                        input.getInput(new ItemStack(Items.GLASS_BOTTLE)),
                        input.getInput(new ItemStack(Items.NETHER_STAR, 1)),
                        input.getInput(new ItemStack(IUItem.crafting_elements, 4, 446)),
                        input.getInput(new ItemStack(IUItem.radiationresources, 1, 2))
                ),
                new RecipeOutput(null, new ItemStack(IUItem.genome_bee, 1, 12))
        ));
        Recipes.recipes.addRecipe("genetic_transposer", new BaseMachineRecipe(
                new Input(
                        new FluidStack(FluidName.fluidbeegenetic.getInstance(), 500),
                        input.getInput(new ItemStack(Items.GLASS_BOTTLE)),
                        input.getInput(new ItemStack(Items.NETHER_STAR, 1)),
                        input.getInput(new ItemStack(IUItem.crafting_elements, 4, 446)),
                        input.getInput(new ItemStack(IUItem.radiationresources, 1, 4))
                ),
                new RecipeOutput(null, new ItemStack(IUItem.genome_bee, 1, 21))
        ));
        Recipes.recipes.addRecipe("genetic_transposer", new BaseMachineRecipe(
                new Input(
                        new FluidStack(FluidName.fluidbeegenetic.getInstance(), 500),
                        input.getInput(new ItemStack(Items.GLASS_BOTTLE)),
                        input.getInput(new ItemStack(Items.NETHER_STAR, 1)),
                        input.getInput(new ItemStack(IUItem.crafting_elements, 4, 446)),
                        input.getInput(new ItemStack(IUItem.toriy))
                ),
                new RecipeOutput(null, new ItemStack(IUItem.genome_bee, 1, 30))
        ));
        Recipes.recipes.addRecipe("genetic_transposer", new BaseMachineRecipe(
                new Input(
                        new FluidStack(FluidName.fluidbeegenetic.getInstance(), 500),
                        input.getInput(new ItemStack(Items.GLASS_BOTTLE)),
                        input.getInput(new ItemStack(Items.NETHER_STAR, 1)),
                        input.getInput(new ItemStack(IUItem.crafting_elements, 4, 446)),
                        input.getInput(new ItemStack(IUItem.radiationresources, 1, 0))
                ),
                new RecipeOutput(null, new ItemStack(IUItem.genome_bee, 1, 18))
        ));
        Recipes.recipes.addRecipe("genetic_transposer", new BaseMachineRecipe(
                new Input(
                        new FluidStack(FluidName.fluidbeegenetic.getInstance(), 500),
                        input.getInput(new ItemStack(Items.GLASS_BOTTLE)),
                        input.getInput(new ItemStack(Items.NETHER_STAR, 1)),
                        input.getInput(new ItemStack(IUItem.crafting_elements, 4, 446)),
                        input.getInput(new ItemStack(IUItem.radiationresources, 1, 1))
                ),
                new RecipeOutput(null, new ItemStack(IUItem.genome_bee, 1, 27))
        ));
        Recipes.recipes.addRecipe("genetic_transposer", new BaseMachineRecipe(
                new Input(
                        new FluidStack(FluidName.fluidbeegenetic.getInstance(), 500),
                        input.getInput(new ItemStack(Items.GLASS_BOTTLE)),
                        input.getInput(new ItemStack(Items.NETHER_STAR, 1)),
                        input.getInput(new ItemStack(IUItem.crafting_elements, 4, 446)),
                        input.getInput(new ItemStack(IUItem.nuclear_res, 1, 2))
                ),
                new RecipeOutput(null, new ItemStack(IUItem.genome_bee, 1, 15))
        ));
        //
        Recipes.recipes.addRecipe("genetic_transposer", new BaseMachineRecipe(
                new Input(
                        new FluidStack(FluidName.fluidcropgenetic.getInstance(), 500),
                        input.getInput(new ItemStack(Items.GLASS_BOTTLE)),
                        input.getInput(new ItemStack(IUItem.nether_star_ingot, 1)),
                        input.getInput(new ItemStack(IUItem.crafting_elements, 4, 445)),
                        input.getInput(new ItemStack(IUItem.toriy))
                ),
                new RecipeOutput(null, new ItemStack(IUItem.genome_crop, 1, 0))
        ));
        Recipes.recipes.addRecipe("genetic_transposer", new BaseMachineRecipe(
                new Input(
                        new FluidStack(FluidName.fluidcropgenetic.getInstance(), 500),
                        input.getInput(new ItemStack(Items.GLASS_BOTTLE)),
                        input.getInput(new ItemStack(IUItem.nether_star_ingot, 1)),
                        input.getInput(new ItemStack(IUItem.crafting_elements, 4, 445)),
                        input.getInput(new ItemStack(IUItem.radiationresources, 1, 4))
                ),
                new RecipeOutput(null, new ItemStack(IUItem.genome_crop, 1, 36))
        ));
        Recipes.recipes.addRecipe("genetic_transposer", new BaseMachineRecipe(
                new Input(
                        new FluidStack(FluidName.fluidcropgenetic.getInstance(), 500),
                        input.getInput(new ItemStack(Items.GLASS_BOTTLE)),
                        input.getInput(new ItemStack(IUItem.nether_star_ingot, 1)),
                        input.getInput(new ItemStack(IUItem.crafting_elements, 4, 445)),
                        input.getInput(new ItemStack(IUItem.nuclear_res, 1, 3))
                ),
                new RecipeOutput(null, new ItemStack(IUItem.genome_crop, 1, 35))
        ));
        Recipes.recipes.addRecipe("genetic_transposer", new BaseMachineRecipe(
                new Input(
                        new FluidStack(FluidName.fluidcropgenetic.getInstance(), 500),
                        input.getInput(new ItemStack(Items.GLASS_BOTTLE)),
                        input.getInput(new ItemStack(IUItem.nether_star_ingot, 1)),
                        input.getInput(new ItemStack(IUItem.crafting_elements, 4, 445)),
                        input.getInput(new ItemStack(IUItem.radiationresources, 1, 3))
                ),
                new RecipeOutput(null, new ItemStack(IUItem.genome_crop, 1, 40))
        ));
        Recipes.recipes.addRecipe("genetic_transposer", new BaseMachineRecipe(
                new Input(
                        new FluidStack(FluidName.fluidcropgenetic.getInstance(), 500),
                        input.getInput(new ItemStack(Items.GLASS_BOTTLE)),
                        input.getInput(new ItemStack(IUItem.nether_star_ingot, 1)),
                        input.getInput(new ItemStack(IUItem.crafting_elements, 4, 445)),
                        input.getInput(new ItemStack(IUItem.radiationresources, 1, 2))
                ),
                new RecipeOutput(null, new ItemStack(IUItem.genome_crop, 1, 28))
        ));
        Recipes.recipes.addRecipe("genetic_transposer", new BaseMachineRecipe(
                new Input(
                        new FluidStack(FluidName.fluidcropgenetic.getInstance(), 500),
                        input.getInput(new ItemStack(Items.GLASS_BOTTLE)),
                        input.getInput(new ItemStack(IUItem.nether_star_ingot, 1)),
                        input.getInput(new ItemStack(IUItem.crafting_elements, 4, 445)),
                        input.getInput(new ItemStack(IUItem.nuclear_res, 1, 2))
                ),
                new RecipeOutput(null, new ItemStack(IUItem.genome_crop, 1, 37))
        ));
        Recipes.recipes.addRecipe("genetic_transposer", new BaseMachineRecipe(
                new Input(
                        new FluidStack(FluidName.fluidcropgenetic.getInstance(), 500),
                        input.getInput(new ItemStack(Items.GLASS_BOTTLE)),
                        input.getInput(new ItemStack(IUItem.nether_star_ingot, 1)),
                        input.getInput(new ItemStack(IUItem.crafting_elements, 4, 445)),
                        input.getInput(new ItemStack(IUItem.radiationresources, 1, 1))
                ),
                new RecipeOutput(null, new ItemStack(IUItem.genome_crop, 1, 13))
        ));
        Recipes.recipes.addRecipe("genetic_transposer", new BaseMachineRecipe(
                new Input(
                        new FluidStack(FluidName.fluidcropgenetic.getInstance(), 500),
                        input.getInput(new ItemStack(Items.GLASS_BOTTLE)),
                        input.getInput(new ItemStack(IUItem.nether_star_ingot, 1)),
                        input.getInput(new ItemStack(IUItem.crafting_elements, 4, 445)),
                        input.getInput(new ItemStack(IUItem.proton))
                ),
                new RecipeOutput(null, new ItemStack(IUItem.genome_crop, 1, 7))
        ));
        Recipes.recipes.addRecipe("genetic_transposer", new BaseMachineRecipe(
                new Input(
                        new FluidStack(FluidName.fluidcropgenetic.getInstance(), 500),
                        input.getInput(new ItemStack(Items.GLASS_BOTTLE)),
                        input.getInput(new ItemStack(IUItem.nether_star_ingot, 1)),
                        input.getInput(new ItemStack(IUItem.crafting_elements, 4, 445)),
                        input.getInput(new ItemStack(IUItem.radiationresources, 1, 0))
                ),
                new RecipeOutput(null, new ItemStack(IUItem.genome_crop, 1, 3))
        ));
        Recipes.recipes.addRecipe("genetic_transposer", new BaseMachineRecipe(
                new Input(
                        new FluidStack(FluidName.fluidcropgenetic.getInstance(), 500),
                        input.getInput(new ItemStack(Items.GLASS_BOTTLE)),
                        input.getInput(new ItemStack(IUItem.nether_star_ingot, 1)),
                        input.getInput(new ItemStack(IUItem.crafting_elements, 4, 446)),
                        input.getInput(new ItemStack(IUItem.crafting_elements, 4, 445))
                ),
                new RecipeOutput(null, new ItemStack(IUItem.genome_crop, 1, 7))
        ));

        Recipes.recipes.addRecipe("genetic_transposer", new BaseMachineRecipe(
                new Input(
                        new FluidStack(FluidName.fluidcropgenetic.getInstance(), 500),
                        input.getInput(new ItemStack(Items.GLASS_BOTTLE)),
                        input.getInput(new ItemStack(IUItem.nether_star_ingot, 1)),
                        input.getInput(new ItemStack(IUItem.crafting_elements, 4, 446)),
                        input.getInput(new ItemStack(IUItem.proton))
                ),
                new RecipeOutput(null, new ItemStack(IUItem.genome_crop, 1, 10))
        ));

        Recipes.recipes.addRecipe("genetic_transposer", new BaseMachineRecipe(
                new Input(
                        new FluidStack(FluidName.fluidcropgenetic.getInstance(), 500),
                        input.getInput(new ItemStack(Items.GLASS_BOTTLE)),
                        input.getInput(new ItemStack(IUItem.nether_star_ingot, 1)),
                        input.getInput(new ItemStack(IUItem.crafting_elements, 4, 446)),
                        input.getInput(new ItemStack(IUItem.radiationresources, 1, 3))
                ),
                new RecipeOutput(null, new ItemStack(IUItem.genome_crop, 1, 46))
        ));
        Recipes.recipes.addRecipe("genetic_transposer", new BaseMachineRecipe(
                new Input(
                        new FluidStack(FluidName.fluidcropgenetic.getInstance(), 500),
                        input.getInput(new ItemStack(Items.GLASS_BOTTLE)),
                        input.getInput(new ItemStack(IUItem.nether_star_ingot, 1)),
                        input.getInput(new ItemStack(IUItem.crafting_elements, 4, 446)),
                        input.getInput(new ItemStack(IUItem.nuclear_res, 1, 3))
                ),
                new RecipeOutput(null, new ItemStack(IUItem.genome_crop, 1, 43))
        ));
        Recipes.recipes.addRecipe("genetic_transposer", new BaseMachineRecipe(
                new Input(
                        new FluidStack(FluidName.fluidcropgenetic.getInstance(), 500),
                        input.getInput(new ItemStack(Items.GLASS_BOTTLE)),
                        input.getInput(new ItemStack(IUItem.nether_star_ingot, 1)),
                        input.getInput(new ItemStack(IUItem.crafting_elements, 4, 446)),
                        input.getInput(new ItemStack(IUItem.radiationresources, 1, 2))
                ),
                new RecipeOutput(null, new ItemStack(IUItem.genome_crop, 1, 22))
        ));
        Recipes.recipes.addRecipe("genetic_transposer", new BaseMachineRecipe(
                new Input(
                        new FluidStack(FluidName.fluidcropgenetic.getInstance(), 500),
                        input.getInput(new ItemStack(Items.GLASS_BOTTLE)),
                        input.getInput(new ItemStack(IUItem.nether_star_ingot, 1)),
                        input.getInput(new ItemStack(IUItem.crafting_elements, 4, 446)),
                        input.getInput(new ItemStack(IUItem.radiationresources, 1, 4))
                ),
                new RecipeOutput(null, new ItemStack(IUItem.genome_crop, 1, 31))
        ));
        Recipes.recipes.addRecipe("genetic_transposer", new BaseMachineRecipe(
                new Input(
                        new FluidStack(FluidName.fluidcropgenetic.getInstance(), 500),
                        input.getInput(new ItemStack(Items.GLASS_BOTTLE)),
                        input.getInput(new ItemStack(IUItem.nether_star_ingot, 1)),
                        input.getInput(new ItemStack(IUItem.crafting_elements, 4, 446)),
                        input.getInput(new ItemStack(IUItem.toriy))
                ),
                new RecipeOutput(null, new ItemStack(IUItem.genome_crop, 1, 25))
        ));
        Recipes.recipes.addRecipe("genetic_transposer", new BaseMachineRecipe(
                new Input(
                        new FluidStack(FluidName.fluidcropgenetic.getInstance(), 500),
                        input.getInput(new ItemStack(Items.GLASS_BOTTLE)),
                        input.getInput(new ItemStack(IUItem.nether_star_ingot, 1)),
                        input.getInput(new ItemStack(IUItem.crafting_elements, 4, 446)),
                        input.getInput(new ItemStack(IUItem.radiationresources, 1, 0))
                ),
                new RecipeOutput(null, new ItemStack(IUItem.genome_crop, 1, 19))
        ));
        Recipes.recipes.addRecipe("genetic_transposer", new BaseMachineRecipe(
                new Input(
                        new FluidStack(FluidName.fluidcropgenetic.getInstance(), 500),
                        input.getInput(new ItemStack(Items.GLASS_BOTTLE)),
                        input.getInput(new ItemStack(IUItem.nether_star_ingot, 1)),
                        input.getInput(new ItemStack(IUItem.crafting_elements, 4, 446)),
                        input.getInput(new ItemStack(IUItem.radiationresources, 1, 1))
                ),
                new RecipeOutput(null, new ItemStack(IUItem.genome_crop, 1, 34))
        ));
        Recipes.recipes.addRecipe("genetic_transposer", new BaseMachineRecipe(
                new Input(
                        new FluidStack(FluidName.fluidcropgenetic.getInstance(), 500),
                        input.getInput(new ItemStack(Items.GLASS_BOTTLE)),
                        input.getInput(new ItemStack(IUItem.nether_star_ingot, 1)),
                        input.getInput(new ItemStack(IUItem.crafting_elements, 4, 446)),
                        input.getInput(new ItemStack(IUItem.nuclear_res, 1, 2))
                ),
                new RecipeOutput(null, new ItemStack(IUItem.genome_crop, 1, 12))
        ));
    }


    public int gaugeLiquidScaled(int i) {
        return this.getFluidTank().getFluidAmount() <= 0
                ? 0
                : this.getFluidTank().getFluidAmount() * i / this.getFluidTank().getCapacity();
    }


    @SideOnly(Side.CLIENT)
    public GuiScreen getGui(EntityPlayer entityPlayer, boolean isAdmin) {
        return new GuiGeneticTransposer(getGuiContainer(entityPlayer));

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
