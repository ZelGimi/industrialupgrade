package com.denfop.tiles.mechanism.exp;


import com.denfop.IUItem;
import com.denfop.Localization;
import com.denfop.api.audio.EnumTypeAudio;
import com.denfop.api.audio.IAudioFixer;
import com.denfop.api.inv.IAdvInventory;
import com.denfop.api.sytem.EnergyType;
import com.denfop.api.tile.IMultiTileBlock;
import com.denfop.audio.EnumSound;
import com.denfop.blocks.BlockResource;
import com.denfop.blocks.BlockTileEntity;
import com.denfop.blocks.mechanism.BlockBaseMachine2;
import com.denfop.componets.ComponentBaseEnergy;
import com.denfop.container.ContainerBase;
import com.denfop.container.ContainerStorageExp;
import com.denfop.gui.GuiCore;
import com.denfop.gui.GuiStorageExp;
import com.denfop.invslot.InvSlotExpStorage;
import com.denfop.network.DecoderHandler;
import com.denfop.network.EncoderHandler;
import com.denfop.network.IUpdatableTileEvent;
import com.denfop.network.packet.CustomPacketBuffer;
import com.denfop.network.packet.PacketStopSound;
import com.denfop.tiles.base.TileEntityInventory;
import com.denfop.utils.ExperienceUtils;
import com.denfop.utils.ModUtils;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

import java.io.IOException;
import java.util.List;

