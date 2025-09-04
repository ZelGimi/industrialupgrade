package com.denfop.blockentity.mechanism.quarry;

import com.denfop.IUCore;
import com.denfop.IUItem;
import com.denfop.api.blockentity.MultiBlockEntity;
import com.denfop.api.container.CustomWorldContainer;
import com.denfop.api.otherenergies.common.EnergyType;
import com.denfop.api.recipe.InventoryOutput;
import com.denfop.api.sound.AudioFixer;
import com.denfop.api.sound.EnumTypeAudio;
import com.denfop.api.upgrades.IUpgradableBlock;
import com.denfop.api.upgrades.UpgradableProperty;
import com.denfop.api.vein.common.Type;
import com.denfop.api.vein.common.VeinBase;
import com.denfop.api.vein.common.VeinSystem;
import com.denfop.api.widget.IType;
import com.denfop.blockentity.base.BlockEntityInventory;
import com.denfop.blocks.BlockResource;
import com.denfop.componets.ComponentBaseEnergy;
import com.denfop.componets.EnumTypeStyle;
import com.denfop.containermenu.ContainerMenuBase;
import com.denfop.containermenu.ContainerMenuQuantumQuarry;
import com.denfop.inventory.InventoryQuantumQuarry;
import com.denfop.items.modules.EnumQuarryModules;
import com.denfop.items.modules.EnumQuarryType;
import com.denfop.network.DecoderHandler;
import com.denfop.network.EncoderHandler;
import com.denfop.network.IUpdatableTileEvent;
import com.denfop.network.packet.CustomPacketBuffer;
import com.denfop.network.packet.PacketStopSound;
import com.denfop.network.packet.PacketUpdateFieldTile;
import com.denfop.screen.ScreenIndustrialUpgrade;
import com.denfop.screen.ScreenQuantumQuarry;
import com.denfop.sound.EnumSound;
import com.denfop.utils.Keyboard;
import com.denfop.utils.Localization;
import com.denfop.utils.ModUtils;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

import java.io.IOException;
import java.util.*;

