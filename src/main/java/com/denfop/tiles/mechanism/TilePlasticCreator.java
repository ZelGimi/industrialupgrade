package com.denfop.tiles.mechanism;

import com.denfop.IUItem;
import com.denfop.Localization;
import com.denfop.api.Recipes;
import com.denfop.api.gui.EnumTypeSlot;
import com.denfop.api.inv.IAdvInventory;
import com.denfop.api.recipe.*;
import com.denfop.api.tile.IMultiTileBlock;
import com.denfop.api.upgrades.UpgradableProperty;
import com.denfop.blocks.BlockTileEntity;
import com.denfop.blocks.FluidName;
import com.denfop.blocks.mechanism.BlockBaseMachine2;
import com.denfop.componets.AirPollutionComponent;
import com.denfop.componets.SoilPollutionComponent;
import com.denfop.container.ContainerBase;
import com.denfop.container.ContainerPlasticCreator;
import com.denfop.gui.GuiCore;
import com.denfop.gui.GuiPlasticCreator;
import com.denfop.invslot.InvSlot;
import com.denfop.recipe.IInputHandler;
import com.denfop.tiles.base.TileBasePlasticCreator;
import net.minecraft.core.BlockPos;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.material.Fluids;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.fluids.FluidStack;

import java.util.EnumSet;
import java.util.Set;

public class TilePlasticCreator extends TileBasePlasticCreator implements IHasRecipe {

    public final InvSlot input_slot;
    private final SoilPollutionComponent pollutionSoil;
    private final AirPollutionComponent pollutionAir;

