package com.denfop.blockentity.mechanism.multimechanism.simple;

import com.denfop.IUItem;
import com.denfop.api.Recipes;
import com.denfop.api.blockentity.MultiBlockEntity;
import com.denfop.api.recipe.BaseMachineRecipe;
import com.denfop.api.recipe.Input;
import com.denfop.api.recipe.RecipeOutput;
import com.denfop.api.widget.EnumTypeSlot;
import com.denfop.blockentity.base.BlockEntityMultiMachine;
import com.denfop.blockentity.base.EnumMultiMachine;
import com.denfop.blockentity.mechanism.multimechanism.IFarmer;
import com.denfop.blocks.BlockTileEntity;
import com.denfop.blocks.mechanism.BlockMoreMachine3Entity;
import com.denfop.componets.AirPollutionComponent;
import com.denfop.componets.SoilPollutionComponent;
import com.denfop.inventory.Inventory;
import com.denfop.network.packet.CustomPacketBuffer;
import com.denfop.recipe.IInputHandler;
import com.denfop.utils.Localization;
import net.minecraft.core.BlockPos;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;

public class BlockEntityFermer extends BlockEntityMultiMachine implements IFarmer {

    private final Inventory fertilizerSlot;
    private final AirPollutionComponent pollutionAir;
    private final SoilPollutionComponent pollutionSoil;
    int col = 0;

    public BlockEntityFermer(BlockPos pos, BlockState state) {
        super(EnumMultiMachine.Fermer.usagePerTick, EnumMultiMachine.Fermer.lenghtOperation, BlockMoreMachine3Entity.farmer, pos, state);
        this.fertilizerSlot = new Inventory(this, Inventory.TypeItemSlot.INPUT, 1) {
            @Override
            public boolean canPlaceItem(final int index, final ItemStack stack) {
                return stack.getItem() == IUItem.fertilizer.getItem();
            }

            public EnumTypeSlot getTypeSlot() {
                return EnumTypeSlot.FERTILIZER;
            }
        };
        Recipes.recipes.addInitRecipes(this);
        this.pollutionSoil = this.addComponent(new SoilPollutionComponent(this, 0.1));
        this.pollutionAir = this.addComponent(new AirPollutionComponent(this, 0.15));
    }

    public static void addrecipe(ItemStack input, Item output) {
        final IInputHandler input1 = com.denfop.api.Recipes.inputFactory;
        Recipes.recipes.addRecipe(
                "farmer",
                new BaseMachineRecipe(
                        new Input(
                                input1.getInput(input)
                        ),
                        new RecipeOutput(null, new ItemStack(output))
                )
        );
    }

    public static void addrecipe(ItemStack input, Item output, int n) {
        final IInputHandler input1 = com.denfop.api.Recipes.inputFactory;
        Recipes.recipes.addRecipe(
                "farmer",
                new BaseMachineRecipe(
                        new Input(
                                input1.getInput(input)
                        ),
                        new RecipeOutput(null, new ItemStack(output, n))
                )
        );
    }

    public static void addrecipe(Item input, ItemStack output, int n) {
        final IInputHandler input1 = com.denfop.api.Recipes.inputFactory;
        Recipes.recipes.addRecipe(
                "farmer",
                new BaseMachineRecipe(
                        new Input(
                                input1.getInput(new ItemStack(input))
                        ),
                        new RecipeOutput(null, new ItemStack(output.getItem(), n
                        )
                        )
                )
        );

    }

    public static void addrecipe(Item input, Item output) {
        final IInputHandler input1 = com.denfop.api.Recipes.inputFactory;
        Recipes.recipes.addRecipe(
                "farmer",
                new BaseMachineRecipe(
                        new Input(
                                input1.getInput(new ItemStack(input))
                        ),
                        new RecipeOutput(null, new ItemStack(output)
                        )
                )
        );
    }

    public static void addrecipe(ItemStack input, ItemStack output) {
        final IInputHandler input1 = com.denfop.api.Recipes.inputFactory;

        Recipes.recipes.addRecipe(
                "farmer",
                new BaseMachineRecipe(
                        new Input(
                                input1.getInput((input))
                        ),
                        new RecipeOutput(null, output
                        )
                )
        );
    }

    public static void addrecipe(ItemStack input, ItemStack output, int n) {
        final IInputHandler input1 = com.denfop.api.Recipes.inputFactory;
        output = output.copy();
        output.setCount(n);
        Recipes.recipes.addRecipe(
                "farmer",
                new BaseMachineRecipe(
                        new Input(
                                input1.getInput((input))
                        ),
                        new RecipeOutput(null, output
                        )
                )
        );
    }

    public static void addrecipe(Item input, Item output, int n) {
        final IInputHandler input1 = com.denfop.api.Recipes.inputFactory;
        Recipes.recipes.addRecipe(
                "farmer",
                new BaseMachineRecipe(
                        new Input(
                                input1.getInput(new ItemStack(input))
                        ),
                        new RecipeOutput(null, new ItemStack(output, n)
                        )
                )
        );
    }

