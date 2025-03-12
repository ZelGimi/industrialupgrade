package com.denfop.tiles.mechanism;

import com.denfop.IUItem;
import com.denfop.Localization;
import com.denfop.api.agriculture.CropNetwork;
import com.denfop.api.bee.BeeNetwork;
import com.denfop.api.sytem.EnergyType;
import com.denfop.api.tile.IMultiTileBlock;
import com.denfop.blocks.BlockTileEntity;
import com.denfop.blocks.mechanism.BlockBaseMachine3;
import com.denfop.componets.ComponentBaseEnergy;
import com.denfop.container.ContainerApothecaryBee;
import com.denfop.container.ContainerWeeder;
import com.denfop.gui.GuiApothecaryBee;
import com.denfop.gui.GuiWeeder;
import com.denfop.tiles.base.TileEntityInventory;
import com.denfop.tiles.bee.TileEntityApiary;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.world.chunk.Chunk;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import java.util.ArrayList;
import java.util.List;

public class TileEntityApothecaryBee extends TileEntityInventory {


    private static final int RADIUS = 4;
    public final ComponentBaseEnergy energy;
    AxisAlignedBB searchArea = new AxisAlignedBB(
            pos.add(-RADIUS, -RADIUS, -RADIUS),
            pos.add(RADIUS, RADIUS, RADIUS)
    );

    public TileEntityApothecaryBee() {

        this.energy = this.addComponent(ComponentBaseEnergy.asBasicSink(EnergyType.QUANTUM, this, 20000));
    }


    @Override
    public BlockTileEntity getBlock() {
        return IUItem.basemachine2;
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
    List<List<TileEntityApiary>> list = new ArrayList<>();
    List<Chunk> chunks;
    @Override
    public void onLoaded() {
        super.onLoaded();
        if (!this.getWorld().isRemote) {
            final AxisAlignedBB aabb = searchArea.offset(pos);
            searchArea = aabb;
            int j2 = MathHelper.floor((aabb.minX - 2) / 16.0D);
            int k2 = MathHelper.ceil((aabb.maxX + 2) / 16.0D);
            int l2 = MathHelper.floor((aabb.minZ - 2) / 16.0D);
            int i3 = MathHelper.ceil((aabb.maxZ + 2) / 16.0D);
           chunks = new ArrayList<>();
            for (int j3 = j2; j3 < k2; ++j3) {
                for (int k3 = l2; k3 < i3; ++k3) {
                    final Chunk chunk = world.getChunkFromChunkCoords(j3, k3);
                    if (!chunks.contains(chunk)) {
                        chunks.add(chunk);
                    }
                }
            }
            for (Chunk chunk : chunks) {
                this.list.add(BeeNetwork.instance.getApiaryFromChunk(world, chunk.getPos()));
            }
        }
    }

    @Override
    public ContainerApothecaryBee getGuiContainer(final EntityPlayer var1) {
        return new ContainerApothecaryBee(this, var1);
    }

    @Override
    @SideOnly(Side.CLIENT)
    public GuiScreen getGui(final EntityPlayer var1, final boolean var2) {
        return new GuiApothecaryBee(getGuiContainer(var1));
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
        for (Chunk chunk : chunks) {
            this.list.add(BeeNetwork.instance.getApiaryFromChunk(world, chunk.getPos()));
        }
    }
    @Override
    public void updateEntityServer() {
        super.updateEntityServer();
        if (this.getWorld().getWorldTime() % 100 == 0){
            updateBee();
        }
        if (this.getWorld().provider.getWorldTime() % 20 == 0 && this.energy.canUseEnergy(50)) {
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
