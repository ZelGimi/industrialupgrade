package com.denfop.tiles.base;

import com.denfop.IUItem;
import com.denfop.Localization;
import com.denfop.api.audio.EnumTypeAudio;
import com.denfop.api.audio.IAudioFixer;
import com.denfop.api.recipe.IHasRecipe;
import com.denfop.audio.EnumSound;
import com.denfop.blocks.BlockResource;
import com.denfop.componets.AbstractComponent;
import com.denfop.componets.ComponentSteamEnergy;
import com.denfop.componets.Fluids;
import com.denfop.componets.PressureComponent;
import com.denfop.componets.SteamProcessMultiComponent;
import com.denfop.container.ContainerSteamMultiMachine;
import com.denfop.gui.GuiSteamMultiMachine;
import com.denfop.invslot.InvSlot;
import com.denfop.network.DecoderHandler;
import com.denfop.network.EncoderHandler;
import com.denfop.network.IUpdatableTileEvent;
import com.denfop.network.packet.CustomPacketBuffer;
import com.denfop.network.packet.PacketStopSound;
import com.denfop.network.packet.PacketUpdateFieldTile;
import com.denfop.tiles.mechanism.EnumTypeMachines;
import com.denfop.utils.ModUtils;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.SoundEvent;
import net.minecraftforge.fluids.FluidUtil;
import net.minecraftforge.fluids.capability.CapabilityFluidHandler;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import org.lwjgl.input.Keyboard;

import java.io.IOException;
import java.util.List;

