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
import com.denfop.componets.AirPollutionComponent;
import com.denfop.componets.SoilPollutionComponent;
import com.denfop.container.ContainerAnalyzer;
import com.denfop.gui.GuiAnalyzer;
import com.denfop.invslot.InventoryAnalyzer;
import com.denfop.network.DecoderHandler;
import com.denfop.network.EncoderHandler;
import com.denfop.network.IUpdatableTileEvent;
import com.denfop.network.packet.CustomPacketBuffer;
import com.denfop.tiles.mechanism.TileEntityAnalyzerChest;
import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.player.EntityPlayer;
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
import java.util.Collections;
import java.util.List;

public class TileAnalyzer extends TileElectricMachine implements IUpdatableTileEvent {

    public final InventoryAnalyzer inputslot;
    public final InventoryAnalyzer inputslotA;
    private final SoilPollutionComponent pollutionSoil;
    private final AirPollutionComponent pollutionAir;
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
    public DataOreList<DataOre> dataOreList;
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
    public boolean macerator;
    public boolean comb_macerator;
    public boolean polisher;
    private int chunkx;
    private int chunkz;
    private int y;
    private FakePlayerSpawner fake;
    private int indexOre;
    private int indexPos;
    private int indexVein;

    public TileAnalyzer() {
        super(10000000, 14, 1);

        this.analysis = false;
        this.numberores = 0;
        this.breakblock = 0;
        dataOreList = new DataOreList<>();
        this.quarry = false;
        this.inputslot = new InventoryAnalyzer(this, "input", 3, 0);
        this.inputslotA = new InventoryAnalyzer(this, "input1", 1, 1);
        this.y = 257;
        this.blacklist = new ArrayList<>();
        this.whitelist = new ArrayList<>();
        this.furnace = false;
        this.lucky = 0;
        this.size = 0;
        this.chunkx = 0;
        this.chunkz = 0;
        this.pollutionSoil = this.addComponent(new SoilPollutionComponent(this, 0.5));
        this.pollutionAir = this.addComponent(new AirPollutionComponent(this, 1));
    }

