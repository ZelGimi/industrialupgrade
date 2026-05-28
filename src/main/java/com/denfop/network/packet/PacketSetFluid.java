package com.denfop.network.packet;


import com.denfop.IUCore;
import com.denfop.api.storage.autocrafting.SameStack;
import com.denfop.blockentity.storage.BlockEntityPatternMonitor;
import com.denfop.network.DecoderHandler;
import com.denfop.network.EncoderHandler;
import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;

import java.io.IOException;

public class PacketSetFluid implements IPacket {

    private CustomPacketBuffer buffer;

    public PacketSetFluid() {

    }

    public PacketSetFluid(BlockEntityPatternMonitor base, boolean input, int slotId, SameStack sameStack, boolean item_to_fluid) {
        CustomPacketBuffer customPacketBuffer = new CustomPacketBuffer(base.getWorld().registryAccess());
        customPacketBuffer.writeByte(getId());

        try {
            EncoderHandler.encode(customPacketBuffer, base.getLevel());
            EncoderHandler.encode(customPacketBuffer, base.getPos());
            customPacketBuffer.writeBoolean(input);
            customPacketBuffer.writeBoolean(item_to_fluid);
            customPacketBuffer.writeInt(slotId);
            EncoderHandler.encode(customPacketBuffer, sameStack.writeToNBT(base.getWorld().registryAccess()));
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
        return 124;
    }

    @Override
    public void readPacket(final CustomPacketBuffer customPacketBuffer, final Player entityPlayer) {
        try {
            Level level = (Level) DecoderHandler.decode(customPacketBuffer);
            BlockPos pos = (BlockPos) DecoderHandler.decode(customPacketBuffer);
            if (level.getBlockEntity(pos) instanceof BlockEntityPatternMonitor controller && controller.network != null) {
                boolean input = customPacketBuffer.readBoolean();
                boolean item_to_fluid = customPacketBuffer.readBoolean();
                int slotId = customPacketBuffer.readInt();
                SameStack sameStack = SameStack.readFromNBT((CompoundTag) DecoderHandler.decode(customPacketBuffer), controller.registryAccess());
                controller.modeCraft = 1;
                if (input) {
                    controller.inputItems.sameStackList.set(slotId, sameStack);
                    controller.inputItems.booleanList.set(slotId, item_to_fluid);
                } else {
                    controller.outputItems.sameStackList.set(slotId, sameStack);
                    controller.outputItems.booleanList.set(slotId, item_to_fluid);
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

