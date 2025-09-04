package com.denfop.potion;


import com.denfop.config.ModConfig;
import com.denfop.damagesource.IUDamageSource;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.effect.MobEffectCategory;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.entity.LivingEntity;

public class IUPotion extends MobEffect {

    public static IUPotion radiation;
    public static IUPotion frostbite;
    public static IUPotion poison_gas;

    public IUPotion(MobEffectCategory typeIn, int liquidColorIn) {
        super(typeIn, liquidColorIn);
    }

    public boolean isDurationEffectTick(int duration, int amplifier) {
        int rate = 25 >> amplifier;
        return rate == 0 || duration % rate == 0;
    }

    @Override
    public void applyEffectTick(LivingEntity entity, int amplifier) {

        if (ModConfig.COMMON.damageRadiation.get())
            if (this == radiation) {
                entity.hurt(IUDamageSource.radiation, (float) (amplifier / 100) + 0.5F);
            }
        if (this == poison_gas) {
            entity.hurt(IUDamageSource.poison_gas, (float) (amplifier / 100) + 0.5F);
        }
        if (this == poison_gas) {
            entity.hurt(IUDamageSource.poison_gas, (float) (amplifier / 100) + 0.5F);
        }
    }

    public void applyEffect(LivingEntity entityLiving, int radiationDuration) {
        MobEffectInstance effect = new MobEffectInstance(this, radiationDuration);
        entityLiving.addEffect(effect);
    }
}