public abstract class TileSteamMultiMachine extends TileEntityInventory implements
        IAudioFixer, IUpdatableTileEvent, IHasRecipe, ISteamMechanism {


    public final int type;
    public final SteamProcessMultiComponent multi_process;
    public final int sizeWorkingSlot;
    public final ComponentSteamEnergy steam;
    public final Fluids.InternalFluidTank fluidTank;
    public final PressureComponent pressure;
    public EnumTypeAudio typeAudio = EnumTypeAudio.OFF;
    public EnumTypeAudio[] valuesAudio = EnumTypeAudio.values();
    private boolean sound;
    private Fluids fluid = null;

    public TileSteamMultiMachine(int energyconsume, int OperationsPerTick, int type) {
        this(1, energyconsume, OperationsPerTick, type);
    }

    public TileSteamMultiMachine(
            int aDefaultTier,
            int energyconsume,
            int OperationsPerTick,
            int type
    ) {

        this.sizeWorkingSlot = this.getMachine().sizeWorkingSlot;
        this.steam = this.addComponent(ComponentSteamEnergy.asBasicSink(this, 1000));
        this.pressure = this.addComponent(PressureComponent.asBasicSink(this, 1));
        fluid = this.addComponent(new Fluids(this));
        this.fluidTank = fluid.addTank("tank", OperationsPerTick * energyconsume, InvSlot.TypeItemSlot.NONE);
        steam.setFluidTank(fluidTank);
        this.type = type;
        this.sound = true;
        this.multi_process = this.addComponent(new SteamProcessMultiComponent(this, getMachine()));
    }


    @Override
    public void readContainerPacket(final CustomPacketBuffer customPacketBuffer) {
        super.readContainerPacket(customPacketBuffer);
        try {
            sound = (boolean) DecoderHandler.decode(customPacketBuffer);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

    }

    @Override
    public CustomPacketBuffer writeContainerPacket() {
        final CustomPacketBuffer packet = super.writeContainerPacket();
        try {
            EncoderHandler.encode(packet, sound);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        return packet;
    }

    public void init() {

    }

    @Override
    public SoundEvent getSound() {
        return this.getMachine().type.getSound();
    }

    public void addInformation(ItemStack stack, List<String> tooltip) {
        if (!Keyboard.isKeyDown(Keyboard.KEY_LSHIFT)) {
            tooltip.add(Localization.translate("press.lshift"));
        }
        if (Keyboard.isKeyDown(Keyboard.KEY_LSHIFT)) {
            tooltip.add(Localization.translate("iu.machines_work_length") + this.multi_process.operationLength);
        }
        super.addInformation(stack, tooltip);
    }


    public EnumTypeAudio getTypeAudio() {
        return typeAudio;
    }

    public void setType(EnumTypeAudio type) {
        typeAudio = type;
    }

    @Override
    public boolean getEnable() {
        return this.sound;
    }

    public void initiate(int soundEvent) {
        if (this.getTypeAudio() == valuesAudio[soundEvent % valuesAudio.length]) {
            return;
        }

        setType(valuesAudio[soundEvent % valuesAudio.length]);
        if (getSound() == null) {
            return;
        }
        if (getEnable()) {
            if (soundEvent == 0) {
                this.getWorld().playSound(null, this.pos, getSound(), SoundCategory.BLOCKS, 1F, 1);
            } else if (soundEvent == 1) {
                new PacketStopSound(getWorld(), this.pos);
                this.getWorld().playSound(
                        null,
                        this.pos,
                        EnumSound.interruptone_steam.getSoundEvent(),
                        SoundCategory.BLOCKS,
                        1F,
                        1
                );
            } else {
                new PacketStopSound(getWorld(), this.pos);
            }
        }
    }


    public void onUpdate() {

    }


    public List<ItemStack> getDrop() {

        return getAuxDrops(0);

    }

    @Override
    public void onPlaced(final ItemStack stack, final EntityLivingBase placer, final EnumFacing facing) {
        super.onPlaced(stack, placer, facing);
    }

    public ItemStack adjustDrop(ItemStack drop, boolean wrench) {
        if (!wrench) {
            switch (this.teBlock.getDefaultDrop()) {
                case Self:
                default:
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
        return drop;
    }

    public List<ItemStack> getWrenchDrops(EntityPlayer player, int fortune) {


        return super.getWrenchDrops(player, fortune);
    }


    @Override
    public boolean onActivated(
            final EntityPlayer entityPlayer,
            final EnumHand hand,
            final EnumFacing side,
            final float hitX,
            final float hitY,
            final float hitZ
    ) {

        if (!entityPlayer.getHeldItem(hand).isEmpty()) {
            if (!this.getWorld().isRemote && FluidUtil.getFluidHandler(entityPlayer.getHeldItem(hand)) != null && this.fluid != null) {
                for (AbstractComponent component : componentList) {
                    if (component.onBlockActivated(entityPlayer, hand))
                        return true;
                }
                return ModUtils.interactWithFluidHandler(entityPlayer, hand,
                        this.fluid.getCapability(CapabilityFluidHandler.FLUID_HANDLER_CAPABILITY, side)
                );
            }


        }
        return super.onActivated(entityPlayer, hand, side, hitX, hitY, hitZ);
    }


    public abstract EnumMultiMachine getMachine();

    public void readFromNBT(NBTTagCompound nbttagcompound) {
        super.readFromNBT(nbttagcompound);
        this.sound = nbttagcompound.getBoolean("sound");

    }

    public NBTTagCompound writeToNBT(NBTTagCompound nbttagcompound) {
        super.writeToNBT(nbttagcompound);
        nbttagcompound.setBoolean("sound", this.sound);

        return nbttagcompound;
    }

    public void updateTileServer(EntityPlayer player, double event) {
        if (event == 0) {

        } else {

            sound = !sound;
            new PacketUpdateFieldTile(this, "sound", this.sound);

            if (!sound) {
                if (this.getTypeAudio() == EnumTypeAudio.ON) {
                    setType(EnumTypeAudio.OFF);
                    new PacketStopSound(getWorld(), this.pos);


                }
            }
        }
    }

    public void onLoaded() {
        super.onLoaded();
        if (!this.getWorld().isRemote) {
            new PacketUpdateFieldTile(this, "sound", this.sound);

        }

    }

    public void updateField(String name, CustomPacketBuffer is) {

        if (name.equals("sound")) {
            try {
                this.sound = (boolean) DecoderHandler.decode(is);
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }
        super.updateField(name, is);
    }

    public EnumTypeMachines getTypeMachine() {
        return this.getMachine().type;
    }


    public void onUnloaded() {
        super.onUnloaded();


    }


    public void updateEntityServer() {
        super.updateEntityServer();
    }


    public ContainerSteamMultiMachine getGuiContainer(EntityPlayer player) {
        return new ContainerSteamMultiMachine(player, this, this.sizeWorkingSlot);
    }

    @SideOnly(Side.CLIENT)
    public GuiSteamMultiMachine getGui(EntityPlayer player, boolean isAdmin) {

        return new GuiSteamMultiMachine(new ContainerSteamMultiMachine(player, this, sizeWorkingSlot));


    }


    public int getMode() {
        return 0;
    }

}
