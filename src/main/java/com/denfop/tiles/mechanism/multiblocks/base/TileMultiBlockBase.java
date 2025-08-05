package com.denfop.tiles.mechanism.multiblocks.base;

import com.denfop.IUCore;
import com.denfop.Localization;
import com.denfop.api.multiblock.IMainMultiBlock;
import com.denfop.api.multiblock.MultiBlockStructure;
import com.denfop.api.tile.IMultiTileBlock;
import com.denfop.events.client.GlobalRenderManager;
import com.denfop.mixin.access.LevelRendererAccessor;
import com.denfop.network.DecoderHandler;
import com.denfop.network.EncoderHandler;
import com.denfop.network.IUpdatableTileEvent;
import com.denfop.network.packet.CustomPacketBuffer;
import com.denfop.network.packet.PacketUpdateFieldTile;
import com.denfop.tiles.base.TileEntityInventory;
import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.math.Axis;
import net.minecraft.ChatFormatting;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.LevelRenderer;
import net.minecraft.client.renderer.entity.ItemRenderer;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.client.resources.model.BakedModel;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemDisplayContext;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.Vec3;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.client.event.RenderLevelStageEvent;

import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.function.Function;

public abstract class TileMultiBlockBase extends TileEntityInventory implements IMainMultiBlock,
        IUpdatableTileEvent {

    private MultiBlockStructure multiBlockStructure;
    public boolean full;
    public boolean activate;
    public List<Player> entityPlayerList;
    public boolean visible = true;
    @OnlyIn(Dist.CLIENT)
    private Function render;

    public TileMultiBlockBase(MultiBlockStructure multiBlockStructure, IMultiTileBlock multiTileBlock, BlockPos pos, BlockState state) {
        super(multiTileBlock, pos, state);
        this.multiBlockStructure = multiBlockStructure;
        this.full = false;
        this.entityPlayerList = new ArrayList<>();
        this.activate = false;
    }

    @OnlyIn(Dist.CLIENT)
    public static Function<RenderLevelStageEvent, Void> createFunction(TileMultiBlockBase te) {
        Function<RenderLevelStageEvent, Void> function = o -> {
            PoseStack poseStack = o.getPoseStack();
            poseStack.pushPose();
            poseStack.translate(te.getBlockPos().getX() + 0.5f, te.getBlockPos().getY(), te.getBlockPos().getZ() + 0.5f);
            RenderSystem.setShaderColor(1.0F, 1.0F, 1.0F, 1.0F);
            te.render(te, o);

            poseStack.popPose();

            return null;
        };
        return function;
    }

    @Override
    public int getBlockLevel() {
        return 0;
    }

    @Override
    public void readContainerPacket(final CustomPacketBuffer customPacketBuffer) {
        super.readContainerPacket(customPacketBuffer);
        if (!this.isFull()) {
            this.updateFull();
            this.full = true;
            this.activate = true;
        }
    }

    @Override
    public boolean isMain() {
        return true;
    }

    @Override
    public void addInformation(final ItemStack stack, final List<String> tooltip) {
        super.addInformation(stack, tooltip);
        tooltip.add(Localization.translate("iu.multiblock.info_visible_structure"));
        if (level == null) {
            tooltip.add(Localization.translate("multiblock.jei1"));
            if (getMultiBlockStucture() != null) {
                for (ItemStack stack1 : getMultiBlockStucture().itemStackList) {
                    if (!stack1.isEmpty()) {
                        tooltip.add(ChatFormatting.GREEN + "" + stack1.getCount() + "x" + ChatFormatting.GRAY + stack1.getDisplayName().getString());
                    }
                }
            }else{
                try {
                    this.multiBlockStructure = ((TileMultiBlockBase)this.getTeBlock().getTeClass().getConstructors()[0].newInstance(BlockPos.ZERO, this.getBlockState())).multiBlockStructure;
                } catch (InstantiationException e) {
                    throw new RuntimeException(e);
                } catch (IllegalAccessException e) {
                    throw new RuntimeException(e);
                } catch (InvocationTargetException e) {
                    throw new RuntimeException(e);
                }
            }
        }
    }

    private Direction rotateFacing(Direction original, Direction baseRotation) {
        if (original == Direction.UP || original == Direction.DOWN) {
            return original;
        }

        switch (baseRotation) {
            case EAST:
                return rotate90(original);
            case WEST:
                return rotate90(rotate90(rotate90(original)));
            case SOUTH:
                return rotate90(rotate90(original));
            default:
                return original;
        }
    }

    private Direction rotate90(Direction facing) {
        switch (facing) {
            case NORTH:
                return Direction.EAST;
            case EAST:
                return Direction.SOUTH;
            case SOUTH:
                return Direction.WEST;
            case WEST:
                return Direction.NORTH;
            default:
                return facing;
        }
    }

    public MultiBlockStructure getMultiBlockStructure() {
        return multiBlockStructure;
    }

    public CustomPacketBuffer writePacket() {
        final CustomPacketBuffer packet = super.writePacket();
        try {
            EncoderHandler.encode(packet, full);
            EncoderHandler.encode(packet, activate);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        return packet;
    }

    public boolean onRemovedByPlayer(Player player, boolean willHarvest) {

        return true;
    }

    public void readPacket(CustomPacketBuffer customPacketBuffer) {
        super.readPacket(customPacketBuffer);
        try {
            full = (boolean) DecoderHandler.decode(customPacketBuffer);
            activate = (boolean) DecoderHandler.decode(customPacketBuffer);
            if (full && activate) {
                updateFull();
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public abstract void updateAfterAssembly();

    public abstract void usingBeforeGUI();

    @OnlyIn(Dist.CLIENT)
    public void render(
            TileMultiBlockBase tileEntityMultiBlockBase, RenderLevelStageEvent event
    ) {
        if (!this.isFull()) {
            renderBlock(tileEntityMultiBlockBase, event);
        } else if (this.getMultiBlockStucture().hasUniqueModels) {
            renderUniqueMultiBlock(event);
        }
    }

    private void rotateBlocks() {
        for (Map.Entry<BlockPos, Direction> entry : this.multiBlockStructure.RotationMap.entrySet()) {
            BlockPos pos1;
            Direction rotation = entry.getValue();
            pos1 = switch (this.getFacing()) {
                case NORTH -> entry.getKey();
                case EAST -> new BlockPos(-entry.getKey().getZ(), entry.getKey().getY(), entry.getKey().getX());
                case WEST -> new BlockPos(entry.getKey().getZ(), entry.getKey().getY(), -entry.getKey().getX());
                case SOUTH -> new BlockPos(-entry.getKey().getX(), entry.getKey().getY(), -entry.getKey().getZ());
                default -> throw new IllegalStateException("Unexpected value: " + this.getFacing());
            };
            Direction facing = this.getFacing();
            pos1 = pos.offset(pos1);
            if (pos1.equals(pos)) {
                continue;
            }
            if (facing == Direction.NORTH) {
                if (rotation == Direction.EAST || rotation == Direction.WEST) {
                    rotation = rotation.getOpposite();
                }
            } else if (facing == Direction.SOUTH) {
                if (rotation == Direction.SOUTH || rotation == Direction.NORTH) {
                    rotation = rotation.getOpposite();
                }
            } else if (facing == Direction.EAST) {
                if (rotation == Direction.EAST || rotation == Direction.WEST) {
                    if (rotation == Direction.EAST) {
                        rotation = Direction.NORTH;
                    } else {
                        rotation = Direction.SOUTH;
                    }
                } else {
                    if (rotation == Direction.SOUTH) {
                        rotation = Direction.WEST;
                    } else {
                        rotation = Direction.EAST;
                    }
                }
            } else if (facing == Direction.WEST) {
                if (rotation == Direction.EAST || rotation == Direction.WEST) {
                    if (rotation == Direction.WEST) {
                        rotation = Direction.NORTH;
                    } else {
                        rotation = Direction.SOUTH;
                    }
                } else {
                    if (rotation == Direction.SOUTH) {
                        rotation = Direction.EAST;
                    } else {
                        rotation = Direction.WEST;
                    }
                }
            }

            TileEntityMultiBlockElement multiBlockElement = (TileEntityMultiBlockElement) this.getWorld().getBlockEntity(pos1);
            if (multiBlockElement != null) {
                multiBlockElement.setFacing(rotation);
            }
        }

    }

    @OnlyIn(Dist.CLIENT)
    private void renderBlock(
            TileMultiBlockBase tile, RenderLevelStageEvent event
    ) {
        if (facing == 0 || facing == 1 || !visible) {
            return;
        }
        for (Map.Entry<BlockPos, ItemStack> entry : this.multiBlockStructure.ItemStackMap.entrySet()) {
            BlockPos pos1;
            if (entry.getValue().isEmpty()) {
                continue;
            }
            Direction rotation = this.multiBlockStructure.RotationMap.get(entry.getKey());
            switch (this.getFacing()) {
                case NORTH:
                    pos1 = new BlockPos(entry.getKey().getX(), entry.getKey().getY(), entry.getKey().getZ());
                    break;
                case EAST:
                    pos1 = new BlockPos(entry.getKey().getZ() * -1, entry.getKey().getY(), entry.getKey().getX());
                    break;
                case WEST:
                    pos1 = new BlockPos(entry.getKey().getZ(), entry.getKey().getY(), entry.getKey().getX() * -1);
                    break;
                case SOUTH:
                    pos1 = new BlockPos(entry.getKey().getX() * -1, entry.getKey().getY(), entry.getKey().getZ() * -1);
                    break;
                default:
                    throw new IllegalStateException("Unexpected value: " + this.getFacing());
            }

            final ItemStack item = entry.getValue();
            if (item.isEmpty()) {
                continue;
            }
            PoseStack poseStack = event.getPoseStack();

            poseStack.pushPose();
            poseStack.translate(pos1.getX(), 0.25 + pos1.getY(), pos1.getZ()); // Moving into the first slot
            if (rotation != null) {
                switch (this.getFacing()) {
                    case NORTH:
                        switch (rotation) {
                            case SOUTH:
                                poseStack.mulPose(Axis.YP.rotationDegrees(180));  // Rotate 180 degrees around Y-axis
                                break;
                            case NORTH:
                                break;
                            case WEST:
                                poseStack.mulPose(Axis.YP.rotationDegrees(-90));  // Rotate -90 degrees around Y-axis
                                break;
                            case EAST:
                                poseStack.mulPose(Axis.YP.rotationDegrees(90));  // Rotate 90 degrees around Y-axis
                                break;
                        }
                        break;
                    case SOUTH:
                        switch (rotation) {
                            case SOUTH:
                                break;
                            case NORTH:
                                poseStack.mulPose(Axis.YP.rotationDegrees(180));  // Rotate 180 degrees around Y-axis
                                break;
                            case WEST:
                                poseStack.mulPose(Axis.YP.rotationDegrees(90));  // Rotate 90 degrees around Y-axis
                                break;
                            case EAST:
                                poseStack.mulPose(Axis.YP.rotationDegrees(-90));  // Rotate -90 degrees around Y-axis
                                break;
                        }
                        break;
                    case WEST:
                        switch (rotation) {
                            case SOUTH:
                                poseStack.mulPose(Axis.YP.rotationDegrees(-90));  // Rotate -90 degrees around Y-axis
                                break;
                            case NORTH:
                                poseStack.mulPose(Axis.YP.rotationDegrees(90));  // Rotate 90 degrees around Y-axis
                                break;
                            case WEST:
                                break;
                            case EAST:
                                poseStack.mulPose(Axis.YP.rotationDegrees(180));  // Rotate 180 degrees around Y-axis
                                break;
                        }
                        break;
                    case EAST:
                        switch (rotation) {
                            case SOUTH:
                                poseStack.mulPose(Axis.YP.rotationDegrees(90));  // Rotate 90 degrees around Y-axis
                                break;
                            case NORTH:
                                poseStack.mulPose(Axis.YP.rotationDegrees(-90));  // Rotate -90 degrees around Y-axis
                                break;
                            case WEST:
                                poseStack.mulPose(Axis.YP.rotationDegrees(180));  // Rotate 180 degrees around Y-axis
                                break;
                            case EAST:
                                break;
                        }
                        break;
                }

            }

            poseStack.scale(1f, 1f, 1f);
            ItemRenderer renderItem = Minecraft.getInstance().getItemRenderer();
            BakedModel itemModel = this.multiBlockStructure.bakedModelMap.get(entry.getKey());
            if (itemModel == null) {
                itemModel = renderItem.getModel(item, tile.getWorld(), null, 0);
                this.multiBlockStructure.bakedModelMap.put(entry.getKey(), itemModel);
            }
            int i;
            if (level != null) {
                i = LevelRenderer.getLightColor(level, getBlockPos());
            } else {
                i = 15728880;
            }
            renderItem.render(item, ItemDisplayContext.FIXED, false,
                    poseStack, ((LevelRendererAccessor) event.getLevelRenderer()).getRenderBuffers().bufferSource(), i, OverlayTexture.NO_OVERLAY, itemModel);
            ((LevelRendererAccessor) event.getLevelRenderer()).getRenderBuffers().bufferSource().endBatch();
            poseStack.popPose();
        }
    }

    @OnlyIn(Dist.CLIENT)
    public void renderUniqueMultiBlock(RenderLevelStageEvent event) {

    }

    @Override
    public boolean isFull() {
        return this.full;
    }

    @Override
    public void setFull(final boolean full) {
        if (!full) {
            if (!this.entityPlayerList.isEmpty()) {
                this.entityPlayerList.forEach(Player::closeContainer);
            }
        }
        this.full = full;
        if (full) {
            rotateBlocks();
        }
        if (!this.getWorld().isClientSide) {
            new PacketUpdateFieldTile(this, "full", full);
        }

    }

    @Override
    public MultiBlockStructure getMultiBlockStucture() {
        return multiBlockStructure;
    }



    @Override
    public boolean wasActivated() {
        return this.activate;
    }

    @Override
    public void setActivated(final boolean active) {
        this.activate = active;
        new PacketUpdateFieldTile(this, "activate", activate);

    }

    public void updateField(String name, CustomPacketBuffer is) {

        if (name.equals("full")) {
            try {
                this.full = (boolean) DecoderHandler.decode(is);
                if (this.full) {
                    this.updateAfterAssembly();
                }
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }
        if (name.equals("activate")) {
            try {
                this.activate = (boolean) DecoderHandler.decode(is);
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }
        if (name.equals("visible")) {
            try {
                this.visible = (boolean) DecoderHandler.decode(is);
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }
        super.updateField(name, is);
    }

    @Override
    public void updateEntityServer() {
        super.updateEntityServer();
        if (this.full && !this.activate) {
            this.setFull(false);
        }
    }

    public void updateFull() {
        setFull(getMultiBlockStucture().getFull(getFacing(), getBlockPos(), getLevel()));
        if (isFull()) {
            setActivated(true);
        }
    }

    @Override
    public void loadBeforeFirstClientUpdate() {
        super.loadBeforeFirstClientUpdate();
        if (this.activate) {
            this.updateFull();
            if (full) {
                this.updateAfterAssembly();
            } else {
                activate = false;
            }

        }
    }

    @Override
    public void loadBeforeFirstUpdate() {
        super.loadBeforeFirstUpdate();
        if (this.activate) {
            this.updateFull();
            if (full) {
                this.updateAfterAssembly();
            } else {
                activate = false;
            }

        }
    }


    public void updateFull(Player player) {
        setFull(getMultiBlockStucture().getFull(getFacing(), getBlockPos(), getLevel(), player));
        if (isFull()) {
            setActivated(true);
        }
    }

    @Override
    public boolean onActivated(Player player, InteractionHand hand, Direction side, Vec3 vec3) {

        if (!this.full || !this.activate) {
            this.getCooldownTracker().setTick(1);

            if (!this.getMultiBlockStucture().isHasActivatedItem()) {
                this.updateFull(player);
                if (full) {
                    this.updateAfterAssembly();
                }
                return true;
            }
            if (this.getMultiBlockStucture().isActivateItem(player.getItemInHand(hand))) {
                this.updateFull(player);
                if (!this.full) {
                    return false;
                } else {
                    this.updateAfterAssembly();
                }
            } else {
                if (!this.getWorld().isClientSide) {
                    IUCore.proxy.messagePlayer(
                            player,
                            Localization.translate("iu.activate_multiblock") + " " + this
                                    .getMultiBlockStucture()
                                    .getActivateItem()
                                    .getDisplayName().getString()
                    );
                }
            }
            return false;
        } else {
            this.usingBeforeGUI();
        }


        return super.onActivated(player, hand, side, vec3);
    }

    @Override
    public boolean onSneakingActivated(Player player, InteractionHand hand, Direction side, Vec3 vec3) {
        if (this.level.isClientSide)
            return true;
        this.getCooldownTracker().setTick(20);
        visible = !visible;
        new PacketUpdateFieldTile(this,"visible",visible);
        return super.onSneakingActivated(player, hand, side, vec3);

    }

    @Override
    public void onLoaded() {
        super.onLoaded();
        if (this.getLevel().isClientSide) {
            this.render = createFunction(this);
            GlobalRenderManager.addRender(this.getWorld(), pos, render);
        }
    }

    @Override
    public void onUnloaded() {
        if (this.getLevel().isClientSide) {
            GlobalRenderManager.removeRender(this.getWorld(), pos);
        }
        if (this.isFull()) {
            if (this.multiBlockStructure != null) {
                List<BlockPos> blockPosList = this.multiBlockStructure.getPoses(this.getFacing(), this.getBlockPos());
                for (BlockPos pos1 : blockPosList) {
                    BlockEntity tileentity = this.getLevel().getBlockEntity(pos1);
                    if (tileentity instanceof TileEntityMultiBlockElement) {
                        TileEntityMultiBlockElement te = (TileEntityMultiBlockElement) tileentity;
                        te.setMainMultiElement(null);
                    }
                }
            }
        }
        super.onUnloaded();
    }

    public void readFromNBT(CompoundTag nbttagcompound) {
        super.readFromNBT(nbttagcompound);
        this.activate = nbttagcompound.getBoolean("activate");
        this.visible = nbttagcompound.getBoolean("visible");
    }

    public CompoundTag writeToNBT(CompoundTag nbttagcompound) {
        super.writeToNBT(nbttagcompound);
        nbttagcompound.putBoolean("activate", this.activate);
        nbttagcompound.putBoolean("visible", this.visible);
        return nbttagcompound;
    }

    @Override
    public IMainMultiBlock getMain() {
        return this;
    }

    @Override
    public void setMainMultiElement(final IMainMultiBlock main) {
    }


}
