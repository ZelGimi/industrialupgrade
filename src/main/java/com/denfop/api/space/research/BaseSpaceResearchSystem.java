package com.denfop.api.space.research;

import com.denfop.api.space.IBaseResource;
import com.denfop.api.space.IBody;
import com.denfop.api.space.IPlanet;
import com.denfop.api.space.ISatellite;
import com.denfop.api.space.SpaceNet;
import com.denfop.api.space.fakebody.Data;
import com.denfop.api.space.fakebody.EnumOperation;
import com.denfop.api.space.fakebody.FakePlanet;
import com.denfop.api.space.fakebody.FakeSatellite;
import com.denfop.api.space.fakebody.IFakeBody;
import com.denfop.api.space.fakebody.IFakePlanet;
import com.denfop.api.space.fakebody.IFakeSatellite;
import com.denfop.api.space.fakebody.SpaceOperation;
import com.denfop.api.space.rovers.IRovers;

import java.util.List;

public class BaseSpaceResearchSystem implements IResearchSystem {


    public BaseSpaceResearchSystem() {

    }

    @Override
    public void sendingOperation(final IRovers rovers, final IBody body, final IResearchTable table) {
        if (canSendingOperation(rovers, body, table)) {
            if (body instanceof IPlanet) {
                final FakePlanet fakeplanet = new FakePlanet(table.getPlayer(),
                        (IPlanet) body, rovers, new Data(table.getPlayer(), body)
                );
                if (!SpaceNet.instance.getFakeSpaceSystem().getFakePlayers().contains(table.getPlayer())) {
                    SpaceNet.instance.getFakeSpaceSystem().getFakePlayers().add(table.getPlayer());
                }
                SpaceNet.instance.getFakeSpaceSystem().addFakePlanet(fakeplanet);
                table.getSpaceBody().put(body, new SpaceOperation(fakeplanet.getPlanet(), EnumOperation.WAIT));
            }
            if (body instanceof ISatellite) {
                final FakeSatellite fakeSatellite = new FakeSatellite(table.getPlayer(),
                        (ISatellite) body, rovers, new Data(table.getPlayer(), body)
                );
                if (!SpaceNet.instance.getFakeSpaceSystem().getFakePlayers().contains(table.getPlayer())) {
                    SpaceNet.instance.getFakeSpaceSystem().getFakePlayers().add(table.getPlayer());
                }
                SpaceNet.instance.getFakeSpaceSystem().addFakeSatellite(fakeSatellite);
                table.getSpaceBody().put(body, new SpaceOperation(fakeSatellite.getSatellite(), EnumOperation.WAIT));

            }

        }
    }

    @Override
    public boolean canSendingOperation(final IRovers rovers, final IBody body, final IResearchTable table) {
        if (body instanceof IPlanet) {
            if (SpaceNet.instance.getFakeSpaceSystem().cadAddFakePlanet(new FakePlanet(table.getPlayer(),
                    (IPlanet) body, rovers, new Data(table.getPlayer(), body)
            ))) {
                return table.getLevel().ordinal() > ((IPlanet) body).getLevels().ordinal() &&
                        (!table.getSpaceBody().containsKey(body) || (table.getSpaceBody().get(body) != null && table
                                .getSpaceBody()
                                .get(body)
                                .getOperation() != EnumOperation.WAIT));
            }
        }
        if (body instanceof ISatellite) {
            if (SpaceNet.instance.getFakeSpaceSystem().cadAddFakeSatellite(new FakeSatellite(table.getPlayer(),
                    (ISatellite) body, rovers, new Data(table.getPlayer(), body)
            ))) {
                return table.getLevel().ordinal() > ((ISatellite) body).getLevels().ordinal() &&
                        (!table.getSpaceBody().containsKey(body) || (table.getSpaceBody().get(body) != null && table
                                .getSpaceBody()
                                .get(body)
                                .getOperation() != EnumOperation.WAIT));
            }
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
            if (fakePlanet != null) {
                fakePlanet.setEnd();
                if (table.getContainerBlock() != null) {
                    final List<IBaseResource> resourceList = SpaceNet.instance.getFakeSpaceSystem().getFakePlanetListMap().get(
                            fakePlanet);
                    for (IBaseResource resource : resourceList) {
                        table.getContainerBlock().getSlotOutput().add(resource.getItemStack());
                    }
                    table.getContainerBlock().getSlotOutput().add(fakePlanet.getRover().getItemStack());
                    SpaceNet.instance.getFakeSpaceSystem().getFakePlanetListMap().remove(fakePlanet);
                    SpaceNet.instance.getFakeSpaceSystem().getFakePlanetList().remove(fakePlanet);
                    SpaceNet.instance.getFakeSpaceSystem().removeFakeBodyFromPlayer(table, fakePlanet.getPlanet());
                    table.getSpaceBody().get(fakePlanet.getPlanet()).setOperation(EnumOperation.FAIL);
                    fakePlanet.remove();
                }

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
            if (fakeSatellite != null) {
                fakeSatellite.setEnd();
                if (table.getContainerBlock() != null) {
                    final List<IBaseResource> resourceList = SpaceNet.instance.getFakeSpaceSystem().getFakeSatelliteListMap().get(
                            fakeSatellite);
                    for (IBaseResource resource : resourceList) {
                        table.getContainerBlock().getSlotOutput().add(resource.getItemStack());
                    }
                    table.getContainerBlock().getSlotOutput().add(fakeSatellite.getRover().getItemStack());
                    SpaceNet.instance.getFakeSpaceSystem().getFakeSatelliteListMap().remove(fakeSatellite);
                    SpaceNet.instance.getFakeSpaceSystem().getFakeSatelliteList().remove(fakeSatellite);
                    SpaceNet.instance.getFakeSpaceSystem().removeFakeBodyFromPlayer(table, fakeSatellite.getSatellite());
                    table.getSpaceBody().get(fakeSatellite.getSatellite()).setOperation(EnumOperation.FAIL);
                    fakeSatellite.remove();
                }

            }
        }
    }

}
