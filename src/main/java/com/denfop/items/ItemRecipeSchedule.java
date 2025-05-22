package com.denfop.items;

import com.denfop.Constants;
import com.denfop.IUCore;
import com.denfop.Localization;
import com.denfop.api.IModelRegister;
import com.denfop.api.Recipes;
import com.denfop.api.recipe.BaseMachineRecipe;
import com.denfop.api.recipe.IBaseRecipe;
import com.denfop.api.recipe.IInput;
import com.denfop.api.recipe.IRecipeInputStack;
import com.denfop.api.recipe.RecipeArrayList;
import com.denfop.api.recipe.RecipeInputStack;
import com.denfop.items.bags.BagsDescription;
import com.denfop.register.Register;
import com.denfop.utils.ModUtils;
import net.minecraft.client.renderer.block.model.ModelBakery;
import net.minecraft.client.renderer.block.model.ModelResourceLocation;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.text.TextFormatting;
import net.minecraft.world.World;
import net.minecraftforge.client.model.ModelLoader;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import org.jetbrains.annotations.Nullable;
import org.lwjgl.input.Keyboard;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;

public class ItemRecipeSchedule extends Item implements IModelRegister {

    private final String name;

    public ItemRecipeSchedule() {
        super();
        this.setMaxStackSize(1);
        this.canRepair = false;
        this.name = "recipe_schedule";
        this.setCreativeTab(IUCore.IUTab);
        Register.registerItem((Item) this, IUCore.getIdentifier(name)).setUnlocalizedName(name);
        IUCore.proxy.addIModelRegister(this);
    }

    @SideOnly(Side.CLIENT)
    public static ModelResourceLocation getModelLocation(String name) {
        final String loc = Constants.MOD_ID +
                ':' +
                name;

        return new ModelResourceLocation(loc, null);
    }

    @SideOnly(Side.CLIENT)
    public static void registerModel(Item item, int meta, String name) {

        ModelLoader.setCustomMeshDefinition(item, stack -> {
            final NBTTagCompound nbt = ModUtils.nbt(stack);
            return getModelLocation(name + (nbt.getBoolean("mode") ? "" : "_black"));

        });
        String[] mode = {"", "_black"};
        for (final String s : mode) {
            ModelBakery.registerItemVariants(item, getModelLocation(name + s));
        }
    }

    public List<ItemStack> getItems(ItemStack stack) {
        List<ItemStack> list = new ArrayList<>();
        final NBTTagCompound nbt = ModUtils.nbt(stack);
        for (int i = 0; i < 9; i++) {
            final NBTTagCompound tag = nbt.getCompoundTag("recipe_" + i);
            ItemStack stack1 = new ItemStack(tag);
            if (!stack1.isEmpty()) {
                list.add(stack1);
            }
        }
        return list;
    }

    public RecipeArrayList<IRecipeInputStack> getInputs(IBaseRecipe baseRecipe, ItemStack stack) {
        final NBTTagCompound nbt = ModUtils.nbt(stack);
        final List<ItemStack> items = this.getItems(stack);
        final List<BaseMachineRecipe> recipe_list = Recipes.recipes.getRecipeList(baseRecipe.getName());
        if (nbt.getBoolean("mode")) {
            if (items.isEmpty()) {
                return new RecipeArrayList<>();
            }
            RecipeArrayList<IRecipeInputStack> recipeArrayList = new RecipeArrayList<>();
            for (BaseMachineRecipe baseMachineRecipe : recipe_list) {
                boolean find = false;
                cycle:
                for (ItemStack output_schedule : items) {
                    for (ItemStack output : baseMachineRecipe.output.items) {
                        if (output.isItemEqual(output_schedule)) {
                            find = true;
                            break cycle;
                        }
                    }
                }
                if (find) {
                    baseMachineRecipe.input.getInputs().forEach(iInputItemStack -> recipeArrayList.add(new RecipeInputStack(
                            iInputItemStack)));
                }
            }
            return recipeArrayList;
        } else {
            if (items.isEmpty()) {
                return Recipes.recipes.getMap_recipe_managers_itemStack(baseRecipe.getName());
            }
            RecipeArrayList<IRecipeInputStack> recipeArrayList = new RecipeArrayList<>();
            for (BaseMachineRecipe baseMachineRecipe : recipe_list) {
                boolean find = false;
                cycle:
                for (ItemStack output_schedule : items) {
                    for (ItemStack output : baseMachineRecipe.output.items) {
                        if (!output.isItemEqual(output_schedule)) {
                            find = true;
                            break cycle;
                        }
                    }
                }
                if (find) {
                    baseMachineRecipe.input.getInputs().forEach(iInputItemStack -> recipeArrayList.add(new RecipeInputStack(
                            iInputItemStack)));
                }
            }
            return recipeArrayList;


        }
    }

