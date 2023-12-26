package com.denfop.tiles.base;

import com.denfop.IUItem;
import com.denfop.Localization;
import com.denfop.api.tile.IMultiTileBlock;
import com.denfop.api.vein.Type;
import com.denfop.api.vein.Vein;
import com.denfop.api.vein.VeinSystem;
import com.denfop.audio.EnumSound;
import com.denfop.blocks.BlockTileEntity;
import com.denfop.blocks.mechanism.BlockBaseMachine2;
import com.denfop.container.ContainerAnalyzer;
import com.denfop.gui.GuiAnalyzer;
import com.denfop.invslot.InvSlotAnalyzer;
import com.denfop.network.DecoderHandler;
import com.denfop.network.EncoderHandler;
import com.denfop.network.IUpdatableTileEvent;
import com.denfop.network.packet.CustomPacketBuffer;
import com.denfop.tiles.mechanism.TileEntityAnalyzerChest;
import com.denfop.utils.ModUtils;
import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.SoundEvent;
import net.minecraft.util.math.BlockPos;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import net.minecraftforge.oredict.OreDictionary;
import org.lwjgl.input.Keyboard;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicBoolean;

public class TileAnalyzer extends TileElectricMachine implements IUpdatableTileEvent {

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
    public boolean quarry;
    public List<String> blacklist;
    public List<String> whitelist;
    public double consume;
    List<Integer> y1;
    private int chunkx;
    private int chunkz;
    private int y;
    private FakePlayerSpawner fake;

