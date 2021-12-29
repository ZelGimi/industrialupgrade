package com.denfop.items.energy;


import com.denfop.Constants;
import com.denfop.IUCore;
import com.denfop.api.IModelRegister;
import com.denfop.utils.EnumInfoUpgradeModules;
import com.denfop.utils.KeyboardClient;
import com.denfop.utils.ModUtils;
import ic2.api.item.ElectricItem;
import ic2.api.item.IElectricItem;
import ic2.core.IC2;
import ic2.core.init.BlocksItems;
import ic2.core.init.Localization;
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
import net.minecraft.init.Items;
import net.minecraft.item.IItemPropertyGetter;
import net.minecraft.item.Item;
import net.minecraft.item.ItemArrow;
import net.minecraft.item.ItemBow;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.ActionResult;
import net.minecraft.util.EnumActionResult;
import net.minecraft.util.EnumHand;
import net.minecraft.util.NonNullList;
import net.minecraft.util.ResourceLocation;
import net.minecraft.world.World;
import net.minecraftforge.client.model.ModelLoader;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.entity.player.ArrowLooseEvent;
import net.minecraftforge.event.entity.player.ArrowNockEvent;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import org.jetbrains.annotations.Nullable;
import org.lwjgl.input.Keyboard;

import java.util.List;

public class EnergyBow extends ItemBow implements IElectricItem, IModelRegister {


    static final int[] CHARGE = new int[]{1500, 750, 2000, 5000, 1000};

    static final String[] MODE = new String[]{"normal", "rapidfire", "spread", "sniper", "flame"};
    private static float type;
    private final String name;
    private final double nanoBowBoost;
    private final int tier;
    private final int transferenergy;
    private final int maxenergy;

    public EnergyBow(String name, double nanoBowBoost, int tier, int transferenergy, int maxenergy, float type) {
        setMaxDamage(27);
        setFull3D();
        setCreativeTab(IUCore.EnergyTab);
        this.name = name;
        this.nanoBowBoost = nanoBowBoost;
        this.tier = tier;
        this.transferenergy = transferenergy;
        this.maxenergy = maxenergy;
        EnergyBow.type = type;
        setUnlocalizedName(name);
        BlocksItems.registerItem((Item) this, IUCore.getIdentifier(name)).setUnlocalizedName(name);
        IUCore.proxy.addIModelRegister(this);
        this.addPropertyOverride(new ResourceLocation("pulling"), new IItemPropertyGetter() {
            @SideOnly(Side.CLIENT)
            public float apply(
                    ItemStack stack,
                    @javax.annotation.Nullable World worldIn,
                    @javax.annotation.Nullable EntityLivingBase entityIn
            ) {
                return entityIn != null && entityIn.isHandActive() && entityIn.getActiveItemStack() == stack ? 1.0F : 0.0F;
            }
        });
    }

    public static float getArrowVelocity(int charge) {
        float f = charge / 20.0F;
        f = (f * f + f * 2.0F) / 3.0F;
        return Math.min(f, 1.5F);
    }

    @SideOnly(Side.CLIENT)
    @Override
    public void addInformation(
            final ItemStack stack,
            @Nullable final World p_77624_2_,
            final List<String> tooltip,
            final ITooltipFlag p_77624_4_
    ) {
        NBTTagCompound nbt = ModUtils.nbt(stack);
        IElectricItem item = (IElectricItem) stack.getItem();
        if (!nbt.getBoolean("loaded")) {
            if (nbt.getInteger("tier") == 0) {
                nbt.setInteger("tier", item.getTier(stack));
            }
            if (nbt.getDouble("transferLimit") == 0.0D) {
                nbt.setDouble("transferLimit", item.getTransferLimit(stack));
            }
            if (nbt.getDouble("maxCharge") == 0.0D) {
                nbt.setDouble("maxCharge", item.getMaxCharge(stack));
            }
            nbt.setBoolean("loaded", true);
        }
        if (nbt.getDouble("transferLimit") != item.getTransferLimit(stack)) {
            tooltip.add(String.format(Localization.translate("info.transferspeed"), nbt.getDouble("transferLimit")));
        }
        if (nbt.getInteger("tier") != item.getTier(stack)) {
            tooltip.add(String.format(Localization.translate("info.chargingtier"), nbt.getInteger("tier")));
        }

        if (!Keyboard.isKeyDown(Keyboard.KEY_LSHIFT)) {
            tooltip.add(Localization.translate("press.lshift"));
        }


        if (Keyboard.isKeyDown(Keyboard.KEY_LSHIFT)) {
            tooltip.add(Localization.translate("iu.changemode_key") +  Keyboard.getKeyName(KeyboardClient.changemode.getKeyCode()) + Localization.translate(
                    "iu.changemode_rcm"));
        }

        super.addInformation(stack, p_77624_2_, tooltip, p_77624_4_);
    }

