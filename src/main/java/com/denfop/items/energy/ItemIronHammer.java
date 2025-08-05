package com.denfop.items.energy;

import com.denfop.Localization;
import com.denfop.items.energy.instruments.EnumTypeInstruments;
import com.denfop.utils.ExperienceUtils;
import com.denfop.utils.ModUtils;
import com.denfop.utils.RetraceDiggingUtils;
import net.minecraft.core.BlockPos;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.tags.BlockTags;
import net.minecraft.tags.TagKey;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Tiers;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.item.enchantment.EnchantmentHelper;
import net.minecraft.world.item.enchantment.Enchantments;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.LiquidBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.HitResult;
import net.minecraftforge.common.ForgeHooks;
import org.jetbrains.annotations.Nullable;

import javax.annotation.Nonnull;
import java.util.List;
import java.util.Set;

public class ItemIronHammer extends ItemToolIU {
    private final Set<BlockState> mineableBlocks;
    private final List<TagKey<Block>> item_tools;

    public ItemIronHammer() {
        super(2, 1, BlockTags.MINEABLE_WITH_PICKAXE);
        this.mineableBlocks = EnumTypeInstruments.DRILL.getMineableBlocks();
        this.item_tools = EnumTypeInstruments.DRILL.getListItems();
    }

    @Override
    public void appendHoverText(ItemStack pStack, @Nullable Level pLevel, List<Component> pTooltipComponents, TooltipFlag pIsAdvanced) {
        super.appendHoverText(pStack, pLevel, pTooltipComponents, pIsAdvanced);
        pTooltipComponents.add(Component.literal(Localization.translate( "iu.hammer.info")));
    }

    @Override
    public boolean isEnchantable(ItemStack p_41456_) {
        return true;
    }

    @Override
    public int getEnchantmentValue() {
        return Tiers.IRON.getEnchantmentValue();
    }
    public boolean onBlockStartBreak(@Nonnull ItemStack stack, @Nonnull BlockPos pos, @Nonnull Player player) {
        Level world = player.level();
        BlockState state = world.getBlockState(pos);
        Block block = state.getBlock();
        BlockHitResult mop = RetraceDiggingUtils.retrace(player);

        if (state.isAir()) {
            return super.onBlockStartBreak(stack, pos, player);
        }

        byte aoe = 0;
        if (player.isShiftKeyDown()) {
            if (mop.getType() != HitResult.Type.MISS) {
                return breakBlock(world, block, mop, aoe, player, pos, stack);
            }
        }

        return breakBlock(world, block, mop, (byte) (1 + aoe), player, pos, stack);
    }

    @Override
    public boolean isCorrectToolForDrops(ItemStack stack, BlockState state) {
        if (mineableBlocks.contains(state)) {
            return true;
        }

        for (TagKey<Block> blockTagKey : this.item_tools)
            if (state.is(blockTagKey))
                return true;
        return false;
    }

    public boolean isCorrectToolForDrops(BlockState p_150816_) {
        for (TagKey<Block> blockTagKey : this.item_tools)
            if (p_150816_.is(blockTagKey))
                return true;
        return super.isCorrectToolForDrops(p_150816_);
    }

    private int getExperience(
            BlockState state,
            Level world,
            BlockPos pos_block,
            int fortune,
            ItemStack stack,
            final Block localBlock
    ) {
        int col = localBlock.getExpDrop(state, world, world.random, pos_block, fortune, 0);
        return col;
    }