public class TileStorageExp extends TileEntityInventory implements
        IUpdatableTileEvent, IAudioFixer {

    public final InvSlotExpStorage inputSlot;
    public final ComponentBaseEnergy energy;
    public int expirencelevel;
    public int expirencelevel1;
    public EnumTypeAudio typeAudio = EnumTypeAudio.OFF;
    public EnumTypeAudio[] valuesAudio = EnumTypeAudio.values();
    private double energy1;
    private boolean sound = true;

    public TileStorageExp(BlockPos pos, BlockState state) {
        super(BlockBaseMachine2.expierence_block, pos, state);
        this.inputSlot = new InvSlotExpStorage(this);
        this.energy = this.addComponent(new ComponentBaseEnergy(EnergyType.EXPERIENCE, this, 4000000000d, ModUtils.allFacings,
                ModUtils.allFacings, 14
        ));
        this.energy.setDirections(ModUtils.allFacings, ModUtils.allFacings);
    }

    @Override
    public void readFromNBT(final CompoundTag nbttagcompound) {
        super.readFromNBT(nbttagcompound);
        this.sound = nbttagcompound.getBoolean("sound");

    }

    @Override
    public void readContainerPacket(final CustomPacketBuffer customPacketBuffer) {
        super.readContainerPacket(customPacketBuffer);
        try {
            expirencelevel = (int) DecoderHandler.decode(customPacketBuffer);
            expirencelevel1 = (int) DecoderHandler.decode(customPacketBuffer);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

    }

    @Override
    public CustomPacketBuffer writeContainerPacket() {
        final CustomPacketBuffer packet = super.writeContainerPacket();
        try {
            EncoderHandler.encode(packet, expirencelevel);
            EncoderHandler.encode(packet, expirencelevel1);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        return packet;
    }

    public CompoundTag writeToNBT(CompoundTag nbttagcompound) {
        super.writeToNBT(nbttagcompound);
        nbttagcompound.putBoolean("sound", this.sound);
        return nbttagcompound;
    }

    public EnumTypeAudio getTypeAudio() {
        return typeAudio;
    }

    public void setType(EnumTypeAudio type) {
        typeAudio = type;
    }

    public IMultiTileBlock getTeBlock() {
        return BlockBaseMachine2.expierence_block;
    }

    public BlockTileEntity getBlock() {
        return IUItem.basemachine1.getBlock(this.getTeBlock().getId());
    }

    @Override
    @OnlyIn(Dist.CLIENT)
    public void addInformation(final ItemStack stack, final List<String> tooltip) {
        final CompoundTag nbt = ModUtils.nbt(stack);
        final double energy1 = nbt.getDouble("energy");
        if (energy1 != 0) {
            tooltip.add(Localization.translate("iu.item.tooltip.Store") + " " + ModUtils.getString(energy1) + "/" + ModUtils.getString(
                    energy.getCapacity())
                    + " EXP");
        }
    }

    public ItemStack adjustDrop(ItemStack drop, boolean wrench) {
        if (!wrench) {
            switch (this.teBlock.getDefaultDrop()) {
                case Self:
                default:
                    final ComponentBaseEnergy component2 = this.energy;
                    if (component2 != null) {
                        if (component2.getEnergy() != 0) {
                            final CompoundTag nbt = ModUtils.nbt(drop);
                            nbt.putDouble("energy", component2.getEnergy());
                        }
                    }
                    return drop;
                case None:
                    return null;
                case Generator:
                    return new ItemStack(IUItem.basemachine2.getItem(78), 1);
                case Machine:
                    return IUItem.blockResource.getItemStack(BlockResource.Type.machine);
                case AdvMachine:
                    return IUItem.blockResource.getItemStack(BlockResource.Type.advanced_machine);
            }
        }

        final ComponentBaseEnergy component2 = this.energy;
        if (component2 != null) {
            if (component2.getEnergy() != 0) {
                final CompoundTag nbt = ModUtils.nbt(drop);
                nbt.putDouble("energy", component2.getEnergy());
            }
        }

        return drop;
    }

    public void onPlaced(final ItemStack stack, final LivingEntity placer, final Direction facing) {
        super.onPlaced(stack, placer, facing);
        final CompoundTag nbt = ModUtils.nbt(stack);
        final double energy1 = nbt.getDouble("energy");
        if (energy1 != 0) {
            this.energy.addEnergy(energy1);
        }
    }

    @Override
    public boolean getEnable() {
        return this.sound;
    }


    @OnlyIn(Dist.CLIENT)
    public GuiCore<ContainerBase<? extends IAdvInventory>> getGui(Player entityPlayer, ContainerBase<? extends IAdvInventory> isAdmin) {
        return new GuiStorageExp((ContainerStorageExp) isAdmin);
    }

    public ContainerStorageExp getGuiContainer(Player entityPlayer) {
        return new ContainerStorageExp(entityPlayer, this);
    }


    @Override
    public void updateEntityServer() {
        super.updateEntityServer();
        if (energy1 != this.energy.getEnergy()) {
            this.expirencelevel = ExperienceUtils.getLevelForExperience((int) Math.min(this.energy.getEnergy(), 2000000000));
            this.expirencelevel1 =
                    ExperienceUtils.getLevelForExperience((int) Math.min(
                            Math.min(this.energy.getEnergy() - 2000000000, 0),
                            2000000000
                    ));
            this.energy1 = this.energy.getEnergy();
        }

    }

    public void onUnloaded() {
        super.onUnloaded();


    }

    public void initiate(int soundEvent) {
        if (this.getTypeAudio() == valuesAudio[soundEvent % valuesAudio.length]) {
            return;
        }

        setType(valuesAudio[soundEvent % valuesAudio.length]);
        if (!getEnable()) {
            return;
        }
        if (getSound() == null) {
            return;
        }
        if (soundEvent == 0) {
            this.getWorld().playSound(null, this.pos, getSound(), SoundSource.BLOCKS, 1F, 1);
        } else if (soundEvent == 1) {
            new PacketStopSound(getWorld(), this.pos);
            this.getWorld().playSound(null, this.pos, EnumSound.zab.getSoundEvent(), SoundSource.BLOCKS, 1F, 1);
        } else {
            new PacketStopSound(getWorld(), this.pos);
        }
    }

    @Override
    public void onLoaded() {
        super.onLoaded();
        this.expirencelevel = ExperienceUtils.getLevelForExperience((int) Math.min(this.energy.getEnergy(), 2000000000));
        this.expirencelevel1 =
                ExperienceUtils.getLevelForExperience((int) Math.min(
                        Math.min(this.energy.getEnergy() - 2000000000, 0),
                        2000000000
                ));
        this.energy1 = this.energy.getEnergy();
    }

    @Override
    public SoundEvent getSound() {
        return EnumSound.zap.getSoundEvent();
    }

    @Override
    public void updateTileServer(Player player, double event) {
        // 0 убрать с меха опыт
        // 1 добавить в мех опыт
        if (event == 1) {
            if (this.energy.getEnergy() < this.energy.getCapacity()) {
                this.energy.buffer.storage = ExperienceUtils.removePlayerXP(player, this.energy.getCapacity(), this.energy.getEnergy());
                initiate(1);
            }

        }
        if (event == 0) {
            if (this.energy.getEnergy() > 0) {
                int temp = 0;
                if (this.energy.getEnergy() > 2000000000) {
                    temp = (int) (this.energy.getEnergy() - 2000000000);
                }
                this.energy.buffer.storage = ExperienceUtils.addPlayerXP1(player, (int) Math.min(this.energy.getEnergy(), 2000000000));
                this.energy.addEnergy(temp);
                initiate(0);
            }
        }
        this.expirencelevel = ExperienceUtils.getLevelForExperience((int) Math.min(this.energy.getEnergy(), 2000000000));
        this.expirencelevel1 =
                ExperienceUtils.getLevelForExperience((int) Math.min(
                        Math.max(this.energy.getEnergy() - 2000000000, 0),
                        2000000000
                ));

        this.energy1 = this.energy.getEnergy();
    }


}
