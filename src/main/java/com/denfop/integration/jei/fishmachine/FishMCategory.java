package com.denfop.integration.jei.fishmachine;

import com.denfop.Constants;
import com.denfop.IUItem;
import com.denfop.Localization;
import com.denfop.api.gui.Component;
import com.denfop.api.gui.EnumTypeComponent;
import com.denfop.api.gui.GuiComponent;
import com.denfop.api.recipe.InventoryOutput;
import com.denfop.blocks.mechanism.BlockBaseMachine2;
import com.denfop.componets.ComponentProgress;
import com.denfop.componets.ComponentRenderInventory;
import com.denfop.componets.EnumTypeComponentSlot;
import com.denfop.container.ContainerFisher;
import com.denfop.container.SlotInvSlot;
import com.denfop.gui.GuiIU;
import com.denfop.tiles.base.TileFisher;
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

public class FishMCategory extends GuiIU implements IRecipeCategory<FishMWrapper> {

    private final IDrawableStatic bg;
    private final ContainerFisher container1;
    private final GuiComponent progress_bar;
    private int energy = 0;
    private int progress = 0;

    public FishMCategory(
            final IGuiHelper guiHelper
    ) {
        super(((TileFisher) BlockBaseMachine2.fisher.getDummyTe()).getGuiContainer(Minecraft.getMinecraft().player));
        bg = guiHelper.createDrawable(new ResourceLocation(Constants.MOD_ID, "textures/gui/guimachine" +
                        ".png"), 3, 3, 140,
                82
        );
        this.componentList.clear();
        this.slots = new GuiComponent(this, 3, 3, getComponent(),
                new Component<>(new ComponentRenderInventory(EnumTypeComponentSlot.SLOTS_UPGRADE_JEI))
        );
        this.container1 = (ContainerFisher) this.getContainer();
        this.componentList.add(slots);
        progress_bar = new GuiComponent(this, 70, 35, EnumTypeComponent.PROGRESS4,
                new Component<>(new ComponentProgress(container1.base, 1, (short) 100))
        );
        this.componentList.add(progress_bar);


    }

    @Nonnull
    @Override
    public String getUid() {
        return BlockBaseMachine2.fisher.getName();
    }

    @Nonnull
    @Override
    public String getTitle() {
        return Localization.translate(new ItemStack(IUItem.basemachine1, 1, 1).getUnlocalizedName());
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
        progress++;
        if (this.energy < 100) {
            energy++;
        }

        double xScale = progress / 100D;
        if (xScale >= 1) {
            progress = 0;
        }
        this.slots.drawBackground(0, 0);

        progress_bar.renderBar(-30, 8, xScale);

    }

    @Override
    public void setRecipe(
            final IRecipeLayout layout,
            final FishMWrapper recipes,
            @Nonnull final IIngredients ingredients
    ) {
        IGuiItemStackGroup isg = layout.getItemStacks();
        final List<SlotInvSlot> slots1 = container1.getSlots();

        int i = 0;
        isg.init(i, true, slots1.get(i).getJeiX(), slots1.get(i).getJeiY());
        isg.set(i, new ItemStack(Items.FISHING_ROD));

        final SlotInvSlot outputSlot = container1.findClassSlot(InventoryOutput.class);
        isg.init(i + 1, false, outputSlot.getJeiX(), outputSlot.getJeiY());
        isg.set(i + 1, recipes.getOutput());

    }

    protected ResourceLocation getTexture() {
        return new ResourceLocation(Constants.MOD_ID, "textures/gui/guimachine.png");
    }


}
