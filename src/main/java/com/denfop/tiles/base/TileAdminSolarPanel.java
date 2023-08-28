package com.denfop.tiles.base;


import com.denfop.IUItem;
import com.denfop.api.tile.IMultiTileBlock;
import com.denfop.blocks.BlockTileEntity;
import com.denfop.blocks.mechanism.BlockAdminPanel;
import com.denfop.tiles.panels.entity.EnumSolarPanels;
import com.denfop.tiles.panels.entity.TileSolarPanel;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.RayTraceResult;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public class TileAdminSolarPanel extends TileSolarPanel {

    public TileAdminSolarPanel() {
        super(14, EnumSolarPanels.QUARK_SOLAR_PANEL.genday * 4, EnumSolarPanels.QUARK_SOLAR_PANEL.producing * 4,
                EnumSolarPanels.QUARK_SOLAR_PANEL.maxstorage * 16, null
        );
    }

    public boolean doesSideBlockRendering(EnumFacing side) {
        return false;
    }

    @SideOnly(Side.CLIENT)
    public boolean shouldSideBeRendered(EnumFacing side, BlockPos otherPos) {
        return false;
    }

    @Override
    public ItemStack getPickBlock(final EntityPlayer player, final RayTraceResult target) {
        return new ItemStack(IUItem.blockadmin);
    }

    @Override
    public boolean isNormalCube() {
        return false;
    }

    public IMultiTileBlock getTeBlock() {
        return BlockAdminPanel.admpanel;
    }

    public BlockTileEntity getBlock() {
        return IUItem.blockadmin;
    }

}
