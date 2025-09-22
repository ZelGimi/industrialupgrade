package com.denfop.tiles.smeltery;

import com.denfop.IUCore;
import com.denfop.IUItem;
import com.denfop.api.Recipes;
import com.denfop.api.recipe.BaseFluidMachineRecipe;
import com.denfop.api.recipe.FluidHandlerRecipe;
import com.denfop.api.recipe.IHasRecipe;
import com.denfop.api.recipe.InputFluid;
import com.denfop.api.recipe.InventoryOutput;
import com.denfop.api.recipe.RecipeOutput;
import com.denfop.api.tile.IMultiTileBlock;
import com.denfop.blocks.BlockTileEntity;
import com.denfop.blocks.FluidName;
import com.denfop.blocks.mechanism.BlockSmeltery;
import com.denfop.componets.ComponentProgress;
import com.denfop.container.ContainerSmelteryCasting;
import com.denfop.gui.GuiSmelteryCasting;
import com.denfop.invslot.Inventory;
import com.denfop.network.packet.CustomPacketBuffer;
import com.denfop.tiles.mechanism.multiblocks.base.TileEntityMultiBlockElement;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public class TileEntitySmelteryCasting extends TileEntityMultiBlockElement implements ICasting, IHasRecipe {


    public final ComponentProgress progress;
    public final Inventory inputSlotB;
    public final FluidHandlerRecipe fluid_handler;
    public final InventoryOutput outputSlot;

    public TileEntitySmelteryCasting() {
        super();
        this.progress = this.addComponent(new ComponentProgress(this, 1, 108));
        this.fluid_handler = new FluidHandlerRecipe("ingot_casting");

        this.outputSlot = new InventoryOutput(this, 1);
        this.inputSlotB = new Inventory(this, Inventory.TypeItemSlot.INPUT, 1) {
            @Override
            public boolean isItemValidForSlot(final int index, final ItemStack stack) {
                if (stack.getItem() != IUItem.crafting_elements) {
                    return false;
                }
                int damage = stack.getItemDamage();
                return damage == 496 || damage == 497;
            }

            @Override
            public void put(final int index, final ItemStack content) {
                super.put(index, content);
                if (!getWorld().isRemote) {
                    ((TileEntitySmelteryCasting) this.base).fluid_handler.setName("empty");
                    int damage = content.getItemDamage();
                    ((TileEntitySmelteryCasting) this.base).fluid_handler.setOutput(null);
                    if (damage == 496) {
                        ((TileEntitySmelteryCasting) this.base).fluid_handler.setName("ingot_casting");
                        if (((TileEntitySmelteryCasting) this.base).getMain() != null) {
                            TileEntitySmelteryController controller = (TileEntitySmelteryController) ((TileEntitySmelteryCasting) this.base).getMain();
                            fluid_handler.load();
                            ((TileEntitySmelteryCasting) this.base).fluid_handler.getOutput(controller.getFirstTank());
                            ;
                        }
                    } else if (damage == 497) {
                        ((TileEntitySmelteryCasting) this.base).fluid_handler.setName("gear_casting");
                        TileEntitySmelteryController controller = (TileEntitySmelteryController) ((TileEntitySmelteryCasting) this.base).getMain();
                        fluid_handler.load();
                        ((TileEntitySmelteryCasting) this.base).fluid_handler.getOutput(controller.getFirstTank());
                        ;
                    }
                }
            }
        };


        Recipes.recipes.getRecipeFluid().addInitRecipes(this);

    }

    @Override
    public void readContainerPacket(final CustomPacketBuffer customPacketBuffer) {
        super.readContainerPacket(customPacketBuffer);
        boolean hasMain = customPacketBuffer.readBoolean();
        if (hasMain && this.getMain() != null) {
            TileEntitySmelteryController controller = (TileEntitySmelteryController) this.getMain();
            controller.readContainerPacket(customPacketBuffer);
        }
    }

    @Override
    public CustomPacketBuffer writeContainerPacket() {
        CustomPacketBuffer customPacketBuffer = super.writeContainerPacket();
        customPacketBuffer.writeBoolean(this.getMain() != null);
        if (this.getMain() != null) {
            TileEntitySmelteryController controller = (TileEntitySmelteryController) this.getMain();
            customPacketBuffer.writeBytes(controller.writeContainerPacket());
        }
        return customPacketBuffer;
    }

    public ComponentProgress getProgress() {
        return progress;
    }

    public FluidHandlerRecipe getFluid_handler() {
        return fluid_handler;
    }

    public InventoryOutput getOutputSlot() {
        return outputSlot;
    }

    public Inventory getInputSlotB() {
        return inputSlotB;
    }

    @Override
    public ContainerSmelteryCasting getGuiContainer(final EntityPlayer var1) {
        return new ContainerSmelteryCasting(this, var1);
    }

    @Override
    @SideOnly(Side.CLIENT)
    public GuiScreen getGui(final EntityPlayer var1, final boolean var2) {
        return new GuiSmelteryCasting(getGuiContainer(var1));
    }

    public void onLoaded() {
        super.onLoaded();
        if (IUCore.proxy.isSimulating()) {
            this.fluid_handler.setName("empty");
            int damage = this.inputSlotB.get().getItemDamage();
            if (damage == 496) {
                this.fluid_handler.setName("ingot_casting");
                if (this.getMain() != null) {
                    TileEntitySmelteryController controller = (TileEntitySmelteryController) this.getMain();
                    fluid_handler.load();
                    this.fluid_handler.getOutput(controller.getFirstTank());

                }
            } else if (damage == 497) {
                this.fluid_handler.setName("gear_casting");
                if (this.getMain() != null) {
                    TileEntitySmelteryController controller = (TileEntitySmelteryController) this.getMain();
                    fluid_handler.load();
                    this.fluid_handler.getOutput(controller.getFirstTank());

                }
            } else {
                fluid_handler.load();
            }
        }


    }

    @Override
    public boolean hasOwnInventory() {
        return this.getMain() != null;
    }

    @Override
    public IMultiTileBlock getTeBlock() {
        return BlockSmeltery.smeltery_casting;
    }

    @Override
    public BlockTileEntity getBlock() {
        return IUItem.smeltery;
    }

    @Override
    public void init() {
        Recipes.recipes.getRecipeFluid().addRecipe("ingot_casting", new BaseFluidMachineRecipe(new InputFluid(
                new FluidStack(FluidName.fluidiron.getInstance(), 144)), new RecipeOutput(
                null,
                new ItemStack(Items.IRON_INGOT)
        )));
        Recipes.recipes.getRecipeFluid().addRecipe("ingot_casting", new BaseFluidMachineRecipe(new InputFluid(
                new FluidStack(FluidName.fluidgold.getInstance(), 144)), new RecipeOutput(
                null,
                new ItemStack(Items.GOLD_INGOT)
        )));
        Recipes.recipes.getRecipeFluid().addRecipe("ingot_casting", new BaseFluidMachineRecipe(new InputFluid(
                new FluidStack(FluidName.fluidaluminium.getInstance(), 144)), new RecipeOutput(
                null,
                new ItemStack(IUItem.iuingot, 1, 1)
        )));
        Recipes.recipes.getRecipeFluid().addRecipe("ingot_casting", new BaseFluidMachineRecipe(new InputFluid(
                new FluidStack(FluidName.fluidtin.getInstance(), 144)), new RecipeOutput(
                null,
                new ItemStack(IUItem.iuingot, 1, 24)
        )));
        Recipes.recipes.getRecipeFluid().addRecipe("ingot_casting", new BaseFluidMachineRecipe(new InputFluid(
                new FluidStack(FluidName.fluidcopper.getInstance(), 144)), new RecipeOutput(
                null,
                new ItemStack(IUItem.iuingot, 1, 21)
        )));
        Recipes.recipes.getRecipeFluid().addRecipe("ingot_casting", new BaseFluidMachineRecipe(new InputFluid(
                new FluidStack(FluidName.fluidnichrome.getInstance(), 144)), new RecipeOutput(
                null,
                new ItemStack(IUItem.alloysingot, 1, 4)
        )));
        Recipes.recipes.getRecipeFluid().addRecipe("ingot_casting", new BaseFluidMachineRecipe(new InputFluid(
                new FluidStack(FluidName.fluidtemperedglass.getInstance(), 144)), new RecipeOutput(
                null,
                new ItemStack(IUItem.blockResource, 1, 13)
        )));
        Recipes.recipes.getRecipeFluid().addRecipe("ingot_casting", new BaseFluidMachineRecipe(new InputFluid(
                new FluidStack(FluidName.fluidbronze.getInstance(), 144)), new RecipeOutput(
                null,
                new ItemStack(IUItem.iuingot, 1, 20)
        )));
        Recipes.recipes.getRecipeFluid().addRecipe("ingot_casting", new BaseFluidMachineRecipe(new InputFluid(
                new FluidStack(FluidName.fluidsteel.getInstance(), 144)), new RecipeOutput(
                null,
                new ItemStack(IUItem.crafting_elements, 1, 502)
        )));
        Recipes.recipes.getRecipeFluid().addRecipe("ingot_casting", new BaseFluidMachineRecipe(new InputFluid(
                new FluidStack(FluidName.fluidobsidian.getInstance(), 144 * 9)), new RecipeOutput(
                null,
                new ItemStack(Blocks.OBSIDIAN)
        )));
        Recipes.recipes.getRecipeFluid().addRecipe("ingot_casting", new BaseFluidMachineRecipe(new InputFluid(
                new FluidStack(FluidName.fluidarsenicum.getInstance(), 144)), new RecipeOutput(
                null,
                new ItemStack(IUItem.iuingot, 1, 28)
        )));
        Recipes.recipes.getRecipeFluid().addRecipe("ingot_casting", new BaseFluidMachineRecipe(new InputFluid(
                new FluidStack(FluidName.fluidtitanium.getInstance(), 144)), new RecipeOutput(
                null,
                new ItemStack(IUItem.iuingot, 1, 10)
        )));
        Recipes.recipes.getRecipeFluid().addRecipe("ingot_casting", new BaseFluidMachineRecipe(new InputFluid(
                new FluidStack(FluidName.fluidinvar.getInstance(), 144)), new RecipeOutput(
                null,
                new ItemStack(IUItem.iuingot, 1, 4)
        )));
        Recipes.recipes.getRecipeFluid().addRecipe("ingot_casting", new BaseFluidMachineRecipe(new InputFluid(
                new FluidStack(FluidName.fluidelectrum.getInstance(), 144)), new RecipeOutput(
                null,
                new ItemStack(IUItem.iuingot, 1, 13)
        )));
        Recipes.recipes.getRecipeFluid().addRecipe("ingot_casting", new BaseFluidMachineRecipe(new InputFluid(
                new FluidStack(FluidName.fluidsilver.getInstance(), 144)), new RecipeOutput(
                null,
                new ItemStack(IUItem.iuingot, 1, 14)
        )));
        Recipes.recipes.getRecipeFluid().addRecipe("ingot_casting", new BaseFluidMachineRecipe(new InputFluid(
                new FluidStack(FluidName.fluidaluminiumbronze.getInstance(), 144)), new RecipeOutput(
                null,
                new ItemStack(IUItem.alloysingot, 1, 0)
        )));

        Recipes.recipes.getRecipeFluid().addRecipe("ingot_casting", new BaseFluidMachineRecipe(new InputFluid(
                new FluidStack(FluidName.fluidferromanganese.getInstance(), 144)), new RecipeOutput(
                null,
                new ItemStack(IUItem.alloysingot, 1, 9)
        )));
        Recipes.recipes.getRecipeFluid().addRecipe("ingot_casting", new BaseFluidMachineRecipe(new InputFluid(
                new FluidStack(FluidName.fluidduralumin.getInstance(), 144)), new RecipeOutput(
                null,
                new ItemStack(IUItem.alloysingot, 1, 8)
        )));
        Recipes.recipes.getRecipeFluid().addRecipe("ingot_casting", new BaseFluidMachineRecipe(new InputFluid(
                new FluidStack(FluidName.fluidarsenicum_gallium.getInstance(), 144)), new RecipeOutput(
                null,
                new ItemStack(IUItem.alloysingot, 1, 31)
        )));
        Recipes.recipes.getRecipeFluid().addRecipe("ingot_casting", new BaseFluidMachineRecipe(new InputFluid(
                new FluidStack(FluidName.fluidtitaniumsteel.getInstance(), 144)), new RecipeOutput(
                null,
                new ItemStack(IUItem.crafting_elements, 1, 503)
        )));


        Recipes.recipes.getRecipeFluid().addRecipe("gear_casting", new BaseFluidMachineRecipe(new InputFluid(
                new FluidStack(FluidName.fluidinvar.getInstance(), 144 * 4)), new RecipeOutput(
                null,
                new ItemStack(IUItem.gear, 1, 4)
        )));
        Recipes.recipes.getRecipeFluid().addRecipe("gear_casting", new BaseFluidMachineRecipe(new InputFluid(
                new FluidStack(FluidName.fluidmagnesium.getInstance(), 144 * 4)), new RecipeOutput(
                null,
                new ItemStack(IUItem.gear, 1, 7)
        )));
        Recipes.recipes.getRecipeFluid().addRecipe("gear_casting", new BaseFluidMachineRecipe(new InputFluid(
                new FluidStack(FluidName.fluidelectrum.getInstance(), 144 * 4)), new RecipeOutput(
                null,
                new ItemStack(IUItem.gear, 1, 13)
        )));
        Recipes.recipes.getRecipeFluid().addRecipe("gear_casting", new BaseFluidMachineRecipe(new InputFluid(
                new FluidStack(FluidName.fluidtitanium.getInstance(), 144 * 4)), new RecipeOutput(
                null,
                new ItemStack(IUItem.gear, 1, 10)
        )));
        Recipes.recipes.getRecipeFluid().addRecipe("gear_casting", new BaseFluidMachineRecipe(new InputFluid(
                new FluidStack(FluidName.fluidferromanganese.getInstance(), 144 * 4)), new RecipeOutput(
                null,
                new ItemStack(IUItem.alloygear, 1, 9)
        )));
        Recipes.recipes.getRecipeFluid().addRecipe("gear_casting", new BaseFluidMachineRecipe(new InputFluid(
                new FluidStack(FluidName.fluidaluminiumbronze.getInstance(), 144 * 4)), new RecipeOutput(
                null,
                new ItemStack(IUItem.alloygear, 1, 0)
        )));
        Recipes.recipes.getRecipeFluid().addRecipe("gear_casting", new BaseFluidMachineRecipe(new InputFluid(
                new FluidStack(FluidName.fluidduralumin.getInstance(), 144 * 4)), new RecipeOutput(
                null,
                new ItemStack(IUItem.alloygear, 1, 8)
        )));
    }

}
