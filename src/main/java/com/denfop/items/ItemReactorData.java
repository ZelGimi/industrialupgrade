package com.denfop.items;

import com.denfop.IItemTab;
import com.denfop.IUCore;
import com.denfop.api.reactors.IAdvReactor;
import com.denfop.tiles.mechanism.multiblocks.base.TileMultiBlockBase;
import com.denfop.utils.ModUtils;
import net.minecraft.Util;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.context.UseOnContext;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntity;

public class ItemReactorData extends Item implements IItemTab {
    private String nameItem;

    public ItemReactorData() {
        super(new Item.Properties().stacksTo(1).setNoRepair());
    }
    @Override
    public CreativeModeTab getItemCategory() {
        return IUCore.EnergyTab;
    }
    protected String getOrCreateDescriptionId() {
        if (this.nameItem == null) {
            StringBuilder pathBuilder = new StringBuilder(Util.makeDescriptionId("iu", BuiltInRegistries.ITEM.getKey(this)));
            String targetString = "industrialupgrade.";
            String replacement = "";
            if (replacement != null) {
                int index = pathBuilder.indexOf(targetString);
                while (index != -1) {
                    pathBuilder.replace(index, index + targetString.length(), replacement);
                    index = pathBuilder.indexOf(targetString, index + replacement.length());
                }
            }
            this.nameItem = "iu.reactor_data_item.name";
        }

        return this.nameItem;
    }
    @Override
    public InteractionResult onItemUseFirst(ItemStack stack, UseOnContext p_41427_) {
        Level world = p_41427_.getLevel();
        if (world.isClientSide) {
            return InteractionResult.PASS;
        }
        Player player = p_41427_.getPlayer();
        InteractionHand hand = p_41427_.getHand();
        BlockEntity tileEntity = world.getBlockEntity(p_41427_.getClickedPos());
        if (tileEntity instanceof TileMultiBlockBase && tileEntity instanceof IAdvReactor) {
            TileMultiBlockBase tileMultiBlockBase = (TileMultiBlockBase) tileEntity;
            final CompoundTag nbt = ModUtils.nbt(player.getItemInHand(hand));
            nbt.putInt("x", tileMultiBlockBase.getBlockPos().getX());
            nbt.putInt("y", tileMultiBlockBase.getBlockPos().getY());
            nbt.putInt("z", tileMultiBlockBase.getBlockPos().getZ());
            nbt.putString("name", tileMultiBlockBase.getPickBlock(player, null).getDisplayName().getString());
            return InteractionResult.SUCCESS;
        }
        return InteractionResult.PASS;
    }

}
