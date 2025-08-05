package com.denfop.tiles.mechanism;

import com.denfop.IUItem;
import com.denfop.api.Recipes;
import com.denfop.api.gui.EnumTypeSlot;
import com.denfop.api.inv.IAdvInventory;
import com.denfop.api.recipe.*;
import com.denfop.api.tile.IMultiTileBlock;
import com.denfop.api.upgrades.UpgradableProperty;
import com.denfop.audio.EnumSound;
import com.denfop.blocks.BlockTileEntity;
import com.denfop.blocks.FluidName;
import com.denfop.blocks.mechanism.BlockBaseMachine3;
import com.denfop.componets.AirPollutionComponent;
import com.denfop.componets.HeatComponent;
import com.denfop.componets.SoilPollutionComponent;
import com.denfop.container.ContainerBase;
import com.denfop.container.ContainerElectricRefractoryFurnace;
import com.denfop.gui.GuiCore;
import com.denfop.gui.GuiElectricRefractoryFurnace;
import com.denfop.invslot.InvSlot;
import com.denfop.recipe.IInputHandler;
import com.denfop.tiles.base.TileBasePlasticPlateCreator;
import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.fluids.FluidStack;

import java.util.EnumSet;
import java.util.Set;

import static com.denfop.register.RegisterOreDictionary.*;

public class TileEntityElectricRefractoryFurnace extends TileBasePlasticPlateCreator implements IUpdateTick, IHasRecipe {

    public final InvSlot input_slot;
    public final HeatComponent heat;
    private final SoilPollutionComponent pollutionSoil;
    private final AirPollutionComponent pollutionAir;

    public TileEntityElectricRefractoryFurnace(BlockPos pos, BlockState state) {
        super(1, 200, 1,BlockBaseMachine3.electric_refractory_furnace,pos,state);
        this.heat = this.addComponent(HeatComponent.asBasicSink(this, 1000));
        this.inputSlotA = new InvSlotRecipes(this, "elec_refractory_furnace", this, this.fluidTank);
        fluidTank.setTypeItemSlot(InvSlot.TypeItemSlot.INPUT);
        this.componentProcess.setInvSlotRecipes(inputSlotA);
        this.inputSlotA.setInvSlotConsumableLiquidByList(this.fluidSlot);
        Recipes.recipes.addInitRecipes(this);
        this.input_slot = new InvSlot(this, InvSlot.TypeItemSlot.INPUT, 1) {
            @Override
            public ItemStack set(final int index, final ItemStack content) {
                super.set(index, content);
                if (this.get(0).isEmpty()) {
                    ((TileEntityElectricRefractoryFurnace) this.base).inputSlotA.changeAccepts(ItemStack.EMPTY);
                } else {
                    ((TileEntityElectricRefractoryFurnace) this.base).inputSlotA.changeAccepts(this.get(0));
                }
                return content;
            }

            @Override
            public boolean accepts(final ItemStack stack, final int index) {
                return stack.getItem() == IUItem.recipe_schedule.getItem();
            }

            @Override
            public EnumTypeSlot getTypeSlot() {
                return EnumTypeSlot.RECIPE_SCHEDULE;
            }
        };
        this.pollutionSoil = this.addComponent(new SoilPollutionComponent(this, 0.25));
        this.pollutionAir = this.addComponent(new AirPollutionComponent(this, 0.5));
    }

    @Override
    public void onLoaded() {
        super.onLoaded();
        if (!this.getWorld().isClientSide) {
            if (this.input_slot.isEmpty()) {
                (this).inputSlotA.changeAccepts(ItemStack.EMPTY);
            } else {
                (this).inputSlotA.changeAccepts(this.input_slot.get(0));
            }
        }
    }

