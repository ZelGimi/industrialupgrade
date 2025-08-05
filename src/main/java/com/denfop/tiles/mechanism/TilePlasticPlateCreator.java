package com.denfop.tiles.mechanism;

import com.denfop.IUItem;
import com.denfop.Localization;
import com.denfop.api.Recipes;
import com.denfop.api.gui.EnumTypeSlot;
import com.denfop.api.inv.IAdvInventory;
import com.denfop.api.recipe.*;
import com.denfop.api.tile.IMultiTileBlock;
import com.denfop.api.upgrades.UpgradableProperty;
import com.denfop.audio.EnumSound;
import com.denfop.blocks.BlockTileEntity;
import com.denfop.blocks.FluidName;
import com.denfop.blocks.mechanism.BlockBaseMachine2;
import com.denfop.componets.AirPollutionComponent;
import com.denfop.componets.SoilPollutionComponent;
import com.denfop.container.ContainerBase;
import com.denfop.container.ContainerPlasticPlateCreator;
import com.denfop.gui.GuiCore;
import com.denfop.gui.GuiPlasticPlateCreator;
import com.denfop.invslot.InvSlot;
import com.denfop.recipe.IInputHandler;
import com.denfop.tiles.base.TileBasePlasticPlateCreator;
import com.denfop.utils.ModUtils;
import com.denfop.utils.ParticleUtils;
import net.minecraft.core.BlockPos;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.material.Fluids;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.fluids.FluidStack;

import java.util.EnumSet;
import java.util.Set;

public class TilePlasticPlateCreator extends TileBasePlasticPlateCreator implements IUpdateTick, IHasRecipe {

    public final InvSlot input_slot;
    private final SoilPollutionComponent pollutionSoil;
    private final AirPollutionComponent pollutionAir;

