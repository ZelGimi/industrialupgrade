package com.denfop.items.armour;

import com.denfop.Config;
import com.denfop.Constants;
import com.denfop.IUCore;
import com.denfop.api.IModelRegister;
import com.denfop.utils.EnumInfoUpgradeModules;
import com.denfop.utils.ModUtils;
import ic2.api.item.ElectricItem;
import ic2.api.item.IElectricItem;
import ic2.api.item.IItemHudInfo;
import ic2.api.item.IMetalArmor;
import ic2.core.IC2;
import ic2.core.IC2Potion;
import ic2.core.init.BlocksItems;
import ic2.core.init.Localization;
import ic2.core.item.BaseElectricItem;
import ic2.core.item.ItemTinCan;
import ic2.core.item.armor.ItemArmorElectric;
import ic2.core.ref.ItemName;
import ic2.core.util.LogCategory;
import ic2.core.util.StackUtil;
import net.minecraft.block.material.MapColor;
import net.minecraft.client.renderer.block.model.ModelBakery;
import net.minecraft.client.renderer.block.model.ModelResourceLocation;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Items;
import net.minecraft.init.MobEffects;
import net.minecraft.inventory.EntityEquipmentSlot;
import net.minecraft.item.Item;
import net.minecraft.item.ItemArmor;
import net.minecraft.item.ItemFood;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.potion.Potion;
import net.minecraft.potion.PotionEffect;
import net.minecraft.util.ActionResult;
import net.minecraft.util.DamageSource;
import net.minecraft.util.EnumActionResult;
import net.minecraft.util.NonNullList;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.client.model.ModelLoader;
import net.minecraftforge.common.ISpecialArmor;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

