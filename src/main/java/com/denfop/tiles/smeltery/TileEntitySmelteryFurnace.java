package com.denfop.tiles.smeltery;

import com.denfop.IUItem;
import com.denfop.api.Recipes;
import com.denfop.api.recipe.*;
import com.denfop.api.recipe.InventoryRecipes;
import com.denfop.api.tile.IMultiTileBlock;
import com.denfop.blocks.BlockTileEntity;
import com.denfop.blocks.FluidName;
import com.denfop.blocks.mechanism.BlockSmeltery;
import com.denfop.componets.ComponentProgress;
import com.denfop.container.ContainerSmelteryFurnace;
import com.denfop.gui.GuiSmelteryFurnace;
import com.denfop.recipe.IInputHandler;
import com.denfop.tiles.mechanism.multiblocks.base.TileEntityMultiBlockElement;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import java.util.Arrays;

public class TileEntitySmelteryFurnace extends TileEntityMultiBlockElement implements IFurnace, IHasRecipe {


    public final InventoryRecipes smeltery;
    public final ComponentProgress progress;
    private MachineRecipe output;
    private boolean changeRecipe;

    public TileEntitySmelteryFurnace() {
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
        if (!this.getWorld().isRemote) {
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
    public ContainerSmelteryFurnace getGuiContainer(final EntityPlayer var1) {
        return new ContainerSmelteryFurnace(this, var1);
    }

    @Override
    @SideOnly(Side.CLIENT)
    public GuiScreen getGui(final EntityPlayer var1, final boolean var2) {
        return new GuiSmelteryFurnace(getGuiContainer(var1));
    }

    @Override
    public IMultiTileBlock getTeBlock() {
        return BlockSmeltery.smeltery_furnace;
    }

    @Override
    public BlockTileEntity getBlock() {
        return IUItem.smeltery;
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
                new ItemStack(IUItem.crafting_elements, 1, 503),
                new FluidStack(FluidName.fluidtitaniumsteel.getInstance(), 100)
        );
        addRecipe(
                "gemQuartz",
                new ItemStack(Items.QUARTZ),
                new FluidStack(FluidName.fluidquartz.getInstance(), 144)
        );
        addRecipe(
                "",
                new ItemStack(IUItem.crafting_elements, 1, 499),
                new FluidStack(FluidName.fluidcarbon.getInstance(), 144)
        );
        String[] names = new String[]{"ingot", "plate", "casing", "raw", "block", "gear"};
        int[] amount = new int[]{1, 1, 2, 1, 1, 1};
        int[] amount1 = new int[]{1, 1, 1, 1, 9, 4};
        for (int i = 0; i < names.length; i++) {
            if (i == 3) {
                continue;
            }

            addRecipe(
                    names[i] + "Invar",
                    new ItemStack(IUItem.crafting_elements, 1, 503),
                    new FluidStack(FluidName.fluidinvar.getInstance(), amount1[i] * 100 / amount[i])
            );
            addRecipe(
                    names[i] + "Electrum",
                    new ItemStack(IUItem.crafting_elements, 1, 503),
                    new FluidStack(FluidName.fluidelectrum.getInstance(), amount1[i] * 100 / amount[i])
            );
            addRecipe(
                    names[i] + "GalliumArsenic",
                    new ItemStack(IUItem.crafting_elements, 1, 503),
                    new FluidStack(FluidName.fluidarsenicum_gallium.getInstance(), amount1[i] * 100 / amount[i])
            );
            addRecipe(
                    names[i] + "Nichrome",
                    new ItemStack(IUItem.crafting_elements, 1, 503),
                    new FluidStack(FluidName.fluidnichrome.getInstance(), amount1[i] * 100 / amount[i])
            );
            addRecipe(
                    names[i] + "Duralumin",
                    new ItemStack(IUItem.crafting_elements, 1, 503),
                    new FluidStack(FluidName.fluidduralumin.getInstance(), amount1[i] * 100 / amount[i])
            );
            if (i != 5) {
                addRecipe(
                        names[i] + "Bronze",
                        new ItemStack(IUItem.crafting_elements, 1, 503),
                        new FluidStack(FluidName.fluidbronze.getInstance(), amount1[i] * 100 / amount[i])
                );
            }
            addRecipe(
                    names[i] + "Ferromanganese",
                    new ItemStack(IUItem.crafting_elements, 1, 503),
                    new FluidStack(FluidName.fluidferromanganese.getInstance(), amount1[i] * 100 / amount[i])
            );
            addRecipe(
                    names[i] + "Aluminumbronze",
                    new ItemStack(IUItem.crafting_elements, 1, 503),
                    new FluidStack(FluidName.fluidaluminiumbronze.getInstance(), amount1[i] * 100 / amount[i])
            );
        }
        for (int i = 0; i < names.length; i++) {
            if (i != 5) {
                addRecipe(
                        names[i] + "Iron",
                        null,
                        new FluidStack(FluidName.fluidiron.getInstance(), amount1[i] * 144 / amount[i])
                );
            }
            if (i != 5) {
                addRecipe(
                        names[i] + "Gold",
                        new ItemStack(Items.GOLD_INGOT),
                        new FluidStack(FluidName.fluidgold.getInstance(), amount1[i] * 144 / amount[i])
                );
            }

            addRecipe(
                    names[i] + "Aluminium",
                    new ItemStack(IUItem.iuingot, 1, 1),
                    new FluidStack(FluidName.fluidaluminium.getInstance(), amount1[i] * 144 / amount[i])
            );
            addRecipe(
                    names[i] + "Manganese",
                    new ItemStack(IUItem.iuingot, 1, 16),
                    new FluidStack(FluidName.fluidmanganese.getInstance(), amount1[i] * 144 / amount[i])
            );
            if (i != 5) {
                addRecipe(
                        names[i] + "Tin",
                        new ItemStack(IUItem.iuingot, 1, 24),
                        new FluidStack(FluidName.fluidtin.getInstance(), amount1[i] * 144 / amount[i])
                );
            }
            addRecipe(
                    names[i] + "Magnesium",
                    new ItemStack(IUItem.iuingot, 1, 7),
                    new FluidStack(FluidName.fluidmagnesium.getInstance(), amount1[i] * 144 / amount[i])
            );
            if (i != 5) {
                addRecipe(
                        names[i] + "Copper",
                        new ItemStack(IUItem.iuingot, 1, 21),
                        new FluidStack(FluidName.fluidcopper.getInstance(), amount1[i] * 144 / amount[i])
                );
            }
            addRecipe(
                    names[i] + "Arsenic",
                    new ItemStack(IUItem.iuingot, 1, 28),
                    new FluidStack(FluidName.fluidarsenicum.getInstance(), amount1[i] * 144 / amount[i])
            );
            addRecipe(
                    names[i] + "Titanium",
                    new ItemStack(IUItem.iuingot, 1, 10),
                    new FluidStack(FluidName.fluidtitanium.getInstance(), amount1[i] * 144 / amount[i])
            );
            addRecipe(
                    names[i] + "Gallium",
                    new ItemStack(IUItem.iuingot, 1, 32),
                    new FluidStack(FluidName.fluidgallium.getInstance(), amount1[i] * 144 / amount[i])
            );
            if (!names[i].equals("raw") && i != 5) {
                addRecipe(
                        names[i] + "Steel",
                        new ItemStack(IUItem.iuingot, 1, 23),
                        new FluidStack(FluidName.fluidsteel.getInstance(), amount1[i] * 144 / amount[i])
                );
            }
            addRecipe(
                    names[i] + "Nickel",
                    new ItemStack(IUItem.iuingot, 1, 8),
                    new FluidStack(FluidName.fluidnickel.getInstance(), amount1[i] * 144 / amount[i])
            );
            addRecipe(
                    names[i] + "Silver",
                    new ItemStack(IUItem.iuingot, 1, 14),
                    new FluidStack(FluidName.fluidsilver.getInstance(), amount1[i] * 144 / amount[i])
            );
            addRecipe(
                    names[i] + "Tungsten",
                    new ItemStack(IUItem.iuingot, 1, 3),
                    new FluidStack(FluidName.fluidtungsten.getInstance(), amount1[i] * 144 / amount[i])
            );
            addRecipe(
                    names[i] + "Chromium",
                    new ItemStack(IUItem.iuingot, 1, 11),
                    new FluidStack(FluidName.fluidchromium.getInstance(), amount1[i] * 144 / amount[i])
            );
        }
    }

}
