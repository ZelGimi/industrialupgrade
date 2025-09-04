package com.denfop.integration.jei.genradiation;

import com.denfop.Constants;
import com.denfop.api.recipe.InventoryRecipes;
import com.denfop.api.widget.EnumTypeComponent;
import com.denfop.api.widget.ScreenWidget;
import com.denfop.api.widget.WidgetDefault;
import com.denfop.blockentity.mechanism.BlockEntityNuclearWasteRecycler;
import com.denfop.blocks.mechanism.BlockBaseMachine3Entity;
import com.denfop.componets.ComponentRenderInventory;
import com.denfop.componets.EnumTypeComponentSlot;
import com.denfop.containermenu.ContainerMenuNuclearWasteRecycler;
import com.denfop.containermenu.slot.SlotInvSlot;
import com.denfop.integration.jei.IRecipeCategory;
import com.denfop.integration.jei.JEICompat;
import com.denfop.integration.jei.JeiInform;
import com.denfop.screen.ScreenMain;
import com.denfop.utils.Localization;
import com.denfop.utils.ModUtils;
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
import java.util.Collections;
import java.util.List;

public class GenRadCategory extends ScreenMain implements IRecipeCategory<GenRadHandler> {


    private final ContainerMenuNuclearWasteRecycler container1;
    private final ScreenWidget progress_bar;
    private final IDrawableStatic bg;
    private final JeiInform jeiInform;
    private int progress;

    public GenRadCategory(
            IGuiHelper guiHelper, JeiInform jeiInform
    ) {
        super(((BlockEntityNuclearWasteRecycler) BlockBaseMachine3Entity.nuclear_waste_recycler.getDummyTe()).getGuiContainer(Minecraft.getInstance().player));
        this.jeiInform = jeiInform;
        this.title = net.minecraft.network.chat.Component.literal(getTitles());
        bg = guiHelper.createDrawable(new ResourceLocation(Constants.MOD_ID, "textures/gui/guimachine" +
                        ".png"), 5, 5, 140,
                75
        );
        this.componentList.clear();
        this.slots = new ScreenWidget(this, 3, 3, getComponent(),
                new WidgetDefault<>(new ComponentRenderInventory(EnumTypeComponentSlot.SLOTS__JEI))
        );
        this.container1 = (ContainerMenuNuclearWasteRecycler) this.getContainer();
        this.componentList.add(slots);
        progress_bar = new ScreenWidget(this, 70, 35, EnumTypeComponent.RADIATION_PROCESS,
                new WidgetDefault<>(this.container1.base.componentProgress)
        );
        this.componentList.add(progress_bar);
    }

    @Override
    public RecipeType<GenRadHandler> getRecipeType() {
        return jeiInform.recipeType;
    }

    @Nonnull
    @Override
    public String getTitles() {
        return Localization.translate(JEICompat.getBlockStack(BlockBaseMachine3Entity.nuclear_waste_recycler).getDescriptionId());
    }


    @Nonnull
    @Override
    public IDrawable getBackground() {
        return bg;
    }

    @Override
    public void draw(GenRadHandler recipe, IRecipeSlotsView recipeSlotsView, PoseStack stack, double mouseX, double mouseY) {

        progress++;
        double xScale = progress / 100D;
        if (xScale >= 1) {
            progress = 0;
        }
        this.slots.drawBackground(stack, 0, 0);

        progress_bar.renderBar(stack, 0, 0, xScale);
        drawSplitString(stack,
                ModUtils.getString(recipe.getEnergy()) + "☢",
                70,
                60,
                140 - 10,
                4210752
        );
        bindTexture(getTexture());
    }

    @Override
    public void setRecipe(IRecipeLayoutBuilder builder, GenRadHandler recipes, IFocusGroup focuses) {
        final List<SlotInvSlot> slots1 = container1.findClassSlots(InventoryRecipes.class);
        final List<ItemStack> inputs = Collections.singletonList(recipes.getOutput());
        int i = 0;
        for (; i < inputs.size(); i++) {
            builder.addSlot(RecipeIngredientRole.OUTPUT, slots1.get(i).getJeiX(), slots1.get(i).getJeiY()).addItemStack(inputs.get(i));


        }
    }


    protected ResourceLocation getTexture() {
        return new ResourceLocation(Constants.MOD_ID, "textures/gui/guimachine.png");
    }


}
