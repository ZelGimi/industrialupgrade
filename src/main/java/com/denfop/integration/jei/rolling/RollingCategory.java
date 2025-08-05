package com.denfop.integration.jei.rolling;

import com.denfop.Constants;
import com.denfop.IUItem;
import com.denfop.Localization;
import com.denfop.api.gui.Component;
import com.denfop.api.gui.EnumTypeComponent;
import com.denfop.api.gui.GuiComponent;
import com.denfop.api.recipe.InvSlotMultiRecipes;
import com.denfop.api.recipe.InvSlotOutput;
import com.denfop.blocks.mechanism.BlockMoreMachine2;
import com.denfop.componets.ComponentProcessRender;
import com.denfop.componets.ComponentRenderInventory;
import com.denfop.componets.EnumTypeComponentSlot;
import com.denfop.container.ContainerMultiMachine;
import com.denfop.container.SlotInvSlot;
import com.denfop.gui.GuiIU;
import com.denfop.integration.jei.IRecipeCategory;
import com.denfop.integration.jei.JeiInform;
import com.denfop.recipes.ItemStackHelper;
import com.denfop.tiles.mechanism.multimechanism.simple.TileRolling;
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
import net.minecraft.world.inventory.Slot;
import net.minecraft.world.item.ItemStack;

import javax.annotation.Nonnull;
import java.util.Collections;
import java.util.List;

public class RollingCategory extends GuiIU implements IRecipeCategory<RollingHandler> {

    private final IDrawableStatic bg;
    private final ContainerMultiMachine container1;
    private final GuiComponent progress_bar;
    JeiInform jeiInform;
    private int progress = 0;
    private int energy = 0;

    public RollingCategory(
            IGuiHelper guiHelper, JeiInform jeiInform
    ) {
        super(new ContainerMultiMachine(Minecraft.getInstance().player,
                ((TileRolling) BlockMoreMachine2.rolling.getDummyTe()), 1, true
        ));
        this.jeiInform = jeiInform;
        this.title = net.minecraft.network.chat.Component.literal(getTitles());
        bg = guiHelper.createDrawable(ResourceLocation.tryBuild(Constants.MOD_ID, "textures/gui/guimachine" +
                        ".png"), 3, 3, 140,
                80
        );
        this.componentList.clear();
        this.slots = new GuiComponent(this, 3, 3, getComponent(),
                new Component<>(new ComponentRenderInventory(EnumTypeComponentSlot.SLOTS__JEI))
        );
        this.container1 = (ContainerMultiMachine) this.getContainer();
        this.componentList.add(slots);
        progress_bar = new GuiComponent(this, 0, 0, EnumTypeComponent.MULTI_PROCESS,
                new Component<>(new ComponentProcessRender(container1.base.multi_process, container1.base.getTypeMachine()))
        );
        for (Slot slot : this.container1.slots) {
            if (slot instanceof SlotInvSlot) {
                int xX = slot.x;
                int yY = slot.y;
                SlotInvSlot slotInv = (SlotInvSlot) slot;
                if (slotInv.invSlot instanceof InvSlotMultiRecipes) {
                    this.progress_bar.setIndex(0);
                    this.progress_bar.setX(xX);
                    this.progress_bar.setY(yY + 19);
                }

            }
        }
        this.componentList.add(progress_bar);
    }

    @Override
    public RecipeType<RollingHandler> getRecipeType() {
        return jeiInform.recipeType;
    }

    @Nonnull
    @Override
    public String getTitles() {
        return Localization.translate(ItemStackHelper.fromData(IUItem.machines_base2, 1, 0).getDescriptionId());
    }


    @Nonnull
    @Override
    public IDrawable getBackground() {
        return bg;
    }

    @Override
    public void draw(RollingHandler recipe, IRecipeSlotsView recipeSlotsView, GuiGraphics stack, double mouseX, double mouseY) {
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
    public void setRecipe(IRecipeLayoutBuilder builder, RollingHandler recipe, IFocusGroup focuses) {
        final List<SlotInvSlot> slots1 = container1.findClassSlots(InvSlotMultiRecipes.class);
        final List<ItemStack> inputs = Collections.singletonList(recipe.getInput());
        int i = 0;
        for (; i < inputs.size(); i++) {
            builder.addSlot(RecipeIngredientRole.INPUT, slots1.get(i).getJeiX(), slots1.get(i).getJeiY()).addItemStack(inputs.get(i));

        }
        final SlotInvSlot outputSlot = container1.findClassSlot(InvSlotOutput.class);
        builder.addInvisibleIngredients(RecipeIngredientRole.INPUT).addItemStacks(recipe.getContainer().input.getAllStackInputs());

        builder.addSlot(RecipeIngredientRole.OUTPUT, outputSlot.getJeiX(), outputSlot.getJeiY()).addItemStack(recipe.getOutput());
    }

    protected ResourceLocation getTexture() {
        return ResourceLocation.tryBuild(Constants.MOD_ID, "textures/gui/GUIMachine2.png".toLowerCase());
    }


}
