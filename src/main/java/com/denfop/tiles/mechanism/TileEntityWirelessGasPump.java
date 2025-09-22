package com.denfop.tiles.mechanism;

import com.denfop.IUItem;
import com.denfop.Localization;
import com.denfop.api.tile.IMultiTileBlock;
import com.denfop.api.vein.Type;
import com.denfop.api.vein.Vein;
import com.denfop.api.vein.VeinSystem;
import com.denfop.blocks.BlockTileEntity;
import com.denfop.blocks.FluidName;
import com.denfop.blocks.mechanism.BlockBaseMachine3;
import com.denfop.componets.Energy;
import com.denfop.componets.Fluids;
import com.denfop.container.ContainerWirelessGasPump;
import com.denfop.gui.GuiWirelessGasPump;
import com.denfop.invslot.Inventory;
import com.denfop.items.ItemVeinSensor;
import com.denfop.network.DecoderHandler;
import com.denfop.network.EncoderHandler;
import com.denfop.network.packet.CustomPacketBuffer;
import com.denfop.tiles.base.IManufacturerBlock;
import com.denfop.tiles.base.TileEntityInventory;
import com.denfop.utils.ModUtils;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.ChunkPos;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import org.lwjgl.input.Keyboard;

import java.io.IOException;
import java.util.LinkedList;
import java.util.List;

public class TileEntityWirelessGasPump extends TileEntityInventory implements IManufacturerBlock {

    public final Fluids fluids;
    public final Fluids.InternalFluidTank fluidTank;
    public final Energy energy;
    public final Inventory invslot;
    public List<Vein> veinList = new LinkedList<>();
    public int level;

    public TileEntityWirelessGasPump() {
        this.fluids = this.addComponent(new Fluids(this));
        this.fluidTank = fluids.addTank("tank", 256000, Fluids.fluidPredicate(FluidName.fluidgas.getInstance()
        ));
        this.energy = this.addComponent(Energy.asBasicSink(this, 50000, 14));
        this.invslot = new Inventory(this, Inventory.TypeItemSlot.INPUT, 4) {
            @Override
            public void put(final int index, final ItemStack content) {
                super.put(index, content);
                updateList();
            }

            @Override
            public boolean isItemValidForSlot(final int index, final ItemStack stack) {
                if (!(stack.getItem() instanceof ItemVeinSensor)) {
                    return false;
                }
                final NBTTagCompound nbt = ModUtils.nbt(stack);
                if (!nbt.getString("type").isEmpty()) {
                    return nbt.getString("type").equals("gas");
                }
                return false;
            }
        };
    }

    @Override
    public BlockTileEntity getBlock() {
        return IUItem.basemachine2;
    }

    @Override
    @SideOnly(Side.CLIENT)
    public GuiScreen getGui(final EntityPlayer var1, final boolean var2) {
        return new GuiWirelessGasPump(this.getGuiContainer(var1));
    }

    @Override
    public ContainerWirelessGasPump getGuiContainer(final EntityPlayer var1) {
        return new ContainerWirelessGasPump(this, var1);
    }

    @Override
    public IMultiTileBlock getTeBlock() {
        return BlockBaseMachine3.wireless_gas_pump;
    }

    @Override
    public void readFromNBT(final NBTTagCompound nbtTagCompound) {
        super.readFromNBT(nbtTagCompound);
        this.level = nbtTagCompound.getInteger("level");
    }

    @Override
    public NBTTagCompound writeToNBT(final NBTTagCompound nbt) {
        NBTTagCompound nbtTagCompound = super.writeToNBT(nbt);
        nbtTagCompound.setInteger("level", level);
        return nbtTagCompound;
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

        if (level < 10) {
            ItemStack stack = player.getHeldItem(hand);
            if (!stack.getItem().equals(IUItem.upgrade_speed_creation)) {
                return super.onActivated(player, hand, side, hitX, hitY, hitZ);
            } else {
                stack.shrink(1);
                this.level++;
                return true;
            }
        } else {
            return super.onActivated(player, hand, side, hitX, hitY, hitZ);
        }
    }

