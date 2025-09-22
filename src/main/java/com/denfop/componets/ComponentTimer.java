package com.denfop.componets;

import com.denfop.network.packet.CustomPacketBuffer;
import com.denfop.tiles.base.TileEntityInventory;
import com.denfop.utils.Timer;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.nbt.NBTTagCompound;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class ComponentTimer extends AbstractComponent {

    private final List<Timer> defaultTimers;
    private final List<Timer> timers;
    double percent = 1;
    private int indexWork;
    private boolean canWork = true;

    public ComponentTimer(TileEntityInventory inventory, Timer... timers) {
        super(inventory);
        this.timers = Arrays.asList(timers);
        this.defaultTimers = new ArrayList<>();
        for (Timer timer : this.timers) {
            this.defaultTimers.add(timer.cloning());
        }
        this.indexWork = 0;
    }

    public int getIndexWork() {
        return indexWork;
    }

    @Override
    public boolean isServer() {
        return true;
    }

    public int getTickFromSecond() {
        return 20;
    }

    @Override
    public void updateEntityServer() {
        super.updateEntityServer();
        boolean need = true;
        if (this.getParent().getWorld().provider.getWorldTime() % getTickFromSecond() == 0 && this.canWork) {
            for (int i = 0; i < this.timers.size(); i++) {
                Timer timer = this.timers.get(i);
                if (timer.canWork()) {
                    timer.work(percent);
                    this.indexWork = i;
                    need = false;
                    break;
                }
            }
            if (need) {
                this.indexWork = -1;
            }
        }

    }

    public boolean isCanWork() {
        return canWork;
    }

    public void setCanWork(final boolean canWork) {
        boolean oldWork = this.canWork;
        this.canWork = canWork;
        if (oldWork && !this.canWork) {
            this.resetTime();
        }
    }

    public void setCanWorkWithOut(final boolean canWork) {
        this.canWork = canWork;
    }

    public void resetTime() {
        for (int i = 0; i < this.timers.size(); i++) {
            this.timers.get(i).readTimer(this.defaultTimers.get(i));
        }

    }

    public String getTime() {
        if (this.indexWork != -1) {
            return this.timers.get(this.indexWork % this.timers.size()).getDisplay();
        } else {
            return this.timers.get(this.timers.size() - 1).getDisplay();
        }

    }

    public double getTimes() {
        if (this.indexWork != -1) {
            final int max = this.getDefaultTimers().get(this.indexWork).getBar();
            return (max - this.timers.get(this.indexWork).getBar()) / (max * 1D);
        } else {
            final int max = this.getDefaultTimers().get(this.timers.size() - 1).getBar();
            return (max - this.timers.get(this.timers.size() - 1).getBar()) / (max * 1D);
        }

    }

    public NBTTagCompound writeNBTToDrops(NBTTagCompound tagCompound) {
        tagCompound.setInteger("size", this.timers.size());
        for (int i = 0; i < this.timers.size(); i++) {
            tagCompound.setTag("Timer_" + i, this.timers.get(i).writeNBT(new NBTTagCompound()));
        }
        return tagCompound;
    }

    public CustomPacketBuffer updateComponent() {
        final CustomPacketBuffer packet = super.updateComponent();
        this.timers.forEach(timer -> timer.writeBuffer(packet));
        packet.writeInt(this.indexWork);
        return packet;
    }

    @Override
    public NBTTagCompound writeToNbt() {
        NBTTagCompound nbtTagCompound = super.writeToNbt();
        nbtTagCompound.setInteger("indexWork", indexWork);
        nbtTagCompound.setInteger("size", this.timers.size());
        for (int i = 0; i < this.timers.size(); i++) {
            nbtTagCompound.setTag("Timer_" + i, this.timers.get(i).writeNBT(new NBTTagCompound()));
        }
        return nbtTagCompound;
    }

    @Override
    public void readFromNbt(final NBTTagCompound nbt) {
        super.readFromNbt(nbt);
        indexWork = nbt.getInteger("indexWork");
        final int size = nbt.getInteger("size");
        for (int i = 0; i < size; i++) {
            this.timers.get(i).readNBT(nbt.getCompoundTag("Timer_" + i));
        }
    }

    public List<Timer> getDefaultTimers() {
        return defaultTimers;
    }

    public List<Timer> getTimers() {
        return timers;
    }

    @Override
    public boolean canUsePurifier(final EntityPlayer player) {
        return true;
    }

    public TypePurifierJob getPurifierJob() {
        return TypePurifierJob.Actions;
    }

    public void ActionPurifier() {
        this.resetTime();
    }

    @Override
    public void onContainerUpdate(EntityPlayerMP player) {
        CustomPacketBuffer buffer = new CustomPacketBuffer(this.timers.size() * 3 + 1);
        this.timers.forEach(timer -> timer.writeBuffer(buffer));
        buffer.writeInt(this.indexWork);
        buffer.flip();
        this.setNetworkUpdate(player, buffer);

    }

    public void onNetworkUpdate(CustomPacketBuffer is) throws IOException {
        this.timers.forEach(timer -> {
            try {
                timer.readBuffer(is);
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        });
        this.indexWork = is.readInt();
    }


}
