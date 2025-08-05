package com.denfop.tiles.lightning_rod;

import com.denfop.IUItem;
import com.denfop.Localization;
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
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class TileEntityLightningRodController extends TileMultiBlockBase implements IController, IUpdatableTileEvent {

    public static Map<BlockPos, IController> controllerMap = new HashMap<>();
    private final Timer timer;
    private final ComponentTimer componentTimer;
    public Energy energy;
    public BlockPos AntennaMasPos;

    public TileEntityLightningRodController(BlockPos pos, BlockState state) {
        super(InitMultiBlockSystem.LightningRodMultiBlock,BlockLightningRod.lightning_rod_controller,pos,state);
        this.energy = this.addComponent(Energy.asBasicSource(this, 500000, 2));
        this.timer = new Timer(0, 5, 0);
        this.componentTimer = this.addComponent(new ComponentTimer(this, timer));
        componentTimer.setCanWork(false);

    }

    @Override
    public void addInformation(ItemStack stack, List<String> tooltip) {
        super.addInformation(stack, tooltip);
        tooltip.add(Localization.translate("iu.lightning.info"));
    }

    @Override
    public boolean canPlace(final TileEntityBlock te, final BlockPos pos, final Level world, Direction direction, LivingEntity entity) {
        for (int x = -7; x <= 7; x++) {
            for (int z = -7; z <= 7; z++) {
                for (int y = -14; y <= 14; y++) {
                    BlockEntity tile = world.getBlockEntity(pos.offset(x, y, z));
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
        if (this.level.isClientSide) {
            return;
        }
        controllerMap.put(this.getPos(), this);
    }

    @Override
    public void onUnloaded() {
        super.onUnloaded();
        if (this.level.isClientSide) {
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
        return IUItem.lightning_rod.getBlock(getTeBlock());
    }

    @Override
    public IMultiTileBlock getTeBlock() {
        return BlockLightningRod.lightning_rod_controller;
    }

    @Override
    public void updateTileServer(final Player var1, final double var2) {

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
