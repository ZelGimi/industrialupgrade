package com.denfop.blockentity.mechanism;

import com.denfop.IUItem;
import com.denfop.api.blockentity.MultiBlockEntity;
import com.denfop.api.container.CustomWorldContainer;
import com.denfop.api.otherenergies.common.EnergyType;
import com.denfop.api.recipe.InventoryOutput;
import com.denfop.blockentity.base.BlockEntityInventory;
import com.denfop.blocks.BlockTileEntity;
import com.denfop.blocks.mechanism.BlockBaseMachine3Entity;
import com.denfop.componets.ComponentBaseEnergy;
import com.denfop.componets.ComponentProgress;
import com.denfop.containermenu.ContainerMenuBase;
import com.denfop.containermenu.ContainerMenuQuantumMiner;
import com.denfop.screen.ScreenIndustrialUpgrade;
import com.denfop.screen.ScreenQuantumMiner;
import net.minecraft.core.BlockPos;
import net.minecraft.util.RandomSource;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

public class BlockEntityQuantumMiner extends BlockEntityInventory {

    public final InventoryOutput outputSlot;
    public final ComponentBaseEnergy qe;
    public final ComponentProgress progress;
    private int[] metas = new int[]{637, 638, 639, 640, 643, 644, 648, 649};

    public BlockEntityQuantumMiner(BlockPos pos, BlockState state) {
        super(BlockBaseMachine3Entity.quantum_miner, pos, state);
        this.outputSlot = new InventoryOutput(this, 18);
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


    public ContainerMenuQuantumMiner getGuiContainer(Player player) {
        return new ContainerMenuQuantumMiner(player, this);

    }

    @Override
    @OnlyIn(Dist.CLIENT)
    public ScreenIndustrialUpgrade<ContainerMenuBase<? extends CustomWorldContainer>> getGui(Player var1, ContainerMenuBase<? extends CustomWorldContainer> menu) {

        return new ScreenQuantumMiner((ContainerMenuQuantumMiner) menu);
    }

    @Override
    public BlockTileEntity getBlock() {
        return IUItem.basemachine2.getBlock(getTeBlock());
    }

    @Override
    public MultiBlockEntity getTeBlock() {
        return BlockBaseMachine3Entity.quantum_miner;
    }

}
