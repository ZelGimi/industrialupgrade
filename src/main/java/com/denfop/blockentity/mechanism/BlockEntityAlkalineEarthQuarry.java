package com.denfop.blockentity.mechanism;

import com.denfop.IUItem;
import com.denfop.api.blockentity.MultiBlockEntity;
import com.denfop.api.container.CustomWorldContainer;
import com.denfop.api.upgrades.BlockEntityUpgrade;
import com.denfop.api.upgrades.EnumBlockEntityUpgrade;
import com.denfop.blockentity.base.BlockEntityElectricMachine;
import com.denfop.blockentity.base.IManufacturerBlock;
import com.denfop.blocks.BlockTileEntity;
import com.denfop.blocks.mechanism.BlockBaseMachine3Entity;
import com.denfop.componets.AirPollutionComponent;
import com.denfop.componets.ComponentTimer;
import com.denfop.componets.SoilPollutionComponent;
import com.denfop.containermenu.ContainerMenuAlkalineEarthQuarry;
import com.denfop.containermenu.ContainerMenuBase;
import com.denfop.inventory.Inventory;
import com.denfop.inventory.InventoryUpgrade;
import com.denfop.items.ItemMesh;
import com.denfop.screen.ScreenAlkalineEarthQuarry;
import com.denfop.screen.ScreenIndustrialUpgrade;
import com.denfop.utils.ModUtils;
import com.denfop.utils.Timer;
import com.denfop.world.WorldBaseGen;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.Vec3;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

import java.util.EnumSet;
import java.util.List;
import java.util.Set;

