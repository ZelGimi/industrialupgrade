package com.denfop.tiles.mechanism.generator.things.fluid;

import com.denfop.IUItem;
import com.denfop.Localization;
import com.denfop.api.tile.IMultiTileBlock;
import com.denfop.api.upgrades.IUpgradableBlock;
import com.denfop.api.upgrades.UpgradableProperty;
import com.denfop.audio.EnumSound;
import com.denfop.blocks.BlockTileEntity;
import com.denfop.blocks.FluidName;
import com.denfop.blocks.mechanism.BlockBaseMachine3;
import com.denfop.componets.Fluids;
import com.denfop.container.ContainerAirCollector;
import com.denfop.gui.GuiAirCollector;
import com.denfop.invslot.InvSlot;
import com.denfop.invslot.InvSlotDrainTank;
import com.denfop.invslot.InvSlotFluid;
import com.denfop.invslot.InvSlotUpgrade;
import com.denfop.network.DecoderHandler;
import com.denfop.network.EncoderHandler;
import com.denfop.network.packet.CustomPacketBuffer;
import com.denfop.tiles.base.IManufacturerBlock;
import com.denfop.tiles.base.TileElectricMachine;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.SoundEvent;
import net.minecraftforge.fluids.Fluid;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.fluids.FluidTank;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import org.lwjgl.input.Keyboard;

import java.io.IOException;
import java.util.EnumSet;
import java.util.List;
import java.util.Set;

public class TileAirCollector extends TileElectricMachine implements IUpgradableBlock, IManufacturerBlock {

    public final Fluids fluids;
    public final InvSlotUpgrade upgradeSlot;
    public final InvSlotDrainTank[] containerslot;
    public FluidTank[] fluidTank;
    private int level;

    public TileAirCollector() {
        super(5000, 1, 3);
        this.fluidTank = new FluidTank[3];
        this.fluids = this.addComponent(new Fluids(this));
        Fluid[] name1 = new Fluid[]{FluidName.fluidazot.getInstance(), FluidName.fluidoxy.getInstance(),
                FluidName.fluidco2.getInstance()};
        for (int i = 0; i < fluidTank.length; i++) {

            this.fluidTank[i] = this.fluids.addTank("fluidTank" + i, 10000, InvSlot.TypeItemSlot.OUTPUT,
                    Fluids.fluidPredicate(name1[i])
            );

        }
        this.containerslot = new InvSlotDrainTank[name1.length];
        for (int i = 0; i < name1.length; i++) {
            this.containerslot[i] = new InvSlotDrainTank(this, InvSlot.TypeItemSlot.INPUT, 1,
                    InvSlotFluid.TypeFluidSlot.OUTPUT, name1[i]
            );
        }
        this.upgradeSlot = new InvSlotUpgrade(this, 4);
        this.level = 0;

    }

    @Override
    public void readContainerPacket(final CustomPacketBuffer customPacketBuffer) {
        super.readContainerPacket(customPacketBuffer);
        try {
            fluidTank = (FluidTank[]) DecoderHandler.decode(customPacketBuffer);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

    }

    @Override
    public CustomPacketBuffer writeContainerPacket() {
        final CustomPacketBuffer packet = super.writeContainerPacket();
        try {
            EncoderHandler.encode(packet, fluidTank);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        return packet;
    }

    public IMultiTileBlock getTeBlock() {
        return BlockBaseMachine3.aircollector;
    }

    public BlockTileEntity getBlock() {
        return IUItem.basemachine2;
    }

    @SideOnly(Side.CLIENT)
    public void addInformation(final ItemStack stack, final List<String> tooltip, final ITooltipFlag advanced) {
        if (!Keyboard.isKeyDown(Keyboard.KEY_LSHIFT)) {
            tooltip.add(Localization.translate("press.lshift"));
        }
        if (Keyboard.isKeyDown(Keyboard.KEY_LSHIFT)) {
            tooltip.add(Localization.translate("iu.machines_work_energy") + 5 + Localization.translate("iu" +
                    ".machines_work_energy_type_eu"));
            tooltip.add(Localization.translate("iu.aircollector.info", 20) + new FluidStack(
                    FluidName.fluidazot.getInstance(),
                    1
            ).getLocalizedName());
            tooltip.add(Localization.translate("iu.aircollector.info", 60) + new FluidStack(
                    FluidName.fluidoxy.getInstance(),
                    1
            ).getLocalizedName());
            tooltip.add(Localization.translate("iu.aircollector.info", 120) + new FluidStack(
                    FluidName.fluidco2.getInstance(),
                    1
            ).getLocalizedName());

        }
        super.addInformation(stack, tooltip, advanced);
    }

    public List<ItemStack> getWrenchDrops(EntityPlayer player, int fortune) {
        List<ItemStack> ret = super.getWrenchDrops(player, fortune);
        if (this.level != 0) {
            ret.add(new ItemStack(IUItem.upgrade_speed_creation, this.level));
            this.level = 0;
        }
        return ret;
    }

    public FluidTank getFluidTank(int num) {
        return this.fluidTank[num];
    }

    @Override
    public ContainerAirCollector getGuiContainer(final EntityPlayer entityPlayer) {
        return new ContainerAirCollector(entityPlayer, this);
    }

    @Override
    public NBTTagCompound writeToNBT(final NBTTagCompound nbttagcompound) {
        super.writeToNBT(nbttagcompound);
        nbttagcompound.setInteger("level", this.level);

        return nbttagcompound;
    }

    @Override
    public void readFromNBT(final NBTTagCompound nbttagcompound) {
        super.readFromNBT(nbttagcompound);
        this.level = nbttagcompound.getInteger("level");

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
                return false;
            }
        } else {
            return super.onActivated(player, hand, side, hitX, hitY, hitZ);
        }
    }

