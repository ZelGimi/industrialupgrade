package com.denfop.network.packet;


import com.denfop.IUCore;
import com.denfop.blockentity.storage.BlockEntityFluidMonitor;
import com.denfop.blockentity.storage.BlockEntityMonitor;
import com.denfop.blockentity.storage.BlockEntityPatternMonitor;
import com.denfop.network.DecoderHandler;
import com.denfop.network.EncoderHandler;
import net.minecraft.core.BlockPos;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;

import java.io.IOException;
import java.util.UUID;

public class PacketUpdateMonitor implements IPacket {

    private CustomPacketBuffer buffer;

    public PacketUpdateMonitor() {

    }


    public PacketUpdateMonitor(Player player, BlockPos pos, int type) {
        CustomPacketBuffer customPacketBuffer = new CustomPacketBuffer(player.registryAccess());
        customPacketBuffer.writeByte(getId());

        try {
            EncoderHandler.encode(customPacketBuffer, player.getUUID());
            EncoderHandler.encode(customPacketBuffer, pos);
            customPacketBuffer.writeInt(0);
            customPacketBuffer.writeInt(type);
        } catch (IOException ignored) {
        }
        this.buffer = customPacketBuffer;
        IUCore.network.getClient().sendPacket(this, customPacketBuffer);
    }

    public PacketUpdateMonitor(Player player, BlockPos pos, String s) {
        CustomPacketBuffer customPacketBuffer = new CustomPacketBuffer(player.registryAccess());
        customPacketBuffer.writeByte(getId());

        try {
            EncoderHandler.encode(customPacketBuffer, player.getUUID());
            EncoderHandler.encode(customPacketBuffer, pos);
            customPacketBuffer.writeInt(1);
            customPacketBuffer.writeString(s);
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
        return 122;
    }

    @Override
    public void readPacket(final CustomPacketBuffer customPacketBuffer, final Player entityPlayer) {
        try {
            UUID uuid = (UUID) DecoderHandler.decode(customPacketBuffer);
            BlockPos pos = (BlockPos) DecoderHandler.decode(customPacketBuffer);
            if (entityPlayer.getGameProfile().getId().equals(uuid)) {
                if (entityPlayer.level().getBlockEntity(pos) instanceof BlockEntityMonitor controller && controller.network != null) {
                    int typeRead = customPacketBuffer.readInt();
                    if (typeRead == 0) {
                        int type = customPacketBuffer.readInt();
                        if (type == 0) {
                            controller.sizeMode++;
                            if (controller.sizeMode > 2)
                                controller.sizeMode = 0;
                        }
                        if (type == 1) {
                            controller.decreasing++;
                            if (controller.decreasing >= 4)
                                controller.decreasing = 0;
                        }
                        if (type == 2) {
                            controller.viewMode++;
                            if (controller.viewMode >= 3)
                                controller.viewMode = 0;
                        }
                        if (type == 3) {
                            controller.fieldMode++;
                            if (controller.fieldMode >= 4)
                                controller.fieldMode = 0;
                        }
                    }
                }
                if (entityPlayer.level().getBlockEntity(pos) instanceof BlockEntityPatternMonitor controller && controller.network != null) {
                    int typeRead = customPacketBuffer.readInt();
                    if (typeRead == 0) {
                        int type = customPacketBuffer.readInt();
                        if (type == 0) {
                            controller.sizeMode++;
                            if (controller.sizeMode > 2)
                                controller.sizeMode = 0;
                        }
                        if (type == 1) {
                            controller.decreasing++;
                            if (controller.decreasing >= 4)
                                controller.decreasing = 0;
                        }
                        if (type == 2) {
                            controller.viewMode++;
                            if (controller.viewMode >= 3)
                                controller.viewMode = 0;
                        }
                        if (type >= 3 && type <= 6) {
                            controller.value1 = type - 3;
                        }
                        if (type >= 6 && type <= 9) {
                            controller.value = type - 6;
                        }
                        if (type == 10) {
                            int prev = controller.modeCraft;
                            controller.modeCraft++;
                            if (controller.modeCraft >= 2)
                                controller.modeCraft = 0;
                            if (prev == 1) {
                                for (int i = 0; i < 9; i++) {
                                    if (controller.inputItems.booleanList.get(i))
                                        controller.inputItems.set(i, ItemStack.EMPTY);
                                }
                                controller.updateCraft();

                            }
                        }
                        if (type == 11) {
                            controller.fieldMode++;
                            if (controller.fieldMode >= 4)
                                controller.fieldMode = 0;
                        }
                    } else {
                        controller.fieldString = customPacketBuffer.readString();

                    }

                }
                if (entityPlayer.level().getBlockEntity(pos) instanceof BlockEntityFluidMonitor controller && controller.network != null) {
                    int typeRead = customPacketBuffer.readInt();
                    if (typeRead == 0) {
                        int type = customPacketBuffer.readInt();
                        if (type == 0) {
                            controller.sizeMode++;
                            if (controller.sizeMode > 2)
                                controller.sizeMode = 0;
                        }
                        if (type == 1) {
                            controller.decreasing++;
                            if (controller.decreasing >= 4)
                                controller.decreasing = 0;
                        }
                        if (type == 2) {
                            controller.viewMode++;
                            if (controller.viewMode >= 3)
                                controller.viewMode = 0;
                        }
                        if (type == 3) {
                            controller.fieldMode++;
                            if (controller.fieldMode >= 4)
                                controller.fieldMode = 0;
                        }
                    }
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

