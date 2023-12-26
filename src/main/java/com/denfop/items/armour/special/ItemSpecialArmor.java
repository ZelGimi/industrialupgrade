package com.denfop.items.armour.special;

import com.denfop.Constants;
import com.denfop.ElectricItem;
import com.denfop.IUCore;
import com.denfop.IUItem;
import com.denfop.IUPotion;
import com.denfop.Localization;
import com.denfop.api.IModelRegister;
import com.denfop.api.inv.IAdvInventory;
import com.denfop.api.item.IEnergyItem;
import com.denfop.api.upgrade.EnumUpgrades;
import com.denfop.api.upgrade.IUpgradeItem;
import com.denfop.api.upgrade.UpgradeSystem;
import com.denfop.api.upgrade.event.EventItemLoad;
import com.denfop.audio.EnumSound;
import com.denfop.audio.SoundHandler;
import com.denfop.container.ContainerBags;
import com.denfop.items.EnumInfoUpgradeModules;
import com.denfop.items.IItemStackInventory;
import com.denfop.items.bags.BagsDescription;
import com.denfop.items.bags.ItemStackBags;
import com.denfop.register.Register;
import com.denfop.utils.KeyboardClient;
import com.denfop.utils.ModUtils;
import net.minecraft.client.renderer.block.model.ModelBakery;
import net.minecraft.client.renderer.block.model.ModelResourceLocation;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.init.MobEffects;
import net.minecraft.inventory.EntityEquipmentSlot;
import net.minecraft.item.Item;
import net.minecraft.item.ItemArmor;
import net.minecraft.item.ItemFood;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.network.play.server.SPacketEntityTeleport;
import net.minecraft.potion.Potion;
import net.minecraft.potion.PotionEffect;
import net.minecraft.util.DamageSource;
import net.minecraft.util.EnumParticleTypes;
import net.minecraft.util.NonNullList;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.text.TextFormatting;
import net.minecraft.util.text.translation.I18n;
import net.minecraft.world.World;
import net.minecraftforge.client.model.ModelLoader;
import net.minecraftforge.common.ISpecialArmor;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.entity.living.LivingEvent;
import net.minecraftforge.event.entity.living.LivingFallEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.lwjgl.input.Keyboard;

import javax.annotation.Nonnull;
import java.util.ArrayList;
import java.util.IdentityHashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;

