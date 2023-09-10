package com.denfop.items.energy;

import com.denfop.Constants;
import com.denfop.ElectricItem;
import com.denfop.IUCore;
import com.denfop.Localization;
import com.denfop.api.IModelRegister;
import com.denfop.api.item.IEnergyItem;
import com.denfop.api.upgrade.EnumUpgrades;
import com.denfop.api.upgrade.IUpgradeItem;
import com.denfop.api.upgrade.UpgradeSystem;
import com.denfop.api.upgrade.event.EventItemLoad;
import com.denfop.audio.EnumSound;
import com.denfop.audio.SoundHandler;
import com.denfop.items.EnumInfoUpgradeModules;
import com.denfop.items.armour.special.ItemSpecialArmor;
import com.denfop.network.packet.PacketSoundPlayer;
import com.denfop.register.Register;
import com.denfop.utils.ModUtils;
import com.google.common.collect.HashMultimap;
import com.google.common.collect.Multimap;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.renderer.block.model.ModelBakery;
import net.minecraft.client.renderer.block.model.ModelResourceLocation;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.SharedMonsterAttributes;
import net.minecraft.entity.ai.attributes.AttributeModifier;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.init.MobEffects;
import net.minecraft.inventory.EntityEquipmentSlot;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.ItemTool;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.potion.PotionEffect;
import net.minecraft.util.ActionResult;
import net.minecraft.util.EnumActionResult;
import net.minecraft.util.EnumHand;
import net.minecraft.util.NonNullList;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.client.model.ModelLoader;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import javax.annotation.Nonnull;
import java.util.Arrays;
import java.util.Collections;
import java.util.Iterator;

public class ItemQuantumSaber extends ItemTool implements IEnergyItem, IUpgradeItem, IModelRegister {

    public static int ticker = 0;
    public final int maxCharge;
    public final int transferLimit;
    public final int tier;
    private final String name;
    private final int damage1;
    public int activedamage;
    private int soundTicker;


    public ItemQuantumSaber(
            String name, int maxCharge,
            int transferLimit, int tier, int activedamage1, int damage
    ) {
        super(0, 2, ToolMaterial.DIAMOND, Collections.emptySet());
        this.soundTicker = 0;
        this.setHarvestLevel("sword", 3);
        this.maxCharge = maxCharge;
        this.transferLimit = transferLimit;
        this.tier = tier;
        this.name = name;
        this.activedamage = activedamage1;
        this.damage1 = damage;
        setMaxStackSize(1);
        setNoRepair();
        setUnlocalizedName(name);
        setCreativeTab(IUCore.EnergyTab);
        Register.registerItem((Item) this, IUCore.getIdentifier(name)).setUnlocalizedName(name);
        IUCore.proxy.addIModelRegister(this);

        UpgradeSystem.system.addRecipe(this, EnumUpgrades.SABERS.list);
    }

    private static boolean isActive(ItemStack stack) {
        NBTTagCompound nbt = ModUtils.nbt(stack);
        return isActive(nbt);
    }

    private static boolean isActive(NBTTagCompound nbt) {
        return nbt.getBoolean("active");
    }

    private static void setActive(NBTTagCompound nbt, boolean active) {
        nbt.setBoolean("active", active);
    }

    @SideOnly(Side.CLIENT)
    public static ModelResourceLocation getModelLocation1(String name, String extraName) {
        final String loc = Constants.MOD_ID +
                ':' +
                "energy_tools" + "/" + name + extraName;

        return new ModelResourceLocation(loc, null);
    }

    public boolean showDurabilityBar(final ItemStack stack) {
        return true;
    }

    public int getRGBDurabilityForDisplay(ItemStack stack) {
        return ModUtils.convertRGBcolorToInt(33, 91, 199);
    }

    public double getDurabilityForDisplay(ItemStack stack) {
        return Math.min(
                Math.max(
                        1 - ElectricItem.manager.getCharge(stack) / ElectricItem.manager.getMaxCharge(stack),
                        0.0
                ),
                1.0
        );
    }

    public boolean isBookEnchantable(@Nonnull ItemStack stack, @Nonnull ItemStack book) {
        return false;
    }

    @Override
    public void getSubItems(@Nonnull final CreativeTabs subs, @Nonnull final NonNullList<ItemStack> items) {
        if (this.isInCreativeTab(subs)) {
            ItemStack stack = new ItemStack(this, 1);


            ElectricItem.manager.charge(stack, 2.147483647E9D, 2147483647, true, false);
            items.add(stack);
            ItemStack itemstack = new ItemStack(this, 1, 27);
            items.add(itemstack);
        }
    }

    public boolean canHarvestBlock(IBlockState state, @Nonnull ItemStack itemStack) {
        return true;
    }

    public int getItemEnchantability() {
        return 0;
    }

