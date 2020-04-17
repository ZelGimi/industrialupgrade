// 
// Decompiled by Procyon v0.5.36
// 

package com.Denfop.ssp.items;

import ic2.core.IC2;
import ic2.core.init.MainConfig;
import ic2.core.util.ConfigUtil;
import ic2.core.util.StackUtil;


import ic2.core.item.armor.ItemArmorQuantumSuit;
import ic2.api.item.ElectricItem;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.MobEffects;
import net.minecraft.world.World;
import net.minecraft.item.EnumRarity;
import net.minecraft.nbt.NBTBase;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.potion.Potion;
import net.minecraft.potion.PotionEffect;
import net.minecraft.util.DamageSource;
import net.minecraft.inventory.EntityEquipmentSlot;
import net.minecraft.entity.Entity;
import net.minecraft.item.ItemStack;

public class ItemArmorQuantumBoosts extends ItemBoosts
{
    protected static final int DEFAULT_COLOUR = -1;
    
    public ItemArmorQuantumBoosts() {
        super("graviBoosts", 6.0E7, 100000.0, 4);
    }
    
    @Override
    public String getArmorTexture(final ItemStack stack, final Entity entity, final EntityEquipmentSlot slot, final String type) {
        return "super_solar_panels:textures/armour/" + this.name + ((type != null) ? "Overlay" : "") + ".png";
    }
    
    public void func_82813_b(final ItemStack stack, final int colour) {
        this.getDisplayNbt(stack, true).setInteger("colour", colour);
    }
    
    public boolean func_82816_b_(final ItemStack stack) {
        return this.func_82814_b(stack) != -1;
    }
    
    public int func_82814_b(final ItemStack stack) {
        final NBTTagCompound nbt = this.getDisplayNbt(stack, false);
        return (nbt == null || !nbt.hasKey("colour", 3)) ? -1 : nbt.getInteger("colour");
    }
    
    public void func_82815_c(final ItemStack stack) {
        final NBTTagCompound nbt = this.getDisplayNbt(stack, false);
        if (nbt == null || !nbt.hasKey("colour", 3)) {
            return;
        }
        nbt.removeTag("colour");
        if (nbt.hasNoTags()) {
            stack.getTagCompound().removeTag("display");
        }
    }
    
    protected NBTTagCompound getDisplayNbt(final ItemStack stack, final boolean create) {
        NBTTagCompound nbt = stack.getTagCompound();
        if (nbt == null) {
            if (!create) {
                return null;
            }
            nbt = new NBTTagCompound();
            stack.setTagCompound(nbt);
        }
        NBTTagCompound out;
        if (!nbt.hasKey("display", 10)) {
            if (!create) {
                return null;
            }
            out = new NBTTagCompound();
            nbt.setTag("display", (NBTBase)out);
        }
        else {
            out = nbt.getCompoundTag("display");
        }
        return out;
    }
    
    @Override
    public EnumRarity getRarity(final ItemStack stack) {
        return EnumRarity.EPIC;
    }
    private float jumpCharge;
    @Override
    public void onArmorTick(final World world, final EntityPlayer player, final ItemStack stack) {
        super.onArmorTick(world, player, stack);
        player.extinguish();
        final NBTTagCompound nbtData = StackUtil.getOrCreateNbtData(stack);
        byte toggleTimer = nbtData.getByte("toggleTimer");
        boolean ret = false;
        if (IC2.platform.isSimulating()) {
            final boolean wasOnGround = !nbtData.hasKey("wasOnGround") || nbtData.getBoolean("wasOnGround");
            if (wasOnGround && !player.onGround && IC2.keyboard.isJumpKeyDown(player) && IC2.keyboard.isBoostKeyDown(player)) {
                ElectricItem.manager.use(stack, 4000.0, null);
                ret = true;
            }
            if (player.onGround != wasOnGround) {
                nbtData.setBoolean("wasOnGround", player.onGround);
            }
        }
        else {
            if (ElectricItem.manager.canUse(stack, 4000.0) && player.onGround) {
                this.jumpCharge = 1.0f;
            }
            if (player.motionY >= 0.0 && this.jumpCharge > 0.0f && !player.isInWater()) {
                
                    if (this.jumpCharge == 0.9f) {
                        player.motionX *= 1.0;
                        player.motionZ *= 1.0;
                    }
                    player.motionY += this.jumpCharge * 0.3f;
                    this.jumpCharge *= 0.75;
                }
                else if (this.jumpCharge < 1.0f) {
                    this.jumpCharge = 0.0f;
                }
            }
        player.addPotionEffect(new PotionEffect(MobEffects.WATER_BREATHING, 300, 0, true, true));
        player.addPotionEffect(new PotionEffect(MobEffects.JUMP_BOOST, 300, 0, true, true));
        player.addPotionEffect(new PotionEffect(MobEffects.REGENERATION, 300, 4, true, true));
        }
        
       
    
    
 
    
    
    @Override
    public boolean isJetpackActive(final ItemStack stack) {
        return super.isJetpackActive(stack) && ElectricItem.manager.getCharge(stack) >= 10000.0;
    }
    
    @Override
    public float getPower(final ItemStack stack) {
        return 1.5f;
    }
    
    @Override
    public float getDropPercentage(final ItemStack stack) {
        return 0.01f;
    }
    
    @Override
    public float getBaseThrust(final ItemStack stack, final boolean hover) {
        return hover ? 1.0f : 0.5f;
    }
    
    @Override
    public float getBoostThrust(final EntityPlayer player, final ItemStack stack, final boolean hover) {
        return (IC2.keyboard.isBoostKeyDown(player) && ElectricItem.manager.getCharge(stack) >= 834.0) ? (hover ? 0.1f : 0.3f) : 0.0f;
    }
    
    @Override
    public boolean useBoostPower(final ItemStack stack, final float boostAmount) {
        return ElectricItem.manager.discharge(stack, 834.0, Integer.MAX_VALUE, true, false, false) > 0.0;
    }
    
    @Override
    public float getWorldHeightDivisor(final ItemStack stack) {
        return 0.91071427f;
    }
    
    @Override
    public float getHoverMultiplier(final ItemStack stack, final boolean upwards) {
        return 0.25f;
    }
    
    @Override
    public float getHoverBoost(final EntityPlayer player, final ItemStack stack, final boolean up) {
        if (IC2.keyboard.isBoostKeyDown(player) && ElectricItem.manager.getCharge(stack) >= 834.0) {
            if (!player.onGround) {
                ElectricItem.manager.discharge(stack, 834.0, Integer.MAX_VALUE, true, false, false);
            }
            return 3.0f;
        }
        return 1.0f;
    }
    
    @Override
    public boolean drainEnergy(final ItemStack pack, final int amount) {
        return ElectricItem.manager.discharge(pack, 278.0, Integer.MAX_VALUE, true, false, false) > 0.0;
    }
    
    @Override
    public int getEnergyPerDamage() {
        return 20000;
    }
    
    @Override
    public double getDamageAbsorptionRatio() {
        return 1.1;
    }
    
    @Override
    public boolean canProvideEnergy(final ItemStack stack) {
        return true;
    }
}
