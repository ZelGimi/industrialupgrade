package com.denfop.blockentity.mechanism;

import com.denfop.IUItem;
import com.denfop.api.blockentity.MultiBlockEntity;
import com.denfop.api.container.CustomWorldContainer;
import com.denfop.api.energy.networking.EnergyNetGlobal;
import com.denfop.api.otherenergies.common.EnergyType;
import com.denfop.blockentity.base.BlockEntityElectricMachine;
import com.denfop.blocks.BlockTileEntity;
import com.denfop.blocks.mechanism.BlockBaseMachine3Entity;
import com.denfop.componets.ComponentBaseEnergy;
import com.denfop.containermenu.ContainerMenuAmpereStorage;
import com.denfop.containermenu.ContainerMenuBase;
import com.denfop.network.IUpdatableTileEvent;
import com.denfop.network.packet.CustomPacketBuffer;
import com.denfop.screen.ScreenAmpereStorage;
import com.denfop.screen.ScreenIndustrialUpgrade;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

import java.util.Arrays;

public class BlockEntityAmpereStorage extends BlockEntityElectricMachine implements IUpdatableTileEvent {


    public final ComponentBaseEnergy pressure;

    public BlockEntityAmpereStorage(BlockPos pos, BlockState state) {
        super(0, 0, 1, BlockBaseMachine3Entity.ampere_storage, pos, state);

        this.pressure = this.addComponent((new ComponentBaseEnergy(EnergyType.AMPERE, this, 100000,

                Arrays.asList(Direction.values()),
                Arrays.asList(Direction.values()),
                EnergyNetGlobal.instance.getTierFromPower(14),
                EnergyNetGlobal.instance.getTierFromPower(14)
        )));

    }


    @Override
    public MultiBlockEntity getTeBlock() {
        return BlockBaseMachine3Entity.ampere_storage;
    }

    @Override
    public BlockTileEntity getBlock() {
        return IUItem.basemachine2.getBlock(getTeBlock());
    }

    @Override
    public CustomPacketBuffer writeContainerPacket() {
        final CustomPacketBuffer packet = super.writeContainerPacket();
        return packet;
    }

    @Override
    public void onLoaded() {
        super.onLoaded();
    }

    @Override
    public void updateTileServer(final Player entityPlayer, final double i) {

    }


    public void updateEntityServer() {
        super.updateEntityServer();


    }


    @Override
    public ContainerMenuAmpereStorage getGuiContainer(final Player entityPlayer) {
        return new ContainerMenuAmpereStorage(entityPlayer, this);
    }

    @Override
    @OnlyIn(Dist.CLIENT)
    public ScreenIndustrialUpgrade<ContainerMenuBase<? extends CustomWorldContainer>> getGui(Player var1, ContainerMenuBase<? extends CustomWorldContainer> menu) {

        return new ScreenAmpereStorage((ContainerMenuAmpereStorage) menu);
    }


    @Override
    public SoundEvent getSound() {
        return null;
    }

}
