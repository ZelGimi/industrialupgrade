package com.denfop.items.armour;


import cofh.redstoneflux.api.IEnergyContainerItem;
import com.denfop.*;
import com.denfop.api.IModelRegister;
import com.denfop.api.item.IEnergyItem;
import com.denfop.api.upgrade.EnumUpgrades;
import com.denfop.api.upgrade.IUpgradeItem;
import com.denfop.api.upgrade.UpgradeSystem;
import com.denfop.api.upgrade.event.EventItemLoad;
import com.denfop.items.EnumInfoUpgradeModules;
import com.denfop.proxy.CommonProxy;
import com.denfop.register.Register;
import com.denfop.utils.ModUtils;
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
import net.minecraft.util.*;
import net.minecraft.util.text.TextFormatting;
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

public class ItemLappack extends ItemArmorEnergy implements IEnergyItem, IModelRegister, ISpecialArmor,
        IUpgradeItem {

    private final double maxCharge;

    private final double transferLimit;

    private final int tier;


    private final String name;

    public ItemLappack(
            String name,
            double MaxCharge,
            int Tier,
            double TransferLimit
    ) {
        super("", EntityEquipmentSlot.CHEST, MaxCharge, TransferLimit, Tier);

        this.maxCharge = MaxCharge;
        this.transferLimit = TransferLimit;
        setCreativeTab(IUCore.EnergyTab);
        this.setUnlocalizedName(name);

        this.name = name;
        this.tier = Tier;
        Register.registerItem((Item) this, IUCore.getIdentifier(name)).setUnlocalizedName(name);
        IUCore.proxy.addIModelRegister(this);
        UpgradeSystem.system.addRecipe(this, EnumUpgrades.LAPPACK.list);

    }

    @SideOnly(Side.CLIENT)
    public static ModelResourceLocation getModelLocation1(String name, String extraName) {
        final String loc = Constants.MOD_ID +
                ':' +
                "armour" + "/" + name + extraName;

        return new ModelResourceLocation(loc, null);
    }

    public static int readToolMode(ItemStack itemstack) {
        NBTTagCompound nbttagcompound = ModUtils.nbt(itemstack);
        int toolMode = nbttagcompound.getInteger("toolMode");
        if (toolMode < 0 || toolMode > 1) {
            toolMode = 0;
        }
        return toolMode;
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

    public void setDamage(ItemStack stack, int damage) {
        int prev = this.getDamage(stack);


    }

    public String getUnlocalizedName() {
        return "item." + super.getUnlocalizedName().substring(3) + ".name";
    }

    @Override
    public double getDamageAbsorptionRatio() {
        return 0.4;
    }

    @Override
    public void registerModels() {
        registerModels(this.name);
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

    public ISpecialArmor.ArmorProperties getProperties(
            EntityLivingBase player,
            @Nonnull ItemStack armor,
            DamageSource source,
            double damage,
            int slot
    ) {
        double absorptionRatio = getBaseAbsorptionRatio() * 0;
        int energyPerDamage = this.getEnergyPerDamage();
        int protect = (UpgradeSystem.system.hasModules(EnumInfoUpgradeModules.PROTECTION, armor) ?
                UpgradeSystem.system.getModules(EnumInfoUpgradeModules.PROTECTION, armor).number : 0);

        int damageLimit = (int) ((energyPerDamage > 0)
                ? (25.0D * ElectricItem.manager.getCharge(armor) / (energyPerDamage - energyPerDamage * protect * 0.2))
                : 0.0D);
        return new ISpecialArmor.ArmorProperties(0, absorptionRatio, damageLimit);
    }

    public int getEnergyPerDamage() {
        return 10000;
    }


    public boolean isMetalArmor(ItemStack itemstack, EntityPlayer player) {
        return true;
    }

    public int getArmorDisplay(EntityPlayer player, @Nonnull ItemStack armor, int slot) {
        return (int) Math.round(20.0D * getBaseAbsorptionRatio() * 0);
    }

    public boolean isRepairable() {
        return false;
    }

    public int getItemEnchantability() {
        return 0;
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


    public boolean canProvideEnergy(ItemStack itemStack) {
        return true;
    }

    public double getMaxEnergy(ItemStack itemStack) {
        return this.maxCharge;
    }

    public short getTierItem(ItemStack itemStack) {
        return (short) this.tier;
    }

    public double getTransferEnergy(ItemStack itemStack) {
        return this.transferLimit;
    }


    public void saveToolMode(ItemStack itemstack, Integer toolMode) {
        NBTTagCompound nbttagcompound = ModUtils.nbt(itemstack);
        nbttagcompound.setInteger("toolMode", toolMode);
    }

    @Nonnull
    @Override
    public ActionResult<ItemStack> onItemRightClick(
            @Nonnull final World world,
            final EntityPlayer player,
            @Nonnull final EnumHand hand
    ) {
        ItemStack itemStack = player.getHeldItem(hand);
        if (IUCore.keyboard.isChangeKeyDown(player)) {
            int toolMode = readToolMode(itemStack);
            toolMode++;
            if (toolMode > 1) {
                toolMode = 0;
            }
            saveToolMode(itemStack, toolMode);
            if (toolMode == 0) {
                CommonProxy.sendPlayerMessage(
                        player,
                        TextFormatting.GOLD + Localization.translate("iu.message.text.powerSupply") + " " + TextFormatting.RED + Localization.translate(
                                "iu.message.text.disabled")
                );
            }
            if (toolMode == 1) {
                CommonProxy.sendPlayerMessage(player, TextFormatting.GOLD + Localization.translate("iu.message.text" +
                        ".powerSupply") + " " + TextFormatting.GREEN + Localization.translate("iu.message.text.enabled"));
            }
        }
        return new ActionResult<>(EnumActionResult.PASS, itemStack);
    }


    @Override
    public void addInformation(
            @Nonnull final ItemStack par1ItemStack,
            @Nullable final World p_77624_2_,
            @Nonnull final List<String> par3List,
            @Nonnull final ITooltipFlag p_77624_4_
    ) {
        int toolMode = readToolMode(par1ItemStack);
        if (toolMode == 0) {
            par3List.add(TextFormatting.GOLD + Localization.translate("iu.message.text.powerSupply") + ": " + TextFormatting.RED + Localization.translate(
                    "iu.message.text.disabled"));
        }
        if (toolMode == 1) {
            par3List.add(TextFormatting.GOLD + Localization.translate("iu.message.text.powerSupply") + ": " + TextFormatting.GREEN + Localization.translate(
                    "iu.message.text.enabled"));
        }
        if (!Keyboard.isKeyDown(Keyboard.KEY_LSHIFT)) {
            par3List.add(Localization.translate("press.lshift"));
        }
        if (Keyboard.isKeyDown(Keyboard.KEY_LSHIFT)) {
            par3List.add(Localization.translate("iu.changemode_key") + "M + " + Localization.translate(
                    "iu.changemode_rcm1"));
        }
    }


    public void onArmorTick(@Nonnull World world, @Nonnull EntityPlayer player, @Nonnull ItemStack itemStack) {
        NBTTagCompound nbtData = ModUtils.nbt(itemStack);

        byte toggleTimer = nbtData.getByte("toggleTimer");
        if (IUCore.keyboard.isChangeKeyDown(player) && toggleTimer == 0) {
            toggleTimer = 10;
            int toolMode = readToolMode(itemStack);
            toolMode++;
            if (toolMode > 1) {
                toolMode = 0;
            }
            saveToolMode(itemStack, toolMode);
            if (toolMode == 0) {
                CommonProxy.sendPlayerMessage(
                        player,
                        TextFormatting.GOLD + Localization.translate("iu.message.text.powerSupply") + " " + TextFormatting.RED + Localization.translate(
                                "iu.message.text.disabled")
                );
            }
            if (toolMode == 1) {
                CommonProxy.sendPlayerMessage(
                        player,
                        TextFormatting.GOLD + Localization.translate("iu.message.text.powerSupply") + " " + TextFormatting.GREEN + Localization.translate(
                                "iu.message.text.enabled")
                );
            }
        }
        if (IUCore.proxy.isSimulating() && toggleTimer > 0) {
            toggleTimer = (byte) (toggleTimer - 1);
            nbtData.setByte("toggleTimer", toggleTimer);
        }
        int toolMode = readToolMode(itemStack);
        boolean ret = false;
        double energy3 = (UpgradeSystem.system.hasModules(EnumInfoUpgradeModules.LAPPACK_ENERGY, itemStack) ?
                UpgradeSystem.system.getModules(EnumInfoUpgradeModules.LAPPACK_ENERGY, itemStack).number : 0);
        if (energy3 != 0) {
            energy3 *= 0.005;
        }
        if (toolMode == 1) {

            for (int i = 0; i < player.inventory.armorInventory.size(); i++) {

                if (!player.inventory.armorInventory.get(i).isEmpty() && player.inventory.armorInventory
                        .get(i)
                        .getItem() instanceof IEnergyItem) {
                    if (ElectricItem.manager.getCharge(itemStack) > 0 && !(itemStack.isItemEqual(player.inventory.armorInventory.get(
                            i)))) {

                        double sentPacket = ElectricItem.manager.charge(
                                player.inventory.armorInventory.get(i),
                                ElectricItem.manager.getCharge(itemStack),
                                2147483647,
                                true,
                                false
                        );

                        if (sentPacket > 0.0D) {
                            ElectricItem.manager.discharge(itemStack, sentPacket, Integer.MAX_VALUE, true, false,
                                    false
                            );
                            ElectricItem.manager.charge(
                                    itemStack,
                                    sentPacket * energy3,
                                    2147483647,
                                    true,
                                    false
                            );
                            ret = true;

                        }
                    }
                }
                IEnergyContainerItem item;
                if (!player.inventory.armorInventory.get(i).isEmpty()
                        && player.inventory.armorInventory.get(i).getItem() instanceof IEnergyContainerItem) {
                    if (ElectricItem.manager.getCharge(itemStack) > 0 && !(itemStack.isItemEqual(player.inventory.armorInventory.get(
                            i)))) {
                        item = (IEnergyContainerItem) player.inventory.armorInventory.get(i).getItem();

                        int amountRfCanBeReceivedIncludesLimit = item.receiveEnergy(
                                player.inventory.armorInventory.get(i),
                                Integer.MAX_VALUE,
                                true
                        );
                        double realSentEnergyRF = Math.min(
                                amountRfCanBeReceivedIncludesLimit,
                                ElectricItem.manager.getCharge(itemStack) * Config.coefficientrf
                        );
                        item.receiveEnergy(player.inventory.armorInventory.get(i), (int) realSentEnergyRF, false);
                        if (realSentEnergyRF > 0) {
                            ElectricItem.manager.discharge(
                                    itemStack,
                                    (realSentEnergyRF / (double) Config.coefficientrf),
                                    Integer.MAX_VALUE,
                                    true,
                                    false,
                                    false
                            );
                            ElectricItem.manager.charge(
                                    itemStack,
                                    (realSentEnergyRF / (double) Config.coefficientrf) * energy3,
                                    2147483647,
                                    true,
                                    false
                            );
                        }
                    }
                }
            }
            for (int j = 0; j < player.inventory.mainInventory.size(); j++) {

                if (!player.inventory.mainInventory.get(j).isEmpty()
                        && player.inventory.mainInventory.get(j).getItem() instanceof IEnergyItem) {
                    if (ElectricItem.manager.getCharge(itemStack) > 0) {
                        double sentPacket = ElectricItem.manager.charge(
                                player.inventory.mainInventory.get(j),
                                ElectricItem.manager.getCharge(itemStack),
                                2147483647,
                                true,
                                false
                        );


                        if (sentPacket > 0.0D) {
                            ElectricItem.manager.discharge(itemStack, sentPacket, Integer.MAX_VALUE, true, false, false);
                            ElectricItem.manager.charge(
                                    itemStack,
                                    sentPacket * energy3,
                                    2147483647,
                                    true,
                                    false
                            );
                            ret = true;

                        }
                    }

                }
                IEnergyContainerItem item;
                if (!player.inventory.mainInventory.get(j).isEmpty()
                        && player.inventory.mainInventory.get(j).getItem() instanceof IEnergyContainerItem) {
                    if (ElectricItem.manager.getCharge(itemStack) > 0) {
                        item = (IEnergyContainerItem) player.inventory.mainInventory.get(j).getItem();

                        int amountRfCanBeReceivedIncludesLimit = item.receiveEnergy(
                                player.inventory.mainInventory.get(j),
                                Integer.MAX_VALUE,
                                true
                        );
                        double realSentEnergyRF = Math.min(
                                amountRfCanBeReceivedIncludesLimit,
                                ElectricItem.manager.getCharge(itemStack) * Config.coefficientrf
                        );
                        item.receiveEnergy(player.inventory.mainInventory.get(j), (int) realSentEnergyRF, false);
                        if (realSentEnergyRF > 0) {
                            ElectricItem.manager.discharge(
                                    itemStack,
                                    (realSentEnergyRF / (double) Config.coefficientrf),
                                    Integer.MAX_VALUE,
                                    true,
                                    false,
                                    false
                            );
                            ElectricItem.manager.charge(
                                    itemStack,
                                    (realSentEnergyRF / (double) Config.coefficientrf) * energy3,
                                    2147483647,
                                    true,
                                    false
                            );
                        }
                    }
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
            if (ret) {
                player.openContainer.detectAndSendChanges();
            }
        }
    }

    public List<String> getHudInfo(ItemStack stack, boolean advanced) {
        List<String> info = new ArrayList<>();
        info.add(ElectricItem.manager.getToolTip(stack));
        return info;
    }

}
