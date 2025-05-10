package com.denfop.integration.jei.genetic_transposer;

import com.denfop.Constants;
import com.denfop.Localization;
import com.denfop.api.gui.Component;
import com.denfop.api.gui.EnumTypeComponent;
import com.denfop.api.gui.GuiComponent;
import com.denfop.api.gui.GuiElement;
import com.denfop.api.gui.TankGauge;
import com.denfop.api.recipe.InvSlotOutput;
import com.denfop.api.recipe.InvSlotRecipes;
import com.denfop.blocks.mechanism.BlockBaseMachine3;
import com.denfop.componets.ComponentRenderInventory;
import com.denfop.componets.EnumTypeComponentSlot;
import com.denfop.container.ContainerGeneticTransposer;
import com.denfop.container.SlotInvSlot;
import com.denfop.gui.GuiIU;
import com.denfop.integration.jei.IRecipeCategory;
import com.denfop.integration.jei.JEICompat;
import com.denfop.integration.jei.JeiInform;
import com.denfop.tiles.mechanism.TileEntityGeneticTransposer;
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
import java.util.List;

public class GeneticTransposerCategory extends GuiIU implements IRecipeCategory<GeneticTransposerHandler> {

    private final IDrawableStatic bg;
    private final ContainerGeneticTransposer container1;
    private final GuiComponent progress_bar;
    private final JeiInform jeiInform;
    private int progress = 0;
    private int energy = 0;

    public GeneticTransposerCategory(
            IGuiHelper guiHelper, JeiInform jeiInform
    ) {
        super(((TileEntityGeneticTransposer) BlockBaseMachine3.genetic_transposer.getDummyTe()).getGuiContainer(Minecraft.getInstance().player));
        bg = guiHelper.createDrawable(new ResourceLocation(Constants.MOD_ID, "textures/gui/guimachine" +
                        ".png"), 3, 3, 140,
                77
        );
        this.jeiInform=jeiInform;
        this.title = net.minecraft.network.chat.Component.literal(getTitles());
        this.componentList.clear();
        this.slots = new GuiComponent(this, 3, 3, getComponent(),
                new Component<>(new ComponentRenderInventory(EnumTypeComponentSlot.SLOTS__JEI))
        );
        this.container1 = (ContainerGeneticTransposer) this.getContainer();
        this.componentList.add(slots);
        progress_bar = new GuiComponent(this, 70, 35, EnumTypeComponent.PROCESS,
                new Component<>(this.container1.base.componentProgress)
        );
        this.componentList.add(progress_bar);
        this.addElement(TankGauge.createNormal(this, 0, 4,
                ((TileEntityGeneticTransposer) BlockBaseMachine3.genetic_transposer.getDummyTe()).fluidTank
        ));

    }


    @Nonnull
    @Override
    public String getTitles() {
        return Localization.translate(JEICompat.getBlockStack(BlockBaseMachine3.genetic_transposer).getDescriptionId());
    }

    @Override
    public RecipeType<GeneticTransposerHandler> getRecipeType() {
        return jeiInform.recipeType;
    }


    @Nonnull
    @Override
    public IDrawable getBackground() {
        return bg;
    }

    @Override
    public void draw(GeneticTransposerHandler recipe, IRecipeSlotsView recipeSlotsView, PoseStack stack, double mouseX, double mouseY) {
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

        progress_bar.renderBar( stack,10, 0, xScale);
       bindTexture(getTexture());
        for (final GuiElement<?> element : ((List<GuiElement<?>>) this.elements)) {
            element.drawBackground( stack,this.guiLeft, this.guiTop);
        }
    }

    @Override
    public void setRecipe(IRecipeLayoutBuilder builder, GeneticTransposerHandler recipes, IFocusGroup focuses) {
        final List<SlotInvSlot> slots1 = container1.findClassSlots(InvSlotRecipes.class);
        final List<ItemStack> inputs = recipes.getInputs();
        int i = 0;
        for (; i < inputs.size(); i++) {
            builder.addSlot(RecipeIngredientRole.INPUT,slots1.get(i).getJeiX(), slots1.get(i).getJeiY()).addItemStack(inputs.get(i));


        }

        final SlotInvSlot outputSlot = container1.findClassSlot(InvSlotOutput.class);
        builder.addSlot(RecipeIngredientRole.OUTPUT,outputSlot.getJeiX(), outputSlot.getJeiY()).addItemStack(recipes.getOutput());

        builder.addSlot(RecipeIngredientRole.INPUT, 4, 8).setFluidRenderer(12000,true,12,47).addFluidStack( recipes.getInput2().getFluid(), recipes.getInput2().getAmount());


    }


    protected ResourceLocation getTexture() {
        return new ResourceLocation(Constants.MOD_ID, "textures/gui/GUIPlastic.png".toLowerCase());
    }


}
