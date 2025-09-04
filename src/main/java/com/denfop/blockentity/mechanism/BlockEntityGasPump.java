package com.denfop.blockentity.mechanism;

import com.denfop.IUItem;
import com.denfop.api.blockentity.MultiBlockEntity;
import com.denfop.api.container.CustomWorldContainer;
import com.denfop.api.upgrades.IUpgradableBlock;
import com.denfop.api.upgrades.UpgradableProperty;
import com.denfop.api.vein.common.Type;
import com.denfop.api.vein.common.VeinBase;
import com.denfop.api.vein.common.VeinSystem;
import com.denfop.blockentity.base.BlockEntityElectricLiquidTankInventory;
import com.denfop.blockentity.base.IManufacturerBlock;
import com.denfop.blocks.BlockTileEntity;
import com.denfop.blocks.FluidName;
import com.denfop.blocks.mechanism.BlockBaseMachine3Entity;
import com.denfop.blocks.state.DefaultDrop;
import com.denfop.componets.Fluids;
import com.denfop.containermenu.ContainerMenuBase;
import com.denfop.containermenu.ContainerMenuGasPump;
import com.denfop.datacomponent.DataComponentsInit;
import com.denfop.inventory.Inventory;
import com.denfop.inventory.InventoryFluid;
import com.denfop.inventory.InventoryFluidByList;
import com.denfop.inventory.InventoryUpgrade;
import com.denfop.network.DecoderHandler;
import com.denfop.network.EncoderHandler;
import com.denfop.network.packet.CustomPacketBuffer;
import com.denfop.network.packet.PacketUpdateFieldTile;
import com.denfop.screen.ScreenGasPump;
import com.denfop.screen.ScreenIndustrialUpgrade;
import com.denfop.sound.EnumSound;
import com.denfop.utils.Keyboard;
import com.denfop.utils.Localization;
import com.denfop.utils.ModUtils;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.Vec3;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.api.distmarker.OnlyIn;
import net.neoforged.neoforge.fluids.FluidStack;
import net.neoforged.neoforge.fluids.capability.IFluidHandler;
import net.neoforged.neoforge.fluids.capability.templates.FluidTank;
import org.apache.commons.lang3.mutable.MutableObject;

import java.io.IOException;
import java.util.EnumSet;
import java.util.List;
import java.util.Set;

public class BlockEntityGasPump extends BlockEntityElectricLiquidTankInventory implements IUpgradableBlock, IManufacturerBlock {


    public final int defaultTier;
    public final InventoryUpgrade upgradeSlot;
    public final InventoryFluid containerslot;
    public int levelBlock;
    public boolean find;
    public int count;
    public int maxcount;
    public VeinBase vein;
    public int type;

    public BlockEntityGasPump(BlockPos pos, BlockState state) {
        super(50000, 14, 20, Fluids.fluidPredicate(FluidName.fluidgas.getInstance().get()), BlockBaseMachine3Entity.gas_pump, pos, state);
        this.containerslot = new InventoryFluidByList(this,
                Inventory.TypeItemSlot.INPUT, 1, InventoryFluid.TypeFluidSlot.OUTPUT,
                FluidName.fluidgas.getInstance().get()
        );
        this.upgradeSlot = new InventoryUpgrade(this, 4);
        this.defaultTier = 14;
        this.levelBlock = 0;

        fluidTank.setTypeItemSlot(Inventory.TypeItemSlot.INPUT);
        this.fluidTank.setTypeItemSlot(Inventory.TypeItemSlot.OUTPUT);
    }

    private static int applyModifier(int extra) {
        double ret = (double) Math.round(((double) 14 + (double) extra));
        return ret > 2.147483647E9D ? 2147483647 : (int) ret;
    }

    public MultiBlockEntity getTeBlock() {
        return BlockBaseMachine3Entity.gas_pump;
    }

