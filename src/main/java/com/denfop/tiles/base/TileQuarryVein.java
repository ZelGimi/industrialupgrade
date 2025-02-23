package com.denfop.tiles.base;

import com.denfop.IUItem;
import com.denfop.Localization;
import com.denfop.api.gui.IType;
import com.denfop.api.tile.IMultiTileBlock;
import com.denfop.api.vein.IVein;
import com.denfop.api.vein.Type;
import com.denfop.api.vein.VeinSystem;
import com.denfop.audio.EnumSound;
import com.denfop.blocks.BlockTileEntity;
import com.denfop.blocks.mechanism.BlockQuarryVein;
import com.denfop.componets.EnumTypeStyle;
import com.denfop.container.ContainerQuarryVein;
import com.denfop.gui.GuiQuarryVein;
import com.denfop.items.ItemVeinSensor;
import com.denfop.items.upgradekit.ItemUpgradeMachinesKit;
import com.denfop.network.DecoderHandler;
import com.denfop.network.EncoderHandler;
import com.denfop.network.IUpdatableTileEvent;
import com.denfop.network.packet.CustomPacketBuffer;
import com.denfop.network.packet.PacketUpdateFieldTile;
import com.denfop.render.oilquarry.DataBlock;
import com.denfop.utils.ModUtils;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.SoundEvent;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.ChunkPos;
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.world.chunk.Chunk;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import org.lwjgl.input.Keyboard;

import java.io.IOException;
import java.util.List;

