package com.denfop.integration.jei.rotorrods;

import com.denfop.Constants;
import com.denfop.IUItem;
import com.denfop.Localization;
import com.denfop.api.gui.Component;
import com.denfop.api.gui.EnumTypeComponent;
import com.denfop.api.gui.GuiComponent;
import com.denfop.api.recipe.InvSlotOutput;
import com.denfop.api.recipe.InvSlotRecipes;
import com.denfop.blocks.mechanism.BlockBaseMachine3;
import com.denfop.componets.ComponentProgress;
import com.denfop.componets.ComponentRenderInventory;
import com.denfop.componets.EnumTypeComponentSlot;
import com.denfop.container.ContainerRodManufacturer;
import com.denfop.container.SlotInvSlot;
import com.denfop.gui.GuiIU;
import com.denfop.integration.jei.IRecipeCategory;
import com.denfop.integration.jei.JeiInform;
import com.denfop.recipes.ItemStackHelper;
import com.denfop.tiles.mechanism.TileEntityRodManufacturer;
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

import javax.annotation.Nonnull;
import java.util.List;

public class RotorsRodCategory extends GuiIU implements IRecipeCategory<RotorsRodHandler> {

    private final IDrawableStatic bg;
    private final ContainerRodManufacturer container1;
    private final GuiComponent progress_bar;
    private int progress = 0;
    JeiInform jeiInform;
    public RotorsRodCategory(
            IGuiHelper guiHelper, JeiInform jeiInform
    ) {
        super(((TileEntityRodManufacturer) BlockBaseMachine3.rods_manufacturer.getDummyTe()).getGuiContainer(Minecraft.getInstance().player));
        bg = guiHelper.createDrawable(new ResourceLocation(Constants.MOD_ID, "textures/gui/guimachine" +
                        ".png"), 3, 3, 140,
                77
        );
        this.jeiInform = jeiInform;
        this.title = net.minecraft.network.chat.Component.literal(getTitles());
        this.componentList.clear();
        this.slots = new GuiComponent(this, 3, 3, getComponent(),
                new Component<>(new ComponentRenderInventory(EnumTypeComponentSlot.SLOTS__JEI))
        );
        this.container1 = (ContainerRodManufacturer) this.getContainer();
        this.componentList.add(slots);
        progress_bar = new GuiComponent(this, 70, 36, EnumTypeComponent.PROCESS,
                new Component<>(new ComponentProgress(null, 1, (short) container1.base.defaultOperationLength))
        );
        this.componentList.add(progress_bar);
    }


    @Nonnull
    @Override
    public String getTitles() {
        return Localization.translate(ItemStackHelper.fromData(IUItem.basemachine2, 1, 21).getDescriptionId());
    }


    @Nonnull
    @Override
    public IDrawable getBackground() {
        return bg;
    }

    @Override
    public void draw(RotorsRodHandler recipe, IRecipeSlotsView recipeSlotsView, PoseStack stack, double mouseX, double mouseY) {
        progress++;

        double xScale = progress / 100D;
        if (xScale >= 1) {
            progress = 0;
        }
        this.slots.drawBackground(stack,0, 0);

        progress_bar.renderBar(stack,10, 0, xScale);
    }

    @Override
    public void setRecipe(IRecipeLayoutBuilder builder, RotorsRodHandler recipe, IFocusGroup focuses) {
        final List<SlotInvSlot> slots1 = container1.findClassSlots(InvSlotRecipes.class);
        int i = 0;
        for (; i < recipe.getInputs().length; i++) {
            builder.addSlot(RecipeIngredientRole.INPUT,slots1.get(i).getJeiX(), slots1.get(i).getJeiY()).addItemStack(recipe.getInputs()[i]);


        }

        final SlotInvSlot outputSlot = container1.findClassSlot(InvSlotOutput.class);
        builder.addSlot(RecipeIngredientRole.OUTPUT,outputSlot.getJeiX(), outputSlot.getJeiY()).addItemStack(recipe.getOutput());

    }

    @Override
    public RecipeType<RotorsRodHandler> getRecipeType() {
        return jeiInform.recipeType;
    }

    protected ResourceLocation getTexture() {
        return new ResourceLocation(Constants.MOD_ID, "textures/gui/guirotorrods.png");
    }


}