    public BlockTileEntity getBlock() {
        return IUItem.basemachine2.getBlock(BlockBaseMachine3Entity.gas_pump);
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
    public void addInformation(final ItemStack stack, final List<String> tooltip) {
        if (!Keyboard.isKeyDown(Keyboard.KEY_LSHIFT)) {
            tooltip.add(Localization.translate("press.lshift"));
        }
        if (Keyboard.isKeyDown(Keyboard.KEY_LSHIFT)) {
            tooltip.add(Localization.translate("iu.machines_work_energy") + 2 + Localization.translate("iu" +
                    ".machines_work_energy_type_eu"));
        }
        super.addInformation(stack, tooltip);
    }

    @Override
    public CompoundTag writeToNBT(final CompoundTag nbttagcompound) {
        super.writeToNBT(nbttagcompound);
        nbttagcompound.putInt("level", this.levelBlock);
        nbttagcompound.putBoolean("find", this.find);
        return nbttagcompound;
    }

    @Override
    public void readFromNBT(final CompoundTag nbttagcompound) {
        super.readFromNBT(nbttagcompound);
        this.levelBlock = nbttagcompound.getInt("level");
        this.find = nbttagcompound.getBoolean("find");
    }

    @Override
    public void readContainerPacket(final CustomPacketBuffer customPacketBuffer) {
        super.readContainerPacket(customPacketBuffer);
        try {
            count = (int) DecoderHandler.decode(customPacketBuffer);
            find = (boolean) DecoderHandler.decode(customPacketBuffer);
            maxcount = (int) DecoderHandler.decode(customPacketBuffer);
            levelBlock = (int) DecoderHandler.decode(customPacketBuffer);
            type = (int) DecoderHandler.decode(customPacketBuffer);
            vein = (VeinBase) DecoderHandler.decode(customPacketBuffer);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

    }

    @Override
    public CustomPacketBuffer writeContainerPacket() {
        final CustomPacketBuffer packet = super.writeContainerPacket();
        try {
            EncoderHandler.encode(packet, count);
            EncoderHandler.encode(packet, find);
            EncoderHandler.encode(packet, maxcount);
            EncoderHandler.encode(packet, levelBlock);
            EncoderHandler.encode(packet, type);
            EncoderHandler.encode(packet, vein);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        return packet;
    }

    public VeinBase getVein() {
        return vein;
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

    public List<ItemStack> getWrenchDrops(Player player, int fortune) {
        List<ItemStack> ret = super.getWrenchDrops(player, fortune);
        if (this.levelBlock != 0) {
            ret.add(new ItemStack(IUItem.upgrade_speed_creation.getItem(), this.levelBlock));
            this.levelBlock = 0;
        }
        return ret;
    }

    private void updateTileEntityField() {
        new PacketUpdateFieldTile(this, "vein", vein);
    }

    public ItemStack adjustDrop(ItemStack drop, boolean wrench) {
        drop = super.adjustDrop(drop, wrench);
        if (drop.is(this.getPickBlock(
                null,
                null
        ).getItem()) && (wrench || this.teBlock.getDefaultDrop() == DefaultDrop.Self)) {
            CompoundTag nbt = ModUtils.nbt(drop);
            if (this.fluidTank.getFluidAmount() > 0) {
                nbt.put("fluid", this.fluidTank.getFluid().save(level.registryAccess(), new CompoundTag()));
            }
        }
        return drop;
    }

    @Override
    public void onPlaced(final ItemStack stack, final LivingEntity placer, final Direction facing) {
        super.onPlaced(stack, placer, facing);
        if (this.level.isClientSide) {
            return;
        }
        this.vein = VeinSystem.system.getVein(this.getWorld().getChunk(this.pos).getPos());
        if (this.vein != VeinSystem.system.getEMPTY()) {
            this.find = this.vein.get();
            this.count = this.vein.getCol();
            this.maxcount = this.vein.getMaxCol();
            this.type = this.vein.getType().ordinal();
        }
        if (stack.has(DataComponentsInit.DATA) && stack.get(DataComponentsInit.DATA).contains("fluid")) {
            FluidStack fluidStack = FluidStack.parseOptional(level.registryAccess(), (CompoundTag) stack.get(DataComponentsInit.DATA).get("fluid"));
            if (fluidStack != null) {
                this.fluidTank.fill(fluidStack, IFluidHandler.FluidAction.EXECUTE);
            }
            new PacketUpdateFieldTile(this, "fluidTank", fluidTank);
        }
        updateTileEntityField();
    }

    public void updateField(String name, CustomPacketBuffer is) {

        if (name.equals("fluidTank")) {
            try {
                FluidTank fluidTank = (FluidTank) DecoderHandler.decode(is);
                this.fluidTank.readFromNBT(is.registryAccess(), fluidTank.writeToNBT(is.registryAccess(), new CompoundTag()));
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }
        if (name.equals("vein")) {
            try {
                vein = (VeinBase) DecoderHandler.decode(is);

            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }

        super.updateField(name, is);
    }

    @Override
    public void onLoaded() {
        super.onLoaded();
        if (this.level.isClientSide) {
            return;
        }
        this.vein = VeinSystem.system.getVein(this.getWorld().getChunk(this.pos).getPos());
        if (this.vein != VeinSystem.system.getEMPTY()) {
            boolean find = this.vein.get();
            if (this.find != find) {
                this.vein.setFind(this.find);
            }
            this.count = this.vein.getCol();
            this.maxcount = this.vein.getMaxCol();
            this.type = this.vein.getType().ordinal();
        }
        updateTileEntityField();
    }


    public void updateEntityServer() {
        super.updateEntityServer();
        if (this.vein == null || this.vein.getType() != Type.GAS) {
            return;
        }

        MutableObject<ItemStack> output = new MutableObject<>();
        if (this.containerslot.transferFromTank(this.fluidTank, output, true)
                && (output.getValue() == null || this.outputSlot.canAdd(output.getValue()))) {
            this.containerslot.transferFromTank(this.fluidTank, output, false);

            if (output.getValue() != null) {
                this.outputSlot.add(output.getValue());
            }
        }
        if (this.energy.getEnergy() >= 2 && this.find) {
            getGas();
            if (!this.getActive()) {
                this.setActive(true);
                initiate(0);
            }
        } else {
            if (this.getActive()) {
                this.setActive(false);
                initiate(2);
            }
        }


    }

    private void getGas() {
        if (vein.getCol() >= 1) {
            int size = Math.min(this.levelBlock + 1, vein.getCol());
            size = Math.min(size, this.fluidTank.getCapacity() - this.fluidTank.getFluidAmount());
            if (this.fluidTank.getFluidAmount() + size <= this.fluidTank.getCapacity()) {
                this.fluidTank.fill(new FluidStack(FluidName.fluidgas.getInstance().get(), size), IFluidHandler.FluidAction.EXECUTE);
                vein.removeCol(size);
                this.count = vein.getCol();
                this.energy.useEnergy(2);
                updateTileEntityField();
                if (this.upgradeSlot.tickNoMark()) {
                    setUpgradestat();
                }
            }
        }
    }


    public void setChanged() {
        super.setChanged();
        if (!level.isClientSide) {
            setUpgradestat();
        }
    }

    public void setUpgradestat() {
        this.energy.setSinkTier(applyModifier(this.upgradeSlot.extraTier));
    }

    public double getEnergy() {
        return this.energy.getEnergy();
    }

    public boolean useEnergy(double amount) {
        if (this.energy.getEnergy() >= amount) {
            this.energy.useEnergy(amount);
            return true;
        }
        return false;
    }

    public String getStartSoundFile() {
        return "Machines/oilgetter.ogg";
    }

    public String getInterruptSoundFile() {
        return "Machines/InterruptOne.ogg";
    }


    @Override
    public ContainerMenuGasPump getGuiContainer(Player entityPlayer) {
        return new ContainerMenuGasPump(entityPlayer, this);
    }

    @Override
    @OnlyIn(Dist.CLIENT)
    public ScreenIndustrialUpgrade<ContainerMenuBase<? extends CustomWorldContainer>> getGui(Player var1, ContainerMenuBase<? extends CustomWorldContainer> menu) {
        return new ScreenGasPump((ContainerMenuGasPump) menu);
    }


    @Override
    public Set<UpgradableProperty> getUpgradableProperties() {
        return EnumSet.of(UpgradableProperty.Transformer, UpgradableProperty.FluidExtract
        );
    }


    @Override
    public SoundEvent getSound() {
        return EnumSound.oilgetter.getSoundEvent();
    }

}
