package com.denfop.integration.jei.reactorschemes.fluid.simple;

import com.denfop.Constants;
import com.denfop.api.widget.ScreenWidget;
import com.denfop.api.widget.WidgetDefault;
import com.denfop.blockentity.mechanism.multimechanism.simple.BlockEntityRecycler;
import com.denfop.blocks.mechanism.BlockSimpleMachineEntity;
import com.denfop.componets.ComponentRenderInventory;
import com.denfop.componets.EnumTypeComponentSlot;
import com.denfop.containermenu.ContainerMenuMultiMachine;
import com.denfop.integration.jei.IRecipeCategory;
import com.denfop.integration.jei.JeiInform;
import com.denfop.screen.ScreenMain;
import com.denfop.utils.Localization;
import com.denfop.utils.ModUtils;
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
import net.minecraft.util.Tuple;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;

import javax.annotation.Nonnull;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ReactorSchemesFluidSimpleCategory extends ScreenMain implements IRecipeCategory<ReactorSchemesFluidSimpleHandler> {

    private final IGuiHelper guiHelper;
    private final ContainerMenuMultiMachine container1;
    JeiInform jeiInform;
    ResourceLocation background = new ResourceLocation(Constants.MOD_ID, "textures/gui/guifluidreactor" +
            ".png");
    private IDrawableStatic bg;
    private int progress = 0;
    private int energy = 0;

    public ReactorSchemesFluidSimpleCategory(
            IGuiHelper guiHelper, JeiInform jeiInform
    ) {
        super(new ContainerMenuMultiMachine(Minecraft.getInstance().player,
                ((BlockEntityRecycler) BlockSimpleMachineEntity.recycler_iu.getDummyTe()), 1, true
        ));
        this.jeiInform = jeiInform;
        this.title = net.minecraft.network.chat.Component.literal(getTitles());
        this.guiHelper = guiHelper;
        bg = guiHelper.createDrawable(new ResourceLocation(Constants.MOD_ID, "textures/gui/guifluidreactor" +
                        ".png"), 37, 14, 143 - 37,
                122
        );
        this.componentList.clear();
        this.slots = new ScreenWidget(this, 3, 3, getComponent(),
                new WidgetDefault<>(new ComponentRenderInventory(EnumTypeComponentSlot.SLOTS__JEI))
        );
        this.container1 = (ContainerMenuMultiMachine) this.getContainer();
        this.componentList.clear();
    }

    public static ItemStack[] convertPatternToLayout(List<String> pattern, List<Tuple<Character, Item>> keys, int width, int height) {
        ItemStack[] layout = new ItemStack[width * height];

        Map<Character, ItemStack> keyMap = new HashMap<>();
        for (Tuple<Character, Item> tuple : keys) {
            keyMap.put(tuple.getA(), new ItemStack(tuple.getB()));
        }

        for (int y = 0; y < pattern.size(); y++) {
            String row = pattern.get(y);
            for (int x = 0; x < row.length(); x++) {
                char c = row.charAt(x);
                int index = x + y * width;
                if (c == ' ' || !keyMap.containsKey(c)) {
                    layout[index] = ItemStack.EMPTY;
                } else {
                    layout[index] = keyMap.get(c).copy();
                }
            }
        }


        for (int y = pattern.size(); y < height; y++) {
            for (int x = 0; x < width; x++) {
                layout[x + y * width] = ItemStack.EMPTY;
            }
        }

        return layout;
    }

    @Override
    public RecipeType<ReactorSchemesFluidSimpleHandler> getRecipeType() {
        return jeiInform.recipeType;
    }

    @Nonnull
    @Override
    public String getTitles() {
        return "Reactor Schemes";
    }

    @Nonnull
    @Override
    public IDrawable getBackground() {
        return bg;
    }

    @Override
    public void draw(ReactorSchemesFluidSimpleHandler recipe, IRecipeSlotsView recipeSlotsView, GuiGraphics stack, double mouseX, double mouseY) {
        String nameReactor1 = Localization.translate("multiblock." + recipe.getReactors().getNameReactor().toLowerCase());
        draw(stack, nameReactor1, -5, -20, ModUtils.convertRGBAcolorToInt(0, 0, 0));
    }

    @Override
    public void setRecipe(IRecipeLayoutBuilder builder, ReactorSchemesFluidSimpleHandler recipe, IFocusGroup focuses) {

        final List<ItemStack> inputs = Arrays.asList(convertPatternToLayout(recipe.getPattern(), recipe.getInput(), recipe.getReactors().getWidth(), recipe.getReactors().getHeight()));
        int i = 0;
        for (; i < inputs.size(); i++) {
            if (!inputs.get(i).isEmpty())
                builder.addSlot(RecipeIngredientRole.INPUT, 60 - 37 + (i % recipe.getReactors().getWidth()) * 18, 47 - 14 + (i / recipe.getReactors().getHeight()) * 18).addItemStack(inputs.get(i));

        }
    }

    protected ResourceLocation getTexture() {
        return background;
    }


}
