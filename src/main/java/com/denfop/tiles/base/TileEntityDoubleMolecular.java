package com.denfop.tiles.base;

import cofh.redstoneflux.api.IEnergyReceiver;
import com.denfop.Config;
import com.denfop.IUItem;
import com.denfop.Ic2Items;
import com.denfop.api.Recipes;
import com.denfop.container.ContainerBaseDoubleMolecular;
import com.denfop.gui.GuiDoubleMolecularTransformer;
import com.denfop.invslot.InvSlotProcessable;
import com.denfop.invslot.InvSlotProcessableDoubleMolecular;
import com.denfop.items.modules.AdditionModule;
import ic2.api.energy.EnergyNet;
import ic2.api.energy.tile.IEnergySink;
import ic2.api.network.INetworkClientTileEntityEventListener;
import ic2.api.network.INetworkTileEntityEventListener;
import ic2.api.recipe.IRecipeInputFactory;
import ic2.api.recipe.RecipeOutput;
import ic2.core.ContainerBase;
import ic2.core.IC2;
import ic2.core.audio.AudioSource;
import ic2.core.block.comp.Energy;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.enchantment.Enchantment;
import net.minecraft.enchantment.EnchantmentData;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.init.Enchantments;
import net.minecraft.init.Items;
import net.minecraft.init.PotionTypes;
import net.minecraft.item.ItemEnchantedBook;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.potion.PotionUtils;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.RayTraceResult;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import net.minecraftforge.oredict.OreDictionary;

import java.util.ArrayList;
import java.util.List;

