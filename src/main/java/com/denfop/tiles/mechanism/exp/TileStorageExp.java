package com.denfop.tiles.mechanism.exp;


import com.denfop.IUItem;
import com.denfop.Localization;
import com.denfop.api.audio.EnumTypeAudio;
import com.denfop.api.audio.IAudioFixer;
import com.denfop.api.sytem.EnergyType;
import com.denfop.api.tile.IMultiTileBlock;
import com.denfop.audio.EnumSound;
import com.denfop.blocks.BlockResource;
import com.denfop.blocks.BlockTileEntity;
import com.denfop.blocks.mechanism.BlockBaseMachine2;
import com.denfop.componets.ComponentBaseEnergy;
import com.denfop.container.ContainerStorageExp;
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
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.SoundEvent;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

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

    public TileStorageExp() {
        this.inputSlot = new InvSlotExpStorage(this);
        this.energy = this.addComponent(ComponentBaseEnergy.asBasicSink(EnergyType.EXPERIENCE, this, 2000000000, 14));

    }

    @Override
    public void readFromNBT(final NBTTagCompound nbttagcompound) {
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

    public NBTTagCompound writeToNBT(NBTTagCompound nbttagcompound) {
        super.writeToNBT(nbttagcompound);
        nbttagcompound.setBoolean("sound", this.sound);
        return nbttagcompound;
    }

    public EnumTypeAudio getType() {
        return typeAudio;
    }

    public void setType(EnumTypeAudio type) {
        typeAudio = type;
    }

    public IMultiTileBlock getTeBlock() {
        return BlockBaseMachine2.expierence_block;
    }

    public BlockTileEntity getBlock() {
        return IUItem.basemachine1;
    }

    @Override
    @SideOnly(Side.CLIENT)
    public void addInformation(final ItemStack stack, final List<String> tooltip, final ITooltipFlag advanced) {
        final NBTTagCompound nbt = ModUtils.nbt(stack);
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
                            final NBTTagCompound nbt = ModUtils.nbt(drop);
                            nbt.setDouble("energy", component2.getEnergy());
                        }
                    }
                    return drop;
                case None:
                    return null;
                case Generator:
                    return new ItemStack(IUItem.basemachine2, 1, 78);
                case Machine:
                    return IUItem.blockResource.getItemStack(BlockResource.Type.machine);
                case AdvMachine:
                    return IUItem.blockResource.getItemStack(BlockResource.Type.advanced_machine);
            }
        }

        final ComponentBaseEnergy component2 = this.energy;
        if (component2 != null) {
            if (component2.getEnergy() != 0) {
                final NBTTagCompound nbt = ModUtils.nbt(drop);
                nbt.setDouble("energy", component2.getEnergy());
            }
        }

        return drop;
    }

    public void onPlaced(final ItemStack stack, final EntityLivingBase placer, final EnumFacing facing) {
        super.onPlaced(stack, placer, facing);
        final NBTTagCompound nbt = ModUtils.nbt(stack);
        final double energy1 = nbt.getDouble("energy");
        if (energy1 != 0) {
            this.energy.addEnergy(energy1);
        }
    }

    @Override
    public boolean getEnable() {
        return this.sound;
    }

    public String getStartSoundFile() {
        return "Machines/zab.ogg";
    }

    public String getInterruptSoundFile() {
        return "Machines/zap.ogg";
    }


    @SideOnly(Side.CLIENT)
    public GuiScreen getGui(EntityPlayer entityPlayer, boolean isAdmin) {
        return new GuiStorageExp(new ContainerStorageExp(entityPlayer, this));
    }

    public ContainerStorageExp getGuiContainer(EntityPlayer entityPlayer) {
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
        if (this.getType() == valuesAudio[soundEvent % valuesAudio.length]) {
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
            this.getWorld().playSound(null, this.pos, getSound(), SoundCategory.BLOCKS, 1F, 1);
        } else if (soundEvent == 1) {
            new PacketStopSound(getWorld(), this.pos);
            this.getWorld().playSound(null, this.pos, EnumSound.zab.getSoundEvent(), SoundCategory.BLOCKS, 1F, 1);
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
    public void updateTileServer(EntityPlayer player, double event) {
        // 0 убрать с меха опыт
        // 1 добавить в мех опыт
        if (event == 1) {
            if (this.energy.getEnergy() < this.energy.getCapacity()) {
                this.energy.storage = ExperienceUtils.removePlayerXP(player, this.energy.getCapacity(), this.energy.getEnergy());
                initiate(1);
            }

        }
        if (event == 0) {
            if (this.energy.getEnergy() > 0) {
                int temp = 0;
                if (this.energy.getEnergy() > 2000000000) {
                    temp = (int) (this.energy.getEnergy() - 2000000000);
                }
                this.energy.storage = ExperienceUtils.addPlayerXP1(player, (int) Math.min(this.energy.getEnergy(), 2000000000));
                this.energy.addEnergy(temp);
                initiate(0);
            }
        }
        this.expirencelevel = ExperienceUtils.getLevelForExperience((int) Math.min(this.energy.getEnergy(), 2000000000));
        this.expirencelevel1 =
                ExperienceUtils.getLevelForExperience((int) Math.min(
                        Math.min(this.energy.getEnergy() - 2000000000, 0),
                        2000000000
                ));

        this.energy1 = this.energy.getEnergy();
    }


}
