package com.denfop.items.armour;


import com.denfop.Constants;
import com.denfop.ElectricItem;
import com.denfop.IUCore;
import com.denfop.Localization;
import com.denfop.api.item.IEnergyItem;
import com.denfop.api.upgrade.EnumUpgrades;
import com.denfop.api.upgrade.IUpgradeItem;
import com.denfop.api.upgrade.UpgradeSystem;
import com.denfop.api.upgrade.event.EventItemLoad;
import com.denfop.items.EnumInfoUpgradeModules;
import com.denfop.items.IProperties;
import com.denfop.proxy.CommonProxy;
import com.denfop.utils.KeyboardClient;
import com.denfop.utils.KeyboardIU;
import com.denfop.utils.ModUtils;
import com.mojang.blaze3d.platform.InputConstants;
import net.minecraft.ChatFormatting;
import net.minecraft.Util;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.core.NonNullList;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ArmorMaterial;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.level.Level;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.api.distmarker.OnlyIn;
import net.neoforged.neoforge.common.NeoForge;
import org.jetbrains.annotations.Nullable;

import javax.annotation.Nonnull;
import java.util.List;

public class ItemLappack extends ItemArmorEnergy implements IEnergyItem, ISpecialArmor,
        IUpgradeItem, IProperties {

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
        super("", Type.CHESTPLATE, MaxCharge, TransferLimit, Tier);

        this.maxCharge = MaxCharge;
        this.transferLimit = TransferLimit;
        this.name = name;
        this.tier = Tier;

        IUCore.runnableListAfterRegisterItem.add(() -> UpgradeSystem.system.addRecipe(this, EnumUpgrades.LAPPACK.list));

    }

    public static int readToolMode(ItemStack itemstack) {
        CompoundTag nbttagcompound = ModUtils.nbt(itemstack);
        int toolMode = nbttagcompound.getInt("toolMode");
        if (toolMode < 0 || toolMode > 1) {
            toolMode = 0;
        }
        return toolMode;
    }

    @Override
    public void fillItemCategory(CreativeModeTab p_41391_, NonNullList<ItemStack> p_41392_) {
        if (this.allowedIn(p_41391_)) {
            final ItemStack var4 = new ItemStack(this, 1);
            ElectricItem.manager.charge(var4, 2.147483647E9, Integer.MAX_VALUE, true, false);
            p_41392_.add(var4);
            p_41392_.add(new ItemStack(this, 1));
        }
    }

    protected String getOrCreateDescriptionId() {
        if (this.nameItem == null) {
            StringBuilder pathBuilder = new StringBuilder(Util.makeDescriptionId("iu", BuiltInRegistries.ITEM.getKey(this)));
            String targetString = "industrialupgrade.";
            String replacement = "";
            if (replacement != null) {
                int index = pathBuilder.indexOf(targetString);
                while (index != -1) {
                    pathBuilder.replace(index, index + targetString.length(), replacement);
                    index = pathBuilder.indexOf(targetString, index + replacement.length());
                }
            }
            this.nameItem = "item." + pathBuilder.toString().split("\\.")[2];
        }

        return this.nameItem;
    }

    @Override
    public void inventoryTick(ItemStack stack, Level worldIn, Entity p_41406_, int p_41407_, boolean p_41408_) {
        super.inventoryTick(stack, worldIn, p_41406_, p_41407_, p_41408_);

        if (!UpgradeSystem.system.hasInMap(stack)) {
            NeoForge.EVENT_BUS.post(new EventItemLoad(worldIn, this, stack));
        }
        if (p_41407_ >= Inventory.INVENTORY_SIZE && p_41407_ < Inventory.INVENTORY_SIZE + 4 && p_41406_ instanceof Player player)
            this.onArmorTick(stack, worldIn, (Player) p_41406_);
    }


    public void setDamage(ItemStack stack, int damage) {
        int prev = this.getDamage(stack);


    }


    @Override
    public double getDamageAbsorptionRatio() {
        return 0.4;
    }

    @Override
    public @Nullable ResourceLocation getArmorTexture(ItemStack stack, Entity entity, EquipmentSlot slot, ArmorMaterial.Layer layer, boolean innerModel) {
        CompoundTag nbtData = ModUtils.nbt(stack);
        if (!nbtData.getString("mode").isEmpty()) {
            return ResourceLocation.parse(Constants.TEXTURES + ":textures/armor/" + this.name + "_" + nbtData.getString("mode") + ".png");
        }


        return ResourceLocation.parse(Constants.TEXTURES + ":textures/armor/" + this.name + ".png");
    }


    public ISpecialArmor.ArmorProperties getProperties(
            LivingEntity player,
            @Nonnull ItemStack armor,
            DamageSource source,
            double damage,
            int slot
    ) {
        double absorptionRatio = getBaseAbsorptionRatio() * this.getDamageAbsorptionRatio();
        int energyPerDamage = this.getEnergyPerDamage();
        int protect = (UpgradeSystem.system.hasModules(EnumInfoUpgradeModules.PROTECTION, armor) ?
                UpgradeSystem.system.getModules(EnumInfoUpgradeModules.PROTECTION, armor).number : 0);

        int damageLimit = (int) ((energyPerDamage > 0)
                ? (25.0D * ElectricItem.manager.getCharge(armor) / (energyPerDamage - energyPerDamage * protect * 0.2))
                : 0.0D);
        return new ISpecialArmor.ArmorProperties(0, absorptionRatio, damageLimit);
    }

    public int getEnergyPerDamage() {
        return (int) (maxCharge * 0.05);
    }


    public int getArmorDisplay(Player player, @Nonnull ItemStack armor, int slot) {
        return (int) Math.round(20.0D * getBaseAbsorptionRatio() * this.getDamageAbsorptionRatio());
    }

    public boolean isRepairable() {
        return false;
    }

    public int getItemEnchantability() {
        return 0;
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
        CompoundTag nbttagcompound = ModUtils.nbt(itemstack);
        nbttagcompound.putInt("toolMode", toolMode);
    }


    @Override
    public InteractionResultHolder<ItemStack> use(Level p_41432_, Player player, InteractionHand hand) {


        ItemStack itemStack = player.getItemInHand(hand);
        if (IUCore.keyboard.isChangeKeyDown(player)) {
            int toolMode = readToolMode(itemStack);
            toolMode++;
            if (toolMode > 1) {
                toolMode = 0;
            }
            saveToolMode(itemStack, toolMode);
            if (toolMode == 0) {
                if (!p_41432_.isClientSide)
                    CommonProxy.sendPlayerMessage(
                            player,
                            ChatFormatting.GOLD + Localization.translate("iu.message.text.powerSupply") + " " + ChatFormatting.RED + Localization.translate(
                                    "iu.message.text.disabled")
                    );
            }
            if (toolMode == 1) {
                if (!p_41432_.isClientSide)
                    CommonProxy.sendPlayerMessage(player, ChatFormatting.GOLD + Localization.translate("iu.message.text" +
                            ".powerSupply") + " " + ChatFormatting.GREEN + Localization.translate("iu.message.text.enabled"));
            }
            return new InteractionResultHolder<>(InteractionResult.SUCCESS, itemStack);
        }
        return new InteractionResultHolder<>(InteractionResult.PASS, itemStack);

    }


    @Override
    public void appendHoverText(ItemStack par1ItemStack, @Nullable TooltipContext p_41422_, List<Component> par3List, TooltipFlag p_41424_) {
        super.appendHoverText(par1ItemStack, p_41422_, par3List, p_41424_);
        int toolMode = readToolMode(par1ItemStack);
        par3List.add(Component.literal(Localization.translate("iu.lappack.info")));
        if (toolMode == 0) {
            par3List.add(Component.literal(ChatFormatting.GOLD + Localization.translate("iu.message.text.powerSupply") + ": " + ChatFormatting.RED + Localization.translate(
                    "iu.message.text.disabled")));
        }
        if (toolMode == 1) {
            par3List.add(Component.literal(ChatFormatting.GOLD + Localization.translate("iu.message.text.powerSupply") + ": " + ChatFormatting.GREEN + Localization.translate(
                    "iu.message.text.enabled")));
        }
        if (!KeyboardIU.isKeyDown(InputConstants.KEY_LSHIFT)) {
            par3List.add(Component.literal(Localization.translate("press.lshift")));
        }
        if (KeyboardIU.isKeyDown(InputConstants.KEY_LSHIFT)) {
            par3List.add(Component.literal(Localization.translate("iu.changemode_key") + KeyboardClient.changemode.getKey().getDisplayName().getString() + " +" +
                    " " + Localization.translate(
                    "iu.changemode_rcm1")));
        }
    }


    public void onArmorTick(ItemStack itemStack, Level world, Player player) {
        if (world.isClientSide)
            return;

        CompoundTag nbtData = ModUtils.nbt(itemStack);

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
                        ChatFormatting.GOLD + Localization.translate("iu.message.text.powerSupply") + " " + ChatFormatting.RED + Localization.translate(
                                "iu.message.text.disabled")
                );
            }
            if (toolMode == 1) {
                CommonProxy.sendPlayerMessage(
                        player,
                        ChatFormatting.GOLD + Localization.translate("iu.message.text.powerSupply") + " " + ChatFormatting.GREEN + Localization.translate(
                                "iu.message.text.enabled")
                );
            }
        }
        if (!player.level().isClientSide() && toggleTimer > 0) {
            toggleTimer = (byte) (toggleTimer - 1);
            nbtData.putByte("toggleTimer", toggleTimer);
        }
        int toolMode = readToolMode(itemStack);
        boolean ret = false;
        double energy3 = (UpgradeSystem.system.hasModules(EnumInfoUpgradeModules.LAPPACK_ENERGY, itemStack) ?
                UpgradeSystem.system.getModules(EnumInfoUpgradeModules.LAPPACK_ENERGY, itemStack).number : 0);
        if (energy3 != 0) {
            energy3 *= 0.005;
        }
        if (toolMode == 1) {

            for (int i = 0; i < player.getInventory().armor.size(); i++) {

                if (!player.getInventory().armor.get(i).isEmpty() && player.getInventory().armor
                        .get(i)
                        .getItem() instanceof IEnergyItem) {
                    if (ElectricItem.manager.getCharge(itemStack) > 0 && !(itemStack.is(player.getInventory().armor.get(
                            i).getItem()))) {

                        double sentPacket = ElectricItem.manager.charge(
                                player.getInventory().armor.get(i),
                                ElectricItem.manager.getCharge(itemStack),
                                2147483647,
                                true,
                                false
                        );

                        if (sentPacket > 0.0D) {
                            ElectricItem.manager.discharge(itemStack, sentPacket, Integer.MAX_VALUE, true, false,
                                    false
                            );
                            ret = true;

                        }
                    }
                }
            }
            for (int j = 0; j < player.getInventory().items.size(); j++) {

                if (!player.getInventory().items.get(j).isEmpty()
                        && player.getInventory().items.get(j).getItem() instanceof IEnergyItem) {
                    if (ElectricItem.manager.getCharge(itemStack) > 0) {
                        double sentPacket = ElectricItem.manager.charge(
                                player.getInventory().items.get(j),
                                ElectricItem.manager.getCharge(itemStack),
                                2147483647,
                                true,
                                false
                        );


                        if (sentPacket > 0.0D) {
                            ElectricItem.manager.discharge(itemStack, sentPacket, Integer.MAX_VALUE, true, false, false);
                            ret = true;

                        }
                    }

                }
            }
            boolean fireResistance = UpgradeSystem.system.hasModules(EnumInfoUpgradeModules.FIRE_PROTECTION, itemStack);
            if (fireResistance) {
                player.addEffect(new MobEffectInstance(MobEffects.FIRE_RESISTANCE, 300));
            }
            int resistance = (UpgradeSystem.system.hasModules(EnumInfoUpgradeModules.RESISTANCE, itemStack) ?
                    UpgradeSystem.system.getModules(EnumInfoUpgradeModules.RESISTANCE, itemStack).number : 0);

            if (resistance != 0) {
                player.addEffect(new MobEffectInstance(MobEffects.DAMAGE_RESISTANCE, 300, resistance));
            }
            if (UpgradeSystem.system.hasModules(EnumInfoUpgradeModules.INVISIBILITY, itemStack)) {
                player.addEffect(new MobEffectInstance(MobEffects.INVISIBILITY, 300));
            }
            if (ret) {
                player.containerMenu.broadcastChanges();
            }
        }
    }


    @Override
    public String[] properties() {
        return new String[]{"", "Demon", "Dark", "Cold", "Ender"};
    }

    @OnlyIn(Dist.CLIENT)
    @Override
    public float getItemProperty(ItemStack stack, ClientLevel level, LivingEntity entity, int p174679, String property) {
        final CompoundTag nbt = ModUtils.nbt(stack);
        return nbt.getString("mode").equals(property) ? 1 : 0;
    }


    @Override
    public List<EnumInfoUpgradeModules> getUpgradeModules() {
        return EnumUpgrades.LAPPACK.list;
    }
}
