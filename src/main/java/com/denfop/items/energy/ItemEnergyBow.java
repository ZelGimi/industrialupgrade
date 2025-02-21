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
import com.denfop.items.EnumInfoUpgradeModules;
import com.denfop.network.packet.PacketSoundPlayer;
import com.denfop.register.Register;
import com.denfop.utils.KeyboardClient;
import com.denfop.utils.ModUtils;
import net.minecraft.client.renderer.block.model.ModelBakery;
import net.minecraft.client.renderer.block.model.ModelResourceLocation;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.projectile.EntityArrow;
import net.minecraft.init.Enchantments;
import net.minecraft.init.MobEffects;
import net.minecraft.item.IItemPropertyGetter;
import net.minecraft.item.Item;
import net.minecraft.item.ItemBow;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.potion.PotionEffect;
import net.minecraft.util.ActionResult;
import net.minecraft.util.EnumActionResult;
import net.minecraft.util.EnumHand;
import net.minecraft.util.NonNullList;
import net.minecraft.util.ResourceLocation;
import net.minecraft.world.World;
import net.minecraftforge.client.model.ModelLoader;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.entity.living.LivingHurtEvent;
import net.minecraftforge.event.entity.player.ArrowLooseEvent;
import net.minecraftforge.event.entity.player.ArrowNockEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import org.jetbrains.annotations.Nullable;
import org.lwjgl.input.Keyboard;

import javax.annotation.Nonnull;
import java.util.List;

public class ItemEnergyBow extends ItemBow implements IEnergyItem, IUpgradeItem, IModelRegister {


    static final int[] CHARGE = new int[]{1500, 750, 2000, 5000, 1000};

    static final String[] MODE = new String[]{"normal", "rapidfire", "spread", "sniper", "flame"};
    private final float type;
    private final String name;
    private final double nanoBowBoost;
    private final int tier;
    private final int transferenergy;
    private final int maxenergy;

    public ItemEnergyBow(String name, double nanoBowBoost, int tier, int transferenergy, int maxenergy, float type) {
        setFull3D();
        setCreativeTab(IUCore.EnergyTab);
        this.name = name;
        this.nanoBowBoost = nanoBowBoost;
        this.tier = tier;
        this.transferenergy = transferenergy;
        this.maxenergy = maxenergy;
        this.type = type;
        setUnlocalizedName(name);
        Register.registerItem((Item) this, IUCore.getIdentifier(name)).setUnlocalizedName(name);
        IUCore.proxy.addIModelRegister(this);
        this.addPropertyOverride(new ResourceLocation("pulling"), new IItemPropertyGetter() {
            @SideOnly(Side.CLIENT)
            public float apply(
                    @Nonnull ItemStack stack,
                    @javax.annotation.Nullable World worldIn,
                    @javax.annotation.Nullable EntityLivingBase entityIn
            ) {
                return entityIn != null && entityIn.isHandActive() && entityIn.getActiveItemStack() == stack ? 1.0F : 0.0F;
            }
        });
        MinecraftForge.EVENT_BUS.register(this);
        UpgradeSystem.system.addRecipe(this, EnumUpgrades.BOW.list);
    }

    public static float getArrowVelocity(int charge) {
        float f = charge / 20.0F;
        f = (f * f + f * 2.0F) / 3.0F;
        return Math.min(f, 1.5F);
    }

    @SideOnly(Side.CLIENT)
    public static ModelResourceLocation getModelLocation1(String name) {
        final String loc = Constants.MOD_ID +
                ':' +
                "energy_tools" + "/" + name;

        return new ModelResourceLocation(loc, null);
    }

