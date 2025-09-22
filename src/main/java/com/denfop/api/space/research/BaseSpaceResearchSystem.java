package com.denfop.api.space.research;

import com.denfop.ElectricItem;
import com.denfop.api.space.IAsteroid;
import com.denfop.api.space.IBody;
import com.denfop.api.space.IPlanet;
import com.denfop.api.space.ISatellite;
import com.denfop.api.space.SpaceNet;
import com.denfop.api.space.fakebody.Data;
import com.denfop.api.space.fakebody.EnumOperation;
import com.denfop.api.space.fakebody.FakeAsteroid;
import com.denfop.api.space.fakebody.FakePlanet;
import com.denfop.api.space.fakebody.FakeSatellite;
import com.denfop.api.space.fakebody.IFakeAsteroid;
import com.denfop.api.space.fakebody.IFakeBody;
import com.denfop.api.space.fakebody.IFakePlanet;
import com.denfop.api.space.fakebody.IFakeSatellite;
import com.denfop.api.space.fakebody.SpaceOperation;
import com.denfop.api.space.research.api.IResearchSystem;
import com.denfop.api.space.research.api.IResearchTable;
import com.denfop.api.space.research.api.IRocketLaunchPad;
import com.denfop.api.space.rovers.api.IRovers;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.fluids.FluidUtil;

import java.util.LinkedList;
import java.util.List;
import java.util.UUID;

public class BaseSpaceResearchSystem implements IResearchSystem {


    public BaseSpaceResearchSystem() {

    }

    public Data getDataFromPlayer(final IResearchTable table, IBody body) {
        return SpaceNet.instance.getFakeSpaceSystem().getDataFromUUID(table.getPlayer()).computeIfAbsent(
                body,
                k -> new Data(table.getPlayer(), body)
        );
    }

