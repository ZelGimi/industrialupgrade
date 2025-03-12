package com.denfop.tiles.mechanism;

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
import com.denfop.api.upgrades.UpgradableProperty;
import com.denfop.audio.EnumSound;
import com.denfop.blocks.BlockTileEntity;
import com.denfop.blocks.FluidName;
import com.denfop.blocks.mechanism.BlockBaseMachine2;
import com.denfop.componets.AirPollutionComponent;
import com.denfop.componets.SoilPollutionComponent;
import com.denfop.container.ContainerPlasticPlateCreator;
import com.denfop.gui.GuiPlasticPlateCreator;
import com.denfop.invslot.InvSlot;
import com.denfop.recipe.IInputHandler;
import com.denfop.tiles.base.TileBasePlasticPlateCreator;
import com.denfop.utils.ModUtils;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;
import net.minecraft.util.SoundEvent;
import net.minecraftforge.fluids.FluidRegistry;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import java.util.EnumSet;
import java.util.Set;

public class TilePlasticPlateCreator extends TileBasePlasticPlateCreator implements IUpdateTick, IHasRecipe {

    public final InvSlot input_slot;
    private final SoilPollutionComponent pollutionSoil;
    private final AirPollutionComponent pollutionAir;

    public TilePlasticPlateCreator() {
        super(1, 300, 1);
        this.inputSlotA = new InvSlotRecipes(this, "plasticplate", this, this.fluidTank);
        fluidTank.setTypeItemSlot(InvSlot.TypeItemSlot.INPUT);
        this.componentProcess.setInvSlotRecipes(inputSlotA);
        this.inputSlotA.setInvSlotConsumableLiquidByList(this.fluidSlot);
        Recipes.recipes.addInitRecipes(this);
        this.input_slot = new InvSlot(this, InvSlot.TypeItemSlot.INPUT, 1) {
            @Override
            public void put(final int index, final ItemStack content) {
                super.put(index, content);
                if (this.get().isEmpty()) {
                    ((TilePlasticPlateCreator) this.base).inputSlotA.changeAccepts(ItemStack.EMPTY);
                } else {
                    ((TilePlasticPlateCreator) this.base).inputSlotA.changeAccepts(this.get());
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
        this.pollutionSoil = this.addComponent(new SoilPollutionComponent(this, 0.1));
        this.pollutionAir = this.addComponent(new AirPollutionComponent(this, 0.1));
    }

    @Override
    public void onLoaded() {
        super.onLoaded();
        if (!this.getWorld().isRemote) {
            if (this.input_slot.isEmpty()) {
                (this).inputSlotA.changeAccepts(ItemStack.EMPTY);
            } else {
                (this).inputSlotA.changeAccepts(this.input_slot.get());
            }
        }
    }

    public void init() {
        final IInputHandler input = com.denfop.api.Recipes.inputFactory;
        Recipes.recipes.addRecipe("plasticplate", new BaseMachineRecipe(new Input(
                new FluidStack(FluidName.fluidoxy.getInstance(), 1000),
                input.getInput(new ItemStack(IUItem.plast))
        ), new RecipeOutput(null, new ItemStack(IUItem.plastic_plate))));

        Recipes.recipes.addRecipe("plasticplate", new BaseMachineRecipe(
                new Input(
                        new FluidStack(FluidName.fluidglucose.getInstance(), 200),
                        input.getInput(Items.GLOWSTONE_DUST)
                ),
                new RecipeOutput(null, new ItemStack(Items.SUGAR,2))
        ));
        Recipes.recipes.addRecipe("plasticplate", new BaseMachineRecipe(new Input(
                new FluidStack(FluidName.fluidco2.getInstance(), 500),
                input.getInput(new ItemStack(Items.FLINT, 4))
        ), new RecipeOutput(null, new ItemStack(IUItem.crafting_elements, 1, 344))));
        Recipes.recipes.addRecipe("plasticplate", new BaseMachineRecipe(new Input(
                new FluidStack(FluidRegistry.WATER, 500),
                input.getInput(new ItemStack(IUItem.iudust, 2, 66))
        ), new RecipeOutput(null, new ItemStack(IUItem.raw_apatite))));
        Recipes.recipes.addRecipe("plasticplate", new BaseMachineRecipe(new Input(
                new FluidStack(FluidRegistry.WATER, 500),
                input.getInput(new ItemStack(Blocks.DIRT))
        ), new RecipeOutput(null, new ItemStack(Blocks.CLAY))));

        Recipes.recipes.addRecipe("plasticplate", new BaseMachineRecipe(new Input(
                new FluidStack(FluidName.fluidoxy.getInstance(), 200),
                input.getInput(new ItemStack(IUItem.red_phosphorus, 4))
        ), new RecipeOutput(null, new ItemStack(IUItem.phosphorus_oxide, 2))));

        Recipes.recipes.addRecipe("plasticplate", new BaseMachineRecipe(new Input(
                new FluidStack(FluidName.fluidglowstone.getInstance(), 125),
                input.getInput(new ItemStack(IUItem.iudust,1,75))
        ), new RecipeOutput(null, new ItemStack(Items.ENDER_PEARL))));

        Recipes.recipes.addRecipe("plasticplate", new BaseMachineRecipe(new Input(
                new FluidStack(FluidName.fluidglowstone.getInstance(), 125),
                input.getInput(new ItemStack(IUItem.iudust,1,77))
        ), new RecipeOutput(null, new ItemStack(Items.GHAST_TEAR))));


        Recipes.recipes.addRecipe("plasticplate", new BaseMachineRecipe(new Input(
                new FluidStack(FluidName.fluidchlorum.getInstance(), 180),
                input.getInput(  new ItemStack(IUItem.iudust, 2, 26))
        ), new RecipeOutput(null, new ItemStack(IUItem.iudust, 1, 79))));

        Recipes.recipes.addRecipe("plasticplate", new BaseMachineRecipe(new Input(
                new FluidStack(FluidName.fluidorthophosphoricacid.getInstance(), 100),
                input.getInput(new ItemStack(IUItem.iudust, 1, 37))
        ), new RecipeOutput(null, new ItemStack(IUItem.iudust, 1, 70))));

        Recipes.recipes.addRecipe("plasticplate", new BaseMachineRecipe(new Input(
                new FluidStack(FluidName.fluidorthophosphoricacid.getInstance(), 100),
                input.getInput(new ItemStack(IUItem.iudust, 1, 64))
        ), new RecipeOutput(null, new ItemStack(IUItem.iudust, 1, 69))));

        Recipes.recipes.addRecipe("plasticplate", new BaseMachineRecipe(new Input(
                new FluidStack(FluidName.fluidbutadiene_nitrile.getInstance(), 100),
                input.getInput(ModUtils.setSize(IUItem.rubber, 1))
        ), new RecipeOutput(null, new ItemStack(IUItem.synthetic_rubber, 4))));

        Recipes.recipes.addRecipe("plasticplate", new BaseMachineRecipe(new Input(
                new FluidStack(FluidName.fluidroyaljelly.getInstance(), 200),
                input.getInput(Items.WHEAT)
        ), new RecipeOutput(null, new ItemStack(IUItem.royal_jelly))));

    }

    public IMultiTileBlock getTeBlock() {
        return BlockBaseMachine2.plastic_plate_creator;
    }

    public BlockTileEntity getBlock() {
        return IUItem.basemachine1;
    }

    public String getInventoryName() {

        return Localization.translate("iu.blockPlasticPlateCreator.name");
    }

    public int gaugeLiquidScaled(int i) {
        return this.getFluidTank().getFluidAmount() <= 0
                ? 0
                : this.getFluidTank().getFluidAmount() * i / this.getFluidTank().getCapacity();
    }


    @Override
    public ContainerPlasticPlateCreator getGuiContainer(final EntityPlayer entityPlayer) {
        return new ContainerPlasticPlateCreator(entityPlayer, this);
    }

    @SideOnly(Side.CLIENT)
    public GuiScreen getGui(EntityPlayer entityPlayer, boolean isAdmin) {
        return new GuiPlasticPlateCreator(new ContainerPlasticPlateCreator(entityPlayer, this));

    }


    @Override
    public SoundEvent getSound() {
        return EnumSound.plastic_plate.getSoundEvent();
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

}
