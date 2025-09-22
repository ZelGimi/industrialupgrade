package com.denfop.integration.jei.impalloysmelter;

import com.denfop.Constants;
import com.denfop.Localization;
import com.denfop.api.gui.Component;
import com.denfop.api.gui.EnumTypeComponent;
import com.denfop.api.gui.GuiComponent;
import com.denfop.api.recipe.InventoryOutput;
import com.denfop.api.recipe.InventoryRecipes;
import com.denfop.blocks.mechanism.BlockBaseMachine3;
import com.denfop.componets.ComponentRenderInventory;
import com.denfop.componets.EnumTypeComponentSlot;
import com.denfop.container.ContainerImpAlloySmelter;
import com.denfop.container.SlotInvSlot;
import com.denfop.gui.GuiIU;
import com.denfop.integration.jei.JEICompat;
import com.denfop.tiles.mechanism.TileEntityImpAlloySmelter;
import mezz.jei.api.IGuiHelper;
import mezz.jei.api.gui.IDrawable;
import mezz.jei.api.gui.IDrawableStatic;
import mezz.jei.api.gui.IGuiItemStackGroup;
import mezz.jei.api.gui.IRecipeLayout;
import mezz.jei.api.ingredients.IIngredients;
import mezz.jei.api.recipe.IRecipeCategory;
import net.minecraft.client.Minecraft;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;

import javax.annotation.Nonnull;
import java.util.Arrays;
import java.util.List;

public class ImpAlloySmelterCategory extends GuiIU implements IRecipeCategory<ImpAlloySmelterRecipeWrapper> {

    private final IDrawableStatic bg;
    private final ContainerImpAlloySmelter container1;
    private final GuiComponent progress_bar;
    private final GuiComponent slots1;
    private int progress = 0;
    private int energy = 0;

    public ImpAlloySmelterCategory(
            final IGuiHelper guiHelper
    ) {
        super(((TileEntityImpAlloySmelter) BlockBaseMachine3.imp_alloy_smelter.getDummyTe()).getGuiContainer(Minecraft.getMinecraft().player));

        bg = guiHelper.createDrawable(new ResourceLocation(Constants.MOD_ID, "textures/gui/guimachine" +
                        ".png"), 3, 3, 140,
                77
        );
        this.componentList.clear();
        this.slots = new GuiComponent(this, 3, 3, getComponent(),
                new Component<>(new ComponentRenderInventory(EnumTypeComponentSlot.SLOTS__JEI_INPUT))
        );
        this.slots1 = new GuiComponent(this, 3, 3, getComponent(),
                new Component<>(new ComponentRenderInventory(EnumTypeComponentSlot.SLOTS__JEI_OUTPUT))
        );
        this.container1 = (ContainerImpAlloySmelter) this.getContainer();
        this.componentList.add(slots);
        progress_bar = new GuiComponent(this, 85, 35, EnumTypeComponent.PROCESS,
                new Component<>(this.container1.base.componentProgress)
        );
        this.componentList.add(progress_bar);

    }

    @Nonnull
    @Override
    public String getUid() {
        return BlockBaseMachine3.imp_alloy_smelter.getName();
    }

    @Nonnull
    @Override
    public String getTitle() {
        return Localization.translate(JEICompat.getBlockStack(BlockBaseMachine3.imp_alloy_smelter).getUnlocalizedName());
    }

    @Nonnull
    @Override
    public String getModName() {
        return Constants.MOD_NAME;
    }

    @Nonnull
    @Override
    public IDrawable getBackground() {
        return bg;
    }


    @Override
    public void drawExtras(@Nonnull final Minecraft mc) {
        progress++;
        if (this.energy < 100) {
            energy++;
        }
        double energylevel = energy / 100D;
        double xScale = progress / 100D;
        if (xScale >= 1) {
            progress = 0;
        }
        this.slots.drawBackground(0, 0);
        this.slots1.drawBackground(0, 0);
        progress_bar.renderBar(0, 0, xScale);
        mc.getTextureManager().bindTexture(getTexture());
    }

    @Override
    public void setRecipe(
            final IRecipeLayout layout,
            final ImpAlloySmelterRecipeWrapper recipes,
            @Nonnull final IIngredients ingredients
    ) {
        IGuiItemStackGroup isg = layout.getItemStacks();

        final List<SlotInvSlot> slots1 = container1.findClassSlots(InventoryRecipes.class);
        final List<ItemStack> inputs = Arrays.asList(recipes.getInput(), recipes.getInput1(), recipes.getInput2(),
                recipes.inputstack3
        );
        int i = 0;
        for (; i < inputs.size(); i++) {

            isg.init(i, true, slots1.get(i).getJeiX(), slots1.get(i).getJeiY());
            isg.set(i, inputs.get(i));

        }

        final SlotInvSlot outputSlot = container1.findClassSlot(InventoryOutput.class);
        isg.init(i, false, outputSlot.getJeiX(), outputSlot.getJeiY());
        isg.set(i, recipes.getOutput());
    }

    protected ResourceLocation getTexture() {
        return new ResourceLocation(Constants.MOD_ID, "textures/gui/guimachine.png");
    }


}
