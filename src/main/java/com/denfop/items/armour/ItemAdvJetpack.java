package com.denfop.items.armour;

import com.denfop.Constants;
import com.denfop.IUCore;
import com.denfop.Ic2Items;
import com.denfop.api.IModelRegister;
import com.denfop.api.upgrade.EnumUpgrades;
import com.denfop.api.upgrade.IUpgradeItem;
import com.denfop.api.upgrade.UpgradeSystem;
import com.denfop.api.upgrade.event.EventItemLoad;
import com.denfop.audio.AudioSource;
import com.denfop.audio.PositionSpec;
import com.denfop.items.EnumInfoUpgradeModules;
import com.denfop.utils.KeyboardClient;
import com.denfop.utils.ModUtils;
import ic2.api.item.ElectricItem;
import ic2.api.item.IElectricItem;
import ic2.api.item.IItemHudInfo;
import ic2.api.item.IMetalArmor;
import ic2.core.IC2;
import ic2.core.init.BlocksItems;
import ic2.core.init.Localization;
import ic2.core.item.BaseElectricItem;
import ic2.core.item.armor.ItemArmorElectric;
import ic2.core.util.LogCategory;
import ic2.core.util.StackUtil;
import net.minecraft.client.renderer.block.model.ModelBakery;
import net.minecraft.client.renderer.block.model.ModelResourceLocation;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.MobEffects;
import net.minecraft.inventory.EntityEquipmentSlot;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.potion.PotionEffect;
import net.minecraft.util.DamageSource;
import net.minecraft.util.NonNullList;
import net.minecraft.world.World;
import net.minecraftforge.client.model.ModelLoader;
import net.minecraftforge.common.ISpecialArmor;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import org.jetbrains.annotations.Nullable;
import org.lwjgl.input.Keyboard;

import javax.annotation.Nonnull;
import java.util.ArrayList;
import java.util.List;

