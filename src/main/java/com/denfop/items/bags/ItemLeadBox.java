package com.denfop.items.bags;

import com.denfop.IItemTab;
import com.denfop.IUCore;
import com.denfop.IUPotion;
import com.denfop.Localization;
import com.denfop.api.inv.IAdvInventory;
import com.denfop.container.ContainerLeadBox;
import com.denfop.items.IItemStackInventory;
import com.denfop.items.reactors.IRadioactiveItemType;
import com.denfop.items.reactors.ItemBaseRod;
import com.denfop.network.packet.CustomPacketBuffer;
import com.denfop.utils.ModUtils;
import net.minecraft.ChatFormatting;
import net.minecraft.Util;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.core.NonNullList;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.level.Level;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.network.NetworkHooks;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.List;

public class ItemLeadBox extends Item implements IItemStackInventory, IItemTab {
    private final int slots;
    private String nameItem;

    public ItemLeadBox() {
        super(new Properties().stacksTo(1));
        this.slots = 27;
    }
    @Override
    public void fillItemCategory(CreativeModeTab p_41391_, NonNullList<ItemStack> p_41392_) {
        if (this.allowedIn(p_41391_)) {
            p_41392_.add(new ItemStack(this, 1));
        }
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
            this.nameItem = "item."+pathBuilder.toString().split("\\.")[2];
        }

        return this.nameItem;
    }
    public IAdvInventory getInventory(Player player, ItemStack stack) {
        return new ItemStackLeadBox(player, stack, this.slots);
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
            if (slotId != itemSlot && !world.isClientSide && !stack.isEmpty() && player.containerMenu instanceof ContainerLeadBox) {
                ItemStackLeadBox toolbox = ((ContainerLeadBox) player.containerMenu).base;
                if (toolbox.isThisContainer(stack)) {
                    toolbox.saveAsThrown(stack);
                    player.closeContainer();
                    nbt.putBoolean("open", false);
                }
            }
        }

        if (world.getGameTime() % 40 == 0) {
            if (!(player.containerMenu instanceof ContainerLeadBox)) {
                boolean rod = nbt.getBoolean("rod");
                for (int i = 0; i < player.getInventory().getContainerSize(); i++) {
                    ItemStack currentStack = player.getInventory().getItem(i);
                    if (currentStack.getItem() instanceof IRadioactiveItemType) {
                        if (!rod && currentStack.getItem() instanceof ItemBaseRod) {
                            continue;
                        }
                        ItemStackLeadBox box = (ItemStackLeadBox) getInventory(player, stack);
                        if (box.canAdd(currentStack)) {
                            box.add(currentStack);
                            player.removeEffect(IUPotion.radiation);
                            player.getInventory().setItem(i, ItemStack.EMPTY);
                            player.containerMenu.broadcastChanges();
                            box.setChanged();
                        }
                    }
                }
            }
        }
    }

    @Override
    @OnlyIn(Dist.CLIENT)
    public void appendHoverText(
            ItemStack stack,
            @Nullable Level world,
            List<Component> tooltip,
            TooltipFlag flag
    ) {
        tooltip.add(Component.translatable("iu.radiationbox"));
        CompoundTag nbt = stack.getOrCreateTag();
        boolean rod = nbt.getBoolean("rod");
        tooltip.add(Component.translatable("message.text.mode_no_instrument").append(": ")
                .append(rod ? Component.translatable("message.leadbox.enable") : Component.translatable("message.leadbox.disable")));

        if (!Screen.hasShiftDown()) {
            tooltip.add(Component.translatable("press.lshift"));
        } else {
            tooltip.add(Component.translatable("iu.changemode_key").append(Component.translatable("iu.changemode_rcm1")).append(" + SHIFT"));
        }
        super.appendHoverText(stack, world, tooltip, flag);
    }

    @Override
    public boolean onDroppedByPlayer(@Nonnull ItemStack stack, @Nonnull Player player) {
        if (!player.level().isClientSide && !stack.isEmpty() && player.containerMenu instanceof ContainerLeadBox) {
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
       /* if (world.isClientSide) {
            try {
                Optional<MenuScreens.ScreenConstructor<ContainerBase<?>, ?>> factory = MenuScreens.getScreenFactory(Register.inventory_container.get(), Minecraft.getInstance(), 0, Component.translatable("iu"));
                factory.get().create(getInventory(player,stack).getGuiContainer(player),player.getInventory(), Component.translatable(""));
            }catch (Exception e){
                e.printStackTrace();
            }
        }*/
        if (!world.isClientSide && !player.isShiftKeyDown()) {
            save(stack, player);

            CustomPacketBuffer growingBuffer = new CustomPacketBuffer();

            growingBuffer.writeByte(1);

            growingBuffer.flip();
            NetworkHooks.openScreen((ServerPlayer) player, getInventory(player, player.getItemInHand(hand)), buf -> buf.writeBytes(growingBuffer));


            return InteractionResultHolder.success(player.getItemInHand(hand));

        } else if (!world.isClientSide && player.isShiftKeyDown()) {
            CompoundTag nbt = ModUtils.nbt(stack);
            boolean rod = !nbt.getBoolean("rod");
            nbt.putBoolean("rod", rod);

            if (rod) {
                IUCore.proxy.messagePlayer(
                        player,
                        ChatFormatting.GREEN + Localization.translate("message.text.mode_no_instrument") + ": "
                                + Localization.translate("message.leadbox.enable")
                );
            } else {
                IUCore.proxy.messagePlayer(
                        player,
                        ChatFormatting.RED + Localization.translate("message.text.mode_no_instrument") + ": "
                                + Localization.translate("message.leadbox.disable")
                );
            }
        }

        return InteractionResultHolder.pass(player.getItemInHand(hand));
    }

    public boolean canInsert(Player player, ItemStack stack, ItemStack stack1) {
        ItemStackLeadBox box = (ItemStackLeadBox) getInventory(player, stack);
        CompoundTag nbt = ModUtils.nbt(stack);
        boolean rod = nbt.getBoolean("rod");
        if (stack1.getItem() instanceof IRadioactiveItemType) {
            if (!rod) {
                return !(stack1.getItem() instanceof ItemBaseRod);
            } else {
                return box.canAdd(stack1);
            }
        }
        return false;
    }

    public void insert(Player player, ItemStack stack, ItemStack stack1) {
        ItemStackLeadBox box = (ItemStackLeadBox) getInventory(player, stack);
        box.add(stack1);
        box.setChanged();
    }
}
