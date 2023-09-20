package com.denfop.tiles.base;

import com.denfop.IUCore;
import com.denfop.IUItem;
import com.denfop.api.tile.IMultiTileBlock;
import com.denfop.blocks.BlockResource;
import com.denfop.blocks.BlockTileEntity;
import com.denfop.componets.AbstractComponent;
import com.denfop.componets.Redstone;
import com.denfop.events.TickHandlerIU;
import com.denfop.invslot.InvSlot;
import com.denfop.network.DecoderHandler;
import com.denfop.network.packet.CustomPacketBuffer;
import com.denfop.network.packet.PacketRemoveUpdateTile;
import com.denfop.network.packet.PacketStopSound;
import com.denfop.network.packet.PacketUpdateFieldTile;
import com.denfop.network.packet.PacketUpdateTile;
import com.denfop.utils.ModUtils;
import net.minecraft.block.Block;
import net.minecraft.block.SoundType;
import net.minecraft.block.state.BlockFaceShape;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.network.play.server.SPacketUpdateTileEntity;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.ITickable;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.util.math.Vec3d;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.TextComponentString;
import net.minecraft.world.Explosion;
import net.minecraft.world.World;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import org.jetbrains.annotations.Nullable;

import javax.annotation.Nonnull;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.IdentityHashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

public abstract class TileEntityBlock extends TileEntity implements ITickable {


    public static final List<AxisAlignedBB> defaultAabbs = Collections.singletonList(new AxisAlignedBB(
            0.0,
            0.0,
            0.0,
            1.0,
            1.0,
            1.0
    ));
    public final IMultiTileBlock teBlock;
    public final BlockTileEntity block;
    public Map<Capability<?>, AbstractComponent> capabilityComponents;
    public IBlockState blockState = null;
    public List<AbstractComponent> componentList = new ArrayList<>();
    public List<AbstractComponent> updateServerList = new ArrayList<>();
    public List<AbstractComponent> updateClientList = new ArrayList<>();
    public Map<String, AbstractComponent> advComponentMap = new HashMap<>();

    public String active = "";
    public byte facing;
    public boolean isLoaded;

    public TileEntityBlock() {
        this.facing = (byte) EnumFacing.DOWN.ordinal();
        this.teBlock = getTeBlock();
        this.block = getBlock();

    }

    public static <T extends TileEntityBlock> T instantiate(Class<T> cls) {
        try {
            return cls.newInstance();
        } catch (Exception var2) {
            throw new RuntimeException(var2);
        }
    }

