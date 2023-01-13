package com.denfop.tiles.base;

import com.denfop.IUCore;
import com.denfop.IUItem;
import com.denfop.api.gui.IType;
import com.denfop.api.vein.IVein;
import com.denfop.api.vein.Type;
import com.denfop.api.vein.VeinSystem;
import com.denfop.componets.EnumTypeStyle;
import com.denfop.container.ContainerQuarryVein;
import com.denfop.gui.GuiQuarryVein;
import com.denfop.items.ItemVeinSensor;
import com.denfop.items.upgradekit.ItemUpgradeMachinesKit;
import com.denfop.utils.ModUtils;
import ic2.api.network.INetworkClientTileEntityEventListener;
import ic2.api.network.INetworkDataProvider;
import ic2.api.network.INetworkUpdateListener;
import ic2.core.IC2;
import ic2.core.block.machine.BlockMiningPipe;
import ic2.core.init.Localization;
import ic2.core.network.NetworkManager;
import ic2.core.ref.BlockName;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.network.play.server.SPacketUpdateTileEntity;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.ChunkPos;
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.world.chunk.Chunk;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import org.lwjgl.input.Keyboard;

import java.util.ArrayList;
import java.util.List;

public class TileEntityQuarryVein extends TileEntityElectricMachine implements INetworkUpdateListener, INetworkDataProvider,
        INetworkClientTileEntityEventListener, IType, com.denfop.network.INetworkDataProvider {


    public int level;
    public int time;
    public int progress;
    public IVein vein;
    public boolean start = true;
    private int count;
    public TileEntityQuarryVein() {
        super(400, 14, 1);
        this.progress = 0;
        this.time = 0;
        this.level = 1;
        this.count = 0;
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
                    updateTileEntityField();
                    return true;
                } else if (this.level < 4 && player.getHeldItem(hand).getItemDamage() == 3) {
                    this.level = 4;
                    player.getHeldItem(hand).setCount(player.getHeldItem(hand).getCount() - 1);
                    updateTileEntityField();
                    return true;
                }
            }else if(player.getHeldItem(hand).getItem() instanceof ItemVeinSensor){
                if(this.vein != null && this.vein.get() && this.vein.getType() != Type.EMPTY){
                    final NBTTagCompound nbt = ModUtils.nbt(player.getHeldItem(hand));
                    if(this.vein.getType() == Type.VEIN) {
                        String s = getType(this.vein.getMeta());
                        nbt.setString("type",s);
                    }else{
                        String s = "oil";
                        nbt.setString("type",s);
                    }
                    nbt.setInteger("x",this.pos.getX());
                    nbt.setInteger("z",this.pos.getZ());
                    return true;
                }
            }
        }
        return super.onActivated(player, hand, side, hitX, hitY, hitZ);
    }

    private String getType(int meta) {
      String[] s = { "magnetite", "calaverite", "galena", "nickelite", "pyrite", "quartzite", "uranite", "azurite",
                "rhodonite", "alfildit", "euxenite"    , "smithsonite", "ilmenite", "todorokite", "ferroaugite", "sheelite"};

        return s[meta % s.length];
    }

    @Override
    public void onPlaced(final ItemStack stack, final EntityLivingBase placer, final EnumFacing facing) {
        super.onPlaced(stack, placer, facing);
        final NBTTagCompound nbt = ModUtils.nbt(stack);

        this.level = nbt.getInteger("level") != 0 ? nbt.getInteger("level") : 1;
        if (getWorld().provider.getDimension() != 0) {
            this.vein = null;
        } else {
            if (this.world.isRemote) {
                return;
            }
            final Chunk chunk = this.getWorld().getChunkFromBlockCoords(this.pos);
            final ChunkPos chunkpos = chunk.getPos();
            if(!VeinSystem.system.getChunkPos().contains(chunkpos)){
                VeinSystem.system.addVein(chunk);
            }
            this.vein = VeinSystem.system.getVein(chunkpos);
            if (this.vein.get()) {
                this.progress = 1200;
            }
            IUCore.network.get(true).updateTileEntityField(this, "vein");

        }
        if (!world.isRemote) {
            IUCore.network.get(true).sendInitialData(this);
        }

    }

    @Override
    public List<String> getNetworkFields() {
        final List<String> list = new ArrayList<>();
        list.add("level");
        if(this.vein != null)
        list.add("vein");
        return list;
    }
    public NBTTagCompound getUpdateTag() {
        super.getUpdateTag();
        IUCore.network.get(true).sendInitialData(this);
        return  new NBTTagCompound();
    }

    public SPacketUpdateTileEntity getUpdatePacket() {
        super.getUpdatePacket();
        IUCore.network.get(true).sendInitialData(this);
        return null;
    }

    @Override
    protected void onLoaded() {
        super.onLoaded();
        if (this.world.isRemote) {
            return;
        }
        if (getWorld().provider.getDimension() != 0) {
            this.vein = null;
        } else {
            final Chunk chunk = this.getWorld().getChunkFromBlockCoords(this.pos);
            final ChunkPos chunkpos = chunk.getPos();
            if(!VeinSystem.system.getChunkPos().contains(chunkpos)){
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
        if(!this.getWorld().isRemote)
            IUCore.network.get(true).sendInitialData(this);
    }

    private void updateTileEntityField() {
        IC2.network.get(true).updateTileEntityField(this, "level");
        IUCore.network.get(true).updateTileEntityField(this, "vein");

    }

    @Override
    @SideOnly(Side.CLIENT)
    public void addInformation(final ItemStack stack, final List<String> tooltip, final ITooltipFlag advanced) {
        if (!Keyboard.isKeyDown(Keyboard.KEY_LSHIFT)) {
            tooltip.add(Localization.translate("press.lshift"));
        }
        if (Keyboard.isKeyDown(Keyboard.KEY_LSHIFT)) {
            tooltip.add(Localization.translate("iu.machines_work_energy") + 5 + Localization.translate("iu" +
                    ".machines_work_energy_type_eu"));
        }
        super.addInformation(stack, tooltip, advanced);

    }

    public void updateEntityServer() {
        super.updateEntityServer();
        if (this.vein == null) {
            return;
        }
        if (this.vein.getCol() != this.count) {
            this.count = this.vein.getCol();
            if (this.getWorld().provider.getWorldTime() % 4 == 0) {
                IC2.network.get(true).updateTileEntityField(this, "count");
            }
        }
        if (this.vein.get()) {
            return;
        }
        if (this.progress < 1200 && this.energy.getEnergy() >= 5 && !this.vein.get()) {
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
            final ItemStack itemstack = BlockName.mining_pipe.getItemStack(BlockMiningPipe.MiningPipeType.pipe);
            this.energy.useEnergy(5);
            if (progress >= 1200) {
                initiate(2);
                this.progress = 1200;
                this.vein.setFind(true);
                updateTileEntityField();
            }


        } else {
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


    public void onNetworkEvent(EntityPlayer player, int event) {

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

}
