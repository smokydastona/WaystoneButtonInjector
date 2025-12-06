/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  com.google.common.collect.Lists
 *  net.blay09.mods.balm.api.config.BalmConfigData
 *  net.blay09.mods.balm.api.config.Comment
 *  net.blay09.mods.balm.api.config.Config
 *  net.blay09.mods.balm.api.config.ExpectedType
 *  net.blay09.mods.balm.api.config.Synced
 */
package net.blay09.mods.waystones.config;

import com.google.common.collect.Lists;
import java.util.ArrayList;
import java.util.List;
import net.blay09.mods.balm.api.config.BalmConfigData;
import net.blay09.mods.balm.api.config.Comment;
import net.blay09.mods.balm.api.config.Config;
import net.blay09.mods.balm.api.config.ExpectedType;
import net.blay09.mods.balm.api.config.Synced;
import net.blay09.mods.waystones.config.DimensionalWarp;
import net.blay09.mods.waystones.config.InventoryButtonMode;
import net.blay09.mods.waystones.config.WorldGenStyle;
import net.blay09.mods.waystones.worldgen.namegen.NameGenerationMode;

@Config(value="waystones")
public class WaystonesConfigData
implements BalmConfigData {
    public XpCost xpCost = new XpCost();
    public Restrictions restrictions = new Restrictions();
    public Cooldowns cooldowns = new Cooldowns();
    public InventoryButton inventoryButton = new InventoryButton();
    public WorldGen worldGen = new WorldGen();
    public Client client = new Client();
    public Compatibility compatibility = new Compatibility();

    public InventoryButtonMode getInventoryButtonMode() {
        return new InventoryButtonMode(this.inventoryButton.inventoryButton);
    }

    public static class XpCost {
        @Synced
        @Comment(value="Set to true if experience cost should be inverted, meaning the shorter the distance, the more expensive. Can be used to encourage other methods for short-distance travel.")
        public boolean inverseXpCost = false;
        @Synced
        @Comment(value="The amount of blocks per xp level requirement. If set to 500, the base xp cost for travelling 1000 blocks will be 2 levels.")
        public int blocksPerXpLevel = 1000;
        @Synced
        @Comment(value="The minimum base xp cost (may be subceeded by multipliers defined below)")
        public double minimumBaseXpCost = 0.0;
        @Synced
        @Comment(value="The maximum base xp cost (may be exceeded by multipliers defined below), set to 0 to disable all distance-based XP costs")
        public double maximumBaseXpCost = 3.0;
        @Synced
        @Comment(value="How much xp is needed per leashed animal to travel with you")
        public int xpCostPerLeashed = 0;
        @Synced
        @Comment(value="The base xp level cost when travelling between dimensions. Ignores block distance.")
        public int dimensionalWarpXpCost = 3;
        @Synced
        @Comment(value="The multiplier applied to the base xp cost when teleporting to a global waystone through any method.")
        public double globalWaystoneXpCostMultiplier = 0.0;
        @Synced
        @Comment(value="The multiplier applied to the base xp cost when teleporting using a Warp Stone item (not the Waystone block, Konstantin)")
        public double warpStoneXpCostMultiplier = 0.0;
        @Synced
        @Comment(value="The multiplier applied to the base xp cost when teleporting from one waystone to another.")
        public double waystoneXpCostMultiplier = 0.0;
        @Synced
        @Comment(value="The multiplier applied to the base xp cost when teleporting from one sharestone to another.")
        public double sharestoneXpCostMultiplier = 0.0;
        @Synced
        @Comment(value="The multiplier applied to the base xp cost when teleporting from a portstone.")
        public double portstoneXpCostMultiplier = 0.0;
        @Synced
        @Comment(value="The multiplier applied to the base xp cost when teleporting from one warp plate to another.")
        public double warpPlateXpCostMultiplier = 0.0;
        @Synced
        @Comment(value="The multiplier applied to the base xp cost when teleporting via the inventory button.")
        public double inventoryButtonXpCostMultiplier = 0.0;
    }

    public static class Restrictions {
        @Synced
        @Comment(value="If enabled, only creative players can place, edit or break waystones. This does NOT disable the crafting recipe.")
        public boolean restrictToCreative = false;
        @Synced
        @Comment(value="If enabled, only the owner of a waystone (the one who placed it) can rename it.")
        public boolean restrictRenameToOwner = false;
        @Synced
        @Comment(value="If enabled, waystones generated in worldgen are unbreakable.")
        public boolean generatedWaystonesUnbreakable = false;
        @Synced
        @Comment(value="If enabled, leashed mobs will be teleported with you")
        public boolean transportLeashed = true;
        @Synced
        @Comment(value="Whether to take leashed mobs with you when teleporting between dimensions")
        public boolean transportLeashedDimensional = true;
        @Comment(value="List of leashed mobs that cannot be taken with you when teleporting")
        @ExpectedType(value=String.class)
        public List<String> leashedDenyList = Lists.newArrayList((Object[])new String[]{"minecraft:wither"});
        @Synced
        @Comment(value="Set to 'ALLOW' to allow dimensional warp in general. Set to 'GLOBAL_ONLY' to restrict dimensional warp to global waystones. Set to 'DENY' to disallow all dimensional warps.")
        public DimensionalWarp dimensionalWarp = DimensionalWarp.ALLOW;
        @Comment(value="List of dimensions that players are allowed to warp cross-dimension from and to. If left empty, all dimensions except those in dimensionalWarpDenyList are allowed.")
        @ExpectedType(value=String.class)
        public List<String> dimensionalWarpAllowList = new ArrayList<String>();
        @Comment(value="List of dimensions that players are not allowed to warp cross-dimension from and to. Only used if dimensionalWarpAllowList is empty.")
        @ExpectedType(value=String.class)
        public List<String> dimensionalWarpDenyList = new ArrayList<String>();
        @Comment(value="Set to true if players should be able to teleport between waystones by simply right-clicking a waystone.")
        public boolean allowWaystoneToWaystoneTeleport = true;
        @Synced
        @Comment(value="Set to false to allow non-creative players to make waystones globally activated for all players.")
        public boolean globalWaystoneSetupRequiresCreativeMode = true;
    }

    public static class Cooldowns {
        @Synced
        @Comment(value="The multiplier applied to the cooldown when teleporting to a global waystone via inventory button or warp stone.")
        public double globalWaystoneCooldownMultiplier = 1.0;
        @Synced
        @Comment(value="The cooldown between usages of the warp stone in seconds. This is bound to the player, not the item, so multiple warp stones share the same cooldown.")
        public int warpStoneCooldown = 30;
        @Synced
        @Comment(value="The time in ticks that it takes to use a warp stone. This is the charge-up time when holding right-click.")
        public int warpStoneUseTime = 32;
        @Synced
        @Comment(value="The time in ticks that it takes to use a warp plate. This is the time the player has to stand on top for.")
        public int warpPlateUseTime = 20;
        @Synced
        @Comment(value="The time in ticks it takes to use a scroll. This is the charge-up time when holding right-click.")
        public int scrollUseTime = 32;
        @Synced
        @Comment(value="The cooldown between usages of the inventory button in seconds.")
        public int inventoryButtonCooldown = 300;
    }

    public static class InventoryButton {
        @Synced
        @Comment(value="Set to 'NONE' for no inventory button. Set to 'NEAREST' for an inventory button that teleports to the nearest waystone. Set to 'ANY' for an inventory button that opens the waystone selection menu. Set to a waystone name for an inventory button that teleports to a specifically named waystone.")
        public String inventoryButton = "";
        @Comment(value="The x position of the warp button in the inventory.")
        public int warpButtonX = 58;
        @Comment(value="The y position of the warp button in the inventory.")
        public int warpButtonY = 60;
        @Comment(value="The y position of the warp button in the creative menu.")
        public int creativeWarpButtonX = 88;
        @Comment(value="The y position of the warp button in the creative menu.")
        public int creativeWarpButtonY = 33;
    }

    public static class WorldGen {
        @Comment(value="Set to 'DEFAULT' to only generate the normally textured waystones. Set to 'MOSSY' or 'SANDY' to generate all as that variant. Set to 'BIOME' to make the style depend on the biome it is generated in.")
        public WorldGenStyle worldGenStyle = WorldGenStyle.BIOME;
        @Comment(value="Approximate chunk distance between waystones generated freely in world generation. Set to 0 to disable generation.")
        public int frequency = 25;
        @Comment(value="List of dimensions that waystones are allowed to spawn in through world gen. If left empty, all dimensions except those in worldGenDimensionDenyList are used.")
        @ExpectedType(value=String.class)
        public List<String> dimensionAllowList = Lists.newArrayList((Object[])new String[]{"minecraft:overworld", "minecraft:the_nether", "minecraft:the_end"});
        @Comment(value="List of dimensions that waystones are not allowed to spawn in through world gen. Only used if worldGenDimensionAllowList is empty.")
        @ExpectedType(value=String.class)
        public List<String> dimensionDenyList = new ArrayList<String>();
        @Comment(value="Set to 'PRESET_FIRST' to first use names from the custom names list. Set to 'PRESET_ONLY' to use only those custom names. Set to 'MIXED' to have some waystones use custom names, and others random names.")
        public NameGenerationMode nameGenerationMode = NameGenerationMode.PRESET_FIRST;
        @Comment(value="These names will be used for the PRESET name generation mode. See the nameGenerationMode option for more info.")
        @ExpectedType(value=String.class)
        public List<String> customWaystoneNames = new ArrayList<String>();
        @Comment(value="Set to true if waystones should be added to the generation of villages. Some villages may still spawn without a waystone.")
        public boolean spawnInVillages = true;
        @Comment(value="Ensures that pretty much every village will have a waystone, by spawning it as early as possible. In addition, this means waystones will generally be located in the center of the village.")
        public boolean forceSpawnInVillages = false;
    }

    public static class Client {
        @Comment(value="If enabled, the text overlay on waystones will no longer always render at full brightness.")
        public boolean disableTextGlow = false;
    }

    public static class Compatibility {
        @Comment(value="If enabled, JourneyMap waypoints will be created for each activated waystone.")
        public boolean displayWaystonesOnJourneyMap = true;
        @Comment(value="If enabled, JourneyMap waypoints will only be created if the mod 'JourneyMap Integration' is not installed")
        public boolean preferJourneyMapIntegration = true;
    }
}

