// 
// Decompiled by Procyon v0.5.36
// 

package com.Denfop.ssp.items;

import ic2.core.IC2;
import ic2.core.IC2Potion;
import ic2.api.item.ElectricItem;
import ic2.api.item.IHazmatLike;
import ic2.api.item.IItemHudProvider;
import ic2.core.init.Localization;
import net.minecraft.util.text.TextFormatting;

import java.util.Map;

import com.Denfop.ssp.SSPKeys;
import com.Denfop.ssp.SuperSolarPanels;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.MobEffects;
import net.minecraft.world.World;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.potion.Potion;
import ic2.core.util.StackUtil;
import net.minecraft.item.EnumRarity;
import net.minecraft.entity.Entity;
import net.minecraft.item.ItemStack;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import net.minecraftforge.client.model.ModelLoader;
import net.minecraft.client.renderer.block.model.ModelResourceLocation;
import com.google.common.base.CaseFormat;
import net.minecraft.item.Item;
import ic2.core.init.BlocksItems;
import net.minecraft.util.ResourceLocation;
import ic2.core.ref.ItemName;
import net.minecraft.inventory.EntityEquipmentSlot;
import ic2.core.item.armor.jetpack.IBoostingJetpack;
import ic2.core.item.armor.ItemArmorElectric;

import java.util.IdentityHashMap;
import java.util.Iterator;
import net.minecraft.util.ActionResult;
import ic2.core.util.ConfigUtil;
import ic2.core.init.MainConfig;
import net.minecraft.util.math.BlockPos;
import ic2.core.init.Localization;
import ic2.api.item.HudMode;
import net.minecraft.potion.PotionEffect;
import java.util.Collection;
import java.util.LinkedList;
import net.minecraft.util.EnumActionResult;
import ic2.core.item.ItemTinCan;
import ic2.core.util.StackUtil;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.world.World;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import net.minecraft.item.EnumRarity;
import net.minecraftforge.fml.common.eventhandler.EventPriority;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import ic2.core.IC2;
import net.minecraftforge.event.entity.living.LivingFallEvent;
import net.minecraftforge.common.ISpecialArmor;
import net.minecraft.util.DamageSource;
import ic2.api.item.ElectricItem;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.nbt.NBTBase;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.item.ItemStack;
import ic2.core.IC2Potion;
import net.minecraft.init.MobEffects;
import net.minecraftforge.common.MinecraftForge;
import net.minecraft.inventory.EntityEquipmentSlot;
import ic2.core.ref.ItemName;
import net.minecraft.potion.Potion;
import java.util.Map;
import ic2.api.item.IItemHudProvider;
import ic2.api.item.IHazmatLike;
import ic2.core.item.armor.jetpack.IJetpack;

public class ItemChest extends ItemArmorElectric implements IBoostingJetpack {
	
    protected final String name;
    
    public ItemChest() {
        this("advancedJetpack");
    }
    
    protected ItemChest(final String name) {
        this(name, 3000000.0, 30000.0, 3);
    }
    
    protected ItemChest(final String name, final double maxCharge, final double transferLimit, final int tier) {
        super((ItemName)null, (String)null, EntityEquipmentSlot.CHEST, maxCharge, transferLimit, tier);
        ((ItemChest)BlocksItems.registerItem((Item)this, new ResourceLocation("super_solar_panels", this.name = name))).setUnlocalizedName(name);
        this.setMaxDamage(27);
        this.setMaxStackSize(1);
        this.setNoRepair();
    }
    
    @SideOnly(Side.CLIENT)
    public void registerModels(final ItemName name) {
        ModelLoader.setCustomModelResourceLocation((Item)this, 0, new ModelResourceLocation("super_solar_panels:" + CaseFormat.LOWER_CAMEL.to(CaseFormat.LOWER_UNDERSCORE, this.name), (String)null));
    }
    
    public String getArmorTexture(final ItemStack stack, final Entity entity, final EntityEquipmentSlot slot, final String type) {
        return "super_solar_panels:textures/armour/" + this.name + ".png";
    }
    
