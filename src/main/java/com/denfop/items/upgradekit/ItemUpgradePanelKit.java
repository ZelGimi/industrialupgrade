package com.denfop.items.upgradekit;

import com.denfop.IUCore;
import com.denfop.IUItem;
import com.denfop.blockentity.panels.entity.BlockEntitySolarPanel;
import com.denfop.blockentity.panels.entity.EnumSolarPanels;
import com.denfop.blocks.ISubEnum;
import com.denfop.items.ItemMain;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.network.chat.Component;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.item.context.UseOnContext;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.Vec3;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.List;
import java.util.Locale;

public class ItemUpgradePanelKit<T extends Enum<T> & ISubEnum> extends ItemMain<T> {
    public static int tick = 0;

    public ItemUpgradePanelKit(T element) {
        super(new Item.Properties(), element);
    }

    @Override
    public CreativeModeTab getItemCategory() {
        return IUCore.UpgradeTab;
    }

    @Override
    public InteractionResult onItemUseFirst(ItemStack stack, UseOnContext context) {
        Player player = context.getPlayer();
        Level world = context.getLevel();
        BlockPos pos = context.getClickedPos();
        Direction side = context.getClickedFace();
        InteractionHand hand = context.getHand();

        if (player == null || world.isClientSide()) {
            return InteractionResult.PASS;
        }


        int meta = getElement().getId();
        BlockEntity blockEntity = world.getBlockEntity(pos);

        if (blockEntity instanceof BlockEntitySolarPanel tile) {
            if (tile.getPanels() == null) {
                return InteractionResult.PASS;
            }

            EnumSolarPanels oldPanel = tile.getPanels();
            EnumSolarPanels kit = EnumSolarPanels.getFromID(meta + 1);

            if (kit.solarold != null && !kit.solarold.equals(oldPanel)) {
                return InteractionResult.PASS;
            }

            EnumSolarPanelsKit kit1 = EnumSolarPanelsKit.getFromID(meta);
            BlockState state = world.getBlockState(pos);


            world.removeBlock(pos, false);


            state.getBlock().destroy(world, pos, state);


            AABB area = new AABB(Vec3.atCenterOf(pos.offset(-1, -1, -1)), Vec3.atCenterOf(pos.offset(1, 1, 1)));
            List<ItemEntity> items = world.getEntitiesOfClass(ItemEntity.class, area);
            for (ItemEntity item : items) {
                item.discard();
            }


            ItemStack stack1 = new ItemStack(kit1.solarpanel_new.block.getItem(kit1.solarpanel_new.meta), 1);

            ItemEntity itemEntity = new ItemEntity(world, player.getX(), player.getY(), player.getZ(), stack1);
            world.addFreshEntity(itemEntity);


            List<ItemStack> dropList = tile.getDrop();
            for (ItemStack drop : dropList) {
                ItemEntity dropEntity = new ItemEntity(world, player.getX(), player.getY(), player.getZ(), drop);
                world.addFreshEntity(dropEntity);
            }


            stack.shrink(1);

            return InteractionResult.SUCCESS;
        }

        return InteractionResult.PASS;
    }

    @Override
    public void appendHoverText(
            @Nonnull ItemStack stack,
            @Nullable Level world,
            @Nonnull List<Component> tooltip,
            @Nonnull TooltipFlag flag
    ) {
        tooltip.add(Component.translatable("waring_kit"));

        super.appendHoverText(stack, world, tooltip, flag);
    }

    public enum Types implements ISubEnum {
        upgradepanelkit(0),
        upgradepanelkit1(1),
        upgradepanelkit2(2),
        upgradepanelkit3(3),

        upgradepanelkit4(4),
        upgradepanelkit5(5),
        upgradepanelkit6(6),
        upgradepanelkit7(7),
        upgradepanelkit8(8),
        upgradepanelkit9(9),
        upgradepanelkit10(10),
        upgradepanelkit11(11),
        upgradepanelkit12(12),
        upgradepanelkit13(13),


        ;

        private final String name;
        private final int ID;

        Types(final int ID) {
            this.name = this.name().toLowerCase(Locale.US);
            this.ID = ID;
        }

        public static Types getFromID(final int ID) {
            return values()[ID % values().length];
        }

        public String getName() {
            return this.name;
        }

        @Override
        public String getMainPath() {
            return "upgradekitpanel";
        }

        public int getId() {
            return this.ID;
        }
    }

    public enum EnumSolarPanelsKit {
        ADVANCED(EnumSolarPanels.ADVANCED_SOLAR_PANEL, 0, true),
        HYBRID(EnumSolarPanels.HYBRID_SOLAR_PANEL, 1, true),
        PERFECT(EnumSolarPanels.PERFECT_SOLAR_PANEL, 2, true),
        QUANTUM(EnumSolarPanels.QUANTUM_SOLAR_PANEL, 3, true),
        SPECTRAL(EnumSolarPanels.SPECTRAL_SOLAR_PANEL, 4, true),
        PROTON(EnumSolarPanels.PROTON_SOLAR_PANEL, 5, true),
        SINGULAR(EnumSolarPanels.SINGULAR_SOLAR_PANEL, 6, true),
        DIFFRACTION(EnumSolarPanels.DIFFRACTION_SOLAR_PANEL, 7, true),
        PHOTON(EnumSolarPanels.PHOTONIC_SOLAR_PANEL, 8, true),
        NEUTRONIUM(EnumSolarPanels.NEUTRONIUN_SOLAR_PANEL, 9, true),
        BARION(EnumSolarPanels.BARION_SOLAR_PANEL, 10, true),
        HADRON(EnumSolarPanels.HADRON_SOLAR_PANEL, 11, true),
        GRAVITON(EnumSolarPanels.GRAVITON_SOLAR_PANEL, 12, true),
        QUARK(EnumSolarPanels.QUARK_SOLAR_PANEL, 13, true),


        ;

        public final int item_meta;
        public final EnumSolarPanels solarpanel_new;
        public final boolean register;

        EnumSolarPanelsKit(EnumSolarPanels solarpanel_new, int item_meta, boolean register) {
            this.item_meta = item_meta;
            this.solarpanel_new = solarpanel_new;
            this.register = register;
        }

        public static EnumSolarPanelsKit getFromID(final int ID) {
            return values()[ID % values().length];
        }

        public static void registerkit() {
            for (EnumSolarPanelsKit machine : EnumSolarPanelsKit.values()) {
                IUItem.map1.put(machine.item_meta, machine.solarpanel_new);

            }
        }
    }
}
