/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  net.minecraft.resources.ResourceLocation
 *  net.minecraftforge.network.NetworkRegistry
 *  net.minecraftforge.network.simple.SimpleChannel
 */
package net.blay09.mods.balm.forge.network;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.function.Predicate;
import net.minecraft.resources.ResourceLocation;
import net.minecraftforge.network.NetworkRegistry;
import net.minecraftforge.network.simple.SimpleChannel;

public class NetworkChannels {
    private static final String version = "1.0";
    private static final Map<String, SimpleChannel> channels = new ConcurrentHashMap<String, SimpleChannel>();
    private static final Map<String, Predicate<String>> acceptedClientVersions = new ConcurrentHashMap<String, Predicate<String>>();
    private static final Map<String, Predicate<String>> acceptedServerVersions = new ConcurrentHashMap<String, Predicate<String>>();

    public static SimpleChannel get(String modId) {
        return channels.computeIfAbsent(modId, key -> {
            ResourceLocation channelName = new ResourceLocation(key, "network");
            return NetworkRegistry.newSimpleChannel((ResourceLocation)channelName, () -> version, it -> acceptedClientVersions.getOrDefault(modId, NetworkChannels::defaultVersionCheck).test((String)it), it -> acceptedServerVersions.getOrDefault(modId, NetworkChannels::defaultVersionCheck).test((String)it));
        });
    }

    public static void allowClientOnly(String modId) {
        acceptedClientVersions.put(modId, it -> true);
    }

    public static void allowServerOnly(String modId) {
        acceptedServerVersions.put(modId, it -> true);
    }

    private static boolean defaultVersionCheck(String it) {
        return it.equals(version);
    }

    public static void defineNetworkVersion(String modId, String version) {
    }
}

