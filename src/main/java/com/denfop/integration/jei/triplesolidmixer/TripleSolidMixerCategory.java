package com.denfop.integration.jei.triplesolidmixer;

import com.denfop.Constants;
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
import com.denfop.container.ContainerTripleSolidMixer;
import com.denfop.container.SlotInvSlot;
import com.denfop.gui.GuiIU;
import com.denfop.integration.jei.IRecipeCategory;
import com.denfop.integration.jei.JEICompat;
import com.denfop.integration.jei.JeiInform;
import com.denfop.tiles.mechanism.TileEntityTripleSolidMixer;
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

public class TripleSolidMixerCategory extends GuiIU implements IRecipeCategory<TripleSolidMixerHandler> {

    private final IDrawableStatic bg;
    private final ContainerTripleSolidMixer container1;
    private final GuiComponent progress_bar;
    private final GuiComponent slots1;
    JeiInform jeiInform;
    private int progress = 0;
    private int energy = 0;

    public TripleSolidMixerCategory(
            IGuiHelper guiHelper, JeiInform jeiInform
    ) {
        super(((TileEntityTripleSolidMixer) BlockBaseMachine3.triple_solid_mixer.getDummyTe()).getGuiContainer(Minecraft.getInstance().player));
        bg = guiHelper.createDrawable(ResourceLocation.tryBuild(Constants.MOD_ID, "textures/gui/guimachine" +
                        ".png"), 3, 3, 140,
                77
        );
        this.jeiInform = jeiInform;
        this.title = net.minecraft.network.chat.Component.literal(getTitles());
        this.componentList.clear();
        this.slots = new GuiComponent(this, 3, 3, getComponent(),
                new Component<>(new ComponentRenderInventory(EnumTypeComponentSlot.SLOTS__JEI_INPUT))
        );
        this.slots1 = new GuiComponent(this, 3, 3, getComponent(),
                new Component<>(new ComponentRenderInventory(EnumTypeComponentSlot.SLOTS__JEI_OUTPUT))
        );
        this.container1 = (ContainerTripleSolidMixer) this.getContainer();
        this.componentList.add(slots);
        this.componentList.add(slots1);
        progress_bar = new GuiComponent(this, 70, 35, EnumTypeComponent.PROCESS,
                new Component<>(new ComponentProgress(this.container1.base, 0, 100))
        );
        this.componentList.add(progress_bar);
    }

    @Override
    public RecipeType<TripleSolidMixerHandler> getRecipeType() {
        return jeiInform.recipeType;
    }

    @Nonnull
    @Override
    public String getTitles() {
        return Localization.translate(JEICompat.getBlockStack(BlockBaseMachine3.triple_solid_mixer).getDescriptionId());
    }


    @Nonnull
    @Override
    public IDrawable getBackground() {
        return bg;
    }

    @Override
    public void draw(TripleSolidMixerHandler recipe, IRecipeSlotsView recipeSlotsView, GuiGraphics stack, double mouseX, double mouseY) {
        if (this.energy < 100) {
            energy++;
        }
        double energylevel = energy / 100D;
        double xScale = progress / 100D;
        if (xScale >= 1) {
            progress = 0;
        }
        this.slots.drawBackground(stack, -8, -10);
        this.slots1.drawBackground(stack, -8, -10);

        progress_bar.renderBar(stack, 4, 0, xScale);
        bindTexture(getTexture());
    }

    @Override
    public void setRecipe(IRecipeLayoutBuilder builder, TripleSolidMixerHandler recipe, IFocusGroup focuses) {
        final List<SlotInvSlot> slots1 = container1.findClassSlots(InvSlotRecipes.class);
        final List<ItemStack> inputs = recipe.getInputs();
        final List<ItemStack> outputs = recipe.getOutputs();
        int i = 0;
        for (; i < inputs.size(); i++) {
            builder.addSlot(RecipeIngredientRole.INPUT, slots1.get(i).getJeiX() - 8, slots1.get(i).getJeiY() - 10).addItemStack(inputs.get(i));


        }
        final List<SlotInvSlot> outputSlot = container1.findClassSlots(InvSlotOutput.class);
        i = 0;
        for (; i < outputSlot.size(); i++) {
            builder.addSlot(RecipeIngredientRole.OUTPUT, outputSlot.get(i).getJeiX() - 8, outputSlot.get(i).getJeiY() - 10).addItemStack(outputs.get(i));


        }
        builder.addInvisibleIngredients(RecipeIngredientRole.INPUT).addItemStacks(recipe.getContainer().input.getAllStackInputs());

    }


    protected ResourceLocation getTexture() {
        return ResourceLocation.tryBuild(Constants.MOD_ID, "textures/gui/guimachine.png");
    }


}
