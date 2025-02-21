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
import com.denfop.container.ContainerSiliconCrystalHandler;
import com.denfop.invslot.InvSlotUpgrade;
import com.denfop.network.EncoderHandler;
import com.denfop.network.IUpdatableTileEvent;
import com.denfop.network.packet.CustomPacketBuffer;
import com.denfop.network.packet.PacketUpdateFieldTile;
import com.denfop.recipe.IInputHandler;
import com.denfop.tiles.base.TileElectricMachine;
import com.denfop.utils.ModUtils;
import com.denfop.utils.Timer;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.BlockPos;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import net.minecraftforge.items.CapabilityItemHandler;
import org.jetbrains.annotations.NotNull;

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
    private int level;
    private boolean checkState;

    public TileEntityPrimalSiliconCrystalHandler() {
        super(0, 0, 1);
        Recipes.recipes.addInitRecipes(this);
        inputSlotA = new InvSlotRecipes(this, "silicon_recipe", this){
            @Override
            public boolean accepts(final ItemStack itemStack, final int index) {
                return false;
            }
        };

        this.upgradeSlot = new InvSlotUpgrade(this, 4);
        this.timer = this.addComponent(new ComponentTimer(this, new Timer(0, 15, 0)));
        this.level = 0;
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

    public boolean doesSideBlockRendering(EnumFacing side) {
        return false;
    }

    @SideOnly(Side.CLIENT)
    public boolean shouldSideBeRendered(EnumFacing side, BlockPos otherPos) {
        return false;
    }

    @Override
    public boolean isNormalCube() {
        return false;
    }

    @Override
    public void onLoaded() {
        super.onLoaded();
        if (!this.getWorld().isRemote) {
            inputSlotA.load();
            this.getOutput();
        }
        this.output = inputSlotA.process();
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
        ItemStack stack = player.getHeldItem(hand);
        if (!stack.isEmpty() && outputSlot.isEmpty()) {
            if (stack.getItem() == IUItem.iudust && stack.getItemDamage() == 60) {
                if (this.inputSlotA.get(1).isEmpty()) {
                    final ItemStack stack1 = stack.copy();
                    if (stack1.getCount() > 3) {
                        stack1.setCount(3);
                        stack.shrink(3);
                    } else {
                        stack.shrink(stack1.getCount());
                    }
                    this.inputSlotA.put(1, stack1);
                    if (!world.isRemote) {
                        getOutput();
                    }
                    return true;
                } else if (!this.inputSlotA.get(1).isEmpty() && this.inputSlotA.get(1).isItemEqual(stack)) {
                    int minCount = 3 - this.inputSlotA.get(1).getCount();
                    minCount = Math.min(stack.getCount(), minCount);
                    this.inputSlotA.get(1).grow(minCount);
                    stack.grow(-minCount);
                    if (!world.isRemote) {
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
                        this.inputSlotA.put(0, stack1);
                        if (!world.isRemote) {
                            getOutput();
                        }
                        return true;
                    }
                }

            }
        } else {
            if (!outputSlot.isEmpty()) {
                if (!world.isRemote) {
                    ModUtils.dropAsEntity(world, pos, outputSlot.get(), player);
                }
                outputSlot.put(0, ItemStack.EMPTY);
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
        return IUItem.primalSiliconCrystal;
    }

    @Override
    public IMultiTileBlock getTeBlock() {
        return BlockPrimalSiliconCrystalHandler.primal_silicon_crystal_handler;
    }

    @Override
    public void onUpdate() {

    }


    @Override
    public ContainerSiliconCrystalHandler getGuiContainer(final EntityPlayer var1) {
        return null;
    }

    @Override
    @SideOnly(Side.CLIENT)
    public GuiScreen getGui(final EntityPlayer var1, final boolean var2) {
        return null;
    }


    @Override
    public void updateEntityServer() {
        super.updateEntityServer();
        if (this.getWorld().provider.getWorldTime() % 20 == 0) {
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

        if (this.inputSlotA.get().isEmpty() || this.output == null || !this.outputSlot.get().isEmpty()) {
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

    @Override
    public NBTTagCompound writeToNBT(final NBTTagCompound nbttagcompound) {
        super.writeToNBT(nbttagcompound);
        nbttagcompound.setInteger("level", this.level);
        return nbttagcompound;
    }

    public List<ItemStack> getWrenchDrops(EntityPlayer player, int fortune) {
        List<ItemStack> ret = super.getWrenchDrops(player, fortune);

        return ret;
    }

    @Override
    public boolean hasCapability(@NotNull final Capability<?> capability, final EnumFacing facing) {
        return super.hasCapability(capability, facing) && capability != CapabilityItemHandler.ITEM_HANDLER_CAPABILITY;
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
    public void readFromNBT(final NBTTagCompound nbttagcompound) {
        super.readFromNBT(nbttagcompound);
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
