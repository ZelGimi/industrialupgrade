package com.denfop.tiles.mechanism;

import com.denfop.IUItem;
import com.denfop.api.recipe.InvSlotOutput;
import com.denfop.api.sytem.EnergyType;
import com.denfop.api.tile.IMultiTileBlock;
import com.denfop.blocks.BlockTileEntity;
import com.denfop.blocks.mechanism.BlockBaseMachine3;
import com.denfop.componets.ComponentBaseEnergy;
import com.denfop.componets.ComponentProgress;
import com.denfop.container.ContainerQuantumMiner;
import com.denfop.gui.GuiQuantumMiner;
import com.denfop.tiles.base.TileEntityInventory;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.BlockPos;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import java.util.Random;

public class TileEntityQuantumMiner extends TileEntityInventory {

    public final InvSlotOutput outputSlot;
    public final ComponentBaseEnergy qe;
    public final ComponentProgress progress;
    private int[] metas = new int[]{637, 638, 639, 640, 643, 644, 648, 649};

    public TileEntityQuantumMiner() {
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
                Random random = getWorld().rand;
                int meta = random.nextInt(8);
                this.outputSlot.add(new ItemStack(IUItem.crafting_elements, 1, metas[meta]));
            }
        }
    }

    public boolean doesSideBlockRendering(EnumFacing side) {
        return false;
    }

    @SideOnly(Side.CLIENT)
    public boolean shouldSideBeRendered(EnumFacing side, BlockPos otherPos) {
        return false;
    }

    @Override
    public boolean isNormalCube() {
        return false;
    }

    public ContainerQuantumMiner getGuiContainer(EntityPlayer player) {
        return new ContainerQuantumMiner(player, this);

    }

    @SideOnly(Side.CLIENT)
    public GuiQuantumMiner getGui(EntityPlayer player, boolean isAdmin) {

        return new GuiQuantumMiner(new ContainerQuantumMiner(player, this));
    }

    @Override
    public BlockTileEntity getBlock() {
        return IUItem.basemachine2;
    }

    @Override
    public IMultiTileBlock getTeBlock() {
        return BlockBaseMachine3.quantum_miner;
    }

}
