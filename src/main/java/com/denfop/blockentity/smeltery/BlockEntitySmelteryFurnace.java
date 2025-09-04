package com.denfop.blockentity.smeltery;

import com.denfop.IUItem;
import com.denfop.api.Recipes;
import com.denfop.api.blockentity.MultiBlockEntity;
import com.denfop.api.container.CustomWorldContainer;
import com.denfop.api.recipe.*;
import com.denfop.blockentity.mechanism.multiblocks.base.BlockEntityMultiBlockElement;
import com.denfop.blocks.BlockTileEntity;
import com.denfop.blocks.FluidName;
import com.denfop.blocks.mechanism.BlockSmelteryEntity;
import com.denfop.componets.ComponentProgress;
import com.denfop.containermenu.ContainerMenuBase;
import com.denfop.containermenu.ContainerMenuSmelteryFurnace;
import com.denfop.recipe.IInputHandler;
import com.denfop.screen.ScreenIndustrialUpgrade;
import com.denfop.screen.ScreenSmelteryFurnace;
import net.minecraft.core.BlockPos;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.fluids.FluidStack;

import java.util.Arrays;

public class BlockEntitySmelteryFurnace extends BlockEntityMultiBlockElement implements IFurnace, IHasRecipe {


    public final InventoryRecipes smeltery;
    public final ComponentProgress progress;
    private MachineRecipe output;
    private boolean changeRecipe;

    public BlockEntitySmelteryFurnace(BlockPos pos, BlockState state) {
        super(BlockSmelteryEntity.smeltery_furnace, pos, state);
        this.smeltery = new InventoryRecipes(this, "smeltery", this);
        this.progress = this.addComponent(new ComponentProgress(this, 1, 108));
        Recipes.recipes.addInitRecipes(this);
    }

    public static void addRecipe(String container, ItemStack container1, FluidStack... fluidStack) {
        final IInputHandler input = com.denfop.api.Recipes.inputFactory;
        Recipes.recipes.addRecipe(
                "smeltery",
                new BaseMachineRecipe(
                        new Input(
                                input.getInput(container.isEmpty() ? container1 : container)),
                        new RecipeOutput(null, container1)
                )
        );
        if (!container.isEmpty()) {
            Recipes.recipes.getRecipeFluid().addRecipe("smeltery", new BaseFluidMachineRecipe(new InputFluid(
                    container), Arrays.asList(
                    fluidStack)));
        } else {
            Recipes.recipes.getRecipeFluid().addRecipe("smeltery", new BaseFluidMachineRecipe(new InputFluid(
                    container1), Arrays.asList(
                    fluidStack)));
        }
    }

    public boolean isChangeRecipe() {
        return changeRecipe;
    }

    public void setChangeRecipe(final boolean changeRecipe) {
        this.changeRecipe = changeRecipe;
    }

    public void onLoaded() {
        super.onLoaded();
        if (!this.getWorld().isClientSide) {
            smeltery.load();
            this.getOutput();
        }
    }

    @Override
    public void updateEntityServer() {
        super.updateEntityServer();
    }

    public MachineRecipe getOutput() {
        this.output = this.smeltery.process();
        return this.output;
    }

    @Override
    public ComponentProgress getComponent() {
        return progress;
    }

    @Override
    public boolean isActive() {
        return getActive();
    }

    @Override
    public boolean hasOwnInventory() {
        return true;
    }

    @Override
    public ContainerMenuSmelteryFurnace getGuiContainer(final Player var1) {
        return new ContainerMenuSmelteryFurnace(this, var1);
    }

    @Override
    @OnlyIn(Dist.CLIENT)
    public ScreenIndustrialUpgrade<ContainerMenuBase<? extends CustomWorldContainer>> getGui(Player var1, ContainerMenuBase<? extends CustomWorldContainer> menu) {
        return new ScreenSmelteryFurnace((ContainerMenuSmelteryFurnace) menu);
    }

    @Override
    public MultiBlockEntity getTeBlock() {
        return BlockSmelteryEntity.smeltery_furnace;
    }

    @Override
    public BlockTileEntity getBlock() {
        return IUItem.smeltery.getBlock(getTeBlock());
    }

    @Override
    public InventoryRecipes getInvSlot() {
        return smeltery;
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
        if (this.output != output && output != null && this.output != null) {
            this.changeRecipe = true;
        }
        this.output = output;
    }

