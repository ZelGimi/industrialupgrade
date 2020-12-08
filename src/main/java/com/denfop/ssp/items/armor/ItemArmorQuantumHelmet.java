package com.denfop.ssp.items.armor;



import java.util.Locale;

import net.minecraft.util.ActionResult;
import net.minecraft.util.DamageSource;
import net.minecraft.util.EnumActionResult;
import net.minecraft.item.EnumRarity;
import java.util.List;
import ic2.core.item.ElectricItemManager;
import ic2.core.item.ItemTinCan;
import ic2.core.ref.IItemModelProvider;
import ic2.core.ref.ItemName;
import net.minecraft.util.NonNullList;
import net.minecraft.util.math.BlockPos;
import ic2.api.item.HudMode;
import java.util.Iterator;
import java.util.LinkedList;

import ic2.core.util.StackUtil;
import net.minecraft.entity.EntityLivingBase;
import ic2.api.item.ElectricItem;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.MobEffects;
import net.minecraft.world.World;
import net.minecraft.nbt.NBTBase;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.potion.Potion;
import net.minecraft.potion.PotionEffect;
import net.minecraft.entity.Entity;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import net.minecraftforge.client.model.ModelLoader;
import net.minecraft.client.renderer.block.model.ModelResourceLocation;

import com.denfop.ssp.Configs;
import com.denfop.ssp.keyboard.SSPKeys;
import com.google.common.base.CaseFormat;
import ic2.core.init.Localization;
import net.minecraft.item.ItemStack;
import net.minecraft.creativetab.CreativeTabs;
import ic2.core.IC2;
import ic2.core.IC2Potion;
import net.minecraft.item.Item;
import ic2.core.init.BlocksItems;
import net.minecraft.util.ResourceLocation;
import net.minecraft.inventory.EntityEquipmentSlot;
import ic2.api.item.IItemHudProvider;
import net.minecraftforge.common.ISpecialArmor;
import ic2.api.item.IMetalArmor;
import ic2.api.item.IElectricItem;
import net.minecraft.item.ItemArmor;
import java.util.Map;
public class ItemArmorQuantumHelmet extends ItemArmor implements IItemModelProvider, IElectricItem, IMetalArmor, ISpecialArmor, IItemHudProvider
{
    protected static final int DEFAULT_COLOUR = -1;
    protected final SolarHelmetTypes type;
    public static boolean chargeWholeInventory;
    
    
    protected int ticker;
    public ItemArmorQuantumHelmet(final SolarHelmetTypes type) {
        super(ItemArmor.ArmorMaterial.DIAMOND, -1, EntityEquipmentSlot.HEAD);
        ((ItemArmorQuantumHelmet)BlocksItems.registerItem((Item)this, new ResourceLocation("super_solar_panels", type.getName()))).setUnlocalizedName(type.getLocalisedName());
        this.setCreativeTab((CreativeTabs)IC2.tabIC2);
        this.setMaxDamage(27);
        this.type = type;
       
    }
    
    public String func_77658_a() {
        return "super_solar_panels." + super.getUnlocalizedName().substring(5);
    }
    
    public String getUnlocalizedName(final ItemStack stack) {
        return this.getUnlocalizedName();
    }
    
    public String getItemStackDisplayName(final ItemStack stack) {
        return Localization.translate(this.getUnlocalizedName(stack));
    }
    
    public int getMetadata(final ItemStack stack) {
        return 0;
    }
    
    @SideOnly(Side.CLIENT)
    public void registerModels(final ItemName name) {
        ModelLoader.setCustomModelResourceLocation((Item)this, 0, new ModelResourceLocation("super_solar_panels:" + CaseFormat.LOWER_CAMEL.to(CaseFormat.LOWER_UNDERSCORE, this.type.getName()), (String)null));
    }
    
    public String getArmorTexture(final ItemStack stack, final Entity entity, final EntityEquipmentSlot slot, final String type) {
        return "super_solar_panels:textures/armour/" + this.type.getName() + ((type != null) ? "Overlay" : "") + ".png";
    }
    
   
    public boolean canBeDyed() {
        return this.type != SolarHelmetTypes.Helmet;
    }
    public void func_82813_b(final ItemStack stack, final int colour) {
        this.getDisplayNbt(stack, true).setInteger("colour", colour);
    }

    
    public boolean func_82816_b_(final ItemStack stack) {
        return this.func_82814_b(stack) != -1;
    }
    
