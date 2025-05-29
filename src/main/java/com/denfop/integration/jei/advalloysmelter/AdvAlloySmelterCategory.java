package com.denfop.integration.jei.advalloysmelter;

import com.denfop.Constants;
import com.denfop.IUItem;
import com.denfop.Localization;
import com.denfop.api.gui.Component;
import com.denfop.api.gui.EnumTypeComponent;
import com.denfop.api.gui.GuiComponent;
import com.denfop.api.recipe.InvSlotOutput;
import com.denfop.api.recipe.InvSlotRecipes;
import com.denfop.blocks.mechanism.BlockBaseMachine1;
import com.denfop.componets.ComponentRenderInventory;
import com.denfop.componets.EnumTypeComponentSlot;
import com.denfop.container.ContainerTripleElectricMachine;
import com.denfop.container.SlotInvSlot;
import com.denfop.gui.GuiIU;
import com.denfop.integration.jei.IRecipeCategory;
import com.denfop.integration.jei.JeiInform;
import com.denfop.recipes.ItemStackHelper;
import com.denfop.tiles.mechanism.triple.heat.TileAdvAlloySmelter;
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

public class AdvAlloySmelterCategory extends GuiIU implements IRecipeCategory<AdvAlloySmelterHandler> {

    private final IDrawableStatic bg;
    private final ContainerTripleElectricMachine container1;
    private final GuiComponent progress_bar;
    private final GuiComponent slots1;
    private int progress = 0;
    private int energy = 0;
    private final JeiInform<AdvAlloySmelterCategory,AdvAlloySmelterHandler>  jeiInform;
    public AdvAlloySmelterCategory(
            IGuiHelper guiHelper, JeiInform<AdvAlloySmelterCategory,AdvAlloySmelterHandler> jeiInform
    ) {
        super(((TileAdvAlloySmelter) BlockBaseMachine1.adv_alloy_smelter.getDummyTe()).getGuiContainer(Minecraft.getInstance().player));

        bg = guiHelper.createDrawable(new ResourceLocation(Constants.MOD_ID, "textures/gui/guimachine" +
                        ".png"), 3, 3, 140,
                77
        );
        this.title = net.minecraft.network.chat.Component.literal(getTitles());
        this.jeiInform=jeiInform;
        this.componentList.clear();
        this.slots = new GuiComponent(this, 3, 3, getComponent(),
                new Component<>(new ComponentRenderInventory(EnumTypeComponentSlot.SLOTS__JEI_INPUT))
        );
        this.slots1 = new GuiComponent(this, 3, 3, getComponent(),
                new Component<>(new ComponentRenderInventory(EnumTypeComponentSlot.SLOTS__JEI_OUTPUT))
        );
        this.container1 = (ContainerTripleElectricMachine) this.getContainer();
        this.componentList.add(slots);
        progress_bar = new GuiComponent(this, 85, 35, EnumTypeComponent.PROCESS,
                new Component<>(this.container1.base.componentProgress)
        );
        this.componentList.add(progress_bar);

    }


    @Override
    public RecipeType<AdvAlloySmelterHandler> getRecipeType() {
        return jeiInform.recipeType;
    }

    @Nonnull
    @Override
    public String getTitles() {
        return Localization.translate(ItemStackHelper.fromData(IUItem.basemachine, 1, 3).getDescriptionId());
    }



    @SuppressWarnings("removal")
    @Override
    public IDrawable getBackground() {
        return bg;
    }

    @Override
    public IDrawable getIcon() {
        return null;
    }

    @Override
    public void setRecipe(IRecipeLayoutBuilder builder, AdvAlloySmelterHandler recipes, IFocusGroup focuses) {
        final List<SlotInvSlot> slots1 = container1.findClassSlots(InvSlotRecipes.class);
        final List<ItemStack> inputs = Arrays.asList(recipes.getInput(), recipes.getInput1(), recipes.getInput2());
        int i = 0;
        for (; i < inputs.size(); i++) {
            builder.addSlot(RecipeIngredientRole.INPUT,slots1.get(i).getJeiX() - 20, slots1.get(i).getJeiY() + 15).addItemStack(inputs.get(i));


        }
        builder.addInvisibleIngredients(RecipeIngredientRole.INPUT).addItemStacks(recipes.getContainer().input.getAllStackInputs());

        final SlotInvSlot outputSlot = container1.findClassSlot(InvSlotOutput.class);
        builder.addSlot(RecipeIngredientRole.OUTPUT, outputSlot.getJeiX(), outputSlot.getJeiY()).addItemStack(recipes.getOutput());
    }
    @Override
    public void draw(AdvAlloySmelterHandler recipe, IRecipeSlotsView recipeSlotsView, GuiGraphics poseStack, double mouseX, double mouseY) {
        progress++;
        if (this.energy < 100) {
            energy++;
        }
        double energylevel = energy / 100D;
        double xScale = progress / 100D;
        if (xScale >= 1) {
            progress = 0;
        }
        this.slots.drawBackground(poseStack, -20, 15);
        this.slots1.drawBackground(poseStack,0, 0);
        progress_bar.renderBar(poseStack,0, 0, xScale);
        bindTexture(getTexture());
        int temp = recipe.temperature;

        this.draw(poseStack, "" + temp + "°C", 82, 55, 4210752);

    }




    protected ResourceLocation getTexture() {
        return new ResourceLocation(Constants.MOD_ID, "textures/gui/guimachine.png");
    }


}
