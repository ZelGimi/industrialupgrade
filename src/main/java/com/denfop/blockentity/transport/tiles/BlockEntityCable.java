package com.denfop.blockentity.transport.tiles;

import com.denfop.api.blockentity.MultiBlockEntity;
import com.denfop.api.energy.InfoCable;
import com.denfop.api.energy.event.load.EnergyTileLoadEvent;
import com.denfop.api.energy.event.unload.EnergyTileUnLoadEvent;
import com.denfop.api.energy.interfaces.EnergyAcceptor;
import com.denfop.api.energy.interfaces.EnergyConductor;
import com.denfop.api.energy.interfaces.EnergyEmitter;
import com.denfop.api.energy.interfaces.EnergyTile;
import com.denfop.api.energy.networking.ConductorInfo;
import com.denfop.api.energy.networking.EnergyNetGlobal;
import com.denfop.api.energy.networking.NodeStats;
import com.denfop.api.item.armor.HazmatLike;
import com.denfop.api.otherenergies.common.InfoTile;
import com.denfop.blockentity.transport.types.CableType;
import com.denfop.blockentity.transport.types.ICableItem;
import com.denfop.damagesource.IUDamageSource;
import com.denfop.network.DecoderHandler;
import com.denfop.network.EncoderHandler;
import com.denfop.network.packet.CustomPacketBuffer;
import com.denfop.utils.Localization;
import com.denfop.utils.ModUtils;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.ChunkPos;
import net.minecraft.world.level.block.SoundType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.AABB;
import net.minecraftforge.common.MinecraftForge;

import java.io.IOException;
import java.util.*;

import static com.denfop.blocks.BlockTileEntity.*;


public class BlockEntityCable extends BlockEntityMultiCable implements EnergyConductor {


    private final ConductorInfo conductor;
    public boolean addedToEnergyNet;
    public int type;
    protected CableType cableType;
    boolean updateConnect = false;
    Map<Direction, EnergyTile> energyConductorMap = new HashMap<>();
    List<InfoTile<EnergyTile>> validReceivers = new LinkedList<>();
    int hashCodeSource;
    private boolean needUpdate;
    private ChunkPos chunkPos;
    private InfoCable cable;
    private long id;

    public BlockEntityCable(CableType cableType, MultiBlockEntity block, BlockPos pos, BlockState state) {
        super(cableType, block, pos, state);
        this.addedToEnergyNet = false;
        this.cableType = cableType;
        this.type = cableType.ordinal();
        this.conductor = new ConductorInfo(pos, this);
    }

    @Override
    public void addInformation(ItemStack stack, List<String> info) {
        CableType type = this.cableType;
        double capacity;
        double loss;

        capacity = type.capacity;
        loss = type.loss;


        info.add(ModUtils.getString(capacity) + " " + Localization.translate("iu.generic.text.EUt"));
        info.add(Localization.translate("cable.tooltip.loss", lossFormat.format(loss)));

    }


    public ICableItem getCableItem() {
        return cableType;
    }

    public void readFromNBT(CompoundTag nbt) {
        super.readFromNBT(nbt);
        this.cableType = CableType.values[nbt.getByte("cableType") & 255];
    }

    public CompoundTag writeToNBT(CompoundTag nbt) {
        super.writeToNBT(nbt);
        nbt.putByte("cableType", (byte) this.cableType.ordinal());
        return nbt;
    }

    @Override
    public void updateTileServer(final Player var1, final double var2) {
        super.updateTileServer(var1, var2);
        MinecraftForge.EVENT_BUS.post(new EnergyTileUnLoadEvent(this.getWorld(), this));
        this.needUpdate = true;
    }

    @Override
    public void updateEntityServer() {
        super.updateEntityServer();
        if (this.needUpdate) {
            this.energyConductorMap.clear();
            validReceivers.clear();
            MinecraftForge.EVENT_BUS.post(new EnergyTileLoadEvent(this.getWorld(), this, this));
            this.needUpdate = false;
            this.updateConnectivity();
        }
        if (updateConnect) {
            updateConnect = false;
            this.updateConnectivity();
        }
        NodeStats stats = EnergyNetGlobal.instance.getNodeStats(this, this.level);
        if ((cableType == CableType.tin || cableType == CableType.copper || cableType == CableType.gold || cableType == CableType.iron) && stats.getEnergyIn() > 0) {
            AABB aabb = new AABB(this.worldPosition.getX(), this.worldPosition.getY(), this.worldPosition.getZ(),
                    this.worldPosition.getX() + 1, this.worldPosition.getY() + 1, this.worldPosition.getZ() + 1)
                    .inflate(0.1D, 0.1D, 0.1D);

            List<LivingEntity> targets = this.level.getEntitiesOfClass(LivingEntity.class, aabb);
            for (LivingEntity entity : targets)
                if (entity instanceof Player) {
                    Player player = (Player) entity;
                    if (!HazmatLike.hasCompleteHazmat(player)) {

                        entity.hurt(IUDamageSource.current, 0.25f);
                    }
                } else if (stats.getEnergyIn() > 0) {
                    entity.hurt(IUDamageSource.current, 0.25f);

                }
        }
    }

