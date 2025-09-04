package com.denfop.componets;

import com.denfop.api.energy.interfaces.Dual;
import com.denfop.api.energy.interfaces.EnergySink;
import com.denfop.api.energy.interfaces.EnergySource;
import com.denfop.api.energy.interfaces.EnergyTile;
import com.denfop.api.energy.networking.EnergyNetGlobal;
import com.denfop.blockentity.base.BlockEntityBase;
import com.denfop.network.packet.CustomPacketBuffer;
import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.ListTag;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.level.Level;

import java.io.IOException;
import java.util.LinkedList;
import java.util.List;
import java.util.Objects;

public class WirelessComponent extends AbstractComponent {

    boolean hasUpdate = false;
    List<Connect> connectList = new LinkedList<>();
    EnergySource energySource;
    private boolean isDual;

    public WirelessComponent(final BlockEntityBase parent) {
        super(parent);
    }

    public void setUpdate(final boolean hasUpdate) {
        this.hasUpdate = hasUpdate;
    }

    public boolean isHasUpdate() {
        return hasUpdate;
    }

    public void setEnergySource(final EnergySource energySource) {
        this.energySource = energySource;
        this.isDual = this.energySource instanceof Dual;
    }

    @Override
    public void onNetworkUpdate(final CustomPacketBuffer is) throws IOException {
        super.onNetworkUpdate(is);
        this.hasUpdate = is.readBoolean();
    }

    public void onContainerUpdate(ServerPlayer player) {
        CustomPacketBuffer buffer = new CustomPacketBuffer(16, player.registryAccess());
        buffer.writeBoolean(this.hasUpdate);
        buffer.flip();
        this.setNetworkUpdate(player, buffer);
    }

    @Override
    public CustomPacketBuffer updateComponent() {
        CustomPacketBuffer packetBuffer = super.updateComponent();
        packetBuffer.writeBoolean(this.hasUpdate);
        return packetBuffer;
    }

    @Override
    public boolean isServer() {
        return true;
    }

    @Override
    public void updateEntityServer() {
        super.updateEntityServer();
        if (hasUpdate) {
            if (energySource != null && energySource.isSource()) {
                if (this.energySource.canExtractEnergy() == 0) {
                    return;
                }
                for (Connect connect : connectList) {
                    if (this.energySource.canExtractEnergy() == 0) {
                        break;
                    }

                    if (connect.getTile() == null) {
                        connect.setTile(EnergyNetGlobal.instance.getTile(this.getParent().getLevel(), connect.getPos()));
                    }
                    if (this.isDual && connect.isDual()) {
                        continue;
                    }
                    if (connect.getTile() != null && connect.getTile() instanceof EnergySink) {
                        EnergySink sink = (EnergySink) connect.getTile();
                        double demanded = sink.getDemandedEnergy();
                        demanded = Math.min(demanded, this.energySource.canExtractEnergy());
                        sink.receiveEnergy(demanded);
                        energySource.extractEnergy(demanded);
                    }
                }
            }
        }
    }

    public void addConnect(BlockPos pos) {
        connectList.add(new Connect(pos, this.getParent().getLevel()));
    }

    public void removeConnect(BlockPos pos) {
        connectList.removeIf(connect -> connect.getPos().equals(pos));
    }

    @Override
    public CompoundTag writeToNbt() {
        CompoundTag nbtTagCompound = super.writeToNbt();
        ListTag nbtTagList = new ListTag();
        for (Connect connect : this.connectList) {
            CompoundTag nbt = new CompoundTag();
            nbt.putInt("x", connect.getPos().getX());
            nbt.putInt("y", connect.getPos().getY());
            nbt.putInt("z", connect.getPos().getZ());
            nbtTagList.add(nbt);
        }
        nbtTagCompound.put("connect", nbtTagList);
        return nbtTagCompound;
    }

    @Override
    public void readFromNbt(final CompoundTag nbt) {
        super.readFromNbt(nbt);
        ListTag contentList = nbt.getList("Items", 10);
        for (int i = 0; i < contentList.size(); ++i) {
            CompoundTag connectNbt = contentList.getCompound(i);
            Connect connect = new Connect(new BlockPos(connectNbt.getInt("x"), connectNbt.getInt("y"),
                    connectNbt.getInt("z")
            ));
            this.connectList.add(connect);

        }
        if (!connectList.isEmpty()) {
            setUpdate(true);
        }
    }

    public void removeAll() {
        this.connectList.clear();
    }

}


class Connect {

    private final BlockPos pos;
    private boolean isDual;
    private EnergyTile tile;

    public Connect(BlockPos pos, Level world) {
        this.tile = EnergyNetGlobal.instance.getTile(world, pos);
        this.isDual = this.tile instanceof Dual;
        this.pos = pos;
    }

    public Connect(BlockPos pos) {
        this.pos = pos;
    }

    public BlockPos getPos() {
        return pos;
    }

    @Override
    public boolean equals(final Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        Connect connect = (Connect) o;
        return Objects.equals(pos, connect.pos);
    }

    public boolean isDual() {
        return isDual;
    }

    @Override
    public int hashCode() {
        return Objects.hash(pos);
    }

    public EnergyTile getTile() {
        return tile;
    }

    public void setTile(final EnergyTile tile) {
        this.tile = tile;
        this.isDual = this.tile instanceof Dual;
    }

}

