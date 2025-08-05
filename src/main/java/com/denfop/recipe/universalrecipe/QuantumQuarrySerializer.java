package com.denfop.recipe.universalrecipe;

import com.denfop.IUCore;
import com.denfop.recipe.InputOreDict;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.GsonHelper;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.RecipeSerializer;
import net.minecraftforge.registries.ForgeRegistries;

import java.util.ArrayList;
import java.util.List;

public class QuantumQuarrySerializer implements RecipeSerializer<QuantumQuarryRecipe> {
    public static final QuantumQuarrySerializer INSTANCE = new QuantumQuarrySerializer();

    @Override
    public QuantumQuarryRecipe fromJson(ResourceLocation id, JsonObject json) {
        String recipeType = GsonHelper.getAsString(json, "recipe_type");
        String recipeOperation = GsonHelper.getAsString(json, "recipeOperation");

        List<ItemStack> input = new ArrayList<>();
        if (GsonHelper.isValidNode(json, "inputs")) {
            JsonArray inArray = GsonHelper.getAsJsonArray(json, "inputs");
            for (JsonElement el : inArray) {
                JsonObject obj = el.getAsJsonObject();
                String type = GsonHelper.getAsString(obj, "type");
                String itemId = GsonHelper.getAsString(obj, "id");
                int amount = GsonHelper.getAsInt(obj, "amount", 1);

                switch (type) {
                    case "item":
                        ItemStack stack = new ItemStack(
                                ForgeRegistries.ITEMS.getValue(new ResourceLocation(itemId)),
                                amount
                        );
                        input.add(stack);
                        break;
                    case "tag":
                        input.add(new InputOreDict(itemId, amount).getInputs().get(0));
                        break;

                    default:
                        throw new IllegalArgumentException("Unknown input type: " + type);
                }
            }
        }
        switch (recipeOperation) {
            case "default":
                switch (recipeOperation) {
                    case "add":
                        IUCore.list_adding.addAll(input);
                        break;
                    case "remove":
                        IUCore.list_removing.addAll(input);
                        break;
                }
                break;
            case "furnace":
                switch (recipeOperation) {
                    case "add":
                        IUCore.list_furnace_adding.addAll(input);
                        break;
                    case "remove":
                        IUCore.list_furnace_removing.addAll(input);
                        break;
                }
                break;
            case "macerator":
                switch (recipeOperation) {
                    case "add":
                        IUCore.list_crushed_adding.addAll(input);
                        break;
                    case "remove":
                        IUCore.list_crushed_removing.addAll(input);
                        break;
                }
                break;
            case "comb_macerator":
                switch (recipeOperation) {
                    case "add":
                        IUCore.list_comb_crushed_adding.addAll(input);
                        break;
                    case "remove":
                        IUCore.list_comb_crushed_removing.addAll(input);
                        break;
                }
                break;
        }

        return new QuantumQuarryRecipe(id, recipeType, input, recipeOperation);
    }

    @Override
    public QuantumQuarryRecipe fromNetwork(ResourceLocation id, FriendlyByteBuf buf) {

        return new QuantumQuarryRecipe(id, "", new ArrayList<>(), "");
    }

    @Override
    public void toNetwork(FriendlyByteBuf buf, QuantumQuarryRecipe recipe) {


    }
}
