package com.denfop.tiles.mechanism;

import com.denfop.IUItem;
import com.denfop.api.tile.IMultiTileBlock;
import com.denfop.blocks.BlockTileEntity;
import com.denfop.blocks.mechanism.BlockWaterMill;
import com.denfop.componets.Energy;
import com.denfop.events.client.GlobalRenderManager;
import com.denfop.render.oilquarry.DataBlock;
import com.denfop.render.watermill.RendererWaterMill;
import com.denfop.tiles.base.TileEntityInventory;
import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.block.model.IBakedModel;
import net.minecraft.init.Blocks;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.BlockPos;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import java.util.function.Function;

public class TileEntityWaterMill extends TileEntityInventory {

    public final Energy energy;
    @SideOnly(Side.CLIENT)
    public DataBlock dataBlock;
    @SideOnly(Side.CLIENT)
    private RendererWaterMill render;

    public TileEntityWaterMill() {
        this.energy = this.addComponent(Energy.asBasicSource(this, 1000, 1));
    }

    @Override
    public IMultiTileBlock getTeBlock() {
        return BlockWaterMill.water_mill;
    }

    @Override
    public BlockTileEntity getBlock() {
        return IUItem.watermill;
    }

    public boolean water = false;

    @Override
    public void onNeighborChange(final Block neighbor, final BlockPos neighborPos) {
        super.onNeighborChange(neighbor, neighborPos);
        if (this.pos.up().distanceSq(neighborPos) == 0) {
            IBlockState blockState = world.getBlockState(this.pos.up());
            if (blockState.getMaterial() != Material.AIR) {
                this.water =
                        blockState.getBlock() == Blocks.WATER || blockState.getBlock() == Blocks.FLOWING_WATER;
            } else {
                water = false;
            }
        }
    }

    public boolean space = false;

    @Override
    public void onLoaded() {
        super.onLoaded();

        if (this.getWorld().isRemote) {
            this.setActive("global");
            IBlockState blockState1 = this.block
                    .getDefaultState()
                    .withProperty(this.block.typeProperty, this.block.typeProperty.getState(this.teBlock, ""))
                    .withProperty(
                            BlockTileEntity.facingProperty,
                            this.getFacing()
                    );
            this.dataBlock = new DataBlock(blockState1);
            IBakedModel model = Minecraft
                    .getMinecraft()
                    .getBlockRendererDispatcher()
                    .getModelForState(blockState1);
            this.dataBlock.setState(model);
            render = new RendererWaterMill();
            GlobalRenderManager.addRender(world, pos, createFunction(this));
        }
        if (!this.getWorld().isRemote) {
            this.setActive("active");
        }
    }

    @Override
    public void updateEntityClient() {
        super.updateEntityClient();

    }

    @Override
    public void updateEntityServer() {
        super.updateEntityServer();
        if (this.getWorld().getWorldTime() % 60 == 0) {
            EnumFacing facing = this.getFacing();
            if (facing == EnumFacing.EAST || facing == EnumFacing.WEST) {
                boolean find = true;
                cycle:
                for (int x = -1; x < 2; x++) {
                    for (int y = -1; y < 1; y++) {
                        IBlockState blockState = this.getWorld().getBlockState(pos.add(x, y, 0));
                        if (pos.add(x, y, 0).distanceSq(pos) != 0)
                        if (!(blockState.getMaterial() == Material.AIR || blockState.getMaterial().isLiquid())) {
                            this.space = false;
                            find = false;
                            break cycle;
                        }
                    }
                }
                if (find) {
                    space = true;
                }
            }
            if (facing == EnumFacing.NORTH || facing == EnumFacing.SOUTH) {
                boolean find = true;
                cycle:
                for (int z = -1; z < 2; z++) {
                    for (int y = -1; y < 1; y++) {
                        IBlockState blockState = this.getWorld().getBlockState(pos.add(0, y, z));
                        if (pos.add(0, y, z).distanceSq(pos) != 0)
                        if (!(blockState.getMaterial() == Material.AIR || blockState.getMaterial().isLiquid())) {
                            this.space = false;
                            find = false;
                            break cycle;
                        }
                    }
                }
                if (find) {
                    space = true;
                }
            }
        }
        if (this.space && water){
            this.energy.addEnergy(0.1);
        }
    }

    @SideOnly(Side.CLIENT)
    public Function createFunction(TileEntityWaterMill te) {
        Function function = o -> {
            render.render(te);
            return 0;
        };
        return function;
    }

    public boolean doesSideBlockRendering(EnumFacing side) {
        return false;
    }

    @SideOnly(Side.CLIENT)
    public boolean shouldSideBeRendered(EnumFacing side, BlockPos otherPos) {
        return false;
    }

    @Override
    public boolean clientNeedsExtraModelInfo() {
        return true;
    }
 /*   @Override
    public void onUnloaded() {
        super.onUnloaded();
        if (this.getWorld().isRemote) {
            GlobalRenderManager.removeRender(world, pos);
        }
    }*/
}
