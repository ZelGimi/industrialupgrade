package com.denfop.integration.jei.welding;

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
import com.denfop.container.ContainerEnchanterBooks;
import com.denfop.container.SlotInvSlot;
import com.denfop.gui.GuiIU;
import com.denfop.integration.jei.IRecipeCategory;
import com.denfop.integration.jei.JEICompat;
import com.denfop.integration.jei.JeiInform;
import com.denfop.tiles.mechanism.TileEntityEnchanterBooks;
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
import java.util.Arrays;
import java.util.List;

public class WeldingCategory extends GuiIU implements IRecipeCategory<WeldingHandler> {

    private final IDrawableStatic bg;
    private final ContainerEnchanterBooks container1;
    private final GuiComponent progress_bar;
    private int progress = 0;
    private int energy = 0;
    JeiInform jeiInform;
    public WeldingCategory(
            IGuiHelper guiHelper, JeiInform jeiInform
    ) {
        super(((TileEntityEnchanterBooks) BlockBaseMachine3.enchanter_books.getDummyTe()).getGuiContainer(Minecraft.getInstance().player));

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
        this.container1 = (ContainerEnchanterBooks) this.getContainer();
        this.componentList.add(slots);
        progress_bar = new GuiComponent(this, 85, 35, EnumTypeComponent.PROCESS,
                new Component<>(this.container1.base.componentProgress)
        );
        this.componentList.add(progress_bar);
    }

    @Override
    public RecipeType<WeldingHandler> getRecipeType() {
        return jeiInform.recipeType;
    }

    @Nonnull
    @Override
    public String getTitles() {
        return Localization.translate(JEICompat.getBlockStack(BlockBaseMachine3.welding)
                .getDescriptionId());
    }

    @Nonnull
    @Override
    public IDrawable getBackground() {
        return bg;
    }

    @Override
    public void draw(WeldingHandler recipe, IRecipeSlotsView recipeSlotsView, GuiGraphics stack, double mouseX, double mouseY) {
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

        progress_bar.renderBar( stack,-10, 0, xScale);
        int temp = recipe.temperature;

     draw( stack,"" + temp + "°C", 82, 55, 4210752);

    }

    @Override
    public void setRecipe(IRecipeLayoutBuilder builder, WeldingHandler recipe, IFocusGroup focuses) {

        final List<SlotInvSlot> slots1 = container1.findClassSlots(InvSlotRecipes.class);
        final List<ItemStack> inputs = Arrays.asList(recipe.getInput(), recipe.getInput1());
        int i = 0;
        for (; i < inputs.size(); i++) {
            builder.addSlot(RecipeIngredientRole.INPUT,slots1.get(i).getJeiX(), slots1.get(i).getJeiY()).addItemStack(inputs.get(i));


        }

        final SlotInvSlot outputSlot = container1.findClassSlot(InvSlotOutput.class);

        builder.addSlot(RecipeIngredientRole.OUTPUT,outputSlot.getJeiX(), outputSlot.getJeiY()).addItemStack(recipe.output);

    }

    protected ResourceLocation getTexture() {
        return new ResourceLocation(Constants.MOD_ID, "textures/gui/guialloysmelter.png");
    }


}