public class ItemAdvJetpack extends ItemArmorElectric implements IElectricItem, IModelRegister, ISpecialArmor, IMetalArmor,
        IItemHudInfo, IUpgradeItem {

    protected static AudioSource audioSource;
    private static boolean lastJetpackUsed = false;
    private final String armorName;
    private final double maxStorage;
    private final double TransferLimit;
    private final int tier;

    public ItemAdvJetpack(String name, double maxStorage, double TransferLimit, int tier) {
        super(null, "", EntityEquipmentSlot.CHEST, maxStorage, TransferLimit, tier);

        this.setMaxDamage(27);
        this.setMaxStackSize(1);
        this.setNoRepair();
        this.setUnlocalizedName(name);
        this.armorName = name;
        setCreativeTab(IUCore.EnergyTab);
        setMaxStackSize(1);
        this.maxStorage = maxStorage;
        this.TransferLimit = TransferLimit;
        this.tier = tier;

        BlocksItems.registerItem((Item) this, IUCore.getIdentifier(name)).setUnlocalizedName(name);
        IUCore.proxy.addIModelRegister(this);
        UpgradeSystem.system.addRecipe(this, EnumUpgrades.JETPACK.list);

    }

    @SideOnly(Side.CLIENT)
    public static ModelResourceLocation getModelLocation1(String name, String extraName) {
        final String loc = Constants.MOD_ID +
                ':' +
                "armour" + "/" + name + extraName;

        return new ModelResourceLocation(loc, null);
    }

    @Override
    public void onUpdate(
            final ItemStack stack,
            final World worldIn,
            final Entity entityIn,
            final int itemSlot,
            final boolean isSelected
    ) {
        NBTTagCompound nbt = ModUtils.nbt(stack);

        if (!UpgradeSystem.system.hasInMap(stack)) {
            nbt.setBoolean("hasID", false);
            MinecraftForge.EVENT_BUS.post(new EventItemLoad(worldIn, this, stack));
        }
    }

    @Override
    public void addInformation(
            @Nonnull final ItemStack stack,
            @Nullable final World p_77624_2_,
            @Nonnull final List<String> tooltip,
            @Nonnull final ITooltipFlag p_77624_4_
    ) {
        super.addInformation(stack, p_77624_2_, tooltip, p_77624_4_);
        NBTTagCompound nbtData = ModUtils.nbt(stack);
        if (UpgradeSystem.system.hasModules(
                EnumInfoUpgradeModules.FLY,
                stack
        )) {
            tooltip.add(Localization.translate("iu.fly") + " " + ModUtils.Boolean(nbtData.getBoolean("jetpack")));
            tooltip.add(Localization.translate("iu.fly_need"));

            if (!Keyboard.isKeyDown(Keyboard.KEY_LSHIFT)) {
                tooltip.add(Localization.translate("press.lshift"));
            }


            if (Keyboard.isKeyDown(Keyboard.KEY_LSHIFT)) {
                tooltip.add(Localization.translate("iu.changemode_fly") + Keyboard.getKeyName(Math.abs(KeyboardClient.flymode.getKeyCode())));
            }
        }
    }

    public void setDamage(ItemStack stack, int damage) {
        int prev = this.getDamage(stack);
        if (damage != prev && BaseElectricItem.logIncorrectItemDamaging) {
            IC2.log.warn(
                    LogCategory.Armor,
                    new Throwable(),
                    "Detected invalid armor damage application (%d):",
                    damage - prev
            );
        }

    }

    @Override
    public double getDamageAbsorptionRatio() {
        return 0.4;
    }

    @Override
    public int getEnergyPerDamage() {
        return 20000;
    }

    public String getUnlocalizedName() {
        return super.getUnlocalizedName().substring(4) + ".name";
    }

    @Override
    public void registerModels() {
        registerModels(this.armorName);
    }

    @SideOnly(Side.CLIENT)
    public void registerModels(final String name) {
        ModelLoader.setCustomMeshDefinition(this, stack -> {
            final NBTTagCompound nbt = ModUtils.nbt(stack);

            return getModelLocation1(name, nbt.getString("mode"));
        });
        String[] mode = {"", "Demon", "Dark", "Cold", "Ender"};
        for (final String s : mode) {
            ModelBakery.registerItemVariants(this, getModelLocation1(name, s));
        }

    }

    public String getArmorTexture(ItemStack stack, Entity entity, EntityEquipmentSlot slot, String type) {
        int suffix = (this.armorType == EntityEquipmentSlot.LEGS) ? 2 : 1;
        NBTTagCompound nbtData = ModUtils.nbt(stack);
        if (!nbtData.getString("mode").isEmpty()) {
            return Constants.TEXTURES + ":textures/armor/" + this.armorName + "_" + nbtData.getString("mode") + "_" + suffix + ".png";
        }


        return Constants.TEXTURES + ":textures/armor/" + this.armorName + "_" + suffix + ".png";
    }


    public String getUnlocalizedName(ItemStack itemStack) {
        return this.getUnlocalizedName();
    }

    public String getItemStackDisplayName(ItemStack itemStack) {
        return Localization.translate(this.getUnlocalizedName(itemStack));
    }

    public double getCharge(ItemStack itemStack) {
        return ElectricItem.manager.getCharge(itemStack);
    }

    public void use(ItemStack itemStack, double amount) {
        ElectricItem.manager.discharge(itemStack, amount, 2147483647, true, false, false);
    }


    public boolean useJetpack(EntityPlayer player, boolean hoverMode) {
        ItemStack jetpack = player.inventory.armorInventory.get(2);
        if (this.getCharge(jetpack) <= 0.0D) {
            return false;
        } else {
            boolean electric = jetpack.getItem() != Ic2Items.jetpack.getItem();
            float power = 1.0F;
            float dropPercentage = 0.2F;
            if (electric) {
                power = 0.7F;
                dropPercentage = 0.05F;
            }

            if (this.getCharge(jetpack) / this.getMaxCharge(jetpack) <= (double) dropPercentage) {
                power = (float) ((double) power * (this.getCharge(jetpack) / (this.getMaxCharge(jetpack) * (double) dropPercentage)));
            }

            if (IC2.keyboard.isForwardKeyDown(player)) {
                float retruster = 0.15F;
                if (hoverMode) {
                    retruster = 1.0F;
                }

                if (electric) {
                    retruster += 0.15F;
                }

                float forwardpower = power * retruster * 2.0F;
                if (forwardpower > 0.0F) {
                    player.moveRelative(0.0F, 0.0F, 0.4F * forwardpower, 0.02F);
                }
            }

            int worldHeight = IC2.getWorldHeight(player.getEntityWorld());
            int maxFlightHeight = electric ? (int) ((float) worldHeight / 1.28F) : worldHeight;
            double y = player.posY;
            if (y > (double) (maxFlightHeight - 25)) {
                if (y > (double) maxFlightHeight) {
                    y = maxFlightHeight;
                }

                power = (float) ((double) power * (((double) maxFlightHeight - y) / 25.0D));
            }

            double prevmotion = player.motionY;
            player.motionY = Math.min(player.motionY + (double) (power * 0.2F), 0.6000000238418579D);
            if (hoverMode) {
                float maxHoverY = 0.0F;
                if (IC2.keyboard.isJumpKeyDown(player)) {
                    if (electric) {
                        maxHoverY = 0.1F;
                    } else {
                        maxHoverY = 0.2F;
                    }
                }

                if (IC2.keyboard.isSneakKeyDown(player)) {
                    if (electric) {
                        maxHoverY = -0.1F;
                    } else {
                        maxHoverY = -0.2F;
                    }
                }

                if (player.motionY > (double) maxHoverY) {
                    player.motionY = maxHoverY;
                    if (prevmotion > player.motionY) {
                        player.motionY = prevmotion;
                    }
                }
            }

            int consume = 2;
            if (hoverMode) {
                consume = 1;
            }

            if (electric) {
                consume += 6;
            }

            if (!player.onGround) {
                this.use(jetpack, consume);
            }

            player.fallDistance = 0.0F;
            player.distanceWalkedModified = 0.0F;
            IC2.platform.resetPlayerInAirTime(player);
            return true;
        }
    }

    public boolean canProvideEnergy(ItemStack itemStack) {
        return false;
    }


    public double getMaxCharge(ItemStack itemStack) {
        return maxStorage;
    }

    public int getTier(ItemStack itemStack) {
        return this.tier;
    }

    public double getTransferLimit(ItemStack itemStack) {
        return this.TransferLimit;
    }

    @Override
    public void getSubItems(final CreativeTabs p_150895_1_, final NonNullList<ItemStack> var3) {
        if (this.isInCreativeTab(p_150895_1_)) {
            final ItemStack var4 = new ItemStack(this, 1);
            ElectricItem.manager.charge(var4, 2.147483647E9, Integer.MAX_VALUE, true, false);
            var3.add(var4);
            var3.add(new ItemStack(this, 1, 27));
        }
    }


    public void onArmorTick(@Nonnull World world, EntityPlayer player, @Nonnull ItemStack itemStack) {
        if (player.inventory.armorInventory.get(2).isItemEqual(itemStack)) {
            NBTTagCompound nbtData = StackUtil.getOrCreateNbtData(itemStack);
            boolean hoverMode = nbtData.getBoolean("hoverMode");
            byte toggleTimer = nbtData.getByte("toggleTimer");
            boolean jetpackUsed = false;
            if (IC2.keyboard.isJumpKeyDown(player) && IC2.keyboard.isModeSwitchKeyDown(player) && toggleTimer == 0) {
                toggleTimer = 10;
                hoverMode = !hoverMode;
                if (IC2.platform.isSimulating()) {
                    nbtData.setBoolean("hoverMode", hoverMode);
                    if (hoverMode) {
                        IC2.platform.messagePlayer(player, "Hover Mode enabled.");
                    } else {
                        IC2.platform.messagePlayer(player, "Hover Mode disabled.");
                    }
                }
            }
            boolean jetpack;
            if (nbtData.getBoolean("jetpack")) {
                player.fallDistance = 0;

                if (nbtData.getBoolean("jump") && !nbtData.getBoolean("canFly") && !player.capabilities.allowFlying && IC2.keyboard.isJumpKeyDown(
                        player) && !nbtData.getBoolean(
                        "isFlyActive") && toggleTimer == 0) {
                    toggleTimer = 10;
                    nbtData.setBoolean("canFly", true);
                }
                nbtData.setBoolean("jump", !player.onGround);

                if (!player.onGround) {
                    if (ElectricItem.manager.canUse(itemStack, 25)) {
                        ElectricItem.manager.use(itemStack, 25, null);
                    } else {
                        nbtData.setBoolean("jetpack", false);
                    }
                }
            }
            jetpack = nbtData.getBoolean("jetpack");
            if ((IC2.keyboard.isJumpKeyDown(player) || hoverMode) && !jetpack) {
                jetpackUsed = this.useJetpack(player, hoverMode);
            }
            if (IUCore.keyboard.isFlyModeKeyDown(player) && toggleTimer == 0 && UpgradeSystem.system.hasModules(
                    EnumInfoUpgradeModules.FLY,
                    itemStack
            )) {
                toggleTimer = 10;
                jetpack = !jetpack;
                if (IC2.platform.isSimulating()) {

                    nbtData.setBoolean("jetpack", jetpack);
                    if (jetpack) {
                        IC2.platform.messagePlayer(player, Localization.translate("iu.flymode_armor.info"));
                    } else {
                        IC2.platform.messagePlayer(player, Localization.translate("iu.flymode_armor.info1"));

                    }
                }
            }
            if (IC2.platform.isSimulating() && toggleTimer > 0) {
                --toggleTimer;
                nbtData.setByte("toggleTimer", toggleTimer);
            }

            if (IC2.platform.isRendering() && player == IC2.platform.getPlayerInstance()) {
                if (lastJetpackUsed != jetpackUsed) {
                    if (jetpackUsed) {
                        if (audioSource == null) {
                            audioSource = IUCore.audioManager.createSource(
                                    player,
                                    PositionSpec.Backpack,
                                    "Tools/Jetpack/JetpackLoop.ogg",
                                    true,
                                    false,
                                    IC2.audioManager.getDefaultVolume()
                            );
                        }

                        if (audioSource != null) {
                            audioSource.play();
                        }
                    } else if (audioSource != null) {
                        audioSource.remove();
                        audioSource = null;
                    }

                    lastJetpackUsed = jetpackUsed;
                }

                if (audioSource != null) {
                    audioSource.updatePosition();
                }
            }
            boolean fireResistance = UpgradeSystem.system.hasModules(EnumInfoUpgradeModules.FIRE_PROTECTION, itemStack);
            if (fireResistance) {
                player.addPotionEffect(new PotionEffect(MobEffects.FIRE_RESISTANCE, 300));
            }
            int resistance = (UpgradeSystem.system.hasModules(EnumInfoUpgradeModules.RESISTANCE, itemStack) ?
                    UpgradeSystem.system.getModules(EnumInfoUpgradeModules.RESISTANCE, itemStack).number : 0);

            if (resistance != 0) {
                player.addPotionEffect(new PotionEffect(MobEffects.RESISTANCE, 300, resistance));
            }
            if (UpgradeSystem.system.hasModules(EnumInfoUpgradeModules.INVISIBILITY, itemStack)) {
                player.addPotionEffect(new PotionEffect(MobEffects.INVISIBILITY, 300));
            }
            if (jetpackUsed) {
                player.inventoryContainer.detectAndSendChanges();
            }

        }
    }

    public boolean getIsRepairable(ItemStack par1ItemStack, ItemStack par2ItemStack) {
        return false;
    }

    @Override
    public boolean isMetalArmor(ItemStack itemstack, EntityPlayer player) {
        return true;
    }

    public ArmorProperties getProperties(
            EntityLivingBase player,
            @Nonnull ItemStack armor,
            DamageSource source,
            double damage,
            int slot
    ) {
        return new ArmorProperties(0, 0.0D, 0);
    }

    public int getArmorDisplay(EntityPlayer player, @Nonnull ItemStack armor, int slot) {
        return 0;
    }


    public List<String> getHudInfo(ItemStack stack, boolean advanced) {
        List<String> info = new ArrayList<>();
        info.add(ElectricItem.manager.getToolTip(stack));
        return info;
    }

}