public class ItemSolarPanelHelmet extends ItemArmorElectric implements IElectricItem, IModelRegister, IMetalArmor, ISpecialArmor,
        IItemHudInfo {

    protected static final Map<Potion, Integer> potionRemovalCost = new HashMap<Potion, Integer>();
    private final int solarType;
    private final String name;
    private double maxCharge;
    private double transferLimit;
    private int tier;
    private double genDay;
    private double genNight;
    private int energyPerDamage;
    private double damageAbsorptionRatio;
    private double baseAbsorptionRatio;
    private boolean sunIsUp;
    private boolean skyIsVisible;
    private boolean ret = false;
    private double storage;
    private double maxstorage;

    public ItemSolarPanelHelmet(
            final ItemArmor.ArmorMaterial par2EnumArmorMaterial, final int par3, final int par4,
            final int htype, String name
    ) {
        super(null, "", EntityEquipmentSlot.HEAD,
                htype == 1 ? 1000000.0 : htype == 2 ? 1.0E7 : htype == 3 ? 10000.0 : Config.Storagequantumsuit,
                htype == 1 ? 3000.0 : htype == 2 ? 10000.0 : htype == 3 ? 10000.0 : 38000.0,
                htype == 1 ? 1 : htype == 2 ? 2 : htype == 3 ? 3 : htype == 4 ? 5 : 7
        );

        this.solarType = htype;
        this.name = name;
        this.transferLimit = 3000.0;
        this.tier = 1;
        if (this.solarType == 1) {
            this.genDay = Config.advGenDay;
            this.genNight = genDay / 2;
            this.maxCharge = 1000000.0;
            this.energyPerDamage = 800;
            this.damageAbsorptionRatio = 0.9;
            this.baseAbsorptionRatio = 0.15;
            this.storage = 0;
            this.maxstorage = Config.advStorage / 2;

        }
        if (this.solarType == 2) {
            this.genDay = Config.hGenDay;
            this.genNight = genDay / 2;
            this.maxCharge = 1.0E7;
            this.transferLimit = 10000.0;
            this.tier = 2;
            this.energyPerDamage = 2000;
            this.damageAbsorptionRatio = 1.0;
            this.baseAbsorptionRatio = 0.15;
            this.storage = 0;
            this.maxstorage = Config.hStorage / 2;
        }
        if (this.solarType == 3) {
            this.genDay = Config.uhGenDay;
            this.genNight = genDay / 2;
            this.maxCharge = 1.0E7;
            this.transferLimit = 10000.0;
            this.tier = 3;
            this.energyPerDamage = 2000;
            this.damageAbsorptionRatio = 1.0;
            this.baseAbsorptionRatio = 0.15;
            this.storage = 0;
            this.maxstorage = Config.uhStorage / 2;
        }
        if (this.solarType == 4) {
            this.genDay = Config.spectralpanelGenDay;
            this.genNight = genDay / 2;
            this.transferLimit = 38000.0;
            this.maxCharge = Config.Storagequantumsuit;
            this.tier = 5;
            this.energyPerDamage = 800;
            this.damageAbsorptionRatio = 0.9;
            this.baseAbsorptionRatio = 0.15;
            this.storage = 0;
            this.maxstorage = Config.spectralpanelstorage / 2;
        }
        if (this.solarType == 5) {
            this.genDay = Config.singularpanelGenDay;
            this.genNight = genDay / 2;
            this.transferLimit = 100000.0;
            this.maxCharge = Config.Storagequantumsuit;
            this.tier = 7;
            this.energyPerDamage = 2000;
            this.damageAbsorptionRatio = 1.0;
            this.baseAbsorptionRatio = 0.15;
            this.storage = 0;
            this.maxstorage = Config.singularpanelstorage / 2;
        }
        this.setCreativeTab(IUCore.EnergyTab);
        this.setMaxDamage(27);
        potionRemovalCost.put(MobEffects.POISON, 100);
        potionRemovalCost.put(IC2Potion.radiation, 20);
        potionRemovalCost.put(MobEffects.WITHER, 100);
        potionRemovalCost.put(MobEffects.HUNGER, 200);
        this.setUnlocalizedName(name);
        BlocksItems.registerItem((Item) this, IUCore.getIdentifier(name)).setUnlocalizedName(name);
        IUCore.proxy.addIModelRegister(this);
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

    public String getUnlocalizedName() {
        return "item." + super.getUnlocalizedName().substring(4) + ".name";
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
        loc.append("armour").append("/").append(name + extraName);

        return new ModelResourceLocation(loc.toString(), null);
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

        NBTTagCompound nbtData = ModUtils.nbt(stack);
        if (!nbtData.getString("mode").isEmpty()) {
            return Constants.TEXTURES + ":textures/armor/" + this.name + "_" + nbtData.getString("mode") + ".png";
        }


        return Constants.TEXTURES + ":textures/armor/" + this.name + ".png";
    }

    public boolean isBookEnchantable(ItemStack stack, ItemStack book) {
        return true;
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

    public void onArmorTick(World worldObj, EntityPlayer player, ItemStack itemStack) {
        if (worldObj.isRemote) {
            return;
        }
        gainFuel(player);
        NBTTagCompound nbtData = ModUtils.nbt(itemStack);
        NBTTagCompound nbt = ModUtils.nbt(itemStack);
        int resistance = 0;
        int repaired = 0;
        for (int i = 0; i < 4; i++) {
            if (nbtData.getString("mode_module" + i).equals("invisibility")) {
                player.addPotionEffect(new PotionEffect(MobEffects.INVISIBILITY, 300));
            }
            if (nbtData.getString("mode_module" + i).equals("resistance")) {
                resistance++;
            }
            if (nbtData.getString("mode_module" + i).equals("repaired")) {
                repaired++;
            }
        }
        if (repaired != 0) {
            if (worldObj.provider.getWorldTime() % 80 == 0) {
                ElectricItem.manager.charge(
                        itemStack,
                        this.getMaxCharge(itemStack) * 0.00001 * repaired,
                        Integer.MAX_VALUE,
                        true,
                        false
                );
            }
        }
        if (resistance != 0) {
            player.addPotionEffect(new PotionEffect(MobEffects.RESISTANCE, 300, resistance));
        }

        int genday = 0;
        int gennight = 0;
        int storage = 0;
        for (int i = 0; i < 4; i++) {
            if (nbt.getString("mode_module" + i).equals("genday")) {
                genday++;
            }
            if (nbt.getString("mode_module" + i).equals("gennight")) {
                gennight++;
            }
            if (nbt.getString("mode_module" + i).equals("storage")) {
                storage++;
            }
        }
        genday = Math.min(genday, EnumInfoUpgradeModules.GENDAY.max);
        gennight = Math.min(gennight, EnumInfoUpgradeModules.GENNIGHT.max);
        storage = Math.min(storage, EnumInfoUpgradeModules.STORAGE.max);
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
        if (nbtData.getDouble("storage") >= (maxstorage + maxstorage * 0.05 * storage)) {
            nbtData.setDouble("storage", (maxstorage + maxstorage * 0.05 * storage));
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
                    IC2.platform.removePotion(player, effect.getPotion());
                    ret = true;
                }
            }
        }
        if (ElectricItem.manager.canUse(itemStack, 1000.0D) && player.getFoodStats().needFood()) {
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
                stack = can.onItemUseFinish(stack, worldObj, player);
                if (stack.stackSize <= 0) {
                    player.inventory.mainInventory.set(slot, new ItemStack(Items.AIR));
                }
                ElectricItem.manager.use(itemStack, 1000.0D, null);
                ret = true;
            }
            for (int i = 0; i < player.inventory.mainInventory.size(); ++i) {
                ItemStack playerStack = player.inventory.mainInventory.get(i);
                if (!StackUtil.isEmpty(playerStack) && playerStack.getItem() == ItemName.filled_tin_can.getInstance()) {
                    slot = i;
                    break;
                }
            }

            if (slot > -1) {
                ItemStack playerStack = player.inventory.mainInventory.get(slot);
                ItemTinCan can = (ItemTinCan) playerStack.getItem();
                ActionResult<ItemStack> result = can.onEaten(player, playerStack);
                playerStack = result.getResult();
                if (StackUtil.isEmpty(playerStack)) {
                    player.inventory.mainInventory.set(slot, StackUtil.emptyStack);
                }

                if (result.getType() == EnumActionResult.SUCCESS) {
                    ElectricItem.manager.use(itemStack, 1000.0D, null);
                }

                ret = true;
            }
        } else if (player.getFoodStats().getFoodLevel() <= 0) {
            IC2.achievements.issueAchievement(player, "starveWithQHelmet");
        }
        if (this.solarType == 2 || this.solarType == 3) {
            int airLevel = player.getAir();
            if (ElectricItem.manager.canUse(itemStack, 1000.0D) && airLevel < 100) {
                player.setAir(airLevel + 200);
                ElectricItem.manager.use(itemStack, 1000.0D, null);
            }
        }

        double tempstorage = nbtData.getDouble("storage");
        if (tempstorage > 0) {
            double energyLeft = tempstorage;
            for (int i = 0; i < player.inventory.armorInventory.size(); i++) {
                if (energyLeft > 0) {
                    if (!player.inventory.armorInventory.get(i).isEmpty()
                            && player.inventory.armorInventory.get(i).getItem() instanceof IElectricItem) {
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
                            && player.inventory.mainInventory.get(j).getItem() instanceof ic2.api.item.IElectricItem) {
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
            final EntityLivingBase player, final ItemStack armor,
            final DamageSource source, final double damage, final int slot
    ) {
        if (source.isUnblockable()) {
            return new ISpecialArmor.ArmorProperties(0, 0.0, 0);
        }

        final double absorptionRatio = this.getBaseAbsorptionRatio() * this.getDamageAbsorptionRatio();
        NBTTagCompound nbt = ModUtils.nbt(armor);
        int protect = 0;
        for (int i = 0; i < 4; i++) {
            if (nbt.getString("mode_module" + i).equals("protect")) {
                protect++;
            }

        }
        protect = Math.min(protect, EnumInfoUpgradeModules.PROTECTION.max);

        final int energyPerDamage = (int) (this.getEnergyPerDamage() - this.getEnergyPerDamage() * 0.2 * protect);
        final int damageLimit = (int) ((energyPerDamage > 0)
                ? (25.0 * ElectricItem.manager.getCharge(armor) / energyPerDamage)
                : 0.0);
        return new ISpecialArmor.ArmorProperties(0, absorptionRatio, damageLimit);
    }

    public int getArmorDisplay(final EntityPlayer player, final ItemStack armor, final int slot) {
        if (ElectricItem.manager.getCharge(armor) >= this.getEnergyPerDamage()) {
            return (int) Math.round(20.0 * this.getBaseAbsorptionRatio() * this.getDamageAbsorptionRatio());
        }
        return 0;
    }

    public void damageArmor(
            final EntityLivingBase entity, final ItemStack stack, final DamageSource source,
            final int damage, final int slot
    ) {
        NBTTagCompound nbt = ModUtils.nbt(stack);
        int protect = 0;
        for (int i = 0; i < 4; i++) {
            if (nbt.getString("mode_module" + i).equals("protect")) {
                protect++;
            }

        }
        protect = Math.min(protect, EnumInfoUpgradeModules.PROTECTION.max);

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
    public void getSubItems(final CreativeTabs p_150895_1_, final NonNullList<ItemStack> var3) {
        if (this.isInCreativeTab(p_150895_1_)) {
            final ItemStack var4 = new ItemStack(this, 1);
            ElectricItem.manager.charge(var4, 2.147483647E9, Integer.MAX_VALUE, true, false);
            var3.add(var4);
            var3.add(new ItemStack(this, 1, this.getMaxDamage()));
        }
    }


    public boolean canProvideEnergy(final ItemStack itemStack) {
        return false;
    }


    public double getMaxCharge(final ItemStack itemStack) {
        return this.maxCharge;
    }

    public int getTier(final ItemStack itemStack) {
        return this.tier;
    }

    public double getTransferLimit(final ItemStack itemStack) {
        return this.transferLimit;
    }

    @Override
    public void addInformation(
            final ItemStack itemStack,
            @Nullable final World p_77624_2_,
            final List<String> info,
            final ITooltipFlag p_77624_4_
    ) {
        NBTTagCompound nbtData1 = ModUtils.nbt(itemStack);

        info.add(Localization.translate("iu.storage.helmet") + " "
                + ModUtils.getString(nbtData1.getDouble("storage")) + " EU");
    }


}
