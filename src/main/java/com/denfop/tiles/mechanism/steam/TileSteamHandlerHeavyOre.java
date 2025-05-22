package com.denfop.tiles.mechanism.steam;

import com.denfop.IUItem;
import com.denfop.Localization;
import com.denfop.api.gui.IType;
import com.denfop.api.recipe.IUpdateTick;
import com.denfop.api.recipe.InvSlotOutput;
import com.denfop.api.recipe.InvSlotRecipes;
import com.denfop.api.recipe.MachineRecipe;
import com.denfop.api.tile.IMultiTileBlock;
import com.denfop.api.upgrades.UpgradableProperty;
import com.denfop.audio.EnumSound;
import com.denfop.blocks.BlockTileEntity;
import com.denfop.blocks.FluidName;
import com.denfop.blocks.mechanism.BlockBaseMachine3;
import com.denfop.componets.ComponentProgress;
import com.denfop.componets.ComponentSteamEnergy;
import com.denfop.componets.ComponentSteamProcess;
import com.denfop.componets.EnumTypeStyle;
import com.denfop.componets.Fluids;
import com.denfop.componets.HeatComponent;
import com.denfop.componets.PressureComponent;
import com.denfop.container.ContainerSteamHandlerHeavyOre;
import com.denfop.gui.GuiSteamHandlerHeavyOre;
import com.denfop.invslot.InvSlot;
import com.denfop.tiles.base.TileElectricMachine;
import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.item.ItemStack;
import net.minecraft.util.SoundEvent;
import net.minecraft.util.math.BlockPos;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import org.lwjgl.input.Keyboard;

import java.util.EnumSet;
import java.util.List;
import java.util.Random;
import java.util.Set;

