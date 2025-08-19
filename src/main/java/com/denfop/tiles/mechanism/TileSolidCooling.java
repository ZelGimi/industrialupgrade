package com.denfop.tiles.mechanism;

import com.denfop.IUItem;
import com.denfop.Localization;
import com.denfop.api.inv.IAdvInventory;
import com.denfop.api.tile.IMultiTileBlock;
import com.denfop.audio.EnumSound;
import com.denfop.blocks.BlockTileEntity;
import com.denfop.blocks.mechanism.BlockBaseMachine3;
import com.denfop.componets.CoolComponent;
import com.denfop.componets.client.ComponentClientEffectRender;
import com.denfop.componets.client.EffectType;
import com.denfop.container.ContainerBase;
import com.denfop.container.ContainerSolidCoolMachine;
import com.denfop.gui.GuiCore;
import com.denfop.gui.GuiSolidCoolMachine;
import com.denfop.invslot.InvSlot;
import com.denfop.network.DecoderHandler;
import com.denfop.network.EncoderHandler;
import com.denfop.network.IUpdatableTileEvent;
import com.denfop.network.packet.CustomPacketBuffer;
import com.denfop.tiles.base.TileElectricMachine;
import com.denfop.utils.Keyboard;
import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class TileSolidCooling extends TileElectricMachine implements IUpdatableTileEvent {


    public static boolean init = false;
    static Map<ItemStack, Integer> timerItem = new HashMap<>();
    public final InvSlot slot;
    public int time;
    public CoolComponent cold;
    public int max;
    public boolean work;

    public TileSolidCooling(BlockPos pos, BlockState state) {
        super(0, 0, 1,BlockBaseMachine3.solid_cooling,pos,state);
        this.cold = this.addComponent(CoolComponent.asBasicSource(this, 4, 14));
        this.max = 4;
        this.componentClientEffectRender = new ComponentClientEffectRender(this, EffectType.REFRIGERATOR);
        this.time = 0;
        this.slot = new InvSlot(this, InvSlot.TypeItemSlot.INPUT, 1) {
            @Override
            public boolean accepts(final ItemStack stack, final int index) {
                boolean find = false;
                for (Map.Entry<ItemStack, Integer> entry : timerItem.entrySet()) {
                    if (entry.getKey().is(stack.getItem())) {
                        find = true;
                        break;
                    }
                }
                return find;
            }
        };

    }

    public static void init() {
        timerItem.put(new ItemStack(Items.SNOWBALL, 1), 2);
        timerItem.put(new ItemStack(Blocks.SNOW, 1), 10);
        timerItem.put(new ItemStack(Blocks.ICE, 1), 30);
        timerItem.put(new ItemStack(Blocks.PACKED_ICE, 1), 90);
        timerItem.put(new ItemStack(Blocks.BLUE_ICE, 1), 90*9);
    }



    @Override
    public boolean needUpdate() {
        return true;
    }

    @Override
    public CustomPacketBuffer writeUpdatePacket() {
        final CustomPacketBuffer packet = super.writeUpdatePacket();
        try {
            EncoderHandler.encode(packet, cold, false);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        return packet;
    }

    @Override
    public void readUpdatePacket(final CustomPacketBuffer customPacketBuffer) {
        super.readUpdatePacket(customPacketBuffer);
        try {
            cold.onNetworkUpdate(customPacketBuffer);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void readContainerPacket(final CustomPacketBuffer customPacketBuffer) {
        super.readContainerPacket(customPacketBuffer);
        try {
            max = (int) DecoderHandler.decode(customPacketBuffer);
            work = (boolean) DecoderHandler.decode(customPacketBuffer);
            time = (int) DecoderHandler.decode(customPacketBuffer);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

    }

    @Override
    public CustomPacketBuffer writeContainerPacket() {
        final CustomPacketBuffer packet = super.writeContainerPacket();
        try {
            EncoderHandler.encode(packet, max);
            EncoderHandler.encode(packet, work);
            EncoderHandler.encode(packet, time);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        return packet;
    }

    public CustomPacketBuffer writePacket() {
        final CustomPacketBuffer packet = super.writePacket();
        try {
            EncoderHandler.encode(packet, cold, false);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        return packet;
    }

    public void readPacket(CustomPacketBuffer customPacketBuffer) {
        super.readPacket(customPacketBuffer);
        try {
            cold.onNetworkUpdate(customPacketBuffer);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public IMultiTileBlock getTeBlock() {
        return BlockBaseMachine3.solid_cooling;
    }

    public BlockTileEntity getBlock() {
        return IUItem.basemachine2.getBlock(getTeBlock());
    }

    public void readFromNBT(CompoundTag nbttagcompound) {
        super.readFromNBT(nbttagcompound);
        this.max = nbttagcompound.getInt("max");
        this.work = nbttagcompound.getBoolean("work");
        this.cold.setCapacity(this.max);
    }

    public CompoundTag writeToNBT(CompoundTag nbttagcompound) {
        super.writeToNBT(nbttagcompound);
        nbttagcompound.putInt("max", this.max);
        nbttagcompound.putBoolean("work", this.work);
        return nbttagcompound;

    }


    @Override
    public void updateTileServer(final Player entityPlayer, final double i) {
        if (i == 0) {
            this.cold.setCapacity(this.max + 4);
            if (this.cold.getCapacity() > 16) {
                this.cold.setCapacity(16);

            }
            this.max = (int) this.cold.getCapacity();
        }
        if (i == 1) {
            this.cold.setCapacity(this.max - 4);
            if (this.cold.getCapacity() < 4) {
                this.cold.setCapacity(4);

            }
            this.max = (int) this.cold.getCapacity();
        }
        if (i == 2) {
            this.work = !this.work;
        }
        if (i == 10) {
            super.updateTileServer(entityPlayer, i);
        }
    }


    @Override
    public void addInformation(final ItemStack stack, final List<String> tooltip) {
        if (!Keyboard.isKeyDown(Keyboard.KEY_LSHIFT)) {
            tooltip.add(Localization.translate("press.lshift"));
        }
        tooltip.add(Localization.translate("iu.solid_colling"));
        super.addInformation(stack, tooltip);
    }

    @Override
    public void onLoaded() {
        super.onLoaded();

    }

    public void updateEntityServer() {
        super.updateEntityServer();
        if ((this.cold.buffer.allow || work) && time > 0) {
            if (!this.slot.isEmpty()) {
                for (Map.Entry<ItemStack, Integer> entry : timerItem.entrySet()) {
                    if (entry.getKey().is(this.slot.get(0).getItem())) {
                        this.time = entry.getValue();
                        this.slot.get(0).shrink(1);
                        break;
                    }
                }
            }
        }
        if ((this.cold.buffer.allow || work) && time > 0) {
            if (this.cold.getEnergy() < this.cold.getCapacity()) {
                this.cold.addEnergy(1);
                this.time--;
                initiate(0);
                this.setActive(true);

            }
            if (this.level.getGameTime() % 400 == 0) {
                initiate(2);
            }

            if (this.time == 0) {
                initiate(2);
                this.setActive(false);
            } else {
                initiate(0);
            }

        } else {
            initiate(2);
            this.setActive(false);
        }
        if (this.level.getGameTime() % 20 == 0 && this.cold.getEnergy() >= 1) {
            this.cold.addEnergy(-1);
        }
    }


    @Override
    public ContainerSolidCoolMachine getGuiContainer(final Player entityPlayer) {
        return new ContainerSolidCoolMachine(entityPlayer, this);
    }

    @Override
    @OnlyIn(Dist.CLIENT)
    public GuiCore<ContainerBase<? extends IAdvInventory>> getGui(Player var1, ContainerBase<? extends IAdvInventory> menu) {
        return new GuiSolidCoolMachine((ContainerSolidCoolMachine) menu);
    }


    @Override
    public SoundEvent getSound() {
        return EnumSound.cooling.getSoundEvent();
    }

}
