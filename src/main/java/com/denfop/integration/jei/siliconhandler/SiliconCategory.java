package com.denfop.integration.jei.siliconhandler;

import com.denfop.Constants;
import com.denfop.IUItem;
import com.denfop.Localization;
import com.denfop.api.gui.Component;
import com.denfop.api.gui.EnumTypeComponent;
import com.denfop.api.gui.GuiComponent;
import com.denfop.api.recipe.InvSlotOutput;
import com.denfop.api.recipe.InvSlotRecipes;
import com.denfop.blocks.mechanism.BlockBaseMachine3;
import com.denfop.componets.ComponentRenderInventory;
import com.denfop.componets.EnumTypeComponentSlot;
import com.denfop.container.ContainerSiliconCrystalHandler;
import com.denfop.container.SlotInvSlot;
import com.denfop.gui.GuiIU;
import com.denfop.integration.jei.IRecipeCategory;
import com.denfop.integration.jei.JEICompat;
import com.denfop.integration.jei.JeiInform;
import com.denfop.recipes.ItemStackHelper;
import com.denfop.tiles.mechanism.TileEntitySiliconCrystalHandler;
import com.mojang.blaze3d.vertex.PoseStack;
import mezz.jei.api.gui.builder.IRecipeLayoutBuilder;
import mezz.jei.api.gui.drawable.IDrawable;
import mezz.jei.api.gui.drawable.IDrawableStatic;
import mezz.jei.api.gui.ingredient.IRecipeSlotsView;
import mezz.jei.api.helpers.IGuiHelper;
import mezz.jei.api.recipe.IFocusGroup;
import mezz.jei.api.recipe.RecipeIngredientRole;
import mezz.jei.api.recipe.RecipeType;
import net.minecraft.client.Minecraft;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ItemStack;

import javax.annotation.Nonnull;
import java.util.List;

public class SiliconCategory extends GuiIU implements IRecipeCategory<SiliconHandler> {

    private final IDrawableStatic bg;
    private final ContainerSiliconCrystalHandler container1;
    private final GuiComponent progress_bar;
    private int progress = 0;
    private int energy = 0;
    JeiInform jeiInform;
    public SiliconCategory(
            IGuiHelper guiHelper, JeiInform jeiInform
    ) {
        super(((TileEntitySiliconCrystalHandler) BlockBaseMachine3.silicon_crystal_handler.getDummyTe()).getGuiContainer(Minecraft.getInstance().player));
        bg = guiHelper.createDrawable(new ResourceLocation(Constants.MOD_ID, "textures/gui/guimachine" +
                        ".png"), 3, 3, 140,
                77
        );
        this.jeiInform = jeiInform;
        this.title = net.minecraft.network.chat.Component.literal(getTitles());
        this.componentList.clear();
        this.slots = new GuiComponent(this, 3, 3, getComponent(),
                new Component<>(new ComponentRenderInventory(EnumTypeComponentSlot.SLOTS_UPGRADE_JEI))
        );
        this.container1 = (ContainerSiliconCrystalHandler) this.getContainer();
        this.componentList.add(slots);
        progress_bar = new GuiComponent(this, 70, 35, EnumTypeComponent.PROCESS,
                new Component<>(this.container1.base.timer)
        );
        this.componentList.add(progress_bar);
    }

    @Override
    public RecipeType<SiliconHandler> getRecipeType() {
        return jeiInform.recipeType;
    }

    @Nonnull
    @Override
    public String getTitles() {
        return Localization.translate(JEICompat.getBlockStack(BlockBaseMachine3.silicon_crystal_handler).getDescriptionId());
    }



    @Nonnull
    @Override
    public IDrawable getBackground() {
        return bg;
    }

    @Override
    public void draw(SiliconHandler recipe, IRecipeSlotsView recipeSlotsView, PoseStack stack, double mouseX, double mouseY) {
        progress++;
        if (this.energy < 100) {
            energy++;
        }
        double energylevel = energy / 100D;
        double xScale = progress / 100D;
        if (xScale >= 1) {
            progress = 0;
        }
        this.slots.drawBackground( stack,0, 0);

        progress_bar.renderBar( stack,0, 0, xScale);
    }

    @Override
    public void setRecipe(IRecipeLayoutBuilder builder, SiliconHandler recipe, IFocusGroup focuses) {

        final List<SlotInvSlot> slots1 = container1.findClassSlots(InvSlotRecipes.class);
        final List<ItemStack> inputs = recipe.getInputs1();
        int i = 0;
        for (; i < inputs.size(); i++) {
            builder.addSlot(RecipeIngredientRole.INPUT,slots1.get(i).getJeiX(), slots1.get(i).getJeiY()).addItemStack(inputs.get(i));


        }

        final SlotInvSlot outputSlot = container1.findClassSlot(InvSlotOutput.class);
        builder.addSlot(RecipeIngredientRole.OUTPUT,outputSlot.getJeiX(), outputSlot.getJeiY()).addItemStack(recipe.getOutput());
        builder.addSlot(RecipeIngredientRole.INPUT,30,43).addItemStack(ItemStackHelper.fromData(IUItem.iudust, 1, 21));

    }



    protected ResourceLocation getTexture() {
        return new ResourceLocation(Constants.MOD_ID, "textures/gui/guimachine.png");
    }


}
