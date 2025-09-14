package com.denfop.network.packet;

import com.denfop.IUCore;
import com.denfop.network.DecoderHandler;
import com.denfop.network.EncoderHandler;
import com.denfop.world.vein.ChanceOre;
import com.denfop.world.vein.TypeVein;
import com.denfop.world.vein.VeinType;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;

import java.io.IOException;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import static com.denfop.datagen.loader.VeinDataLoader.VEIN_DATA;
import static com.denfop.items.ItemVeinSensor.dataColors;
import static com.denfop.world.WorldBaseGen.*;

public class PacketUpdateVeinData implements IPacket {



    public PacketUpdateVeinData() {

    }

    public PacketUpdateVeinData(ServerPlayer player) {
        CustomPacketBuffer buffer = new CustomPacketBuffer();
        buffer.writeByte(this.getId());
        buffer.writeInt(dataColors.size());
        for (Map.Entry<BlockState, Integer> entry : dataColors.entrySet()) {
            try {
                EncoderHandler.encode(buffer, entry.getKey().getBlock());
                EncoderHandler.encode(buffer, entry.getValue());
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }
        buffer.writeInt(VEIN_DATA.size());
        for (Map.Entry<ResourceLocation, VeinType> entry : VEIN_DATA.entrySet()) {
            try {
                EncoderHandler.encode(buffer, entry.getKey());
                EncoderHandler.encode(buffer, entry.getValue().getDeposits().getBlock());
                EncoderHandler.encode(buffer, entry.getValue().getOres().size());
                for (int i = 0; i < entry.getValue().getOres().size(); i++) {
                    ChanceOre chanceOre = entry.getValue().getOres().get(i);
                    EncoderHandler.encode(buffer, chanceOre.getBlock().getBlock());
                    EncoderHandler.encode(buffer, chanceOre.getChance());
                    EncoderHandler.encode(buffer, chanceOre.getMeta());
                }
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }
        IUCore.network.getServer().sendPacket(buffer, player);
    }




    @Override
    public byte getId() {
        return 75;
    }


    @Override
    public void readPacket(final CustomPacketBuffer customPacketBuffer, final Player entityPlayer) {

        try {
            int size = (int) customPacketBuffer.readInt();
            for (int i = 0; i < size; i++) {
                Block block = (Block) DecoderHandler.decode(customPacketBuffer);
                Integer id = (Integer) DecoderHandler.decode(customPacketBuffer);
                if (!dataColors.containsKey(block.defaultBlockState())) {
                    dataColors.put(block.defaultBlockState(), id);
                }
            }
            size = (int)  customPacketBuffer.readInt();
            for (int i = 0; i < size; i++) {
                ResourceLocation resourceLocation = (ResourceLocation) DecoderHandler.decode(customPacketBuffer);
                Block block = (Block) DecoderHandler.decode(customPacketBuffer);
                int size1 = (int) DecoderHandler.decode(customPacketBuffer);
                List<ChanceOre> list = new LinkedList<>();
                for (int i1 = 0; i1 < size1; i1++) {
                    Block block1 = (Block) DecoderHandler.decode(customPacketBuffer);
                    int chance = (int) DecoderHandler.decode(customPacketBuffer);
                    int meta = (int) DecoderHandler.decode(customPacketBuffer);
                    list.add(new ChanceOre(block1.defaultBlockState(), chance, meta));
                }
                if (!VEIN_DATA.containsKey(resourceLocation)) {
                    VeinType veinType = new VeinType(block.defaultBlockState(), TypeVein.SMALL, list);
                    VEIN_DATA.put(resourceLocation, veinType);
                    for (ChanceOre chanceOre : veinType.getOres()) {
                        BlockState state = chanceOre.getBlock();
                        if (!idToblockStateMap.containsKey(state.getBlock())) {
                            idToblockStateMap.put(state.getBlock(), id);
                            blockStateMap.put(id, state);
                            id++;
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
        return EnumTypePacket.SERVER;
    }

}
