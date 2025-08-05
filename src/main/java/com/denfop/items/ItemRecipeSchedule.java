package com.denfop.items;

import com.denfop.IItemTab;
import com.denfop.IUCore;
import com.denfop.api.Recipes;
import com.denfop.api.recipe.*;
import com.denfop.items.bags.BagsDescription;
import com.denfop.utils.Keyboard;
import com.denfop.utils.ModUtils;
import net.minecraft.ChatFormatting;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.level.Level;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

import javax.annotation.Nullable;
import java.util.ArrayList;
import java.util.List;

public class ItemRecipeSchedule extends Item implements IItemTab, IProperties {

    private final String name;

    public ItemRecipeSchedule() {
        super(new Properties().stacksTo(1).setNoRepair());
        this.name = "recipe_schedule";
        IUCore.proxy.addProperties(this);
    }

    @Override
    public String[] properties() {
        return new String[]{"mode"};
    }


    @OnlyIn(Dist.CLIENT)
    @Override
    public float getItemProperty(ItemStack itemStack, ClientLevel level, LivingEntity entity, int p174679, String property) {
        return ModUtils.nbt(itemStack).getBoolean("mode") ? 1 : 0;
    }

    @Override
    public CreativeModeTab getItemCategory() {
        return IUCore.ItemTab;
    }
    public List<ItemStack> getItems(ItemStack stack) {
        List<ItemStack> list = new ArrayList<>();
        final CompoundTag nbt = ModUtils.nbt(stack);
        for (int i = 0; i < 9; i++) {
            final CompoundTag tag = nbt.getCompound("recipe_" + i);
            ItemStack stack1 = ItemStack.of(tag);
            if (!stack1.isEmpty()) {
                list.add(stack1);
            }
        }
        return list;
    }

    public RecipeArrayList<IRecipeInputStack> getInputs(IBaseRecipe baseRecipe, ItemStack stack) {
        final CompoundTag nbt = ModUtils.nbt(stack);
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
                        if (output.is(output_schedule.getItem())) {
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
                        if (!output.is(output_schedule.getItem())) {
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
    public void appendHoverText(
            ItemStack stack,
            @Nullable Level world,
            List<Component> tooltip,
            TooltipFlag flag
    ) {
        if (!Keyboard.isKeyDown(Keyboard.KEY_LSHIFT)) {
            tooltip.add(Component.translatable("press.lshift"));
        } else {
            CompoundTag nbt = stack.getOrCreateTag();
            List<BagsDescription> list = new ArrayList<>();
            for (int i = 0; i < 9; i++) {
                String key = "recipe_" + i;
                if (!nbt.contains(key)) {
                    continue;
                }
                ItemStack stack1 = ItemStack.of(nbt.getCompound(key));
                if (!stack1.isEmpty()) {
                    list.add(new BagsDescription(stack1));
                }
            }
            for (BagsDescription description : list) {
                tooltip.add(Component.literal(ChatFormatting.GREEN + description.getStack().getHoverName().getString()));
            }
        }
    }



}
