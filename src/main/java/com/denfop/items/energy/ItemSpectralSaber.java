package com.denfop.items.energy;

import com.denfop.Constants;
import com.denfop.IUCore;
import com.denfop.api.IModelRegister;
import com.denfop.utils.EnumInfoUpgradeModules;
import com.denfop.utils.ModUtils;
import com.google.common.collect.HashMultimap;
import com.google.common.collect.Multimap;
import ic2.api.item.ElectricItem;
import ic2.api.item.IBoxable;
import ic2.api.item.IElectricItem;
import ic2.api.item.IItemHudInfo;
import ic2.core.IC2;
import ic2.core.audio.PositionSpec;
import ic2.core.init.BlocksItems;
import ic2.core.init.Localization;
import ic2.core.item.armor.ItemArmorNanoSuit;
import ic2.core.item.armor.ItemArmorQuantumSuit;
import ic2.core.item.tool.HarvestLevel;
import ic2.core.item.tool.IToolClass;
import ic2.core.item.tool.ToolClass;
import ic2.core.slot.ArmorSlot;
import ic2.core.util.StackUtil;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.renderer.block.model.ModelBakery;
import net.minecraft.client.renderer.block.model.ModelResourceLocation;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.enchantment.Enchantment;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.SharedMonsterAttributes;
import net.minecraft.entity.ai.attributes.AttributeModifier;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.init.Enchantments;
import net.minecraft.init.MobEffects;
import net.minecraft.inventory.EntityEquipmentSlot;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.ItemTool;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.potion.PotionEffect;
import net.minecraft.util.ActionResult;
import net.minecraft.util.EnumActionResult;
import net.minecraft.util.EnumHand;
import net.minecraft.util.NonNullList;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.client.model.ModelLoader;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import java.util.EnumSet;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

public class ItemSpectralSaber extends ItemTool implements IElectricItem, IBoxable, IItemHudInfo, IModelRegister {

    public static final int ticker = 0;
    public static int activedamage;
    private static int damage1;
    public final int maxCharge;
    public final int transferLimit;
    public final int tier;
    private final EnumSet<ToolClass> toolClasses;
    private int soundTicker;
    private final String name;

    public ItemSpectralSaber(
            String internalName, int maxCharge, int transferLimit, int tier, int activedamage,
            int damage
    ) {
        this(internalName, HarvestLevel.Diamond, maxCharge, transferLimit, tier, activedamage, damage);
    }

    public ItemSpectralSaber(
            String name, HarvestLevel harvestLevel, int maxCharge,
            int transferLimit, int tier, int activedamage, int damage
    ) {
        super(0, harvestLevel.toolMaterial.getAttackDamage(), harvestLevel.toolMaterial, null);
        this.soundTicker = 0;
        this.toolClasses = EnumSet.of(ToolClass.Sword);
        this.maxCharge = maxCharge;
        this.transferLimit = transferLimit;
        this.tier = tier;
        this.name = name;
        ItemSpectralSaber.activedamage = activedamage;
        damage1 = damage;
        setMaxDamage(27);
        setMaxStackSize(1);
        setNoRepair();
        setUnlocalizedName(name);
        setCreativeTab(IUCore.EnergyTab);
        BlocksItems.registerItem((Item) this, IUCore.getIdentifier(name)).setUnlocalizedName(name);
        IUCore.proxy.addIModelRegister(this);


    }

    @Override
    public void getSubItems(final CreativeTabs subs, final NonNullList<ItemStack> items) {
        if (this.isInCreativeTab(subs)) {
            ItemStack stack = new ItemStack(this, 1);


            ElectricItem.manager.charge(stack, 2.147483647E9D, 2147483647, true, false);
            items.add(stack);
            ItemStack itemstack = new ItemStack(this, 1, getMaxDamage());
            items.add(itemstack);
        }
    }

    public boolean canHarvestBlock(IBlockState state, ItemStack itemStack) {
        Material material = state.getMaterial();
        Iterator var4 = this.toolClasses.iterator();

        IToolClass toolClass;
        do {
            if (!var4.hasNext()) {
                return super.canHarvestBlock(state, itemStack);
            }

            toolClass = (IToolClass) var4.next();
            if (toolClass.getBlacklist().contains(state.getBlock())) {
                return false;
            }

            if (toolClass.getBlacklist().contains(material)) {
                return false;
            }

            if (toolClass.getWhitelist().contains(state.getBlock())) {
                return true;
            }
        } while (!toolClass.getWhitelist().contains(material));

        return true;
    }

