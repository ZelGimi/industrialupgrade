package com.denfop.tiles.transport.tiles;

import com.denfop.Constants;
import com.denfop.IUItem;
import com.denfop.api.tile.IMultiTileBlock;
import com.denfop.blocks.BlockTileEntity;
import com.denfop.componets.AbstractComponent;
import com.denfop.container.ContainerCable;
import com.denfop.gui.GuiCable;
import com.denfop.network.DecoderHandler;
import com.denfop.network.EncoderHandler;
import com.denfop.network.IUpdatableTileEvent;
import com.denfop.network.packet.CustomPacketBuffer;
import com.denfop.network.packet.PacketCableSound;
import com.denfop.network.packet.PacketUpdateFieldTile;
import com.denfop.render.transport.DataCable;
import com.denfop.tiles.base.TileEntityInventory;
import com.denfop.tiles.transport.types.ICableItem;
import com.denfop.utils.ModUtils;
import net.minecraft.block.Block;
import net.minecraft.block.SoundType;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import net.minecraftforge.items.CapabilityItemHandler;
import org.jetbrains.annotations.NotNull;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class TileEntityMultiCable extends TileEntityInventory implements IUpdatableTileEvent {

    private final ICableItem cableItem;
    public byte connectivity;
    private ResourceLocation texture;
    private List<EnumFacing> blackList = new ArrayList<>();
    public ItemStack stackFacade = ItemStack.EMPTY;
    @SideOnly(Side.CLIENT)
    public DataCable dataCable;


    public TileEntityMultiCable(ICableItem name) {
        this.cableItem = name;
        this.connectivity = 0;
    }

    public List<EnumFacing> getBlackList() {
        return blackList;
    }

    public ICableItem getCableItem() {
        return cableItem;
    }

    public ResourceLocation getTexture() {
        if (this.texture == null) {
            this.texture = new ResourceLocation(
                    Constants.MOD_ID,
                    "textures/blocks/wiring/" + getCableItem().getMainPath() + "/" + getCableItem()
                            .getNameCable() + ".png"
            );
        }
        return this.texture;
    }

    public void removeConductor() {
        this.getWorld().setBlockToAir(this.pos);
        new PacketCableSound(this.getWorld(), this.pos,
                0.5F,
                2.6F + (world.rand.nextFloat() - world.rand.nextFloat()) * 0.8F
        );
    }

    @Override
    public NBTTagCompound writeToNBT(NBTTagCompound nbt) {
        nbt = super.writeToNBT(nbt);
        final NBTTagCompound nbt1 = new NBTTagCompound();
        if (!this.blackList.isEmpty()) {
            nbt1.setInteger("size", this.blackList.size());
            for (int i = 0; i < this.blackList.size(); i++) {
                nbt1.setInteger(String.valueOf(i), this.blackList.get(i).ordinal());
            }
            nbt.setTag("list", nbt1);
        }
        if (this.stackFacade != null && !this.stackFacade.isEmpty()) {
            final NBTTagCompound nbt2 = new NBTTagCompound();
            this.stackFacade.writeToNBT(nbt2);
            nbt.setTag("stackFacade", nbt2);
        }
        return nbt;
    }

    public boolean hasCapability(@NotNull Capability<?> capability, EnumFacing facing) {

        if (capability == CapabilityItemHandler.ITEM_HANDLER_CAPABILITY) {
            return false;
        }
        if (super.hasCapability(capability, facing)) {
            return true;
        } else if (this.capabilityComponents == null) {
            return false;
        } else {
            AbstractComponent comp = this.capabilityComponents.get(capability);
            return comp != null && comp.getProvidedCapabilities(facing).contains(capability);
        }

    }

    @Override
    public void readFromNBT(final NBTTagCompound nbtTagCompound) {
        super.readFromNBT(nbtTagCompound);
        if (nbtTagCompound.hasKey("list")) {
            final NBTTagCompound tagList = nbtTagCompound.getCompoundTag("list");
            final int size = tagList.getInteger("size");
            for (int i = 0; i < size; i++) {
                this.blackList.add(EnumFacing.values()[tagList.getInteger(String.valueOf(i))]);
            }
        }
        if (nbtTagCompound.hasKey("stackFacade")) {
            final NBTTagCompound stackFacade = nbtTagCompound.getCompoundTag("stackFacade");
            this.stackFacade = new ItemStack(stackFacade);
        }
    }

    @Override
    public void onLoaded() {
        super.onLoaded();
        new PacketUpdateFieldTile(this, "stackFacade", this.stackFacade);
    }

    public SoundType getBlockSound(Entity entity) {
        return SoundType.CLOTH;
    }

    public List<AxisAlignedBB> getAabbs(boolean forCollision) {
        if (this.stackFacade == null || this.stackFacade.isEmpty()) {
            float th = 0.25F;
            float sp = (1.0F - th) / 2.0F;
            List<AxisAlignedBB> ret = new ArrayList<>(7);
            ret.add(new AxisAlignedBB(
                    sp,
                    sp,
                    sp,
                    sp + th,
                    sp + th,
                    sp + th
            ));
            EnumFacing[] var5 = EnumFacing.VALUES;
            for (EnumFacing facing : var5) {
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

                    ret.add(new AxisAlignedBB(xS, yS, zS, xE, yE, zE));
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

    @SideOnly(Side.CLIENT)
    public boolean shouldSideBeRendered(EnumFacing side, BlockPos otherPos) {
        return false;
    }

    public boolean isNormalCube() {
        return false;
    }

    public boolean doesSideBlockRendering(EnumFacing side) {
        return false;
    }

    public boolean isSideSolid(EnumFacing side) {
        return false;
    }

    public boolean clientNeedsExtraModelInfo() {
        return true;
    }

    public boolean shouldRenderInPass(int pass) {
        return true;
    }

    public void setConnectivity(final byte connectivity) {
        if (this.connectivity != connectivity) {
            this.connectivity = connectivity;
            new PacketUpdateFieldTile(this, "connectivity", this.connectivity);
            new PacketUpdateFieldTile(this, "texture", this.texture);

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

        final ItemStack stack = player.getHeldItem(hand);
        if (!this.getWorld().isRemote && stack.getItem() == IUItem.facadeItem) {
            final NBTTagCompound nbt = ModUtils.nbt(stack);
            NBTTagList contentList = nbt.getTagList("Items", 10);

            for (int i = 0; i < 1; ++i) {
                NBTTagCompound slotNbt = contentList.getCompoundTagAt(i);
                this.stackFacade = new ItemStack(slotNbt);
            }
            final Block block = Block.getBlockFromItem(this.stackFacade.getItem());
            if (block != Blocks.AIR) {
                this.stackFacade = this.stackFacade.copy();
                new PacketUpdateFieldTile(this, "stackFacade", stackFacade);
            } else {
                this.stackFacade = ItemStack.EMPTY;
            }
        } else if (stack.getItem() == IUItem.connect_item) {
            return super.onActivated(player, hand, side, hitX, hitY, hitZ);
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

    @Override
    @SideOnly(Side.CLIENT)
    public GuiCable getGui(final EntityPlayer var1, final boolean var2) {
        return new GuiCable(getGuiContainer(var1));
    }

    @Override
    public ContainerCable getGuiContainer(final EntityPlayer var1) {
        return new ContainerCable(var1, this);
    }

    @Override
    public boolean onSneakingActivated(
            final EntityPlayer player,
            final EnumHand hand,
            final EnumFacing side,
            final float hitX,
            final float hitY,
            final float hitZ
    ) {
        if (world.isRemote) {
            return false;
        }
        final ItemStack stack = player.getHeldItem(hand);
        if (stack.isEmpty()) {
            if (!this.stackFacade.isEmpty()) {
                this.stackFacade = ItemStack.EMPTY;
                new PacketUpdateFieldTile(this, "stackFacade", stackFacade);
            }
        }
        return super.onSneakingActivated(player, hand, side, hitX, hitY, hitZ);
    }

    public void updateField(String name, CustomPacketBuffer is) {
        super.updateField(name, is);
        if (name.equals("connectivity")) {
            try {
                this.connectivity = (byte) DecoderHandler.decode(is);
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }
        if (name.equals("texture")) {
            try {
                this.texture = (ResourceLocation) DecoderHandler.decode(is);
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
            this.blackList = (List<EnumFacing>) DecoderHandler.decode(customPacketBuffer);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void updateTileServer(final EntityPlayer var1, final double var2) {
        byte event1 = (byte) var2;
        EnumFacing facing1 = EnumFacing.values()[event1];
        if (this.blackList.contains(facing1)) {
            this.blackList.remove(facing1);
        } else {
            this.blackList.add(facing1);
        }
    }

}