public class TileSteamHandlerHeavyOre extends TileElectricMachine
        implements IUpdateTick, IType {


    public final InvSlotOutput outputSlot;
    public final ComponentSteamProcess componentProcess;
    public final ComponentProgress componentProgress;
    public final Fluids.InternalFluidTank fluidTank;
    public final PressureComponent pressure;
    public final ComponentSteamEnergy steam;
    public final HeatComponent heat;
    private final EnumTypeStyle enumTypeSlot;
    private final double coef;
    private final Fluids fluids;
    public InvSlotRecipes inputSlotA;
    public MachineRecipe output;
    private int[] col;
    private boolean work = false;

    public TileSteamHandlerHeavyOre() {
        this(1, 300, 3, EnumTypeStyle.DEFAULT);
    }

    public TileSteamHandlerHeavyOre(
            int energyPerTick,
            int length,
            int outputSlots,
            EnumTypeStyle enumTypeSlot
    ) {
        super(0, 1, 1);
        this.enumTypeSlot = enumTypeSlot;
        this.outputSlot = new InvSlotOutput(this, outputSlots + 2 * enumTypeSlot.ordinal());
        this.inputSlotA = new InvSlotRecipes(this, "handlerho", this);
        this.col = new int[0];
        this.coef = getCoef();
        this.componentProgress = this.addComponent(new ComponentProgress(this, 1,
                (short) length
        ));
        this.fluids = this.addComponent(new Fluids(this));
        this.fluidTank = fluids.addTank("fluidTank2", 4000, InvSlot.TypeItemSlot.NONE, Fluids.fluidPredicate(
                FluidName.fluidsteam.getInstance()
        ));
        this.heat = this.addComponent(HeatComponent
                .asBasicSink(this, 3000));
        this.pressure = this.addComponent(PressureComponent.asBasicSink(this, 3));
        this.steam = this.addComponent(ComponentSteamEnergy.asBasicSink(this, 4000));
        this.steam.setFluidTank(fluidTank);
        this.componentProcess = this.addComponent(new ComponentSteamProcess(this, (int) (length / this.getSpeed()),
                energyPerTick, 3
        ) {
            @Override
            public void operateWithMax(final MachineRecipe output) {
                if (output.getRecipe() == null) {
                    return;
                }
                for (int i = 0; i < 1; i++) {
                    operate(output);
                }

            }

            @Override
            public void operateOnce(final List<ItemStack> processResult) {
                for (int i = 0; i < col.length; i++) {
                    final Random rand = world.rand;
                    if ((100 - col[i]) <= rand.nextInt(100)) {
                        this.outputSlot.add(processResult.get(i));
                    }
                }
                this.invSlotRecipes.consume();
            }
        });

        this.componentProcess.setSlotOutput(outputSlot);
        this.componentProcess.setInvSlotRecipes(this.inputSlotA);

    }

    public static int applyModifier(int base, int extra, double multiplier) {
        double ret = Math.round((base + extra) * multiplier);
        return (ret > 2.147483647E9D) ? Integer.MAX_VALUE : (int) ret;
    }

    @Override
    public BlockTileEntity getBlock() {
        return IUItem.basemachine2;
    }

    @Override
    public IMultiTileBlock getTeBlock() {
        return BlockBaseMachine3.steam_handler_ore;
    }

    protected double getCoef() {
        switch (this.enumTypeSlot) {
            case ADVANCED:
                return 1.1;
            case PERFECT:
                return 1.3;
            case IMPROVED:
                return 1.2;
            default:
                return 1;
        }
    }

    @Override
    public EnumTypeStyle getStyle() {
        return this.enumTypeSlot;
    }


    @Override
    @SideOnly(Side.CLIENT)
    public void addInformation(final ItemStack stack, final List<String> tooltip) {

        if (!Keyboard.isKeyDown(Keyboard.KEY_LSHIFT)) {
            tooltip.add(Localization.translate("press.lshift"));
        }
        if (Keyboard.isKeyDown(Keyboard.KEY_LSHIFT)) {
            tooltip.add(Localization.translate("iu.heatmachine.info"));

        }
        super.addInformation(stack, tooltip);
    }


    public Set<UpgradableProperty> getUpgradableProperties() {
        return EnumSet.of(UpgradableProperty.Processing, UpgradableProperty.Transformer,
                UpgradableProperty.EnergyStorage, UpgradableProperty.ItemExtract, UpgradableProperty.ItemInput
        );
    }

    public void onLoaded() {
        super.onLoaded();
        if (!this.getWorld().isRemote) {
            inputSlotA.load();
            this.getOutput();
            IBlockState blockState = world.getBlockState(this.pos.down());
            if (blockState.getMaterial() != Material.AIR) {
                this.work = blockState.getBlock() == Blocks.LAVA || blockState.getBlock() == Blocks.FLOWING_LAVA || blockState.getBlock() == FluidName.fluidpahoehoe_lava
                        .getInstance()
                        .getBlock();
            } else {
                work = false;
            }
        }
    }

    public void onUnloaded() {
        super.onUnloaded();
    }


    @Override
    public ContainerSteamHandlerHeavyOre getGuiContainer(EntityPlayer entityPlayer) {
        return new ContainerSteamHandlerHeavyOre(entityPlayer, this);

    }

    @Override
    public void onNeighborChange(final Block neighbor, final BlockPos neighborPos) {
        super.onNeighborChange(neighbor, neighborPos);
        if (work) {
            if (this.pos.down().distanceSq(neighborPos) == 0) {
                IBlockState blockState = world.getBlockState(this.pos.down());
                if (blockState.getMaterial() != Material.AIR) {
                    this.work = blockState.getBlock() == Blocks.LAVA || blockState.getBlock() == Blocks.FLOWING_LAVA || blockState.getBlock() == FluidName.fluidpahoehoe_lava
                            .getInstance()
                            .getBlock();
                } else {
                    work = false;
                }
            }
        } else {
            if (this.pos.down().distanceSq(neighborPos) == 0) {
                IBlockState blockState = world.getBlockState(this.pos.down());
                if (blockState.getMaterial() != Material.AIR) {
                    this.work = blockState.getBlock() == Blocks.LAVA || blockState.getBlock() == Blocks.FLOWING_LAVA || blockState.getBlock() == FluidName.fluidpahoehoe_lava
                            .getInstance()
                            .getBlock();
                } else {
                    work = false;
                }
            }
        }
    }

    @SideOnly(Side.CLIENT)
    public GuiScreen getGui(EntityPlayer entityPlayer, boolean isAdmin) {
        return new GuiSteamHandlerHeavyOre(new ContainerSteamHandlerHeavyOre(entityPlayer, this));
    }

    public void operateOnce(List<ItemStack> processResult) {
        for (int i = 0; i < col.length; i++) {
            final Random rand = world.rand;
            if ((100 - col[i]) <= rand.nextInt(100)) {
                this.outputSlot.add(processResult.get(i));
            }
        }
        this.inputSlotA.consume();
    }

    public MachineRecipe getOutput() {

        this.output = this.inputSlotA.process();


        return this.output;
    }

    @Override
    public void updateEntityServer() {
        super.updateEntityServer();
        if (this.work && this.getWorld().provider.getWorldTime() % 2 == 0) {
            this.heat.addEnergy(1);
        }
        if (this.getWorld().provider.getWorldTime() % 20 == 0) {
            this.heat.useEnergy(1);
        }
    }


    @Override
    public SoundEvent getSound() {
        return EnumSound.handlerho.getSoundEvent();
    }

    @Override
    public void onUpdate() {

    }

    private double getSpeed() {
        switch (this.enumTypeSlot) {
            case ADVANCED:
                return 1.3;
            case IMPROVED:
                return 1.5;
            case PERFECT:
                return 2;
            default:
                return 1;
        }
    }

    @Override
    public MachineRecipe getRecipeOutput() {
        return this.output;
    }

    @Override
    public void setRecipeOutput(final MachineRecipe output) {
        this.output = output;
        if (output == null) {
            this.col = new int[0];
        } else {
            this.col = new int[output.getRecipe().output.items.size()];
            for (int i = 0; i < col.length; i++) {
                col[i] = (int) (output.getRecipe().output.metadata.getInteger(("input" + i)) * this.coef);
                col[i] = Math.max(0, Math.min(col[i], 95) - 5);
            }
        }
    }

}
