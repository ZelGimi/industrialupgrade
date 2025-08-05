package com.denfop.tiles.mechanism;

import com.denfop.IUItem;
import com.denfop.api.Recipes;
import com.denfop.api.gui.EnumTypeSlot;
import com.denfop.api.inv.IAdvInventory;
import com.denfop.api.recipe.*;
import com.denfop.api.tile.IMultiTileBlock;
import com.denfop.api.upgrades.UpgradableProperty;
import com.denfop.blocks.BlockTileEntity;
import com.denfop.blocks.mechanism.BlockBaseMachine;
import com.denfop.componets.AirPollutionComponent;
import com.denfop.componets.SoilPollutionComponent;
import com.denfop.container.ContainerBase;
import com.denfop.container.ContainerBaseGenerationChipMachine;
import com.denfop.datacomponent.DataComponentsInit;
import com.denfop.gui.GuiCore;
import com.denfop.gui.GuiGenerationMicrochip;
import com.denfop.invslot.InvSlot;
import com.denfop.recipe.IInputHandler;
import com.denfop.recipe.IInputItemStack;
import com.denfop.tiles.base.TileBaseGenerationMicrochip;
import com.denfop.utils.ParticleUtils;
import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.ItemTags;
import net.minecraft.tags.TagKey;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.block.state.BlockState;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.api.distmarker.OnlyIn;

import java.util.EnumSet;
import java.util.Optional;
import java.util.Set;

public class TileGenerationMicrochip extends TileBaseGenerationMicrochip implements IUpdateTick, IHasRecipe {


    public final InvSlot input_slot;
    private final SoilPollutionComponent pollutionSoil;
    private final AirPollutionComponent pollutionAir;

    public TileGenerationMicrochip(BlockPos pos, BlockState blockState) {
        super(1, 300, 1, BlockBaseMachine.generator_microchip, pos, blockState);
        this.inputSlotA = new InvSlotRecipes(this, "microchip", this);
        Recipes.recipes.addInitRecipes(this);
        this.componentProcess.setInvSlotRecipes(inputSlotA);
        this.input_slot = new InvSlot(this, InvSlot.TypeItemSlot.INPUT, 1) {
            @Override
            public ItemStack set(final int index, final ItemStack content) {
                super.set(index, content);
                if (this.get(0).isEmpty()) {
                    ((TileGenerationMicrochip) this.base).inputSlotA.changeAccepts(ItemStack.EMPTY);
                } else {
                    ((TileGenerationMicrochip) this.base).inputSlotA.changeAccepts(this.get(0));
                }
                return content;
            }

            @Override
            public boolean accepts(final ItemStack stack, final int index) {
                return stack.getItem() == IUItem.recipe_schedule.getItem();
            }

            @Override
            public EnumTypeSlot getTypeSlot() {
                return EnumTypeSlot.RECIPE_SCHEDULE;
            }
        };
        this.pollutionSoil = this.addComponent(new SoilPollutionComponent(this, 0.15));
        this.pollutionAir = this.addComponent(new AirPollutionComponent(this, 0.2));
    }

    private static void add(
            ItemStack second, ItemStack three,
            ItemStack four, ItemStack five,
            ItemStack output
    ) {
        IInputItemStack first1;
        IInputItemStack second1;
        IInputItemStack three1;
        IInputItemStack four1;
        IInputItemStack five1;

        CompoundTag nbt = new CompoundTag();
        nbt.putShort("temperature", (short) 4000);
        final IInputHandler input = com.denfop.api.Recipes.inputFactory;
        first1 = input.getInput("c:ingots/aluminium");

        second1 = input.getInput(second);
        three1 = input.getInput(three);
        four1 = input.getInput(four);
        five1 = input.getInput(five);

        Recipes.recipes.addRecipe(
                "microchip",
                new BaseMachineRecipe(
                        new Input(first1, second1, three1, four1, five1),
                        new RecipeOutput(nbt, output)
                )
        );
    }

