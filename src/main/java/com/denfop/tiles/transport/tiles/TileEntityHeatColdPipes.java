package com.denfop.tiles.transport.tiles;

import com.denfop.Localization;
import com.denfop.api.cool.ICoolAcceptor;
import com.denfop.api.cool.ICoolConductor;
import com.denfop.api.cool.ICoolEmitter;
import com.denfop.api.cool.ICoolTile;
import com.denfop.api.cool.event.CoolTileLoadEvent;
import com.denfop.api.cool.event.CoolTileUnloadEvent;
import com.denfop.api.heat.*;
import com.denfop.api.heat.event.HeatTileLoadEvent;
import com.denfop.api.heat.event.HeatTileUnloadEvent;
import com.denfop.api.sytem.InfoTile;
import com.denfop.api.tile.IMultiTileBlock;
import com.denfop.network.DecoderHandler;
import com.denfop.network.EncoderHandler;
import com.denfop.network.packet.CustomPacketBuffer;
import com.denfop.tiles.transport.types.HeatColdType;
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


public class TileEntityHeatColdPipes extends TileEntityMultiCable implements ICoolConductor, IHeatConductor {


    public boolean addedToEnergyNet;
    protected HeatColdType cableType;
    int hashCodeSource;
    Map<Direction, ICoolTile> energyCoolConductorMap = new HashMap<>();
    List<InfoTile<ICoolTile>> validColdReceivers = new LinkedList<>();
    Map<Direction, IHeatTile> energyHeatConductorMap = new HashMap<>();
    boolean updateConnect = false;
    List<InfoTile<IHeatTile>> validHeatReceivers = new LinkedList<>();
    private boolean needUpdate;
    private long id;
    private com.denfop.api.cool.InfoCable typeColdCable;
    private InfoCable typeHeatCable;

    public TileEntityHeatColdPipes(HeatColdType cableType, IMultiTileBlock tileBlock, BlockPos pos, BlockState state) {
        super(cableType, tileBlock, pos, state);
        this.cableType = cableType;
        this.connectivity = 0;
        this.addedToEnergyNet = false;
    }
    @Override
    public void addInformation(ItemStack stack, List<String> info) {
        HeatColdType type = this.cableType;
        double capacity;
        capacity = type.capacity;
        info.add(Localization.translate("iu.transport.cold") + "-" + ModUtils.getString(capacity) + " °C");
        info.add(Localization.translate("iu.transport.heat") + ModUtils.getString(type.capacity1) + " °C");


    }
    public ICableItem getCableItem() {
        return cableType;
    }

    @Override
    public BlockPos getBlockPos() {
        return this.pos;
    }

    public void readFromNBT(CompoundTag nbt) {
        super.readFromNBT(nbt);
        this.cableType = HeatColdType.values[nbt.getByte("cableType") & 255];
    }

    @Override
    public com.denfop.api.cool.InfoCable getCoolCable() {
        return typeColdCable;
    }

