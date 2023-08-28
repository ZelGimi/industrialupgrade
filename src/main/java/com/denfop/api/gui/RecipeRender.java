package com.denfop.api.gui;

import com.denfop.gui.GuiCore;
import com.denfop.utils.CraftManagerUtils;
import com.denfop.utils.ModUtils;
import com.google.common.base.Supplier;
import net.minecraft.client.renderer.RenderHelper;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.IRecipe;
import net.minecraft.item.crafting.Ingredient;
import net.minecraft.util.NonNullList;

public class RecipeRender extends GuiElement<RecipeRender> {

    private final Supplier<ItemStack> itemSupplier;
    private final IRecipe recipe;

    public RecipeRender(GuiCore<?> gui, int x, int y, Supplier<ItemStack> itemSupplier) {
        super(gui, x, y, 116, 58);
        this.itemSupplier = itemSupplier;
        this.recipe = CraftManagerUtils.getRecipe(this.itemSupplier.get());
    }

    public void drawBackground(int mouseX, int mouseY) {
        super.drawBackground(mouseX, mouseY);
        ItemStack stack = this.itemSupplier.get();
        bindCommonTexture();

        this.getGui().drawTexturedModalRect(mouseX + this.x, mouseY + this.y, 140, 202, 116, 54);
        if (!ModUtils.isEmpty(stack)) {
            RenderHelper.enableGUIStandardItemLighting();
            this.gui.drawItemStack(235 - 140 + this.x, 221 - 202 + this.y, stack);
            RenderHelper.disableStandardItemLighting();
        }
        NonNullList<Ingredient> ingredients = recipe.getIngredients();
        for (int x = 0; x < 3; ++x) {
            for (int y = 0; y < 3; ++y) {
                int i = y * 3 + x;
                if (ingredients.size() > i) {
                    Ingredient ingredient = ingredients.get(i);
                    final NonNullList<ItemStack> items = NonNullList.from(ItemStack.EMPTY, ingredient.getMatchingStacks());
                    int perm = (int) (System.currentTimeMillis() / 1000L % (long) items.size());
                    stack = items.get(perm);
                    if (!ModUtils.isEmpty(stack)) {
                        RenderHelper.enableGUIStandardItemLighting();
                        this.gui.drawItemStack(this.x + 1 + x * 18, this.y + 1 + y * 18, stack);
                        RenderHelper.disableStandardItemLighting();
                    }
                }
            }
        }
    }

    public void drawForeground(int mouseX, int mouseY) {

        ItemStack stack = this.itemSupplier.get();
        if (!ModUtils.isEmpty(stack)) {
            if (mouseX >= 235 - 140 + this.x && mouseX < 235 - 140 + this.x + 18 && mouseY >= 221 - 202 + this.y && mouseY < 221 - 202 + this.y + 18) {
                this.gui.drawTooltip(mouseX, mouseY, stack);
            }
        }


        NonNullList<Ingredient> ingredients = recipe.getIngredients();
        for (int x = 0; x < 3; ++x) {
            for (int y = 0; y < 3; ++y) {
                int i = y * 3 + x;
                if (ingredients.size() > i) {
                    Ingredient ingredient = ingredients.get(i);
                    final NonNullList<ItemStack> items = NonNullList.from(ItemStack.EMPTY, ingredient.getMatchingStacks());
                    int perm = (int) (System.currentTimeMillis() / 1000L % (long) items.size());
                    stack = items.get(perm);
                    if (!ModUtils.isEmpty(stack)) {
                        RenderHelper.enableGUIStandardItemLighting();
                        if (mouseX >= this.x + 1 + x * 18 && mouseX < this.x + 1 + (x + 1) * 18 && mouseY >= this.y + 1 + y * 18 && mouseY < this.y + 1 + (y + 1) * 18) {
                            this.gui.drawTooltip(mouseX, mouseY, stack);
                        }
                        RenderHelper.disableStandardItemLighting();
                    }
                }
            }
        }
    }

}