    public void drainSaber(ItemStack itemStack, double amount, EntityLivingBase entity) {
        int saberenergy = (UpgradeSystem.system.hasModules(EnumInfoUpgradeModules.SABERENERGY, itemStack) ?
                UpgradeSystem.system.getModules(EnumInfoUpgradeModules.SABERENERGY, itemStack).number : 0);


        if (!ElectricItem.manager.use(itemStack, amount - amount * 0.15 * saberenergy, entity)) {
            NBTTagCompound nbtData = ModUtils.nbt(itemStack);
            nbtData.setBoolean("active", false);
        }
    }

    @Override
    public double getMaxEnergy(ItemStack itemStack) {
        return this.maxCharge;
    }


    @Override
    public boolean canProvideEnergy(ItemStack itemStack) {
        return true;
    }

    public boolean onDroppedByPlayer(@Nonnull ItemStack item, @Nonnull EntityPlayer player) {
        this.removeAudioSource();
        return true;
    }

    public float getDestroySpeed(@Nonnull ItemStack itemStack, @Nonnull IBlockState state) {

        NBTTagCompound nbtData = ModUtils.nbt(itemStack);
        if (nbtData.getBoolean("active")) {
            this.soundTicker++;
            if (this.soundTicker % 4 == 0) {
                IUCore.proxy.playSoundSp(getRandomSwingSound(), 1.0F, 1.0F);
            }
            return 4.0F;
        }
        return 1.0F;
    }

    @Nonnull
    @Override
    public Multimap<String, AttributeModifier> getAttributeModifiers(
            @Nonnull final EntityEquipmentSlot p_getAttributeModifiers_1_,
            @Nonnull final ItemStack stack
    ) {
        if (p_getAttributeModifiers_1_ != EntityEquipmentSlot.MAINHAND) {
            return super.getAttributeModifiers(p_getAttributeModifiers_1_, stack);
        } else {
            int saberdamage = (UpgradeSystem.system.hasModules(EnumInfoUpgradeModules.SABER_DAMAGE, stack) ?
                    UpgradeSystem.system.getModules(EnumInfoUpgradeModules.SABER_DAMAGE, stack).number : 0);
            int dmg = (int) (damage1 + damage1 * 0.15 * saberdamage);
            if (ElectricItem.manager.canUse(stack, 400.0D)) {
                NBTTagCompound nbtData = ModUtils.nbt(stack);
                if (nbtData.getBoolean("active")) {
                    dmg = (int) (activedamage + activedamage * 0.15 * saberdamage);
                }
            }
            HashMultimap<String, AttributeModifier> hashMultimap = HashMultimap.create();
            hashMultimap.put(
                    SharedMonsterAttributes.ATTACK_SPEED.getName(),
                    new AttributeModifier(ATTACK_SPEED_MODIFIER, "Tool modifier", this.attackSpeed, 0)
            );
            hashMultimap.put(
                    SharedMonsterAttributes.ATTACK_DAMAGE.getName(),
                    new AttributeModifier(Item.ATTACK_DAMAGE_MODIFIER, "Tool modifier", dmg, 0)
            );


            return hashMultimap;
        }
    }

    @Override
    public boolean hitEntity(@Nonnull ItemStack stack, @Nonnull EntityLivingBase target, @Nonnull EntityLivingBase source) {
        NBTTagCompound nbtData = ModUtils.nbt(stack);
        if (!nbtData.getBoolean("active")) {
            return true;
        }
        if (IUCore.proxy.isSimulating()) {
            drainSaber(stack, 400.0D, source);
            int vampires = (UpgradeSystem.system.hasModules(EnumInfoUpgradeModules.VAMPIRES, stack) ?
                    UpgradeSystem.system.getModules(EnumInfoUpgradeModules.VAMPIRES, stack).number : 0);
            boolean wither = UpgradeSystem.system.hasModules(EnumInfoUpgradeModules.WITHER, stack);
            boolean poison = UpgradeSystem.system.hasModules(EnumInfoUpgradeModules.POISON, stack);


            if (vampires != 0) {
                target.addPotionEffect(new PotionEffect(MobEffects.REGENERATION, 40, vampires));
            }
            if (wither) {
                target.addPotionEffect(new PotionEffect(MobEffects.WITHER, 60));
            }
            if (poison) {
                target.addPotionEffect(new PotionEffect(MobEffects.POISON, 60));
            }
            if (UpgradeSystem.system.hasModules(EnumInfoUpgradeModules.HUNGRY, stack)) {
                target.addPotionEffect(new PotionEffect(MobEffects.HUNGER, 60));
            }

            Iterator<EntityEquipmentSlot> var4;
            if (!(source instanceof EntityPlayerMP) || !(target instanceof EntityPlayer) || ((EntityPlayerMP) source).canAttackPlayer(
                    (EntityPlayer) target)) {
                var4 = Arrays
                        .stream(EntityEquipmentSlot.values())
                        .filter(slot -> slot != EntityEquipmentSlot.MAINHAND && slot != EntityEquipmentSlot.OFFHAND)
                        .iterator();


                while (var4.hasNext()) {
                    EntityEquipmentSlot slot = var4.next();
                    if (!ElectricItem.manager.canUse(stack, 2000.0D)) {
                        break;
                    }

                    ItemStack armor = target.getItemStackFromSlot(slot);
                    if (armor.isEmpty()) {
                        double amount = 0.0D;
                        if (armor.getItem() instanceof ItemSpecialArmor) {
                            amount = ((ItemSpecialArmor) armor.getItem()).getArmor().getDamageEnergy();
                        }

                        if (amount > 0.0D) {
                            ElectricItem.manager.discharge(armor, amount, this.tier, true, false, false);
                            if (!ElectricItem.manager.canUse(armor, 1.0D)) {
                                target.setItemStackToSlot(slot, ItemStack.EMPTY);
                            }

                            drainSaber(stack, 2000.0D, source);
                        }
                    }
                }

            }
            if (source instanceof EntityPlayerMP) {
                new PacketSoundPlayer(getRandomSwingSound(), (EntityPlayer) source);
            }
        }

        return true;
    }

