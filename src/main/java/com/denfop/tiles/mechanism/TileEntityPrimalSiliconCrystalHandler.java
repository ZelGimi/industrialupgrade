package com.denfop.tiles.mechanism;

import com.denfop.IUItem;
import com.denfop.Localization;
import com.denfop.api.Recipes;
import com.denfop.api.recipe.IHasRecipe;
import com.denfop.api.recipe.IUpdateTick;
import com.denfop.api.recipe.InvSlotRecipes;
import com.denfop.api.recipe.MachineRecipe;
import com.denfop.api.tile.IMultiTileBlock;
import com.denfop.api.upgrades.IUpgradableBlock;
import com.denfop.api.upgrades.UpgradableProperty;
import com.denfop.blocks.BlockTileEntity;
import com.denfop.blocks.mechanism.BlockPrimalSiliconCrystalHandler;
import com.denfop.componets.ComponentTimer;
import com.denfop.invslot.InvSlotUpgrade;
import com.denfop.items.resource.ItemDust;
import com.denfop.network.EncoderHandler;
import com.denfop.network.IUpdatableTileEvent;
import com.denfop.network.packet.CustomPacketBuffer;
import com.denfop.network.packet.PacketUpdateFieldTile;
import com.denfop.recipe.IInputHandler;
import com.denfop.tiles.base.TileElectricMachine;
import com.denfop.utils.ModUtils;
import com.denfop.utils.Timer;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.Vec3;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.ForgeCapabilities;
import net.minecraftforge.common.util.LazyOptional;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.io.IOException;
import java.util.EnumSet;
import java.util.List;
import java.util.Set;

