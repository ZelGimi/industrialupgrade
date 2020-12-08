package com.Denfop.ssp.items.armorbase;


import com.Denfop.ssp.Configs;
import com.Denfop.ssp.keyboard.SSPKeys;
import com.google.common.base.CaseFormat;
import ic2.api.item.ElectricItem;
import ic2.api.item.HudMode;
import ic2.core.IC2;
import ic2.core.init.BlocksItems;
import ic2.core.init.Localization;
import ic2.core.item.armor.ItemArmorElectric;
import ic2.core.item.armor.jetpack.IBoostingJetpack;
import ic2.core.ref.ItemName;
import ic2.core.util.StackUtil;
import net.minecraft.client.renderer.block.model.ModelResourceLocation;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.MobEffects;
import net.minecraft.inventory.EntityEquipmentSlot;
import net.minecraft.item.EnumRarity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.potion.PotionEffect;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.text.TextFormatting;
import net.minecraft.world.World;
import net.minecraftforge.client.model.ModelLoader;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public class ItemAdvancedElectricJetpack extends ItemArmorElectric implements IBoostingJetpack {
  protected final String name;
  
  public ItemAdvancedElectricJetpack() {
    this("advancedJetpack");
  }
  
  protected ItemAdvancedElectricJetpack(String name) {
    this(name, 3000000.0D, 30000.0D, 4);
  }
  
  protected ItemAdvancedElectricJetpack(String name, double maxCharge, double transferLimit, int tier) {
    super(null, null, EntityEquipmentSlot.CHEST, maxCharge, transferLimit, tier);
    ((ItemAdvancedElectricJetpack)BlocksItems.registerItem((Item)this, new ResourceLocation("super_solar_panels", this.name = name))).setUnlocalizedName(name);
    setMaxDamage(27);
    setMaxStackSize(1);
    setNoRepair();
  }
  
  @SideOnly(Side.CLIENT)
  public void registerModels(ItemName name) {
    ModelLoader.setCustomModelResourceLocation((Item)this, 0, new ModelResourceLocation("super_solar_panels:" + CaseFormat.LOWER_CAMEL.to(CaseFormat.LOWER_UNDERSCORE, this.name), null));
  }
  
  public String getArmorTexture(ItemStack stack, Entity entity, EntityEquipmentSlot slot, String type) {
    return "super_solar_panels:textures/armour/" + this.name + ".png";
  }
  
  public String getTranslationKey() {
    return "ssp." + super.getUnlocalizedName().substring(4);
  }
  
  public EnumRarity getRarity(ItemStack stack) {
    return EnumRarity.UNCOMMON;
  }
  
  public static boolean isJetpackOn(ItemStack stack) {
    return StackUtil.getOrCreateNbtData(stack).getBoolean("isFlyActive");
  }
  
  public static boolean isHovering(ItemStack stack) {
    return StackUtil.getOrCreateNbtData(stack).getBoolean("hoverMode");
  }
  
  public static boolean switchJetpack(ItemStack stack) {
    NBTTagCompound nbt = StackUtil.getOrCreateNbtData(stack);
    boolean newMode;
    nbt.setBoolean("isFlyActive", newMode = !nbt.getBoolean("isFlyActive"));
    return newMode;
  }
  
  public void onArmorTick(World world, EntityPlayer player, ItemStack stack) {
    NBTTagCompound nbt = StackUtil.getOrCreateNbtData(stack);
    byte toggleTimer = nbt.getByte("toggleTimer");
    if (com.Denfop.ssp.keyboard.SSPKeys.isFlyKeyDown(player) && toggleTimer == 0) {
      nbt.setByte("toggleTimer", toggleTimer = 10);
      if (!world.isRemote) {
        String mode;
        if (switchJetpack(stack)) {
          mode = TextFormatting.DARK_GREEN + Localization.translate("gravisuite.message.on");
        } else {
          mode = TextFormatting.DARK_RED + Localization.translate("gravisuite.message.off");
        } 
      } 
    } 
    if (toggleTimer > 0 && !isJetpackOn(stack)) {
      toggleTimer = (byte)(toggleTimer - 1);
      nbt.setByte("toggleTimer", toggleTimer);
    }
    boolean ret = false;
    NBTTagCompound nbtData = StackUtil.getOrCreateNbtData(stack);
    boolean Nightvision = nbtData.getBoolean("Nightvision");
    short hubmode = nbtData.getShort("HudMode");
    if (SSPKeys.Isremovepoison2(player) && toggleTimer == 0) {
        toggleTimer = 10;
        Nightvision = !Nightvision;
        if (IC2.platform.isSimulating()) {
            nbtData.setBoolean("Nightvision", Nightvision);
            if (Nightvision) {
                IC2.platform.messagePlayer(player, "Effects enabled.", new Object[0]);
            }
            else {
                IC2.platform.messagePlayer(player, "Effects disabled.", new Object[0]);
            }
        }
    }
    if (IC2.keyboard.isAltKeyDown(player) && IC2.keyboard.isHudModeKeyDown(player) && toggleTimer == 0) {
        toggleTimer = 10;
        if (hubmode == HudMode.getMaxMode()) {
            hubmode = 0;
        }
        else {
            ++hubmode;
        }
        if (IC2.platform.isSimulating()) {
            nbtData.setShort("HudMode", hubmode);
            IC2.platform.messagePlayer(player, Localization.translate(HudMode.getFromID(hubmode).getTranslationKey()), new Object[0]);
        }
    }
    if (IC2.platform.isSimulating() && toggleTimer > 0) {
        final NBTTagCompound nbtTagCompound = nbtData;
        final String s = "toggleTimer";
        --toggleTimer;
        nbtTagCompound.setByte(s, toggleTimer);
    }
    if (Nightvision && IC2.platform.isSimulating() && ElectricItem.manager.use(stack, 1.0, (EntityLivingBase)player)) {
        final BlockPos pos = new BlockPos((int)Math.floor(player.posX), (int)Math.floor(player.posY), (int)Math.floor(player.posZ));
        final int skylight = player.getEntityWorld().getLightFromNeighbors(pos);
        if (skylight > 8) {
            
            if (Configs.canCraftDoubleSlabs) {
                player.addPotionEffect(new PotionEffect(MobEffects.FIRE_RESISTANCE, 300, 0, true, true));
              } else {
                return;
              } 
              
        }
        else {
           
            if (Configs.canCraftDoubleSlabs) {
                player.addPotionEffect(new PotionEffect(MobEffects.FIRE_RESISTANCE, 300, 0, true, true));
              } else {
                return;
              } 
             
          
        }
        ret = true;
    }
  }
  
  public boolean isJetpackActive(ItemStack stack) {
    return isJetpackOn(stack);
  }
  
  public double getChargeLevel(ItemStack stack) {
    return ElectricItem.manager.getCharge(stack) / getMaxCharge(stack);
  }
  
  public float getPower(ItemStack stack) {
    return 1.0F;
  }
  
  public float getDropPercentage(ItemStack stack) {
    return 0.05F;
  }
  
  public float getBaseThrust(ItemStack stack, boolean hover) {
    return hover ? 0.65F : 0.3F;
  }
  
  public float getBoostThrust(EntityPlayer player, ItemStack stack, boolean hover) {
    return (IC2.keyboard.isBoostKeyDown(player) && ElectricItem.manager.getCharge(stack) >= 60.0D) ? (hover ? 0.07F : 0.09F) : 0.0F;
  }
  
  public boolean useBoostPower(ItemStack stack, float boostAmount) {
    return (ElectricItem.manager.discharge(stack, 60.0D, 2147483647, true, false, false) > 0.0D);
  }
  
  public float getWorldHeightDivisor(ItemStack stack) {
    return 1.0F;
  }
  
  public float getHoverMultiplier(ItemStack stack, boolean upwards) {
    return 0.2F;
  }
  
  public float getHoverBoost(EntityPlayer player, ItemStack stack, boolean up) {
    if (IC2.keyboard.isBoostKeyDown(player) && ElectricItem.manager.getCharge(stack) >= 60.0D) {
      if (!player.onGround)
        ElectricItem.manager.discharge(stack, 60.0D, 2147483647, true, false, false); 
      return 2.0F;
    } 
    return 1.0F;
  }
  
  public boolean drainEnergy(ItemStack pack, int amount) {
    return (ElectricItem.manager.discharge(pack, (amount * 6), 2147483647, true, false, false) > 0.0D);
  }
  
  public boolean canProvideEnergy(ItemStack stack) {
    return true;
  }
  
  public int getEnergyPerDamage() {
    return 0;
  }
  
  public double getDamageAbsorptionRatio() {
    return 0.0D;
  }
}
