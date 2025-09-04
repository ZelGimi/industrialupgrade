package com.denfop.blockentity.mechanism;

import com.denfop.IUItem;
import com.denfop.api.Recipes;
import com.denfop.api.blockentity.MultiBlockEntity;
import com.denfop.api.container.CustomWorldContainer;
import com.denfop.api.recipe.IHasRecipe;
import com.denfop.api.recipe.IUpdateTick;
import com.denfop.api.recipe.InventoryRecipes;
import com.denfop.api.recipe.MachineRecipe;
import com.denfop.api.upgrades.UpgradableProperty;
import com.denfop.blockentity.base.BlockEntityElectricMachine;
import com.denfop.blocks.BlockTileEntity;
import com.denfop.blocks.mechanism.BlockSolderingMechanismEntity;
import com.denfop.componets.ComponentProgress;
import com.denfop.containermenu.ContainerMenuBase;
import com.denfop.containermenu.ContainerMenuSolderingMechanism;
import com.denfop.inventory.Inventory;
import com.denfop.network.DecoderHandler;
import com.denfop.network.EncoderHandler;
import com.denfop.network.IUpdatableTileEvent;
import com.denfop.network.packet.CustomPacketBuffer;
import com.denfop.screen.ScreenIndustrialUpgrade;
import com.denfop.screen.ScreenSolderingMechanism;
import com.denfop.utils.Localization;
import com.denfop.world.WorldBaseGen;
import net.minecraft.core.BlockPos;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.state.BlockState;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.api.distmarker.OnlyIn;

import java.io.IOException;
import java.util.EnumSet;
import java.util.List;
import java.util.Random;
import java.util.Set;