    @Override
    public void sendingAutoOperation(final IRovers rovers, final IBody body, final IResearchTable table) {
        if (canSendingOperation(rovers, body, table)) {
            if (body instanceof IPlanet) {
                UUID uuid = table.getPlayer();
                final FakePlanet fakeplanet = new FakePlanet(uuid,
                        (IPlanet) body, rovers,
                        SpaceNet.instance.getFakeSpaceSystem().getDataFromUUID(uuid).computeIfAbsent(body, k -> new Data(
                                uuid,
                                body
                        )),
                        new SpaceOperation(
                                body,
                                EnumOperation.WAIT, true
                        )
                );
                FluidStack fluidStack = FluidUtil.getFluidHandler(rovers.getItemStack()).drain(Integer.MAX_VALUE, false);
                if (fluidStack != null && fluidStack.amount >= (fakeplanet.getTimerFrom().getTime() + fakeplanet
                        .getTimerTo()
                        .getTime())) {
                    SpaceNet.instance.getFakeSpaceSystem().addFakePlanet(fakeplanet);
                    SpaceNet.instance
                            .getFakeSpaceSystem()
                            .getBodyMap()
                            .computeIfAbsent(uuid, l -> new LinkedList<>())
                            .add(fakeplanet);
                    SpaceNet.instance.getFakeSpaceSystem().getSpaceTable(uuid).put(body, fakeplanet.getSpaceOperation());
                } else {
                    IRocketLaunchPad rocketLaunchPad = SpaceNet.instance
                            .getFakeSpaceSystem()
                            .getRocketPadMap()
                            .get(table.getPlayer());
                    rocketLaunchPad.getSlotOutput().add(rovers.getItemStack());
                }
            }
            if (body instanceof ISatellite) {
                UUID uuid = table.getPlayer();
                final FakeSatellite fakeSatellite = new FakeSatellite(uuid,
                        (ISatellite) body, rovers,
                        SpaceNet.instance.getFakeSpaceSystem().getDataFromUUID(uuid).computeIfAbsent(body, k -> new Data(
                                uuid,
                                body
                        )),
                        new SpaceOperation(
                                body,
                                EnumOperation.WAIT, true
                        )
                );
                FluidStack fluidStack = FluidUtil.getFluidHandler(rovers.getItemStack()).drain(Integer.MAX_VALUE, false);
                if (fluidStack != null && fluidStack.amount >= (fakeSatellite.getTimerFrom().getTime() + fakeSatellite
                        .getTimerTo()
                        .getTime())) {
                    SpaceNet.instance.getFakeSpaceSystem().addFakeSatellite(fakeSatellite);
                    SpaceNet.instance.getFakeSpaceSystem().getBodyMap().computeIfAbsent(uuid, l -> new LinkedList<>()).add(
                            fakeSatellite);
                    SpaceNet.instance.getFakeSpaceSystem().getSpaceTable(uuid).put(body, fakeSatellite.getSpaceOperation());

                } else {
                    IRocketLaunchPad rocketLaunchPad = SpaceNet.instance
                            .getFakeSpaceSystem()
                            .getRocketPadMap()
                            .get(table.getPlayer());
                    rocketLaunchPad.getSlotOutput().add(rovers.getItemStack());
                }
            }
            if (body instanceof IAsteroid) {
                UUID uuid = table.getPlayer();
                final FakeAsteroid fakeSatellite = new FakeAsteroid(uuid,
                        (IAsteroid) body, rovers,
                        SpaceNet.instance.getFakeSpaceSystem().getDataFromUUID(uuid).computeIfAbsent(body, k -> new Data(
                                uuid,
                                body
                        )),
                        new SpaceOperation(
                                body,
                                EnumOperation.WAIT, true
                        )
                );
                FluidStack fluidStack = FluidUtil.getFluidHandler(rovers.getItemStack()).drain(Integer.MAX_VALUE, false);
                if (fluidStack != null && fluidStack.amount >= (fakeSatellite.getTimerFrom().getTime() + fakeSatellite
                        .getTimerTo()
                        .getTime())) {
                    SpaceNet.instance.getFakeSpaceSystem().addFakeAsteroid(fakeSatellite);
                    SpaceNet.instance.getFakeSpaceSystem().getBodyMap().computeIfAbsent(uuid, l -> new LinkedList<>()).add(
                            fakeSatellite);
                    SpaceNet.instance.getFakeSpaceSystem().getSpaceTable(uuid).put(body, fakeSatellite.getSpaceOperation());

                } else {
                    IRocketLaunchPad rocketLaunchPad = SpaceNet.instance
                            .getFakeSpaceSystem()
                            .getRocketPadMap()
                            .get(table.getPlayer());
                    rocketLaunchPad.getSlotOutput().add(rovers.getItemStack());
                }
            }
            IRocketLaunchPad rocketLaunchPad = SpaceNet.instance.getFakeSpaceSystem().getRocketPadMap().get(table.getPlayer());
            rocketLaunchPad.consumeRover();
        }
    }