    public static void drainSaber(ItemStack itemStack, double amount, EntityLivingBase entity) {
        NBTTagCompound nbt = ModUtils.nbt(itemStack);
        int saberenergy = 0;
        for (int i = 0; i < 4; i++) {
            if (nbt.getString("mode_module" + i).equals("saberenergy")) {
                saberenergy++;
            }

        }
        saberenergy = Math.min(saberenergy, EnumInfoUpgradeModules.SABERENERGY.max);

        if (!ElectricItem.manager.use(itemStack, amount - amount * 0.15 * saberenergy, entity)) {
            NBTTagCompound nbtData = StackUtil.getOrCreateNbtData(itemStack);
            nbtData.setBoolean("active", false);
            updateAttributes(nbtData);
        }
    }

    private static void updateAttributes(NBTTagCompound nbtData) {
        boolean active = nbtData.getBoolean("active");

        int saberdamage = 0;
        for (int i = 0; i < 4; i++) {
            if (nbtData.getString("mode_module" + i).equals("saberdamage")) {
                saberdamage++;
            }

        }
        saberdamage = Math.min(saberdamage, EnumInfoUpgradeModules.SABER_DAMAGE.max);


        int damage = (int) (damage1 + damage1 * 0.15 * saberdamage);
        if (active) {
            damage = (int) (activedamage + activedamage * 0.15 * saberdamage);
        }
        NBTTagCompound entry = new NBTTagCompound();
        entry.setString("AttributeName", SharedMonsterAttributes.ATTACK_DAMAGE.getName());
        entry.setLong("UUIDMost", ATTACK_DAMAGE_MODIFIER.getMostSignificantBits());
        entry.setLong("UUIDLeast", ATTACK_DAMAGE_MODIFIER.getLeastSignificantBits());
        entry.setString("Name", "Tool modifier");
        entry.setDouble("Amount", damage);
        entry.setInteger("Operation", 0);
        NBTTagList list = new NBTTagList();
        list.appendTag(entry);
        nbtData.setTag("AttributeModifiers", list);
    }


    @Override
    public double getMaxCharge(ItemStack itemStack) {
        return this.maxCharge;
    }

    public boolean isBookEnchantable(ItemStack stack, ItemStack book) {
        return true;
    }


    @Override
    public boolean canProvideEnergy(ItemStack itemStack) {
        return true;
    }

    public int getItemEnchantability() {
        return 0;
    }


    public float getDestroySpeed(ItemStack itemStack, IBlockState state) {

        NBTTagCompound nbtData = StackUtil.getOrCreateNbtData(itemStack);
        if (nbtData.getBoolean("active")) {
            this.soundTicker++;
            if (this.soundTicker % 4 == 0) {
                IC2.platform.playSoundSp(getRandomSwingSound(), 1.0F, 1.0F);
            }
            return 4.0F;
        }
        return 1.0F;
    }

    @Override
    public Multimap<String, AttributeModifier> getAttributeModifiers(
            final EntityEquipmentSlot p_getAttributeModifiers_1_,
            final ItemStack stack
    ) {
        NBTTagCompound nbt = ModUtils.nbt(stack);
        int saberdamage = 0;
        for (int i = 0; i < 4; i++) {
            if (nbt.getString("mode_module" + i).equals("saberdamage")) {
                saberdamage++;
            }

        }
        saberdamage = Math.min(saberdamage, EnumInfoUpgradeModules.SABER_DAMAGE.max);

        int dmg = (int) (damage1 + damage1 * 0.15 * saberdamage);
        if (ElectricItem.manager.canUse(stack, 400.0D)) {
            NBTTagCompound nbtData = StackUtil.getOrCreateNbtData(stack);
            if (nbtData.getBoolean("active")) {
                dmg = (int) (activedamage + activedamage * 0.15 * saberdamage);
            }
        }
        HashMultimap<String, AttributeModifier> hashMultimap = HashMultimap.create();
        hashMultimap.put(
                SharedMonsterAttributes.ATTACK_DAMAGE.getName(),
                new AttributeModifier(ATTACK_DAMAGE_MODIFIER, "Tool modifier", dmg, 0)
        );
        return hashMultimap;
    }


