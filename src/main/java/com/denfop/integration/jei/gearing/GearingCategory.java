package com.denfop.integration.jei.gearing;

import com.denfop.Constants;
import com.denfop.IUItem;
import com.denfop.Localization;
import com.denfop.api.gui.Component;
import com.denfop.api.gui.EnumTypeComponent;
import com.denfop.api.gui.GuiComponent;
import com.denfop.api.recipe.InventoryMultiRecipes;
import com.denfop.api.recipe.InventoryOutput;
import com.denfop.blocks.mechanism.BlockMoreMachine3;
import com.denfop.componets.ComponentProcessRender;
import com.denfop.componets.ComponentRenderInventory;
import com.denfop.componets.EnumTypeComponentSlot;
import com.denfop.container.ContainerMultiMachine;
import com.denfop.container.SlotInvSlot;
import com.denfop.gui.GuiIU;
import com.denfop.tiles.mechanism.multimechanism.simple.TileGearMachine;
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
import java.util.Collections;
import java.util.List;

public class GearingCategory extends GuiIU implements IRecipeCategory<GearingWrapper> {

    private final IDrawableStatic bg;
    private final ContainerMultiMachine container1;
    private final GuiComponent progress_bar;
    private int progress = 0;
    private int energy = 0;

    public GearingCategory(
            final IGuiHelper guiHelper
    ) {
        super(new ContainerMultiMachine(Minecraft.getMinecraft().player,
                ((TileGearMachine) BlockMoreMachine3.gearing.getDummyTe()), 1, true
        ));
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
        for (Slot slot : this.container1.inventorySlots) {
            if (slot instanceof SlotInvSlot) {
                int xX = slot.xPos;
                int yY = slot.yPos;
                SlotInvSlot slotInv = (SlotInvSlot) slot;
                if (slotInv.inventory instanceof InventoryMultiRecipes) {
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
    public String getUid() {
        return BlockMoreMachine3.gearing.getName();
    }

    @Nonnull
    @Override
    public String getTitle() {
        return Localization.translate(new ItemStack(IUItem.machines_base3, 1, 16).getUnlocalizedName());
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
            final GearingWrapper recipes,
            @Nonnull final IIngredients ingredients
    ) {
        IGuiItemStackGroup isg = layout.getItemStacks();
        final List<SlotInvSlot> slots1 = container1.findClassSlots(InventoryMultiRecipes.class);
        final List<ItemStack> inputs = Collections.singletonList(recipes.getInput());
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
