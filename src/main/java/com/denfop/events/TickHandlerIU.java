package com.denfop.events;

import com.denfop.IUCore;
import com.denfop.damagesource.IUDamageSource;
import com.denfop.items.armour.ISpecialArmor;
import com.denfop.network.WorldData;
import com.denfop.world.IWorldTickCallback;
import net.minecraft.client.Minecraft;
import net.minecraft.core.NonNullList;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.api.distmarker.OnlyIn;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.neoforge.event.entity.living.LivingDamageEvent;
import net.neoforged.neoforge.event.tick.LevelTickEvent;

public class TickHandlerIU {


    public static void requestSingleWorldTick(Level world, IWorldTickCallback callback) {
        WorldData.get(world).singleUpdates.add(callback);

    }

    private static void processUpdates(Level world, WorldData worldData) {

        IWorldTickCallback callback;
        for (; (callback = worldData.singleUpdates.poll()) != null; callback.onTick(world)) {

        }


    }

    @SubscribeEvent
    public void hurt(LivingDamageEvent.Pre event) {
        if (event.getEntity() instanceof LivingEntity) {
            NonNullList<ItemStack> armorList = NonNullList.withSize(4, ItemStack.EMPTY);
            armorList.set(0, (event.getEntity().getItemBySlot(EquipmentSlot.FEET)));
            armorList.set(1, (event.getEntity().getItemBySlot(EquipmentSlot.LEGS)));
            armorList.set(2, (event.getEntity().getItemBySlot(EquipmentSlot.CHEST)));
            armorList.set(3, (event.getEntity().getItemBySlot(EquipmentSlot.HEAD)));
            float damageAmount = ISpecialArmor.ArmorProperties.applyArmor(event.getEntity(), armorList, event.getSource(), event.getOriginalDamage());
            damageAmount = ISpecialArmor.ArmorProperties.applyArmor(event.getEntity(), armorList, event.getSource(), damageAmount);
            event.setNewDamage(damageAmount);
        }
    }

    @SubscribeEvent
    public void onWorldTick1(LevelTickEvent.Pre event) {
        Level world = event.getLevel();
        WorldData worldData = WorldData.get(world, false);
        if (worldData != null)
            processUpdates(world, worldData);


    }

    @SubscribeEvent
    public void onWorldTick(LevelTickEvent.Post event) {
        Level world = event.getLevel();
        WorldData worldData = WorldData.get(world, false);
        if (worldData != null) {

            if (world.isClientSide) {
                IUCore.network.getClient().onTickEnd(worldData);
            } else {
                IUCore.network.getServer().onTickEnd(worldData);
            }


        }
    }

    @SubscribeEvent
    @OnlyIn(Dist.CLIENT)
    public void onClientTick(LevelTickEvent.Pre event) {
        if (!event.getLevel().isClientSide)
            return;

        IUCore.keyboard.sendKeyUpdate(event.getLevel());

        if (Minecraft.getInstance().level != null) {
            if (IUDamageSource.current == null)
                IUDamageSource.initDamage(event.getLevel().registryAccess());

            Level world = event.getLevel();
            processUpdates(world, WorldData.get(world));

        }


    }

}
