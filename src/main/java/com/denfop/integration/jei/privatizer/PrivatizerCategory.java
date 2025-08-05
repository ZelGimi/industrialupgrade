package com.denfop.integration.jei.privatizer;

import com.denfop.Constants;
import com.denfop.IUItem;
import com.denfop.Localization;
import com.denfop.api.gui.Component;
import com.denfop.api.gui.GuiComponent;
import com.denfop.blocks.mechanism.BlockBaseMachine3;
import com.denfop.componets.ComponentRenderInventory;
import com.denfop.componets.EnumTypeComponentSlot;
import com.denfop.container.ContainerPrivatizer;
import com.denfop.container.SlotInvSlot;
import com.denfop.gui.GuiIU;
import com.denfop.integration.jei.IRecipeCategory;
import com.denfop.integration.jei.JeiInform;
import com.denfop.recipes.ItemStackHelper;
import com.denfop.tiles.mechanism.TilePrivatizer;
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
import net.minecraft.world.item.ItemStack;

import javax.annotation.Nonnull;
import java.util.List;

public class PrivatizerCategory extends GuiIU implements IRecipeCategory<PrivatizerHandler> {

    private final IDrawableStatic bg;
    private final ContainerPrivatizer container1;
    private final JeiInform jeiInform;

    public PrivatizerCategory(
            IGuiHelper guiHelper, JeiInform jeiInform
    ) {
        super(((TilePrivatizer) BlockBaseMachine3.privatizer.getDummyTe()).getGuiContainer(Minecraft.getInstance().player));
        this.jeiInform = jeiInform;
        this.title = net.minecraft.network.chat.Component.literal(getTitles());
        bg = guiHelper.createDrawable(ResourceLocation.tryBuild(Constants.MOD_ID, "textures/gui/guimachine.png"), 3, 3, 169,
                75
        );
        this.componentList.clear();
        this.slots = new GuiComponent(this, 3, 3, getComponent(),
                new Component<>(new ComponentRenderInventory(EnumTypeComponentSlot.SLOTS_UPGRADE_JEI))
        );
        this.container1 = (ContainerPrivatizer) this.getContainer();
        this.componentList.add(slots);
    }

    @Override
    public RecipeType<PrivatizerHandler> getRecipeType() {
        return jeiInform.recipeType;
    }

    @Nonnull
    @Override
    public String getTitles() {
        return Localization.translate(ItemStackHelper.fromData(IUItem.basemachine2, 1, 2).getDescriptionId());
    }


    @Nonnull
    @Override
    public IDrawable getBackground() {
        return bg;
    }

    @Override
    public void draw(PrivatizerHandler recipe, IRecipeSlotsView recipeSlotsView, GuiGraphics stack, double mouseX, double mouseY) {
        this.slots.drawBackground(stack, 0, 0);
    }

    @Override
    public void setRecipe(IRecipeLayoutBuilder builder, PrivatizerHandler recipe, IFocusGroup focuses) {
        final List<SlotInvSlot> slots1 = container1.getSlots();
        builder.addSlot(RecipeIngredientRole.INPUT, slots1.get(0).getJeiX(), slots1.get(0).getJeiY()).addItemStack(new ItemStack(IUItem.module7.getStack(0), 1));

        builder.addSlot(RecipeIngredientRole.INPUT, slots1.get(1).getJeiX(), slots1.get(1).getJeiY()).addItemStack(recipe.getOutput());

    }


    protected ResourceLocation getTexture() {
        return ResourceLocation.tryBuild(Constants.MOD_ID, "textures/gui/guiprivatizer_jei.png");
    }


}