    public void init() {
        final IInputHandler input = com.denfop.api.Recipes.inputFactory;
        for (String s : list_string) {
            if (Recipes.inputFactory.getInput("forge:raw_ingots/" + s).getInputs().isEmpty()) {
                continue;
            }
            final CompoundTag nbt = new CompoundTag();
            nbt.putInt("temperature", 1000);
            Recipes.recipes.addRecipe("elec_refractory_furnace", new BaseMachineRecipe(new Input(
                    new FluidStack(FluidName.fluidfluorhyd.getInstance().get(), 2),
                    input.getInput("forge:crushed/" + s)
            ), new RecipeOutput(nbt, Recipes.inputFactory.getInput("forge:raw_ingots/" + s).getInputs().get(0))));
            Recipes.recipes.addRecipe("elec_refractory_furnace", new BaseMachineRecipe(new Input(
                    new FluidStack(FluidName.fluidfluorhyd.getInstance().get(), 2),
                    input.getInput("forge:dusts/" + s)
            ), new RecipeOutput(nbt,Recipes.inputFactory.getInput("forge:raw_ingots/" + s).getInputs().get(0))));

            Recipes.recipes.addRecipe("elec_refractory_furnace", new BaseMachineRecipe(new Input(
                    new FluidStack(FluidName.fluidnitricacid.getInstance().get(), 5),
                    input.getInput("forge:crushed/" + s)
            ), new RecipeOutput(nbt, Recipes.inputFactory.getInput("forge:raw_ingots/" + s).getInputs().get(0))));
            Recipes.recipes.addRecipe("elec_refractory_furnace", new BaseMachineRecipe(new Input(
                    new FluidStack(FluidName.fluidnitricacid.getInstance().get(), 5),
                    input.getInput("forge:dusts/" + s)
            ), new RecipeOutput(nbt,Recipes.inputFactory.getInput("forge:raw_ingots/" + s).getInputs().get(0))));

        }

        for (String s : list_baseore1) {
            if (Recipes.inputFactory.getInput("forge:raw_ingots/" + s).getInputs().isEmpty()) {
                continue;
            }
            final CompoundTag nbt = new CompoundTag();
            nbt.putInt("temperature", 1000);
            Recipes.recipes.addRecipe("elec_refractory_furnace", new BaseMachineRecipe(new Input(
                    new FluidStack(FluidName.fluidfluorhyd.getInstance().get(), 2),
                    input.getInput("forge:crushed/" + s)
            ), new RecipeOutput(nbt, Recipes.inputFactory.getInput("forge:raw_ingots/" + s).getInputs().get(0))));
            Recipes.recipes.addRecipe("elec_refractory_furnace", new BaseMachineRecipe(new Input(
                    new FluidStack(FluidName.fluidfluorhyd.getInstance().get(), 2),
                    input.getInput("forge:dusts/" + s)
            ), new RecipeOutput(nbt,Recipes.inputFactory.getInput("forge:raw_ingots/" + s).getInputs().get(0))));

            Recipes.recipes.addRecipe("elec_refractory_furnace", new BaseMachineRecipe(new Input(
                    new FluidStack(FluidName.fluidnitricacid.getInstance().get(), 5),
                    input.getInput("forge:crushed/" + s)
            ), new RecipeOutput(nbt, Recipes.inputFactory.getInput("forge:raw_ingots/" + s).getInputs().get(0))));
            Recipes.recipes.addRecipe("elec_refractory_furnace", new BaseMachineRecipe(new Input(
                    new FluidStack(FluidName.fluidnitricacid.getInstance().get(), 5),
                    input.getInput("forge:dusts/" + s)
            ), new RecipeOutput(nbt,Recipes.inputFactory.getInput("forge:raw_ingots/" + s).getInputs().get(0))));

        }
        for (String s : standardList) {
            if (Recipes.inputFactory.getInput("forge:raw_ingots/" + s).getInputs().isEmpty()) {
                continue;
            }
            final CompoundTag nbt = new CompoundTag();
            nbt.putInt("temperature", 1000);
            Recipes.recipes.addRecipe("elec_refractory_furnace", new BaseMachineRecipe(new Input(
                    new FluidStack(FluidName.fluidfluorhyd.getInstance().get(), 2),
                    input.getInput("forge:crushed/" + s)
            ), new RecipeOutput(nbt, Recipes.inputFactory.getInput("forge:raw_ingots/" + s).getInputs().get(0))));
            Recipes.recipes.addRecipe("elec_refractory_furnace", new BaseMachineRecipe(new Input(
                    new FluidStack(FluidName.fluidfluorhyd.getInstance().get(), 2),
                    input.getInput("forge:dusts/" + s)
            ), new RecipeOutput(nbt,Recipes.inputFactory.getInput("forge:raw_ingots/" + s).getInputs().get(0))));

            Recipes.recipes.addRecipe("elec_refractory_furnace", new BaseMachineRecipe(new Input(
                    new FluidStack(FluidName.fluidnitricacid.getInstance().get(), 5),
                    input.getInput("forge:crushed/" + s)
            ), new RecipeOutput(nbt, Recipes.inputFactory.getInput("forge:raw_ingots/" + s).getInputs().get(0))));
            Recipes.recipes.addRecipe("elec_refractory_furnace", new BaseMachineRecipe(new Input(
                    new FluidStack(FluidName.fluidnitricacid.getInstance().get(), 5),
                    input.getInput("forge:dusts/" + s)
            ), new RecipeOutput(nbt,Recipes.inputFactory.getInput("forge:raw_ingots/" + s).getInputs().get(0))));

        }

    }

    public IMultiTileBlock getTeBlock() {
        return BlockBaseMachine3.electric_refractory_furnace;
    }

    public BlockTileEntity getBlock() {
        return IUItem.basemachine2.getBlock(getTeBlock());
    }


    public int gaugeLiquidScaled(int i) {
        return this.getFluidTank().getFluidAmount() <= 0
                ? 0
                : this.getFluidTank().getFluidAmount() * i / this.getFluidTank().getCapacity();
    }


    @Override
    public ContainerElectricRefractoryFurnace getGuiContainer(final Player entityPlayer) {
        return new ContainerElectricRefractoryFurnace(entityPlayer, this);
    }

    @Override
    @OnlyIn(Dist.CLIENT)
    public GuiCore<ContainerBase<? extends IAdvInventory>> getGui(Player var1, ContainerBase<? extends IAdvInventory> menu) {

        return new GuiElectricRefractoryFurnace((ContainerElectricRefractoryFurnace) menu);

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
