package com.denfop.items.energy;

import com.denfop.Constants;
import com.denfop.IUCore;
import com.denfop.api.IModelRegister;
import com.denfop.api.upgrade.IUpgradeItem;
import ic2.api.item.ElectricItem;
import ic2.api.item.IElectricItem;
import ic2.core.init.BlocksItems;
import net.minecraft.block.BlockDispenser;
import net.minecraft.client.renderer.block.model.ModelBakery;
import net.minecraft.client.renderer.block.model.ModelResourceLocation;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.*;
import net.minecraft.util.ActionResult;
import net.minecraft.util.EnumActionResult;
import net.minecraft.util.EnumHand;
import net.minecraft.util.ResourceLocation;
import net.minecraft.world.World;
import net.minecraftforge.client.model.ModelLoader;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import org.jetbrains.annotations.NotNull;

import javax.annotation.Nullable;

public class ItemEnergyShield extends ItemShield implements IElectricItem, IUpgradeItem, IModelRegister {

    private final double maxCharge;
    private final double transferLimit;
    private final int tier;
    private final String name;

    public ItemEnergyShield(String name, double maxCharge, double transferLimit, int tier) {
        this.maxCharge = maxCharge;
        this.transferLimit = transferLimit;
        this.tier = tier;
        this.setMaxDamage(27);
        this.setMaxStackSize(1);
        this.setNoRepair();
        this.setNoRepair();
        this.setCreativeTab(IUCore.EnergyTab);
        this.name = name;
        setUnlocalizedName(name);
        BlocksItems.registerItem((Item) this, IUCore.getIdentifier(name)).setUnlocalizedName(name);
        setMaxStackSize(1);
        IUCore.proxy.addIModelRegister(this);
        this.addPropertyOverride(new ResourceLocation("blocking"), new IItemPropertyGetter() {
            @SideOnly(Side.CLIENT)
            public float apply(ItemStack stack, @Nullable World worldIn, @Nullable EntityLivingBase entityIn) {
                return entityIn != null && entityIn.isHandActive() && entityIn.getActiveItemStack() == stack ? 1.0F : 0.0F;
            }
        });
        BlockDispenser.DISPENSE_BEHAVIOR_REGISTRY.putObject(this, ItemArmor.DISPENSER_BEHAVIOR);

    }

    @SideOnly(Side.CLIENT)
    public static ModelResourceLocation getModelLocation1(String name) {
        final String loc = Constants.MOD_ID +
                ':' +
                "energy_tools" + "/" + name;

        return new ModelResourceLocation(loc, null);
    }

    @Override
    public void registerModels() {
        registerModels(this.name);
    }

    @SideOnly(Side.CLIENT)
    public void registerModels(final String name) {
        ModelLoader.setCustomMeshDefinition(this, stack -> getModelLocation1(name));
        ModelBakery.registerItemVariants(this, getModelLocation1(name));


    }

    public boolean getIsRepairable(ItemStack toRepair, ItemStack repair) {
        return false;
    }

    @Override
    public boolean canProvideEnergy(final ItemStack itemStack) {
        return false;
    }

    public @NotNull ActionResult<ItemStack> onItemRightClick(World worldIn, EntityPlayer playerIn, EnumHand handIn) {
        ItemStack itemstack = playerIn.getHeldItem(handIn);
        if (ElectricItem.manager.canUse(itemstack, 100.0)) {
            playerIn.setActiveHand(handIn);
            return new ActionResult<>(EnumActionResult.SUCCESS, itemstack);
        } else {
            return new ActionResult<>(EnumActionResult.FAIL, itemstack);
        }
    }

    public void onPlayerStoppedUsing(ItemStack stack, World worldIn, EntityLivingBase entityLiving, int timeLeft) {
        ElectricItem.manager.use(stack, 100.0, entityLiving);
        if (entityLiving instanceof EntityPlayer) {
            EntityPlayer player = (EntityPlayer) entityLiving;
            player.inventoryContainer.detectAndSendChanges();
        }

    }

    @Override
    public double getMaxCharge(final ItemStack itemStack) {
        return this.maxCharge;
    }

    @Override
    public int getTier(final ItemStack itemStack) {
        return this.tier;
    }

    @Override
    public double getTransferLimit(final ItemStack itemStack) {
        return this.transferLimit;
    }

    @Override
    public boolean isShield(final ItemStack stack, @org.jetbrains.annotations.Nullable final EntityLivingBase entity) {
        return true;
    }

}
