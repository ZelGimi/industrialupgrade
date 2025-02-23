package com.denfop.items.armour;

import com.denfop.Constants;
import com.denfop.ElectricItem;
import com.denfop.IUCore;
import com.denfop.api.IModelRegister;
import com.denfop.api.item.IEnergyItem;
import com.denfop.register.Register;
import com.denfop.utils.ElectricItemManager;
import com.denfop.utils.KeyboardClient;
import com.denfop.utils.ModUtils;
import net.minecraft.client.renderer.block.model.ModelBakery;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.MobEffects;
import net.minecraft.inventory.EntityEquipmentSlot;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.potion.PotionEffect;
import net.minecraft.util.NonNullList;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.client.model.ModelLoader;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.lwjgl.input.Keyboard;

import java.util.List;

public class ItemArmorNightvisionGoggles extends ItemArmorUtility implements IModelRegister, IEnergyItem {

    public ItemArmorNightvisionGoggles() {
        super("nightvision", EntityEquipmentSlot.HEAD);
        this.armorName = "nightvision_goggles";
        setUnlocalizedName(armorName);
        setCreativeTab(IUCore.EnergyTab);
        Register.registerItem((Item) this, IUCore.getIdentifier(armorName)).setUnlocalizedName(armorName);
        IUCore.proxy.addIModelRegister(this);
        this.setNoRepair();
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

    @SideOnly(Side.CLIENT)
    @Override
    public void addInformation(
            final ItemStack p_77624_1_,
            @Nullable final World p_77624_2_,
            final List<String> p_77624_3_,
            final ITooltipFlag p_77624_4_
    ) {
        p_77624_3_.add("Nightvision Key: " + Keyboard.getKeyName(Math.abs(KeyboardClient.armormode.getKeyCode())));
        super.addInformation(p_77624_1_, p_77624_2_, p_77624_3_, p_77624_4_);
    }

    public boolean canProvideEnergy(ItemStack stack) {
        return false;
    }

    public double getMaxEnergy(ItemStack stack) {
        return 200000.0;
    }

    public short getTierItem(ItemStack stack) {
        return 1;
    }

    public double getTransferEnergy(ItemStack stack) {
        return 200.0;
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
                ModelBakery.registerItemVariants(this, getModelLocation1("nightvision", "_" + this.armorType.ordinal() + s));
            }

        }

    }

    public String getArmorTexture(ItemStack stack, Entity entity, EntityEquipmentSlot slot, String type) {
        int suffix = (this.armorType == EntityEquipmentSlot.LEGS) ? 2 : 1;
        NBTTagCompound nbtData = ModUtils.nbt(stack);
        if (!nbtData.getString("mode").isEmpty()) {
            return Constants.TEXTURES + ":textures/armor/" + "armor" + this.armorType.ordinal() + "_" + nbtData.getString("mode") + ".png";
        }


        return Constants.TEXTURES + ":textures/armor/" + this.armorName + "_" + suffix + ".png";
    }

    public void onArmorTick(World world, EntityPlayer player, ItemStack stack) {
        NBTTagCompound nbtData = ModUtils.nbt(stack);
        boolean active = nbtData.getBoolean("active");
        byte toggleTimer = nbtData.getByte("toggleTimer");
        if (IUCore.keyboard.isArmorKey(player) && toggleTimer == 0) {
            toggleTimer = 10;
            active = !active;
            if (IUCore.proxy.isSimulating()) {
                nbtData.setBoolean("active", active);
                if (active) {
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

        boolean ret = false;
        if (active && IUCore.proxy.isSimulating() && ElectricItem.manager.use(stack, 1.0, player)) {
            int skylight = player.getEntityWorld().getLightFromNeighbors(new BlockPos(player));
            if (skylight > 8) {
                IUCore.proxy.removePotion(player, MobEffects.NIGHT_VISION);
                player.addPotionEffect(new PotionEffect(MobEffects.BLINDNESS, 100, 0, true, true));
            } else {
                IUCore.proxy.removePotion(player, MobEffects.BLINDNESS);
                player.addPotionEffect(new PotionEffect(MobEffects.NIGHT_VISION, 300, 0, true, true));
            }

            ret = true;
        }

        if (ret) {
            player.inventoryContainer.detectAndSendChanges();
        }

    }

    public void getSubItems(CreativeTabs tab, @NotNull NonNullList<ItemStack> subItems) {
        if (this.isInCreativeTab(tab)) {
            ElectricItemManager.addChargeVariants(this, subItems);
        }
    }

    public boolean getIsRepairable(ItemStack par1ItemStack, ItemStack par2ItemStack) {
        return false;
    }

}