    @Override
    @SideOnly(Side.CLIENT)
    public void addInformation(
            final ItemStack stack,
            @Nullable final World worldIn,
            final List<String> tooltip,
            final ITooltipFlag flagIn
    ) {
        if (!Keyboard.isKeyDown(Keyboard.KEY_LSHIFT)) {
            tooltip.add(Localization.translate("press.lshift"));
        } else {
            final NBTTagCompound nbt = ModUtils.nbt(stack);
            List<BagsDescription> list = new ArrayList<>();
            for (int i = 0; i < 9; i++) {
                if (!nbt.hasKey("recipe_" + i)) {
                    continue;
                }
                ItemStack stack1 = new ItemStack(nbt.getCompoundTag("recipe_" + i));
                if (!stack1.isEmpty()) {
                    list.add(new BagsDescription(stack1));
                }
            }
            for (BagsDescription description : list) {
                tooltip.add(TextFormatting.GREEN + description.getStack().getDisplayName());
            }
        }
        super.addInformation(stack, worldIn, tooltip, flagIn);
    }

    @Override
    public void registerModels() {
        registerModel(this, 0, this.name);
    }

    public HashMap<Integer, RecipeArrayList<IRecipeInputStack>> getInputsMap(IBaseRecipe baseRecipe, ItemStack stack) {
        final NBTTagCompound nbt = ModUtils.nbt(stack);
        final List<ItemStack> items = this.getItems(stack);
        final List<BaseMachineRecipe> recipe_list = Recipes.recipes.getRecipeList(baseRecipe.getName());
        if (nbt.getBoolean("mode")) {
            if (items.isEmpty()) {
                return new HashMap<>();
            }
            List<BaseMachineRecipe> recipeArrayList = new LinkedList<>();
            for (BaseMachineRecipe baseMachineRecipe : recipe_list) {
                cycle:
                for (ItemStack output_schedule : items) {
                    for (ItemStack output : baseMachineRecipe.output.items) {
                        if (output.isItemEqual(output_schedule)) {
                            recipeArrayList.add(baseMachineRecipe);
                            break cycle;
                        }
                    }
                }
            }
            HashMap<Integer, RecipeArrayList<IRecipeInputStack>> map = new HashMap<>();
            for (BaseMachineRecipe baseMachineRecipe : recipeArrayList) {
                final IInput input = baseMachineRecipe.input;
                for (int i = 0; i < input.getInputs().size(); i++) {
                    final RecipeArrayList<IRecipeInputStack> list = map.get(i);
                    if (list == null) {
                        RecipeArrayList<IRecipeInputStack> inputStackList = new RecipeArrayList<>();
                        inputStackList.add(new RecipeInputStack(input.getInputs().get(i)));
                        map.put(i, inputStackList);
                    } else {
                        list.add(new RecipeInputStack(input.getInputs().get(i)));
                    }
                }
            }
            return map;
        } else {
            if (items.isEmpty()) {
                HashMap<Integer, RecipeArrayList<IRecipeInputStack>> map = new HashMap<>();
                for (BaseMachineRecipe baseMachineRecipe : recipe_list) {
                    final IInput input = baseMachineRecipe.input;
                    for (int i = 0; i < input.getInputs().size(); i++) {
                        final RecipeArrayList<IRecipeInputStack> list = map.get(i);
                        if (list == null) {
                            RecipeArrayList<IRecipeInputStack> inputStackList = new RecipeArrayList<>();
                            inputStackList.add(new RecipeInputStack(input.getInputs().get(i)));
                            map.put(i, inputStackList);
                        } else {
                            list.add(new RecipeInputStack(input.getInputs().get(i)));
                        }
                    }
                }
                return map;
            }
            HashMap<Integer, RecipeArrayList<IRecipeInputStack>> map = new HashMap<>();
            List<BaseMachineRecipe> recipeArrayList = new LinkedList<>();

            for (BaseMachineRecipe baseMachineRecipe : recipe_list) {
                cycle:
                for (ItemStack output_schedule : items) {
                    for (ItemStack output : baseMachineRecipe.output.items) {
                        if (!output.isItemEqual(output_schedule)) {
                            recipeArrayList.add(baseMachineRecipe);
                            break cycle;
                        }
                    }
                }
            }
            for (BaseMachineRecipe baseMachineRecipe : recipeArrayList) {
                final IInput input = baseMachineRecipe.input;
                for (int i = 0; i < input.getInputs().size(); i++) {
                    final RecipeArrayList<IRecipeInputStack> list = map.get(i);
                    if (list == null) {
                        RecipeArrayList<IRecipeInputStack> inputStackList = new RecipeArrayList<>();
                        inputStackList.add(new RecipeInputStack(input.getInputs().get(i)));
                        map.put(i, inputStackList);
                    } else {
                        list.add(new RecipeInputStack(input.getInputs().get(i)));
                    }
                }
            }
            return map;


        }
    }

}
