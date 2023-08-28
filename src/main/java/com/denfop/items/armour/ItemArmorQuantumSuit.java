package com.denfop.items.armour;


import com.denfop.Constants;
import com.denfop.ElectricItem;
import com.denfop.IUCore;
import com.denfop.IUPotion;
import com.denfop.api.IModelRegister;
import com.denfop.api.item.IHazmatLike;
import com.denfop.api.upgrade.EnumUpgrades;
import com.denfop.api.upgrade.IUpgradeItem;
import com.denfop.api.upgrade.UpgradeSystem;
import com.denfop.api.upgrade.event.EventItemLoad;
import com.denfop.audio.EnumSound;
import com.denfop.audio.SoundHandler;
import com.denfop.register.Register;
import com.denfop.utils.KeyboardClient;
import com.denfop.utils.ModUtils;
import net.minecraft.client.renderer.block.model.ModelBakery;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.MobEffects;
import net.minecraft.inventory.EntityEquipmentSlot;
import net.minecraft.item.EnumRarity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.potion.Potion;
import net.minecraft.potion.PotionEffect;
import net.minecraft.util.DamageSource;
import net.minecraft.util.EnumParticleTypes;
import net.minecraft.util.NonNullList;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.client.model.ModelLoader;
import net.minecraftforge.common.ISpecialArmor;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.entity.living.LivingFallEvent;
import net.minecraftforge.fml.common.eventhandler.EventPriority;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import org.jetbrains.annotations.Nullable;
import org.lwjgl.input.Keyboard;

import javax.annotation.Nonnull;
import java.util.IdentityHashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Random;

public class ItemArmorQuantumSuit extends ItemArmorEnergy implements IModelRegister, IUpgradeItem, IHazmatLike {

    protected static final Map<Potion, Integer> potionRemovalCost = new IdentityHashMap<>();
    private static boolean lastJetpackUsed = false;
    private float jumpCharge;

    public ItemArmorQuantumSuit(String name, EntityEquipmentSlot armorType) {
        super("quantum", armorType, 1.0E7, 12000.0, 4);
        if (armorType == EntityEquipmentSlot.FEET) {
            MinecraftForge.EVENT_BUS.register(this);
        }

        potionRemovalCost.put(MobEffects.POISON, 10000);
        potionRemovalCost.put(IUPotion.radiation, 10000);
        potionRemovalCost.put(MobEffects.WITHER, 25000);
        this.armorName = name;
        setUnlocalizedName(name);
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
    @Override
    public void addInformation(
            final ItemStack p_77624_1_,
            @Nullable final World p_77624_2_,
            final List<String> p_77624_3_,
            final ITooltipFlag p_77624_4_
    ) {
        if (this.armorType == EntityEquipmentSlot.HEAD) {
            p_77624_3_.add("Nightvision Key: " + Keyboard.getKeyName(Math.abs(KeyboardClient.armormode.getKeyCode())));
        }

        super.addInformation(p_77624_1_, p_77624_2_, p_77624_3_, p_77624_4_);
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
            ItemStack itemstack = new ItemStack(this, 1, 27);
            nbt = ModUtils.nbt(itemstack);
            nbt.setInteger("ID_Item", Integer.MAX_VALUE);
            items.add(itemstack);
        }
    }

    @Override
    public void registerModels() {
        registerModels(this.armorName);
    }

    @SideOnly(Side.CLIENT)
    public void registerModels(final String name) {
        ModelLoader.setCustomMeshDefinition(this, stack -> {
            final NBTTagCompound nbt = ModUtils.nbt(stack);

            String mode = nbt.getString("mode");
            if (nbt.getString("mode").equals("")) {
                return getModelLocation1(name, "");
            } else {
                return getModelLocation1("armor", "_" + this.armorType.ordinal() + "_" + mode);
            }
        });
        String[] mode = {"", "_Zelen", "_Demon", "_Dark", "_Cold", "_Ender", "_Ukraine", "_Fire", "_Snow", "_Taiga", "_Desert",
                "_Emerald"};
        for (final String s : mode) {
            if (s.equals("")) {
                ModelBakery.registerItemVariants(this, getModelLocation1(name, s));
            } else {
                ModelBakery.registerItemVariants(this, getModelLocation1("armor", "_" + this.armorType.ordinal() + s));
            }

        }

    }

