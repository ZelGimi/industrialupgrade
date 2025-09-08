package com.denfop.blockentity.base;

import com.denfop.IUCore;
import com.denfop.IUItem;
import com.denfop.api.blockentity.MultiBlockEntity;
import com.denfop.api.energy.event.load.EnergyTileLoadEvent;
import com.denfop.api.energy.event.unload.EnergyTileUnLoadEvent;
import com.denfop.api.energy.forgeenergy.EnergyForge;
import com.denfop.api.energy.forgeenergy.EnergyForgeSink;
import com.denfop.api.energy.forgeenergy.EnergyForgeSinkSource;
import com.denfop.api.energy.forgeenergy.EnergyForgeSource;
import com.denfop.api.energy.interfaces.EnergyTile;
import com.denfop.api.energy.networking.EnergyNetGlobal;
import com.denfop.blocks.BlockResource;
import com.denfop.blocks.BlockTileEntity;
import com.denfop.blocks.state.HarvestTool;
import com.denfop.componets.*;
import com.denfop.events.TickHandlerIU;
import com.denfop.inventory.Inventory;
import com.denfop.network.DecoderHandler;
import com.denfop.network.packet.CustomPacketBuffer;
import com.denfop.network.packet.PacketStopSound;
import com.denfop.network.packet.PacketUpdateFieldTile;
import com.denfop.network.packet.PacketUpdateTile;
import com.denfop.utils.Keyboard;
import com.denfop.utils.Localization;
import com.denfop.utils.ModUtils;
import com.denfop.world.WorldBaseGen;
import net.minecraft.core.*;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.protocol.Packet;
import net.minecraft.network.protocol.game.ClientGamePacketListener;
import net.minecraft.resources.ResourceKey;
import net.minecraft.util.Mth;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.ChunkPos;
import net.minecraft.world.level.Explosion;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.EntityBlock;
import net.minecraft.world.level.block.SoundType;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.Property;
import net.minecraft.world.level.chunk.status.ChunkStatus;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.HitResult;
import net.minecraft.world.phys.Vec3;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.api.distmarker.OnlyIn;
import net.neoforged.neoforge.capabilities.BlockCapability;
import net.neoforged.neoforge.capabilities.Capabilities;
import net.neoforged.neoforge.common.NeoForge;
import net.neoforged.neoforge.energy.IEnergyStorage;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.io.IOException;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

public abstract class BlockEntityBase extends BlockEntity {
    public static final List<AABB> defaultAabbs = Collections.singletonList(new AABB(
            0.0,
            0.0,
            0.0,
            1.0,
            1.0,
            1.0
    ));
    public static int ticker = 1;
    public static Map<ResourceKey<Level>, List<ChunkPos>> updates = new ConcurrentHashMap<>();
    public final MultiBlockEntity teBlock;
    public final BlockTileEntity block;
    public BlockPos pos;
    public Map<BlockCapability<?, ?>, AbstractComponent> capabilityComponents;
    public List<AbstractComponent> componentList = new ArrayList<>();
    public List<AbstractComponent> updateServerList = new ArrayList<>();
    public List<AbstractComponent> updateClientList = new ArrayList<>();
    public Map<String, AbstractComponent> advComponentMap = new HashMap<>();
    public SoilPollutionComponent pollutionSoil;
    public AirPollutionComponent pollutionAir;
    public String active = "";
    public boolean isLoaded;
    public byte facing;
    public HolderLookup.Provider provider;
    public BlockState blockState;
    boolean hasHashCode = false;
    CooldownTracker cooldownTracker = new CooldownTracker();
    boolean loaded = false;
    private boolean isClientLoaded;
    private int hashCode;

    public BlockEntityBase(MultiBlockEntity multiBlockItem, BlockPos p_155229_, BlockState p_155230_) {
        super(multiBlockItem.getBlockType(), p_155229_, p_155230_);
        this.blockState = p_155230_;
        this.teBlock = getTeBlock();
        this.block = getBlock();
        this.pos = p_155229_;


    }

