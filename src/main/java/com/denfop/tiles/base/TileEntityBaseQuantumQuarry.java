package com.denfop.tiles.base;

import com.denfop.Config;
import com.denfop.IUCore;
import com.denfop.IUItem;
import com.denfop.Ic2Items;
import com.denfop.audio.AudioSource;
import com.denfop.container.ContainerQuantumQuarry;
import com.denfop.gui.GUIQuantumQuarry;
import com.denfop.invslot.InvSlotQuantumQuarry;
import com.denfop.items.modules.EnumQuarryModules;
import com.denfop.items.modules.EnumQuarryType;
import com.denfop.utils.ModUtils;
import ic2.api.network.INetworkTileEntityEventListener;
import ic2.api.upgrade.IUpgradableBlock;
import ic2.api.upgrade.IUpgradeItem;
import ic2.api.upgrade.UpgradableProperty;
import ic2.core.ContainerBase;
import ic2.core.IC2;
import ic2.core.IHasGui;
import ic2.core.audio.PositionSpec;
import ic2.core.block.TileEntityInventory;
import ic2.core.block.comp.Energy;
import ic2.core.block.invslot.InvSlotOutput;
import ic2.core.init.Localization;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.math.BlockPos;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import net.minecraftforge.oredict.OreDictionary;

import java.util.EnumSet;
import java.util.List;
import java.util.Random;
import java.util.Set;

