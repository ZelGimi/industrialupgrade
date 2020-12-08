
package com.denfop.ssp.items.itembase;

import ic2.api.item.ElectricItem;
import ic2.api.item.IElectricItem;
import ic2.api.item.IItemHudInfo;
import ic2.core.IC2;
import ic2.core.audio.AudioSource;
import ic2.core.audio.PositionSpec;
import ic2.core.init.BlocksItems;
import ic2.core.init.Localization;
import ic2.core.item.BaseElectricItem;
import ic2.core.item.ElectricItemManager;
import ic2.core.item.IPseudoDamageItem;
import ic2.core.item.ItemToolIC2;
import ic2.core.item.tool.HarvestLevel;
import ic2.core.item.tool.ToolClass;
import ic2.core.ref.ItemName;
import ic2.core.util.LogCategory;
import ic2.core.util.StackUtil;
import java.util.Collections;
import java.util.EnumSet;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;

import com.google.common.base.CaseFormat;

import net.minecraft.block.Block;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.renderer.block.model.ModelResourceLocation;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.EntityEquipmentSlot;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ActionResult;
import net.minecraft.util.EnumActionResult;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.NonNullList;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.client.model.ModelLoader;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public abstract class ItemElectricTool extends ItemToolIC2 implements IPseudoDamageItem, IElectricItem, IItemHudInfo {
  public double operationEnergyCost;
  
  public static int maxCharge;
  
  public static int transferLimit;
  
  public static int tier;
  
  protected AudioSource audioSource;
  
  protected boolean wasEquipped;
  public static  String name;
  protected ItemElectricTool(String name, int operationEnergyCost, HarvestLevel harvestLevel, ToolClass toolClasses, int maxCharge, int transferLimit, int tier, int damage2, int damage1) {
    this(name, operationEnergyCost, HarvestLevel.Iron, Collections.emptySet(), maxCharge, transferLimit , tier );
  }
  
  protected ItemElectricTool(String name, int operationEnergyCost, HarvestLevel harvestLevel, Set<ToolClass> toolClasses, int maxCharge, int transferLimit, int tier) {
    this(name, 2.0F, -3.0F, operationEnergyCost, harvestLevel, toolClasses, new HashSet<>(), maxCharge, transferLimit , tier);
  }
 
  private ItemElectricTool(String name, float damage, float speed, int operationEnergyCost, HarvestLevel harvestLevel, Set<ToolClass> toolClasses, Set<Block> mineableBlocks, int maxCharge, int transferLimit, int tier) {
    super(null, damage, speed, harvestLevel, toolClasses, mineableBlocks);
    this.operationEnergyCost = operationEnergyCost;
    setMaxDamage(27);
    setNoRepair();
    ((ItemElectricTool)BlocksItems.registerItem(this, new ResourceLocation("super_solar_panels", this.name = name))).setUnlocalizedName(name);
    
  }
 

public String getUnlocalizedName() {
	    return "super_solar_panels." + super.getUnlocalizedName().substring(4);
	  }
  @SideOnly(Side.CLIENT)
  public void registerModels(ItemName name) {
    ModelLoader.setCustomModelResourceLocation((Item)this, 0, new ModelResourceLocation("super_solar_panels:" + CaseFormat.LOWER_CAMEL.to(CaseFormat.LOWER_UNDERSCORE, this.name), null));
  }
  public EnumActionResult onItemUse(EntityPlayer player, World world, BlockPos pos, EnumHand hand, EnumFacing side, float xOffset, float yOffset, float zOffset) {
	    ElectricItem.manager.use(StackUtil.get(player, hand), 0.0D, (EntityLivingBase)player);
	    return super.onItemUse(player, world, pos, hand, side, xOffset, yOffset, zOffset);
	  }
	  
	  public ActionResult<ItemStack> onItemRightClick(World world, EntityPlayer player, EnumHand hand) {
	    ElectricItem.manager.use(StackUtil.get(player, hand), 0.0D, (EntityLivingBase)player);
	    return super.onItemRightClick(world, player, hand);
	  }
	  
	  public float getDestroySpeed(ItemStack stack, IBlockState state) {
	    if (!ElectricItem.manager.canUse(stack, this.operationEnergyCost))
	      return 1.0F; 
	    return super.getDestroySpeed(stack, state);
	  }
	  
	  public boolean hitEntity(ItemStack itemstack, EntityLivingBase entityliving, EntityLivingBase entityliving1) {
	    return true;
	  }
	  
	  public int getItemEnchantability() {
	    return 0;
	  }
	  
	  public boolean isRepairable() {
	    return false;
	  }
	  
	  public boolean canProvideEnergy(ItemStack stack) {
	    return false;
	  }
	  
	  public double getMaxCharge(ItemStack stack) {
	    return this.maxCharge;
	  }
	  
	  public int getTier(ItemStack stack) {
	    return this.tier;
	  }
	  
	  public double getTransferLimit(ItemStack stack) {
	    return this.transferLimit;
	  }
	  
	  public boolean onBlockDestroyed(ItemStack stack, World world, IBlockState state, BlockPos pos, EntityLivingBase user) {
	    if (state.getBlockHardness(world, pos) != 0.0F)
	      if (user != null) {
	        ElectricItem.manager.use(stack, this.operationEnergyCost, user);
	      } else {
	        ElectricItem.manager.discharge(stack, this.operationEnergyCost, this.tier, true, false, false);
	      }  
	    return true;
	  }
	  
	  public boolean getIsRepairable(ItemStack par1ItemStack, ItemStack par2ItemStack) {
	    return false;
	  }
	  
	  public boolean isBookEnchantable(ItemStack itemstack1, ItemStack itemstack2) {
	    return false;
	  }
	  
	  public void getSubItems(CreativeTabs tab, NonNullList<ItemStack> subItems) {
	    if (!isInCreativeTab(tab))
	      return; 
	    ElectricItemManager.addChargeVariants((Item)this, (List)subItems);
	  }
	  
	  public List<String> getHudInfo(ItemStack stack, boolean advanced) {
	    List<String> info = new LinkedList<>();
	    info.add(ElectricItem.manager.getToolTip(stack));
	    info.add(Localization.translate("ic2.item.tooltip.PowerTier", new Object[] { Integer.valueOf(this.tier) }));
	    return info;
	  }
	  
	  protected ItemStack getItemStack(double charge) {
	    ItemStack ret = new ItemStack((Item)this);
	    ElectricItem.manager.charge(ret, charge, 2147483647, true, false);
	    return ret;
	  }
	  
	  public void onUpdate(ItemStack itemstack, World world, Entity entity, int i, boolean flag) {
	    boolean isEquipped = (flag && entity instanceof EntityLivingBase);
	    if (IC2.platform.isRendering()) {
	      if (isEquipped && !this.wasEquipped) {
	        if (this.audioSource == null) {
	          String sound = getIdleSound((EntityLivingBase)entity, itemstack);
	          if (sound != null)
	            this.audioSource = IC2.audioManager.createSource(entity, PositionSpec.Hand, sound, true, false, IC2.audioManager.getDefaultVolume()); 
	        } 
	        if (this.audioSource != null)
	          this.audioSource.play(); 
	        String initSound = getStartSound((EntityLivingBase)entity, itemstack);
	        if (initSound != null)
	          IC2.audioManager.playOnce(entity, PositionSpec.Hand, initSound, true, IC2.audioManager.getDefaultVolume()); 
	      } else if (!isEquipped && this.audioSource != null) {
	        if (entity instanceof EntityLivingBase) {
	          EntityLivingBase theEntity = (EntityLivingBase)entity;
	          ItemStack stack = theEntity.getItemStackFromSlot(EntityEquipmentSlot.MAINHAND);
	          if (stack == null || stack.getItem() != this || stack == itemstack) {
	            removeAudioSource();
	            String sound = getStopSound(theEntity, itemstack);
	            if (sound != null)
	              IC2.audioManager.playOnce(entity, PositionSpec.Hand, sound, true, IC2.audioManager.getDefaultVolume()); 
	          } 
	        } 
	      } else if (this.audioSource != null) {
	        this.audioSource.updatePosition();
	      } 
	      this.wasEquipped = isEquipped;
	    } 
	  }
	  
	  protected void removeAudioSource() {
	    if (this.audioSource != null) {
	      this.audioSource.stop();
	      this.audioSource.remove();
	      this.audioSource = null;
	    } 
	  }
	  
	  public boolean onDroppedByPlayer(ItemStack item, EntityPlayer player) {
	    removeAudioSource();
	    return true;
	  }
	  
	  protected String getIdleSound(EntityLivingBase player, ItemStack stack) {
	    return null;
	  }
	  
	  protected String getStopSound(EntityLivingBase player, ItemStack stack) {
	    return null;
	  }
	  
	  protected String getStartSound(EntityLivingBase player, ItemStack stack) {
	    return null;
	  }
	  
	  public void setDamage(ItemStack stack, int damage) {
	    int prev = getDamage(stack);
	    if (damage != prev && BaseElectricItem.logIncorrectItemDamaging)
	      IC2.log.warn(LogCategory.Armor, new Throwable(), "Detected invalid armor damage application (%d):", new Object[] { Integer.valueOf(damage - prev) }); 
	  }
	  
	  public void setStackDamage(ItemStack stack, int damage) {
	    super.setDamage(stack, damage);
	  }
	}