    public static boolean checkSide(List<AABB> aabbs, Direction side, boolean strict) {
        if (aabbs == defaultAabbs) {
            return true;
        } else {
            int dx = side.getStepX();
            int dy = side.getStepY();
            int dz = side.getStepZ();
            int xS = (dx + 1) / 2;
            int yS = (dy + 1) / 2;
            int zS = (dz + 1) / 2;
            int xE = (dx + 2) / 2;
            int yE = (dy + 2) / 2;
            int zE = (dz + 2) / 2;
            Iterator var12;
            AABB aabb;
            if (strict) {
                var12 = aabbs.iterator();

                while (var12.hasNext()) {
                    aabb = (AABB) var12.next();
                    switch (side) {
                        case DOWN:
                            if (aabb.minY < 0.0) {
                                return false;
                            }
                            break;
                        case UP:
                            if (aabb.maxY > 1.0) {
                                return false;
                            }
                            break;
                        case NORTH:
                            if (aabb.minZ < 0.0) {
                                return false;
                            }
                            break;
                        case SOUTH:
                            if (aabb.maxZ > 1.0) {
                                return false;
                            }
                            break;
                        case WEST:
                            if (aabb.minX < 0.0) {
                                return false;
                            }
                            break;
                        case EAST:
                            if (aabb.maxX > 1.0) {
                                return false;
                            }
                    }
                }
            }

            var12 = aabbs.iterator();

            do {
                if (!var12.hasNext()) {
                    return false;
                }

                aabb = (AABB) var12.next();
            } while (!(aabb.minX <= (double) xS) || !(aabb.minY <= (double) yS) || !(aabb.minZ <= (double) zS) || !(aabb.maxX >= (double) xE) || !(aabb.maxY >= (double) yE) || !(aabb.maxZ >= (double) zE));

            return true;
        }
    }

    public RegistryAccess registryAccess() {
        return getWorld().registryAccess();
    }

    @Override
    public void setChanged() {
        if (!this.getLevel().isClientSide()) {
            for (final AbstractComponent abstractComponent : this.componentList) {
                abstractComponent.markDirty();
            }
        }
        if (this.level != null) {
            setChanged(this.level, this.worldPosition, this.blockState);
        }
    }

    public BlockPos getPos() {
        return worldPosition;
    }

    public void addInformation(ItemStack stack, List<String> tooltip) {


        if (level == null) {
            AirPollutionComponent air = this.getComp(AirPollutionComponent.class);
            SoilPollutionComponent soil = this.getComp(SoilPollutionComponent.class);
            if (air != null || soil != null) {
                if (Keyboard.isKeyDown(Keyboard.KEY_LSHIFT)) {
                    tooltip.add(Localization.translate("iu.pollution.info1"));
                    tooltip.add(Localization.translate("iu.pollution.info2"));
                    tooltip.add(Localization.translate("iu.pollution.info3"));
                }
                tooltip.add(Localization.translate("iu.pollution.info"));

            }

        }
        for (AbstractComponent component : this.componentList) {
            component.addInformation(stack, tooltip);
        }

    }

    public Level getWorld() {
        return this.getLevel();
    }

    public void readFromNBT(CompoundTag nbt) {
        this.facing = nbt.getByte("facing");

        this.active = nbt.getString("active");
        if (!this.componentList.isEmpty() && nbt.contains("component_mod", 10)) {
            CompoundTag componentsNbt = nbt.getCompound("component_mod");
            for (int i = 0; i < this.componentList.size(); i++) {
                final AbstractComponent component = this.componentList.get(i);
                CompoundTag componentNbt = componentsNbt.getCompound("component_" + i);
                component.readFromNbt(componentNbt);
            }
        }
    }

    @Override
    protected void loadAdditional(CompoundTag p_338466_, HolderLookup.Provider p_338445_) {
        super.loadAdditional(p_338466_, p_338445_);
        this.provider = p_338445_;
        this.readFromNBT(p_338466_);
    }

    public void onLoaded() {
        this.pos = worldPosition;
        this.componentList.forEach(AbstractComponent::onLoaded);
        this.rerender();
        if (!this.getLevel().isClientSide && this.needUpdate()) {
            IUCore.network.getServer().addTileToOvertimeUpdate(this);
        }

        this.hashCode();
    }

    public CompoundTag writeToNBT(HolderLookup.Provider provider, CompoundTag nbt) {
        this.provider = provider;
        return writeToNBT(nbt);
    }

    public CompoundTag writeToNBT(CompoundTag nbt) {
        nbt.putByte("facing", this.facing);
        nbt.putString("active", this.active);
        if (!this.componentList.isEmpty()) {
            CompoundTag componentsNbt = new CompoundTag();

            for (int i = 0; i < this.componentList.size(); i++) {
                final AbstractComponent component = this.componentList.get(i);
                CompoundTag nbt1 = component.writeToNbt();
                if (nbt1 == null) {
                    nbt1 = new CompoundTag();
                }
                componentsNbt.put("component_" + i, nbt1);
            }
            nbt.put("component_mod", componentsNbt);
        }
        return nbt;
    }

