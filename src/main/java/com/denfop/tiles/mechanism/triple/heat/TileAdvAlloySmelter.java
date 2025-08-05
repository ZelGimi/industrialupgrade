package com.denfop.tiles.mechanism.triple.heat;

import com.denfop.IUItem;
import com.denfop.Localization;
import com.denfop.api.Recipes;
import com.denfop.api.audio.EnumTypeAudio;
import com.denfop.api.gui.EnumTypeSlot;
import com.denfop.api.gui.IType;
import com.denfop.api.inv.IAdvInventory;
import com.denfop.api.recipe.BaseMachineRecipe;
import com.denfop.api.recipe.IHasRecipe;
import com.denfop.api.recipe.Input;
import com.denfop.api.recipe.RecipeOutput;
import com.denfop.api.tile.IMultiTileBlock;
import com.denfop.audio.EnumSound;
import com.denfop.blocks.BlockTileEntity;
import com.denfop.blocks.mechanism.BlockBaseMachine1;
import com.denfop.componets.AirPollutionComponent;
import com.denfop.componets.EnumTypeStyle;
import com.denfop.componets.HeatComponent;
import com.denfop.componets.SoilPollutionComponent;
import com.denfop.container.ContainerBase;
import com.denfop.container.ContainerTripleElectricMachine;
import com.denfop.gui.GuiAdvAlloySmelter;
import com.denfop.gui.GuiCore;
import com.denfop.invslot.InvSlot;
import com.denfop.network.DecoderHandler;
import com.denfop.network.packet.CustomPacketBuffer;
import com.denfop.network.packet.PacketUpdateFieldTile;
import com.denfop.recipe.IInputHandler;
import com.denfop.tiles.base.EnumTripleElectricMachine;
import com.denfop.tiles.base.TileTripleElectricMachine;
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

import java.io.IOException;

public class TileAdvAlloySmelter extends TileTripleElectricMachine implements IHasRecipe, IType {

    public final HeatComponent heat;
    public final InvSlot input_slot;
    private final SoilPollutionComponent pollutionSoil;
    private final AirPollutionComponent pollutionAir;

