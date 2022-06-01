package com.denfop.tiles.base;

import com.denfop.Config;
import com.denfop.IUCore;
import com.denfop.IUItem;
import com.denfop.Ic2Items;
import com.denfop.api.vein.Type;
import com.denfop.api.vein.Vein;
import com.denfop.api.vein.VeinSystem;
import com.denfop.audio.AudioSource;
import com.denfop.audio.PositionSpec;
import com.denfop.componets.QEComponent;
import com.denfop.container.ContainerQuantumQuarry;
import com.denfop.gui.GUIQuantumQuarry;
import com.denfop.invslot.InvSlotQuantumQuarry;
import com.denfop.items.modules.EnumQuarryModules;
import com.denfop.items.modules.EnumQuarryType;
import ic2.api.network.INetworkTileEntityEventListener;
import ic2.api.upgrade.IUpgradableBlock;
import ic2.api.upgrade.IUpgradeItem;
import ic2.api.upgrade.UpgradableProperty;
import ic2.core.ContainerBase;
import ic2.core.IC2;
import ic2.core.IHasGui;
import ic2.core.block.TileEntityInventory;
import ic2.core.block.invslot.InvSlotOutput;
import ic2.core.init.Localization;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.EnumFacing;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import net.minecraftforge.oredict.OreDictionary;

import java.util.ArrayList;
import java.util.EnumSet;
import java.util.List;
import java.util.Random;
import java.util.Set;

public class TileEntityBaseQuantumQuarry extends TileEntityInventory implements IHasGui, INetworkTileEntityEventListener,
        IUpgradableBlock {

    public final Random rand = new Random();
    public final int energyconsume;
    public final InvSlotQuantumQuarry inputslotB;
    public final InvSlotOutput outputSlot;
    public final InvSlotQuantumQuarry inputslot;
    public final InvSlotQuantumQuarry inputslotA;
    private final String name;
    public double consume;
    public boolean mac_enabled = false;
    public boolean comb_mac_enabled = false;
    public boolean furnace;
    public int chance;
    public int col;
    public List<ItemStack> list;
    public AudioSource audioSource;
    public double getblock;
    public QEComponent energy;
    public boolean analyzer;
    public int progress;
    public EnumQuarryModules list_modules;
    public Vein vein;
    public List<ItemStack> main_list = new ArrayList<>(IUCore.list);
    public boolean original = true;
    public boolean can_dig_vein = true;

    public TileEntityBaseQuantumQuarry(String name, int coef) {

        this.progress = 0;
        this.name = name;
        this.getblock = 0;
        this.energyconsume = Config.enerycost * coef;
        this.energy = this.addComponent(QEComponent.asBasicSink(this, 5E7D, 14));
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
    }

    public NBTTagCompound writeToNBT(NBTTagCompound nbttagcompound) {
        super.writeToNBT(nbttagcompound);
        nbttagcompound.setDouble("getblock", this.getblock);
        nbttagcompound.setInteger("progress", this.progress);
        return nbttagcompound;
    }

    protected void initiate(int soundEvent) {
        IC2.network.get(true).initiateTileEntityEvent(this, soundEvent, true);
    }

    protected void onLoaded() {
        super.onLoaded();
        this.inputslot.update();
        this.inputslotA.update();
        this.inputslotB.update();
        this.vein = VeinSystem.system.getVein(this.getWorld().getChunkFromBlockCoords(this.pos).getPos());
        if (this.vein != null) {
            final ItemStack stack = new ItemStack(IUItem.heavyore, 1, vein.getMeta());
            if (list(this.list_modules, stack)) {
                this.can_dig_vein = false;
            }
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
        this.vein = VeinSystem.system.getVein(this.getWorld().getChunkFromBlockCoords(this.pos).getPos());
    }

    protected void updateEntityServer() {
        super.updateEntityServer();
        double proccent = this.consume;

        if (this.vein != null) {
            if (this.analyzer && this.vein.get()) {
                if (this.vein.getType() == Type.VEIN && this.vein.getCol() > 0) {
                    final ItemStack stack = new ItemStack(IUItem.heavyore, 1, vein.getMeta());
                    if (!this.can_dig_vein) {
                        this.setActive(true);
                        if (this.outputSlot.canAdd(stack)) {
                            this.energy.useEnergy(proccent);
                            this.getblock++;
                            this.outputSlot.add(stack);
                            this.vein.removeCol(1);
                        }
                    }
                }
            }
        }
        if (this.analyzer && !Config.enableonlyvein) {
            double col = this.col;
            int chance2 = this.chance;
            int coble = rand.nextInt((int) col + 1);
            this.getblock += coble;
            col -= coble;
            for (double i = 0; i < col; i++) {
                if (this.energy.getEnergy() >= proccent) {
                    if (!this.getActive()) {
                        this.setActive(true);
                        initiate(0);
                    }
                    this.energy.useEnergy(proccent);
                    this.getblock++;
                    int num = main_list.size();
                    int chance1 = rand.nextInt(num);
                    ItemStack stack = main_list.get(chance1);
                    if (this.original) {
                        if (OreDictionary.getOreIDs(stack).length > 0) {
                            String name = OreDictionary.getOreName(OreDictionary.getOreIDs(stack)[0]);
                            if ((!name
                                    .startsWith("gem") && !name
                                    .startsWith("shard")
                                    && stack.getItem() != Items.REDSTONE && stack
                                    .getItem() != Items.DYE && stack.getItem() != Items.COAL && stack
                                    .getItem() != Items.GLOWSTONE_DUST) && chance2 >= 1) {
                                if (this.outputSlot.canAdd(stack)) {
                                    this.outputSlot.add(stack);
                                }
                            } else {
                                for (int j = 0; j < chance2 + 1; j++) {
                                    if (this.outputSlot.canAdd(stack)) {
                                        this.outputSlot.add(stack);
                                    }
                                }
                            }
                        }
                    } else {
                        if (this.outputSlot.canAdd(main_list.get(chance1))) {
                            this.outputSlot.add(main_list.get(chance1));
                        }
                    }


                } else {
                    this.setActive(false);
                    initiate(2);
                }
            }
        } else {
            initiate(2);
            this.setActive(false);
        }

        if (getActive()) {
            if (this.world.getWorldTime() % 8 == 0) {
                ItemStack stack3 = Ic2Items.ejectorUpgrade;
                ((IUpgradeItem) stack3.getItem()).onTick(stack3, this);
            }
        }
    }

    public ContainerBase<? extends TileEntityBaseQuantumQuarry> getGuiContainer(EntityPlayer player) {
        return new ContainerQuantumQuarry(player, this);

    }

    @SideOnly(Side.CLIENT)
    public GuiScreen getGui(EntityPlayer player, boolean isAdmin) {

        return new GUIQuantumQuarry(new ContainerQuantumQuarry(player, this));
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

            return this.list.contains(stack1);


        } else if (type.type == EnumQuarryType.WHITELIST) {

            return !this.list.contains(stack1);

        }
        return false;
    }

    public String getInventoryName() {

        return Localization.translate(name);
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

}
