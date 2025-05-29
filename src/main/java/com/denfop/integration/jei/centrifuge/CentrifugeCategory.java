package com.denfop.integration.jei.centrifuge;

import com.denfop.Constants;
import com.denfop.IUItem;
import com.denfop.Localization;
import com.denfop.api.gui.Component;
import com.denfop.api.gui.EnumTypeComponent;
import com.denfop.api.gui.GuiComponent;
import com.denfop.api.recipe.InvSlotMultiRecipes;
import com.denfop.api.recipe.InvSlotOutput;
import com.denfop.blocks.mechanism.BlockMoreMachine3;
import com.denfop.componets.ComponentProcessRender;
import com.denfop.componets.ComponentRenderInventory;
import com.denfop.componets.EnumTypeComponentSlot;
import com.denfop.container.ContainerMultiMachine;
import com.denfop.container.SlotInvSlot;
import com.denfop.gui.GuiIU;
import com.denfop.integration.jei.IRecipeCategory;
import com.denfop.integration.jei.JeiInform;
import com.denfop.recipes.ItemStackHelper;
import com.denfop.tiles.mechanism.multimechanism.simple.TileCentrifuge;
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

public class CentrifugeCategory extends GuiIU implements IRecipeCategory<CentrifugeHandler> {

    private final IDrawableStatic bg;
    private final ContainerMultiMachine container1;
    private final GuiComponent progress_bar;
    private final JeiInform jeiInform;
    private int progress = 0;
    private int energy = 0;

    public CentrifugeCategory(
            final IGuiHelper guiHelper, JeiInform jeiInform
    ) {
        super(new ContainerMultiMachine(Minecraft.getInstance().player,
                ((TileCentrifuge) BlockMoreMachine3.centrifuge_iu.getDummyTe()), 1, true
        ));
        this.jeiInform=jeiInform;
        this.title = net.minecraft.network.chat.Component.literal(getTitles());
        bg = guiHelper.createDrawable(new ResourceLocation(Constants.MOD_ID, "textures/gui/guimachine" +
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


    @Nonnull
    @Override
    public String getTitles() {
        return Localization.translate(ItemStackHelper.fromData(IUItem.machines_base3, 1, 12).getDescriptionId());
    }


    @Override
    public RecipeType<CentrifugeHandler> getRecipeType() {
        return jeiInform.recipeType;
    }
    @SuppressWarnings("removal")
    @Nonnull
    @Override
    public IDrawable getBackground() {
        return bg;
    }

    @Override
    public void draw(CentrifugeHandler recipe, IRecipeSlotsView recipeSlotsView, GuiGraphics stack, double mouseX, double mouseY) {
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
    }

    @Override
    public void setRecipe(IRecipeLayoutBuilder builder, CentrifugeHandler recipes, IFocusGroup focuses) {
        final List<SlotInvSlot> slots1 = container1.findClassSlots(InvSlotMultiRecipes.class);
        final List<ItemStack> inputs = Collections.singletonList(recipes.getInput());
        int i = 0;
        for (; i < inputs.size(); i++) {
            builder.addSlot(RecipeIngredientRole.INPUT,slots1.get(i).getJeiX(), slots1.get(i).getJeiY()).addItemStack( inputs.get(i));
        }

        final List<SlotInvSlot> outputSlots = container1.findClassSlots(InvSlotOutput.class);
        final List<ItemStack> outputs = recipes.getOutput();
        for (i = 0; i < outputs.size(); i++) {
            builder.addSlot(RecipeIngredientRole.OUTPUT, outputSlots.get(i).getJeiX(), outputSlots.get(i).getJeiY()).addItemStack( outputs.get(i));
        }
        builder.addInvisibleIngredients(RecipeIngredientRole.INPUT).addItemStacks(recipes.getContainer().input.getAllStackInputs());

    }


    protected ResourceLocation getTexture() {
        return new ResourceLocation(Constants.MOD_ID, "textures/gui/GUIMachine.png".toLowerCase());
    }


}
