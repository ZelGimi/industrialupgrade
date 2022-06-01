package com.denfop.tiles.base;

import com.denfop.IUCore;
import com.denfop.audio.AudioSource;
import com.denfop.container.ContainerAnalyzer;
import com.denfop.gui.GuiAnalyzer;
import com.denfop.invslot.InvSlotAnalyzer;
import com.denfop.utils.ModUtils;
import ic2.api.network.INetworkClientTileEntityEventListener;
import ic2.core.ContainerBase;
import ic2.core.IC2;
import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.BlockPos;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import net.minecraftforge.oredict.OreDictionary;

import java.util.ArrayList;
import java.util.List;

public class TileEntityAnalyzer extends TileEntityElectricMachine implements INetworkClientTileEntityEventListener {

    public final InvSlotAnalyzer inputslot;
    public final InvSlotAnalyzer inputslotA;
    public boolean furnace;
    public int lucky;
    public int size;
    public int breakblock;
    public int numberores;
    public double sum;
    public int sum1;
    public boolean analysis;
    public int xTempChunk;
    public int zTempChunk;
    public int xChunk;
    public int zChunk;
    public int xendChunk;
    public int zendChunk;
    public int[] listnumberore1;
    public List<String> listore;
    public List<Integer> listnumberore;
    public List<Integer> yore;
    public List<Double> middleheightores;
    public int[][] chunksx;
    public int[][] chunksz;
    public int xcoord;
    public int zcoord;
    public int xendcoord;
    public int zendcoord;
    public boolean start = true;
    public AudioSource audioSource;
    public boolean quarry;
    public List<String> blacklist;
    public List<String> whitelist;
    List<Integer> y1;
    private int chunkx;
    private int chunkz;
    private int y;

    public TileEntityAnalyzer() {
        super(100000, 14, 1);
        this.listore = new ArrayList<>();

        this.listnumberore = new ArrayList<>();
        this.yore = new ArrayList<>();
        this.middleheightores = new ArrayList<>();
        this.analysis = false;
        this.sum = 0;
        this.sum1 = 0;
        this.numberores = 0;
        this.breakblock = 0;
        this.quarry = false;
        this.inputslot = new InvSlotAnalyzer(this, "input", 3, 0);
        this.inputslotA = new InvSlotAnalyzer(this, "input1", 1, 1);
        this.y1 = new ArrayList<>();
        this.y = 257;
        this.blacklist = new ArrayList<>();
        this.whitelist = new ArrayList<>();
        this.furnace = false;
        this.lucky = 0;
        this.size = 0;
        this.chunkx = 0;
        this.chunkz = 0;
    }

    public void update_chunk() {
        this.chunkx = this.getWorld().getChunkFromBlockCoords(this.pos).x * 16;
        this.chunkz = this.getWorld().getChunkFromBlockCoords(this.pos).z * 16;
        int size = this.size;
        this.xChunk = chunkx - 16 * size;
        this.zChunk = chunkz - 16 * size;
        this.xendChunk = chunkx + 16 + 16 * size;
        this.zendChunk = chunkz + 16 + 16 * size;
    }

    public double getProgress() {

        double temp = xChunk - xendChunk;
        double temp1 = zChunk - zendChunk;
        if (temp < 0) {
            temp *= -1;
        }
        if (temp1 < 0) {
            temp1 *= -1;
        }
        return Math.min(((temp * temp1 * this.y) / (temp * temp1 * 256)), 1);
    }