    public static void add(
            ItemStack first,
            ItemStack second,
            ItemStack three,
            ItemStack four,
            ItemStack five,
            ItemStack output,
            short temperatures,
            boolean check
    ) {
        IInputItemStack first1;
        IInputItemStack second1;
        IInputItemStack three1;
        IInputItemStack four1;
        IInputItemStack five1;

        CompoundTag nbt = new CompoundTag();
        nbt.putShort("temperature", temperatures);
        final IInputHandler input = com.denfop.api.Recipes.inputFactory;

        if (check) {
            first1 = getInputFromTagOrStack(first, input);
            second1 = getInputFromTagOrStack(second, input);
            three1 = getInputFromTagOrStack(three, input);
            four1 = getInputFromTagOrStack(four, input);
            five1 = getInputFromTagOrStack(five, input);
        } else {
            first1 = input.getInput(first, first.getCount());
            second1 = input.getInput(second, second.getCount());
            three1 = input.getInput(three, three.getCount());
            four1 = input.getInput(four, four.getCount());
            five1 = input.getInput(five, five.getCount());
        }

        Recipes.recipes.addRecipe(
                "microchip",
                new BaseMachineRecipe(
                        new Input(first1, second1, three1, four1, five1),
                        new RecipeOutput(nbt, output)
                )
        );
    }

    private static IInputItemStack getInputFromTagOrStack(ItemStack stack, IInputHandler input) {
        Optional<TagKey<Item>> tag = stack.getTags()
                .filter(loc -> loc.location().getNamespace().equals("c") && loc.location().getPath().contains("/"))
                .findFirst();

        if (tag.isPresent() && stack.getItem() instanceof Item) {
            return input.getInput(tag.get(), stack.getCount());
        } else {
            return input.getInput(stack);
        }
    }

    public static void add(
            ItemStack first,
            ItemStack second,
            ItemStack three,
            ItemStack four,
            String five,
            ItemStack output,
            short temperatures,
            boolean check
    ) {
        IInputItemStack first1;
        IInputItemStack second1;
        IInputItemStack three1;
        IInputItemStack four1;
        IInputItemStack five1;

        CompoundTag nbt = new CompoundTag();
        nbt.putShort("temperature", temperatures);
        final IInputHandler input = com.denfop.api.Recipes.inputFactory;

        if (check) {
            first1 = getInputFromTagOrStack(first, input);
            second1 = getInputFromTagOrStack(second, input);
            three1 = getInputFromTagOrStack(three, input);
            four1 = getInputFromTagOrStack(four, input);
            five1 = input.getInput(ItemTags.create(ResourceLocation.tryParse(five)));

            Recipes.recipes.addRecipe(
                    "microchip",
                    new BaseMachineRecipe(
                            new Input(first1, second1, three1, four1, five1),
                            new RecipeOutput(nbt, output)
                    )
            );
        } else {
            Recipes.recipes.addRecipe("microchip", new BaseMachineRecipe(
                    new Input(
                            input.getInput(first),
                            input.getInput(second),
                            input.getInput(three),
                            input.getInput(four),
                            input.getInput(five)
                    ),
                    new RecipeOutput(nbt, output)
            ));
        }
    }

    public static void add(
            ItemStack first,
            ItemStack second,
            ItemStack three,
            String four,
            ItemStack five,
            ItemStack output,
            boolean check
    ) {
        IInputItemStack first1;
        IInputItemStack second1;
        IInputItemStack three1;
        IInputItemStack four1;
        IInputItemStack five1;

        CompoundTag nbt = new CompoundTag();
        nbt.putShort("temperature", (short) 4500);
        final IInputHandler input = com.denfop.api.Recipes.inputFactory;

        if (check) {
            first1 = getInputFromTagOrStack(first, input);
            second1 = getInputFromTagOrStack(second, input);
            three1 = getInputFromTagOrStack(three, input);
            four1 = input.getInput(ItemTags.create(ResourceLocation.tryParse(four)));
            five1 = getInputFromTagOrStack(five, input);
        } else {
            first1 = input.getInput(first);
            second1 = input.getInput(second);
            three1 = input.getInput(three);
            four1 = input.getInput(four);
            five1 = input.getInput(five);
        }

        Recipes.recipes.addRecipe(
                "microchip",
                new BaseMachineRecipe(
                        new Input(first1, second1, three1, four1, five1),
                        new RecipeOutput(nbt, output)
                )
        );
    }

    public static ItemStack getLevelCircuit(ItemStack stack, int level) {
        stack = stack.copy();
        stack.set(DataComponentsInit.LEVEL_MICROCHIP, level);
        return stack;
    }

    @Override
    public void updateEntityServer() {
        super.updateEntityServer();
        if (this.getActive() && this.level.getGameTime() % 5 == 0) {
            ParticleUtils.spawnMicrochipAssemblerParticles(level, pos, level.random);
        }
    }

