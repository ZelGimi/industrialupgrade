package com.denfop.items.relocator;

import com.denfop.Localization;
import com.denfop.api.inv.IAdvInventory;
import com.denfop.container.ContainerLeadBox;
import com.denfop.items.BaseEnergyItem;
import com.denfop.items.IItemStackInventory;
import com.denfop.items.bags.ItemStackLeadBox;
import com.denfop.network.packet.CustomPacketBuffer;
import com.denfop.network.packet.IUpdatableItemStackEvent;
import com.denfop.utils.ModUtils;
import net.minecraft.Util;
import net.minecraft.core.Registry;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.level.Level;
import net.minecraftforge.network.NetworkHooks;
import org.jetbrains.annotations.Nullable;

import javax.annotation.Nonnull;
import java.util.List;

public class ItemRelocator extends BaseEnergyItem implements IItemStackInventory, IUpdatableItemStackEvent {
    public ItemRelocator() {
        super(10000000, 8192, 3);

    }

    @Override
    public void appendHoverText(ItemStack pStack, @Nullable Level pLevel, List<Component> pTooltipComponents, TooltipFlag pIsAdvanced) {
        super.appendHoverText(pStack, pLevel, pTooltipComponents, pIsAdvanced);
        pTooltipComponents.add(Component.literal(Localization.translate( "iu.relocator.info")));
        pTooltipComponents.add(Component.literal(Localization.translate( "iu.relocator.info1")));
        pTooltipComponents.add(Component.literal(Localization.translate( "iu.relocator.info2")));
    }

    @Override
    public void updateField(final String name, final CustomPacketBuffer buffer, final ItemStack stack) {

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
            this.nameItem = "iu.relocator.name";
        }

        return this.nameItem;
    }
    @Override
    public void updateEvent(final int event, final ItemStack stack) {

    }

    @Override
    public boolean onDroppedByPlayer(@Nonnull ItemStack stack, @Nonnull Player player) {
        if (!player.level.isClientSide && !stack.isEmpty() && player.containerMenu instanceof ContainerLeadBox) {
            ItemStackLeadBox toolbox = ((ContainerLeadBox) player.containerMenu).base;
            if (toolbox.isThisContainer(stack)) {
                toolbox.saveAndThrow(stack);
                player.closeContainer();
            }
        }
        return true;
    }

    @Override
    @Nonnull
    public InteractionResultHolder<ItemStack> use(@Nonnull Level world, @Nonnull Player player, @Nonnull InteractionHand hand) {
        ItemStack stack = ModUtils.get(player, hand);
        if (!world.isClientSide) {
            save(stack, player);

            CustomPacketBuffer growingBuffer = new CustomPacketBuffer();

            growingBuffer.writeByte(1);

            growingBuffer.flip();
            NetworkHooks.openScreen((ServerPlayer) player, getInventory(player, player.getItemInHand(hand)), buf -> buf.writeBytes(growingBuffer));


            return InteractionResultHolder.success(player.getItemInHand(hand));


        }

        return InteractionResultHolder.pass(player.getItemInHand(hand));
    }

    public IAdvInventory getInventory(Player player, ItemStack stack) {
        return new ItemStackRelocator(player, stack);
    }

    public void save(ItemStack stack, Player player) {
        final CompoundTag nbt = ModUtils.nbt(stack);
        nbt.putBoolean("open", true);
        nbt.putInt("slot_inventory", player.getInventory().selected);
    }
}
