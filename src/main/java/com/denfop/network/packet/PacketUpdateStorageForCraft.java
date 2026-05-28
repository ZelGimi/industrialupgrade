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
import java.util.HashMap;
import java.util.Map;

public class PacketUpdateStorageForCraft implements IPacket {

    private CustomPacketBuffer buffer;

    public PacketUpdateStorageForCraft() {

    }

    public PacketUpdateStorageForCraft(Map<String, Map<SameStack, Integer>> stacksMapCount, BlockPos pos, Level level) {
        CustomPacketBuffer customPacketBuffer = new CustomPacketBuffer(level.registryAccess());
        customPacketBuffer.writeByte(getId());
        try {
            EncoderHandler.encode(customPacketBuffer, pos);
            EncoderHandler.encode(customPacketBuffer, level);
            writeStacksMapCount(customPacketBuffer, stacksMapCount);
        } catch (IOException ignored) {
        }

        this.buffer = customPacketBuffer;
        IUCore.network.getServer().sendPacket(this, customPacketBuffer);
    }

    private static void writeStacksMapCount(CustomPacketBuffer buf,
                                            Map<String, Map<SameStack, Integer>> map) throws IOException {
        if (map == null || map.isEmpty()) {
            buf.writeInt(0);
            return;
        }

        buf.writeInt(map.size());

        for (Map.Entry<String, Map<SameStack, Integer>> outer : map.entrySet()) {
            String key = outer.getKey();
            Map<SameStack, Integer> inner = outer.getValue();

            buf.writeUtf(key != null ? key : "");

            if (inner == null || inner.isEmpty()) {
                buf.writeInt(0);
                continue;
            }

            buf.writeInt(inner.size());

            for (Map.Entry<SameStack, Integer> in : inner.entrySet()) {
                SameStack ss = in.getKey();
                int count = in.getValue() == null ? 0 : in.getValue();
                boolean isFluid = ss != null && ss.isFluid();
                buf.writeBoolean(isFluid);

                if (!isFluid) {
                    ItemStack stack = (ss != null && ss.getStack() != null) ? ss.getStack().copy() : ItemStack.EMPTY;
                    EncoderHandler.encode(buf, stack);
                } else {
                    FluidStack stack = (ss != null && ss.getFluidStack() != null) ? ss.getFluidStack().copy() : FluidStack.EMPTY;
                    EncoderHandler.encode(buf, stack);
                }

                buf.writeInt(count);
            }
        }
    }

    private static Map<String, Map<SameStack, Integer>> readStacksMapCount(CustomPacketBuffer buf) throws IOException {
        int outerSize = buf.readInt();
        Map<String, Map<SameStack, Integer>> result = new HashMap<>(Math.max(outerSize, 0) * 2);

        for (int i = 0; i < outerSize; i++) {
            String key = buf.readUtf();
            int innerSize = buf.readInt();

            Map<SameStack, Integer> inner = new HashMap<>(Math.max(innerSize, 0) * 2);

            for (int j = 0; j < innerSize; j++) {
                boolean isFluid = buf.readBoolean();

                SameStack ss;
                if (!isFluid) {
                    ItemStack st = (ItemStack) DecoderHandler.decode(buf);
                    ss = new SameStack(st);
                } else {
                    FluidStack st = (FluidStack) DecoderHandler.decode(buf);
                    ss = new SameStack(st);
                }

                int count = buf.readInt();
                inner.put(ss, count);
            }

            result.put(key, inner);
        }

        return result;
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
        return 110;
    }

    @Override
    public void readPacket(final CustomPacketBuffer customPacketBuffer, final Player entityPlayer) {
        try {
            BlockPos pos = (BlockPos) DecoderHandler.decode(customPacketBuffer);
            Level level = (Level) DecoderHandler.decode(customPacketBuffer);
            if (entityPlayer.level().dimension() == level.dimension())
                if (entityPlayer.level().getBlockEntity(pos) instanceof Controller controller) {
                    Map<String, Map<SameStack, Integer>> stacksMapCount = readStacksMapCount(customPacketBuffer);
                    controller.setStackForCraft(stacksMapCount);
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
