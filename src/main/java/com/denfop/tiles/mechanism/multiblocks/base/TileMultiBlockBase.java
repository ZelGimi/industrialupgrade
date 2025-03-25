package com.denfop.tiles.mechanism.multiblocks.base;

import com.denfop.IUCore;
import com.denfop.IUItem;
import com.denfop.Localization;
import com.denfop.api.multiblock.IMainMultiBlock;
import com.denfop.api.multiblock.MultiBlockStructure;
import com.denfop.container.ContainerBase;
import com.denfop.events.client.GlobalRenderManager;
import com.denfop.items.energy.ItemToolWrench;
import com.denfop.network.DecoderHandler;
import com.denfop.network.EncoderHandler;
import com.denfop.network.IUpdatableTileEvent;
import com.denfop.network.packet.CustomPacketBuffer;
import com.denfop.network.packet.PacketUpdateFieldTile;
import com.denfop.tiles.base.TileEntityInventory;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.BufferBuilder;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.RenderItem;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.block.model.BakedQuad;
import net.minecraft.client.renderer.block.model.IBakedModel;
import net.minecraft.client.renderer.vertex.DefaultVertexFormats;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec3i;
import net.minecraft.util.text.TextFormatting;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.function.Function;

import static com.denfop.render.multiblock.TileEntityMultiBlockRender.createFunction;

