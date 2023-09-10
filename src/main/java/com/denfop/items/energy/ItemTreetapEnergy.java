package com.denfop.items.energy;

import com.denfop.ElectricItem;
import com.denfop.IUItem;
import com.denfop.utils.ModUtils;
import net.minecraft.block.Block;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumActionResult;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

public class ItemTreetapEnergy extends ItemEnergyTool {

    public ItemTreetapEnergy() {
        super("electric_treetap", 50);
        this.maxCharge = 10000;
        this.transferLimit = 100;
        this.tier = 1;
    }

    public EnumActionResult onItemUse(
            EntityPlayer player,
            World world,
            BlockPos pos,
            EnumHand hand,
            EnumFacing side,
            float hitX,
            float hitY,
            float hitZ
    ) {
        IBlockState state = world.getBlockState(pos);
        Block block = state.getBlock();
        ItemStack stack = ModUtils.get(player, hand);
        if (block == IUItem.rubWood && ElectricItem.manager.canUse(stack, this.operationEnergyCost)) {
            if (ItemTreetap.attemptExtract(player, world, pos, side, state, null)) {
                ElectricItem.manager.use(stack, this.operationEnergyCost, player);
                return EnumActionResult.SUCCESS;
            } else {
                return super.onItemUse(player, world, pos, hand, side, hitX, hitY, hitZ);
            }
        } else {
            return EnumActionResult.PASS;
        }
    }

    @Override
    public void registerModels() {
        this.registerModels(this.name);
    }

}
