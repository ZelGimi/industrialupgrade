package com.denfop.ssp.events;

import java.util.HashSet;
import java.util.Set;
import java.util.function.Predicate;

import org.lwjgl.input.Keyboard;

import com.denfop.ssp.SuperSolarPanels;
import com.denfop.ssp.items.SSPItems;
import com.denfop.ssp.items.armor.ItemArmorQuantumBoosts;
import com.denfop.ssp.items.armor.ItemGraviChestplate;
import com.denfop.ssp.items.armorbase.ItemAdvancedElectricJetpack;
import com.denfop.ssp.keyboard.SSPKeys;

import ic2.core.util.StackUtil;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.EntityEquipmentSlot;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraftforge.event.entity.living.LivingEvent;
import net.minecraftforge.event.entity.living.LivingEvent.LivingJumpEvent;
import net.minecraftforge.event.entity.living.LivingEvent.LivingUpdateEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

public class EventHandler {
	 @SubscribeEvent
	  public void updateAbilities(LivingEvent.LivingUpdateEvent event) {
	    if (!(event.getEntity() instanceof EntityPlayer))
	      return; 
	    EntityLivingBase entity = event.getEntityLiving();
	    String key = entity.getCachedUniqueIdString() + "|" + entity.world.isRemote;
	 
	    boolean hasChestplate = isPlayerWearing(event.getEntityLiving(), EntityEquipmentSlot.CHEST, item -> item instanceof ItemGraviChestplate);
	    
	    if (hasChestplate) {
	      entitiesWithChestplates.add(key);
	      handleChestplateStateChange(entity, true);
	    } 
	    if (!hasChestplate) {
	      entitiesWithChestplates.remove(key);
	      handleChestplateStateChange(entity, false);
	    } 
	   
	    if (entitiesWithChestplates.contains(key))
	      tickChestplateAbilities(entity); 
	    
	  }
	  
	  public static boolean isPlayerWearing(EntityLivingBase entity, EntityEquipmentSlot slot, Predicate<Item> predicate) {
	    ItemStack stack = entity.getItemStackFromSlot(slot);
	    return (!stack.isEmpty() && predicate.test(stack.getItem()));
	  }
	 public static final Set<String> entitiesWithChestplates = new HashSet<>();
	 public static final Set<String> entitiesWithFlight = new HashSet<>();
	  private static void handleChestplateStateChange(EntityLivingBase entity, boolean isNew) {
	    String key = entity.getCachedUniqueIdString() + "|" + entity.world.isRemote;
	    if (entity instanceof EntityPlayer) {
	      EntityPlayer player = (EntityPlayer)entity;
	      if (isNew) {
	        player.capabilities.allowFlying = true;
	        entitiesWithFlight.add(key);
	      } else if (!player.capabilities.isCreativeMode && entitiesWithFlight.contains(key)) {
	        player.capabilities.allowFlying = false;
	        player.capabilities.isFlying = false;
	        entitiesWithFlight.remove(key);
	      } 
	    } 
	  }
	 private static void tickChestplateAbilities(EntityLivingBase entity) {}
	  private static void stripAbilities(EntityLivingBase entity) {
	    String key = entity.getCachedUniqueIdString() + "|" + entity.world.isRemote;
	    
	    if (entitiesWithChestplates.remove(key))
	      handleChestplateStateChange(entity, false); 
	    
	  }
	      @SubscribeEvent
	         public void jump(LivingJumpEvent  event) {
	    	  if (event.getEntityLiving() == null || !(event.getEntityLiving() instanceof EntityPlayer)) 
	   			  return;
	        	 
	        	 EntityPlayer player = (EntityPlayer) event.getEntity();
	        	 if(player.inventory.armorItemInSlot(0).getItem() instanceof  ItemArmorQuantumBoosts) {
	        		 player.motionY +=0.22;
	        		 
	        	 }
	        	 
	      }
}
