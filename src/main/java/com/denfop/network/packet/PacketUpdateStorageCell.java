package com.denfop.network.packet;

import com.denfop.IUCore;
import com.denfop.api.storage.Controller;
import com.denfop.api.storage.autocrafting.SameStack;
import com.denfop.network.DecoderHandler;
import com.denfop.network.EncoderHandler;
import net.minecraft.core.BlockPos;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.neoforged.neoforge.fluids.FluidStack;

import java.io.IOException;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

public class PacketUpdateStorageCell implements IPacket {

    private CustomPacketBuffer buffer;

    public PacketUpdateStorageCell() {

    }

    public PacketUpdateStorageCell(List<SameStack> stacks, boolean isFluid, boolean canCreate, BlockPos pos, Level level) {
        CustomPacketBuffer customPacketBuffer = new CustomPacketBuffer(level.registryAccess());
        customPacketBuffer.writeByte(getId());
        try {
            EncoderHandler.encode(customPacketBuffer, pos);
            EncoderHandler.encode(customPacketBuffer, level);
        } catch (IOException ignored) {
        }
        customPacketBuffer.writeBoolean(isFluid);
        customPacketBuffer.writeBoolean(canCreate);
        customPacketBuffer.writeInt(stacks.size());

        for (SameStack stack : stacks) {
            try {
                if (!isFluid)
                    EncoderHandler.encode(customPacketBuffer, stack.getStack());
                else
                    EncoderHandler.encode(customPacketBuffer, stack.getFluidStack());
            } catch (IOException ignored) {
            }
        }
        this.buffer = customPacketBuffer;
        IUCore.network.getServer().sendPacket(this, customPacketBuffer);
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
        return 117;
    }

    @Override
    public void readPacket(final CustomPacketBuffer customPacketBuffer, final Player entityPlayer) {
        try {
            BlockPos pos = (BlockPos) DecoderHandler.decode(customPacketBuffer);
            Level level = (Level) DecoderHandler.decode(customPacketBuffer);
            if (entityPlayer.level().dimension() == level.dimension())
                if (entityPlayer.level().getBlockEntity(pos) instanceof Controller controller) {
                    boolean isFluid = customPacketBuffer.readBoolean();
                    boolean isCreate = customPacketBuffer.readBoolean();
                    int size = customPacketBuffer.readInt();
                    List<SameStack> stacks = new LinkedList<>();
                    for (int i = 0; i < size; i++) {
                        try {
                            if (!isFluid)
                                stacks.add(new SameStack((ItemStack) DecoderHandler.decode(customPacketBuffer)));
                            else
                                stacks.add(new SameStack((FluidStack) DecoderHandler.decode(customPacketBuffer)));
                        } catch (IOException ignored) {
                        }
                    }
                    controller.setStack(isFluid, isCreate, new ArrayList<>(stacks));
                }

        } catch (IOException e) {
            throw new RuntimeException(e);
        }

    }

    @Override
    public EnumTypePacket getPacketType() {
        return EnumTypePacket.SERVER;
    }
}
