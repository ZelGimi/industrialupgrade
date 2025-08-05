package com.denfop.items;

import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.ItemStack;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.api.distmarker.OnlyIn;

public interface IProperties {

    String[] properties();

    @OnlyIn(Dist.CLIENT)
    float getItemProperty(ItemStack itemStack, ClientLevel level, LivingEntity entity, int p174679, String property);
}