    @Override
    @SideOnly(Side.CLIENT)
    public GuiScreen getGui(final EntityPlayer entityPlayer, final boolean b) {
        return new GuiAirCollector(new ContainerAirCollector(entityPlayer, this));
    }

    public int gaugeLiquidScaled(int i) {
        return this.getFluidTank(0).getFluidAmount() <= 0 ? 0 :
                this.getFluidTank(0).getFluidAmount() * i / this.getFluidTank(0).getCapacity();
    }

    public double gaugeLiquidScaled(double i) {
        return this.getFluidTank(0).getFluidAmount() <= 0 ? 0 :
                this.getFluidTank(0).getFluidAmount() * i / this.getFluidTank(0).getCapacity();
    }

    public String getStartSoundFile() {
        return "Machines/air_collector.ogg";
    }

    public void onLoaded() {
        super.onLoaded();
        if (!this.getWorld().isRemote) {
            this.setUpgradestat();
        }

    }


    public void updateEntityServer() {
        super.updateEntityServer();
        for (FluidTank tank : fluidTank) {
            if (!tank.equals(fluidTank[0])) {
                for (InvSlotDrainTank slot : this.containerslot) {
                    if (tank.getFluidAmount() >= 1000 && !slot.isEmpty()) {
                        slot.processFromTank(tank, this.outputSlot);
                    }
                }
            }
        }
        boolean work = false;
        if (this.energy.getEnergy() > 5 + 5 * this.level) {
            if (this.world.provider.getWorldTime() % 400 == 0) {
                this.initiate(2);
            }
            work = true;
            if (this.world.provider.getWorldTime() % 20 == 0) {
                if (fluidTank[0].getFluidAmount() + 1 <= fluidTank[0].getCapacity()) {
                    fluidTank[0].fill(
                            new FluidStack(FluidName.fluidazot.getInstance(), Math.min(
                                    1 + this.level,
                                    fluidTank[0].getCapacity() - fluidTank[0].getFluidAmount()
                            )),
                            true
                    );

                }
                if (this.world.provider.getWorldTime() % 60 == 0) {
                    if (fluidTank[1].getFluidAmount() + 1 <= fluidTank[1].getCapacity()) {
                        fluidTank[1].fill(new FluidStack(FluidName.fluidoxy.getInstance(), Math.min(
                                1 + this.level,
                                fluidTank[1].getCapacity() - fluidTank[1].getFluidAmount()
                        )), true);
                        work = true;
                    }
                }
                if (this.world.provider.getWorldTime() % 120 == 0) {
                    if (fluidTank[2].getFluidAmount() + 1 <= fluidTank[2].getCapacity()) {
                        fluidTank[2].fill(new FluidStack(FluidName.fluidco2.getInstance(), Math.min(
                                1 + this.level,
                                fluidTank[2].getCapacity() - fluidTank[2].getFluidAmount()
                        )), true);
                        work = true;
                    }
                }
                this.energy.useEnergy(5 + 5 * this.level);
            }
        }
        if (!work) {
            this.initiate(2);
        } else {
            this.initiate(0);
        }
        if (this.upgradeSlot.tickNoMark()) {
            this.setUpgradestat();
        }
    }

    public void setUpgradestat() {
        this.energy.setSinkTier(this.tier + this.upgradeSlot.extraTier);
    }


    @Override

    public Set<UpgradableProperty> getUpgradableProperties() {
        return EnumSet.of(UpgradableProperty.Transformer,
                UpgradableProperty.ItemConsuming, UpgradableProperty.ItemProducing, UpgradableProperty.FluidProducing
        );
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

    @Override
    public SoundEvent getSound() {
        return EnumSound.air_collector.getSoundEvent();
    }

}
