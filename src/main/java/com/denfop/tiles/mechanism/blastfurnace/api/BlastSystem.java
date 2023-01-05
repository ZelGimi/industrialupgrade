package com.denfop.tiles.mechanism.blastfurnace.api;

import com.denfop.tiles.mechanism.blastfurnace.block.TileEntityBlastFurnaceMain;
import ic2.core.IC2;
import ic2.core.init.Localization;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

import java.util.HashMap;
import java.util.Map;

public class BlastSystem {

    public static BlastSystem instance;
    private final Map<BlockPos, Class> blockPosMap = new HashMap<>();
    private final BlockPos pos;

    public BlastSystem() {
        this.pos = BlockPos.ORIGIN;
        initStructures();
        // TODO: EAST -> (z -> x) (x -> z) z < 0 x < 0
        // TODO: NORTH DEFAULT z > 0
        // TODO: WEST -> (z -> x) (x -> z)
        // TODO: SOUTH DEFAULT z < 0
    }

    public void update(BlockPos pos, World world, IBlastPart part) {
        for (EnumFacing facing : EnumFacing.values()) {
            final TileEntity tile = world.getTileEntity(pos.offset(facing));
            if (tile instanceof IBlastPart) {
                IBlastPart blastPart = (IBlastPart) tile;
                if (blastPart.getMain() == null) {
                    continue;
                } else {
                    blastPart.getMain().update_block();
                    if (part.getMain() == null) {
                        continue;
                    } else {
                        break;
                    }
                }
            } else if (tile instanceof IBlastMain) {
                IBlastMain blastPart = (IBlastMain) tile;
                blastPart.update_block();
                if (part.getMain() == null) {
                    continue;
                } else {
                    break;
                }
            }
        }
    }

    public boolean getFull(EnumFacing facing, BlockPos pos, World world, IBlastMain blastMain, EntityPlayer player) {
        for (Map.Entry<BlockPos, Class> entry : blockPosMap.entrySet()) {
            if (entry.getValue() == IBlastMain.class) {
                continue;
            }
            BlockPos pos1;
            switch (facing) {
                case NORTH:
                    pos1 = pos.add(entry.getKey());
                    break;
                case UP:
                case DOWN:
                    return false;
                case WEST:
                    pos1 = pos.add(entry.getKey().getZ(), entry.getKey().getY(), entry.getKey().getX());
                    break;
                case EAST:
                    pos1 = pos.add(entry.getKey().getZ() * -1, entry.getKey().getY(), entry.getKey().getX() * -1);
                    break;
                case SOUTH:
                    pos1 = pos.add(entry.getKey().getX(), entry.getKey().getY(), entry.getKey().getZ() * -1);
                    break;
                default:
                    throw new IllegalStateException("Unexpected value: " + facing);
            }
            final TileEntity tile = world.getTileEntity(pos1);
            if (entry.getValue() == IBlastInputItem.class) {
                if (!(tile instanceof IBlastInputItem)) {
                    IC2.platform.messagePlayer(
                            player,
                            Localization.translate("iu.not.found") + pos1 + " " + Localization.translate(
                                    "industrialupgrade.blastfurnace.blast_furnace_input")
                    );
                    return false;
                } else {
                    IBlastInputItem blastInputItem = (IBlastInputItem) tile;
                    if (blastInputItem.getMain() != null && blastInputItem.getMain() != blastMain) {
                        IC2.platform.messagePlayer(
                                player,
                                Localization.translate("iu.found.but.no_main") + pos1 + " " + Localization.translate(
                                        "industrialupgrade" +
                                                ".blastfurnace.blast_furnace_input")
                        );

                        return false;
                    }
                    blastMain.setInputItem((IBlastInputItem) tile);
                    blastInputItem.setMain(blastMain);
                }
            } else if (entry.getValue() == IBlastInputFluid.class) {
                if (!(tile instanceof IBlastInputFluid)) {
                    IC2.platform.messagePlayer(
                            player,
                            Localization.translate("iu.not.found") + pos1 + " " + Localization.translate(
                                    "industrialupgrade.blastfurnace.blast_furnace_input_fluid")
                    );
                    return false;
                } else {
                    IBlastInputFluid blastInputFluid = (IBlastInputFluid) tile;
                    if (blastInputFluid.getMain() != null && blastInputFluid.getMain() != blastMain) {
                        IC2.platform.messagePlayer(
                                player,
                                Localization.translate("iu.found.but.no_main") + pos1 + " " + Localization.translate(
                                        "industrialupgrade.blastfurnace.blast_furnace_input_fluid")
                        );

                        return false;
                    }
                    blastMain.setInputFluid((IBlastInputFluid) tile);
                    blastInputFluid.setMain(blastMain);
                }
            } else if (entry.getValue() == IBlastOutputItem.class) {
                if (!(tile instanceof IBlastOutputItem)) {
                    IC2.platform.messagePlayer(
                            player,
                            Localization.translate("iu.not.found") + pos1 + " " + Localization.translate(
                                    "industrialupgrade.blastfurnace.blast_furnace_output")
                    );

                    return false;
                } else {
                    IBlastOutputItem blastOutputItem = (IBlastOutputItem) tile;
                    if (blastOutputItem.getMain() != null && blastOutputItem.getMain() != blastMain) {
                        IC2.platform.messagePlayer(
                                player,
                                Localization.translate("iu.found.but.no_main") + pos1 + " " + Localization.translate(
                                        "industrialupgrade.blastfurnace.blast_furnace_output")
                        );

                        return false;
                    }
                    blastMain.setOutputItem((IBlastOutputItem) tile);
                    blastOutputItem.setMain(blastMain);
                }
            } else if (entry.getValue() == IBlastHeat.class) {
                if (!(tile instanceof IBlastHeat)) {
                    IC2.platform.messagePlayer(
                            player,
                            Localization.translate("iu.not.found") + pos1 + " " + Localization.translate(
                                    "industrialupgrade.blastfurnace.blast_furnace_heat")
                    );

                    return false;
                } else {
                    IBlastHeat blastHeat = (IBlastHeat) tile;
                    if (blastHeat.getMain() != null && blastHeat.getMain() != blastMain) {
                        IC2.platform.messagePlayer(
                                player,
                                Localization.translate("iu.found.but.no_main") + pos1 + " " + Localization.translate(
                                        "industrialupgrade.blastfurnace.blast_furnace_heat")
                        );

                        return false;
                    }
                    blastMain.setHeat((IBlastHeat) tile);
                    blastHeat.setMain(blastMain);
                }
            } else {
                if (!(tile instanceof IOtherBlastPart)) {
                    IC2.platform.messagePlayer(
                            player,
                            Localization.translate("iu.not.found") + pos1 + " " + Localization.translate(
                                    "industrialupgrade.blastfurnace.blast_furnace_part")
                    );

                    return false;
                } else {
                    IOtherBlastPart blastPart = (IOtherBlastPart) tile;
                    if (blastPart.getMain() != null && blastPart.getMain() != blastMain) {
                        IC2.platform.messagePlayer(
                                player,
                                Localization.translate("iu.found.but.no_main") + pos1 + " " + Localization.translate(
                                        "industrialupgrade.blastfurnace.blast_furnace_part")
                        );

                        return false;
                    }
                    blastPart.setMain(blastMain);
                }

            }
        }
        return true;
    }

