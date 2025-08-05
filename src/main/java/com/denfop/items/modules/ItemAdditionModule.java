package com.denfop.items.modules;

import com.denfop.IUCore;
import com.denfop.Localization;
import com.denfop.blocks.ISubEnum;
import com.denfop.componets.Energy;
import com.denfop.datacomponent.DataComponentsInit;
import com.denfop.datacomponent.WirelessConnection;
import com.denfop.items.ItemMain;
import com.denfop.tiles.base.TileElectricBlock;
import com.denfop.tiles.base.TileEntityInventory;
import com.denfop.tiles.mechanism.TileEntityAnalyzerChest;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.network.chat.Component;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.item.context.UseOnContext;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntity;
import org.jetbrains.annotations.Nullable;

import java.util.Collections;
import java.util.List;
import java.util.Locale;

public class ItemAdditionModule<T extends Enum<T> & ISubEnum> extends ItemMain<T> {
    public ItemAdditionModule(T element) {
        super(new Item.Properties(), element);
    }


    @Override
    public void appendHoverText(ItemStack itemStack, @Nullable TooltipContext p_41422_, List<Component> info, TooltipFlag p_41424_) {
        super.appendHoverText(itemStack, p_41422_, info, p_41424_);
        int meta = this.getElement().getId();
        switch (meta) {
            case 0:
                info.add(Component.literal(Localization.translate("iu.modules")));
                info.add(Component.literal(Localization.translate("personality")));
                info.add(Component.literal(Localization.translate("personality1")));
                List<String> stringList = itemStack.getOrDefault(DataComponentsInit.LIST_STRING, Collections.emptyList());
                for (String s : stringList) {
                    info.add(Component.literal(s));
                }
                break;
            case 1:
                info.add(Component.literal(Localization.translate("iu.modules")));
                info.add(Component.literal(Localization.translate("transformator")));
                break;
            case 2:
                info.add(Component.literal(Localization.translate("iu.modules")));
                info.add(Component.literal(Localization.translate("transformator1")));
                break;
            case 3:
                info.add(Component.literal(Localization.translate("modulechange")));

                break;
            case 4:

                break;
            case 5:
            case 8:
            case 7:
            case 6:
                info.add(Component.literal(Localization.translate("storagemodul")));
                info.add(Component.literal(Localization.translate("storagemodul1")));
                break;
            case 10:
                info.add(Component.literal(Localization.translate("iu.modules")));
                info.add(Component.literal(Localization.translate("wirelles")));
                if (itemStack.has(DataComponentsInit.WIRELESS)) {
                    WirelessConnection wirelessConnection = itemStack.get(DataComponentsInit.WIRELESS);
                    info.add(Component.literal(Localization.translate("iu.World") + ": " + wirelessConnection.world().getPath()));

                    info.add(Component.literal(Localization.translate("iu.tier") + wirelessConnection.tier()));
                    info.add(Component.literal(Localization.translate("iu.Xcoord") + ": " + wirelessConnection.x()));
                    info.add(Component.literal(Localization.translate("iu.Ycoord") + ": " + wirelessConnection.y()));
                    info.add(Component.literal(Localization.translate("iu.Zcoord") + ": " + wirelessConnection.z()));
                    if (wirelessConnection.change()) {
                        info.add(Component.literal(Localization.translate("mode.storage")));

                    } else {
                        info.add(Component.literal(Localization.translate("mode.panel")));

                    }
                }
        }
    }

    @Override
    public InteractionResult onItemUseFirst(ItemStack stack, UseOnContext context) {
        Player player = context.getPlayer();
        Level level = context.getLevel();
        BlockPos pos = context.getClickedPos();
        Direction side = context.getClickedFace();
        InteractionHand hand = context.getHand();


        if (this.getElement().getId() == 10) {
            BlockEntity be = level.getBlockEntity(pos);
            if (be instanceof TileEntityInventory tile) {
                if (tile.getComp(Energy.class) != null) {
                    WirelessConnection wirelessConnection = stack.getOrDefault(DataComponentsInit.WIRELESS, WirelessConnection.EMPTY);
                    boolean charge = wirelessConnection.change();
                    if (tile instanceof TileElectricBlock && charge) {
                        return InteractionResult.PASS;
                    }
                    wirelessConnection = new WirelessConnection(level.dimension().location(), tile.getComp(Energy.class).getSinkTier(), tile.getBlockPos().getX(), tile.getBlockPos().getY(), tile.getBlockPos().getZ(), wirelessConnection.change());
                    stack.set(DataComponentsInit.WIRELESS, wirelessConnection);
                    return InteractionResult.SUCCESS;
                } else if (tile instanceof TileEntityAnalyzerChest) {
                    WirelessConnection wirelessConnection = stack.getOrDefault(DataComponentsInit.WIRELESS, WirelessConnection.EMPTY);
                    wirelessConnection = new WirelessConnection(level.dimension().location(), 0, tile.getBlockPos().getX(), tile.getBlockPos().getY(), tile.getBlockPos().getZ(), wirelessConnection.change());
                    stack.set(DataComponentsInit.WIRELESS, wirelessConnection);
                    return InteractionResult.SUCCESS;
                }
            }
        }

        return super.useOn(context);
    }

    @Override
    public CreativeModeTab getItemCategory() {
        return IUCore.ModuleTab;
    }

    public enum Types implements ISubEnum {
        personality(0),
        tier_in(1),
        tier_de(2),
        charger(3),
        rf(4),
        mov_charge_eu(5),
        mov_charge_eu_item(6),
        mov_charge_rf(7),
        mov_charge_rf_item(8),
        purifier(9),
        wireless(10),
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
            return "additionmodule";
        }

        public int getId() {
            return this.ID;
        }
    }
}
