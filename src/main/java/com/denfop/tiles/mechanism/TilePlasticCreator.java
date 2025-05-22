package com.denfop.tiles.mechanism;

import com.denfop.IUItem;
import com.denfop.Localization;
import com.denfop.api.Recipes;
import com.denfop.api.gui.EnumTypeSlot;
import com.denfop.api.recipe.BaseMachineRecipe;
import com.denfop.api.recipe.IHasRecipe;
import com.denfop.api.recipe.Input;
import com.denfop.api.recipe.InvSlotRecipes;
import com.denfop.api.recipe.RecipeOutput;
import com.denfop.api.tile.IMultiTileBlock;
import com.denfop.api.upgrades.UpgradableProperty;
import com.denfop.blocks.BlockTileEntity;
import com.denfop.blocks.FluidName;
import com.denfop.blocks.mechanism.BlockBaseMachine2;
import com.denfop.componets.AirPollutionComponent;
import com.denfop.componets.SoilPollutionComponent;
import com.denfop.container.ContainerPlasticCreator;
import com.denfop.gui.GuiPlasticCreator;
import com.denfop.invslot.InvSlot;
import com.denfop.recipe.IInputHandler;
import com.denfop.tiles.base.TileBasePlasticCreator;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumParticleTypes;
import net.minecraft.util.math.BlockPos;
import net.minecraftforge.fluids.FluidRegistry;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import java.util.EnumSet;
import java.util.Set;

public class TilePlasticCreator extends TileBasePlasticCreator implements IHasRecipe {

    public final InvSlot input_slot;
    private final SoilPollutionComponent pollutionSoil;
    private final AirPollutionComponent pollutionAir;

