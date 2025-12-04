# Waystone Button Injector - AI Agent Instructions

## Project Overview
**Client-side only** Minecraft 1.20.1 Forge mod that:
1. Injects custom buttons into Waystones GUI for manual server transfers
2. Implements built-in Feverdream functionality (death/sleep redirects) client-side
3. Bypasses server mod compatibility checks via mixins
4. Declares compatibility with Feverdream to satisfy server requirements

**No server installation needed** - All features work client-side only.

## Critical Build Workflow

**⚠️ MANDATORY: NEVER EVER BUILD LOCALLY** 

**ALWAYS use GitHub Actions for compilation:**
1. Make code changes
2. `git add . && git commit -m "message"`
3. `git push` - This automatically triggers GitHub Actions build
4. Wait for Actions to complete at https://github.com/smokydastona/WaystoneButtonInjector/actions
5. Download compiled JAR from Actions artifacts
6. **Only when explicitly asked by user:** `git tag vX.X.X && git push origin vX.X.X` for releases

**DO NOT run these commands under ANY circumstances:**
- ❌ `gradlew build`
- ❌ `gradlew.bat build`
- ❌ `.\gradlew.bat build`
- ❌ Any gradle build command

**If workspace gets polluted with build artifacts:**
```powershell
Remove-Item -Recurse -Force .\build\
Remove-Item -Recurse -Force .\.gradle\
```

**Why:** Local builds create hundreds of MB of cache files, waste disk space, and pollute the workspace. GitHub Actions handles all compilation cleanly in the cloud.

## Architecture - 3 Core Systems

### 1. Client-Side Death/Sleep Detection
Replaces need for server-side Feverdream mod:
- `DeathSleepEvents.java` - Listens to `LivingDeathEvent` and `PlayerWakeUpEvent`
- Tracks death count per player in `HashMap<UUID, Integer>`
- Checks config for redirect mappings: `"death:source->destination"` or `"sleep:source->destination"`
- After threshold deaths, triggers `ClientEvents.connectToServer()`

### 2. Server Compatibility Bypass
Replaces need for MyServerIsCompatible mod:
- `MixinClientPacketListener.java` - Injects into `NetworkHooks.isVanillaConnection()`
- Forces return `true` to bypass "Incompatible FML Modded Server" error
- Allows connecting to any server regardless of mod list

### 3. Feverdream Compatibility Declaration
Tells servers we provide Feverdream:
- `mods.toml` includes `[[provides.waystoneinjector]] modId="feverdream"`
- Satisfies server requirements without installing actual Feverdream mod

## Config Structure

**Type:** `ModConfig.Type.CLIENT` (generates `config/waystoneinjector-client.toml`)

**Per-Server Redirect Mappings:**
```java
FEVERDREAM_REDIRECTS = builder.defineList("feverdreamRedirects", 
    Arrays.asList("death:192.168.1.1->192.168.1.2", "sleep:lobby->survival"),
    obj -> obj instanceof String);

FEVERDREAM_DEATH_COUNT = builder.defineInRange("feverdreamDeathCount", 3, 1, 10);
```

**Parsing Pattern:**
```java
public static String getDeathRedirectServer(String currentServer) {
    for (String mapping : FEVERDREAM_REDIRECTS.get()) {
        if (mapping.startsWith("death:")) {
            String[] parts = mapping.substring(6).split("->");
            if (parts[0].equals(currentServer)) return parts[1];
        }
    }
    return null;
}
```

## Mixin System

**Config:** `src/main/resources/waystoneinjector.mixins.json`
```json
{
  "required": true,
  "minVersion": "0.8",
  "package": "com.example.waystoneinjector.mixin",
  "refmap": "waystoneinjector.refmap.json",
  "compatibilityLevel": "JAVA_17",
  "client": ["MixinClientPacketListener"]
}
```

**Critical:** Must add to `build.gradle` jar manifest:
```gradle
'MixinConfigs': 'waystoneinjector.mixins.json'
```

**Mixin Pattern:**
```java
@Mixin(value = NetworkHooks.class, remap = false)
public class MixinClientPacketListener {
    @Inject(method = "isVanillaConnection", at = @At("HEAD"), 
            cancellable = true, remap = false)
    private static void forceVanillaConnection(CallbackInfoReturnable<Boolean> cir) {
        cir.setReturnValue(true);
    }
}
```

**Key attributes:**
- `remap = false` - Don't try to remap Forge classes
- `cancellable = true` - Allow returning early
- `@At("HEAD")` - Inject before first instruction

