package com.denfop.integration.jei;

import com.denfop.Constants;
import com.denfop.screen.ScreenMain;
import mezz.jei.api.helpers.IGuiHelper;
import mezz.jei.api.recipe.RecipeType;
import mezz.jei.api.registration.IRecipeCategoryRegistration;
import mezz.jei.api.registration.IRecipeRegistration;

import java.lang.reflect.Constructor;
import java.lang.reflect.Method;
import java.util.List;

import static com.denfop.integration.jei.JEICompat.informList;

public class JeiInform<C extends ScreenMain & IRecipeCategory<H>, H> {
    public final RecipeType<H> recipeType;
    private final Class<C> categoryClass;
    private final Class<H> handlerClass;
    public Object itemOrBlock;
    private C category;

    public JeiInform(String name, Class<C> categoryClass, Class<H> handlerClass) {
        this.categoryClass = categoryClass;
        this.handlerClass = handlerClass;
        recipeType = RecipeType.create(Constants.MOD_ID, name, handlerClass);
        informList.add(this);
    }

    public void InitHandler(IRecipeRegistration registration) {
        try {
            Method method = handlerClass.getMethod("getRecipes");

            try {
                method.setAccessible(true);
                List<H> recipes = (List<H>) method.invoke(null);
                registration.addRecipes(recipeType, recipes);
            } catch (Exception e) {
                System.out.println(e.getMessage());
            }


        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    public JeiInform<C, H> addObject(Object o) {
        this.itemOrBlock = o;
        return this;
    }

    public Object getItemOrBlock() {
        return itemOrBlock;
    }

    public RecipeType<H> getRecipeType() {
        return recipeType;
    }

    public void initCategory(IRecipeCategoryRegistration registration, IGuiHelper screenHelper) {
        try {
            Constructor<?> constructor = categoryClass.getConstructor(IGuiHelper.class, JeiInform.class);

            category = (C) constructor.newInstance(screenHelper, this);
            Object constructor1 = JeiInterface.class.getConstructors()[0].newInstance(category);
            registration.addRecipeCategories((mezz.jei.api.recipe.category.IRecipeCategory<?>) constructor1);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