    @Override
    public void getSubItems(final CreativeTabs tabs, final NonNullList<ItemStack> items) {
        if (this.isInCreativeTab(tabs)) {
            ItemStack charged = new ItemStack(this, 1);
            ElectricItem.manager.charge(charged, 2.147483647E9D, 2147483647, true, false);
            items.add(charged);
            items.add(new ItemStack(this, 1, getMaxDamage()));
        }
    }


    public boolean isRepairable() {
        return false;
    }

    public int getItemEnchantability() {
        return 0;
    }


    public void onPlayerStoppedUsing(ItemStack stack, World world, EntityLivingBase EntityLivingBase, int timeLeft) {
        if (!(EntityLivingBase instanceof EntityPlayer)) {
            super.onPlayerStoppedUsing(stack, world, EntityLivingBase, timeLeft);
        }
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
            ItemArrow itemarrow = (ItemArrow) (stack.getItem() instanceof ItemArrow ? stack.getItem() : Items.ARROW);
            EntityArrow arrow = itemarrow.createArrow(world, stack, player);
            arrow = this.customizeArrow(arrow);
            arrow.shoot(player, player.rotationPitch, player.rotationYaw, 0.0F, f * 3.0F, 1.0F);

            if (f == 1.5F) {
                arrow.setIsCritical(true);
            }
            int bowdamage = 0;
            for (int i = 0; i < 4; i++) {
                if (nbt.getString("mode_module" + i).equals("bowdamage")) {
                    bowdamage++;
                }

            }
            bowdamage = Math.min(bowdamage, EnumInfoUpgradeModules.BOWDAMAGE.max);

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

            int bowenergy = 0;
            for (int i = 0; i < 4; i++) {
                if (nbt.getString("mode_module" + i).equals("bowenergy")) {
                    bowenergy++;
                }

            }
            bowenergy = Math.min(bowenergy, EnumInfoUpgradeModules.BOWENERGY.max);

            if (mode == 2) {
                if (ElectricItem.manager.canUse(stack, CHARGE[mode] - CHARGE[mode] * 0.1 * bowenergy)) {
                    ElectricItem.manager.use(stack, CHARGE[mode] - CHARGE[mode] * 0.1 * bowenergy, player);
                    world.spawnEntity(arrow);
                    if (arrow.getIsCritical()) {
                        ItemArrow itemarrow2 = (ItemArrow) (stack.getItem() instanceof ItemArrow ? stack.getItem() : Items.ARROW);
                        EntityArrow arrow2 = itemarrow2.createArrow(world, stack, player);
                        arrow2 = this.customizeArrow(arrow2);
                        arrow2.shoot(player, player.rotationPitch, player.rotationYaw, 0.0F, f * 3.0F, 1.0F);
                        arrow2.setDamage(f * 2.0F);
                        arrow2.setPosition(arrow2.posX + 0.25D, arrow2.posY, arrow2.posZ);
                        arrow2.setIsCritical(true);
                        arrow2.pickupStatus = EntityArrow.PickupStatus.CREATIVE_ONLY;

                        ItemArrow itemarrow3 = (ItemArrow) (stack.getItem() instanceof ItemArrow ? stack.getItem() : Items.ARROW);
                        EntityArrow arrow3 = itemarrow3.createArrow(world, stack, player);
                        arrow3 = this.customizeArrow(arrow3);
                        arrow3.shoot(player, player.rotationPitch, player.rotationYaw, 0.0F, f * 3.0F, 1.0F);

                        arrow3.setDamage(f * 2.0F);
                        arrow3.setPosition(arrow3.posX, arrow3.posY + 0.25D, arrow3.posZ);
                        arrow3.setIsCritical(true);
                        arrow3.pickupStatus = EntityArrow.PickupStatus.CREATIVE_ONLY;

                        ItemArrow itemarrow4 = (ItemArrow) (stack.getItem() instanceof ItemArrow ? stack.getItem() : Items.ARROW);
                        EntityArrow arrow4 = itemarrow4.createArrow(world, stack, player);
                        arrow4 = this.customizeArrow(arrow4);
                        arrow4.shoot(player, player.rotationPitch, player.rotationYaw, 0.0F, f * 3.0F, 1.0F);

                        arrow4.setDamage(f * 2.0F);
                        arrow4.setPosition(arrow4.posX - 0.25D, arrow4.posY, arrow4.posZ);
                        arrow4.setIsCritical(true);
                        arrow4.pickupStatus = EntityArrow.PickupStatus.CREATIVE_ONLY;

                        ItemArrow itemarrow5 = (ItemArrow) (stack.getItem() instanceof ItemArrow ? stack.getItem() : Items.ARROW);
                        EntityArrow arrow5 = itemarrow5.createArrow(world, stack, player);
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

                    world.spawnEntity(arrow);
                }
            }
        }
        if (IC2.platform.isRendering()) {
            IUCore.audioManager.playOnce(
                    player,
                    com.denfop.audio.PositionSpec.Hand,
                    "Tools/bow.ogg",
                    true,
                    IC2.audioManager.getDefaultVolume()
            );
        }
    }

