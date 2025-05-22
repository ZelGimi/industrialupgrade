package com.denfop.tiles.lightning_rod;

import com.denfop.IUItem;
import com.denfop.api.tile.IMultiTileBlock;
import com.denfop.blocks.BlockTileEntity;
import com.denfop.blocks.mechanism.BlockLightningRod;
import com.denfop.componets.ComponentTimer;
import com.denfop.componets.Energy;
import com.denfop.network.IUpdatableTileEvent;
import com.denfop.network.packet.CustomPacketBuffer;
import com.denfop.register.InitMultiBlockSystem;
import com.denfop.tiles.base.TileEntityBlock;
import com.denfop.tiles.mechanism.multiblocks.base.TileMultiBlockBase;
import com.denfop.utils.Timer;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.event.entity.EntityStruckByLightningEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class TileEntityLightningRodController extends TileMultiBlockBase implements IController, IUpdatableTileEvent {

    public static Map<BlockPos, IController> controllerMap = new HashMap<>();
    private final Timer timer;
    private final ComponentTimer componentTimer;
    public Energy energy;
    public BlockPos AntennaMasPos;

    public TileEntityLightningRodController() {
        super(InitMultiBlockSystem.LightningRodMultiBlock);
        this.energy = this.addComponent(Energy.asBasicSource(this, 500000, 2));
        this.timer = new Timer(0, 5, 0);
        this.componentTimer = this.addComponent(new ComponentTimer(this, timer));
        componentTimer.setCanWork(false);

    }

    @SubscribeEvent
    public void onLightningStrike(EntityStruckByLightningEvent event) {

    }

    @Override
    public boolean canPlace(final TileEntityBlock te, final BlockPos pos, final World world) {
        for (int x = -7; x <= 7; x++) {
            for (int z = -7; z <= 7; z++) {
                for (int y = -14; y <= 14; y++) {
                    TileEntity tile = world.getTileEntity(pos.add(x, y, z));
                    if (tile instanceof TileEntityLightningRodController || tile instanceof IBase) {
                        return false;
                    }
                }
            }
        }
        return true;
    }

    @Override
    public void onLoaded() {
        super.onLoaded();
        if (this.world.isRemote) {
            return;
        }
        controllerMap.put(this.getPos(), this);
    }

    @Override
    public void onUnloaded() {
        super.onUnloaded();
        if (this.world.isRemote) {
            return;
        }
        controllerMap.remove(this.getPos());
    }

    @Override
    public void updateEntityServer() {
        super.updateEntityServer();
        if (this.componentTimer.getIndexWork() == -1 && this.componentTimer.isCanWork()) {
            this.componentTimer.setCanWork(false);
        }
    }

    @Override
    public BlockTileEntity getBlock() {
        return IUItem.lightning_rod;
    }

    @Override
    public IMultiTileBlock getTeBlock() {
        return BlockLightningRod.lightning_rod_controller;
    }

    @Override
    public void updateTileServer(final EntityPlayer var1, final double var2) {

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

    @Override
    public void setFull(final boolean full) {
        super.setFull(full);
        if (!full) {
            this.AntennaMasPos = null;
        }


    }

    @Override
    public Energy getEnergy() {
        return energy;
    }

    @Override
    public BlockPos getBlockAntennaPos() {
        return AntennaMasPos;
    }

    @Override
    public ComponentTimer getTimer() {
        return componentTimer;
    }

    @Override
    public void updateAfterAssembly() {

        List<BlockPos> pos1 = this
                .getMultiBlockStucture()
                .getPosFromClass(this.getFacing(), this.getBlockPos(),
                        IAntennaMast.class
                );
        this.AntennaMasPos = pos1.get(0);

    }

    @Override
    public void usingBeforeGUI() {

    }

}
