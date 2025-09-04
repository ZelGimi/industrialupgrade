package com.denfop.integration.jei.siliconhandler;

import com.denfop.Constants;
import com.denfop.IUItem;
import com.denfop.api.recipe.InventoryOutput;
import com.denfop.api.recipe.InventoryRecipes;
import com.denfop.api.widget.EnumTypeComponent;
import com.denfop.api.widget.ScreenWidget;
import com.denfop.api.widget.WidgetDefault;
import com.denfop.blockentity.mechanism.BlockEntitySiliconCrystalHandler;
import com.denfop.blocks.mechanism.BlockBaseMachine3Entity;
import com.denfop.componets.ComponentRenderInventory;
import com.denfop.componets.EnumTypeComponentSlot;
import com.denfop.containermenu.ContainerMenuSiliconCrystalHandler;
import com.denfop.containermenu.SlotInvSlot;
import com.denfop.integration.jei.IRecipeCategory;
import com.denfop.integration.jei.JEICompat;
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
import net.minecraft.world.item.ItemStack;

import javax.annotation.Nonnull;
import java.util.List;

public class SiliconCategory extends ScreenMain implements IRecipeCategory<SiliconHandler> {

    private final IDrawableStatic bg;
    private final ContainerMenuSiliconCrystalHandler container1;
    private final ScreenWidget progress_bar;
    JeiInform jeiInform;
    private int progress = 0;
    private int energy = 0;

    public SiliconCategory(
            IGuiHelper guiHelper, JeiInform jeiInform
    ) {
        super(((BlockEntitySiliconCrystalHandler) BlockBaseMachine3Entity.silicon_crystal_handler.getDummyTe()).getGuiContainer(Minecraft.getInstance().player));
        bg = guiHelper.createDrawable(new ResourceLocation(Constants.MOD_ID, "textures/gui/guimachine" +
                        ".png"), 3, 3, 140,
                77
        );
        this.jeiInform = jeiInform;
        this.title = net.minecraft.network.chat.Component.literal(getTitles());
        this.componentList.clear();
        this.slots = new ScreenWidget(this, 3, 3, getComponent(),
                new WidgetDefault<>(new ComponentRenderInventory(EnumTypeComponentSlot.SLOTS_UPGRADE_JEI))
        );
        this.container1 = (ContainerMenuSiliconCrystalHandler) this.getContainer();
        this.componentList.add(slots);
        progress_bar = new ScreenWidget(this, 70, 35, EnumTypeComponent.PROCESS,
                new WidgetDefault<>(this.container1.base.timer)
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
        return Localization.translate(JEICompat.getBlockStack(BlockBaseMachine3Entity.silicon_crystal_handler).getDescriptionId());
    }


    @Nonnull
    @Override
    public IDrawable getBackground() {
        return bg;
    }

    @Override
    public void draw(SiliconHandler recipe, IRecipeSlotsView recipeSlotsView, GuiGraphics stack, double mouseX, double mouseY) {
        progress++;
        if (this.energy < 100) {
            energy++;
        }
        double energylevel = energy / 100D;
        double xScale = progress / 100D;
        if (xScale >= 1) {
            progress = 0;
        }
        this.slots.drawBackground(stack, 0, 0);

        progress_bar.renderBar(stack, 0, 0, xScale);
    }

    @Override
    public void setRecipe(IRecipeLayoutBuilder builder, SiliconHandler recipe, IFocusGroup focuses) {

        final List<SlotInvSlot> slots1 = container1.findClassSlots(InventoryRecipes.class);
        final List<ItemStack> inputs = recipe.getInputs1();
        int i = 0;
        for (; i < inputs.size(); i++) {
            builder.addSlot(RecipeIngredientRole.INPUT, slots1.get(i).getJeiX(), slots1.get(i).getJeiY()).addItemStack(inputs.get(i));


        }

        final SlotInvSlot outputSlot = container1.findClassSlot(InventoryOutput.class);
        builder.addSlot(RecipeIngredientRole.OUTPUT, outputSlot.getJeiX(), outputSlot.getJeiY()).addItemStack(recipe.getOutput());
        builder.addSlot(RecipeIngredientRole.INPUT, 30, 43).addItemStack(ItemStackHelper.fromData(IUItem.iudust, 1, 21));

    }


    protected ResourceLocation getTexture() {
        return new ResourceLocation(Constants.MOD_ID, "textures/gui/guimachine.png");
    }


}
