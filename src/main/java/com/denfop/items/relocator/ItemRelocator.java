package com.denfop.items.relocator;

import com.denfop.api.container.CustomWorldContainer;
import com.denfop.containermenu.ContainerMenuLeadBox;
import com.denfop.datacomponent.ContainerItem;
import com.denfop.items.BaseEnergyItem;
import com.denfop.items.IItemStackInventory;
import com.denfop.items.bags.ItemStackLeadBox;
import com.denfop.network.packet.CustomPacketBuffer;
import com.denfop.network.packet.IUpdatableItemStackEvent;
import com.denfop.utils.Localization;
import com.denfop.utils.ModUtils;
import net.minecraft.Util;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.network.chat.Component;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.level.Level;

import javax.annotation.Nonnull;
import java.util.List;

public class ItemRelocator extends BaseEnergyItem implements IItemStackInventory, IUpdatableItemStackEvent {
    public ItemRelocator() {
        super(10000000, 8192, 3);

    }

    @Override
    public void appendHoverText(ItemStack p_41421_, TooltipContext p_339594_, List<Component> p_41423_, TooltipFlag p_41424_) {
        super.appendHoverText(p_41421_, p_339594_, p_41423_, p_41424_);
        p_41423_.add(Component.literal(Localization.translate("iu.relocator.info")));
        p_41423_.add(Component.literal(Localization.translate("iu.relocator.info1")));
        p_41423_.add(Component.literal(Localization.translate("iu.relocator.info2")));
    }

    @Override
    public void updateField(final String name, final CustomPacketBuffer buffer, final ItemStack stack) {

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
            this.nameItem = "iu.relocator.name";
        }

        return this.nameItem;
    }

    @Override
    public void updateEvent(final int event, final ItemStack stack) {

    }

    @Override
    public boolean onDroppedByPlayer(@Nonnull ItemStack stack, @Nonnull Player player) {
        if (!player.level().isClientSide && !stack.isEmpty() && player.containerMenu instanceof ContainerMenuLeadBox) {
            ItemStackLeadBox toolbox = ((ContainerMenuLeadBox) player.containerMenu).base;
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

            CustomPacketBuffer growingBuffer = new CustomPacketBuffer(world.registryAccess());

            growingBuffer.writeByte(1);

            growingBuffer.flip();
            player.openMenu(getInventory(player, player.getItemInHand(hand)), buf -> buf.writeBytes(growingBuffer));


            return InteractionResultHolder.success(player.getItemInHand(hand));


        }

        return InteractionResultHolder.pass(player.getItemInHand(hand));
    }

    public CustomWorldContainer getInventory(Player player, ItemStack stack) {
        return new ItemStackRelocator(player, stack);
    }

    public void save(ItemStack stack, Player player) {
        ContainerItem containerItem = ContainerItem.getContainer(stack);
        containerItem = containerItem.updateOpen(stack, true);
        containerItem.updateSlot(stack, player.getInventory().selected);
    }
}
