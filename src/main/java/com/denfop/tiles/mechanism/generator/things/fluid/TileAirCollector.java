package com.denfop.tiles.mechanism.generator.things.fluid;

import com.denfop.IUItem;
import com.denfop.Localization;
import com.denfop.api.inv.IAdvInventory;
import com.denfop.api.pollution.ChunkLevel;
import com.denfop.api.pollution.PollutionManager;
import com.denfop.api.tile.IMultiTileBlock;
import com.denfop.api.upgrades.IUpgradableBlock;
import com.denfop.api.upgrades.UpgradableProperty;
import com.denfop.audio.EnumSound;
import com.denfop.blocks.BlockTileEntity;
import com.denfop.blocks.FluidName;
import com.denfop.blocks.mechanism.BlockBaseMachine3;
import com.denfop.componets.Fluids;
import com.denfop.componets.SoilPollutionComponent;
import com.denfop.container.ContainerAirCollector;
import com.denfop.container.ContainerBase;
import com.denfop.gui.GuiAirCollector;
import com.denfop.gui.GuiCore;
import com.denfop.invslot.InvSlot;
import com.denfop.invslot.InvSlotDrainTank;
import com.denfop.invslot.InvSlotFluid;
import com.denfop.invslot.InvSlotUpgrade;
import com.denfop.network.DecoderHandler;
import com.denfop.network.EncoderHandler;
import com.denfop.network.packet.CustomPacketBuffer;
import com.denfop.network.packet.PacketUpdateFieldTile;
import com.denfop.tiles.base.IManufacturerBlock;
import com.denfop.tiles.base.TileElectricMachine;
import com.denfop.utils.Keyboard;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.ChunkPos;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.material.Fluid;
import net.minecraft.world.phys.Vec3;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.fluids.capability.IFluidHandler;
import net.minecraftforge.fluids.capability.templates.FluidTank;

import java.io.IOException;
import java.util.EnumSet;
import java.util.List;
import java.util.Set;

public class TileAirCollector extends TileElectricMachine implements IUpgradableBlock, IManufacturerBlock {

    public final Fluids fluids;
    public final InvSlotUpgrade upgradeSlot;
    public final InvSlotDrainTank[] containerslot;
    public FluidTank[] fluidTank;
    private int levelBlock;
    private boolean work;
    private ChunkPos chunkpos;

    public TileAirCollector(BlockPos pos, BlockState state) {
        super(5000, 1, 3,BlockBaseMachine3.aircollector,pos,state);
        this.fluidTank = new FluidTank[3];
        this.fluids = this.addComponent(new Fluids(this));
        Fluid[] name1 = new Fluid[]{FluidName.fluidazot.getInstance().get(), FluidName.fluidoxy.getInstance().get(),
                FluidName.fluidco2.getInstance().get()};
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
        this.levelBlock = 0;
        this.pollutionSoil = this.addComponent(new SoilPollutionComponent(this, 0.1));

    }

    @Override
    public void onBlockBreak(boolean wrench) {
        for (int i = this.pos.getX() - 5; i <= this.pos.getX() + 5; i++) {
            for (int j = this.pos.getY() - 5; j <= this.pos.getY() + 5; j++) {
                for (int k = this.pos.getZ() - 5; k <= this.pos.getZ() + 5; k++) {
                    final BlockEntity tile = this.getWorld().getBlockEntity(new BlockPos(i, j, k));
                    if (tile instanceof TileAirCollector) {
                        ((TileAirCollector) tile).update_collector(this.pos);
                    }
                }
            }
        }
        super.onBlockBreak(wrench);
    }