    @Override
    public List<EnumInfoUpgradeModules> getUpgradeModules() {
        return EnumUpgrades.BOW.list;
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

    public int getItemEnchantability() {
        return 0;
    }

    public boolean isBookEnchantable(@Nonnull ItemStack stack, @Nonnull ItemStack book) {
        return false;
    }

    @SideOnly(Side.CLIENT)
    @Override
    public void addInformation(
            @Nonnull final ItemStack stack,
            @Nullable final World p_77624_2_,
            @Nonnull final List<String> tooltip,
            @Nonnull final ITooltipFlag p_77624_4_
    ) {
        if (!Keyboard.isKeyDown(Keyboard.KEY_LSHIFT)) {
            tooltip.add(Localization.translate("press.lshift"));
        } else {
            tooltip.add(Localization.translate("iu.changemode_key") + Keyboard.getKeyName(Math.abs(KeyboardClient.changemode.getKeyCode())) + Localization.translate(
                    "iu.changemode_rcm"));
        }
        switch (this.tier) {
            case 2:
                tooltip.add(Localization.translate("iu.bow.maxdamage") + 47);
                break;
            case 3:
                tooltip.add(Localization.translate("iu.bow.maxdamage") + 56);

                break;
            case 4:
                tooltip.add(Localization.translate("iu.bow.maxdamage") + 71);

                break;
        }

        ModUtils.mode(stack, tooltip);
        super.addInformation(stack, p_77624_2_, tooltip, p_77624_4_);
    }

    @Override
    public void getSubItems(@Nonnull final CreativeTabs subs, @Nonnull final NonNullList<ItemStack> items) {
        if (this.isInCreativeTab(subs)) {
            ItemStack stack = new ItemStack(this, 1);

            NBTTagCompound nbt = ModUtils.nbt(stack);
            ElectricItem.manager.charge(stack, 2.147483647E9D, 2147483647, true, false);
            nbt.setInteger("ID_Item", Integer.MAX_VALUE);
            items.add(stack);
            ItemStack itemstack = new ItemStack(this, 1);
            nbt = ModUtils.nbt(itemstack);
            nbt.setInteger("ID_Item", Integer.MAX_VALUE);
            items.add(itemstack);
        }
    }

    public EntityAdvArrow createArrow(World worldIn, ItemStack stack, EntityLivingBase shooter) {
        EntityAdvArrow entitytippedarrow = new EntityAdvArrow(worldIn, shooter);
        entitytippedarrow.setPotionEffect(stack);
        entitytippedarrow.setStack(stack);
        return entitytippedarrow;
    }

    public boolean isRepairable() {
        return false;
    }


    @Nonnull
    public EntityAdvArrow customizeArrow(@Nonnull EntityArrow arrow) {
        return (EntityAdvArrow) arrow;
    }

    public void onPlayerStoppedUsing(
            @Nonnull ItemStack stack,
            @Nonnull World world,
            @Nonnull EntityLivingBase EntityLivingBase,
            int timeLeft
    ) {
        if (!(EntityLivingBase instanceof EntityPlayer)) {
            super.onPlayerStoppedUsing(stack, world, EntityLivingBase, timeLeft);
        }
        assert EntityLivingBase instanceof EntityPlayer;
        EntityPlayer player = (EntityPlayer) EntityLivingBase;
        NBTTagCompound nbt = ModUtils.nbt(stack);
        int mode = nbt.getInteger("bowMode");
        int charge = getMaxItemUseDuration(stack) - timeLeft;
        ArrowLooseEvent event = new ArrowLooseEvent(player, stack, world, charge, false);
        MinecraftForge.EVENT_BUS.post(event);
        if (event.isCanceled()) {
            return;
        }
        charge = event.getCharge();
        if (mode == 3) {
            charge /= 2;
        }
        if (mode == 1) {
            charge *= 4;
        }
        float f = getArrowVelocity(charge);
        if (f < 0.1D) {
            return;
        }
        if (!world.isRemote) {
            EntityAdvArrow arrow = createArrow(world, stack, player);
            arrow = this.customizeArrow(arrow);
            arrow.shoot(player, player.rotationPitch, player.rotationYaw, 0.0F, f * 3.0F, 1.0F);

            if (f == 1.5F) {
                arrow.setIsCritical(true);
            }

            int bowdamage = UpgradeSystem.system.hasModules(EnumInfoUpgradeModules.BOWDAMAGE, stack) ?
                    UpgradeSystem.system.getModules(EnumInfoUpgradeModules.BOWDAMAGE, stack).number : 0;
            arrow.setDamage(arrow.getDamage() + type * 2.5D + 0.5D + type * 2.5D * 0.25 * bowdamage);
            int j = EnchantmentHelper.getEnchantmentLevel(Enchantments.POWER, stack);
            if (j > 0) {
                arrow.setDamage(arrow.getDamage() + j * 0.5D + 0.5D);
            }
            if (mode == 0 && arrow.getIsCritical()) {
                j += 3;
            } else if (mode == 1 && arrow.getIsCritical()) {
                j++;
            } else if (mode == 3 && arrow.getIsCritical()) {
                j += 8;
            }
            if (j > 0) {
                arrow.setDamage(arrow.getDamage() + j * 0.5D + 0.5D);
            }
            if (nanoBowBoost > 0) {
                arrow.setDamage(arrow.getDamage() + nanoBowBoost * 0.5D + 0.5D);
            }

            int k = EnchantmentHelper.getEnchantmentLevel(Enchantments.PUNCH, stack);
            if (mode == 0 && arrow.getIsCritical()) {
                k++;
            } else if (mode == 3 && arrow.getIsCritical()) {
                k += 5;
            }
            if (k > 0) {
                arrow.setKnockbackStrength(k);
            }
            if (EnchantmentHelper.getEnchantmentLevel(Enchantments.FLAME, stack) > 0) {
                arrow.setFire(100);
            }
            if (mode == 4 && arrow.getIsCritical()) {
                arrow.setFire(2000);
            }
            arrow.pickupStatus = EntityArrow.PickupStatus.CREATIVE_ONLY;

            int bowenergy = UpgradeSystem.system.hasModules(EnumInfoUpgradeModules.BOWENERGY, stack) ?
                    UpgradeSystem.system.getModules(EnumInfoUpgradeModules.BOWENERGY, stack).number : 0;

            if (mode == 2) {
                if (ElectricItem.manager.canUse(stack, CHARGE[mode] - CHARGE[mode] * 0.1 * bowenergy)) {
                    ElectricItem.manager.use(stack, CHARGE[mode] - CHARGE[mode] * 0.1 * bowenergy, player);
                    new PacketSoundPlayer(EnumSound.bow, player);
                    world.spawnEntity(arrow);
                    if (arrow.getIsCritical()) {
                        EntityArrow arrow2 = createArrow(world, stack, player);
                        arrow2 = this.customizeArrow(arrow2);
                        arrow2.shoot(player, player.rotationPitch, player.rotationYaw, 0.0F, f * 3.0F, 1.0F);
                        arrow2.setDamage(f * 2.0F);
                        arrow2.setPosition(arrow2.posX + 0.25D, arrow2.posY, arrow2.posZ);
                        arrow2.setIsCritical(true);
                        arrow2.pickupStatus = EntityArrow.PickupStatus.CREATIVE_ONLY;

                        EntityArrow arrow3 = createArrow(world, stack, player);
                        arrow3 = this.customizeArrow(arrow3);
                        arrow3.shoot(player, player.rotationPitch, player.rotationYaw, 0.0F, f * 3.0F, 1.0F);

                        arrow3.setDamage(f * 2.0F);
                        arrow3.setPosition(arrow3.posX, arrow3.posY + 0.25D, arrow3.posZ);
                        arrow3.setIsCritical(true);
                        arrow3.pickupStatus = EntityArrow.PickupStatus.CREATIVE_ONLY;

                        EntityArrow arrow4 = createArrow(world, stack, player);
                        arrow4 = this.customizeArrow(arrow4);
                        arrow4.shoot(player, player.rotationPitch, player.rotationYaw, 0.0F, f * 3.0F, 1.0F);

                        arrow4.setDamage(f * 2.0F);
                        arrow4.setPosition(arrow4.posX - 0.25D, arrow4.posY, arrow4.posZ);
                        arrow4.setIsCritical(true);
                        arrow4.pickupStatus = EntityArrow.PickupStatus.CREATIVE_ONLY;

                        EntityArrow arrow5 = createArrow(world, stack, player);
                        arrow5 = this.customizeArrow(arrow5);
                        arrow5.shoot(player, player.rotationPitch, player.rotationYaw, 0.0F, f * 3.0F, 1.0F);

                        arrow5.setDamage(f * 2.0F);
                        arrow5.setPosition(arrow2.posX, arrow2.posY - 0.25D, arrow2.posZ);
                        arrow5.setIsCritical(true);
                        arrow5.pickupStatus = EntityArrow.PickupStatus.CREATIVE_ONLY;


                        world.spawnEntity(arrow2);
                        world.spawnEntity(arrow3);
                        world.spawnEntity(arrow4);
                        world.spawnEntity(arrow5);
                    }

                }
            } else {
                if (ElectricItem.manager.canUse(stack, CHARGE[mode] - CHARGE[mode] * 0.1 * bowenergy)) {
                    ElectricItem.manager.use(stack, CHARGE[mode] - CHARGE[mode] * 0.1 * bowenergy, player);
                    new PacketSoundPlayer(EnumSound.bow, player);
                    world.spawnEntity(arrow);

                }
            }
        }

    }

    public int getMaxItemUseDuration(@Nonnull ItemStack stack) {
        NBTTagCompound nbt = ModUtils.nbt(stack);
        switch (nbt.getInteger("bowMode")) {
            case 3:
            case 5:
                return 144000;
            case 1:
                return 18000;
        }
        return 72000;
    }

    @Nonnull
    @Override
    public ActionResult<ItemStack> onItemRightClick(
            @Nonnull final World world,
            final EntityPlayer player,
            @Nonnull final EnumHand hand
    ) {
        ItemStack stack = player.getHeldItem(hand);
        NBTTagCompound nbt = ModUtils.nbt(stack);

        int mode = nbt.getInteger("bowMode");
        int bowenergy = UpgradeSystem.system.hasModules(EnumInfoUpgradeModules.BOWENERGY, stack) ?
                UpgradeSystem.system.getModules(EnumInfoUpgradeModules.BOWENERGY, stack).number : 0;

        if (IUCore.keyboard.isChangeKeyDown(player)) {
            if (!world.isRemote) {
                mode++;
                if (mode >= CHARGE.length) {
                    mode = 0;
                }
                nbt.setInteger("bowMode", mode);

            }
            if (IUCore.proxy.isSimulating()) {
                IUCore.proxy.messagePlayer(player, Localization.translate("info.nanobow." + MODE[mode]));
            }
        } else if (player.capabilities.isCreativeMode || ElectricItem.manager.canUse(
                stack,
                CHARGE[mode] - CHARGE[mode] * 0.1 * bowenergy
        )) {
            player.setActiveHand(hand);
        }
        ArrowNockEvent event = new ArrowNockEvent(player, stack, hand, world, false);
        MinecraftForge.EVENT_BUS.post(event);
        if (event.isCanceled()) {
            return event.getAction();
        }

        return new ActionResult<>(EnumActionResult.SUCCESS, stack);

    }

    @SubscribeEvent
    void blindness(LivingHurtEvent event) {
        if (event.getEntityLiving().getEntityWorld().isRemote) {
            return;
        }
        if (!(event.getSource().getImmediateSource() instanceof EntityAdvArrow)) {
            return;
        }
        try {

            EntityAdvArrow tippedArrow = (EntityAdvArrow) event.getSource().getImmediateSource();
            ItemStack stack = tippedArrow.getStack();

            boolean blindness = UpgradeSystem.system.hasModules(EnumInfoUpgradeModules.BLINDNESS, stack);
            if (!blindness) {
                return;
            }
            event.getEntityLiving().addPotionEffect(new PotionEffect(MobEffects.BLINDNESS, 60));
            tippedArrow.setDead();
        } catch (Exception e) {
            EntityAdvArrow tippedArrow = (EntityAdvArrow) event.getSource().getImmediateSource();
            tippedArrow.setDead();
        }
    }

    public void onUpdate(
            @Nonnull ItemStack stack,
            @Nonnull World world,
            @Nonnull Entity entity,
            int itemSlot,
            boolean isSelected
    ) {
        NBTTagCompound nbt = ModUtils.nbt(stack);

        if (!UpgradeSystem.system.hasInMap(stack)) {
            nbt.setBoolean("hasID", false);
            MinecraftForge.EVENT_BUS.post(new EventItemLoad(world, this, stack));
        }

    }

    public void onUsingTick(@Nonnull ItemStack stack, @Nonnull EntityLivingBase EntityLivingBase, int i) {
        if (!(EntityLivingBase instanceof EntityPlayer)) {
            return;
        }
        EntityPlayer player = (EntityPlayer) EntityLivingBase;
        NBTTagCompound nbt = ModUtils.nbt(stack);
        int mode = nbt.getInteger("bowMode");

        if (mode == 1) {
            int bowenergy = UpgradeSystem.system.hasModules(EnumInfoUpgradeModules.BOWENERGY, stack) ?
                    UpgradeSystem.system.getModules(EnumInfoUpgradeModules.BOWENERGY, stack).number : 0;

            int j = getMaxItemUseDuration(stack) - i;
            if (j >= 10 && ElectricItem.manager.canUse(stack, CHARGE[1] - CHARGE[1] * 0.1 * bowenergy)) {
                player.stopActiveHand();
            }
        }
    }

    public boolean canProvideEnergy(ItemStack is) {
        return false;
    }

    public double getMaxEnergy(ItemStack stack) {
        return this.maxenergy;
    }

    public short getTierItem(ItemStack stack) {
        return (short) this.tier;
    }

    public double getTransferEnergy(ItemStack stack) {
        return transferenergy;
    }


    @Override
    public void registerModels() {
        registerModels(this.name);
    }

    @SideOnly(Side.CLIENT)
    public void registerModels(final String name) {
        ModelLoader.setCustomMeshDefinition(this, stack -> {
            final NBTTagCompound nbt = ModUtils.nbt(stack);
            if (nbt.getString("mode").equals("")) {
                return getModelLocation1(name + nbt.getString("mode"));
            }

            return getModelLocation1(name + "_" + nbt.getString("mode"));

        });
        String[] mode = {"", "Zelen", "Demon", "Dark", "Cold", "Ender", "Ukraine", "Fire", "Snow", "Taiga", "Desert", "Emerald"};
        for (final String s : mode) {
            if (!s.isEmpty()) {
                ModelBakery.registerItemVariants(this, getModelLocation1(name + "_" + s));
            } else {
                ModelBakery.registerItemVariants(this, getModelLocation1(name + s));
            }
        }


    }


}
