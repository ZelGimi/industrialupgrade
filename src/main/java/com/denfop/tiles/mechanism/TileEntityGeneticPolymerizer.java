package com.denfop.tiles.mechanism;

import com.denfop.IUItem;
import com.denfop.api.Recipes;
import com.denfop.api.inv.IAdvInventory;
import com.denfop.api.recipe.*;
import com.denfop.api.tile.IMultiTileBlock;
import com.denfop.api.upgrades.IUpgradableBlock;
import com.denfop.api.upgrades.UpgradableProperty;
import com.denfop.blocks.BlockTileEntity;
import com.denfop.blocks.FluidName;
import com.denfop.blocks.mechanism.BlockBaseMachine3;
import com.denfop.componets.*;
import com.denfop.container.ContainerBase;
import com.denfop.container.ContainerGeneticPolymerizer;
import com.denfop.gui.GuiCore;
import com.denfop.gui.GuiGeneticPolymerizer;
import com.denfop.invslot.InvSlot;
import com.denfop.invslot.InvSlotFluidByList;
import com.denfop.invslot.InvSlotUpgrade;
import com.denfop.recipe.IInputHandler;
import com.denfop.tiles.base.TileElectricLiquidTankInventory;
import net.minecraft.core.BlockPos;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.block.state.BlockState;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.api.distmarker.OnlyIn;
import net.neoforged.neoforge.fluids.FluidStack;

import java.util.EnumSet;
import java.util.Set;

