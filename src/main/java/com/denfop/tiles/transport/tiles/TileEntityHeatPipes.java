package com.denfop.tiles.transport.tiles;

import com.denfop.IUCore;
import com.denfop.IUItem;
import com.denfop.api.heat.IHeatAcceptor;
import com.denfop.api.heat.IHeatConductor;
import com.denfop.api.heat.IHeatEmitter;
import com.denfop.api.heat.IHeatTile;
import com.denfop.api.heat.InfoCable;
import com.denfop.api.heat.event.HeatTileLoadEvent;
import com.denfop.api.heat.event.HeatTileUnloadEvent;
import com.denfop.api.sytem.InfoTile;
import com.denfop.api.tile.IMultiTileBlock;
import com.denfop.blocks.BlockTileEntity;
import com.denfop.blocks.mechanism.BlockPipes;
import com.denfop.network.DecoderHandler;
import com.denfop.network.EncoderHandler;
import com.denfop.network.packet.CustomPacketBuffer;
import com.denfop.network.packet.PacketCableSound;
import com.denfop.tiles.transport.types.HeatType;
import com.denfop.tiles.transport.types.ICableItem;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.RayTraceResult;
import net.minecraftforge.common.MinecraftForge;

import java.io.IOException;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;


public class TileEntityHeatPipes extends TileEntityMultiCable implements IHeatConductor {


    public boolean addedToEnergyNet;
    protected HeatType cableType;
    int hashCodeSource;
    Map<EnumFacing, IHeatTile> energyHeatConductorMap = new HashMap<>();
    boolean updateConnect = false;
    List<InfoTile<IHeatTile>> validHeatReceivers = new LinkedList<>();
    private boolean needUpdate;
    private long id;
    private InfoCable typeColdCable;

    public TileEntityHeatPipes(HeatType cableType) {
        super(cableType);
        this.cableType = cableType;
        this.addedToEnergyNet = false;
    }


    public TileEntityHeatPipes() {
        super(HeatType.pipes);
        this.cableType = HeatType.pipes;
        this.addedToEnergyNet = false;
    }

    public static TileEntityHeatPipes delegate(HeatType cableType) {
        return new TileEntityHeatPipes(cableType);
    }

    public IMultiTileBlock getTeBlock() {
        return BlockPipes.pipes_iu;
    }

    public BlockTileEntity getBlock() {
        return IUItem.pipesblock;
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
    public void AddHeatTile(final IHeatTile tile, final EnumFacing dir) {
        if (!this.getWorld().isRemote) {
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
    public void RemoveHeatTile(final IHeatTile tile, final EnumFacing dir) {
        if (!this.getWorld().isRemote) {
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
    public Map<EnumFacing, IHeatTile> getHeatTiles() {
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

    public void readFromNBT(NBTTagCompound nbt) {
        super.readFromNBT(nbt);
        this.cableType = HeatType.values[nbt.getByte("cableType") & 255];
    }

    public NBTTagCompound writeToNBT(NBTTagCompound nbt) {
        super.writeToNBT(nbt);
        nbt.setByte("cableType", (byte) this.cableType.ordinal());
        return nbt;
    }

    public void onLoaded() {
        super.onLoaded();
        if (!this.getWorld().isRemote && !addedToEnergyNet) {
            this.energyHeatConductorMap.clear();
            this.validHeatReceivers.clear();
            MinecraftForge.EVENT_BUS.post(new HeatTileLoadEvent(this, this.getWorld()));
            this.addedToEnergyNet = true;
            this.updateConnectivity();

        }

    }

    @Override
    public void updateTileServer(final EntityPlayer var1, final double var2) {
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
        if (IUCore.proxy.isSimulating() && this.addedToEnergyNet) {
            MinecraftForge.EVENT_BUS.post(new HeatTileUnloadEvent(this, this.getWorld()));
            this.addedToEnergyNet = false;
        }


        super.onUnloaded();
    }


    public ItemStack getPickBlock(EntityPlayer player, RayTraceResult target) {
        return new ItemStack(IUItem.pipes, 1, this.cableType.ordinal());
    }


    public void updateConnectivity() {
        byte newConnectivity = 0;
        EnumFacing[] var4 = EnumFacing.VALUES;

        for (EnumFacing dir : var4) {
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


    public boolean wrenchCanRemove(EntityPlayer player) {
        return false;
    }

    public boolean acceptsHeatFrom(IHeatEmitter emitter, EnumFacing direction) {
        return !getBlackList().contains(direction);
    }

    public boolean emitsHeatTo(IHeatAcceptor receiver, EnumFacing direction) {
        return !getBlackList().contains(direction);
    }


    public double getConductorBreakdownHeat() {
        return this.cableType.capacity + 1;
    }

    public void removeConductor() {
        this.getWorld().setBlockToAir(this.pos);
        new PacketCableSound(this.getWorld(), this.pos,
                0.5F,
                2.6F + (world.rand.nextFloat() - world.rand.nextFloat()) * 0.8F
        );
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
    public TileEntity getTile() {
        return this;
    }


}