    @Override
    protected void saveAdditional(CompoundTag p_187471_, HolderLookup.Provider p_323635_) {
        super.saveAdditional(p_187471_, p_323635_);
        this.provider = p_323635_;
        writeToNBT(p_187471_);
    }

    @javax.annotation.Nullable
    public Packet<ClientGamePacketListener> getUpdatePacket() {
        new PacketUpdateTile(this);
        return null;
    }

    @Override
    public CompoundTag getUpdateTag(HolderLookup.Provider p_323910_) {
        new PacketUpdateTile(this);
        return super.getUpdateTag(p_323910_);
    }

    public void tick() {
        if (this.isRemoved())
            return;
        if (this.getLevel().isClientSide) {
            this.updateEntityClient();
        } else {
            this.updateEntityServer();
            if (ticker % 120 == 0) {
                ChunkPos chunkPos = new ChunkPos(worldPosition);
                if (!this.isRemoved() && !updates.computeIfAbsent(this.level.dimension(), k -> new LinkedList<>()).contains(chunkPos)) {
                    updates.computeIfAbsent(this.level.dimension(), k -> new LinkedList<>()).add(chunkPos);
                    level.blockEntityChanged(worldPosition);
                }
            }
        }
    }

    @OnlyIn(Dist.CLIENT)
    public void updateEntityClient() {
        this.pos = this.worldPosition;
        this.updateClientList.forEach(AbstractComponent::updateEntityClient);
        if (!isClientLoaded) {
            this.loadBeforeFirstClientUpdate();
        }
        if (cooldownTracker.getTick() > 0) {
            cooldownTracker.removeTick();
        }
    }

    public void loadBeforeFirstClientUpdate() {
        isClientLoaded = true;
    }

    public void updateEntityServer() {
        this.pos = this.worldPosition;
        if (!this.getSupportedFacings().contains(getFacing())) {
            for (Property<?> property : blockState.getProperties()) {
                if (property.getName().equals("facing")) {

                    Direction value = (Direction) blockState.getValue(property);
                    this.setFacing(value);
                    break;
                }
            }
        }
        for (AbstractComponent component : this.updateServerList) {
            component.updateEntityServer();
        }
        if (!isLoaded) {
            this.loadBeforeFirstUpdate();
        }
        if (cooldownTracker.getTick() > 0) {
            cooldownTracker.removeTick();
        }
    }

    @Override
    public void setRemoved() {
        if (loaded)
            this.onUnloaded();
        super.setRemoved();
    }

    @Override
    public void onLoad() {

        if (!this.loaded) {
            super.onLoad();
            loaded = true;
            Level world = this.getLevel();
            if (this.worldPosition != null) {
                TickHandlerIU.requestSingleWorldTick(world, world1 -> {
                    BlockEntityBase.this.onLoaded();
                });
            }
        }
    }

    public void loadBeforeFirstUpdate() {
        isLoaded = true;
        try {
            for (Direction direction : Direction.values())
                if (!this.getLevel().isClientSide) {
                    BlockPos neighborPos = pos.offset(direction.getNormal());
                    BlockState neighbor = getLevel().getBlockState(neighborPos);
                    if ((this instanceof EnergyTile || this.getComp(Energy.class) != null) && (neighbor.getBlock() instanceof EntityBlock && !(neighbor.getBlock() instanceof BlockTileEntity<?>))) {
                        IEnergyStorage storage = level.getCapability(Capabilities.EnergyStorage.BLOCK, neighborPos, ModUtils.getFacingFromTwoPositions(this.pos, neighborPos));
                        BlockEntity blockEntity = getLevel().getBlockEntity(neighborPos);
                        if (storage != null && !blockEntity.isRemoved()) {
                            EnergyTile energyTile = EnergyNetGlobal.instance.getTile(level, neighborPos);
                            if (energyTile == EnergyNetGlobal.EMPTY) {
                                EnergyForge energyForge = null;
                                if (storage.canExtract() && storage.canReceive()) {
                                    energyForge = new EnergyForgeSinkSource(blockEntity);
                                } else if (storage.canReceive()) {
                                    energyForge = new EnergyForgeSink(blockEntity);
                                } else if (storage.canExtract()) {
                                    energyForge = new EnergyForgeSource(blockEntity);
                                }
                                if (energyForge != null) {
                                    NeoForge.EVENT_BUS.post(new EnergyTileLoadEvent(this.getWorld(), energyForge));
                                }
                            }
                        }
                    }
                }
            this.rerender();
        } catch (Exception e) {
        }
    }

