/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  net.blay09.mods.balm.api.Balm
 *  net.blay09.mods.balm.api.client.BalmClient
 *  net.minecraftforge.api.distmarker.Dist
 *  net.minecraftforge.fml.DistExecutor
 *  net.minecraftforge.fml.common.Mod
 *  org.slf4j.Logger
 *  org.slf4j.LoggerFactory
 */
package net.blay09.mods.waystones;

import java.lang.reflect.InvocationTargetException;
import net.blay09.mods.balm.api.Balm;
import net.blay09.mods.balm.api.client.BalmClient;
import net.blay09.mods.waystones.Waystones;
import net.blay09.mods.waystones.client.WaystonesClient;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.fml.DistExecutor;
import net.minecraftforge.fml.common.Mod;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@Mod(value="waystones")
public class ForgeWaystones {
    private static final Logger logger = LoggerFactory.getLogger(ForgeWaystones.class);

    public ForgeWaystones() {
        Balm.initialize((String)"waystones", Waystones::initialize);
        DistExecutor.runWhenOn((Dist)Dist.CLIENT, () -> () -> BalmClient.initialize((String)"waystones", WaystonesClient::initialize));
        Balm.initializeIfLoaded((String)"theoneprobe", (String)"net.blay09.mods.waystones.compat.TheOneProbeIntegration");
        if (Balm.isModLoaded((String)"repurposed_structures")) {
            try {
                Class.forName("net.blay09.mods.waystones.compat.RepurposedStructuresIntegration").getConstructor(new Class[0]).newInstance(new Object[0]);
            }
            catch (ClassNotFoundException | IllegalAccessException | InstantiationException | NoSuchMethodException | InvocationTargetException e) {
                logger.error("Failed to load Repurposed Structures integration", (Throwable)e);
            }
        }
    }
}

