package com.denfop.potion;


import com.denfop.config.ModConfig;
import com.denfop.damagesource.IUDamageSource;
import net.minecraft.core.Holder;
import net.minecraft.core.Registry;
import net.minecraft.core.registries.Registries;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.damagesource.DamageType;
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

        if (IUDamageSource.radiation == null || IUDamageSource.frostbite == null || IUDamageSource.poison_gas == null) {
            IUDamageSource.initDamage(entity.level().registryAccess());
        }
        if (ModConfig.COMMON.damageRadiation.get())
            if (this == radiation) {
                Registry<DamageType> dTypeReg = entity.level().registryAccess().registryOrThrow(Registries.DAMAGE_TYPE);
                Holder.Reference<DamageType> dType = dTypeReg.getHolder(com.denfop.datagen.DamageTypes.radiationObject).orElse(dTypeReg.getHolderOrThrow(net.minecraft.world.damagesource.DamageTypes.MAGIC));
                entity.hurt(new DamageSource(dType), (float) (amplifier / 100) + 0.5F);
            }
        if (this == poison_gas) {
            Registry<DamageType> dTypeReg = entity.level().registryAccess().registryOrThrow(Registries.DAMAGE_TYPE);
            Holder.Reference<DamageType> dType = dTypeReg.getHolder(com.denfop.datagen.DamageTypes.frostbiteObject).orElse(dTypeReg.getHolderOrThrow(net.minecraft.world.damagesource.DamageTypes.MAGIC));

            entity.hurt(new DamageSource(dType), (float) (amplifier / 100) + 0.5F);
        }
        if (this == poison_gas) {
            Registry<DamageType> dTypeReg = entity.level().registryAccess().registryOrThrow(Registries.DAMAGE_TYPE);
            Holder.Reference<DamageType> dType = dTypeReg.getHolder(com.denfop.datagen.DamageTypes.poison_gasObject).orElse(dTypeReg.getHolderOrThrow(net.minecraft.world.damagesource.DamageTypes.MAGIC));

            entity.hurt(new DamageSource(dType), (float) (amplifier / 100) + 0.5F);
        }
    }

    public void applyEffect(LivingEntity entityLiving, int radiationDuration) {
        MobEffectInstance effect = new MobEffectInstance(this, radiationDuration);
        entityLiving.addEffect(effect);
    }
}
