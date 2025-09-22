package com.denfop.tiles.transport.tiles;

import com.denfop.IUCore;
import com.denfop.IUItem;
import com.denfop.api.cool.ICoolAcceptor;
import com.denfop.api.cool.ICoolConductor;
import com.denfop.api.cool.ICoolEmitter;
import com.denfop.api.cool.ICoolTile;
import com.denfop.api.cool.event.CoolTileLoadEvent;
import com.denfop.api.cool.event.CoolTileUnloadEvent;
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
import com.denfop.blocks.mechanism.BlockHeatColdPipes;
import com.denfop.network.DecoderHandler;
import com.denfop.network.EncoderHandler;
import com.denfop.network.packet.CustomPacketBuffer;
import com.denfop.network.packet.PacketCableSound;
import com.denfop.tiles.transport.types.HeatColdType;
import com.denfop.tiles.transport.types.ICableItem;
import net.minecraft.block.SoundType;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
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


public class TileEntityHeatColdPipes extends TileEntityMultiCable implements ICoolConductor, IHeatConductor {


    public boolean addedToEnergyNet;
    protected HeatColdType cableType;
    int hashCodeSource;
    Map<EnumFacing, ICoolTile> energyCoolConductorMap = new HashMap<>();
    List<InfoTile<ICoolTile>> validColdReceivers = new LinkedList<>();
    Map<EnumFacing, IHeatTile> energyHeatConductorMap = new HashMap<>();
    boolean updateConnect = false;
    List<InfoTile<IHeatTile>> validHeatReceivers = new LinkedList<>();
    private boolean needUpdate;
    private long id;
    private com.denfop.api.cool.InfoCable typeColdCable;
    private InfoCable typeHeatCable;

    public TileEntityHeatColdPipes(HeatColdType cableType) {
        super(cableType);
        this.cableType = cableType;
        this.connectivity = 0;
        this.addedToEnergyNet = false;
    }

    public TileEntityHeatColdPipes() {
        super(HeatColdType.heatcool);
        this.cableType = HeatColdType.heatcool;
        this.connectivity = 0;
        this.addedToEnergyNet = false;

    }

    public static TileEntityHeatColdPipes delegate(HeatColdType cableType) {
        return new TileEntityHeatColdPipes(cableType);
    }

    public static TileEntityHeatColdPipes delegate() {
        return new TileEntityHeatColdPipes();
    }

    public IMultiTileBlock getTeBlock() {
        return BlockHeatColdPipes.heatcold;
    }

    public BlockTileEntity getBlock() {
        return IUItem.heatcoolpipesblock;
    }

    public ICableItem getCableItem() {
        return cableType;
    }

    @Override
    public BlockPos getBlockPos() {
        return this.pos;
    }

    public void readFromNBT(NBTTagCompound nbt) {
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
    public void AddCoolTile(final ICoolTile tile, final EnumFacing dir) {
        if (!this.getWorld().isRemote) {
            if (!this.energyCoolConductorMap.containsKey(dir)) {
                this.energyCoolConductorMap.put(dir, tile);
                validColdReceivers.add(new InfoTile<>(tile, dir.getOpposite()));
            }
            this.updateConnect = true;
        }
    }

    @Override
    public void RemoveCoolTile(final ICoolTile tile, final EnumFacing dir) {
        if (!this.getWorld().isRemote) {
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
    public Map<EnumFacing, ICoolTile> getCoolTiles() {
        return energyCoolConductorMap;
    }

    @Override
    public List<InfoTile<ICoolTile>> getCoolValidReceivers() {
        return validColdReceivers;
    }

    public NBTTagCompound writeToNBT(NBTTagCompound nbt) {
        super.writeToNBT(nbt);
        nbt.setByte("cableType", (byte) this.cableType.ordinal());
        return nbt;
    }

    public void onLoaded() {
        super.onLoaded();
        if (!this.getWorld().isRemote && !addedToEnergyNet) {

            this.energyCoolConductorMap.clear();
            this.validColdReceivers.clear();

            MinecraftForge.EVENT_BUS.post(new HeatTileLoadEvent(this, this.getWorld()));
            MinecraftForge.EVENT_BUS.post(new CoolTileLoadEvent(this, this.getWorld()));

            this.addedToEnergyNet = true;
            this.updateConnectivity();

        }

    }

    @Override
    public void updateTileServer(final EntityPlayer var1, final double var2) {
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
        if (IUCore.proxy.isSimulating() && this.addedToEnergyNet) {
            MinecraftForge.EVENT_BUS.post(new HeatTileUnloadEvent(this, this.getWorld()));
            MinecraftForge.EVENT_BUS.post(new CoolTileUnloadEvent(this, this.getWorld()));

            this.addedToEnergyNet = false;
        }


        super.onUnloaded();
    }

    public SoundType getBlockSound(Entity entity) {
        return SoundType.CLOTH;
    }

    public void onPlaced(ItemStack stack, EntityLivingBase placer, EnumFacing facing) {
        super.onPlaced(stack, placer, facing);
    }

    public ItemStack getPickBlock(EntityPlayer player, RayTraceResult target) {
        return new ItemStack(IUItem.heatcold_pipes, 1, cableType.ordinal());
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
        return typeHeatCable;
    }

    @Override
    public void setHeatCable(final InfoCable cable) {
        typeHeatCable = cable;
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


    public void updateConnectivity() {
        byte newConnectivity = 0;
        EnumFacing[] var4 = EnumFacing.VALUES;

        for (EnumFacing dir : var4) {
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


    public boolean wrenchCanRemove(EntityPlayer player) {
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

    public void removeConductor() {
        this.getWorld().setBlockToAir(this.pos);
        new PacketCableSound(this.getWorld(), this.pos,
                0.5F,
                2.6F + (world.rand.nextFloat() - world.rand.nextFloat()) * 0.8F
        );
    }

    @Override
    public void update_render() {
        if (!this.getWorld().isRemote) {
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
    public boolean acceptsCoolFrom(final ICoolEmitter var1, final EnumFacing var2) {
        return !getBlackList().contains(var2);
    }

    @Override
    public boolean emitsCoolTo(final ICoolAcceptor var1, final EnumFacing var2) {
        return !getBlackList().contains(var2);
    }

    @Override
    public boolean acceptsHeatFrom(final IHeatEmitter var1, final EnumFacing var2) {
        return !getBlackList().contains(var2);
    }

    @Override
    public boolean emitsHeatTo(final IHeatAcceptor var1, final EnumFacing var2) {
        return !getBlackList().contains(var2);
    }

    @Override
    public TileEntity getTile() {
        return this;
    }


}