    public void rerender() {
        BlockState state1 = this.blockState;
        this.blockState = null;
        BlockState state = this.getBlockState();
        if (state1 == null) {
            state1 = state;
        }
        if (isChunkLoaded(this.level, pos))
            this.getLevel().sendBlockUpdated(this.worldPosition, blockState, blockState, 2);
    }

    public boolean isChunkLoaded(@Nullable Level world, @NotNull BlockPos pos) {
        return isChunkLoaded(world, SectionPos.blockToSectionCoord(pos.getX()), SectionPos.blockToSectionCoord(pos.getZ()));
    }

    public boolean isChunkLoaded(Level world, int chunkX, int chunkZ) {
        if (world == null) {
            return false;
        } else if (world instanceof Level accessor) {
            if (!accessor.isClientSide) {
                return accessor.hasChunk(chunkX, chunkZ);
            }
        }
        return world.getChunk(chunkX, chunkZ, ChunkStatus.FULL, false) != null;
    }

    public void onClicked(Player player) {

    }

    public void onNeighborChange(BlockState neighbor, BlockPos neighborPos) {
        if (this.componentList != null) {
            for (final AbstractComponent component : componentList) {
                component.onNeighborChange(neighbor, neighborPos);
            }
        }
        if (!this.getLevel().isClientSide) {


            if ((this instanceof EnergyTile || this.getComp(Energy.class) != null) && (neighbor.getBlock() instanceof EntityBlock && !(neighbor.getBlock() instanceof BlockTileEntity<?>))) {
                IEnergyStorage storage = level.getCapability(Capabilities.EnergyStorage.BLOCK, neighborPos, ModUtils.getFacingFromTwoPositions(this.pos, neighborPos));
                BlockEntity blockEntity = getLevel().getBlockEntity(neighborPos);
                if (storage != null && !blockEntity.isRemoved()) {
                    EnergyTile energyTile = EnergyNetGlobal.instance.getTile(level, neighborPos);
                    if (energyTile != EnergyNetGlobal.EMPTY) {
                        NeoForge.EVENT_BUS.post(new EnergyTileUnLoadEvent(this.getWorld(), energyTile));
                    }
                    EnergyForge energyForge = null;
                    if (storage.canExtract() && storage.canReceive()) {
                        energyForge = new EnergyForgeSinkSource(blockEntity);
                    } else if (storage.canReceive()) {
                        energyForge = new EnergyForgeSink(blockEntity);
                    } else if (storage.canExtract()) {
                        energyForge = new EnergyForgeSource(blockEntity);
                    }
                    if (energyForge != null) {
                        NeoForge.EVENT_BUS.post(new EnergyTileLoadEvent(this.getWorld(), energyForge));
                    }
                }
            } else if (this instanceof EnergyTile || this.getComp(Energy.class) != null) {
                EnergyTile energyTile = EnergyNetGlobal.instance.getTile(level, neighborPos);
                if (energyTile != EnergyNetGlobal.EMPTY && energyTile instanceof EnergyForge) {
                    NeoForge.EVENT_BUS.post(new EnergyTileUnLoadEvent(this.getWorld(), energyTile));
                }
            }
        }
    }

    public boolean hasSpecialModel() {
        return false;
    }

    public boolean canPlace(BlockEntityBase te, BlockPos pos, Level world, Direction direction, LivingEntity entity) {

        Direction facing = this.getPlacementFacing(entity, direction);
        byte temp = this.facing;
        this.facing = (byte) facing.ordinal();
        AABB aabb = this.getAabb(false).move(pos);
        int minX = Mth.floor(aabb.minX);
        int maxX = Mth.ceil(aabb.maxX);
        int minY = Mth.floor(aabb.minY);
        int maxY = Mth.ceil(aabb.maxY);
        int minZ = Mth.floor(aabb.minZ);
        int maxZ = Mth.ceil(aabb.maxZ);

        BlockPos.MutableBlockPos checkPos = new BlockPos.MutableBlockPos();
        for (int x = minX; x < maxX; x++) {
            for (int y = minY; y < maxY; y++) {
                for (int z = minZ; z < maxZ; z++) {
                    checkPos.set(x, y, z);
                    BlockState state = world.getBlockState(checkPos);
                    if (!state.isAir() && !state.getCollisionShape(world, checkPos).isEmpty()) {
                        this.facing = temp;
                        return false;
                    }
                }
            }
        }

        this.facing = temp;
        return true;
    }

