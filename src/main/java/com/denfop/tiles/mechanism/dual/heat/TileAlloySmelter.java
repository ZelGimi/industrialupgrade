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
import com.denfop.blocks.mechanism.BlockBaseMachine;
import com.denfop.componets.AirPollutionComponent;
import com.denfop.componets.SoilPollutionComponent;
import com.denfop.container.ContainerBase;
import com.denfop.container.ContainerDoubleElectricMachine;
import com.denfop.gui.GuiAlloySmelter;
import com.denfop.gui.GuiCore;
import com.denfop.invslot.InvSlot;
import com.denfop.recipe.IInputHandler;
import com.denfop.recipe.IInputItemStack;
import com.denfop.tiles.base.EnumDoubleElectricMachine;
import com.denfop.tiles.base.TileDoubleElectricMachine;
import com.denfop.utils.ModUtils;
import com.denfop.utils.ParticleUtils;
import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

public class TileAlloySmelter extends TileDoubleElectricMachine implements IHasRecipe {


    public final InvSlot input_slot;
    private final SoilPollutionComponent pollutionSoil;
    private final AirPollutionComponent pollutionAir;

    public TileAlloySmelter(BlockPos pos, BlockState state) {
        super(1, 300, 1, EnumDoubleElectricMachine.ALLOY_SMELTER, BlockBaseMachine.alloy_smelter, pos, state);
        Recipes.recipes.addInitRecipes(this);
        this.input_slot = new InvSlot(this, InvSlot.TypeItemSlot.INPUT, 1) {
            @Override
            public ItemStack set(final int index, final ItemStack content) {
                super.set(index, content);
                if (this.get(0).isEmpty()) {
                    ((TileAlloySmelter) this.base).inputSlotA.changeAccepts(ItemStack.EMPTY);
                } else {
                    ((TileAlloySmelter) this.base).inputSlotA.changeAccepts(this.get(0));
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
        this.pollutionSoil = this.addComponent(new SoilPollutionComponent(this, 0.1));
        this.pollutionAir = this.addComponent(new AirPollutionComponent(this, 0.2));
    }

    public static void addAlloysmelter(IInputItemStack container, IInputItemStack fill, ItemStack output, int temperature) {
        final CompoundTag nbt = ModUtils.nbt();
        nbt.putShort("temperature", (short) temperature);
        Recipes.recipes.addRecipe("alloysmelter", new BaseMachineRecipe(
                new Input(container, fill),
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
        return BlockBaseMachine.alloy_smelter;
    }

    public BlockTileEntity getBlock() {
        return IUItem.machines.getBlock(getTeBlock().getId());
    }
    @Override
    public void updateEntityServer() {
        super.updateEntityServer();
        if (this.getActive()  && this.level.getGameTime() % 5 == 0){
            ParticleUtils.spawnAlloySmelterParticles(level,pos,level.random);
        }
    }
    public void init() {

        final IInputHandler input = com.denfop.api.Recipes.inputFactory;
        addAlloysmelter(
                input.getInput(new ItemStack(Items.IRON_INGOT), 1),
                input.getInput(new ItemStack(Items.COAL), 2),
                new ItemStack(IUItem.crafting_elements.getItemFromMeta(502), 1), 4000
        );
        addAlloysmelter(
                input.getInput("forge:gems/bor", 1),
                input.getInput(new ItemStack(Items.NETHER_STAR), 1),
                new ItemStack(IUItem.nether_star_ingot.getItem()), 2000
        );
        addAlloysmelter(
                input.getInput("forge:ingots/tungsten", 2),
                input.getInput("forge:ingots/nickel", 1),
                new ItemStack(IUItem.wolframite.getItem()), 3000
        );

        addAlloysmelter(
                input.getInput(new ItemStack(IUItem.alloysingot.getItemFromMeta(13), 1)),
                input.getInput(new ItemStack(IUItem.iuingot.getItemFromMeta(3), 2)),
                new ItemStack(IUItem.crafting_elements.getItemFromMeta(480), 2), 4000
        );
        addAlloysmelter(
                input.getInput(new ItemStack(IUItem.iudust.getItemFromMeta(64), 1)),
                input.getInput(new ItemStack(IUItem.iudust.getItemFromMeta(28), 1)),
                new ItemStack(IUItem.iudust.getItemFromMeta(73), 1), 1000
        );
        addAlloysmelter(
                input.getInput(new ItemStack(IUItem.iuingot.getItemFromMeta(28), 2)),
                input.getInput(new ItemStack(IUItem.iuingot.getItemFromMeta(32), 1)),
                new ItemStack(IUItem.alloysingot.getItemFromMeta(31), 1), 4000
        );
        addAlloysmelter(
                input.getInput(new ItemStack(IUItem.iudust.getItemFromMeta(1), 2)),
                input.getInput(new ItemStack(IUItem.iudust.getItemFromMeta(31), 3)),
                new ItemStack(IUItem.iudust.getItemFromMeta(59), 1), 4000
        );
        addAlloysmelter(
                input.getInput(new ItemStack(Items.GOLD_INGOT), 1),
                input.getInput("forge:ingots/silver", 1),
                new ItemStack(IUItem.iuingot.getItemFromMeta(13), 1), 3500
        );
        addAlloysmelter(
                input.getInput(new ItemStack(IUItem.crafting_elements.getItemFromMeta(481), 1)),
                input.getInput("forge:dusts/coal", 2),
                new ItemStack(IUItem.crafting_elements.getItemFromMeta(482), 1), 1000
        );
        addAlloysmelter(
                input.getInput("forge:ingots/nickel", 1),
                input.getInput(new ItemStack(Items.IRON_INGOT), 2),
                input.getInput("forge:ingots/invar",4).getInputs().get(0), 5000
        );

        addAlloysmelter(
                input.getInput(new ItemStack(IUItem.iudust.getItemFromMeta(37), 1)),
                input.getInput("forge:dusts/iron", 2),
                new ItemStack(IUItem.iudust.getItemFromMeta(38), 1), 2000
        );
        addAlloysmelter(
                input.getInput(new ItemStack(IUItem.iudust.getItemFromMeta(71), 1)),
                input.getInput(new ItemStack(IUItem.iudust.getItemFromMeta(60), 1)),
                new ItemStack(IUItem.iudust.getItemFromMeta(33), 1), 2000
        );

        addAlloysmelter(
                input.getInput(new ItemStack(Items.COAL), 1),
                input.getInput(new ItemStack(Items.QUARTZ), 4),
                new ItemStack(IUItem.crafting_elements.getItemFromMeta(319), 1), 2000
        );
        addAlloysmelter(
                input.getInput("forge:storage_blocks/silver", 1),
                input.getInput(new ItemStack(IUItem.crafting_elements.getItemFromMeta(484), 1), 1),
                new ItemStack(IUItem.crafting_elements.getItemFromMeta(434), 1), 2000
        );
        addAlloysmelter(
                input.getInput("forge:ingots/copper", 1),
                input.getInput("forge:ingots/zinc", 1),
                new ItemStack(IUItem.alloysingot.getItemFromMeta(2), 1), 3000
        );
        addAlloysmelter(
                input.getInput("forge:ingots/nickel", 1),
                input.getInput("forge:ingots/chromium", 1),
                new ItemStack(IUItem.alloysingot.getItemFromMeta(4), 1), 4000
        );
        addAlloysmelter(
                input.getInput("forge:ingots/nickel", 1),
                input.getInput("forge:ingots/titanium", 1),
                new ItemStack(IUItem.alloysingot.getItemFromMeta(15), 1), 4000
        );
        addAlloysmelter(
                input.getInput("forge:ingots/tin", 1),
                input.getInput("forge:ingots/copper", 3),
                ModUtils.setSize(IUItem.bronzeIngot, 4), 1000
        );
        addAlloysmelter(
                input.getInput("forge:ingots/aluminium", 1),
                input.getInput("forge:ingots/magnesium", 1),
                new ItemStack(IUItem.alloysingot.getItemFromMeta(8), 1), 2000
        );

        addAlloysmelter(
                input.getInput("forge:ingots/aluminium", 1),
                input.getInput("forge:ingots/titanium", 1),
                new ItemStack(IUItem.alloysingot.getItemFromMeta(1), 1), 5000
        );

        addAlloysmelter(
                input.getInput("forge:ingots/aluminium", 1),
                input.getInput("forge:ingots/lithium", 1),
                new ItemStack(IUItem.alloysingot.getItemFromMeta(22), 1), 2000
        );
        addAlloysmelter(
                input.getInput("forge:ingots/chromium", 1),
                input.getInput("forge:ingots/cobalt", 1),
                new ItemStack(IUItem.alloysingot.getItemFromMeta(23), 1), 2000
        );
        addAlloysmelter(
                input.getInput("forge:ingots/niobium", 2),
                input.getInput("forge:ingots/titanium", 1),
                new ItemStack(IUItem.alloysingot.getItemFromMeta(26), 1), 3000
        );
        addAlloysmelter(
                input.getInput("forge:ingots/osmium", 2),
                input.getInput("forge:ingots/iridium", 1),
                new ItemStack(IUItem.alloysingot.getItemFromMeta(27), 1), 3000
        );
        addAlloysmelter(
                input.getInput(new ItemStack(Items.IRON_INGOT), 1),
                input.getInput("forge:ingots/manganese", 1),
                new ItemStack(IUItem.alloysingot.getItemFromMeta(9), 1), 4500
        );


    }

    @OnlyIn(Dist.CLIENT)
    public GuiCore<ContainerBase<? extends IAdvInventory>> getGui(Player entityPlayer, ContainerBase<? extends IAdvInventory> menu) {
        return new GuiAlloySmelter((ContainerDoubleElectricMachine) menu);
    }


    public float getWrenchDropRate() {
        return 0.85F;
    }


    @Override
    public SoundEvent getSound() {
        return EnumSound.alloysmelter.getSoundEvent();
    }

}
