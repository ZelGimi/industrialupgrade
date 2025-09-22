package com.denfop.tiles.mechanism;

import com.denfop.IUItem;
import com.denfop.api.Recipes;
import com.denfop.api.gui.EnumTypeSlot;
import com.denfop.api.recipe.BaseMachineRecipe;
import com.denfop.api.recipe.IHasRecipe;
import com.denfop.api.recipe.IUpdateTick;
import com.denfop.api.recipe.Input;
import com.denfop.api.recipe.InventoryRecipes;
import com.denfop.api.recipe.MachineRecipe;
import com.denfop.api.recipe.RecipeOutput;
import com.denfop.api.tile.IMultiTileBlock;
import com.denfop.api.upgrades.UpgradableProperty;
import com.denfop.blocks.BlockTileEntity;
import com.denfop.blocks.mechanism.BlockBaseMachine;
import com.denfop.componets.AirPollutionComponent;
import com.denfop.componets.SoilPollutionComponent;
import com.denfop.container.ContainerBaseGenerationChipMachine;
import com.denfop.gui.GuiGenerationMicrochip;
import com.denfop.invslot.Inventory;
import com.denfop.items.resource.ItemIngots;
import com.denfop.recipe.IInputHandler;
import com.denfop.recipe.IInputItemStack;
import com.denfop.tiles.base.TileBaseGenerationMicrochip;
import com.denfop.utils.ModUtils;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import net.minecraftforge.oredict.OreDictionary;

import java.util.EnumSet;
import java.util.Set;

public class TileGenerationMicrochip extends TileBaseGenerationMicrochip implements IUpdateTick, IHasRecipe {


    public final Inventory input_slot;
    private final SoilPollutionComponent pollutionSoil;
    private final AirPollutionComponent pollutionAir;

