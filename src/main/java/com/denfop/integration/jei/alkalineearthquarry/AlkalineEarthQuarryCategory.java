package com.denfop.integration.jei.alkalineearthquarry;

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
import java.util.Collections;
import java.util.List;

public class AlkalineEarthQuarryCategory extends GuiIU implements IRecipeCategory<AlkalineEarthQuarryHandler> {

    private final IDrawableStatic bg;
    private final ContainerLaserPolisher container1;
    private final GuiComponent progress_bar;
    JeiInform<AlkalineEarthQuarryCategory, AlkalineEarthQuarryHandler> jeiInform;
    private int progress = 0;
    private int energy = 0;

    public AlkalineEarthQuarryCategory(
            final IGuiHelper guiHelper, JeiInform<AlkalineEarthQuarryCategory, AlkalineEarthQuarryHandler> jeiInform
    ) {
        super(((TileEntityLaserPolisher) BlockBaseMachine3.laser_polisher.getDummyTe()).getGuiContainer1(Minecraft.getInstance().player));
        bg = guiHelper.createDrawable(ResourceLocation.tryBuild(Constants.MOD_ID, "textures/gui/guimachine" +
                        ".png"), 3, 3, 140,
                77
        );
        this.jeiInform = jeiInform;
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
        this.title = net.minecraft.network.chat.Component.literal(getTitles());
    }

    @Override
    public RecipeType<AlkalineEarthQuarryHandler> getRecipeType() {
        return jeiInform.recipeType;
    }

    @Nonnull
    @Override
    public String getTitles() {
        return Localization.translate(JEICompat.getBlockStack(BlockBaseMachine3.alkalineearthquarry).getDescriptionId());
    }


    @SuppressWarnings("removal")
    @Nonnull
    @Override
    public IDrawable getBackground() {
        return bg;
    }

    @Override
    public IDrawable getIcon() {
        return null;
    }

    @Override
    public void setRecipe(IRecipeLayoutBuilder builder, AlkalineEarthQuarryHandler recipes, IFocusGroup focuses) {

        final List<SlotInvSlot> slots1 = container1.findClassSlots(InvSlotRecipes.class);
        final List<ItemStack> inputs = Collections.singletonList(recipes.getInput());
        int i = 0;
        for (; i < inputs.size(); i++) {
            builder.addSlot(RecipeIngredientRole.INPUT, slots1.get(i).getJeiX(), slots1.get(i).getJeiY()).addItemStack(inputs.get(i));


        }

        final SlotInvSlot outputSlot = container1.findClassSlot(InvSlotOutput.class);
        builder.addSlot(RecipeIngredientRole.OUTPUT, outputSlot.getJeiX(), outputSlot.getJeiY()).addItemStack(recipes.getOutput());
        builder.addSlot(RecipeIngredientRole.INPUT, slots1.get(0).getJeiX() + 15, slots1.get(0).getJeiY() + 15).addItemStack(recipes.getMesh());
    }

    @Override
    public void draw(AlkalineEarthQuarryHandler recipe, IRecipeSlotsView recipeSlotsView, GuiGraphics stack, double mouseX, double mouseY) {
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
        bindTexture(getTexture());

        this.drawSplitString(stack, Localization.translate("earth_quarry.jei1"), 5, 3,
                140 - 5, 4210752
        );
        this.drawSplitString(stack, recipe.chance + "%", 80, 24,
                140 - 5, 4210752
        );
    }


    protected ResourceLocation getTexture() {
        return ResourceLocation.tryBuild(Constants.MOD_ID, "textures/gui/guimachine.png");
    }


}