    public int getWeakPower(Direction side) {
        return 0;
    }

    public boolean canConnectRedstone() {
        return this.hasComponent(Redstone.class);
    }

    public boolean hasComponent(Class<? extends AbstractComponent> cls) {
        for (final AbstractComponent component : componentList) {
            if (component.getClass() == cls) {
                return true;
            }
        }
        return false;
    }

    public int getComparatorInputOverride() {
        return 0;
    }

    public int getLightOpacity() {
        return 0;
    }

    public int getLightValue() {
        return 0;
    }

    public boolean onActivated(Player player, InteractionHand hand, Direction side, Vec3 vec3) {
        return false;
    }

    public CooldownTracker getCooldownTracker() {
        return cooldownTracker;
    }

    public CustomPacketBuffer writeUpdatePacket() {
        return new CustomPacketBuffer(this.getLevel().registryAccess());
    }

    public void readUpdatePacket(CustomPacketBuffer packetBuffer) {
    }

    public boolean needUpdate() {
        return false;
    }

    public CustomPacketBuffer writeContainerPacket() {
        return new CustomPacketBuffer(this.getWorld().registryAccess());
    }

    public void readContainerPacket(CustomPacketBuffer customPacketBuffer) {
    }

    public void onNetworkUpdate(String field) {
        if (field.equals("active") || field.equals("facing")) {
            this.rerender();
        }

    }

    public boolean canEntityDestroy(final Entity entity) {
        for (AbstractComponent component : this.componentList) {
            if (!component.canEntityDestroy(entity)) {
                return false;
            }
        }
        return true;
    }

    public List<AbstractComponent> getComponentList() {
        return componentList;
    }

    public List<ItemStack> getSelfDrops(int fortune, boolean wrench) {
        ItemStack drop = this.getPickBlock(null, null);
        drop = this.adjustDrop(drop, wrench, fortune);
        if (drop == null) {
            drop = ItemStack.EMPTY;
        }
        if (!drop.isEmpty()) {
            for (AbstractComponent component : this.componentList) {
                if (component.needWriteNBTToDrops()) {
                    CompoundTag tagCompound = ModUtils.nbt(drop);
                    tagCompound.put(component.toString(), component.writeNBTToDrops(new CompoundTag()));
                }
            }
        }
        return Collections.singletonList(drop);
    }

    public boolean wrenchCanRemove(final Player player) {
        for (AbstractComponent component : this.componentList) {
            if (!component.wrenchCanRemove(player)) {
                return false;
            }
        }
        return this.getTeBlock().getHarvestTool() == HarvestTool.Wrench;
    }

    public void onPlaced(ItemStack stack, LivingEntity placer, Direction facing) {
        Level world = this.getLevel();

        facing = this.getPlacementFacing(placer, facing);
        if (facing != this.getFacing()) {
            this.setFacing(facing);
        }
        if (world.isClientSide) {
            this.rerender();
        }
        for (AbstractComponent component : this.componentList) {
            if (component.needWriteNBTToDrops()) {
                CompoundTag tagCompound = ModUtils.nbt(stack);
                final CompoundTag tag = tagCompound.getCompound(component.toString());
                component.readFromNbt(tag);
            }
            component.onPlaced(stack, placer, facing);
        }

    }

    public AABB getVisualBoundingBox() {
        return this.getAabb(false);
    }

    public AABB getPhysicsBoundingBox() {
        return this.getAabb(true);
    }

    public AABB getOutlineBoundingBox() {
        return this.getVisualBoundingBox();
    }

    public void addCollisionBoxesToList(AABB mask, List<AABB> list, Entity collidingEntity) {
        AABB maskNormalized = mask.move(
                -this.worldPosition.getX(),
                -this.worldPosition.getY(),
                -this.worldPosition.getZ()
        );
        for (final AABB aabb : this.getAabbs(true)) {
            if (aabb.intersects(maskNormalized)) {
                list.add(aabb.move(this.worldPosition));
            }
        }

    }