    public TilePlasticPlateCreator(BlockPos pos, BlockState state) {
        super(1, 300, 1, BlockBaseMachine2.plastic_plate_creator, pos, state);
        this.inputSlotA = new InvSlotRecipes(this, "plasticplate", this, this.fluidTank);
        fluidTank.setTypeItemSlot(InvSlot.TypeItemSlot.INPUT);
        this.componentProcess.setInvSlotRecipes(inputSlotA);
        this.inputSlotA.setInvSlotConsumableLiquidByList(this.fluidSlot);
        Recipes.recipes.addInitRecipes(this);
        this.input_slot = new InvSlot(this, InvSlot.TypeItemSlot.INPUT, 1) {
            @Override
            public ItemStack set(final int index, final ItemStack content) {
                super.set(index, content);
                if (this.get(0).isEmpty()) {
                    ((TilePlasticPlateCreator) this.base).inputSlotA.changeAccepts(ItemStack.EMPTY);
                } else {
                    ((TilePlasticPlateCreator) this.base).inputSlotA.changeAccepts(this.get(0));
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
        this.pollutionSoil = this.addComponent(new SoilPollutionComponent(this, 0.1));
        this.pollutionAir = this.addComponent(new AirPollutionComponent(this, 0.1));
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
        Recipes.recipes.addRecipe("plasticplate", new BaseMachineRecipe(new Input(
                new FluidStack(FluidName.fluidHelium.getInstance().get(), 1000),
                input.getInput(new ItemStack(IUItem.crafting_elements.getStack(295)))
        ), new RecipeOutput(null, new ItemStack(IUItem.crafting_elements.getStack(769)))));

        Recipes.recipes.addRecipe("plasticplate", new BaseMachineRecipe(new Input(
                new FluidStack(FluidName.fluidoxy.getInstance().get(), 1000),
                input.getInput(new ItemStack(IUItem.plast.getItem()))
        ), new RecipeOutput(null, new ItemStack(IUItem.plastic_plate.getItem()))));

        Recipes.recipes.addRecipe("plasticplate", new BaseMachineRecipe(new Input(
                new FluidStack(Fluids.WATER, 500),
                input.getInput(new ItemStack(Blocks.DIRT))
        ), new RecipeOutput(null, new ItemStack(Blocks.CLAY))));


        Recipes.recipes.addRecipe("plasticplate", new BaseMachineRecipe(
                new Input(
                        new FluidStack(FluidName.fluidglucose.getInstance().get(), 200),
                        input.getInput(Items.GLOWSTONE_DUST)
                ),
                new RecipeOutput(null, new ItemStack(Items.SUGAR, 2))
        ));
        Recipes.recipes.addRecipe("plasticplate", new BaseMachineRecipe(new Input(
                new FluidStack(FluidName.fluidco2.getInstance().get(), 500),
                input.getInput(new ItemStack(Items.FLINT, 4))
        ), new RecipeOutput(null, new ItemStack(IUItem.crafting_elements.getStack(344), 1))));
        Recipes.recipes.addRecipe("plasticplate", new BaseMachineRecipe(new Input(
                new FluidStack(Fluids.WATER, 500),
                input.getInput(new ItemStack(IUItem.iudust.getStack(66), 2))
        ), new RecipeOutput(null, new ItemStack(IUItem.raw_apatite.getItem()))));

        Recipes.recipes.addRecipe("plasticplate", new BaseMachineRecipe(new Input(
                new FluidStack(FluidName.fluidoxy.getInstance().get(), 200),
                input.getInput(new ItemStack(IUItem.red_phosphorus.getItem(), 4))
        ), new RecipeOutput(null, new ItemStack(IUItem.phosphorus_oxide.getItem(), 2))));

        Recipes.recipes.addRecipe("plasticplate", new BaseMachineRecipe(new Input(
                new FluidStack(FluidName.fluidglowstone.getInstance().get(), 125),
                input.getInput(new ItemStack(IUItem.iudust.getStack(75), 1))
        ), new RecipeOutput(null, new ItemStack(Items.ENDER_PEARL))));

        Recipes.recipes.addRecipe("plasticplate", new BaseMachineRecipe(new Input(
                new FluidStack(FluidName.fluidglowstone.getInstance().get(), 125),
                input.getInput(new ItemStack(IUItem.iudust.getStack(77), 1))
        ), new RecipeOutput(null, new ItemStack(Items.GHAST_TEAR))));


        Recipes.recipes.addRecipe("plasticplate", new BaseMachineRecipe(new Input(
                new FluidStack(FluidName.fluidchlorum.getInstance().get(), 180),
                input.getInput(new ItemStack(IUItem.iudust.getStack(26), 2))
        ), new RecipeOutput(null, new ItemStack(IUItem.iudust.getStack(79), 1))));

        Recipes.recipes.addRecipe("plasticplate", new BaseMachineRecipe(new Input(
                new FluidStack(FluidName.fluidorthophosphoricacid.getInstance().get(), 100),
                input.getInput(new ItemStack(IUItem.iudust.getStack(37), 1))
        ), new RecipeOutput(null, new ItemStack(IUItem.iudust.getStack(70), 1))));

        Recipes.recipes.addRecipe("plasticplate", new BaseMachineRecipe(new Input(
                new FluidStack(FluidName.fluidorthophosphoricacid.getInstance().get(), 100),
                input.getInput(new ItemStack(IUItem.iudust.getStack(64), 1))
        ), new RecipeOutput(null, new ItemStack(IUItem.iudust.getStack(69), 1))));

        Recipes.recipes.addRecipe("plasticplate", new BaseMachineRecipe(new Input(
                new FluidStack(FluidName.fluidbutadiene_nitrile.getInstance().get(), 100),
                input.getInput(ModUtils.setSize(IUItem.rubber, 1))
        ), new RecipeOutput(null, new ItemStack(IUItem.synthetic_rubber.getItem(), 4))));

        Recipes.recipes.addRecipe("plasticplate", new BaseMachineRecipe(new Input(
                new FluidStack(FluidName.fluidroyaljelly.getInstance().get(), 200),
                input.getInput(Items.WHEAT)
        ), new RecipeOutput(null, new ItemStack(IUItem.royal_jelly.getItem()))));

    }

    public IMultiTileBlock getTeBlock() {
        return BlockBaseMachine2.plastic_plate_creator;
    }

    public BlockTileEntity getBlock() {
        return IUItem.basemachine1.getBlock(this.getTeBlock().getId());
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
    public void updateEntityServer() {
        super.updateEntityServer();
        if (this.getActive()  && this.level.getGameTime() % 5 == 0){
            ParticleUtils.spawnPlasticParticles(level,pos,level.random);
        }
    }

    @Override
    public ContainerPlasticPlateCreator getGuiContainer(final Player entityPlayer) {
        return new ContainerPlasticPlateCreator(entityPlayer, this);
    }

    @OnlyIn(Dist.CLIENT)
    public GuiCore<ContainerBase<? extends IAdvInventory>> getGui(Player entityPlayer, ContainerBase<? extends IAdvInventory> isAdmin) {
        return new GuiPlasticPlateCreator((ContainerPlasticPlateCreator) isAdmin);

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