    public static boolean checkSide(List<AxisAlignedBB> aabbs, EnumFacing side, boolean strict) {
        if (aabbs == defaultAabbs) {
            return true;
        } else {
            int dx = side.getFrontOffsetX();
            int dy = side.getFrontOffsetY();
            int dz = side.getFrontOffsetZ();
            int xS = (dx + 1) / 2;
            int yS = (dy + 1) / 2;
            int zS = (dz + 1) / 2;
            int xE = (dx + 2) / 2;
            int yE = (dy + 2) / 2;
            int zE = (dz + 2) / 2;
            Iterator var12;
            AxisAlignedBB aabb;
            if (strict) {
                var12 = aabbs.iterator();

                while (var12.hasNext()) {
                    aabb = (AxisAlignedBB) var12.next();
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

                aabb = (AxisAlignedBB) var12.next();
            } while (!(aabb.minX <= (double) xS) || !(aabb.minY <= (double) yS) || !(aabb.minZ <= (double) zS) || !(aabb.maxX >= (double) xE) || !(aabb.maxY >= (double) yE) || !(aabb.maxZ >= (double) zE));

            return true;
        }
    }

    @Override
    public void setWorld(final World worldIn) {
        super.setWorld(worldIn);

    }

    @Override
    public void setPos(final BlockPos posIn) {
        super.setPos(posIn);

    }

    public NBTTagCompound getNBTFromSlot(CustomPacketBuffer customPacketBuffer) {
        try {
            InvSlot slot = (InvSlot) DecoderHandler.decode(customPacketBuffer);
            return slot.writeToNbt(new NBTTagCompound());
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

    }

    @Override
    public boolean shouldRefresh(final World world, final BlockPos pos, final IBlockState oldState, final IBlockState newSate) {
        if ((oldState.getBlock() instanceof BlockTileEntity)) {
            return false;
        }
        return newSate.getBlock() != oldState.getBlock();
    }

    public boolean onSneakingActivated(EntityPlayer player, EnumHand hand, EnumFacing side, float hitX, float hitY, float hitZ) {

        return this.getWorld().isRemote;
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
            try {
                this.facing = (byte) DecoderHandler.decode(is);
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }
        this.onNetworkUpdate(name);
    }

    public boolean hasCustomName() {
        return false;
    }


    public abstract IMultiTileBlock getTeBlock();

    public abstract BlockTileEntity getBlock();

    @Nonnull
    public ITextComponent getDisplayName() {
        return new TextComponentString(this.getName());
    }

    public final BlockTileEntity getBlockType() {
        return this.block;
    }

    public final IBlockState getBlockState() {
        if (this.blockState == null) {
            if (!this.teBlock.hasActive()) {
                if (this.active.contains("active")) {
                    this.active = "";
                }
            }

            this.blockState = this.block
                    .getDefaultState()
                    .withProperty(this.block.typeProperty, this.block.typeProperty.getState(this.teBlock, this.active))
                    .withProperty(
                            BlockTileEntity.facingProperty,
                            this.getFacing()
                    )
            ;
            return this.blockState;
        }
        return this.blockState;
    }

    public final void invalidate() {
        this.onUnloaded();
        super.invalidate();
    }

    public final void onChunkUnload() {
        this.onUnloaded();

        super.onChunkUnload();
    }

    public final void validate() {
        super.validate();
        World world = this.getWorld();
        if (this.pos != null) {
            TickHandlerIU.requestSingleWorldTick(world, world1 -> {
                TileEntityBlock.this.onLoaded();
            });
        }
    }


    public void onLoaded() {
        this.componentList.forEach(AbstractComponent::onLoaded);
        this.rerender();
        if (!this.getWorld().isRemote && this.needUpdate()) {
            IUCore.network.getServer().addTileToOvertimeUpdate(this);
        }
    }

    public void onUnloaded() {
        IUCore.network.getServer().removeTileToOvertimeUpdate(this);
        this.componentList.forEach(AbstractComponent::onUnloaded);
        try {

            new PacketStopSound(getWorld(), this.pos);
        } catch (Exception ignored) {
        }
        new PacketRemoveUpdateTile(this);
    }

    public void readFromNBT(NBTTagCompound nbt) {
        super.readFromNBT(nbt);
        if (!this.getSupportedFacings().isEmpty()) {
            byte facingValue = nbt.getByte("facing");
            if (facingValue >= 0 && facingValue < EnumFacing.VALUES.length && this
                    .getSupportedFacings()
                    .contains(EnumFacing.VALUES[facingValue])) {
                this.facing = facingValue;
            } else if (!this.getSupportedFacings().isEmpty()) {
                this.facing = (byte) this.getSupportedFacings().iterator().next().ordinal();
            } else {
                this.facing = (byte) EnumFacing.DOWN.ordinal();
            }
        }

        this.active = nbt.getString("active");
        if (!this.componentList.isEmpty() && nbt.hasKey("components", 10)) {
            NBTTagCompound componentsNbt = nbt.getCompoundTag("components");
            for (int i = 0; i < this.componentList.size(); i++) {
                final AbstractComponent component = this.componentList.get(i);
                NBTTagCompound componentNbt = componentsNbt.getCompoundTag("component_" + i);
                component.readFromNbt(componentNbt);
            }
        }
    }

    public NBTTagCompound writeToNBT(NBTTagCompound nbt) {
        super.writeToNBT(nbt);
        if (!this.getSupportedFacings().isEmpty()) {
            nbt.setByte("facing", this.facing);
        }

        nbt.setString("active", this.active);
        if (!this.componentList.isEmpty()) {
            NBTTagCompound componentsNbt = new NBTTagCompound();

            for (int i = 0; i < this.componentList.size(); i++) {
                final AbstractComponent component = this.componentList.get(i);
                NBTTagCompound nbt1 = component.writeToNbt();
                if (nbt1 == null) {
                    nbt1 = new NBTTagCompound();
                }
                componentsNbt.setTag("component_" + i, nbt1);
            }
            nbt.setTag("components", componentsNbt);
        }

        return nbt;
    }

    public NBTTagCompound getUpdateTag() {
        new PacketUpdateTile(this);
        return new NBTTagCompound();
    }

    public SPacketUpdateTileEntity getUpdatePacket() {
        new PacketUpdateTile(this);
        return null;
    }

    public final void update() {

        if (this.getWorld().isRemote) {
            this.updateEntityClient();
        } else {
            this.updateEntityServer();
        }


    }

    @SideOnly(Side.CLIENT)
    public void updateEntityClient() {
        this.updateClientList.forEach(AbstractComponent::updateEntityClient);

    }

    public void updateEntityServer() {
        for (AbstractComponent component : this.updateServerList) {
            component.updateEntityServer();
        }
        if (!isLoaded) {
            this.loadBeforeFirstUpdate();
        }

    }

    public void loadBeforeFirstUpdate() {
        isLoaded = true;
        try {
            this.rerender();
        } catch (Exception e) {
        }
    }

    @Override
    public void markDirty() {
        super.markDirty();
        if (IUCore.proxy.isSimulating()) {
            for (final AbstractComponent abstractComponent : this.componentList) {
                abstractComponent.markDirty();
            }
        }
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

    public List<ItemStack> getSelfDrops(int fortune, boolean wrench) {
        ItemStack drop = this.getPickBlock(null, null);
        drop = this.adjustDrop(drop, wrench);
        if (!drop.isEmpty()) {
            for (AbstractComponent component : this.componentList) {
                if (component.needWriteNBTToDrops()) {
                    NBTTagCompound tagCompound = ModUtils.nbt(drop);
                    tagCompound.setTag(component.toString(), component.writeNBTToDrops(new NBTTagCompound()));
                }
            }
        }
        return Collections.singletonList(drop);
    }

    public boolean wrenchCanRemove(final EntityPlayer player) {
        for (AbstractComponent component : this.componentList) {
            if (!component.wrenchCanRemove(player)) {
                return false;
            }
        }
        return true;
    }

    public void onPlaced(ItemStack stack, EntityLivingBase placer, EnumFacing facing) {
        World world = this.getWorld();

        facing = this.getPlacementFacing(placer, facing);
        if (facing != this.getFacing()) {
            this.setFacing(facing);
        }

        if (world.isRemote) {
            this.rerender();
        }
        for (AbstractComponent component : this.componentList) {
            if (component.needWriteNBTToDrops()) {
                NBTTagCompound tagCompound = ModUtils.nbt(stack);
                final NBTTagCompound tag = tagCompound.getCompoundTag(component.toString());
                component.readFromNbt(tag);
            }
            component.onPlaced(stack, placer, facing);
        }

    }

    public AxisAlignedBB getVisualBoundingBox() {
        return this.getAabb(false);
    }

    public AxisAlignedBB getPhysicsBoundingBox() {
        return this.getAabb(true);
    }

    public AxisAlignedBB getOutlineBoundingBox() {
        return this.getVisualBoundingBox();
    }

    public void addCollisionBoxesToList(AxisAlignedBB mask, List<AxisAlignedBB> list, Entity collidingEntity) {
        AxisAlignedBB maskNormalized = mask.offset(
                -this.pos.getX(),
                -this.pos.getY(),
                -this.pos.getZ()
        );
        for (final AxisAlignedBB aabb : this.getAabbs(true)) {
            if (aabb.intersects(maskNormalized)) {
                list.add(aabb.offset(this.pos));
            }
        }

    }

    public AxisAlignedBB getAabb(boolean forCollision) {
        List<AxisAlignedBB> aabbs = this.getAabbs(forCollision);
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

            AxisAlignedBB aabb;
            for (Iterator var15 = aabbs.iterator(); var15.hasNext(); zE = Math.max(zE, aabb.maxZ)) {
                aabb = (AxisAlignedBB) var15.next();
                xS = Math.min(xS, aabb.minX);
                yS = Math.min(yS, aabb.minY);
                zS = Math.min(zS, aabb.minZ);
                xE = Math.max(xE, aabb.maxX);
                yE = Math.max(yE, aabb.maxY);
            }

            return new AxisAlignedBB(xS, yS, zS, xE, yE, zE);
        }
    }

    public void onEntityCollision(Entity entity) {
    }

    @SideOnly(Side.CLIENT)
    public boolean shouldSideBeRendered(EnumFacing side, BlockPos otherPos) {
        AxisAlignedBB aabb = this.getVisualBoundingBox();
        if (aabb != defaultAabbs) {
            switch (side) {
                case DOWN:
                    if (aabb.minY > 0.0) {
                        return true;
                    }
                    break;
                case UP:
                    if (aabb.maxY < 1.0) {
                        return true;
                    }
                    break;
                case NORTH:
                    if (aabb.minZ > 0.0) {
                        return true;
                    }
                    break;
                case SOUTH:
                    if (aabb.maxZ < 1.0) {
                        return true;
                    }
                    break;
                case WEST:
                    if (aabb.minX > 0.0) {
                        return true;
                    }
                    break;
                case EAST:
                    if (aabb.maxX < 1.0) {
                        return true;
                    }
            }
        }

        World world = this.getWorld();
        return !world.getBlockState(otherPos).doesSideBlockRendering(world, otherPos, side.getOpposite());
    }

    public boolean doesSideBlockRendering(EnumFacing side) {
        return checkSide(this.getAabbs(false), side, false);
    }

    public boolean isNormalCube() {
        List<AxisAlignedBB> aabbs = this.getAabbs(false);
        if (aabbs == defaultAabbs) {
            return true;
        } else if (aabbs.size() != 1) {
            return false;
        } else {
            AxisAlignedBB aabb = aabbs.get(0);
            return aabb.minX <= 0.0 && aabb.minY <= 0.0 && aabb.minZ <= 0.0 && aabb.maxX >= 1.0 && aabb.maxY >= 1.0 && aabb.maxZ >= 1.0;
        }
    }

    public boolean isSideSolid(EnumFacing side) {
        return checkSide(this.getAabbs(false), side, true);
    }

    public BlockFaceShape getFaceShape(EnumFacing face) {
        return this.isSideSolid(face) ? BlockFaceShape.SOLID : BlockFaceShape.UNDEFINED;
    }

    public int getLightOpacity() {
        return this.isNormalCube() ? 255 : 0;
    }

    public int getLightValue() {
        return 0;
    }

    public boolean onActivated(EntityPlayer player, EnumHand hand, EnumFacing side, float hitX, float hitY, float hitZ) {
        return false;
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

    public CustomPacketBuffer writePacket() {
        final CustomPacketBuffer packet = new CustomPacketBuffer();
        packet.writeString(this.teBlock.getIdentifier() + "=" + this.teBlock.getName());
        packet.writeString(this.active);
        packet.writeByte(this.facing);
        return packet;
    }

    public void readPacket(CustomPacketBuffer customPacketBuffer) {
        this.active = customPacketBuffer.readString();
        this.facing = customPacketBuffer.readByte();
        this.rerender();
    }

    public void onClicked(EntityPlayer player) {
    }

    public void onNeighborChange(Block neighbor, BlockPos neighborPos) {
        if (this.componentList != null) {
            for (final AbstractComponent component : componentList) {
                component.onNeighborChange(neighbor, neighborPos);
            }
        }

    }

    public int getWeakPower(EnumFacing side) {
        return 0;
    }

    public boolean canConnectRedstone(EnumFacing side) {
        return this.hasComponent(Redstone.class);
    }

    public int getComparatorInputOverride() {
        return 0;
    }

    public <T extends AbstractComponent> T getComp(Class<T> cls) {
        for (final AbstractComponent component : componentList) {
            if (component.getClass() == cls) {
                return (T) component;
            }
        }
        return null;
    }


    public boolean hasComponent(Class<? extends AbstractComponent> cls) {
        for (final AbstractComponent component : componentList) {
            if (component.getClass() == cls) {
                return true;
            }
        }
        return false;
    }

    public List<AbstractComponent> getComponentList() {
        return componentList;
    }


    public <T extends AbstractComponent> T getComponent(String cls) {
        for (AbstractComponent component : this.componentList) {
            if (component.toString().trim().equals(cls)) {
                return (T) component;
            }
        }
        return null;
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

    @Nullable
    @Override
    public <T> T getCapability(final Capability<T> capability, @Nullable final EnumFacing facing) {
        if (this.capabilityComponents == null) {
            return super.getCapability(capability, facing);
        } else {

            AbstractComponent comp = this.capabilityComponents.get(capability);
            return comp == null ? super.getCapability(capability, facing) : comp.getCapability(capability, facing);
        }
    }

    @Override
    public boolean hasCapability(final Capability<?> capability, @Nullable final EnumFacing facing) {
        if (this.capabilityComponents == null) {
            return false;
        } else {
            AbstractComponent comp = this.capabilityComponents.get(capability);
            return comp != null && comp.getProvidedCapabilities(facing).contains(capability);
        }
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

    public Map<Capability<?>, AbstractComponent> getCapabilityComponents() {
        return capabilityComponents;
    }

    public <T extends AbstractComponent> T getComponent(Class<T> cls) {
        for (final AbstractComponent component : componentList) {
            if (component.getClass() == cls) {
                return (T) component;
            }
        }
        return null;
    }

    public Iterable<? extends AbstractComponent> getComps() {
        return componentList;
    }

    public void onExploded(Explosion explosion) {
    }

    public void onBlockBreak(boolean wrench) {

    }

    public void wrenchBreak() {
        this.onBlockBreak(true);
    }

    public boolean onRemovedByPlayer(EntityPlayer player, boolean willHarvest) {
        return true;
    }


    public ItemStack getPickBlock(EntityPlayer player, RayTraceResult target) {
        return this.block.getItemStack(this.teBlock);
    }

    public List<ItemStack> getAuxDrops(int fortune) {
        return Collections.emptyList();
    }

    public float getHardness() {
        return this.teBlock.getHardness();
    }


    public EnumFacing getFacing() {
        return EnumFacing.VALUES[this.facing];
    }

    public void setFacing(EnumFacing facing) {
        if (facing == null) {
            throw new NullPointerException("null facing");
        } else if (this.facing == facing.ordinal()) {
            throw new IllegalArgumentException("unchanged facing");
        } else if (!this.getSupportedFacings().contains(facing)) {
            throw new IllegalArgumentException("invalid facing: " + facing + ", supported: " + this.getSupportedFacings());
        } else {
            this.facing = (byte) facing.ordinal();
            if (!this.getWorld().isRemote) {
                new PacketUpdateFieldTile(this, "facing", this.facing);
            }

        }
    }

    public boolean canSetFacingWrench(EnumFacing facing, EntityPlayer player) {
        if (!this.teBlock.allowWrenchRotating()) {
            return false;
        } else if (facing == this.getFacing()) {
            return false;
        } else {
            return this.getSupportedFacings().contains(facing);
        }
    }

    public boolean setFacingWrench(EnumFacing facing, EntityPlayer player) {
        if (!this.canSetFacingWrench(facing, player)) {
            return false;
        } else {
            this.setFacing(facing);
            return true;
        }
    }


    public List<ItemStack> getWrenchDrops(EntityPlayer player, int fortune) {
        List<ItemStack> ret = new ArrayList();
        ret.addAll(this.getSelfDrops(fortune, true));
        ret.addAll(this.getAuxDrops(fortune));
        return ret;
    }


    public SoundType getBlockSound(Entity entity) {
        return SoundType.STONE;
    }

    public EnumFacing getPlacementFacing(EntityLivingBase placer, EnumFacing facing) {
        Set<EnumFacing> supportedFacings = this.getSupportedFacings();
        if (supportedFacings.isEmpty()) {
            return EnumFacing.DOWN;
        } else if (placer != null) {
            Vec3d dir = placer.getLookVec();
            EnumFacing bestFacing = null;
            double maxMatch = Double.NEGATIVE_INFINITY;

            for (final EnumFacing cFacing : supportedFacings) {
                double match = dir.dotProduct(new Vec3d(cFacing.getOpposite().getDirectionVec()));
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

    @Nonnull
    public String getName() {
        String name = teBlock == null ? "invalid" : teBlock.getName();
        return this.getBlockType().getUnlocalizedName() + "." + name;
    }

    public List<AxisAlignedBB> getAabbs(boolean forCollision) {
        return defaultAabbs;
    }

    public ItemStack adjustDrop(ItemStack drop, boolean wrench) {
        if (!wrench) {
            switch (this.teBlock.getDefaultDrop()) {
                case Self:
                default:
                    break;
                case Generator:
                    drop = new ItemStack(IUItem.basemachine2, 1, 78);
                    break;
                case None:
                    drop = null;
                    break;
                case Machine:
                    return IUItem.blockResource.getItemStack(BlockResource.Type.machine);
                case AdvMachine:
                    return IUItem.blockResource.getItemStack(BlockResource.Type.advanced_machine);
            }
        }

        return drop;
    }

    public Set<EnumFacing> getSupportedFacings() {
        return this.teBlock.getSupportedFacings();
    }

    public boolean getActive() {
        return this.active.contains("active");
    }

    public void setActive(boolean active) {
        if (!active && this.active.equals("")) {
            return;
        }

        if (active) {
            if (!this.active.equals("active")) {
                this.active = "active";
                new PacketUpdateFieldTile(this, "active", this.active);
            }
        } else {
            this.active = "";
            new PacketUpdateFieldTile(this, "active", this.active);
        }
    }

    public void setActive(String active) {
        if (this.active.equals(active)) {
            return;
        }

        this.active = active;
        new PacketUpdateFieldTile(this, "active", this.active);
    }

    @SideOnly(Side.CLIENT)
    public void addInformation(ItemStack stack, List<String> tooltip, ITooltipFlag advanced) {


    }

    public final void rerender() {
        IBlockState state1 = this.blockState;
        this.blockState = null;
        IBlockState state = this.getBlockState();
        if (state1 == null) {
            state1 = state;
        }
        this.getWorld().notifyBlockUpdate(this.pos, state, state, 2);
    }

    public boolean clientNeedsExtraModelInfo() {
        return false;
    }


    public ItemStack getItem(EntityPlayer player, RayTraceResult target) {
        return this.block.getItemStack(this.teBlock);
    }

    public <T extends AbstractComponent> T getComp(String cls) {
        for (AbstractComponent component : this.componentList) {
            if (component.toString().trim().equals(cls)) {
                return (T) component;
            }
        }
        return null;
    }


}