public class TileEntityPrimalSiliconCrystalHandler extends TileElectricMachine implements
        IUpgradableBlock, IUpdateTick, IUpdatableTileEvent, IHasRecipe {

    public final ComponentTimer timer;
    public final InvSlotRecipes inputSlotA;
    public final InvSlotUpgrade upgradeSlot;
    public int col;
    private MachineRecipe output;
    private boolean checkState;

    public TileEntityPrimalSiliconCrystalHandler(BlockPos pos, BlockState state) {
        super(0, 0, 1, BlockPrimalSiliconCrystalHandler.primal_silicon_crystal_handler, pos, state);
        Recipes.recipes.addInitRecipes(this);
        inputSlotA = new InvSlotRecipes(this, "silicon_recipe", this) {
            @Override
            public boolean accepts(final ItemStack itemStack, final int index) {
                return false;
            }
        };

        this.upgradeSlot = new InvSlotUpgrade(this, 4);
        this.timer = this.addComponent(new ComponentTimer(this, new Timer(0, 12, 30)));
    }

    @Override
    public void addInformation(final ItemStack stack, final List<String> tooltip) {
        tooltip.add(Localization.translate("iu.primal_repair4"));
    }

    @Override
    public void updateField(final String name, final CustomPacketBuffer is) {
        super.updateField(name, is);
        if (name.equals("timer")) {
            try {
                is.readByte();
                this.timer.onNetworkUpdate(is);
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }

    }


    @Override
    public void init() {
        final IInputHandler input = com.denfop.api.Recipes.inputFactory;

    }


    @Override
    public void onLoaded() {
        super.onLoaded();
        if (!this.getWorld().isClientSide) {
            inputSlotA.load();

        }
        this.output = inputSlotA.process();
        if (this.output == null)
            this.timer.resetTime();
    }


    @Override
    public boolean onActivated(
            final Player player,
            final InteractionHand hand,
            final Direction side,
            final Vec3 hitX
    ) {
        ItemStack stack = player.getItemInHand(hand);
        if (!stack.isEmpty() && outputSlot.isEmpty()) {
            if (stack.getItem() instanceof ItemDust<?> && IUItem.iudust.getMeta((ItemDust) stack.getItem()) == 60) {
                if (this.inputSlotA.get(1).isEmpty()) {
                    final ItemStack stack1 = stack.copy();
                    if (stack1.getCount() > 3) {
                        stack1.setCount(3);
                        stack.shrink(3);
                    } else {
                        stack.shrink(stack1.getCount());
                    }
                    this.inputSlotA.set(1, stack1);
                    if (!level.isClientSide) {
                        getOutput();
                    }
                    return true;
                } else if (!this.inputSlotA.get(1).isEmpty() && this.inputSlotA.get(1).is(stack.getItem())) {
                    int minCount = 3 - this.inputSlotA.get(1).getCount();
                    minCount = Math.min(stack.getCount(), minCount);
                    this.inputSlotA.get(1).grow(minCount);
                    stack.grow(-minCount);
                    if (!level.isClientSide) {
                        getOutput();
                    }
                    return true;
                }
            } else if (stack.getItem() == Items.FLINT) {
                if (!this.inputSlotA.get(1).isEmpty() && this.inputSlotA.get(1).getCount() >= 3) {
                    if (this.inputSlotA.get(0).isEmpty()) {
                        final ItemStack stack1 = stack.copy();
                        if (stack1.getCount() > 1) {
                            stack1.setCount(1);
                            stack.shrink(1);
                        } else {
                            stack.shrink(stack1.getCount());
                        }
                        this.inputSlotA.set(0, stack1);
                        if (!level.isClientSide) {
                            getOutput();
                        }
                        return true;
                    }
                }

            }
        } else {
            if (!outputSlot.isEmpty()) {
                if (!level.isClientSide) {
                    ModUtils.dropAsEntity(level, pos, outputSlot.get(0));
                }
                outputSlot.set(0, ItemStack.EMPTY);
                return true;
            }

        }


        return false;
    }

    @Override
    public CustomPacketBuffer writePacket() {
        CustomPacketBuffer customPacketBuffer = super.writePacket();
        try {
            EncoderHandler.encode(customPacketBuffer, timer, false);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        return customPacketBuffer;
    }

    @Override
    public BlockTileEntity getBlock() {
        return IUItem.primalSiliconCrystal.getBlock();
    }

    @Override
    public IMultiTileBlock getTeBlock() {
        return BlockPrimalSiliconCrystalHandler.primal_silicon_crystal_handler;
    }

    @Override
    public void onUpdate() {

    }


    @Override
    public void updateEntityServer() {
        super.updateEntityServer();
        if (this.getWorld().getGameTime() % 20 == 0) {
            new PacketUpdateFieldTile(this, "timer", timer);
            checkState = false;
            ItemStack stack1 = this.inputSlotA.get(0);
            ItemStack stack2 = this.inputSlotA.get(1);
            if (this.outputSlot.isEmpty()) {
                if (stack1.isEmpty()) {
                    if (!stack2.isEmpty()) {
                        switch (stack2.getCount()) {
                            case 1:
                                this.setActive("1");
                                break;
                            case 2:
                                this.setActive("2");
                                break;
                            default:
                            case 3:
                                this.setActive("3");
                                break;
                        }
                    } else {
                        this.setActive(false);
                    }
                } else {
                    if (this.outputSlot.isEmpty()) {
                        if (stack2.getCount() >= 3) {
                            final double time = this.timer.getTimes();
                            if (time >= 0.5 && time < 1) {
                                this.setActive("5");
                            } else if (time < 0.5) {
                                this.setActive("4");
                            } else {
                                this.setActive("6");
                            }
                        } else {
                            this.setActive(false);
                        }
                    } else {
                        this.setActive("6");
                    }
                }
            } else {
                this.setActive("6");
            }
        }

        if (this.inputSlotA.get(0).isEmpty() || this.output == null || !this.outputSlot.get(0).isEmpty()) {
            this.timer.setCanWorkWithOut(false);
            return;
        }

        if (!this.timer.isCanWork()) {
            this.timer.setCanWork(true);
        }

        if (this.timer.getTimers().get(0).getTime() <= 0) {
            this.inputSlotA.consume();
            this.outputSlot.add(this.output.getRecipe().output.items.get(0));
            this.output = null;
            this.timer.resetTime();
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
        if (this.output != null) {
            this.timer.resetTime();
        }
        return this.output;
    }


    public List<ItemStack> getWrenchDrops(Player player, int fortune) {
        List<ItemStack> ret = super.getWrenchDrops(player, fortune);

        return ret;
    }

    @Override
    public @NotNull <T> LazyOptional<T> getCapability(@NotNull Capability<T> cap, @Nullable Direction facing) {
        if (cap == ForgeCapabilities.ITEM_HANDLER)
            return LazyOptional.empty();
        return super.getCapability(cap, facing);
    }


    @Override
    public void readPacket(final CustomPacketBuffer customPacketBuffer) {
        super.readPacket(customPacketBuffer);
        try {
            this.timer.onNetworkUpdate(customPacketBuffer);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
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
