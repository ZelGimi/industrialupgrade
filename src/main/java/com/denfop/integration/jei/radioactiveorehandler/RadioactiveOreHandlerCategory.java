package com.denfop.integration.jei.radioactiveorehandler;

import com.denfop.Constants;
import com.denfop.Localization;
import com.denfop.api.gui.Component;
import com.denfop.api.gui.EnumTypeComponent;
import com.denfop.api.gui.GuiComponent;
import com.denfop.api.recipe.InvSlotOutput;
import com.denfop.api.recipe.InvSlotRecipes;
import com.denfop.blocks.mechanism.BlockBaseMachine3;
import com.denfop.componets.ComponentRenderInventory;
import com.denfop.componets.EnumTypeComponentSlot;
import com.denfop.container.ContainerLaserPolisher;
import com.denfop.container.SlotInvSlot;
import com.denfop.gui.GuiIU;
import com.denfop.integration.jei.IRecipeCategory;
import com.denfop.integration.jei.JEICompat;
import com.denfop.integration.jei.JeiInform;
import com.denfop.tiles.mechanism.TileEntityLaserPolisher;
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

public class RadioactiveOreHandlerCategory extends GuiIU implements IRecipeCategory<RadioactiveOreHandlerHandler> {

    private final IDrawableStatic bg;
    private final ContainerLaserPolisher container1;
    private final GuiComponent progress_bar;
    private int progress = 0;
    private int energy = 0;
    JeiInform jeiInform;

    public RadioactiveOreHandlerCategory(
            IGuiHelper guiHelper, JeiInform jeiInform
    ) {
        super(((TileEntityLaserPolisher) BlockBaseMachine3.laser_polisher.getDummyTe()).getGuiContainer1(Minecraft.getInstance().player));
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
        this.container1 = (ContainerLaserPolisher) this.getContainer();
        this.componentList.add(slots);
        progress_bar = new GuiComponent(this, 70, 35, EnumTypeComponent.PROCESS,
                new Component<>(this.container1.base.componentProgress)
        );
        this.componentList.add(progress_bar);
    }

    @Override
    public RecipeType<RadioactiveOreHandlerHandler> getRecipeType() {
        return jeiInform.recipeType;
    }

    @Nonnull
    @Override
    public String getTitles() {
        return Localization.translate(JEICompat.getBlockStack(BlockBaseMachine3.radioactive_handler_ore).getDescriptionId());
    }



    @Nonnull
    @Override
    public IDrawable getBackground() {
        return bg;
    }

    @Override
    public void draw(RadioactiveOreHandlerHandler recipe, IRecipeSlotsView recipeSlotsView, PoseStack stack, double mouseX, double mouseY) {
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
        int y = 58;
        int x = 120;

        int chance = recipe.chance;

       drawSplitString(stack,"" + chance + "%", x, y, 160 - x, 4210752);

    }

    @Override
    public void setRecipe(IRecipeLayoutBuilder builder, RadioactiveOreHandlerHandler recipe, IFocusGroup focuses) {
        final List<SlotInvSlot> slots1 = container1.findClassSlots(InvSlotRecipes.class);
        final List<ItemStack> inputs = Collections.singletonList(recipe.getInput());
        int i = 0;
        for (; i < 1; i++) {
            builder.addSlot(RecipeIngredientRole.INPUT,slots1.get(i).getJeiX(), slots1.get(i).getJeiY()).addItemStack(inputs.get(i));

        }

        final SlotInvSlot outputSlot = container1.findClassSlot(InvSlotOutput.class);
        builder.addSlot(RecipeIngredientRole.OUTPUT, outputSlot.getJeiX(), outputSlot.getJeiY() + 20).addItemStack(recipe.getInput1());

         builder.addSlot(RecipeIngredientRole.OUTPUT, outputSlot.getJeiX(), outputSlot.getJeiY()).addItemStack(recipe.getOutput());
    }



    protected ResourceLocation getTexture() {
        return new ResourceLocation(Constants.MOD_ID, "textures/gui/guimachine.png");
    }


}