public abstract class TileMultiBlockBase extends TileEntityInventory implements IMainMultiBlock,
        IUpdatableTileEvent {

    private final MultiBlockStructure multiBlockStructure;
    public boolean full;
    public boolean activate;
    public List<EntityPlayer> entityPlayerList;
    @SideOnly(Side.CLIENT)
    private Function render;

    public TileMultiBlockBase(MultiBlockStructure multiBlockStructure) {
        this.multiBlockStructure = multiBlockStructure;
        this.full = false;
        this.entityPlayerList = new ArrayList<>();
        this.activate = false;
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
    public void addInformation(final ItemStack stack, final List<String> tooltip) {
        super.addInformation(stack, tooltip);
        if (world == null) {
            tooltip.add(Localization.translate("multiblock.jei1"));
            if (getMultiBlockStucture() != null) {
                for (ItemStack stack1 : getMultiBlockStucture().itemStackList) {
                    if (!stack1.isEmpty()) {
                        tooltip.add(TextFormatting.GREEN + "" + stack1.getCount() + "x" + TextFormatting.GRAY + stack1.getDisplayName());
                    }
                }
            }
        }
    }

    @Override
    public boolean isMain() {
        return true;
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


    public boolean onRemovedByPlayer(EntityPlayer player, boolean willHarvest) {

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

    @SideOnly(Side.CLIENT)
    public void render(IBakedModel model, IBlockState state, EnumFacing enumfacing) {

        Tessellator tessellator = Tessellator.getInstance();
        BufferBuilder bufferbuilder = tessellator.getBuffer();
        int i = 0;
        final List<BakedQuad> listQuads = model.getQuads(state, enumfacing, 0L);
        for (int j = listQuads.size(); i < j; ++i) {
            BakedQuad bakedquad = listQuads.get(i);


            bufferbuilder.begin(7, DefaultVertexFormats.ITEM);
            bufferbuilder.addVertexData(bakedquad.getVertexData());


            Vec3i vec3i = bakedquad.getFace().getDirectionVec();
            bufferbuilder.putNormal((float) vec3i.getX(), (float) vec3i.getY(), (float) vec3i.getZ());
            tessellator.draw();
        }

    }

    @SideOnly(Side.CLIENT)
    public void render(
            TileMultiBlockBase tileEntityMultiBlockBase
    ) {
        if (!this.isFull()) {
            renderBlock(tileEntityMultiBlockBase);
        } else if (this.getMultiBlockStucture().hasUniqueModels) {
            renderUniqueMultiBlock();
        }
    }

    @SideOnly(Side.CLIENT)
    public void renderUniqueMultiBlock() {

    }

    private void rotateBlocks() {
        for (Map.Entry<BlockPos, EnumFacing> entry : this.multiBlockStructure.RotationMap.entrySet()) {
            BlockPos pos1;
            EnumFacing rotation = entry.getValue();
            switch (this.getFacing()) {
                case NORTH:
                    pos1 = entry.getKey();
                    break;
                case EAST:
                    pos1 = new BlockPos(-entry.getKey().getZ(), entry.getKey().getY(), entry.getKey().getX());
                    break;
                case WEST:
                    pos1 = new BlockPos(entry.getKey().getZ(), entry.getKey().getY(), -entry.getKey().getX());
                    break;
                case SOUTH:
                    pos1 = new BlockPos(-entry.getKey().getX(), entry.getKey().getY(), -entry.getKey().getZ());
                    break;
                default:
                    throw new IllegalStateException("Unexpected value: " + this.getFacing());
            }
            EnumFacing facing = ((TileMultiBlockBase) this).getFacing();
            pos1 = pos.add(pos1);
            if (pos1.equals(pos)) {
                continue;
            }
            if (facing == EnumFacing.NORTH) {
                if (rotation == EnumFacing.EAST || rotation == EnumFacing.WEST) {
                    rotation = rotation.getOpposite();
                }
            } else if (facing == EnumFacing.SOUTH) {
                if (rotation == EnumFacing.SOUTH || rotation == EnumFacing.NORTH) {
                    rotation = rotation.getOpposite();
                }
            } else if (facing == EnumFacing.EAST) {
                if (rotation == EnumFacing.EAST || rotation == EnumFacing.WEST) {
                    if (rotation == EnumFacing.EAST) {
                        rotation = EnumFacing.NORTH;
                    } else {
                        rotation = EnumFacing.SOUTH;
                    }
                } else {
                    if (rotation == EnumFacing.SOUTH) {
                        rotation = EnumFacing.WEST;
                    } else {
                        rotation = EnumFacing.EAST;
                    }
                }
            } else if (facing == EnumFacing.WEST) {
                if (rotation == EnumFacing.EAST || rotation == EnumFacing.WEST) {
                    if (rotation == EnumFacing.WEST) {
                        rotation = EnumFacing.NORTH;
                    } else {
                        rotation = EnumFacing.SOUTH;
                    }
                } else {
                    if (rotation == EnumFacing.SOUTH) {
                        rotation = EnumFacing.EAST;
                    } else {
                        rotation = EnumFacing.WEST;
                    }
                }
            }

            TileEntityMultiBlockElement multiBlockElement = (TileEntityMultiBlockElement) this.getWorld().getTileEntity(pos1);
            if (multiBlockElement != null) {
                multiBlockElement.setFacing(rotation);
            }
        }

    }

    private EnumFacing rotateFacing(EnumFacing original, EnumFacing baseRotation) {
        if (original == EnumFacing.UP || original == EnumFacing.DOWN) {
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


    private EnumFacing rotate90(EnumFacing facing) {
        switch (facing) {
            case NORTH:
                return EnumFacing.EAST;
            case EAST:
                return EnumFacing.SOUTH;
            case SOUTH:
                return EnumFacing.WEST;
            case WEST:
                return EnumFacing.NORTH;
            default:
                return facing;
        }
    }

    @SideOnly(Side.CLIENT)
    private void renderBlock(
            TileMultiBlockBase tile
    ) {
        if (facing == 0 || facing == 1) {
            return;
        }
        for (Map.Entry<BlockPos, ItemStack> entry : this.multiBlockStructure.ItemStackMap.entrySet()) {
            BlockPos pos1;
            if (entry.getValue().isEmpty()) {
                continue;
            }
            EnumFacing rotation = this.multiBlockStructure.RotationMap.get(entry.getKey());
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
            GlStateManager.pushMatrix();
            GlStateManager.translate(pos1.getX(), 0.5 + pos1.getY(), pos1.getZ()); // Moving into the first slot
            if (rotation != null) {
                if (rotation != this.getFacing()) {
                    switch (this.getFacing()) {
                        case NORTH:
                            switch (rotation) {
                                case SOUTH:
                                    GlStateManager.rotate(180, 0, 1F, 0);
                                    break;
                                case NORTH:
                                    break;
                                case WEST:
                                    GlStateManager.rotate(-90, 0, 1F, 0);
                                    break;
                                case EAST:
                                    GlStateManager.rotate(90, 0, 1F, 0);
                                    break;
                            }
                            break;
                        case SOUTH:
                            switch (rotation) {
                                case SOUTH:
                                    break;
                                case NORTH:
                                    GlStateManager.rotate(180, 0, 1F, 0);
                                    break;
                                case WEST:
                                    GlStateManager.rotate(90, 0, 1F, 0);
                                    break;
                                case EAST:
                                    GlStateManager.rotate(-90, 0, 1F, 0);
                                    break;
                            }
                            break;
                        case WEST:
                            switch (rotation) {
                                case SOUTH:
                                    GlStateManager.rotate(-90, 0, 1F, 0);
                                    break;
                                case NORTH:
                                    GlStateManager.rotate(90, 0, 1F, 0);
                                    break;
                                case WEST:
                                    break;
                                case EAST:
                                    GlStateManager.rotate(180, 0, 1F, 0);
                                    break;
                            }
                            break;
                        case EAST:
                            switch (rotation) {
                                case SOUTH:
                                    GlStateManager.rotate(90, 0, 1F, 0);
                                    break;
                                case NORTH:
                                    GlStateManager.rotate(-90, 0, 1F, 0);
                                    break;
                                case WEST:
                                    GlStateManager.rotate(180, 0, 1F, 0);

                                    break;
                                case EAST:
                                    break;
                            }
                            break;
                    }
                }
            }
            GlStateManager.scale(0.5f, 0.5f, 0.5f); // Scaling down the block
            RenderItem renderItem = Minecraft.getMinecraft().getRenderItem();
            IBakedModel itemModel = this.multiBlockStructure.bakedModelMap.get(entry.getKey());
            if (itemModel == null) {
                itemModel = renderItem.getItemModelWithOverrides(item, tile.getWorld(), null);
                this.multiBlockStructure.bakedModelMap.put(entry.getKey(), itemModel);
            }
            renderItem.renderItem(item, itemModel);
            GlStateManager.popMatrix();
        }
    }

    public abstract void updateAfterAssembly();

    public abstract void usingBeforeGUI();

    @Override
    public ContainerBase<?> getGuiContainer(final EntityPlayer entityPlayer) {
        if (!this.entityPlayerList.contains(entityPlayer)) {
            this.entityPlayerList.add(entityPlayer);
        }
        return null;
    }

    @Override
    public boolean isFull() {
        return this.full;
    }

    @Override
    public void setFull(final boolean full) {
        if (!full) {
            if (!this.entityPlayerList.isEmpty()) {
                this.entityPlayerList.forEach(EntityPlayer::closeScreen);
            }
        }
        this.full = full;
        if (full) {
            rotateBlocks();
        }
        if (!this.getWorld().isRemote) {
            new PacketUpdateFieldTile(this, "full", full);
        }

    }

    @Override
    public MultiBlockStructure getMultiBlockStucture() {
        return multiBlockStructure;
    }

    @Override
    public int getLevel() {
        return 0;
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
        setFull(getMultiBlockStucture().getFull(getFacing(), getBlockPos(), getWorld()));
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

    public void updateFull(EntityPlayer player) {
        setFull(getMultiBlockStucture().getFull(getFacing(), getBlockPos(), getWorld(), player));
        if (isFull()) {
            setActivated(true);
        }
    }

    @Override
    public boolean onActivated(
            final EntityPlayer player,
            final EnumHand hand,
            final EnumFacing side,
            final float hitX,
            final float hitY,
            final float hitZ
    ) {

        if (!this.full || !this.activate) {
            this.getCooldownTracker().setTick(1);
            if (player.getHeldItem(hand).getItem() instanceof ItemToolWrench || player
                    .getHeldItem(hand)
                    .getItem() == IUItem.GraviTool) {
                return false;
            }
            if (!this.getMultiBlockStucture().isHasActivatedItem()) {
                this.updateFull(player);
                if (full) {
                    this.updateAfterAssembly();
                }
                return true;
            }
            if (this.getMultiBlockStucture().isActivateItem(player.getHeldItem(hand))) {
                this.updateFull(player);
                if (!this.full) {
                    return false;
                } else {
                    this.updateAfterAssembly();
                }
            } else {
                if (!this.getWorld().isRemote) {
                    IUCore.proxy.messagePlayer(
                            player,
                            Localization.translate("iu.activate_multiblock") + " " + this
                                    .getMultiBlockStucture()
                                    .getActivateItem()
                                    .getDisplayName()
                    );
                }
            }
            return false;
        } else {
            this.usingBeforeGUI();
        }


        return super.onActivated(player, hand, side, hitX, hitY, hitZ);
    }

    @Override
    public void onLoaded() {
        super.onLoaded();
        if (this.getWorld().isRemote) {
            this.render = createFunction(this);
            GlobalRenderManager.addRender(this.getWorld(), pos, render);
        }
    }

    @Override
    public void onBlockBreak(final boolean wrench) {
        super.onBlockBreak(wrench);
        if (this.isFull()) {
            if (this.multiBlockStructure != null) {
                List<BlockPos> blockPosList = this.multiBlockStructure.getPoses(this.getFacing(), this.getBlockPos());
                for (BlockPos pos1 : blockPosList) {
                    TileEntity tileentity = this.getWorld().getTileEntity(pos1);
                    if (tileentity instanceof TileEntityMultiBlockElement) {
                        TileEntityMultiBlockElement te = (TileEntityMultiBlockElement) tileentity;
                        te.setMainMultiElement(null);
                    }
                }
            }
            this.setFull(false);
        }
    }

    @Override
    public void onUnloaded() {
        if (this.getWorld().isRemote) {
            GlobalRenderManager.removeRender(this.getWorld(), pos);
        }
        super.onUnloaded();
    }

    public void readFromNBT(NBTTagCompound nbttagcompound) {
        super.readFromNBT(nbttagcompound);
        this.activate = nbttagcompound.getBoolean("activate");
    }

    public NBTTagCompound writeToNBT(NBTTagCompound nbttagcompound) {
        super.writeToNBT(nbttagcompound);
        nbttagcompound.setBoolean("activate", this.activate);
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
