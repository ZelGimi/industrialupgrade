package com.denfop.blockentity.mechanism.generator.things.fluid;

import com.denfop.IUItem;
import com.denfop.api.blockentity.MultiBlockEntity;
import com.denfop.api.container.CustomWorldContainer;
import com.denfop.api.pollution.PollutionManager;
import com.denfop.api.pollution.component.ChunkLevel;
import com.denfop.api.upgrades.IUpgradableBlock;
import com.denfop.api.upgrades.UpgradableProperty;
import com.denfop.blockentity.base.BlockEntityElectricMachine;
import com.denfop.blockentity.base.IManufacturerBlock;
import com.denfop.blocks.BlockTileEntity;
import com.denfop.blocks.FluidName;
import com.denfop.blocks.mechanism.BlockBaseMachine3Entity;
import com.denfop.componets.Fluids;
import com.denfop.componets.SoilPollutionComponent;
import com.denfop.containermenu.ContainerMenuAirCollector;
import com.denfop.containermenu.ContainerMenuBase;
import com.denfop.inventory.Inventory;
import com.denfop.inventory.InventoryDrainTank;
import com.denfop.inventory.InventoryFluid;
import com.denfop.inventory.InventoryUpgrade;
import com.denfop.network.DecoderHandler;
import com.denfop.network.EncoderHandler;
import com.denfop.network.packet.CustomPacketBuffer;
import com.denfop.network.packet.PacketUpdateFieldTile;
import com.denfop.screen.ScreenAirCollector;
import com.denfop.screen.ScreenIndustrialUpgrade;
import com.denfop.sound.EnumSound;
import com.denfop.utils.Keyboard;
import com.denfop.utils.Localization;
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
import net.neoforged.api.distmarker.Dist;
import net.neoforged.api.distmarker.OnlyIn;
import net.neoforged.neoforge.fluids.FluidStack;
import net.neoforged.neoforge.fluids.capability.IFluidHandler;
import net.neoforged.neoforge.fluids.capability.templates.FluidTank;

import java.io.IOException;
import java.util.EnumSet;
import java.util.List;
import java.util.Set;

public class BlockEntityAirCollector extends BlockEntityElectricMachine implements IUpgradableBlock, IManufacturerBlock {

    public final Fluids fluids;
    public final InventoryUpgrade upgradeSlot;
    public final InventoryDrainTank[] containerslot;
    public FluidTank[] fluidTank;
    private int levelBlock;
    private boolean work;
    private ChunkPos chunkpos;

    public BlockEntityAirCollector(BlockPos pos, BlockState state) {
        super(5000, 1, 3, BlockBaseMachine3Entity.aircollector, pos, state);
        this.fluidTank = new FluidTank[3];
        this.fluids = this.addComponent(new Fluids(this));
        Fluid[] name1 = new Fluid[]{FluidName.fluidnitrogen.getInstance().get(), FluidName.fluidoxygen.getInstance().get(),
                FluidName.fluidcarbondioxide.getInstance().get()};
        for (int i = 0; i < fluidTank.length; i++) {

            this.fluidTank[i] = this.fluids.addTank("fluidTank" + i, 10000, Inventory.TypeItemSlot.OUTPUT,
                    Fluids.fluidPredicate(name1[i])
            );

        }
        this.containerslot = new InventoryDrainTank[name1.length];
        for (int i = 0; i < name1.length; i++) {
            this.containerslot[i] = new InventoryDrainTank(this, Inventory.TypeItemSlot.INPUT, 1,
                    InventoryFluid.TypeFluidSlot.OUTPUT, name1[i]
            );
        }
        this.upgradeSlot = new InventoryUpgrade(this, 4);
        this.levelBlock = 0;
        this.pollutionSoil = this.addComponent(new SoilPollutionComponent(this, 0.1));

    }

    @Override
    public void onBlockBreak(boolean wrench) {
        for (int i = this.pos.getX() - 5; i <= this.pos.getX() + 5; i++) {
            for (int j = this.pos.getY() - 5; j <= this.pos.getY() + 5; j++) {
                for (int k = this.pos.getZ() - 5; k <= this.pos.getZ() + 5; k++) {
                    final BlockEntity tile = this.getWorld().getBlockEntity(new BlockPos(i, j, k));
                    if (tile instanceof BlockEntityAirCollector) {
                        ((BlockEntityAirCollector) tile).update_collector(this.pos);
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
                    if (tile instanceof BlockEntityAirCollector) {
                        this.work = false;
                        ((BlockEntityAirCollector) tile).work = false;
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
                    if (tile instanceof BlockEntityAirCollector) {
                        this.work = false;
                        ((BlockEntityAirCollector) tile).work = false;
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

    public MultiBlockEntity getTeBlock() {
        return BlockBaseMachine3Entity.aircollector;
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
                    FluidName.fluidnitrogen.getInstance().get(),
                    1
            ).getDisplayName().getString());
            tooltip.add(Localization.translate("iu.aircollector.info", 60) + new FluidStack(
                    FluidName.fluidoxygen.getInstance().get(),
                    1
            ).getDisplayName().getString());
            tooltip.add(Localization.translate("iu.aircollector.info", 120) + new FluidStack(
                    FluidName.fluidcarbondioxide.getInstance().get(),
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
    public ContainerMenuAirCollector getGuiContainer(final Player entityPlayer) {
        return new ContainerMenuAirCollector(entityPlayer, this);
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
    public ScreenIndustrialUpgrade<ContainerMenuBase<? extends CustomWorldContainer>> getGui(Player var1, ContainerMenuBase<? extends CustomWorldContainer> menu) {
        return new ScreenAirCollector((ContainerMenuAirCollector) menu);
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
                        fluidTank[2].fill(new FluidStack(FluidName.fluidcarbondioxide.getInstance().get(), Math.min(
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

            for (InventoryDrainTank slot : this.containerslot) {
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
                            new FluidStack(FluidName.fluidnitrogen.getInstance().get(), Math.min(
                                    1 + this.levelBlock,
                                    fluidTank[0].getCapacity() - fluidTank[0].getFluidAmount()
                            )),
                            IFluidHandler.FluidAction.EXECUTE
                    );

                }
                if (this.level.getGameTime() % 60 == 0) {
                    if (fluidTank[1].getFluidAmount() + 1 <= fluidTank[1].getCapacity()) {
                        fluidTank[1].fill(new FluidStack(FluidName.fluidoxygen.getInstance().get(), Math.min(
                                1 + this.levelBlock,
                                fluidTank[1].getCapacity() - fluidTank[1].getFluidAmount()
                        )), IFluidHandler.FluidAction.EXECUTE);
                        work = true;
                    }
                }
                if (this.level.getGameTime() % 120 == 0) {
                    if (fluidTank[2].getFluidAmount() + 1 <= fluidTank[2].getCapacity()) {
                        fluidTank[2].fill(new FluidStack(FluidName.fluidcarbondioxide.getInstance().get(), Math.min(
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