    public AABB getAabb(boolean forCollision) {
        List<AABB> aabbs = this.getAabbs(forCollision);
        if (aabbs.isEmpty()) {
            throw new RuntimeException("No AABBs for " + this);
        } else if (aabbs.size() == 1) {
            return aabbs.get(0);
        } else {
            double zS = Double.POSITIVE_INFINITY;
            double yS = Double.POSITIVE_INFINITY;
            double xS = Double.POSITIVE_INFINITY;
            double zE = Double.NEGATIVE_INFINITY;
            double yE = Double.NEGATIVE_INFINITY;
            double xE = Double.NEGATIVE_INFINITY;

            AABB aabb;
            for (Iterator var15 = aabbs.iterator(); var15.hasNext(); zE = Math.max(zE, aabb.maxZ)) {
                aabb = (AABB) var15.next();
                xS = Math.min(xS, aabb.minX);
                yS = Math.min(yS, aabb.minY);
                zS = Math.min(zS, aabb.minZ);
                xE = Math.max(xE, aabb.maxX);
                yE = Math.max(yE, aabb.maxY);
            }

            return new AABB(xS, yS, zS, xE, yE, zE);
        }
    }

    public void onEntityCollision(Entity entity) {
    }

    private void validateBlockState(BlockState p_353132_) {
        if (!this.isValidBlockState(p_353132_)) {
            String var10002 = this.getNameForReporting();
            throw new IllegalStateException("Invalid block entity " + var10002 + " state at " + String.valueOf(this.worldPosition) + ", got " + String.valueOf(p_353132_));
        }
    }

    private String getNameForReporting() {
        String var10000 = String.valueOf(BuiltInRegistries.BLOCK_ENTITY_TYPE.getKey(this.getType()));
        return var10000 + " // " + this.getClass().getCanonicalName();
    }

    @Override
    public BlockState getBlockState() {
        if (this.blockState == null) {
            try {
                this.blockState = this.block
                        .defaultBlockState()
                        .setValue(this.block.typeProperty, this.block.typeProperty.getState(this.teBlock, this.active))
                        .setValue(
                                this.block.facingProperty,
                                this.getFacing()
                        )
                ;
            } catch (Exception e) {
                this.blockState = this.block
                        .defaultBlockState();
            }
            return this.blockState;
        }
        return this.blockState;
    }

    @Deprecated
    public void setBlockState(BlockState p_155251_) {
        this.validateBlockState(p_155251_);
        this.blockState = p_155251_;
    }

    public abstract MultiBlockEntity getTeBlock();

    public abstract BlockTileEntity getBlock();

    @Override
    public int hashCode() {
        if (!hasHashCode) {

            hasHashCode = true;
            this.hashCode = super.hashCode();
        }
        return hashCode;
    }


    public <T extends AbstractComponent> T getComp(String cls) {
        for (AbstractComponent component : this.componentList) {
            if (component.toString().trim().equals(cls)) {
                return (T) component;
            }
        }
        return null;
    }

    public ItemStack getItem(Player player, HitResult target) {
        return this.block.getItemStack();
    }

    public Set<Direction> getSupportedFacings() {
        return this.teBlock.getSupportedFacings();
    }

    public List<AABB> getAabbs(boolean forCollision) {
        return defaultAabbs;
    }

    public ItemStack adjustDrop(ItemStack drop, boolean wrench) {
        return this.adjustDrop(drop, wrench, WorldBaseGen.random.nextInt(100));
    }

    public Iterable<? extends AbstractComponent> getComps() {
        return componentList;
    }

    public <T> T getCapability(@NotNull BlockCapability<T, Direction> cap, @Nullable Direction side) {
        if (this.capabilityComponents == null) {
            return null;
        } else {

            AbstractComponent comp = this.capabilityComponents.get(cap);
            return comp == null ? null : comp.getCapability(cap, side);
        }
    }

    public void onExploded(Explosion explosion) {
    }

    public <T extends AbstractComponent> T addComponent(T component) {
        if (component == null) {
            throw new NullPointerException("null component");
        } else {

            componentList.add(component);
            advComponentMap.put(component.toString(), component);
            if (component.isClient()) {
                this.updateClientList.add(component);
            }
            if (component.isServer()) {
                this.updateServerList.add(component);
            }

            for (final BlockCapability<?, ?> capability : component.getProvidedCapabilities(null)) {
                this.addComponentCapability(capability, component);
            }

            return component;

        }
    }

    public <T extends AbstractComponent> void removeComponent(T component) {
        if (component == null) {
            throw new NullPointerException("null component");
        } else {

            componentList.remove(component);
            advComponentMap.remove(component.toString(), component);
            if (component.isClient()) {
                this.updateClientList.remove(component);
            }
            if (component.isServer()) {
                this.updateServerList.remove(component);
            }

            for (final BlockCapability<?, ?> capability : component.getProvidedCapabilities(null)) {
                this.removeComponentCapability(capability, component);
            }

        }
    }

