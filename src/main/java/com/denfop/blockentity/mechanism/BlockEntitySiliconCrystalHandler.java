package com.denfop.blockentity.mechanism;

import com.denfop.IUItem;
import com.denfop.api.Recipes;
import com.denfop.api.blockentity.MultiBlockEntity;
import com.denfop.api.container.CustomWorldContainer;
import com.denfop.api.recipe.*;
import com.denfop.api.upgrades.IUpgradableBlock;
import com.denfop.api.upgrades.UpgradableProperty;
import com.denfop.blockentity.base.BlockEntityElectricMachine;
import com.denfop.blockentity.base.IManufacturerBlock;
import com.denfop.blocks.BlockTileEntity;
import com.denfop.blocks.mechanism.BlockBaseMachine3Entity;
import com.denfop.componets.AirPollutionComponent;
import com.denfop.componets.ComponentTimer;
import com.denfop.componets.SoilPollutionComponent;
import com.denfop.containermenu.ContainerMenuBase;
import com.denfop.containermenu.ContainerMenuSiliconCrystalHandler;
import com.denfop.events.IUEventHandler;
import com.denfop.inventory.Inventory;
import com.denfop.inventory.InventoryUpgrade;
import com.denfop.network.IUpdatableTileEvent;
import com.denfop.network.packet.CustomPacketBuffer;
import com.denfop.recipe.IInputHandler;
import com.denfop.screen.ScreenIndustrialUpgrade;
import com.denfop.screen.ScreenSiliconCrystalHandler;
import com.denfop.utils.Timer;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.Vec3;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

import java.util.EnumSet;
import java.util.List;
import java.util.Set;

public class BlockEntitySiliconCrystalHandler extends BlockEntityElectricMachine implements
        IUpgradableBlock, IUpdateTick, IUpdatableTileEvent, IHasRecipe, IManufacturerBlock {

    public final ComponentTimer timer;
    public final InventoryRecipes inputSlotA;
    public final Inventory flintSlot;
    public final InventoryUpgrade upgradeSlot;
    public int col;
    private MachineRecipe output;
    private int levelBlock;

    public BlockEntitySiliconCrystalHandler(BlockPos pos, BlockState state) {
        super(1000, 1, 1, BlockBaseMachine3Entity.silicon_crystal_handler, pos, state);
        Recipes.recipes.addInitRecipes(this);
        inputSlotA = new InventoryRecipes(this, "silicon_recipe", this);

        this.upgradeSlot = new InventoryUpgrade(this, 4);
        this.timer = this.addComponent(new ComponentTimer(this, new Timer(0, 3, 0)) {
            @Override
            public int getTickFromSecond() {
                return (int) Math.max(1, 20 - ((BlockEntitySiliconCrystalHandler) this.parent).levelBlock * 1.4);
            }
        });
        this.flintSlot = new Inventory(this, Inventory.TypeItemSlot.INPUT, 1) {
            @Override
            public boolean canPlaceItem(final int index, final ItemStack stack) {
                return stack.is(IUEventHandler.coalDustTag);
            }
        };
        this.addComponent(new SoilPollutionComponent(this, 0.1));
        this.addComponent(new AirPollutionComponent(this, 0.1));
        this.levelBlock = 0;
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
    public CustomPacketBuffer writeContainerPacket() {
        CustomPacketBuffer packetBuffer = super.writeContainerPacket();
        packetBuffer.writeInt(this.col);
        return packetBuffer;
    }

    @Override
    public void readContainerPacket(final CustomPacketBuffer customPacketBuffer) {
        super.readContainerPacket(customPacketBuffer);
        this.col = customPacketBuffer.readInt();
    }


    @Override
    public void init() {
        final IInputHandler input = com.denfop.api.Recipes.inputFactory;
        Recipes.recipes.addRecipe(
                "silicon_recipe",
                new BaseMachineRecipe(
                        new Input(
                                input.getInput(new ItemStack(Items.FLINT)),
                                input.getInput(new ItemStack(IUItem.iudust.getStack(60), 3))
                        ),
                        new RecipeOutput(null, new ItemStack(IUItem.crafting_elements.getStack(492), 1))
                )
        );
    }


    @Override
    public BlockTileEntity getBlock() {
        return IUItem.basemachine2.getBlock(getTeBlock());
    }

    @Override
    public MultiBlockEntity getTeBlock() {
        return BlockBaseMachine3Entity.silicon_crystal_handler;
    }

    @Override
    public void onUpdate() {

    }

    public void onLoaded() {
        super.onLoaded();
        if (!level.isClientSide) {
            inputSlotA.load();
            this.getOutput();
            this.setUpgradestat();
        }


    }

    @Override
    public ContainerMenuSiliconCrystalHandler getGuiContainer(final Player var1) {
        return new ContainerMenuSiliconCrystalHandler(var1, this);
    }

    @Override
    @OnlyIn(Dist.CLIENT)
    public ScreenIndustrialUpgrade<ContainerMenuBase<? extends CustomWorldContainer>> getGui(Player var1, ContainerMenuBase<? extends CustomWorldContainer> menu) {
        return new ScreenSiliconCrystalHandler((ContainerMenuSiliconCrystalHandler) menu);
    }


    @Override
    public void updateEntityServer() {
        super.updateEntityServer();
        if (this.col == 0 && this.flintSlot.isEmpty()) {
            this.timer.setCanWorkWithOut(false);
            this.setActive(false);
            if (this.upgradeSlot.tickNoMark()) {
                setUpgradestat();
            }
            return;
        } else if (!this.flintSlot.isEmpty()) {
            if (this.col + 30 <= 90) {
                this.col += 30;
                this.flintSlot.get(0).shrink(1);
            }
        }

        if (this.energy.getEnergy() < 1 || this.inputSlotA.get(0).isEmpty() || this.output == null || this.outputSlot
                .get(0)
                .getCount() >= 64 || (this.output != null && !this.inputSlotA.continue_process(this.output))) {
            this.timer.setCanWorkWithOut(false);
            this.setActive(false);
            if (this.upgradeSlot.tickNoMark()) {
                setUpgradestat();
            }
            return;
        }
        this.setActive(true);
        if (!this.timer.isCanWork()) {

            this.timer.setCanWork(true);
        }
        if (this.getWorld().getGameTime() % 80 == 0) {
            this.col -= 1;
        }
        this.energy.useEnergy(1);
        if (this.timer.getTimers().get(0).getTime() <= 0) {
            this.inputSlotA.consume();
            this.outputSlot.add(this.output.getRecipe().output.items.get(0));
            getOutput();
        }
        if (this.upgradeSlot.tickNoMark()) {
            setUpgradestat();
        }
    }


    public void setUpgradestat() {
        this.energy.setSinkTier(this.tier + this.upgradeSlot.extraTier);
    }


    @Override
    public MachineRecipe getRecipeOutput() {
        return this.output;
    }

    @Override
    public void setRecipeOutput(final MachineRecipe output) {
        this.output = output;
    }

    public MachineRecipe getOutput() {
        this.output = this.inputSlotA.process();
        this.timer.resetTime();
        return this.output;
    }

    @Override
    public CompoundTag writeToNBT(final CompoundTag nbttagcompound) {
        super.writeToNBT(nbttagcompound);
        nbttagcompound.putInt("level", this.levelBlock);
        return nbttagcompound;
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
    public Set<UpgradableProperty> getUpgradableProperties() {
        return EnumSet.of(
                UpgradableProperty.Transformer,
                UpgradableProperty.EnergyStorage,
                UpgradableProperty.ItemExtract,
                UpgradableProperty.ItemInput
        );
    }


}
