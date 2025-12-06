/*
 * Decompiled with CFR 0.152.
 */
package net.blay09.mods.balm.api.network;

import java.util.Map;
import net.blay09.mods.balm.api.network.NetworkVersions;

public record ServerboundModListMessage(Map<String, NetworkVersions> modList) {
}

