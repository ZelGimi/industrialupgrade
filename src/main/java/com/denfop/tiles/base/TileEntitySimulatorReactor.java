package com.denfop.tiles.base;

import com.denfop.IUItem;
import com.denfop.api.reactors.CreativeReactor;
import com.denfop.api.reactors.EnumReactors;
import com.denfop.api.reactors.EnumTypeComponent;
import com.denfop.api.reactors.EnumTypeSecurity;
import com.denfop.api.reactors.IReactorItem;
import com.denfop.api.reactors.ITypeRector;
import com.denfop.api.reactors.LogicCreativeFluidReactor;
import com.denfop.api.reactors.LogicCreativeGasReactor;
import com.denfop.api.reactors.LogicCreativeGraphiteReactor;
import com.denfop.api.reactors.LogicCreativeHeatReactor;
import com.denfop.api.reactors.LogicCreativeReactor;
import com.denfop.api.tile.IMultiTileBlock;
import com.denfop.blocks.BlockTileEntity;
import com.denfop.blocks.mechanism.BlockBaseMachine3;
import com.denfop.container.ContainerSimulationReactors;
import com.denfop.gui.GuiSimulationReactors;
import com.denfop.invslot.InvSlot;
import com.denfop.invslot.InvSlotSimulatorReactor;
import com.denfop.items.reactors.ItemComponentVent;
import com.denfop.items.resource.ItemCraftingElements;
import com.denfop.network.IUpdatableTileEvent;
import com.denfop.network.packet.CustomPacketBuffer;
import com.denfop.utils.ModUtils;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.util.EnumFacing;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import org.jetbrains.annotations.NotNull;

import java.io.IOException;
import java.util.Iterator;
import java.util.List;

public class TileEntitySimulatorReactor extends TileEntityInventory implements IUpdatableTileEvent {

    public final InvSlotSimulatorReactor invSlot;
    public final InvSlot scheduleSlot;
    public int type = -1;
    public int level = -1;
    public EnumReactors reactors;
    public CreativeReactor reactor;
    public boolean work;
    public boolean explode;
    public LogicCreativeReactor logicReactor;
    public double output;
    public double rad;
    public double heat;
    public EnumTypeSecurity security;
    private boolean needUpdate = false;

    public TileEntitySimulatorReactor() {

        this.invSlot = new InvSlotSimulatorReactor(this, InvSlot.TypeItemSlot.INPUT, 80) {
            @Override
            public boolean accepts(final ItemStack stack, final int index) {
                if (reactors == null) {
                    return false;
                }
                switch (reactors.getType()) {
                    case FLUID:
                    case HIGH_SOLID:
                        if (stack.getItem() instanceof IReactorItem) {
                            IReactorItem iReactorItem = (IReactorItem) stack.getItem();
                            return level >= iReactorItem.getLevel();
                        } else {
                            return false;
                        }
                    case GAS_COOLING_FAST:
                        if (stack.getItem() instanceof IReactorItem) {
                            IReactorItem iReactorItem = (IReactorItem) stack.getItem();
                            return level >= iReactorItem.getLevel() && (iReactorItem.getType() != EnumTypeComponent.HEAT_SINK || (stack.getItem() instanceof ItemComponentVent));
                        } else {
                            return false;
                        }
                    case GRAPHITE_FLUID:
                        if (stack.getItem() instanceof IReactorItem) {
                            IReactorItem iReactorItem = (IReactorItem) stack.getItem();
                            return level >= iReactorItem.getLevel() && (iReactorItem.getType() != EnumTypeComponent.HEAT_EXCHANGER);
                        } else {
                            return false;
                        }
                }
                return false;
            }

            @Override
            public void put(final int index, final ItemStack content) {
                super.put(index, content);
                needUpdate = true;
                switch (reactors.getType()) {
                    case FLUID:
                        logicReactor = new LogicCreativeFluidReactor(reactor);
                        break;
                    case GAS_COOLING_FAST:
                        logicReactor = new LogicCreativeGasReactor(reactor);
                        break;
                    case GRAPHITE_FLUID:
                        logicReactor = new LogicCreativeGraphiteReactor(reactor);
                        break;
                    case HIGH_SOLID:
                        logicReactor = new LogicCreativeHeatReactor(reactor);
                        break;
                }
            }
        };
        this.reactor = new CreativeReactor(this.reactors, invSlot);
        this.scheduleSlot = new InvSlot(this, InvSlot.TypeItemSlot.INPUT, 1) {
            @Override
            public boolean accepts(final ItemStack stack, final int index) {
                return stack.getItem() instanceof ItemCraftingElements && stack.getItemDamage() == 143;
            }

            @Override
            public int getStackSizeLimit() {
                return 1;
            }
        };
    }

