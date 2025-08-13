package com.denfop.tiles.smeltery;

import com.denfop.IUItem;
import com.denfop.api.Recipes;
import com.denfop.api.inv.IAdvInventory;
import com.denfop.api.recipe.*;
import com.denfop.api.tile.IMultiTileBlock;
import com.denfop.blocks.BlockTileEntity;
import com.denfop.blocks.FluidName;
import com.denfop.blocks.mechanism.BlockSmeltery;
import com.denfop.componets.ComponentProgress;
import com.denfop.container.ContainerBase;
import com.denfop.container.ContainerSmelteryFurnace;
import com.denfop.gui.GuiCore;
import com.denfop.gui.GuiSmelteryFurnace;
import com.denfop.recipe.IInputHandler;
import com.denfop.tiles.mechanism.multiblocks.base.TileEntityMultiBlockElement;
import net.minecraft.core.BlockPos;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.block.state.BlockState;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.api.distmarker.OnlyIn;
import net.neoforged.neoforge.fluids.FluidStack;

import java.util.Arrays;

public class TileEntitySmelteryFurnace extends TileEntityMultiBlockElement implements IFurnace, IHasRecipe {


    public final InvSlotRecipes smeltery;
    public final ComponentProgress progress;
    private MachineRecipe output;
    private boolean changeRecipe;

    public TileEntitySmelteryFurnace(BlockPos pos, BlockState state) {
        super(BlockSmeltery.smeltery_furnace, pos, state);
        this.smeltery = new InvSlotRecipes(this, "smeltery", this);
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
    public ContainerSmelteryFurnace getGuiContainer(final Player var1) {
        return new ContainerSmelteryFurnace(this, var1);
    }

    @Override
    @OnlyIn(Dist.CLIENT)
    public GuiCore<ContainerBase<? extends IAdvInventory>> getGui(Player var1, ContainerBase<? extends IAdvInventory> menu) {
        return new GuiSmelteryFurnace((ContainerSmelteryFurnace) menu);
    }

    @Override
    public IMultiTileBlock getTeBlock() {
        return BlockSmeltery.smeltery_furnace;
    }

    @Override
    public BlockTileEntity getBlock() {
        return IUItem.smeltery.getBlock(getTeBlock());
    }

    @Override
    public InvSlotRecipes getInvSlot() {
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
                "c:gems/Quartz",
                new ItemStack(Items.QUARTZ),
                new FluidStack(FluidName.fluidquartz.getInstance().get(), 144)
        );
        addRecipe(
                "",
                new ItemStack(IUItem.crafting_elements.getStack(499)),
                new FluidStack(FluidName.fluidcarbon.getInstance().get(), 144)
        );
        String[] names = new String[]{"c:ingots/", "c:plates/", "c:casings/", "c:raw_materials/", "c:storage_blocks/", "c:gears/"};
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
            if (!names[i].equals("c:raw_materials/") && i != 5) {
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