    public String getRandomSwingSound() {
        switch (IUCore.random.nextInt(3)) {
            default:
                return "nanosabreswing1";
            case 1:
                return "nanosabreswing2";
            case 2:
                break;
        }
        return "nanosabreswing3";
    }

    @Override
    public boolean onBlockStartBreak(@Nonnull ItemStack stack, @Nonnull BlockPos pos, @Nonnull EntityPlayer player) {
        if (isActive(stack)) {
            drainSaber(stack, 80.0D, player);
        }

        return false;
    }

    @Override
    public boolean isFull3D() {
        return true;
    }

    @Nonnull
    @Override
    public ActionResult<ItemStack> onItemRightClick(World world, @Nonnull EntityPlayer player, @Nonnull EnumHand hand) {
        ItemStack stack = ModUtils.get(player, hand);
        if (world.isRemote) {
            return new ActionResult<>(EnumActionResult.PASS, stack);
        } else {
            NBTTagCompound nbt = ModUtils.nbt(stack);
            if (isActive(nbt)) {
                setActive(nbt, false);
                SoundHandler.stopSound(EnumSound.NanosabreIdle);
                return new ActionResult<>(EnumActionResult.SUCCESS, stack);
            } else if (ElectricItem.manager.canUse(stack, 16.0D)) {
                setActive(nbt, true);
                new PacketSoundPlayer(this.getStartSound(), player);
                return new ActionResult<>(EnumActionResult.SUCCESS, stack);
            } else {
                return super.onItemRightClick(world, player, hand);
            }
        }
    }

    protected String getIdleSound() {
        return "NanosabreIdle";
    }

    protected String getStartSound() {
        return "NanosabrePowerup";
    }


    protected String getStopSound() {
        return null;
    }

    protected void removeAudioSource() {


    }

    @Override
    public void onUpdate(@Nonnull ItemStack itemStack, @Nonnull World world, @Nonnull Entity entity, int slot, boolean par5) {
        NBTTagCompound nbt = ModUtils.nbt(itemStack);

        if (!UpgradeSystem.system.hasInMap(itemStack)) {
            nbt.setBoolean("hasID", false);
            MinecraftForge.EVENT_BUS.post(new EventItemLoad(world, this, itemStack));
        }
        if (IUCore.proxy.isRendering() && isActive(itemStack) && world.provider.getWorldTime() % 20 == 0) {
            SoundHandler.playSound(IUCore.proxy.getPlayerInstance(), getIdleSound());
        }

        NBTTagCompound nbtData = ModUtils.nbt(itemStack);

        if (!nbtData.getBoolean("active")) {
            return;
        }
        if (ticker % 16 == 0 && entity instanceof net.minecraft.entity.player.EntityPlayerMP) {
            if (slot < 9) {
                drainSaber(itemStack, 64.0D, (EntityLivingBase) entity);
            } else if (ticker % 64 == 0) {
                drainSaber(itemStack, 16.0D, (EntityLivingBase) entity);
            }

        }
    }

    @Nonnull
    @Override
    public String getUnlocalizedName() {
        return "iu" + super.getUnlocalizedName().substring(4);
    }

    @Nonnull
    @Override
    public String getUnlocalizedName(@Nonnull ItemStack itemStack) {
        return getUnlocalizedName();
    }

    @Nonnull
    @Override
    public String getItemStackDisplayName(@Nonnull ItemStack itemStack) {
        return Localization.translate(getUnlocalizedName(itemStack));
    }

    @Override
    public short getTierItem(ItemStack itemStack) {

        return (short) this.tier;
    }

    @Override
    public double getTransferEnergy(ItemStack itemStack) {

        return this.transferLimit;
    }

    public boolean canBeStoredInToolbox(ItemStack itemstack) {
        return true;
    }

    public boolean isRepairable() {
        return false;
    }


    @Override
    public void registerModels() {
        registerModels(this.name);
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