    public void update_collector() {
        this.work = true;
        for (int i = this.pos.getX() - 5; i <= this.pos.getX() + 5; i++) {
            for (int j = this.pos.getY() - 5; j <= this.pos.getY() + 5; j++) {
                for (int k = this.pos.getZ() - 5; k <= this.pos.getZ() + 5; k++) {

                    if (pos.getX() == i && pos.getY() == j && pos.getZ() == k) {
                        continue;
                    }
                    final BlockEntity tile = this.getWorld().getBlockEntity(new BlockPos(i, j, k));
                    if (tile instanceof TileAirCollector) {
                        this.work = false;
                        ((TileAirCollector) tile).work = false;
                    }
                }
            }
        }
        new PacketUpdateFieldTile(this, "work", this.work);
    }

    public void update_collector(BlockPos pos) {
        this.work = true;
        for (int i = this.pos.getX() - 8; i <= this.pos.getX() + 8; i++) {
            for (int j = this.pos.getY() - 8; j <= this.pos.getY() + 8; j++) {
                for (int k = this.pos.getZ() - 8; k <= this.pos.getZ() + 8; k++) {
                    if (this.pos.getX() == i && this.pos.getY() == j && this.pos.getZ() == k) {
                        continue;
                    }
                    if (pos.getX() == i && pos.getY() == j && pos.getZ() == k) {
                        continue;
                    }
                    final BlockEntity tile = this.getWorld().getBlockEntity(new BlockPos(i, j, k));
                    if (tile instanceof TileAirCollector) {
                        this.work = false;
                        ((TileAirCollector) tile).work = false;
                    }
                }
            }
        }
        new PacketUpdateFieldTile(this, "work", this.work);
    }

