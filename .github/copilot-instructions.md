# Waystone Button Injector - AI Agent Instructions

## Project Overview
Forge 1.20.1 mod that injects custom buttons into the Waystones mod's GUI screen. Uses client-side event listeners to detect the Waystones screen, dynamically adds buttons, and sends packets to the server to execute commands.

## Architecture

**3-Layer Structure:**
- `client/` - Client-side GUI event handlers (`ClientEvents.java`)
- `network/` - Client→Server packet communication (`WaystoneButtonPacket.java`, `WaystoneButtonHandler.java`, `Networking.java`)
- `config/` - ForgeConfigSpec configuration (`WaystoneConfig.java`)

**Data Flow:** User clicks button → Client sends packet → Server receives packet → Server executes command from config

## Critical Build Requirements

**⚠️ NEVER run local Gradle builds** - they fail due to persistent cache corruption. Always use GitHub Actions:
1. Push changes to `main` branch
2. Download artifact from https://github.com/smokydastona/WaystoneButtonInjector/actions
3. Use `gradle/actions/setup-gradle@v3` in workflows (not gradle-wrapper-validation)

**If local build absolutely required:**
```powershell
Remove-Item -Recurse -Force "C:\Users\<user>\.gradle\caches\forge_gradle"
.\gradlew.bat --no-daemon build
```

**Build.gradle MUST include:**
```gradle
repositories {
    mavenCentral()
    maven { url = 'https://maven.minecraftforge.net/' }
}
```

## Version Constraints
- **Minecraft:** 1.20.1 (exact, not range)
- **Forge:** 47.2.0 (loaderVersion "[47,)" allows 47.x)
- **Java:** 17 (toolchain enforced in build.gradle)
- **ForgeGradle:** `[6.0,6.2)` (6.0.x or 6.1.x)

## Event Bus Registration Pattern

**Critical: Two separate event buses**
1. **Mod Event Bus** - For mod lifecycle events (used for networking setup):
```java
IEventBus bus = FMLJavaModLoadingContext.get().getModEventBus();
```

2. **Forge Event Bus** - For game events (used for client GUI events):
```java
// In WaystoneInjectorMod.java
MinecraftForge.EVENT_BUS.register(new ClientEvents());

// In ClientEvents.java - alternative static registration
@Mod.EventBusSubscriber(value = Dist.CLIENT, modid = "waystoneinjector")
public class ClientEvents {
    @SubscribeEvent
    public static void onScreenInit(ScreenEvent.Init.Post event) { }
}
```

**Dist.CLIENT enforcement** - Client-only code MUST use:
```java
if (FMLEnvironment.dist.isClient()) {
    MinecraftForge.EVENT_BUS.register(new ClientEvents());
}
```

## Networking Pattern

**SimpleChannel Setup** (Networking.java):
```java
public static final SimpleChannel CHANNEL = NetworkRegistry.newSimpleChannel(
    new ResourceLocation("waystoneinjector", "main"),
    () -> PROTOCOL_VERSION,  // supplier
    PROTOCOL_VERSION::equals,  // client acceptance predicate
    PROTOCOL_VERSION::equals   // server acceptance predicate
);
```

**Packet Structure** (WaystoneButtonPacket.java):
- Contains single int `buttonId` (index into config arrays)
- `encode()` writes int to FriendlyByteBuf
- `decode()` reads int from FriendlyByteBuf
- `handle()` enqueues work on server thread, calls `WaystoneButtonHandler.handle()`
- **Direction:** `PLAY_TO_SERVER` only

**Button Index Mapping:**
```java
// Client sends button index
Networking.CHANNEL.sendToServer(new WaystoneButtonPacket(buttonIndex))

// Server retrieves command by same index
String command = WaystoneConfig.BUTTON_COMMANDS.get().get(packet.getButtonId());
```

## Screen Detection Pattern

**Why String Matching:**
Waystones classes aren't on compile classpath. Cannot use `instanceof` checks.

```java
String className = screen.getClass().getName();
if (!className.equals("net.blay09.mods.waystones.client.gui.screen.WaystoneSelectionScreen")) {
    return;
}
```

**Event:** `ScreenEvent.Init.Post` fires after screen is fully initialized but before render.

## Button Layout Algorithm

**Dynamic Horizontal Centering:**
```java
int numButtons = Math.min(labels.size(), commands.size());
int bw = 95;  // button width
int bh = 20;  // button height
int spacing = 5;
int totalWidth = (numButtons * bw) + ((numButtons - 1) * spacing);
int startX = (screen.width / 2) - (totalWidth / 2);
int bottomY = screen.height - 50;

for (int i = 0; i < numButtons; i++) {
    int x = startX + (i * (bw + spacing));
    Button button = Button.builder(Component.literal(label), callback)
        .bounds(x, bottomY, bw, bh).build();
    event.addListener(button);
}
```

