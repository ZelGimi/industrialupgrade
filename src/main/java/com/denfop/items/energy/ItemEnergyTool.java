package com.denfop.items.energy;

import com.denfop.ElectricItem;
import com.denfop.api.item.IEnergyItem;
import com.denfop.utils.ElectricItemManager;
import com.denfop.utils.ModUtils;
import net.minecraft.Util;
import net.minecraft.core.BlockPos;
import net.minecraft.core.NonNullList;
import net.minecraft.core.Registry;
import net.minecraft.tags.TagKey;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;

public abstract class ItemEnergyTool extends ItemToolIU implements IEnergyItem {
    public final int operationEnergyCost;
    public int maxCharge;
    public int transferLimit;
    public int tier;
    private String nameItem;

    public ItemEnergyTool(
            int operationEnergyCost
    ) {
        this(2.0F, -3.0F, operationEnergyCost, null);
    }


    public ItemEnergyTool(
            float damage,
            float speed,
            int operationEnergyCost,
            TagKey<Block> mineableBlocks
    ) {
        super(damage, speed, mineableBlocks);
        this.operationEnergyCost = operationEnergyCost;
    }

    protected String getOrCreateDescriptionId() {
        if (this.nameItem == null) {
            StringBuilder pathBuilder = new StringBuilder(Util.makeDescriptionId("iu", Registry.ITEM.getKey(this)));
            String targetString = "industrialupgrade.";
            String replacement = "";
            if (replacement != null) {
                int index = pathBuilder.indexOf(targetString);
                while (index != -1) {
                    pathBuilder.replace(index, index + targetString.length(), replacement);
                    index = pathBuilder.indexOf(targetString, index + replacement.length());
                }
            }
            this.nameItem = "iu."+pathBuilder.toString().split("\\.")[2];
        }

        return this.nameItem;
    }

    public boolean isBarVisible(final ItemStack stack) {
        return true;
    }

    public int getBarColor(ItemStack stack) {
        return ModUtils.convertRGBcolorToInt(33, 91, 199);
    }

    public int getBarWidth(ItemStack stack) {

        return 13 - (int) (13.0F * Math.min(
                Math.max(
                        1 - ElectricItem.manager.getCharge(stack) / ElectricItem.manager.getMaxCharge(stack),
                        0.0
                ),
                1.0
        ));
    }

    @Override
    public float getDestroySpeed(ItemStack p_41004_, BlockState p_41005_) {
        return !ElectricItem.manager.canUse(p_41004_, this.operationEnergyCost) ? 0.1F : super.getDestroySpeed(p_41004_, p_41005_);
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

    @Override
    public boolean mineBlock(ItemStack stack, Level world, BlockState state, BlockPos pos, LivingEntity user) {
        if (state.getDestroySpeed(world, pos) != 0.0F) {
            ElectricItem.manager.use(stack, this.operationEnergyCost, user);
        }

        return true;
    }

    @Override
    public void fillItemCategory(CreativeModeTab p_41391_, NonNullList<ItemStack> p_41392_) {
        if (this.allowedIn(p_41391_)) {
            ElectricItemManager.addChargeVariants(this, p_41392_);
        }
    }
}
