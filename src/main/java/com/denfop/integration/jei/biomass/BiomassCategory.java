package com.denfop.integration.jei.biomass;

import com.denfop.Constants;
import com.denfop.IUItem;
import com.denfop.Localization;
import com.denfop.api.gui.Component;
import com.denfop.api.gui.EnumTypeComponent;
import com.denfop.api.gui.GuiComponent;
import com.denfop.api.gui.GuiElement;
import com.denfop.api.gui.TankGauge;
import com.denfop.api.recipe.InvSlotRecipes;
import com.denfop.blocks.mechanism.BlockBaseMachine3;
import com.denfop.componets.ComponentProgress;
import com.denfop.componets.ComponentRenderInventory;
import com.denfop.componets.EnumTypeComponentSlot;
import com.denfop.container.ContainerItemDivider;
import com.denfop.container.SlotInvSlot;
import com.denfop.gui.GuiIU;
import com.denfop.integration.jei.JEICompat;
import com.denfop.tiles.mechanism.TileEntityItemDivider;
import mezz.jei.api.IGuiHelper;
import mezz.jei.api.gui.IDrawable;
import mezz.jei.api.gui.IDrawableStatic;
import mezz.jei.api.gui.IGuiFluidStackGroup;
import mezz.jei.api.gui.IGuiItemStackGroup;
import mezz.jei.api.gui.IRecipeLayout;
import mezz.jei.api.ingredients.IIngredients;
import mezz.jei.api.recipe.IRecipeCategory;
import net.minecraft.client.Minecraft;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;

import javax.annotation.Nonnull;
import java.util.Collections;
import java.util.List;

public class BiomassCategory extends GuiIU implements IRecipeCategory<BiomassWrapper> {

    private final IDrawableStatic bg;
    private final ContainerItemDivider container1;
    private final GuiComponent progress_bar;
    private int progress = 0;
    private int energy = 0;

    public BiomassCategory(
            final IGuiHelper guiHelper
    ) {
        super(((TileEntityItemDivider) BlockBaseMachine3.item_divider.getDummyTe()).getGuiContainer(Minecraft.getMinecraft().player));
        bg = guiHelper.createDrawable(new ResourceLocation(Constants.MOD_ID, "textures/gui/guimachine" +
                        ".png"), 3, 3, 140,
                77
        );
        this.slots = new GuiComponent(this, 3, 3, getComponent(),
                new Component<>(new ComponentRenderInventory(EnumTypeComponentSlot.SLOTS__JEI_INPUT))
        );
        this.componentList.clear();
        this.container1 = (ContainerItemDivider) this.getContainer();
        progress_bar = new GuiComponent(this, 70, 35, EnumTypeComponent.PROCESS,
                new Component<>(new ComponentProgress(this.container1.base, 1, (short) 100))
        );
        this.componentList.add(slots);
        this.componentList.add(progress_bar);
        this.addElement(TankGauge.createNormal(this, 100, 4,
                ((TileEntityItemDivider) BlockBaseMachine3.item_divider.getDummyTe()).fluidTank1
        ));


    }

    @Nonnull
    @Override
    public String getUid() {
        return BlockBaseMachine3.steam_bio_generator.getName();
    }

    @Nonnull
    @Override
    public String getTitle() {
        return Localization.translate(JEICompat.getBlockStack(BlockBaseMachine3.steam_bio_generator).getUnlocalizedName());
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
        mc.getTextureManager().bindTexture(getTexture());
        this.slots.drawBackground(0, -10);
        progress_bar.renderBar(0, 0, xScale);

        for (final GuiElement<?> element : ((List<GuiElement<?>>) this.elements)) {
            element.drawBackground(this.guiLeft, this.guiTop - 5);
        }

    }

    @Override
    public void setRecipe(
            final IRecipeLayout layout,
            final BiomassWrapper recipes,
            @Nonnull final IIngredients ingredients
    ) {


        IGuiFluidStackGroup fff = layout.getFluidStacks();
        IGuiItemStackGroup isg = layout.getItemStacks();
        final List<SlotInvSlot> slots1 = container1.findClassSlots(InvSlotRecipes.class);
        final List<ItemStack> inputs = Collections.singletonList(recipes.getInputstack());
        int i = 0;
        for (; i < inputs.size(); i++) {
            isg.init(i, true, slots1.get(i).getJeiX(), slots1.get(i).getJeiY() - 10);
            isg.set(i, inputs.get(i));

        }


        fff.init(1, false, 104, 8, 12, 47, 1000, true, null);
        fff.set(1, recipes.getOutputstack());
    }

    protected ResourceLocation getTexture() {
        return new ResourceLocation(Constants.MOD_ID, "textures/gui/guimachine.png");
    }


}
