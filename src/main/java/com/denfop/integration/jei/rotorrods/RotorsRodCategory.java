package com.denfop.integration.jei.rotorrods;

import com.denfop.Constants;
import com.denfop.IUItem;
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
import com.denfop.container.ContainerRodManufacturer;
import com.denfop.container.SlotInvSlot;
import com.denfop.gui.GuiIU;
import com.denfop.tiles.mechanism.TileEntityRodManufacturer;
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

public class RotorsRodCategory extends GuiIU implements IRecipeCategory<RotorsRodWrapper> {

    private final IDrawableStatic bg;
    private final ContainerRodManufacturer container1;
    private final GuiComponent progress_bar;
    private int progress = 0;

    public RotorsRodCategory(
            final IGuiHelper guiHelper
    ) {
        super(((TileEntityRodManufacturer) BlockBaseMachine3.rods_manufacturer.getDummyTe()).getGuiContainer(Minecraft.getMinecraft().player));
        bg = guiHelper.createDrawable(new ResourceLocation(Constants.MOD_ID, "textures/gui/guimachine" +
                        ".png"), 3, 3, 140,
                77
        );
        this.componentList.clear();
        this.slots = new GuiComponent(this, 3, 3, getComponent(),
                new Component<>(new ComponentRenderInventory(EnumTypeComponentSlot.SLOTS__JEI))
        );
        this.container1 = (ContainerRodManufacturer) this.getContainer();
        this.componentList.add(slots);
        progress_bar = new GuiComponent(this, 70, 36, EnumTypeComponent.PROCESS,
                new Component<>(new ComponentProgress(null, 1, (short) container1.base.defaultOperationLength))
        );
        this.componentList.add(progress_bar);
    }

    @Nonnull
    @Override
    public String getUid() {
        return BlockBaseMachine3.rods_manufacturer.getName();
    }

    @Nonnull
    @Override
    public String getTitle() {
        return Localization.translate(new ItemStack(IUItem.basemachine2, 1, 21).getUnlocalizedName());
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

        double xScale = progress / 100D;
        if (xScale >= 1) {
            progress = 0;
        }
        this.slots.drawBackground(0, 0);

        progress_bar.renderBar(10, 0, xScale);
        mc.getTextureManager().bindTexture(getTexture());
    }

    @Override
    public void setRecipe(
            final IRecipeLayout layout,
            final RotorsRodWrapper recipes,
            @Nonnull final IIngredients ingredients
    ) {
        IGuiItemStackGroup isg = layout.getItemStacks();
        final List<SlotInvSlot> slots1 = container1.findClassSlots(InvSlotRecipes.class);
        int i = 0;
        for (; i < recipes.getInput().length; i++) {
            isg.init(i, true, slots1.get(i).getJeiX(), slots1.get(i).getJeiY());
            isg.set(i, recipes.getInput()[i]);

        }

        final SlotInvSlot outputSlot = container1.findClassSlot(InvSlotOutput.class);
        isg.init(i, false, outputSlot.getJeiX(), outputSlot.getJeiY());
        isg.set(i, recipes.getOutput());
    }

    protected ResourceLocation getTexture() {
        return new ResourceLocation(Constants.MOD_ID, "textures/gui/guirotorrods.png");
    }


}
