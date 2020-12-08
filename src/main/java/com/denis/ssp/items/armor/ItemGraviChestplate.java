package com.Denfop.ssp.items.armor;

import com.Denfop.ssp.Configs;
import com.Denfop.ssp.items.armorbase.ItemAdvancedElectricJetpack;

import ic2.api.item.ElectricItem;
import ic2.core.IC2;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.EntityEquipmentSlot;
import net.minecraft.item.EnumRarity;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTBase;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.world.World;

public class ItemGraviChestplate extends ItemAdvancedElectricJetpack {
  protected static final int DEFAULT_COLOUR = -1;
  
  public ItemGraviChestplate() {
    super("graviChestplate", Configs.maxCharge6, Configs.transferLimit6, Configs.tier6);
  }
  
  public String getArmorTexture(ItemStack stack, Entity entity, EntityEquipmentSlot slot, String type) {
    return "super_solar_panels:textures/armour/" + this.name + ((type != null) ? "Overlay" : "") + ".png";
  }
  
  public void setColor(ItemStack stack, int colour) {
    getDisplayNbt(stack, true).setInteger("colour", colour);
  }
  
  public boolean hasColor(ItemStack stack) {
    return (getColor(stack) != -1);
  }
  
  public int getColor(ItemStack stack) {
    NBTTagCompound nbt = getDisplayNbt(stack, false);
    return (nbt == null || !nbt.hasKey("colour", 3)) ? -1 : nbt.getInteger("colour");
  }
  
  public void removeColor(ItemStack stack) {
    NBTTagCompound nbt = getDisplayNbt(stack, false);
    if (nbt == null || !nbt.hasKey("colour", 3))
      return; 
    nbt.removeTag("colour");
    if (nbt.hasNoTags())
      stack.getTagCompound().removeTag("display"); 
  }
  
  protected NBTTagCompound getDisplayNbt(ItemStack stack, boolean create) {
    NBTTagCompound out, nbt = stack.getTagCompound();
    if (nbt == null) {
      if (!create)
        return null; 
      nbt = new NBTTagCompound();
      stack.setTagCompound(nbt);
    } 
    if (!nbt.hasKey("display", 10)) {
      if (!create)
        return null; 
      out = new NBTTagCompound();
      nbt.setTag("display", (NBTBase)out);
    } else {
      out = nbt.getCompoundTag("display");
    } 
    return out;
  }
  
  public EnumRarity getRarity(ItemStack stack) {
    return EnumRarity.EPIC;
  }
  
  public void onArmorTick(World world, EntityPlayer player, ItemStack stack) {
    super.onArmorTick(world, player, stack);
    player.extinguish();
  }
  
  public boolean isJetpackActive(ItemStack stack) {
    return (super.isJetpackActive(stack) && ElectricItem.manager.getCharge(stack) >= 10000.0D);
  }
  
  public float getPower(ItemStack stack) {
    return 1.5F;
  }
  
  public float getDropPercentage(ItemStack stack) {
    return 0.01F;
  }
  
  public float getBaseThrust(ItemStack stack, boolean hover) {
    return hover ? 1.0F : 0.5F;
  }
  
  public float getBoostThrust(EntityPlayer player, ItemStack stack, boolean hover) {
    return (IC2.keyboard.isBoostKeyDown(player) && ElectricItem.manager.getCharge(stack) >= 834.0D) ? (hover ? 0.1F : 0.3F) : 0.0F;
  }
  
  public boolean useBoostPower(ItemStack stack, float boostAmount) {
    return (ElectricItem.manager.discharge(stack, 834.0D, 2147483647, true, false, false) > 0.0D);
  }
  
  public float getWorldHeightDivisor(ItemStack stack) {
    return 0.91071427F;
  }
  
  public float getHoverMultiplier(ItemStack stack, boolean upwards) {
    return 0.25F;
  }
  
  public float getHoverBoost(EntityPlayer player, ItemStack stack, boolean up) {
    if (IC2.keyboard.isBoostKeyDown(player) && ElectricItem.manager.getCharge(stack) >= 834.0D) {
      if (!player.onGround)
        ElectricItem.manager.discharge(stack, 834.0D, 2147483647, true, false, false); 
      return 3.0F;
    } 
    return 1.0F;
  }
  
  public boolean drainEnergy(ItemStack pack, int amount) {
    return (ElectricItem.manager.discharge(pack, 278.0D, 2147483647, true, false, false) > 0.0D);
  }
  
  public int getEnergyPerDamage() {
    return 20000;
  }
  
  public double getDamageAbsorptionRatio() {
    return 1.1D;
  }
  
  public boolean canProvideEnergy(ItemStack stack) {
    return true;
  }
}
