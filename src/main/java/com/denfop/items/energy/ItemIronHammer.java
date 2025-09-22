package com.denfop.items.energy;

import com.denfop.utils.ExperienceUtils;
import com.denfop.utils.ModUtils;
import com.denfop.utils.RetraceDiggingUtils;
import com.google.common.collect.Sets;
import net.minecraft.block.Block;
import net.minecraft.block.material.MaterialLiquid;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.Minecraft;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.init.Blocks;
import net.minecraft.init.Enchantments;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.network.play.client.CPacketPlayerDigging;
import net.minecraft.network.play.server.SPacketBlockChange;
import net.minecraft.network.play.server.SPacketEntityTeleport;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.world.World;
import net.minecraftforge.common.ForgeHooks;

import javax.annotation.Nonnull;
import java.util.HashSet;
import java.util.List;
import java.util.Objects;

public class ItemIronHammer extends ItemToolIU {

    private final HashSet<IBlockState> blocks;

    public ItemIronHammer() {
        super("iron_hammer", 2, 1, new HashSet<>());
        this.setHarvestLevel("pickaxe", 2);
        setMaxDamage((int) (ToolMaterial.IRON.getMaxUses()));
        this.blocks = Sets.newHashSet(
                Blocks.COBBLESTONE.getDefaultState(),
                Blocks.DOUBLE_STONE_SLAB.getDefaultState(),
                Blocks.STONE_SLAB.getDefaultState(),
                Blocks.STONE.getDefaultState(),
                Blocks.SANDSTONE.getDefaultState(),
                Blocks.MOSSY_COBBLESTONE.getDefaultState(),
                Blocks.IRON_ORE.getDefaultState(),
                Blocks.IRON_BLOCK.getDefaultState(),
                Blocks.COAL_ORE.getDefaultState(),
                Blocks.GOLD_BLOCK.getDefaultState(),
                Blocks.GOLD_ORE.getDefaultState(),
                Blocks.DIAMOND_ORE.getDefaultState(),
                Blocks.DIAMOND_BLOCK.getDefaultState(),
                Blocks.ICE.getDefaultState(),
                Blocks.NETHERRACK.getDefaultState(),
                Blocks.LAPIS_ORE.getDefaultState(),
                Blocks.LAPIS_BLOCK.getDefaultState(),
                Blocks.REDSTONE_ORE.getDefaultState(),
                Blocks.LIT_REDSTONE_ORE.getDefaultState(),
                Blocks.RAIL.getDefaultState(),
                Blocks.DETECTOR_RAIL.getDefaultState(),
                Blocks.GOLDEN_RAIL.getDefaultState(),
                Blocks.ACTIVATOR_RAIL.getDefaultState(),
                Blocks.GRASS.getDefaultState(),
                Blocks.DIRT.getDefaultState(),
                Blocks.SAND.getDefaultState(),
                Blocks.GRAVEL.getDefaultState(),
                Blocks.SNOW_LAYER.getDefaultState(),
                Blocks.SNOW.getDefaultState(),
                Blocks.CLAY.getDefaultState(),
                Blocks.FARMLAND.getDefaultState(),
                Blocks.SOUL_SAND.getDefaultState(),
                Blocks.MYCELIUM.getDefaultState()
        );
    }

    public boolean onBlockStartBreak(@Nonnull ItemStack stack, @Nonnull BlockPos pos, @Nonnull EntityPlayer player) {

        World world = player.getEntityWorld();
        IBlockState state = world.getBlockState(pos);
        Block block = state.getBlock();
        RayTraceResult mop = RetraceDiggingUtils.retrace(player);
        if (block.equals(Blocks.AIR)) {
            return super.onBlockStartBreak(stack, pos, player);
        }


        byte aoe = 0;
        if (player.isSneaking()) {
            if (!mop.typeOfHit.equals(RayTraceResult.Type.MISS)) {
                return break_block(world, block, mop, aoe, player, pos, stack
                );
            }
        }

        return break_block(world, block, mop, (byte) (1 + aoe), player, pos, stack
        );
    }

    private int getExperience(
            IBlockState state,
            World world,
            BlockPos pos_block,
            int fortune,
            ItemStack stack,
            final Block localBlock
    ) {
        int col = localBlock.getExpDrop(state, world, pos_block, fortune);
        return col;
    }


