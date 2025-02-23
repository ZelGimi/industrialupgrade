package com.denfop.integration.jei.graphite_handler;

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
import com.denfop.container.ContainerGraphite;
import com.denfop.container.SlotInvSlot;
import com.denfop.gui.GuiIU;
import com.denfop.integration.jei.JEICompat;
import com.denfop.tiles.mechanism.TileEntityGraphiteHandler;
import mezz.jei.api.IGuiHelper;
import mezz.jei.api.gui.IDrawable;
import mezz.jei.api.gui.IDrawableStatic;
import mezz.jei.api.gui.IGuiItemStackGroup;
import mezz.jei.api.gui.IRecipeLayout;
import mezz.jei.api.ingredients.IIngredients;
import mezz.jei.api.recipe.IRecipeCategory;
import net.minecraft.client.Minecraft;
import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;

import javax.annotation.Nonnull;
import java.util.List;

public class GraphiteCategory extends GuiIU implements IRecipeCategory<GraphiteRecipeWrapper> {

    private final IDrawableStatic bg;
    private final ContainerGraphite container1;
    private final GuiComponent progress_bar;
    private int progress = 0;
    private int energy = 0;

    public GraphiteCategory(
            final IGuiHelper guiHelper
    ) {
        super(((TileEntityGraphiteHandler) BlockBaseMachine3.graphite_handler.getDummyTe()).getGuiContainer(Minecraft.getMinecraft().player));
        bg = guiHelper.createDrawable(new ResourceLocation(Constants.MOD_ID, "textures/gui/guimachine" +
                        ".png"), 3, 3, 140,
                77
        );
        this.componentList.clear();
        this.slots = new GuiComponent(this, 3, 3, getComponent(),
                new Component<>(new ComponentRenderInventory(EnumTypeComponentSlot.SLOTS_UPGRADE_JEI))
        );
        this.container1 = (ContainerGraphite) this.getContainer();
        this.componentList.add(slots);
        progress_bar = new GuiComponent(this, 70, 35, EnumTypeComponent.PROCESS,
                new Component<>(this.container1.base.timer)
        );
        this.componentList.add(progress_bar);
    }

    @Nonnull
    @Override
    public String getUid() {
        return BlockBaseMachine3.graphite_handler.getName();
    }

    @Nonnull
    @Override
    public String getTitle() {
        return Localization.translate(JEICompat.getBlockStack(BlockBaseMachine3.graphite_handler).getUnlocalizedName());
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

        progress_bar.renderBar(0, 0, xScale);
        mc.getTextureManager().bindTexture(getTexture());


    }

    @Override
    public void setRecipe(
            final IRecipeLayout layout,
            final GraphiteRecipeWrapper recipes,
            @Nonnull final IIngredients ingredients
    ) {
        IGuiItemStackGroup isg = layout.getItemStacks();

        final List<SlotInvSlot> slots1 = container1.findClassSlots(InvSlotRecipes.class);
        final List<ItemStack> inputs = recipes.getInputs1();
        int i = 0;
        for (; i < inputs.size(); i++) {
            isg.init(i, true, slots1.get(i).getJeiX(), slots1.get(i).getJeiY());
            isg.set(i, inputs.get(i));

        }

        final SlotInvSlot outputSlot = container1.findClassSlot(InvSlotOutput.class);
        isg.init(i, false, outputSlot.getJeiX(), outputSlot.getJeiY());
        isg.set(i, recipes.getOutput());
        i++;
        isg.init(i, false, 29, 42);
        isg.set(i, new ItemStack(Items.FLINT));
    }

    protected ResourceLocation getTexture() {
        return new ResourceLocation(Constants.MOD_ID, "textures/gui/guimachine.png");
    }


}