public class BlockEntityAlkalineEarthQuarry extends BlockEntityElectricMachine implements
        BlockEntityUpgrade, IManufacturerBlock {

    public static ItemStack lithium;
    public static ItemStack beryllium;
    public static ItemStack boron;
    public final ComponentTimer timer;
    public final Inventory inputSlotA;
    public final Inventory inputSlotB;
    public final InventoryUpgrade upgradeSlot;
    public int levelBlock;
    public int level_mesh;
    public int type_block;

    public BlockEntityAlkalineEarthQuarry(BlockPos pos, BlockState state) {
        super(100, 14, 1, BlockBaseMachine3Entity.alkalineearthquarry, pos, state);
        this.addComponent(new SoilPollutionComponent(this, 0.1));
        this.addComponent(new AirPollutionComponent(this, 0.1));
        inputSlotA = new Inventory(this, Inventory.TypeItemSlot.INPUT, 1) {
            @Override
            public boolean canPlaceItem(final int index, final ItemStack stack) {
                Item item = stack.getItem();
                if (!(item instanceof BlockItem)) {
                    return false;
                }
                BlockItem itemBlock = (BlockItem) item;
                return itemBlock.getBlock() == Blocks.DIRT || itemBlock.getBlock() == Blocks.GRAVEL || itemBlock.getBlock() == Blocks.SAND;
            }


            @Override
            public ItemStack set(final int index, final ItemStack content) {
                super.set(index, content);
                if (content.isEmpty()) {
                    type_block = -1;
                } else {
                    Item item = content.getItem();
                    BlockItem itemBlock = (BlockItem) item;
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
                return content;
            }
        };
        inputSlotB = new Inventory(this, Inventory.TypeItemSlot.INPUT, 1) {
            @Override
            public boolean canPlaceItem(final int index, final ItemStack stack) {
                return stack.getItem() instanceof ItemMesh;
            }

            @Override
            public ItemStack set(final int index, final ItemStack content) {
                super.set(index, content);
                if (content.isEmpty()) {
                    level_mesh = -1;
                } else {
                    ItemMesh itemMesh = (ItemMesh) content.getItem();
                    level_mesh = itemMesh.getLevel();
                }
                return content;
            }
        };
        this.timer = this.addComponent(new ComponentTimer(this, new Timer(0, 2, 0)) {
            @Override
            public int getTickFromSecond() {
                return (int) Math.max(1, 20 - ((BlockEntityAlkalineEarthQuarry) this.parent).getLevelMechanism() * 1.1);
            }
        });
        this.upgradeSlot = new InventoryUpgrade(this, 2);
        this.levelBlock = 0;
    }

    public static void addRecipe(int container) {

    }

    @Override
    public int getLevelMechanism() {
        return this.levelBlock;
    }

    @Override
    public void setLevelMech(final int level) {
        this.levelBlock = level;
    }

    @Override
    public void removeLevel(final int level) {
        this.levelBlock -= level;
    }

    @Override
    public boolean onActivated(Player player, InteractionHand hand, Direction side, Vec3 vec3) {
        if (levelBlock < 10) {
            ItemStack stack = player.getItemInHand(hand);
            if (!stack.getItem().equals(IUItem.upgrade_speed_creation.getItem())) {
                return super.onActivated(player, hand, side, vec3);
            } else {
                stack.shrink(1);
                this.levelBlock++;
                return true;
            }
        } else {
            return super.onActivated(player, hand, side, vec3);
        }
    }


    public List<ItemStack> getWrenchDrops(Player player, int fortune) {
        List<ItemStack> ret = super.getWrenchDrops(player, fortune);
        if (this.levelBlock != 0) {
            ret.add(new ItemStack(IUItem.upgrade_speed_creation.getItem(), this.levelBlock));
            this.levelBlock = 0;
        }
        return ret;
    }

    @Override
    public void readFromNBT(final CompoundTag nbttagcompound) {
        super.readFromNBT(nbttagcompound);
        this.levelBlock = nbttagcompound.getInt("level");
    }

    @Override
    public CompoundTag writeToNBT(final CompoundTag nbttagcompound) {
        super.writeToNBT(nbttagcompound);
        nbttagcompound.putInt("level", this.levelBlock);
        return nbttagcompound;
    }

    @Override
    public BlockTileEntity getBlock() {
        return IUItem.basemachine2.getBlock(getTeBlock());
    }

    @Override
    public MultiBlockEntity getTeBlock() {
        return BlockBaseMachine3Entity.alkalineearthquarry;
    }


    public void onLoaded() {
        super.onLoaded();
        lithium = new ItemStack(IUItem.ore2.getItem());
        beryllium = new ItemStack(IUItem.ore2.getItem(1));
        boron = new ItemStack(IUItem.ore2.getItem(2));
        if (this.inputSlotB.isEmpty()) {
            level_mesh = -1;

        } else {
            ItemMesh itemMesh = (ItemMesh) inputSlotB.get(0).getItem();
            level_mesh = itemMesh.getLevel();
        }
        if (this.inputSlotA.isEmpty()) {
            type_block = -1;
        } else {
            Item item = inputSlotA.get(0).getItem();
            BlockItem itemBlock = (BlockItem) item;
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
                .get(0)
                .isEmpty() && !this.inputSlotB.isEmpty() && this.energy.canUseEnergy(1) && canOperate() && this.outputSlot.canAdd(
                getItemStack())) {
            this.setActive(true);
            if (!this.timer.isCanWork()) {
                this.timer.setCanWork(true);
            }
            this.energy.useEnergy(1);
            if (this.timer.getTimers().get(0).getTime() <= 0) {
                this.inputSlotA.get(0).shrink(1);
                if (getChance()) {
                    this.outputSlot.add(this.getItemStack());
                }
                ItemMesh itemMesh = (ItemMesh) this.inputSlotB.get(0).getItem();
                ModUtils.nbt(this.inputSlotB.get(0));
                itemMesh.applyCustomDamage(this.inputSlotB.get(0), 1, null);
                if (itemMesh.getMaxCustomDamage(this.inputSlotB.get(0)) - itemMesh.getCustomDamage(
                        this.inputSlotB.get(0)) == 0) {
                    this.inputSlotB.get(0).shrink(1);
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
    public ContainerMenuAlkalineEarthQuarry getGuiContainer(final Player var1) {
        return new ContainerMenuAlkalineEarthQuarry(this, var1);
    }

    @Override
    @OnlyIn(Dist.CLIENT)
    public ScreenIndustrialUpgrade<ContainerMenuBase<? extends CustomWorldContainer>> getGui(Player var1, ContainerMenuBase<? extends CustomWorldContainer> menu) {
        return new ScreenAlkalineEarthQuarry((ContainerMenuAlkalineEarthQuarry) menu);
    }


    @Override
    public Set<EnumBlockEntityUpgrade> getUpgradableProperties() {
        return EnumSet.of(
                EnumBlockEntityUpgrade.ItemExtract,
                EnumBlockEntityUpgrade.ItemInput
        );
    }


}