public class BlockEntityPrimalSolderingMechanism extends BlockEntityElectricMachine implements IUpdateTick, IHasRecipe,
        IUpdatableTileEvent {


    private static final int RED = 0;
    private static final int GRAY = 1;
    private static final int GREEN = 2;
    private static final int YELLOW = 3;
    private static final int SIZE = 156;
    private static Random random = new Random();
    public final ComponentProgress componentProgress;
    private final int MIN_DISTANCE = 5;
    private final int MAX_ATTEMPTS = 100;
    public Inventory solderingIronSlot;
    public InventoryRecipes inputSlotA;
    public MachineRecipe output;
    public boolean start;
    public int[] data;
    int failed = 0;
    private int RED_PERCENT = 35;
    private int GREEN_PERCENT = 80;
    private int YELLOW_PERCENT = 100 - RED_PERCENT - GREEN_PERCENT;

    public BlockEntityPrimalSolderingMechanism(BlockPos pos, BlockState state) {
        super(0, 0, 1, BlockSolderingMechanismEntity.primal_soldering_mechanism, pos, state);
        this.output = null;
        solderingIronSlot = new Inventory(this, Inventory.TypeItemSlot.INPUT, 1) {
            @Override
            public boolean canPlaceItem(final int index, final ItemStack stack) {
                return stack.getItem() == IUItem.solderingIron.getItem();
            }
        };
        this.componentProgress = this.addComponent(new ComponentProgress(this, 1,
                (short) 300
        ));


        this.inputSlotA = new InventoryRecipes(this, "microchip", this);
        Recipes.recipes.addInitRecipes(this);
    }

    public static int applyModifier(int base, int extra, double multiplier) {
        double ret = Math.round((base + extra) * multiplier);
        return (ret > 2.147483647E9D) ? Integer.MAX_VALUE : (int) ret;
    }


    private int[] generateColorStrip() {
        int[] data = new int[SIZE];

        fillGrayZones(data);

        createGrayGaps(data);

        createIslandsWithSpacing(data, RED, RED_PERCENT);
        createIslandsWithSpacing(data, GREEN, GREEN_PERCENT);


        return data;
    }

    ;

    private void fillGrayZones(int[] data) {
        for (int i = 0; i < SIZE; i++) {
            data[i] = GRAY;
        }
    }

    private void createGrayGaps(int[] data) {
        int index = 0;
        while (index < SIZE) {
            int length = random.nextInt(11) + 10;
            if (index + length > SIZE) {
                length = SIZE - index;
            }
            for (int i = index; i < index + length; i++) {
                data[i] = GRAY;
            }
            index += length + MIN_DISTANCE;
        }
    }

    private void createIslandsWithSpacing(int[] data, int color, int percent) {
        int totalColorCount = SIZE * percent / 100;
        int numIslands = random.nextInt(3) + 3;

        while (totalColorCount > 0 && numIslands > 0) {
            int islandLength = random.nextInt(21) + 10;
            if (islandLength > totalColorCount) {
                islandLength = totalColorCount;
            }


            boolean placed = false;
            int attempts = 0;
            while (!placed && attempts < MAX_ATTEMPTS) {
                int startIndex = random.nextInt(SIZE - islandLength);
                if (isValidPlacement(data, startIndex, islandLength, color)) {
                    for (int i = startIndex; i < startIndex + islandLength; i++) {
                        data[i] = color;
                    }
                    totalColorCount -= islandLength;
                    placed = true;
                    numIslands--;
                }
                attempts++;
            }

            if (attempts >= MAX_ATTEMPTS) {
                break;
            }
        }
    }

    private boolean isValidPlacement(int[] data, int startIndex, int length, int color) {
        for (int i = startIndex; i < startIndex + length; i++) {
            if (data[i] != GRAY) {
                return false;
            }
        }


        if (startIndex > 0 && data[startIndex - 1] == color) {
            return false;
        }
        if (startIndex + length < SIZE && data[startIndex + length] == color) {
            return false;
        }

        return true;
    }

    public void updateTileServer(Player var1, double var2) {
        if (start && var2 == 0) {
            this.componentProgress.addProgress(0, (short) 8);
            if (componentProgress.getProgress(0) >= 300) {
                componentProgress.setProgress((short) 300);
            }
            this.solderingIronSlot.set(0, this.solderingIronSlot.get(0).getItem().getCraftingRemainingItem(this.solderingIronSlot.get(0)));
        } else if (start && var2 == 1) {
            this.componentProgress.addProgress(0, (short) -5);
            if (componentProgress.getProgress(0) < 0) {
                componentProgress.setProgress((short) 0);
            }
            this.solderingIronSlot.set(0, this.solderingIronSlot.get(0).getItem().getCraftingRemainingItem(this.solderingIronSlot.get(0)));
            failed++;
        } else if (start && var2 == 2) {
            this.componentProgress.addProgress(0, (short) -2);
            if (componentProgress.getProgress(0) < 0) {
                componentProgress.setProgress((short) 0);
            }
            this.solderingIronSlot.set(0, this.solderingIronSlot.get(0).getItem().getCraftingRemainingItem(this.solderingIronSlot.get(0)));

        }
        if (failed >= 12) {
            failed = 0;
            if (WorldBaseGen.random.nextDouble() < 0.25) {
                this.inputSlotA.consume();
            }
            this.componentProgress.setProgress(0, (short) 0);
            start = false;
            this.getOutput();
        }
    }

    public void onLoaded() {
        super.onLoaded();
        inputSlotA.load();
        this.getOutput();
    }

    public void onUnloaded() {
        super.onUnloaded();
    }

    @Override
    public void readContainerPacket(final CustomPacketBuffer customPacketBuffer) {
        super.readContainerPacket(customPacketBuffer);
        try {
            guiChargeLevel = (double) DecoderHandler.decode(customPacketBuffer);
            start = (boolean) DecoderHandler.decode(customPacketBuffer);
            data = (int[]) DecoderHandler.decode(customPacketBuffer);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

    }

    @Override
    public CustomPacketBuffer writeContainerPacket() {
        final CustomPacketBuffer packet = super.writeContainerPacket();
        try {
            EncoderHandler.encode(packet, guiChargeLevel);
            EncoderHandler.encode(packet, start);
            EncoderHandler.encode(packet, data);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        return packet;
    }

    @Override
    public void addInformation(final ItemStack stack, final List<String> tooltip) {
        tooltip.add(Localization.translate("iu.primal_repair5"));
    }

    public MachineRecipe getOutput() {
        this.output = this.inputSlotA.process();


        return this.output;
    }

    @Override
    public void updateEntityServer() {
        super.updateEntityServer();
        if (!start) {
            if (output != null) {
                if (!this.solderingIronSlot.isEmpty() && this.inputSlotA.continue_process(this.output) && this.outputSlot.canAdd(
                        this.output.getRecipe().getOutput().items)) {
                    this.start = true;
                    this.data = generateColorStrip();
                } else {
                    this.start = false;
                }
            }
        } else {
            if (output == null || this.solderingIronSlot.isEmpty()) {
                start = false;
            }
        }
        if (start && componentProgress.getBar() >= 1) {
            this.inputSlotA.consume();
            this.componentProgress.setProgress(0, (short) 0);
            this.data = generateColorStrip();
            failed = 0;
            this.outputSlot.add(this.output.getRecipe().getOutput().items);
            this.getOutput();
        }
    }

    public ContainerMenuSolderingMechanism getGuiContainer(Player entityPlayer) {
        return new ContainerMenuSolderingMechanism(
                entityPlayer, this);
    }

    public MultiBlockEntity getTeBlock() {
        return BlockSolderingMechanismEntity.primal_soldering_mechanism;
    }

    public BlockTileEntity getBlock() {
        return IUItem.solderingMechanism.getBlock();
    }

    public void init() {

    }


    @Override
    public void onUpdate() {

    }

    @Override
    public MachineRecipe getRecipeOutput() {
        return this.output;
    }

    @Override
    public void setRecipeOutput(final MachineRecipe output) {
        this.output = output;
    }


    @OnlyIn(Dist.CLIENT)
    public ScreenIndustrialUpgrade<ContainerMenuBase<? extends CustomWorldContainer>> getGui(Player entityPlayer, ContainerMenuBase<? extends CustomWorldContainer> isAdmin) {
        return new ScreenSolderingMechanism((ContainerMenuSolderingMechanism) isAdmin);
    }

    public String getStartSoundFile() {
        return "Machines/genmirc.ogg";
    }

    public String getInterruptSoundFile() {
        return "Machines/InterruptOne.ogg";
    }

    public float getWrenchDropRate() {
        return 0.85F;
    }

    public Set<UpgradableProperty> getUpgradableProperties() {
        return EnumSet.of(UpgradableProperty.Processing, UpgradableProperty.Transformer,
                UpgradableProperty.EnergyStorage, UpgradableProperty.ItemExtract, UpgradableProperty.ItemInput
        );
    }

}