    public int func_82814_b(final ItemStack stack) {
        final NBTTagCompound nbt = this.getDisplayNbt(stack, false);
        if (nbt == null || !nbt.hasKey("colour", 3)) {
            return -1;
        }
        return nbt.getInteger("colour");
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
        if (!nbt.hasKey("display", 50)) {
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
  
    public void onArmorTick(final World world, final EntityPlayer player, final ItemStack stack) {
        if (this.HUDstuff(world.isRemote, player, stack)) {
            return;
        }
        if (this.ticker++ % this.tickRate() == 0) {
            this.checkTheSky(world, player.getPosition());
        }
        if (this.type != SolarHelmetTypes.Helmet) {
            final int airLevel = player.getAir();
            if (ElectricItem.manager.canUse(stack, 1000.0) && airLevel < 100) {
                player.setAir(airLevel + 200);
                ElectricItem.manager.use(stack, 1000.0, (EntityLivingBase)player);
            }
        }
        int output = 0;
       
        for (final ItemStack playerStack : player.inventory.armorInventory.subList(0, player.inventory.armorInventory.size() - 1)) {
            if (!StackUtil.isEmpty(playerStack) && playerStack.getItem() instanceof IElectricItem) {
                output -= (int)ElectricItem.manager.charge(playerStack, (double)output, this.type.tier, false, false);
                if (output <= 0) {
                    return;
                }
                continue;
            }
        }
        if (ItemArmorQuantumHelmet.chargeWholeInventory) {
            for (final ItemStack playerStack : player.inventory.offHandInventory) {
                if (!StackUtil.isEmpty(playerStack) && playerStack.getItem() instanceof IElectricItem) {
                    output -= (int)ElectricItem.manager.charge(playerStack, (double)output, this.type.tier, false, false);
                    if (output <= 0) {
                        return;
                    }
                    continue;
                }
            }
            for (final ItemStack playerStack : player.inventory.mainInventory) {
                if (!StackUtil.isEmpty(playerStack) && playerStack.getItem() instanceof IElectricItem) {
                    output -= (int)ElectricItem.manager.charge(playerStack, (double)output, this.type.tier, false, false);
                    if (output <= 0) {
                        return;
                    }
                    continue;
                }
            }
        }
        final NBTTagCompound nbtData = StackUtil.getOrCreateNbtData(stack);
        byte toggleTimer = nbtData.getByte("toggleTimer");
        boolean ret = false;
        ElectricItem.manager.charge(stack, (double)output, Integer.MAX_VALUE, true, false);
        final int air = player.getAir();
        if (ElectricItem.manager.canUse(stack, 1000.0) && air < 100) {
            player.setAir(air + 200);
            ElectricItem.manager.use(stack, 1000.0, null);
            ret = true;
        }
        else if (air <= 0) {
            IC2.achievements.issueAchievement(player, "starveWithQHelmet");
        }
        if (ElectricItem.manager.canUse(stack, 1000.0) && player.getFoodStats().needFood()) {
            int slot = -1;
            for (int i = 0; i < player.inventory.mainInventory.size(); ++i) {
                final ItemStack playerStack = (ItemStack)player.inventory.mainInventory.get(i);
                
            }
            if (slot > -1) {
                ItemStack playerStack2 = (ItemStack)player.inventory.mainInventory.get(slot);
                final ItemTinCan can = (ItemTinCan)playerStack2.getItem();
                final ActionResult<ItemStack> result = can.onEaten(player, playerStack2);
                playerStack2 = (ItemStack)result.getResult();
                if (StackUtil.isEmpty(playerStack2)) {
                    player.inventory.mainInventory.set(slot, (ItemStack)StackUtil.emptyStack);
                }
                if (result.getType() == EnumActionResult.SUCCESS) {
                    ElectricItem.manager.use(stack, 1000.0, null);
                }
                ret = true;
            }
        }
        else if (player.getFoodStats().getFoodLevel() <= 0) {
            IC2.achievements.issueAchievement(player, "starveWithQHelmet");
        }
        for (final Object effect : new LinkedList<Object>(player.getActivePotionEffects())) {
            final Potion potion = ((PotionEffect) effect).getPotion();
           
            
        }
        boolean Nightvision = nbtData.getBoolean("Nightvision");
        short hubmode = nbtData.getShort("HudMode");
        if (SSPKeys.Isremovepoison(player) && toggleTimer == 0) {
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
                
            	player.removeActivePotionEffect(MobEffects.NIGHT_VISION);
            }
            else {
               
              
                    player.addPotionEffect(new PotionEffect(MobEffects.NIGHT_VISION, 300, 0, true, true));
                  
            }  
            }
            ret = true;
        
      
        player.removePotionEffect(MobEffects.POISON);
        player.removePotionEffect(MobEffects.UNLUCK);
        player.removePotionEffect(MobEffects.WITHER);
        
     //   potionRemovalCost.put(IC2Potion.radiation, Integer.valueOf(10000));
  //      IC2.platform.removePotion((EntityLivingBase)player, MobEffects.WITHER);
     //   IC2.platform.removePotion((EntityLivingBase)player, MobEffects.POISON);
    }
    
    protected boolean HUDstuff(final boolean isRemote, final EntityPlayer player, final ItemStack stack) {
        final NBTTagCompound nbt = StackUtil.getOrCreateNbtData(stack);
        byte toggleTimer = nbt.getByte("toggleTimer");
        if (IC2.keyboard.isAltKeyDown(player) && IC2.keyboard.isHudModeKeyDown(player) && toggleTimer == 0) {
            byte hubmode = nbt.getByte("hudMode");
            toggleTimer = 10;
            if (hubmode == HudMode.getMaxMode()) {
                hubmode = 0;
            }
            else {
                ++hubmode;
            }
            if (!isRemote) {
                nbt.setByte("hudMode", hubmode);
                IC2.platform.messagePlayer(player, Localization.translate(HudMode.getFromID((int)hubmode).getTranslationKey()), new Object[0]);
            }
        }
        if (!isRemote && toggleTimer > 0) {
            final NBTTagCompound nbtTagCompound = nbt;
            final String s = "toggleTimer";
            --toggleTimer;
            nbtTagCompound.setByte(s, toggleTimer);
        }
        return isRemote;
    }
    
    protected int tickRate() {
        return 128;
    }
    
    public void checkTheSky(final World world, final BlockPos pos) {
        if (!world.provider.hasSkyLight() && world.canBlockSeeSky(pos)) {
            if (world.isDaytime() && ((!world.getBiome(pos).canRain() && world.getBiome(pos).getRainfall() <= 0.0f) || (!world.isRaining() && !world.isThundering()))) {
                
            }
            else {
               
            }
        }
        else {
            
        }
    }
    
    public void getSubItems(final CreativeTabs tab, final NonNullList<ItemStack> items) {
        if (this.isInCreativeTab(tab)) {
            ElectricItemManager.addChargeVariants((Item)this, (List)items);
        }
    }
    
    public EnumRarity getRarity(final ItemStack stack) {
        return this.type.rarity;
    }
    
    public boolean isMetalArmor(final ItemStack stack, final EntityPlayer player) {
        return true;
    }
    
    public int getItemEnchantability() {
        return 0;
    }
    
    public boolean getIsRepairable(final ItemStack toRepair, final ItemStack repair) {
        return false;
    }
    
    public ISpecialArmor.ArmorProperties getProperties(final EntityLivingBase player, final ItemStack armour, final DamageSource source, final double damage, final int slot) {
        if (source.isUnblockable()) {
            return new ISpecialArmor.ArmorProperties(0, 0.0, 0);
        }
        return new ISpecialArmor.ArmorProperties(0, 0.15 * this.type.damageAbsorptionRatio, (int)(25.0 * ElectricItem.manager.getCharge(armour) / this.type.energyPerDamage));
    }
    
    public int getArmorDisplay(final EntityPlayer player, final ItemStack armour, final int slot) {
        if (ElectricItem.manager.getCharge(armour) >= this.type.energyPerDamage) {
            return (int)Math.round(3.0 * this.type.damageAbsorptionRatio);
        }
        return 0;
    }
    
    public void damageArmor(final EntityLivingBase entity, final ItemStack stack, final DamageSource source, final int damage, final int slot) {
        ElectricItem.manager.discharge(stack, (double)(damage * this.type.energyPerDamage), Integer.MAX_VALUE, true, false, false);
    }
    
    public boolean canProvideEnergy(final ItemStack stack) {
        return false;
    }
    
    public int getTier(final ItemStack stack) {
        return this.type.tier;
    }
    
    public double getMaxCharge(final ItemStack stack) {
        return this.type.maxCharge;
    }
    
    public double getTransferLimit(final ItemStack stack) {
        return this.type.transferLimit;
    }
    
    public boolean doesProvideHUD(final ItemStack stack) {
        return ElectricItem.manager.getCharge(stack) > 0.0;
    }
    
    public HudMode getHudMode(final ItemStack stack) {
        return HudMode.getFromID((int)StackUtil.getOrCreateNbtData(stack).getByte("hudMode"));
    }
    
    static {
        ItemArmorQuantumHelmet.chargeWholeInventory = false;
    }
    
    public enum SolarHelmetTypes
    {
       Helmet(EnumRarity.EPIC,  Configs.tier4, Configs.maxCharge4, Configs.transferLimit4, 42000, 9);
         
        public final double maxCharge;
        public final double transferLimit;
        public final double damageAbsorptionRatio;
        public final int tier;
        public final int energyPerDamage;
        public final EnumRarity rarity;
        private final String name;
        
        private SolarHelmetTypes(final EnumRarity rarity, final int tier, final double maxCharge, final double transferLimit, final int energyPerDamage, final double damageAbsorptionRatio) {
            this.name = this.name().toLowerCase(Locale.ENGLISH);
            this.rarity = rarity;
            this.tier = tier;
            this.maxCharge = maxCharge;
            this.transferLimit = transferLimit;
            this.energyPerDamage = energyPerDamage;
            this.damageAbsorptionRatio = damageAbsorptionRatio;
            assert damageAbsorptionRatio > 0.0;
        }
        
        public String getName() {
            return this.name + "SolarHelmet";
        }
        
        protected String getLocalisedName() {
            return "solar_helmets." + this.name;
        }
    }
}
