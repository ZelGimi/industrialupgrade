package com.denfop.tiles.reactors.water.security;

import com.denfop.IUItem;
import com.denfop.api.inv.IAdvInventory;
import com.denfop.api.reactors.EnumTypeSecurity;
import com.denfop.api.tile.IMultiTileBlock;
import com.denfop.blocks.BlockTileEntity;
import com.denfop.blocks.mechanism.BlockWaterReactors;
import com.denfop.container.ContainerBase;
import com.denfop.container.ContainerWaterSecurity;
import com.denfop.gui.GuiCore;
import com.denfop.gui.GuiWaterSecurity;
import com.denfop.network.packet.CustomPacketBuffer;
import com.denfop.tiles.mechanism.multiblocks.base.TileEntityMultiBlockElement;
import com.denfop.tiles.reactors.water.ISecurity;
import com.denfop.tiles.reactors.water.controller.TileEntityMainController;
import com.denfop.utils.Timer;
import net.minecraft.core.BlockPos;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

import java.io.IOException;

public class TileEntityPerSecurity extends TileEntityMultiBlockElement implements ISecurity {

    public EnumTypeSecurity security;
    private Timer red_timer = new Timer(0, 2, 30);
    private Timer yellow_timer = new Timer(0, 5, 0);

    public TileEntityPerSecurity(BlockPos pos, BlockState state) {
        super(BlockWaterReactors.water_per_security, pos, state);
    }

    @Override
    public IMultiTileBlock getTeBlock() {
        return BlockWaterReactors.water_per_security;
    }

    @Override
    public BlockTileEntity getBlock() {
        return IUItem.water_reactors_component.getBlock(getTeBlock());
    }

    @Override
    public int getBlockLevel() {
        return 3;
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

    public Timer getYellow_timer() {
        return yellow_timer;
    }

    public Timer getRed_timer() {
        return red_timer;
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

    public EnumTypeSecurity getSecurity() {
        return security;
    }

    @Override
    public void setSecurity(final EnumTypeSecurity typeSecurity) {
        this.security = typeSecurity;
    }
    @Override
    @OnlyIn(Dist.CLIENT)
    public GuiCore<ContainerBase<? extends IAdvInventory>> getGui(Player var1, ContainerBase<? extends IAdvInventory> menu) {
        return new GuiWaterSecurity((ContainerWaterSecurity) menu);
    }


    @Override
    public ContainerWaterSecurity getGuiContainer(final Player var1) {
        return new ContainerWaterSecurity(this, var1);
    }

}
