package com.denfop.tiles.transport.tiles;

import com.denfop.Constants;
import com.denfop.IUItem;
import com.denfop.api.tile.IMultiTileBlock;
import com.denfop.api.transport.ITransportConductor;
import com.denfop.blocks.BlockTileEntity;
import com.denfop.container.ContainerBase;
import com.denfop.container.ContainerCable;
import com.denfop.datacomponent.ContainerItem;
import com.denfop.datacomponent.DataComponentsInit;
import com.denfop.gui.GuiCable;
import com.denfop.gui.GuiCore;
import com.denfop.network.DecoderHandler;
import com.denfop.network.EncoderHandler;
import com.denfop.network.IUpdatableTileEvent;
import com.denfop.network.packet.CustomPacketBuffer;
import com.denfop.network.packet.PacketUpdateFieldTile;
import com.denfop.render.transport.DataCable;
import com.denfop.tiles.base.TileEntityInventory;
import com.denfop.tiles.transport.types.ICableItem;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.SoundType;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.Vec3;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.api.distmarker.OnlyIn;
import net.neoforged.neoforge.capabilities.BlockCapability;
import net.neoforged.neoforge.capabilities.Capabilities;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.io.IOException;
import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.util.*;

import static com.denfop.blocks.BlockTileEntity.*;

public class TileEntityMultiCable extends TileEntityInventory implements IUpdatableTileEvent {

    public static final NumberFormat lossFormat = new DecimalFormat("0.00#");
    public static List<BlockEntityType<? extends TileEntityMultiCable>> list = new ArrayList<>();
    public ICableItem cableItem;
    public byte connectivity;
    public ItemStack stackFacade = ItemStack.EMPTY;
    @OnlyIn(Dist.CLIENT)
    public DataCable dataCable;
    private ResourceLocation texture;
    private List<Direction> blackList = new ArrayList<>();

    public TileEntityMultiCable(ICableItem name, IMultiTileBlock tileBlock, BlockPos pos, BlockState state) {
        super(tileBlock, pos, state);
        this.cableItem = name;
        this.connectivity = 0;
        if (list != null) {
            if (pos.equals(BlockPos.ZERO)) {
                list.add((BlockEntityType<? extends TileEntityMultiCable>) tileBlock.getBlockType());
            }
        }
    }

    public List<Direction> getBlackList() {
        return blackList;
    }

    public ICableItem getCableItem() {
        return cableItem;
    }

    public ResourceLocation getTexture() {
        if (this.texture == null) {
            this.texture = ResourceLocation.tryBuild(
                    Constants.MOD_ID,
                    "blocks/wiring/" + getCableItem().getMainPath() + "/" + getCableItem()
                            .getNameCable()
            );
        }
        return this.texture;
    }

    public void removeConductor() {
        this.getWorld().setBlock(this.pos, Blocks.AIR.defaultBlockState(), 3);

    }

    @Override
    public CompoundTag writeToNBT(CompoundTag nbt) {
        nbt = super.writeToNBT(nbt);
        final CompoundTag nbt1 = new CompoundTag();
        if (!this.blackList.isEmpty()) {
            nbt1.putInt("size", this.blackList.size());
            for (int i = 0; i < this.blackList.size(); i++) {
                nbt1.putInt(String.valueOf(i), this.blackList.get(i).ordinal());
            }
            nbt.put("list", nbt1);
        }
        if (this.stackFacade != null && !this.stackFacade.isEmpty()) {
            final CompoundTag nbt2 = new CompoundTag();
            this.stackFacade.save(provider, nbt2);
            nbt.put("stackFacade", nbt2);
        }
        return nbt;
    }

    @Override
    public <T> T getCapability(@NotNull BlockCapability<T, Direction> cap, @Nullable Direction side) {
        if (cap == Capabilities.ItemHandler.BLOCK)
            return null;
        return super.getCapability(cap, side);
    }


    @Override
    public void readFromNBT(final CompoundTag nbtTagCompound) {
        super.readFromNBT(nbtTagCompound);
        if (nbtTagCompound.contains("list")) {
            final CompoundTag tagList = nbtTagCompound.getCompound("list");
            final int size = tagList.getInt("size");
            for (int i = 0; i < size; i++) {
                this.blackList.add(Direction.values()[tagList.getInt(String.valueOf(i))]);
            }
        }
        if (nbtTagCompound.contains("stackFacade")) {
            final CompoundTag stackFacade = nbtTagCompound.getCompound("stackFacade");
            this.stackFacade = ItemStack.parseOptional(provider, stackFacade);
        }
    }

