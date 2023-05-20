package com.denfop.tiles.mechanism.quarry;

import com.denfop.Config;
import com.denfop.IUCore;
import com.denfop.IUItem;
import com.denfop.api.audio.EnumTypeAudio;
import com.denfop.api.audio.IAudioFixer;
import com.denfop.api.gui.IType;
import com.denfop.api.inv.IHasGui;
import com.denfop.api.recipe.InvSlotOutput;
import com.denfop.api.sytem.EnergyType;
import com.denfop.api.vein.Type;
import com.denfop.api.vein.Vein;
import com.denfop.api.vein.VeinSystem;
import com.denfop.audio.AudioSource;
import com.denfop.audio.PositionSpec;
import com.denfop.componets.ComponentBaseEnergy;
import com.denfop.componets.EnumTypeStyle;
import com.denfop.container.ContainerQuantumQuarry;
import com.denfop.gui.GuiQuantumQuarry;
import com.denfop.invslot.InvSlotQuantumQuarry;
import com.denfop.items.modules.EnumQuarryModules;
import com.denfop.items.modules.EnumQuarryType;
import com.denfop.tiles.base.TileEntityInventory;
import com.denfop.utils.ModUtils;
import ic2.api.network.INetworkClientTileEntityEventListener;
import ic2.api.upgrade.IUpgradableBlock;
import ic2.api.upgrade.UpgradableProperty;
import ic2.core.IC2;
import ic2.core.block.type.ResourceBlock;
import ic2.core.init.Localization;
import ic2.core.ref.BlockName;
import ic2.core.ref.TeBlock;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Items;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.EnumFacing;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import org.lwjgl.input.Keyboard;

import java.util.*;

public class TileEntityBaseQuantumQuarry extends TileEntityInventory implements IHasGui, IAudioFixer,
        IUpgradableBlock, IType, INetworkClientTileEntityEventListener {

    public final Random rand = new Random();
    public final int energyconsume;
    public final InvSlotQuantumQuarry inputslotB;
    public final InvSlotOutput outputSlot;
    public final InvSlotQuantumQuarry inputslot;
    public final InvSlotQuantumQuarry inputslotA;
    public double consume;
    public boolean mac_enabled = false;
    public boolean comb_mac_enabled = false;
    public boolean furnace;
    public int chance;
    public int col;
    public List<QuarryItem> list;
    public AudioSource audioSource;
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

    public TileEntityBaseQuantumQuarry(int coef) {

        this.progress = 0;
        this.getblock = 0;
        this.energyconsume = Config.enerycost * coef;
        this.energy = this.addComponent(ComponentBaseEnergy.asBasicSink(EnergyType.QUANTUM, this, 5E7D, 14));
        this.inputslot = new InvSlotQuantumQuarry(this, 25, "input", 0);
        this.inputslotA = new InvSlotQuantumQuarry(this, 26, "input1", 1);
        this.inputslotB = new InvSlotQuantumQuarry(this, 27, "input2", 2);
        this.outputSlot = new InvSlotOutput(this, "output", 24);
        this.list = new ArrayList<>();
        this.analyzer = false;
        this.chance = 0;
        this.col = 1;
        this.furnace = false;
        this.list_modules = null;
        this.consume = this.energyconsume;
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
            tooltip.add(Localization.translate("ic2.item.tooltip.Store") + " " + ModUtils.getString(energy1) + "/" + ModUtils.getString(
                    energy.getCapacity())
                    + " QE");
        }
        super.addInformation(stack, tooltip, advanced);

    }

    protected ItemStack adjustDrop(ItemStack drop, boolean wrench) {
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
                    return BlockName.te.getItemStack(TeBlock.generator);
                case Machine:
                    return BlockName.resource.getItemStack(ResourceBlock.machine);
                case AdvMachine:
                    return BlockName.resource.getItemStack(ResourceBlock.advanced_machine);
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

    public void initiate(int soundEvent) {
        if (this.getType() == valuesAudio[soundEvent % valuesAudio.length]) {
            return;
        }
        setType(valuesAudio[soundEvent % valuesAudio.length]);
        if (sound) {
            IUCore.network.get(true).initiateTileEntityEvent(this, soundEvent, true);
        }
    }

    public boolean list(TileEntityBaseQuantumQuarry tile, ItemStack stack1) {
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

    public void changeSound() {
        sound = !sound;
        IUCore.network.get(true).updateTileEntityField(this, "sound");

        if (!sound) {
            if (this.getType() == EnumTypeAudio.ON) {
                setType(EnumTypeAudio.OFF);
                IC2.network.get(true).initiateTileEntityEvent(this, 2, true);

            }
        }
    }

    public NBTTagCompound writeToNBT(NBTTagCompound nbttagcompound) {
        super.writeToNBT(nbttagcompound);
        nbttagcompound.setDouble("getblock", this.getblock);
        nbttagcompound.setInteger("progress", this.progress);
        nbttagcompound.setBoolean("sound", this.sound);
        return nbttagcompound;
    }


    protected void onLoaded() {
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
            IUCore.network.get(true).updateTileEntityField(this, "vein");
        }
    }

    protected void onUnloaded() {
        super.onUnloaded();
        if (IC2.platform.isRendering() && this.audioSource != null) {
            IUCore.audioManager.removeSources(this);
            this.audioSource = null;
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
            IUCore.network.get(true).updateTileEntityField(this, "vein");
        }

    }

    protected void updateEntityServer() {
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
                    if (num <= 0) {
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

    public String getStartSoundFile() {
        return "Machines/quarry.ogg";
    }

    public String getInterruptSoundFile() {
        return "Machines/InterruptOne.ogg";
    }

    public void onNetworkEvent(int event) {
        if (this.audioSource == null && this.getStartSoundFile() != null) {
            this.audioSource = IUCore.audioManager.createSource(this, this.getStartSoundFile());
        }

        switch (event) {
            case 0:
                if (this.audioSource != null) {
                    this.audioSource.play();
                }
                break;
            case 1:
                if (this.audioSource != null) {
                    this.audioSource.stop();
                    if (this.getInterruptSoundFile() != null) {
                        IUCore.audioManager.playOnce(
                                this,
                                PositionSpec.Center,
                                this.getInterruptSoundFile(),
                                false,
                                IUCore.audioManager.getDefaultVolume()
                        );
                    }
                }
                break;
            case 2:
                if (this.audioSource != null) {
                    this.audioSource.stop();
                }
            case 3:
        }

    }

    public void onGuiClosed(EntityPlayer player) {
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
    public double getEnergy() {
        return 0;
    }

    @Override
    public boolean useEnergy(final double v) {
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
    public void onNetworkEvent(final EntityPlayer entityPlayer, final int i) {
        sound = !sound;
        IUCore.network.get(true).updateTileEntityField(this, "sound");

        if (!sound) {
            if (this.getType() == EnumTypeAudio.ON) {
                setType(EnumTypeAudio.OFF);
                IUCore.network.get(true).initiateTileEntityEvent(this, 2, true);

            }
        }
    }

}
