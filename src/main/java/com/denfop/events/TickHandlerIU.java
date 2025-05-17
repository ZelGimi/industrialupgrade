package com.denfop.events;

import com.denfop.IUCore;
import com.denfop.items.armour.ISpecialArmor;
import com.denfop.network.WorldData;
import com.denfop.world.IWorldTickCallback;
import net.minecraft.client.Minecraft;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.core.NonNullList;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.event.TickEvent;
import net.minecraftforge.event.entity.living.LivingHurtEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;

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
    public void hurt(LivingHurtEvent event) {
        if (event.getEntity() instanceof LivingEntity) {
            NonNullList<ItemStack> armorList = NonNullList.withSize(4, ItemStack.EMPTY);
            armorList.set(0, (event.getEntity().getItemBySlot(EquipmentSlot.FEET)));
            armorList.set(1, (event.getEntity().getItemBySlot(EquipmentSlot.LEGS)));
            armorList.set(2, (event.getEntity().getItemBySlot(EquipmentSlot.CHEST)));
            armorList.set(3, (event.getEntity().getItemBySlot(EquipmentSlot.HEAD)));
            float damageAmount = ISpecialArmor.ArmorProperties.applyArmor(event.getEntity(), armorList, event.getSource(), event.getAmount());
            event.setAmount(damageAmount);
        }
    }


    @SubscribeEvent
    public void onWorldTick(TickEvent.LevelTickEvent event) {
        Level world = event.level;
        WorldData worldData = WorldData.get(world, false);
        if (worldData != null) {
            if (event.phase == TickEvent.Phase.START) {
                processUpdates(world, worldData);

            } else {
                if (world.isClientSide) {
                    IUCore.network.getClient().onTickEnd(worldData);
                } else {
                    IUCore.network.getServer().onTickEnd(worldData);
                }
            }

        }
    }

    @SubscribeEvent
    @OnlyIn(Dist.CLIENT)
    public void onClientTick(TickEvent.ClientTickEvent event) {
        if (event.phase == TickEvent.Phase.START) {
            IUCore.keyboard.sendKeyUpdate();

            if (Minecraft.getInstance().level != null) {
                ClientLevel world = Minecraft.getInstance().level;
                processUpdates(world, WorldData.get(world));

            }
        }

    }

}