    public String getArmorTexture(ItemStack stack, Entity entity, EntityEquipmentSlot slot, String type) {
        int suffix = (this.armorType == EntityEquipmentSlot.LEGS) ? 2 : 1;
        NBTTagCompound nbtData = ModUtils.nbt(stack);
        if (!nbtData.getString("mode").isEmpty()) {
            return Constants.TEXTURES + ":textures/armor/" + "armor" + this.armorType.ordinal() + "_" + nbtData.getString("mode") + ".png";
        }


        if (suffix == 1) {
            return Constants.TEXTURES + ":textures/armor/" + "quantum_1.png";
        } else {
            return Constants.TEXTURES + ":textures/armor/" + "quantum_2.png";
        }
    }

    public boolean addsProtection(EntityLivingBase entity, EntityEquipmentSlot slot, ItemStack stack) {
        return ElectricItem.manager.getCharge(stack) > 0.0;
    }

    public ISpecialArmor.ArmorProperties getProperties(
            EntityLivingBase entity,
            ItemStack armor,
            DamageSource source,
            double damage,
            int slot
    ) {
        int energyPerDamage = this.getEnergyPerDamage();
        int damageLimit = Integer.MAX_VALUE;
        if (energyPerDamage > 0) {
            damageLimit = (int) Math.min(
                    damageLimit,
                    25.0 * ElectricItem.manager.getCharge(armor) / (double) energyPerDamage
            );
        }

        if (source == DamageSource.FALL) {
            if (this.armorType == EntityEquipmentSlot.FEET) {
                return new ISpecialArmor.ArmorProperties(10, 1.0, damageLimit);
            }

            if (this.armorType == EntityEquipmentSlot.LEGS) {
                return new ISpecialArmor.ArmorProperties(9, 0.8, damageLimit);
            }
        }

        double absorptionRatio = this.getBaseAbsorptionRatio() * this.getDamageAbsorptionRatio();
        return new ISpecialArmor.ArmorProperties(8, absorptionRatio, damageLimit);
    }

    @SubscribeEvent(
            priority = EventPriority.LOW
    )
    public void onEntityLivingFallEvent(LivingFallEvent event) {
        if (IUCore.proxy.isSimulating() && event.getEntity() instanceof EntityLivingBase) {
            EntityLivingBase entity = (EntityLivingBase) event.getEntity();
            ItemStack armor = entity.getItemStackFromSlot(EntityEquipmentSlot.FEET);
            if (!armor.isEmpty() && armor.getItem() == this) {
                int fallDamage = Math.max((int) event.getDistance() - 10, 0);
                double energyCost = this.getEnergyPerDamage() * fallDamage;
                if (energyCost <= ElectricItem.manager.getCharge(armor)) {
                    ElectricItem.manager.discharge(armor, energyCost, Integer.MAX_VALUE, true, false, false);
                    event.setCanceled(true);
                }
            }
        }

    }

    public double getCharge(ItemStack itemStack) {
        return ElectricItem.manager.getCharge(itemStack);
    }

    public void use(ItemStack itemStack, double amount) {
        ElectricItem.manager.discharge(itemStack, amount, 2147483647, true, false, false);
    }

