package com.denfop.items;

import com.denfop.IUCore;
import com.denfop.api.multiblock.IMainMultiBlock;
import com.denfop.tiles.mechanism.multiblocks.base.TileMultiBlockBase;
import net.minecraft.Util;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Registry;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.context.UseOnContext;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.entity.BlockEntity;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class ItemDeplanner extends Item {
    private String nameItem;

    public ItemDeplanner() {
        super(new Properties().tab(IUCore.EnergyTab).stacksTo(1).setNoRepair());
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
            this.nameItem = "item."+pathBuilder.toString().split("\\.")[2];
        }

        return this.nameItem;
    }

    @Override
    public InteractionResult onItemUseFirst(ItemStack stack, UseOnContext context) {

        Player player = context.getPlayer();
        Level world = context.getLevel();
        BlockPos pos = context.getClickedPos();

        BlockEntity tile = world.getBlockEntity(pos);

        if (tile instanceof IMainMultiBlock) {
            IMainMultiBlock mainMultiBlock = (IMainMultiBlock) tile;
            List<ItemStack> itemStackList = new ArrayList<>();

            if (mainMultiBlock.isFull()) {
                for (Map.Entry<BlockPos, ItemStack> entry : mainMultiBlock.getMultiBlockStucture().ItemStackMap.entrySet()) {
                    BlockPos pos1;
                    if (entry.getKey().equals(BlockPos.ZERO)) {
                        continue;
                    }
                    if (entry.getValue().isEmpty()) {
                        continue;
                    }


                    pos1 = switch (((TileMultiBlockBase) mainMultiBlock).getFacing()) {
                        case NORTH -> new BlockPos(entry.getKey().getX(), entry.getKey().getY(), entry.getKey().getZ());
                        case EAST ->
                                new BlockPos(entry.getKey().getZ() * -1, entry.getKey().getY(), entry.getKey().getX());
                        case WEST ->
                                new BlockPos(entry.getKey().getZ(), entry.getKey().getY(), entry.getKey().getX() * -1);
                        case SOUTH ->
                                new BlockPos(entry.getKey().getX() * -1, entry.getKey().getY(), entry.getKey().getZ() * -1);
                        default -> throw new IllegalStateException("Unexpected value: ");
                    };
                    itemStackList.add(entry.getValue().copy());
                    world.removeBlockEntity(pos.offset(pos1));
                    world.setBlock(pos.offset(pos1), Blocks.AIR.defaultBlockState(), 3);

                }
                itemStackList.add(mainMultiBlock.getMultiBlockStucture().ItemStackMap.get(BlockPos.ZERO).copy());

                ((TileMultiBlockBase) mainMultiBlock).onUnloaded();
                world.removeBlockEntity(pos);
                world.setBlock(pos, Blocks.AIR.defaultBlockState(), 3);

                if (!world.isClientSide) {
                    for (ItemStack stack1 : itemStackList) {
                        ItemEntity entityItem = new ItemEntity(world, player.getX(), player.getY(), player.getZ(), stack1);
                        entityItem.setPickUpDelay(0);
                        world.addFreshEntity(entityItem);
                    }
                }
                return InteractionResult.SUCCESS;
            }
        }

        return InteractionResult.PASS;
    }


}
