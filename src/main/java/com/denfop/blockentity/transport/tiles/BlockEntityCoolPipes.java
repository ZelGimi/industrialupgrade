package com.denfop.blockentity.transport.tiles;

import com.denfop.api.blockentity.MultiBlockEntity;
import com.denfop.api.energy.networking.ConductorInfo;
import com.denfop.api.otherenergies.common.InfoTile;
import com.denfop.api.otherenergies.cool.*;
import com.denfop.api.otherenergies.cool.event.CoolTileLoadEvent;
import com.denfop.api.otherenergies.cool.event.CoolTileUnloadEvent;
import com.denfop.blockentity.transport.types.CoolType;
import com.denfop.blockentity.transport.types.ICableItem;
import com.denfop.network.DecoderHandler;
import com.denfop.network.EncoderHandler;
import com.denfop.network.packet.CustomPacketBuffer;
import com.denfop.utils.Localization;
import com.denfop.utils.ModUtils;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.neoforged.neoforge.common.NeoForge;

import java.io.IOException;
import java.util.*;


public class BlockEntityCoolPipes extends BlockEntityMultiCable implements ICoolConductor {


    private final ConductorInfo conductorCool;
    public boolean addedToEnergyNet;
    protected CoolType cableType;
    int hashCodeSource;
    Map<Direction, ICoolTile> energyCoolConductorMap = new HashMap<>();
    List<InfoTile<ICoolTile>> validColdReceivers = new LinkedList<>();
    boolean updateConnect = false;
    private boolean needUpdate;
    private long id;
    private InfoCable typeColdCable;

    public BlockEntityCoolPipes(CoolType cableType, MultiBlockEntity block, BlockPos pos, BlockState state) {
        super(cableType, block, pos, state);
        this.cableType = cableType;
        this.connectivity = 0;
        this.addedToEnergyNet = false;
        this.conductorCool = new ConductorInfo(pos, this);
    }

    @Override
    public ConductorInfo getCoolConductor() {
        return conductorCool;
    }

    @Override
    public void addInformation(ItemStack stack, List<String> info) {
        CoolType type = this.cableType;
        double capacity;
        capacity = type.capacity;
        info.add(Localization.translate("iu.transport.cold") + "-" + ModUtils.getString(capacity) + " °C");

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
        this.cableType = CoolType.values[nbt.getByte("cableType") & 255];
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
            updateConnect = true;
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
            updateConnect = true;
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
            NeoForge.EVENT_BUS.post(new CoolTileLoadEvent(this, this.getWorld()));
            this.addedToEnergyNet = true;
            this.updateConnectivity();

        }

    }

    @Override
    public void updateTileServer(final Player var1, final double var2) {
        super.updateTileServer(var1, var2);
        NeoForge.EVENT_BUS.post(new CoolTileUnloadEvent(this, this.getWorld()));
        this.needUpdate = true;
    }

    @Override
    public void updateEntityServer() {
        super.updateEntityServer();
        if (this.needUpdate) {
            this.energyCoolConductorMap.clear();
            this.validColdReceivers.clear();
            NeoForge.EVENT_BUS.post(new CoolTileLoadEvent(this, this.getWorld()));
            this.needUpdate = false;
            this.updateConnectivity();
        }
        if (updateConnect) {
            updateConnect = false;
            this.updateConnectivity();
        }
    }

    public void onUnloaded() {
        if (!this.getLevel().isClientSide && this.addedToEnergyNet) {
            NeoForge.EVENT_BUS.post(new CoolTileUnloadEvent(this, this.getWorld()));
            this.addedToEnergyNet = false;
        }


        super.onUnloaded();
    }

    public void onNeighborChange(BlockState neighbor, BlockPos neighborPos) {
        super.onNeighborChange(neighbor, neighborPos);
        if (!this.getWorld().isClientSide) {
            this.updateConnectivity();
        }

    }

    @Override
    public InfoCable getCoolCable() {
        return typeColdCable;
    }

    @Override
    public void setCoolCable(final InfoCable cable) {
        typeColdCable = cable;
    }


    public void updateConnectivity() {
        byte newConnectivity = 0;
        Direction[] var4 = Direction.values();

        for (Direction dir : var4) {
            newConnectivity = (byte) (newConnectivity << 1);
            ICoolTile tile = energyCoolConductorMap.get(dir);
            if (!this.getBlackList().contains(dir)) {
                if ((tile instanceof ICoolAcceptor && ((ICoolAcceptor) tile).acceptsCoolFrom(
                        this,
                        dir.getOpposite()
                ) || tile instanceof ICoolEmitter && ((ICoolEmitter) tile).emitsCoolTo(
                        this,
                        dir.getOpposite()
                )) && this.canInteractWith()) {
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

    public boolean acceptsCoolFrom(ICoolEmitter emitter, Direction direction) {
        return !getBlackList().contains(direction);
    }

    public boolean emitsCoolTo(ICoolAcceptor receiver, Direction direction) {
        return !getBlackList().contains(direction);
    }

    public boolean canInteractWith() {

        return true;
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
            cableType = CoolType.values[(int) DecoderHandler.decode(customPacketBuffer)];
            connectivity = (byte) DecoderHandler.decode(customPacketBuffer);
            this.rerender();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }


    @Override
    public BlockEntity getTile() {
        return this;
    }


}
