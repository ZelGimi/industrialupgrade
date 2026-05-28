package com.denfop.network.packet;


import com.denfop.IUCore;
import com.denfop.api.storage.autocrafting.AutoCraftSystem;
import com.denfop.api.storage.autocrafting.AutoCraftSystemVisual;
import com.denfop.api.storage.autocrafting.SameStack;
import com.denfop.blockentity.storage.BlockEntityFluidMonitor;
import com.denfop.blockentity.storage.BlockEntityMonitor;
import com.denfop.blockentity.storage.BlockEntityPatternMonitor;
import com.denfop.network.DecoderHandler;
import com.denfop.network.EncoderHandler;
import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.entity.player.Player;

import java.io.IOException;
import java.util.UUID;

public class PacketAddAutoCraft implements IPacket {

    private CustomPacketBuffer buffer;

    public PacketAddAutoCraft() {

    }


    public PacketAddAutoCraft(Player player, BlockPos pos, SameStack stack) {
        CustomPacketBuffer customPacketBuffer = new CustomPacketBuffer(player.registryAccess());
        customPacketBuffer.writeByte(getId());

        try {
            EncoderHandler.encode(customPacketBuffer, player.getUUID());
            EncoderHandler.encode(customPacketBuffer, pos);
            EncoderHandler.encode(customPacketBuffer, stack.writeToNBT(player.registryAccess()));
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
        return 120;
    }

    @Override
    public void readPacket(final CustomPacketBuffer customPacketBuffer, final Player entityPlayer) {
        try {
            UUID uuid = (UUID) DecoderHandler.decode(customPacketBuffer);
            BlockPos pos = (BlockPos) DecoderHandler.decode(customPacketBuffer);
            if (entityPlayer.getGameProfile().getId().equals(uuid))
                if (entityPlayer.level().getBlockEntity(pos) instanceof BlockEntityMonitor controller && controller.network != null) {
                    SameStack stack = SameStack.readFromNBT((CompoundTag) DecoderHandler.decode(customPacketBuffer), entityPlayer.registryAccess());
                    AutoCraftSystemVisual visual = controller.network.createAutoCraftVisual(stack);
                    AutoCraftSystem autocraft = controller.network.createAutoCraft(visual);
                    controller.network.addAutoCraft(autocraft);
                }
            if (entityPlayer.level().getBlockEntity(pos) instanceof BlockEntityPatternMonitor controller && controller.network != null) {
                SameStack stack = SameStack.readFromNBT((CompoundTag) DecoderHandler.decode(customPacketBuffer), entityPlayer.registryAccess());
                AutoCraftSystemVisual visual = controller.network.createAutoCraftVisual(stack);
                AutoCraftSystem autocraft = controller.network.createAutoCraft(visual);
                controller.network.addAutoCraft(autocraft);
            }
            if (entityPlayer.level().getBlockEntity(pos) instanceof BlockEntityFluidMonitor controller && controller.network != null) {
                SameStack stack = SameStack.readFromNBT((CompoundTag) DecoderHandler.decode(customPacketBuffer), entityPlayer.registryAccess());
                AutoCraftSystemVisual visual = controller.network.createAutoCraftVisual(stack);
                AutoCraftSystem autocraft = controller.network.createAutoCraft(visual);
                controller.network.addAutoCraft(autocraft);
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

