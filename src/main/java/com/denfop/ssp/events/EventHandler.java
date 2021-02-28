package com.denfop.ssp.events;

import com.denfop.ssp.common.Utils;
import com.denfop.ssp.items.armor.ItemArmorQuantumBoosts;
import com.denfop.ssp.items.armor.ItemGraviChestplate;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraftforge.event.entity.living.LivingEvent.LivingJumpEvent;
import net.minecraftforge.event.entity.living.LivingEvent.LivingUpdateEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

public class EventHandler {

    @SubscribeEvent
    public void enableJetpack(LivingUpdateEvent event) {
        if (event.getEntityLiving() == null || !(event.getEntityLiving() instanceof EntityPlayer)) {
            return;
        }

        EntityPlayer player = (EntityPlayer) event.getEntity();
        NBTTagCompound nbtData = Utils.getOrCreateNbtData(player);

        if (!player.inventory.armorItemInSlot(2).isEmpty()) {
            if (player.inventory.armorItemInSlot(2).getItem() instanceof ItemGraviChestplate) {
                if (nbtData.getBoolean("isFlyActive")) {
                    player.capabilities.allowFlying = true;
                    player.capabilities.setFlySpeed((float) 0.15);
                    player.capabilities.isFlying = true;
                } else {
                    player.capabilities.allowFlying = false;
                    player.capabilities.isFlying = false;
                    player.capabilities.setFlySpeed((float) 0.05);
                }
            } else if (nbtData.getBoolean("isFlyActive")) {
                player.capabilities.allowFlying = false;
                player.capabilities.isFlying = false;
                nbtData.setBoolean("isFlyActive", false);
                player.capabilities.setFlySpeed((float) 0.05);
            }
        } else {
            if (nbtData.getBoolean("isFlyActive")) {
                player.capabilities.allowFlying = false;
                player.capabilities.isFlying = false;
                nbtData.setBoolean("isFlyActive", false);
                player.capabilities.setFlySpeed((float) 0.05);
            }
        }


    }

    @SubscribeEvent
    public void jump(LivingJumpEvent event) {
        if (event.getEntityLiving() != null && event.getEntityLiving() instanceof EntityPlayer) {
            EntityPlayer player = (EntityPlayer) event.getEntity();
            if (player.inventory.armorItemInSlot(0).getItem() instanceof ItemArmorQuantumBoosts) {
                player.motionY += 0.22;
            }
        }
    }

}
