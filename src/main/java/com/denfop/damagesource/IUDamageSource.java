package com.denfop.damagesource;


import com.denfop.datagen.DamageTypes;
import net.minecraft.core.Registry;
import net.minecraft.core.RegistryAccess;
import net.minecraft.core.registries.Registries;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.damagesource.DamageType;

public class IUDamageSource {

    public static  DamageSource current;
    public static  DamageSource radiation;

    public static  DamageSource frostbite;
    public static  DamageSource poison_gas;

    public static  DamageSource bee;





    public static void initDamage(RegistryAccess registryAccess) {
        Registry<DamageType> registry = registryAccess.registryOrThrow(Registries.DAMAGE_TYPE);
        current =  new DamageSource(registry.getHolderOrThrow(DamageTypes.currentObject));
        radiation =  new DamageSource(registry.getHolderOrThrow(DamageTypes.radiationObject));
        frostbite =  new DamageSource(registry.getHolderOrThrow(DamageTypes.frostbiteObject));
        poison_gas =  new DamageSource(registry.getHolderOrThrow(DamageTypes.poison_gasObject));
        bee =  new DamageSource(registry.getHolderOrThrow(DamageTypes.beeObject));
    }
}
