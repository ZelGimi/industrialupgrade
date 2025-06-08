package com.denfop;

import net.minecraftforge.common.ForgeConfigSpec;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.event.config.ModConfigEvent;

import java.util.ArrayList;
import java.util.List;

// An example config class. This is not required, but it's a good idea to have one to keep your config organized.
// Demonstrates how to use Forge's config APIs

public class Config {

    public static final List<String> EntityList = new ArrayList<>();
    public static final List<Integer> DimensionList = new ArrayList<>();
    private static final String[] defaultSpawnerList = new String[]{"ExampleMob1", "ExampleMob2", "ExampleMob3 (these examples " +
            "can be deleted)"};


    static List<ForgeConfigSpec.ConfigValue> configValues = new ArrayList<>();
    private static boolean Thaumcraft;



}
