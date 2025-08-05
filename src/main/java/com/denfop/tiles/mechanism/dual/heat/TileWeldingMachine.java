package com.denfop.tiles.mechanism.dual.heat;

import com.denfop.IUItem;
import com.denfop.api.Recipes;
import com.denfop.api.gui.EnumTypeSlot;
import com.denfop.api.inv.IAdvInventory;
import com.denfop.api.recipe.BaseMachineRecipe;
import com.denfop.api.recipe.IHasRecipe;
import com.denfop.api.recipe.Input;
import com.denfop.api.recipe.RecipeOutput;
import com.denfop.api.tile.IMultiTileBlock;
import com.denfop.audio.EnumSound;
import com.denfop.blocks.BlockTileEntity;
import com.denfop.blocks.mechanism.BlockBaseMachine3;
import com.denfop.componets.AirPollutionComponent;
import com.denfop.componets.SoilPollutionComponent;
import com.denfop.container.ContainerBase;
import com.denfop.container.ContainerDoubleElectricMachine;
import com.denfop.gui.GuiCore;
import com.denfop.gui.GuiWelding;
import com.denfop.invslot.InvSlot;
import com.denfop.recipe.IInputHandler;
import com.denfop.tiles.base.EnumDoubleElectricMachine;
import com.denfop.tiles.base.TileDoubleElectricMachine;
import com.denfop.utils.ModUtils;
import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

public class TileWeldingMachine extends TileDoubleElectricMachine implements IHasRecipe {


    public final InvSlot input_slot;
    private final SoilPollutionComponent pollutionSoil;
    private final AirPollutionComponent pollutionAir;