    @Override
    public void updateEntityServer() {
        super.updateEntityServer();
        boolean active = false;
        for (Vein vein : this.veinList) {
            if (this.energy.getEnergy() >= 10 && vein.isFind()) {
                if (vein.getCol() >= 1) {
                    int size = Math.min((this.level + 1) * 2, vein.getCol());
                    size = Math.min(size, this.fluidTank.getCapacity() - this.fluidTank.getFluidAmount());
                    if (this.fluidTank.getFluidAmount() + size <= this.fluidTank.getCapacity()) {
                        this.fluidTank.fill(new FluidStack(FluidName.fluidgas.getInstance(), size), true);
                        vein.removeCol(size);
                        active = true;
                        this.energy.useEnergy(10);
                    }
                }

            }
        }
        this.setActive(active);
    }

    public List<ItemStack> getWrenchDrops(EntityPlayer player, int fortune) {
        List<ItemStack> ret = super.getWrenchDrops(player, fortune);
        if (this.level != 0) {
            ret.add(new ItemStack(IUItem.upgrade_speed_creation, this.level));
            this.level = 0;
        }
        return ret;
    }


    @Override
    public void addInformation(final ItemStack stack, final List<String> tooltip) {
        if (!Keyboard.isKeyDown(Keyboard.KEY_LSHIFT)) {
            tooltip.add(Localization.translate("press.lshift"));
        }
        if (Keyboard.isKeyDown(Keyboard.KEY_LSHIFT)) {
            tooltip.add(Localization.translate("iu.machines_work_energy") + 10 + Localization.translate("iu" +
                    ".machines_work_energy_type_eu"));
        }
        if (stack.hasTagCompound() && stack.getTagCompound().hasKey("fluid")) {
            FluidStack fluidStack = FluidStack.loadFluidStackFromNBT((NBTTagCompound) stack.getTagCompound().getTag("fluid"));

            tooltip.add(Localization.translate("iu.fluid.info") + fluidStack.getLocalizedName());
            tooltip.add(Localization.translate("iu.fluid.info1") + fluidStack.amount / 1000 + " B");

        }
        super.addInformation(stack, tooltip);
    }

    @Override
    public void readContainerPacket(final CustomPacketBuffer customPacketBuffer) {
        super.readContainerPacket(customPacketBuffer);
        this.veinList.clear();
        int col = customPacketBuffer.readInt();
        for (int i = 0; i < col; i++) {
            try {
                this.veinList.add((Vein) DecoderHandler.decode(customPacketBuffer));
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }
    }

    @Override
    public CustomPacketBuffer writeContainerPacket() {
        CustomPacketBuffer customPacketBuffer = super.writeContainerPacket();
        customPacketBuffer.writeInt(this.veinList.size());
        for (Vein vein : this.veinList) {
            try {
                EncoderHandler.encode(customPacketBuffer, vein);
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }
        return customPacketBuffer;
    }

    @Override
    public void onLoaded() {
        super.onLoaded();
        if (!this.getWorld().isRemote) {
            updateList();
        }
    }

    public void updateList() {
        veinList.clear();
        for (ItemStack stack : this.invslot.getContents()) {
            if (stack.isEmpty()) {
                continue;
            }
            final NBTTagCompound nbt = ModUtils.nbt(stack);
            int x = nbt.getInteger("x");
            int z = nbt.getInteger("z");
            ChunkPos chunkPos = new ChunkPos(x >> 4, z >> 4);
            final Vein vein = VeinSystem.system.getVein(chunkPos);
            if (vein.isFind() && vein.getType() == Type.GAS) {
                veinList.add(vein);
            }
        }
    }

    @Override
    public int getLevel() {
        return this.level;
    }

    @Override
    public void setLevel(final int level) {
        this.level = level;
    }

    @Override
    public void removeLevel(final int level) {
        this.level -= level;
    }

}