    public DataOreList<DataOre> getDataOreList() {
        return dataOreList;
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


    public void addInformation(final ItemStack stack, final List<String> tooltip) {
        if (!Keyboard.isKeyDown(Keyboard.KEY_LSHIFT)) {
            tooltip.add(Localization.translate("press.lshift"));
        }
        if (Keyboard.isKeyDown(Keyboard.KEY_LSHIFT)) {
            tooltip.add(Localization.translate("iu.machines_work_energy") + 512 + Localization.translate("iu" +
                    ".machines_work_energy_type_eu"));
        }
        super.addInformation(stack, tooltip);
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
            y = (int) DecoderHandler.decode(customPacketBuffer);
            xChunk = (int) DecoderHandler.decode(customPacketBuffer);
            zChunk = (int) DecoderHandler.decode(customPacketBuffer);
            xendChunk = (int) DecoderHandler.decode(customPacketBuffer);
            zendChunk = (int) DecoderHandler.decode(customPacketBuffer);
            breakblock = (int) DecoderHandler.decode(customPacketBuffer);
            quarry = (boolean) DecoderHandler.decode(customPacketBuffer);
            analysis = (boolean) DecoderHandler.decode(customPacketBuffer);
            consume = (double) DecoderHandler.decode(customPacketBuffer);
            xcoord = (int) DecoderHandler.decode(customPacketBuffer);
            xendcoord = (int) DecoderHandler.decode(customPacketBuffer);
            zcoord = (int) DecoderHandler.decode(customPacketBuffer);
            zendcoord = (int) DecoderHandler.decode(customPacketBuffer);
            numberores = (int) DecoderHandler.decode(customPacketBuffer);
            final List<DataOre> list = (List<DataOre>) DecoderHandler.decode(customPacketBuffer);
            this.dataOreList = new DataOreList<>();
            this.dataOreList.addAll(list);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

    }

    @Override
    public CustomPacketBuffer writeContainerPacket() {
        final CustomPacketBuffer packet = super.writeContainerPacket();
        try {
            EncoderHandler.encode(packet, y);
            EncoderHandler.encode(packet, xChunk);
            EncoderHandler.encode(packet, zChunk);
            EncoderHandler.encode(packet, xendChunk);
            EncoderHandler.encode(packet, zendChunk);
            EncoderHandler.encode(packet, breakblock);
            EncoderHandler.encode(packet, quarry);
            EncoderHandler.encode(packet, analysis);
            EncoderHandler.encode(packet, consume);
            EncoderHandler.encode(packet, xcoord);
            EncoderHandler.encode(packet, xendcoord);
            EncoderHandler.encode(packet, zcoord);
            EncoderHandler.encode(packet, zendcoord);
            EncoderHandler.encode(packet, numberores);
            EncoderHandler.encode(packet, this.dataOreList);
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

        int size = nbttagcompound.getInteger("size_DataOre");
        final NBTTagCompound dataOreTag = nbttagcompound.getCompoundTag("DataOre");
        for (int i = 0; i < size; i++) {
            this.dataOreList.add(new DataOre(dataOreTag.getCompoundTag(String.valueOf(i))));
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
                initiate(0);
                this.indexOre = 0;
                this.indexPos = 0;
                this.breakblock = 0;
                this.indexVein = 0;
            }

            if (this.dataOreList.isEmpty()) {
                this.quarry = false;
                this.setActive(false);
                this.analysis = true;
                this.analyze();
                return;
            }
            if (this.getWorld().provider.getWorldTime() % 2 == 0) {
                if (this.inputslot.getwirelessmodule()) {
                    List<Integer> list6 = this.inputslot.wirelessmodule();
                    int xx = list6.get(0);
                    int yy = list6.get(1);
                    int zz = list6.get(2);
                    BlockPos pos1 = new BlockPos(xx, yy, zz);
                    final TileEntity tile = this.getWorld().getTileEntity(pos1);
                    if (tile instanceof TileEntityAnalyzerChest) {
                        TileEntityAnalyzerChest target1 = (TileEntityAnalyzerChest) tile;
                        quarry(Collections.singletonList(target1));

                    }

                } else {
                    List<TileEntityAnalyzerChest> list = new ArrayList<>();
                    for (EnumFacing direction : EnumFacing.values()) {
                        BlockPos pos1 = new BlockPos(
                                this.pos.getX() + direction.getFrontOffsetX(),
                                this.pos.getY() + direction.getFrontOffsetY(),
                                this.pos.getZ() + direction.getFrontOffsetZ()
                        );
                        TileEntity target = this.getWorld().getTileEntity(pos1);
                        if (target instanceof TileEntityAnalyzerChest) {
                            TileEntityAnalyzerChest target1 = (TileEntityAnalyzerChest) target;
                            list.add(target1);

                        }
                    }
                    quarry(list);
                }

            }
        }

    }

    public void analyze() {


        if (this.y >= 257 && this.start) {
            this.y = 0;
            this.breakblock = 0;
            this.numberores = 0;
            if (!this.getActive()) {
                initiate(0);
                setActive(true);
            }
            this.dataOreList = new DataOreList<>();
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
                for (int yy = this.y; yy < this.y + 4; yy++) {
                    if (this.energy.getEnergy() < 1) {
                        break;
                    }
                    this.energy.useEnergy(1);
                    final BlockPos pos1 = new BlockPos(
                            x,
                            yy,
                            z
                    );
                    final IBlockState blockstate = this.getWorld().getBlockState(pos1);
                    if (!(blockstate.getMaterial() == Material.AIR)) {
                        this.breakblock++;

                        Block block = blockstate.getBlock();
                        ItemStack stack = new ItemStack(block, 1,
                                block.getMetaFromState(blockstate)
                        );
                        if (stack.isEmpty()) {
                            continue;
                        }
                        int[] ints = OreDictionary.getOreIDs(stack);
                        if (!stack.isEmpty()) {
                            if ((blockstate
                                    .getMaterial() == Material.IRON || blockstate
                                    .getMaterial() == Material.ROCK) && ints.length > 0) {


                                int id = ints[0];
                                String name = OreDictionary.getOreName(id);
                                if (name.startsWith("ore")) {
                                    if (!this.inputslot.CheckBlackList(
                                            blacklist,
                                            name
                                    ) && this.inputslot.CheckWhiteList(whitelist, name)) {

                                        DataOre dataOre = this.dataOreList.get(name);
                                        if (dataOre == null) {
                                            dataOre = new DataOre(name, 1, yy, pos1, stack, blockstate);
                                            this.dataOreList.add(dataOre);
                                            numberores++;
                                        } else {

                                            dataOre.addNumber(1);
                                            dataOre.addY(yy);
                                            dataOre.addPos(pos1);
                                            numberores++;

                                        }


                                    }
                                }
                            }
                        }

                    }
                }
            }
        }


        this.y += 4;


        if (this.y >= 256) {
            final Vein vein = VeinSystem.system.getVein(this
                    .getWorld()
                    .getChunkFromBlockCoords(new BlockPos(tempx, 0, tempz))
                    .getPos());
            if (vein != VeinSystem.system.getEMPTY()) {
                if (vein.getType() == Type.VEIN && vein.isFind() && vein.getCol() > 0) {
                    final ItemStack stack;
                    if (vein.isOldMineral()) {
                        stack = new ItemStack(IUItem.heavyore, 1, vein.getMeta());
                    } else {
                        stack = new ItemStack(IUItem.mineral, 1, vein.getMeta());
                    }
                    int id = OreDictionary.getOreIDs(stack)[0];
                    String name = OreDictionary.getOreName(id);
                    DataOre dataOre = this.dataOreList.get(name);
                    if (dataOre == null) {
                        dataOre = new DataOre(name, vein.getCol(), 0, null, stack, null);
                        this.dataOreList.add(dataOre);
                    } else {
                        dataOre.addNumber(vein.getCol());
                    }
                    dataOre.addVein(vein);
                    numberores += vein.getCol();
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
            initiate(2);
        }

    }

    public void quarry(List<TileEntityAnalyzerChest> target1) {
        if (!this.quarry) {
            return;
        }

        List<String> blacklist = this.blacklist;
        List<String> whitelist = this.whitelist;

        DataOre dataOre = this.dataOreList.get(this.indexOre);
        if (dataOre.getListPos().isEmpty() && (dataOre.getVeinsList().isEmpty() || indexVein == dataOre.getVeinsList().size())) {
            this.indexOre++;
            this.indexPos = 0;
            if (this.dataOreList.size() == indexOre) {
                quarry = false;
                this.analysis = true;
                this.analyze();
            }
            return;
        }
        int old = this.indexPos;
        for (; this.indexPos < Math.min(old + 64, dataOre.getListPos().size()); indexPos++) {
            if (this.energy.getEnergy() < 1) {
                break;
            }

            this.energy.useEnergy(1);
            final BlockPos pos1 = dataOre.getListPos().get(indexPos);
            final IBlockState blockstate = this.getWorld().getBlockState(pos1);
            final ItemStack stack4 = new ItemStack(blockstate.getBlock(), 1, blockstate.getBlock().getMetaFromState(blockstate));
            if (stack4.isEmpty()) {
                if (dataOre.getListPos().size() == this.indexPos) {
                    this.indexOre++;
                    this.indexPos = 0;
                    if (this.dataOreList.size() == indexOre) {
                        quarry = false;
                        this.setActive(false);
                        this.analysis = true;
                        this.analyze();
                        return;
                    }
                }
                continue;
            }
            int[] ints = OreDictionary.getOreIDs(stack4);
            if (ints.length == 0) {
                if (dataOre.getListPos().size() == this.indexPos) {
                    this.indexOre++;
                    this.indexPos = 0;
                    if (this.dataOreList.size() == indexOre) {
                        quarry = false;
                        this.setActive(false);
                        this.analysis = true;
                        this.analyze();
                        return;
                    }
                }
                continue;
            }
            int id3 = OreDictionary.getOreIDs(stack4)[0];
            String name3 = OreDictionary.getOreName(id3);
            if (name3.equals(dataOre.getName())) {
                this.breakblock++;
                String name = dataOre.getName();

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


                    boolean need = false;

                    for (TileEntityAnalyzerChest analyzerChest : target1) {
                        List<ItemStack> drop = dataOre.getRecipe_stack(this, pos1);
                        for (ItemStack stack : drop) {
                            if (this.energy.getEnergy() >= consume &&
                                    analyzerChest.outputSlot.add(stack)) {
                                need = true;
                                this.energy.useEnergy(consume);
                            }
                        }
                        if (need) {
                            break;
                        }
                    }
                    if (need) {
                        world.setBlockToAir(pos1);

                    }
                }
            }


        }
        if (dataOre.getListPos().size() == this.indexPos) {
            if (this.indexVein == dataOre.getVeinsList().size()) {
                this.indexOre++;
                this.indexPos = 0;
                if (this.dataOreList.size() == indexOre) {
                    quarry = false;
                    this.setActive(false);
                    this.analysis = true;
                    this.analyze();
                }
            } else {
                if (dataOre.getVeinsList() != null && !dataOre.getVeinsList().isEmpty()) {
                    Vein vein = dataOre.getVeinsList().get(indexVein);
                    indexVein++;
                    if (vein.getCol() <= 0) {
                        for (TileEntityAnalyzerChest analyzerChest : target1) {
                            final int col1 = Math.min(vein.getCol(), 64);
                            if (vein.getCol() <= 0) {
                                break;
                            }

                            ItemStack stack;
                            if (vein.isOldMineral()) {
                                stack = new ItemStack(IUItem.heavyore, col1, vein.getMeta());
                            } else {
                                stack = new ItemStack(IUItem.mineral, col1, vein.getMeta());
                            }
                            while (analyzerChest.outputSlot.add(stack)) {
                                vein.removeCol(col1);
                                stack.setCount(Math.min(vein.getCol(), 64));
                                if (vein.getCol() <= 0) {
                                    break;
                                }
                            }
                            if (vein.getCol() <= 0) {
                                break;
                            }
                        }
                    }
                }
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

        nbttagcompound.setBoolean("analysis", this.analysis);
        nbttagcompound.setBoolean("quarry", this.quarry);
        nbttagcompound.setInteger("size_DataOre", dataOreList.size());
        final NBTTagCompound dataOreTag = new NBTTagCompound();

        for (int i = 0; i < dataOreList.size(); i++) {
            dataOreTag.setTag(String.valueOf(i), this.dataOreList.get(i).getTagCompound());
        }
        nbttagcompound.setTag("DataOre", dataOreTag);
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
