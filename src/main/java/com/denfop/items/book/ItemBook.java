package com.denfop.items.book;

import com.denfop.Constants;
import com.denfop.IItemTab;
import com.denfop.IUCore;
import com.denfop.api.inv.IAdvInventory;
import com.denfop.container.ContainerBeeAnalyzer;
import com.denfop.items.IItemStackInventory;
import com.denfop.items.bee.ItemStackBeeAnalyzer;
import com.denfop.network.packet.CustomPacketBuffer;
import com.denfop.network.packet.IUpdatableItemStackEvent;
import com.denfop.register.Register;
import com.denfop.utils.ModUtils;
import net.minecraft.Util;
import net.minecraft.core.Registry;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.server.level.ServerPlayer;
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
import net.minecraftforge.network.NetworkHooks;


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
        final CompoundTag nbt = ModUtils.nbt(stack);
        nbt.putBoolean("open", true);
        nbt.putInt("slot_inventory", player.getInventory().selected);
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
        CompoundTag nbt = stack.getOrCreateTag();

        if (nbt.getBoolean("open")) {
            int slotId = nbt.getInt("slot_inventory");
            if (slotId != itemSlot && !world.isClientSide && !stack.isEmpty() && player.containerMenu instanceof ContainerBeeAnalyzer) {
                ItemStackBeeAnalyzer toolbox = ((ContainerBeeAnalyzer) player.containerMenu).base;
                if (toolbox.isThisContainer(stack)) {
                    toolbox.saveAsThrown(stack);
                    player.closeContainer();
                    nbt.putBoolean("open", false);
                }
            }
        }


    }

    @Override
    public boolean onDroppedByPlayer(@Nonnull ItemStack stack, @Nonnull Player player) {
        if (!player.level().isClientSide && !stack.isEmpty() && player.containerMenu instanceof ContainerBeeAnalyzer) {
            ItemStackBeeAnalyzer toolbox = ((ContainerBeeAnalyzer) player.containerMenu).base;
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

            CustomPacketBuffer growingBuffer = new CustomPacketBuffer();

            growingBuffer.writeByte(1);

            growingBuffer.flip();
            NetworkHooks.openScreen((ServerPlayer) player, getInventory(player, player.getItemInHand(hand)), buf -> buf.writeBytes(growingBuffer));
        }

        return InteractionResultHolder.success(player.getItemInHand(hand));

    }






    @Override
    public IAdvInventory getInventory(Player player, ItemStack stack) {
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
        ModUtils.nbt(stack).putInt("book_info",event);
    }
}
