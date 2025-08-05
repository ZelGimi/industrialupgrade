package com.denfop.recipe.universalrecipe;

import com.denfop.IUCore;
import com.denfop.api.space.*;
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
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

import static com.denfop.api.space.SpaceInit.regPlanet;
import static com.denfop.api.space.SpaceInit.regSatellite;

public class PlanetSerializer implements RecipeSerializer<PlanetRecipe> {
    public static final PlanetSerializer INSTANCE = new PlanetSerializer();

    @Override
    public PlanetRecipe fromJson(ResourceLocation id, JsonObject json) {
        String name = json.get("name").getAsString();

        ISystem system  = SpaceNet.instance.getSystem().stream().filter(systems -> systems.getName().equals(json.get("system").getAsString().toLowerCase())).toList().get(0);
        ResourceLocation texture = new ResourceLocation(json.get("texture").getAsString()+ ".png");
        EnumLevels level = EnumLevels.valueOf(json.get("level").getAsString().toUpperCase());
        IStar star = (IStar) SpaceNet.instance.getBodyFromName(json.get("star").getAsString());
        int temperature = json.get("temperature").getAsInt();
        boolean pressure = json.get("pressure").getAsBoolean();
        double distance = json.get("distance").getAsDouble();
        EnumType type = EnumType.valueOf(json.get("type").getAsString().toUpperCase());
        boolean oxygen = json.get("oxygen").getAsBoolean();
        boolean colonies = json.get("colonies").getAsBoolean();
        int angle = json.get("angle").getAsInt();
        double time = json.get("time").getAsDouble();
        double size = json.get("size").getAsDouble();
        double rotation = json.get("rotation").getAsDouble();
        regPlanet.add(() ->  new Planet(name, system, texture, level, star,  temperature, pressure,
                        distance, type, oxygen, colonies, angle, time, size, rotation));
        return new PlanetRecipe(id, "", Collections.emptyList(), "");
    }

    @Override
    public PlanetRecipe fromNetwork(ResourceLocation id, FriendlyByteBuf buf) {

        return new PlanetRecipe(id, "", new ArrayList<>(), "");
    }

    @Override
    public void toNetwork(FriendlyByteBuf buf, PlanetRecipe recipe) {


    }
}