    public void readFromNBT(NBTTagCompound nbttagcompound) {
        super.readFromNBT(nbttagcompound);


        this.start = nbttagcompound.getBoolean("start");
        this.xChunk = nbttagcompound.getInteger("xChunk");
        this.zChunk = nbttagcompound.getInteger("zChunk");
        this.xendChunk = nbttagcompound.getInteger("xendChunk");
        this.zendChunk = nbttagcompound.getInteger("zendChunk");
        this.sum = nbttagcompound.getDouble("sum");
        this.sum1 = nbttagcompound.getInteger("sum1");
        this.breakblock = nbttagcompound.getInteger("breakblock");
        this.numberores = nbttagcompound.getInteger("numberores");

        int size4 = nbttagcompound.getInteger("size4");
        int size = nbttagcompound.getInteger("size");
        int size1 = nbttagcompound.getInteger("size1");
        int size2 = nbttagcompound.getInteger("size2");
        int size3 = nbttagcompound.getInteger("size3");
        for (int i = 0; i < size; i++) {
            this.listore.add(nbttagcompound.getString("ore" + i));
        }
        for (int i = 0; i < size1; i++) {
            this.listnumberore.add(nbttagcompound.getInteger("number" + i));
        }
        for (int i = 0; i < size2; i++) {
            this.y1.add(nbttagcompound.getInteger("y" + i));
        }
        for (int i = 0; i < size4; i++) {
            this.yore.add(nbttagcompound.getInteger("yore" + i));
        }

        for (int i = 0; i < size3; i++) {
            this.middleheightores.add(nbttagcompound.getDouble("middleheightores" + i));
        }


        this.analysis = nbttagcompound.getBoolean("analysis");
        this.quarry = nbttagcompound.getBoolean("quarry");

        this.xcoord = nbttagcompound.getInteger("xcoord");
        this.xendcoord = nbttagcompound.getInteger("xendcoord");
        this.zcoord = nbttagcompound.getInteger("zcoord");
        this.zendcoord = nbttagcompound.getInteger("zendcoord");

        this.xTempChunk = nbttagcompound.getInteger("xTempChunk");
        this.zTempChunk = nbttagcompound.getInteger("zTempChunk");
        this.chunksx = new int[this.xendcoord][this.zendcoord];
        this.chunksz = new int[this.xendcoord][this.zendcoord];
        for (int i = 0; i < this.xendcoord; i++) {
            for (int j = 0; j < this.zendcoord; j++) {
                this.chunksx[i][j] = nbttagcompound.getInteger("chunksx" + i + j);
                this.chunksz[i][j] = nbttagcompound.getInteger("chunksz" + i + j);
            }
        }
    }

    public void onNetworkEvent(EntityPlayer player, int event) {
        if (event == 1 && this.inputslot.quarry() && !this.analysis) {
            this.quarry = !this.quarry;
        }
        if (event == 0 && this.y >= 256 && !this.quarry) {
            this.analysis = !this.analysis;
        }
    }

    public void updateEntityServer() {
        super.updateEntityServer();

        if (this.analysis) {
            analyze();
        }
        if (this.quarry) {

            setActive(true);
            if (this.inputslot.getwirelessmodule()) {
                List list6 = this.inputslot.wirelessmodule();
                int xx = (int) list6.get(0);
                int yy = (int) list6.get(1);
                int zz = (int) list6.get(2);
                BlockPos pos1 = new BlockPos(xx, yy, zz);
                if (this.getWorld().getTileEntity(pos1) != null && this
                        .getWorld()
                        .getTileEntity(pos1) instanceof TileEntityBaseQuantumQuarry) {
                    TileEntityBaseQuantumQuarry target1 = (TileEntityBaseQuantumQuarry) this.getWorld().getTileEntity(pos1);
                    quarry(target1);
                }

            } else {
                for (EnumFacing direction : EnumFacing.values()) {
                    BlockPos pos1 = new BlockPos(
                            this.pos.getX() + direction.getFrontOffsetX(),
                            this.pos.getY() + direction.getFrontOffsetY(),
                            this.pos.getZ() + direction.getFrontOffsetZ()
                    );
                    TileEntity target = this.getWorld().getTileEntity(pos1);
                    if (target instanceof TileEntityBaseQuantumQuarry) {
                        TileEntityBaseQuantumQuarry target1 = (TileEntityBaseQuantumQuarry) target;
                        quarry(target1);
                    }
                }
            }


        }

    }

