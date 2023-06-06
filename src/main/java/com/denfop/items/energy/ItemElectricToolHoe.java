package com.denfop.items.energy;

import ic2.api.item.ElectricItem;
import ic2.core.IC2;
import ic2.core.util.StackUtil;
import net.minecraft.block.Block;
import net.minecraft.block.SoundType;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumActionResult;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.entity.player.UseHoeEvent;

import java.util.EnumSet;

public class ItemElectricToolHoe extends ItemElectricTool {

    public ItemElectricToolHoe() {
        super("electric_hoe", 50, HarvestLevel.Iron, EnumSet.of(ToolClass.Hoe));
        this.maxCharge = 10000;
        this.transferLimit = 100;
        this.tier = 1;
        this.efficiency = 16.0F;
    }

    public boolean onBlockStartBreak(ItemStack stack, BlockPos pos, EntityPlayer player) {
        ElectricItem.manager.use(stack, this.operationEnergyCost, player);
        return false;
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
        ItemStack stack = StackUtil.get(player, hand);
        if (!player.canPlayerEdit(pos, side, stack)) {
            return EnumActionResult.PASS;
        } else if (!ElectricItem.manager.canUse(stack, this.operationEnergyCost)) {
            return EnumActionResult.PASS;
        } else if (MinecraftForge.EVENT_BUS.post(new UseHoeEvent(player, stack, world, pos))) {
            return EnumActionResult.PASS;
        } else {
            IBlockState state = world.getBlockState(pos);
            Block block = state.getBlock();
            if (side == EnumFacing.DOWN || !world.isAirBlock(pos.up()) || block != Blocks.MYCELIUM && block != Blocks.GRASS && block != Blocks.DIRT) {
                return super.onItemUse(player, world, pos, hand, side, hitX, hitY, hitZ);
            } else {
                block = Blocks.FARMLAND;
                SoundType stepSound = block.getSoundType(state, world, pos, player);
                world.playSound(
                        null,
                        (double) pos.getX() + 0.5,
                        (double) pos.getY() + 0.5,
                        (double) pos.getZ() + 0.5,
                        stepSound.getStepSound(),
                        SoundCategory.BLOCKS,
                        (stepSound.getVolume() + 1.0F) / 2.0F,
                        stepSound.getPitch() * 0.8F
                );
                if (IC2.platform.isSimulating()) {
                    world.setBlockState(pos, block.getDefaultState());
                    ElectricItem.manager.use(stack, this.operationEnergyCost, player);
                }

                return EnumActionResult.SUCCESS;
            }
        }
    }

    @Override
    public void registerModels() {
        this.registerModels(this.name);
    }

}
