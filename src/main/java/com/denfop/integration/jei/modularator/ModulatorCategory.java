package com.denfop.integration.jei.modularator;

import com.denfop.Constants;
import com.denfop.IUItem;
import com.denfop.api.widget.ScreenWidget;
import com.denfop.api.widget.WidgetDefault;
import com.denfop.blockentity.mechanism.BlockEntityModuleMachine;
import com.denfop.blocks.mechanism.BlockBaseMachineEntity;
import com.denfop.componets.ComponentRenderInventory;
import com.denfop.componets.EnumTypeComponentSlot;
import com.denfop.containermenu.ContainerMenuModuleMachine;
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

public class ModulatorCategory extends ScreenMain implements IRecipeCategory<ModulatorHandler> {

    private final IDrawableStatic bg;
    private final ContainerMenuModuleMachine container1;
    private final JeiInform jeiInform;

    public ModulatorCategory(
            IGuiHelper guiHelper, JeiInform jeiInform
    ) {
        super(((BlockEntityModuleMachine) BlockBaseMachineEntity.modulator.getDummyTe()).getGuiContainer(Minecraft.getInstance().player));

        bg = guiHelper.createDrawable(ResourceLocation.tryBuild(Constants.MOD_ID, "textures/gui/guimachine.png"), 3, 3, 174,
                91
        );
        this.jeiInform = jeiInform;
        this.title = net.minecraft.network.chat.Component.literal(getTitles());
        this.componentList.clear();
        this.slots = new ScreenWidget(this, 3, 3, getComponent(),
                new WidgetDefault<>(new ComponentRenderInventory(EnumTypeComponentSlot.SLOTS_UPGRADE_JEI))
        );
        this.container1 = (ContainerMenuModuleMachine) this.getContainer();
        this.componentList.add(slots);
    }

    @Override
    public RecipeType<ModulatorHandler> getRecipeType() {
        return jeiInform.recipeType;
    }

    @Nonnull
    @Override
    public String getTitles() {
        return Localization.translate(ItemStackHelper.fromData(IUItem.machines, 1, 9).getDescriptionId());
    }


    @Nonnull
    @Override
    public IDrawable getBackground() {
        return bg;
    }

    @Override
    public void draw(ModulatorHandler recipe, IRecipeSlotsView recipeSlotsView, GuiGraphics stack, double mouseX, double mouseY) {

        this.slots.drawBackground(stack, 0, 0);
    }

    @Override
    public void setRecipe(IRecipeLayoutBuilder builder, ModulatorHandler recipe, IFocusGroup focuses) {

        final List<SlotInvSlot> slots1 = container1.getSlots();
        builder.addSlot(RecipeIngredientRole.INPUT, slots1.get(0).getJeiX(), slots1.get(0).getJeiY()).addItemStack(ItemStackHelper.fromData(IUItem.module9, 1, 13));
        builder.addSlot(RecipeIngredientRole.INPUT, slots1.get(1).getJeiX(), slots1.get(1).getJeiY()).addItemStack(recipe.getOutput());


    }


    protected ResourceLocation getTexture() {
        return ResourceLocation.tryBuild(Constants.MOD_ID, "textures/gui/guiquantumquerry.png");
    }


}
