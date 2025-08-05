package com.denfop.tiles.mechanism;

import com.denfop.IUItem;
import com.denfop.api.inv.IAdvInventory;
import com.denfop.api.recipe.InvSlotOutput;
import com.denfop.api.sytem.EnergyType;
import com.denfop.api.tile.IMultiTileBlock;
import com.denfop.blocks.BlockTileEntity;
import com.denfop.blocks.mechanism.BlockBaseMachine3;
import com.denfop.componets.ComponentBaseEnergy;
import com.denfop.componets.ComponentProgress;
import com.denfop.container.ContainerBase;
import com.denfop.container.ContainerQuantumMiner;
import com.denfop.gui.GuiCore;
import com.denfop.gui.GuiQuantumMiner;
import com.denfop.tiles.base.TileEntityInventory;
import net.minecraft.core.BlockPos;
import net.minecraft.util.RandomSource;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.state.BlockState;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.api.distmarker.OnlyIn;

public class TileEntityQuantumMiner extends TileEntityInventory {

    public final InvSlotOutput outputSlot;
    public final ComponentBaseEnergy qe;
    public final ComponentProgress progress;
    private int[] metas = new int[]{637, 638, 639, 640, 643, 644, 648, 649};

    public TileEntityQuantumMiner(BlockPos pos, BlockState state) {
        super(BlockBaseMachine3.quantum_miner, pos, state);
        this.outputSlot = new InvSlotOutput(this, 18);
        this.qe = this.addComponent(ComponentBaseEnergy.asBasicSink(EnergyType.QUANTUM, this, 1000));
        this.progress = this.addComponent(new ComponentProgress(this, 1, 1000));
    }

    @Override
    public void updateEntityServer() {
        super.updateEntityServer();
        if (this.qe.canUseEnergy(62.5)) {
            this.qe.useEnergy(62.5);
            this.progress.addProgress();
            if (this.progress.getBar() == 1) {
                this.progress.setProgress((short) 0);
                RandomSource random = getWorld().random;
                int meta = random.nextInt(8);
                this.outputSlot.add(new ItemStack(IUItem.crafting_elements.getStack(metas[meta])));
            }
        }
    }


    public ContainerQuantumMiner getGuiContainer(Player player) {
        return new ContainerQuantumMiner(player, this);

    }

    @Override
    @OnlyIn(Dist.CLIENT)
    public GuiCore<ContainerBase<? extends IAdvInventory>> getGui(Player var1, ContainerBase<? extends IAdvInventory> menu) {

        return new GuiQuantumMiner((ContainerQuantumMiner) menu);
    }

    @Override
    public BlockTileEntity getBlock() {
        return IUItem.basemachine2.getBlock(getTeBlock());
    }

    @Override
    public IMultiTileBlock getTeBlock() {
        return BlockBaseMachine3.quantum_miner;
    }

}
