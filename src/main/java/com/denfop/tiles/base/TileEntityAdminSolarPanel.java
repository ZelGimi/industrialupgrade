package com.denfop.tiles.base;


import com.denfop.tiles.panels.entity.TileEntitySolarPanel;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.BlockPos;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public class TileEntityAdminSolarPanel extends TileEntitySolarPanel {

    public TileEntityAdminSolarPanel() {
        super(11, 999999999, 999999999, 999999999, null);
    }

    protected boolean doesSideBlockRendering(EnumFacing side) {
        return false;
    }

    @SideOnly(Side.CLIENT)
    protected boolean shouldSideBeRendered(EnumFacing side, BlockPos otherPos) {
        return false;
    }

    @Override
    protected boolean isNormalCube() {
        return false;
    }

}