    @Override
    public boolean hasCapability(@NotNull final Capability<?> capability, final EnumFacing facing) {

        return super.hasCapability(capability, facing);
    }

    @Override
    public void readContainerPacket(final CustomPacketBuffer customPacketBuffer) {
        super.readContainerPacket(customPacketBuffer);
        this.work = customPacketBuffer.readBoolean();
        this.explode = customPacketBuffer.readBoolean();
        this.rad = customPacketBuffer.readDouble();
        this.output = customPacketBuffer.readDouble();
        this.heat = customPacketBuffer.readDouble();
        this.security = EnumTypeSecurity.values()[customPacketBuffer.readInt()];
        this.type = customPacketBuffer.readInt();
        this.level = customPacketBuffer.readInt();
        try {
            this.reactor.timer.readBuffer(customPacketBuffer);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void readPacket(final CustomPacketBuffer customPacketBuffer) {
        super.readPacket(customPacketBuffer);
        type = customPacketBuffer.readInt();
        level = customPacketBuffer.readInt();
        if (level != -1 && type != -1) {
            this.reactors = EnumReactors.values()[(type - 1) * 4 + (level - 1)];
            this.reactor.level = this.level;
            this.invSlot.clear();
            this.reactor.reset(reactors);
            this.size_inventory = 0;
            InvSlot invSlot;
            for (Iterator<InvSlot> var3 = this.invSlots.iterator(); var3.hasNext(); size_inventory += invSlot.size()) {
                invSlot = var3.next();
            }
        }
    }

    @Override
    public CustomPacketBuffer writePacket() {
        CustomPacketBuffer customPacketBuffer = super.writePacket();
        customPacketBuffer.writeInt(this.type);
        customPacketBuffer.writeInt(this.level);
        return customPacketBuffer;
    }

    @Override
    public BlockTileEntity getBlock() {
        return IUItem.basemachine2;
    }

    @Override
    public IMultiTileBlock getTeBlock() {
        return BlockBaseMachine3.simulation_reactors;
    }

    @Override
    public ContainerSimulationReactors getGuiContainer(final EntityPlayer var1) {
        return new ContainerSimulationReactors(this, var1);
    }

    @Override
    @SideOnly(Side.CLIENT)
    public GuiScreen getGui(final EntityPlayer var1, final boolean var2) {
        return new GuiSimulationReactors(getGuiContainer(var1));
    }

    @Override
    public NBTTagCompound writeToNBT(final NBTTagCompound nbt) {
        NBTTagCompound nbtTagCompound = super.writeToNBT(nbt);
        nbtTagCompound.setInteger("type", type);
        nbtTagCompound.setInteger("level", level);
        nbtTagCompound.setBoolean("work", work);
        nbtTagCompound.setBoolean("explode", explode);
        if (reactors != null) {
            nbtTagCompound.setInteger("reactors", reactors.ordinal());
        }

        return nbtTagCompound;
    }

    @Override
    public void readFromNBT(final NBTTagCompound nbtTagCompound) {
        super.readFromNBT(nbtTagCompound);
        type = nbtTagCompound.getInteger("type");
        level = nbtTagCompound.getInteger("level");

        reactors = EnumReactors.values()[nbtTagCompound.getInteger("reactors")];
        work = nbtTagCompound.getBoolean("work");
        explode = nbtTagCompound.getBoolean("explode");
    }

    @Override
    public CustomPacketBuffer writeContainerPacket() {
        CustomPacketBuffer customPacketBuffer = super.writeContainerPacket();
        customPacketBuffer.writeBoolean(this.work);
        customPacketBuffer.writeBoolean(this.explode);
        customPacketBuffer.writeDouble(this.rad);
        customPacketBuffer.writeDouble(this.output);
        customPacketBuffer.writeDouble(this.heat);
        customPacketBuffer.writeInt(this.security.ordinal());
        customPacketBuffer.writeInt(this.type);
        customPacketBuffer.writeInt(this.level);
        this.reactor.timer.writeBuffer(customPacketBuffer);
        return customPacketBuffer;
    }

    @Override
    public void updateField(final String name, final CustomPacketBuffer is) {
        super.updateField(name, is);

    }

    @Override
    public void loadBeforeFirstUpdate() {
        super.loadBeforeFirstUpdate();
    }

    @Override
    public void updateEntityServer() {
        super.updateEntityServer();
        if (this.needUpdate && this.world.provider.getWorldTime() % 20 == 0 && this.reactors != null) {
            if (!this.scheduleSlot.isEmpty()) {
                needUpdate = false;
                ItemStack stack = this.scheduleSlot.get();
                final NBTTagCompound nbt = ModUtils.nbt(stack);

                final List<ItemStack> infoStack = this.logicReactor.getInfoStack();
                final NBTTagList stacks = new NBTTagList();
                for (ItemStack stack1 : infoStack) {
                    NBTTagCompound contentTag = new NBTTagCompound();
                    stack1.writeToNBT(contentTag);
                    stacks.appendTag(contentTag);
                }
                nbt.setInteger("type", type);
                nbt.setInteger("level", level);
                nbt.setString("name", this.reactors.getNameReactor().toLowerCase());
                nbt.setInteger(
                        "generation",
                        (int) (this.logicReactor.getGeneration() * (this.reactors.getType() == ITypeRector.GAS_COOLING_FAST
                                ? 1.175
                                :
                                        1))
                );
                nbt.setInteger(
                        "rad",
                        this.logicReactor.getRadGeneration()
                );
                nbt.setTag("Items", stacks);
                for (int y = 0; y < this.reactors.getHeight(); y++) {
                    for (int x = 0; x < this.reactors.getWidth(); x++) {
                        NBTTagCompound tag = new NBTTagCompound();
                        ItemStack stack1 = this.reactor.getItemAt(x, y);
                        if (stack1.isEmpty()) {
                            tag.setBoolean("empty", true);
                            nbt.setTag(String.valueOf(y * this.reactors.getWidth() + x), tag);
                        } else {
                            tag.setBoolean("empty", false);
                            for (int i = 0; i < infoStack.size(); i++) {
                                if (infoStack.get(i).isItemEqual(stack1)) {
                                    tag.setInteger("index", i);

                                    break;
                                }
                            }
                            nbt.setTag(String.valueOf(y * this.reactors.getWidth() + x), tag);
                        }
                    }
                }

            }
        }
        if (work && !explode && this.reactors != null) {
            this.logicReactor.onTick();
            this.reactor.tick(this.logicReactor.getMaxHeat());
            this.explode = this.reactor.explode;
            this.output = this.reactor.getOutput();
            this.rad = this.reactor.getRad();
            this.heat = this.reactor.getHeat();
            this.security = this.reactor.getSecurity();
        } else {
            this.output = 0;
            this.rad = 0;
            this.heat = 0;
            if (this.security != EnumTypeSecurity.NONE) {
                this.reactor.setTime(EnumTypeSecurity.STABLE);
            }
            security = EnumTypeSecurity.NONE;
        }
    }

    @Override
    public void onLoaded() {
        super.onLoaded();

        if (!this.world.isRemote && type != -1 && level != -1) {
            this.reactors = EnumReactors.values()[(type - 1) * 4 + (level - 1)];
            this.invSlot.clear();
            this.reactor.reset(reactors);
            this.reactor.level = level;
            this.work = false;
            explode = false;
            this.reactor.setTime(EnumTypeSecurity.STABLE);
            switch (this.reactors.getType()) {
                case FLUID:
                    this.logicReactor = new LogicCreativeFluidReactor(reactor);
                    break;
                case GAS_COOLING_FAST:
                    this.logicReactor = new LogicCreativeGasReactor(reactor);
                    break;
                case GRAPHITE_FLUID:
                    this.logicReactor = new LogicCreativeGraphiteReactor(reactor);
                    break;
                case HIGH_SOLID:
                    this.logicReactor = new LogicCreativeHeatReactor(reactor);
                    break;
            }
        }
    }

    @Override
    public void updateTileServer(final EntityPlayer var1, double var2) {
        if (var2 < 0) {
            var2 *= -1;
            level = (int) var2;
        } else if (var2 >= 1) {
            type = (int) var2;
        } else if (var2 < 1) {
            this.work = !work;
            explode = false;
            this.reactor.explode = false;
            this.reactor.setTime(EnumTypeSecurity.STABLE);
            return;
        }
        if (level != -1 && type != -1) {
            this.reactors = EnumReactors.values()[(type - 1) * 4 + (level - 1)];
            this.invSlot.clear();
            this.reactor.reset(reactors);
            this.size_inventory = 0;

            InvSlot invSlot;
            for (Iterator<InvSlot> var3 = this.invSlots.iterator(); var3.hasNext(); size_inventory += invSlot.size()) {
                invSlot = var3.next();
            }
            this.work = false;
            explode = false;
            var1.closeScreen();
            this.reactor.setTime(EnumTypeSecurity.STABLE);
            this.reactor.level = level;
            switch (this.reactors.getType()) {
                case FLUID:
                    this.logicReactor = new LogicCreativeFluidReactor(reactor);
                    break;
                case GAS_COOLING_FAST:
                    this.logicReactor = new LogicCreativeGasReactor(reactor);
                    break;
                case GRAPHITE_FLUID:
                    this.logicReactor = new LogicCreativeGraphiteReactor(reactor);
                    break;
                case HIGH_SOLID:
                    this.logicReactor = new LogicCreativeHeatReactor(reactor);
                    break;
            }

        }
    }

}
