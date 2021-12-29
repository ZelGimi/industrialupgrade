package com.denfop.tiles.base;

import com.denfop.IUItem;
import com.denfop.blocks.BlockVein;
import com.denfop.utils.ModUtils;
import ic2.api.network.INetworkClientTileEntityEventListener;
import ic2.api.network.INetworkDataProvider;
import ic2.api.network.INetworkUpdateListener;
import ic2.core.ContainerBase;
import ic2.core.IC2;
import ic2.core.init.Localization;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.biome.Biome;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import java.util.Map;
import java.util.Random;

public class TileEntityQuarryVein extends TileEntityElectricMachine implements INetworkUpdateListener, INetworkDataProvider,
        INetworkClientTileEntityEventListener {


    public boolean empty;
    public int x;
    public int y;
    public int z;
    public int progress;
    public int number;
    public boolean analysis;


    public TileEntityQuarryVein() {
        super("", 20, 14, 1);
        this.analysis = true;
        this.number = 0;
        this.progress = 0;
        this.x = 0;
        this.y = 0;
        this.z = 0;
        this.empty = true;
    }

    private void updateTileEntityField() {

        IC2.network.get(true).updateTileEntityField(this, "x");
        IC2.network.get(true).updateTileEntityField(this, "y");
        IC2.network.get(true).updateTileEntityField(this, "z");
        IC2.network.get(true).updateTileEntityField(this, "analysis");
        IC2.network.get(true).updateTileEntityField(this, "empty");

    }

    @SideOnly(Side.CLIENT)
    protected boolean shouldSideBeRendered(EnumFacing side, BlockPos otherPos) {
        return false;
    }

    protected boolean isNormalCube() {
        return false;
    }

    protected boolean doesSideBlockRendering(EnumFacing side) {
        return false;
    }

    @Override
    public void onNetworkUpdate(String field) {

    }

    protected boolean isSideSolid(EnumFacing side) {
        return false;
    }

    protected boolean clientNeedsExtraModelInfo() {
        return true;
    }

    public boolean shouldRenderInPass(int pass) {
        return true;
    }

    @Override
    protected boolean onActivated(
            final EntityPlayer player,
            final EnumHand hand,
            final EnumFacing side,
            final float hitX,
            final float hitY,
            final float hitZ
    ) {
        if (player.isSneaking()) {
            return false;
        }
        if (world.isRemote) {
            return true;
        }
        Map map = this.getWorld().getChunkFromBlockCoords(this.pos).tileEntities;

        for (Object o : map.values()) {
            TileEntity tile3 = (TileEntity) o;
            if (tile3 instanceof TileOilBlock) {
                TileOilBlock tile2 = (TileOilBlock) tile3;
                if (!tile2.empty) {
                    IC2.platform.messagePlayer(player, Localization.translate("iu.fluidneft") + ": " + tile2.number + " mb");
                } else {
                    IC2.platform.messagePlayer(player, Localization.translate("iu.empty"));
                }


                return true;
            } else if (tile3 instanceof TileEntityVein) {
                TileEntityVein tile2 = (TileEntityVein) tile3;
                IC2.platform.messagePlayer(
                        player,
                        new ItemStack(IUItem.heavyore, 1, tile2.getBlockMetadata()).getDisplayName() + ": " + tile2.number +
                                "/" + tile2.max
                );


                return true;
            }

        }
        if (this.analysis) {
            IC2.platform.messagePlayer(player, ModUtils.getString(((double) this.progress / 1200) * 100) + Localization.translate(
                    "scanning"));


            return true;
        }
        return true;
    }

    public void updateEntityServer() {
        super.updateEntityServer();
        if (getWorld().provider.getWorldTime() % 40 == 0) {
            updateTileEntityField();
        }
        if (getWorld().provider.getDimension() != 0) {
            this.empty = true;
            return;
        }

        {
            int chunkx = this.getWorld().getChunkFromBlockCoords(this.pos).x * 16;
            int chunkz = this.getWorld().getChunkFromBlockCoords(this.pos).z * 16;
            BlockPos pos = new BlockPos(chunkx, 0, chunkz);
            if (this.getWorld().getTileEntity(pos) != null && (this.getWorld().getTileEntity(pos) instanceof TileOilBlock || this
                    .getWorld()
                    .getTileEntity(pos) instanceof TileEntityVein)) {
                if (this.getWorld().getTileEntity(pos) instanceof TileEntityVein) {
                    TileEntityVein tile1 = (TileEntityVein) this.getWorld().getTileEntity(pos);
                    if (tile1.change) {
                        number = tile1.number;
                        this.analysis = false;
                        this.empty = false;
                        progress = 1200;
                        this.x = chunkx;
                        this.y = 0;
                        this.z = chunkz;
                        return;
                    }
                } else if (this.getWorld().getTileEntity(pos) instanceof TileOilBlock) {
                    TileOilBlock tile1 = (TileOilBlock) this.getWorld().getTileEntity(pos);
                    if (tile1.change && !tile1.empty) {
                        number = tile1.number;
                        this.analysis = false;
                        progress = 1200;
                        this.x = chunkx;
                        this.y = 0;
                        this.z = chunkz;
                        this.empty = tile1.empty;
                        return;
                    } else {
                        this.empty = true;
                    }

                }
            }
        }
        if (this.analysis && this.energy.getEnergy() >= 5) {
            progress++;
            this.energy.useEnergy(5);
            if (progress >= 1200) {
                this.analysis = false;

                int chunkx = this.getWorld().getChunkFromBlockCoords(this.pos).x * 16;
                int chunkz = this.getWorld().getChunkFromBlockCoords(this.pos).z * 16;
                BlockPos pos = new BlockPos(chunkx, 0, chunkz);
                Random rand = new Random();
                int p = rand.nextInt(100);
                if (p >= 10) {
                    getWorld().setBlockState(pos, IUItem.oilblock.getDefaultState());
                    TileOilBlock oil = (TileOilBlock) getWorld().getTileEntity(pos);
                    oil.change = true;
                    getnumber(oil);
                    this.x = chunkx;
                    this.y = 0;
                    this.z = chunkz;
                    number = oil.number;

                } else if (!getWorld().getBlockState(pos).getBlock().equals(Blocks.AIR)) {
                    int k = rand.nextInt(12);

                    getWorld().setBlockState(pos, IUItem.vein.getBlockState().getBaseState().withProperty(
                            BlockVein.VARIANT,
                            BlockVein.Type.getFromID(k)
                    ));

                    TileEntityVein vein = (TileEntityVein) getWorld().getTileEntity(pos);
                    vein.change = true;
                    vein.meta = k;
                    number = vein.number;
                    this.x = chunkx;
                    this.y = 0;
                    this.z = chunkz;

                }
            }


        }
    }

    private void getnumber(TileOilBlock tile) {
        final Biome biome = getWorld().getBiomeForCoordsBody(tile.getPos());
        Random rand = getWorld().rand;

        if (Biome.getIdForBiome(biome) == 2) {
            int random = rand.nextInt(100);
            if (random > 40) {
                tile.number = rand.nextInt(50000) + 20000;
            } else {
                tile.empty = true;
                tile.number = 0;
            }
        } else if (Biome.getIdForBiome(biome) == 0) {
            int random;
            random = rand.nextInt(100);
            if (random > 65) {
                tile.number = rand.nextInt(80000);
            } else {
                tile.empty = true;
                tile.number = 0;
            }
        } else if (Biome.getIdForBiome(biome) == 24) {
            int random;
            random = rand.nextInt(100);
            if (random > 40) {
                tile.number = rand.nextInt(80000);
            } else {
                tile.empty = true;
                tile.number = 0;
            }
        } else if (Biome.getIdForBiome(biome) == 10) {
            int random;
            random = rand.nextInt(100);
            if (random > 65) {
                tile.number = rand.nextInt(80000);
            } else {
                tile.empty = true;
                tile.number = 0;
            }
        } else if (Biome.getIdForBiome(biome) == 17) {
            int random;
            random = rand.nextInt(100);
            if (random > 40) {
                tile.number = rand.nextInt(60000) + 20000;
            } else {
                tile.empty = true;
                tile.number = 0;
            }
        } else if (Biome.getIdForBiome(biome) == 7) {
            int random;
            random = rand.nextInt(100);
            if (random > 55) {
                tile.number = rand.nextInt(20000);
            } else {
                tile.empty = true;
                tile.number = 0;
            }
        } else if (Biome.getIdForBiome(biome) == 35) {
            int random;
            random = rand.nextInt(100);
            if (random > 55) {
                tile.number = rand.nextInt(40000);
            } else {
                tile.empty = true;
                tile.number = 0;
            }
        } else {
            int random;
            random = rand.nextInt(100);
            if (random > 75) {
                tile.number = rand.nextInt(20000);
            } else {
                tile.empty = true;
                tile.number = 0;
            }
        }
        tile.max = tile.number;
    }


    public void readFromNBT(NBTTagCompound nbttagcompound) {
        super.readFromNBT(nbttagcompound);
        this.empty = nbttagcompound.getBoolean("empty");
        this.analysis = nbttagcompound.getBoolean("analysis");
        this.progress = nbttagcompound.getInteger("progress");
        this.number = nbttagcompound.getInteger("number");
        this.x = nbttagcompound.getInteger("x1");
        this.y = nbttagcompound.getInteger("y1");
        this.z = nbttagcompound.getInteger("z1");
    }

    public NBTTagCompound writeToNBT(NBTTagCompound nbttagcompound) {
        super.writeToNBT(nbttagcompound);
        nbttagcompound.setBoolean("empty", this.empty);
        nbttagcompound.setBoolean("analysis", this.analysis);
        nbttagcompound.setInteger("progress", this.progress);
        nbttagcompound.setInteger("number", this.number);
        nbttagcompound.setInteger("x1", this.x);
        nbttagcompound.setInteger("y1", this.y);
        nbttagcompound.setInteger("z1", this.z);
        return nbttagcompound;
    }


    public boolean isItemValidForSlot(int i, ItemStack itemstack) {
        return true;
    }

    @SideOnly(Side.CLIENT)
    public GuiScreen getGui(EntityPlayer entityPlayer, boolean isAdmin) {
        return null;
    }

    public ContainerBase<? extends TileEntityQuarryVein> getGuiContainer(EntityPlayer entityPlayer) {
        return null;
    }


    public void onNetworkEvent(EntityPlayer player, int event) {

    }


    public void onGuiClosed(EntityPlayer arg0) {
    }

}