    public TileAnalyzer() {
        super(10000000, 14, 1);
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

    @Override
    public int getSizeInventory() {
        return 1;
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

    @SideOnly(Side.CLIENT)
    public void addInformation(final ItemStack stack, final List<String> tooltip, final ITooltipFlag advanced) {
        if (!Keyboard.isKeyDown(Keyboard.KEY_LSHIFT)) {
            tooltip.add(Localization.translate("press.lshift"));
        }
        if (Keyboard.isKeyDown(Keyboard.KEY_LSHIFT)) {
            tooltip.add(Localization.translate("iu.machines_work_energy") + 512 + Localization.translate("iu" +
                    ".machines_work_energy_type_eu"));
        }
        super.addInformation(stack, tooltip, advanced);
    }

    public IMultiTileBlock getTeBlock() {
        return BlockBaseMachine2.analyzer;
    }

    public BlockTileEntity getBlock() {
        return IUItem.basemachine1;
    }

    @Override
    public void readContainerPacket(final CustomPacketBuffer customPacketBuffer) {
        super.readContainerPacket(customPacketBuffer);
        try {
            xChunk = (int) DecoderHandler.decode(customPacketBuffer);
            zChunk = (int) DecoderHandler.decode(customPacketBuffer);
            xendChunk = (int) DecoderHandler.decode(customPacketBuffer);
            zendChunk = (int) DecoderHandler.decode(customPacketBuffer);
            sum = (double) DecoderHandler.decode(customPacketBuffer);
            sum1 = (int) DecoderHandler.decode(customPacketBuffer);
            numberores = (int) DecoderHandler.decode(customPacketBuffer);
            listore = (List<String>) DecoderHandler.decode(customPacketBuffer);
            listnumberore = (List<Integer>) DecoderHandler.decode(customPacketBuffer);
            breakblock = (int) DecoderHandler.decode(customPacketBuffer);
            quarry = (boolean) DecoderHandler.decode(customPacketBuffer);
            analysis = (boolean) DecoderHandler.decode(customPacketBuffer);
            y = (int) DecoderHandler.decode(customPacketBuffer);
            yore = (List<Integer>) DecoderHandler.decode(customPacketBuffer);
            middleheightores = (List<Double>) DecoderHandler.decode(customPacketBuffer);
            consume = (double) DecoderHandler.decode(customPacketBuffer);
            xcoord = (int) DecoderHandler.decode(customPacketBuffer);
            xendcoord = (int) DecoderHandler.decode(customPacketBuffer);
            zcoord = (int) DecoderHandler.decode(customPacketBuffer);
            zendcoord = (int) DecoderHandler.decode(customPacketBuffer);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

    }

    @Override
    public CustomPacketBuffer writeContainerPacket() {
        final CustomPacketBuffer packet = super.writeContainerPacket();
        try {
            EncoderHandler.encode(packet, xChunk);
            EncoderHandler.encode(packet, zChunk);
            EncoderHandler.encode(packet, xendChunk);
            EncoderHandler.encode(packet, zendChunk);
            EncoderHandler.encode(packet, sum);
            EncoderHandler.encode(packet, sum1);
            EncoderHandler.encode(packet, numberores);
            EncoderHandler.encode(packet, listore);
            EncoderHandler.encode(packet, listnumberore);
            EncoderHandler.encode(packet, breakblock);
            EncoderHandler.encode(packet, quarry);
            EncoderHandler.encode(packet, analysis);
            EncoderHandler.encode(packet, y);
            EncoderHandler.encode(packet, yore);
            EncoderHandler.encode(packet, middleheightores);
            EncoderHandler.encode(packet, consume);
            EncoderHandler.encode(packet, xcoord);
            EncoderHandler.encode(packet, xendcoord);
            EncoderHandler.encode(packet, zcoord);
            EncoderHandler.encode(packet, zendcoord);

        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        return packet;
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

    public void updateTileServer(EntityPlayer player, double event) {
        if (event == 1 && this.inputslot.quarry() && !this.analysis) {
            this.quarry = !this.quarry;
        }
        if (event == 0 && this.y >= 256 && !this.quarry) {
            this.analysis = !this.analysis;
        }
        if (event == 10) {
            super.updateTileServer(player, event);
        }
    }

    public void updateEntityServer() {
        super.updateEntityServer();

        if (this.analysis) {
            analyze();
        }
        if (this.quarry) {
            if (!this.getActive()) {
                setActive(true);
            }
            if (this.inputslot.getwirelessmodule()) {
                List<Integer> list6 = this.inputslot.wirelessmodule();
                int xx = list6.get(0);
                int yy = list6.get(1);
                int zz = list6.get(2);
                BlockPos pos1 = new BlockPos(xx, yy, zz);
                final TileEntity tile = this.getWorld().getTileEntity(pos1);
                if (tile instanceof TileEntityAnalyzerChest) {
                    TileEntityAnalyzerChest target1 = (TileEntityAnalyzerChest) tile;
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
                    if (target instanceof TileEntityAnalyzerChest) {
                        TileEntityAnalyzerChest target1 = (TileEntityAnalyzerChest) target;
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
        for (int x = tempx; x < tempx + 16; x++) {
            for (int z = tempz; z < tempz + 16; z++) {
                for (int yy = this.y; yy < this.y + 2; yy++) {
                    if (this.energy.getEnergy() < 1) {
                        break;
                    }
                    this.energy.useEnergy(1);
                    final IBlockState blockstate = this.getWorld().getBlockState(new BlockPos(
                            x,
                            yy,
                            z
                    ));
                    if (!(blockstate.getMaterial() == Material.AIR)) {
                        this.breakblock++;

                        Block block = blockstate.getBlock();
                        ItemStack stack = new ItemStack(block, 1,
                                block.getMetaFromState(blockstate)
                        );
                        if (!stack.isEmpty()) {
                            if ((blockstate
                                    .getMaterial() == Material.IRON || blockstate
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
                                                if (i < this.yore.size()) {
                                                    this.middleheightores.add((this.yore.get(i) / (double) this.listnumberore.get(
                                                            i)));
                                                } else {
                                                    this.middleheightores.add((0 / (double) this.listnumberore.get(
                                                            i)));
                                                }
                                            }

                                        }
                                        if (listore.contains(name)) {
                                            final int index = listore.indexOf(name);
                                            if (index < this.yore.size()) {
                                                yore.set(index, yore.get(index) + yy);
                                            }


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
                                                if (i < this.yore.size()) {
                                                    this.middleheightores.add((this.yore.get(i) / (double) this.listnumberore.get(
                                                            i)));
                                                } else {
                                                    this.middleheightores.add((0 / (double) this.listnumberore.get(
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


        this.y += 2;


        if (this.y >= 256) {
            final Vein vein = VeinSystem.system.getVein(this
                    .getWorld()
                    .getChunkFromBlockCoords(new BlockPos(tempx, 0, tempz))
                    .getPos());
            if (vein != VeinSystem.system.getEMPTY()) {
                if (vein.getType() == Type.VEIN) {
                    final ItemStack stack = new ItemStack(IUItem.heavyore, 1, vein.getMeta());
                    int id = OreDictionary.getOreIDs(stack)[0];
                    String name = OreDictionary.getOreName(id);
                    boolean has = false;
                    if (!this.listore.contains(name)) {

                        has = true;
                        this.listore.add(name);
                        this.listnumberore.add(vein.getCol());

                        this.numberores = this.listore.size();


                    }
                    if (listore.contains(name) && !has) {


                        this.listnumberore.set(
                                listore.indexOf(name),
                                listnumberore.get(listore.indexOf(name)) + vein.getCol()
                        );

                        this.numberores = listore.size();
                        this.listnumberore1 = new int[this.listnumberore.size()];
                        for (int i = 0; i < this.listnumberore.size(); i++) {
                            this.listnumberore1[i] = this.listnumberore.get(i);
                        }

                        this.sum = ModUtils.getsum1(this.listnumberore) - this.listnumberore.size();


                    }
                }
            }
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
                if (i < this.yore.size()) {
                    this.middleheightores.add((this.yore.get(i) / (double) this.listnumberore.get(i)));
                } else {
                    this.middleheightores.add((0 / (double) this.listnumberore.get(i)));
                }
            }
            initiate(2);
        }

    }

    public void quarry(TileEntityAnalyzerChest target1) {
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
            this.fake = new FakePlayerSpawner(world);
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

                        final IBlockState blockstate = this.getWorld().getBlockState(new BlockPos(x, yy, z));
                        if (!(blockstate.getMaterial() == Material.AIR)) {
                            final TileEntity tile = this.getWorld().getTileEntity(new BlockPos(x, yy, z));
                            if (tile == null && !blockstate.getBlock().equals(Blocks.AIR)) {
                                this.breakblock++;
                                Block block = blockstate.getBlock();
                                ItemStack stack = new ItemStack(block, 1,
                                        block.getMetaFromState(blockstate)
                                );

                                if (!stack.isEmpty()) {
                                    if ((blockstate
                                            .getMaterial() == Material.IRON || blockstate
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
                                            final List<ItemStack> drops = block.getDrops(
                                                    world,
                                                    new BlockPos(x, yy, z),
                                                    blockstate,
                                                    0
                                            );
                                            final AtomicBoolean need = new AtomicBoolean(false);
                                            drops.forEach(
                                                    drop -> {
                                                        if (OreDictionary.getOreIDs(drop).length > 0) {
                                                            int id1 = OreDictionary.getOreIDs(drop)[0];
                                                            String name1 = OreDictionary.getOreName(id1);
                                                            if (name1.startsWith("shard") || name1.startsWith("gem") || name1.startsWith(
                                                                    "dust")) {

                                                                ItemStack gem = ItemStack.EMPTY;

                                                                if (OreDictionary.getOres(name1).size() != 0) {
                                                                    gem = OreDictionary.getOres(name1).get(0);
                                                                }

                                                                int chance2 = this.lucky;


                                                                boolean get = false;
                                                                if (this.energy.getEnergy() >= consume && !gem.isEmpty()) {
                                                                    for (int j = 0; j < chance2 + 1; j++) {
                                                                        if (target1.outputSlot.add(gem)) {
                                                                            get = true;
                                                                        }
                                                                    }
                                                                }

                                                                if (get) {
                                                                    need.set(true);
                                                                    this.energy.useEnergy(consume);
                                                                }
                                                            } else {
                                                                boolean furnace = this.furnace;
                                                                if (!furnace) {

                                                                    if (this.energy.getEnergy() >= consume &&
                                                                            target1.outputSlot.add(drop)) {
                                                                        need.set(true);
                                                                        this.energy.useEnergy(consume);
                                                                    }
                                                                } else {
                                                                    String temp = name.substring(3);
                                                                    temp = "ingot" + temp;
                                                                    if (OreDictionary.getOres(temp).isEmpty()) {


                                                                        if (this.energy.getEnergy() >= consume &&
                                                                                target1.outputSlot.add(drop)) {
                                                                            {
                                                                                need.set(true);
                                                                                this.energy.useEnergy(consume);
                                                                            }
                                                                        }
                                                                    } else {

                                                                        ItemStack stack1 = OreDictionary.getOres(temp).get(0);
                                                                        if (this.energy.getEnergy() >= consume &&
                                                                                target1.outputSlot.add(stack1)) {
                                                                            need.set(true);
                                                                            this.energy.useEnergy(consume);
                                                                        }

                                                                    }
                                                                }
                                                            }
                                                        }
                                                    }
                                            );
                                            if (need.get()) {
                                                block.removedByPlayer(
                                                        blockstate,
                                                        world,
                                                        new BlockPos(x, yy, z),
                                                        this.fake,
                                                        true
                                                );
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
                final Vein vein = VeinSystem.system.getVein(this
                        .getWorld()
                        .getChunkFromBlockCoords(new BlockPos(tempx, 0, tempz))
                        .getPos());
                if (vein != VeinSystem.system.getEMPTY()) {
                    if (vein.getType() == Type.VEIN) {
                        final ItemStack stack = new ItemStack(IUItem.heavyore, 1, vein.getMeta());
                        int size = vein.getCol();
                        for (int i = 0; i < size; i++) {
                            if (target1.outputSlot.add(stack)) {
                                vein.removeCol(1);
                            } else {
                                break;
                            }

                        }
                    }
                }
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
    public void onLoaded() {
        super.onLoaded();
        this.inputslot.update();
    }

    public void onUnloaded() {
        super.onUnloaded();
    }


    @Override
    @SideOnly(Side.CLIENT)
    public GuiAnalyzer getGui(EntityPlayer entityPlayer, boolean isAdmin) {
        return new GuiAnalyzer(new ContainerAnalyzer(entityPlayer, this));
    }

    public ContainerAnalyzer getGuiContainer(EntityPlayer entityPlayer) {
        return new ContainerAnalyzer(entityPlayer, this);
    }

    @Override
    public SoundEvent getSound() {
        return EnumSound.analyzer.getSoundEvent();
    }

}
