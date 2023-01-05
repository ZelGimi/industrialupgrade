package com.denfop.tiles.mechanism.blastfurnace.block;

import com.denfop.IUCore;
import com.denfop.IUItem;
import com.denfop.Ic2Items;
import com.denfop.api.audio.EnumTypeAudio;
import com.denfop.api.audio.IAudioFixer;
import com.denfop.audio.AudioSource;
import com.denfop.audio.PositionSpec;
import com.denfop.componets.HeatComponent;
import com.denfop.container.ContainerBlastFurnace;
import com.denfop.gui.GuiBlastFurnace;
import com.denfop.tiles.base.TileEntityInventory;
import com.denfop.tiles.mechanism.blastfurnace.api.BlastSystem;
import com.denfop.tiles.mechanism.blastfurnace.api.IBlastHeat;
import com.denfop.tiles.mechanism.blastfurnace.api.IBlastInputFluid;
import com.denfop.tiles.mechanism.blastfurnace.api.IBlastInputItem;
import com.denfop.tiles.mechanism.blastfurnace.api.IBlastMain;
import com.denfop.tiles.mechanism.blastfurnace.api.IBlastOutputItem;
import ic2.api.network.INetworkClientTileEntityEventListener;
import ic2.api.network.INetworkTileEntityEventListener;
import ic2.core.IC2;
import ic2.core.IHasGui;
import ic2.core.block.comp.Fluids;
import ic2.core.block.invslot.InvSlot;
import ic2.core.init.Localization;
import ic2.core.ref.FluidName;
import ic2.core.util.LiquidUtil;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.fluids.FluidTank;
import net.minecraftforge.fluids.FluidUtil;
import net.minecraftforge.fluids.capability.CapabilityFluidHandler;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import java.util.ArrayList;
import java.util.List;

