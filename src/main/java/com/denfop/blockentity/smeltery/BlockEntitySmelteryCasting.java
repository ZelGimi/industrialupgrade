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
import com.denfop.containermenu.ContainerMenuSmelteryCasting;
import com.denfop.inventory.Inventory;
import com.denfop.items.ItemCraftingElements;
import com.denfop.network.packet.CustomPacketBuffer;
import com.denfop.screen.ScreenIndustrialUpgrade;
import com.denfop.screen.ScreenSmelteryCasting;
import net.minecraft.core.BlockPos;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.fluids.FluidStack;

public class BlockEntitySmelteryCasting extends BlockEntityMultiBlockElement implements ICasting, IHasRecipe {


    public final ComponentProgress progress;
    public final Inventory inputSlotB;
    public final FluidHandlerRecipe fluid_handler;
    public final InventoryOutput outputSlot;

    public BlockEntitySmelteryCasting(BlockPos pos, BlockState state) {
        super(BlockSmelteryEntity.smeltery_casting, pos, state);
        this.progress = this.addComponent(new ComponentProgress(this, 1, 108));
        this.fluid_handler = new FluidHandlerRecipe("ingot_casting");

        this.outputSlot = new InventoryOutput(this, 1);
        this.inputSlotB = new Inventory(this, Inventory.TypeItemSlot.INPUT, 1) {
            @Override
            public boolean canPlaceItem(final int index, final ItemStack stack) {
                if (!(stack.getItem() instanceof ItemCraftingElements<?>)) {
                    return false;
                }
                int damage = ((ItemCraftingElements<?>) stack.getItem()).getElement().getId();
                return damage == 496 || damage == 497;
            }

            @Override
            public ItemStack set(final int index, final ItemStack content) {
                super.set(index, content);
                if (!getWorld().isClientSide) {
                    ((BlockEntitySmelteryCasting) this.base).fluid_handler.setName("empty");
                    if (!content.isEmpty()) {
                        int damage = ((ItemCraftingElements<?>) content.getItem()).getElement().getId();
                        ((BlockEntitySmelteryCasting) this.base).fluid_handler.setOutput(null);
                        if (damage == 496) {
                            ((BlockEntitySmelteryCasting) this.base).fluid_handler.setName("ingot_casting");
                            if (((BlockEntitySmelteryCasting) this.base).getMain() != null) {
                                BlockEntitySmelteryController controller = (BlockEntitySmelteryController) ((BlockEntitySmelteryCasting) this.base).getMain();
                                fluid_handler.load();
                                ((BlockEntitySmelteryCasting) this.base).fluid_handler.getOutput(controller.getFirstTank());
                                ;
                            }
                        } else if (damage == 497) {
                            ((BlockEntitySmelteryCasting) this.base).fluid_handler.setName("gear_casting");
                            BlockEntitySmelteryController controller = (BlockEntitySmelteryController) ((BlockEntitySmelteryCasting) this.base).getMain();
                            fluid_handler.load();
                            ((BlockEntitySmelteryCasting) this.base).fluid_handler.getOutput(controller.getFirstTank());
                            ;
                        }
                    }
                }
                return content;
            }
        };


        Recipes.recipes.getRecipeFluid().addInitRecipes(this);

    }

    @Override
    public void readContainerPacket(final CustomPacketBuffer customPacketBuffer) {
        super.readContainerPacket(customPacketBuffer);
        boolean hasMain = customPacketBuffer.readBoolean();
        if (hasMain && this.getMain() != null) {
            BlockEntitySmelteryController controller = (BlockEntitySmelteryController) this.getMain();
            controller.readContainerPacket(customPacketBuffer);
        }
    }