    @Override
    public void onLoaded() {
        super.onLoaded();
        if (!this.getWorld().isClientSide) {
            if (this.input_slot.isEmpty()) {
                (this).inputSlotA.changeAccepts(ItemStack.EMPTY);
            } else {
                (this).inputSlotA.changeAccepts(this.input_slot.get(0));
            }
        }
    }

    public ContainerBaseGenerationChipMachine getGuiContainer(Player entityPlayer) {
        return new ContainerBaseGenerationChipMachine(
                entityPlayer, this);
    }

    public IMultiTileBlock getTeBlock() {
        return BlockBaseMachine.generator_microchip;
    }

    public BlockTileEntity getBlock() {
        return IUItem.machines.getBlock(getTeBlock().getId());
    }

    public void init() {
        add(new ItemStack(Items.FLINT), new ItemStack(Items.LAPIS_LAZULI), new ItemStack(Items.IRON_INGOT),
                new ItemStack(IUItem.iuingot.getItemFromMeta(11), 1), new ItemStack(IUItem.iuingot.getItemFromMeta(15)), new ItemStack(IUItem.basecircuit.getItemFromMeta(0)), (short) 3000, false
        );

        add(
                new ItemStack(IUItem.iuingot.getItemFromMeta(39)),
                new ItemStack(IUItem.iuingot.getItemFromMeta(30)),
                new ItemStack(IUItem.iuingot.getItemFromMeta(41)),
                new ItemStack(IUItem.alloysingot.getItemFromMeta(14)),
                new ItemStack(IUItem.alloysingot.getItemFromMeta(23)),
                new ItemStack(IUItem.basecircuit.getItemFromMeta(18)),
                (short) 5000, true
        );

        add(
                new ItemStack(Items.IRON_INGOT),
                new ItemStack(Items.REDSTONE),
                new ItemStack(Items.GOLD_INGOT),
                new ItemStack(Items.FLINT),
                new ItemStack(IUItem.iuingot.getItemFromMeta(14)),
                new ItemStack(IUItem.basecircuit.getItemFromMeta(15)),
                (short) 1000, true
        );

        add(
                new ItemStack(IUItem.iuingot.getItemFromMeta(1)),
                new ItemStack(Items.REDSTONE, 1),
                new ItemStack(Items.GOLD_INGOT),
                new ItemStack(IUItem.iuingot.getItemFromMeta(7)),
                "c:ingots/copper",
                new ItemStack(IUItem.basecircuit.getItemFromMeta(1)),
                (short) 4000, true
        );
        add(
                new ItemStack(Items.REDSTONE, 1),
                new ItemStack(Items.GOLD_INGOT),
                new ItemStack(IUItem.iuingot.getItemFromMeta(7)),
                IUItem.copperIngot,
                new ItemStack(IUItem.basecircuit.getItemFromMeta(1))
        );
        add(
                new ItemStack(IUItem.iuingot.getItemFromMeta(18)),
                new ItemStack(Items.REDSTONE, 1),
                new ItemStack(Items.DIAMOND),
                new ItemStack(IUItem.iuingot.getItemFromMeta(0)),
                new ItemStack(IUItem.iuingot.getItemFromMeta(5)),
                new ItemStack(IUItem.basecircuit.getItemFromMeta(2)),
                (short) 5000, true
        );
        add(
                new ItemStack(IUItem.iuingot.getItemFromMeta(18)),
                new ItemStack(Items.REDSTONE, 1),
                new ItemStack(Items.EMERALD),
                new ItemStack(IUItem.iuingot.getItemFromMeta(0)),
                new ItemStack(IUItem.iuingot.getItemFromMeta(5)),
                new ItemStack(IUItem.basecircuit.getItemFromMeta(2)),
                (short) 5000, true
        );


        add(
                new ItemStack(Items.FLINT),
                new ItemStack(Items.LAPIS_LAZULI),
                new ItemStack(IUItem.iuingot.getItemFromMeta(9)),
                "c:ingots/steel", new ItemStack(Items.GOLD_INGOT),

                new ItemStack(IUItem.basecircuit.getItemFromMeta(12)),
                true
        );
        add(
                new ItemStack(IUItem.basecircuit.getItemFromMeta(15)),
                new ItemStack(Items.IRON_INGOT, 2),
                new ItemStack(IUItem.iuingot.getItemFromMeta(4)),
                new ItemStack(IUItem.iuingot.getItemFromMeta(25)),
                new ItemStack(IUItem.iuingot.getItemFromMeta(3)),
                new ItemStack(IUItem.crafting_elements.getItemFromMeta(414)),
                (short) 1000, true
        );

        add(
                new ItemStack(IUItem.crafting_elements.getItemFromMeta(414)),
                new ItemStack(Items.GOLD_INGOT, 1),
                new ItemStack(IUItem.iuingot.getItemFromMeta(24)),
                new ItemStack(IUItem.iuingot.getItemFromMeta(26)),
                new ItemStack(IUItem.iuingot.getItemFromMeta(13)),
                new ItemStack(IUItem.crafting_elements.getItemFromMeta(426)),
                (short) 2000, true
        );

        add(
                new ItemStack(IUItem.crafting_elements.getItemFromMeta(426)),
                new ItemStack(IUItem.iuingot.getItemFromMeta(2)),
                new ItemStack(IUItem.iuingot.getItemFromMeta(0), 2),
                new ItemStack(IUItem.iuingot.getItemFromMeta(20), 3),
                new ItemStack(IUItem.crafting_elements.getItemFromMeta(274)),
                new ItemStack(IUItem.crafting_elements.getItemFromMeta(373)),
                (short) 3000, true
        );
        add(
                new ItemStack(IUItem.crafting_elements.getItemFromMeta(373)),
                new ItemStack(IUItem.iuingot.getItemFromMeta(12), 2),
                new ItemStack(IUItem.iuingot.getItemFromMeta(14), 2),
                new ItemStack(IUItem.iuingot.getItemFromMeta(16)),
                new ItemStack(IUItem.iuingot.getItemFromMeta(7)),
                new ItemStack(IUItem.crafting_elements.getItemFromMeta(402)),
                (short) 4000, true
        );


        add(
                new ItemStack(IUItem.crafting_elements.getItemFromMeta(488)),
                new ItemStack(IUItem.crafting_elements.getItemFromMeta(539)),
                new ItemStack(IUItem.crafting_elements.getItemFromMeta(538)),
                new ItemStack(IUItem.crafting_elements.getItemFromMeta(533)),
                new ItemStack(IUItem.basecircuit.getItemFromMeta(17)),
                getLevelCircuit(IUItem.electronicCircuit, 1),
                (short) 1000,
                true
        );
        add(
                new ItemStack(IUItem.crafting_elements.getItemFromMeta(488)),
                new ItemStack(IUItem.crafting_elements.getItemFromMeta(547)),
                new ItemStack(IUItem.crafting_elements.getItemFromMeta(538)),
                new ItemStack(IUItem.crafting_elements.getItemFromMeta(541)),
                new ItemStack(IUItem.basecircuit.getItemFromMeta(17)),
                getLevelCircuit(IUItem.electronicCircuit, 2),
                (short) 1000,
                true
        );

        add(
                new ItemStack(IUItem.crafting_elements.getItemFromMeta(491)),
                new ItemStack(IUItem.crafting_elements.getItemFromMeta(547)),
                new ItemStack(IUItem.crafting_elements.getItemFromMeta(557)),
                new ItemStack(IUItem.crafting_elements.getItemFromMeta(543)),
                new ItemStack(IUItem.basecircuit.getItemFromMeta(14)),
                getLevelCircuit(IUItem.advancedCircuit, 3),
                (short) 2000,
                true
        );
        add(
                new ItemStack(IUItem.crafting_elements.getItemFromMeta(491)),
                new ItemStack(IUItem.crafting_elements.getItemFromMeta(535)),
                new ItemStack(IUItem.crafting_elements.getItemFromMeta(557)),
                new ItemStack(IUItem.crafting_elements.getItemFromMeta(545)),
                new ItemStack(IUItem.basecircuit.getItemFromMeta(14)),
                getLevelCircuit(IUItem.advancedCircuit, 4),
                (short) 2000,
                true
        );

        add(
                new ItemStack(IUItem.crafting_elements.getItemFromMeta(486)),
                new ItemStack(IUItem.crafting_elements.getItemFromMeta(535)),
                new ItemStack(IUItem.crafting_elements.getItemFromMeta(557)),
                new ItemStack(IUItem.crafting_elements.getItemFromMeta(549)),
                new ItemStack(IUItem.basecircuit.getItemFromMeta(6)),
                getLevelCircuit(IUItem.circuitNano, 5),
                (short) 3000,
                true
        );

        add(
                new ItemStack(IUItem.crafting_elements.getItemFromMeta(486)),
                new ItemStack(IUItem.crafting_elements.getItemFromMeta(544)),
                new ItemStack(IUItem.crafting_elements.getItemFromMeta(552)),
                new ItemStack(IUItem.crafting_elements.getItemFromMeta(551)),
                new ItemStack(IUItem.basecircuit.getItemFromMeta(6)),
                getLevelCircuit(IUItem.circuitNano, 6),
                (short) 3000,
                true
        );

        add(
                new ItemStack(IUItem.crafting_elements.getItemFromMeta(485)),
                new ItemStack(IUItem.crafting_elements.getItemFromMeta(553)),
                new ItemStack(IUItem.crafting_elements.getItemFromMeta(542)),
                new ItemStack(IUItem.crafting_elements.getItemFromMeta(555)),
                new ItemStack(IUItem.basecircuit.getItemFromMeta(7)),
                getLevelCircuit(IUItem.cirsuitQuantum, 7),
                (short) 4000,
                true
        );
        add(
                new ItemStack(IUItem.crafting_elements.getItemFromMeta(485)),
                new ItemStack(IUItem.crafting_elements.getItemFromMeta(553)),
                new ItemStack(IUItem.crafting_elements.getItemFromMeta(540)),
                new ItemStack(IUItem.crafting_elements.getItemFromMeta(556)),
                new ItemStack(IUItem.basecircuit.getItemFromMeta(7)),
                getLevelCircuit(IUItem.cirsuitQuantum, 8),
                (short) 4000,
                true
        );

        add(
                new ItemStack(IUItem.crafting_elements.getItemFromMeta(490)),
                new ItemStack(IUItem.crafting_elements.getItemFromMeta(550)),
                new ItemStack(IUItem.crafting_elements.getItemFromMeta(548)),
                new ItemStack(IUItem.crafting_elements.getItemFromMeta(558)),
                new ItemStack(IUItem.basecircuit.getItemFromMeta(8)),
                getLevelCircuit(IUItem.circuitSpectral, 9),
                (short) 5000,
                true
        );

        add(
                new ItemStack(IUItem.crafting_elements.getItemFromMeta(490)),
                new ItemStack(IUItem.crafting_elements.getItemFromMeta(537)),
                new ItemStack(IUItem.crafting_elements.getItemFromMeta(536)),
                new ItemStack(IUItem.crafting_elements.getItemFromMeta(558)),
                new ItemStack(IUItem.basecircuit.getItemFromMeta(8)),
                getLevelCircuit(IUItem.circuitSpectral, 10),
                (short) 5000,
                true
        );

        add(
                new ItemStack(IUItem.crafting_elements.getItemFromMeta(490)),
                new ItemStack(IUItem.crafting_elements.getItemFromMeta(537)),
                new ItemStack(IUItem.crafting_elements.getItemFromMeta(536)),
                new ItemStack(IUItem.crafting_elements.getItemFromMeta(534)),
                new ItemStack(IUItem.basecircuit.getItemFromMeta(20)),
                getLevelCircuit(new ItemStack(IUItem.basecircuit.getItemFromMeta(21)), 11),
                (short) 5000,
                true
        );

        add(
                new ItemStack(IUItem.crafting_elements.getItemFromMeta(490)),
                new ItemStack(IUItem.crafting_elements.getItemFromMeta(546)),
                new ItemStack(IUItem.crafting_elements.getItemFromMeta(554)),
                new ItemStack(IUItem.crafting_elements.getItemFromMeta(534)),
                new ItemStack(IUItem.basecircuit.getItemFromMeta(20)),
                getLevelCircuit(new ItemStack(IUItem.basecircuit.getItemFromMeta(21)), 12),
                (short) 5000,
                true
        );
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

    public String getInventoryName() {

        return "Generation Microchip";
    }

    @OnlyIn(Dist.CLIENT)
    public GuiCore<ContainerBase<? extends IAdvInventory>> getGui(Player entityPlayer, ContainerBase<? extends IAdvInventory> menu) {
        return new GuiGenerationMicrochip((ContainerBaseGenerationChipMachine) menu);
    }


    public Set<UpgradableProperty> getUpgradableProperties() {
        return EnumSet.of(UpgradableProperty.Processing, UpgradableProperty.Transformer,
                UpgradableProperty.EnergyStorage, UpgradableProperty.ItemExtract, UpgradableProperty.ItemInput
        );
    }

}
