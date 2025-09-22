package com.denfop.damagesource;

import net.minecraft.util.DamageSource;

public class IUDamageSource {

    public static final DamageSource current;
    public static final DamageSource radiation;

    public static final DamageSource frostbite;

    public static final DamageSource poison_gas;

    public static final DamageSource bee;

    static {

        current = new DamageSource(("current")).setDamageBypassesArmor();
        bee = new DamageSource(("bee")).setDamageBypassesArmor();
        radiation = new DamageSource(("radiation")).setDamageBypassesArmor().setFireDamage();
        poison_gas = new DamageSource(("poison_gas")).setDamageBypassesArmor().setFireDamage();
        frostbite = new DamageSource(("frostbite")).setDamageBypassesArmor().setFireDamage();

    }


}