    @Override
    public void setCoolCable(final com.denfop.api.cool.InfoCable cable) {
        typeColdCable = cable;
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
    public void AddCoolTile(final ICoolTile tile, final Direction dir) {
        if (!this.getWorld().isClientSide) {
            if (!this.energyCoolConductorMap.containsKey(dir)) {
                this.energyCoolConductorMap.put(dir, tile);
                validColdReceivers.add(new InfoTile<>(tile, dir.getOpposite()));
            }
            this.updateConnect = true;
        }
    }

    @Override
    public void RemoveCoolTile(final ICoolTile tile, final Direction dir) {
        if (!this.getWorld().isClientSide) {
            this.energyCoolConductorMap.remove(dir);
            final Iterator<InfoTile<ICoolTile>> iter = validColdReceivers.iterator();
            while (iter.hasNext()) {
                InfoTile<ICoolTile> tileInfoTile = iter.next();
                if (tileInfoTile.tileEntity == tile) {
                    iter.remove();
                    break;
                }
            }
            this.updateConnect = true;
        }
    }

    @Override
    public Map<Direction, ICoolTile> getCoolTiles() {
        return energyCoolConductorMap;
    }

    @Override
    public List<InfoTile<ICoolTile>> getCoolValidReceivers() {
        return validColdReceivers;
    }

    public CompoundTag writeToNBT(CompoundTag nbt) {
        super.writeToNBT(nbt);
        nbt.putByte("cableType", (byte) this.cableType.ordinal());
        return nbt;
    }

    public void onLoaded() {
        super.onLoaded();
        if (!this.getWorld().isClientSide && !addedToEnergyNet) {

            this.energyCoolConductorMap.clear();
            this.validColdReceivers.clear();

            MinecraftForge.EVENT_BUS.post(new HeatTileLoadEvent(this, this.getWorld()));
            MinecraftForge.EVENT_BUS.post(new CoolTileLoadEvent(this, this.getWorld()));

            this.addedToEnergyNet = true;
            this.updateConnectivity();

        }

    }

    @Override
    public void updateTileServer(final Player var1, final double var2) {
        super.updateTileServer(var1, var2);
        MinecraftForge.EVENT_BUS.post(new HeatTileUnloadEvent(this, this.getWorld()));
        MinecraftForge.EVENT_BUS.post(new CoolTileUnloadEvent(this, this.getWorld()));
        this.needUpdate = true;
    }

    @Override
    public void updateEntityServer() {
        super.updateEntityServer();
        if (this.needUpdate) {
            this.energyCoolConductorMap.clear();
            this.validColdReceivers.clear();
            this.validHeatReceivers.clear();
            this.energyHeatConductorMap.clear();
            MinecraftForge.EVENT_BUS.post(new HeatTileLoadEvent(this, this.getWorld()));
            MinecraftForge.EVENT_BUS.post(new CoolTileLoadEvent(this, this.getWorld()));
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
            MinecraftForge.EVENT_BUS.post(new CoolTileUnloadEvent(this, this.getWorld()));

            this.addedToEnergyNet = false;
        }


        super.onUnloaded();
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
        return typeHeatCable;
    }

    @Override
    public void setHeatCable(final InfoCable cable) {
        typeHeatCable = cable;
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


    public void updateConnectivity() {
        byte newConnectivity = 0;
        Direction[] var4 = Direction.values();

        for (Direction dir : var4) {
            newConnectivity = (byte) (newConnectivity << 1);
            ICoolTile tile = energyCoolConductorMap.get(dir);
            if (!getBlackList().contains(dir)) {
                if (tile != null) {
                    if ((tile instanceof ICoolAcceptor && ((ICoolAcceptor) tile).acceptsCoolFrom(
                            this,
                            dir.getOpposite()
                    ) || tile instanceof ICoolEmitter && ((ICoolEmitter) tile).emitsCoolTo(
                            this,
                            dir.getOpposite()
                    )) && this.canInteractWith()) {
                        newConnectivity = (byte) (newConnectivity + 1);
                    }
                } else {
                    IHeatTile heatTile = energyHeatConductorMap.get(dir);

                    if ((heatTile instanceof IHeatAcceptor && ((IHeatAcceptor) heatTile).acceptsHeatFrom(
                            this,
                            dir.getOpposite()
                    ) || heatTile instanceof IHeatEmitter && ((IHeatEmitter) heatTile).emitsHeatTo(
                            this,
                            dir.getOpposite()
                    )) && this.canInteractWith()) {
                        newConnectivity = (byte) (newConnectivity + 1);
                    }

                }
            }
        }

        setConnectivity(newConnectivity);
        this.cableItem = cableType;
    }


    public boolean wrenchCanRemove(Player player) {
        return false;
    }


    public boolean canInteractWith() {

        return true;
    }

    @Override
    public double getConductorBreakdownHeat() {
        return this.cableType.capacity1 + 1;
    }

    public double getConductorBreakdownCold() {
        return this.cableType.capacity + 1;
    }


    @Override
    public void update_render() {
        if (!this.getWorld().isClientSide) {
            this.updateConnectivity();
        }
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
            cableType = HeatColdType.values[(int) DecoderHandler.decode(customPacketBuffer)];
            connectivity = (byte) DecoderHandler.decode(customPacketBuffer);
            this.rerender();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }


    @Override
    public boolean acceptsCoolFrom(final ICoolEmitter var1, final Direction var2) {
        return !getBlackList().contains(var2);
    }

    @Override
    public boolean emitsCoolTo(final ICoolAcceptor var1, final Direction var2) {
        return !getBlackList().contains(var2);
    }

    @Override
    public boolean acceptsHeatFrom(final IHeatEmitter var1, final Direction var2) {
        return !getBlackList().contains(var2);
    }

    @Override
    public boolean emitsHeatTo(final IHeatAcceptor var1, final Direction var2) {
        return !getBlackList().contains(var2);
    }

    @Override
    public BlockEntity getTile() {
        return this;
    }


}
