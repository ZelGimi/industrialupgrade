package com.denfop.integration.jei.rotorrods;

import com.denfop.Constants;
import com.denfop.IUItem;
import com.denfop.api.recipe.InventoryOutput;
import com.denfop.api.recipe.InventoryRecipes;
import com.denfop.api.widget.EnumTypeComponent;
import com.denfop.api.widget.ScreenWidget;
import com.denfop.api.widget.WidgetDefault;
import com.denfop.blockentity.mechanism.BlockEntityRodManufacturer;
import com.denfop.blocks.mechanism.BlockBaseMachine3Entity;
import com.denfop.componets.ComponentProgress;
import com.denfop.componets.ComponentRenderInventory;
import com.denfop.componets.EnumTypeComponentSlot;
import com.denfop.containermenu.ContainerMenuRodManufacturer;
import com.denfop.containermenu.SlotInvSlot;
import com.denfop.integration.jei.IRecipeCategory;
import com.denfop.integration.jei.JeiInform;
import com.denfop.recipes.ItemStackHelper;
import com.denfop.screen.ScreenMain;
import com.denfop.utils.Localization;
import mezz.jei.api.gui.builder.IRecipeLayoutBuilder;
import mezz.jei.api.gui.drawable.IDrawable;
import mezz.jei.api.gui.drawable.IDrawableStatic;
import mezz.jei.api.gui.ingredient.IRecipeSlotsView;
import mezz.jei.api.helpers.IGuiHelper;
import mezz.jei.api.recipe.IFocusGroup;
import mezz.jei.api.recipe.RecipeIngredientRole;
import mezz.jei.api.recipe.RecipeType;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.resources.ResourceLocation;

import javax.annotation.Nonnull;
import java.util.List;

public class RotorsRodCategory extends ScreenMain implements IRecipeCategory<RotorsRodHandler> {

    private final IDrawableStatic bg;
    private final ContainerMenuRodManufacturer container1;
    private final ScreenWidget progress_bar;
    JeiInform jeiInform;
    private int progress = 0;

    public RotorsRodCategory(
            IGuiHelper guiHelper, JeiInform jeiInform
    ) {
        super(((BlockEntityRodManufacturer) BlockBaseMachine3Entity.rods_manufacturer.getDummyTe()).getGuiContainer(Minecraft.getInstance().player));
        bg = guiHelper.createDrawable(new ResourceLocation(Constants.MOD_ID, "textures/gui/guimachine" +
                        ".png"), 3, 3, 140,
                77
        );
        this.jeiInform = jeiInform;
        this.title = net.minecraft.network.chat.Component.literal(getTitles());
        this.componentList.clear();
        this.slots = new ScreenWidget(this, 3, 3, getComponent(),
                new WidgetDefault<>(new ComponentRenderInventory(EnumTypeComponentSlot.SLOTS__JEI))
        );
        this.container1 = (ContainerMenuRodManufacturer) this.getContainer();
        this.componentList.add(slots);
        progress_bar = new ScreenWidget(this, 70, 36, EnumTypeComponent.PROCESS,
                new WidgetDefault<>(new ComponentProgress(null, 1, (short) container1.base.defaultOperationLength))
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
    public void draw(RotorsRodHandler recipe, IRecipeSlotsView recipeSlotsView, GuiGraphics stack, double mouseX, double mouseY) {
        progress++;

        double xScale = progress / 100D;
        if (xScale >= 1) {
            progress = 0;
        }
        this.slots.drawBackground(stack, 0, 0);

        progress_bar.renderBar(stack, 10, 0, xScale);
    }

    @Override
    public void setRecipe(IRecipeLayoutBuilder builder, RotorsRodHandler recipe, IFocusGroup focuses) {
        final List<SlotInvSlot> slots1 = container1.findClassSlots(InventoryRecipes.class);
        int i = 0;
        for (; i < recipe.getInputs().length; i++) {
            builder.addSlot(RecipeIngredientRole.INPUT, slots1.get(i).getJeiX(), slots1.get(i).getJeiY()).addItemStack(recipe.getInputs()[i]);


        }

        final SlotInvSlot outputSlot = container1.findClassSlot(InventoryOutput.class);
        builder.addSlot(RecipeIngredientRole.OUTPUT, outputSlot.getJeiX(), outputSlot.getJeiY()).addItemStack(recipe.getOutput());

    }

    @Override
    public RecipeType<RotorsRodHandler> getRecipeType() {
        return jeiInform.recipeType;
    }

    protected ResourceLocation getTexture() {
        return new ResourceLocation(Constants.MOD_ID, "textures/gui/guirotorrods.png");
    }


}
