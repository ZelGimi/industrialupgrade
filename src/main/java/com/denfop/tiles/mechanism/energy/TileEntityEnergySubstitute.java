package com.denfop.tiles.mechanism.energy;

import com.denfop.api.energy.EnergyNetGlobal;
import com.denfop.api.energy.EnergyNetLocal;
import com.denfop.api.energy.IAdvConductor;
import com.denfop.api.energy.IEnergyController;
import com.denfop.api.energy.event.EventLoadController;
import com.denfop.api.energy.event.EventUnloadController;
import com.denfop.api.inv.IHasGui;
import com.denfop.container.ContainerSubstitute;
import com.denfop.gui.GuiEnergySubstitute;
import com.denfop.invslot.CableItem;
import com.denfop.invslot.InvSlotSubstitute;
import com.denfop.tiles.base.FakePlayerSpawner;
import com.denfop.tiles.base.TileEntityInventory;
import com.denfop.tiles.transport.types.CableType;
import com.denfop.utils.ModUtils;
import ic2.api.network.INetworkClientTileEntityEventListener;
import ic2.core.block.TileEntityBlock;
import ic2.core.init.Localization;
import ic2.core.item.block.ItemBlockTileEntity;
import ic2.core.util.StackUtil;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.BlockPos;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class TileEntityEnergySubstitute extends TileEntityInventory implements IHasGui,
        INetworkClientTileEntityEventListener, IEnergyController {

    public final InvSlotSubstitute slot;
    public List<EnergyNetLocal.EnergyPath> energyPathList = new ArrayList<>();
    public Set<IAdvConductor> conductorList = new HashSet<>();
    public boolean work = false;
    public int size;
    public int max_value = 0;
    List<CableItem> cableItemList = new ArrayList<>();
    CableItem main_cableItem = null;
    FakePlayerSpawner fakePlayer;

    public TileEntityEnergySubstitute() {
        slot = new InvSlotSubstitute(this);
    }

    private static CableType getCableType(ItemStack stack) {
        NBTTagCompound nbt = StackUtil.getOrCreateNbtData(stack);
        int type = nbt.getByte("type") & 255;
        return type < CableType.values.length ? CableType.values[type] : CableType.glass;
    }

    private static int getInsulation(ItemStack stack) {
        NBTTagCompound nbt = StackUtil.getOrCreateNbtData(stack);
        return nbt.getByte("insulation") & 255;
    }

    @Override
    public TileEntity getTileEntity() {
        return this;
    }

    @SideOnly(Side.CLIENT)
    public void addInformation(ItemStack stack, List<String> tooltip, ITooltipFlag advanced) {
        tooltip.add(Localization.translate("iu.controller_cables.info"));
        tooltip.add(Localization.translate("iu.controller_cables.info1"));
    }

    public List<CableItem> getCableItemList() {
        return cableItemList;
    }

    public void setCableItemList(final List<CableItem> cableItemList) {
        this.cableItemList = cableItemList;
    }

    @Override
    protected void onLoaded() {
        super.onLoaded();
        if (!this.getWorld().isRemote) {
            MinecraftForge.EVENT_BUS.post(new EventLoadController(this));
            fakePlayer = new FakePlayerSpawner(this.getWorld());
            this.slot.onChanged();
        }

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
    protected void onUnloaded() {
        if (!this.getWorld().isRemote) {
            MinecraftForge.EVENT_BUS.post(new EventUnloadController(this));
        }
        super.onUnloaded();
    }

    @Override
    public ContainerSubstitute getGuiContainer(final EntityPlayer entityPlayer) {
        return new ContainerSubstitute(this, entityPlayer);
    }

    @Override
    @SideOnly(Side.CLIENT)
    public GuiScreen getGui(final EntityPlayer entityPlayer, final boolean b) {
        return new GuiEnergySubstitute(getGuiContainer(entityPlayer));
    }

    @Override
    public void onGuiClosed(final EntityPlayer entityPlayer) {

    }

    @Override
    public NBTTagCompound writeToNBT(final NBTTagCompound nbt) {
        nbt.setBoolean("work", work);
        return super.writeToNBT(nbt);
    }

    @Override
    public void readFromNBT(final NBTTagCompound nbtTagCompound) {
        super.readFromNBT(nbtTagCompound);
        this.work = nbtTagCompound.getBoolean("work");

    }

    @Override
    protected void updateEntityServer() {
        super.updateEntityServer();
        if (this.work) {
            if (main_cableItem != null) {
                List<ItemStack> itemStackList = new ArrayList<>();
                for (IAdvConductor conductor : this.conductorList) {
                    if (conductor.getConductorBreakdownEnergy() - 1 < this.max_value) {

                        for (ItemStack stack : this.slot) {

                            if (stack.isItemEqual(main_cableItem.getStack()) && (ModUtils.nbt(main_cableItem.getStack()).equals(
                                    ModUtils.nbt(stack)))) {
                                if (main_cableItem.getStack().getItem() instanceof com.denfop.items.transport.ItemCable) {
                                    final com.denfop.tiles.transport.tiles.TileEntityCable te =
                                            com.denfop.tiles.transport.tiles.TileEntityCable.delegate(com.denfop.items.transport.ItemCable.getCableType(
                                                    main_cableItem.getStack()
                                            ), getInsulation(main_cableItem.getStack()));
                                    TileEntityBlock tile = (TileEntityBlock) EnergyNetGlobal.instance.getBlockPosFromEnergyTile(
                                            conductor);
                                    EnumFacing facing = tile.getFacing().getOpposite();
                                    final List<ItemStack> drops = tile.getBlockType().getDrops(
                                            world,
                                            tile.getPos(),
                                            tile.getBlockState(),
                                            100
                                    );
                                    if (!drops.isEmpty()) {
                                        itemStackList.add(drops.get(0));
                                    }
                                    conductor.removeConductor();
                                    BlockPos pos = tile.getPos();

                                    if (ItemBlockTileEntity.placeTeBlock(main_cableItem.getStack(),
                                            fakePlayer,
                                            this.getWorld(),
                                            pos,
                                            facing
                                            ,
                                            te
                                    )) {
                                        stack.shrink(1);
                                        main_cableItem.shrink(1);
                                        EnergyNetGlobal.getForWorld(this.getWorld()).update(pos);
                                        com.denfop.tiles.transport.tiles.TileEntityCable cable =
                                                (com.denfop.tiles.transport.tiles.TileEntityCable) this.getWorld().getTileEntity(
                                                        pos);
                                        if (cable != null) {
                                            cable.update_render();
                                        }
                                        break;
                                    }
                                }
                            }
                        }
                        if (main_cableItem.getCount() == 0) {
                            this.slot.onChanged();
                            if (main_cableItem == null) {
                                break;
                            }
                        }
                    }

                }
                for (ItemStack stack : itemStackList) {
                    this.slot.add(stack);
                }
            }
            energyPathList.clear();
            conductorList.clear();
            this.size = 0;
            this.work = false;
            for (EnumFacing facing : EnumFacing.values()) {
                final List<EnergyNetLocal.EnergyPath> energyPathList1 = EnergyNetGlobal.instance.getEnergyPaths(
                        this.getWorld(),
                        this.getBlockPos().offset(facing)
                );

                for (EnergyNetLocal.EnergyPath path : energyPathList1) {
                    if (!energyPathList.contains(path)) {
                        energyPathList.add(path);
                    }
                }
            }
            energyPathList.forEach(energyPath -> this.conductorList.addAll(energyPath.getConductors()));
            this.size = conductorList.size();
            this.main_cableItem = null;
            this.max_value = 0;
            this.slot.onChanged();
        }
    }

    @Override
    public void onNetworkEvent(final EntityPlayer entityPlayer, final int i) {
        if (this.world.isRemote) {
            return;
        }
        if (i == 0) {
            energyPathList.clear();
            conductorList.clear();
            for (EnumFacing facing : EnumFacing.values()) {
                final List<EnergyNetLocal.EnergyPath> energyPathList1 = EnergyNetGlobal.instance.getEnergyPaths(
                        this.getWorld(),
                        this.getBlockPos().offset(facing)
                );

                for (EnergyNetLocal.EnergyPath path : energyPathList1) {
                    if (!energyPathList.contains(path)) {
                        energyPathList.add(path);
                    }
                }
            }
            energyPathList.forEach(energyPath -> this.conductorList.addAll(energyPath.getConductors()));
            this.size = conductorList.size();
            this.main_cableItem = null;
            this.max_value = 0;
            this.slot.onChanged();
        } else if (i == 1) {
            this.work = true;


        }
    }

    @Override
    public boolean getWork() {
        return this.work;
    }

    @Override
    public void work() {
        if (this.getWork()) {
            energyPathList.clear();
            conductorList.clear();
            for (EnumFacing facing : EnumFacing.values()) {
                final List<EnergyNetLocal.EnergyPath> energyPathList1 = EnergyNetGlobal.instance.getEnergyPaths(
                        this.getWorld(),
                        this.getBlockPos().offset(facing)
                );

                for (EnergyNetLocal.EnergyPath path : energyPathList1) {
                    if (!energyPathList.contains(path)) {
                        energyPathList.add(path);
                    }
                }
            }
            energyPathList.forEach(energyPath -> this.conductorList.addAll(energyPath.getConductors()));
            this.size = conductorList.size();

        }
    }

    @Override
    public void unload() {
        this.energyPathList.clear();
        this.conductorList.clear();
    }


    public void setMaxValue(int max_value, double provider, final CableItem cableItem) {
        if (provider > max_value) {
            this.max_value = (int) provider;
            this.main_cableItem = cableItem;
        }
    }

}
