/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  net.blay09.mods.balm.api.Balm
 *  net.blay09.mods.balm.api.event.LivingDamageEvent
 *  net.blay09.mods.balm.api.event.PlayerLoginEvent
 */
package net.blay09.mods.waystones.handler;

import net.blay09.mods.balm.api.Balm;
import net.blay09.mods.balm.api.event.LivingDamageEvent;
import net.blay09.mods.balm.api.event.PlayerLoginEvent;
import net.blay09.mods.waystones.api.WaystoneActivatedEvent;
import net.blay09.mods.waystones.handler.LoginHandler;
import net.blay09.mods.waystones.handler.WarpDamageResetHandler;
import net.blay09.mods.waystones.handler.WaystoneActivationStatHandler;

public class ModEventHandlers {
    public static void initialize() {
        Balm.getEvents().onEvent(PlayerLoginEvent.class, LoginHandler::onPlayerLogin);
        Balm.getEvents().onEvent(LivingDamageEvent.class, WarpDamageResetHandler::onDamage);
        Balm.getEvents().onEvent(WaystoneActivatedEvent.class, WaystoneActivationStatHandler::onWaystoneActivated);
    }
}