    @Override
    public void updateField(final String name, final CustomPacketBuffer is) {
        super.updateField(name, is);
        if (name.equals("work")) {
            try {
                this.work = (boolean) DecoderHandler.decode(is);
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }
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
        return IUItem.basemachine2.getBlock(getTeBlock());
    }


    public void addInformation(final ItemStack stack, final List<String> tooltip) {
        tooltip.add(Localization.translate("iu.air_purifier.info"));
        if (!Keyboard.isKeyDown(Keyboard.KEY_LSHIFT)) {
            tooltip.add(Localization.translate("press.lshift"));
        }
        if (Keyboard.isKeyDown(Keyboard.KEY_LSHIFT)) {
            tooltip.add(Localization.translate("iu.machines_work_energy") + 5 + Localization.translate("iu" +
                    ".machines_work_energy_type_eu"));
            tooltip.add(Localization.translate("iu.aircollector.info", 20) + new FluidStack(
                    FluidName.fluidazot.getInstance().get(),
                    1
            ).getDisplayName().getString());
            tooltip.add(Localization.translate("iu.aircollector.info", 60) + new FluidStack(
                    FluidName.fluidoxy.getInstance().get(),
                    1
            ).getDisplayName().getString());
            tooltip.add(Localization.translate("iu.aircollector.info", 120) + new FluidStack(
                    FluidName.fluidco2.getInstance().get(),
                    1
            ).getDisplayName().getString());

        }
        super.addInformation(stack, tooltip);
    }

    public List<ItemStack> getWrenchDrops(Player player, int fortune) {
        List<ItemStack> ret = super.getWrenchDrops(player, fortune);
        if (this.levelBlock != 0) {
            ret.add(new ItemStack(IUItem.upgrade_speed_creation.getItem(), this.levelBlock));
            this.levelBlock = 0;
        }
        return ret;
    }

    public FluidTank getFluidTank(int num) {
        return this.fluidTank[num];
    }

    @Override
    public ContainerAirCollector getGuiContainer(final Player entityPlayer) {
        return new ContainerAirCollector(entityPlayer, this);
    }

    @Override
    public CompoundTag writeToNBT(final CompoundTag nbttagcompound) {
        super.writeToNBT(nbttagcompound);
        nbttagcompound.putInt("level", this.levelBlock);

        return nbttagcompound;
    }

    @Override
    public void readFromNBT(final CompoundTag nbttagcompound) {
        super.readFromNBT(nbttagcompound);
        this.levelBlock = nbttagcompound.getInt("level");

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
    @OnlyIn(Dist.CLIENT)
    public GuiCore<ContainerBase<? extends IAdvInventory>> getGui(Player var1, ContainerBase<? extends IAdvInventory> menu) {
        return new GuiAirCollector((ContainerAirCollector) menu);
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
        if (!this.getWorld().isClientSide) {
            this.setUpgradestat();
            update_collector();
            this.chunkpos = new ChunkPos(this.pos);
        }

    }


    public void updateEntityServer() {
        super.updateEntityServer();
        if (this.level.getGameTime() % 60 == 0 && this.energy.getEnergy() > 5) {
            ChunkLevel chunkLevel = PollutionManager.pollutionManager.getChunkLevelAir(chunkpos);
            if (chunkLevel != null) {
                if (chunkLevel.removePollution(5)) {
                    if (fluidTank[2].getFluidAmount() + 10 <= fluidTank[2].getCapacity()) {
                        fluidTank[2].fill(new FluidStack(FluidName.fluidco2.getInstance().get(), Math.min(
                                10,
                                fluidTank[2].getCapacity() - fluidTank[2].getFluidAmount()
                        )), IFluidHandler.FluidAction.EXECUTE);
                        work = true;
                        this.energy.useEnergy(5);
                    }
                }
                ;
            }
        }
        for (FluidTank tank : fluidTank) {

            for (InvSlotDrainTank slot : this.containerslot) {
                if (tank.getFluidAmount() >= 1000 && !slot.isEmpty() && slot.acceptsLiquid(tank.getFluid().getFluid())) {
                    slot.processFromTank(tank, this.outputSlot);
                }
            }

        }
        boolean work = false;
        if (this.energy.getEnergy() > 5 + 5 * this.levelBlock) {
            if (this.level.getGameTime() % 400 == 0) {
                this.initiate(2);
            }
            work = true;
            if (this.level.getGameTime() % 20 == 0) {
                if (fluidTank[0].getFluidAmount() + 1 <= fluidTank[0].getCapacity()) {
                    fluidTank[0].fill(
                            new FluidStack(FluidName.fluidazot.getInstance().get(), Math.min(
                                    1 + this.levelBlock,
                                    fluidTank[0].getCapacity() - fluidTank[0].getFluidAmount()
                            )),
                            IFluidHandler.FluidAction.EXECUTE
                    );

                }
                if (this.level.getGameTime() % 60 == 0) {
                    if (fluidTank[1].getFluidAmount() + 1 <= fluidTank[1].getCapacity()) {
                        fluidTank[1].fill(new FluidStack(FluidName.fluidoxy.getInstance().get(), Math.min(
                                1 + this.levelBlock,
                                fluidTank[1].getCapacity() - fluidTank[1].getFluidAmount()
                        )), IFluidHandler.FluidAction.EXECUTE);
                        work = true;
                    }
                }
                if (this.level.getGameTime() % 120 == 0) {
                    if (fluidTank[2].getFluidAmount() + 1 <= fluidTank[2].getCapacity()) {
                        fluidTank[2].fill(new FluidStack(FluidName.fluidco2.getInstance().get(), Math.min(
                                1 + this.levelBlock,
                                fluidTank[2].getCapacity() - fluidTank[2].getFluidAmount()
                        )), IFluidHandler.FluidAction.EXECUTE);
                        work = true;
                    }
                }
                this.energy.useEnergy(5 + 5 * this.levelBlock);
            }
        }
        if (!work) {
            this.initiate(2);
            this.setActive(false);
        } else {
            this.initiate(0);
            this.setActive(true);
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
        return EnumSet.of(UpgradableProperty.Transformer, UpgradableProperty.FluidExtract
        );
    }

    @Override
    public int getLevelMechanism() {
        return this.levelBlock;
    }

    public void setLevelMech(final int levelBlock) {
        this.levelBlock = levelBlock;
    }

    @Override
    public void removeLevel(final int level) {
        this.levelBlock -= level;
    }

    @Override
    public SoundEvent getSound() {
        return EnumSound.air_collector.getSoundEvent();
    }

}
