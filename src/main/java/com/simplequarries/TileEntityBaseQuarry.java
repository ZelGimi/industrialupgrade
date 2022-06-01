package com.simplequarries;

import com.denfop.IUCore;
import com.denfop.Ic2Items;
import com.denfop.api.Recipes;
import com.denfop.api.recipe.BaseMachineRecipe;
import com.denfop.api.vein.Vein;
import com.denfop.audio.AudioSource;
import com.denfop.audio.PositionSpec;
import com.denfop.componets.AdvEnergy;
import com.denfop.componets.QEComponent;
import com.denfop.items.modules.EnumQuarryModules;
import com.denfop.items.modules.EnumQuarryType;
import com.denfop.tiles.base.FakePlayerSpawner;
import com.denfop.utils.ExperienceUtils;
import ic2.api.network.INetworkClientTileEntityEventListener;
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
import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Enchantments;
import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import net.minecraftforge.oredict.OreDictionary;

import java.util.ArrayList;
import java.util.EnumSet;
import java.util.List;
import java.util.Set;

public class TileEntityBaseQuarry extends TileEntityInventory implements IHasGui, INetworkTileEntityEventListener,
        INetworkClientTileEntityEventListener,
        IUpgradableBlock {

    public final String name;
    public final int index;
    public final InvSlotBaseQuarry input;
    public final double constenergyconsume;
    public final double speed;
    public final int exp_max_storage;
    public final InvSlotOutput outputSlot;
    public int exp_storage;
    public int min_y;
    public int max_y;
    public AudioSource audioSource;
    public double energyconsume;
    public AdvEnergy energy;
    public BlockPos blockpos = null;
    public FakePlayerSpawner player;
    public boolean work;
    public List<ItemStack> list = new ArrayList<>();
    public QEComponent energy1;
    public boolean analyzer;
    public int progress;
    public EnumQuarryModules list_modules;
    public boolean mac_enabled = false;
    public boolean comb_mac_enabled = false;
    public double consume;
    public boolean furnace;
    public int chance;
    public int col;
    public Vein vein;

    public TileEntityBaseQuarry(String name, double coef, int index) {
        this.name = name;
        this.energyconsume = 500 * coef;
        this.energy = this.addComponent(AdvEnergy.asBasicSink(this, 5E7D, 14));
        this.energy1 = this.addComponent(QEComponent.asBasicSink(this, 200000, 14));

        this.outputSlot = new InvSlotOutput(this, "output", 24);
        this.work = true;
        this.index = index;
        this.speed = Math.pow(2, index - 1);
        this.input = new InvSlotBaseQuarry(this, index);
        this.constenergyconsume = 500 * coef;
        this.min_y = 0;
        this.max_y = 256;
        this.exp_max_storage = 5000;
        this.exp_storage = 0;
        this.chance = 0;
        this.col = 1;
        this.furnace = false;
        this.list_modules = null;
        this.consume = this.energyconsume;
    }


    public void readFromNBT(NBTTagCompound nbttagcompound) {
        super.readFromNBT(nbttagcompound);
        int x = nbttagcompound.getInteger("blockpos_x");
        int y = nbttagcompound.getInteger("blockpos_y");
        int z = nbttagcompound.getInteger("blockpos_z");
        this.min_y = nbttagcompound.getInteger("min_y");
        this.max_y = nbttagcompound.getInteger("max_y");
        this.exp_storage = nbttagcompound.getInteger("exp_storage");
        this.blockpos = new BlockPos(x, y, z);
        this.work = nbttagcompound.getBoolean("work");
    }

    public NBTTagCompound writeToNBT(NBTTagCompound nbttagcompound) {
        super.writeToNBT(nbttagcompound);
        if (this.blockpos != null) {
            nbttagcompound.setInteger("blockpos_x", this.blockpos.getX());
            nbttagcompound.setInteger("blockpos_y", this.blockpos.getY());
            nbttagcompound.setInteger("blockpos_z", this.blockpos.getZ());
        }
        nbttagcompound.setInteger("min_y", this.min_y);
        nbttagcompound.setInteger("max_y", this.max_y);
        nbttagcompound.setInteger("exp_storage", this.exp_storage);
        nbttagcompound.setBoolean("work", work);
        return nbttagcompound;
    }

    protected void initiate(int soundEvent) {
        IC2.network.get(true).initiateTileEntityEvent(this, soundEvent, true);
    }

    @Override
    public void onPlaced(final ItemStack stack, final EntityLivingBase placer, final EnumFacing facing) {
        super.onPlaced(stack, placer, facing);
        this.max_y = placer.getEntityWorld().provider.getHeight();
    }

    protected void onLoaded() {
        super.onLoaded();
        this.input.update();
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

    protected void onUnloaded() {
        super.onUnloaded();
        if (IC2.platform.isRendering() && this.audioSource != null) {
            IUCore.audioManager.removeSources(this);
            this.audioSource = null;
        }

    }

    public void markDirty() {
        super.markDirty();


    }

    protected void updateEntityServer() {
        super.updateEntityServer();
        if (!this.work) {
            return;
        }
        this.energyconsume = this.constenergyconsume;
        if (this.blockpos == null) {
            int chunkx = this.getWorld().getChunkFromBlockCoords(this.pos).x * 16;
            int chunkz = this.getWorld().getChunkFromBlockCoords(this.pos).z * 16;
            this.blockpos = new BlockPos(chunkx, this.min_y, chunkz);
        }

        for (int i = 0; i < this.speed; i++) {
            if (this.energy.canUseEnergy(this.energyconsume)) {
                this.energy.useEnergy(this.energyconsume);
                initiate(0);
                setActive(true);
                if (!this.getWorld().isAirBlock(this.blockpos)) {
                    Block block = this.getWorld().getBlockState(this.blockpos).getBlock();
                    ItemStack stack = new ItemStack(block, 1,
                            block.getMetaFromState(this.getWorld().getBlockState(this.blockpos))
                    );
                    if (!stack.isEmpty()) {
                        if ((this.getWorld().getBlockState(this.blockpos).getMaterial() == Material.IRON || this
                                .getWorld()
                                .getBlockState(this.blockpos)
                                .getMaterial() == Material.ROCK) && OreDictionary.getOreIDs(stack).length > 0) {
                            int id = OreDictionary.getOreIDs(stack)[0];
                            String name = OreDictionary.getOreName(id);
                            if (name.startsWith("ore")) {

                                if (list(this.list_modules, stack)) {
                                    int chunkx = this.getWorld().getChunkFromBlockCoords(this.pos).x * 16 * col;
                                    int chunkz = this.getWorld().getChunkFromBlockCoords(this.pos).z * 16 * col;
                                    if (this.blockpos.getX() < chunkx + 16 * this.col) {
                                        this.blockpos = this.blockpos.add(1, 0, 0);
                                    } else {
                                        if (this.blockpos.getZ() < chunkz + 16 * this.col) {
                                            this.blockpos = this.blockpos.add(-16 * this.col, 0, 1);
                                        } else {
                                            if (this.blockpos.getY() < this.max_y) {
                                                this.blockpos = this.blockpos.add(0, 1, -16 * col);
                                            } else {
                                                this.work = false;
                                            }
                                        }
                                    }
                                    if (this.col == 1) {
                                        if (this.blockpos.getX() > chunkx + 16 || this.blockpos.getZ() > chunkz + 16) {
                                            this.blockpos = null;
                                        }
                                    }
                                    return;
                                }

                                if (this.player == null) {
                                    this.player = new FakePlayerSpawner(getWorld());
                                }
                                ItemStack stack1 = new ItemStack(Items.ENCHANTED_BOOK);
                                if (this.chance != 0) {
                                    stack.addEnchantment(Enchantments.FORTUNE, this.chance);
                                }

                                final IBlockState state = this
                                        .getWorld()
                                        .getBlockState(this.blockpos);
                                block.onBlockHarvested(this.world, this.blockpos, state, player);

                                if (block.removedByPlayer(state, world, this.blockpos, player, true)) {
                                    int exp = block.getExpDrop(state, world, this.blockpos, this.chance);
                                    block.onBlockDestroyedByPlayer(world, this.blockpos, state);
                                    block.harvestBlock(world, player, this.blockpos, state, null, stack1);
                                    this.getWorld().setBlockToAir(this.blockpos);
                                    this.exp_storage = Math.min(this.exp_storage + exp, this.exp_max_storage);
                                    List<EntityItem> items = player.getEntityWorld().getEntitiesWithinAABB(
                                            EntityItem.class,
                                            new AxisAlignedBB(
                                                    this.blockpos.getX() - 1,
                                                    this.blockpos.getY() - 1,
                                                    this.blockpos.getZ() - 1,
                                                    this.blockpos.getX() + 1,
                                                    this.blockpos.getY() + 1,
                                                    this.blockpos.getZ() + 1
                                            )
                                    );
                                    int chance = 0;
                                    if (this.energy1.canUseEnergy(80)) {
                                        chance = this.getWorld().rand.nextInt(101);
                                        this.energy1.useEnergy(80);
                                    }
                                    for (EntityItem item : items) {
                                        if (!this.furnace) {

                                            if (this.mac_enabled) {
                                                ItemStack stack4;
                                                BaseMachineRecipe recipe = Recipes.recipes.getRecipeOutput(
                                                        "macerator",
                                                        false,
                                                        item.getItem()
                                                );
                                                if (recipe != null) {
                                                    stack4 = recipe.getOutput().items.get(0);
                                                } else {
                                                    stack4 = item.getItem();
                                                }
                                                if (this.outputSlot.canAdd(stack4)) {
                                                    this.outputSlot.add(stack4);
                                                    if (chance > 85) {
                                                        this.outputSlot.add(stack4);
                                                    }
                                                    item.setDead();
                                                }
                                            } else if (this.comb_mac_enabled) {
                                                ItemStack stack4;
                                                BaseMachineRecipe recipe = Recipes.recipes.getRecipeOutput(
                                                        "comb_macerator",
                                                        false,
                                                        item.getItem()
                                                );
                                                if (recipe != null) {
                                                    stack4 = recipe.getOutput().items.get(0);
                                                } else {
                                                    stack4 = item.getItem();
                                                }
                                                if (this.outputSlot.canAdd(stack4)) {
                                                    this.outputSlot.add(stack4);
                                                    if (chance > 85) {
                                                        this.outputSlot.add(stack4);
                                                    }
                                                    item.setDead();
                                                }
                                            } else {
                                                if (this.outputSlot.canAdd(item.getItem())) {
                                                    this.outputSlot.add(item.getItem());
                                                    if (chance > 85) {
                                                        this.outputSlot.add(item.getItem());
                                                    }
                                                    item.setDead();
                                                }
                                            }
                                        } else {
                                            ItemStack stack4 = item.getItem();
                                            ItemStack stack5 = ItemStack.EMPTY;
                                            if (this.mac_enabled) {
                                                BaseMachineRecipe recipe = Recipes.recipes.getRecipeOutput(
                                                        "macerator",
                                                        false,
                                                        item.getItem()
                                                );
                                                if (recipe != null) {
                                                    stack5 = recipe.getOutput().items.get(0);
                                                }


                                            } else if (this.comb_mac_enabled) {
                                                BaseMachineRecipe recipe = Recipes.recipes.getRecipeOutput(
                                                        "comb_macerator",
                                                        false,
                                                        item.getItem()
                                                );
                                                if (recipe != null) {
                                                    stack5 = recipe.getOutput().items.get(0);
                                                }


                                            }
                                            BaseMachineRecipe recipe;
                                            if (stack5.isEmpty()) {
                                                recipe = Recipes.recipes.getRecipeOutput("furnace", false,
                                                        stack4
                                                );
                                            } else {
                                                recipe = Recipes.recipes.getRecipeOutput("furnace", false,
                                                        stack5
                                                );
                                            }

                                            if (recipe != null) {
                                                stack4 = recipe.getOutput().items.get(0);
                                            } else {
                                                stack4 = item.getItem();
                                            }

                                            if (!stack5.isEmpty()) {
                                                stack4.setCount(stack5.getCount());
                                            } else if (!stack4.isEmpty()) {
                                                stack4.setCount(item.getItem().getCount());
                                            }
                                            if (stack4.isEmpty()) {
                                                if (this.outputSlot.canAdd(item.getItem())) {
                                                    this.outputSlot.add(item.getItem());
                                                    if (chance > 85) {
                                                        this.outputSlot.add(item.getItem());
                                                    }
                                                }
                                            } else {
                                                if (this.outputSlot.canAdd(stack4)) {
                                                    this.outputSlot.add(stack4);
                                                    if (chance > 85) {
                                                        this.outputSlot.add(stack4);
                                                    }
                                                }
                                            }
                                            item.setDead();
                                        }

                                    }

                                }

                            }
                        }
                    }
                }
                int chunkx = this.getWorld().getChunkFromBlockCoords(this.pos).x * 16;
                int chunkz = this.getWorld().getChunkFromBlockCoords(this.pos).z * 16;
                if (this.blockpos.getX() < chunkx + 16 * col) {
                    this.blockpos = blockpos.add(1, 0, 0);
                } else {
                    if (this.blockpos.getZ() < chunkz + 16 * col) {
                        this.blockpos = this.blockpos.add(-16 * col, 0, 1);
                    } else {
                        if (this.blockpos.getY() < this.max_y) {
                            this.blockpos = this.blockpos.add(0, 1, -16 * col);
                        } else {
                            this.work = false;
                        }
                    }
                }
                if (col == 1) {
                    if (this.blockpos.getX() > chunkx + 16 || this.blockpos.getZ() > chunkz + 16) {
                        this.blockpos = new BlockPos(chunkx, this.min_y, chunkz);
                    }
                }

            } else {
                setActive(false);
            }
        }


        if (this.getWorld().provider.getWorldTime() % 200 == 0) {
            initiate(2);
        }
        if (getActive()) {
            ItemStack stack3 = Ic2Items.ejectorUpgrade;
            ((IUpgradeItem) stack3.getItem()).onTick(stack3, this);
        }
    }


    public ContainerBase<? extends TileEntityBaseQuarry> getGuiContainer(EntityPlayer player) {
        return new ContainerBaseQuarry(player, this);

    }

    @SideOnly(Side.CLIENT)
    public GuiScreen getGui(EntityPlayer player, boolean isAdmin) {

        return new GuiBaseQuarry(new ContainerBaseQuarry(player, this));
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

    @Override
    public void onNetworkEvent(final EntityPlayer entityPlayer, final int i) {
        switch (i) {
            case 0:
                this.min_y = Math.min(this.max_y, this.min_y + 1);

                if (this.blockpos.getY() < this.min_y) {
                    int temp = this.min_y - this.blockpos.getY();
                    this.blockpos = this.blockpos.add(0, temp, 0);
                }

                break;
            case 1:
                this.min_y = Math.max(0, this.min_y - 1);

                if (this.blockpos.getY() < this.min_y) {
                    int temp = this.min_y - this.blockpos.getY();
                    this.blockpos = this.blockpos.add(0, temp, 0);
                }

                break;
            case 2:
                this.max_y = Math.min(this.getWorld().provider.getHeight(), this.max_y + 1);
                if (this.min_y > this.max_y) {
                    this.min_y = this.max_y;
                }

                if (this.blockpos.getY() < this.min_y) {
                    int temp = this.min_y - this.blockpos.getY();
                    this.blockpos = this.blockpos.add(0, temp, 0);
                }

                break;
            case 3:
                this.max_y = Math.max(0, this.max_y - 1);
                if (this.min_y > this.max_y) {
                    this.min_y = this.max_y;
                }
                if (this.blockpos.getY() < this.min_y) {
                    int temp = this.min_y - this.blockpos.getY();
                    this.blockpos = this.blockpos.add(0, temp, 0);
                }
                break;
            case 4:
                this.exp_storage = ExperienceUtils.addPlayerXP1(entityPlayer, this.exp_storage);
                break;
        }
    }

}
