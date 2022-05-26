package com.denfop.tiles.base;

import com.denfop.IUItem;
import com.denfop.blocks.BlockVein;
import com.denfop.container.ContainerQuarryVein;
import com.denfop.gui.GuiQuarryVein;
import com.denfop.items.ItemUpgradeMachinesKit;
import com.denfop.utils.ModUtils;
import ic2.api.network.INetworkClientTileEntityEventListener;
import ic2.api.network.INetworkDataProvider;
import ic2.api.network.INetworkUpdateListener;
import ic2.core.IC2;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.world.biome.Biome;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import java.util.Random;

public class TileEntityQuarryVein extends TileEntityElectricMachine implements INetworkUpdateListener, INetworkDataProvider,
        INetworkClientTileEntityEventListener {


    public int level;
    public int time;
    public boolean empty;
    public int x;
    public int y;
    public int z;
    public int progress;
    public int number;
    public boolean analysis;
    public int max;

    public TileEntityQuarryVein() {
        super("", 400, 14, 1);
        this.analysis = true;
        this.number = 0;
        this.progress = 0;
        this.x = 0;
        this.y = 0;
        this.z = 0;
        this.empty = true;
        this.time = 0;
        this.level = 1;
    }

    @Override
    protected ItemStack getPickBlock(final EntityPlayer player, final RayTraceResult target) {
        ItemStack stack = new ItemStack(IUItem.oilquarry);
        final NBTTagCompound nbt = ModUtils.nbt(stack);
        nbt.setInteger("level", this.level);
        return stack;
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

    public String getStartSoundFile() {
        return "Machines/rig.ogg";
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
        if (!player.getHeldItem(hand).isEmpty()) {
            if (player.getHeldItem(hand).getItem() instanceof ItemUpgradeMachinesKit) {
                if (this.level < 4 && this.level ==
                        (player.getHeldItem(hand).getItemDamage() + 1)) {
                    this.level++;
                    player.getHeldItem(hand).setCount(player.getHeldItem(hand).getCount() - 1);
                    return true;
                } else if (this.level < 4 && player.getHeldItem(hand).getItemDamage() == 3) {
                    this.level = 4;
                    player.getHeldItem(hand).setCount(player.getHeldItem(hand).getCount() - 1);
                    return true;
                }
            }
        }
        return super.onActivated(player, hand, side, hitX, hitY, hitZ);
    }

    @Override
    public void onPlaced(final ItemStack stack, final EntityLivingBase placer, final EnumFacing facing) {
        super.onPlaced(stack, placer, facing);
        final NBTTagCompound nbt = ModUtils.nbt(stack);
        this.level = nbt.getInteger("level") != 0 ? nbt.getInteger("level") : 1;
        if (getWorld().provider.getDimension() != 0) {
            this.empty = true;
        }

    }

    private void updateTileEntityField() {
        IC2.network.get(true).updateTileEntityField(this, "analysis");
        IC2.network.get(true).updateTileEntityField(this, "empty");
        IC2.network.get(true).updateTileEntityField(this, "level");
        IC2.network.get(true).updateTileEntityField(this, "x");
        IC2.network.get(true).updateTileEntityField(this, "y");
        IC2.network.get(true).updateTileEntityField(this, "z");
    }

    public void updateEntityServer() {
        super.updateEntityServer();
        if (getWorld().provider.getWorldTime() % 40 == 0) {
            updateTileEntityField();
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
                        this.number = tile1.number;
                        this.empty = false;
                        this.progress = 1200;
                        this.max = tile1.max;
                        this.x = chunkx;
                        this.y = 0;
                        this.z = chunkz;
                        this.analysis = false;
                        return;
                    }

                } else if (this.getWorld().getTileEntity(pos) instanceof TileOilBlock) {
                    TileOilBlock tile1 = (TileOilBlock) this.getWorld().getTileEntity(pos);
                    if (tile1.change && !tile1.empty) {
                        this.number = tile1.number;
                        this.x = chunkx;
                        this.y = 0;
                        this.z = chunkz;
                        this.empty = false;
                        this.max = tile1.max;
                        this.analysis = false;
                    } else {
                        this.analysis = false;
                        this.empty = true;
                    }
                    this.progress = 1200;
                    return;

                }
                return;
            }
        }

        if (this.analysis && this.energy.getEnergy() >= 5) {
            if (progress == 0) {
                initiate(2);
                time = 0;
                initiate(0);
            }
            if (time > 340) {
                initiate(2);
                time = 0;
            }
            if (time == 0) {
                initiate(0);
            }
            time++;
            progress += Math.pow(2, this.level - 1);
            this.energy.useEnergy(5);
            if (progress >= 1200) {
                initiate(2);
                this.analysis = false;

                int chunkx = this.getWorld().getChunkFromBlockCoords(this.pos).x * 16;
                int chunkz = this.getWorld().getChunkFromBlockCoords(this.pos).z * 16;
                BlockPos pos = new BlockPos(chunkx, 0, chunkz);
                Random rand = new Random();
                int p = rand.nextInt(100);
                if (p >= 20 + ((this.level - 1) * 2)) {
                    getWorld().setBlockState(pos, IUItem.oilblock.getDefaultState());
                    TileOilBlock oil = (TileOilBlock) getWorld().getTileEntity(pos);
                    oil.change = true;
                    getnumber(oil);
                    this.x = chunkx;
                    this.y = 0;
                    this.z = chunkz;
                    this.number = oil.number;

                } else if (!getWorld().getBlockState(pos).getBlock().equals(Blocks.AIR)) {
                    int k = rand.nextInt(16);

                    getWorld().setBlockState(pos, IUItem.vein.getBlockState().getBaseState().withProperty(
                            BlockVein.VARIANT,
                            BlockVein.Type.getFromID(k)
                    ));

                    TileEntityVein vein = (TileEntityVein) getWorld().getTileEntity(pos);
                    vein.change = true;
                    vein.meta = k;
                    this.number = vein.number;
                    this.x = chunkx;
                    this.y = 0;
                    this.z = chunkz;

                }
            }


        } else {
            if (this.time > 0) {
                initiate(2);
                this.time = 0;
            }
        }
    }

    private void getnumber(TileOilBlock tile) {
        final Biome biome = getWorld().getBiomeForCoordsBody(tile.getPos());
        Random rand = getWorld().rand;

        if (Biome.getIdForBiome(biome) == 2) {
            int random = rand.nextInt(100);
            if (random > 40 - ((this.level - 1) * 3)) {
                tile.number = rand.nextInt(50000) + 20000;
            } else {
                tile.empty = true;
                tile.number = 0;
            }
        } else if (Biome.getIdForBiome(biome) == 0) {
            int random;
            random = rand.nextInt(100);
            if (random > 65 - ((this.level - 1) * 3)) {
                tile.number = rand.nextInt(80000);
            } else {
                tile.empty = true;
                tile.number = 0;
            }
        } else if (Biome.getIdForBiome(biome) == 24) {
            int random;
            random = rand.nextInt(100);
            if (random > 40 - ((this.level - 1) * 3)) {
                tile.number = rand.nextInt(80000);
            } else {
                tile.empty = true;
                tile.number = 0;
            }
        } else if (Biome.getIdForBiome(biome) == 10) {
            int random;
            random = rand.nextInt(100);
            if (random > 65 - ((this.level - 1) * 3)) {
                tile.number = rand.nextInt(80000);
            } else {
                tile.empty = true;
                tile.number = 0;
            }
        } else if (Biome.getIdForBiome(biome) == 17) {
            int random;
            random = rand.nextInt(100);
            if (random > 40 - ((this.level - 1) * 3)) {
                tile.number = rand.nextInt(60000) + 20000;
            } else {
                tile.empty = true;
                tile.number = 0;
            }
        } else if (Biome.getIdForBiome(biome) == 7) {
            int random;
            random = rand.nextInt(100);
            if (random > 55 - ((this.level - 1) * 3)) {
                tile.number = rand.nextInt(20000);
            } else {
                tile.empty = true;
                tile.number = 0;
            }
        } else if (Biome.getIdForBiome(biome) == 35) {
            int random;
            random = rand.nextInt(100);
            if (random > 55 - ((this.level - 1) * 3)) {
                tile.number = rand.nextInt(40000);
            } else {
                tile.empty = true;
                tile.number = 0;
            }
        } else {
            int random;
            random = rand.nextInt(100);
            if (random > 75 - ((this.level - 1) * 3)) {
                tile.number = rand.nextInt(20000);
            } else {
                tile.empty = true;
                tile.number = 0;
            }
        }
        tile.max = tile.number;
        tile.change = true;
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
        this.level = nbttagcompound.getInteger("level");
        this.max = nbttagcompound.getInteger("max");

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
        nbttagcompound.setInteger("level", this.level);
        nbttagcompound.setInteger("max", this.max);
        return nbttagcompound;
    }


    @SideOnly(Side.CLIENT)
    public GuiScreen getGui(EntityPlayer entityPlayer, boolean isAdmin) {
        return new GuiQuarryVein(getGuiContainer(entityPlayer));
    }

    public ContainerQuarryVein getGuiContainer(EntityPlayer entityPlayer) {
        return new ContainerQuarryVein(entityPlayer, this);
    }


    public void onNetworkEvent(EntityPlayer player, int event) {

    }


    public void onGuiClosed(EntityPlayer arg0) {
    }

}
