package com.denfop.items.energy;

import com.denfop.utils.ElectricItem;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.context.UseOnContext;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.SoundType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraftforge.common.ToolAction;
import net.minecraftforge.common.ToolActions;


public class ItemEnergyToolHoe extends ItemEnergyTool {
    public ItemEnergyToolHoe() {
        super(50);
        this.maxCharge = 10000;
        this.transferLimit = 100;
        this.tier = 1;
    }

    public boolean canPerformAction(ItemStack stack, ToolAction toolAction) {
        return ToolActions.DEFAULT_HOE_ACTIONS.contains(toolAction);
    }

    @Override
    public InteractionResult useOn(UseOnContext context) {
        Player player = context.getPlayer();
        Level world = context.getLevel();
        BlockPos pos = context.getClickedPos();
        InteractionHand hand = context.getHand();
        ItemStack stack = context.getItemInHand();

        if (player == null || !player.mayUseItemAt(pos, context.getClickedFace(), stack)) {
            return InteractionResult.PASS;
        }

        if (!ElectricItem.manager.canUse(stack, this.operationEnergyCost)) {
            return InteractionResult.PASS;
        }


        BlockState state = world.getBlockState(pos);
        Block block = state.getBlock();

        if (context.getClickedFace() == Direction.DOWN || (
                !world.getBlockState(pos.below()).isAir() &&
                        (block != Blocks.MYCELIUM && block != Blocks.GRASS_BLOCK && block != Blocks.DIRT))) {
            return super.useOn(context);
        }

        Block farmland = Blocks.FARMLAND;
        SoundType stepSound = farmland.getSoundType(state, world, pos, player);

        world.playSound(
                null,
                pos.getX() + 0.5,
                pos.getY() + 0.5,
                pos.getZ() + 0.5,
                stepSound.getStepSound(),
                SoundSource.BLOCKS,
                (stepSound.getVolume() + 1.0F) / 2.0F,
                stepSound.getPitch() * 0.8F
        );

        if (!world.isClientSide) {
            world.setBlockAndUpdate(pos, farmland.defaultBlockState());
            ElectricItem.manager.use(stack, this.operationEnergyCost, player);
        }

        return InteractionResult.SUCCESS;
    }
}
