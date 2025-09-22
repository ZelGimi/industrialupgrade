package com.denfop.tiles.transport.tiles;

import com.denfop.IUCore;
import com.denfop.IUItem;
import com.denfop.api.cool.ICoolAcceptor;
import com.denfop.api.cool.ICoolConductor;
import com.denfop.api.cool.ICoolEmitter;
import com.denfop.api.cool.ICoolTile;
import com.denfop.api.cool.event.CoolTileLoadEvent;
import com.denfop.api.cool.event.CoolTileUnloadEvent;
import com.denfop.api.sytem.InfoTile;
import com.denfop.api.tile.IMultiTileBlock;
import com.denfop.blocks.BlockTileEntity;
import com.denfop.blocks.mechanism.BlockCoolPipes;
import com.denfop.network.DecoderHandler;
import com.denfop.network.EncoderHandler;
import com.denfop.network.packet.CustomPacketBuffer;
import com.denfop.network.packet.PacketCableSound;
import com.denfop.tiles.transport.types.CoolType;
import com.denfop.tiles.transport.types.ICableItem;
import net.minecraft.block.Block;
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


public class TileEntityCoolPipes extends TileEntityMultiCable implements ICoolConductor {


    public boolean addedToEnergyNet;
    protected CoolType cableType;
    int hashCodeSource;
    Map<EnumFacing, ICoolTile> energyCoolConductorMap = new HashMap<>();
    List<InfoTile<ICoolTile>> validColdReceivers = new LinkedList<>();
    boolean updateConnect = false;
    private boolean needUpdate;
    private long id;
    private com.denfop.api.cool.InfoCable typeColdCable;

    public TileEntityCoolPipes(CoolType cableType) {
        super(cableType);
        this.cableType = cableType;
        this.connectivity = 0;
        this.addedToEnergyNet = false;
    }

    public TileEntityCoolPipes() {
        super(CoolType.cool);
        this.cableType = CoolType.cool;
        this.connectivity = 0;
        this.addedToEnergyNet = false;

    }

    public static TileEntityCoolPipes delegate(CoolType cableType) {
        return new TileEntityCoolPipes(cableType);
    }

    public IMultiTileBlock getTeBlock() {
        return BlockCoolPipes.cool_pipes_iu;
    }

    public BlockTileEntity getBlock() {
        return IUItem.coolpipesblock;
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
    public void AddCoolTile(final ICoolTile tile, final EnumFacing dir) {
        if (!this.getWorld().isRemote) {
            if (!this.energyCoolConductorMap.containsKey(dir)) {
                this.energyCoolConductorMap.put(dir, tile);
                validColdReceivers.add(new InfoTile<>(tile, dir.getOpposite()));
            }
            updateConnect = true;
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
            updateConnect = true;
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
            MinecraftForge.EVENT_BUS.post(new CoolTileLoadEvent(this, this.getWorld()));
            this.addedToEnergyNet = true;
            this.updateConnectivity();

        }

    }

    @Override
    public void updateTileServer(final EntityPlayer var1, final double var2) {
        super.updateTileServer(var1, var2);
        MinecraftForge.EVENT_BUS.post(new CoolTileUnloadEvent(this, this.getWorld()));
        this.needUpdate = true;
    }

    @Override
    public void updateEntityServer() {
        super.updateEntityServer();
        if (this.needUpdate) {
            this.energyCoolConductorMap.clear();
            this.validColdReceivers.clear();
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
        return new ItemStack(IUItem.coolpipes, 1, this.cableType.ordinal());
    }

    public void onNeighborChange(Block neighbor, BlockPos neighborPos) {
        super.onNeighborChange(neighbor, neighborPos);
        if (!this.getWorld().isRemote) {
            this.updateConnectivity();
        }

    }

    @Override
    public com.denfop.api.cool.InfoCable getCoolCable() {
        return typeColdCable;
    }

    @Override
    public void setCoolCable(final com.denfop.api.cool.InfoCable cable) {
        typeColdCable = cable;
    }


    public void updateConnectivity() {
        byte newConnectivity = 0;
        EnumFacing[] var4 = EnumFacing.VALUES;

        for (EnumFacing dir : var4) {
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

    public boolean wrenchCanRemove(EntityPlayer player) {
        return false;
    }

    public boolean acceptsCoolFrom(ICoolEmitter emitter, EnumFacing direction) {
        return !getBlackList().contains(direction);
    }

    public boolean emitsCoolTo(ICoolAcceptor receiver, EnumFacing direction) {
        return !getBlackList().contains(direction);
    }

    public boolean canInteractWith() {

        return true;
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
            cableType = CoolType.values[(int) DecoderHandler.decode(customPacketBuffer)];
            connectivity = (byte) DecoderHandler.decode(customPacketBuffer);
            this.rerender();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }


    @Override
    public TileEntity getTile() {
        return this;
    }


}
