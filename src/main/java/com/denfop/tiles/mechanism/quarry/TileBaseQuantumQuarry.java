package com.denfop.tiles.mechanism.quarry;

import com.denfop.Config;
import com.denfop.IUCore;
import com.denfop.IUItem;
import com.denfop.Localization;
import com.denfop.api.audio.EnumTypeAudio;
import com.denfop.api.audio.IAudioFixer;
import com.denfop.api.gui.IType;
import com.denfop.api.recipe.InvSlotOutput;
import com.denfop.api.sytem.EnergyType;
import com.denfop.api.upgrades.IUpgradableBlock;
import com.denfop.api.upgrades.UpgradableProperty;
import com.denfop.api.vein.Type;
import com.denfop.api.vein.Vein;
import com.denfop.api.vein.VeinSystem;
import com.denfop.audio.EnumSound;
import com.denfop.blocks.BlockResource;
import com.denfop.componets.ComponentBaseEnergy;
import com.denfop.componets.EnumTypeStyle;
import com.denfop.container.ContainerQuantumQuarry;
import com.denfop.gui.GuiQuantumQuarry;
import com.denfop.invslot.InvSlotQuantumQuarry;
import com.denfop.items.modules.EnumQuarryModules;
import com.denfop.items.modules.EnumQuarryType;
import com.denfop.network.DecoderHandler;
import com.denfop.network.EncoderHandler;
import com.denfop.network.IUpdatableTileEvent;
import com.denfop.network.packet.CustomPacketBuffer;
import com.denfop.network.packet.PacketStopSound;
import com.denfop.network.packet.PacketUpdateFieldTile;
import com.denfop.tiles.base.TileEntityInventory;
import com.denfop.utils.ModUtils;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Items;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.SoundEvent;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import org.lwjgl.input.Keyboard;

import java.io.IOException;
import java.util.*;

