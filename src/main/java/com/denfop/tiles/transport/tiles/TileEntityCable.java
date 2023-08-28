package com.denfop.tiles.transport.tiles;

import com.denfop.IUCore;
import com.denfop.IUItem;
import com.denfop.api.energy.*;
import com.denfop.api.energy.event.EnergyTileLoadEvent;
import com.denfop.api.energy.event.EnergyTileUnLoadEvent;
import com.denfop.api.tile.IMultiTileBlock;
import com.denfop.blocks.BlockTileEntity;
import com.denfop.blocks.mechanism.BlockCable;
import com.denfop.network.DecoderHandler;
import com.denfop.network.EncoderHandler;
import com.denfop.network.packet.CustomPacketBuffer;
import com.denfop.network.packet.PacketCableSound;
import com.denfop.tiles.transport.types.CableType;
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
import net.minecraft.world.World;
import net.minecraftforge.common.MinecraftForge;

import java.io.IOException;


public class TileEntityCable extends TileEntityMultiCable implements IEnergyConductor {


    public boolean addedToEnergyNet;
    public int type;
    protected CableType cableType;

    public TileEntityCable(CableType cableType) {
        super(cableType);
        this.addedToEnergyNet = false;
        this.cableType = cableType;
        this.type = cableType.ordinal();
        this.active = this.cableType.name();
    }

    public TileEntityCable() {
        super(CableType.glass);
        this.addedToEnergyNet = false;
        this.cableType = CableType.glass;
        this.type = cableType.ordinal();
        this.active = this.cableType.name();
    }

    public static TileEntityCable delegate(CableType cableType) {
        return new TileEntityCable(cableType);
    }

    public IMultiTileBlock getTeBlock() {
        return BlockCable.cable_iu;
    }

    public BlockTileEntity getBlock() {
        return IUItem.cableblock;
    }

    public ICableItem getCableItem() {
        return cableType;
    }

    public void readFromNBT(NBTTagCompound nbt) {
        super.readFromNBT(nbt);
        this.cableType = CableType.values[nbt.getByte("cableType") & 255];
    }

    public NBTTagCompound writeToNBT(NBTTagCompound nbt) {
        super.writeToNBT(nbt);
        nbt.setByte("cableType", (byte) this.cableType.ordinal());
        return nbt;
    }

    public void onLoaded() {
        super.onLoaded();
        if (!this.getWorld().isRemote) {
            MinecraftForge.EVENT_BUS.post(new EnergyTileLoadEvent(this.getWorld(), this, this));
            this.addedToEnergyNet = true;
            this.updateConnectivity();

        }

    }

    public void onUnloaded() {
        if (IUCore.proxy.isSimulating() && this.addedToEnergyNet) {
            MinecraftForge.EVENT_BUS.post(new EnergyTileUnLoadEvent(this.getWorld(), this));
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
        return new ItemStack(IUItem.cable, 1, this.cableType.ordinal());
    }

    @Override
    public ItemStack getItem(final EntityPlayer player, final RayTraceResult target) {
        return new ItemStack(IUItem.cable, 1, this.cableType.ordinal());
    }

    public void onNeighborChange(Block neighbor, BlockPos neighborPos) {
        super.onNeighborChange(neighbor, neighborPos);
        if (!this.getWorld().isRemote) {
            this.updateConnectivity();
        }

    }


    private void updateConnectivity() {
        World world = this.getWorld();
        byte newConnectivity = 0;
        EnumFacing[] var4 = EnumFacing.VALUES;

        for (EnumFacing dir : var4) {
            newConnectivity = (byte) (newConnectivity << 1);
            IEnergyTile tile = EnergyNetGlobal.instance.getTile(world, this.pos.offset(dir));

            if ((tile instanceof IEnergyAcceptor && ((IEnergyAcceptor) tile).acceptsEnergyFrom(
                    this,
                    dir.getOpposite()
            ) || tile instanceof IEnergyEmitter && ((IEnergyEmitter) tile).emitsEnergyTo(
                    this,
                    dir.getOpposite()
            ))) {
                newConnectivity = (byte) (newConnectivity + 1);
            }


        }
        setConnectivity(newConnectivity);

    }

    public boolean wrenchCanRemove(EntityPlayer player) {
        return false;
    }

    public boolean acceptsEnergyFrom(IEnergyEmitter emitter, EnumFacing direction) {
        return true;
    }

    public boolean emitsEnergyTo(IEnergyAcceptor receiver, EnumFacing direction) {
        return true;
    }


    public double getConductionLoss() {
        return this.cableType.loss;
    }

    public double getConductorBreakdownEnergy() {
        return this.cableType.capacity + 1;
    }

    public void removeConductor() {
        this.getWorld().setBlockToAir(this.pos);
        new PacketCableSound(this.getWorld(), this.pos,
                0.5F,
                2.6F + (world.rand.nextFloat() - world.rand.nextFloat()) * 0.8F
        );
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
            cableType = CableType.values[(int) DecoderHandler.decode(customPacketBuffer)];
            connectivity = (byte) DecoderHandler.decode(customPacketBuffer);
            this.rerender();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }


    @Override
    public void update_render() {
        if (!this.getWorld().isRemote) {
            this.updateConnectivity();
        }
    }

    @Override
    public TileEntity getTileEntity() {
        return this;
    }

    @Override
    public BlockPos getBlockPos() {
        return this.pos;
    }

}