    @Override
    public void sendingOperation(final IRovers rovers, final IBody body, final IResearchTable table) {
        if (canSendingOperation(rovers, body, table)) {
            if (body instanceof IPlanet) {
                UUID uuid = table.getPlayer();
                final FakePlanet fakeplanet = new FakePlanet(uuid,
                        (IPlanet) body, rovers,
                        SpaceNet.instance.getFakeSpaceSystem().getDataFromUUID(uuid).computeIfAbsent(body, k -> new Data(
                                uuid,
                                body
                        )),
                        new SpaceOperation(
                                body,
                                EnumOperation.WAIT
                        )
                );
                SpaceNet.instance.getFakeSpaceSystem().addFakePlanet(fakeplanet);
                IRocketLaunchPad rocketLaunchPad = SpaceNet.instance.getFakeSpaceSystem().getRocketPadMap().get(uuid);
                rocketLaunchPad.addDataRocket(fakeplanet.getRover().getItemStack());
                SpaceNet.instance
                        .getFakeSpaceSystem()
                        .getBodyMap()
                        .computeIfAbsent(uuid, l -> new LinkedList<>())
                        .add(fakeplanet);
                SpaceNet.instance.getFakeSpaceSystem().getSpaceTable(uuid).put(body, fakeplanet.getSpaceOperation());
            }
            if (body instanceof ISatellite) {
                UUID uuid = table.getPlayer();
                final FakeSatellite fakeSatellite = new FakeSatellite(uuid,
                        (ISatellite) body, rovers,
                        SpaceNet.instance.getFakeSpaceSystem().getDataFromUUID(uuid).computeIfAbsent(body, k -> new Data(
                                uuid,
                                body
                        )),
                        new SpaceOperation(
                                body,
                                EnumOperation.WAIT
                        )
                );
                SpaceNet.instance.getFakeSpaceSystem().addFakeSatellite(fakeSatellite);
                IRocketLaunchPad rocketLaunchPad = SpaceNet.instance.getFakeSpaceSystem().getRocketPadMap().get(uuid);
                rocketLaunchPad.addDataRocket(fakeSatellite.getRover().getItemStack());
                SpaceNet.instance.getFakeSpaceSystem().getBodyMap().computeIfAbsent(uuid, l -> new LinkedList<>()).add(
                        fakeSatellite);
                SpaceNet.instance.getFakeSpaceSystem().getSpaceTable(uuid).put(body, fakeSatellite.getSpaceOperation());
            }
            if (body instanceof IAsteroid) {
                UUID uuid = table.getPlayer();
                final FakeAsteroid fakeSatellite = new FakeAsteroid(uuid,
                        (IAsteroid) body, rovers,
                        SpaceNet.instance.getFakeSpaceSystem().getDataFromUUID(uuid).computeIfAbsent(body, k -> new Data(
                                uuid,
                                body
                        )),
                        new SpaceOperation(
                                body,
                                EnumOperation.WAIT
                        )
                );
                SpaceNet.instance.getFakeSpaceSystem().addFakeAsteroid(fakeSatellite);
                IRocketLaunchPad rocketLaunchPad = SpaceNet.instance.getFakeSpaceSystem().getRocketPadMap().get(uuid);
                rocketLaunchPad.addDataRocket(fakeSatellite.getRover().getItemStack());
                SpaceNet.instance.getFakeSpaceSystem().getBodyMap().computeIfAbsent(uuid, l -> new LinkedList<>()).add(
                        fakeSatellite);
                SpaceNet.instance.getFakeSpaceSystem().getSpaceTable(uuid).put(body, fakeSatellite.getSpaceOperation());
            }
            IRocketLaunchPad rocketLaunchPad = SpaceNet.instance.getFakeSpaceSystem().getRocketPadMap().get(table.getPlayer());
            rocketLaunchPad.consumeRover();
        }

    }