public class TileBaseQuantumQuarry extends TileEntityInventory implements IAudioFixer,
        IUpgradableBlock, IType, IUpdatableTileEvent {

    public final Random rand = new Random();
    public final InvSlotQuantumQuarry inputslotB;
    public final InvSlotOutput outputSlot;
    public final InvSlotQuantumQuarry inputslot;
    public final InvSlotQuantumQuarry inputslotA;
    public int energyconsume;
    public double consume;
    public boolean mac_enabled = false;
    public boolean comb_mac_enabled = false;
    public boolean furnace;
    public int chance;
    public int col;
    public List<QuarryItem> list;
    public double getblock;
    public ComponentBaseEnergy energy;
    public boolean analyzer;
    public int progress;
    public EnumQuarryModules list_modules;
    public Vein vein;
    public List<QuarryItem> main_list = new ArrayList<>(IUCore.list_quarry);
    public boolean original = true;
    public boolean can_dig_vein = true;
    public EnumTypeAudio typeAudio = EnumTypeAudio.OFF;
    public EnumTypeAudio[] valuesAudio = EnumTypeAudio.values();
    public double col_tick;
    private boolean sound = true;

    public TileBaseQuantumQuarry(int coef) {

        this.progress = 0;
        this.getblock = 0;
        this.energyconsume = Config.enerycost * coef;
        this.energy = this.addComponent(ComponentBaseEnergy.asBasicSink(EnergyType.QUANTUM, this, 5E7D, 14));
        this.inputslot = new InvSlotQuantumQuarry(this, 25, 0);
        this.inputslotA = new InvSlotQuantumQuarry(this, 26, 1);
        this.inputslotB = new InvSlotQuantumQuarry(this, 27, 2);
        this.outputSlot = new InvSlotOutput(this, "output", 24);
        this.list = new ArrayList<>();
        this.analyzer = false;
        this.chance = 0;
        this.col = 1;
        this.furnace = false;
        this.list_modules = null;
        this.consume = this.energyconsume;
    }

    public void updateField(String name, CustomPacketBuffer is) {

        if (name.equals("analyzer")) {
            try {
                this.analyzer = (boolean) DecoderHandler.decode(is);
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }
        if (name.equals("sound")) {
            try {
                this.sound = (boolean) DecoderHandler.decode(is);
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }
        if (name.equals("vein")) {
            try {
                this.vein = (Vein) DecoderHandler.decode(is);
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }
        super.updateField(name, is);
    }

    @Override
    @SideOnly(Side.CLIENT)
    public void addInformation(final ItemStack stack, final List<String> tooltip, final ITooltipFlag advanced) {
        if (!Keyboard.isKeyDown(Keyboard.KEY_LSHIFT)) {
            tooltip.add(Localization.translate("press.lshift"));
        }
        if (Keyboard.isKeyDown(Keyboard.KEY_LSHIFT)) {
            tooltip.add(Localization.translate("iu.quarry_energy.info"));
            tooltip.add(Localization.translate("iu.machines_work_energy") + this.energyconsume + Localization.translate(
                    "iu.machines_work_energy_type_qe"));
        }
        final NBTTagCompound nbt = ModUtils.nbt(stack);
        final double energy1 = nbt.getDouble("energy");
        if (energy1 != 0) {
            tooltip.add(Localization.translate("iu.item.tooltip.Store") + " " + ModUtils.getString(energy1) + "/" + ModUtils.getString(
                    energy.getCapacity())
                    + " QE");
        }
        super.addInformation(stack, tooltip, advanced);

    }

    @Override
    public void readContainerPacket(final CustomPacketBuffer customPacketBuffer) {
        super.readContainerPacket(customPacketBuffer);
        try {
            energyconsume = (int) DecoderHandler.decode(customPacketBuffer);
            progress = (int) DecoderHandler.decode(customPacketBuffer);
            getblock = (double) DecoderHandler.decode(customPacketBuffer);
            consume = (double) DecoderHandler.decode(customPacketBuffer);
            col_tick = (double) DecoderHandler.decode(customPacketBuffer);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

    }

    @Override
    public CustomPacketBuffer writeContainerPacket() {
        final CustomPacketBuffer packet = super.writeContainerPacket();
        try {
            EncoderHandler.encode(packet, energyconsume);
            EncoderHandler.encode(packet, progress);
            EncoderHandler.encode(packet, getblock);
            EncoderHandler.encode(packet, consume);
            EncoderHandler.encode(packet, col_tick);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        return packet;
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

    @Override
    public SoundEvent getSound() {
        return EnumSound.quarry.getSoundEvent();
    }

    public void initiate(int soundEvent) {
        if (this.getType() == valuesAudio[soundEvent % valuesAudio.length]) {
            return;
        }
        setType(valuesAudio[soundEvent % valuesAudio.length]);
        if (sound) {
            if (soundEvent == 0) {
                this.getWorld().playSound(null, this.pos, getSound(), SoundCategory.BLOCKS, 1F, 1);
            } else if (soundEvent == 1) {
                new PacketStopSound(getWorld(), this.pos);
                this.getWorld().playSound(null, this.pos, EnumSound.InterruptOne.getSoundEvent(), SoundCategory.BLOCKS, 1F, 1);
            } else {
                new PacketStopSound(getWorld(), this.pos);
            }
        }
    }

    public boolean list(TileBaseQuantumQuarry tile, ItemStack stack1) {
        if (tile.list_modules == null) {
            return false;
        }
        return list(tile.list_modules, stack1);
    }

    public void readFromNBT(NBTTagCompound nbttagcompound) {
        super.readFromNBT(nbttagcompound);
        this.progress = nbttagcompound.getInteger("progress");
        this.getblock = nbttagcompound.getDouble("getblock");
        this.sound = nbttagcompound.getBoolean("sound");
    }

    public NBTTagCompound writeToNBT(NBTTagCompound nbttagcompound) {
        super.writeToNBT(nbttagcompound);
        nbttagcompound.setDouble("getblock", this.getblock);
        nbttagcompound.setInteger("progress", this.progress);
        nbttagcompound.setBoolean("sound", this.sound);
        return nbttagcompound;
    }


    public void onLoaded() {
        super.onLoaded();
        this.inputslot.update();
        this.inputslotA.update();
        this.inputslotB.update();
        this.vein = VeinSystem.system.getVein(this.getWorld().getChunkFromBlockCoords(this.pos).getPos());
        if (this.vein != VeinSystem.system.getEMPTY()) {
            if (this.vein.getType() != Type.VEIN) {
                return;
            }
            final ItemStack stack = new ItemStack(IUItem.heavyore, 1, vein.getMeta());
            if (list(this.list_modules, stack)) {
                this.can_dig_vein = false;
            }
            new PacketUpdateFieldTile(this, "vein", this.vein);
        }
    }


    @Override
    public void onPlaced(final ItemStack stack, final EntityLivingBase placer, final EnumFacing facing) {
        super.onPlaced(stack, placer, facing);
        final NBTTagCompound nbt = ModUtils.nbt(stack);
        final double energy1 = nbt.getDouble("energy");
        if (energy1 != 0) {
            this.energy.addEnergy(energy1);
        }
        this.vein = VeinSystem.system.getVein(this.getWorld().getChunkFromBlockCoords(this.pos).getPos());
        if (this.vein != VeinSystem.system.getEMPTY()) {
            if (this.vein.getType() != Type.VEIN) {
                return;
            }
            final ItemStack stack1 = new ItemStack(IUItem.heavyore, 1, vein.getMeta());
            if (list(this.list_modules, stack1)) {
                this.can_dig_vein = false;
            }
            new PacketUpdateFieldTile(this, "vein", this.vein);
        }

    }

    public void updateEntityServer() {
        super.updateEntityServer();
        double proccent = this.consume;

        if (this.vein != null) {
            if (this.analyzer && this.vein.get()) {
                if (this.vein.getType() == Type.VEIN && this.vein.getCol() > 0 && this.energy.getEnergy() > consume) {
                    final ItemStack stack = new ItemStack(IUItem.heavyore, 1, vein.getMeta());
                    if (this.can_dig_vein) {
                        if (!this.getActive()) {
                            this.setActive(true);
                        }
                        if (this.outputSlot.add(stack)) {
                            this.energy.useEnergy(proccent);
                            this.getblock++;
                            this.vein.removeCol(1);
                        }
                    }
                }
            }
        }
        this.col_tick = 0;
        if (this.analyzer && !Config.enableonlyvein) {
            double col = this.col;
            int chance2 = this.chance;
            int coble = rand.nextInt((int) col + 1);
            this.getblock += coble;
            col -= coble;

            boolean work = this.energy.getEnergy() >= proccent;
            for (double i = 0; i < col; i++) {
                if (this.energy.getEnergy() >= proccent) {
                    work = true;
                    this.col_tick++;
                    this.energy.useEnergy(proccent);
                    this.getblock++;
                    int num = main_list.size();
                    if (num == 0) {
                        return;
                    }
                    int chance1 = rand.nextInt(num);
                    if (num != IUCore.list.size()) {
                        int chance3 = rand.nextInt(IUCore.list.size());
                        if (!(chance3 <= chance1)) {
                            continue;
                        }
                    }
                    QuarryItem stack = main_list.get(chance1);
                    if (this.original) {
                        Item item = stack.getStack().getItem();
                        if ((!stack.isGem() && !stack.isShard()
                                && item != Items.REDSTONE && item != Items.DYE && item != Items.COAL && item != Items.GLOWSTONE_DUST) && chance2 >= 1) {

                            this.outputSlot.add(stack.getStack());

                        } else {
                            int k = this.world.rand.nextInt(chance2 + 1);
                            for (int j = 0; j < k + 1; j++) {
                                this.outputSlot.add(stack.getStack());
                            }
                        }

                    } else {

                        this.outputSlot.add(stack.getStack());

                    }


                }
            }
            if (work) {
                if (!this.getActive()) {
                    this.setActive(true);
                    initiate(0);
                }
            } else {
                if (this.getActive()) {
                    initiate(2);
                    this.setActive(false);
                }
            }
        } else {
            if (this.getActive()) {
                initiate(2);
                this.setActive(false);
            }
        }


        if (this.world.getWorldTime() % 20 == 0 && !this.outputSlot.isEmpty()) {
            ModUtils.tick(this.outputSlot, this);
        }

    }

    public ContainerQuantumQuarry getGuiContainer(EntityPlayer player) {
        return new ContainerQuantumQuarry(player, this);

    }

    @SideOnly(Side.CLIENT)
    public GuiQuantumQuarry getGui(EntityPlayer player, boolean isAdmin) {

        return new GuiQuantumQuarry(new ContainerQuantumQuarry(player, this));
    }

    public boolean list(EnumQuarryModules type, ItemStack stack1) {
        if (type == null) {
            return false;
        }
        if (type.type == EnumQuarryType.BLACKLIST) {

            return this.list.contains(new QuarryItem(stack1));


        } else if (type.type == EnumQuarryType.WHITELIST) {

            return !this.list.contains(new QuarryItem(stack1));

        }
        return false;
    }

    public boolean list(EnumQuarryModules type, QuarryItem stack1) {
        if (type == null) {
            return false;
        }
        if (type.type == EnumQuarryType.BLACKLIST) {

            return this.list.contains(stack1);


        } else if (type.type == EnumQuarryType.WHITELIST) {

            return !this.list.contains(stack1);

        }
        return false;
    }

    @Override
    public Set<UpgradableProperty> getUpgradableProperties() {
        return EnumSet.of(
                UpgradableProperty.ItemProducing
        );
    }

    @Override
    public EnumTypeStyle getStyle() {
        return EnumTypeStyle.DEFAULT;
    }

    @Override
    public void updateTileServer(final EntityPlayer entityPlayer, final double i) {
        sound = !sound;
        new PacketUpdateFieldTile(this, "sound", this.sound);

        if (!sound) {
            if (this.getType() == EnumTypeAudio.ON) {
                setType(EnumTypeAudio.OFF);
                initiate(2);

            }
        }
    }

}
