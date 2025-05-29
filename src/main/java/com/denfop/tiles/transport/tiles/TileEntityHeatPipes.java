package com.denfop.tiles.transport.tiles;

import com.denfop.Localization;
import com.denfop.api.heat.*;
import com.denfop.api.heat.event.HeatTileLoadEvent;
import com.denfop.api.heat.event.HeatTileUnloadEvent;
import com.denfop.api.sytem.InfoTile;
import com.denfop.api.tile.IMultiTileBlock;
import com.denfop.network.DecoderHandler;
import com.denfop.network.EncoderHandler;
import com.denfop.network.packet.CustomPacketBuffer;
import com.denfop.tiles.transport.types.HeatType;
import com.denfop.tiles.transport.types.ICableItem;
import com.denfop.utils.ModUtils;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraftforge.common.MinecraftForge;

import java.io.IOException;
import java.util.*;


public class TileEntityHeatPipes extends TileEntityMultiCable implements IHeatConductor {


    public boolean addedToEnergyNet;
    protected HeatType cableType;
    int hashCodeSource;
    Map<Direction, IHeatTile> energyHeatConductorMap = new HashMap<>();
    boolean updateConnect = false;
    List<InfoTile<IHeatTile>> validHeatReceivers = new LinkedList<>();
    private boolean needUpdate;
    private long id;
    private InfoCable typeColdCable;

    public TileEntityHeatPipes(HeatType cableType, IMultiTileBlock tileBlock, BlockPos pos, BlockState state) {
        super(cableType, tileBlock, pos, state);
        this.cableType = cableType;
        this.addedToEnergyNet = false;
    }
    @Override
    public void addInformation(ItemStack stack, List<String> info) {
        HeatType type =this.cableType;
        double capacity;
        capacity = type.capacity;
        info.add(Localization.translate("iu.transport.heat") + ModUtils.getString(capacity) + " °C");


    }
    public ICableItem getCableItem() {
        return cableType;
    }

    public long getIdNetwork() {
        return this.id;
    }

    @Override
    public int getHashCodeSource() {
        return hashCodeSource;
    }

    @Override
    public void setHashCodeSource(final int hashCode) {
        hashCodeSource = hashCode;
    }

    public void setId(final long id) {
        this.id = id;
    }

    @Override
    public void AddHeatTile(final IHeatTile tile, final Direction dir) {
        if (!this.getWorld().isClientSide) {
            if (!this.energyHeatConductorMap.containsKey(dir)) {
                this.energyHeatConductorMap.put(dir, tile);
                validHeatReceivers.add(new InfoTile<>(tile, dir.getOpposite()));
            }
            updateConnect = true;
        }
    }

    @Override
    public InfoCable getHeatCable() {
        return typeColdCable;
    }

    @Override
    public void setHeatCable(final InfoCable cable) {
        typeColdCable = cable;
    }

    @Override
    public void RemoveHeatTile(final IHeatTile tile, final Direction dir) {
        if (!this.getWorld().isClientSide) {
            this.energyHeatConductorMap.remove(dir);
            final Iterator<InfoTile<IHeatTile>> iter = validHeatReceivers.iterator();
            while (iter.hasNext()) {
                InfoTile<IHeatTile> tileInfoTile = iter.next();
                if (tileInfoTile.tileEntity == tile) {
                    iter.remove();
                    break;
                }
            }
            updateConnect = true;
        }
    }

    @Override
    public Map<Direction, IHeatTile> getHeatTiles() {
        return energyHeatConductorMap;
    }

    @Override
    public List<InfoTile<IHeatTile>> getHeatValidReceivers() {
        return validHeatReceivers;
    }


    @Override
    public BlockPos getBlockPos() {
        return this.pos;
    }

    public void readFromNBT(CompoundTag nbt) {
        super.readFromNBT(nbt);
        this.cableType = HeatType.values[nbt.getByte("cableType") & 255];
    }

    public CompoundTag writeToNBT(CompoundTag nbt) {
        super.writeToNBT(nbt);
        nbt.putByte("cableType", (byte) this.cableType.ordinal());
        return nbt;
    }

    public void onLoaded() {
        super.onLoaded();
        if (!this.getWorld().isClientSide && !addedToEnergyNet) {
            this.energyHeatConductorMap.clear();
            this.validHeatReceivers.clear();
            MinecraftForge.EVENT_BUS.post(new HeatTileLoadEvent(this, this.getWorld()));
            this.addedToEnergyNet = true;
            this.updateConnectivity();

        }

    }

    @Override
    public void updateTileServer(final Player var1, final double var2) {
        super.updateTileServer(var1, var2);
        MinecraftForge.EVENT_BUS.post(new HeatTileUnloadEvent(this, this.getWorld()));
        this.needUpdate = true;
    }

    @Override
    public void updateEntityServer() {
        super.updateEntityServer();
        if (this.needUpdate) {
            this.energyHeatConductorMap.clear();
            this.validHeatReceivers.clear();
            MinecraftForge.EVENT_BUS.post(new HeatTileLoadEvent(this, this.getWorld()));
            this.needUpdate = false;
            this.updateConnectivity();
        }
        if (updateConnect) {
            updateConnect = false;
            this.updateConnectivity();
        }
    }

    public void onUnloaded() {
        if (!getLevel().isClientSide && this.addedToEnergyNet) {
            MinecraftForge.EVENT_BUS.post(new HeatTileUnloadEvent(this, this.getWorld()));
            this.addedToEnergyNet = false;
        }


        super.onUnloaded();
    }


    public void updateConnectivity() {
        byte newConnectivity = 0;
        Direction[] var4 = Direction.values();

        for (Direction dir : var4) {
            newConnectivity = (byte) (newConnectivity << 1);
            IHeatTile tile = getHeatTiles().get(dir);
            if (!getBlackList().contains(dir)) {
                if ((tile instanceof IHeatAcceptor && ((IHeatAcceptor) tile).acceptsHeatFrom(
                        this,
                        dir.getOpposite()
                ) || tile instanceof IHeatEmitter && ((IHeatEmitter) tile).emitsHeatTo(
                        this,
                        dir.getOpposite()
                ))) {
                    newConnectivity = (byte) (newConnectivity + 1);
                }
            }
        }

        setConnectivity(newConnectivity);
        this.cableItem = cableType;
    }


    public boolean wrenchCanRemove(Player player) {
        return false;
    }

    public boolean acceptsHeatFrom(IHeatEmitter emitter, Direction direction) {
        return !getBlackList().contains(direction);
    }

    public boolean emitsHeatTo(IHeatAcceptor receiver, Direction direction) {
        return !getBlackList().contains(direction);
    }


    public double getConductorBreakdownHeat() {
        return this.cableType.capacity + 1;
    }


    @Override
    public void update_render() {
        this.updateConnectivity();
    }


    public CustomPacketBuffer writePacket() {
        final CustomPacketBuffer packet = super.writePacket();
        try {
            EncoderHandler.encode(packet, cableType);
            EncoderHandler.encode(packet, connectivity);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        return packet;
    }

    public void readPacket(CustomPacketBuffer customPacketBuffer) {
        super.readPacket(customPacketBuffer);
        try {
            cableType = HeatType.values()[(int) DecoderHandler.decode(customPacketBuffer)];
            connectivity = (byte) DecoderHandler.decode(customPacketBuffer);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }


    @Override
    public BlockEntity getTile() {
        return this;
    }


}
