package com.denfop.datagen.loader;

import com.denfop.utils.ModUtils;
import com.denfop.world.vein.ChanceOre;
import com.denfop.world.vein.TypeVein;
import com.denfop.world.vein.VeinType;
import com.google.gson.*;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.packs.resources.ResourceManager;
import net.minecraft.server.packs.resources.SimpleJsonResourceReloadListener;
import net.minecraft.util.GsonHelper;
import net.minecraft.util.profiling.ProfilerFiller;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraftforge.event.AddReloadListenerEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

import java.util.*;

import static com.denfop.items.ItemVeinSensor.dataColors;
import static com.denfop.world.WorldBaseGen.*;

@Mod.EventBusSubscriber(modid = "industrialupgrade")
public class VeinDataLoader extends SimpleJsonResourceReloadListener {
    private static final Gson GSON = new Gson();


    public static  Map<ResourceLocation,VeinType> VEIN_DATA = new HashMap<>();

    public VeinDataLoader() {
        super(GSON, "vein");
    }

    @Override
    protected void apply(Map<ResourceLocation, JsonElement> map, ResourceManager pResourceManager, ProfilerFiller pProfiler) {
        for (Map.Entry<ResourceLocation, JsonElement> entry : map.entrySet()) {
            try {
                if (VEIN_DATA.containsKey(entry.getKey()))
                    continue;
                JsonObject json = entry.getValue().getAsJsonObject();


                List<ChanceOre> inputs = new ArrayList<>();
                JsonArray inputsArray = GsonHelper.getAsJsonArray(json, "inputs");
                for (JsonElement e : inputsArray) {
                    JsonObject obj = e.getAsJsonObject();
                    String id = GsonHelper.getAsString(obj, "id");
                    int chance = GsonHelper.getAsInt(obj, "chance", 100);
                    inputs.add(new ChanceOre(BuiltInRegistries.BLOCK.get(new ResourceLocation(id)).defaultBlockState(), chance,0));
                }

                JsonArray colorsArray = GsonHelper.getAsJsonArray(json, "colors");

                for (JsonElement e : colorsArray) {
                    JsonObject obj = e.getAsJsonObject();
                    String id = GsonHelper.getAsString(obj, "id");
                    int color = GsonHelper.getAsInt(obj, "color", ModUtils.convertRGBAcolorToInt(255,255,255));
                    dataColors.put(BuiltInRegistries.BLOCK.get(new ResourceLocation(id)).defaultBlockState(),color);
                }

                List<BlockState> deposit = new ArrayList<>();
                JsonArray depositArray = GsonHelper.getAsJsonArray(json, "deposit");
                for (JsonElement e : depositArray) {
                    JsonObject obj = e.getAsJsonObject();
                    String id = GsonHelper.getAsString(obj, "id");
                    deposit.add(BuiltInRegistries.BLOCK.get(new ResourceLocation(id)).defaultBlockState());
                }
                VeinType type = new VeinType(deposit.get(0), TypeVein.SMALL, inputs);
                veinTypes.add(type);
                VEIN_DATA.put(entry.getKey(), type);

            } catch (Exception ex) {
                System.err.println("[VeinLoader] Error parsing file " + entry.getKey() + ": " + ex.getMessage());
            }
        }
        for (VeinType veinType : VEIN_DATA.values()){
            for (ChanceOre chanceOre : veinType.getOres()){
                BlockState state = chanceOre.getBlock();
                if (!idToblockStateMap.containsKey(state.getBlock())){
                    idToblockStateMap.put(state.getBlock(),id);
                    blockStateMap.put(id,state);
                    id++;
                }
            }
        }
        System.out.println("[VeinLoader] Vein files loaded: " + VEIN_DATA.size());

    }





    @SubscribeEvent
    public static void onAddReloadListeners(AddReloadListenerEvent event) {
        event.addListener(new VeinDataLoader());
    }
}