    @Override
    public Inventory getFertilizerSlot() {
        return fertilizerSlot;
    }

    @Override
    public int getSize(int size) {
        size = Math.min(super.getSize(size), fertilizerSlot.get(0).getCount() * 8 + col);
        return size;

    }

    @Override
    public boolean canoperate(final int size) {
        return !fertilizerSlot.isEmpty() && fertilizerSlot.get(0).getCount() * 8 + col >= size;
    }

    public int getFertilizer() {
        return col;
    }

    @Override
    public void readContainerPacket(CustomPacketBuffer customPacketBuffer) {
        super.readContainerPacket(customPacketBuffer);
        this.col = customPacketBuffer.readInt();
    }

    @Override
    public CustomPacketBuffer writeContainerPacket() {
        CustomPacketBuffer packetBuffer = super.writeContainerPacket();
        packetBuffer.writeInt(col);
        return packetBuffer;
    }

    @Override
    public void consume(final int size) {
        int size1 = size;
        while (size1 > 0) {
            if (col == 0) {
                col += 16;
                fertilizerSlot.get(0).shrink(1);
            }
            if (size1 <= col) {
                col -= size1;
                size1 = 0;
            } else {
                size1 -= col;
                col = 0;
            }
        }

    }

    public MultiBlockEntity getTeBlock() {
        return BlockMoreMachine3Entity.farmer;
    }

    public BlockTileEntity getBlock() {
        return IUItem.machines_base3.getBlock(getTeBlock().getId());
    }

    public void init() {
        addrecipe(Items.WHEAT_SEEDS, Items.WHEAT, 2);
        addrecipe(Items.WHEAT, Items.WHEAT_SEEDS, 1);
        addrecipe(new ItemStack(IUItem.rubberSapling.getItem()), new ItemStack(IUItem.rubWood.getItem(0)), 1);
        addrecipe(new ItemStack(IUItem.rubWood.getItem(0)), new ItemStack(IUItem.rawLatex.getItem(), 2), 2);
        addrecipe(IUItem.rawLatex.getItem(), new ItemStack(IUItem.rubberSapling.getItem()), 1);
        addrecipe(Items.CARROT, Items.CARROT, 2);
        addrecipe(Items.POTATO, Items.POTATO, 2);
        addrecipe(Blocks.PUMPKIN.asItem(), Items.PUMPKIN_SEEDS, 1);

        addrecipe(Items.PUMPKIN_SEEDS, Blocks.PUMPKIN.asItem(), 2);
        addrecipe(Items.MELON_SEEDS, Items.MELON, 2);
        addrecipe(Items.MELON, Items.MELON_SEEDS, 1);

        addrecipe(new ItemStack(Blocks.OAK_SAPLING, 1), new ItemStack(Blocks.OAK_LOG, 2));
        addrecipe(new ItemStack(Blocks.BIRCH_SAPLING, 1), new ItemStack(Blocks.BIRCH_LOG, 2));
        addrecipe(new ItemStack(Blocks.ACACIA_SAPLING, 1), new ItemStack(Blocks.ACACIA_LOG, 2));
        addrecipe(new ItemStack(Blocks.JUNGLE_SAPLING, 1), new ItemStack(Blocks.JUNGLE_LOG, 2));
        addrecipe(new ItemStack(Blocks.DARK_OAK_SAPLING, 1), new ItemStack(Blocks.DARK_OAK_LOG, 2));
        addrecipe(new ItemStack(Blocks.SPRUCE_SAPLING, 1), new ItemStack(Blocks.SPRUCE_LOG, 2));

        addrecipe(new ItemStack(Blocks.OAK_LOG, 1), new ItemStack(Blocks.OAK_SAPLING, 2));
        addrecipe(new ItemStack(Blocks.BIRCH_LOG, 1), new ItemStack(Blocks.BIRCH_SAPLING, 2));
        addrecipe(new ItemStack(Blocks.ACACIA_LOG, 1), new ItemStack(Blocks.ACACIA_SAPLING, 2));
        addrecipe(new ItemStack(Blocks.JUNGLE_LOG, 1), new ItemStack(Blocks.JUNGLE_SAPLING, 2));
        addrecipe(new ItemStack(Blocks.DARK_OAK_LOG, 1), new ItemStack(Blocks.DARK_OAK_SAPLING, 2));
        addrecipe(new ItemStack(Blocks.SPRUCE_LOG, 1), new ItemStack(Blocks.SPRUCE_SAPLING, 2));


    }

    @Override
    public EnumMultiMachine getMachine() {
        return EnumMultiMachine.Fermer;
    }

    public String getInventoryName() {
        return Localization.translate("iu.blockFermer.name");
    }

    public String getStartSoundFile() {
        return "Machines/Fermer.ogg";
    }

    public String getInterruptSoundFile() {
        return "Machines/InterruptOne.ogg";
    }


}