    public boolean getFull(EnumFacing facing, BlockPos pos, World world, IBlastMain blastMain) {
        for (Map.Entry<BlockPos, Class> entry : blockPosMap.entrySet()) {
            if (entry.getValue() == IBlastMain.class) {
                continue;
            }
            BlockPos pos1;
            switch (facing) {
                case NORTH:
                    pos1 = pos.add(entry.getKey());
                    break;
                case UP:
                case DOWN:
                    return false;
                case WEST:
                    pos1 = pos.add(entry.getKey().getZ(), entry.getKey().getY(), entry.getKey().getX());
                    break;
                case EAST:
                    pos1 = pos.add(entry.getKey().getZ() * -1, entry.getKey().getY(), entry.getKey().getX() * -1);
                    break;
                case SOUTH:
                    pos1 = pos.add(entry.getKey().getX(), entry.getKey().getY(), entry.getKey().getZ() * -1);
                    break;
                default:
                    throw new IllegalStateException("Unexpected value: " + facing);
            }
            final TileEntity tile = world.getTileEntity(pos1);
            if (entry.getValue() == IBlastInputItem.class) {
                if (!(tile instanceof IBlastInputItem)) {

                    return false;
                } else {
                    IBlastInputItem blastInputItem = (IBlastInputItem) tile;
                    if (blastInputItem.getMain() != null && blastInputItem.getMain() != blastMain) {
                        return false;
                    }
                    blastMain.setInputItem((IBlastInputItem) tile);
                    blastInputItem.setMain(blastMain);
                }
            } else if (entry.getValue() == IBlastInputFluid.class) {
                if (!(tile instanceof IBlastInputFluid)) {
                    return false;
                } else {
                    IBlastInputFluid blastInputFluid = (IBlastInputFluid) tile;
                    if (blastInputFluid.getMain() != null && blastInputFluid.getMain() != blastMain) {
                        return false;
                    }
                    blastMain.setInputFluid((IBlastInputFluid) tile);
                    blastInputFluid.setMain(blastMain);
                }
            } else if (entry.getValue() == IBlastOutputItem.class) {
                if (!(tile instanceof IBlastOutputItem)) {
                    return false;
                } else {
                    IBlastOutputItem blastOutputItem = (IBlastOutputItem) tile;
                    if (blastOutputItem.getMain() != null && blastOutputItem.getMain() != blastMain) {
                        return false;
                    }
                    blastMain.setOutputItem((IBlastOutputItem) tile);
                    blastOutputItem.setMain(blastMain);
                }
            } else if (entry.getValue() == IBlastHeat.class) {
                if (!(tile instanceof IBlastHeat)) {
                    return false;
                } else {
                    IBlastHeat blastHeat = (IBlastHeat) tile;
                    if (blastHeat.getMain() != null && blastHeat.getMain() != blastMain) {
                        return false;
                    }
                    blastMain.setHeat((IBlastHeat) tile);
                    blastHeat.setMain(blastMain);
                }
            } else {
                if (!(tile instanceof IOtherBlastPart)) {
                    return false;
                } else {
                    IOtherBlastPart blastPart = (IOtherBlastPart) tile;
                    if (blastPart.getMain() != null && blastPart.getMain() != blastMain) {
                        return false;
                    }
                    blastPart.setMain(blastMain);
                }

            }
        }
        return true;
    }

