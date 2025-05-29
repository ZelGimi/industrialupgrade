package com.denfop.datagen;

import com.denfop.Constants;
import net.minecraft.core.registries.Registries;
import net.minecraft.data.worldgen.BootstapContext;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.damagesource.DamageEffects;
import net.minecraft.world.damagesource.DamageType;

public class DamageTypes {
    public static final ResourceKey<DamageType> currentObject = registerKey("current");

    public static final ResourceKey<DamageType> radiationObject = registerKey("radiation");
    public static final ResourceKey<DamageType> frostbiteObject = registerKey("frostbite");
    public static final ResourceKey<DamageType> poison_gasObject = registerKey("poison_gas");
    public static final ResourceKey<DamageType> beeObject = registerKey("bee");

    private static ResourceKey<DamageType> registerKey(String name) {
        return ResourceKey.create(Registries.DAMAGE_TYPE, new ResourceLocation(Constants.MOD_ID, name));
    }

    public static void bootstrap(BootstapContext<DamageType> pContext) {
        pContext.register(currentObject, new DamageType("current", 0.1F, DamageEffects.HURT));
        pContext.register(radiationObject, new DamageType("radiation", 0.1F, DamageEffects.HURT));
        pContext.register(frostbiteObject, new DamageType("frostbite", 0.1F, DamageEffects.HURT));
        pContext.register(poison_gasObject, new DamageType("poison_gas", 0.1F, DamageEffects.HURT));
        pContext.register(beeObject, new DamageType("bee", 0.1F, DamageEffects.HURT));

    }
}
