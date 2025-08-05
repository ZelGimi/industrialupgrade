package com.denfop.tiles.mechanism;

import com.denfop.IUItem;
import com.denfop.Localization;
import com.denfop.api.bee.BeeNetwork;
import com.denfop.api.inv.IAdvInventory;
import com.denfop.api.sytem.EnergyType;
import com.denfop.api.tile.IMultiTileBlock;
import com.denfop.blocks.BlockTileEntity;
import com.denfop.blocks.mechanism.BlockBaseMachine3;
import com.denfop.componets.ComponentBaseEnergy;
import com.denfop.componets.client.ComponentVisibleArea;
import com.denfop.container.ContainerApothecaryBee;
import com.denfop.container.ContainerBase;
import com.denfop.gui.GuiApothecaryBee;
import com.denfop.gui.GuiCore;
import com.denfop.tiles.base.TileEntityInventory;
import com.denfop.tiles.bee.TileEntityApiary;
import net.minecraft.core.BlockPos;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.ChunkPos;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.chunk.LevelChunk;
import net.minecraft.world.phys.AABB;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

import java.util.ArrayList;
import java.util.List;

public class TileEntityApothecaryBee extends TileEntityInventory {


    private static final int RADIUS = 4;
    public final ComponentBaseEnergy energy;
    AABB searchArea = new AABB(
            pos.offset(-RADIUS, -RADIUS, -RADIUS),
            pos.offset(RADIUS+1, RADIUS+1, RADIUS+1)
    );
    List<List<TileEntityApiary>> list = new ArrayList<>();
    List<ChunkPos> chunks;
    private ComponentVisibleArea visible;

    public TileEntityApothecaryBee(BlockPos pos, BlockState state) {
        super(BlockBaseMachine3.apothecary_bee,pos,state);
        this.energy = this.addComponent(ComponentBaseEnergy.asBasicSink(EnergyType.QUANTUM, this, 20000));
        visible = this.addComponent(new ComponentVisibleArea(this));
    }

    @Override
    public BlockTileEntity getBlock() {
        return IUItem.basemachine2.getBlock(getTeBlock());
    }

    @Override
    public IMultiTileBlock getTeBlock() {
        return BlockBaseMachine3.apothecary_bee;
    }

    @Override
    public void addInformation(final ItemStack stack, final List<String> tooltip) {
        super.addInformation(stack, tooltip);
        tooltip.add(Localization.translate("iu.apothecary_bee.info"));
    }

    @Override
    public void onLoaded() {
        super.onLoaded();
        visible.aabb = searchArea;
        if (!this.getWorld().isClientSide) {
            final AABB aabb = searchArea;
            int j2 = Mth.floor((aabb.minX - 2) / 16.0D);
            int k2 = Mth.ceil((aabb.maxX + 2) / 16.0D);
            int l2 = Mth.floor((aabb.minZ - 2) / 16.0D);
            int i3 = Mth.ceil((aabb.maxZ + 2) / 16.0D);
            chunks = new ArrayList<>();
            for (int j3 = j2; j3 < k2; ++j3) {
                for (int k3 = l2; k3 < i3; ++k3) {
                    final LevelChunk chunk = level.getChunk(j3, k3);
                    if (!chunks.contains(chunk.getPos())) {
                        chunks.add(chunk.getPos());
                    }
                }
            }
            for (ChunkPos chunk : chunks) {
                this.list.add(BeeNetwork.instance.getApiaryFromChunk(level, chunk));
            }
        }
    }

    @Override
    public ContainerApothecaryBee getGuiContainer(final Player var1) {
        return new ContainerApothecaryBee(this, var1);
    }

    @Override
    @OnlyIn(Dist.CLIENT)
    public GuiCore<ContainerBase<? extends IAdvInventory>> getGui(Player var1, ContainerBase<? extends IAdvInventory> menu) {

        return new GuiApothecaryBee((ContainerApothecaryBee) menu);
    }

    public boolean contains(BlockPos vec) {
        if (vec.getX() > this.searchArea.minX && vec.getX() < searchArea.maxX) {
            if (vec.getY() > this.searchArea.minY && vec.getY() < searchArea.maxY) {
                return vec.getZ() > searchArea.minZ && vec.getZ() < searchArea.maxZ;
            } else {
                return false;
            }
        } else {
            return false;
        }
    }

    private void updateBee() {
        list.clear();
        for (ChunkPos chunk : chunks) {
            this.list.add(BeeNetwork.instance.getApiaryFromChunk(level, chunk));
        }
    }

    @Override
    public void updateEntityServer() {
        super.updateEntityServer();
        if (this.getWorld().getGameTime() % 100 == 0) {
            updateBee();
        }
        if (this.getWorld().getGameTime() % 20 == 0 && this.energy.canUseEnergy(50)) {
            cycle:
            for (List<TileEntityApiary> bees : list) {
                for (TileEntityApiary bee : bees) {
                    if (this.energy.getEnergy() > 50 && bee.ill > 0) {
                        if (this.contains(bee.getPos())) {
                            bee.healBeesFromApothecary(this);
                        }
                    } else {
                        break cycle;
                    }
                }
            }
        }
    }


}
