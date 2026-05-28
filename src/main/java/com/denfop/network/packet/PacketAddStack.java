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
import net.neoforged.neoforge.fluids.FluidStack;
import net.neoforged.neoforge.fluids.capability.IFluidHandler;

import java.io.IOException;
import java.util.UUID;

public class PacketAddStack implements IPacket {
    private CustomPacketBuffer buffer;

    public PacketAddStack() {
    }

    public PacketAddStack(Player player, ItemStack stack, BlockPos pos) {
        CustomPacketBuffer customPacketBuffer = new CustomPacketBuffer(player.registryAccess());
        customPacketBuffer.writeByte(getId());
        try {
            EncoderHandler.encode(customPacketBuffer, pos);
            EncoderHandler.encode(customPacketBuffer, player.getUUID());
            EncoderHandler.encode(customPacketBuffer, stack);
        } catch (IOException ignored) {
        }
        this.buffer = customPacketBuffer;
        IUCore.network.getClient().sendPacket(this, customPacketBuffer);
    }

    public PacketAddStack(Player player, FluidStack stack, BlockPos pos) {
        CustomPacketBuffer customPacketBuffer = new CustomPacketBuffer(player.registryAccess());
        customPacketBuffer.writeByte(getId());
        try {
            EncoderHandler.encode(customPacketBuffer, pos);
            EncoderHandler.encode(customPacketBuffer, player.getUUID());
            EncoderHandler.encode(customPacketBuffer, stack);
        } catch (IOException ignored) {
        }
        this.buffer = customPacketBuffer;
        IUCore.network.getClient().sendPacket(this, customPacketBuffer);
    }

    @Override
    public CustomPacketBuffer getPacketBuffer() {
        return buffer;
    }

    @Override
    public void setPacketBuffer(CustomPacketBuffer customPacketBuffer) {
        buffer = customPacketBuffer;
    }

    @Override
    public byte getId() {
        return 114;
    }

    @Override
    public void readPacket(final CustomPacketBuffer customPacketBuffer, final Player entityPlayer) {
        try {
            BlockPos pos = (BlockPos) DecoderHandler.decode(customPacketBuffer);
            UUID uuid = (UUID) DecoderHandler.decode(customPacketBuffer);
            if (entityPlayer.getUUID().equals(uuid))
                if (entityPlayer.level().getBlockEntity(pos) instanceof BlockEntityMonitor controller && controller.network != null) {
                    ItemStack stack = (ItemStack) DecoderHandler.decode(customPacketBuffer);
                    int count = controller.network.canAdd(stack);
                    if (count > 0) {
                        stack.setCount(count);
                        controller.network.addStack(stack.getItem(), stack.getComponents(), count);
                        int count1 = entityPlayer.containerMenu.getCarried().getCount() - count;
                        entityPlayer.containerMenu.setCarried(entityPlayer.containerMenu.getCarried().split(count1));
                        new PackerUpdateClientAddStack(entityPlayer, stack);
                    }
                }
            if (entityPlayer.level().getBlockEntity(pos) instanceof BlockEntityPatternMonitor controller && controller.network != null) {
                ItemStack stack = (ItemStack) DecoderHandler.decode(customPacketBuffer);
                int count = controller.network.canAdd(stack);
                if (count > 0) {
                    stack.setCount(count);
                    controller.network.addStack(stack.getItem(), stack.getComponents(), count);
                    int count1 = entityPlayer.containerMenu.getCarried().getCount() - count;
                    entityPlayer.containerMenu.setCarried(entityPlayer.containerMenu.getCarried().split(count1));
                    new PackerUpdateClientAddStack(entityPlayer, stack);
                }
            }
            if (entityPlayer.level().getBlockEntity(pos) instanceof BlockEntityFluidMonitor controller && controller.network != null) {
                FluidStack stack = (FluidStack) DecoderHandler.decode(customPacketBuffer);
                int count = controller.network.canAdd(stack);
                if (count > 0) {
                    stack.setAmount(count);
                    controller.network.addStack(stack.getFluid(), stack.getComponents(), count);
                    ItemStack stackCell = entityPlayer.containerMenu.getCarried();
                    int amount = stackCell.getCount();
                    stackCell.setCount(1);
                    stack.setAmount(count / amount);
                    FluidHandlerFix.getFluidHandler(stackCell).drain(stack, IFluidHandler.FluidAction.EXECUTE);
                    entityPlayer.containerMenu.setCarried(stackCell);
                    stackCell.setCount(amount);
                    new PackerUpdateClientAddStack(entityPlayer, stackCell);
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