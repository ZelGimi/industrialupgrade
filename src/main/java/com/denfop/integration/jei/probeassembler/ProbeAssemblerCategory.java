package com.denfop.integration.jei.probeassembler;

import com.denfop.Constants;
import com.denfop.api.recipe.InventoryOutput;
import com.denfop.api.recipe.InventoryRecipes;
import com.denfop.api.widget.EnumTypeComponent;
import com.denfop.api.widget.ScreenWidget;
import com.denfop.api.widget.WidgetDefault;
import com.denfop.blockentity.mechanism.BlockEntityProbeAssembler;
import com.denfop.blocks.mechanism.BlockBaseMachine3Entity;
import com.denfop.componets.ComponentRenderInventory;
import com.denfop.componets.EnumTypeComponentSlot;
import com.denfop.containermenu.ContainerMenuProbeAssembler;
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
import net.minecraft.world.inventory.Slot;
import net.minecraft.world.item.ItemStack;

import javax.annotation.Nonnull;
import java.util.ArrayList;
import java.util.List;

public class ProbeAssemblerCategory extends ScreenMain implements IRecipeCategory<ProbeAssemblerHandler> {

    private final IDrawableStatic bg;
    private final ContainerMenuProbeAssembler container1;
    private final ScreenWidget progress_bar;
    private final ScreenWidget slots1;
    private final JeiInform jeiInform;
    private int progress = 0;
    private int energy = 0;

    public ProbeAssemblerCategory(
            IGuiHelper guiHelper, JeiInform jeiInform
    ) {
        super(((BlockEntityProbeAssembler) BlockBaseMachine3Entity.probe_assembler.getDummyTe()).getGuiContainer(Minecraft.getInstance().player));
        bg = guiHelper.createDrawable(new ResourceLocation(Constants.MOD_ID, "textures/gui/common3" +
                        ".png"), 3, 3, 240,
                170
        );
        this.jeiInform = jeiInform;
        this.title = net.minecraft.network.chat.Component.literal(getTitles());
        this.componentList.clear();
        this.slots = new ScreenWidget(this, 3, 3, getComponent(),
                new WidgetDefault<>(new ComponentRenderInventory(EnumTypeComponentSlot.SLOTS__JEI_INPUT))
        );
        this.slots1 = new ScreenWidget(this, 3, 3, getComponent(),
                new WidgetDefault<>(new ComponentRenderInventory(EnumTypeComponentSlot.SLOTS__JEI_OUTPUT))
        );
        this.container1 = (ContainerMenuProbeAssembler) this.getContainer();
        this.componentList.add(slots);
        this.componentList.add(slots1);
        progress_bar = new ScreenWidget(this, 70, 35, EnumTypeComponent.PROCESS,
                new WidgetDefault<>(this.container1.base.componentProgress)
        );
        this.componentList.add(progress_bar);
    }

    @Override
    public RecipeType<ProbeAssemblerHandler> getRecipeType() {
        return jeiInform.recipeType;
    }

    @Nonnull
    @Override
    public String getTitles() {
        return Localization.translate(JEICompat.getBlockStack(BlockBaseMachine3Entity.probe_assembler).getDescriptionId());
    }


    @Nonnull
    @Override
    public IDrawable getBackground() {
        return bg;
    }

    @Override
    public void draw(ProbeAssemblerHandler recipe, IRecipeSlotsView recipeSlotsView, PoseStack stack, double mouseX, double mouseY) {
        progress++;
        if (this.energy < 100) {
            energy++;
        }
        double energylevel = energy / 100D;
        double xScale = progress / 100D;
        if (xScale >= 1) {
            progress = 0;
        }
        this.slots.drawBackground(stack, 0, 0);
        this.slots1.drawBackground(stack, 0, 0);

        progress_bar.renderBar(stack, 90, 33, xScale);
    }

    @Override
    public void setRecipe(IRecipeLayoutBuilder builder, ProbeAssemblerHandler recipe, IFocusGroup focuses) {
        List<SlotInvSlot> list = new ArrayList<>();
        for (Slot slot : container1.slots) {
            if (slot instanceof SlotInvSlot) {
                if (((SlotInvSlot) slot).inventory instanceof InventoryRecipes) {
                    list.add((SlotInvSlot) slot);
                }
            }
        }
        final List<ItemStack> inputs = recipe.getInput();
        int i = 0;
        for (; i < inputs.size(); i++) {
            builder.addSlot(RecipeIngredientRole.INPUT, list.get(i).getJeiX(), list.get(i).getJeiY()).addItemStack(inputs.get(i));

        }

        final SlotInvSlot outputSlot = container1.findClassSlot(InventoryOutput.class);
        builder.addSlot(RecipeIngredientRole.OUTPUT, outputSlot.getJeiX(), outputSlot.getJeiY()).addItemStack(recipe.output);

    }


    protected ResourceLocation getTexture() {
        return new ResourceLocation(Constants.MOD_ID, "textures/gui/common3.png");
    }


}
