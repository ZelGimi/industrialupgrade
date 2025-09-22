package com.denfop.api.gui;

import com.denfop.api.Recipes;
import com.denfop.api.recipe.BaseMachineRecipe;
import com.denfop.gui.GuiCore;
import com.denfop.recipe.IInputItemStack;
import com.denfop.utils.ModUtils;
import com.google.common.base.Supplier;
import net.minecraft.client.renderer.RenderHelper;
import net.minecraft.item.ItemStack;

import java.util.ArrayList;
import java.util.List;

public class DefaultRecipeElement extends GuiElement {

    private final Supplier<ItemStack> itemSupplier;
    private int texY;
    private int texX;
    private List<IInputItemStack> inputs = new ArrayList<>();
    private int size = 0;
    private BaseMachineRecipe recipe;

    public DefaultRecipeElement(GuiCore<?> gui, int x, int y, Supplier<ItemStack> itemSupplier, String nameRecipe) {
        super(gui, x, y, 120, 20);
        this.itemSupplier = itemSupplier;
        final List<BaseMachineRecipe> recipes = Recipes.recipes.getRecipeList(nameRecipe);
        for (BaseMachineRecipe recipe : recipes) {
            for (ItemStack output_stack : recipe.output.items) {
                if (this.itemSupplier.get().isItemEqual(output_stack)) {
                    this.recipe = recipe;
                    break;
                }
            }


        }
        if (this.recipe != null) {
            this.inputs = recipe.input.getInputs();
            this.size = this.inputs.size();
        }
        this.texX = 0;
        this.texY = 0;
        switch (this.size) {
            case 2:
                this.texX = 1;
                this.texY = 146;
                break;
            case 3:
                this.texX = 1;
                this.texY = 168;
                break;
            case 4:
                this.texX = 1;
                this.texY = 190;
                break;
            case 1:
                this.texX = 1;
                this.texY = 212;
                break;
        }
    }

    public DefaultRecipeElement(
            GuiCore<?> gui,
            int x,
            int y,
            Supplier<ItemStack> itemSupplier,
            String nameRecipe,
            boolean input
    ) {
        super(gui, x, y, 120, 20);
        this.itemSupplier = itemSupplier;
        recipe = Recipes.recipes.getRecipeOutput(nameRecipe, false, itemSupplier.get());

        if (this.recipe != null) {
            this.inputs = recipe.input.getInputs();
            this.size = this.inputs.size();
        }
        this.texX = 0;
        this.texY = 0;
        switch (this.size) {
            case 2:
                this.texX = 1;
                this.texY = 146;
                break;
            case 3:
                this.texX = 1;
                this.texY = 168;
                break;
            case 4:
                this.texX = 1;
                this.texY = 190;
                break;
            case 1:
                this.texX = 1;
                this.texY = 212;
                break;
        }
    }

    public void drawBackground(int mouseX, int mouseY) {
        super.drawBackground(mouseX, mouseY);
        ItemStack stack;
        bindCommonTexture1();

        this.getGui().drawTexturedModalRect(mouseX + this.x, mouseY + this.y, texX, texY, this.width, this.height);
        int x = 0;
        for (int i = 0; i < this.size; i++) {
            final IInputItemStack ingredient = inputs.get(i);
            final List<ItemStack> items = ingredient.getInputs();
            int perm = (int) (System.currentTimeMillis() / 1000L % (long) items.size());
            stack = items.get(perm);
            x = this.x + 1 + 19 * i;
            RenderHelper.enableGUIStandardItemLighting();
            this.gui.drawItemStack(x, this.y + 1, stack);
            RenderHelper.disableStandardItemLighting();
        }
        x += 26 + 18;
        for (int i = 0; i < this.recipe.output.items.size(); i++) {
            stack = this.recipe.getOutput().items.get(i);
            if (!ModUtils.isEmpty(stack)) {
                RenderHelper.enableGUIStandardItemLighting();
                x = x + 19 * i;
                this.gui.drawItemStack(x, this.y + 1, stack);
                RenderHelper.disableStandardItemLighting();
            }
        }
    }

    public void drawForeground(int mouseX, int mouseY) {

        ItemStack stack = this.itemSupplier.get();

        int x = 0;
        for (int i = 0; i < this.size; i++) {
            final IInputItemStack ingredient = inputs.get(i);
            final List<ItemStack> items = ingredient.getInputs();
            int perm = (int) (System.currentTimeMillis() / 1000L % (long) items.size());
            stack = items.get(perm);
            x = this.x + 1 + 19 * i;
            if (mouseX >= x && mouseX < x + 19 && mouseY > y + 1 && mouseY < y + 20) {
                this.gui.drawTooltip(mouseX, mouseY, stack);
            }
        }
        x += 26 + 18;
        for (int i = 0; i < this.recipe.output.items.size(); i++) {
            stack = this.recipe.getOutput().items.get(i);
            x = x + 19 * i;
            if (mouseX >= x && mouseX < x + 19 && mouseY > y + 1 && mouseY < y + 20) {
                this.gui.drawTooltip(mouseX, mouseY, stack);
            }
        }

    }

}