    @Override
    public void onLoaded() {
        super.onLoaded();
        if (!this.getWorld().isClientSide) {
            new PacketUpdateFieldTile(this, "stackFacade", this.stackFacade);
            this.updateConnectivity();
        }
    }


    public List<ItemStack> getAuxDrops(int fortune) {
        return Collections.emptyList();
    }

    public SoundType getBlockSound(Entity entity) {
        return SoundType.WOOL;
    }

    public AABB getVisualBoundingBox() {
        return super.getVisualBoundingBox();

    }

    public List<AABB> getAabbs(boolean forCollision) {
        if (this.stackFacade == null || this.stackFacade.isEmpty() && !forCollision) {
            float th = this.cableItem.getThickness();
            float sp = (1.0F - th) / 2.0F;
            List<AABB> ret = new ArrayList<>();
            ret.add(new AABB(
                    sp,
                    sp,
                    sp,
                    sp + th,
                    sp + th,
                    sp + th
            ));
            Direction[] var5 = Direction.values();
            for (Direction facing : var5) {
                boolean hasConnection = (this.connectivity & 1 << facing.ordinal()) != 0;
                if (hasConnection) {
                    float zS = sp;
                    float yS = sp;
                    float xS = sp;
                    float yE;
                    float zE;
                    float xE = yE = zE = sp + th;
                    switch (facing) {
                        case DOWN:
                            xS = sp + th;
                            xE = 1.0F;
                            break;
                        case UP:
                            xS = 0.0F;
                            xE = sp;
                            break;
                        case NORTH:
                            zS = sp + th;
                            zE = 1.0F;
                            break;
                        case SOUTH:
                            zS = 0.0F;
                            zE = sp;
                            break;
                        case WEST:
                            yS = sp + th;
                            yE = 1.0F;
                            break;
                        case EAST:
                            yS = 0.0F;
                            yE = sp;
                            break;
                        default:
                            throw new RuntimeException();
                    }

                    ret.add(new AABB(xS, yS, zS, xE, yE, zE));
                }
            }

            return ret;
        } else {
            return super.getAabbs(forCollision);
        }
    }


    @Override
    public IMultiTileBlock getTeBlock() {
        return null;
    }

    @Override
    public BlockTileEntity getBlock() {
        return null;
    }


    public void setConnectivity(final byte connectivity) {
        if (this.connectivity != connectivity) {
            this.connectivity = connectivity;
            new PacketUpdateFieldTile(this, "connectivity", this.connectivity);
            Direction[] var5 = Direction.values();
            Map<Direction, Boolean> booleanMap = new HashMap<>();
            for (Direction facing : var5) {
                boolean hasConnection = (this.connectivity & 1 << facing.ordinal()) != 0;
                booleanMap.put(facing, hasConnection);
            }
            this.setBlockState(this.getBlockState().setValue(NORTH, booleanMap.get(Direction.SOUTH))
                    .setValue(SOUTH, booleanMap.get(Direction.NORTH))
                    .setValue(WEST, booleanMap.get(Direction.UP))
                    .setValue(EAST, booleanMap.get(Direction.DOWN))
                    .setValue(UP, booleanMap.get(Direction.WEST))
                    .setValue(DOWN, booleanMap.get(Direction.EAST)));
            this.getWorld().setBlock(this.worldPosition, getBlockState(), 3);

        }
    }

    @Override
    public boolean onSneakingActivated(Player player, InteractionHand hand, Direction side, Vec3 vec3) {
        if (level.isClientSide) {
            return false;
        }
        final ItemStack stack = player.getItemInHand(hand);
        if (stack.isEmpty()) {
            if (!this.stackFacade.isEmpty()) {
                this.stackFacade = ItemStack.EMPTY;
                new PacketUpdateFieldTile(this, "stackFacade", stackFacade);
            }
        }
        return super.onSneakingActivated(player, hand, side, vec3);
    }

    @Override
    @OnlyIn(Dist.CLIENT)
    public GuiCore<ContainerBase<?>> getGui(final Player var1, final ContainerBase<?> var2) {
        return new GuiCable(getGuiContainer(var1));
    }