    @Override
    public void init() {

        addRecipe(
                "",
                new ItemStack(IUItem.crafting_elements.getStack(503)),
                new FluidStack(FluidName.fluidtitaniumsteel.getInstance().get(), 144)
        );
        addRecipe(
                "forge:gems/Quartz",
                new ItemStack(Items.QUARTZ),
                new FluidStack(FluidName.fluidquartz.getInstance().get(), 144)
        );
        addRecipe(
                "",
                new ItemStack(IUItem.crafting_elements.getStack(499)),
                new FluidStack(FluidName.fluidcarbon.getInstance().get(), 144)
        );
        String[] names = new String[]{"forge:ingots/", "forge:plates/", "forge:casings/", "forge:raw_materials/", "forge:storage_blocks/", "forge:gears/"};
        int[] amount = new int[]{1, 1, 2, 1, 1, 1};
        int[] amount1 = new int[]{1, 1, 1, 1, 9, 4};
        for (int i = 0; i < names.length; i++) {
            if (i == 3) {
                continue;
            }

            addRecipe(
                    names[i] + "Invar",
                    new ItemStack(IUItem.crafting_elements.getStack(503)),
                    new FluidStack(FluidName.fluidinvar.getInstance().get(), amount1[i] * 144 / amount[i])
            );
            addRecipe(
                    names[i] + "Electrum",
                    new ItemStack(IUItem.crafting_elements.getStack(503)),
                    new FluidStack(FluidName.fluidelectrum.getInstance().get(), amount1[i] * 144 / amount[i])
            );
            addRecipe(
                    names[i] + "GalliumArsenic",
                    new ItemStack(IUItem.crafting_elements.getStack(503)),
                    new FluidStack(FluidName.fluidarsenicum_gallium.getInstance().get(), amount1[i] * 144 / amount[i])
            );
            addRecipe(
                    names[i] + "Nichrome",
                    new ItemStack(IUItem.crafting_elements.getStack(503)),
                    new FluidStack(FluidName.fluidnichrome.getInstance().get(), amount1[i] * 144 / amount[i])
            );
            addRecipe(
                    names[i] + "Duralumin",
                    new ItemStack(IUItem.crafting_elements.getStack(503)),
                    new FluidStack(FluidName.fluidduralumin.getInstance().get(), amount1[i] * 144 / amount[i])
            );
            if (i != 5) {
                addRecipe(
                        names[i] + "Bronze",
                        new ItemStack(IUItem.crafting_elements.getStack(503)),
                        new FluidStack(FluidName.fluidbronze.getInstance().get(), amount1[i] * 144 / amount[i])
                );
            }
            addRecipe(
                    names[i] + "Ferromanganese",
                    new ItemStack(IUItem.crafting_elements.getStack(503)),
                    new FluidStack(FluidName.fluidferromanganese.getInstance().get(), amount1[i] * 144 / amount[i])
            );
            addRecipe(
                    names[i] + "Aluminumbronze",
                    new ItemStack(IUItem.crafting_elements.getStack(503)),
                    new FluidStack(FluidName.fluidaluminiumbronze.getInstance().get(), amount1[i] * 144 / amount[i])
            );
        }
        for (int i = 0; i < names.length; i++) {
            if (i != 5) {
                addRecipe(
                        names[i] + "Iron",
                        null,
                        new FluidStack(FluidName.fluidiron.getInstance().get(), amount1[i] * 144 / amount[i])
                );
            }
            if (i != 5) {
                addRecipe(
                        names[i] + "Gold",
                        new ItemStack(Items.GOLD_INGOT),
                        new FluidStack(FluidName.fluidgold.getInstance().get(), amount1[i] * 144 / amount[i])
                );
            }

            addRecipe(
                    names[i] + "Aluminium",
                    new ItemStack(IUItem.iuingot.getStack(1)),
                    new FluidStack(FluidName.fluidaluminium.getInstance().get(), amount1[i] * 144 / amount[i])
            );
            addRecipe(
                    names[i] + "Manganese",
                    new ItemStack(IUItem.iuingot.getStack(16)),
                    new FluidStack(FluidName.fluidmanganese.getInstance().get(), amount1[i] * 144 / amount[i])
            );
            if (i != 5) {
                addRecipe(
                        names[i] + "Tin",
                        new ItemStack(IUItem.iuingot.getStack(24)),
                        new FluidStack(FluidName.fluidtin.getInstance().get(), amount1[i] * 144 / amount[i])
                );
            }
            addRecipe(
                    names[i] + "Magnesium",
                    new ItemStack(IUItem.iuingot.getStack(7)),
                    new FluidStack(FluidName.fluidmagnesium.getInstance().get(), amount1[i] * 144 / amount[i])
            );
            if (i != 5) {
                addRecipe(
                        names[i] + "Copper",
                        new ItemStack(Items.COPPER_INGOT),
                        new FluidStack(FluidName.fluidcopper.getInstance().get(), amount1[i] * 144 / amount[i])
                );
            }
            addRecipe(
                    names[i] + "Arsenic",
                    new ItemStack(IUItem.iuingot.getStack(28)),
                    new FluidStack(FluidName.fluidarsenicum.getInstance().get(), amount1[i] * 144 / amount[i])
            );
            addRecipe(
                    names[i] + "Titanium",
                    new ItemStack(IUItem.iuingot.getStack(10)),
                    new FluidStack(FluidName.fluidtitanium.getInstance().get(), amount1[i] * 144 / amount[i])
            );
            addRecipe(
                    names[i] + "Gallium",
                    new ItemStack(IUItem.iuingot.getStack(32)),
                    new FluidStack(FluidName.fluidgallium.getInstance().get(), amount1[i] * 144 / amount[i])
            );
            if (!names[i].equals("forge:raw_materials/") && i != 5) {
                addRecipe(
                        names[i] + "Steel",
                        new ItemStack(IUItem.iuingot.getStack(23)),
                        new FluidStack(FluidName.fluidsteel.getInstance().get(), amount1[i] * 144 / amount[i])
                );
            }
            addRecipe(
                    names[i] + "Nickel",
                    new ItemStack(IUItem.iuingot.getStack(8)),
                    new FluidStack(FluidName.fluidnickel.getInstance().get(), amount1[i] * 144 / amount[i])
            );
            addRecipe(
                    names[i] + "Silver",
                    new ItemStack(IUItem.iuingot.getStack(14)),
                    new FluidStack(FluidName.fluidsilver.getInstance().get(), amount1[i] * 144 / amount[i])
            );
            addRecipe(
                    names[i] + "Tungsten",
                    new ItemStack(IUItem.iuingot.getStack(3)),
                    new FluidStack(FluidName.fluidtungsten.getInstance().get(), amount1[i] * 144 / amount[i])
            );
            addRecipe(
                    names[i] + "Chromium",
                    new ItemStack(IUItem.iuingot.getStack(11)),
                    new FluidStack(FluidName.fluidchromium.getInstance().get(), amount1[i] * 144 / amount[i])
            );
        }
    }

}