    public String func_77658_a() {
        return "super_solar_panels." + super.getUnlocalizedName().substring(4);
    }
    
    public EnumRarity func_77613_e(final ItemStack stack) {
        return EnumRarity.UNCOMMON;
    }
    
    public static boolean isJetpackOn(final ItemStack stack) {
        return StackUtil.getOrCreateNbtData(stack).getBoolean("isFlyActive");
    }
    
    public static boolean isHovering(final ItemStack stack) {
        return StackUtil.getOrCreateNbtData(stack).getBoolean("hoverMode");
    }
    
    public static boolean switchJetpack(final ItemStack stack) {
        final NBTTagCompound nbt = StackUtil.getOrCreateNbtData(stack);
        final boolean newMode;
        nbt.setBoolean("isFlyActive", newMode = !nbt.getBoolean("isFlyActive"));
        return newMode;
    }
    
    public void onArmorTick(final World world, final EntityPlayer player, final ItemStack stack) {
        final NBTTagCompound nbt = StackUtil.getOrCreateNbtData(stack);
        byte toggleTimer = nbt.getByte("toggleTimer");
        if (SSPKeys.isFlyKeyDown(player) && toggleTimer == 0) {
            nbt.setByte("toggleTimer", toggleTimer = 10);
            if (!world.isRemote) {
                String mode;
                if (switchJetpack(stack)) {
                    mode = TextFormatting.DARK_GREEN + Localization.translate("super_solar_panels.message.on");
                }
                else {
                    mode = TextFormatting.DARK_RED + Localization.translate("super_solar_panels.message.off");
                }
            }
        }
        if (toggleTimer > 0 && !isJetpackOn(stack)) {
            final NBTTagCompound nbtTagCompound = nbt;
            final String s = "toggleTimer";
            --toggleTimer;
            nbtTagCompound.setByte(s, toggleTimer);
        }
    }
    
    public boolean isJetpackActive(final ItemStack stack) {
        return isJetpackOn(stack);
    }
    
    public double getChargeLevel(final ItemStack stack) {
        return ElectricItem.manager.getCharge(stack) / this.getMaxCharge(stack);
    }
    
    public float getPower(final ItemStack stack) {
        return 1.0f;
    }
    
    public float getDropPercentage(final ItemStack stack) {
        return 0.05f;
    }
    
    public float getBaseThrust(final ItemStack stack, final boolean hover) {
        return hover ? 0.65f : 0.3f;
    }
    
    public float getBoostThrust(final EntityPlayer player, final ItemStack stack, final boolean hover) {
        return (IC2.keyboard.isBoostKeyDown(player) && ElectricItem.manager.getCharge(stack) >= 60.0) ? (hover ? 0.07f : 0.09f) : 0.0f;
    }
    
    public boolean useBoostPower(final ItemStack stack, final float boostAmount) {
        return ElectricItem.manager.discharge(stack, 60.0, Integer.MAX_VALUE, true, false, false) > 0.0;
    }
    
    public float getWorldHeightDivisor(final ItemStack stack) {
        return 1.0f;
    }
    
    public float getHoverMultiplier(final ItemStack stack, final boolean upwards) {
        return 0.2f;
    }
    
    public float getHoverBoost(final EntityPlayer player, final ItemStack stack, final boolean up) {
        if (IC2.keyboard.isBoostKeyDown(player) && ElectricItem.manager.getCharge(stack) >= 60.0) {
            if (!player.onGround) {
                ElectricItem.manager.discharge(stack, 60.0, Integer.MAX_VALUE, true, false, false);
            }
            return 2.0f;
        }
        return 1.0f;
    }
    
    public boolean drainEnergy(final ItemStack pack, final int amount) {
        return ElectricItem.manager.discharge(pack, (double)(amount * 6), Integer.MAX_VALUE, true, false, false) > 0.0;
    }
    
    public boolean canProvideEnergy(final ItemStack stack) {
        return true;
    }
    
    public int getEnergyPerDamage() {
        return 0;
    }
    
    public double getDamageAbsorptionRatio() {
        return 0.0;
    }
}
