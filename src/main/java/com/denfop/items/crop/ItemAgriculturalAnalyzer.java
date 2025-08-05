package com.denfop.items.crop;

import com.denfop.IItemTab;
import com.denfop.IUCore;
import com.denfop.Localization;
import com.denfop.api.inv.IAdvInventory;
import com.denfop.container.ContainerAgriculturalAnalyzer;
import com.denfop.items.IItemStackInventory;
import com.denfop.network.packet.CustomPacketBuffer;
import com.denfop.utils.ModUtils;
import net.minecraft.Util;
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
import net.minecraft.world.level.ClipContext;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraftforge.network.NetworkHooks;
import org.jetbrains.annotations.Nullable;

import javax.annotation.Nonnull;
import java.util.List;

public class ItemAgriculturalAnalyzer extends Item implements IItemStackInventory, IItemTab {
    private final int slots;
    private String nameItem;

    public ItemAgriculturalAnalyzer() {
        super(new Properties().stacksTo(1));
        this.slots = 1;
    }

    @Override
    public void appendHoverText(ItemStack pStack, @Nullable Level pLevel, List<Component> pTooltipComponents, TooltipFlag pIsAdvanced) {
        super.appendHoverText(pStack, pLevel, pTooltipComponents, pIsAdvanced);
        pTooltipComponents.add(Component.literal(Localization.translate("iu.crop_analyzer.info")));
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
        return new ItemStackAgriculturalAnalyzer(player, stack, 1);
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
            if (slotId != itemSlot && !world.isClientSide && !stack.isEmpty() && player.containerMenu instanceof ContainerAgriculturalAnalyzer) {
                ItemStackAgriculturalAnalyzer toolbox = ((ContainerAgriculturalAnalyzer) player.containerMenu).base;
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
        if (!player.level().isClientSide && !stack.isEmpty() && player.containerMenu instanceof ContainerAgriculturalAnalyzer) {
            ItemStackAgriculturalAnalyzer toolbox = ((ContainerAgriculturalAnalyzer) player.containerMenu).base;
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

    public boolean canInsert(Player player, ItemStack stack, ItemStack stack1) {

        return true;
    }


    @Override
    public void fillItemCategory(CreativeModeTab p_41391_, NonNullList<ItemStack> p_41392_) {
        if (allowedIn(p_41391_))
            p_41392_.add(new ItemStack(this));
    }

    @Override
    public CreativeModeTab getItemCategory() {
        return IUCore.EnergyTab;
    }
}
