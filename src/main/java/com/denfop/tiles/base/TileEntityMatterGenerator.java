package com.denfop.tiles.base;

import com.denfop.Config;
import com.denfop.Localization;
import com.denfop.api.recipe.InvSlotOutput;
import com.denfop.api.upgrades.IUpgradableBlock;
import com.denfop.api.upgrades.UpgradableProperty;
import com.denfop.componets.AdvEnergy;
import com.denfop.container.ContainerSolidMatter;
import com.denfop.gui.GuiSolidMatter;
import com.denfop.invslot.InvSlotUpgrade;
import com.denfop.network.DecoderHandler;
import com.denfop.network.EncoderHandler;
import com.denfop.network.packet.CustomPacketBuffer;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import org.lwjgl.input.Keyboard;

import java.io.IOException;
import java.util.EnumSet;
import java.util.List;
import java.util.Set;

public abstract class TileEntityMatterGenerator extends TileEntityInventory implements
        IUpgradableBlock {

    public final InvSlotOutput outputSlot;
    public final ItemStack itemstack;
    public final InvSlotUpgrade upgradeSlot;
    private final AdvEnergy energy;
    private final String name;
    private double progress;

    public TileEntityMatterGenerator(ItemStack itemstack, String name) {
        this.itemstack = itemstack;
        this.outputSlot = new InvSlotOutput(this, "output", 1);
        this.upgradeSlot = new com.denfop.invslot.InvSlotUpgrade(this, 4);
        this.progress = 0;
        this.name = name;
        this.energy = this.addComponent(AdvEnergy.asBasicSink(this, Config.SolidMatterStorage, 10));

    }

    private static int applyModifier(int extra) {
        double ret = (double) Math.round(((double) 10 + (double) extra));
        return ret > 2.147483647E9D ? 2147483647 : (int) ret;
    }

    @Override
    public void readContainerPacket(final CustomPacketBuffer customPacketBuffer) {
        super.readContainerPacket(customPacketBuffer);
        try {
            progress = (double) DecoderHandler.decode(customPacketBuffer);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

    }

    @Override
    public CustomPacketBuffer writeContainerPacket() {
        final CustomPacketBuffer packet = super.writeContainerPacket();
        try {
            EncoderHandler.encode(packet, progress);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        return packet;
    }

    @SideOnly(Side.CLIENT)
    public void addInformation(ItemStack stack, List<String> tooltip, ITooltipFlag advanced) {
        if (!Keyboard.isKeyDown(Keyboard.KEY_LSHIFT)) {
            tooltip.add(Localization.translate("press.lshift"));
        }
        if (Keyboard.isKeyDown(Keyboard.KEY_LSHIFT)) {
            tooltip.add(Localization.translate("iu.matter_solid_work_info") + (int) Config.SolidMatterStorage);
        }
        AdvEnergy energy = this.energy;
        if (!energy.getSourceDirs().isEmpty()) {
            tooltip.add(Localization.translate("iu.item.tooltip.PowerTier", energy.getSourceTier()));
        } else if (!energy.getSinkDirs().isEmpty()) {
            tooltip.add(Localization.translate("iu.item.tooltip.PowerTier", energy.getSinkTier()));
        }


    }

    public void readFromNBT(NBTTagCompound nbttagcompound) {
        super.readFromNBT(nbttagcompound);
        this.progress = nbttagcompound.getDouble("progress");
    }

    public NBTTagCompound writeToNBT(NBTTagCompound nbttagcompound) {
        super.writeToNBT(nbttagcompound);
        nbttagcompound.setDouble("progress", this.progress);
        return nbttagcompound;
    }


    public void updateEntityServer() {
        super.updateEntityServer();
        if (this.energy.getEnergy() > 0) {
            this.progress = this.energy.getEnergy() / this.energy.getCapacity();
            if (this.energy.getEnergy() >= this.energy.getCapacity()) {
                if (this.outputSlot.add(itemstack)) {
                    this.energy.useEnergy(this.energy.getCapacity());
                    this.progress = 0;
                }
            }

        }


        if (this.upgradeSlot.tickNoMark()) {
            setUpgradestat();
        }

    }

    @SideOnly(Side.CLIENT)
    public GuiSolidMatter getGui(EntityPlayer entityPlayer, boolean isAdmin) {
        return new GuiSolidMatter(new ContainerSolidMatter(entityPlayer, this));
    }

    public ContainerSolidMatter getGuiContainer(EntityPlayer entityPlayer) {
        return new ContainerSolidMatter(entityPlayer, this);
    }

    public void setUpgradestat() {
        this.energy.setSinkTier(applyModifier(this.upgradeSlot.extraTier));
    }

    public String getStartSoundFile() {
        return null;
    }

    public String getInterruptSoundFile() {
        return null;
    }


    @Override
    public Set<UpgradableProperty> getUpgradableProperties() {
        return EnumSet.of(
                UpgradableProperty.Transformer,
                UpgradableProperty.ItemProducing
        );
    }

    public void onGuiClosed(EntityPlayer player) {
    }


    public final float getChargeLevel() {
        return Math.min((float) this.energy.getEnergy() / (float) this.energy.getCapacity(), 1);
    }

    public double getProgress() {
        return this.progress;
    }

    public int getMode() {
        return 0;
    }

    public String getInventoryName() {
        return this.name;
    }


}
