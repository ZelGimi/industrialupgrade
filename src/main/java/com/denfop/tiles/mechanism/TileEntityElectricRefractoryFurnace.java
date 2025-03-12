package com.denfop.tiles.mechanism;

import com.denfop.IUItem;
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
import com.denfop.blocks.mechanism.BlockBaseMachine3;
import com.denfop.componets.AirPollutionComponent;
import com.denfop.componets.HeatComponent;
import com.denfop.componets.SoilPollutionComponent;
import com.denfop.container.ContainerElectricRefractoryFurnace;
import com.denfop.container.ContainerPlasticPlateCreator;
import com.denfop.gui.GuiElectricRefractoryFurnace;
import com.denfop.gui.GuiPlasticPlateCreator;
import com.denfop.invslot.InvSlot;
import com.denfop.items.resource.ItemCrushed;
import com.denfop.items.resource.ItemRawMetals;
import com.denfop.recipe.IInputHandler;
import com.denfop.tiles.base.TileBasePlasticPlateCreator;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.SoundEvent;
import net.minecraftforge.fluids.FluidRegistry;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import net.minecraftforge.oredict.OreDictionary;

import java.util.EnumSet;
import java.util.Set;

import static com.denfop.register.RegisterOreDictionary.list_baseore1;
import static com.denfop.register.RegisterOreDictionary.list_string;
import static com.denfop.register.RegisterOreDictionary.standardList;

public class TileEntityElectricRefractoryFurnace extends TileBasePlasticPlateCreator implements IUpdateTick, IHasRecipe {

    public final InvSlot input_slot;
    private final SoilPollutionComponent pollutionSoil;
    private final AirPollutionComponent pollutionAir;
    public final HeatComponent heat;

    public TileEntityElectricRefractoryFurnace() {
        super(1, 200, 1);
        this.heat = this.addComponent(HeatComponent.asBasicSink(this,1000));
        this.inputSlotA = new InvSlotRecipes(this, "elec_refractory_furnace", this, this.fluidTank);
        fluidTank.setTypeItemSlot(InvSlot.TypeItemSlot.INPUT);
        this.componentProcess.setInvSlotRecipes(inputSlotA);
        this.inputSlotA.setInvSlotConsumableLiquidByList(this.fluidSlot);
        Recipes.recipes.addInitRecipes(this);
        this.input_slot = new InvSlot(this, InvSlot.TypeItemSlot.INPUT, 1) {
            @Override
            public void put(final int index, final ItemStack content) {
                super.put(index, content);
                if (this.get().isEmpty()) {
                    ((TileEntityElectricRefractoryFurnace) this.base).inputSlotA.changeAccepts(ItemStack.EMPTY);
                } else {
                    ((TileEntityElectricRefractoryFurnace) this.base).inputSlotA.changeAccepts(this.get());
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
        this.pollutionSoil = this.addComponent(new SoilPollutionComponent(this, 0.25));
        this.pollutionAir = this.addComponent(new AirPollutionComponent(this, 0.5));
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
        for (String s : list_string) {
            if (OreDictionary.getOres("rawIngot" + s).isEmpty())
                continue;
            final NBTTagCompound nbt = new NBTTagCompound();
            nbt.setInteger("temperature", 1000);
            Recipes.recipes.addRecipe("elec_refractory_furnace", new BaseMachineRecipe(new Input(
                    new FluidStack(FluidName.fluidfluorhyd.getInstance(), 10),
                    input.getInput("crushed" + s)
            ), new RecipeOutput(nbt, OreDictionary.getOres("rawIngot" + s).get(0))));
            Recipes.recipes.addRecipe("elec_refractory_furnace", new BaseMachineRecipe(new Input(
                    new FluidStack(FluidName.fluidfluorhyd.getInstance(), 10),
                    input.getInput("dust" + s)
            ), new RecipeOutput(nbt, OreDictionary.getOres("rawIngot" + s).get(0))));
        }
        final NBTTagCompound nbt1 = new NBTTagCompound();
        nbt1.setInteger("temperature", 1000);
        Recipes.recipes.addRecipe("elec_refractory_furnace", new BaseMachineRecipe(new Input(
                new FluidStack(FluidName.fluidfluorhyd.getInstance(), 10),
                input.getInput(new ItemStack(IUItem.crafting_elements, 1, 502))
        ), new RecipeOutput(nbt1, IUItem.advIronIngot)));
        for (String s : list_baseore1) {
            if (OreDictionary.getOres("rawIngot" + s).isEmpty())
                continue;
            final NBTTagCompound nbt = new NBTTagCompound();
            nbt.setInteger("temperature", 1000);
            Recipes.recipes.addRecipe("elec_refractory_furnace", new BaseMachineRecipe(new Input(
                    new FluidStack(FluidName.fluidfluorhyd.getInstance(), 10),
                    input.getInput("crushed" + s)
            ), new RecipeOutput(nbt, OreDictionary.getOres("rawIngot" + s).get(0))));
            Recipes.recipes.addRecipe("elec_refractory_furnace", new BaseMachineRecipe(new Input(
                    new FluidStack(FluidName.fluidfluorhyd.getInstance(), 10),
                    input.getInput("dust" + s)
            ), new RecipeOutput(nbt, OreDictionary.getOres("rawIngot" + s).get(0))));
        }
        for (String s : standardList) {
            if (OreDictionary.getOres("rawIngot" + s).isEmpty())
                continue;
            final NBTTagCompound nbt = new NBTTagCompound();
            nbt.setInteger("temperature", 1000);
            Recipes.recipes.addRecipe("elec_refractory_furnace", new BaseMachineRecipe(new Input(
                    new FluidStack(FluidName.fluidfluorhyd.getInstance(), 10),
                    input.getInput("crushed" + s)
            ), new RecipeOutput(nbt, OreDictionary.getOres("rawIngot" + s).get(0))));
            Recipes.recipes.addRecipe("elec_refractory_furnace", new BaseMachineRecipe(new Input(
                    new FluidStack(FluidName.fluidfluorhyd.getInstance(), 10),
                    input.getInput("dust" + s)
            ), new RecipeOutput(nbt, OreDictionary.getOres("rawIngot" + s).get(0))));
        }

    }

    public IMultiTileBlock getTeBlock() {
        return BlockBaseMachine3.electric_refractory_furnace;
    }

    public BlockTileEntity getBlock() {
        return IUItem.basemachine2;
    }


    public int gaugeLiquidScaled(int i) {
        return this.getFluidTank().getFluidAmount() <= 0
                ? 0
                : this.getFluidTank().getFluidAmount() * i / this.getFluidTank().getCapacity();
    }


    @Override
    public ContainerElectricRefractoryFurnace getGuiContainer(final EntityPlayer entityPlayer) {
        return new ContainerElectricRefractoryFurnace(entityPlayer, this);
    }

    @SideOnly(Side.CLIENT)
    public GuiScreen getGui(EntityPlayer entityPlayer, boolean isAdmin) {
        return new GuiElectricRefractoryFurnace(new ContainerElectricRefractoryFurnace(entityPlayer, this));

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
