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

public class TileEntityPerSecurity  extends TileEntityMultiBlockElement implements ISecurity {
    @Override
    public IMultiTileBlock getTeBlock() {
        return BlockWaterReactors.water_per_security;
    }

    @Override
    public BlockTileEntity getBlock() {
        return IUItem.water_reactors_component;
    }
    @Override
    public int getLevel() {
        return 3;
    }

    public EnumTypeSecurity security;
    @Override
    public void setSecurity(final EnumTypeSecurity typeSecurity) {
        this.security = typeSecurity;
    } @Override
    public boolean hasOwnInventory() {
        return true;
    }

    @Override
    public CustomPacketBuffer writeContainerPacket() {
        CustomPacketBuffer customPacketBuffer = super.writeContainerPacket();
        customPacketBuffer.writeBoolean(this.security == null);
        if(this.security != null)
            customPacketBuffer.writeInt(this.security.ordinal());
        return customPacketBuffer;
    }

    @Override
    public void readContainerPacket(final CustomPacketBuffer customPacketBuffer) {
        super.readContainerPacket(customPacketBuffer);
        boolean can = customPacketBuffer.readBoolean();
        if(can){
            this.security = EnumTypeSecurity.values()[customPacketBuffer.readInt()];
        }else{
            this.security = EnumTypeSecurity.NONE;
        }
    }

    public EnumTypeSecurity getSecurity() {
        return security;
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

}