    @Override
    public boolean hitEntity(ItemStack stack, EntityLivingBase target, EntityLivingBase source) {
        NBTTagCompound nbtData = StackUtil.getOrCreateNbtData(stack);
        if (!nbtData.getBoolean("active")) {
            return true;
        }
        if (IC2.platform.isSimulating()) {
            drainSaber(stack, 400.0D, source);
            NBTTagCompound nbt = ModUtils.nbt(stack);
            int vampires = 0;
            boolean wither = false;
            boolean poison = false;
            for (int i = 0; i < 4; i++) {
                if (nbt.getString("mode_module" + i).equals("vampires")) {
                    vampires++;
                }
                if (nbt.getString("mode_module" + i).equals("wither")) {
                    wither = true;
                }
                if (nbt.getString("mode_module" + i).equals("poison")) {
                    poison = true;
                }
            }
            vampires = Math.min(vampires, EnumInfoUpgradeModules.VAMPIRES.max);
            if (vampires != 0) {
                target.addPotionEffect(new PotionEffect(MobEffects.REGENERATION, 40, vampires));
            }
            if (wither) {
                target.addPotionEffect(new PotionEffect(MobEffects.WITHER, 60));
            }
            if (poison) {
                target.addPotionEffect(new PotionEffect(MobEffects.POISON, 60));
            }


            Iterator<EntityEquipmentSlot> var4 = null;
            if (!(source instanceof EntityPlayer) || ((EntityPlayerMP) source).canAttackPlayer((EntityPlayer) target)
                    || !(target instanceof EntityPlayer)) {
                var4 = ArmorSlot.getAll().iterator();
            }
            while (var4.hasNext()) {
                EntityEquipmentSlot slot = var4.next();
                if (!ElectricItem.manager.canUse(stack, 2000.0D)) {
                    break;
                }

                ItemStack armor = target.getItemStackFromSlot(slot);
                if (!armor.isEmpty()) {
                    double amount = 0.0D;
                    if (armor.getItem() instanceof ItemArmorNanoSuit) {
                        amount = 48000.0D;
                    } else if (armor.getItem() instanceof ItemArmorQuantumSuit) {
                        amount = 300000.0D;
                    }

                    if (amount > 0.0D) {
                        ElectricItem.manager.discharge(armor, amount, this.tier, true, false, false);
                        if (!ElectricItem.manager.canUse(armor, 1.0D)) {
                            target.setItemStackToSlot(slot, null);
                        }

                        drainSaber(stack, 2000.0D, source);
                    }
                }
            }

        }
        if (IC2.platform.isRendering()) {
            IC2.platform.playSoundSp(getRandomSwingSound(), 1.0F, 1.0F);
        }
        return true;
    }

    public String getRandomSwingSound() {
        switch(IC2.random.nextInt(3)) {
            case 1:
                return "Tools/Nanosabre/NanosabreSwing2.ogg";
            case 2:
                return "Tools/Nanosabre/NanosabreSwing3.ogg";
            default:
                return "Tools/Nanosabre/NanosabreSwing1.ogg";
        }
    }

    @Override
    public boolean onBlockStartBreak(ItemStack stack, BlockPos pos, EntityPlayer player) {
        if (isActive(stack)) {
            drainSaber(stack, 80.0D, player);
        }

        return false;
    }

    private static boolean isActive(ItemStack stack) {
        NBTTagCompound nbt = StackUtil.getOrCreateNbtData(stack);
        return isActive(nbt);
    }

    private static boolean isActive(NBTTagCompound nbt) {
        return nbt.getBoolean("active");
    }
    public boolean onEntitySwing(EntityLivingBase entity, ItemStack stack) {
        if (IC2.platform.isRendering() && isActive(stack)) {
            IC2.audioManager.playOnce(entity, PositionSpec.Hand, this.getRandomSwingSound(), true, IC2.audioManager.getDefaultVolume());
        }

        return false;
    }

    protected String getIdleSound(EntityLivingBase player, ItemStack stack) {
        return "Tools/Nanosabre/NanosabreIdle.ogg";
    }

    protected String getStartSound(EntityLivingBase player, ItemStack stack) {
        return "Tools/Nanosabre/NanosabrePowerup.ogg";
    }
    @Override
    public boolean isFull3D() {
        return true;
    }