    public boolean breakBlock(
            Level level, Block block, BlockHitResult mop, byte modeItem, Player player, BlockPos pos,
            ItemStack stack
    ) {
        byte xRange = modeItem;
        byte yRange = modeItem;
        byte zRange = modeItem;
        int x = pos.getX();
        int y = pos.getY();
        int z = pos.getZ();
        byte digDepth = 0;

        switch (mop.getDirection()) {
            case DOWN:
            case UP:
                yRange = digDepth;
                break;
            case NORTH:
            case SOUTH:
                zRange = digDepth;
                break;
            case WEST:
            case EAST:
                xRange = digDepth;
                break;
        }

        boolean silkTouch = EnchantmentHelper.getItemEnchantmentLevel(Enchantments.SILK_TOUCH, stack) > 0;
        int fortune = EnchantmentHelper.getItemEnchantmentLevel(Enchantments.BLOCK_FORTUNE, stack);
        fortune = Math.min(3, fortune);

        int yOffset = yRange > 0 ? yRange - 1 : 0;
        stack.hurtAndBreak(1, player, (p) -> p.broadcastBreakEvent(EquipmentSlot.MAINHAND));

        if (!player.getAbilities().instabuild) {
            for (int xPos = x - xRange; xPos <= x + xRange; xPos++) {
                for (int yPos = y - yRange + yOffset; yPos <= y + yRange + yOffset; yPos++) {
                    for (int zPos = z - zRange; zPos <= z + zRange; zPos++) {
                        if (stack.getDamageValue() > 0) {
                            BlockPos blockPos = new BlockPos(xPos, yPos, zPos);
                            BlockState state = level.getBlockState(blockPos);
                            Block localBlock = state.getBlock();

                            if (localBlock != Blocks.AIR && isCorrectToolForDrops(stack, state)
                                    && state.getDestroySpeed(level, blockPos) >= 0.0F
                            ) {
                                if (state.getDestroySpeed(level, blockPos) > 0.0F) {
                                    mineBlock(stack, level, state, blockPos, player);
                                }
                                if (!silkTouch) {
                                    ExperienceUtils.addPlayerXP(player, getExperience(state, level, blockPos, fortune, stack, localBlock));
                                }
                            }
                        } else {
                            break;
                        }
                    }
                }
            }
        } else {
            if (stack.getDamageValue() > 0) {
                BlockState state = level.getBlockState(pos);
                Block localBlock = state.getBlock();
                if ((localBlock != Blocks.AIR && isCorrectToolForDrops(stack, state)
                        && state.getDestroySpeed(level, pos) >= 0.0F)
                        || (block == Blocks.INFESTED_STONE)) {
                    if (state.getDestroySpeed(level, pos) >= 0.0F) {
                        mineBlock(stack, level, state, pos, player);
                    }
                    if (!silkTouch) {
                        ExperienceUtils.addPlayerXP(player, getExperience(state, level, pos, fortune, stack, localBlock));
                    }
                } else {
                    if (state.getDestroySpeed(level, pos) >= 0.0F) {
                        return mineBlock(stack, level, state, pos, player);
                    }
                }
            }
        }
        return true;
    }

    @Override
    public float getDestroySpeed(ItemStack stack, BlockState state) {
        return mineableBlocks.contains(state) ? this.speed : 1.0F;
    }

    public boolean mineBlock(ItemStack stack, Level world, BlockState state, BlockPos pos, LivingEntity entity) {
        if (!(entity instanceof Player player)) {
            return false;
        }

        Block block = state.getBlock();
        if (state.isAir()) {
            return false;
        }

        if (block instanceof LiquidBlock || (state.getDestroySpeed(world, pos) == -1 && !player.isCreative())) {
            return false;
        }

        if (!world.isClientSide) {
            ServerLevel serverWorld = (ServerLevel) world;
            ServerPlayer serverPlayer = (ServerPlayer) player;

            if (ForgeHooks.onBlockBreakEvent(serverWorld, serverPlayer.gameMode.getGameModeForPlayer(), serverPlayer, pos) == -1) {
                return false;
            }

            if (world.destroyBlock(pos, true, entity)) {

                List<ItemEntity> items = world.getEntitiesOfClass(
                        ItemEntity.class,
                        new AABB(pos.offset(-1, -1, -1), pos.offset(1, 1, 1))
                );

                serverPlayer.causeFoodExhaustion(-0.025F);

                if (ModUtils.getOre(block)) {
                    for (ItemEntity item : items) {
                        if (!world.isClientSide) {
                            item.setPos(player.getX(), player.getY(), player.getZ());
                            item.setPickUpDelay(0);
                        }
                    }
                } else {
                    if (ModUtils.nbt(stack).getBoolean("black")) {
                        for (ItemEntity item : items) {
                            if (!world.isClientSide) {
                                item.discard();
                            }
                        }
                    }
                }
            }

        } else {
            if (world.destroyBlock(pos, true, player)) {
                block.playerDestroy(world, player, pos, state, null, stack);
            }

        }

        return true;
    }

}
