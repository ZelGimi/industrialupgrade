package com.denfop.ssp.items.tools;

import com.denfop.ssp.Configs;
import com.denfop.ssp.items.itembase.ItemElectricTool;
import com.google.common.base.CaseFormat;
import com.google.common.collect.HashMultimap;
import com.google.common.collect.Multimap;
import ic2.api.item.ElectricItem;
import ic2.core.IC2;
import ic2.core.audio.PositionSpec;
import ic2.core.item.tool.HarvestLevel;
import ic2.core.item.tool.ToolClass;
import ic2.core.ref.ItemName;
import ic2.core.slot.ArmorSlot;
import ic2.core.util.StackUtil;
import java.util.EnumSet;
import java.util.Set;

import net.minecraft.block.state.IBlockState;
import net.minecraft.client.renderer.ItemMeshDefinition;
import net.minecraft.client.renderer.block.model.ModelBakery;
import net.minecraft.client.renderer.block.model.ModelResourceLocation;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.SharedMonsterAttributes;
import net.minecraft.entity.ai.attributes.AttributeModifier;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.init.Blocks;
import net.minecraft.inventory.EntityEquipmentSlot;
import net.minecraft.item.EnumRarity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.ActionResult;
import net.minecraft.util.EnumActionResult;
import net.minecraft.util.EnumHand;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.client.model.ModelLoader;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public class ItemNanoSaber extends ItemElectricTool {
	protected String name;

	private int damage1;

	private int damage2;

public ItemNanoSaber(String name,int damage, HarvestLevel harvestLevel, ToolClass toolClasses, int maxCharge, int transferLimit , int tier, int damage2, int damage1) {
    super(name, damage, harvestLevel, toolClasses, maxCharge, transferLimit , tier, damage2, damage1);
    this.soundTicker = 0;
    this.maxCharge = maxCharge;
    this.transferLimit = transferLimit;
    this.tier = tier;
    this.damage1 = damage1;
    this.damage2 = damage2;
  }
  
 
  
  public float getDestroySpeed(ItemStack stack, IBlockState state) {
    if (isActive(stack)) {
      this.soundTicker++;
      if (IC2.platform.isRendering() && this.soundTicker % 4 == 0)
        IC2.platform.playSoundSp(getRandomSwingSound(), 1.0F, 1.0F); 
      return (state.getBlock() == Blocks.WEB) ? 50.0F : 4.0F;
    } 
    return 1.0F;
  }
  
  public Multimap<String, AttributeModifier> getAttributeModifiers(EntityEquipmentSlot slot, ItemStack stack) {
    if (slot != EntityEquipmentSlot.MAINHAND)
      return super.getAttributeModifiers(slot, stack); 
    int dmg = damage2;
    if (ElectricItem.manager.canUse(stack, 400.0D) && isActive(stack))
      dmg = damage1; 
    HashMultimap hashMultimap = HashMultimap.create();
    hashMultimap.put(SharedMonsterAttributes.ATTACK_SPEED.getName(), new AttributeModifier(ATTACK_SPEED_MODIFIER, "Tool modifier", this.attackSpeed, 0));
    hashMultimap.put(SharedMonsterAttributes.ATTACK_DAMAGE.getName(), new AttributeModifier(Item.ATTACK_DAMAGE_MODIFIER, "Tool modifier", dmg, 0));
    return (Multimap<String, AttributeModifier>)hashMultimap;
  }
  
  public boolean hitEntity(ItemStack stack, EntityLivingBase target, EntityLivingBase source) {
    if (!isActive(stack))
      return true; 
    if (IC2.platform.isSimulating()) {
      drainSaber(stack, 400.0D, source);
      if (!(source instanceof EntityPlayerMP) || !(target instanceof EntityPlayer) || ((EntityPlayerMP)source).canAttackPlayer((EntityPlayer)target))
        for (EntityEquipmentSlot slot : ArmorSlot.getAll()) {
          if (!ElectricItem.manager.canUse(stack, 2000.0D))
            break; 
          ItemStack armor = target.getItemStackFromSlot(slot);
          if (armor == null)
            continue; 
          double amount = 0.0D;
          if (armor.getItem() instanceof ic2.core.item.armor.ItemArmorNanoSuit) {
            amount = 48000.0D;
          } else if (armor.getItem() instanceof ic2.core.item.armor.ItemArmorQuantumSuit) {
            amount = 300000.0D;
          } 
          if (amount > 0.0D) {
            ElectricItem.manager.discharge(armor, amount, this.tier, true, false, false);
            if (!ElectricItem.manager.canUse(armor, 1.0D))
              target.setItemStackToSlot(slot, null); 
            drainSaber(stack, 2000.0D, source);
          } 
        }  
    } 
    if (IC2.platform.isRendering())
      IC2.platform.playSoundSp(getRandomSwingSound(), 1.0F, 1.0F); 
    return true;
  }
  
  public String getRandomSwingSound() {
    switch (IC2.random.nextInt(3)) {
      default:
        return "Tools/Nanosabre/NanosabreSwing1.ogg";
      case 1:
        return "Tools/Nanosabre/NanosabreSwing2.ogg";
      case 2:
        break;
    } 
    return "Tools/Nanosabre/NanosabreSwing3.ogg";
  }
  
  public boolean canDestroyBlockInCreative(World world, BlockPos pos, ItemStack stack, EntityPlayer player) {
    return false;
  }
  
  public boolean onBlockStartBreak(ItemStack stack, BlockPos pos, EntityPlayer player) {
    if (isActive(stack))
      drainSaber(stack, 80.0D, (EntityLivingBase)player); 
    return false;
  }
  
  public boolean isFull3D() {
    return true;
  }
  
  public static void drainSaber(ItemStack stack, double amount, EntityLivingBase entity) {
    if (!ElectricItem.manager.use(stack, amount, entity)) {
      NBTTagCompound nbt = StackUtil.getOrCreateNbtData(stack);
      setActive(nbt, false);
    } 
  }
  
  public ActionResult<ItemStack> onItemRightClick(World world, EntityPlayer player, EnumHand hand) {
    ItemStack stack = StackUtil.get(player, hand);
    if (world.isRemote)
      return new ActionResult(EnumActionResult.PASS, stack); 
    NBTTagCompound nbt = StackUtil.getOrCreateNbtData(stack);
    if (isActive(nbt)) {
      setActive(nbt, false);
      return new ActionResult(EnumActionResult.SUCCESS, stack);
    } 
    if (ElectricItem.manager.canUse(stack, 32.0D)) {
      setActive(nbt, true);
      return new ActionResult(EnumActionResult.SUCCESS, stack);
    } 
    return super.onItemRightClick(world, player, hand);
  }
  
  public void onUpdate(ItemStack stack, World world, Entity entity, int slot, boolean par5) {
    super.onUpdate(stack, world, entity, slot, (par5 && isActive(stack)));
    if (!isActive(stack))
      return; 
    if (ticker % 16 == 0 && entity instanceof EntityPlayerMP)
      if (slot < 9) {
        drainSaber(stack, 64.0D, (EntityLivingBase)entity);
      } else if (ticker % 64 == 0) {
        drainSaber(stack, 32.0D, (EntityLivingBase)entity);
      }  
  }
  
  @SideOnly(Side.CLIENT)
  public EnumRarity getRarity(ItemStack stack) {
    return EnumRarity.UNCOMMON;
  }
  
  private static boolean isActive(ItemStack stack) {
    NBTTagCompound nbt = StackUtil.getOrCreateNbtData(stack);
    return isActive(nbt);
  }
  
  private static boolean isActive(NBTTagCompound nbt) {
    return nbt.getBoolean("active");
  }
  
  private static void setActive(NBTTagCompound nbt, boolean active) {
    nbt.setBoolean("active", active);
  }
  
  public boolean onEntitySwing(EntityLivingBase entity, ItemStack stack) {
    if (IC2.platform.isRendering() && isActive(stack))
      IC2.audioManager.playOnce(entity, PositionSpec.Hand, getRandomSwingSound(), true, IC2.audioManager.getDefaultVolume()); 
    return false;
  }
  
  protected String getIdleSound(EntityLivingBase player, ItemStack stack) {
    return "Tools/Nanosabre/NanosabreIdle.ogg";
  }
  
  protected String getStartSound(EntityLivingBase player, ItemStack stack) {
    return "Tools/Nanosabre/NanosabrePowerup.ogg";
  }
  
  public static int ticker = 4;
  
  private int soundTicker;
}