    public void analyze() {


        if (this.y >= 257 && this.start) {
            this.y = 0;
            this.breakblock = 0;
            this.numberores = 0;
            this.sum = 0;
            this.sum1 = 0;
            if (!this.getActive()) {
                initiate(0);
                setActive(true);
            }
            this.yore = new ArrayList<>();
            this.listore = new ArrayList<>();
            this.listnumberore = new ArrayList<>();
            this.y1 = new ArrayList<>();
            this.middleheightores = new ArrayList<>();
            int size = this.size;
            int size1 = size * 2 + 1;
            this.xTempChunk = chunkx - 16 * size;
            this.zTempChunk = chunkz - 16 * size;
            this.chunksx = new int[size1][size1];
            this.chunksz = new int[size1][size1];
            this.xcoord = 0;
            this.zcoord = 0;
            this.xendcoord = size1;
            this.zendcoord = size1;
            for (int i = 0; i < size1; i++) {
                for (int j = 0; j < size1; j++) {
                    int m1 = 1;
                    int m2 = 1;
                    if (i < size) {
                        m1 = -1;
                    }
                    if (j < size) {
                        m2 = -1;
                    }
                    this.chunksx[i][j] = chunkx + 16 * i * m1;
                    this.chunksz[i][j] = chunkz + 16 * j * m2;
                }
            }
            this.xChunk = chunkx - 16 * size;
            this.zChunk = chunkz - 16 * size;

            this.xendChunk = chunkx + 16 + 16 * size;
            this.zendChunk = chunkz + 16 + 16 * size;
            this.start = false;
        }
        int tempx = this.chunksx[this.xcoord][this.zcoord];
        int tempz = this.chunksz[this.xcoord][this.zcoord];
        List<String> blacklist = this.blacklist;
        List<String> whitelist = this.whitelist;
        if (this.getWorld().provider.getWorldTime() % 4 == 0) {
            for (int x = tempx; x < tempx + 16; x++) {
                for (int z = tempz; z < tempz + 16; z++) {
                    for (int yy = this.y; yy < this.y + 4; yy++) {
                        if (this.energy.getEnergy() < 1) {
                            break;
                        }
                        this.energy.useEnergy(1);

                        if (!this.getWorld().isAirBlock(new BlockPos(x, yy, z))) {
                            if (!this.getWorld().getBlockState(new BlockPos(x, yy, z)).getBlock().equals(Blocks.AIR)) {
                                this.breakblock++;
                                Block block = this.getWorld().getBlockState(new BlockPos(x, yy, z)).getBlock();
                                ItemStack stack = new ItemStack(block, 1,
                                        block.getMetaFromState(this.getWorld().getBlockState(new BlockPos(x, yy, z)))
                                );
                                if (!stack.isEmpty()) {
                                    if ((this
                                            .getWorld()
                                            .getBlockState(new BlockPos(x, yy, z))
                                            .getMaterial() == Material.IRON || this
                                            .getWorld()
                                            .getBlockState(new BlockPos(x, yy, z))
                                            .getMaterial() == Material.ROCK) && OreDictionary.getOreIDs(stack).length > 0) {


                                        int id = OreDictionary.getOreIDs(stack)[0];
                                        String name = OreDictionary.getOreName(id);
                                        if (name.startsWith("ore")) {
                                            if (!this.inputslot.CheckBlackList(
                                                    blacklist,
                                                    name
                                            ) && this.inputslot.CheckWhiteList(whitelist, name)) {

                                                if (this.listore.isEmpty()) {
                                                    this.listore.add(name);
                                                    this.listnumberore.add(1);
                                                    this.yore.add(yy);
                                                    this.y1.add(yy);
                                                    this.numberores = this.listore.size();
                                                    this.listnumberore1 = new int[this.listnumberore.size()];
                                                    for (int i = 0; i < this.listnumberore.size(); i++) {
                                                        this.listnumberore1[i] = this.listnumberore.get(i);
                                                    }

                                                    this.sum = ModUtils.getsum1(this.listnumberore) - this.listnumberore.size();
                                                    this.sum1 = ModUtils.getsum1(this.y1);
                                                    this.middleheightores = new ArrayList<>();
                                                    for (int i = 0; i < this.listore.size(); i++) {
                                                        this.middleheightores.add((this.yore.get(i) / (double) this.listnumberore.get(
                                                                i)));
                                                    }

                                                }

                                                if (!this.listore.contains(name)) {


                                                    this.listore.add(name);
                                                    this.listnumberore.add(1);
                                                    this.yore.add(yy);
                                                    this.y1.add(yy);
                                                    this.numberores = this.listore.size();
                                                    this.listnumberore1 = new int[this.listnumberore.size()];
                                                    for (int i = 0; i < this.listnumberore.size(); i++) {
                                                        this.listnumberore1[i] = this.listnumberore.get(i);
                                                    }

                                                    this.sum = ModUtils.getsum1(this.listnumberore) - this.listnumberore.size();
                                                    this.sum1 = ModUtils.getsum1(this.y1);
                                                    this.middleheightores = new ArrayList<>();
                                                    for (int i = 0; i < this.listore.size(); i++) {
                                                        this.middleheightores.add((this.yore.get(i) / (double) this.listnumberore.get(
                                                                i)));
                                                    }

                                                }
                                                if (listore.contains(name)) {
                                                    yore.set(listore.indexOf(name), yore.get(listore.indexOf(name)) + yy);

                                                    this.listnumberore.set(
                                                            listore.indexOf(name),
                                                            listnumberore.get(listore.indexOf(name)) + 1
                                                    );
                                                    this.y1.add(yy);
                                                    this.numberores = listore.size();
                                                    this.listnumberore1 = new int[this.listnumberore.size()];
                                                    for (int i = 0; i < this.listnumberore.size(); i++) {
                                                        this.listnumberore1[i] = this.listnumberore.get(i);
                                                    }

                                                    this.sum = ModUtils.getsum1(this.listnumberore) - this.listnumberore.size();
                                                    this.sum1 = ModUtils.getsum1(this.y1);
                                                    this.middleheightores = new ArrayList<>();
                                                    for (int i = 0; i < this.listore.size(); i++) {
                                                        this.middleheightores.add((this.yore.get(i) / (double) this.listnumberore.get(
                                                                i)));
                                                    }

                                                }


                                            }
                                        }
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }

        if (this.getWorld().provider.getWorldTime() % 4 == 0) {
            this.y += 4;


            if (this.y >= 256) {
                zcoord++;
                this.y = 0;
                if (zcoord == zendcoord) {
                    xcoord++;
                    zcoord = 0;
                    if (xcoord == xendcoord) {
                        zcoord = zendcoord;
                    }
                }

            }

            if (xcoord == xendcoord && zcoord == zendcoord) {
                this.analysis = false;
                this.setActive(false);
                this.xTempChunk = this.chunksx[this.xcoord - 1][this.zcoord - 1];
                this.zTempChunk = this.chunksz[this.xcoord - 1][this.zcoord - 1];
                this.y = 257;
                this.start = true;
                this.middleheightores = new ArrayList<>();
                for (int i = 0; i < this.listore.size(); i++) {
                    this.middleheightores.add((this.yore.get(i) / (double) this.listnumberore.get(i)));
                }
                initiate(2);
            }
        }
    }

    public void quarry(TileEntityBaseQuantumQuarry target1) {
        if (this.y >= 257 && this.start) {
            this.y = 0;
            this.breakblock = 0;
            this.numberores = 0;
            this.sum = 0;
            this.sum1 = 0;
            if (!this.getActive()) {
                initiate(0);
                setActive(true);
            }
            int size = this.size;
            int size1 = size * 2 + 1;
            this.xTempChunk = chunkx - 16 * size;
            this.zTempChunk = chunkz - 16 * size;
            this.chunksx = new int[size1][size1];
            this.chunksz = new int[size1][size1];
            this.xcoord = 0;
            this.zcoord = 0;
            this.xendcoord = size1;
            this.zendcoord = size1;
            for (int i = 0; i < size1; i++) {
                for (int j = 0; j < size1; j++) {
                    int m1 = 1;
                    int m2 = 1;
                    if (i < size) {
                        m1 = -1;
                    }
                    if (j < size) {
                        m2 = -1;
                    }
                    this.chunksx[i][j] = chunkx + 16 * i * m1;
                    this.chunksz[i][j] = chunkz + 16 * j * m2;
                }
            }
            this.xChunk = chunkx - 16 * size;
            this.zChunk = chunkz - 16 * size;

            this.xendChunk = chunkx + 16 + 16 * size;
            this.zendChunk = chunkz + 16 + 16 * size;
            this.start = false;
        }
        int tempx = this.chunksx[this.xcoord][this.zcoord];
        int tempz = this.chunksz[this.xcoord][this.zcoord];
        List<String> blacklist = this.blacklist;
        List<String> whitelist = this.whitelist;
        if (this.getWorld().provider.getWorldTime() % 4 == 0) {
            for (int x = tempx; x < tempx + 16; x++) {
                for (int z = tempz; z < tempz + 16; z++) {
                    for (int yy = this.y; yy < this.y + 4; yy++) {
                        if (this.energy.getEnergy() < 1) {
                            break;
                        }
                        this.energy.useEnergy(1);
                        if (!this.getWorld().isAirBlock(new BlockPos(x, yy, z))) {
                            if (!this.getWorld().getBlockState(new BlockPos(x, yy, z)).getBlock().equals(Blocks.AIR)) {
                                this.breakblock++;
                                Block block = this.getWorld().getBlockState(new BlockPos(x, yy, z)).getBlock();
                                ItemStack stack = new ItemStack(block, 1,
                                        block.getMetaFromState(this.getWorld().getBlockState(new BlockPos(x, yy, z)))
                                );
                                if (!stack.isEmpty()) {
                                    if ((this
                                            .getWorld()
                                            .getBlockState(new BlockPos(x, yy, z))
                                            .getMaterial() == Material.IRON || this
                                            .getWorld()
                                            .getBlockState(new BlockPos(x, yy, z))
                                            .getMaterial() == Material.ROCK) && OreDictionary.getOreIDs(stack).length > 0) {
                                        int id = OreDictionary.getOreIDs(stack)[0];
                                        String name = OreDictionary.getOreName(id);
                                        if (name.startsWith("ore")) {
                                            if (!(!this.inputslot.CheckBlackList(
                                                    blacklist,
                                                    name
                                            ) && this.inputslot.CheckWhiteList(
                                                    whitelist,
                                                    name
                                            ))) {
                                                continue;
                                            }
                                            double energycost = this.inputslot.getenergycost(target1);
                                            String temp = name.substring(3);

                                            if (temp.startsWith("Infused")) {
                                                temp = name.substring("Infused".length() + 3);
                                            }

                                            if (!name.equals("oreRedstone") && (OreDictionary.getOres("gem" + temp) == null || OreDictionary
                                                    .getOres("gem" + temp)
                                                    .size() < 1) && (OreDictionary.getOres("shard" + temp) == null || OreDictionary
                                                    .getOres("shard" + temp)
                                                    .size() < 1)) {

                                                boolean furnace = this.furnace;
                                                if (!furnace) {
                                                    if (!target1.list(target1, stack)) {
                                                        if (target1.energy.getEnergy() >= energycost &&
                                                                target1.outputSlot.canAdd(stack)) {
                                                            target1.outputSlot.add(stack);
                                                            this.getWorld().setBlockToAir(new BlockPos(x, yy, z));
                                                            target1.energy.useEnergy(energycost);
                                                            target1.getblock++;
                                                        }
                                                    }
                                                } else {
                                                    temp = name.substring(3);
                                                    temp = "ingot" + temp;
                                                    if (OreDictionary.getOres(temp).isEmpty()) {
                                                        if (!target1.list(target1, stack)) {
                                                            if (target1.energy.getEnergy() >= energycost &&
                                                                    target1.outputSlot.canAdd(stack)) {
                                                                target1.outputSlot.add(stack);
                                                                this.getWorld().setBlockToAir(new BlockPos(x, yy, z));
                                                                target1.energy.useEnergy(energycost);
                                                                target1.getblock++;
                                                            }
                                                        }
                                                    } else {

                                                        ItemStack stack1 = OreDictionary.getOres(temp).get(0);
                                                        if (!target1.list(target1, stack)) {
                                                            if (target1.energy.getEnergy() >= energycost &&
                                                                    target1.outputSlot.canAdd(stack1)) {
                                                                target1.outputSlot.add(stack1);
                                                                this.getWorld().setBlockToAir(new BlockPos(x, yy, z));
                                                                target1.energy.useEnergy(energycost);
                                                                target1.getblock++;
                                                            }
                                                        }
                                                    }
                                                }
                                            } else {
                                                ItemStack gem = null;

                                                if (OreDictionary.getOres("gem" + temp).size() != 0) {
                                                    gem = OreDictionary.getOres("gem" + temp).get(0);
                                                } else if (OreDictionary.getOres("shard" + temp).size() != 0) {
                                                    gem = OreDictionary.getOres("shard" + temp).get(0);
                                                } else if (OreDictionary.getOres("dust" + temp).size() != 0) {
                                                    gem = OreDictionary.getOres("dust" + temp).get(0);
                                                }
                                                int chance2 = this.lucky;


                                                List<Boolean> get = new ArrayList<>();
                                                if (!target1.list(target1, stack)) {
                                                    if (target1.energy.getEnergy() >= energycost) {
                                                        for (int j = 0; j < chance2 + 1; j++) {
                                                            if (target1.outputSlot.canAdd(gem)) {
                                                                target1.outputSlot.add(gem);
                                                                get.add(true);
                                                            } else {
                                                                get.add(false);
                                                            }
                                                        }
                                                    }
                                                }
                                                if (ModUtils.Boolean(get)) {
                                                    this.getWorld().setBlockToAir(new BlockPos(x, yy, z));
                                                    target1.energy.useEnergy(energycost);
                                                    target1.getblock++;
                                                }

                                            }
                                        }
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }

        if (this.getWorld().provider.getWorldTime() % 4 == 0) {
            this.y += 4;


            if (this.y >= 256) {
                this.zcoord++;
                this.y = 0;
                if (this.zcoord == this.zendcoord) {
                    this.xcoord++;
                    this.zcoord = 0;
                    if (this.xcoord == this.xendcoord) {
                        this.zcoord = this.zendcoord;
                    }
                }

            }

            if (this.xcoord == this.xendcoord && this.zcoord == this.zendcoord) {
                this.setActive(false);
                this.xTempChunk = this.chunksx[this.xcoord - 1][this.zcoord - 1];
                this.zTempChunk = this.chunksz[this.xcoord - 1][this.zcoord - 1];
                this.y = 257;
                this.start = true;
                this.quarry = false;
                this.analysis = true;
                this.initiate(2);
                this.analyze();
            }
        }
    }

    public NBTTagCompound writeToNBT(NBTTagCompound nbttagcompound) {
        super.writeToNBT(nbttagcompound);


        for (int i = 0; i < this.xendcoord; i++) {
            for (int j = 0; j < this.zendcoord; j++) {
                nbttagcompound.setInteger(("chunksx" + i + j), this.chunksx[i][j]);
                nbttagcompound.setInteger(("chunksz" + i + j), this.chunksz[i][j]);
            }
        }


        nbttagcompound.setInteger("size4", this.yore.size());
        nbttagcompound.setBoolean("start", this.start);
        nbttagcompound.setInteger("xcoord", this.xcoord);
        nbttagcompound.setInteger("xendcoord", this.xendcoord);
        nbttagcompound.setInteger("zcoord", this.zcoord);
        nbttagcompound.setInteger("zendcoord", this.zendcoord);

        nbttagcompound.setInteger("xTempChunk", this.xTempChunk);
        nbttagcompound.setInteger("zTempChunk", this.zTempChunk);


        nbttagcompound.setInteger("xChunk", this.xChunk);
        nbttagcompound.setInteger("zChunk", this.zChunk);
        nbttagcompound.setInteger("xendChunk", this.xendChunk);
        nbttagcompound.setInteger("zendChunk", this.zendChunk);
        nbttagcompound.setDouble("sum", this.sum);
        nbttagcompound.setInteger("sum1", this.sum1);
        nbttagcompound.setInteger("breakblock", this.breakblock);
        nbttagcompound.setInteger("numberores", this.numberores);
        nbttagcompound.setInteger("size", this.listore.size());
        nbttagcompound.setInteger("size1", this.listnumberore.size());
        nbttagcompound.setInteger("size2", this.y1.size());
        nbttagcompound.setInteger("size3", this.middleheightores.size());
        nbttagcompound.setBoolean("analysis", this.analysis);
        nbttagcompound.setBoolean("quarry", this.quarry);

        for (int i = 0; i < this.yore.size(); i++) {
            nbttagcompound.setInteger(("yore" + i), this.yore.get(i));
        }
        for (int i = 0; i < this.listore.size(); i++) {
            nbttagcompound.setString(("ore" + i), this.listore.get(i));
        }
        for (int i = 0; i < this.listnumberore.size(); i++) {
            nbttagcompound.setInteger(("number" + i), this.listnumberore.get(i));
        }
        for (int i = 0; i < this.middleheightores.size(); i++) {
            nbttagcompound.setDouble(("middleheightores" + i), this.middleheightores.get(i));
        }
        for (int i = 0; i < this.y1.size(); i++) {
            nbttagcompound.setInteger(("y" + i), this.y1.get(i));
        }
        return nbttagcompound;
    }

    @Override
    protected void onLoaded() {
        super.onLoaded();
        this.inputslot.update();
    }

    public void onUnloaded() {
        super.onUnloaded();
        if (IC2.platform.isRendering() && this.audioSource != null) {
            IUCore.audioManager.removeSources(this);
            this.audioSource = null;
        }
    }

    public void onNetworkEvent(int event) {
        if (this.audioSource == null && getStartSoundFile() != null) {
            this.audioSource = IUCore.audioManager.createSource(this, getStartSoundFile());
        }
        switch (event) {
            case 0:
                if (this.audioSource != null) {
                    this.audioSource.play();
                }
                break;
            case 1:
                if (this.audioSource != null) {
                    this.audioSource.stop();
                    if (getInterruptSoundFile() != null) {
                        IUCore.audioManager.playOnce(this, getInterruptSoundFile());
                    }
                }
                break;
            case 2:
                if (this.audioSource != null) {
                    this.audioSource.stop();
                }
                break;


        }
    }

    @Override
    @SideOnly(Side.CLIENT)
    public GuiScreen getGui(EntityPlayer entityPlayer, boolean isAdmin) {
        return new GuiAnalyzer(new ContainerAnalyzer(entityPlayer, this));
    }

    public ContainerBase<? extends TileEntityAnalyzer> getGuiContainer(EntityPlayer entityPlayer) {
        return new ContainerAnalyzer(entityPlayer, this);
    }

}