public class TileEntityBaseQuantumQuarry extends TileEntityInventory implements IHasGui, INetworkTileEntityEventListener,
        IUpgradableBlock {

    private static boolean analyzer;
    private final String name;
    private int progress;

    public AudioSource audioSource;

    public static final Random rand = new Random();

    public final int energyconsume;

    public double getblock;
    public final InvSlotQuantumQuarry inputslotB;
    public final InvSlotOutput outputSlot;
    public final InvSlotQuantumQuarry inputslot;

    public final InvSlotQuantumQuarry inputslotA;
    public Energy energy;

    public TileEntityBaseQuantumQuarry(String name, int coef) {

        this.progress = 0;
        this.name = name;
        this.getblock = 0;
        this.energyconsume = 25000 * coef;
        energy = this.addComponent(Energy.asBasicSink(this, 5E7D, 14));
        this.inputslot = new InvSlotQuantumQuarry(this, 25, "input", 0);
        this.inputslotA = new InvSlotQuantumQuarry(this, 26, "input1", 1);
        this.inputslotB = new InvSlotQuantumQuarry(this, 27, "input2", 2);
        this.outputSlot = new InvSlotOutput(this, "output", 24);

        analyzer = false;
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


    }

    protected void onUnloaded() {
        super.onUnloaded();
        if (IC2.platform.isRendering() && this.audioSource != null) {
            IC2.audioManager.removeSources(this);
            this.audioSource = null;
        }

    }

    public void markDirty() {
        super.markDirty();


    }

    protected void updateEntityServer() {
        super.updateEntityServer();
        double proccent = this.energyconsume;
        boolean vein = false;
        if (getWorld().provider.getWorldTime() % 20 == 0) {
            analyzer = !this.inputslotB.isEmpty();
            int chunkx =
                    this.getWorld().getChunkFromBlockCoords(this.pos).x * 16;
            int chunkz = this.getWorld().getChunkFromBlockCoords(this.pos).z * 16;
            if (getWorld().getTileEntity(new BlockPos(chunkx, 0, chunkz)) != null && getWorld().getTileEntity(new BlockPos(
                    chunkx,
                    0,
                    chunkz
            )) instanceof TileEntityVein) {
                vein = true;
            }
        }
        if (analyzer && vein) {

            int chunkx =
                    this.getWorld().getChunkFromBlockCoords(this.pos).x * 16;
            int chunkz = this.getWorld().getChunkFromBlockCoords(this.pos).z * 16;
            int y = 0;

            if (this.getWorld().getTileEntity(new BlockPos(chunkx, 0, chunkz)) != null && this
                    .getWorld()
                    .getTileEntity(new BlockPos(chunkx, 0, chunkz)) instanceof TileEntityVein) {
                TileEntityVein tile = (TileEntityVein) this.getWorld().getTileEntity(new BlockPos(chunkx, 0, chunkz));
                if (tile.number > 0) {
                    if (this.inputslot.get() != null) {
                        EnumQuarryModules module = EnumQuarryModules.getFromID(this.inputslot.get().getItemDamage());
                        EnumQuarryType type = module.type;

                        if (type == EnumQuarryType.SPEED) {
                            proccent = this.energyconsume - this.energyconsume * 0.05 * module.efficiency;
                        }

                    }
                    if (this.energy.getEnergy() >= proccent && this.outputSlot.canAdd(new ItemStack(IUItem.heavyore, 1,
                                    this
                                            .getWorld()
                                            .getBlockState(new BlockPos(chunkx, 0, chunkz))
                                            .getBlock()
                                            .getMetaFromState(this.getWorld().getBlockState(new BlockPos(chunkx, 0, chunkz)))
                            )
                    )) {
                        this.setActive(true);
                        this.energy.useEnergy(proccent);
                        this.getblock++;
                        this.outputSlot.add(new ItemStack(IUItem.heavyore, 1,
                                this
                                        .getWorld()
                                        .getBlockState(new BlockPos(chunkx, 0, chunkz))
                                        .getBlock()
                                        .getMetaFromState(this.getWorld().getBlockState(new BlockPos(chunkx, 0, chunkz)))
                        ));
                        tile.number--;
                        return;
                    }

                }
            }


        }
        if (analyzer && !vein && !Config.enableonlyvein) {
            double col = 1;
            int chance2 = 0;
            boolean furnace = false;
            if (!this.inputslot.isEmpty()) {
                ItemStack type1 = this.inputslot.get();
                EnumQuarryModules module = EnumQuarryModules.getFromID(type1.getItemDamage());
                EnumQuarryType type = module.type;

                switch (type) {
                    case SPEED:
                        proccent = this.energyconsume - this.energyconsume * 0.05 * module.efficiency;
                        break;
                    case DEPTH:
                        col = module.efficiency * module.efficiency;
                        proccent = this.energyconsume * Math.pow(1.1, module.meta - 8);
                        break;
                    case LUCKY:
                        chance2 = module.efficiency;
                        break;
                    case FURNACE:
                        furnace = true;
                        break;

                }
            }
            EnumQuarryModules list_check = null;
            if (!this.inputslotA.get().isEmpty()) {
                EnumQuarryModules module = EnumQuarryModules.getFromID(this.inputslotA.get().getItemDamage());
                EnumQuarryType type = module.type;

                switch (type) {
                    case WHITELIST:
                    case BLACKLIST:
                        list_check = module;
                        break;

                }
            }
            for (double i = 0; i < col; i++) {
                if (this.energy.getEnergy() >= proccent) {
                    this.setActive(true);
                    this.energy.useEnergy(proccent);
                    int chance = rand.nextInt(100) + 1;
                    this.getblock++;
                    initiate(0);
                    if (chance > 95) {
                        if (checkinventoy()) {
                            return;
                        }

                        if (furnace) {
                            List<ItemStack> list = IUCore.get_ingot;
                            int num = list.size();
                            int chance1 = rand.nextInt(num);
                            if (!list(list_check, list.get(chance1))) {
                                if (this.outputSlot.canAdd(list.get(chance1))) {
                                    this.outputSlot.add(list.get(chance1));
                                }
                            }
                        } else {
                            List<ItemStack> list = IUCore.list;
                            int num = list.size();
                            int chance1 = rand.nextInt(num);
                            if (!list(list_check, list.get(chance1))) {
                                if (OreDictionary.getOreIDs(list.get(chance1)).length > 0) {
                                    if ((!OreDictionary
                                            .getOreName(OreDictionary.getOreIDs(list.get(chance1))[0])
                                            .startsWith("gem") && !OreDictionary
                                            .getOreName(OreDictionary.getOreIDs(list.get(chance1))[0])
                                            .startsWith("shard")
                                            && list.get(chance1).getItem() != Items.REDSTONE && list
                                            .get(chance1)
                                            .getItem() != Items.DYE && list.get(chance1).getItem() != Items.COAL && list
                                            .get(chance1)
                                            .getItem() != Items.GLOWSTONE_DUST) && chance2 >= 0) {
                                        if (this.outputSlot.canAdd(list.get(chance1))) {
                                            this.outputSlot.add(list.get(chance1));
                                        }
                                    } else {
                                        for (int j = 0; j < chance2 + 1; j++) {
                                            if (this.outputSlot.canAdd(list.get(chance1))) {
                                                this.outputSlot.add(list.get(chance1));
                                            }
                                        }
                                    }
                                }
                            }
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
        if (this.getWorld().provider.getWorldTime() % 200 == 0) {
            initiate(2);
        }
        if (getActive()) {
            ItemStack stack3 = Ic2Items.ejectorUpgrade;
            ((IUpgradeItem) stack3.getItem()).onTick(stack3, this);
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
                        IC2.audioManager.playOnce(
                                this,
                                PositionSpec.Center,
                                this.getInterruptSoundFile(),
                                false,
                                IC2.audioManager.getDefaultVolume()
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
        String stack = OreDictionary.getOreName(OreDictionary.getOreIDs(stack1)[0]);
        if (type.type == EnumQuarryType.BLACKLIST) {
            for (int j = 0; j < 9; j++) {
                String l = "number_" + j;
                String temp = ModUtils.NBTGetString(this.inputslotA.get(), l);
                if (temp.startsWith("ore") || temp.startsWith("gem") || temp.startsWith("dust") || temp.startsWith("shard")) {
                    if (temp.equals(stack)) {
                        return true;
                    }

                }
            }

            return false;


        } else if (type.type == EnumQuarryType.WHITELIST) {
            for (int j = 0; j < 9; j++) {
                String l = "number_" + j;
                String temp = ModUtils.NBTGetString(this.inputslotA.get(), l);
                if (temp.startsWith("ore") || temp.startsWith("gem") || temp.startsWith("dust") || temp.startsWith("shard")) {

                    if (temp.equals(stack)) {
                        return false;
                    }

                }

            }
            return true;
        }
        return false;
    }

    @SuppressWarnings("BooleanMethodIsAlwaysInverted")
    public static boolean list(TileEntityBaseQuantumQuarry tile, ItemStack stack1) {
        if (tile.inputslotA.isEmpty()) {
            return false;
        }
        EnumQuarryModules module = EnumQuarryModules.getFromID(tile.inputslotA.get().getItemDamage());
        EnumQuarryType type = module.type;
        String stack = OreDictionary.getOreName(OreDictionary.getOreIDs(stack1)[0]);
        if (type == EnumQuarryType.BLACKLIST) {
            for (int j = 0; j < 9; j++) {
                String l = "number_" + j;
                String temp = ModUtils.NBTGetString(tile.inputslotA.get(), l);
                if (temp.startsWith("ore") || temp.startsWith("gem") || temp.startsWith("dust") || temp.startsWith("shard")) {
                    if (temp.equals(stack)) {
                        return true;
                    }

                }
            }

            return false;


        } else if (type == EnumQuarryType.WHITELIST) {
            for (int j = 0; j < 9; j++) {
                String l = "number_" + j;
                String temp = ModUtils.NBTGetString(tile.inputslotA.get(), l);
                if (temp.startsWith("ore") || temp.startsWith("gem") || temp.startsWith("dust") || temp.startsWith("shard")) {

                    if (temp.equals(stack)) {
                        return false;
                    }

                }

            }
            return true;
        }
        return false;
    }

    private boolean checkinventoy() {
        for (int i = 0; i < this.outputSlot.size(); i++) {
            if (this.outputSlot.get(i) == null) {
                return false;
            }
            if (this.outputSlot.get(i).getCount() != 64) {

                return false;
            }
        }
        return true;
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
