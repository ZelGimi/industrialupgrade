package com.denfop.items.energy;

import com.denfop.Constants;
import com.denfop.ElectricItem;
import com.denfop.api.item.IEnergyItem;
import com.denfop.utils.ElectricItemManager;
import com.denfop.utils.ModUtils;
import net.minecraft.block.Block;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.renderer.block.model.ModelResourceLocation;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ActionResult;
import net.minecraft.util.EnumActionResult;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.NonNullList;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import java.util.HashSet;
import java.util.Set;

public abstract class ItemEnergyTool extends ItemToolIU implements IEnergyItem {

    public double operationEnergyCost;
    public int maxCharge;
    public int transferLimit;
    public int tier;


    protected ItemEnergyTool(
            String name,
            int operationEnergyCost
    ) {
        this(name, 2.0F, 0.5F, operationEnergyCost, new HashSet());
    }

    private ItemEnergyTool(
            String name,
            float damage,
            float speed,
            int operationEnergyCost,
            Set<Block> mineableBlocks
    ) {
        super(name, damage, speed, mineableBlocks);
        this.operationEnergyCost = operationEnergyCost;
        this.setMaxDamage(0);
        this.setNoRepair();
    }

    @SideOnly(Side.CLIENT)
    public static ModelResourceLocation getModelLocation(String name) {

        final String loc = Constants.MOD_ID +
                ':' +
                "energy_tools" + "/" + name;
        return new ModelResourceLocation(loc, null);
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
        return super.onItemUse(player, world, pos, hand, side, xOffset, yOffset, zOffset);
    }

    public ActionResult<ItemStack> onItemRightClick(World world, EntityPlayer player, EnumHand hand) {
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

    public double getMaxEnergy(ItemStack stack) {
        return this.maxCharge;
    }

    public short getTierItem(ItemStack stack) {
        return (short) this.tier;
    }

    public double getTransferEnergy(ItemStack stack) {
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


    public ItemStack getItemStack(double charge) {
        ItemStack ret = new ItemStack(this);
        ElectricItem.manager.charge(ret, charge, Integer.MAX_VALUE, true, false);
        return ret;
    }

    public ItemStack getItemStack() {
        ItemStack ret = new ItemStack(this);
        ElectricItem.manager.charge(ret, this.maxCharge, Integer.MAX_VALUE, true, false);
        return ret;
    }


    public boolean onDroppedByPlayer(ItemStack item, EntityPlayer player) {
        return true;
    }


    public void setDamage(ItemStack stack, int damage) {
        this.getDamage(stack);


    }

}
