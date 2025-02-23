package com.denfop.integration.jei.rocketassembler;

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
import com.denfop.container.ContainerLaserPolisher;
import com.denfop.container.ContainerRocketAssembler;
import com.denfop.container.SlotInvSlot;
import com.denfop.gui.GuiIU;
import com.denfop.integration.jei.JEICompat;
import com.denfop.tiles.mechanism.TileEntityLaserPolisher;
import com.denfop.tiles.mechanism.TileEntityRocketAssembler;
import mezz.jei.api.IGuiHelper;
import mezz.jei.api.gui.IDrawable;
import mezz.jei.api.gui.IDrawableStatic;
import mezz.jei.api.gui.IGuiItemStackGroup;
import mezz.jei.api.gui.IRecipeLayout;
import mezz.jei.api.ingredients.IIngredients;
import mezz.jei.api.recipe.IRecipeCategory;
import net.minecraft.client.Minecraft;
import net.minecraft.inventory.Slot;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;

import javax.annotation.Nonnull;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

public class RocketAssemblerCategory extends GuiIU implements IRecipeCategory<RocketAssemblerRecipeWrapper> {

    private final IDrawableStatic bg;
    private final ContainerRocketAssembler container1;
    private final GuiComponent progress_bar;
    private final GuiComponent slots1;
    private int progress = 0;
    private int energy = 0;

    public RocketAssemblerCategory(
            final IGuiHelper guiHelper
    ) {
        super(((TileEntityRocketAssembler) BlockBaseMachine3.rocket_assembler.getDummyTe()).getGuiContainer(Minecraft.getMinecraft().player));
        bg = guiHelper.createDrawable(new ResourceLocation(Constants.MOD_ID, "textures/gui/common3" +
                        ".png"), 3, 3, 240,
                180
        );
        this.componentList.clear();
        this.slots = new GuiComponent(this, 3, 3, getComponent(),
                new Component<>(new ComponentRenderInventory(EnumTypeComponentSlot.SLOTS__JEI_INPUT))
        );
        this.slots1 = new GuiComponent(this, 3, 3, getComponent(),
                new Component<>(new ComponentRenderInventory(EnumTypeComponentSlot.SLOTS__JEI_OUTPUT))
        );
        this.container1 = (ContainerRocketAssembler) this.getContainer();
        this.componentList.add(slots);
        this.componentList.add(slots1);
        progress_bar = new GuiComponent(this, 70, 35, EnumTypeComponent.PROCESS,
                new Component<>(this.container1.base.componentProgress)
        );
        this.componentList.add(progress_bar);
    }

    @Nonnull
    @Override
    public String getUid() {
        return BlockBaseMachine3.rocket_assembler.getName();
    }

    @Nonnull
    @Override
    public String getTitle() {
        return Localization.translate(JEICompat.getBlockStack(BlockBaseMachine3.rocket_assembler).getUnlocalizedName());
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

        progress_bar.renderBar(50, 30, xScale);
        mc.getTextureManager().bindTexture(getTexture());


    }

    @Override
    public void setRecipe(
            final IRecipeLayout layout,
            final RocketAssemblerRecipeWrapper recipes,
            @Nonnull final IIngredients ingredients
    ) {
        IGuiItemStackGroup isg = layout.getItemStacks();

        List<SlotInvSlot> list = new ArrayList<>();
        for (Slot slot : container1.inventorySlots) {
            if (slot instanceof SlotInvSlot) {
                if (((SlotInvSlot) slot).invSlot instanceof InvSlotRecipes) {
                    list.add((SlotInvSlot) slot);
                }
            }
        }
        final List<ItemStack> inputs = recipes.getInputs1();
        int i = 0;
        for (; i < inputs.size(); i++) {
            isg.init(i, true, list.get(i).getJeiX(), list.get(i).getJeiY());
            isg.set(i, inputs.get(i));

        }

        final SlotInvSlot outputSlot = container1.findClassSlot(InvSlotOutput.class);
        isg.init(i, false, outputSlot.getJeiX(), outputSlot.getJeiY());
        isg.set(i, recipes.getOutput());
    }

    protected ResourceLocation getTexture() {
        return new ResourceLocation(Constants.MOD_ID, "textures/gui/common3.png");
    }


}
