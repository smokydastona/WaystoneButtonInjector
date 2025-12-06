/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  journeymap.client.api.ClientPlugin
 *  journeymap.client.api.IClientAPI
 *  journeymap.client.api.IClientPlugin
 *  journeymap.client.api.display.Displayable
 *  journeymap.client.api.display.Waypoint
 *  journeymap.client.api.display.WaypointGroup
 *  journeymap.client.api.event.ClientEvent
 *  journeymap.client.api.event.ClientEvent$Type
 *  net.blay09.mods.balm.api.Balm
 *  net.minecraft.client.resources.language.I18n
 *  org.jetbrains.annotations.Nullable
 */
package net.blay09.mods.waystones.compat;

import java.util.ArrayList;
import java.util.EnumSet;
import java.util.HashSet;
import java.util.List;
import java.util.UUID;
import journeymap.client.api.ClientPlugin;
import journeymap.client.api.IClientAPI;
import journeymap.client.api.IClientPlugin;
import journeymap.client.api.display.Displayable;
import journeymap.client.api.display.Waypoint;
import journeymap.client.api.display.WaypointGroup;
import journeymap.client.api.event.ClientEvent;
import net.blay09.mods.balm.api.Balm;
import net.blay09.mods.waystones.api.IWaystone;
import net.blay09.mods.waystones.api.KnownWaystonesEvent;
import net.blay09.mods.waystones.api.WaystoneUpdateReceivedEvent;
import net.blay09.mods.waystones.config.WaystonesConfig;
import net.blay09.mods.waystones.config.WaystonesConfigData;
import net.minecraft.client.resources.language.I18n;
import org.jetbrains.annotations.Nullable;

@ClientPlugin
public class JourneyMapIntegration
implements IClientPlugin {
    private static final UUID WAYSTONE_GROUP_ID = UUID.fromString("005bdf11-2dbb-4a27-8aa4-0184e86fa33c");
    private IClientAPI api;
    private boolean journeyMapReady;
    private final List<Runnable> scheduledJobsWhenReady = new ArrayList<Runnable>();
    private static JourneyMapIntegration instance;

    public JourneyMapIntegration() {
        instance = this;
        Balm.getEvents().onEvent(KnownWaystonesEvent.class, this::onKnownWaystones);
        Balm.getEvents().onEvent(WaystoneUpdateReceivedEvent.class, this::onWaystoneUpdateReceived);
    }

    public void initialize(IClientAPI iClientAPI) {
        this.api = iClientAPI;
        this.api.subscribe("waystones", EnumSet.of(ClientEvent.Type.MAPPING_STARTED));
    }

    @Nullable
    public static JourneyMapIntegration getInstance() {
        return instance;
    }

    public String getModId() {
        return "waystones";
    }

    public void onEvent(ClientEvent clientEvent) {
        if (clientEvent.type == ClientEvent.Type.MAPPING_STARTED) {
            this.journeyMapReady = true;
            for (Runnable scheduledJob : this.scheduledJobsWhenReady) {
                scheduledJob.run();
            }
            this.scheduledJobsWhenReady.clear();
        }
    }

    public void onKnownWaystones(KnownWaystonesEvent event) {
        if (JourneyMapIntegration.shouldManageWaypoints()) {
            this.runWhenJourneyMapIsReady(() -> this.updateAllWaypoints(event.getWaystones()));
        }
    }

    private static boolean shouldManageWaypoints() {
        WaystonesConfigData config = WaystonesConfig.getActive();
        if (config.compatibility.preferJourneyMapIntegration && Balm.isModLoaded((String)"jmi")) {
            return false;
        }
        return config.compatibility.displayWaystonesOnJourneyMap;
    }

    public void onWaystoneUpdateReceived(WaystoneUpdateReceivedEvent event) {
        if (JourneyMapIntegration.shouldManageWaypoints()) {
            this.runWhenJourneyMapIsReady(() -> this.updateWaypoint(event.getWaystone()));
        }
    }

    private void runWhenJourneyMapIsReady(Runnable runnable) {
        if (this.journeyMapReady) {
            runnable.run();
        } else {
            this.scheduledJobsWhenReady.add(runnable);
        }
    }

    private void updateAllWaypoints(List<IWaystone> waystones) {
        HashSet<String> stillExistingIds = new HashSet<String>();
        for (IWaystone waystone : waystones) {
            stillExistingIds.add(waystone.getWaystoneUid().toString());
            this.updateWaypoint(waystone);
        }
        List waypoints = this.api.getWaypoints("waystones");
        for (Waypoint waypoint : waypoints) {
            if (stillExistingIds.contains(waypoint.getId())) continue;
            this.api.remove((Displayable)waypoint);
        }
    }

    private void updateWaypoint(IWaystone waystone) {
        try {
            String waystoneName = waystone.hasName() ? waystone.getName() : I18n.m_118938_((String)"waystones.map.untitled_waystone", (Object[])new Object[0]);
            Waypoint oldWaypoint = this.api.getWaypoint("waystones", waystone.getWaystoneUid().toString());
            Waypoint waypoint = new Waypoint("waystones", waystone.getWaystoneUid().toString(), waystoneName, waystone.getDimension(), waystone.getPos().m_6630_(2));
            waypoint.setName(waystoneName);
            waypoint.setGroup(new WaypointGroup("waystones", WAYSTONE_GROUP_ID.toString(), "Waystones"));
            if (oldWaypoint != null) {
                waypoint.setEnabled(oldWaypoint.isEnabled());
                if (oldWaypoint.hasColor()) {
                    waypoint.setColor(oldWaypoint.getColor().intValue());
                }
                if (oldWaypoint.hasBackgroundColor()) {
                    waypoint.setBackgroundColor(oldWaypoint.getBackgroundColor().intValue());
                }
                this.api.remove((Displayable)oldWaypoint);
            }
            this.api.show((Displayable)waypoint);
        }
        catch (Exception e) {
            e.printStackTrace();
        }
    }
}

