package com.denfop;

import com.denfop.damagesource.IUDamageSource;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.item.ItemStack;
import net.minecraft.potion.Potion;
import net.minecraft.potion.PotionEffect;
import net.minecraftforge.fml.common.registry.ForgeRegistries;

import java.util.Arrays;
import java.util.List;

public class IUPotion extends Potion {

    public static IUPotion radiation;
    private final List<ItemStack> curativeItems;

    public IUPotion(String name, boolean badEffect, int liquidColor, ItemStack... curativeItems) {
        super(badEffect, liquidColor);
        this.curativeItems = Arrays.asList(curativeItems);
        ForgeRegistries.POTIONS.register(this.setRegistryName(name));
    }

    public static void init() {
        radiation.setPotionName("iu.potion.radiation");
        radiation.setIconIndex(6, 0);
        radiation.setEffectiveness(0.25);
    }

    public void performEffect(EntityLivingBase entity, int amplifier) {
        if (this == radiation) {
            entity.attackEntityFrom(IUDamageSource.radiation, (float) (amplifier / 100) + 0.5F);
        }

    }

    public boolean isReady(int duration, int amplifier) {
        if (this == radiation) {
            int rate = 25 >> amplifier;
            return rate == 0 || duration % rate == 0;
        } else {
            return false;
        }
    }

    public void applyTo(EntityLivingBase entity, int duration, int amplifier) {
        PotionEffect effect = new PotionEffect(radiation, duration, amplifier);
        effect.setCurativeItems(this.curativeItems);
        entity.addPotionEffect(effect);
    }

}
