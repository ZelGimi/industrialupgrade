package com.denfop.items.armour;

import com.denfop.*;
import com.denfop.api.IModelRegister;
import com.denfop.api.item.IEnergyItem;
import com.denfop.api.upgrade.EnumUpgrades;
import com.denfop.api.upgrade.IUpgradeItem;
import com.denfop.api.upgrade.UpgradeSystem;
import com.denfop.api.upgrade.event.EventItemLoad;
import com.denfop.items.EnumInfoUpgradeModules;
import com.denfop.register.Register;
import com.denfop.utils.KeyboardClient;
import com.denfop.utils.ModUtils;
import net.minecraft.block.material.MapColor;
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
import net.minecraft.potion.Potion;
import net.minecraft.potion.PotionEffect;
import net.minecraft.util.DamageSource;
import net.minecraft.util.NonNullList;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.world.World;
import net.minecraftforge.client.model.ModelLoader;
import net.minecraftforge.common.ISpecialArmor;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import org.jetbrains.annotations.Nullable;
import org.lwjgl.input.Keyboard;

import javax.annotation.Nonnull;
import java.util.*;

public class ItemSolarPanelHelmet extends ItemArmorEnergy implements IEnergyItem, IModelRegister, ISpecialArmor,
        IUpgradeItem {

    protected static final Map<Potion, Integer> potionRemovalCost = new HashMap<>();
    private final int solarType;
    private final String name;
    private double maxCharge;
    private double transferLimit;
    private int tier;
    private double genDay;
    private double genNight;
    private int energyPerDamage;
    private double damageAbsorptionRatio;
    private boolean sunIsUp;
    private boolean skyIsVisible;
    private boolean ret = false;
    private double storage;
    private double maxStorage;

    public ItemSolarPanelHelmet(
            final int type, String name
    ) {
        super(
                "", EntityEquipmentSlot.HEAD,
                type == 1 ? 1000000.0 : type == 2 ? 1.0E7 : type == 3 ? 10000.0 : Config.Storagequantumsuit,
                type == 1 ? 3000.0 : type == 2 ? 10000.0 : type == 3 ? 10000.0 : 38000.0,
                type == 1 ? 1 : type == 2 ? 2 : type == 3 ? 3 : type == 4 ? 5 : 7
        );

        this.solarType = type;
        this.name = name;
        this.transferLimit = 3000.0;
        this.tier = 1;
        if (this.solarType == 1) {
            this.genDay = Config.advGenDay;
            this.genNight = genDay / 2;
            this.maxCharge = 1000000.0;
            this.energyPerDamage = 800;
            this.damageAbsorptionRatio = 0.9;
            this.storage = 0;
            this.maxStorage = Config.advStorage / 2;

        }
        if (this.solarType == 2) {
            this.genDay = Config.hGenDay;
            this.genNight = genDay / 2;
            this.maxCharge = 1.0E7;
            this.transferLimit = 10000.0;
            this.tier = 2;
            this.energyPerDamage = 2000;
            this.damageAbsorptionRatio = 1.0;
            this.storage = 0;
            this.maxStorage = Config.hStorage / 2;
        }
        if (this.solarType == 3) {
            this.genDay = Config.uhGenDay;
            this.genNight = genDay / 2;
            this.maxCharge = 1.0E7;
            this.transferLimit = 10000.0;
            this.tier = 3;
            this.energyPerDamage = 2000;
            this.damageAbsorptionRatio = 1.0;
            this.storage = 0;
            this.maxStorage = Config.uhStorage / 2;
        }
        if (this.solarType == 4) {
            this.genDay = Config.spectralpanelGenDay;
            this.genNight = genDay / 2;
            this.transferLimit = 38000.0;
            this.maxCharge = Config.Storagequantumsuit;
            this.tier = 5;
            this.energyPerDamage = 800;
            this.damageAbsorptionRatio = 0.9;
            this.storage = 0;
            this.maxStorage = Config.spectralpanelstorage / 2;
        }
        if (this.solarType == 5) {
            this.genDay = Config.singularpanelGenDay;
            this.genNight = genDay / 2;
            this.transferLimit = 100000.0;
            this.maxCharge = Config.Storagequantumsuit;
            this.tier = 7;
            this.energyPerDamage = 2000;
            this.damageAbsorptionRatio = 1.0;
            this.storage = 0;
            this.maxStorage = Config.singularpanelstorage / 2;
        }
        this.setCreativeTab(IUCore.EnergyTab);
        potionRemovalCost.put(MobEffects.POISON, 100);
        potionRemovalCost.put(IUPotion.radiation, 20);
        potionRemovalCost.put(MobEffects.WITHER, 100);
        potionRemovalCost.put(MobEffects.HUNGER, 200);
        this.setMaxDamage(0);
        this.setUnlocalizedName(name);
        Register.registerItem((Item) this, IUCore.getIdentifier(name)).setUnlocalizedName(name);
        IUCore.proxy.addIModelRegister(this);
        UpgradeSystem.system.addRecipe(this, EnumUpgrades.SOLAR_HELMET.list);
    }

    @SideOnly(Side.CLIENT)
    public static ModelResourceLocation getModelLocation1(String name, String extraName) {
        final String loc = Constants.MOD_ID +
                ':' +
                "armour" + "/" + name + extraName;

        return new ModelResourceLocation(loc, null);
    }

    public List<EnumInfoUpgradeModules> getUpgradeModules() {
        return EnumUpgrades.SOLAR_HELMET.list;
    }

    public void setDamage(ItemStack stack, int damage) {
        int prev = this.getDamage(stack);


    }

    public String getUnlocalizedName() {
        return "item." + super.getUnlocalizedName().substring(3) + ".name";
    }

    @Override
    public void registerModels() {
        registerModels(this.name);
    }

    @SideOnly(Side.CLIENT)
    public void registerModels(final String name) {
        ModelLoader.setCustomMeshDefinition(this, stack -> {
            final NBTTagCompound nbt = ModUtils.nbt(stack);

            return getModelLocation1(name, nbt.getString("mode").equals("") ? "" : "_" + nbt.getString("mode"));
        });
        String[] mode = {"", "_Zelen", "_Demon", "_Dark", "_Cold", "_Ender", "_Ukraine", "_Fire", "_Snow", "_Taiga", "_Desert",
                "_Emerald"};
        for (final String s : mode) {
            ModelBakery.registerItemVariants(this, getModelLocation1(name, s));
        }

    }

    public String getArmorTexture(ItemStack stack, Entity entity, EntityEquipmentSlot slot, String type) {

        NBTTagCompound nbtData = ModUtils.nbt(stack);
        if (!nbtData.getString("mode").isEmpty()) {
            return Constants.TEXTURES + ":textures/armor/" + this.name + "_" + nbtData.getString("mode") + ".png";
        }


        return Constants.TEXTURES + ":textures/armor/" + this.name + ".png";
    }

    public boolean isBookEnchantable(ItemStack stack, ItemStack book) {
        return false;
    }

    private double experimental_generating(World world) {
        double k = 0;
        float angle = world.getCelestialAngle(1.0F) - 0.784690560F < 0 ? 1.0F - 0.784690560F : -0.784690560F;
        float celestialAngle = (world.getCelestialAngle(1.0F) + angle) * 360.0F;

        celestialAngle %= 360;
        celestialAngle += 12;
        //TODO: end code GC
        if (celestialAngle <= 90) {
            k = celestialAngle / 90;
        } else if (celestialAngle > 90 && celestialAngle < 180) {
            celestialAngle -= 90;
            k = 1 - celestialAngle / 90;
        } else if (celestialAngle > 180 && celestialAngle < 270) {
            celestialAngle -= 180;
            k = celestialAngle / 90;
        } else if (celestialAngle > 270 && celestialAngle < 360) {
            celestialAngle -= 270;
            k = 1 - celestialAngle / 90;
        }


        return k;

    }

    public void onArmorTick(World worldObj, @Nonnull EntityPlayer player, @Nonnull ItemStack itemStack) {
        if (worldObj.isRemote) {
            return;
        }

        gainFuel(player);
        NBTTagCompound nbtData = ModUtils.nbt(itemStack);
        int resistance = (UpgradeSystem.system.hasModules(EnumInfoUpgradeModules.RESISTANCE, itemStack) ?
                UpgradeSystem.system.getModules(EnumInfoUpgradeModules.RESISTANCE, itemStack).number : 0);
        int repaired = (UpgradeSystem.system.hasModules(EnumInfoUpgradeModules.REPAIRED, itemStack) ?
                UpgradeSystem.system.getModules(EnumInfoUpgradeModules.REPAIRED, itemStack).number : 0);
        if (UpgradeSystem.system.hasModules(EnumInfoUpgradeModules.INVISIBILITY, itemStack)) {
            player.addPotionEffect(new PotionEffect(MobEffects.INVISIBILITY, 300));
        }
        boolean NightvisioModule = UpgradeSystem.system.hasModules(EnumInfoUpgradeModules.RESISTANCE, itemStack);

        boolean Nightvision = nbtData.getBoolean("Nightvision");
        byte toggleTimer = nbtData.getByte("toggleTimer");
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
        if (!NightvisioModule) {
            if (Nightvision && IUCore.proxy.isSimulating() &&
                    ElectricItem.manager.use(itemStack, 1.0D, player)) {
                int x = MathHelper.floor(player.posX);
                int z = MathHelper.floor(player.posZ);
                int y = MathHelper.floor(player.posY);
                int skylight = player.getEntityWorld().getLightFromNeighbors(new BlockPos(x, y, z));
                boolean with = this.solarType == 1;
                boolean without = this.solarType == 2 || this.solarType == 3;
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
        } else {
            player.addPotionEffect(new PotionEffect(MobEffects.NIGHT_VISION, 300, 0));

        }
        if (repaired != 0) {
            if (worldObj.provider.getWorldTime() % 80 == 0) {
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

        int genday = (UpgradeSystem.system.hasModules(EnumInfoUpgradeModules.GENDAY, itemStack) ?
                UpgradeSystem.system.getModules(EnumInfoUpgradeModules.GENDAY, itemStack).number : 0);

        int gennight = (UpgradeSystem.system.hasModules(EnumInfoUpgradeModules.GENNIGHT, itemStack) ?
                UpgradeSystem.system.getModules(EnumInfoUpgradeModules.GENNIGHT, itemStack).number : 0);
        int storage = (UpgradeSystem.system.hasModules(EnumInfoUpgradeModules.STORAGE, itemStack) ?
                UpgradeSystem.system.getModules(EnumInfoUpgradeModules.STORAGE, itemStack).number : 0);

        double k = experimental_generating(worldObj);
        if (this.sunIsUp && this.skyIsVisible) {
            this.storage = nbtData.getDouble("storage");
            this.storage = this.storage + (this.genDay + this.genDay * 0.05 * genday) * k;
            nbtData.setDouble("storage", this.storage);
        }
        if (this.skyIsVisible) {
            this.storage = nbtData.getDouble("storage");
            this.storage = this.storage + (this.genNight + this.genNight * 0.05 * gennight) * k;
            nbtData.setDouble("storage", this.storage);

        }
        if (nbtData.getDouble("storage") >= (maxStorage + maxStorage * 0.05 * storage)) {
            nbtData.setDouble("storage", (maxStorage + maxStorage * 0.05 * storage));
        }
        if (nbtData.getDouble("storage") < 0) {
            nbtData.setDouble("storage", 0);
        }

        for (PotionEffect effect : new LinkedList<>(player.getActivePotionEffects())) {

            Integer cost = potionRemovalCost.get(effect.getPotion());
            if (cost != null) {
                cost = cost * (effect.getAmplifier() + 1);
                if (ElectricItem.manager.canUse(itemStack, cost)) {
                    ElectricItem.manager.use(itemStack, cost, null);
                    IUCore.proxy.removePotion(player, effect.getPotion());
                    ret = true;
                }
            }
        }

        double tempstorage = nbtData.getDouble("storage");
        if (tempstorage > 0) {
            double energyLeft = tempstorage;
            for (int i = 0; i < player.inventory.armorInventory.size(); i++) {
                if (energyLeft > 0) {
                    if (!player.inventory.armorInventory.get(i).isEmpty()
                            && player.inventory.armorInventory.get(i).getItem() instanceof IEnergyItem) {
                        double sentPacket = ElectricItem.manager.charge(player.inventory.armorInventory.get(i), energyLeft,
                                2147483647, true, false
                        );

                        if (sentPacket > 0.0D) {
                            energyLeft = (energyLeft - sentPacket);
                            nbtData.setDouble("storage", energyLeft);
                            ret = true;

                        }
                    }
                } else {
                    return;
                }
            }
            for (int j = 0; j < player.inventory.mainInventory.size(); j++) {
                if (energyLeft > 0) {
                    if (!player.inventory.mainInventory.get(j).isEmpty()
                            && player.inventory.mainInventory.get(j).getItem() instanceof IEnergyItem) {
                        double sentPacket = ElectricItem.manager.charge(player.inventory.mainInventory.get(j), energyLeft,
                                2147483647, true, false
                        );
                        if (sentPacket > 0.0D) {
                            energyLeft -= sentPacket;
                            nbtData.setDouble("storage", energyLeft);
                            ret = true;

                        }

                    }

                } else {
                    return;
                }
            }
        }

        if (ret) {
            player.openContainer.detectAndSendChanges();
        }
        if (UpgradeSystem.system.hasModules(EnumInfoUpgradeModules.NIGTHVISION, itemStack)) {
            player.addPotionEffect(new PotionEffect(MobEffects.NIGHT_VISION, 300));
        }
        if (UpgradeSystem.system.hasModules(EnumInfoUpgradeModules.WATER, itemStack)) {
            player.addPotionEffect(new PotionEffect(MobEffects.WATER_BREATHING, 300));
        }


    }

    public void gainFuel(EntityPlayer player) {
        if (player.getEntityWorld().provider.getWorldTime() % 128 == 0) {
            updateVisibility(player);
        }

    }

    public List<String> getHudInfo(ItemStack stack, boolean advanced) {
        List<String> info = new ArrayList<>();
        info.add(ElectricItem.manager.getToolTip(stack));
        return info;
    }

    public void updateVisibility(EntityPlayer player) {
        BlockPos pos = new BlockPos(player.posX, player.posY, player.posZ);
        boolean wetBiome = player.world.getBiome(pos).getRainfall() > 0.0F;
        boolean noSunWorld = player.world.provider.isNether();
        boolean rainWeather = wetBiome && (player.world.isRaining() || player.world.isThundering());
        this.sunIsUp = player.getEntityWorld().isDaytime() && !rainWeather;
        this.skyIsVisible = player.world.canBlockSeeSky(pos.up()) &&
                (player.world.getBlockState(pos).getMaterial().getMaterialMapColor() ==
                        MapColor.AIR) && !noSunWorld;
    }

    public boolean isMetalArmor(final ItemStack itemstack, final EntityPlayer player) {
        return true;
    }

    public ISpecialArmor.ArmorProperties getProperties(
            final EntityLivingBase player, @Nonnull final ItemStack armor,
            final DamageSource source, final double damage, final int slot
    ) {
        if (source.isUnblockable()) {
            return new ISpecialArmor.ArmorProperties(0, 0.0, 0);
        }

        final double absorptionRatio = this.getBaseAbsorptionRatio() * this.getDamageAbsorptionRatio();
        int protect = (UpgradeSystem.system.hasModules(EnumInfoUpgradeModules.PROTECTION, armor) ?
                UpgradeSystem.system.getModules(EnumInfoUpgradeModules.PROTECTION, armor).number : 0);

        final int energyPerDamage = (int) (this.getEnergyPerDamage() - this.getEnergyPerDamage() * 0.2 * protect);
        final int damageLimit = (int) ((energyPerDamage > 0)
                ? (25.0 * ElectricItem.manager.getCharge(armor) / energyPerDamage)
                : 0.0);
        return new ISpecialArmor.ArmorProperties(0, absorptionRatio, damageLimit);
    }

    public int getArmorDisplay(final EntityPlayer player, @Nonnull final ItemStack armor, final int slot) {
        if (ElectricItem.manager.getCharge(armor) >= this.getEnergyPerDamage()) {
            return (int) Math.round(20.0 * this.getBaseAbsorptionRatio() * this.getDamageAbsorptionRatio());
        }
        return 0;
    }

    public void damageArmor(
            final EntityLivingBase entity, @Nonnull final ItemStack stack, final DamageSource source,
            final int damage, final int slot
    ) {
        int protect = (UpgradeSystem.system.hasModules(EnumInfoUpgradeModules.PROTECTION, stack) ?
                UpgradeSystem.system.getModules(EnumInfoUpgradeModules.PROTECTION, stack).number : 0);

        ElectricItem.manager.discharge(
                stack,
                damage * (this.getEnergyPerDamage() - this.getEnergyPerDamage() * 0.2 * protect),
                Integer.MAX_VALUE,
                true,
                false,
                false
        );
    }

    public int getEnergyPerDamage() {
        return this.energyPerDamage;
    }

    public double getDamageAbsorptionRatio() {
        return this.damageAbsorptionRatio;
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


    public boolean canProvideEnergy(final ItemStack itemStack) {
        return false;
    }


    public double getMaxEnergy(final ItemStack itemStack) {
        return this.maxCharge;
    }

    public short getTierItem(final ItemStack itemStack) {
        return (short) this.tier;
    }

    public double getTransferEnergy(final ItemStack itemStack) {
        return this.transferLimit;
    }

    @Override
    public void addInformation(
            @Nonnull final ItemStack itemStack,
            @Nullable final World p_77624_2_,
            final List<String> info,
            @Nonnull final ITooltipFlag p_77624_4_
    ) {
        NBTTagCompound nbtData1 = ModUtils.nbt(itemStack);

        info.add(Localization.translate("iu.storage.helmet") + " "
                + ModUtils.getString(nbtData1.getDouble("storage")) + " EF");
        ModUtils.mode(itemStack, info);
        boolean with = this.solarType == 1;
        boolean without = this.solarType == 2 || this.solarType == 3;
        boolean auto = this.solarType > 3;
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
    }


    @Override
    public void onUpdate(@Nonnull ItemStack itemStack, @Nonnull World world, @Nonnull Entity entity, int slot, boolean par5) {
        NBTTagCompound nbt = ModUtils.nbt(itemStack);

        if (!UpgradeSystem.system.hasInMap(itemStack)) {
            nbt.setBoolean("hasID", false);
            MinecraftForge.EVENT_BUS.post(new EventItemLoad(world, this, itemStack));
        }
    }

}