    public boolean useJetpack(EntityPlayer player) {
        ItemStack jetpack = player.inventory.armorInventory.get(2);
        if (this.getCharge(jetpack) <= 0.0D) {
            return false;
        } else {
            boolean electric = true;
            float power = 1.0F;
            float dropPercentage = 0.2F;
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

    public double getDamageAbsorptionRatio() {
        return this.armorType == EntityEquipmentSlot.CHEST ? 1.2 : 1.0;
    }

    public int getEnergyPerDamage() {
        return 20000;
    }

    @SideOnly(Side.CLIENT)
    public EnumRarity getRarity(ItemStack stack) {
        return EnumRarity.RARE;
    }

    public void onArmorTick(World world, EntityPlayer player, ItemStack stack) {
        NBTTagCompound nbtData = ModUtils.nbt(stack);
        byte toggleTimer = nbtData.getByte("toggleTimer");
        boolean ret = false;
        int skylight;
        switch (this.armorType) {
            case HEAD:


                for (final Object o : new LinkedList<>(player.getActivePotionEffects())) {
                    PotionEffect effect = (PotionEffect) o;
                    Potion potion = effect.getPotion();
                    Integer cost = potionRemovalCost.get(potion);
                    if (cost != null) {
                        cost = cost * (effect.getAmplifier() + 1);
                        if (ElectricItem.manager.canUse(stack, (double) cost)) {
                            ElectricItem.manager.use(stack, (double) cost, null);
                            IUCore.proxy.removePotion(player, potion);
                        }
                    }
                }

                boolean Nightvision = nbtData.getBoolean("Nightvision");
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
                    --toggleTimer;
                    nbtData.setByte("toggleTimer", toggleTimer);
                }

                if (Nightvision && IUCore.proxy.isSimulating() && ElectricItem.manager.use(stack, 1.0, player)) {
                    BlockPos pos = new BlockPos(
                            (int) Math.floor(player.posX),
                            (int) Math.floor(player.posY),
                            (int) Math.floor(player.posZ)
                    );
                    skylight = player.getEntityWorld().getLightFromNeighbors(pos);
                    if (skylight > 8) {
                        IUCore.proxy.removePotion(player, MobEffects.NIGHT_VISION);
                        player.addPotionEffect(new PotionEffect(MobEffects.BLINDNESS, 100, 0, true, true));
                    } else {
                        IUCore.proxy.removePotion(player, MobEffects.BLINDNESS);
                        player.addPotionEffect(new PotionEffect(MobEffects.NIGHT_VISION, 300, 0, true, true));
                    }

                    ret = true;
                }

                break;
            case CHEST:
                boolean jetpackUsed = false;
                if (IUCore.keyboard.isJumpKeyDown(player)) {
                    jetpackUsed = this.useJetpack(player);
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
                break;
            case LEGS:

                if (ElectricItem.manager.canUse(
                        stack,
                        1000.0
                ) && (player.onGround || player.isInWater()) && IUCore.keyboard.isForwardKeyDown(player) && player.isSprinting()) {
                    skylight = nbtData.getByte("speedTicker");
                    skylight = (byte) (skylight + 1);
                    if (skylight >= 10) {
                        skylight = 0;
                        ElectricItem.manager.use(stack, 1000.0, null);
                        ret = true;
                    }

                    nbtData.setByte("speedTicker", (byte) skylight);
                    float speed = 0.22F;
                    if (player.isInWater()) {
                        speed = 0.1F;
                        if (IUCore.keyboard.isJumpKeyDown(player)) {
                            player.motionY += 0.10000000149011612;
                        }
                    }

                    if (speed > 0.0F) {
                        player.moveRelative(0.0F, 0.0F, 1.0F, speed);
                    }
                }

                break;
            case FEET:
                if (IUCore.proxy.isSimulating()) {
                    boolean wasOnGround = !nbtData.hasKey("wasOnGround") || nbtData.getBoolean("wasOnGround");
                    if (wasOnGround && !player.onGround && IUCore.keyboard.isJumpKeyDown(player) && IUCore.keyboard.isChangeKeyDown(
                            player)) {
                        ElectricItem.manager.use(stack, 4000.0, null);
                        ret = true;
                    }

                    if (player.onGround != wasOnGround) {
                        nbtData.setBoolean("wasOnGround", player.onGround);
                    }
                } else {
                    if (ElectricItem.manager.canUse(stack, 4000.0) && player.onGround) {
                        this.jumpCharge = 1.0F;
                    }

                    if (player.motionY >= 0.0 && this.jumpCharge > 0.0F && !player.isInWater()) {
                        if (IUCore.keyboard.isJumpKeyDown(player) && IUCore.keyboard.isChangeKeyDown(player)) {
                            if (this.jumpCharge == 1.0F) {
                                player.motionX *= 3.5;
                                player.motionZ *= 3.5;
                            }

                            player.motionY += this.jumpCharge * 0.3F;
                            this.jumpCharge = (float) ((double) this.jumpCharge * 0.75);
                        } else if (this.jumpCharge < 1.0F) {
                            this.jumpCharge = 0.0F;
                        }
                    }
                }

        }

        if (ret) {
            player.inventoryContainer.detectAndSendChanges();
        }

    }

    public int getItemEnchantability() {
        return 0;
    }


    public double getChargeLevel(ItemStack stack) {
        return ElectricItem.manager.getCharge(stack) / this.getMaxEnergy(stack);
    }


    public boolean doesProvideHUD(ItemStack stack) {
        return this.armorType == EntityEquipmentSlot.HEAD && ElectricItem.manager.getCharge(stack) > 0.0;
    }


}