## Version System

**Auto-incrementing based on git commits:**
```gradle
def getVersionFromGit() {
    def proc = 'git rev-list --count HEAD'.execute()
    def commitCount = proc.text.trim()
    return "3.0.${commitCount}"
}
version = getVersionFromGit()
```

**Version 3.x Major Changes:**
- 3.0.0: Incorporated Feverdream client-side functionality
- 3.0.1: Added spawn teleport after death redirects  
- 3.0.2-3.0.5: Mixin fixes and compatibility improvements

## Death Redirect Flow with Spawn Teleport

1. Player dies → `DeathSleepEvents.onPlayerDeath()` triggered
2. Check if `getDeathRedirectServer(currentServer)` returns mapping
3. Increment death count, check against `FEVERDREAM_DEATH_COUNT`
4. Set `deathRedirectActive = true` flag
5. Wait 2 seconds (for death screen), then `ClientEvents.connectToServer(targetServer)`
6. `ResourcePackHandler.onPlayerLogin()` detects `deathRedirectActive == true`
7. Wait 1 second for world load
8. Execute `/spawn` command via `mc.player.connection.sendCommand("spawn")`
9. Clear `deathRedirectActive` flag

**Critical:** Sleep redirects do NOT trigger spawn teleport (flag remains false).

## Event Registration Pattern

**Mod entry point** - Order matters:
```java
public WaystoneInjectorMod() {
    if (FMLEnvironment.dist.isClient()) {
        WaystoneConfig.register();  // FIRST - config must exist
        
        // Register event handlers
        MinecraftForge.EVENT_BUS.register(new ClientEvents());
        MinecraftForge.EVENT_BUS.register(DeathSleepEvents.class);
        MinecraftForge.EVENT_BUS.register(ResourcePackHandler.class);
        
        FeverdreamNetworking.register();  // LAST - networking
    }
}
```

**Two event buses:**
1. `MinecraftForge.EVENT_BUS` - Game events (death, sleep, login)
2. `FMLJavaModLoadingContext.get().getModEventBus()` - Mod lifecycle events

## Common Patterns

**Server Connection:**
```java
public static void connectToServer(String address) {
    Minecraft mc = Minecraft.getInstance();
    ServerAddress parsed = ServerAddress.parseString(address);
    ServerData serverData = new ServerData("", parsed.getHost() + ":" + parsed.getPort(), false);
    
    mc.execute(() -> {
        ConnectScreen.startConnecting(mc.screen, mc, parsed, serverData, false);
    });
}
```

**Thread Safety for Server Commands:**
```java
mc.execute(() -> {
    // Execute on main Minecraft thread, not event thread
    if (mc.player != null && mc.getConnection() != null) {
        mc.player.connection.sendCommand("spawn");
    }
});
```

## File Structure
```
src/main/java/com/example/waystoneinjector/
├── WaystoneInjectorMod.java           # @Mod entry, client-side only
├── client/
│   ├── ClientEvents.java              # Waystone GUI button injection
│   ├── DeathSleepEvents.java          # Death/sleep detection, redirect logic
│   └── ResourcePackHandler.java      # Login event, spawn teleport
├── config/WaystoneConfig.java         # CLIENT config, per-server mappings
└── mixin/
    └── MixinClientPacketListener.java # Server compatibility bypass

src/main/resources/
├── waystoneinjector.mixins.json       # Mixin configuration
└── META-INF/mods.toml                 # Declares provides feverdream
```

## Debugging

**Check logs for:**
- `[WaystoneInjector] Player death detected`
- `[WaystoneInjector] Death threshold reached - triggering redirect`
- `[WaystoneInjector] Death redirect completed - will teleport to spawn`
- `[WaystoneInjector] Forcing vanilla connection - bypassing mod compatibility check`

**Common issues:**
1. **Death redirect not triggering** - Check config has `death:` prefix on mapping
2. **Spawn teleport not working** - Verify `deathRedirectActive` flag is set before connection
3. **Server rejects connection** - Mixin may not be loading (check manifest includes MixinConfigs)
4. **Config not found** - Ensure CLIENT type, not COMMON (only generates on game launch)

## Testing Workflow
1. Push changes to GitHub
2. Wait for Actions build to complete
3. Download JAR from artifacts
4. Copy to `.minecraft/mods/` folder
5. Test in-game, check `logs/latest.log` for debug output
6. **Only after successful testing** - Tag and push release version