    @Override
    public boolean canSendingOperation(final IRovers rovers, final IBody body, final IResearchTable table) {
        final List<IFakeBody> list = SpaceNet.instance.getFakeSpaceSystem().getBodyMap().computeIfAbsent(
                table.getPlayer(),
                k -> new LinkedList<>()
        );
        for (IFakeBody fakeBody : list) {
            if (fakeBody.matched(body)) {
                return false;
            }
        }

        IRocketLaunchPad rocketLaunchPad = SpaceNet.instance.getFakeSpaceSystem().getRocketPadMap().get(table.getPlayer());
        if (rocketLaunchPad == null) {
            return false;
        }
        if (rovers == null) {
            return false;
        }
        if (rovers.getItem().getFluidHandler(rovers.getItemStack()).drain(10, false) == null) {
            return false;
        }
        Data data = SpaceNet.instance.getFakeSpaceSystem().getDataFromUUID(table.getPlayer()).computeIfAbsent(
                body,
                k -> new Data(table.getPlayer(), body)
        );

        switch (rovers.getItem().getType()) {
            case ROVERS:
                if (data.getPercent() >= 0) {
                    break;
                }
            case PROBE:
                if (data.getPercent() >= 20) {
                    break;
                } else {
                    return false;
                }
            case SATELLITE:
                if (data.getPercent() >= 50) {
                    break;
                } else {
                    return false;
                }
            case ROCKET:
                if (data.getPercent() >= 80) {
                    break;
                } else {
                    return false;
                }
        }
        if (!ElectricItem.manager.canUse(rovers.getItemStack(), 10)) {
            return false;
        }
        if (body instanceof IPlanet) {
            return table.getLevel().ordinal() >= ((IPlanet) body).getLevels().ordinal() && rovers
                    .getItem()
                    .getLevel()
                    .getLevelsList()
                    .contains(((IPlanet) body).getLevels());
        }
        if (body instanceof ISatellite) {
            return table.getLevel().ordinal() >= ((ISatellite) body).getLevels().ordinal() && rovers
                    .getItem()
                    .getLevel()
                    .getLevelsList()
                    .contains(((ISatellite) body).getLevels());
        }
        if (body instanceof IAsteroid) {
            return table.getLevel().ordinal() >= ((IAsteroid) body).getLevels().ordinal() && rovers
                    .getItem()
                    .getLevel()
                    .getLevelsList()
                    .contains(((IAsteroid) body).getLevels());
        }
        return false;
    }

    @Override
    public void returnOperation(final IBody body, final IResearchTable table) {
        if (body instanceof IPlanet) {
            final List<IFakeBody> list = SpaceNet.instance.getFakeSpaceSystem()
                    .getBodyMap()
                    .get(table.getPlayer());
            IFakePlanet fakePlanet = null;
            for (IFakeBody fakeBody : list) {
                if (fakeBody.matched(body)) {
                    fakePlanet = (IFakePlanet) fakeBody;
                    break;
                }
            }
            if (fakePlanet != null && fakePlanet.getTimerTo().canWork()) {
                fakePlanet.getTimerTo().setCanWork(false);
                fakePlanet.getTimerFrom().setCanWork(true);
                fakePlanet.getTimerFrom().getTimeFromTimerRemove(fakePlanet.getTimerTo());

            }
        } else if (body instanceof ISatellite) {
            final List<IFakeBody> list = SpaceNet.instance.getFakeSpaceSystem()
                    .getBodyMap()
                    .get(table.getPlayer());
            IFakeSatellite fakeSatellite = null;
            for (IFakeBody fakeBody : list) {
                if (fakeBody.matched(body)) {
                    fakeSatellite = (IFakeSatellite) fakeBody;
                    break;
                }
            }
            if (fakeSatellite != null && fakeSatellite.getTimerTo().canWork()) {
                fakeSatellite.getTimerTo().setCanWork(false);
                fakeSatellite.getTimerFrom().setCanWork(true);
                fakeSatellite.getTimerFrom().getTimeFromTimerRemove(fakeSatellite.getTimerTo());

            }
        } else if (body instanceof IAsteroid) {
            final List<IFakeBody> list = SpaceNet.instance.getFakeSpaceSystem()
                    .getBodyMap()
                    .get(table.getPlayer());
            IFakeAsteroid fakeSatellite = null;
            for (IFakeBody fakeBody : list) {
                if (fakeBody.matched(body)) {
                    fakeSatellite = (IFakeAsteroid) fakeBody;
                    break;
                }
            }
            if (fakeSatellite != null && fakeSatellite.getTimerTo().canWork()) {
                fakeSatellite.getTimerTo().setCanWork(false);
                fakeSatellite.getTimerFrom().setCanWork(true);
                fakeSatellite.getTimerFrom().getTimeFromTimerRemove(fakeSatellite.getTimerTo());

            }
        }
    }

}
