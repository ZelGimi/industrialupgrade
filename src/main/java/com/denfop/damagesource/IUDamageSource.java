package com.denfop.damagesource;


import net.minecraft.world.damagesource.DamageSource;

public class IUDamageSource {

    public static final DamageSource current;
    public static final DamageSource radiation;

    public static final DamageSource frostbite;
    public static final DamageSource poison_gas;

    public static final DamageSource bee;

    static {

        current = new DamageSource(("current")).bypassArmor();
        radiation = new DamageSource(("radiation")).bypassArmor().setIsFire();
        frostbite = new DamageSource(("frostbite")).bypassArmor().setIsFire();
        poison_gas = new DamageSource(("poison_gas")).bypassArmor().setIsFire();
        bee = new DamageSource(("bee")).bypassArmor().setIsFire();

    }


}
