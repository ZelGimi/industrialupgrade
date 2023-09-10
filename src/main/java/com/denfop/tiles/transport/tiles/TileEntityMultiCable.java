package com.denfop.tiles.transport.tiles;

import com.denfop.Constants;
import com.denfop.api.tile.IMultiTileBlock;
import com.denfop.blocks.BlockTileEntity;
import com.denfop.network.DecoderHandler;
import com.denfop.network.packet.CustomPacketBuffer;
import com.denfop.network.packet.PacketUpdateFieldTile;
import com.denfop.tiles.base.TileEntityBlock;
import com.denfop.tiles.transport.types.ICableItem;
import net.minecraft.block.SoundType;
import net.minecraft.entity.Entity;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class TileEntityMultiCable extends TileEntityBlock {

    private final ICableItem cableItem;
    public byte connectivity;
    private ResourceLocation texture;

    public TileEntityMultiCable(ICableItem name) {
        this.cableItem = name;
        this.connectivity = 0;
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

    public SoundType getBlockSound(Entity entity) {
        return SoundType.CLOTH;
    }

    public List<AxisAlignedBB> getAabbs(boolean forCollision) {
        {
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

    public void updateField(String name, CustomPacketBuffer is) {
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
    }

}