    @Override
    public ContainerCable getGuiContainer(final Player var1) {
        return new ContainerCable(var1, this);
    }

    @Override
    public BlockState getBlockState() {
        if (this.blockState == null) {
            try {
                Direction[] var5 = Direction.values();
                Map<Direction, Boolean> booleanMap = new HashMap<>();
                for (Direction facing : var5) {
                    boolean hasConnection = (this.connectivity & 1 << facing.ordinal()) != 0;
                    booleanMap.put(facing, hasConnection);
                }
                this.blockState = this.block
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
                        .setValue(DOWN, booleanMap.get(Direction.EAST));
                ;
            } catch (Exception e) {
                this.blockState = this.block
                        .defaultBlockState();
            }
            return this.blockState;
        }
        return this.blockState;
    }

    public void updateConnectivity() {

    }

    @Override
    public boolean onActivated(Player player, InteractionHand hand, Direction side, Vec3 vec3) {
        final ItemStack stack = player.getItemInHand(hand);
        if (!this.getWorld().isClientSide && stack.getItem() == IUItem.facadeItem.getItem() && stack.has(DataComponentsInit.CONTAINER) && !stack.get(DataComponentsInit.CONTAINER).listItem().isEmpty()) {
            ContainerItem containerItem = stack.get(DataComponentsInit.CONTAINER);
            this.stackFacade = containerItem.listItem().get(0).copy();
            final Block block = this.stackFacade.getItem() instanceof BlockItem ? ((BlockItem) this.stackFacade.getItem()).getBlock() : Blocks.AIR;
            if (block != Blocks.AIR) {
                this.stackFacade = this.stackFacade.copy();
                new PacketUpdateFieldTile(this, "stackFacade", stackFacade);
            } else {
                this.stackFacade = ItemStack.EMPTY;
            }
        } else if (stack.getItem() == IUItem.connect_item.getItem()) {
            return super.onActivated(player, hand, side, vec3);
        }
        if (this instanceof ITransportConductor) {
            boolean can = ((ITransportConductor) this).isInput() || ((ITransportConductor) this).isOutput();
            if (can) {
                return super.onActivated(player, hand, side, vec3);
            } else {
                return false;
            }
        }
        return false;

    }

    public void rerenderCable(ItemStack stack) {
        if (stack != ItemStack.EMPTY) {
            this.stackFacade = stack.copy();
        } else {
            this.stackFacade = stack;
        }
        new PacketUpdateFieldTile(this, "stackFacade", stackFacade);
    }


    public void updateField(String name, CustomPacketBuffer is) {
        super.updateField(name, is);
        if (name.equals("connectivity")) {
            try {
                this.connectivity = (byte) DecoderHandler.decode(is);
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
                this.getWorld().setBlock(this.worldPosition, super.getBlockState(), 3);
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }
        if (name.equals("stackFacade")) {
            try {
                this.stackFacade = (ItemStack) DecoderHandler.decode(is);
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }
    }

    @Override
    public CustomPacketBuffer writePacket() {
        final CustomPacketBuffer packet = super.writePacket();
        try {
            EncoderHandler.encode(packet, this.stackFacade);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        return packet;
    }

    @Override
    public void readPacket(final CustomPacketBuffer customPacketBuffer) {
        super.readPacket(customPacketBuffer);
        try {
            stackFacade = (ItemStack) DecoderHandler.decode(customPacketBuffer);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public CustomPacketBuffer writeContainerPacket() {
        final CustomPacketBuffer buffer = super.writeContainerPacket();
        try {
            EncoderHandler.encode(buffer, this.blackList);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        return buffer;
    }

    @Override
    public void readContainerPacket(final CustomPacketBuffer customPacketBuffer) {
        super.readContainerPacket(customPacketBuffer);
        try {
            this.blackList = (List<Direction>) DecoderHandler.decode(customPacketBuffer);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public boolean hasSpecialModel() {
        return true;
    }


    @Override
    public void updateTileServer(final Player var1, final double var2) {
        byte event1 = (byte) var2;
        Direction facing1 = Direction.values()[event1];
        if (this.blackList.contains(facing1)) {
            this.blackList.remove(facing1);
        } else {
            this.blackList.add(facing1);
        }
    }

}