    public TileAdvAlloySmelter(BlockPos pos, BlockState state) {
        super(1, 300, 1, Localization.translate("iu.AdvAlloymachine.name"), EnumTripleElectricMachine.ADV_ALLOY_SMELTER, BlockBaseMachine1.adv_alloy_smelter, pos, state);
        this.heat = this.addComponent(HeatComponent
                .asBasicSink(this, 5000));
        Recipes.recipes.addInitRecipes(this);
        this.input_slot = new InvSlot(this, InvSlot.TypeItemSlot.INPUT, 1) {
            @Override
            public ItemStack set(final int index, final ItemStack content) {
                super.set(index, content);
                if (this.get(0).isEmpty()) {
                    ((TileAdvAlloySmelter) this.base).inputSlotA.changeAccepts(ItemStack.EMPTY);
                } else {
                    ((TileAdvAlloySmelter) this.base).inputSlotA.changeAccepts(this.get(0));
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
        this.pollutionSoil = this.addComponent(new SoilPollutionComponent(this, 0.075));
        this.pollutionAir = this.addComponent(new AirPollutionComponent(this, 0.15));
    }
    @Override
    public void updateEntityServer() {
        super.updateEntityServer();
        if (this.getActive()  && this.level.getGameTime() % 5 == 0){
            ParticleUtils.spawnAlloySmelterParticles(level,pos,level.random);
        }
    }
    public static void addAlloysmelter(Object container, Object fill, Object fill1, ItemStack output, int temperature) {
        final IInputHandler input = com.denfop.api.Recipes.inputFactory;
        final CompoundTag nbt = ModUtils.nbt();
        nbt.putShort("temperature", (short) temperature);
        Recipes.recipes.addRecipe("advalloysmelter", new BaseMachineRecipe(
                new Input(input.getInput(container), input.getInput(fill), input.getInput(fill1)),
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
        return BlockBaseMachine1.adv_alloy_smelter;
    }

    public BlockTileEntity getBlock() {
        return IUItem.basemachine.getBlock(getTeBlock().getId());
    }

    @Override

    public void init() {
        addAlloysmelter("forge:ingots/Copper", "forge:ingots/Zinc", "forge:ingots/Lead", new ItemStack(IUItem.alloysingot.getStack(3), 1), 4500);
        addAlloysmelter("forge:ingots/Aluminium", "forge:ingots/Magnesium", "forge:ingots/Manganese", new ItemStack(IUItem.alloysingot.getStack(5), 1), 4000);

        addAlloysmelter("forge:ingots/Aluminium",
                "forge:ingots/Copper", "forge:ingots/Tin",
                new ItemStack(IUItem.alloysingot.getStack(0), 1), 3000
        );

        addAlloysmelter("forge:ingots/Aluminium",
                "forge:ingots/vanady", "forge:ingots/Cobalt",
                new ItemStack(IUItem.alloysingot.getStack(6), 1), 4500
        );
        addAlloysmelter("forge:ingots/Aluminium",
                "forge:ingots/vanady", "forge:ingots/Cobalt",
                new ItemStack(IUItem.alloysingot.getStack(6), 1), 4500
        );
        addAlloysmelter("forge:ingots/Chromium",
                "forge:ingots/Tungsten", "forge:ingots/Nickel",
                new ItemStack(IUItem.alloysingot.getStack(7), 1), 5000
        );

        addAlloysmelter(
                new ItemStack(IUItem.alloysingot.getStack(2), 4),
                new ItemStack(IUItem.alloysingot.getStack(1), 2),
                new ItemStack(IUItem.crafting_elements.getStack(310)
                        , 1),
                new ItemStack(IUItem.crafting_elements.getStack(355),
                        1
                ),
                3000
        );

        addAlloysmelter("forge:ingots/Aluminium", "forge:ingots/Magnesium", Items.FLINT, new ItemStack(IUItem.alloysingot.getStack(10), 1), 3000);

        addAlloysmelter(
                "forge:gems/Beryllium",
                new ItemStack(Items.COPPER_INGOT, 3),
                "forge:ingots/Tin",
                new ItemStack(IUItem.alloysingot.getStack(11), 1),
                4000
        );
        addAlloysmelter("forge:ingots/Copper", "forge:ingots/Nickel", "forge:ingots/Zinc", new ItemStack(IUItem.alloysingot.getStack(12), 1), 4500);


        addAlloysmelter("forge:ingots/Hafnium",
                "forge:gems/Bor", "forge:ingots/Tantalum",
                new ItemStack(IUItem.alloysingot.getStack(17), 1), 5000
        );
        addAlloysmelter("forge:ingots/Hafnium",
                "forge:ingots/Tantalum", "forge:ingots/Tungsten",
                new ItemStack(IUItem.alloysingot.getStack(20), 1), 5000
        );
        addAlloysmelter("forge:ingots/Hafnium",
                "forge:ingots/Tantalum", new ItemStack(Items.COPPER_INGOT, 4),
                new ItemStack(IUItem.alloysingot.getStack(24), 1), 5000
        );

        addAlloysmelter("forge:ingots/Molybdenum",
                "forge:ingots/Steel", "forge:ingots/Thallium",
                new ItemStack(IUItem.alloysingot.getStack(25), 1), 5000
        );

        addAlloysmelter("forge:ingots/Neodymium", "forge:ingots/Yttrium", "forge:ingots/Aluminium", new ItemStack(IUItem.alloysingot.getStack(30)
                )
                , 5000);

        addAlloysmelter(new ItemStack(IUItem.iuingot.getStack(23), 2), new ItemStack(IUItem.iuingot.getStack(10), 1),
                new ItemStack(IUItem.iuingot.getStack(29)
                        , 1),
                new ItemStack(IUItem.crafting_elements.getStack(504),
                        1
                ), 3000
        );
    }


    @OnlyIn(Dist.CLIENT)
    public GuiCore<ContainerBase<? extends IAdvInventory>> getGui(Player entityPlayer, ContainerBase<? extends IAdvInventory> isAdmin) {
        return new GuiAdvAlloySmelter((ContainerTripleElectricMachine) isAdmin);
    }


    @Override
    public EnumTypeStyle getStyle() {
        return EnumTypeStyle.ADVANCED;
    }

    @Override
    public void updateTileServer(final Player entityPlayer, final double i) {
        sound = !sound;
        new PacketUpdateFieldTile(this, "sound", this.sound);

        if (!sound) {
            if (this.getTypeAudio() == EnumTypeAudio.ON) {
                setType(EnumTypeAudio.OFF);
                initiate(2);

            }
        }
    }

    public void updateField(String name, CustomPacketBuffer is) {

        if (name.equals("sound")) {
            try {
                this.sound = (boolean) DecoderHandler.decode(is);
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }
        super.updateField(name, is);
    }

    @Override
    public SoundEvent getSound() {
        return EnumSound.alloysmelter.getSoundEvent();
    }

}
