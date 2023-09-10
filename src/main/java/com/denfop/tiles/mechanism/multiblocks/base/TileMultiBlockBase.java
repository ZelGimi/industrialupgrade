package com.denfop.tiles.mechanism.multiblocks.base;

import com.denfop.IUCore;
import com.denfop.Localization;
import com.denfop.api.multiblock.IMainMultiBlock;
import com.denfop.api.multiblock.MultiBlockStructure;
import com.denfop.container.ContainerBase;
import com.denfop.network.DecoderHandler;
import com.denfop.network.EncoderHandler;
import com.denfop.network.IUpdatableTileEvent;
import com.denfop.network.packet.CustomPacketBuffer;
import com.denfop.network.packet.PacketUpdateFieldTile;
import com.denfop.tiles.base.TileEntityInventory;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.RenderItem;
import net.minecraft.client.renderer.block.model.IBakedModel;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.BlockPos;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public abstract class TileMultiBlockBase extends TileEntityInventory implements IMainMultiBlock,
        IUpdatableTileEvent {

    private final MultiBlockStructure multiBlockStructure;
    public boolean full;
    public boolean activate;
    public List<EntityPlayer> entityPlayerList;

    public TileMultiBlockBase(MultiBlockStructure multiBlockStructure) {
        this.multiBlockStructure = multiBlockStructure;
        this.full = false;
        this.entityPlayerList = new ArrayList<>();
        this.activate = false;
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

    public void readPacket(CustomPacketBuffer customPacketBuffer) {
        super.readPacket(customPacketBuffer);
        try {
            full = (boolean) DecoderHandler.decode(customPacketBuffer);
            activate = (boolean) DecoderHandler.decode(customPacketBuffer);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    @SideOnly(Side.CLIENT)
    public void render(
            TileMultiBlockBase tileEntityMultiBlockBase,
            double x,
            double y,
            double z,
            float partialTicks,
            int destroyStage,
            float alpha
    ) {
        if (!this.isFull()) {
            renderBlock(tileEntityMultiBlockBase, x, y, z, partialTicks, destroyStage, alpha);
        }
    }

    @SideOnly(Side.CLIENT)
    private void renderBlock(
            TileMultiBlockBase tile,
            double x,
            double y,
            double z,
            float partialTicks,
            int destroyStage,
            float alpha
    ) {
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


            // Calculating offsets for the grid


            // Rendering an item
            RenderItem renderItem = Minecraft.getMinecraft().getRenderItem();
            IBakedModel itemModel = renderItem.getItemModelWithOverrides(item, tile.getWorld(), null);


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
        new PacketUpdateFieldTile(this, "full", full);

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

    public void updateFull() {
        setFull(getMultiBlockStucture().getFull(getFacing(), getBlockPos(), getWorld()));
        if (isFull()) {
            setActivated(true);
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
        if (getWorld().isRemote) {
            return false;
        }
        if (!this.full || !this.activate) {

            if (!this.getMultiBlockStucture().isHasActivatedItem()) {
                this.updateFull(player);
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
                if (IUCore.proxy.isSimulating()) {
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
        if (this.activate && !this.getWorld().isRemote) {
            this.updateFull();
            this.updateAfterAssembly();

        }
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
