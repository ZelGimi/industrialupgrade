package com.denfop;

import com.denfop.damagesource.IUDamageSource;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Gui;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.item.ItemStack;
import net.minecraft.potion.Potion;
import net.minecraft.potion.PotionEffect;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.common.registry.ForgeRegistries;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import java.util.Arrays;
import java.util.List;

public class IUPotion extends Potion {

    public static IUPotion radiation;
    public static IUPotion frostbite;
    public static IUPotion poison_gas;
    private final List<ItemStack> curativeItems;
    private final ResourceLocation nameIcon;

    public IUPotion(String name, boolean badEffect, int liquidColor, ItemStack... curativeItems) {
        super(badEffect, liquidColor);
        this.curativeItems = Arrays.asList(curativeItems);
        this.nameIcon = new ResourceLocation(Constants.MOD_ID + ":textures/gui/" + name + ".png");
        ForgeRegistries.POTIONS.register(this.setRegistryName(name));
    }

    public static void init() {
        radiation.setPotionName("iu.potion.radiation");
        radiation.setEffectiveness(0.25);
        frostbite.setPotionName("iu.potion.frostbite");
        frostbite.setEffectiveness(0.25);
        poison_gas.setPotionName("iu.potion.poison_gas");
        poison_gas.setEffectiveness(0.25);
    }

    @Override
    @SideOnly(Side.CLIENT)
    public void renderHUDEffect(int x, int y, PotionEffect effect, Minecraft mc, float alpha) {
        mc.renderEngine.bindTexture(nameIcon);

        Gui.drawModalRectWithCustomSizedTexture(x + 3, y + 3, 0, 0, 18, 18, 18, 18);
    }

    @Override
    @SideOnly(Side.CLIENT)
    public void renderInventoryEffect(int x, int y, PotionEffect effect, Minecraft mc) {
        mc.renderEngine.bindTexture(nameIcon);

        Gui.drawModalRectWithCustomSizedTexture(x + 6, y + 7, 0, 0, 18, 18, 18, 18);
    }

    public void performEffect(EntityLivingBase entity, int amplifier) {
        if (this == radiation) {
            entity.attackEntityFrom(IUDamageSource.radiation, (float) (amplifier / 100) + 0.5F);
        }
        if (this == frostbite) {
            entity.attackEntityFrom(IUDamageSource.frostbite, (float) (amplifier / 100) + 0.5F);
        }
        if (this == poison_gas) {
            entity.attackEntityFrom(IUDamageSource.poison_gas, (float) (amplifier / 100) + 0.5F);
        }
    }

    public boolean isReady(int duration, int amplifier) {
        int rate = 25 >> amplifier;
        return rate == 0 || duration % rate == 0;
    }

    public void applyTo(EntityLivingBase entity, int duration, int amplifier) {
        PotionEffect effect = new PotionEffect(radiation, duration, amplifier);
        effect.setCurativeItems(this.curativeItems);
        entity.addPotionEffect(effect);
    }

}
