package com.denfop.blockentity.reactors.water.security;

import com.denfop.IUItem;
import com.denfop.api.blockentity.MultiBlockEntity;
import com.denfop.api.container.CustomWorldContainer;
import com.denfop.api.reactors.EnumTypeSecurity;
import com.denfop.blockentity.mechanism.multiblocks.base.BlockEntityMultiBlockElement;
import com.denfop.blockentity.reactors.water.ISecurity;
import com.denfop.blockentity.reactors.water.controller.BlockEntityMainController;
import com.denfop.blocks.BlockTileEntity;
import com.denfop.blocks.mechanism.BlockWaterReactorsEntity;
import com.denfop.containermenu.ContainerMenuBase;
import com.denfop.containermenu.ContainerMenuWaterSecurity;
import com.denfop.network.packet.CustomPacketBuffer;
import com.denfop.screen.ScreenIndustrialUpgrade;
import com.denfop.screen.ScreenWaterSecurity;
import com.denfop.utils.Timer;
import net.minecraft.core.BlockPos;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

import java.io.IOException;

public class BlockEntityAdvSecurity extends BlockEntityMultiBlockElement implements ISecurity {


    public EnumTypeSecurity security;
    private Timer red_timer = new Timer(0, 2, 30);
    private Timer yellow_timer = new Timer(0, 5, 0);

    public BlockEntityAdvSecurity(BlockPos pos, BlockState state) {
        super(BlockWaterReactorsEntity.water_adv_security, pos, state);
    }

    @Override
    public MultiBlockEntity getTeBlock() {
        return BlockWaterReactorsEntity.water_adv_security;
    }

    @Override
    public BlockTileEntity getBlock() {
        return IUItem.water_reactors_component.getBlock(getTeBlock());
    }

    @Override
    public int getBlockLevel() {
        return 1;
    }

    @Override
    public CustomPacketBuffer writeContainerPacket() {
        CustomPacketBuffer customPacketBuffer = super.writeContainerPacket();
        customPacketBuffer.writeBoolean(this.getMain() != null);
        if (this.getMain() != null) {
            BlockEntityMainController controller = (BlockEntityMainController) this.getMain();
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
    @OnlyIn(Dist.CLIENT)
    public ScreenIndustrialUpgrade<ContainerMenuBase<? extends CustomWorldContainer>> getGui(Player var1, ContainerMenuBase<? extends CustomWorldContainer> menu) {
        return new ScreenWaterSecurity((ContainerMenuWaterSecurity) menu);
    }

    @Override
    public boolean hasOwnInventory() {
        return true;
    }

    @Override
    public ContainerMenuWaterSecurity getGuiContainer(final Player var1) {
        return new ContainerMenuWaterSecurity(this, var1);
    }

    public EnumTypeSecurity getSecurity() {
        return security;
    }

    @Override
    public void setSecurity(final EnumTypeSecurity typeSecurity) {
        this.security = typeSecurity;
    }


}