    @Override
    public ActionResult<ItemStack> onItemRightClick(World world, EntityPlayer player, EnumHand hand) {
        ItemStack stack = StackUtil.get(player, hand);
        if (world.isRemote) {
            return new ActionResult(EnumActionResult.PASS, stack);
        } else {
            NBTTagCompound nbt = StackUtil.getOrCreateNbtData(stack);
            if (isActive(nbt)) {
                setActive(nbt, false);
                return new ActionResult(EnumActionResult.SUCCESS, stack);
            } else if (ElectricItem.manager.canUse(stack, 16.0D)) {
                setActive(nbt, true);
                return new ActionResult(EnumActionResult.SUCCESS, stack);
            } else {
                return super.onItemRightClick(world, player, hand);
            }
        }
    }

    private static void setActive(NBTTagCompound nbt, boolean active) {
        nbt.setBoolean("active", active);
    }

    @Override
    public void onUpdate(ItemStack itemStack, World world, Entity entity, int slot, boolean par5) {
        NBTTagCompound nbtData = StackUtil.getOrCreateNbtData(itemStack);
        if (!nbtData.getBoolean("active")) {
            return;
        }
        int loot = 0;
        int fire = 0;
        for (int i = 0; i < 4; i++) {
            if (nbtData.getString("mode_module" + i).equals("loot")) {
                loot++;
            }
            if (nbtData.getString("mode_module" + i).equals("fire")) {
                fire++;
            }

        }
        loot = Math.min(loot, EnumInfoUpgradeModules.LOOT.max);
        fire = Math.min(fire, EnumInfoUpgradeModules.FIRE.max);
        Map<Enchantment, Integer> enchantmentMap = EnchantmentHelper.getEnchantments(itemStack);
        if (loot != 0) {
            enchantmentMap.put(Enchantments.LOOTING, loot);
        }
        if (fire != 0) {
            enchantmentMap.put(Enchantments.FIRE_ASPECT, fire);
        }
        EnchantmentHelper.setEnchantments(enchantmentMap, itemStack);
        if (ticker % 16 == 0 && entity instanceof net.minecraft.entity.player.EntityPlayerMP) {
            if (slot < 9) {
                drainSaber(itemStack, 64.0D, (EntityLivingBase) entity);
            } else if (ticker % 64 == 0) {
                drainSaber(itemStack, 16.0D, (EntityLivingBase) entity);
            }
        }
    }

    @Override
    public String getUnlocalizedName() {
        return "iu" + super.getUnlocalizedName().substring(4);
    }

    @Override
    public String getUnlocalizedName(ItemStack itemStack) {
        return getUnlocalizedName();
    }

    @Override
    public String getItemStackDisplayName(ItemStack itemStack) {
        return Localization.translate(getUnlocalizedName(itemStack));
    }


    @Override
    public int getTier(ItemStack itemStack) {

        return this.tier;
    }

    @Override
    public double getTransferLimit(ItemStack itemStack) {

        return this.transferLimit;
    }


    public boolean canBeStoredInToolbox(ItemStack itemstack) {
        return true;
    }

    public boolean isRepairable() {
        return false;
    }

    @Override
    public List<String> getHudInfo(final ItemStack itemStack, final boolean b) {
        List<String> info = new LinkedList<>();
        info.add(ElectricItem.manager.getToolTip(itemStack));
        info.add(Localization.translate("ic2.item.tooltip.PowerTier") + " " + this.tier);
        return info;
    }

    @Override
    public void registerModels() {
        registerModels(this.name);
    }

    @SideOnly(Side.CLIENT)
    public static ModelResourceLocation getModelLocation1(String name, String extraName) {
        StringBuilder loc = new StringBuilder();
        loc.append(Constants.MOD_ID);
        loc.append(':');
        loc.append("energy_tools").append("/").append(name + extraName);

        return new ModelResourceLocation(loc.toString(), null);
    }

    @SideOnly(Side.CLIENT)
    public void registerModels(final String name) {
        ModelLoader.setCustomMeshDefinition(this, stack -> {
            final NBTTagCompound nbt = ModUtils.nbt(stack);

            return getModelLocation1(name, nbt.getBoolean("active") ? "_active" : "_off");
        });
        String[] mode = {"_off", "_active"};
        for (final String s : mode) {
            ModelBakery.registerItemVariants(this, getModelLocation1(name, s));
        }

    }

}
