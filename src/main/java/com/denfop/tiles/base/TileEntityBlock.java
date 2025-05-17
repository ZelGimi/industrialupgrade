package com.denfop.tiles.base;

import com.denfop.IUCore;
import com.denfop.IUItem;
import com.denfop.Localization;
import com.denfop.api.tile.IMultiTileBlock;
import com.denfop.blocks.BlockResource;
import com.denfop.blocks.BlockTileEntity;
import com.denfop.blocks.state.HarvestTool;
import com.denfop.componets.AbstractComponent;
import com.denfop.componets.AirPollutionComponent;
import com.denfop.componets.Redstone;
import com.denfop.componets.SoilPollutionComponent;
import com.denfop.events.TickHandlerIU;
import com.denfop.invslot.InvSlot;
import com.denfop.network.DecoderHandler;
import com.denfop.network.packet.CustomPacketBuffer;
import com.denfop.network.packet.PacketStopSound;
import com.denfop.network.packet.PacketUpdateFieldTile;
import com.denfop.network.packet.PacketUpdateTile;
import com.denfop.tiles.mechanism.multimechanism.simple.TileExtractor;
import com.denfop.utils.Keyboard;
import com.denfop.utils.ModUtils;
import com.denfop.world.WorldBaseGen;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.protocol.Packet;
import net.minecraft.network.protocol.game.ClientGamePacketListener;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Explosion;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.SoundType;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.HitResult;
import net.minecraft.world.phys.Vec3;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.common.PlantType;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.util.LazyOptional;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.io.IOException;
import java.util.*;

public abstract class TileEntityBlock extends BlockEntity {
    public static final PlantType noCrop = PlantType.get("nocrop");
    public static final List<AABB> defaultAabbs = Collections.singletonList(new AABB(
            0.0,
            0.0,
            0.0,
            1.0,
            1.0,
            1.0
    ));
    public final IMultiTileBlock teBlock;
    public final BlockTileEntity block;
    public BlockPos pos;
    public Map<Capability<?>, AbstractComponent> capabilityComponents;
    public List<AbstractComponent> componentList = new ArrayList<>();
    public List<AbstractComponent> updateServerList = new ArrayList<>();
    public List<AbstractComponent> updateClientList = new ArrayList<>();
    public Map<String, AbstractComponent> advComponentMap = new HashMap<>();
    public SoilPollutionComponent pollutionSoil;
    public AirPollutionComponent pollutionAir;
    public String active = "";
    public boolean isLoaded;
    public byte facing;
    boolean hasHashCode = false;
    CooldownTracker cooldownTracker = new CooldownTracker();
    private boolean isClientLoaded;
    private int hashCode;

