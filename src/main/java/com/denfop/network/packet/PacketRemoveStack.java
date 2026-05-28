package com.denfop.network.packet;


import com.denfop.IUCore;
import com.denfop.blockentity.storage.BlockEntityFluidMonitor;
import com.denfop.blockentity.storage.BlockEntityMonitor;
import com.denfop.blockentity.storage.BlockEntityPatternMonitor;
import com.denfop.network.DecoderHandler;
import com.denfop.network.EncoderHandler;
import com.denfop.utils.FluidHandlerFix;
import net.minecraft.core.BlockPos;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.fluids.capability.IFluidHandler;
import net.minecraftforge.fluids.capability.IFluidHandlerItem;

import java.io.IOException;
import java.util.UUID;

public class PacketRemoveStack implements IPacket {

    public PacketRemoveStack() {

    }

    public PacketRemoveStack(Player player, FluidStack stack, Level level, BlockPos pos) {
        CustomPacketBuffer customPacketBuffer = new CustomPacketBuffer();
        customPacketBuffer.writeByte(getId());

        try {
            EncoderHandler.encode(customPacketBuffer, pos);
            EncoderHandler.encode(customPacketBuffer, level);
            EncoderHandler.encode(customPacketBuffer, player.getUUID());
            EncoderHandler.encode(customPacketBuffer, stack);
        } catch (IOException ignored) {
        }
        IUCore.network.getClient().sendPacket(customPacketBuffer);
    }

    public PacketRemoveStack(Player player, ItemStack stack, Level level, BlockPos pos) {
        CustomPacketBuffer customPacketBuffer = new CustomPacketBuffer();
        customPacketBuffer.writeByte(getId());

        try {
            EncoderHandler.encode(customPacketBuffer, pos);
            EncoderHandler.encode(customPacketBuffer, level);
            EncoderHandler.encode(customPacketBuffer, player.getUUID());
            EncoderHandler.encode(customPacketBuffer, stack);
        } catch (IOException ignored) {
        }
        IUCore.network.getClient().sendPacket(customPacketBuffer);
    }

    @Override
    public byte getId() {
        return 112;
    }

    @Override
    public void readPacket(final CustomPacketBuffer customPacketBuffer, final Player entityPlayer) {
        try {
            BlockPos pos = (BlockPos) DecoderHandler.decode(customPacketBuffer);
            Level level = (Level) DecoderHandler.decode(customPacketBuffer);
            UUID uuid = (UUID) DecoderHandler.decode(customPacketBuffer);

            if (entityPlayer.getUUID().equals(uuid))
                if (entityPlayer.getLevel().getBlockEntity(pos) instanceof BlockEntityMonitor controller && controller.network != null) {
                    ItemStack stack = (ItemStack) DecoderHandler.decode(customPacketBuffer);
                    int count = controller.network.canRemoveMonitor(stack);
                    stack.setCount(count);
                    controller.network.removeStack(stack);
                    entityPlayer.containerMenu.setCarried(stack);
                    new PackerUpdateClientRemoveStack(entityPlayer, stack);
                } else if (entityPlayer.getLevel().getBlockEntity(pos) instanceof BlockEntityPatternMonitor controller && controller.network != null) {
                    ItemStack stack = (ItemStack) DecoderHandler.decode(customPacketBuffer);
                    int count = controller.network.canRemoveMonitor(stack);
                    stack.setCount(count);
                    controller.network.removeStack(stack);
                    entityPlayer.containerMenu.setCarried(stack);
                    new PackerUpdateClientRemoveStack(entityPlayer, stack);
                } else if (entityPlayer.getLevel().getBlockEntity(pos) instanceof BlockEntityFluidMonitor controller && controller.network != null) {
                    FluidStack stack = (FluidStack) DecoderHandler.decode(customPacketBuffer);
                    int count = controller.network.canRemoveMonitor(stack);
                    stack.setAmount(count);
                    if (entityPlayer.containerMenu.getCarried().getCount() == 1) {
                        IFluidHandlerItem handlerFix = FluidHandlerFix.getFluidHandler(entityPlayer.containerMenu.getCarried());
                        handlerFix.fill(stack.copy(), IFluidHandler.FluidAction.EXECUTE);
                        controller.network.removeStack(stack);
                        entityPlayer.containerMenu.setCarried(entityPlayer.containerMenu.getCarried());
                        new PackerUpdateClientRemoveStack(entityPlayer, entityPlayer.containerMenu.getCarried());
                    } else {
                        int amount = entityPlayer.containerMenu.getCarried().getCount();
                        entityPlayer.containerMenu.getCarried().setCount(1);
                        IFluidHandlerItem handlerFix = FluidHandlerFix.getFluidHandler(entityPlayer.containerMenu.getCarried());
                        FluidStack fluidStack = stack.copy();
                        fluidStack.setAmount(fluidStack.getAmount() / amount);
                        handlerFix.fill(fluidStack, IFluidHandler.FluidAction.EXECUTE);
                        controller.network.removeStack(stack);
                        entityPlayer.containerMenu.getCarried().setCount(amount);
                        entityPlayer.containerMenu.setCarried(entityPlayer.containerMenu.getCarried());
                        new PackerUpdateClientRemoveStack(entityPlayer, entityPlayer.containerMenu.getCarried());
                    }
                }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

    }

    @Override
    public EnumTypePacket getPacketType() {
        return EnumTypePacket.CLIENT;
    }
}