    @Override
    public CustomPacketBuffer writeContainerPacket() {
        CustomPacketBuffer customPacketBuffer = super.writeContainerPacket();
        customPacketBuffer.writeBoolean(this.getMain() != null);
        if (this.getMain() != null) {
            BlockEntitySmelteryController controller = (BlockEntitySmelteryController) this.getMain();
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
    public ContainerMenuSmelteryCasting getGuiContainer(final Player var1) {
        return new ContainerMenuSmelteryCasting(this, var1);
    }

    @Override
    @OnlyIn(Dist.CLIENT)
    public ScreenIndustrialUpgrade<ContainerMenuBase<? extends CustomWorldContainer>> getGui(Player var1, ContainerMenuBase<? extends CustomWorldContainer> menu) {

        return new ScreenSmelteryCasting((ContainerMenuSmelteryCasting) menu);
    }

    public void onLoaded() {
        super.onLoaded();
        if (!getLevel().isClientSide) {
            this.fluid_handler.setName("empty");
            if (!inputSlotB.get(0).isEmpty()) {
                int damage = ((ItemCraftingElements<?>) inputSlotB.get(0).getItem()).getElement().getId();
                if (damage == 496) {
                    this.fluid_handler.setName("ingot_casting");
                    if (this.getMain() != null) {
                        BlockEntitySmelteryController controller = (BlockEntitySmelteryController) this.getMain();
                        fluid_handler.load();
                        this.fluid_handler.getOutput(controller.getFirstTank());

                    }
                } else if (damage == 497) {
                    this.fluid_handler.setName("gear_casting");
                    if (this.getMain() != null) {
                        BlockEntitySmelteryController controller = (BlockEntitySmelteryController) this.getMain();
                        fluid_handler.load();
                        this.fluid_handler.getOutput(controller.getFirstTank());

                    }
                } else {
                    fluid_handler.load();
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
    public MultiBlockEntity getTeBlock() {
        return BlockSmelteryEntity.smeltery_casting;
    }

    @Override
    public BlockTileEntity getBlock() {
        return IUItem.smeltery.getBlock(getTeBlock());
    }

    @Override
    public void init() {
        Recipes.recipes.getRecipeFluid().addRecipe("ingot_casting", new BaseFluidMachineRecipe(new InputFluid(
                new FluidStack(FluidName.fluidiron.getInstance().get(), 144)), new RecipeOutput(
                null,
                new ItemStack(Items.IRON_INGOT)
        )));
        Recipes.recipes.getRecipeFluid().addRecipe("ingot_casting", new BaseFluidMachineRecipe(new InputFluid(
                new FluidStack(FluidName.fluidgold.getInstance().get(), 144)), new RecipeOutput(
                null,
                new ItemStack(Items.GOLD_INGOT)
        )));
        Recipes.recipes.getRecipeFluid().addRecipe("ingot_casting", new BaseFluidMachineRecipe(new InputFluid(
                new FluidStack(FluidName.fluidaluminium.getInstance().get(), 144)), new RecipeOutput(
                null,
                new ItemStack(IUItem.iuingot.getStack(1))
        )));
        Recipes.recipes.getRecipeFluid().addRecipe("ingot_casting", new BaseFluidMachineRecipe(new InputFluid(
                new FluidStack(FluidName.fluidtin.getInstance().get(), 144)), new RecipeOutput(
                null,
                new ItemStack(IUItem.iuingot.getStack(24))
        )));
        Recipes.recipes.getRecipeFluid().addRecipe("ingot_casting", new BaseFluidMachineRecipe(new InputFluid(
                new FluidStack(FluidName.fluidcopper.getInstance().get(), 144)), new RecipeOutput(
                null,
                new ItemStack(Items.COPPER_INGOT)
        )));
        Recipes.recipes.getRecipeFluid().addRecipe("ingot_casting", new BaseFluidMachineRecipe(new InputFluid(
                new FluidStack(FluidName.fluidnichrome.getInstance().get(), 144)), new RecipeOutput(
                null,
                new ItemStack(IUItem.alloysingot.getStack(4))
        )));
        Recipes.recipes.getRecipeFluid().addRecipe("ingot_casting", new BaseFluidMachineRecipe(new InputFluid(
                new FluidStack(FluidName.fluidtemperedglass.getInstance().get(), 144)), new RecipeOutput(
                null,
                new ItemStack(IUItem.blockResource.getItemStack(13))
        )));
        Recipes.recipes.getRecipeFluid().addRecipe("ingot_casting", new BaseFluidMachineRecipe(new InputFluid(
                new FluidStack(FluidName.fluidbronze.getInstance().get(), 144)), new RecipeOutput(
                null,
                new ItemStack(IUItem.iuingot.getStack(20))
        )));
        Recipes.recipes.getRecipeFluid().addRecipe("ingot_casting", new BaseFluidMachineRecipe(new InputFluid(
                new FluidStack(FluidName.fluidsteel.getInstance().get(), 144)), new RecipeOutput(
                null,
                new ItemStack(IUItem.crafting_elements.getStack(502))
        )));
        Recipes.recipes.getRecipeFluid().addRecipe("ingot_casting", new BaseFluidMachineRecipe(new InputFluid(
                new FluidStack(FluidName.fluidobsidian.getInstance().get(), 144 * 9)), new RecipeOutput(
                null,
                new ItemStack(Blocks.OBSIDIAN)
        )));
        Recipes.recipes.getRecipeFluid().addRecipe("ingot_casting", new BaseFluidMachineRecipe(new InputFluid(
                new FluidStack(FluidName.fluidarsenicum.getInstance().get(), 144)), new RecipeOutput(
                null,
                new ItemStack(IUItem.iuingot.getStack(28))
        )));
        Recipes.recipes.getRecipeFluid().addRecipe("ingot_casting", new BaseFluidMachineRecipe(new InputFluid(
                new FluidStack(FluidName.fluidtitanium.getInstance().get(), 144)), new RecipeOutput(
                null,
                new ItemStack(IUItem.iuingot.getStack(10))
        )));
        Recipes.recipes.getRecipeFluid().addRecipe("ingot_casting", new BaseFluidMachineRecipe(new InputFluid(
                new FluidStack(FluidName.fluidinvar.getInstance().get(), 144)), new RecipeOutput(
                null,
                new ItemStack(IUItem.iuingot.getStack(4))
        )));
        Recipes.recipes.getRecipeFluid().addRecipe("ingot_casting", new BaseFluidMachineRecipe(new InputFluid(
                new FluidStack(FluidName.fluidelectrum.getInstance().get(), 144)), new RecipeOutput(
                null,
                new ItemStack(IUItem.iuingot.getStack(13))
        )));
        Recipes.recipes.getRecipeFluid().addRecipe("ingot_casting", new BaseFluidMachineRecipe(new InputFluid(
                new FluidStack(FluidName.fluidsilver.getInstance().get(), 144)), new RecipeOutput(
                null,
                new ItemStack(IUItem.iuingot.getStack(14))
        )));
        Recipes.recipes.getRecipeFluid().addRecipe("ingot_casting", new BaseFluidMachineRecipe(new InputFluid(
                new FluidStack(FluidName.fluidaluminiumbronze.getInstance().get(), 144)), new RecipeOutput(
                null,
                new ItemStack(IUItem.alloysingot.getStack(0))
        )));

        Recipes.recipes.getRecipeFluid().addRecipe("ingot_casting", new BaseFluidMachineRecipe(new InputFluid(
                new FluidStack(FluidName.fluidferromanganese.getInstance().get(), 144)), new RecipeOutput(
                null,
                new ItemStack(IUItem.alloysingot.getStack(9))
        )));
        Recipes.recipes.getRecipeFluid().addRecipe("ingot_casting", new BaseFluidMachineRecipe(new InputFluid(
                new FluidStack(FluidName.fluidduralumin.getInstance().get(), 144)), new RecipeOutput(
                null,
                new ItemStack(IUItem.alloysingot.getStack(8))
        )));
        Recipes.recipes.getRecipeFluid().addRecipe("ingot_casting", new BaseFluidMachineRecipe(new InputFluid(
                new FluidStack(FluidName.fluidarsenicum_gallium.getInstance().get(), 144)), new RecipeOutput(
                null,
                new ItemStack(IUItem.alloysingot.getStack(31))
        )));
        Recipes.recipes.getRecipeFluid().addRecipe("ingot_casting", new BaseFluidMachineRecipe(new InputFluid(
                new FluidStack(FluidName.fluidtitaniumsteel.getInstance().get(), 144)), new RecipeOutput(
                null,
                new ItemStack(IUItem.crafting_elements.getStack(503))
        )));


        Recipes.recipes.getRecipeFluid().addRecipe("gear_casting", new BaseFluidMachineRecipe(new InputFluid(
                new FluidStack(FluidName.fluidinvar.getInstance().get(), 144 * 4)), new RecipeOutput(
                null,
                new ItemStack(IUItem.gear.getStack(4))
        )));
        Recipes.recipes.getRecipeFluid().addRecipe("gear_casting", new BaseFluidMachineRecipe(new InputFluid(
                new FluidStack(FluidName.fluidmagnesium.getInstance().get(), 144 * 4)), new RecipeOutput(
                null,
                new ItemStack(IUItem.gear.getStack(7))
        )));
        Recipes.recipes.getRecipeFluid().addRecipe("gear_casting", new BaseFluidMachineRecipe(new InputFluid(
                new FluidStack(FluidName.fluidelectrum.getInstance().get(), 144 * 4)), new RecipeOutput(
                null,
                new ItemStack(IUItem.gear.getStack(13))
        )));
        Recipes.recipes.getRecipeFluid().addRecipe("gear_casting", new BaseFluidMachineRecipe(new InputFluid(
                new FluidStack(FluidName.fluidtitanium.getInstance().get(), 144 * 4)), new RecipeOutput(
                null,
                new ItemStack(IUItem.gear.getStack(10))
        )));
        Recipes.recipes.getRecipeFluid().addRecipe("gear_casting", new BaseFluidMachineRecipe(new InputFluid(
                new FluidStack(FluidName.fluidferromanganese.getInstance().get(), 144 * 4)), new RecipeOutput(
                null,
                new ItemStack(IUItem.alloygear.getStack(9))
        )));
        Recipes.recipes.getRecipeFluid().addRecipe("gear_casting", new BaseFluidMachineRecipe(new InputFluid(
                new FluidStack(FluidName.fluidaluminiumbronze.getInstance().get(), 144 * 4)), new RecipeOutput(
                null,
                new ItemStack(IUItem.alloygear.getStack(0))
        )));
        Recipes.recipes.getRecipeFluid().addRecipe("gear_casting", new BaseFluidMachineRecipe(new InputFluid(
                new FluidStack(FluidName.fluidduralumin.getInstance().get(), 144 * 4)), new RecipeOutput(
                null,
                new ItemStack(IUItem.alloygear.getStack(8))
        )));
    }

}
