package com.denfop.items.book;

import com.denfop.IUCore;
import com.denfop.api.container.CustomWorldContainer;
import com.denfop.containermenu.ContainerMenuBeeAnalyzer;
import com.denfop.datacomponent.ContainerItem;
import com.denfop.datacomponent.DataComponentsInit;
import com.denfop.items.IItemStackInventory;
import com.denfop.items.bee.ItemStackBeeAnalyzer;
import com.denfop.network.packet.CustomPacketBuffer;
import com.denfop.network.packet.IUpdatableItemStackEvent;
import com.denfop.tabs.IItemTab;
import com.denfop.utils.ModUtils;
import net.minecraft.Util;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.ClipContext;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.BlockHitResult;

import javax.annotation.Nonnull;

public class ItemBook extends Item implements IItemStackInventory, IItemTab, IUpdatableItemStackEvent {


    private final String internalName;
    private String nameItem;

    public ItemBook(String internalName) {
        super(new Properties().stacksTo(1));
        this.internalName = internalName;
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
            this.nameItem = pathBuilder.toString();
        }

        return this.nameItem;
    }

    @Nonnull
    public String getUnlocalizedName() {
        return "item." + this.internalName + ".name";
    }


    public void save(ItemStack stack, Player player) {
        ContainerItem containerItem = ContainerItem.getContainer(stack);
        containerItem = containerItem.updateOpen(stack, true);
        containerItem.updateSlot(stack, player.getInventory().selected);
    }

    @Override
    public void inventoryTick(
            ItemStack stack,
            Level world,
            Entity entity,
            int itemSlot,
            boolean isSelected
    ) {
        super.inventoryTick(stack, world, entity, itemSlot, isSelected);

        if (!(entity instanceof Player)) {
            return;
        }
        Player player = (Player) entity;
        ContainerItem containerItem = ContainerItem.getContainer(stack);

        if (containerItem.open()) {
            int slotId = containerItem.slot_inventory();
            if (slotId != itemSlot && !world.isClientSide && !stack.isEmpty() && player.containerMenu instanceof ContainerMenuBeeAnalyzer) {
                ItemStackBeeAnalyzer toolbox = ((ContainerMenuBeeAnalyzer) player.containerMenu).base;
                if (toolbox.isThisContainer(stack)) {
                    toolbox.saveAsThrown(stack);
                    player.closeContainer();
                    containerItem.updateOpen(stack, false);
                }
            }
        }


    }

    @Override
    public boolean onDroppedByPlayer(@Nonnull ItemStack stack, @Nonnull Player player) {
        if (!player.level().isClientSide && !stack.isEmpty() && player.containerMenu instanceof ContainerMenuBeeAnalyzer) {
            ItemStackBeeAnalyzer toolbox = ((ContainerMenuBeeAnalyzer) player.containerMenu).base;
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
        BlockHitResult blockhitresult = getPlayerPOVHitResult(world, player, ClipContext.Fluid.SOURCE_ONLY);
        if (!player.level().isClientSide && world.getBlockEntity(blockhitresult.getBlockPos()) == null) {
            save(stack, player);

            CustomPacketBuffer growingBuffer = new CustomPacketBuffer(player.registryAccess());

            growingBuffer.writeByte(1);

            growingBuffer.flip();
            player.openMenu(getInventory(player, player.getItemInHand(hand)), buf -> buf.writeBytes(growingBuffer));
        }

        return InteractionResultHolder.success(player.getItemInHand(hand));

    }


    @Override
    public CustomWorldContainer getInventory(Player player, ItemStack stack) {
        return new ItemStackBook(player, stack);
    }

    @Override
    public CreativeModeTab getItemCategory() {
        return IUCore.ItemTab;
    }

    @Override
    public void updateField(String name, CustomPacketBuffer buffer, ItemStack stack) {

    }

    @Override
    public void updateEvent(int event, ItemStack stack) {
        stack.set(DataComponentsInit.MODE, event);
    }
}
