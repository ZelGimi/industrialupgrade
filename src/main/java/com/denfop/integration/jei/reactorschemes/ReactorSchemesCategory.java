package com.denfop.integration.jei.reactorschemes;

import com.denfop.Constants;
import com.denfop.IUItem;
import com.denfop.Localization;
import com.denfop.api.gui.Component;
import com.denfop.api.gui.EnumTypeComponent;
import com.denfop.api.gui.GuiComponent;
import com.denfop.api.recipe.InvSlotMultiRecipes;
import com.denfop.api.recipe.InvSlotOutput;
import com.denfop.blocks.mechanism.BlockSimpleMachine;
import com.denfop.componets.ComponentProcessRender;
import com.denfop.componets.ComponentRenderInventory;
import com.denfop.componets.EnumTypeComponentSlot;
import com.denfop.container.ContainerMultiMachine;
import com.denfop.container.SlotInvSlot;
import com.denfop.gui.GuiIU;
import com.denfop.integration.jei.IRecipeCategory;
import com.denfop.integration.jei.JeiInform;
import com.denfop.recipes.ItemStackHelper;
import com.denfop.tiles.mechanism.multimechanism.simple.TileRecycler;
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
import net.minecraft.world.inventory.Slot;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;

import javax.annotation.Nonnull;
import java.util.*;

public class ReactorSchemesCategory extends GuiIU implements IRecipeCategory<ReactorSchemesHandler> {

    private final IGuiHelper guiHelper;
    private IDrawableStatic bg;
    private final ContainerMultiMachine container1;
    private int progress = 0;
    private int energy = 0;
    JeiInform jeiInform;

    public ReactorSchemesCategory(
            IGuiHelper guiHelper, JeiInform jeiInform
    ) {
        super(new ContainerMultiMachine(Minecraft.getInstance().player,
                ((TileRecycler) BlockSimpleMachine.recycler_iu.getDummyTe()), 1, true
        ));
        this.jeiInform = jeiInform;
        this.title = net.minecraft.network.chat.Component.literal(getTitles());
        this.guiHelper=guiHelper;
        bg = guiHelper.createDrawable(new ResourceLocation(Constants.MOD_ID, "textures/gui/guimachine" +
                        ".png"), 3, 3, 140,
                80
        );
        this.componentList.clear();
        this.slots = new GuiComponent(this, 3, 3, getComponent(),
                new Component<>(new ComponentRenderInventory(EnumTypeComponentSlot.SLOTS__JEI))
        );
        this.container1 = (ContainerMultiMachine) this.getContainer();
        this.componentList.clear();
    }

    @Override
    public RecipeType<ReactorSchemesHandler> getRecipeType() {
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
    public void draw(ReactorSchemesHandler recipe, IRecipeSlotsView recipeSlotsView, GuiGraphics stack, double mouseX, double mouseY) {
        IDrawableStatic bg1 = null;
       switch (recipe.getReactors()){
           case FS -> {
               bg = guiHelper.createDrawable(new ResourceLocation(Constants.MOD_ID, "textures/gui/guifluidreactor" +
                               ".png"), 37, 14, 143 - 37,
                       122
               );
               break;
           }
       }
       if (bg != null)
           bg.draw(stack,0,0);
        String nameReactor1 = Localization.translate("multiblock." + recipe.getReactors().getNameReactor().toLowerCase());
        draw(stack,nameReactor1,-5,-20, ModUtils.convertRGBAcolorToInt(0,0,0));
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
    public void setRecipe(IRecipeLayoutBuilder builder, ReactorSchemesHandler recipe, IFocusGroup focuses) {

        final List<ItemStack> inputs =Arrays.asList(convertPatternToLayout(recipe.getPattern(),recipe.getInput(),recipe.getReactors().getWidth(),recipe.getReactors().getHeight()));
        switch (recipe.getReactors()){
            case FS -> {
                int i = 0;
                for (; i < inputs.size(); i++) {
                    if (!inputs.get(i).isEmpty())
                    builder.addSlot(RecipeIngredientRole.INPUT,60-37 + (i%recipe.getReactors().getWidth())*18 , 47-14+ (i/recipe.getReactors().getHeight())*18).addItemStack(inputs.get(i));

                }
            }

        }
      }

    ResourceLocation   background = new ResourceLocation(Constants.MOD_ID, "textures/gui/guifluidreactor" +
            ".png");
    protected ResourceLocation getTexture() {
        return background;
    }


}
