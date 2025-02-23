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
import com.denfop.tiles.reactors.water.controller.TileEntityMainController;
import com.denfop.utils.Timer;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import java.io.IOException;

public class TileEntityImpSecurity extends TileEntityMultiBlockElement implements ISecurity {

    public EnumTypeSecurity security;
    private Timer red_timer = new Timer(0, 2, 30);
    private Timer yellow_timer = new Timer(0, 5, 0);

    @Override
    public IMultiTileBlock getTeBlock() {
        return BlockWaterReactors.water_imp_security;
    }

    @Override
    public BlockTileEntity getBlock() {
        return IUItem.water_reactors_component;
    }

    @Override
    public int getLevel() {
        return 2;
    }

    @Override
    public boolean hasOwnInventory() {
        return true;
    }

    @Override
    public CustomPacketBuffer writeContainerPacket() {
        CustomPacketBuffer customPacketBuffer = super.writeContainerPacket();
        customPacketBuffer.writeBoolean(this.getMain() != null);
        if (this.getMain() != null) {
            TileEntityMainController controller = (TileEntityMainController) this.getMain();
            controller.getRed_timer().writeBuffer(customPacketBuffer);
            controller.getYellow_timer().writeBuffer(customPacketBuffer);
        }
        return customPacketBuffer;
    }

    @Override
    public void readContainerPacket(final CustomPacketBuffer customPacketBuffer) {
        super.readContainerPacket(customPacketBuffer);
        boolean can = customPacketBuffer.readBoolean();
        if (can) {
            try {
                this.red_timer.readBuffer(customPacketBuffer);
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
            try {
                this.yellow_timer.readBuffer(customPacketBuffer);
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }
    }

    public Timer getYellow_timer() {
        return yellow_timer;
    }

    public Timer getRed_timer() {
        return red_timer;
    }

    @Override
    @SideOnly(Side.CLIENT)
    public GuiScreen getGui(final EntityPlayer var1, final boolean var2) {
        return new GuiWaterSecurity(getGuiContainer(var1));
    }

    @Override
    public ContainerWaterSecurity getGuiContainer(final EntityPlayer var1) {
        return new ContainerWaterSecurity(this, var1);
    }

    public EnumTypeSecurity getSecurity() {
        return security;
    }

    @Override
    public void setSecurity(final EnumTypeSecurity typeSecurity) {
        this.security = typeSecurity;
    }

}