    public TileEntityBlock(IMultiTileBlock multiBlockItem, BlockPos p_155229_, BlockState p_155230_) {
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

    @Override
    public void setChanged() {
        super.setChanged();
        if (!this.getLevel().isClientSide()) {
            for (final AbstractComponent abstractComponent : this.componentList) {
                abstractComponent.markDirty();
            }
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
        if (!this.getSupportedFacings().isEmpty()) {
            byte facingValue = nbt.getByte("facing");
            if (facingValue >= 0 && facingValue < Direction.values().length && this
                    .getSupportedFacings()
                    .contains(Direction.values()[facingValue])) {
                this.facing = facingValue;
            } else if (!this.getSupportedFacings().isEmpty()) {
                this.facing = (byte) this.getSupportedFacings().iterator().next().ordinal();
            } else {
                this.facing = (byte) Direction.NORTH.ordinal();
            }
        }

        this.active = nbt.getString("active");
        if (!this.componentList.isEmpty() && nbt.contains("components", 10)) {
            CompoundTag componentsNbt = nbt.getCompound("components");
            for (int i = 0; i < this.componentList.size(); i++) {
                final AbstractComponent component = this.componentList.get(i);
                CompoundTag componentNbt = componentsNbt.getCompound("component_" + i);
                component.readFromNbt(componentNbt);
            }
        }
    }

    @Override
    public void load(CompoundTag nbt) {
        super.load(nbt);
        this.readFromNBT(nbt);
    }

    @Override
    public void setRemoved() {
        if (loaded)
            this.onUnloaded();
        super.setRemoved();
    }

    boolean loaded = false;

    @Override
    public void onLoad() {
        super.onLoad();
        if (!this.loaded) {
            Level world = this.getLevel();
            if (this.worldPosition != null) {
                TickHandlerIU.requestSingleWorldTick(world, world1 -> {
                    TileEntityBlock.this.onLoaded();
                });
            }
        }
    }

    @Override
    public void clearRemoved() {
        super.clearRemoved();

    }

    public void onLoaded() {
        this.loaded = true;
        this.pos = worldPosition;
        this.componentList.forEach(AbstractComponent::onLoaded);
        this.rerender();
        if (!this.getLevel().isClientSide && this.needUpdate()) {
            IUCore.network.getServer().addTileToOvertimeUpdate(this);
        }
        this.hashCode();
    }

    public CompoundTag writeToNBT(CompoundTag nbt) {
        if (!this.getSupportedFacings().isEmpty()) {
            nbt.putByte("facing", this.facing);
        }

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
            nbt.put("components", componentsNbt);
        }
        return nbt;
    }

    @Override
    protected void saveAdditional(CompoundTag nbt) {
        super.saveAdditional(nbt);

        writeToNBT(nbt);
    }

    @javax.annotation.Nullable
    public Packet<ClientGamePacketListener> getUpdatePacket() {
        new PacketUpdateTile(this);
        return null;
    }

    @Override
    public CompoundTag getUpdateTag() {
        new PacketUpdateTile(this);
        return super.getUpdateTag();
    }

    public void tick() {
        if (this.getLevel().isClientSide) {
            this.updateEntityClient();
        } else {
            this.updateEntityServer();
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

    public void loadBeforeFirstUpdate() {
        isLoaded = true;
        try {
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
        this.getLevel().sendBlockUpdated(this.worldPosition, blockState, blockState, 2);
    }

    public void onClicked(Player player) {

    }

    public void onNeighborChange(BlockState neighbor, BlockPos neighborPos) {
        if (this.componentList != null) {
            for (final AbstractComponent component : componentList) {
                component.onNeighborChange(neighbor, neighborPos);
            }
        }

    }

    public boolean hasSpecialModel() {
        return false;
    }

    public boolean canPlace(TileEntityBlock te, BlockPos pos, Level world) {
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
        return new CustomPacketBuffer();
    }

    public void readUpdatePacket(CustomPacketBuffer packetBuffer) {
    }

    public boolean needUpdate() {
        return false;
    }

    public CustomPacketBuffer writeContainerPacket() {
        return new CustomPacketBuffer();
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

    public abstract IMultiTileBlock getTeBlock();

    public abstract BlockTileEntity getBlock();

    @Override
    public int hashCode() {
        if (!hasHashCode) {

            hasHashCode = true;
            this.hashCode = super.hashCode();
        }
        return hashCode;
    }

    public PlantType getPlantType() {
        return noCrop;
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

    public <T> @NotNull LazyOptional<T> getCapability(@NotNull Capability<T> cap, @Nullable Direction side) {
        if (this.capabilityComponents == null) {
            return super.getCapability(cap, side);
        } else {

            AbstractComponent comp = this.capabilityComponents.get(cap);
            return comp == null ? super.getCapability(cap, side) : LazyOptional.of(() -> comp.getCapability(cap, side)).cast();
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

            for (final Capability<?> capability : component.getProvidedCapabilities(null)) {
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

            for (final Capability<?> capability : component.getProvidedCapabilities(null)) {
                this.removeComponentCapability(capability, component);
            }

        }
    }

    public void addComponentCapability(Capability<?> cap, AbstractComponent component) {
        if (this.capabilityComponents == null) {
            this.capabilityComponents = new IdentityHashMap<>();
        }

        AbstractComponent prev = this.capabilityComponents.put(cap, component);

        assert prev == null;

    }

    public void removeComponentCapability(Capability<?> cap, AbstractComponent component) {
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
        this.loaded = false;
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

        return this.getLevel().isClientSide;
    }

    public Map<Capability<?>, AbstractComponent> getCapabilityComponents() {
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
        final CustomPacketBuffer packet = new CustomPacketBuffer();
        packet.writeShort(this.teBlock.getIDBlock());
        packet.writeString(this.active);
        packet.writeByte(this.facing);
        return packet;
    }

    public CompoundTag getNBTFromSlot(CustomPacketBuffer customPacketBuffer) {
        try {
            InvSlot slot = (InvSlot) DecoderHandler.decode(customPacketBuffer);
            return slot.writeToNbt(new CompoundTag());
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
