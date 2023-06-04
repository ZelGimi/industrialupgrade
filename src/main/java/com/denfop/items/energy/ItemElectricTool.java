package com.denfop.items.energy;

import com.denfop.Constants;
import com.denfop.IUCore;
import com.denfop.audio.AudioSource;
import com.denfop.audio.PositionSpec;
import com.denfop.items.BaseElectricItem;
import ic2.api.item.ElectricItem;
import ic2.api.item.IElectricItem;
import ic2.api.item.IItemHudInfo;
import ic2.core.IC2;
import ic2.core.init.Localization;
import ic2.core.item.ElectricItemManager;
import ic2.core.item.IPseudoDamageItem;
import ic2.core.util.LogCategory;
import ic2.core.util.StackUtil;
import net.minecraft.block.Block;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.renderer.block.model.ModelResourceLocation;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.EntityEquipmentSlot;
import net.minecraft.item.ItemStack;
import net.minecraft.util.*;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import java.util.*;

public abstract class ItemElectricTool extends ItemToolIU implements IPseudoDamageItem, IElectricItem, IItemHudInfo {

    public double operationEnergyCost;
    public int maxCharge;
    public int transferLimit;
    public int tier;
    protected AudioSource audioSource;
    protected boolean wasEquipped;

    protected ItemElectricTool(String name, int operationEnergyCost) {
        this(name, operationEnergyCost, HarvestLevel.Iron, Collections.emptySet());
    }

    protected ItemElectricTool(
            String name,
            int operationEnergyCost,
            HarvestLevel harvestLevel,
            Set<? extends IToolClass> toolClasses
    ) {
        this(name, 2.0F, -3.0F, operationEnergyCost, harvestLevel, toolClasses, new HashSet());
    }

    private ItemElectricTool(
            String name,
            float damage,
            float speed,
            int operationEnergyCost,
            HarvestLevel harvestLevel,
            Set<? extends IToolClass> toolClasses,
            Set<Block> mineableBlocks
    ) {
        super(name, damage, speed, harvestLevel, toolClasses, mineableBlocks);
        this.operationEnergyCost = operationEnergyCost;
        this.setMaxDamage(27);
        this.setNoRepair();
    }

    @SideOnly(Side.CLIENT)
    public static ModelResourceLocation getModelLocation(String name) {

        final String loc = Constants.MOD_ID +
                ':' +
                "energy_tools" + "/" + name;
        return new ModelResourceLocation(loc, null);
    }

    public EnumActionResult onItemUse(
            EntityPlayer player,
            World world,
            BlockPos pos,
            EnumHand hand,
            EnumFacing side,
            float xOffset,
            float yOffset,
            float zOffset
    ) {
        ElectricItem.manager.use(StackUtil.get(player, hand), 0.0, player);
        return super.onItemUse(player, world, pos, hand, side, xOffset, yOffset, zOffset);
    }

    public ActionResult<ItemStack> onItemRightClick(World world, EntityPlayer player, EnumHand hand) {
        ElectricItem.manager.use(StackUtil.get(player, hand), 0.0, player);
        return super.onItemRightClick(world, player, hand);
    }

    public float getDestroySpeed(ItemStack stack, IBlockState state) {
        return !ElectricItem.manager.canUse(stack, this.operationEnergyCost) ? 1.0F : super.getDestroySpeed(stack, state);
    }

    public boolean hitEntity(ItemStack itemstack, EntityLivingBase entityliving, EntityLivingBase entityliving1) {
        return true;
    }

    public int getItemEnchantability() {
        return 0;
    }

    public boolean isRepairable() {
        return false;
    }

    public boolean canProvideEnergy(ItemStack stack) {
        return false;
    }

    public double getMaxCharge(ItemStack stack) {
        return this.maxCharge;
    }

    public int getTier(ItemStack stack) {
        return this.tier;
    }

    public double getTransferLimit(ItemStack stack) {
        return this.transferLimit;
    }