public class ItemSpecialArmor extends ItemArmor implements IModelRegister, IItemStackInventory, ISpecialArmor, IEnergyItem,
        IUpgradeItem {

    protected final Map<Potion, Integer> potionRemovalCost = new IdentityHashMap<>();
    private final List<EnumCapability> listCapability;
    private final String name;
    private final double maxCharge;
    private final int tier;
    private final double transferLimit;
    private final EnumTypeArmor armor;
    private float jumpCharge;
    private boolean lastJetpackUsed = false;

    public ItemSpecialArmor(EnumSubTypeArmor subTypeArmor, EnumTypeArmor typeArmor) {
        super(ArmorMaterial.DIAMOND, -1, subTypeArmor.getEntityEquipmentSlot());
        final List<EnumCapability> list = new ArrayList<>(subTypeArmor.getCapabilities());
        list.removeIf(capability -> !typeArmor.getListCapability().contains(capability));
        this.listCapability = list;
        if (armorType == EntityEquipmentSlot.FEET) {
            MinecraftForge.EVENT_BUS.register(this);
        }
        if (this.listCapability.contains(EnumCapability.ACTIVE_EFFECT) || this.listCapability.contains(EnumCapability.ALL_ACTIVE_EFFECT)) {
            potionRemovalCost.put(IUPotion.radiation, 20);
            if (this.listCapability.contains(EnumCapability.ALL_ACTIVE_EFFECT)) {
                potionRemovalCost.put(MobEffects.POISON, 100);
                potionRemovalCost.put(MobEffects.WITHER, 100);
                potionRemovalCost.put(MobEffects.HUNGER, 200);
                potionRemovalCost.put(MobEffects.SLOWNESS, 200);
                potionRemovalCost.put(MobEffects.UNLUCK, 200);
                potionRemovalCost.put(MobEffects.LEVITATION, 200);
                potionRemovalCost.put(MobEffects.NAUSEA, 200);
                potionRemovalCost.put(MobEffects.BLINDNESS, 200);
                potionRemovalCost.put(MobEffects.WEAKNESS, 200);
            }
        }

        this.armor = typeArmor;
        this.name = typeArmor.name().toLowerCase() + "_" + subTypeArmor.name().toLowerCase();

        this.maxCharge = typeArmor.getMaxEnergy();
        this.tier = typeArmor.getTier();
        this.transferLimit = typeArmor.getMaxTransfer();
        setMaxStackSize(1);
        setNoRepair();
        setUnlocalizedName(name);
        this.setMaxDamage(0);
        setCreativeTab(IUCore.EnergyTab);
        Register.registerItem((Item) this, IUCore.getIdentifier(name)).setUnlocalizedName(name);
        IUCore.proxy.addIModelRegister(this);
        switch (armorType) {
            case HEAD:
                UpgradeSystem.system.addRecipe(this, EnumUpgrades.HELMET.list);
                break;
            case CHEST:
                UpgradeSystem.system.addRecipe(this, EnumUpgrades.BODY.list);
                break;
            case LEGS:
                UpgradeSystem.system.addRecipe(this, EnumUpgrades.LEGGINGS.list);
                break;
            case FEET:
                UpgradeSystem.system.addRecipe(this, EnumUpgrades.BOOTS.list);
                break;
        }
    }

    @SideOnly(Side.CLIENT)
    public static ModelResourceLocation getModelLocation1(String name, String extraName) {
        final String loc = Constants.MOD_ID +
                ':' +
                "armour" + "/" + name + extraName;

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

    public EnumTypeArmor getArmor() {
        return armor;
    }

    public List<EnumCapability> getListCapability() {
        return listCapability;
    }

    @Override
    public void onUpdate(@Nonnull ItemStack itemStack, @Nonnull World world, @Nonnull Entity entity, int slot, boolean par5) {
        NBTTagCompound nbt = ModUtils.nbt(itemStack);

        if (!UpgradeSystem.system.hasInMap(itemStack)) {
            nbt.setBoolean("hasID", false);
            MinecraftForge.EVENT_BUS.post(new EventItemLoad(world, this, itemStack));
        }


    }

    @Override
    public void getSubItems(final CreativeTabs subs, final NonNullList<ItemStack> items) {
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

    @Override
    public void registerModels() {
        registerModels(this.name);
    }

    @SideOnly(Side.CLIENT)
    public void registerModels(final String name) {
        ModelLoader.setCustomMeshDefinition(this, stack -> {
            final NBTTagCompound nbt = ModUtils.nbt(stack);

            String mode = nbt.getString("mode");
            if (nbt.getString("mode").equals("") || !armor.getSkinsList().contains(mode)) {
                return getModelLocation1(name, "");
            } else {
                return getModelLocation1(name + "_" + mode.toLowerCase(), "");
            }
        });
        String[] mode = {"", "_Zelen", "_Demon", "_Dark", "_Cold", "_Ender", "_Ukraine", "_Fire", "_Snow", "_Taiga", "_Desert",
                "_Emerald"};
        for (final String s : mode) {
            if (s.equals("")) {
                ModelBakery.registerItemVariants(this, getModelLocation1(name, s));
            } else {
                ModelBakery.registerItemVariants(this, getModelLocation1(name + s.toLowerCase(), ""));
            }

        }
    }

    public String getArmorTexture(ItemStack stack, Entity entity, EntityEquipmentSlot slot, String type) {
        int suffix = (this.armorType == EntityEquipmentSlot.LEGS) ? 2 : 1;
        NBTTagCompound nbtData = ModUtils.nbt(stack);
        final String mode = nbtData.getString("mode");
        if (!mode.isEmpty() && armor.getSkinsList().contains(mode)) {
            if (suffix == 1) {
                return Constants.TEXTURES + ":textures/armor/" + this.armor
                        .name()
                        .toLowerCase() + "_" + mode.toLowerCase() + "_1.png";
            } else {
                return Constants.TEXTURES + ":textures/armor/" + this.armor
                        .name()
                        .toLowerCase() + "_" + mode.toLowerCase() + "_2.png";
            }
        }

        if (suffix == 1) {
            return Constants.TEXTURES + ":textures/armor/" + this.armor.name().toLowerCase() + "_1.png";
        } else {
            return Constants.TEXTURES + ":textures/armor/" + this.armor.name().toLowerCase() + "_2.png";
        }

    }

    @SubscribeEvent
    public void Potion(LivingEvent.LivingUpdateEvent event) {
        if (!(event.getEntityLiving() instanceof EntityPlayer)) {
            return;
        }
        EntityPlayer player = (EntityPlayer) event.getEntityLiving();
        NBTTagCompound nbtData = player.getEntityData();
        if (!player.inventory.armorInventory.get(0).isEmpty()
                && player.inventory.armorInventory
                .get(0)
                .getItem() == this && this.listCapability.contains(EnumCapability.AUTO_JUMP)) {
            nbtData.setBoolean("stepHeight", true);
            player.stepHeight = 1.0F;


        } else {
            if (nbtData.getBoolean("stepHeight")) {
                player.stepHeight = 0.5F;
                nbtData.setBoolean("stepHeight", false);
            }


        }
    }

    public void use(ItemStack itemStack, double amount) {
        ElectricItem.manager.discharge(itemStack, amount, 2147483647, true, false, false);
    }

    public double getCharge(ItemStack itemStack) {
        return ElectricItem.manager.getCharge(itemStack);
    }

    public boolean isBookEnchantable(ItemStack stack, ItemStack book) {
        return false;
    }

    public void onArmorTick(@Nonnull World world, @Nonnull EntityPlayer player, @Nonnull ItemStack itemStack) {

        boolean Nightvision;
        boolean jetpack;
        NBTTagCompound nbtData = ModUtils.nbt(itemStack);
        byte toggleTimer = nbtData.getByte("toggleTimer");
        boolean ret = false;
        int resistance = (UpgradeSystem.system.hasModules(EnumInfoUpgradeModules.RESISTANCE, itemStack) ?
                UpgradeSystem.system.getModules(EnumInfoUpgradeModules.RESISTANCE, itemStack).number : 0);


        int repaired = (UpgradeSystem.system.hasModules(EnumInfoUpgradeModules.REPAIRED, itemStack) ?
                UpgradeSystem.system.getModules(EnumInfoUpgradeModules.REPAIRED, itemStack).number : 0);
        if (UpgradeSystem.system.hasModules(EnumInfoUpgradeModules.INVISIBILITY, itemStack)) {
            player.addPotionEffect(new PotionEffect(MobEffects.INVISIBILITY, 300));
        }


        if (repaired != 0) {
            if (world.provider.getWorldTime() % 80 == 0) {
                ElectricItem.manager.charge(
                        itemStack,
                        this.getMaxEnergy(itemStack) * 0.00001 * repaired,
                        Integer.MAX_VALUE,
                        true,
                        false
                );
            }
        }
        if (resistance != 0) {
            player.addPotionEffect(new PotionEffect(MobEffects.RESISTANCE, 300, resistance));
        }


        switch (this.armorType) {
            case HEAD:
                List<PotionEffect> effects = new ArrayList<>(player.getActivePotionEffects());

                for (PotionEffect effect : effects) {

                    Integer cost = potionRemovalCost.get(effect.getPotion());
                    if (cost != null) {
                        cost = cost * (effect.getAmplifier() + 1);
                        if (ElectricItem.manager.canUse(itemStack, cost)) {
                            ElectricItem.manager.use(itemStack, cost, null);
                            IUCore.proxy.removePotion(player, effect.getPotion());
                        }
                    }
                }

                Nightvision = nbtData.getBoolean("Nightvision");
                if (IUCore.keyboard.isArmorKey(player) && toggleTimer == 0) {
                    toggleTimer = 10;
                    Nightvision = !Nightvision;
                    if (IUCore.proxy.isSimulating()) {
                        nbtData.setBoolean("Nightvision", Nightvision);
                        if (Nightvision) {
                            IUCore.proxy.messagePlayer(player, "Nightvision enabled.");
                        } else {
                            IUCore.proxy.messagePlayer(player, "Nightvision disabled.");
                        }
                    }
                }
                if (IUCore.proxy.isSimulating() && toggleTimer > 0) {
                    toggleTimer = (byte) (toggleTimer - 1);
                    nbtData.setByte("toggleTimer", toggleTimer);
                }

                if (Nightvision && IUCore.proxy.isSimulating() &&
                        ElectricItem.manager.use(itemStack, 1.0D, player)) {
                    int x = MathHelper.floor(player.posX);
                    int z = MathHelper.floor(player.posZ);
                    int y = MathHelper.floor(player.posY);
                    int skylight = player.getEntityWorld().getLightFromNeighbors(new BlockPos(x, y, z));
                    boolean with = this.listCapability.contains(EnumCapability.NIGHT_VISION_WITH);
                    boolean without = this.listCapability.contains(EnumCapability.NIGHT_VISION_WITHOUT);
                    if (without || with) {
                        if (skylight > 8) {
                            IUCore.proxy.removePotion(player, MobEffects.NIGHT_VISION);
                            if (with) {
                                player.addPotionEffect(new PotionEffect(MobEffects.BLINDNESS, 100, 0, true, true));

                            }
                        } else {
                            if (with) {
                                IUCore.proxy.removePotion(player, MobEffects.BLINDNESS);

                            }
                            player.addPotionEffect(new PotionEffect(MobEffects.NIGHT_VISION, 300, 0));
                        }
                    } else {
                        player.addPotionEffect(new PotionEffect(MobEffects.NIGHT_VISION, 300, 0));
                    }
                    ret = true;
                }
                if (this.listCapability.contains(EnumCapability.FOOD) && ElectricItem.manager.canUse(itemStack, 1000.0D) && player
                        .getFoodStats()
                        .needFood()) {
                    int slot = -1;
                    for (int i = 0; i < player.inventory.mainInventory.size(); i++) {
                        if (!player.inventory.mainInventory.get(i).isEmpty()
                                && player.inventory.mainInventory.get(i).getItem() instanceof ItemFood) {
                            slot = i;
                            break;
                        }
                    }
                    if (slot > -1) {
                        ItemStack stack = player.inventory.mainInventory.get(slot);
                        ItemFood can = (ItemFood) stack.getItem();
                        stack = can.onItemUseFinish(stack, world, player);
                        if (stack.getCount() <= 0) {
                            player.inventory.mainInventory.set(slot, ItemStack.EMPTY);
                        }
                        ElectricItem.manager.use(itemStack, 1000.0D, player);
                        ret = true;
                    }
                }
                if (UpgradeSystem.system.hasModules(EnumInfoUpgradeModules.WATER, itemStack)) {
                    player.addPotionEffect(new PotionEffect(MobEffects.WATER_BREATHING, 300));
                }
                if (UpgradeSystem.system.hasModules(EnumInfoUpgradeModules.NIGTHVISION, itemStack)) {
                    player.addPotionEffect(new PotionEffect(MobEffects.NIGHT_VISION, 300));
                }


                break;
            case CHEST:

                if (nbtData.getBoolean("jetpack")) {
                    player.fallDistance = 0;

                    if (nbtData.getBoolean("jump") && !nbtData.getBoolean("canFly") && !player.capabilities.allowFlying && IUCore.keyboard.isJumpKeyDown(
                            player) && !nbtData.getBoolean(
                            "isFlyActive") && toggleTimer == 0) {
                        toggleTimer = 10;
                        nbtData.setBoolean("canFly", true);
                    }
                    nbtData.setBoolean("jump", !player.onGround);

                    if (!player.onGround) {
                        if (ElectricItem.manager.canUse(itemStack, 45)) {
                            ElectricItem.manager.use(itemStack, 45, null);
                        } else {
                            nbtData.setBoolean("jetpack", false);
                        }
                    }
                }


                jetpack = nbtData.getBoolean("jetpack");
                boolean vertical = nbtData.getBoolean("vertical");

                if (this.listCapability.contains(EnumCapability.VERTICAL_FLY) && IUCore.keyboard.isVerticalMode(player) && toggleTimer == 0) {
                    toggleTimer = 10;
                    vertical = !vertical;
                    if (IUCore.proxy.isSimulating()) {

                        nbtData.setBoolean("vertical", vertical);
                        if (vertical) {
                            IUCore.proxy.messagePlayer(player, Localization.translate("iu.flymode_armor.info2"));

                        } else {
                            IUCore.proxy.messagePlayer(player, Localization.translate("iu.flymode_armor.info3"));

                        }
                    }
                }
                if (vertical && jetpack) {
                    double motion = 0;
                    if (IUCore.keyboard.isJumpKeyDown(player)) {
                        motion = 0.3;
                    }
                    if (player.isSneaking()) {
                        motion = -0.3;
                    }

                    player.motionY += motion;
                }


                if (IUCore.keyboard.isStreakKeyDown(player) && toggleTimer == 0 && IUItem.spectral_chestplate == this) {
                    toggleTimer = 10;
                    player.openGui(
                            IUCore.instance,
                            4,
                            player.getEntityWorld(),
                            (int) player.posX,
                            (int) player.posY,
                            (int) player.posZ
                    );

                }
                int reTimer = nbtData.getInteger("reTimer");
                if (this.listCapability.contains(EnumCapability.JETPACK_FLY)) {

                    if (reTimer > 0) {
                        reTimer--;
                        nbtData.setInteger("reTimer", reTimer);
                    }
                }
                if ((this.listCapability.contains(EnumCapability.FLY) || this.listCapability.contains(EnumCapability.JETPACK_FLY)) && IUCore.keyboard.isFlyModeKeyDown(
                        player) && toggleTimer == 0 && reTimer == 0) {
                    toggleTimer = 10;
                    jetpack = !jetpack;
                    if (IUCore.proxy.isSimulating()) {
                        nbtData.setBoolean("jetpack", jetpack);

                        if (jetpack) {
                            IUCore.proxy.messagePlayer(player, Localization.translate("iu.flymode_armor.info"));
                            if (this.listCapability.contains(EnumCapability.JETPACK_FLY)) {
                                nbtData.setInteger("timer", 600);
                            }
                        } else {
                            IUCore.proxy.messagePlayer(player, Localization.translate("iu.flymode_armor.info1"));

                        }
                    }
                }
                if (this.listCapability.contains(EnumCapability.JETPACK_FLY)) {
                    jetpack = nbtData.getBoolean("jetpack");
                    int timer = nbtData.getInteger("timer");
                    if (timer > 0) {
                        timer--;
                        nbtData.setInteger("timer", timer);
                    } else {
                        if (jetpack) {
                            nbtData.setBoolean("jetpack", false);
                            nbtData.setInteger("reTimer", 5 * 20 * 60);
                            IUCore.proxy.messagePlayer(player, Localization.translate("iu.flymode_armor.info1"));
                        }
                    }

                    boolean jetpackUsed = false;
                    if (!jetpack) {
                        if (IUCore.keyboard.isJumpKeyDown(player)) {
                            jetpackUsed = this.useJetpack(player);
                        }
                    }
                    if (IUCore.proxy.isRendering() && player == IUCore.proxy.getPlayerInstance()) {
                        if (lastJetpackUsed != jetpackUsed) {
                            if (jetpackUsed) {
                                SoundHandler.playSound(player, "JetpackLoop");
                            }


                            lastJetpackUsed = jetpackUsed;
                            if (!lastJetpackUsed) {
                                SoundHandler.stopSound(EnumSound.JetpackLoop);
                            }
                        }
                        final Random rnd = world.rand;
                        if (jetpackUsed) {
                            for (int i = 0; i < rnd.nextInt(10); i++) {
                                world.spawnParticle(
                                        EnumParticleTypes.SMOKE_NORMAL,
                                        (float) (player.posX - player.motionX) + rnd.nextFloat(),
                                        (float) (player.posY),
                                        (float) (player.posZ - player.motionZ) + rnd.nextFloat(), 0, -0.25, 0
                                );
                            }
                            for (int i = 0; i < rnd.nextInt(10); i++) {
                                world.spawnParticle(
                                        EnumParticleTypes.FLAME,
                                        (float) (player.posX - player.motionX) + rnd.nextFloat(),
                                        (float) (player.posY),
                                        (float) (player.posZ - player.motionZ) + rnd.nextFloat(), 0, -0.25, 0
                                );
                            }
                        }


                    }
                }
                if (listCapability.contains(EnumCapability.JETPACK)) {
                    boolean jetpackUsed = false;
                    if (!jetpack) {
                        if (IUCore.keyboard.isJumpKeyDown(player)) {
                            jetpackUsed = this.useJetpack(player);
                        }
                    }
                    if (IUCore.proxy.isRendering() && player == IUCore.proxy.getPlayerInstance()) {
                        if (lastJetpackUsed != jetpackUsed) {
                            if (jetpackUsed) {
                                SoundHandler.playSound(player, "JetpackLoop");
                            }


                            lastJetpackUsed = jetpackUsed;
                            if (!lastJetpackUsed) {
                                SoundHandler.stopSound(EnumSound.JetpackLoop);
                            }
                        }
                        final Random rnd = world.rand;
                        if (jetpackUsed) {
                            for (int i = 0; i < rnd.nextInt(10); i++) {
                                world.spawnParticle(
                                        EnumParticleTypes.SMOKE_NORMAL,
                                        (float) (player.posX - player.motionX) + rnd.nextFloat(),
                                        (float) (player.posY),
                                        (float) (player.posZ - player.motionZ) + rnd.nextFloat(), 0, -0.25, 0
                                );
                            }
                            for (int i = 0; i < rnd.nextInt(10); i++) {
                                world.spawnParticle(
                                        EnumParticleTypes.FLAME,
                                        (float) (player.posX - player.motionX) + rnd.nextFloat(),
                                        (float) (player.posY),
                                        (float) (player.posZ - player.motionZ) + rnd.nextFloat(), 0, -0.25, 0
                                );
                            }
                        }


                    }
                }

                if (IUCore.proxy.isSimulating() && toggleTimer > 0) {
                    toggleTimer = (byte) (toggleTimer - 1);
                    nbtData.setByte("toggleTimer", toggleTimer);
                }


                if (UpgradeSystem.system.hasModules(EnumInfoUpgradeModules.FIRE_PROTECTION, itemStack)) {
                    player.addPotionEffect(new PotionEffect(MobEffects.FIRE_RESISTANCE, 300));
                }

                player.extinguish();
                break;
            case LEGS:


                if (this.listCapability.contains(EnumCapability.SPEED) && ElectricItem.manager.canUse(
                        itemStack,
                        1000.0D
                ) && (player.onGround || player.isInWater()) && IUCore.keyboard.isForwardKeyDown(player)) {
                    byte speedTicker = nbtData.getByte("speedTicker");
                    speedTicker = (byte) (speedTicker + 1);
                    if (speedTicker >= 10) {
                        speedTicker = 0;
                        ElectricItem.manager.use(itemStack, 1000.0D, null);
                        ret = true;
                    }
                    nbtData.setByte("speedTicker", speedTicker);
                    float speed = 0.22F;
                    if (player.isInWater()) {
                        speed = 0.1F;
                        if (IUCore.keyboard.isJumpKeyDown(player)) {
                            player.motionY += 0.10000000149011612D;
                        }
                    }
                    player.moveRelative(0.0F, 0.0F, 1.0F, speed);
                }
                if (this.listCapability.contains(EnumCapability.BAGS)) {
                    if (IUCore.keyboard.isLeggingsMode(player) && IUCore.keyboard.isBootsMode(player) && toggleTimer == 0) {
                        toggleTimer = 10;
                        if (IUCore.proxy.isSimulating()) {
                            save(itemStack, player);
                            player.openGui(IUCore.instance, 2, world, (int) player.posX, (int) player.posY, (int) player.posZ);

                        }
                    }
                }
                if (this.listCapability.contains(EnumCapability.MAGNET)) {
                    boolean magnet = !nbtData.getBoolean("magnet");

                    if (IUCore.keyboard.isLeggingsMode(player) && IUCore.keyboard.isChangeKeyDown(player) && toggleTimer == 0) {
                        toggleTimer = 10;
                        if (IUCore.proxy.isSimulating()) {
                            if (magnet) {
                                IUCore.proxy.messagePlayer(player, "Magnet enabled.");
                            }
                            if (!magnet) {
                                IUCore.proxy.messagePlayer(player, "Magnet disabled.");
                            }
                            nbtData.setBoolean("magnet", magnet);
                        }
                    }
                    if (IUCore.keyboard.isLeggingsMode(player) && IUCore.keyboard.isSaveModeKeyDown(player) && toggleTimer == 0) {
                        toggleTimer = 10;
                        if (IUCore.proxy.isSimulating()) {
                            int mode = ModUtils.NBTGetInteger(itemStack, "mode1");
                            mode++;
                            if (mode > 2 || mode < 0) {
                                mode = 0;
                            }

                            ModUtils.NBTSetInteger(itemStack, "mode1", mode);
                            IUCore.proxy.messagePlayer(
                                    player,
                                    TextFormatting.GREEN + Localization.translate("message.text.mode") + ": "
                                            + Localization.translate("message.magnet.mode." + mode)
                            );
                        }
                    }
                    int mode = ModUtils.NBTGetInteger(itemStack, "mode1");
                    if (mode != 0) {
                        int radius = 11;
                        AxisAlignedBB axisalignedbb = new AxisAlignedBB(player.posX - radius, player.posY - radius,
                                player.posZ - radius, player.posX + radius, player.posY + radius, player.posZ + radius
                        );
                        List<Entity> list = player.getEntityWorld().getEntitiesWithinAABBExcludingEntity(
                                player,
                                axisalignedbb
                        );
                        boolean ret1 = false;
                        for (Entity entityinlist : list) {
                            if (entityinlist instanceof EntityItem) {
                                EntityItem item = (EntityItem) entityinlist;
                                if (ElectricItem.manager.canUse(itemStack, 200)) {
                                    if (mode == 1) {

                                        item.setLocationAndAngles(player.posX, player.posY, player.posZ, 0.0F, 0.0F);
                                        if (!player.world.isRemote) {
                                            ((EntityPlayerMP) player).connection.sendPacket(new SPacketEntityTeleport(item));
                                        }
                                        item.setPickupDelay(0);
                                        ElectricItem.manager.use(itemStack, 200, null);
                                        ret1 = true;
                                    } else if (mode == 2) {
                                        boolean xcoord = item.posX + 2 >= player.posX && item.posX - 2 <= player.posX;
                                        boolean zcoord = item.posZ + 2 >= player.posZ && item.posZ - 2 <= player.posZ;

                                        if (!xcoord && !zcoord) {
                                            item.setPosition(player.posX, player.posY - 1, player.posZ);
                                            item.setPickupDelay(10);
                                        }

                                    }
                                }

                            }
                        }
                        if (ret1) {
                            player.inventoryContainer.detectAndSendChanges();
                        }

                    }
                }

                if (UpgradeSystem.system.hasModules(EnumInfoUpgradeModules.SPEED, itemStack)) {
                    player.addPotionEffect(new PotionEffect(MobEffects.SPEED, 300));

                }
                if (IUCore.proxy.isSimulating() && toggleTimer > 0) {
                    toggleTimer = (byte) (toggleTimer - 1);
                    nbtData.setByte("toggleTimer", toggleTimer);
                }
                break;
            case FEET:
                if (IUCore.proxy.isSimulating()) {
                    boolean wasOnGround = !nbtData.hasKey("wasOnGround") || nbtData.getBoolean("wasOnGround");
                    if (wasOnGround && !player.onGround && IUCore.keyboard

                            .isJumpKeyDown(player) && IUCore.keyboard
                            .isChangeKeyDown(player)) {
                        ElectricItem.manager.use(itemStack, 4000.0D, null);
                        ret = true;
                    }
                    if (player.onGround != wasOnGround) {
                        nbtData.setBoolean("wasOnGround", player.onGround);
                    }
                } else {
                    if (ElectricItem.manager.canUse(itemStack, 4000.0D) && player.onGround) {

                        this.jumpCharge = 1.0F;
                    }
                    if (player.motionY >= 0.0D && this.jumpCharge > 0.0F && !player.isInWater()) {
                        if (IUCore.keyboard.isJumpKeyDown(player) && IUCore.keyboard.isBootsMode(player)) {
                            if (this.jumpCharge == 1.0F) {
                                player.motionX *= 3.5D;
                                player.motionZ *= 3.5D;
                            }
                            player.motionY += (this.jumpCharge * 0.3F);
                            this.jumpCharge = (float) (this.jumpCharge * 0.75D);
                        } else if (this.jumpCharge < 1.0F) {
                            this.jumpCharge = 0.0F;
                        }
                    }
                }

                if (UpgradeSystem.system.hasModules(EnumInfoUpgradeModules.JUMP, itemStack)) {


                    player.addPotionEffect(new PotionEffect(MobEffects.JUMP_BOOST, 300));
                }
                break;

        }

        if (ret) {
            player.openContainer.detectAndSendChanges();
        }
    }

    public String getUnlocalizedName() {
        return Constants.ABBREVIATION + "." + super.getUnlocalizedName().substring(5);
    }

    public String getUnlocalizedName(ItemStack stack) {
        return this.getUnlocalizedName();
    }

    public String getItemStackDisplayName(ItemStack stack) {
        return I18n.translateToLocal(this.getUnlocalizedName(stack));
    }

    public void save(ItemStack stack, EntityPlayer player) {
        final NBTTagCompound nbt = ModUtils.nbt(stack);
        nbt.setBoolean("open", true);
        nbt.setInteger("slot_inventory", player.inventory.currentItem);
    }

    public boolean onDroppedByPlayer(@Nonnull ItemStack stack, EntityPlayer player) {
        if (!player.getEntityWorld().isRemote && !ModUtils.isEmpty(stack) && player.openContainer instanceof ContainerBags) {
            ItemStackBags toolbox = ((ContainerBags) player.openContainer).base;
            if (toolbox.isThisContainer(stack)) {
                toolbox.saveAsThrown(stack);
                player.closeScreen();
            }
        }

        return true;
    }

    @Override
    @SideOnly(Side.CLIENT)
    public void addInformation(
            @Nonnull final ItemStack itemStack,
            @Nullable final World worldIn,
            @Nonnull final List<String> info,
            @Nonnull final ITooltipFlag flagIn
    ) {
        NBTTagCompound nbtData = ModUtils.nbt(itemStack);

        if (this.listCapability.contains(EnumCapability.FLY) || this.listCapability.contains(EnumCapability.JETPACK_FLY)) {
            info.add(Localization.translate("iu.fly") + " " + ModUtils.Boolean(nbtData.getBoolean("jetpack")));
        }


        if (!Keyboard.isKeyDown(Keyboard.KEY_LSHIFT)) {
            info.add(Localization.translate("press.lshift"));
        }


        if (Keyboard.isKeyDown(Keyboard.KEY_LSHIFT)) {
            boolean with = this.listCapability.contains(EnumCapability.NIGHT_VISION_WITH);
            boolean without = this.listCapability.contains(EnumCapability.NIGHT_VISION_WITHOUT);
            boolean auto = this.listCapability.contains(EnumCapability.NIGHT_VISION_AUTO);
            if (listCapability.contains(EnumCapability.SPEED)) {
                info.add(Localization.translate("iu.special_armor_speed"));
            }
            if (with || without || auto) {
                info.add(Localization.translate("iu.special_armor_nightvision") + Keyboard.getKeyName(Math.abs(KeyboardClient.armormode.getKeyCode())));
                if (with) {
                    info.add(Localization.translate("iu.special_armor_nightvision_1"));
                }
                if (without) {
                    info.add(Localization.translate("iu.special_armor_nightvision_2"));
                }
                if (auto) {
                    info.add(Localization.translate("iu.special_armor_nightvision_3"));
                }
            }
            if (this.listCapability.contains(EnumCapability.BIG_JUMP)) {
                info.add(Localization.translate("iu.special armor big jump") + Keyboard.getKeyName(
                        Math.abs(Keyboard.KEY_SPACE)) + " + " + Keyboard.getKeyName(Math.abs(KeyboardClient.bootsmode.getKeyCode())));
            }
            if (this.listCapability.contains(EnumCapability.AUTO_JUMP)) {
                info.add(Localization.translate("iu.special_armor_auto_jump"));
            }
            if (this.listCapability.contains(EnumCapability.ACTIVE_EFFECT)) {
                info.add(Localization.translate("iu.special_armor_active_effect"));
            }
            if (this.listCapability.contains(EnumCapability.ALL_ACTIVE_EFFECT)) {
                info.add(Localization.translate("iu.special_armor_all_active_effect"));
            }
            if (this.listCapability.contains(EnumCapability.BAGS)) {
                info.add("Open bag: " + Keyboard.getKeyName(Math.abs(KeyboardClient.bootsmode.getKeyCode())) + " + " + Keyboard.getKeyName(
                        Math.abs(KeyboardClient.leggingsmode.getKeyCode())));

                final NBTTagCompound nbt = ModUtils.nbt(itemStack);
                if (nbt.hasKey("bag")) {

                    List<BagsDescription> list = new ArrayList<>();
                    final NBTTagCompound nbt1 = nbt.getCompoundTag("bag");
                    int size = nbt1.getInteger("size");
                    for (int i = 0; i < size; i++) {
                        list.add(new BagsDescription(nbt1.getCompoundTag(String.valueOf(i))));
                    }
                    for (BagsDescription description : list) {
                        info.add(TextFormatting.GREEN + "" + description.getCount() + "x " + description
                                .getStack()
                                .getDisplayName());
                    }
                }
            }
            if (this.listCapability.contains(EnumCapability.FLY) || this.listCapability.contains(EnumCapability.JETPACK_FLY)) {
                info.add(Localization.translate("iu.fly_need"));
                info.add(Localization.translate("iu.changemode_fly") + Keyboard.getKeyName(Math.abs(KeyboardClient.flymode.getKeyCode())));
            }
            if (this.listCapability.contains(EnumCapability.VERTICAL_FLY)) {

                info.add(Localization.translate("iu.vertical") + Keyboard.getKeyName(Math.abs(KeyboardClient.verticalmode.getKeyCode())));
            }
            if (this.listCapability.contains(EnumCapability.FOOD)) {
                info.add(Localization.translate("iu.food_mode_helmet"));
            }
            if (this.listCapability.contains(EnumCapability.JETPACK_FLY)) {
                info.add(Localization.translate("iu.jetpack_fly_chestplate"));

                final NBTTagCompound nbt = ModUtils.nbt(itemStack);
                final int reTimer = nbt.getInteger("reTimer");
                final int timer = nbt.getInteger("timer");
                if (timer > 0) {
                    final List<Double> time = ModUtils.Time(timer / 20D);
                    double hours = 0;
                    double minutes = 0;
                    double seconds = 0;
                    if (time.size() > 0) {
                        hours = time.get(0);
                        minutes = time.get(1);
                        seconds = time.get(2);
                    }
                    String time1 = hours > 0 ? ModUtils.getString(hours) + Localization.translate("iu.hour") + "" : "";
                    String time2 = minutes > 0 ? ModUtils.getString(minutes) + Localization.translate("iu.minutes") + "" : "";
                    String time3 = seconds > 0 ? ModUtils.getString(seconds) + Localization.translate("iu.seconds") + "" : "";

                    info.add(Localization.translate("iu.timetoend") + time1 + time2 + time3 + " " + Localization.translate(
                            "iu.jetpack_fly_chestplate_2"));
                }
                if (reTimer > 0) {
                    final List<Double> time = ModUtils.Time(reTimer / 20D);
                    double hours = 0;
                    double minutes = 0;
                    double seconds = 0;
                    if (time.size() > 0) {
                        hours = time.get(0);
                        minutes = time.get(1);
                        seconds = time.get(2);
                    }
                    String time1 = hours > 0 ? ModUtils.getString(hours) + Localization.translate("iu.hour") + "" : "";
                    String time2 = minutes > 0 ? ModUtils.getString(minutes) + Localization.translate("iu.minutes") + "" : "";
                    String time3 = seconds > 0 ? ModUtils.getString(seconds) + Localization.translate("iu.seconds") + "" : "";

                    info.add(Localization.translate("iu.timetoend") + time1 + time2 + time3 + " " + Localization.translate(
                            "iu.jetpack_fly_chestplate_1"));
                }
            }
            if (itemStack.getItem() == IUItem.spectral_chestplate) {
                info.add(Localization.translate("iu.streak") + Keyboard.getKeyName(KeyboardClient.streakmode.getKeyCode()));
            }
            if (this.listCapability.contains(EnumCapability.MAGNET)) {
                info.add(Localization.translate("iu.magnet_mode") + Keyboard.getKeyName(Math.abs(KeyboardClient.changemode.getKeyCode())) + " + " + Keyboard.getKeyName(
                        Math.abs(KeyboardClient.leggingsmode.getKeyCode())));
                info.add(Localization.translate("iu.changemode_key") + Keyboard.getKeyName(Math.abs(KeyboardClient.leggingsmode.getKeyCode())) + " + " + Keyboard.getKeyName(
                        Math.abs(KeyboardClient.savemode.getKeyCode())));
                int mode = ModUtils.NBTGetInteger(itemStack, "mode1");
                if (mode > 2 || mode < 0) {
                    mode = 0;
                }

                info.add(
                        TextFormatting.GREEN + Localization.translate("message.text.mode") + ": "
                                + Localization.translate("message.magnet.mode." + mode)
                );
            }


        }
        ModUtils.mode(itemStack, info);
    }

    public boolean useJetpack(EntityPlayer player) {
        ItemStack jetpack = player.inventory.armorInventory.get(2);
        if (this.getCharge(jetpack) <= 0.0D) {
            return false;
        } else {
            boolean electric = true;
            float power;
            float dropPercentage;
            power = 0.7F;
            dropPercentage = 0.05F;

            if (this.getCharge(jetpack) / this.getMaxEnergy(jetpack) <= (double) dropPercentage) {
                power = (float) ((double) power * (this.getCharge(jetpack) / (this.getMaxEnergy(jetpack) * (double) dropPercentage)));
            }

            if (IUCore.keyboard.isForwardKeyDown(player)) {
                float retruster = 0.15F;


                retruster += 0.15F;

                float forwardpower = power * retruster * 2.0F;
                if (forwardpower > 0.0F) {
                    player.moveRelative(0.0F, 0.0F, 0.4F * forwardpower, 0.02F);
                }
            }

            int worldHeight = player.getEntityWorld().getHeight();
            int maxFlightHeight = electric ? (int) ((float) worldHeight / 1.28F) : worldHeight;
            double y = player.posY;
            if (y > (double) (maxFlightHeight - 25)) {
                if (y > (double) maxFlightHeight) {
                    y = maxFlightHeight;
                }

                power = (float) ((double) power * (((double) maxFlightHeight - y) / 25.0D));
            }

            player.motionY = Math.min(player.motionY + (double) (power * 0.2F), 0.6000000238418579D);


            int consume = 2;


            consume += 6;

            if (!player.onGround) {
                this.use(jetpack, consume);
            }

            player.fallDistance = 0.0F;
            player.distanceWalkedModified = 0.0F;
            return true;
        }
    }

    @SubscribeEvent
    public void onEntityLivingFallEvent(LivingFallEvent event) {
        if (IUCore.proxy.isSimulating() && event.getEntity() instanceof EntityLivingBase) {
            EntityLivingBase entity = (EntityLivingBase) event.getEntity();
            ItemStack armor = entity.getItemStackFromSlot(EntityEquipmentSlot.FEET);
            if (armor.getItem() == this) {
                int fallDamage = Math.max((int) event.getDistance() - 10, 0);

                double energyCost = (this.armor.getDamageEnergy() * fallDamage) * (1 - (UpgradeSystem.system.hasModules(
                        EnumInfoUpgradeModules.FALLING_DAMAGE,
                        armor
                ) ?
                        UpgradeSystem.system.getModules(EnumInfoUpgradeModules.FALLING_DAMAGE, armor).number : 0) * 0.25);

                if (energyCost <= ElectricItem.manager.getCharge(armor)) {
                    ElectricItem.manager.discharge(armor, energyCost, 2147483647, true, false, false);
                    event.setCanceled(true);
                }
            }
            if (entity instanceof EntityPlayer && entity.getItemStackFromSlot(EntityEquipmentSlot.CHEST).getItem() == this) {
                if (entity.getEntityData().getBoolean("isFlyActive")) {
                    event.setCanceled(true);
                }
            }
        }
    }


    @Override
    public boolean canProvideEnergy(final ItemStack var1) {
        return false;
    }

    @Override
    public double getMaxEnergy(final ItemStack var1) {
        return this.maxCharge;
    }

    @Override
    public short getTierItem(final ItemStack var1) {
        return (short) this.tier;
    }

    @Override
    public double getTransferEnergy(final ItemStack var1) {
        return this.transferLimit;
    }

    @Override
    public ArmorProperties getProperties(
            final EntityLivingBase player,
            @NotNull final ItemStack armor,
            final DamageSource source,
            final double damage,
            final int slot
    ) {
        if (source == DamageSource.FALL && this.armorType.getIndex() == 3) {
            int protect = (UpgradeSystem.system.hasModules(EnumInfoUpgradeModules.PROTECTION, armor) ?
                    UpgradeSystem.system.getModules(EnumInfoUpgradeModules.PROTECTION, armor).number : 0);

            int energyPerDamage = (int) (this.armor.getDamageEnergy() - this.armor.getDamageEnergy() * 0.2 * protect);
            int damageLimit = 2147483647;
            if (energyPerDamage > 0) {
                damageLimit = (int) Math.min(
                        damageLimit,
                        25.0D * ElectricItem.manager.getCharge(armor) / (double) energyPerDamage
                );
            }

            return new ArmorProperties(10, 1.0D, damageLimit);
        }
        double absorptionRatio = getBaseAbsorptionRatio();
        int protect = (UpgradeSystem.system.hasModules(EnumInfoUpgradeModules.PROTECTION, armor) ?
                UpgradeSystem.system.getModules(EnumInfoUpgradeModules.PROTECTION, armor).number : 0);
        int energyPerDamage = (int) (this.armor.getDamageEnergy() - this.armor.getDamageEnergy() * 0.2 * protect);
        int damageLimit = (int) ((energyPerDamage > 0)
                ? (25.0D * ElectricItem.manager.getCharge(armor) / energyPerDamage)
                : 0.0D);
        return new ArmorProperties(10, absorptionRatio, damageLimit);
    }

    public boolean getIsRepairable(ItemStack par1ItemStack, ItemStack par2ItemStack) {
        return false;
    }


    public int getArmorDisplay(EntityPlayer player, @Nonnull ItemStack armor, int slot) {
        return ElectricItem.manager.getCharge(armor) >= this.armor.getDamageEnergy()
                ? (int) Math.round(40.0D * this.getBaseAbsorptionRatio())
                : 0;
    }

    private double getBaseAbsorptionRatio() {
        switch (this.armorType) {
            case FEET:
                return this.armor.getArmorMulDamage().getBootsMul();
            case HEAD:
                return this.armor.getArmorMulDamage().getHeadMul();
            case CHEST:
                return this.armor.getArmorMulDamage().getChestMul();
            case LEGS:
                return this.armor.getArmorMulDamage().getLeggingsMul();
        }
        return 1;
    }


    @Override
    public void damageArmor(
            final EntityLivingBase entity,
            @NotNull final ItemStack stack,
            final DamageSource source,
            final int damage,
            final int slot
    ) {
        int protect = (UpgradeSystem.system.hasModules(EnumInfoUpgradeModules.PROTECTION, stack) ?
                UpgradeSystem.system.getModules(EnumInfoUpgradeModules.PROTECTION, stack).number : 0);

        ElectricItem.manager.discharge(
                stack,
                (damage * (this.armor.getDamageEnergy() - this.armor.getDamageEnergy() * 0.2 * protect)),
                2147483647,
                true,
                false,
                false
        );
    }

    @Override
    public IAdvInventory getInventory(final EntityPlayer player, final ItemStack stack) {
        return new ItemStackLegsBags(player, stack);
    }

}