    public TileWeldingMachine(BlockPos pos, BlockState state) {
        super(1, 140, 1, EnumDoubleElectricMachine.WELDING,BlockBaseMachine3.welding,pos,state);
        Recipes.recipes.addInitRecipes(this);
        this.pollutionSoil = this.addComponent(new SoilPollutionComponent(this, 0.1));
        this.pollutionAir = this.addComponent(new AirPollutionComponent(this, 0.2));
        this.input_slot = new InvSlot(this, InvSlot.TypeItemSlot.INPUT, 1) {
            @Override
            public ItemStack set(final int index, final ItemStack content) {
                super.set(index, content);
                if (this.get(0).isEmpty()) {
                    ((TileWeldingMachine) this.base).inputSlotA.changeAccepts(ItemStack.EMPTY);
                } else {
                    ((TileWeldingMachine) this.base).inputSlotA.changeAccepts(this.get(0));
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
    }

    public static void addRecipe(ItemStack fill, ItemStack output, int temperature) {
        final IInputHandler input = com.denfop.api.Recipes.inputFactory;

        final CompoundTag nbt = ModUtils.nbt();
        nbt.putShort("temperature", (short) temperature);
        Recipes.recipes.addRecipe("welding", new BaseMachineRecipe(
                new Input(input.getInput(new ItemStack(IUItem.crafting_elements.getStack(122), 1)), input.getInput(fill)),
                new RecipeOutput(nbt, output)
        ));
    }

    public static void addRecipe(ItemStack container, ItemStack fill, ItemStack output, int temperature) {
        final IInputHandler input = com.denfop.api.Recipes.inputFactory;

        final CompoundTag nbt = ModUtils.nbt();
        nbt.putShort("temperature", (short) temperature);
        Recipes.recipes.addRecipe("welding", new BaseMachineRecipe(
                new Input(input.getInput(container), input.getInput(fill)),
                new RecipeOutput(nbt, output)
        ));
    }

    public static void addRecipe(String fill, ItemStack output, int temperature) {
        final IInputHandler input = com.denfop.api.Recipes.inputFactory;

        final CompoundTag nbt = ModUtils.nbt();
        nbt.putShort("temperature", (short) temperature);
        Recipes.recipes.addRecipe("welding", new BaseMachineRecipe(
                new Input(input.getInput(new ItemStack(IUItem.crafting_elements.getStack(122), 1)), input.getInput(fill)),
                new RecipeOutput(nbt, output)
        ));
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

    public IMultiTileBlock getTeBlock() {
        return BlockBaseMachine3.welding;
    }

    public BlockTileEntity getBlock() {
        return IUItem.basemachine2.getBlock(getTeBlock());
    }

    @Override
    public SoundEvent getSound() {
        return EnumSound.welding.getSoundEvent();
    }

    public void init() {
        addRecipe(
                IUItem.crafting_elements.getItemStack(122, 1),
                IUItem.itemiu.getItemStack(2, 2),
                IUItem.radcable_item.getItemStack(0, 1),
                2000
        );

        addRecipe("forge:plates/Lead", IUItem.coolpipes.getItemStack(0, 4), 1000);
        addRecipe("forge:plateDense/Iron", IUItem.coolpipes.getItemStack(1, 4), 2000);
        addRecipe("forge:plateDense/Steel", IUItem.coolpipes.getItemStack(2, 4), 3000);
        addRecipe("forge:doubleplate/AluminiumSilicon", IUItem.coolpipes.getItemStack(3, 4), 4000);
        addRecipe("forge:doubleplate/Woods", IUItem.coolpipes.getItemStack(4, 4), 5000);

        addRecipe("forge:plates/Aluminium", IUItem.pipes.getItemStack(0, 4), 1000);
        addRecipe("forge:doubleplate/Aluminium", IUItem.pipes.getItemStack(1, 4), 2000);
        addRecipe("forge:plates/Duralumin", IUItem.pipes.getItemStack(2, 4), 3000);
        addRecipe("forge:doubleplate/Alcled", IUItem.pipes.getItemStack(3, 4), 4000);
        addRecipe("forge:doubleplate/Permalloy", IUItem.pipes.getItemStack(4, 4), 5000);

        for (int i = 0; i < 5; i++) {
            addRecipe(
                    IUItem.coolpipes.getItemStack(i),
                    IUItem.pipes.getItemStack(i),
                    IUItem.heatcold_pipes.getItemStack(i),
                    1000 + 1000 * i
            );
        }

        addRecipe(IUItem.crafting_elements.getItemStack(437),
                IUItem.sunnarium.getItemStack(2, 4),
                IUItem.crafting_elements.getItemStack(421), 1000);

        addRecipe(IUItem.crafting_elements.getItemStack(437),
                IUItem.sunnariumpanel.getItemStack(0, 4),
                IUItem.crafting_elements.getItemStack(311), 1000);

        addRecipe(IUItem.crafting_elements.getItemStack(437),
                IUItem.sunnariumpanel.getItemStack(1, 4),
                IUItem.crafting_elements.getItemStack(399), 1000);

        addRecipe(IUItem.crafting_elements.getItemStack(437),
                IUItem.sunnariumpanel.getItemStack(2, 4),
                IUItem.crafting_elements.getItemStack(346), 1000);

        addRecipe(IUItem.crafting_elements.getItemStack(437),
                IUItem.sunnariumpanel.getItemStack(3, 4),
                IUItem.crafting_elements.getItemStack(407), 2000);

        addRecipe(IUItem.crafting_elements.getItemStack(437),
                IUItem.sunnariumpanel.getItemStack(4, 4),
                IUItem.crafting_elements.getItemStack(382), 2000);

        addRecipe(IUItem.crafting_elements.getItemStack(437),
                IUItem.sunnariumpanel.getItemStack(5, 4),
                IUItem.crafting_elements.getItemStack(389), 2000);

        addRecipe(IUItem.crafting_elements.getItemStack(437),
                IUItem.sunnariumpanel.getItemStack(6, 4),
                IUItem.crafting_elements.getItemStack(330), 2000);

        addRecipe(IUItem.crafting_elements.getItemStack(437),
                IUItem.sunnariumpanel.getItemStack(7, 4),
                IUItem.crafting_elements.getItemStack(430), 3000);

        addRecipe(IUItem.crafting_elements.getItemStack(437),
                IUItem.sunnariumpanel.getItemStack(8, 4),
                IUItem.crafting_elements.getItemStack(359), 3000);

        addRecipe(IUItem.crafting_elements.getItemStack(437),
                IUItem.sunnariumpanel.getItemStack(9, 4),
                IUItem.crafting_elements.getItemStack(307), 3000);

        addRecipe(IUItem.crafting_elements.getItemStack(437),
                IUItem.sunnariumpanel.getItemStack(10, 4),
                IUItem.crafting_elements.getItemStack(302), 3000);

        addRecipe(IUItem.crafting_elements.getItemStack(437),
                IUItem.sunnariumpanel.getItemStack(11, 4),
                IUItem.crafting_elements.getItemStack(316), 4000);

        addRecipe(IUItem.crafting_elements.getItemStack(437),
                IUItem.sunnariumpanel.getItemStack(12, 4),
                IUItem.crafting_elements.getItemStack(350), 4000);

        addRecipe("forge:rods/Titanium", "forge:plates/Iron",
                IUItem.crafting_elements.getItemStack(338), 2000);

        addRecipe("forge:rods/Germanium", "forge:plates/Steel",
                IUItem.crafting_elements.getItemStack(411), 3000);

        addRecipe("forge:rods/Iridium", "forge:plates/Iridium",
                IUItem.crafting_elements.getItemStack(343), 4000);

        addRecipe(IUItem.sunnarium.getItemStack(4),
                IUItem.sunnarium.getItemStack(3),
                IUItem.crafting_elements.getItemStack(416), 1000);

        addRecipe("forge:nuggets/Platinum", "forge:ingots/Platinum",
                IUItem.crafting_elements.getItemStack(314), 1000);

        addRecipe("forge:nuggets/Mikhail", "forge:ingots/Mikhail",
                IUItem.crafting_elements.getItemStack(401), 1000);

        addRecipe("forge:nuggets/Chromium", "forge:ingots/Chromium",
                IUItem.crafting_elements.getItemStack(345), 1000);

        addRecipe("forge:nuggets/Electrum", "forge:ingots/Electrum",
                IUItem.crafting_elements.getItemStack(406), 2000);

        addRecipe("forge:nuggets/Magnesium", "forge:ingots/Magnesium",
                IUItem.crafting_elements.getItemStack(381), 2000);

        addRecipe("forge:nuggets/Zinc", "forge:ingots/Zinc",
                IUItem.crafting_elements.getItemStack(391), 2000);

        addRecipe("forge:nuggets/Manganese", "forge:ingots/Manganese",
                IUItem.crafting_elements.getItemStack(329), 2000);

        addRecipe("forge:nuggets/Cobalt", "forge:ingots/Cobalt",
                IUItem.crafting_elements.getItemStack(429), 3000);

        addRecipe("forge:nuggets/Nickel", "forge:ingots/Nickel",
                IUItem.crafting_elements.getItemStack(358), 3000);

        addRecipe("forge:nuggets/Silver", "forge:ingots/Silver",
                IUItem.crafting_elements.getItemStack(306), 3000);

        addRecipe("forge:nuggets/Vanady", "forge:ingots/Vanady",
                IUItem.crafting_elements.getItemStack(301), 3000);

        addRecipe("forge:nuggets/Vitalium", "forge:ingots/Vitalium",
                IUItem.crafting_elements.getItemStack(315), 4000);

        addRecipe("forge:nuggets/Caravky", "forge:ingots/Caravky",
                IUItem.crafting_elements.getItemStack(349), 4000);

        addRecipe("forge:plateDense/Steel", "forge:rods/Tungsten",
                IUItem.crafting_elements.getItemStack(413), 2000);

        addRecipe("forge:plateDense/Steel", IUItem.advancedAlloy,
                IUItem.crafting_elements.getItemStack(370), 2000);

        addRecipe("forge:plateDense/Steel", "forge:gears/Tungsten",
                IUItem.crafting_elements.getItemStack(412), 2000);

        addRecipe("forge:plateDense/Steel", IUItem.crafting_elements.getItemStack(137),
                IUItem.crafting_elements.getItemStack(438), 2000);

        addRecipe("forge:plateDense/Steel", "forge:gems/Ruby",
                IUItem.crafting_elements.getItemStack(369), 2000);

    }

    private void addRecipe(String container, String fill, ItemStack output, int temperature) {
        final IInputHandler input = com.denfop.api.Recipes.inputFactory;

        final CompoundTag nbt = ModUtils.nbt();
        nbt.putShort("temperature", (short) temperature);
        Recipes.recipes.addRecipe("welding", new BaseMachineRecipe(
                new Input(input.getInput(container), input.getInput(fill)),
                new RecipeOutput(nbt, output)
        ));
    }

    private void addRecipe(String container, ItemStack fill, ItemStack output, int temperature) {
        final IInputHandler input = com.denfop.api.Recipes.inputFactory;

        final CompoundTag nbt = ModUtils.nbt();
        nbt.putShort("temperature", (short) temperature);
        Recipes.recipes.addRecipe("welding", new BaseMachineRecipe(
                new Input(input.getInput(container), input.getInput(fill)),
                new RecipeOutput(nbt, output)
        ));
    }

    @Override
    @OnlyIn(Dist.CLIENT)
    public GuiCore<ContainerBase<? extends IAdvInventory>> getGui(Player var1, ContainerBase<? extends IAdvInventory> menu) {
        return new GuiWelding((ContainerDoubleElectricMachine) menu);
    }


    public String getStartSoundFile() {
        return "Machines/welding.ogg";
    }


    public String getInterruptSoundFile() {
        return "Machines/InterruptOne.ogg";
    }

    public float getWrenchDropRate() {
        return 0.85F;
    }


}
