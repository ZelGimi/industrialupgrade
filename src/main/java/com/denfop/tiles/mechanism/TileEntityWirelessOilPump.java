package com.denfop.tiles.mechanism;

import com.denfop.IUItem;
import com.denfop.Localization;
import com.denfop.api.inv.IAdvInventory;
import com.denfop.api.tile.IMultiTileBlock;
import com.denfop.api.vein.Type;
import com.denfop.api.vein.Vein;
import com.denfop.api.vein.VeinSystem;
import com.denfop.blocks.BlockTileEntity;
import com.denfop.blocks.FluidName;
import com.denfop.blocks.mechanism.BlockBaseMachine3;
import com.denfop.componets.Energy;
import com.denfop.componets.Fluids;
import com.denfop.container.ContainerBase;
import com.denfop.container.ContainerWirelessOilPump;
import com.denfop.datacomponent.DataComponentsInit;
import com.denfop.datacomponent.VeinInfo;
import com.denfop.gui.GuiCore;
import com.denfop.gui.GuiWirelessOilPump;
import com.denfop.invslot.InvSlot;
import com.denfop.items.ItemVeinSensor;
import com.denfop.network.DecoderHandler;
import com.denfop.network.EncoderHandler;
import com.denfop.network.packet.CustomPacketBuffer;
import com.denfop.tiles.base.IManufacturerBlock;
import com.denfop.tiles.base.TileEntityInventory;
import com.denfop.utils.Keyboard;
import com.denfop.utils.ModUtils;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.ChunkPos;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.Vec3;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.api.distmarker.OnlyIn;
import net.neoforged.neoforge.fluids.FluidStack;
import net.neoforged.neoforge.fluids.capability.IFluidHandler;

import java.io.IOException;
import java.util.LinkedList;
import java.util.List;

public class TileEntityWirelessOilPump extends TileEntityInventory implements IManufacturerBlock {

    public final Fluids fluids;
    public final Fluids.InternalFluidTank fluidTank;
    public final Energy energy;
    public final InvSlot invslot;
    public List<Vein> veinList = new LinkedList<>();
    public int levelBlock;

    public TileEntityWirelessOilPump(BlockPos pos, BlockState state) {
        super(BlockBaseMachine3.wireless_oil_pump, pos, state);
        this.fluids = this.addComponent(new Fluids(this));
        this.fluidTank = fluids.addTank("tank", 256000, Fluids.fluidPredicate(FluidName.fluidneft.getInstance().get(),
                FluidName.fluidsour_light_oil.getInstance().get(), FluidName.fluidsour_heavy_oil.getInstance().get(),
                FluidName.fluidsour_medium_oil.getInstance().get(), FluidName.fluidsweet_medium_oil.getInstance().get(),
                FluidName.fluidsweet_heavy_oil.getInstance().get()
        ));
        this.energy = this.addComponent(Energy.asBasicSink(this, 50000, 14));
        this.invslot = new InvSlot(this, InvSlot.TypeItemSlot.INPUT, 4) {
            @Override
            public ItemStack set(final int index, final ItemStack content) {
                super.set(index, content);
                updateList();
                return content;
            }

            @Override
            public boolean accepts(final ItemStack stack, final int index) {
                if (!(stack.getItem() instanceof ItemVeinSensor)) {
                    return false;
                }
                final CompoundTag nbt = ModUtils.nbt(stack);
                if (stack.has(DataComponentsInit.VEIN_INFO)) {
                    return stack.get(DataComponentsInit.VEIN_INFO).type().equals("oil");
                }
                return false;
            }
        };
    }

    @Override
    public BlockTileEntity getBlock() {
        return IUItem.basemachine2.getBlock(getTeBlock());
    }

    @Override
    @OnlyIn(Dist.CLIENT)
    public GuiCore<ContainerBase<? extends IAdvInventory>> getGui(Player var1, ContainerBase<? extends IAdvInventory> menu) {
        return new GuiWirelessOilPump((ContainerWirelessOilPump) menu);
    }

    @Override
    public ContainerWirelessOilPump getGuiContainer(final Player var1) {
        return new ContainerWirelessOilPump(this, var1);
    }

    @Override
    public IMultiTileBlock getTeBlock() {
        return BlockBaseMachine3.wireless_oil_pump;
    }

    @Override
    public void readFromNBT(final CompoundTag nbtTagCompound) {
        super.readFromNBT(nbtTagCompound);
        this.levelBlock = nbtTagCompound.getInt("level");
    }

    @Override
    public CompoundTag writeToNBT(final CompoundTag nbt) {
        CompoundTag nbtTagCompound = super.writeToNBT(nbt);
        nbtTagCompound.putInt("level", levelBlock);
        return nbtTagCompound;
    }

