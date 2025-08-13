package com.denfop.items.energy;

import com.denfop.Localization;
import com.denfop.items.energy.instruments.EnumTypeInstruments;
import com.denfop.utils.ExperienceUtils;
import com.denfop.utils.ModUtils;
import com.denfop.utils.RetraceDiggingUtils;
import net.minecraft.core.BlockPos;
import net.minecraft.core.registries.Registries;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.tags.BlockTags;
import net.minecraft.tags.TagKey;
import net.minecraft.world.entity.Entity;
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
import net.minecraft.world.phys.Vec3;
import net.neoforged.neoforge.common.CommonHooks;
import net.neoforged.neoforge.common.ItemAbilities;
import net.neoforged.neoforge.common.ItemAbility;

import java.util.List;
import java.util.Set;

import static net.minecraft.tags.ItemTags.PICKAXES;

public class ItemIronHammer extends ItemToolIU {
    private final Set<BlockState> mineableBlocks;
    private final List<TagKey<Block>> item_tools;

    public ItemIronHammer() {
        super(BlockTags.MINEABLE_WITH_PICKAXE);
        this.mineableBlocks = EnumTypeInstruments.DRILL.getMineableBlocks();
        this.item_tools = EnumTypeInstruments.DRILL.getListItems();
    }

    @Override
    public boolean isEnchantable(ItemStack p_41456_) {
        return true;
    }


    @Override
    public int getEnchantmentValue(ItemStack stack) {
        return Tiers.IRON.getEnchantmentValue();
    }

    @Override
    public int getEnchantmentValue() {
        return Tiers.IRON.getEnchantmentValue();
    }

    @Override
    public String[] getTags() {
        return new String[]{PICKAXES.location().toString()};
    }

    @Override
    public void appendHoverText(ItemStack p_41421_, TooltipContext p_339594_, List<Component> p_41423_, TooltipFlag p_41424_) {
        super.appendHoverText(p_41421_, p_339594_, p_41423_, p_41424_);
        p_41423_.add(Component.literal(Localization.translate("iu.hammer.info")));
    }

    @Override
    public boolean mineBlock(ItemStack stack, Level p_41417_, BlockState state, BlockPos pos, LivingEntity p_41420_) {
        if (!(p_41420_ instanceof Player player)) {
            return false;
        }
        Level world = player.level();
        Block block = state.getBlock();
        BlockHitResult mop = RetraceDiggingUtils.retrace(player);

        if (state.isAir()) {
            return super.mineBlock(stack, p_41417_, state, pos, player);
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


    private int getExperience(
            BlockState state,
            Level world,
            BlockPos pos_block,
            Entity entity,
            ItemStack stack,
            final Block localBlock
    ) {
        int col = localBlock.getExpDrop(state, world, pos_block, null, entity, stack);
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

        boolean silkTouch = EnchantmentHelper.getItemEnchantmentLevel(player.registryAccess().registryOrThrow(Registries.ENCHANTMENT).getHolderOrThrow(Enchantments.SILK_TOUCH), stack) > 0;
        int fortune = EnchantmentHelper.getItemEnchantmentLevel(player.registryAccess().registryOrThrow(Registries.ENCHANTMENT).getHolderOrThrow(Enchantments.FORTUNE), stack);
        fortune = Math.min(3, fortune);

        int yOffset = yRange > 0 ? yRange - 1 : 0;
        stack.hurtAndBreak(1, player, EquipmentSlot.MAINHAND);

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
                                    onDestroyed(stack, level, state, blockPos, player);
                                }
                                if (!silkTouch) {
                                    ExperienceUtils.addPlayerXP(player, getExperience(state, level, blockPos, player, stack, localBlock));
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
                        onDestroyed(stack, level, state, pos, player);
                    }
                    if (!silkTouch) {
                        ExperienceUtils.addPlayerXP(player, getExperience(state, level, pos, player, stack, localBlock));
                    }
                } else {
                    if (state.getDestroySpeed(level, pos) >= 0.0F) {
                        return onDestroyed(stack, level, state, pos, player);
                    }
                }
            }
        }
        return true;
    }

    @Override
    public float getDestroySpeed(ItemStack stack, BlockState state) {
        return mineableBlocks.contains(state) ? this.getTier().getSpeed() : 1.0F;
    }


    @Override
    public boolean canPerformAction(ItemStack stack, ItemAbility toolAction) {
        return ItemAbilities.DEFAULT_SHOVEL_ACTIONS.contains(toolAction) || ItemAbilities.DEFAULT_PICKAXE_ACTIONS.contains(toolAction);
    }
    public boolean onDestroyed(ItemStack stack, Level world, BlockState state, BlockPos pos, LivingEntity entity) {
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

            if (CommonHooks.fireBlockBreak(serverWorld, serverPlayer.gameMode.getGameModeForPlayer(), serverPlayer, pos, state).isCanceled()) {
                return false;
            }

            if (block.onDestroyedByPlayer(state, world, pos, (ServerPlayer) entity, true, world.getFluidState(pos))) {
                block.destroy(world, pos, state);
                block.playerDestroy(world, (ServerPlayer) entity, pos, state, null, stack);


                List<ItemEntity> items = world.getEntitiesOfClass(
                        ItemEntity.class,
                        new AABB(Vec3.atLowerCornerOf(pos.offset(-1, -1, -1)), Vec3.atLowerCornerOf(pos.offset(1, 1, 1)))
                );

                serverPlayer.causeFoodExhaustion(-0.025F);

                if (ModUtils.getOre(block)) {
                    for (ItemEntity item : items) {
                        if (!world.isClientSide) {
                            item.setPos(player.getX(), player.getY(), player.getZ());
                            item.setPickUpDelay(0);
                        }
                    }
                }
            }

        } else {
            if (block.onDestroyedByPlayer(state, world, pos, (ServerPlayer) entity, true, world.getFluidState(pos))) {
                block.destroy(world, pos, state);
                block.playerDestroy(world, (ServerPlayer) entity, pos, state, null, stack);

            }

        }

        return true;
    }
}
