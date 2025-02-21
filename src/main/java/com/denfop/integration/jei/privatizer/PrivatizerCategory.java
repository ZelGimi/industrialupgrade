package com.denfop.integration.jei.privatizer;

import com.denfop.Constants;
import com.denfop.IUItem;
import com.denfop.Localization;
import com.denfop.api.gui.Component;
import com.denfop.api.gui.GuiComponent;
import com.denfop.blocks.mechanism.BlockBaseMachine3;
import com.denfop.componets.ComponentRenderInventory;
import com.denfop.componets.EnumTypeComponentSlot;
import com.denfop.container.ContainerPrivatizer;
import com.denfop.container.SlotInvSlot;
import com.denfop.gui.GuiIU;
import com.denfop.tiles.mechanism.TilePrivatizer;
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
import java.util.List;

public class PrivatizerCategory extends GuiIU implements IRecipeCategory<PrivatizerWrapper> {

    private final IDrawableStatic bg;
    private final ContainerPrivatizer container1;

    public PrivatizerCategory(
            final IGuiHelper guiHelper
    ) {
        super(((TilePrivatizer) BlockBaseMachine3.privatizer.getDummyTe()).getGuiContainer(Minecraft.getMinecraft().player));

        bg = guiHelper.createDrawable(new ResourceLocation(Constants.MOD_ID, "textures/gui/guimachine.png"), 3, 3, 169,
                75
        );
        this.componentList.clear();
        this.slots = new GuiComponent(this, 3, 3, getComponent(),
                new Component<>(new ComponentRenderInventory(EnumTypeComponentSlot.SLOTS_UPGRADE_JEI))
        );
        this.container1 = (ContainerPrivatizer) this.getContainer();
        this.componentList.add(slots);
    }

    @Nonnull
    @Override
    public String getUid() {
        return BlockBaseMachine3.privatizer.getName();
    }

    @Nonnull
    @Override
    public String getTitle() {
        return Localization.translate(new ItemStack(IUItem.basemachine2, 1, 2).getUnlocalizedName());
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
    public void drawExtras(final Minecraft mc) {

        this.slots.drawBackground(0, 0);
    }

    @Override
    public void setRecipe(
            final IRecipeLayout layout,
            final PrivatizerWrapper recipes,
            @Nonnull final IIngredients ingredients
    ) {

        IGuiItemStackGroup isg = layout.getItemStacks();
        final List<SlotInvSlot> slots1 = container1.getSlots();
        isg.init(0, true, slots1.get(0).getJeiX(), slots1.get(0).getJeiY());
        isg.set(0, new ItemStack(IUItem.module7, 1, 0));
        isg.init(1, true, slots1.get(1).getJeiX(), slots1.get(1).getJeiY());
        isg.set(1, recipes.getOutput());


    }

    protected ResourceLocation getTexture() {
        return new ResourceLocation(Constants.MOD_ID, "textures/gui/guiprivatizer_jei.png");
    }


}