    @Override
    public boolean onActivated(Player player, InteractionHand hand, Direction side, Vec3 vec3) {
        if (levelBlock < 10) {
            ItemStack stack = player.getItemInHand(hand);
            if (!stack.getItem().equals(IUItem.upgrade_speed_creation.getItem())) {
                return super.onActivated(player, hand, side, vec3);
            } else {
                stack.shrink(1);
                this.levelBlock++;
                return true;
            }
        } else {
            return super.onActivated(player, hand, side, vec3);
        }
    }


    @Override
    public void updateEntityServer() {
        super.updateEntityServer();
        boolean active = false;
        for (Vein vein : this.veinList) {
            if (this.energy.getEnergy() >= 10 && vein.isFind()) {
                if (vein.getCol() >= 1) {
                    int size = Math.min((this.levelBlock + 1) * 2, vein.getCol());
                    size = Math.min(size, this.fluidTank.getCapacity() - this.fluidTank.getFluidAmount());
                    if (this.fluidTank.getFluidAmount() + size <= this.fluidTank.getCapacity()) {
                        int variety = vein.getMeta() / 3;
                        int type = vein.getMeta() % 3;
                        switch (variety) {
                            case 0:
                                switch (type) {
                                    case 0:
                                        this.fluidTank.fill(new FluidStack(FluidName.fluidneft.getInstance().get(), size), IFluidHandler.FluidAction.EXECUTE);
                                        break;
                                    case 1:
                                        this.fluidTank.fill(
                                                new FluidStack(FluidName.fluidsweet_medium_oil.getInstance().get(), size),
                                                IFluidHandler.FluidAction.EXECUTE
                                        );
                                        break;
                                    case 2:
                                        this.fluidTank.fill(
                                                new FluidStack(FluidName.fluidsweet_heavy_oil.getInstance().get(), size),
                                                IFluidHandler.FluidAction.EXECUTE
                                        );
                                        break;
                                }
                                break;
                            case 1:
                                switch (type) {
                                    case 0:
                                        this.fluidTank.fill(
                                                new FluidStack(FluidName.fluidsour_light_oil.getInstance().get(), size),
                                                IFluidHandler.FluidAction.EXECUTE
                                        );
                                        break;
                                    case 1:
                                        this.fluidTank.fill(
                                                new FluidStack(FluidName.fluidsour_medium_oil.getInstance().get(), size),
                                                IFluidHandler.FluidAction.EXECUTE
                                        );
                                        break;
                                    case 2:
                                        this.fluidTank.fill(
                                                new FluidStack(FluidName.fluidsour_heavy_oil.getInstance().get(), size),
                                                IFluidHandler.FluidAction.EXECUTE
                                        );
                                        break;
                                }
                                break;
                        }
                        vein.removeCol(size);
                        active = true;
                        this.energy.useEnergy(10);
                    }
                }

            }
        }
        this.setActive(active);
    }

    public List<ItemStack> getWrenchDrops(Player player, int fortune) {
        List<ItemStack> ret = super.getWrenchDrops(player, fortune);
        if (this.levelBlock != 0) {
            ret.add(new ItemStack(IUItem.upgrade_speed_creation.getItem(), this.levelBlock));
            this.levelBlock = 0;
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
        if (stack.has(DataComponentsInit.DATA) && stack.get(DataComponentsInit.DATA).contains("fluid")) {
            FluidStack fluidStack = FluidStack.parseOptional(level.registryAccess(), (CompoundTag) stack.get(DataComponentsInit.DATA).get("fluid"));

            tooltip.add(Localization.translate("iu.fluid.info") + fluidStack.getHoverName().getString());
            tooltip.add(Localization.translate("iu.fluid.info1") + fluidStack.getAmount() / 1000 + " B");

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
        if (!this.getWorld().isClientSide) {
            updateList();
        }
    }

    public void updateList() {
        veinList.clear();
        for (ItemStack stack : this.invslot) {
            if (stack.isEmpty()) {
                continue;
            }
            VeinInfo veinInfo = stack.get(DataComponentsInit.VEIN_INFO);
            int x = veinInfo.x();
            int z = veinInfo.z();
            ChunkPos chunkPos = new ChunkPos(x >> 4, z >> 4);
            final Vein vein = VeinSystem.system.getVein(chunkPos);
            if (vein.isFind() && vein.getType() == Type.OIL) {
                veinList.add(vein);
            }
        }
    }

    @Override
    public int getLevelMechanism() {
        return this.levelBlock;
    }

    @Override
    public void setLevelMech(final int level) {
        this.levelBlock = level;
    }

    @Override
    public void removeLevel(final int level) {
        this.levelBlock -= level;
    }

}
