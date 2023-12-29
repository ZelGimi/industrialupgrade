package com.denfop.tiles.reactors.water.security;

import com.denfop.IUItem;
import com.denfop.api.reactors.EnumTypeSecurity;
import com.denfop.api.tile.IMultiTileBlock;
import com.denfop.blocks.BlockTileEntity;
import com.denfop.blocks.mechanism.BlockWaterReactors;
import com.denfop.container.ContainerWaterSecurity;
import com.denfop.gui.GuiWaterSecurity;
import com.denfop.network.packet.CustomPacketBuffer;
import com.denfop.tiles.mechanism.multiblocks.base.TileEntityMultiBlockElement;
import com.denfop.tiles.reactors.water.ISecurity;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public class TileEntitySimpleSecurity  extends TileEntityMultiBlockElement implements ISecurity {
    @Override
    public IMultiTileBlock getTeBlock() {
        return BlockWaterReactors.water_security;
    }

    @Override
    public BlockTileEntity getBlock() {
        return IUItem.water_reactors_component;
    }
    @Override
    public boolean hasOwnInventory() {
        return true;
    }
    @Override
    public int getLevel() {
        return 0;
    }
    @Override
    public CustomPacketBuffer writeContainerPacket() {
        CustomPacketBuffer customPacketBuffer = super.writeContainerPacket();
        return customPacketBuffer;
    }

    @Override
    public void readContainerPacket(final CustomPacketBuffer customPacketBuffer) {
        super.readContainerPacket(customPacketBuffer);
    }
    public EnumTypeSecurity security;
    @Override
    public void setSecurity(final EnumTypeSecurity typeSecurity) {
        this.security = typeSecurity;
    }
    @Override
    @SideOnly(Side.CLIENT)
    public GuiScreen getGui(final EntityPlayer var1, final boolean var2) {
        return new GuiWaterSecurity(getGuiContainer(var1));
    }

    @Override
    public ContainerWaterSecurity getGuiContainer(final EntityPlayer var1) {
        return new ContainerWaterSecurity(this,var1);
    }
    public EnumTypeSecurity getSecurity() {
        return security;
    }

}