public class TileEntityGeneticPolymerizer extends TileElectricLiquidTankInventory implements IHasRecipe, IUpdateTick,
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

    public TileEntityGeneticPolymerizer(BlockPos pos, BlockState state) {
        super(
                300,
                1,
                12,
                Fluids.fluidPredicate(FluidName.fluidbeegenetic.getInstance().get(), FluidName.fluidcropgenetic.getInstance().get()), BlockBaseMachine3.genetic_polymerizer, pos, state
        );
        this.outputSlot1 = new InvSlotOutput(this, 1);
        this.fluidSlot = new InvSlotFluidByList(
                this,
                1,
                FluidName.fluidbeegenetic.getInstance().get(),
                FluidName.fluidcropgenetic.getInstance().get()
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

        this.inputSlotA = new InvSlotRecipes(this, "genetic_polymerizer", this, this.fluidTank);
        this.componentProcess.setInvSlotRecipes(inputSlotA);
        this.inputSlotA.setInvSlotConsumableLiquidByList(this.fluidSlot);
        fluidTank.setTypeItemSlot(InvSlot.TypeItemSlot.INPUT);
        this.pollutionSoil = this.addComponent(new SoilPollutionComponent(this, 0.1));
        this.pollutionAir = this.addComponent(new AirPollutionComponent(this, 0.25));
        Recipes.recipes.addInitRecipes(this);
    }

    public ContainerGeneticPolymerizer getGuiContainer(Player entityPlayer) {
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
        return IUItem.basemachine2.getBlock(getTeBlock());
    }

    public void init() {
        final IInputHandler input = com.denfop.api.Recipes.inputFactory;
        Recipes.recipes.addRecipe("genetic_polymerizer", new BaseMachineRecipe(
                new Input(
                        new FluidStack(FluidName.fluidbeegenetic.getInstance().get(), 500),
                        input.getInput(new ItemStack(IUItem.genome_bee.getStack(0))),
                        input.getInput(new ItemStack(Items.GLASS_BOTTLE)),
                        input.getInput(new ItemStack(Items.NETHER_STAR, 1)),
                        input.getInput(new ItemStack(IUItem.crafting_elements.getStack(445), 8)),
                        input.getInput(new ItemStack(IUItem.toriy.getItem(), 2))
                ),
                new RecipeOutput(null, new ItemStack(IUItem.genome_bee.getStack(1)))
        ));
        Recipes.recipes.addRecipe("genetic_polymerizer", new BaseMachineRecipe(
                new Input(
                        new FluidStack(FluidName.fluidbeegenetic.getInstance().get(), 1000),
                        input.getInput(new ItemStack(IUItem.genome_bee.getStack(1))),
                        input.getInput(new ItemStack(Items.GLASS_BOTTLE)),
                        input.getInput(new ItemStack(Items.NETHER_STAR, 2)),
                        input.getInput(new ItemStack(IUItem.crafting_elements.getStack(445), 10)),
                        input.getInput(new ItemStack(IUItem.toriy.getItem(), 2))
                ),
                new RecipeOutput(null, new ItemStack(IUItem.genome_bee.getStack(2)))
        ));

        Recipes.recipes.addRecipe("genetic_polymerizer", new BaseMachineRecipe(
                new Input(
                        new FluidStack(FluidName.fluidbeegenetic.getInstance().get(), 500),
                        input.getInput(new ItemStack(IUItem.genome_bee.getStack(3))),
                        input.getInput(new ItemStack(Items.GLASS_BOTTLE)),
                        input.getInput(new ItemStack(Items.NETHER_STAR, 1)),
                        input.getInput(new ItemStack(IUItem.crafting_elements.getStack(445), 8)),
                        input.getInput(new ItemStack(IUItem.radiationresources.getStack(4), 2))
                ),
                new RecipeOutput(null, new ItemStack(IUItem.genome_bee.getStack(4)))
        ));
        Recipes.recipes.addRecipe("genetic_polymerizer", new BaseMachineRecipe(
                new Input(
                        new FluidStack(FluidName.fluidbeegenetic.getInstance().get(), 1000),
                        input.getInput(new ItemStack(IUItem.genome_bee.getStack(4))),
                        input.getInput(new ItemStack(Items.GLASS_BOTTLE)),
                        input.getInput(new ItemStack(Items.NETHER_STAR, 2)),
                        input.getInput(new ItemStack(IUItem.crafting_elements.getStack(445), 10)),
                        input.getInput(new ItemStack(IUItem.radiationresources.getStack(4), 2))
                ),
                new RecipeOutput(null, new ItemStack(IUItem.genome_bee.getStack(5)))
        ));

        Recipes.recipes.addRecipe("genetic_polymerizer", new BaseMachineRecipe(
                new Input(
                        new FluidStack(FluidName.fluidbeegenetic.getInstance().get(), 500),
                        input.getInput(new ItemStack(IUItem.genome_bee.getStack(7))),
                        input.getInput(new ItemStack(Items.GLASS_BOTTLE)),
                        input.getInput(new ItemStack(Items.NETHER_STAR, 1)),
                        input.getInput(new ItemStack(IUItem.crafting_elements.getStack(445), 8)),
                        input.getInput(new ItemStack(IUItem.proton.getItem(), 2))
                ),
                new RecipeOutput(null, new ItemStack(IUItem.genome_bee.getStack(8)))
        ));
        Recipes.recipes.addRecipe("genetic_polymerizer", new BaseMachineRecipe(
                new Input(
                        new FluidStack(FluidName.fluidbeegenetic.getInstance().get(), 1000),
                        input.getInput(new ItemStack(IUItem.genome_bee.getStack(8))),
                        input.getInput(new ItemStack(Items.GLASS_BOTTLE)),
                        input.getInput(new ItemStack(Items.NETHER_STAR, 2)),
                        input.getInput(new ItemStack(IUItem.crafting_elements.getStack(445), 10)),
                        input.getInput(new ItemStack(IUItem.proton.getItem(), 2))
                ),
                new RecipeOutput(null, new ItemStack(IUItem.genome_bee.getStack(9)))
        ));
        Recipes.recipes.addRecipe("genetic_polymerizer", new BaseMachineRecipe(
                new Input(
                        new FluidStack(FluidName.fluidbeegenetic.getInstance().get(), 500),
                        input.getInput(new ItemStack(IUItem.genome_bee.getStack(10))),
                        input.getInput(new ItemStack(Items.GLASS_BOTTLE)),
                        input.getInput(new ItemStack(Items.NETHER_STAR, 1)),
                        input.getInput(new ItemStack(IUItem.crafting_elements.getStack(445), 8)),
                        input.getInput(new ItemStack(IUItem.proton.getItem(), 2))
                ),
                new RecipeOutput(null, new ItemStack(IUItem.genome_bee.getStack(11)))
        ));
        Recipes.recipes.addRecipe("genetic_polymerizer", new BaseMachineRecipe(
                new Input(
                        new FluidStack(FluidName.fluidbeegenetic.getInstance().get(), 500),
                        input.getInput(new ItemStack(IUItem.genome_bee.getStack(12))),
                        input.getInput(new ItemStack(Items.GLASS_BOTTLE)),
                        input.getInput(new ItemStack(Items.NETHER_STAR, 1)),
                        input.getInput(new ItemStack(IUItem.crafting_elements.getStack(446), 8)),
                        input.getInput(new ItemStack(IUItem.radiationresources.getStack(2), 2))
                ),
                new RecipeOutput(null, new ItemStack(IUItem.genome_bee.getStack(13)))
        ));
        Recipes.recipes.addRecipe("genetic_polymerizer", new BaseMachineRecipe(
                new Input(
                        new FluidStack(FluidName.fluidbeegenetic.getInstance().get(), 1000),
                        input.getInput(new ItemStack(IUItem.genome_bee.getStack(13))),
                        input.getInput(new ItemStack(Items.GLASS_BOTTLE)),
                        input.getInput(new ItemStack(Items.NETHER_STAR, 2)),
                        input.getInput(new ItemStack(IUItem.crafting_elements.getStack(446), 10)),
                        input.getInput(new ItemStack(IUItem.radiationresources.getStack(2), 2))
                ),
                new RecipeOutput(null, new ItemStack(IUItem.genome_bee.getStack(14)))
        ));

        Recipes.recipes.addRecipe("genetic_polymerizer", new BaseMachineRecipe(
                new Input(
                        new FluidStack(FluidName.fluidbeegenetic.getInstance().get(), 500),
                        input.getInput(new ItemStack(IUItem.genome_bee.getStack(15))),
                        input.getInput(new ItemStack(Items.GLASS_BOTTLE)),
                        input.getInput(new ItemStack(Items.NETHER_STAR, 1)),
                        input.getInput(new ItemStack(IUItem.crafting_elements.getStack(446), 8)),
                        input.getInput(new ItemStack(IUItem.nuclear_res.getStack(2), 2))
                ),
                new RecipeOutput(null, new ItemStack(IUItem.genome_bee.getStack(16)))
        ));
        Recipes.recipes.addRecipe("genetic_polymerizer", new BaseMachineRecipe(
                new Input(
                        new FluidStack(FluidName.fluidbeegenetic.getInstance().get(), 1000),
                        input.getInput(new ItemStack(IUItem.genome_bee.getStack(16))),
                        input.getInput(new ItemStack(Items.GLASS_BOTTLE)),
                        input.getInput(new ItemStack(Items.NETHER_STAR, 2)),
                        input.getInput(new ItemStack(IUItem.crafting_elements.getStack(446), 10)),
                        input.getInput(new ItemStack(IUItem.nuclear_res.getStack(2), 2))
                ),
                new RecipeOutput(null, new ItemStack(IUItem.genome_bee.getStack(17)))
        ));

        Recipes.recipes.addRecipe("genetic_polymerizer", new BaseMachineRecipe(
                new Input(
                        new FluidStack(FluidName.fluidbeegenetic.getInstance().get(), 500),
                        input.getInput(new ItemStack(IUItem.genome_bee.getStack(18))),
                        input.getInput(new ItemStack(Items.GLASS_BOTTLE)),
                        input.getInput(new ItemStack(Items.NETHER_STAR, 1)),
                        input.getInput(new ItemStack(IUItem.crafting_elements.getStack(446), 8)),
                        input.getInput(new ItemStack(IUItem.radiationresources.getStack(0), 2))
                ),
                new RecipeOutput(null, new ItemStack(IUItem.genome_bee.getStack(19)))
        ));
        Recipes.recipes.addRecipe("genetic_polymerizer", new BaseMachineRecipe(
                new Input(
                        new FluidStack(FluidName.fluidbeegenetic.getInstance().get(), 1000),
                        input.getInput(new ItemStack(IUItem.genome_bee.getStack(19))),
                        input.getInput(new ItemStack(Items.GLASS_BOTTLE)),
                        input.getInput(new ItemStack(Items.NETHER_STAR, 2)),
                        input.getInput(new ItemStack(IUItem.crafting_elements.getStack(446), 10)),
                        input.getInput(new ItemStack(IUItem.radiationresources.getStack(0), 2))
                ),
                new RecipeOutput(null, new ItemStack(IUItem.genome_bee.getStack(20)))
        ));

        Recipes.recipes.addRecipe("genetic_polymerizer", new BaseMachineRecipe(
                new Input(
                        new FluidStack(FluidName.fluidbeegenetic.getInstance().get(), 500),
                        input.getInput(new ItemStack(IUItem.genome_bee.getStack(21))),
                        input.getInput(new ItemStack(Items.GLASS_BOTTLE)),
                        input.getInput(new ItemStack(Items.NETHER_STAR, 1)),
                        input.getInput(new ItemStack(IUItem.crafting_elements.getStack(446), 8)),
                        input.getInput(new ItemStack(IUItem.radiationresources.getStack(4), 2))
                ),
                new RecipeOutput(null, new ItemStack(IUItem.genome_bee.getStack(22)))
        ));
        Recipes.recipes.addRecipe("genetic_polymerizer", new BaseMachineRecipe(
                new Input(
                        new FluidStack(FluidName.fluidbeegenetic.getInstance().get(), 1000),
                        input.getInput(new ItemStack(IUItem.genome_bee.getStack(22))),
                        input.getInput(new ItemStack(Items.GLASS_BOTTLE)),
                        input.getInput(new ItemStack(Items.NETHER_STAR, 2)),
                        input.getInput(new ItemStack(IUItem.crafting_elements.getStack(446), 10)),
                        input.getInput(new ItemStack(IUItem.radiationresources.getStack(4), 2))
                ),
                new RecipeOutput(null, new ItemStack(IUItem.genome_bee.getStack(23)))
        ));

        Recipes.recipes.addRecipe("genetic_polymerizer", new BaseMachineRecipe(
                new Input(
                        new FluidStack(FluidName.fluidbeegenetic.getInstance().get(), 500),
                        input.getInput(new ItemStack(IUItem.genome_bee.getStack(24))),
                        input.getInput(new ItemStack(Items.GLASS_BOTTLE)),
                        input.getInput(new ItemStack(Items.NETHER_STAR, 1)),
                        input.getInput(new ItemStack(IUItem.crafting_elements.getStack(445), 8)),
                        input.getInput(new ItemStack(IUItem.radiationresources.getStack(2), 2))
                ),
                new RecipeOutput(null, new ItemStack(IUItem.genome_bee.getStack(25)))
        ));
        Recipes.recipes.addRecipe("genetic_polymerizer", new BaseMachineRecipe(
                new Input(
                        new FluidStack(FluidName.fluidbeegenetic.getInstance().get(), 1000),
                        input.getInput(new ItemStack(IUItem.genome_bee.getStack(25))),
                        input.getInput(new ItemStack(Items.GLASS_BOTTLE)),
                        input.getInput(new ItemStack(Items.NETHER_STAR, 2)),
                        input.getInput(new ItemStack(IUItem.crafting_elements.getStack(445), 10)),
                        input.getInput(new ItemStack(IUItem.radiationresources.getStack(2), 2))
                ),
                new RecipeOutput(null, new ItemStack(IUItem.genome_bee.getStack(26)))
        ));

        Recipes.recipes.addRecipe("genetic_polymerizer", new BaseMachineRecipe(
                new Input(
                        new FluidStack(FluidName.fluidbeegenetic.getInstance().get(), 500),
                        input.getInput(new ItemStack(IUItem.genome_bee.getStack(27))),
                        input.getInput(new ItemStack(Items.GLASS_BOTTLE)),
                        input.getInput(new ItemStack(Items.NETHER_STAR, 1)),
                        input.getInput(new ItemStack(IUItem.crafting_elements.getStack(446), 8)),
                        input.getInput(new ItemStack(IUItem.nuclear_res.getStack(2), 2))
                ),
                new RecipeOutput(null, new ItemStack(IUItem.genome_bee.getStack(28)))
        ));
        Recipes.recipes.addRecipe("genetic_polymerizer", new BaseMachineRecipe(
                new Input(
                        new FluidStack(FluidName.fluidbeegenetic.getInstance().get(), 1000),
                        input.getInput(new ItemStack(IUItem.genome_bee.getStack(28))),
                        input.getInput(new ItemStack(Items.GLASS_BOTTLE)),
                        input.getInput(new ItemStack(Items.NETHER_STAR, 2)),
                        input.getInput(new ItemStack(IUItem.crafting_elements.getStack(446), 10)),
                        input.getInput(new ItemStack(IUItem.nuclear_res.getStack(2), 2))
                ),
                new RecipeOutput(null, new ItemStack(IUItem.genome_bee.getStack(29)))
        ));
        Recipes.recipes.addRecipe("genetic_polymerizer", new BaseMachineRecipe(
                new Input(
                        new FluidStack(FluidName.fluidbeegenetic.getInstance().get(), 500),
                        input.getInput(new ItemStack(IUItem.genome_bee.getStack(30))),
                        input.getInput(new ItemStack(Items.GLASS_BOTTLE)),
                        input.getInput(new ItemStack(Items.NETHER_STAR, 1)),
                        input.getInput(new ItemStack(IUItem.crafting_elements.getStack(446), 8)),
                        input.getInput(new ItemStack(IUItem.toriy.getItem(), 2))
                ),
                new RecipeOutput(null, new ItemStack(IUItem.genome_bee.getStack(31)))
        ));
        Recipes.recipes.addRecipe("genetic_polymerizer", new BaseMachineRecipe(
                new Input(
                        new FluidStack(FluidName.fluidbeegenetic.getInstance().get(), 1000),
                        input.getInput(new ItemStack(IUItem.genome_bee.getStack(31))),
                        input.getInput(new ItemStack(Items.GLASS_BOTTLE)),
                        input.getInput(new ItemStack(Items.NETHER_STAR, 2)),
                        input.getInput(new ItemStack(IUItem.crafting_elements.getStack(446), 10)),
                        input.getInput(new ItemStack(IUItem.toriy.getItem(), 2))
                ),
                new RecipeOutput(null, new ItemStack(IUItem.genome_bee.getStack(32)))
        ));
        Recipes.recipes.addRecipe("genetic_polymerizer", new BaseMachineRecipe(
                new Input(
                        new FluidStack(FluidName.fluidbeegenetic.getInstance().get(), 500),
                        input.getInput(new ItemStack(IUItem.genome_bee.getStack(33))),
                        input.getInput(new ItemStack(Items.GLASS_BOTTLE)),
                        input.getInput(new ItemStack(Items.NETHER_STAR, 1)),
                        input.getInput(new ItemStack(IUItem.crafting_elements.getStack(445), 8)),
                        input.getInput(new ItemStack(IUItem.radiationresources.getStack(1), 2))
                ),
                new RecipeOutput(null, new ItemStack(IUItem.genome_bee.getStack(34)))
        ));
        Recipes.recipes.addRecipe("genetic_polymerizer", new BaseMachineRecipe(
                new Input(
                        new FluidStack(FluidName.fluidbeegenetic.getInstance().get(), 1000),
                        input.getInput(new ItemStack(IUItem.genome_bee.getStack(34))),
                        input.getInput(new ItemStack(Items.GLASS_BOTTLE)),
                        input.getInput(new ItemStack(Items.NETHER_STAR, 2)),
                        input.getInput(new ItemStack(IUItem.crafting_elements.getStack(445), 10)),
                        input.getInput(new ItemStack(IUItem.radiationresources.getStack(1), 2))
                ),
                new RecipeOutput(null, new ItemStack(IUItem.genome_bee.getStack(35)))
        ));

        Recipes.recipes.addRecipe("genetic_polymerizer", new BaseMachineRecipe(
                new Input(
                        new FluidStack(FluidName.fluidbeegenetic.getInstance().get(), 500),
                        input.getInput(new ItemStack(IUItem.genome_bee.getStack(38))),
                        input.getInput(new ItemStack(Items.GLASS_BOTTLE)),
                        input.getInput(new ItemStack(Items.NETHER_STAR, 1)),
                        input.getInput(new ItemStack(IUItem.crafting_elements.getStack(445), 8)),
                        input.getInput(new ItemStack(IUItem.nuclear_res.getStack(2), 2))
                ),
                new RecipeOutput(null, new ItemStack(IUItem.genome_bee.getStack(39)))
        ));
        Recipes.recipes.addRecipe("genetic_polymerizer", new BaseMachineRecipe(
                new Input(
                        new FluidStack(FluidName.fluidbeegenetic.getInstance().get(), 1000),
                        input.getInput(new ItemStack(IUItem.genome_bee.getStack(39))),
                        input.getInput(new ItemStack(Items.GLASS_BOTTLE)),
                        input.getInput(new ItemStack(Items.NETHER_STAR, 2)),
                        input.getInput(new ItemStack(IUItem.crafting_elements.getStack(445), 10)),
                        input.getInput(new ItemStack(IUItem.nuclear_res.getStack(2), 2))
                ),
                new RecipeOutput(null, new ItemStack(IUItem.genome_bee.getStack(40)))
        ));

        Recipes.recipes.addRecipe("genetic_polymerizer", new BaseMachineRecipe(
                new Input(
                        new FluidStack(FluidName.fluidbeegenetic.getInstance().get(), 500),
                        input.getInput(new ItemStack(IUItem.genome_bee.getStack(41))),
                        input.getInput(new ItemStack(Items.GLASS_BOTTLE)),
                        input.getInput(new ItemStack(Items.NETHER_STAR, 1)),
                        input.getInput(new ItemStack(IUItem.crafting_elements.getStack(445), 8)),
                        input.getInput(new ItemStack(IUItem.radiationresources.getStack(3), 2))
                ),
                new RecipeOutput(null, new ItemStack(IUItem.genome_bee.getStack(42)))
        ));
        Recipes.recipes.addRecipe("genetic_polymerizer", new BaseMachineRecipe(
                new Input(
                        new FluidStack(FluidName.fluidbeegenetic.getInstance().get(), 1000),
                        input.getInput(new ItemStack(IUItem.genome_bee.getStack(42))),
                        input.getInput(new ItemStack(Items.GLASS_BOTTLE)),
                        input.getInput(new ItemStack(Items.NETHER_STAR, 2)),
                        input.getInput(new ItemStack(IUItem.crafting_elements.getStack(445), 10)),
                        input.getInput(new ItemStack(IUItem.radiationresources.getStack(3), 2))
                ),
                new RecipeOutput(null, new ItemStack(IUItem.genome_bee.getStack(43)))
        ));
        Recipes.recipes.addRecipe("genetic_polymerizer", new BaseMachineRecipe(
                new Input(
                        new FluidStack(FluidName.fluidbeegenetic.getInstance().get(), 500),
                        input.getInput(new ItemStack(IUItem.genome_bee.getStack(44))),
                        input.getInput(new ItemStack(Items.GLASS_BOTTLE)),
                        input.getInput(new ItemStack(Items.NETHER_STAR, 1)),
                        input.getInput(new ItemStack(IUItem.crafting_elements.getStack(446), 8)),
                        input.getInput(new ItemStack(IUItem.nuclear_res.getStack(3), 2))
                ),
                new RecipeOutput(null, new ItemStack(IUItem.genome_bee.getStack(45)))
        ));
        Recipes.recipes.addRecipe("genetic_polymerizer", new BaseMachineRecipe(
                new Input(
                        new FluidStack(FluidName.fluidbeegenetic.getInstance().get(), 1000),
                        input.getInput(new ItemStack(IUItem.genome_bee.getStack(45))),
                        input.getInput(new ItemStack(Items.GLASS_BOTTLE)),
                        input.getInput(new ItemStack(Items.NETHER_STAR, 2)),
                        input.getInput(new ItemStack(IUItem.crafting_elements.getStack(446), 10)),
                        input.getInput(new ItemStack(IUItem.nuclear_res.getStack(3), 2))
                ),
                new RecipeOutput(null, new ItemStack(IUItem.genome_bee.getStack(46)))
        ));

        Recipes.recipes.addRecipe("genetic_polymerizer", new BaseMachineRecipe(
                new Input(
                        new FluidStack(FluidName.fluidbeegenetic.getInstance().get(), 500),
                        input.getInput(new ItemStack(IUItem.genome_bee.getStack(47))),
                        input.getInput(new ItemStack(Items.GLASS_BOTTLE)),
                        input.getInput(new ItemStack(Items.NETHER_STAR, 1)),
                        input.getInput(new ItemStack(IUItem.crafting_elements.getStack(446), 8)),
                        input.getInput(new ItemStack(IUItem.radiationresources.getStack(3), 2))
                ),
                new RecipeOutput(null, new ItemStack(IUItem.genome_bee.getStack(48)))
        ));
        Recipes.recipes.addRecipe("genetic_polymerizer", new BaseMachineRecipe(
                new Input(
                        new FluidStack(FluidName.fluidbeegenetic.getInstance().get(), 1000),
                        input.getInput(new ItemStack(IUItem.genome_bee.getStack(48))),
                        input.getInput(new ItemStack(Items.GLASS_BOTTLE)),
                        input.getInput(new ItemStack(Items.NETHER_STAR, 2)),
                        input.getInput(new ItemStack(IUItem.crafting_elements.getStack(446), 10)),
                        input.getInput(new ItemStack(IUItem.radiationresources.getStack(3), 2))
                ),
                new RecipeOutput(null, new ItemStack(IUItem.genome_bee.getStack(49)))
        ));


        Recipes.recipes.addRecipe("genetic_polymerizer", new BaseMachineRecipe(
                new Input(
                        new FluidStack(FluidName.fluidcropgenetic.getInstance().get(), 500),
                        input.getInput(new ItemStack(IUItem.genome_crop.getStack(0))),
                        input.getInput(new ItemStack(Items.GLASS_BOTTLE)),
                        input.getInput(new ItemStack(IUItem.nether_star_ingot.getItem(), 1)),
                        input.getInput(new ItemStack(IUItem.crafting_elements.getStack(445), 8)),
                        input.getInput(new ItemStack(IUItem.toriy.getItem(), 2))
                ),
                new RecipeOutput(null, new ItemStack(IUItem.genome_crop.getStack(1)))
        ));
        Recipes.recipes.addRecipe("genetic_polymerizer", new BaseMachineRecipe(
                new Input(
                        new FluidStack(FluidName.fluidcropgenetic.getInstance().get(), 1000),
                        input.getInput(new ItemStack(IUItem.genome_crop.getStack(1))),
                        input.getInput(new ItemStack(Items.GLASS_BOTTLE)),
                        input.getInput(new ItemStack(IUItem.nether_star_ingot.getItem(), 2)),
                        input.getInput(new ItemStack(IUItem.crafting_elements.getStack(445), 10)),
                        input.getInput(new ItemStack(IUItem.toriy.getItem(), 2))
                ),
                new RecipeOutput(null, new ItemStack(IUItem.genome_crop.getStack(2)))
        ));

        Recipes.recipes.addRecipe("genetic_polymerizer", new BaseMachineRecipe(
                new Input(
                        new FluidStack(FluidName.fluidcropgenetic.getInstance().get(), 500),
                        input.getInput(new ItemStack(IUItem.genome_crop.getStack(3))),
                        input.getInput(new ItemStack(Items.GLASS_BOTTLE)),
                        input.getInput(new ItemStack(IUItem.nether_star_ingot.getItem(), 1)),
                        input.getInput(new ItemStack(IUItem.crafting_elements.getStack(445), 8)),
                        input.getInput(new ItemStack(IUItem.radiationresources.getStack(4), 2))
                ),
                new RecipeOutput(null, new ItemStack(IUItem.genome_crop.getStack(4)))
        ));
        Recipes.recipes.addRecipe("genetic_polymerizer", new BaseMachineRecipe(
                new Input(
                        new FluidStack(FluidName.fluidcropgenetic.getInstance().get(), 1000),
                        input.getInput(new ItemStack(IUItem.genome_crop.getStack(4))),
                        input.getInput(new ItemStack(Items.GLASS_BOTTLE)),
                        input.getInput(new ItemStack(IUItem.nether_star_ingot.getItem(), 2)),
                        input.getInput(new ItemStack(IUItem.crafting_elements.getStack(445), 10)),
                        input.getInput(new ItemStack(IUItem.radiationresources.getStack(4), 2))
                ),
                new RecipeOutput(null, new ItemStack(IUItem.genome_crop.getStack(5)))
        ));

        Recipes.recipes.addRecipe("genetic_polymerizer", new BaseMachineRecipe(
                new Input(
                        new FluidStack(FluidName.fluidcropgenetic.getInstance().get(), 500),
                        input.getInput(new ItemStack(IUItem.genome_crop.getStack(7))),
                        input.getInput(new ItemStack(Items.GLASS_BOTTLE)),
                        input.getInput(new ItemStack(IUItem.nether_star_ingot.getItem(), 1)),
                        input.getInput(new ItemStack(IUItem.crafting_elements.getStack(445), 8)),
                        input.getInput(new ItemStack(IUItem.proton.getItem(), 2))
                ),
                new RecipeOutput(null, new ItemStack(IUItem.genome_crop.getStack(8)))
        ));
        Recipes.recipes.addRecipe("genetic_polymerizer", new BaseMachineRecipe(
                new Input(
                        new FluidStack(FluidName.fluidcropgenetic.getInstance().get(), 1000),
                        input.getInput(new ItemStack(IUItem.genome_crop.getStack(8))),
                        input.getInput(new ItemStack(Items.GLASS_BOTTLE)),
                        input.getInput(new ItemStack(IUItem.nether_star_ingot.getItem(), 2)),
                        input.getInput(new ItemStack(IUItem.crafting_elements.getStack(445), 10)),
                        input.getInput(new ItemStack(IUItem.proton.getItem(), 2))
                ),
                new RecipeOutput(null, new ItemStack(IUItem.genome_crop.getStack(9)))
        ));
        Recipes.recipes.addRecipe("genetic_polymerizer", new BaseMachineRecipe(
                new Input(
                        new FluidStack(FluidName.fluidcropgenetic.getInstance().get(), 500),
                        input.getInput(new ItemStack(IUItem.genome_crop.getStack(10))),
                        input.getInput(new ItemStack(Items.GLASS_BOTTLE)),
                        input.getInput(new ItemStack(IUItem.nether_star_ingot.getItem(), 1)),
                        input.getInput(new ItemStack(IUItem.crafting_elements.getStack(446), 8)),
                        input.getInput(new ItemStack(IUItem.proton.getItem(), 2))
                ),
                new RecipeOutput(null, new ItemStack(IUItem.genome_crop.getStack(11)))
        ));


        Recipes.recipes.addRecipe("genetic_polymerizer", new BaseMachineRecipe(
                new Input(
                        new FluidStack(FluidName.fluidcropgenetic.getInstance().get(), 500),
                        input.getInput(new ItemStack(IUItem.genome_crop.getStack(13))),
                        input.getInput(new ItemStack(Items.GLASS_BOTTLE)),
                        input.getInput(new ItemStack(IUItem.nether_star_ingot.getItem(), 1)),
                        input.getInput(new ItemStack(IUItem.crafting_elements.getStack(445), 8)),
                        input.getInput(new ItemStack(IUItem.radiationresources.getStack(1), 2))
                ),
                new RecipeOutput(null, new ItemStack(IUItem.genome_crop.getStack(14)))
        ));
        Recipes.recipes.addRecipe("genetic_polymerizer", new BaseMachineRecipe(
                new Input(
                        new FluidStack(FluidName.fluidcropgenetic.getInstance().get(), 1000),
                        input.getInput(new ItemStack(IUItem.genome_crop.getStack(14))),
                        input.getInput(new ItemStack(Items.GLASS_BOTTLE)),
                        input.getInput(new ItemStack(IUItem.nether_star_ingot.getItem(), 2)),
                        input.getInput(new ItemStack(IUItem.crafting_elements.getStack(445), 10)),
                        input.getInput(new ItemStack(IUItem.radiationresources.getStack(1), 2))
                ),
                new RecipeOutput(null, new ItemStack(IUItem.genome_crop.getStack(15)))
        ));
        Recipes.recipes.addRecipe("genetic_polymerizer", new BaseMachineRecipe(
                new Input(
                        new FluidStack(FluidName.fluidcropgenetic.getInstance().get(), 1000),
                        input.getInput(new ItemStack(IUItem.genome_crop.getStack(15))),
                        input.getInput(new ItemStack(Items.GLASS_BOTTLE)),
                        input.getInput(new ItemStack(IUItem.nether_star_ingot.getItem(), 4)),
                        input.getInput(new ItemStack(IUItem.crafting_elements.getStack(445), 10)),
                        input.getInput(new ItemStack(IUItem.radiationresources.getStack(1), 2))
                ),
                new RecipeOutput(null, new ItemStack(IUItem.genome_crop.getStack(16)))
        ));


        Recipes.recipes.addRecipe("genetic_polymerizer", new BaseMachineRecipe(
                new Input(
                        new FluidStack(FluidName.fluidcropgenetic.getInstance().get(), 500),
                        input.getInput(new ItemStack(IUItem.genome_crop.getStack(22))),
                        input.getInput(new ItemStack(Items.GLASS_BOTTLE)),
                        input.getInput(new ItemStack(IUItem.nether_star_ingot.getItem(), 1)),
                        input.getInput(new ItemStack(IUItem.crafting_elements.getStack(446), 8)),
                        input.getInput(new ItemStack(IUItem.radiationresources.getStack(2), 2))
                ),
                new RecipeOutput(null, new ItemStack(IUItem.genome_crop.getStack(23)))
        ));
        Recipes.recipes.addRecipe("genetic_polymerizer", new BaseMachineRecipe(
                new Input(
                        new FluidStack(FluidName.fluidcropgenetic.getInstance().get(), 1000),
                        input.getInput(new ItemStack(IUItem.genome_crop.getStack(23))),
                        input.getInput(new ItemStack(Items.GLASS_BOTTLE)),
                        input.getInput(new ItemStack(IUItem.nether_star_ingot.getItem(), 2)),
                        input.getInput(new ItemStack(IUItem.crafting_elements.getStack(446), 10)),
                        input.getInput(new ItemStack(IUItem.radiationresources.getStack(2), 2))
                ),
                new RecipeOutput(null, new ItemStack(IUItem.genome_crop.getStack(24)))
        ));

        Recipes.recipes.addRecipe("genetic_polymerizer", new BaseMachineRecipe(
                new Input(
                        new FluidStack(FluidName.fluidcropgenetic.getInstance().get(), 500),
                        input.getInput(new ItemStack(IUItem.genome_crop.getStack(25))),
                        input.getInput(new ItemStack(Items.GLASS_BOTTLE)),
                        input.getInput(new ItemStack(IUItem.nether_star_ingot.getItem(), 1)),
                        input.getInput(new ItemStack(IUItem.crafting_elements.getStack(446), 8)),
                        input.getInput(new ItemStack(IUItem.toriy.getItem(), 2))
                ),
                new RecipeOutput(null, new ItemStack(IUItem.genome_crop.getStack(26)))
        ));
        Recipes.recipes.addRecipe("genetic_polymerizer", new BaseMachineRecipe(
                new Input(
                        new FluidStack(FluidName.fluidcropgenetic.getInstance().get(), 1000),
                        input.getInput(new ItemStack(IUItem.genome_crop.getStack(26))),
                        input.getInput(new ItemStack(Items.GLASS_BOTTLE)),
                        input.getInput(new ItemStack(IUItem.nether_star_ingot.getItem(), 2)),
                        input.getInput(new ItemStack(IUItem.crafting_elements.getStack(446), 10)),
                        input.getInput(new ItemStack(IUItem.toriy.getItem(), 2))
                ),
                new RecipeOutput(null, new ItemStack(IUItem.genome_crop.getStack(27)))
        ));


        Recipes.recipes.addRecipe("genetic_polymerizer", new BaseMachineRecipe(
                new Input(
                        new FluidStack(FluidName.fluidcropgenetic.getInstance().get(), 500),
                        input.getInput(new ItemStack(IUItem.genome_crop.getStack(28))),
                        input.getInput(new ItemStack(Items.GLASS_BOTTLE)),
                        input.getInput(new ItemStack(IUItem.nether_star_ingot.getItem(), 1)),
                        input.getInput(new ItemStack(IUItem.crafting_elements.getStack(445), 8)),
                        input.getInput(new ItemStack(IUItem.radiationresources.getStack(2), 2))
                ),
                new RecipeOutput(null, new ItemStack(IUItem.genome_crop.getStack(29)))
        ));
        Recipes.recipes.addRecipe("genetic_polymerizer", new BaseMachineRecipe(
                new Input(
                        new FluidStack(FluidName.fluidcropgenetic.getInstance().get(), 1000),
                        input.getInput(new ItemStack(IUItem.genome_crop.getStack(29))),
                        input.getInput(new ItemStack(Items.GLASS_BOTTLE)),
                        input.getInput(new ItemStack(IUItem.nether_star_ingot.getItem(), 2)),
                        input.getInput(new ItemStack(IUItem.crafting_elements.getStack(445), 10)),
                        input.getInput(new ItemStack(IUItem.radiationresources.getStack(2), 2))
                ),
                new RecipeOutput(null, new ItemStack(IUItem.genome_crop.getStack(30)))
        ));

        Recipes.recipes.addRecipe("genetic_polymerizer", new BaseMachineRecipe(
                new Input(
                        new FluidStack(FluidName.fluidcropgenetic.getInstance().get(), 500),
                        input.getInput(new ItemStack(IUItem.genome_crop.getStack(31))),
                        input.getInput(new ItemStack(Items.GLASS_BOTTLE)),
                        input.getInput(new ItemStack(IUItem.nether_star_ingot.getItem(), 1)),
                        input.getInput(new ItemStack(IUItem.crafting_elements.getStack(446), 8)),
                        input.getInput(new ItemStack(IUItem.radiationresources.getStack(4), 2))
                ),
                new RecipeOutput(null, new ItemStack(IUItem.genome_crop.getStack(32)))
        ));
        Recipes.recipes.addRecipe("genetic_polymerizer", new BaseMachineRecipe(
                new Input(
                        new FluidStack(FluidName.fluidcropgenetic.getInstance().get(), 1000),
                        input.getInput(new ItemStack(IUItem.genome_crop.getStack(32))),
                        input.getInput(new ItemStack(Items.GLASS_BOTTLE)),
                        input.getInput(new ItemStack(IUItem.nether_star_ingot.getItem(), 2)),
                        input.getInput(new ItemStack(IUItem.crafting_elements.getStack(446), 10)),
                        input.getInput(new ItemStack(IUItem.radiationresources.getStack(4), 2))
                ),
                new RecipeOutput(null, new ItemStack(IUItem.genome_crop.getStack(33)))
        ));

        Recipes.recipes.addRecipe("genetic_polymerizer", new BaseMachineRecipe(
                new Input(
                        new FluidStack(FluidName.fluidcropgenetic.getInstance().get(), 500),
                        input.getInput(new ItemStack(IUItem.genome_crop.getStack(37))),
                        input.getInput(new ItemStack(Items.GLASS_BOTTLE)),
                        input.getInput(new ItemStack(IUItem.nether_star_ingot.getItem(), 1)),
                        input.getInput(new ItemStack(IUItem.crafting_elements.getStack(445), 8)),
                        input.getInput(new ItemStack(IUItem.nuclear_res.getStack(2), 2))
                ),
                new RecipeOutput(null, new ItemStack(IUItem.genome_crop.getStack(38)))
        ));
        Recipes.recipes.addRecipe("genetic_polymerizer", new BaseMachineRecipe(
                new Input(
                        new FluidStack(FluidName.fluidcropgenetic.getInstance().get(), 1000),
                        input.getInput(new ItemStack(IUItem.genome_crop.getStack(38))),
                        input.getInput(new ItemStack(Items.GLASS_BOTTLE)),
                        input.getInput(new ItemStack(IUItem.nether_star_ingot.getItem(), 2)),
                        input.getInput(new ItemStack(IUItem.crafting_elements.getStack(445), 10)),
                        input.getInput(new ItemStack(IUItem.nuclear_res.getStack(2), 2))
                ),
                new RecipeOutput(null, new ItemStack(IUItem.genome_crop.getStack(39)))
        ));

        Recipes.recipes.addRecipe("genetic_polymerizer", new BaseMachineRecipe(
                new Input(
                        new FluidStack(FluidName.fluidcropgenetic.getInstance().get(), 500),
                        input.getInput(new ItemStack(IUItem.genome_crop.getStack(40))),
                        input.getInput(new ItemStack(Items.GLASS_BOTTLE)),
                        input.getInput(new ItemStack(IUItem.nether_star_ingot.getItem(), 1)),
                        input.getInput(new ItemStack(IUItem.crafting_elements.getStack(445), 8)),
                        input.getInput(new ItemStack(IUItem.radiationresources.getStack(3), 2))
                ),
                new RecipeOutput(null, new ItemStack(IUItem.genome_crop.getStack(41)))
        ));
        Recipes.recipes.addRecipe("genetic_polymerizer", new BaseMachineRecipe(
                new Input(
                        new FluidStack(FluidName.fluidcropgenetic.getInstance().get(), 1000),
                        input.getInput(new ItemStack(IUItem.genome_crop.getStack(41))),
                        input.getInput(new ItemStack(Items.GLASS_BOTTLE)),
                        input.getInput(new ItemStack(IUItem.nether_star_ingot.getItem(), 2)),
                        input.getInput(new ItemStack(IUItem.crafting_elements.getStack(445), 10)),
                        input.getInput(new ItemStack(IUItem.radiationresources.getStack(3), 2))
                ),
                new RecipeOutput(null, new ItemStack(IUItem.genome_crop.getStack(42)))
        ));

        Recipes.recipes.addRecipe("genetic_polymerizer", new BaseMachineRecipe(
                new Input(
                        new FluidStack(FluidName.fluidcropgenetic.getInstance().get(), 500),
                        input.getInput(new ItemStack(IUItem.genome_crop.getStack(43))),
                        input.getInput(new ItemStack(Items.GLASS_BOTTLE)),
                        input.getInput(new ItemStack(IUItem.nether_star_ingot.getItem(), 1)),
                        input.getInput(new ItemStack(IUItem.crafting_elements.getStack(446), 8)),
                        input.getInput(new ItemStack(IUItem.nuclear_res.getStack(3), 2))
                ),
                new RecipeOutput(null, new ItemStack(IUItem.genome_crop.getStack(44)))
        ));
        Recipes.recipes.addRecipe("genetic_polymerizer", new BaseMachineRecipe(
                new Input(
                        new FluidStack(FluidName.fluidcropgenetic.getInstance().get(), 1000),
                        input.getInput(new ItemStack(IUItem.genome_crop.getStack(44))),
                        input.getInput(new ItemStack(Items.GLASS_BOTTLE)),
                        input.getInput(new ItemStack(IUItem.nether_star_ingot.getItem(), 2)),
                        input.getInput(new ItemStack(IUItem.crafting_elements.getStack(446), 10)),
                        input.getInput(new ItemStack(IUItem.nuclear_res.getStack(3), 2))
                ),
                new RecipeOutput(null, new ItemStack(IUItem.genome_crop.getStack(45)))
        ));

        Recipes.recipes.addRecipe("genetic_polymerizer", new BaseMachineRecipe(
                new Input(
                        new FluidStack(FluidName.fluidcropgenetic.getInstance().get(), 500),
                        input.getInput(new ItemStack(IUItem.genome_crop.getStack(46))),
                        input.getInput(new ItemStack(Items.GLASS_BOTTLE)),
                        input.getInput(new ItemStack(IUItem.nether_star_ingot.getItem(), 1)),
                        input.getInput(new ItemStack(IUItem.crafting_elements.getStack(446), 8)),
                        input.getInput(new ItemStack(IUItem.radiationresources.getStack(3), 2))
                ),
                new RecipeOutput(null, new ItemStack(IUItem.genome_crop.getStack(47)))
        ));
        Recipes.recipes.addRecipe("genetic_polymerizer", new BaseMachineRecipe(
                new Input(
                        new FluidStack(FluidName.fluidcropgenetic.getInstance().get(), 1000),
                        input.getInput(new ItemStack(IUItem.genome_crop.getStack(47))),
                        input.getInput(new ItemStack(Items.GLASS_BOTTLE)),
                        input.getInput(new ItemStack(IUItem.nether_star_ingot.getItem(), 2)),
                        input.getInput(new ItemStack(IUItem.crafting_elements.getStack(446), 10)),
                        input.getInput(new ItemStack(IUItem.radiationresources.getStack(3), 2))
                ),
                new RecipeOutput(null, new ItemStack(IUItem.genome_crop.getStack(48)))
        ));


        Recipes.recipes.addRecipe("genetic_polymerizer", new BaseMachineRecipe(
                new Input(
                        new FluidStack(FluidName.fluidcropgenetic.getInstance().get(), 1000),
                        input.getInput(new ItemStack(IUItem.genome_crop.getStack(19))),
                        input.getInput(new ItemStack(Items.GLASS_BOTTLE)),
                        input.getInput(new ItemStack(IUItem.nether_star_ingot.getItem(), 2)),
                        input.getInput(new ItemStack(IUItem.crafting_elements.getStack(446), 10)),
                        input.getInput(new ItemStack(IUItem.radiationresources.getStack(3), 2))
                ),
                new RecipeOutput(null, new ItemStack(IUItem.genome_crop.getStack(17)))
        ));

        Recipes.recipes.addRecipe("genetic_polymerizer", new BaseMachineRecipe(
                new Input(
                        new FluidStack(FluidName.fluidcropgenetic.getInstance().get(), 1000),
                        input.getInput(new ItemStack(IUItem.genome_crop.getStack(19))),
                        input.getInput(new ItemStack(Items.GLASS_BOTTLE)),
                        input.getInput(new ItemStack(IUItem.nether_star_ingot.getItem(), 2)),
                        input.getInput(new ItemStack(IUItem.crafting_elements.getStack(446), 10)),
                        input.getInput(new ItemStack(IUItem.radiationresources.getStack(1), 2))
                ),
                new RecipeOutput(null, new ItemStack(IUItem.genome_crop.getStack(18)))
        ));

        Recipes.recipes.addRecipe("genetic_polymerizer", new BaseMachineRecipe(
                new Input(
                        new FluidStack(FluidName.fluidcropgenetic.getInstance().get(), 1000),
                        input.getInput(new ItemStack(IUItem.genome_crop.getStack(19))),
                        input.getInput(new ItemStack(Items.GLASS_BOTTLE)),
                        input.getInput(new ItemStack(IUItem.nether_star_ingot.getItem(), 2)),
                        input.getInput(new ItemStack(IUItem.crafting_elements.getStack(446), 10)),
                        input.getInput(new ItemStack(IUItem.radiationresources.getStack(2), 2))
                ),
                new RecipeOutput(null, new ItemStack(IUItem.genome_crop.getStack(20)))
        ));
        Recipes.recipes.addRecipe("genetic_polymerizer", new BaseMachineRecipe(
                new Input(
                        new FluidStack(FluidName.fluidcropgenetic.getInstance().get(), 1000),
                        input.getInput(new ItemStack(IUItem.genome_crop.getStack(19))),
                        input.getInput(new ItemStack(Items.GLASS_BOTTLE)),
                        input.getInput(new ItemStack(IUItem.nether_star_ingot.getItem(), 2)),
                        input.getInput(new ItemStack(IUItem.crafting_elements.getStack(446), 10)),
                        input.getInput(new ItemStack(IUItem.radiationresources.getStack(0), 2))
                ),
                new RecipeOutput(null, new ItemStack(IUItem.genome_crop.getStack(21)))
        ));
    }


    public int gaugeLiquidScaled(int i) {
        return this.getFluidTank().getFluidAmount() <= 0
                ? 0
                : this.getFluidTank().getFluidAmount() * i / this.getFluidTank().getCapacity();
    }


    @Override
    @OnlyIn(Dist.CLIENT)
    public GuiCore<ContainerBase<? extends IAdvInventory>> getGui(Player var1, ContainerBase<? extends IAdvInventory> menu) {

        return new GuiGeneticPolymerizer((ContainerGeneticPolymerizer) menu);

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