    public void addComponentCapability(BlockCapability<?, ?> cap, AbstractComponent component) {
        if (this.capabilityComponents == null) {
            this.capabilityComponents = new IdentityHashMap<>();
        }

        AbstractComponent prev = this.capabilityComponents.put(cap, component);

        assert prev == null;

    }

    public void removeComponentCapability(BlockCapability<?, ?> cap, AbstractComponent component) {
        if (this.capabilityComponents == null) {
            this.capabilityComponents = new IdentityHashMap<>();
        }

        this.capabilityComponents.remove(cap, component);


    }

    @Override
    public void onChunkUnloaded() {
        if (loaded)
            this.onUnloaded();
        super.onChunkUnloaded();
    }

    public void onUnloaded() {
        if (this.needUpdate())
            IUCore.network.getServer().removeTileToOvertimeUpdate(this);
        this.componentList.forEach(AbstractComponent::onUnloaded);
        try {

            new PacketStopSound(getWorld(), this.getBlockPos());
        } catch (Exception ignored) {
        }
        if (!this.getLevel().isClientSide) {
            //    new PacketRemoveUpdateTile(this);
        }
    }

    public boolean onSneakingActivated(Player player, InteractionHand hand, Direction side, Vec3 vec3) {
        for (AbstractComponent component : componentList) {
            if (component.onSneakingActivated(player, hand))
                return true;
        }
        return this.getLevel().isClientSide;
    }

    public Map<BlockCapability<?, ?>, AbstractComponent> getCapabilityComponents() {
        return capabilityComponents;
    }

    public void onBlockBreak(boolean wrench) {
        for (AbstractComponent component : this.componentList) {
            component.blockBreak();
        }
    }

    public void wrenchBreak() {
        this.onBlockBreak(true);
    }

    public boolean onRemovedByPlayer(Player player, boolean willHarvest) {
        return true;
    }

    public ItemStack getPickBlock(Player player, HitResult target) {
        return this.block.getItemStack();
    }

    public List<ItemStack> getAuxDrops(int fortune) {
        return Collections.emptyList();
    }

    public float getHardness() {
        return this.teBlock.getHardness();
    }

    public boolean getActive() {
        return this.active.contains("active");
    }

    public void setActive(String active) {
        if (this.active.equals(active)) {
            return;
        }
        if (!isChunkLoaded(this.level, pos))
            return;
        this.active = active;
        if (!this.getLevel().isClientSide) {
            new PacketUpdateFieldTile(this, "active", this.active);
        }
        this.getWorld().setBlock(this.worldPosition, this.getBlockState().setValue(this.block.typeProperty, this.block.typeProperty.getState(this.teBlock, this.active)), 3);

    }

    public void setActive(boolean active) {
        if (!active && this.active.equals("")) {
            return;
        }


        if (active) {
            if (!this.active.equals("active")) {
                this.active = "active";
                if (!this.getLevel().isClientSide) {
                    new PacketUpdateFieldTile(this, "active", this.active);
                }
            }
        } else {
            this.active = "";
            if (!this.getLevel().isClientSide) {
                new PacketUpdateFieldTile(this, "active", this.active);
            }
        }
        if (!isChunkLoaded(this.level, pos))
            return;
        this.getWorld().setBlock(this.worldPosition, this.getBlockState().setValue(this.block.typeProperty, this.block.typeProperty.getState(this.teBlock, this.active)), 3);

    }

    public Direction getFacing() {
        return Direction.values()[this.facing];
    }

    public void setFacing(Direction facing) {
        if (facing == null) {
            throw new NullPointerException("null facing");
        } else if (this.facing == facing.ordinal()) {
            return;
        } else if (!this.getSupportedFacings().contains(facing)) {
            return;
        } else {
            this.facing = (byte) facing.ordinal();
            if (!this.getLevel().isClientSide) {
                new PacketUpdateFieldTile(this, "facing", this.facing);
            }
            if (!isChunkLoaded(this.level, pos))
                return;
            this.getWorld().setBlock(this.worldPosition, this.getBlockState().setValue(this.block.facingProperty, this.getFacing()), 3);


        }
    }

