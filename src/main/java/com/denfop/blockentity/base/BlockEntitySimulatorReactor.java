package com.denfop.blockentity.base;

import com.denfop.IUItem;
import com.denfop.api.blockentity.MultiBlockEntity;
import com.denfop.api.container.CustomWorldContainer;
import com.denfop.api.reactors.*;
import com.denfop.blocks.BlockTileEntity;
import com.denfop.blocks.mechanism.BlockBaseMachine3Entity;
import com.denfop.containermenu.ContainerMenuBase;
import com.denfop.containermenu.ContainerMenuSimulationReactors;
import com.denfop.datacomponent.DataComponentsInit;
import com.denfop.datacomponent.ReactorSchedule;
import com.denfop.inventory.Inventory;
import com.denfop.inventory.InventorySimulatorReactor;
import com.denfop.items.ItemCraftingElements;
import com.denfop.items.reactors.ItemComponentVent;
import com.denfop.network.IUpdatableTileEvent;
import com.denfop.network.packet.CustomPacketBuffer;
import com.denfop.screen.ScreenIndustrialUpgrade;
import com.denfop.screen.ScreenSimulationReactors;
import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.state.BlockState;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.api.distmarker.OnlyIn;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class BlockEntitySimulatorReactor extends BlockEntityInventory implements IUpdatableTileEvent {

    public final InventorySimulatorReactor invSlot;
    public final Inventory scheduleSlot;
    public int type = -1;
    public int levelReactor = -1;
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

    public BlockEntitySimulatorReactor(BlockPos pos, BlockState state) {
        super(BlockBaseMachine3Entity.simulation_reactors, pos, state);
        this.invSlot = new InventorySimulatorReactor(this, Inventory.TypeItemSlot.INPUT, 80) {
            @Override
            public boolean canPlaceItem(final int index, final ItemStack stack) {
                if (reactors == null) {
                    return false;
                }
                switch (reactors.getType()) {
                    case FLUID:
                    case HIGH_SOLID:
                        if (stack.getItem() instanceof IReactorItem) {
                            IReactorItem iReactorItem = (IReactorItem) stack.getItem();
                            return levelReactor >= iReactorItem.getLevel();
                        } else {
                            return false;
                        }
                    case GAS_COOLING_FAST:
                        if (stack.getItem() instanceof IReactorItem) {
                            IReactorItem iReactorItem = (IReactorItem) stack.getItem();
                            return levelReactor >= iReactorItem.getLevel() && (iReactorItem.getType() != EnumTypeComponent.HEAT_SINK || (stack.getItem() instanceof ItemComponentVent));
                        } else {
                            return false;
                        }
                    case GRAPHITE_FLUID:
                        if (stack.getItem() instanceof IReactorItem) {
                            IReactorItem iReactorItem = (IReactorItem) stack.getItem();
                            return levelReactor >= iReactorItem.getLevel() && (iReactorItem.getType() != EnumTypeComponent.HEAT_EXCHANGER);
                        } else {
                            return false;
                        }
                }
                return false;
            }

            @Override
            public ItemStack set(final int index, final ItemStack content) {
                super.set(index, content);
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
                return content;
            }
        };
        this.reactor = new CreativeReactor(this.reactors, invSlot);
        this.scheduleSlot = new Inventory(this, Inventory.TypeItemSlot.INPUT, 1) {
            @Override
            public boolean canPlaceItem(final int index, final ItemStack stack) {
                return stack.getItem() instanceof ItemCraftingElements && ((ItemCraftingElements<?>) stack.getItem()).getElement().getId() == 143;
            }

            @Override
            public int getStackSizeLimit() {
                return 1;
            }
        };
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
        this.levelReactor = customPacketBuffer.readInt();
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
        levelReactor = customPacketBuffer.readInt();
        if (levelReactor != -1 && type != -1) {
            this.reactors = EnumReactors.values()[(type - 1) * 4 + (levelReactor - 1)];
            this.reactor.level = this.levelReactor;
            this.invSlot.clear();
            this.reactor.reset(reactors);
            this.size_inventory = 0;
            Inventory inventory;
            for (Iterator<Inventory> var3 = this.inventories.iterator(); var3.hasNext(); size_inventory += inventory.size()) {
                inventory = var3.next();
            }
        }
    }

    @Override
    public CustomPacketBuffer writePacket() {
        CustomPacketBuffer customPacketBuffer = super.writePacket();
        customPacketBuffer.writeInt(this.type);
        customPacketBuffer.writeInt(this.levelReactor);
        return customPacketBuffer;
    }

    @Override
    public BlockTileEntity getBlock() {
        return IUItem.basemachine2.getBlock(getTeBlock());
    }

    @Override
    public MultiBlockEntity getTeBlock() {
        return BlockBaseMachine3Entity.simulation_reactors;
    }

    @Override
    public ContainerMenuSimulationReactors getGuiContainer(final Player var1) {
        return new ContainerMenuSimulationReactors(this, var1);
    }

    @Override
    @OnlyIn(Dist.CLIENT)
    public ScreenIndustrialUpgrade<ContainerMenuBase<? extends CustomWorldContainer>> getGui(Player var1, ContainerMenuBase<? extends CustomWorldContainer> menu) {
        return new ScreenSimulationReactors((ContainerMenuSimulationReactors) menu);
    }

    @Override
    public CompoundTag writeToNBT(final CompoundTag nbt) {
        CompoundTag nbtTagCompound = super.writeToNBT(nbt);
        nbtTagCompound.putInt("type", type);
        nbtTagCompound.putInt("level", levelReactor);
        nbtTagCompound.putBoolean("work", work);
        nbtTagCompound.putBoolean("explode", explode);
        if (reactors != null) {
            nbtTagCompound.putInt("reactors", reactors.ordinal());
        }

        return nbtTagCompound;
    }

    @Override
    public void readFromNBT(final CompoundTag nbtTagCompound) {
        super.readFromNBT(nbtTagCompound);
        type = nbtTagCompound.getInt("type");
        levelReactor = nbtTagCompound.getInt("level");

        reactors = EnumReactors.values()[nbtTagCompound.getInt("reactors")];
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
        customPacketBuffer.writeInt(this.levelReactor);
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
        if (this.needUpdate && this.level.getGameTime() % 20 == 0 && this.reactors != null) {
            if (!this.scheduleSlot.isEmpty()) {
                needUpdate = false;
                ItemStack stack = this.scheduleSlot.get(0);
                final List<ItemStack> infoStack = this.logicReactor.getInfoStack();
                List<ItemStack> stacksItem = new ArrayList<>();
                List<Integer> indexItem = new ArrayList<>();
                for (int y = 0; y < this.reactors.getHeight(); y++) {
                    for (int x = 0; x < this.reactors.getWidth(); x++) {
                        ItemStack stack1 = this.reactor.getItemAt(x, y);
                        if (stack1.isEmpty()) {
                            stacksItem.add(ItemStack.EMPTY);
                            indexItem.add(y * this.reactors.getWidth() + x);
                        } else {
                            for (ItemStack itemStack : infoStack) {
                                if (itemStack.is(stack1.getItem())) {
                                    stacksItem.add(stack1);
                                    indexItem.add(y * this.reactors.getWidth() + x);
                                    break;
                                }
                            }
                        }
                    }
                }
                ReactorSchedule reactorSchedule = new ReactorSchedule(type, levelReactor, this.reactors.getNameReactor().toLowerCase(),
                        (int) (this.logicReactor.getGeneration() * (this.reactors.getType() == ITypeRector.GAS_COOLING_FAST
                                ? 1.175
                                :
                                1)), this.logicReactor.getRadGeneration(), stacksItem, indexItem);
                stack.set(DataComponentsInit.REACTOR_SCHEDULE, reactorSchedule);
            }
        }
        if (work && !explode && this.reactors != null && logicReactor != null) {
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

        if (!this.level.isClientSide && type != -1 && levelReactor != -1) {
            this.reactors = EnumReactors.values()[(type - 1) * 4 + (levelReactor - 1)];
            this.invSlot.clear();
            this.reactor.reset(reactors);
            this.reactor.level = levelReactor;
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
    public void updateTileServer(final Player var1, double var2) {
        if (var2 < 0) {
            var2 *= -1;
            levelReactor = (int) var2;
        } else if (var2 >= 1) {
            type = (int) var2;
        } else if (var2 < 1) {
            this.work = !work;
            explode = false;
            this.reactor.explode = false;
            this.reactor.setTime(EnumTypeSecurity.STABLE);
            return;
        }
        if (levelReactor != -1 && type != -1) {
            this.reactors = EnumReactors.values()[(type - 1) * 4 + (levelReactor - 1)];
            this.invSlot.clear();
            this.reactor.reset(reactors);
            this.size_inventory = 0;

            Inventory inventory;
            for (Iterator<Inventory> var3 = this.inventories.iterator(); var3.hasNext(); size_inventory += inventory.size()) {
                inventory = var3.next();
            }
            this.work = false;
            explode = false;
            var1.closeContainer();
            this.reactor.setTime(EnumTypeSecurity.STABLE);
            this.reactor.level = levelReactor;
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