    public void onLoaded() {
        super.onLoaded();
        if (!this.getWorld().isClientSide && !addedToEnergyNet) {
            this.energyConductorMap.clear();
            validReceivers.clear();
            MinecraftForge.EVENT_BUS.post(new EnergyTileLoadEvent(this.getWorld(), this, this));
            this.addedToEnergyNet = true;
            this.updateConnectivity();

        }

    }

    public void onUnloaded() {
        if (!this.getWorld().isClientSide && this.addedToEnergyNet) {
            MinecraftForge.EVENT_BUS.post(new EnergyTileUnLoadEvent(this.getWorld(), this));
            this.addedToEnergyNet = false;
        }


        super.onUnloaded();
    }

    public SoundType getBlockSound(Entity entity) {
        return SoundType.WOOL;
    }

    public void onNeighborChange(BlockState neighbor, BlockPos neighborPos) {
        super.onNeighborChange(neighbor, neighborPos);


    }

    @Override
    public InfoCable getCable() {
        return cable;
    }

    @Override
    public void setCable(final InfoCable cable) {
        this.cable = cable;
    }

    @Override
    public ConductorInfo getInfo() {
        return conductor;
    }

    public void updateConnectivity() {
        byte newConnectivity = 0;
        Direction[] var4 = Direction.values();

        for (Direction dir : var4) {
            newConnectivity = (byte) (newConnectivity << 1);
            EnergyTile tile = energyConductorMap.get(dir);
            if (!this.getBlackList().contains(dir)) {
                if ((tile instanceof EnergyAcceptor && ((EnergyAcceptor) tile).acceptsEnergyFrom(
                        this,
                        dir.getOpposite()
                ) || tile instanceof EnergyEmitter && ((EnergyEmitter) tile).emitsEnergyTo(
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

    public boolean acceptsEnergyFrom(EnergyEmitter emitter, Direction direction) {
        return !getBlackList().contains(direction);
    }

    public boolean emitsEnergyTo(EnergyAcceptor receiver, Direction direction) {
        return !getBlackList().contains(direction);
    }

    public double getConductionLoss() {
        return this.cableType.loss;
    }

    public double getConductorBreakdownEnergy() {
        return this.cableType.capacity + 1;
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
            Direction[] var5 = Direction.values();
            Map<Direction, Boolean> booleanMap = new HashMap<>();
            for (Direction facing : var5) {
                boolean hasConnection = (this.connectivity & 1 << facing.ordinal()) != 0;
                booleanMap.put(facing, hasConnection);
            }
            this.setBlockState(this.block
                    .defaultBlockState()
                    .setValue(this.block.typeProperty, this.block.typeProperty.getState(this.teBlock, this.active))
                    .setValue(
                            this.block.facingProperty,
                            this.getFacing()
                    ).setValue(NORTH, booleanMap.get(Direction.SOUTH))
                    .setValue(SOUTH, booleanMap.get(Direction.NORTH))
                    .setValue(WEST, booleanMap.get(Direction.UP))
                    .setValue(EAST, booleanMap.get(Direction.DOWN))
                    .setValue(UP, booleanMap.get(Direction.WEST))
                    .setValue(DOWN, booleanMap.get(Direction.EAST)));
            this.getWorld().setBlock(this.worldPosition, getBlockState(), 3);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public Map<Direction, EnergyTile> getTiles() {
        return energyConductorMap;
    }

    @Override
    public List<InfoTile<EnergyTile>> getValidReceivers() {
        return validReceivers;
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
    public void RemoveTile(EnergyTile tile, final Direction facing1) {
        if (!this.getWorld().isClientSide()) {
            this.energyConductorMap.remove(facing1);
            final Iterator<InfoTile<EnergyTile>> iter = validReceivers.iterator();
            while (iter.hasNext()) {
                InfoTile<EnergyTile> tileInfoTile = iter.next();
                if (tileInfoTile.tileEntity == tile) {
                    iter.remove();
                    break;
                }
            }
            updateConnect = true;
        }
    }

    @Override
    public void AddTile(EnergyTile tile, final Direction facing1) {
        if (!this.getWorld().isClientSide) {
            if (!this.energyConductorMap.containsKey(facing1)) {
                this.energyConductorMap.put(facing1, tile);
                validReceivers.add(new InfoTile<>(tile, facing1.getOpposite()));
            }
            updateConnect = true;
        }
    }

    @Override
    public BlockPos getPos() {
        return this.pos;
    }

}