    public boolean canSetFacingWrench(Direction facing, Player player) {
        if (!this.teBlock.allowWrenchRotating()) {
            return false;
        } else if (facing == this.getFacing()) {
            return false;
        } else {
            this.setFacing(facing);
            return this.getSupportedFacings().contains(facing);
        }
    }

    public boolean setFacingWrench(Direction facing, Player player) {
        if (!this.canSetFacingWrench(facing, player)) {
            return false;
        } else {
            this.setFacing(facing);
            return true;
        }
    }

    public List<ItemStack> getWrenchDrops(Player player, int fortune) {
        List<ItemStack> ret = new ArrayList();
        ret.addAll(this.getSelfDrops(fortune, true));
        ret.addAll(this.getAuxDrops(fortune));
        return ret;
    }

    public SoundType getBlockSound(Entity entity) {
        return SoundType.STONE;
    }

    public Direction getPlacementFacing(LivingEntity placer, Direction facing) {
        Set<Direction> supportedFacings = this.getSupportedFacings();
        if (supportedFacings.isEmpty()) {
            return Direction.DOWN;
        } else if (placer != null) {
            Vec3 dir = placer.getLookAngle();
            Direction bestFacing = null;
            double maxMatch = Double.NEGATIVE_INFINITY;

            for (final Direction cFacing : supportedFacings) {
                double match = dir.dot(Vec3.atLowerCornerOf(cFacing.getOpposite().getNormal()));
                if (match > maxMatch) {
                    maxMatch = match;
                    bestFacing = cFacing;
                }
            }

            return bestFacing;
        } else {
            return facing != null && supportedFacings.contains(facing.getOpposite())
                    ? facing.getOpposite()
                    : this.getSupportedFacings().iterator().next();
        }
    }

    public ItemStack adjustDrop(ItemStack drop, boolean wrench, int fortune) {
        if (!wrench) {
            switch (this.teBlock.getDefaultDrop()) {
                case Self:
                default:
                    drop = getPickBlock(null, null);
                    break;
                case Generator:

                    drop = new ItemStack(IUItem.basemachine2.getItem(78), 1);

                    break;
                case None:
                    drop = null;
                    break;
                case Machine:

                    return IUItem.blockResource.getItemStack(BlockResource.Type.machine);

                case AdvMachine:

                    return IUItem.blockResource.getItemStack(BlockResource.Type.advanced_machine);

            }
        } else {
            switch (this.teBlock.getDefaultDrop()) {
                case Self:
                default:
                    drop = getPickBlock(null, null);
                    break;
                case Generator:
                    if (fortune < 2) {
                        drop = new ItemStack(IUItem.basemachine2.getItem(78), 1);
                    }
                    break;
                case None:
                    drop = null;
                    break;
                case Machine:
                    if (fortune < 2) {
                        return IUItem.blockResource.getItemStack(BlockResource.Type.machine);
                    }
                case AdvMachine:
                    if (fortune < 2) {
                        return IUItem.blockResource.getItemStack(BlockResource.Type.advanced_machine);
                    }
            }
        }

        return drop;
    }

    public CustomPacketBuffer writePacket() {
        final CustomPacketBuffer packet = new CustomPacketBuffer(this.getLevel().registryAccess());
        packet.writeShort(this.teBlock.getIDBlock());
        packet.writeString(this.active);
        packet.writeByte(this.facing);
        return packet;
    }

    public CompoundTag getNBTFromSlot(CustomPacketBuffer customPacketBuffer) {
        try {
            Inventory slot = (Inventory) DecoderHandler.decode(customPacketBuffer);
            return slot.writeToNbt(new CompoundTag(), customPacketBuffer.registryAccess());
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

    }

    public void updateField(String name, CustomPacketBuffer is) {

        if (name.equals("active")) {
            try {
                this.active = (String) DecoderHandler.decode(is);
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }
        if (name.equals("facing")) {
            is.readUnsignedByte();
            this.facing = is.readByte();
        }
        this.onNetworkUpdate(name);
    }

    public void readPacket(CustomPacketBuffer customPacketBuffer) {
        this.active = customPacketBuffer.readString();
        this.facing = customPacketBuffer.readByte();
        this.rerender();
    }


    public boolean doesSideBlockRendering(Direction side) {
        return checkSide(this.getAabbs(false), side, false);
    }

    public <T extends AbstractComponent> T getComp(Class<T> cls) {
        for (final AbstractComponent component : componentList) {
            if (component.getClass() == cls) {
                return (T) component;
            }
        }
        return null;
    }
}
