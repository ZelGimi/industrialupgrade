package com.denfop.items;

import com.denfop.IItemTab;
import com.denfop.IUCore;
import com.denfop.api.Recipes;
import com.denfop.api.recipe.*;
import com.denfop.datacomponent.DataComponentsInit;
import com.denfop.utils.Keyboard;
import net.minecraft.ChatFormatting;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.core.HolderLookup;
import net.minecraft.network.chat.Component;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.api.distmarker.OnlyIn;

import javax.annotation.Nullable;
import java.util.Collections;
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
        return itemStack.getOrDefault(DataComponentsInit.BLACK_LIST, false) ? 1 : 0;
    }

    public List<ItemStack> getItems(ItemStack stack) {

        return stack.getOrDefault(DataComponentsInit.LIST_STACK, Collections.emptyList());
    }


    @Override
    public void appendHoverText(
            ItemStack stack,
            @Nullable TooltipContext world,
            List<Component> tooltip,
            TooltipFlag flag
    ) {
        if (!Keyboard.isKeyDown(Keyboard.KEY_LSHIFT)) {
            tooltip.add(Component.translatable("press.lshift"));
        } else {

            for (ItemStack description : getItems(stack)) {
                tooltip.add(Component.literal(ChatFormatting.GREEN + description.getHoverName().getString()));
            }
        }
    }

    public RecipeArrayList<IRecipeInputStack> getInputs(HolderLookup.Provider provider, IBaseRecipe baseRecipe, ItemStack stack) {
        return getInputs(baseRecipe, stack);
    }

    public RecipeArrayList<IRecipeInputStack> getInputs(IBaseRecipe baseRecipe, ItemStack stack) {
        final List<ItemStack> items = this.getItems(stack);
        final List<BaseMachineRecipe> recipe_list = Recipes.recipes.getRecipeList(baseRecipe.getName());
        if (stack.getOrDefault(DataComponentsInit.BLACK_LIST, false)) {
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
    public CreativeModeTab getItemCategory() {
        return IUCore.ItemTab;
    }

  /*  @Override
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
    }*/


}