public class TileQuarryVein extends TileElectricMachine implements
        IUpdatableTileEvent, IType {

    @SideOnly(Side.CLIENT)
    public DataBlock dataBlock;
    public int level;
    public int time;
    public int progress;
    public IVein vein;
    public boolean start = true;
    public int col;
    private int count;
    private boolean work;

    public TileQuarryVein() {
        super(400, 14, 1);
        this.progress = 0;
        this.time = 0;
        this.level = 1;
        this.count = 0;
    }

    public IMultiTileBlock getTeBlock() {
        return BlockQuarryVein.quarry_vein;
    }

    public BlockTileEntity getBlock() {
        return IUItem.oilquarry;
    }

    @Override
    public ItemStack getPickBlock(final EntityPlayer player, final RayTraceResult target) {
        ItemStack stack = new ItemStack(IUItem.oilquarry);
        final NBTTagCompound nbt = ModUtils.nbt(stack);
        nbt.setString("state", "active_" + this.level);
        nbt.setInteger("level", this.level);
        return stack;
    }

    @Override
    @SideOnly(Side.CLIENT)
    public void updateEntityClient() {
        super.updateEntityClient();
        if (this.vein == null || !this.vein.get()) {
            if (this.getWorld().provider.getWorldTime() % 6 == 0) {
                if (this.work) {
                    final BlockPos pos1 = pos.down();
                    IBlockState state = world.getBlockState(pos1);
                    if (state.getMaterial() == Material.AIR) {
                        state = Blocks.STONE.getDefaultState();
                    }
                    Minecraft.getMinecraft().effectRenderer.addBlockDestroyEffects(pos, state);
                }
            }
        }
    }

    @SideOnly(Side.CLIENT)
    public boolean shouldSideBeRendered(EnumFacing side, BlockPos otherPos) {
        return false;
    }

    public boolean isNormalCube() {
        return false;
    }

    public boolean doesSideBlockRendering(EnumFacing side) {
        return false;
    }

    public String getStartSoundFile() {
        return "Machines/rig.ogg";
    }

    public boolean isSideSolid(EnumFacing side) {
        return false;
    }

    public boolean clientNeedsExtraModelInfo() {
        return true;
    }

    public boolean shouldRenderInPass(int pass) {
        return true;
    }

    @Override
    public boolean onActivated(
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
                    updateTileEntityField();
                    this.setActive("active_" + level);
                    return true;
                } else if (this.level < 4 && player.getHeldItem(hand).getItemDamage() == 3) {
                    this.level = 4;
                    this.setActive("active_" + level);
                    player.getHeldItem(hand).setCount(player.getHeldItem(hand).getCount() - 1);
                    updateTileEntityField();
                    return true;
                }
            } else if (player.getHeldItem(hand).getItem() instanceof ItemVeinSensor) {
                if (this.vein != VeinSystem.system.getEMPTY() && this.vein.get() && this.vein.getType() != Type.EMPTY) {
                    final NBTTagCompound nbt = ModUtils.nbt(player.getHeldItem(hand));
                    if (this.vein.getType() == Type.VEIN) {
                        String s = getType(this.vein.getMeta(), this.vein.isOldMineral());
                        nbt.setString("type", s);
                    } else {
                        if (this.vein.getType() == Type.OIL) {
                            String s = "oil";
                            nbt.setString("type", s);
                        } else {
                            String s = "gas";
                            nbt.setString("type", s);
                        }
                    }
                    nbt.setInteger("x", this.pos.getX());
                    nbt.setInteger("z", this.pos.getZ());
                    return true;
                }
            }
        }
        return super.onActivated(player, hand, side, hitX, hitY, hitZ);
    }

    private String getType(int meta, boolean oldMineral) {
        if (oldMineral) {
            String[] s = {"magnetite", "calaverite", "galena", "nickelite", "pyrite", "quartzite", "uranite", "azurite",
                    "rhodonite", "alfildit", "euxenite", "smithsonite", "ilmenite", "todorokite", "ferroaugite", "sheelite"};
            return s[meta % s.length];
        } else {
            String[] s = {"arsenopyrite", "braggite", "wolframite", "germanite", "coltan", "crocoite", "xenotime", "iridosmine",
                    "theoprastite", "tetrahedrite", "fergusonite", "celestine", "zircon", "crystal"};
            return s[meta % s.length];
        }
    }


    @Override
    public void onPlaced(final ItemStack stack, final EntityLivingBase placer, final EnumFacing facing) {
        super.onPlaced(stack, placer, facing);
        final NBTTagCompound nbt = ModUtils.nbt(stack);

        this.level = nbt.getInteger("level") != 0 ? nbt.getInteger("level") : 1;
        if (level != 1) {
            this.setActive("active_" + level);
        }
        if (getWorld().provider.getDimension() != 0) {
            this.vein = VeinSystem.system.getEMPTY();
        } else {
            if (this.world.isRemote) {
                return;
            }
            final Chunk chunk = this.getWorld().getChunkFromBlockCoords(this.pos);
            final ChunkPos chunkpos = chunk.getPos();
            if (!VeinSystem.system.getChunkPos().contains(chunkpos)) {
                VeinSystem.system.addVein(chunk);
            }
            this.vein = VeinSystem.system.getVein(chunkpos);
            if (this.vein.get()) {
                this.progress = 1200;
            }
            new PacketUpdateFieldTile(this, "vein", this.vein);

        }

    }

    public CustomPacketBuffer writePacket() {
        final CustomPacketBuffer packet = super.writePacket();
        try {
            EncoderHandler.encode(packet, level);
            EncoderHandler.encode(packet, vein);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        return packet;
    }

    public void readPacket(CustomPacketBuffer customPacketBuffer) {
        super.readPacket(customPacketBuffer);
        try {
            level = (int) DecoderHandler.decode(customPacketBuffer);
            vein = (IVein) DecoderHandler.decode(customPacketBuffer);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void readContainerPacket(final CustomPacketBuffer customPacketBuffer) {
        super.readContainerPacket(customPacketBuffer);
        try {
            progress = (int) DecoderHandler.decode(customPacketBuffer);
            level = (int) DecoderHandler.decode(customPacketBuffer);
            vein = (IVein) DecoderHandler.decode(customPacketBuffer);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

    }

    @Override
    public CustomPacketBuffer writeContainerPacket() {
        final CustomPacketBuffer packet = super.writeContainerPacket();
        try {
            EncoderHandler.encode(packet, progress);
            EncoderHandler.encode(packet, level);
            EncoderHandler.encode(packet, vein);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        return packet;
    }

    @Override
    public void onLoaded() {
        super.onLoaded();
        if (this.world.isRemote) {
            return;
        }
        if (getWorld().provider.getDimension() != 0) {
            this.vein = VeinSystem.system.getEMPTY();
        } else {
            final Chunk chunk = this.getWorld().getChunkFromBlockCoords(this.pos);
            final ChunkPos chunkpos = chunk.getPos();
            if (!VeinSystem.system.getChunkPos().contains(chunkpos)) {
                VeinSystem.system.addVein(chunk);
            }
            this.vein = VeinSystem.system.getVein(chunkpos);


            if (this.vein.get()) {
                this.progress = 1200;
            }
            if (this.progress >= 1200) {
                this.vein.setFind(true);
            }
        }
        updateTileEntityField();
    }

    private void updateTileEntityField() {
        new PacketUpdateFieldTile(this, "level", this.level);
        new PacketUpdateFieldTile(this, "vein", this.vein);

    }

    @Override
    public void addInformation(final ItemStack stack, final List<String> tooltip) {
        if (!Keyboard.isKeyDown(Keyboard.KEY_LSHIFT)) {
            tooltip.add(Localization.translate("press.lshift"));
        }
        if (Keyboard.isKeyDown(Keyboard.KEY_LSHIFT)) {
            tooltip.add(Localization.translate("iu.machines_work_energy") + 5 + Localization.translate("iu" +
                    ".machines_work_energy_type_eu"));
        }
        super.addInformation(stack, tooltip);

    }

    public void updateField(String name, CustomPacketBuffer is) {

        if (name.equals("count")) {
            try {
                this.count = (int) DecoderHandler.decode(is);
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }
        if (name.equals("progress")) {
            try {
                this.progress = (int) DecoderHandler.decode(is);
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }
        if (name.equals("work")) {
            try {
                this.work = (boolean) DecoderHandler.decode(is);
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }
        if (name.equals("vein")) {
            try {
                this.vein = (IVein) DecoderHandler.decode(is);
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }
        if (name.equals("level")) {
            try {
                this.level = (int) DecoderHandler.decode(is);
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }
        super.updateField(name, is);
    }

    public void updateEntityServer() {
        super.updateEntityServer();
        if (this.vein == null) {
            return;
        }
        if (this.vein.getCol() != this.count) {
            this.count = this.vein.getCol();
            if (this.getWorld().provider.getWorldTime() % 4 == 0) {
                new PacketUpdateFieldTile(this, "count", this.count);
            }
        }
        if (this.vein.get()) {
            if (this.getWorld().provider.getWorldTime() % 20 == 0) {
                if (this.col != vein.getCol()) {
                    new PacketUpdateFieldTile(this, "vein", this.vein);
                    col = vein.getCol();
                }
            }

            return;
        }

        if (this.progress < 1200 && this.energy.getEnergy() >= 5 && !this.vein.get()) {
            if (!work) {
                work = true;
                new PacketUpdateFieldTile(this, "work", this.work);
            }
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
            progress += (int) Math.pow(2, this.level - 1);
            this.energy.useEnergy(5);
            if (progress >= 1200) {
                initiate(2);
                this.progress = 1200;
                this.vein.setFind(true);
                updateTileEntityField();
            }


        } else {
            if (work) {
                work = false;
                new PacketUpdateFieldTile(this, "work", this.work);
            }
            if (this.time > 0) {
                initiate(2);
                this.time = 0;
            }
        }

    }


    public void readFromNBT(NBTTagCompound nbttagcompound) {
        super.readFromNBT(nbttagcompound);
        this.progress = nbttagcompound.getInteger("progress");
        this.level = nbttagcompound.getInteger("level");

    }

    public NBTTagCompound writeToNBT(NBTTagCompound nbttagcompound) {
        super.writeToNBT(nbttagcompound);
        nbttagcompound.setInteger("progress", this.progress);
        nbttagcompound.setInteger("level", this.level);
        return nbttagcompound;
    }


    @SideOnly(Side.CLIENT)
    public GuiScreen getGui(EntityPlayer entityPlayer, boolean isAdmin) {
        return new GuiQuarryVein(getGuiContainer(entityPlayer));
    }

    public ContainerQuarryVein getGuiContainer(EntityPlayer entityPlayer) {
        return new ContainerQuarryVein(entityPlayer, this);
    }


    public void updateTileServer(EntityPlayer player, double event) {

    }


    public void onGuiClosed(EntityPlayer arg0) {
    }

    @Override
    public EnumTypeStyle getStyle() {
        switch (this.level) {
            case 2:
                return EnumTypeStyle.ADVANCED;
            case 3:
                return EnumTypeStyle.IMPROVED;
            case 4:
                return EnumTypeStyle.PERFECT;
        }
        return EnumTypeStyle.DEFAULT;
    }

    @Override
    public SoundEvent getSound() {
        return EnumSound.quarry.getSoundEvent();
    }

}
