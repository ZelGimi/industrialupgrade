package com.denfop.blockentity.mechanism;

import com.denfop.IUItem;
import com.denfop.api.blockentity.MultiBlockEntity;
import com.denfop.api.container.CustomWorldContainer;
import com.denfop.api.vein.common.Type;
import com.denfop.api.vein.common.VeinBase;
import com.denfop.api.vein.common.VeinSystem;
import com.denfop.blockentity.base.BlockEntityInventory;
import com.denfop.blockentity.base.IManufacturerBlock;
import com.denfop.blocks.BlockTileEntity;
import com.denfop.blocks.FluidName;
import com.denfop.blocks.mechanism.BlockBaseMachine3Entity;
import com.denfop.componets.Energy;
import com.denfop.componets.Fluids;
import com.denfop.containermenu.ContainerMenuBase;
import com.denfop.containermenu.ContainerMenuWirelessGasPump;
import com.denfop.inventory.Inventory;
import com.denfop.items.ItemVeinSensor;
import com.denfop.network.DecoderHandler;
import com.denfop.network.EncoderHandler;
import com.denfop.network.packet.CustomPacketBuffer;
import com.denfop.screen.ScreenIndustrialUpgrade;
import com.denfop.screen.ScreenWirelessGasPump;
import com.denfop.utils.Keyboard;
import com.denfop.utils.Localization;
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
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.fluids.capability.IFluidHandler;

import java.io.IOException;
import java.util.LinkedList;
import java.util.List;

public class BlockEntityWirelessGasPump extends BlockEntityInventory implements IManufacturerBlock {

    public final Fluids fluids;
    public final Fluids.InternalFluidTank fluidTank;
    public final Energy energy;
    public final Inventory invslot;
    public List<VeinBase> veinList = new LinkedList<>();
    public int levelBlock;

    public BlockEntityWirelessGasPump(BlockPos pos, BlockState state) {
        super(BlockBaseMachine3Entity.wireless_gas_pump, pos, state);
        this.fluids = this.addComponent(new Fluids(this));
        this.fluidTank = fluids.addTank("tank", 256000, Fluids.fluidPredicate(FluidName.fluidgas.getInstance().get()
        ));
        this.energy = this.addComponent(Energy.asBasicSink(this, 50000, 14));
        this.invslot = new Inventory(this, Inventory.TypeItemSlot.INPUT, 4) {
            @Override
            public ItemStack set(final int index, final ItemStack content) {
                super.set(index, content);
                updateList();
                return content;
            }

            @Override
            public boolean canPlaceItem(final int index, final ItemStack stack) {
                if (!(stack.getItem() instanceof ItemVeinSensor)) {
                    return false;
                }
                final CompoundTag nbt = ModUtils.nbt(stack);
                if (!nbt.getString("type").isEmpty()) {
                    return nbt.getString("type").equals("gas");
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
    public ScreenIndustrialUpgrade<ContainerMenuBase<? extends CustomWorldContainer>> getGui(Player var1, ContainerMenuBase<? extends CustomWorldContainer> menu) {

        return new ScreenWirelessGasPump((ContainerMenuWirelessGasPump) menu);
    }

    @Override
    public ContainerMenuWirelessGasPump getGuiContainer(final Player var1) {
        return new ContainerMenuWirelessGasPump(this, var1);
    }

    @Override
    public MultiBlockEntity getTeBlock() {
        return BlockBaseMachine3Entity.wireless_gas_pump;
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
        for (VeinBase vein : this.veinList) {
            if (this.energy.getEnergy() >= 10 && vein.isFind()) {
                if (vein.getCol() >= 1) {
                    int size = Math.min((this.levelBlock + 1) * 2, vein.getCol());
                    size = Math.min(size, this.fluidTank.getCapacity() - this.fluidTank.getFluidAmount());
                    if (this.fluidTank.getFluidAmount() + size <= this.fluidTank.getCapacity()) {
                        this.fluidTank.fill(new FluidStack(FluidName.fluidgas.getInstance().get(), size), IFluidHandler.FluidAction.EXECUTE);
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
        if (stack.hasTag() && stack.getTag().contains("fluid")) {
            FluidStack fluidStack = FluidStack.loadFluidStackFromNBT((CompoundTag) stack.getTag().get("fluid"));

            tooltip.add(Localization.translate("iu.fluid.info") + fluidStack.getFluid().getFluidType().getDescription().getString());
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
                this.veinList.add((VeinBase) DecoderHandler.decode(customPacketBuffer));
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }
    }

    @Override
    public CustomPacketBuffer writeContainerPacket() {
        CustomPacketBuffer customPacketBuffer = super.writeContainerPacket();
        customPacketBuffer.writeInt(this.veinList.size());
        for (VeinBase vein : this.veinList) {
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
            final CompoundTag nbt = ModUtils.nbt(stack);
            int x = nbt.getInt("x");
            int z = nbt.getInt("z");
            ChunkPos chunkPos = new ChunkPos(x >> 4, z >> 4);
            final VeinBase vein = VeinSystem.system.getVein(chunkPos);
            if (vein.isFind() && vein.getType() == Type.GAS) {
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
