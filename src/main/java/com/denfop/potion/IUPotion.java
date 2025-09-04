package com.denfop.potion;


import com.denfop.config.ModConfig;
import com.denfop.damagesource.IUDamageSource;
import net.minecraft.core.Holder;
import net.minecraft.core.Registry;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.damagesource.DamageType;
import net.minecraft.world.damagesource.DamageTypes;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.effect.MobEffectCategory;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.entity.LivingEntity;
import net.neoforged.neoforge.registries.DeferredHolder;

public class IUPotion extends MobEffect {

    public static IUPotion radiation;
    public static IUPotion frostbite;
    public static IUPotion poison_gas;
    public static DeferredHolder<MobEffect, IUPotion> rad;
    public static DeferredHolder<MobEffect, IUPotion> frost;
    public static DeferredHolder<MobEffect, IUPotion> poison;

    public IUPotion(MobEffectCategory typeIn, int liquidColorIn) {
        super(typeIn, liquidColorIn);

    }

    @Override
    public boolean shouldApplyEffectTickThisTick(int duration, int amplifier) {
        int rate = 25 >> amplifier;
        return rate == 0 || duration % rate == 0;
    }


    @Override
    public boolean applyEffectTick(LivingEntity entity, int amplifier) {
        if (IUDamageSource.radiation == null || IUDamageSource.frostbite == null || IUDamageSource.poison_gas == null) {
            IUDamageSource.initDamage(entity.registryAccess());
        }
        if (this == radiation) {
            if (ModConfig.COMMON.damageRadiation.get())
                if (entity.getHealth() > 1.0F) {
                    Registry<DamageType> dTypeReg = entity.damageSources().damageTypes;
                    Holder.Reference<DamageType> dType = dTypeReg.getHolder(com.denfop.datagen.DamageTypes.radiationObject).orElse(dTypeReg.getHolderOrThrow(DamageTypes.MAGIC));
                    entity.hurt(new DamageSource(dType), (float) (amplifier / 100) + 0.5F);
                }
            return true;
        }
        if (this == frostbite) {
            Registry<DamageType> dTypeReg = entity.damageSources().damageTypes;
            Holder.Reference<DamageType> dType = dTypeReg.getHolder(com.denfop.datagen.DamageTypes.frostbiteObject).orElse(dTypeReg.getHolderOrThrow(DamageTypes.MAGIC));

            entity.hurt(new DamageSource(dType), (float) (amplifier / 100) + 0.5F);
            return true;
        }
        if (this == poison_gas) {
            Registry<DamageType> dTypeReg = entity.damageSources().damageTypes;
            Holder.Reference<DamageType> dType = dTypeReg.getHolder(com.denfop.datagen.DamageTypes.poison_gasObject).orElse(dTypeReg.getHolderOrThrow(DamageTypes.MAGIC));

            entity.hurt(new DamageSource(dType), (float) (amplifier / 100) + 0.5F);
            return true;
        }
        return false;
    }

    public void applyEffect(LivingEntity entityLiving, int radiationDuration) {
        MobEffectInstance effect = null;
        if (this == radiation) {
            effect = new MobEffectInstance(rad, radiationDuration);
        }
        if (this == poison_gas) {
            effect = new MobEffectInstance(poison, radiationDuration);
        }
        if (this == frostbite) {
            effect = new MobEffectInstance(frost, radiationDuration);
        }
        assert effect != null;
        entityLiving.addEffect(effect);
    }
}
