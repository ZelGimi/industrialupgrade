package com.denfop.tiles.mechanism;

import ic2.core.block.TileEntityBlock;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import java.util.Collections;
import java.util.List;

public class TileEntityResearchTableSpace extends TileEntityBlock {


    private static final List<AxisAlignedBB> aabbs = Collections.singletonList(new AxisAlignedBB(0, 0.0D, 0, 2, 2.0D,
            1.5
    ));
    private static final List<AxisAlignedBB> aabbs_east = Collections.singletonList(new AxisAlignedBB(0, 0.0D, -1, 1, 2.0D,
            1
    ));
    private static final List<AxisAlignedBB> aabbs_south = Collections.singletonList(new AxisAlignedBB(0, 0.0D, 0, 2, 2.0D,
            1
    ));
    private static final List<AxisAlignedBB> aabbs_west = Collections.singletonList(new AxisAlignedBB(0, 0.0D, 0, 1, 2.0D,
            2
    ));
    private static final List<AxisAlignedBB> aabbs_north = Collections.singletonList(new AxisAlignedBB(-1, 0.0D, 0, 1, 2.0D,
            1
    ));

    @SideOnly(Side.CLIENT)
    protected boolean shouldSideBeRendered(EnumFacing side, BlockPos otherPos) {
        return false;
    }

    protected boolean isNormalCube() {
        return false;
    }

    protected boolean doesSideBlockRendering(EnumFacing side) {
        return false;
    }

    protected boolean isSideSolid(EnumFacing side) {
        return false;
    }

    protected boolean clientNeedsExtraModelInfo() {
        return true;
    }

    public boolean shouldRenderInPass(int pass) {
        return true;
    }

    protected List<AxisAlignedBB> getAabbs(boolean forCollision) {
        switch (this.getFacing()) {
            case EAST:
                return aabbs_east;
            case SOUTH:
                return aabbs_south;
            case WEST:
                return aabbs_west;
            case NORTH:
                return aabbs_north;
            default:
                return aabbs;
        }

    }

}
