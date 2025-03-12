package com.denfop.tiles.mechanism;

import com.denfop.IUItem;
import com.denfop.api.tile.IMultiTileBlock;
import com.denfop.api.upgrades.IUpgradableBlock;
import com.denfop.api.upgrades.UpgradableProperty;
import com.denfop.blocks.BlockTileEntity;
import com.denfop.blocks.mechanism.BlockBaseMachine3;
import com.denfop.componets.AirPollutionComponent;
import com.denfop.componets.ComponentTimer;
import com.denfop.componets.SoilPollutionComponent;
import com.denfop.container.ContainerAlkalineEarthQuarry;
import com.denfop.gui.GuiAlkalineEarthQuarry;
import com.denfop.invslot.InvSlot;
import com.denfop.invslot.InvSlotUpgrade;
import com.denfop.items.ItemMesh;
import com.denfop.tiles.base.IManufacturerBlock;
import com.denfop.tiles.base.TileElectricMachine;
import com.denfop.utils.ModUtils;
import com.denfop.utils.Timer;
import com.denfop.world.WorldBaseGen;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.item.Item;
import net.minecraft.item.ItemBlock;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import java.util.EnumSet;
import java.util.List;
import java.util.Set;

public class TileEntityAlkalineEarthQuarry extends TileElectricMachine implements
        IUpgradableBlock, IManufacturerBlock {

    public static ItemStack lithium;
    public static ItemStack beryllium;
    public static ItemStack boron;
    public final ComponentTimer timer;
    public final InvSlot inputSlotA;
    public final InvSlot inputSlotB;
    public final InvSlotUpgrade upgradeSlot;
    public int level;
    public int level_mesh;
    public int type_block;

    public TileEntityAlkalineEarthQuarry() {
        super(100, 14, 1);
        this.addComponent(new SoilPollutionComponent(this, 0.1));
        this.addComponent(new AirPollutionComponent(this, 0.1));
        inputSlotA = new InvSlot(this, InvSlot.TypeItemSlot.INPUT, 1) {
            @Override
            public boolean accepts(final ItemStack stack, final int index) {
                Item item = stack.getItem();
                if (!(item instanceof ItemBlock)) {
                    return false;
                }
                ItemBlock itemBlock = (ItemBlock) item;
                return itemBlock.getBlock() == Blocks.DIRT || itemBlock.getBlock() == Blocks.GRAVEL || itemBlock.getBlock() == Blocks.SAND;
            }


            @Override
            public void put(final int index, final ItemStack content) {
                super.put(index, content);
                if (content.isEmpty()) {
                    type_block = -1;
                } else {
                    Item item = content.getItem();
                    ItemBlock itemBlock = (ItemBlock) item;
                    if (itemBlock.getBlock() == Blocks.DIRT) {
                        type_block = 1;
                    }
                    if (itemBlock.getBlock() == Blocks.GRAVEL) {
                        type_block = 2;
                    }
                    if (itemBlock.getBlock() == Blocks.SAND) {
                        type_block = 3;
                    }
                }
            }
        };
        inputSlotB = new InvSlot(this, InvSlot.TypeItemSlot.INPUT, 1) {
            @Override
            public boolean accepts(final ItemStack stack, final int index) {
                return stack.getItem() instanceof ItemMesh;
            }

            @Override
            public void put(final int index, final ItemStack content) {
                super.put(index, content);
                if (content.isEmpty()) {
                    level_mesh = -1;
                } else {
                    ItemMesh itemMesh = (ItemMesh) content.getItem();
                    level_mesh = itemMesh.getLevel();
                }
            }
        };
        this.timer = this.addComponent(new ComponentTimer(this, new Timer(0, 2, 0)) {
            @Override
            public int getTickFromSecond() {
                return (int) Math.max(1, 20 - ((TileEntityAlkalineEarthQuarry) this.parent).getLevel() * 1.1);
            }
        });
        this.upgradeSlot = new InvSlotUpgrade(this,2);
        this.level = 0;
    }

    public static void addRecipe(int container) {

    }

    @Override
    public int getLevel() {
        return this.level;
    }

    @Override
    public void setLevel(final int level) {
        this.level = level;
    }

    @Override
    public void removeLevel(final int level) {
        this.level -= level;
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
        if (level < 10) {
            ItemStack stack = player.getHeldItem(hand);
            if (!stack.getItem().equals(IUItem.upgrade_speed_creation)) {
                return super.onActivated(player, hand, side, hitX, hitY, hitZ);
            } else {
                stack.shrink(1);
                this.level++;
                return true;
            }
        } else {

            return super.onActivated(player, hand, side, hitX, hitY, hitZ);
        }
    }

    public List<ItemStack> getWrenchDrops(EntityPlayer player, int fortune) {
        List<ItemStack> ret = super.getWrenchDrops(player, fortune);
        if (this.level != 0) {
            ret.add(new ItemStack(IUItem.upgrade_speed_creation, this.level));
            this.level = 0;
        }
        return ret;
    }

    @Override
    public void readFromNBT(final NBTTagCompound nbttagcompound) {
        super.readFromNBT(nbttagcompound);
        this.level = nbttagcompound.getInteger("level");
    }

    @Override
    public NBTTagCompound writeToNBT(final NBTTagCompound nbttagcompound) {
        super.writeToNBT(nbttagcompound);
        nbttagcompound.setInteger("level", this.level);
        return nbttagcompound;
    }

    @Override
    public BlockTileEntity getBlock() {
        return IUItem.basemachine2;
    }

    @Override
    public IMultiTileBlock getTeBlock() {
        return BlockBaseMachine3.alkalineearthquarry;
    }


    public void onLoaded() {
        super.onLoaded();
        lithium = new ItemStack(IUItem.ore2);
        beryllium = new ItemStack(IUItem.ore2, 1, 1);
        boron = new ItemStack(IUItem.ore2, 1, 2);
        if (this.inputSlotB.isEmpty()) {
            level_mesh = -1;

        } else {
            ItemMesh itemMesh = (ItemMesh) inputSlotB.get().getItem();
            level_mesh = itemMesh.getLevel();
        }
        if (this.inputSlotA.isEmpty()) {
            type_block = -1;
        } else {
            Item item = inputSlotA.get().getItem();
            ItemBlock itemBlock = (ItemBlock) item;
            if (itemBlock.getBlock() == Blocks.DIRT) {
                type_block = 1;
            }
            if (itemBlock.getBlock() == Blocks.GRAVEL) {
                type_block = 2;
            }
            if (itemBlock.getBlock() == Blocks.SAND) {
                type_block = 3;
            }
        }
    }

    @Override
    public void updateEntityServer() {
        super.updateEntityServer();
        if (!this.inputSlotA
                .get()
                .isEmpty() && !this.inputSlotB.isEmpty() && this.energy.canUseEnergy(1) && canOperate() && this.outputSlot.canAdd(
                getItemStack())) {
            this.setActive(true);
            if (!this.timer.isCanWork()) {
                this.timer.setCanWork(true);
            }
            this.energy.useEnergy(1);
            if (this.timer.getTimers().get(0).getTime() <= 0) {
                this.inputSlotA.get().shrink(1);
                if (getChance()) {
                    this.outputSlot.add(this.getItemStack());
                }
                ItemMesh itemMesh = (ItemMesh) this.inputSlotB.get().getItem();
                ModUtils.nbt(this.inputSlotB.get());
                itemMesh.applyCustomDamage(this.inputSlotB.get(), -1, null);
                if (itemMesh.getMaxCustomDamage(this.inputSlotB.get()) - itemMesh.getCustomDamage(
                        this.inputSlotB.get()) == 0) {
                    this.inputSlotB.get().shrink(1);
                }
                this.timer.resetTime();
            }
        } else {
            this.timer.setCanWork(false);
            this.setActive(false);
            return;
        }
        this.upgradeSlot.tickNoMark();
    }

    private boolean getChance() {
        if (level_mesh == 1) {
            if (type_block == 3) {
                return WorldBaseGen.random.nextInt(200) == 0;
            }
        }
        if (level_mesh == 2) {
            if (type_block == 3) {
                return WorldBaseGen.random.nextInt(200) < 5;
            }
            if (type_block == 1) {
                return WorldBaseGen.random.nextInt(200) == 0;
            }
            if (type_block == 2) {
                return WorldBaseGen.random.nextInt(200) == 0;
            }
        }
        if (level_mesh == 3) {
            if (type_block == 3) {
                return WorldBaseGen.random.nextInt(200) < 8;
            }
            if (type_block == 1) {
                return WorldBaseGen.random.nextInt(200) < 4;
            }
            if (type_block == 2) {
                return WorldBaseGen.random.nextInt(200) == 0;
            }

        }
        if (level_mesh == 4) {
            if (type_block == 3) {
                return WorldBaseGen.random.nextInt(200) < 10;
            }
            if (type_block == 1) {
                return WorldBaseGen.random.nextInt(200) < 6;
            }
            if (type_block == 2) {
                return WorldBaseGen.random.nextInt(200) < 2;
            }

        }
        if (level_mesh == 5) {
            if (type_block == 3) {
                return WorldBaseGen.random.nextInt(200) < 14;
            }
            if (type_block == 1) {
                return WorldBaseGen.random.nextInt(200) < 8;
            }
            if (type_block == 2) {
                return WorldBaseGen.random.nextInt(200) < 4;
            }

        }
        return false;
    }

    private ItemStack getItemStack() {
        switch (type_block) {
            case 1:
                return beryllium;
            case 2:
                return boron;
            case 3:
                return lithium;
        }
        return ItemStack.EMPTY;
    }

    private boolean canOperate() {
        if (level_mesh == 1) {
            if (type_block == 3) {
                return true;
            }
        }
        if (level_mesh == 2) {
            if (type_block == 3 || type_block == 2 || type_block == 1) {
                return true;
            }
        }
        if (level_mesh >= 3) {
            if (type_block == 3 || type_block == 1 || type_block == 2) {
                return true;
            }
        }
        return false;
    }


    @Override
    public ContainerAlkalineEarthQuarry getGuiContainer(final EntityPlayer var1) {
        return new ContainerAlkalineEarthQuarry(this, var1);
    }

    @Override
    @SideOnly(Side.CLIENT)
    public GuiScreen getGui(final EntityPlayer var1, final boolean var2) {
        return new GuiAlkalineEarthQuarry(getGuiContainer(var1));
    }


    @Override
    public Set<UpgradableProperty> getUpgradableProperties() {
        return EnumSet.of(
                UpgradableProperty.ItemExtract,
                UpgradableProperty.ItemInput
        );
    }


}
