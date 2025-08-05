package com.denfop.tiles.mechanism;

import com.denfop.IUItem;
import com.denfop.Localization;
import com.denfop.api.inv.IAdvInventory;
import com.denfop.api.recipe.InvSlotOutput;
import com.denfop.api.tile.IMultiTileBlock;
import com.denfop.api.vein.Type;
import com.denfop.api.vein.Vein;
import com.denfop.api.vein.VeinSystem;
import com.denfop.blocks.BlockTileEntity;
import com.denfop.blocks.mechanism.BlockBaseMachine3;
import com.denfop.componets.Energy;
import com.denfop.container.ContainerBase;
import com.denfop.container.ContainerWirelessMineralQuarry;
import com.denfop.datacomponent.DataComponentsInit;
import com.denfop.datacomponent.VeinInfo;
import com.denfop.gui.GuiCore;
import com.denfop.gui.GuiWirelessMineralQuarry;
import com.denfop.invslot.InvSlot;
import com.denfop.items.ItemVeinSensor;
import com.denfop.network.DecoderHandler;
import com.denfop.network.EncoderHandler;
import com.denfop.network.packet.CustomPacketBuffer;
import com.denfop.tiles.base.IManufacturerBlock;
import com.denfop.tiles.base.TileEntityInventory;
import com.denfop.utils.Keyboard;
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

import java.io.IOException;
import java.util.LinkedList;
import java.util.List;

public class TileEntityWirelessMineralQuarry extends TileEntityInventory implements IManufacturerBlock {


    public final Energy energy;
    public final InvSlot invslot;
    public final InvSlotOutput output;
    public List<Vein> veinList = new LinkedList<>();
    public List<ItemStack> itemStacks = new LinkedList<>();
    public int levelBlock;

    public TileEntityWirelessMineralQuarry(BlockPos pos, BlockState state) {
        super(BlockBaseMachine3.wireless_mineral_quarry, pos, state);
        this.output = new InvSlotOutput(this, 18);
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

                if (stack.has(DataComponentsInit.VEIN_INFO)) {
                    return !stack.get(DataComponentsInit.VEIN_INFO).type().equals("oil") && !stack.get(DataComponentsInit.VEIN_INFO).type().equals("gas");
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
    public ContainerWirelessMineralQuarry getGuiContainer(final Player var1) {
        return new ContainerWirelessMineralQuarry(this, var1);
    }

    @Override
    @OnlyIn(Dist.CLIENT)
    public GuiCore<ContainerBase<? extends IAdvInventory>> getGui(Player var1, ContainerBase<? extends IAdvInventory> menu) {
        return new GuiWirelessMineralQuarry((ContainerWirelessMineralQuarry) menu);
    }

    @Override
    public IMultiTileBlock getTeBlock() {
        return BlockBaseMachine3.wireless_mineral_quarry;
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
        for (int i = 0; i < veinList.size(); i++) {
            Vein vein = veinList.get(i);
            ItemStack stack = itemStacks.get(i);
            if (this.energy.getEnergy() >= 10 && vein.isFind()) {
                if (vein.getCol() >= 1) {
                    int size = Math.min((this.levelBlock + 1) * 2, vein.getCol());
                    stack.setCount(size);
                    int prev = this.output.addExperimental(stack);
                    size = size - prev;
                    if (size != 0) {
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

        super.addInformation(stack, tooltip);
    }

    @Override
    public void readContainerPacket(final CustomPacketBuffer customPacketBuffer) {
        super.readContainerPacket(customPacketBuffer);
        this.veinList.clear();
        this.itemStacks.clear();
        int col = customPacketBuffer.readInt();
        for (int i = 0; i < col; i++) {
            try {
                this.veinList.add((Vein) DecoderHandler.decode(customPacketBuffer));
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }
        for (int i = 0; i < col; i++) {
            try {
                this.itemStacks.add((ItemStack) DecoderHandler.decode(customPacketBuffer));
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
        for (ItemStack vein : this.itemStacks) {
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
        itemStacks.clear();
        for (ItemStack stack : this.invslot) {
            if (stack.isEmpty()) {
                continue;
            }
            VeinInfo veinInfo = stack.get(DataComponentsInit.VEIN_INFO);
            int x = veinInfo.x();
            int z = veinInfo.z();
            ChunkPos chunkPos = new ChunkPos(x >> 4, z >> 4);
            final Vein vein = VeinSystem.system.getVein(chunkPos);
            if (vein.isFind() && vein.getType() == Type.VEIN) {
                veinList.add(vein);
                if (vein.isOldMineral()) {
                    itemStacks.add(new ItemStack(IUItem.heavyore.getItem(vein.getMeta()), 1));
                } else {
                    itemStacks.add(new ItemStack(IUItem.mineral.getItem(vein.getMeta()), 1));
                }
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