    public TilePlasticCreator(BlockPos pos, BlockState state) {
        super(1, 300, 1, BlockBaseMachine2.plastic_creator, pos, state);
        this.inputSlotA = new InvSlotRecipes(this, "plastic", this, this.fluidTank);
        this.componentProcess.setInvSlotRecipes(inputSlotA);
        this.inputSlotA.setInvSlotConsumableLiquidByList(this.fluidSlot);
        fluidTank.setTypeItemSlot(InvSlot.TypeItemSlot.INPUT);
        this.pollutionSoil = this.addComponent(new SoilPollutionComponent(this, 0.1));
        this.pollutionAir = this.addComponent(new AirPollutionComponent(this, 0.25));
        Recipes.recipes.addInitRecipes(this);
        this.input_slot = new InvSlot(this, InvSlot.TypeItemSlot.INPUT, 1) {
            @Override
            public ItemStack set(final int index, final ItemStack content) {
                super.set(index, content);
                if (this.get(0).isEmpty()) {
                    ((TilePlasticCreator) this.base).inputSlotA.changeAccepts(ItemStack.EMPTY);
                } else {
                    ((TilePlasticCreator) this.base).inputSlotA.changeAccepts(this.get(0));
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
    }

    private static void spawnParticles(Level level, double x, double y, double z) {
        level.addParticle(ParticleTypes.LARGE_SMOKE, x, y, z, 0, 0.3, 0);
        level.addParticle(ParticleTypes.SMOKE, x, y, z, 0, 0.1, 0);
        level.addParticle(ParticleTypes.FLAME, x, y - 0.1, z, 0, 0.025, 0);
    }

    private static void spawnFlame(Level level, double x, double y, double z) {
        level.addParticle(ParticleTypes.FLAME, x, y, z, 0, 0.025, 0);
    }

    public ContainerPlasticCreator getGuiContainer(Player entityPlayer) {
        return new ContainerPlasticCreator(entityPlayer, this);

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

    public IMultiTileBlock getTeBlock() {
        return BlockBaseMachine2.plastic_creator;
    }

    public BlockTileEntity getBlock() {
        return IUItem.basemachine1.getBlock(this.getTeBlock().getId());
    }

    public void init() {
        final IInputHandler input = com.denfop.api.Recipes.inputFactory;
        Recipes.recipes.addRecipe("plastic", new BaseMachineRecipe(
                new Input(
                        new FluidStack(Fluids.WATER, 1000),
                        input.getInput(new ItemStack(IUItem.crafting_elements.getStack(483), 1)),
                        input.getInput(new ItemStack(IUItem.crafting_elements.getStack(484), 1))
                ),
                new RecipeOutput(null, new ItemStack(IUItem.plast.getItem()))
        ));
        Recipes.recipes.addRecipe("plastic", new BaseMachineRecipe(
                new Input(
                        new FluidStack(Fluids.WATER, 1000),
                        input.getInput(new ItemStack(IUItem.iudust.getStack(38), 1)),
                        input.getInput("forge:dusts/Coal", 4)
                ),
                new RecipeOutput(null, new ItemStack(IUItem.iudust.getStack(39), 1))
        ));


        Recipes.recipes.addRecipe("plastic", new BaseMachineRecipe(
                new Input(
                        new FluidStack(FluidName.fluidHelium.getInstance().get(), 1000),
                        input.getInput(new ItemStack(IUItem.cooling_mixture.getItem())),
                        input.getInput(new ItemStack(IUItem.crafting_elements.getStack(386), 1))
                ),
                new RecipeOutput(null, new ItemStack(IUItem.helium_cooling_mixture.getItem()))
        ));

        Recipes.recipes.addRecipe("plastic", new BaseMachineRecipe(
                new Input(
                        new FluidStack(FluidName.fluidcryogen.getInstance().get(), 1000),
                        input.getInput(new ItemStack(IUItem.helium_cooling_mixture.getItem())),
                        input.getInput(new ItemStack(IUItem.radiationresources.getStack(3), 1))
                ),
                new RecipeOutput(null, new ItemStack(IUItem.cryogenic_cooling_mixture.getItem()))
        ));

        Recipes.recipes.addRecipe("plastic", new BaseMachineRecipe(
                new Input(
                        new FluidStack(FluidName.fluidazot.getInstance().get(), 12000),
                        input.getInput("forge:storage_blocks/Vitalium"),
                        input.getInput(new ItemStack(IUItem.crafting_elements.getStack(269), 1))
                ),
                new RecipeOutput(null, new ItemStack(IUItem.crafting_elements.getStack(270), 1))
        ));

        Recipes.recipes.addRecipe("plastic", new BaseMachineRecipe(
                new Input(
                        new FluidStack(FluidName.fluidpolyeth.getInstance().get(), 500),
                        input.getInput(new ItemStack(IUItem.crafting_elements.getStack(344), 1)),
                        input.getInput("forge:doubleplate/Titanium")
                ),
                new RecipeOutput(null, new ItemStack(IUItem.crafting_elements.getStack(340), 1))
        ));

        Recipes.recipes.addRecipe("plastic", new BaseMachineRecipe(
                new Input(
                        new FluidStack(FluidName.fluidneft.getInstance().get(), 5000),
                        input.getInput(new ItemStack(IUItem.crafting_elements.getStack(283), 8)),
                        input.getInput("forge:ingots/Germanium", 4)
                ),
                new RecipeOutput(null, new ItemStack(IUItem.crafting_elements.getStack(386), 1))
        ));

        Recipes.recipes.addRecipe("plastic", new BaseMachineRecipe(
                new Input(
                        new FluidStack(FluidName.fluidtrinitrotoluene.getInstance().get(), 1000),
                        input.getInput(new ItemStack(Items.GUNPOWDER, 4)),
                        input.getInput(new ItemStack(Items.GLOWSTONE_DUST, 3))
                ),
                new RecipeOutput(null, new ItemStack(IUItem.iudust.getStack(72), 1))
        ));

        Recipes.recipes.addRecipe("plastic", new BaseMachineRecipe(
                new Input(
                        new FluidStack(FluidName.fluidsulfuricacid.getInstance().get(), 100),
                        input.getInput(new ItemStack(IUItem.classic_ore.getItem(3), 4)),
                        input.getInput(IUItem.stoneDust, 2)
                ),
                new RecipeOutput(null, new ItemStack(IUItem.nuclear_res.getStack(21), 1))
        ));
        Recipes.recipes.addRecipe("plastic", new BaseMachineRecipe(
                new Input(
                        new FluidStack(FluidName.fluidnitricacid.getInstance().get(), 100),
                        input.getInput(new ItemStack(IUItem.toriyore.getItem(0))),
                        input.getInput(new ItemStack(IUItem.iudust.getStack(54), 2), 2)
                ),
                new RecipeOutput(null, new ItemStack(IUItem.nuclear_res.getStack(16), 9))
        ));

        Recipes.recipes.addRecipe("plastic", new BaseMachineRecipe(
                new Input(
                        new FluidStack(FluidName.fluidseedoil.getInstance().get(), 1000),
                        input.getInput(new ItemStack(IUItem.beeswax.getItem())),
                        input.getInput(new ItemStack(Items.STICK), 2)
                ),
                new RecipeOutput(null, new ItemStack(IUItem.wax_stick.getItem()))
        ));
        Recipes.recipes.addRecipe("plastic", new BaseMachineRecipe(
                new Input(
                        new FluidStack(FluidName.fluidhoney.getInstance().get(), 1000),
                        input.getInput(new ItemStack(IUItem.wax_stick.getItem())),
                        input.getInput(new ItemStack(IUItem.royal_jelly.getItem()), 1)
                ),
                new RecipeOutput(null, new ItemStack(IUItem.polished_stick.getItem()))
        ));
        for (int i = 0; i < 14; i++) {
            Recipes.recipes.addRecipe("plastic", new BaseMachineRecipe(
                    new Input(new FluidStack(FluidName.fluidtemperedglass.getInstance().get(), 144),
                            input.getInput(new ItemStack(IUItem.solar_day_glass.getStack(i), 1)),
                            input.getInput(new ItemStack(IUItem.solar_night_glass.getStack(i), 1))),
                    new RecipeOutput(null, new ItemStack(IUItem.solar_night_day_glass.getStack(i), 1))
            ));
        }
    }

    @Override
    @OnlyIn(Dist.CLIENT)
    public void updateEntityClient() {
        super.updateEntityClient();
        if (this.getActive()) {
            if (this.getWorld().getGameTime() % 5 == 0) {
                double x = pos.getX();
                double y = pos.getY();
                double z = pos.getZ();
                switch (getFacing()) {
                    case NORTH -> {
                        spawnParticles(level, x + 0.8, y + 1.2, z + 0.8);
                        spawnParticles(level, x + 0.2, y + 1.2, z + 0.8);
                    }
                    case SOUTH -> {
                        spawnFlame(level, x + 0.5, y + 1.2, z);
                        spawnFlame(level, x + 0.5, y + 1.2, z + 0.3);
                        spawnFlame(level, x + 0.7, y + 1.2, z + 0.3);
                        spawnFlame(level, x + 0.3, y + 1.2, z + 0.3);
                    }
                    case WEST -> {
                        spawnParticles(level, x + 0.8, y + 1.2, z + 0.8);
                        spawnParticles(level, x + 0.8, y + 1.2, z + 0.2);
                    }
                    case EAST -> {
                        spawnParticles(level, x + 0.2, y + 1.2, z + 0.8);
                        spawnParticles(level, x + 0.2, y + 1.2, z + 0.2);
                    }
                }
            }

        }
    }

    public String getInventoryName() {

        return Localization.translate("iu.blockPlasticCreator.name");
    }

    public int gaugeLiquidScaled(int i) {
        return this.getFluidTank().getFluidAmount() <= 0
                ? 0
                : this.getFluidTank().getFluidAmount() * i / this.getFluidTank().getCapacity();
    }


    @OnlyIn(Dist.CLIENT)
    public GuiCore<ContainerBase<? extends IAdvInventory>> getGui(Player entityPlayer, ContainerBase<? extends IAdvInventory> isAdmin) {
        return new GuiPlasticCreator((ContainerPlasticCreator) isAdmin);

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
