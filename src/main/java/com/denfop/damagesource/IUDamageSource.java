package com.denfop.damagesource;

import net.minecraft.util.DamageSource;

public class IUDamageSource {

    public static final DamageSource current;
    public static final DamageSource radiation;

    public static final DamageSource frostbite;

    static {

        current = new DamageSource(("current")).setDamageBypassesArmor();
        radiation = new DamageSource(("radiation")).setDamageBypassesArmor().setFireDamage();
        frostbite = new DamageSource(("frostbite")).setDamageBypassesArmor().setFireDamage();

    }


}