public class TileEntityDoubleMolecular extends TileEntityElectricMachine implements INetworkTileEntityEventListener,
        IEnergyReceiver, INetworkClientTileEntityEventListener {

    public List<Double> time;
    public boolean queue;
    public byte redstoneMode;
    public int operationLength;
    public boolean rf = false;
    public int operationsPerTick;
    public AudioSource audioSource;
    public InvSlotProcessable inputSlot;
    public double perenergy;
    public double differenceenergy;
    protected double progress;
    protected double guiProgress;

    public TileEntityDoubleMolecular() {
        super("", 0, 14, 1);
        this.progress = 0;
        this.time = new ArrayList<>();
        this.queue = false;
        this.redstoneMode = 0;
        this.inputSlot = new InvSlotProcessableDoubleMolecular(this, "input", 2);
        this.energy = this.addComponent(Energy.asBasicSink(this, 0, 14).addManagedSlot(this.dischargeSlot));

    }

    public static void init() {


        addrecipe(
                new ItemStack(IUItem.module_schedule, 1),
                PotionUtils.addPotionToItemStack(new ItemStack(Items.POTIONITEM), PotionTypes.LONG_REGENERATION),
                new ItemStack(IUItem.upgrademodule
                        , 1, 17),
                4000000
        );
        addrecipe(
                new ItemStack(IUItem.module_schedule, 1),
                PotionUtils.addPotionToItemStack(new ItemStack(Items.POTIONITEM), PotionTypes.LONG_INVISIBILITY),
                new ItemStack(IUItem.upgrademodule, 1, 22),
                4000000
        );
        addrecipe(new ItemStack(IUItem.module_schedule, 1), new ItemStack(Items.GOLDEN_APPLE, 1, 1),
                new ItemStack(IUItem.upgrademodule, 1, 18), 4000000
        );
        addrecipe(
                new ItemStack(IUItem.module_schedule, 1),
                PotionUtils.addPotionToItemStack(new ItemStack(Items.POTIONITEM), PotionTypes.LONG_POISON),
                new ItemStack(IUItem.upgrademodule, 1, 19),
                4000000
        );
        addrecipe(
                new ItemStack(IUItem.module_schedule, 1),
                new ItemStack(Items.NETHER_STAR, 1),
                new ItemStack(IUItem.upgrademodule, 1,
                        20
                ),
                4000000
        );
        addrecipe(
                new ItemStack(IUItem.module_schedule, 1),
                new ItemStack(IUItem.spawnermodules, 1),
                new ItemStack(IUItem.upgrademodule, 1, 23),
                4000000
        );
        addrecipe(
                new ItemStack(IUItem.module_schedule, 1),
                new ItemStack(Items.BLAZE_ROD, 4),
                new ItemStack(IUItem.upgrademodule, 1,
                        25
                ),
                4000000
        );
        addrecipe(new ItemStack(IUItem.module_schedule, 1), new ItemStack(Blocks.WEB, 1), new ItemStack(IUItem.upgrademodule, 1,
                21
        ), 4000000);

        addrecipe(
                new ItemStack(IUItem.module_schedule, 1),
                new ItemStack(IUItem.itemBatChargeCrystal, 1, OreDictionary.WILDCARD_VALUE),
                new ItemStack(IUItem.upgrademodule, 1, 23),
                4000000
        );

        addrecipe(
                new ItemStack(IUItem.module_schedule, 1),
                IUItem.module1,
                new ItemStack(IUItem.upgrademodule, 1, 0),
                2500000
        );
        addrecipe(
                new ItemStack(IUItem.module_schedule, 1),
                IUItem.module2,
                new ItemStack(IUItem.upgrademodule, 1, 1),
                2500000
        );

        addrecipe(
                new ItemStack(IUItem.module_schedule, 1),
                new ItemStack(IUItem.alloysdoubleplate, 1, 8),
                new ItemStack(IUItem.upgrademodule, 1, 2),
                2500000
        );
        addrecipe(
                new ItemStack(IUItem.module_schedule, 1),
                new ItemStack(IUItem.alloysdoubleplate, 1, 0),
                new ItemStack(IUItem.upgrademodule, 1, 3),
                2500000
        );
        addrecipe(
                new ItemStack(IUItem.module_schedule, 1),
                new ItemStack(IUItem.alloysdoubleplate, 1, 4),
                new ItemStack(IUItem.upgrademodule, 1, 4),
                2500000
        );
        addrecipe(
                new ItemStack(IUItem.module_schedule, 1),
                new ItemStack(IUItem.radiationresources, 4, 1),
                new ItemStack(IUItem.upgrademodule, 1, 5),
                2500000
        );
        addrecipe(
                new ItemStack(IUItem.module_schedule, 1),
                new ItemStack(IUItem.radiationresources, 4, 2),
                new ItemStack(IUItem.upgrademodule, 1, 6),
                2500000
        );
        addrecipe(
                new ItemStack(IUItem.module_schedule, 1),
                PotionUtils.addPotionToItemStack(new ItemStack(Items.POTIONITEM), PotionTypes.LONG_FIRE_RESISTANCE),
                new ItemStack(IUItem.upgrademodule, 1, 7),
                2500000
        );
        addrecipe(
                new ItemStack(IUItem.module_schedule, 1),
                PotionUtils.addPotionToItemStack(new ItemStack(Items.POTIONITEM), PotionTypes.LONG_WATER_BREATHING),
                new ItemStack(IUItem.upgrademodule, 1, 8),
                2500000
        );
        addrecipe(
                new ItemStack(IUItem.module_schedule, 1),
                PotionUtils.addPotionToItemStack(new ItemStack(Items.POTIONITEM), PotionTypes.LONG_SWIFTNESS),
                new ItemStack(IUItem.upgrademodule, 1, 9),
                2500000
        );
        addrecipe(
                new ItemStack(IUItem.module_schedule, 1),
                new ItemStack(IUItem.alloysdoubleplate, 1, 6),
                new ItemStack(IUItem.upgrademodule, 1, 10),
                2500000
        );

        addrecipe(
                new ItemStack(IUItem.module_schedule, 1),
                PotionUtils.addPotionToItemStack(new ItemStack(Items.POTIONITEM), PotionTypes.LONG_STRENGTH),
                new ItemStack(IUItem.upgrademodule, 1, 11),
                2500000
        );
        addrecipe(
                new ItemStack(IUItem.module_schedule, 1),
                new ItemStack(IUItem.radiationresources, 2, 3),
                new ItemStack(IUItem.upgrademodule, 1, 13),
                2500000
        );
        addrecipe(
                new ItemStack(IUItem.module_schedule, 1),
                PotionUtils.addPotionToItemStack(new ItemStack(Items.POTIONITEM), PotionTypes.SWIFTNESS),
                new ItemStack(IUItem.upgrademodule, 1, 14),
                2500000
        );
        addrecipe(
                new ItemStack(IUItem.module_schedule, 1),
                IUItem.module3,
                new ItemStack(IUItem.upgrademodule, 1, 15),
                2500000
        );
        addrecipe(
                new ItemStack(IUItem.module_schedule, 1),
                new ItemStack(Ic2Items.energyCrystal.getItem(), 1, OreDictionary.WILDCARD_VALUE),
                new ItemStack(IUItem.upgrademodule, 1, 16),
                2500000
        );

        addrecipe(IUItem.module1, IUItem.module1, IUItem.genmodule, 7500000);
        addrecipe(
                IUItem.genmodule,
                IUItem.genmodule,
                IUItem.genmodule1,
                10000000
        );
        addrecipe(
                IUItem.module2,
                IUItem.module2,
                IUItem.gennightmodule,
                7500000
        );
        addrecipe(
                IUItem.gennightmodule,
                IUItem.gennightmodule,
                IUItem.gennightmodule1,
                10000000
        );
        addrecipe(
                IUItem.module3,
                IUItem.module3,
                IUItem.storagemodule,
                7500000
        );
        addrecipe(
                IUItem.storagemodule,
                IUItem.storagemodule,
                IUItem.storagemodule1,
                10000000
        );
        addrecipe(
                IUItem.module4,
                IUItem.module4,
                IUItem.outputmodule,
                7500000
        );
        addrecipe(
                IUItem.outputmodule,
                IUItem.outputmodule,
                IUItem.outputmodule1,
                10000000
        );
        addrecipe(
                new ItemStack(IUItem.entitymodules, 1, 1),
                new ItemStack(IUItem.entitymodules, 1, 1),
                new ItemStack(IUItem.spawnermodules, 1, 6),
                20000000
        );
        addrecipe(
                new ItemStack(IUItem.spawnermodules, 1, 6),
                new ItemStack(IUItem.spawnermodules, 1, 6),
                new ItemStack(IUItem.spawnermodules, 1, 7),
                20000000
        );

        addrecipe(
                IUItem.phase_module,
                IUItem.phase_module,
                IUItem.phase_module1,
                7500000
        );
        addrecipe(
                IUItem.phase_module1,
                IUItem.phase_module1,
                IUItem.phase_module2,
                10000000
        );
        addrecipe(
                IUItem.moonlinse_module,
                IUItem.moonlinse_module,
                IUItem.moonlinse_module1,
                7500000
        );
        addrecipe(
                IUItem.moonlinse_module1,
                IUItem.moonlinse_module1,
                IUItem.moonlinse_module2,
                10000000
        );
        addrecipe(
                new ItemStack(IUItem.module_schedule, 1),
                new ItemStack(Blocks.LAPIS_BLOCK, 1),
                new ItemStack(IUItem.upgrademodule, 1, 26),
                2500000
        );
        addrecipe(
                new ItemStack(IUItem.module_schedule, 1),
                new ItemStack(Blocks.REDSTONE_BLOCK, 1),
                new ItemStack(IUItem.upgrademodule, 1, 27),
                2500000
        );
        addrecipe(
                new ItemStack(IUItem.module_schedule, 1),
                new ItemStack(IUItem.module9, 1),
                new ItemStack(IUItem.upgrademodule, 1, 28),
                1500000
        );
        addrecipe(
                new ItemStack(IUItem.module_schedule, 1),
                PotionUtils.addPotionToItemStack(new ItemStack(Items.POTIONITEM), PotionTypes.NIGHT_VISION),
                new ItemStack(IUItem.upgrademodule, 1, 29),
                1500000
        );
        addrecipe(
                new ItemStack(IUItem.module_schedule, 1),
                ItemEnchantedBook.getEnchantedItemStack(new EnchantmentData(Enchantments.THORNS,1)),
                new ItemStack(IUItem.upgrademodule, 1, 30),
                1500000
        );
        addrecipe(
                new ItemStack(IUItem.module_schedule, 1),
                new ItemStack(IUItem.spawnermodules, 1,5),
                new ItemStack(IUItem.upgrademodule, 1, 31),
                1500000
        );
        addrecipe(
                new ItemStack(IUItem.module_schedule, 1),
                PotionUtils.addPotionToItemStack(new ItemStack(Items.POTIONITEM), PotionTypes.STRONG_HARMING),

                new ItemStack(IUItem.upgrademodule, 1, 32),
                1500000
        );
        addrecipe(
                new ItemStack(IUItem.module_schedule, 1),
                ItemEnchantedBook.getEnchantedItemStack(new EnchantmentData(Enchantments.PROJECTILE_PROTECTION,1)),
                new ItemStack(IUItem.upgrademodule, 1, 33),
                1500000
        );
        addrecipe(
                new ItemStack(IUItem.module_schedule, 1),
                ItemEnchantedBook.getEnchantedItemStack(new EnchantmentData(Enchantments.FEATHER_FALLING,1)),
                new ItemStack(IUItem.upgrademodule, 1, 34),
                1500000
        );
        addrecipe(
                new ItemStack(IUItem.module_schedule, 1),
                new ItemStack(IUItem.machines_base, 1,2),
                new ItemStack(IUItem.upgrademodule, 1, 35),
                1500000
        );
        addrecipe(
                new ItemStack(IUItem.module_schedule, 1),
                new ItemStack(IUItem.machines_base1, 1,9),
                new ItemStack(IUItem.upgrademodule, 1, 36),
                1500000
        );
        addrecipe(
                new ItemStack(IUItem.module_schedule, 1),
                "doubleplateInvar",
                new ItemStack(IUItem.upgrademodule, 1, 37),
                1500000
        );
        addrecipe(
                new ItemStack(IUItem.module_schedule, 1),
                new ItemStack(Items.FISH, 1,3),
                new ItemStack(IUItem.upgrademodule, 1, 38),
                1500000
        );
        addrecipe(
                new ItemStack(IUItem.module_schedule, 1),
                Ic2Items.generator,
                new ItemStack(IUItem.upgrademodule, 1, 39),
                1500000
        );
    }

    public static void addrecipe(ItemStack stack, ItemStack stack2, ItemStack stack1, double energy) {
        NBTTagCompound nbt = new NBTTagCompound();
        nbt.setDouble("energy", energy);
        final IRecipeInputFactory input = ic2.api.recipe.Recipes.inputFactory;
        Recipes.doublemolecular.addRecipe(input.forStack(stack), input.forStack(stack2), nbt, stack1);
    }
    public static void addrecipe(ItemStack stack, String stack2, ItemStack stack1, double energy) {
        NBTTagCompound nbt = new NBTTagCompound();
        nbt.setDouble("energy", energy);
        final IRecipeInputFactory input = ic2.api.recipe.Recipes.inputFactory;
        Recipes.doublemolecular.addRecipe(input.forStack(stack), input.forOreDict(stack2), nbt, stack1);
    }
    @Override
    protected ItemStack getPickBlock(final EntityPlayer player, final RayTraceResult target) {
        return new ItemStack(IUItem.blockdoublemolecular);
    }

    @Override
    public ContainerBase<? extends TileEntityDoubleMolecular> getGuiContainer(EntityPlayer entityPlayer) {
        return new ContainerBaseDoubleMolecular(entityPlayer, this);
    }

    @Override
    protected boolean onActivated(
            final EntityPlayer player,
            final EnumHand hand,
            final EnumFacing side,
            final float hitX,
            final float hitY,
            final float hitZ
    ) {
        if (player.getHeldItem(hand).getItem() instanceof AdditionModule && player.getHeldItem(hand).getItemDamage() == 4) {
            if (!this.rf) {
                this.rf = true;
                player.getHeldItem(hand).setCount(player.getHeldItem(hand).getCount() - 1);
                return true;
            }
        }
        return super.onActivated(player, hand, side, hitX, hitY, hitZ);
    }

    protected boolean doesSideBlockRendering(EnumFacing side) {
        return false;
    }

    @SideOnly(Side.CLIENT)
    protected boolean shouldSideBeRendered(EnumFacing side, BlockPos otherPos) {
        return false;
    }

    @Override
    protected boolean isNormalCube() {
        return false;
    }

    public List<String> getNetworkedFields() {
        List<String> ret = super.getNetworkedFields();
        ret.add("guiProgress");
        ret.add("queue");
        ret.add("redstoneMode");
        ret.add("energy");
        ret.add("perenergy");
        ret.add("differenceenergy");
        ret.add("time");

        return ret;
    }

    public boolean shouldRenderInPass(int pass) {
        return true;
    }

    public String getInventoryName() {
        return "Molecular Transformer";
    }

    public void readFromNBT(NBTTagCompound nbttagcompound) {
        super.readFromNBT(nbttagcompound);


        this.queue = nbttagcompound.getBoolean("queue");
        this.redstoneMode = nbttagcompound.getByte("redstoneMode");

        this.progress = nbttagcompound.getDouble("progress");
        this.rf = nbttagcompound.getBoolean("rf");

    }

    public NBTTagCompound writeToNBT(NBTTagCompound nbttagcompound) {
        super.writeToNBT(nbttagcompound);
        nbttagcompound.setByte("redstoneMode", this.redstoneMode);
        nbttagcompound.setBoolean("queue", this.queue);
        nbttagcompound.setDouble("progress", this.progress);
        nbttagcompound.setBoolean("rf", this.rf);
        return nbttagcompound;

    }

    @SideOnly(Side.CLIENT)
    public GuiScreen getGui(EntityPlayer entityPlayer, boolean isAdmin) {
        return new GuiDoubleMolecularTransformer(new ContainerBaseDoubleMolecular(entityPlayer, this));
    }


    public void onNetworkEvent(EntityPlayer player, int event) {

        if (event == 0) {
            this.redstoneMode = (byte) (this.redstoneMode + 1);
            if (this.redstoneMode >= 8) {
                this.redstoneMode = 0;
            }
            this.getWorld().notifyBlockUpdate(this.getPos(), this.getBlockState(), this.getBlockState(), 2);
        }
        if (event == 1) {
            this.queue = !this.queue;
            setOverclockRates();
        }

    }


    public void markDirty() {
        super.markDirty();
        if (IC2.platform.isSimulating()) {
            setOverclockRates();
        }
    }

    public void operate(RecipeOutput output) {
        List<ItemStack> processResult = output.items;
        operateOnce(processResult);
    }

    public void operate(RecipeOutput output, int size) {
        List<ItemStack> processResult = output.items;
        operateOnce(processResult, size);
    }

    public void operateOnce(List<ItemStack> processResult) {
        if (this.outputSlot.canAdd(processResult)) {
            this.inputSlot.consume();
            this.outputSlot.add(processResult);
        }
    }

    public void operateOnce(List<ItemStack> processResult, int size) {
        for (int i = 0; i < size; i++) {
            if (this.outputSlot.canAdd(processResult)) {
                this.inputSlot.consume();
                this.outputSlot.add(processResult);
            }
        }
    }

    public void setOverclockRates() {


        RecipeOutput output = getOutput();

        if (!this.queue) {
            if (inputSlot.isEmpty()) {
                this.energy.setCapacity(0);
            } else if (output != null) {
                this.energy.setCapacity(output.metadata.getDouble("energy"));
            } else {
                this.energy.setCapacity(0);
            }
        } else {

            if (inputSlot.isEmpty()) {
                this.energy.setCapacity(0);
            } else if (output != null) {

                int size = 0;
                int size2 = 0;
                boolean getrecipe = false;
                ItemStack output1 = null;
                for (int i = 0; !getrecipe; i++) {
                    for (int j = 0; j < 64; j++) {
                        ItemStack stack = new ItemStack(this.inputSlot.get(0).getItem(), i, this.inputSlot.get().getItemDamage());
                        ItemStack stack1 = new ItemStack(
                                this.inputSlot.get(1).getItem(),
                                j,
                                this.inputSlot.get(1).getItemDamage()
                        );

                        if (Recipes.doublemolecular.getOutputFor(stack, stack1, false, false) != null) {
                            size = i;
                            size2 = j;
                            getrecipe = true;
                            output1 = Recipes.doublemolecular.getOutputFor(stack, stack1, false, false).items.get(0);
                            break;

                        }
                    }
                }

                size = (int) Math.floor((float) this.inputSlot.get(0).stackSize / size);
                size2 = (int) Math.floor((float) this.inputSlot.get(1).stackSize / size2);
                size = Math.min(size, size2);
                int size1 = this.outputSlot.get() != null
                        ? (64 - this.outputSlot.get().stackSize) / output1.stackSize
                        : 64 / output1.stackSize;
                size = Math.min(size1, size);
                size = Math.min(output1.getMaxStackSize(), size);

                this.energy.setCapacity(output.metadata.getDouble("energy") * size);
            } else {
                this.energy.setCapacity(0);
            }
        }


    }

    public void updateEntityServer() {
        super.updateEntityServer();

        RecipeOutput output = getOutput();

        IC2.network.get(true).updateTileEntityField(this, "redstoneMode");
        if (!queue) {
            if (output != null) {
                this.differenceenergy = this.energy.getEnergy() - this.perenergy;
                this.perenergy = this.energy.getEnergy();
                markDirty();
                setActive(true);
                IC2.network.get(true).initiateTileEntityEvent(this, 0, true);
                if (this.getWorld().provider.getWorldTime() % 200 == 0) {
                    IC2.network.get(true).initiateTileEntityEvent(this, 2, true);
                }


                this.progress = this.energy.getEnergy();
                double k = this.progress;
                this.guiProgress = (k / output.metadata.getDouble("energy"));
                if (this.energy.getEnergy() >= output.metadata.getDouble("energy")) {
                    operate(output);

                    this.progress = 0;
                    this.energy.useEnergy(this.energy.getEnergy());

                    IC2.network.get(true).initiateTileEntityEvent(this, 2, true);
                }
            } else {
                if (this.energy.getEnergy() != 0 && getActive()) {
                    IC2.network.get(true).initiateTileEntityEvent(this, 1, true);
                }
                this.energy.useEnergy(this.energy.getEnergy());
                this.energy.setCapacity(0);
                setActive(false);
            }

        } else {
            if (output != null) {
                setActive(true);
                markDirty();
                this.differenceenergy = this.energy.getEnergy() - this.perenergy;
                this.perenergy = this.energy.getEnergy();
                IC2.network.get(true).initiateTileEntityEvent(this, 0, true);
                if (this.getWorld().provider.getWorldTime() % 200 == 0) {
                    IC2.network.get(true).initiateTileEntityEvent(this, 2, true);
                }

                int size = 0;
                int size2 = 0;
                boolean getrecipe = false;
                ItemStack output1 = null;
                for (int i = 0; !getrecipe; i++) {
                    for (int j = 0; j < 64; j++) {
                        ItemStack stack = new ItemStack(this.inputSlot.get(0).getItem(), i, this.inputSlot.get().getItemDamage());
                        ItemStack stack1 = new ItemStack(
                                this.inputSlot.get(1).getItem(),
                                j,
                                this.inputSlot.get(1).getItemDamage()
                        );

                        if (Recipes.doublemolecular.getOutputFor(stack, stack1, false, false) != null) {
                            size = i;
                            size2 = j;
                            getrecipe = true;
                            output1 = Recipes.doublemolecular.getOutputFor(stack, stack1, false, false).items.get(0);
                            break;

                        }
                    }
                }

                size = (int) Math.floor((float) this.inputSlot.get(0).stackSize / size);
                size2 = (int) Math.floor((float) this.inputSlot.get(1).stackSize / size2);
                size = Math.min(size, size2);
                int size1 = this.outputSlot.get() != null
                        ? (64 - this.outputSlot.get().stackSize) / output1.stackSize
                        : 64 / output1.stackSize;
                size = Math.min(size1, size);
                size = Math.min(output1.getMaxStackSize(), size);
                this.progress = this.energy.getEnergy();
                double k = this.progress;
                double p = (k / (output.metadata.getDouble("energy") * size));
                if (p <= 1) {
                    this.guiProgress = p;
                }
                if (p > 1) {
                    this.guiProgress = 1;
                }
                if (this.energy.getEnergy() >= (output.metadata.getDouble("energy") * size)) {
                    operate(output, size);

                    this.progress = 0;
                    this.energy.useEnergy(this.energy.getEnergy());

                    IC2.network.get(true).initiateTileEntityEvent(this, 2, true);
                }
            } else {
                if (this.energy.getEnergy() != 0 && getActive()) {
                    IC2.network.get(true).initiateTileEntityEvent(this, 1, true);
                }
                this.energy.useEnergy(this.energy.getEnergy());
                this.energy.setCapacity(0);
                setActive(false);
            }
        }
        if (getActive() && output == null) {
            setActive(false);
        }
    }

    public RecipeOutput getOutput() {
        if (this.inputSlot.isEmpty()) {
            return null;
        }
        RecipeOutput output = this.inputSlot.process();
        if (output == null) {
            return null;
        }
        if (this.outputSlot.canAdd(output.items)) {
            return output;
        }
        return null;
    }

    public double getProgress() {
        return Math.min(this.energy.getFillRatio(), 1);
    }


    @Override
    public int receiveEnergy(final EnumFacing enumFacing, final int i, final boolean b) {
        if (this.rf) {
            return receiveEnergy(i, b);
        } else {
            return 0;
        }
    }

    public int receiveEnergy(int paramInt, boolean paramBoolean) {
        int i = (int) Math.min(
                ((IEnergySink)this.energy.getDelegate()).getDemandedEnergy() * Config.coefficientrf,
                Math.min(EnergyNet.instance.getPowerFromTier(14) * Config.coefficientrf, paramInt)
        );
        if (!paramBoolean) {
            this.energy.addEnergy(i * 1F/ Config.coefficientrf);
        }
        return i;
    }

    public String getStartSoundFile() {
        return "Machines/molecular.ogg";
    }

    @Override
    public int getEnergyStored(final EnumFacing enumFacing) {
        return (int) this.energy.getEnergy();
    }

    @Override
    public int getMaxEnergyStored(final EnumFacing enumFacing) {
        return (int) this.energy.getCapacity();
    }

    @Override
    public boolean canConnectEnergy(final EnumFacing enumFacing) {
        return true;
    }

}