    private void initStructures() {
        add(pos, IBlastMain.class);
        add(pos.add(0, 1, 1), IBlastInputItem.class);
        add(pos.add(0, 0, 2), IBlastInputFluid.class);
        add(pos.add(1, 0, 1), IBlastOutputItem.class);
        add(pos.add(-1, 0, 1), IBlastHeat.class);
        BlockPos pos1 = pos.add(0, 0, 1);
        for (int i = -1; i < 2; i++) {
            for (int j = -1; j < 2; j++) {
                for (int k = -1; k < 2; k++) {
                    add(pos1.add(i, j, k), IOtherBlastPart.class);
                }
            }
        }
    }

    public void add(BlockPos pos, Class class1) {
        if (this.blockPosMap.containsKey(pos)) {
            return;
        }
        this.blockPosMap.put(pos, class1);

    }

    public void deleteMain(EnumFacing facing, BlockPos pos, World world, TileEntityBlastFurnaceMain blastMain) {
        for (Map.Entry<BlockPos, Class> entry : blockPosMap.entrySet()) {
            if (entry.getValue() == IBlastMain.class) {
                continue;
            }
            BlockPos pos1;
            switch (facing) {
                case NORTH:
                    pos1 = pos.add(entry.getKey());
                    break;
                case UP:
                case DOWN:
                    return;
                case WEST:
                    pos1 = pos.add(entry.getKey().getZ(), entry.getKey().getY(), entry.getKey().getX());
                    break;
                case EAST:
                    pos1 = pos.add(entry.getKey().getZ() * -1, entry.getKey().getY(), entry.getKey().getX() * -1);
                    break;
                case SOUTH:
                    pos1 = pos.add(entry.getKey().getX(), entry.getKey().getY(), entry.getKey().getZ() * -1);
                    break;
                default:
                    throw new IllegalStateException("Unexpected value: " + facing);
            }
            final TileEntity tile = world.getTileEntity(pos1);
            if (entry.getValue() == IBlastInputItem.class) {
                if (!(tile instanceof IBlastInputItem)) {

                    continue;
                } else {
                    IBlastInputItem blastInputItem = (IBlastInputItem) tile;
                    if (blastInputItem.getMain() == blastMain) {
                        blastInputItem.setMain(null);
                    }
                }
            } else if (entry.getValue() == IBlastInputFluid.class) {
                if (!(tile instanceof IBlastInputFluid)) {
                    continue;
                } else {
                    IBlastInputFluid blastInputFluid = (IBlastInputFluid) tile;
                    if (blastInputFluid.getMain() == blastMain) {
                        blastInputFluid.setMain(null);
                    }
                }
            } else if (entry.getValue() == IBlastOutputItem.class) {
                if (!(tile instanceof IBlastOutputItem)) {
                    continue;
                } else {
                    IBlastOutputItem blastOutputItem = (IBlastOutputItem) tile;
                    if (blastOutputItem.getMain() == blastMain) {
                        blastOutputItem.setMain(null);
                    }
                }
            } else if (entry.getValue() == IBlastHeat.class) {
                if (!(tile instanceof IBlastHeat)) {
                    continue;
                } else {
                    IBlastHeat blastHeat = (IBlastHeat) tile;
                    if (blastHeat.getMain() == blastMain) {
                        blastHeat.setMain(null);
                    }
                }
            } else {
                if (!(tile instanceof IOtherBlastPart)) {
                    continue;
                } else {
                    IOtherBlastPart blastPart = (IOtherBlastPart) tile;
                    if (blastPart.getMain() == blastMain) {
                        blastPart.setMain(null);
                    }
                }

            }

        }
    }

}
