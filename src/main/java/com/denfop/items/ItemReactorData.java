package com.denfop.items;

import com.denfop.IUCore;
import com.denfop.api.reactors.IAdvReactor;
import com.denfop.blockentity.mechanism.multiblocks.base.BlockEntityMultiBlockBase;
import com.denfop.utils.Localization;
import com.denfop.utils.ModUtils;
import net.minecraft.Util;
import net.minecraft.core.Registry;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.item.context.UseOnContext;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntity;
import org.jetbrains.annotations.Nullable;

import java.util.List;

public class ItemReactorData extends Item {
    private String nameItem;

    public ItemReactorData() {
        super(new Item.Properties().tab(IUCore.EnergyTab).stacksTo(1).setNoRepair());
    }

    @Override
    public void appendHoverText(ItemStack pStack, @Nullable Level pLevel, List<Component> pTooltipComponents, TooltipFlag pIsAdvanced) {
        super.appendHoverText(pStack, pLevel, pTooltipComponents, pIsAdvanced);
        pTooltipComponents.add(Component.literal(Localization.translate("iu.reactor_sensor.info")));
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
        if (tileEntity instanceof BlockEntityMultiBlockBase && tileEntity instanceof IAdvReactor) {
            BlockEntityMultiBlockBase tileMultiBlockBase = (BlockEntityMultiBlockBase) tileEntity;
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