    public TilePlasticCreator() {
        super(1, 300, 1);
        this.inputSlotA = new InvSlotRecipes(this, "plastic", this, this.fluidTank);
        this.componentProcess.setInvSlotRecipes(inputSlotA);
        this.inputSlotA.setInvSlotConsumableLiquidByList(this.fluidSlot);
        fluidTank.setTypeItemSlot(InvSlot.TypeItemSlot.INPUT);
        this.pollutionSoil = this.addComponent(new SoilPollutionComponent(this, 0.1));
        this.pollutionAir = this.addComponent(new AirPollutionComponent(this, 0.25));
        Recipes.recipes.addInitRecipes(this);
        this.input_slot = new InvSlot(this, InvSlot.TypeItemSlot.INPUT, 1) {
            @Override
            public void put(final int index, final ItemStack content) {
                super.put(index, content);
                if (this.get().isEmpty()) {
                    ((TilePlasticCreator) this.base).inputSlotA.changeAccepts(ItemStack.EMPTY);
                } else {
                    ((TilePlasticCreator) this.base).inputSlotA.changeAccepts(this.get());
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

    public ContainerPlasticCreator getGuiContainer(EntityPlayer entityPlayer) {
        return new ContainerPlasticCreator(entityPlayer, this);

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

    public IMultiTileBlock getTeBlock() {
        return BlockBaseMachine2.plastic_creator;
    }

    public BlockTileEntity getBlock() {
        return IUItem.basemachine1;
    }

    public void init() {
        final IInputHandler input = com.denfop.api.Recipes.inputFactory;
        Recipes.recipes.addRecipe("plastic", new BaseMachineRecipe(
                new Input(
                        new FluidStack(FluidRegistry.WATER, 1000),
                        input.getInput(new ItemStack(IUItem.crafting_elements, 1, 483)),
                        input.getInput(new ItemStack(IUItem.crafting_elements, 1, 484))
                ),
                new RecipeOutput(null, new ItemStack(IUItem.plast))
        ));

        Recipes.recipes.addRecipe("plastic", new BaseMachineRecipe(
                new Input(
                        new FluidStack(FluidRegistry.WATER, 1000),
                        input.getInput(new ItemStack(IUItem.iudust, 1, 38)),
                        input.getInput("dustCoal", 4)
                ),
                new RecipeOutput(null, new ItemStack(IUItem.iudust, 1, 39))
        ));
        Recipes.recipes.addRecipe("plastic", new BaseMachineRecipe(
                new Input(
                        new FluidStack(FluidName.fluidoxy.getInstance(), 150),
                        input.getInput(new ItemStack(Items.REDSTONE, 6)),
                        input.getInput("ingotBarium", 1)
                ),
                new RecipeOutput(null, new ItemStack(Blocks.GLOWSTONE))
        ));

        Recipes.recipes.addRecipe("plastic", new BaseMachineRecipe(
                new Input(
                        new FluidStack(FluidName.fluidHelium.getInstance(), 1000),
                        input.getInput(new ItemStack(IUItem.cooling_mixture)),
                        input.getInput(new ItemStack(IUItem.crafting_elements, 1, 386))
                ),
                new RecipeOutput(null, new ItemStack(IUItem.helium_cooling_mixture))
        ));

        Recipes.recipes.addRecipe("plastic", new BaseMachineRecipe(
                new Input(
                        new FluidStack(FluidName.fluidcryogen.getInstance(), 1000),
                        input.getInput(new ItemStack(IUItem.helium_cooling_mixture)),
                        input.getInput(new ItemStack(IUItem.radiationresources, 1, 3))
                ),
                new RecipeOutput(null, new ItemStack(IUItem.cryogenic_cooling_mixture))
        ));

        Recipes.recipes.addRecipe("plastic", new BaseMachineRecipe(
                new Input(
                        new FluidStack(FluidName.fluidazot.getInstance(), 12000),
                        input.getInput("blockVitalium"),
                        input.getInput(new ItemStack(IUItem.crafting_elements, 1, 269))
                ),
                new RecipeOutput(null, new ItemStack(IUItem.crafting_elements, 1, 270))
        ));

        Recipes.recipes.addRecipe("plastic", new BaseMachineRecipe(
                new Input(
                        new FluidStack(FluidName.fluidpolyeth.getInstance(), 500),
                        input.getInput(new ItemStack(IUItem.crafting_elements, 1, 344)),
                        input.getInput("doubleplateTitanium")
                ),
                new RecipeOutput(null, new ItemStack(IUItem.crafting_elements, 1, 340))
        ));

        Recipes.recipes.addRecipe("plastic", new BaseMachineRecipe(
                new Input(
                        new FluidStack(FluidName.fluidneft.getInstance(), 5000),
                        input.getInput(new ItemStack(IUItem.crafting_elements, 8, 283)),
                        input.getInput("ingotGermanium", 4)
                ),
                new RecipeOutput(null, new ItemStack(IUItem.crafting_elements, 1, 386))
        ));

        Recipes.recipes.addRecipe("plastic", new BaseMachineRecipe(
                new Input(
                        new FluidStack(FluidName.fluidtrinitrotoluene.getInstance(), 1000),
                        input.getInput(new ItemStack(Items.GUNPOWDER, 4)),
                        input.getInput(new ItemStack(Items.GLOWSTONE_DUST, 3))
                ),
                new RecipeOutput(null, new ItemStack(IUItem.iudust, 1, 72))
        ));

        Recipes.recipes.addRecipe("plastic", new BaseMachineRecipe(
                new Input(
                        new FluidStack(FluidName.fluidsulfuricacid.getInstance(), 100),
                        input.getInput(new ItemStack(IUItem.classic_ore, 4, 3)),
                        input.getInput(IUItem.stoneDust, 2)
                ),
                new RecipeOutput(null, new ItemStack(IUItem.nuclear_res, 1, 21))
        ));
        Recipes.recipes.addRecipe("plastic", new BaseMachineRecipe(
                new Input(
                        new FluidStack(FluidName.fluidnitricacid.getInstance(), 100),
                        input.getInput(new ItemStack(IUItem.toriyore)),
                        input.getInput(new ItemStack(IUItem.iudust, 2, 54), 2)
                ),
                new RecipeOutput(null, new ItemStack(IUItem.nuclear_res, 9, 16))
        ));

        Recipes.recipes.addRecipe("plastic", new BaseMachineRecipe(
                new Input(
                        new FluidStack(FluidName.fluidseedoil.getInstance(), 1000),
                        input.getInput(new ItemStack(IUItem.beeswax)),
                        input.getInput(new ItemStack(Items.STICK), 2)
                ),
                new RecipeOutput(null, new ItemStack(IUItem.wax_stick))
        ));
        Recipes.recipes.addRecipe("plastic", new BaseMachineRecipe(
                new Input(
                        new FluidStack(FluidName.fluidhoney.getInstance(), 1000),
                        input.getInput(new ItemStack(IUItem.wax_stick)),
                        input.getInput(new ItemStack(IUItem.royal_jelly), 1)
                ),
                new RecipeOutput(null, new ItemStack(IUItem.polished_stick))
        ));
        for (int i = 0; i < 14; i++) {
            Recipes.recipes.addRecipe("plastic", new BaseMachineRecipe(
                    new Input(
                            new FluidStack(FluidName.fluidtemperedglass.getInstance(), 144),
                            input.getInput(new ItemStack(IUItem.solar_day_glass, 1, i)),
                            input.getInput(new ItemStack(IUItem.solar_night_glass, 1, i))
                    ),
                    new RecipeOutput(null, new ItemStack(IUItem.solar_night_day_glass, 1, i))
            ));
        }
    }

    @Override
    @SideOnly(Side.CLIENT)
    public void updateEntityClient() {
        super.updateEntityClient();
        if (this.getActive()) {
            if (this.getWorld().provider.getWorldTime() % 5 == 0) {
                switch (facing) {
                    case 2:
                        this.world.spawnParticle(
                                EnumParticleTypes.SMOKE_LARGE, this.getPos().getX() + 0.8,
                                this.getPos().getY() + 2,
                                this.getPos().getZ() + 0.8, 0, 0.3, 0
                        );
                        this.world.spawnParticle(EnumParticleTypes.SMOKE_LARGE, this.getPos().getX() + 0.2,
                                this.getPos().getY() + 2,
                                this.getPos().getZ() + 0.8, 0, 0.3, 0
                        );
                        this.world.spawnParticle(EnumParticleTypes.SMOKE_NORMAL, this.getPos().getX() + 0.8,
                                this.getPos().getY() + 2,
                                this.getPos().getZ() + 0.8, 0, 0.1, 0
                        );
                        this.world.spawnParticle(EnumParticleTypes.SMOKE_NORMAL, this.getPos().getX() + 0.2,
                                this.getPos().getY() + 2,
                                this.getPos().getZ() + 0.8, 0, 0.1, 0
                        );

                        this.world.spawnParticle(EnumParticleTypes.FLAME, this.getPos().getX() + 0.8,
                                this.getPos().getY() + 1.9,
                                this.getPos().getZ() + 0.8, 0, 0.025, 0
                        );
                        this.world.spawnParticle(EnumParticleTypes.FLAME, this.getPos().getX() + 0.2,
                                this.getPos().getY() + 1.9,
                                this.getPos().getZ() + 0.8, 0, 0.025, 0
                        );
                        break;
                    case 3:

                        this.world.spawnParticle(EnumParticleTypes.FLAME, this.getPos().getX() + 0.5,
                                this.getPos().getY() + 1,
                                this.getPos().getZ(), 0, 0.025, 0
                        );

                        this.world.spawnParticle(EnumParticleTypes.FLAME, this.getPos().getX() + 0.5,
                                this.getPos().getY() + 1,
                                this.getPos().getZ() + 0.3, 0, 0.025, 0
                        );
                        this.world.spawnParticle(EnumParticleTypes.FLAME, this.getPos().getX() + 0.7,
                                this.getPos().getY() + 1,
                                this.getPos().getZ() + 0.3, 0, 0.025, 0
                        );
                        this.world.spawnParticle(EnumParticleTypes.FLAME, this.getPos().getX() + 0.3,
                                this.getPos().getY() + 1,
                                this.getPos().getZ() + 0.3, 0, 0.025, 0
                        );
                        break;
                    case 4:
                        this.world.spawnParticle(EnumParticleTypes.SMOKE_LARGE, this.getPos().getX() + 0.8,
                                this.getPos().getY() + 2,
                                this.getPos().getZ() + 0.8, 0, 0.3, 0
                        );
                        this.world.spawnParticle(EnumParticleTypes.SMOKE_LARGE, this.getPos().getX() + 0.8,
                                this.getPos().getY() + 2,
                                this.getPos().getZ() + 0.2, 0, 0.3, 0
                        );
                        this.world.spawnParticle(EnumParticleTypes.SMOKE_NORMAL, this.getPos().getX() + 0.82,
                                this.getPos().getY() + 2,
                                this.getPos().getZ() + 0.8, 0, 0.1, 0
                        );
                        this.world.spawnParticle(EnumParticleTypes.SMOKE_NORMAL, this.getPos().getX() + 0.8,
                                this.getPos().getY() + 2,
                                this.getPos().getZ() + 0.2, 0, 0.1, 0
                        );

                        this.world.spawnParticle(EnumParticleTypes.FLAME, this.getPos().getX() + 0.8,
                                this.getPos().getY() + 1.9,
                                this.getPos().getZ() + 0.8, 0, 0.025, 0
                        );
                        this.world.spawnParticle(EnumParticleTypes.FLAME, this.getPos().getX() + 0.8,
                                this.getPos().getY() + 1.9,
                                this.getPos().getZ() + 0.2, 0, 0.025, 0
                        );
                        break;
                    case 5:
                        this.world.spawnParticle(EnumParticleTypes.SMOKE_LARGE, this.getPos().getX() + 0.2,
                                this.getPos().getY() + 2,
                                this.getPos().getZ() + 0.8, 0, 0.3, 0
                        );
                        this.world.spawnParticle(EnumParticleTypes.SMOKE_LARGE, this.getPos().getX() + 0.2,
                                this.getPos().getY() + 2,
                                this.getPos().getZ() + 0.2, 0, 0.3, 0
                        );
                        this.world.spawnParticle(EnumParticleTypes.SMOKE_NORMAL, this.getPos().getX() + 0.2,
                                this.getPos().getY() + 2,
                                this.getPos().getZ() + 0.8, 0, 0.1, 0
                        );
                        this.world.spawnParticle(EnumParticleTypes.SMOKE_NORMAL, this.getPos().getX() + 0.2,
                                this.getPos().getY() + 2,
                                this.getPos().getZ() + 0.2, 0, 0.1, 0
                        );

                        this.world.spawnParticle(EnumParticleTypes.FLAME, this.getPos().getX() + 0.2,
                                this.getPos().getY() + 1.9,
                                this.getPos().getZ() + 0.8, 0, 0.025, 0
                        );
                        this.world.spawnParticle(EnumParticleTypes.FLAME, this.getPos().getX() + 0.2,
                                this.getPos().getY() + 1.9,
                                this.getPos().getZ() + 0.2, 0, 0.025, 0
                        );
                        break;
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

    public boolean doesSideBlockRendering(EnumFacing side) {
        return false;
    }

    @SideOnly(Side.CLIENT)
    public boolean shouldSideBeRendered(EnumFacing side, BlockPos otherPos) {
        return false;
    }

    @Override
    public boolean isNormalCube() {
        return false;
    }

    @SideOnly(Side.CLIENT)
    public GuiScreen getGui(EntityPlayer entityPlayer, boolean isAdmin) {
        return new GuiPlasticCreator(new ContainerPlasticCreator(entityPlayer, this));

    }

    public String getStartSoundFile() {
        return "Machines/plastic.ogg";
    }

    public String getInterruptSoundFile() {
        return "Machines/InterruptOne.ogg";
    }

    public float getWrenchDropRate() {
        return 0.85F;
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