public class BlockEntityBaseQuantumQuarry extends BlockEntityInventory implements AudioFixer,
        IUpgradableBlock, IType, IUpdatableTileEvent {

    public final Random rand = new Random();
    public final InventoryQuantumQuarry inputslotB;
    public final InventoryOutput outputSlot;
    public final InventoryQuantumQuarry inputslot;
    public final InventoryQuantumQuarry inputslotA;
    public final InventoryQuantumQuarry inputslotC;
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
    public VeinBase vein;
    public List<QuarryItem> main_list = new ArrayList<>(IUCore.list_quarry);
    public boolean original = true;
    public boolean can_dig_vein = true;
    public EnumTypeAudio typeAudio = EnumTypeAudio.OFF;
    public EnumTypeAudio[] valuesAudio = EnumTypeAudio.values();
    public double col_tick;
    public boolean polisher = false;
    public boolean separator = false;
    public boolean plasma;
    private boolean sound = true;

    public BlockEntityBaseQuantumQuarry(int coef, MultiBlockEntity block, BlockPos pos, BlockState state) {
        super(block, pos, state);
        this.progress = 0;
        this.getblock = 0;
        this.energyconsume = 25000 * coef;
        this.energy = this.addComponent(ComponentBaseEnergy.asBasicSink(EnergyType.QUANTUM, this, 5E7D, 14));
        this.inputslot = new InventoryQuantumQuarry(this, 24, 0);
        this.inputslotA = new InventoryQuantumQuarry(this, 1, 1);
        this.inputslotB = new InventoryQuantumQuarry(this, 1, 2);
        this.inputslotC = new InventoryQuantumQuarry(this, 1, 3);
        this.outputSlot = new InventoryOutput(this, 49);
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
                this.vein = (VeinBase) DecoderHandler.decode(is);
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }
        super.updateField(name, is);
    }

    @Override
    public void addInformation(final ItemStack stack, final List<String> tooltip) {
        if (!Keyboard.isKeyDown(Keyboard.KEY_LSHIFT)) {
            tooltip.add(Localization.translate("press.lshift"));
        }
        if (Keyboard.isKeyDown(Keyboard.KEY_LSHIFT)) {
            tooltip.add(Localization.translate("iu.quarry_energy.info"));
            tooltip.add(Localization.translate("iu.machines_work_energy") + this.energyconsume + Localization.translate(
                    "iu.machines_work_energy_type_qe"));
        }
        final CompoundTag nbt = ModUtils.nbt(stack);
        final double energy1 = nbt.getDouble("energy");
        if (energy1 != 0) {
            tooltip.add(Localization.translate("iu.item.tooltip.Store") + " " + ModUtils.getString(energy1) + "/" + ModUtils.getString(
                    energy.getCapacity())
                    + " QE");
        }
        super.addInformation(stack, tooltip);

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

    @Override
    public SoundEvent getSound() {
        return EnumSound.quarry.getSoundEvent();
    }

    public void initiate(int soundEvent) {
        if (this.getTypeAudio() == valuesAudio[soundEvent % valuesAudio.length]) {
            return;
        }
        setType(valuesAudio[soundEvent % valuesAudio.length]);
        if (sound) {
            if (soundEvent == 0) {
                this.getWorld().playSound(null, this.getBlockPos(), getSound(), SoundSource.BLOCKS, 1F, 1);
            } else if (soundEvent == 1) {
                new PacketStopSound(getWorld(), this.getBlockPos());
                this.getWorld().playSound(null, this.getBlockPos(), EnumSound.InterruptOne.getSoundEvent(), SoundSource.BLOCKS, 1F, 1);
            } else {
                new PacketStopSound(getWorld(), this.getBlockPos());
            }
        }
    }

    public boolean list(BlockEntityBaseQuantumQuarry tile, ItemStack stack1) {
        if (tile.list_modules == null) {
            return false;
        }
        return list(tile.list_modules, stack1);
    }

    public void readFromNBT(CompoundTag nbttagcompound) {
        super.readFromNBT(nbttagcompound);
        this.progress = nbttagcompound.getInt("progress");
        this.getblock = nbttagcompound.getDouble("getblock");
        this.sound = nbttagcompound.getBoolean("sound");
    }

    public CompoundTag writeToNBT(CompoundTag nbttagcompound) {
        super.writeToNBT(nbttagcompound);
        nbttagcompound.putDouble("getblock", this.getblock);
        nbttagcompound.putInt("progress", this.progress);
        nbttagcompound.putBoolean("sound", this.sound);
        return nbttagcompound;
    }


    public void onLoaded() {
        super.onLoaded();
        this.inputslot.update();
        this.inputslotA.update();
        this.inputslotB.update();
        this.inputslotC.update();
        this.vein = VeinSystem.system.getVein(this.getWorld().getChunkAt(this.getBlockPos()).getPos());
        if (this.vein != VeinSystem.system.getEMPTY()) {
            if (this.vein.getType() != Type.VEIN) {
                return;
            }
            final ItemStack stack;
            if (vein.isOldMineral()) {
                stack = new ItemStack(IUItem.heavyore.getItem(vein.getMeta()), 1);
            } else {
                stack = new ItemStack(IUItem.mineral.getItem(vein.getMeta()), 1);
            }

            if (list(this.list_modules, stack)) {
                this.can_dig_vein = false;
            }
            new PacketUpdateFieldTile(this, "vein", this.vein);
        }
    }


    @Override
    public void onPlaced(final ItemStack stack, final LivingEntity placer, final Direction facing) {
        super.onPlaced(stack, placer, facing);
        final CompoundTag nbt = ModUtils.nbt(stack);
        final double energy1 = nbt.getDouble("energy");
        if (energy1 != 0) {
            this.energy.addEnergy(energy1);
        }
        this.vein = VeinSystem.system.getVein(this.getWorld().getChunkAt(this.getBlockPos()).getPos());
        if (this.vein != VeinSystem.system.getEMPTY()) {
            if (this.vein.getType() != Type.VEIN) {
                return;
            }
            final ItemStack stack1;
            if (vein.isOldMineral()) {
                stack1 = new ItemStack(IUItem.heavyore.getItem(vein.getMeta()), 1);
            } else {
                stack1 = new ItemStack(IUItem.mineral.getItem(vein.getMeta()), 1);
            }

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
                    final ItemStack stack;
                    if (vein.isOldMineral()) {
                        stack = new ItemStack(IUItem.heavyore.getItem(vein.getMeta()), 1);
                    } else {
                        stack = new ItemStack(IUItem.mineral.getItem(vein.getMeta()), 1);
                    }

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
        if (this.analyzer && plasma) {
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
                                && item != Items.REDSTONE && item != Items.LAPIS_LAZULI && item != Items.COAL && item != Items.GLOWSTONE_DUST) && chance2 >= 1) {

                            this.outputSlot.add(stack.getStack());

                        } else {
                            int k = this.level.random.nextInt(chance2 + 1);
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


        if (this.level.getGameTime() % 20 == 0 && !this.outputSlot.isEmpty()) {
            ModUtils.tick(this.outputSlot, this);
        }

    }

    public ContainerMenuQuantumQuarry getGuiContainer(Player player) {
        return new ContainerMenuQuantumQuarry(player, this);

    }

    @OnlyIn(Dist.CLIENT)
    public ScreenIndustrialUpgrade<ContainerMenuBase<? extends CustomWorldContainer>> getGui(Player player, ContainerMenuBase<? extends CustomWorldContainer> containerMenuBase) {

        return new ScreenQuantumQuarry((ContainerMenuQuantumQuarry) containerMenuBase);
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
        return Collections.EMPTY_SET;
    }

    @Override
    public EnumTypeStyle getStyle() {
        return EnumTypeStyle.DEFAULT;
    }

    @Override
    public void updateTileServer(final Player entityPlayer, final double i) {
        sound = !sound;
        new PacketUpdateFieldTile(this, "sound", this.sound);

        if (!sound) {
            if (this.getTypeAudio() == EnumTypeAudio.ON) {
                setType(EnumTypeAudio.OFF);
                initiate(2);

            }
        }
    }

}