    public boolean onBlockDestroyed(
            @Nonnull ItemStack stack,
            @Nonnull World world,
            IBlockState state,
            @Nonnull BlockPos pos,
            @Nonnull EntityLivingBase entity
    ) {

        Block block = state.getBlock();
        if (block.equals(Blocks.AIR)) {
            return false;
        } else {


            if (state.getMaterial() instanceof MaterialLiquid || (state.getBlockHardness(
                    world,
                    pos
            ) == -1 && !((EntityPlayer) entity).capabilities.isCreativeMode)) {
                return false;
            }

            if (!world.isRemote) {

                if (ForgeHooks.onBlockBreakEvent(world, world.getWorldInfo().getGameType(), (EntityPlayerMP) entity, pos) == -1) {
                    return false;
                }
                if (block.removedByPlayer(state, world, pos, (EntityPlayerMP) entity, true)) {
                    block.onBlockDestroyedByPlayer(world, pos, state);

                    block.harvestBlock(world, (EntityPlayerMP) entity, pos, state, null, stack);
                    NBTTagCompound nbt = ModUtils.nbt(stack);
                    List<EntityItem> items = entity.getEntityWorld().getEntitiesWithinAABB(
                            EntityItem.class,
                            new AxisAlignedBB(pos.getX() - 1, pos.getY() - 1, pos.getZ() - 1, pos.getX() + 1,
                                    pos.getY() + 1,
                                    pos.getZ() + 1
                            )
                    );
                    ((EntityPlayerMP) entity).addExhaustion(-0.025F);
                    if ((ModUtils.getore(block, block.getMetaFromState(state)))) {
                        for (EntityItem item : items) {
                            if (!entity.getEntityWorld().isRemote) {
                                ItemStack stack1 = item.getItem();
                                item.setLocationAndAngles(entity.posX, entity.posY, entity.posZ, 0.0F, 0.0F);
                                item.setPickupDelay(0);
                                ((EntityPlayerMP) entity).connection.sendPacket(new SPacketEntityTeleport(item));


                            }
                        }
                    } else {
                        if (nbt.getBoolean("black")) {
                            for (EntityItem item : items) {
                                if (!entity.getEntityWorld().isRemote) {
                                    item.setDead();

                                }
                            }
                        }
                    }

                }
                EntityPlayerMP mpPlayer = (EntityPlayerMP) entity;
                mpPlayer.connection.sendPacket(new SPacketBlockChange(world, pos));
            } else {
                if (block.removedByPlayer(state, world, pos, (EntityPlayer) entity, true)) {
                    block.onBlockDestroyedByPlayer(world, pos, state);
                }

                Objects.requireNonNull(Minecraft.getMinecraft().getConnection()).sendPacket(new CPacketPlayerDigging(
                        CPacketPlayerDigging.Action.STOP_DESTROY_BLOCK,
                        pos,
                        Minecraft.getMinecraft().objectMouseOver.sideHit
                ));
            }


            return true;
        }
    }

    boolean break_block(
            World world, Block block, RayTraceResult mop, byte mode_item, EntityPlayer player, BlockPos pos,
            ItemStack stack
    ) {
        byte xRange = mode_item;
        byte yRange = mode_item;
        byte zRange = mode_item;
        int x = pos.getX();
        int y = pos.getY();
        int z = pos.getZ();
        byte dig_depth = 0;
        switch (mop.sideHit.ordinal()) {
            case 0:
            case 1:
                yRange = dig_depth;
                break;
            case 2:
            case 3:
                zRange = dig_depth;
                break;
            case 4:
            case 5:
                xRange = dig_depth;
                break;
        }

        boolean silktouch = EnchantmentHelper.getEnchantmentLevel(Enchantments.SILK_TOUCH, stack) > 0;
        int fortune = EnchantmentHelper.getEnchantmentLevel(Enchantments.FORTUNE, stack);
        fortune = Math.min(3, fortune);

        int Yy;
        Yy = yRange > 0 ? yRange - 1 : 0;
        stack.damageItem(1, player);

        if (!player.capabilities.isCreativeMode) {
            for (int xPos = x - xRange; xPos <= x + xRange; xPos++) {
                for (int yPos = y - yRange + Yy; yPos <= y + yRange + Yy; yPos++) {
                    for (int zPos = z - zRange; zPos <= z + zRange; zPos++) {
                        if (stack.getItemDamage() > 0) {

                            BlockPos pos_block = new BlockPos(xPos, yPos, zPos);
                            IBlockState state = world.getBlockState(pos_block);
                            Block localBlock = state.getBlock();
                            if (!localBlock.equals(Blocks.AIR) && canHarvestBlock(state, stack)
                                    && state.getBlockHardness(world, pos_block) >= 0.0F
                            ) {
                                if (state.getBlockHardness(world, pos_block) > 0.0F) {
                                    onBlockDestroyed(stack, world, state, pos_block,
                                            player
                                    );
                                }
                                if (!silktouch) {
                                    ExperienceUtils.addPlayerXP(player, getExperience(state, world, pos_block, fortune, stack
                                            , localBlock));
                                }


                            }
                        } else {
                            break;
                        }
                    }
                }
            }
        } else {
            if (stack.getItemDamage() > 0) {
                IBlockState state = world.getBlockState(pos);
                Block localBlock = state.getBlock();
                if (!localBlock.equals(Blocks.AIR) && canHarvestBlock(state, stack)
                        && state.getBlockHardness(world, pos) >= 0.0F
                        || (
                        block == Blocks.MONSTER_EGG)) {
                    if (state.getBlockHardness(world, pos) >= 0.0F) {
                        onBlockDestroyed(stack, world, state, pos,
                                player
                        );
                    }
                    if (!silktouch) {
                        ExperienceUtils.addPlayerXP(player, getExperience(state, world, pos, fortune, stack
                                , localBlock));
                    }


                } else {
                    if (state.getBlockHardness(world, pos) >= 0.0F) {
                        return onBlockDestroyed(stack, world, state, pos,
                                player
                        );
                    }
                }
            }
        }
        return true;
    }

    public float getDestroySpeed(ItemStack stack, IBlockState state) {
        for (String type : getToolClasses(stack)) {
            if (state.getBlock().isToolEffective(type, state)) {
                return efficiency;
            }
        }
        return blocks.contains(state) ? this.efficiency : 1.0F;
    }

    @Override
    public void registerModels() {
        this.registerModels(this.name);
    }

}
