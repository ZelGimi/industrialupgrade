package com.denfop.integration.jei.refractoryfurnace;

import com.denfop.Constants;
import com.denfop.api.recipe.InventoryRecipes;
import com.denfop.api.widget.EnumTypeComponent;
import com.denfop.api.widget.ScreenWidget;
import com.denfop.api.widget.TankWidget;
import com.denfop.api.widget.WidgetDefault;
import com.denfop.blockentity.mechanism.BlockEntityItemDivider;
import com.denfop.blocks.mechanism.BlockBaseMachine3Entity;
import com.denfop.blocks.mechanism.BlockRefractoryFurnaceEntity;
import com.denfop.componets.ComponentProgress;
import com.denfop.componets.ComponentRenderInventory;
import com.denfop.componets.EnumTypeComponentSlot;
import com.denfop.containermenu.ContainerMenuItemDivider;
import com.denfop.containermenu.slot.SlotInvSlot;
import com.denfop.integration.jei.IRecipeCategory;
import com.denfop.integration.jei.JEICompat;
import com.denfop.integration.jei.JeiInform;
import com.denfop.screen.ScreenMain;
import com.denfop.utils.Localization;
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

public class RefractoryFurnaceCategory extends ScreenMain implements IRecipeCategory<RefractoryFurnaceHandler> {

    private final IDrawableStatic bg;
    private final ContainerMenuItemDivider container1;
    private final ScreenWidget progress_bar;
    JeiInform jeiInform;
    private int progress = 0;
    private int energy = 0;

    public RefractoryFurnaceCategory(
            IGuiHelper guiHelper, JeiInform jeiInform
    ) {
        super(((BlockEntityItemDivider) BlockBaseMachine3Entity.item_divider.getDummyTe()).getGuiContainer(Minecraft.getInstance().player));
        bg = guiHelper.createDrawable(new ResourceLocation(Constants.MOD_ID, "textures/gui/guimachine" +
                        ".png"), 3, 3, 140,
                77
        );
        this.jeiInform = jeiInform;
        this.title = net.minecraft.network.chat.Component.literal(getTitles());
        this.slots = new ScreenWidget(this, 3, 3, getComponent(),
                new WidgetDefault<>(new ComponentRenderInventory(EnumTypeComponentSlot.SLOTS__JEI_INPUT))
        );
        this.componentList.clear();
        this.container1 = (ContainerMenuItemDivider) this.getContainer();
        progress_bar = new ScreenWidget(this, 70, 35, EnumTypeComponent.PROCESS,
                new WidgetDefault<>(new ComponentProgress(this.container1.base, 1, (short) 100))
        );
        this.componentList.add(slots);
        this.componentList.add(progress_bar);
        this.addWidget(TankWidget.createNormal(this, 100, 4,
                ((BlockEntityItemDivider) BlockBaseMachine3Entity.item_divider.getDummyTe()).fluidTank1
        ));


    }

    @Override
    public RecipeType<RefractoryFurnaceHandler> getRecipeType() {
        return jeiInform.recipeType;
    }

    @Nonnull
    @Override
    public String getTitles() {
        return Localization.translate(JEICompat.getBlockStack(BlockRefractoryFurnaceEntity.refractory_furnace).getDescriptionId());
    }


    @Nonnull
    @Override
    public IDrawable getBackground() {
        return bg;
    }

    @Override
    public void draw(RefractoryFurnaceHandler recipe, IRecipeSlotsView recipeSlotsView, PoseStack stack, double mouseX, double mouseY) {
        progress++;
        if (this.energy < 100) {
            energy++;
        }
        double energylevel = energy / 100D;
        double xScale = progress / 100D;
        if (xScale >= 1) {
            progress = 0;
        }
        bindTexture(getTexture());
        this.slots.drawBackground(stack, 0, -10);
        progress_bar.renderBar(stack, 0, 0, xScale);

        for (final ScreenWidget element : ((List<ScreenWidget>) this.elements)) {
            element.drawBackground(stack, this.guiLeft, this.guiTop);
        }
    }


    @Override
    public void setRecipe(IRecipeLayoutBuilder builder, RefractoryFurnaceHandler recipe, IFocusGroup focuses) {
        final List<SlotInvSlot> slots1 = container1.findClassSlots(InventoryRecipes.class);
        final List<ItemStack> inputs = Collections.singletonList(recipe.getInput());
        int i = 0;
        for (; i < inputs.size(); i++) {
            builder.addSlot(RecipeIngredientRole.INPUT, slots1.get(i).getJeiX(), slots1.get(i).getJeiY() - 10).addItemStack(inputs.get(i));

        }
        builder.addSlot(RecipeIngredientRole.OUTPUT, 104, 8).setFluidRenderer(10000, true, 12, 47).addFluidStack(recipe.getOutput().getFluid(), recipe.getOutput().getAmount());


    }


    protected ResourceLocation getTexture() {
        return new ResourceLocation(Constants.MOD_ID, "textures/gui/guimachine.png");
    }


}