    public TileGenerationMicrochip() {
        super(1, 300, 1);
        this.inputSlotA = new InventoryRecipes(this, "microchip", this);
        Recipes.recipes.addInitRecipes(this);
        this.componentProcess.setInvSlotRecipes(inputSlotA);
        this.input_slot = new Inventory(this, Inventory.TypeItemSlot.INPUT, 1) {
            @Override
            public void put(final int index, final ItemStack content) {
                super.put(index, content);
                if (this.get().isEmpty()) {
                    ((TileGenerationMicrochip) this.base).inputSlotA.changeAccepts(ItemStack.EMPTY);
                } else {
                    ((TileGenerationMicrochip) this.base).inputSlotA.changeAccepts(this.get());
                }
            }

            @Override
            public boolean isItemValidForSlot(final int index, final ItemStack stack) {
                return stack.getItem() == IUItem.recipe_schedule;
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

        NBTTagCompound nbt = new NBTTagCompound();
        nbt.setShort("temperature", (short) 4000);
        final IInputHandler input = com.denfop.api.Recipes.inputFactory;
        first1 = input.getInput("ingotAluminum");
        if (OreDictionary.getOreIDs(second).length > 0 && !OreDictionary
                .getOreName(OreDictionary.getOreIDs(second)[0])
                .isEmpty() && second.getItem() instanceof ItemIngots) {
            second1 = input.getInput(OreDictionary.getOreName(OreDictionary.getOreIDs(second)[0]));
        } else {
            second1 = input.getInput(second);
        }
        if (OreDictionary.getOreIDs(three).length > 0 && !OreDictionary
                .getOreName(OreDictionary.getOreIDs(three)[0])
                .isEmpty() && three.getItem() instanceof ItemIngots) {
            three1 = input.getInput(OreDictionary.getOreName(OreDictionary.getOreIDs(three)[0]));
        } else {
            three1 = input.getInput(three);
        }
        if (OreDictionary.getOreIDs(four).length > 0 && !OreDictionary
                .getOreName(OreDictionary.getOreIDs(four)[0])
                .isEmpty() && four.getItem() instanceof ItemIngots) {
            four1 = input.getInput(OreDictionary.getOreName(OreDictionary.getOreIDs(four)[0]));
        } else {
            four1 = input.getInput(four);
        }
        if (OreDictionary.getOreIDs(five).length > 0 && !OreDictionary
                .getOreName(OreDictionary.getOreIDs(five)[0])
                .isEmpty() && five.getItem() instanceof ItemIngots) {
            five1 = input.getInput(OreDictionary.getOreName(OreDictionary.getOreIDs(five)[0]));
        } else {
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

        NBTTagCompound nbt = new NBTTagCompound();
        nbt.setShort("temperature", temperatures);
        final IInputHandler input = com.denfop.api.Recipes.inputFactory;
        if (check) {
            if (OreDictionary.getOreIDs(first).length > 0 && !OreDictionary
                    .getOreName(OreDictionary.getOreIDs(first)[0])
                    .isEmpty() && first.getItem() instanceof ItemIngots) {
                first1 = input.getInput(OreDictionary.getOreName(OreDictionary.getOreIDs(first)[0]), first.getCount());
            } else {
                first1 = input.getInput(first);
            }
            if (OreDictionary.getOreIDs(second).length > 0 && !OreDictionary
                    .getOreName(OreDictionary.getOreIDs(second)[0])
                    .isEmpty() && second.getItem() instanceof ItemIngots) {
                second1 = input.getInput(OreDictionary.getOreName(OreDictionary.getOreIDs(second)[0]), second.getCount());
            } else {
                second1 = input.getInput(second);
            }
            if (OreDictionary.getOreIDs(three).length > 0 && !OreDictionary
                    .getOreName(OreDictionary.getOreIDs(three)[0])
                    .isEmpty() && three.getItem() instanceof ItemIngots) {
                three1 = input.getInput(OreDictionary.getOreName(OreDictionary.getOreIDs(three)[0]), three.getCount());
            } else {
                three1 = input.getInput(three);
            }
            if (OreDictionary.getOreIDs(four).length > 0 && !OreDictionary
                    .getOreName(OreDictionary.getOreIDs(four)[0])
                    .isEmpty() && four.getItem() instanceof ItemIngots) {
                four1 = input.getInput(OreDictionary.getOreName(OreDictionary.getOreIDs(four)[0]), four.getCount());
            } else {
                four1 = input.getInput(four);
            }
            if (OreDictionary.getOreIDs(five).length > 0 && !OreDictionary
                    .getOreName(OreDictionary.getOreIDs(five)[0])
                    .isEmpty() && five.getItem() instanceof ItemIngots) {
                five1 = input.getInput(OreDictionary.getOreName(OreDictionary.getOreIDs(five)[0]), five.getCount());
            } else {
                five1 = input.getInput(five);
            }
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

        NBTTagCompound nbt = new NBTTagCompound();
        nbt.setShort("temperature", temperatures);
        final IInputHandler input = com.denfop.api.Recipes.inputFactory;
        if (check) {
            if (OreDictionary.getOreIDs(first).length > 0 && !OreDictionary
                    .getOreName(OreDictionary.getOreIDs(first)[0])
                    .isEmpty() && first.getItem() instanceof ItemIngots) {

                first1 = input.getInput(OreDictionary.getOreName(OreDictionary.getOreIDs(first)[0]));
            } else {
                first1 = input.getInput(first);
            }
            if (OreDictionary.getOreIDs(second).length > 0 && !OreDictionary
                    .getOreName(OreDictionary.getOreIDs(second)[0])
                    .isEmpty() && second.getItem() instanceof ItemIngots) {
                second1 = input.getInput(OreDictionary.getOreName(OreDictionary.getOreIDs(second)[0]));
            } else {
                second1 = input.getInput(second);
            }
            if (OreDictionary.getOreIDs(three).length > 0 && !OreDictionary
                    .getOreName(OreDictionary.getOreIDs(three)[0])
                    .isEmpty() && three.getItem() instanceof ItemIngots) {
                three1 = input.getInput(OreDictionary.getOreName(OreDictionary.getOreIDs(three)[0]));
            } else {
                three1 = input.getInput(three);
            }
            if (OreDictionary.getOreIDs(four).length > 0 && !OreDictionary
                    .getOreName(OreDictionary.getOreIDs(four)[0])
                    .isEmpty() && four.getItem() instanceof ItemIngots) {
                four1 = input.getInput(OreDictionary.getOreName(OreDictionary.getOreIDs(four)[0]));
            } else {
                four1 = input.getInput(four);
            }
            five1 = input.getInput(five);
            Recipes.recipes.addRecipe(
                    "microchip",
                    new BaseMachineRecipe(
                            new Input(first1, second1, three1, four1, five1),
                            new RecipeOutput(nbt, output)
                    )
            );
        }
    }

    public static void add(
            String first,
            ItemStack second,
            ItemStack three,
            ItemStack four,
            ItemStack five,
            ItemStack output,
            boolean check
    ) {
        IInputItemStack first1;
        IInputItemStack second1;
        IInputItemStack three1;
        IInputItemStack four1;
        IInputItemStack five1;

        NBTTagCompound nbt = new NBTTagCompound();
        nbt.setShort("temperature", (short) 4500);
        final IInputHandler input = com.denfop.api.Recipes.inputFactory;
        if (check) {
            first1 = input.getInput(first);
            if (OreDictionary.getOreIDs(second).length > 0 && !OreDictionary
                    .getOreName(OreDictionary.getOreIDs(second)[0])
                    .isEmpty() && second.getItem() instanceof ItemIngots) {
                second1 = input.getInput(OreDictionary.getOreName(OreDictionary.getOreIDs(second)[0]));
            } else {
                second1 = input.getInput(second);
            }
            if (OreDictionary.getOreIDs(three).length > 0 && !OreDictionary
                    .getOreName(OreDictionary.getOreIDs(three)[0])
                    .isEmpty() && three.getItem() instanceof ItemIngots) {
                three1 = input.getInput(OreDictionary.getOreName(OreDictionary.getOreIDs(three)[0]));
            } else {
                three1 = input.getInput(three);
            }
            if (OreDictionary.getOreIDs(four).length > 0 && !OreDictionary
                    .getOreName(OreDictionary.getOreIDs(four)[0])
                    .isEmpty() && four.getItem() instanceof ItemIngots) {
                four1 = input.getInput(OreDictionary.getOreName(OreDictionary.getOreIDs(four)[0]));
            } else {
                four1 = input.getInput(four);
            }
            if (OreDictionary.getOreIDs(five).length > 0 && !OreDictionary
                    .getOreName(OreDictionary.getOreIDs(five)[0])
                    .isEmpty() && five.getItem() instanceof ItemIngots) {
                five1 = input.getInput(OreDictionary.getOreName(OreDictionary.getOreIDs(five)[0]));
            } else {
                five1 = input.getInput(five);
            }
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

        NBTTagCompound nbt = new NBTTagCompound();
        nbt.setShort("temperature", (short) 2000);
        final IInputHandler input = com.denfop.api.Recipes.inputFactory;
        if (check) {
            if (OreDictionary.getOreIDs(first).length > 0 && !OreDictionary
                    .getOreName(OreDictionary.getOreIDs(first)[0])
                    .isEmpty() && first.getItem() instanceof ItemIngots) {
                first1 = input.getInput(OreDictionary.getOreName(OreDictionary.getOreIDs(first)[0]));
            } else {
                first1 = input.getInput(first);
            }
            if (OreDictionary.getOreIDs(second).length > 0 && !OreDictionary
                    .getOreName(OreDictionary.getOreIDs(second)[0])
                    .isEmpty() && second.getItem() instanceof ItemIngots) {
                second1 = input.getInput(OreDictionary.getOreName(OreDictionary.getOreIDs(second)[0]));
            } else {
                second1 = input.getInput(second);
            }
            if (OreDictionary.getOreIDs(three).length > 0 && !OreDictionary
                    .getOreName(OreDictionary.getOreIDs(three)[0])
                    .isEmpty() && three.getItem() instanceof ItemIngots) {
                three1 = input.getInput(OreDictionary.getOreName(OreDictionary.getOreIDs(three)[0]));
            } else {
                three1 = input.getInput(three);
            }
            four1 = input.getInput(four);
            if (OreDictionary.getOreIDs(five).length > 0 && !OreDictionary
                    .getOreName(OreDictionary.getOreIDs(five)[0])
                    .isEmpty() && five.getItem() instanceof ItemIngots) {
                five1 = input.getInput(OreDictionary.getOreName(OreDictionary.getOreIDs(five)[0]));
            } else {
                five1 = input.getInput(five);
            }
            Recipes.recipes.addRecipe(
                    "microchip",
                    new BaseMachineRecipe(
                            new Input(first1, second1, three1, four1, five1),
                            new RecipeOutput(nbt, output)
                    )
            );
        } else {
            Recipes.recipes.addRecipe(
                    "microchip",
                    new BaseMachineRecipe(
                            new Input(
                                    input.getInput(first),
                                    input.getInput(second),
                                    input.getInput(three),
                                    input.getInput(four),
                                    input.getInput(five)
                            ),
                            new RecipeOutput(nbt, output)
                    )
            );


        }
    }

    public static ItemStack getLevelCircuit(ItemStack stack, int level) {
        stack = stack.copy();
        final NBTTagCompound nbt = ModUtils.nbt(stack);
        nbt.setInteger("level", level);
        return stack;
    }

    @Override
    public void onLoaded() {
        super.onLoaded();
        if (!this.getWorld().isRemote) {
            if (this.input_slot.isEmpty()) {
                (this).inputSlotA.changeAccepts(ItemStack.EMPTY);
            } else {
                (this).inputSlotA.changeAccepts(this.input_slot.get());
            }
        }
    }

    public ContainerBaseGenerationChipMachine getGuiContainer(EntityPlayer entityPlayer) {
        return new ContainerBaseGenerationChipMachine(
                entityPlayer, this);
    }

    public IMultiTileBlock getTeBlock() {
        return BlockBaseMachine.generator_microchip;
    }

    public BlockTileEntity getBlock() {
        return IUItem.machines;
    }

    public void init() {
        add(new ItemStack(Items.FLINT), new ItemStack(Items.DYE, 1, 4), new ItemStack(Items.IRON_INGOT),
                new ItemStack(IUItem.iuingot, 1
                        , 11), new ItemStack(IUItem.iuingot, 1, 15), new ItemStack(IUItem.basecircuit), (short) 3000, false
        );

        add(
                new ItemStack(IUItem.iuingot, 1, 39),
                new ItemStack(IUItem.iuingot, 1, 30),
                new ItemStack(IUItem.iuingot, 1, 41),
                new ItemStack(IUItem.alloysingot, 1, 14),
                new ItemStack(IUItem.alloysingot, 1, 23),
                new ItemStack(IUItem.basecircuit, 1, 18),
                (short) 5000, true
        );

        add(
                new ItemStack(Items.IRON_INGOT),
                new ItemStack(Items.REDSTONE),
                new ItemStack(Items.GOLD_INGOT),
                new ItemStack(Items.FLINT),
                new ItemStack(IUItem.iuingot, 1, 14),
                new ItemStack(IUItem.basecircuit, 1, 15),
                (short) 1000, true
        );

        add(
                new ItemStack(IUItem.iuingot, 1, 1),
                new ItemStack(Items.REDSTONE, 1),
                new ItemStack(Items.GOLD_INGOT),
                new ItemStack(IUItem.iuingot, 1, 7),
                "ingotCopper",
                new ItemStack(IUItem.basecircuit, 1, 1),
                (short) 4000, true
        );
        add(
                new ItemStack(Items.REDSTONE, 1),
                new ItemStack(Items.GOLD_INGOT),
                new ItemStack(IUItem.iuingot, 1, 7),
                IUItem.copperIngot,
                new ItemStack(IUItem.basecircuit, 1, 1)
        );
        add(
                new ItemStack(IUItem.iuingot, 1, 18),
                new ItemStack(Items.REDSTONE, 1),
                new ItemStack(Items.DIAMOND),
                new ItemStack(IUItem.iuingot, 1, 0),
                new ItemStack(IUItem.iuingot, 1, 5),
                new ItemStack(IUItem.basecircuit, 1, 2),
                (short) 5000, true
        );
        add(
                new ItemStack(IUItem.iuingot, 1, 18),
                new ItemStack(Items.REDSTONE, 1),
                new ItemStack(Items.EMERALD),
                new ItemStack(IUItem.iuingot, 1, 0),
                new ItemStack(IUItem.iuingot, 1, 5),
                new ItemStack(IUItem.basecircuit, 1, 2),
                (short) 5000, true
        );


        add(
                new ItemStack(Items.FLINT),
                new ItemStack(Items.DYE, 1, 4),
                new ItemStack(IUItem.iuingot, 1, 9),
                "ingotSteel", new ItemStack(Items.GOLD_INGOT),

                new ItemStack(IUItem.basecircuit, 1, 12),
                true
        );
        add(
                new ItemStack(IUItem.basecircuit, 1, 15),
                new ItemStack(Items.IRON_INGOT, 2),
                new ItemStack(IUItem.iuingot, 1, 4),
                new ItemStack(IUItem.iuingot, 1, 25),
                new ItemStack(IUItem.iuingot, 1, 3),
                new ItemStack(IUItem.crafting_elements, 1, 414),
                (short) 1000, true
        );

        add(
                new ItemStack(IUItem.crafting_elements, 1, 414),
                new ItemStack(Items.GOLD_INGOT, 1),
                new ItemStack(IUItem.iuingot, 1, 24),
                new ItemStack(IUItem.iuingot, 1, 26),
                new ItemStack(IUItem.iuingot, 1, 13),
                new ItemStack(IUItem.crafting_elements, 1, 426),
                (short) 2000, true
        );

        add(
                new ItemStack(IUItem.crafting_elements, 1, 426),
                new ItemStack(IUItem.iuingot, 1, 2),
                new ItemStack(IUItem.iuingot, 2, 0),
                new ItemStack(IUItem.iuingot, 3, 20),
                new ItemStack(IUItem.crafting_elements, 1, 274),
                new ItemStack(IUItem.crafting_elements, 1, 373),
                (short) 3000, true
        );
        add(
                new ItemStack(IUItem.crafting_elements, 1, 373),
                new ItemStack(IUItem.iuingot, 2, 12),
                new ItemStack(IUItem.iuingot, 2, 14),
                new ItemStack(IUItem.iuingot, 1, 16),
                new ItemStack(IUItem.iuingot, 1, 7),
                new ItemStack(IUItem.crafting_elements, 1, 402),
                (short) 4000, true
        );


        add(
                new ItemStack(IUItem.crafting_elements, 1, 488),
                new ItemStack(IUItem.crafting_elements, 1, 539),
                new ItemStack(IUItem.crafting_elements, 1, 538),
                new ItemStack(IUItem.crafting_elements, 1, 533),
                new ItemStack(IUItem.basecircuit, 1, 17),
                getLevelCircuit(IUItem.electronicCircuit, 1),
                (short) 1000,
                true
        );
        add(
                new ItemStack(IUItem.crafting_elements, 1, 488),
                new ItemStack(IUItem.crafting_elements, 1, 547),
                new ItemStack(IUItem.crafting_elements, 1, 538),
                new ItemStack(IUItem.crafting_elements, 1, 541),
                new ItemStack(IUItem.basecircuit, 1, 17),
                getLevelCircuit(IUItem.electronicCircuit, 2),
                (short) 1000,
                true
        );

        add(
                new ItemStack(IUItem.crafting_elements, 1, 491),
                new ItemStack(IUItem.crafting_elements, 1, 547),
                new ItemStack(IUItem.crafting_elements, 1, 557),
                new ItemStack(IUItem.crafting_elements, 1, 543),
                new ItemStack(IUItem.basecircuit, 1, 14),
                getLevelCircuit(IUItem.advancedCircuit, 3),
                (short) 2000,
                true
        );
        add(
                new ItemStack(IUItem.crafting_elements, 1, 491),
                new ItemStack(IUItem.crafting_elements, 1, 535),
                new ItemStack(IUItem.crafting_elements, 1, 557),
                new ItemStack(IUItem.crafting_elements, 1, 545),
                new ItemStack(IUItem.basecircuit, 1, 14),
                getLevelCircuit(IUItem.advancedCircuit, 4),
                (short) 2000,
                true
        );

        add(
                new ItemStack(IUItem.crafting_elements, 1, 486),
                new ItemStack(IUItem.crafting_elements, 1, 535),
                new ItemStack(IUItem.crafting_elements, 1, 557),
                new ItemStack(IUItem.crafting_elements, 1, 549),
                new ItemStack(IUItem.basecircuit, 1, 6),
                getLevelCircuit(IUItem.circuitNano, 5),
                (short) 3000,
                true
        );

        add(
                new ItemStack(IUItem.crafting_elements, 1, 486),
                new ItemStack(IUItem.crafting_elements, 1, 544),
                new ItemStack(IUItem.crafting_elements, 1, 552),
                new ItemStack(IUItem.crafting_elements, 1, 551),
                new ItemStack(IUItem.basecircuit, 1, 6),
                getLevelCircuit(IUItem.circuitNano, 6),
                (short) 3000,
                true
        );

        add(
                new ItemStack(IUItem.crafting_elements, 1, 485),
                new ItemStack(IUItem.crafting_elements, 1, 553),
                new ItemStack(IUItem.crafting_elements, 1, 542),
                new ItemStack(IUItem.crafting_elements, 1, 555),
                new ItemStack(IUItem.basecircuit, 1, 7),
                getLevelCircuit(IUItem.cirsuitQuantum, 7),
                (short) 4000,
                true
        );
        add(
                new ItemStack(IUItem.crafting_elements, 1, 485),
                new ItemStack(IUItem.crafting_elements, 1, 553),
                new ItemStack(IUItem.crafting_elements, 1, 540),
                new ItemStack(IUItem.crafting_elements, 1, 556),
                new ItemStack(IUItem.basecircuit, 1, 7),
                getLevelCircuit(IUItem.cirsuitQuantum, 8),
                (short) 4000,
                true
        );

        add(
                new ItemStack(IUItem.crafting_elements, 1, 490),
                new ItemStack(IUItem.crafting_elements, 1, 550),
                new ItemStack(IUItem.crafting_elements, 1, 548),
                new ItemStack(IUItem.crafting_elements, 1, 558),
                new ItemStack(IUItem.basecircuit, 1, 8),
                getLevelCircuit(IUItem.circuitSpectral, 9),
                (short) 5000,
                true
        );

        add(
                new ItemStack(IUItem.crafting_elements, 1, 490),
                new ItemStack(IUItem.crafting_elements, 1, 537),
                new ItemStack(IUItem.crafting_elements, 1, 536),
                new ItemStack(IUItem.crafting_elements, 1, 558),
                new ItemStack(IUItem.basecircuit, 1, 8),
                getLevelCircuit(IUItem.circuitSpectral, 10),
                (short) 5000,
                true
        );

        add(
                new ItemStack(IUItem.crafting_elements, 1, 490),
                new ItemStack(IUItem.crafting_elements, 1, 537),
                new ItemStack(IUItem.crafting_elements, 1, 536),
                new ItemStack(IUItem.crafting_elements, 1, 534),
                new ItemStack(IUItem.basecircuit, 1, 20),
                getLevelCircuit(new ItemStack(IUItem.basecircuit, 1, 21), 11),
                (short) 5000,
                true
        );

        add(
                new ItemStack(IUItem.crafting_elements, 1, 490),
                new ItemStack(IUItem.crafting_elements, 1, 546),
                new ItemStack(IUItem.crafting_elements, 1, 554),
                new ItemStack(IUItem.crafting_elements, 1, 534),
                new ItemStack(IUItem.basecircuit, 1, 20),
                getLevelCircuit(new ItemStack(IUItem.basecircuit, 1, 21), 12),
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

    @SideOnly(Side.CLIENT)
    public GuiScreen getGui(EntityPlayer entityPlayer, boolean isAdmin) {
        return new GuiGenerationMicrochip(new ContainerBaseGenerationChipMachine(entityPlayer, this));
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
