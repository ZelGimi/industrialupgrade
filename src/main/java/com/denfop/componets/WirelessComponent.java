package com.denfop.componets;

import com.denfop.api.energy.EnergyNetGlobal;
import com.denfop.api.energy.IDual;
import com.denfop.api.energy.IEnergySink;
import com.denfop.api.energy.IEnergySource;
import com.denfop.api.energy.IEnergyTile;
import com.denfop.network.packet.CustomPacketBuffer;
import com.denfop.tiles.base.TileEntityBlock;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

import java.io.IOException;
import java.util.LinkedList;
import java.util.List;
import java.util.Objects;

public class WirelessComponent extends AbstractComponent {

    boolean hasUpdate = false;
    List<Connect> connectList = new LinkedList<>();
    IEnergySource energySource;
    private boolean isDual;

    public WirelessComponent(final TileEntityBlock parent) {
        super(parent);
    }

    public void setUpdate(final boolean hasUpdate) {
        this.hasUpdate = hasUpdate;
    }

    public boolean isHasUpdate() {
        return hasUpdate;
    }

    public void setEnergySource(final IEnergySource energySource) {
        this.energySource = energySource;
        this.isDual = this.energySource instanceof IDual;
    }

    @Override
    public void onNetworkUpdate(final CustomPacketBuffer is) throws IOException {
        super.onNetworkUpdate(is);
        this.hasUpdate = is.readBoolean();
    }

    public void onContainerUpdate(EntityPlayerMP player) {
        CustomPacketBuffer buffer = new CustomPacketBuffer(16);
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
                        connect.setTile(EnergyNetGlobal.instance.getTile(this.getParent().getWorld(), connect.getPos()));
                    }
                    if (this.isDual && connect.isDual()) {
                        continue;
                    }
                    if (connect.getTile() != null && connect.getTile() instanceof IEnergySink) {
                        IEnergySink sink = (IEnergySink) connect.getTile();
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
        connectList.add(new Connect(pos, this.getParent().getWorld()));
    }

    public void removeConnect(BlockPos pos) {
        connectList.removeIf(connect -> connect.getPos().equals(pos));
    }

    @Override
    public NBTTagCompound writeToNbt() {
        NBTTagCompound nbtTagCompound = super.writeToNbt();
        NBTTagList nbtTagList = new NBTTagList();
        for (Connect connect : this.connectList) {
            NBTTagCompound nbt = new NBTTagCompound();
            nbt.setInteger("x", connect.getPos().getX());
            nbt.setInteger("y", connect.getPos().getY());
            nbt.setInteger("z", connect.getPos().getZ());
            nbtTagList.appendTag(nbt);
        }
        nbtTagCompound.setTag("connect", nbtTagList);
        return nbtTagCompound;
    }

    @Override
    public void readFromNbt(final NBTTagCompound nbt) {
        super.readFromNbt(nbt);
        NBTTagList contentList = nbt.getTagList("Items", 10);
        for (int i = 0; i < contentList.tagCount(); ++i) {
            NBTTagCompound connectNbt = contentList.getCompoundTagAt(i);
            Connect connect = new Connect(new BlockPos(connectNbt.getInteger("x"), connectNbt.getInteger("y"),
                    connectNbt.getInteger("z")
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
    private IEnergyTile tile;

    public Connect(BlockPos pos, World world) {
        this.tile = EnergyNetGlobal.instance.getTile(world, pos);
        this.isDual = this.tile instanceof IDual;
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

    public IEnergyTile getTile() {
        return tile;
    }

    public void setTile(final IEnergyTile tile) {
        this.tile = tile;
        this.isDual = this.tile instanceof IDual;
    }

}