public class TileEntityBlastFurnaceMain extends TileEntityInventory implements IBlastMain, INetworkClientTileEntityEventListener,
        IHasGui, IAudioFixer, INetworkTileEntityEventListener {

    public final FluidTank tank;
    public FluidTank tank1 = null;
    public HeatComponent component = null;
    public IBlastHeat blastHeat;
    public IBlastInputFluid blastInputFluid;
    public IBlastInputItem blastInputItem;
    public IBlastOutputItem blastOutputItem;
    public List<EntityPlayer> entityPlayerList;
    public double progress = 0;
    public int bar = 1;
    public AudioSource audioSource;
    public EnumTypeAudio typeAudio = EnumTypeAudio.OFF;
    public EnumTypeAudio[] valuesAudio = EnumTypeAudio.values();
    private boolean full;
    private boolean sound = true;

    public TileEntityBlastFurnaceMain() {
        this.full = false;
        final Fluids fluids = this.addComponent(new Fluids(this));
        this.tank = fluids.addTank("tank", 10000, InvSlot.Access.I, InvSlot.InvSide.ANY,
                Fluids.fluidPredicate(FluidName.steam.getInstance())
        );
        this.entityPlayerList = new ArrayList<>();
    }

    @Override
    @SideOnly(Side.CLIENT)
    public void addInformation(final ItemStack stack, final List<String> tooltip, final ITooltipFlag advanced) {
        super.addInformation(stack, tooltip, advanced);
        tooltip.add(Localization.translate("iu.blastfurnace.info1"));
        tooltip.add(Localization.translate("iu.blastfurnace.info3") + Localization.translate(new ItemStack(
                IUItem.blastfurnace,
                1,
                0
        ).getUnlocalizedName()));
        tooltip.add(Localization.translate("iu.blastfurnace.info4"));
        tooltip.add(Localization.translate("iu.blastfurnace.info5") + Localization.translate(Ic2Items.ForgeHammer.getUnlocalizedName()));
        tooltip.add(Localization.translate("iu.blastfurnace.info6"));
    }

    public EnumTypeAudio getType() {
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
        if (this.getType() == valuesAudio[soundEvent % valuesAudio.length]) {
            return;
        }
        setType(valuesAudio[soundEvent % valuesAudio.length]);
        if (sound) {
            IC2.network.get(true).initiateTileEntityEvent(this, soundEvent, true);
        }
    }

    @Override
    public void changeSound() {

    }

    public String getStartSoundFile() {
        return "Machines/blast_furnace.ogg";
    }

    public String getInterruptSoundFile() {
        return null;
    }


    public void onNetworkEvent(int event) {
        if (this.audioSource == null && this.getStartSoundFile() != null) {
            this.audioSource = IUCore.audioManager.createSource(this, this.getStartSoundFile());
        }

        switch (event) {
            case 0:
                if (this.audioSource != null && !this.audioSource.playing()) {
                    this.audioSource.play();
                }
                break;
            case 1:

                if (this.getInterruptSoundFile() != null) {
                    IUCore.audioManager.playOnce(
                            this,
                            PositionSpec.Center,
                            this.getInterruptSoundFile(),
                            false,
                            IUCore.audioManager.getDefaultVolume()
                    );

                }
                break;
            case 2:
                if (this.audioSource != null) {
                    this.audioSource.stop();
                }
            case 3:
        }

    }

    @Override
    public boolean getFull() {
        return full;
    }

    @Override
    public void setFull(final boolean full) {
        if (!full) {
            if (!this.entityPlayerList.isEmpty()) {
                this.entityPlayerList.forEach(EntityPlayer::closeScreen);
            }
        }
        this.full = full;
    }

    @Override
    public void update_block() {

        full = BlastSystem.instance.getFull(this.getFacing(), this.pos, this.getWorld(), this);
    }

    public void update_block(EntityPlayer player) {
        if (!this.world.isRemote) {
            full = BlastSystem.instance.getFull(this.getFacing(), this.pos, this.getWorld(), this, player);
        }
    }

    @Override
    public void onPlaced(final ItemStack stack, final EntityLivingBase placer, final EnumFacing facing) {
        super.onPlaced(stack, placer, facing);
        if (!this.world.isRemote) {
            if (placer instanceof EntityPlayer) {
                update_block((EntityPlayer) placer);
            } else {
                update_block();
            }
        }
    }

    @Override
    protected void onLoaded() {
        super.onLoaded();
        update_block();
        if (!this.getWorld().isRemote) {
            IC2.network.get(true).updateTileEntityField(this, "sound");
        }
    }

    @Override
    protected void onUnloaded() {
        if (this.full) {
            BlastSystem.instance.deleteMain(this.getFacing(), this.pos, this.getWorld(), this);
        }
        if (IC2.platform.isRendering() && this.audioSource != null) {
            IC2.audioManager.removeSources(this);
            this.audioSource = null;
        }
        super.onUnloaded();

    }

    @Override
    protected void updateEntityServer() {
        super.updateEntityServer();
        if (!this.full) {
            if (this.getActive()) {
                this.setActive(false);
                initiate(2);
            }
            return;
        }
        try {

            if (!this.getInputItem().getInput().isEmpty()) {
                int amount_stream = tank.getFluidAmount();
                if (this.getHeat().getHeatComponent().getEnergy() == this.getHeat().getHeatComponent().getCapacity()) {
                    int bar1 = bar;
                    if (amount_stream < bar1 * 2) {
                        bar1 = amount_stream / 2;
                    }
                    if (bar1 > 0) {
                        if (progress == 0) {
                            this.setActive(true);
                            initiate(0);
                        }
                        if (!this.getActive()) {
                            this.setActive(true);
                        }
                        progress += 1 + (0.25 * (bar1 - 1));
                        tank.drain(Math.min(bar1 * 2, this.tank.getFluidAmount()), true);
                        if (progress >= 3600 && this.getOutputItem().getOutput().add(Ic2Items.advIronIngot)) {
                            progress = 0;
                            this.getInputItem().getInput().get(0).shrink(1);
                            this.setActive(false);
                            initiate(2);
                        }
                    }
                }
                double heat = this.getHeat().getHeatComponent().getEnergy();
                if (heat > 250 && this.tank.getFluidAmount() + 2 < this.tank.getCapacity()) {
                    int size = this.tank1.getFluidAmount();
                    int size_stream = this.tank.getCapacity() - this.tank.getFluidAmount();
                    int size1 = size / 5;
                    size1 = Math.min(size1, 10);
                    if (size1 > 0) {
                        int add = Math.min(size_stream / 2, size1);
                        if (add > 0) {
                            this.tank.fill(new FluidStack(FluidName.steam.getInstance(), add * 2), true);
                            this.getInputFluid().getFluidTank().drain(add * 5, true);

                        }
                    }
                }

            } else if (this.getActive()) {
                this.setActive(false);
            }
            if (this.component.getEnergy() > 0) {
                this.component.useEnergy(1);
            }
        } catch (Exception ignored) {
        }
    }

    @Override
    public IBlastHeat getHeat() {
        return blastHeat;
    }

    @Override
    public void setHeat(final IBlastHeat blastHeat) {
        this.blastHeat = blastHeat;
        if (this.blastHeat == null) {
            this.component = null;
        } else {
            this.component = this.blastHeat.getHeatComponent();
        }
    }

    @Override
    public IBlastInputFluid getInputFluid() {
        return blastInputFluid;
    }

    @Override
    public void setInputFluid(final IBlastInputFluid blastInputFluid) {
        this.blastInputFluid = blastInputFluid;
        if (this.blastInputFluid == null) {
            this.tank1 = null;
        } else {
            this.tank1 = this.blastInputFluid.getFluidTank();
        }
    }

    @Override
    public IBlastInputItem getInputItem() {
        return blastInputItem;
    }

    @Override
    public void setInputItem(final IBlastInputItem blastInputItem) {
        this.blastInputItem = blastInputItem;
    }

    @Override
    public IBlastOutputItem getOutputItem() {
        return blastOutputItem;
    }

    @Override
    public void setOutputItem(final IBlastOutputItem blastOutputItem) {
        this.blastOutputItem = blastOutputItem;
    }

    public void readFromNBT(NBTTagCompound nbttagcompound) {
        super.readFromNBT(nbttagcompound);
        this.sound = nbttagcompound.getBoolean("sound");

    }

    public NBTTagCompound writeToNBT(NBTTagCompound nbttagcompound) {
        super.writeToNBT(nbttagcompound);
        nbttagcompound.setBoolean("sound", this.sound);

        return nbttagcompound;
    }

    @Override
    public double getProgress() {
        return this.progress;
    }

    @Override
    public void onNetworkEvent(final EntityPlayer entityPlayer, final int i) {
        switch (i) {
            case 0:
                this.bar = Math.min(this.bar + 1, 5);
                break;
            case 1:
                this.bar = Math.max(1, this.bar - 1);
                break;
            case 10:

                sound = !sound;
                IC2.network.get(true).updateTileEntityField(this, "sound");

                if (!sound) {
                    if (this.getType() == EnumTypeAudio.ON) {
                        setType(EnumTypeAudio.OFF);
                        IC2.network.get(true).initiateTileEntityEvent(this, 2, true);

                    }
                }
                break;
        }
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
        if (!this.full) {
            if (player.getHeldItem(hand).isEmpty()) {
                return false;
            }
            if (player.getHeldItem(hand).isItemEqual(Ic2Items.ForgeHammer)) {
                update_block(player);

                if (!this.full) {
                    return false;
                } else {
                    if (!this.getWorld().isRemote && LiquidUtil.isFluidContainer(player.getHeldItem(hand))) {

                        return FluidUtil.interactWithFluidHandler(player, hand,
                                this.blastInputFluid
                                        .getFluid()
                                        .getCapability(CapabilityFluidHandler.FLUID_HANDLER_CAPABILITY, side)
                        );
                    }
                    return super.onActivated(player, hand, side, hitX, hitY, hitZ);
                }
            }
            return false;
        }

        return super.onActivated(player, hand, side, hitX, hitY, hitZ);
    }

    @Override
    public ContainerBlastFurnace getGuiContainer(final EntityPlayer entityPlayer) {
        if (!this.entityPlayerList.contains(entityPlayer)) {
            this.entityPlayerList.add(entityPlayer);
        }
        return new ContainerBlastFurnace(entityPlayer, this);
    }

    @Override
    @SideOnly(Side.CLIENT)
    public GuiBlastFurnace getGui(final EntityPlayer entityPlayer, final boolean b) {

        return new GuiBlastFurnace(this.getGuiContainer(entityPlayer));
    }

    @Override
    public void onGuiClosed(final EntityPlayer entityPlayer) {

    }


}