## Config System Details

**Registration Timing:**
```java
public WaystoneInjectorMod() {
    // Config MUST be FIRST - before events, before networking
    WaystoneConfig.register();
    
    IEventBus bus = FMLJavaModLoadingContext.get().getModEventBus();
    
    if (FMLEnvironment.dist.isClient()) {
        MinecraftForge.EVENT_BUS.register(new ClientEvents());
    }
    
    Networking.register();
}
```

**Config Type:** `ModConfig.Type.CLIENT` (not COMMON)
- Generates `config/waystoneinjector-client.toml` on game launch
- COMMON configs only generate when world/server loads
- Explicit filename: `"waystoneinjector-client.toml"` in register() call

**Config Structure:**
```java
ForgeConfigSpec.Builder builder = new ForgeConfigSpec.Builder();
builder.comment("...").push("buttons");

BUTTON_LABELS = builder
    .comment("Button labels (use & for color codes, e.g., &aChaos Town)")
    .defineList("labels", Arrays.asList(), obj -> obj instanceof String);

BUTTON_COMMANDS = builder
    .comment("Commands to execute (without leading /)")
    .defineList("commands", Arrays.asList(), obj -> obj instanceof String);

builder.pop();
SPEC = builder.build();
```

**Security:** Default `Arrays.asList()` (empty) prevents exposing server IPs when distributing mod.

## Server Command Execution

**Server Thread Safety:**
```java
public static void handle(ServerPlayer player, WaystoneButtonPacket packet) {
    if (player == null) return;
    
    List<? extends String> commands = WaystoneConfig.BUTTON_COMMANDS.get();
    if (packet.getButtonId() >= commands.size()) return;
    
    String command = commands.get(packet.getButtonId());
    MinecraftServer server = player.getServer();
    if (server != null) {
        // Execute on server thread, not network thread
        server.execute(() -> {
            CommandSourceStack css = server.createCommandSourceStack();
            server.getCommands().performPrefixedCommand(css, command);
        });
    }
}
```

Commands run with server permissions, not player permissions. ServerRedirect mod required for `redirect` command.

## Common Gotchas

1. **VS Code Errors:** Red squiggles are normal - Minecraft/Forge classes not in IDE. Code compiles fine in Gradle.

2. **Config Not Generating:** 
   - Check mod shows in Mods menu
   - Config is CLIENT type - generates on game launch
   - Check logs for "waystoneinjector" mentions

3. **Buttons Not Appearing:**
   - Verify className string matches exactly
   - Check config has matching label/command array lengths
   - Ensure event is `ScreenEvent.Init.Post`, not `.Pre`

4. **Gradle "Could not find forge":**
   - Delete `C:\Users\<user>\.gradle\caches\forge_gradle`
   - Ensure `repositories` block in build.gradle
   - Use GitHub Actions instead

5. **Packet Not Received:**
   - Verify `NetworkDirection.PLAY_TO_SERVER` in registration
   - Check mod installed on BOTH client and server
   - Ensure `handle()` calls `ctx.get().setPacketHandled(true)`

## Debugging Workflow

**Client-Side:**
```java
System.out.println("Screen class: " + screen.getClass().getName());
System.out.println("Config labels: " + WaystoneConfig.BUTTON_LABELS.get());
```

**Server-Side:**
```java
System.out.println("Packet received: buttonId=" + packet.getButtonId());
System.out.println("Executing command: " + command);
```

Check `logs/latest.log` in Minecraft instance directory for output.

## File Structure
```
src/main/java/com/example/waystoneinjector/
├── WaystoneInjectorMod.java          # @Mod entry, config→events→networking order
├── client/ClientEvents.java          # @Mod.EventBusSubscriber, ScreenEvent.Init.Post
├── config/WaystoneConfig.java        # ForgeConfigSpec, CLIENT type, empty defaults
└── network/
    ├── Networking.java                # SimpleChannel, ResourceLocation, registerMessage
    ├── WaystoneButtonPacket.java      # encode/decode/handle pattern, buttonId int
    └── WaystoneButtonHandler.java     # Server thread execution, command lookup

src/main/resources/META-INF/
└── mods.toml                          # modId, forge [47,), minecraft [1.20.1]
```

## Testing
1. Build via GitHub Actions
2. Download jar from Actions artifacts
3. Install in `%appdata%\.minecraft\mods` or CurseForge instance mods folder
4. Launch Minecraft 1.20.1 with Forge 47.2.0+
5. Edit `config/waystoneinjector-client.toml` with server addresses
6. Requires Waystones mod + ServerRedirect mod (server-side) for `redirect` command