    public boolean onBlockDestroyed(ItemStack stack, World world, IBlockState state, BlockPos pos, EntityLivingBase user) {
        if (state.getBlockHardness(world, pos) != 0.0F) {
            ElectricItem.manager.use(stack, this.operationEnergyCost, user);
        }

        return true;
    }

    public boolean getIsRepairable(ItemStack par1ItemStack, ItemStack par2ItemStack) {
        return false;
    }

    public boolean isBookEnchantable(ItemStack itemstack1, ItemStack itemstack2) {
        return false;
    }

    public void getSubItems(CreativeTabs tab, NonNullList<ItemStack> subItems) {
        if (this.isInCreativeTab(tab)) {
            ElectricItemManager.addChargeVariants(this, subItems);
        }
    }

    public List<String> getHudInfo(ItemStack stack, boolean advanced) {
        List<String> info = new LinkedList<>();
        info.add(ElectricItem.manager.getToolTip(stack));
        info.add(Localization.translate("ic2.item.tooltip.PowerTier", this.tier));
        return info;
    }

    protected ItemStack getItemStack(double charge) {
        ItemStack ret = new ItemStack(this);
        ElectricItem.manager.charge(ret, charge, Integer.MAX_VALUE, true, false);
        return ret;
    }

    public void onUpdate(ItemStack itemstack, World world, Entity entity, int i, boolean flag) {
        boolean isEquipped = flag && entity instanceof EntityLivingBase;
        if (IC2.platform.isRendering()) {
            if (isEquipped && !this.wasEquipped) {
                String initSound;
                if (this.audioSource == null) {
                    initSound = this.getIdleSound((EntityLivingBase) entity, itemstack);
                    if (initSound != null) {
                        this.audioSource = IUCore.audioManager.createSource(
                                entity,
                                PositionSpec.Hand,
                                initSound,
                                true,
                                false,
                                IC2.audioManager.getDefaultVolume()
                        );
                    }
                }

                if (this.audioSource != null) {
                    this.audioSource.play();
                }

                initSound = this.getStartSound((EntityLivingBase) entity, itemstack);
                if (initSound != null) {
                    IUCore.audioManager.playOnce(entity, PositionSpec.Hand, initSound, true, IC2.audioManager.getDefaultVolume());
                }
            } else if (!isEquipped && this.audioSource != null) {
                if (entity instanceof EntityLivingBase) {
                    EntityLivingBase theEntity = (EntityLivingBase) entity;
                    ItemStack stack = theEntity.getItemStackFromSlot(EntityEquipmentSlot.MAINHAND);
                    if (stack.isEmpty() || stack.getItem() != this || stack == itemstack) {
                        this.removeAudioSource();
                        String sound = this.getStopSound(theEntity, itemstack);
                        if (sound != null) {
                            IUCore.audioManager.playOnce(
                                    entity,
                                    PositionSpec.Hand,
                                    sound,
                                    true,
                                    IC2.audioManager.getDefaultVolume()
                            );
                        }
                    }
                }
            } else if (this.audioSource != null) {
                this.audioSource.updatePosition();
            }

            this.wasEquipped = isEquipped;
        }

    }

    protected void removeAudioSource() {
        if (this.audioSource != null) {
            this.audioSource.stop();
            this.audioSource.remove();
            this.audioSource = null;
        }

    }

    public boolean onDroppedByPlayer(ItemStack item, EntityPlayer player) {
        this.removeAudioSource();
        return true;
    }

    protected String getIdleSound(EntityLivingBase player, ItemStack stack) {
        return null;
    }

    protected String getStopSound(EntityLivingBase player, ItemStack stack) {
        return null;
    }

    protected String getStartSound(EntityLivingBase player, ItemStack stack) {
        return null;
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

    public void setStackDamage(ItemStack stack, int damage) {
        super.setDamage(stack, damage);
    }

}
