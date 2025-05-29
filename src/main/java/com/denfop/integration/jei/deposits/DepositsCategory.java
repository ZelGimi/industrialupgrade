package com.denfop.integration.jei.deposits;

import com.denfop.Constants;
import com.denfop.Localization;
import com.denfop.blocks.mechanism.BlockBaseMachine3;
import com.denfop.gui.GuiIU;
import com.denfop.integration.jei.IRecipeCategory;
import com.denfop.integration.jei.JeiInform;
import com.denfop.tiles.mechanism.TileEntityLaserPolisher;
import com.denfop.world.vein.VeinType;
import mezz.jei.api.gui.builder.IRecipeLayoutBuilder;
import mezz.jei.api.gui.drawable.IDrawable;
import mezz.jei.api.gui.drawable.IDrawableStatic;
import mezz.jei.api.gui.ingredient.IRecipeSlotsView;
import mezz.jei.api.helpers.IGuiHelper;
import mezz.jei.api.recipe.IFocusGroup;
import mezz.jei.api.recipe.RecipeIngredientRole;
import mezz.jei.api.recipe.RecipeType;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ItemStack;

import javax.annotation.Nonnull;
import java.util.List;

public class DepositsCategory extends GuiIU implements IRecipeCategory<DepositsHandler> {

    private final IDrawableStatic bg;
    private final JeiInform jeiInform;

    public DepositsCategory(
            final IGuiHelper guiHelper, JeiInform jeiInform
    ) {
        super(((TileEntityLaserPolisher) BlockBaseMachine3.laser_polisher.getDummyTe()).getGuiContainer1(Minecraft.getInstance().player));

        bg = guiHelper.createDrawable(new ResourceLocation(Constants.MOD_ID, "textures/gui/common3.png"), 3, 3, 200,
                180
        );
        this.jeiInform=jeiInform;
        this.title = net.minecraft.network.chat.Component.literal(getTitles());
    }


    @Nonnull
    @Override
    public String getTitles() {
        return Localization.translate("deposists.jei");
    }



    @SuppressWarnings("removal")
    @Nonnull
    @Override
    public IDrawable getBackground() {
        return bg;
    }

    @Override
    public void draw(DepositsHandler recipe, IRecipeSlotsView recipeSlotsView, GuiGraphics stack, double mouseX, double mouseY) {
        VeinType vein = recipe.getVeinType();
        int y = 20;
        int x = 25;
        this.drawSplitString(stack, Localization.translate("deposists.jei1") + (vein.getHeavyOre() != null ?
                        new ItemStack(vein.getHeavyOre().getBlock(), 1).getDisplayName().getString() :
                        new ItemStack(vein.getOres().get(0).getBlock().getBlock(), 1
                        ).getDisplayName().getString()), 5, 3,
                200 - 5, 4210752
        );
        if (vein.getHeavyOre() != null) {
            this.drawSplitString(stack, " 50%", x, y, 200 - x, 4210752);
            for (int i = 0; i < vein.getOres().size(); i++) {
                this.drawSplitString(stack, " " + vein.getOres().get(i).getChance() + "%", x, y + 19 + 20 * i,
                        200 - x, 4210752
                );
            }
        } else {
            for (int i = 0; i < vein.getOres().size(); i++) {
                this.drawSplitString(stack, " " + vein.getOres().get(i).getChance() + "%", x, y + 20 * i,
                        200 - x, 4210752
                );
            }
        }
    }

    @Override
    public RecipeType<DepositsHandler> getRecipeType() {
        return jeiInform.recipeType;
    }

    @Override
    public void setRecipe(IRecipeLayoutBuilder builder, DepositsHandler recipes, IFocusGroup focuses) {
        final List<ItemStack> stackList = recipes.getInputs();
        for (int i = 0; i < stackList.size(); i++) {
            int x = 5 + (i / 8) * 40;
            int y = 15 + (i % 8) * 20;
            builder.addSlot(RecipeIngredientRole.OUTPUT,x,y).addItemStack(stackList.get(i));


        }
    }



    protected ResourceLocation getTexture() {
        return new ResourceLocation(Constants.MOD_ID, "textures/gui/guivein.png");
    }


}