    public int getMaxItemUseDuration(ItemStack stack) {
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

    @Override
    public ActionResult<ItemStack> onItemRightClick(
            final World world,
            final EntityPlayer player,
            final EnumHand hand
    ) {
        ItemStack stack = player.getHeldItem(hand);
        NBTTagCompound nbt = ModUtils.nbt(stack);

        int mode = nbt.getInteger("bowMode");
        int bowenergy = 0;
        for (int i = 0; i < 4; i++) {
            if (nbt.getString("mode_module" + i).equals("bowenergy")) {
                bowenergy++;
            }

        }
        bowenergy = Math.min(bowenergy, EnumInfoUpgradeModules.BOWENERGY.max);

        if (IUCore.keyboard.isChangeKeyDown(player) && nbt.getByte("toggleTimer") == 0) {
            if (!world.isRemote) {
                byte toggle = 10;
                nbt.setByte("toggleTimer", toggle);
                mode++;

                if (mode > 4) {
                    mode = 0;
                }
                nbt.setInteger("bowMode", mode);

            }
            if (IC2.platform.isSimulating()) {
                IC2.platform.messagePlayer(player, Localization.translate("info.nanobow." + MODE[mode]));
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

        return new ActionResult<ItemStack>(EnumActionResult.SUCCESS, stack);

    }


    public void onUpdate(ItemStack stack, World world, Entity entity, int itemSlot, boolean isSelected) {
        NBTTagCompound nbt = ModUtils.nbt(stack);
        byte toggle = nbt.getByte("toggleTimer");
        if (toggle > 0) {
            toggle = (byte) (toggle - 1);
            nbt.setByte("toggleTimer", toggle);
        }

    }


    public void onUsingTick(ItemStack stack, EntityLivingBase EntityLivingBase, int i) {
        if (!(EntityLivingBase instanceof EntityPlayer)) {
            return;
        }
        EntityPlayer player = (EntityPlayer) EntityLivingBase;
        NBTTagCompound nbt = ModUtils.nbt(stack);
        int mode = nbt.getInteger("bowMode");

        if (mode == 1) {
            int bowenergy = 0;
            for (int k = 0; k < 4; k++) {
                if (nbt.getString("mode_module" + k).equals("bowenergy")) {
                    bowenergy++;
                }

            }
            bowenergy = Math.min(bowenergy, EnumInfoUpgradeModules.BOWENERGY.max);

            int j = getMaxItemUseDuration(stack) - i;
            if (j >= 10 && ElectricItem.manager.canUse(stack, CHARGE[1] - CHARGE[1] * 0.1 * bowenergy)) {
                player.stopActiveHand();
            }
        }
    }


    public boolean canProvideEnergy(ItemStack is) {
        return false;
    }

    public double getMaxCharge(ItemStack stack) {
        NBTTagCompound nbt = ModUtils.nbt(stack);
        if (nbt.getDouble("maxCharge") == 0.0D) {
            nbt.setDouble("maxCharge", getDefaultMaxCharge());
        }
        return nbt.getDouble("maxCharge");
    }

    public int getTier(ItemStack stack) {
        NBTTagCompound nbt = ModUtils.nbt(stack);
        if (nbt.getInteger("tier") == 0) {
            nbt.setInteger("tier", getDefaultTier());
        }
        return nbt.getInteger("tier");
    }

    public double getTransferLimit(ItemStack stack) {
        NBTTagCompound nbt = ModUtils.nbt(stack);
        if (nbt.getDouble("transferLimit") == 0.0D) {
            nbt.setDouble("transferLimit", getDefaultTransferLimit());
        }
        return nbt.getDouble("transferLimit");
    }


    public int getDefaultMaxCharge() {
        return maxenergy;
    }

    public int getDefaultTier() {
        return this.tier;
    }

    public int getDefaultTransferLimit() {
        return this.transferenergy;
    }


    @Override
    public void registerModels() {
        registerModels(this.name);
    }

    @SideOnly(Side.CLIENT)
    public static ModelResourceLocation getModelLocation1(String name) {
        final String loc = Constants.MOD_ID +
                ':' +
                "energy_tools" + "/" + name;

        return new ModelResourceLocation(loc, null);
    }

    @SideOnly(Side.CLIENT)
    public void registerModels(final String name) {
        ModelLoader.setCustomMeshDefinition(this, stack -> getModelLocation1(name));
        ModelBakery.registerItemVariants(this, getModelLocation1(name));


    }

}
