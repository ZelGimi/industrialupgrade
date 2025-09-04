package com.denfop.items;

import com.denfop.IUCore;
import com.denfop.IUItem;
import net.minecraft.Util;
import net.minecraft.core.Registry;
import net.minecraft.network.chat.Component;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.food.FoodProperties;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;

import java.util.ArrayList;

public class ItemFoodIU extends Item {
    private final String name;
    private final String path;
    private String nameItem;


    public ItemFoodIU(String name, int amount, float saturation) {
        this(name, "", amount, saturation);
    }

    public ItemFoodIU(String name, int amount, float saturation, boolean edible) {
        super(new Item.Properties()
                .tab(IUCore.CropsTab)
                .food(new FoodProperties.Builder()
                        .nutrition(amount)
                        .saturationMod(saturation).alwaysEat()
                        .build())
        );

        this.name = name;
        this.path = "";
    }

    public ItemFoodIU(String name, String path, int amount, float saturation) {
        super(new Item.Properties()
                .tab(IUCore.CropsTab)
                .food(new FoodProperties.Builder()
                        .nutrition(amount)
                        .saturationMod(saturation)
                        .build())
        );

        this.name = name;
        this.path = path;
    }

    protected String getOrCreateDescriptionId() {
        if (this.nameItem == null) {
            StringBuilder pathBuilder = new StringBuilder(Util.makeDescriptionId("iu", Registry.ITEM.getKey(this)));
            String targetString = "industrialupgrade.";
            String replacement = "";
            if (replacement != null) {
                int index = pathBuilder.indexOf(targetString);
                while (index != -1) {
                    pathBuilder.replace(index, index + targetString.length(), replacement);
                    index = pathBuilder.indexOf(targetString, index + replacement.length());
                }
            }
            this.nameItem = "iu." + pathBuilder.toString().split("\\.")[1] + ".name";
        }

        return this.nameItem;
    }

    @Override
    public Component getName(ItemStack stack) {
        return super.getName(stack);
    }

    @Override
    public ItemStack finishUsingItem(ItemStack stack, Level world, LivingEntity player) {
        if (!world.isClientSide && stack.getItem() == IUItem.terra_wart.getItem()) {
            for (MobEffectInstance effect : new ArrayList<>(player.getActiveEffects())) {
                MobEffect potion = effect.getEffect();
                if (!potion.isBeneficial()) {
                    player.removeEffect(potion);
                }
            }
        }
        return super.finishUsingItem(stack, world, player);
    }

}
