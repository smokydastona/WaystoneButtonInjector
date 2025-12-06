/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  net.blay09.mods.unbreakables.api.UnbreakablesAPI
 *  net.blay09.mods.unbreakables.api.parameter.NoParameter
 */
package net.blay09.mods.waystones.compat;

import java.util.Optional;
import net.blay09.mods.unbreakables.api.UnbreakablesAPI;
import net.blay09.mods.unbreakables.api.parameter.NoParameter;
import net.blay09.mods.waystones.api.IWaystone;
import net.blay09.mods.waystones.api.WaystonesAPI;

public class UnbreakablesIntegration {
    public UnbreakablesIntegration() {
        UnbreakablesAPI.registerCondition((String)"is_waystone_owner", NoParameter.class, (context, params) -> context.viaServer(level -> {
            Optional<IWaystone> waystone = WaystonesAPI.getWaystoneAt(level.m_7654_(), context.getBlockGetter(), context.getPos());
            return waystone.map(it -> it.isOwner(context.getPlayer())).orElse(true);
        }));
        UnbreakablesAPI.registerCondition((String)"is_waystone_global", NoParameter.class, (context, params) -> context.viaServer(level -> {
            Optional<IWaystone> waystone = WaystonesAPI.getWaystoneAt(level.m_7654_(), context.getBlockGetter(), context.getPos());
            return waystone.map(IWaystone::isGlobal).orElse(true);
        }));
    }
}

