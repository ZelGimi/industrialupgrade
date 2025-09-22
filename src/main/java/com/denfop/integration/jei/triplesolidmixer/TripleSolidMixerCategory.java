package com.denfop.integration.jei.triplesolidmixer;

import com.denfop.Constants;
import com.denfop.Localization;
import com.denfop.api.gui.Component;
import com.denfop.api.gui.EnumTypeComponent;
import com.denfop.api.gui.GuiComponent;
import com.denfop.api.recipe.InventoryOutput;
import com.denfop.api.recipe.InventoryRecipes;
import com.denfop.blocks.mechanism.BlockBaseMachine3;
import com.denfop.componets.ComponentProgress;
import com.denfop.componets.ComponentRenderInventory;
import com.denfop.componets.EnumTypeComponentSlot;
import com.denfop.container.ContainerTripleSolidMixer;
import com.denfop.container.SlotInvSlot;
import com.denfop.gui.GuiIU;
import com.denfop.integration.jei.JEICompat;
import com.denfop.tiles.mechanism.TileEntityTripleSolidMixer;
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

public class TripleSolidMixerCategory extends GuiIU implements IRecipeCategory<TripleSolidMixerRecipeWrapper> {

    private final IDrawableStatic bg;
    private final ContainerTripleSolidMixer container1;
    private final GuiComponent progress_bar;
    private final GuiComponent slots1;
    private int progress = 0;
    private int energy = 0;

    public TripleSolidMixerCategory(
            final IGuiHelper guiHelper
    ) {
        super(((TileEntityTripleSolidMixer) BlockBaseMachine3.triple_solid_mixer.getDummyTe()).getGuiContainer(Minecraft.getMinecraft().player));
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
        this.container1 = (ContainerTripleSolidMixer) this.getContainer();
        this.componentList.add(slots);
        this.componentList.add(slots1);
        progress_bar = new GuiComponent(this, 70, 35, EnumTypeComponent.PROCESS,
                new Component<>(new ComponentProgress(this.container1.base, 0, 100))
        );
        this.componentList.add(progress_bar);
    }

    @Nonnull
    @Override
    public String getUid() {
        return BlockBaseMachine3.triple_solid_mixer.getName();
    }

    @Nonnull
    @Override
    public String getTitle() {
        return Localization.translate(JEICompat.getBlockStack(BlockBaseMachine3.triple_solid_mixer).getUnlocalizedName());
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
        this.slots.drawBackground(-8, -10);
        this.slots1.drawBackground(-8, -10);

        progress_bar.renderBar(4, 0, xScale);
        mc.getTextureManager().bindTexture(getTexture());


    }

    @Override
    public void setRecipe(
            final IRecipeLayout layout,
            final TripleSolidMixerRecipeWrapper recipes,
            @Nonnull final IIngredients ingredients
    ) {
        IGuiItemStackGroup isg = layout.getItemStacks();

        final List<SlotInvSlot> slots1 = container1.findClassSlots(InventoryRecipes.class);
        final List<ItemStack> inputs = recipes.getInputs1();
        final List<ItemStack> outputs = recipes.getOutputs();
        int i = 0;
        for (; i < inputs.size(); i++) {
            isg.init(i, true, slots1.get(i).getJeiX() - 8, slots1.get(i).getJeiY() - 10);
            isg.set(i, inputs.get(i));

        }
        final List<SlotInvSlot> outputSlot = container1.findClassSlots(InventoryOutput.class);
        i = 0;
        for (; i < outputSlot.size(); i++) {
            isg.init(i + inputs.size(), true, outputSlot.get(i).getJeiX() - 8, outputSlot.get(i).getJeiY() - 10);
            isg.set(i + inputs.size(), outputs.get(i));

        }


    }

    protected ResourceLocation getTexture() {
        return new ResourceLocation(Constants.MOD_ID, "textures/gui/guimachine.png");
    }


}
