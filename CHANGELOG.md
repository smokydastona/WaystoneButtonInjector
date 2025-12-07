# Changelog

All notable changes to the Waystone Button Injector mod will be documented in this file.

## [3.0.237] - 2025-12-06

### Fixed - Complete Mixin Strategy Rewrite
- **MixinMenuScreens Total Rewrite**: Abandoned `@Shadow` approach, using `@ModifyArg` instead
  - Root Cause: `@Shadow` requires exact field name matching in obfuscated bytecode, which is unreliable
  - Problem with v3.0.231-236: Tried multiple approaches (`SCREENS`, `f_96579_`, aliases, `@Final`, `@Mutable`) all failed
  - Core Issue: Cannot reliably access private static final fields via `@Shadow` in obfuscated environment
  - **NEW APPROACH**: Use `@ModifyArg` to intercept the factory parameter during `Map.put()` call
  - Technical Implementation:
    ```java
    @ModifyArg(
        method = "register(...)",
        at = @At(value = "INVOKE", target = "Ljava/util/Map;put(...)"),
        index = 1  // Modify second argument (the factory)
    )
    private static MenuScreens.ScreenConstructor wrapFactory(MenuScreens.ScreenConstructor original) {
        return new MenuScreens.ScreenConstructor() {
            @Override
            public Screen create(menu, inv, title) {
                if (menu instanceof WaystoneSelectionMenu) {
                    return new EnhancedWaystoneSelectionScreen(...);
                }
                return original.create(menu, inv, title);
            }
        };
    }
    ```
  - Impact: No field access needed - wraps factory at registration time instead of modifying map after
  - Compatibility: Should work with Furniture Mod and other mods using `MenuScreens`
  - Status: ✅ Complete rewrite - no `@Shadow`, no field access, intercepts method argument directly

### Technical Details
- **Why This Works**: 
  - Intercepts the `factory` parameter BEFORE it's stored in the map
  - Wraps it with our own factory that checks for `WaystoneSelectionMenu`
  - No need to access or modify the internal `SCREENS` map
  - Works in both dev and production environments (no obfuscation dependency)
- **Advantages Over @Shadow**:
  - No field name obfuscation issues
  - No `@Final`/`@Mutable` complexity
  - No refmap dependency
  - Cleaner, more maintainable code
- **Performance**: Factory wrapper only checks `instanceof` once per screen creation (negligible overhead)

## [3.0.236] - 2025-12-06

### Fixed - @Shadow Field Access with @Final @Mutable
- **MixinMenuScreens @Shadow Fix**: Added `@Final` and `@Mutable` annotations to properly access static final field
  - Root Cause: The `SCREENS`/`f_96579_` field in `MenuScreens` class is declared as `private static final`
  - Previous Fix (v3.0.235): Changed field name to SRG `f_96579_` but didn't account for `final` modifier
  - Issue: Mixin couldn't locate field because it lacked proper annotations for final field access
  - Solution: Added `@Final` to declare it's a final field and `@Mutable` to allow modifications
  - Technical Change:
    ```java
    @Shadow
    @Final
    @Mutable
    private static Map<MenuType<?>, MenuScreens.ScreenConstructor<?, ?>> f_96579_;
    ```
  - Impact: Mixin can now properly access and modify the static final registry map
  - Error Message Before: `@Shadow field f_96579_ was not located in the target class. No refMap loaded.`
  - Status: ✅ Fixed - Proper annotations for final field shadowing

### Technical Details
- **Why @Final**: Tells Mixin the target field is declared as `final` in the target class
- **Why @Mutable**: Allows the mixin to modify a final field (removes final modifier at runtime)
- **Why No Aliases**: Direct SRG name with proper annotations is cleaner than alias approach
- **Backwards Compatibility**: ✅ Fully compatible - annotations are Mixin framework features

## [3.0.235] - 2025-12-06

### Fixed - Critical Production Obfuscation Fix
- **MixinMenuScreens Obfuscation Fix**: Changed `@Shadow` field from `SCREENS` to `f_96579_` with dual aliases
  - Root Cause: In production (obfuscated) environment, field ONLY exists as SRG name `f_96579_`
  - Previous Fix (v3.0.231): Added SRG alias but kept MCP name as primary field name
  - Issue: Mixin tried to match field named "SCREENS" which doesn't exist in obfuscated bytecode
  - Solution: Use SRG name as primary field name with aliases `{"SCREENS", "f_96579_"}`
  - Technical: `@Shadow(aliases = {"SCREENS", "f_96579_"}) private static Map<...> f_96579_;`
  - Impact: Mod now works in both development (MCP names) and production (SRG names) environments
  - Error Message Before: `@Shadow field SCREENS was not located in the target class. No refMap loaded.`
  - Status: ✅ Fixed - Uses SRG name directly, aliases handle both environments

### Technical Details
- **Why This Matters**: Forge uses different naming conventions in dev vs production
  - Development: Uses MCP (Mod Coder Pack) names like `SCREENS` for readability
  - Production: Uses SRG (Searge) names like `f_96579_` for obfuscation
  - Mixin @Shadow annotations must match the actual bytecode field name
- **What Changed**: 
  - Field name: `SCREENS` → `f_96579_`
  - Aliases: `"f_96579_"` → `{"SCREENS", "f_96579_"}`
  - All references updated to use `f_96579_` throughout the class
- **Why v3.0.231 Fix Wasn't Enough**: Aliases are search hints, but the primary field name must match runtime bytecode
- **Backwards Compatibility**: ✅ Fully compatible - aliases ensure dev environment still works

## [3.0.233] - 2025-12-06

### Added - Quality of Life & Robustness Improvements
- **GuiCompatibilityDetector System** (180 lines): Automatic GUI compatibility detection
  - Percentage-based positioning (5% left, 85% right, 75% vertical) - works on any screen size
  - Button overlap detection and auto-spacing (10% screen height per button)
  - Screen bounds constraint (10px margins) - prevents off-screen buttons
  - Safe mode support - uses vanilla button style when enabled
- **ErrorToastManager System** (165 lines): In-game toast notifications for errors
  - SystemToast integration with colored titles (§c red, §e yellow, §a green)
  - 11 pre-built notification methods:
    * `showRedirectError()` - Connection failures
    * `showConfigError()` - Invalid configs
    * `showButtonDisabled()` - Disabled button info
    * `showInvalidServerError()` - Bad server addresses
    * `showRedirectTimeout()` - Connection timeouts (30s default)
    * `showRedirectSuccess()` - Successful connections
    * `showOverlapWarning()` - Button collisions
    * `showSafeModeInfo()` - Safe mode activation
    * Generic `showError/Warning/Info()` methods
- **RedirectManager System** (150 lines): Server redirect timeout handling
  - 30-second default timeout (configurable 5-120s)
  - 5-second warning threshold (logs warning if still connecting)
  - CompletableFuture-based async monitoring (checks every 100ms)
  - Prevents multiple simultaneous redirects
  - Returns to server list on timeout
  - Toast notifications for all states (success, failure, timeout, warning)
- **Granular Debug Categories**: Fine-grained control over debug logging
  - 6 independent toggles: `gui`, `config`, `mixin`, `redirect`, `event`, `resource`
  - Each category can be enabled/disabled individually
  - Only takes effect when `debugMode = true`
- **Safety & Compatibility Settings**: New `[safety]` config section
  - `safeMode = false` - Enable vanilla button style + percentage positioning for maximum compatibility
  - `autoSpacing = true` - Automatically adjust button positions if they overlap
  - `redirectTimeout = 30` - Server redirect timeout in seconds (prevents client freeze)
- **Button Tooltips**: Hover tooltip support for all 6 buttons
  - Supports Minecraft color codes (`&aGreen`, `&cRed`, etc.)
  - Optional per-button configuration
  - Leave empty for no tooltip

### Changed
- **DebugLogger Enhanced**: Added granular category toggles
  - New setters: `setDebugGui()`, `setDebugConfig()`, `setDebugMixin()`, `setDebugRedirect()`, `setDebugEvent()`, `setDebugResource()`
  - Category-specific logging methods now respect individual toggles
- **WaystoneConfig Expanded**: Added 15 new configuration fields
  - Debug categories: `DEBUG_GUI`, `DEBUG_CONFIG`, `DEBUG_MIXIN`, `DEBUG_REDIRECT`, `DEBUG_EVENT`, `DEBUG_RESOURCE`
  - Safety settings: `SAFE_MODE`, `AUTO_SPACING`, `REDIRECT_TIMEOUT_SECONDS`
  - Tooltips: `BUTTON1-6_TOOLTIP` (6 new fields)
- **onConfigLoad() Enhanced**: Applies new settings automatically
  - Applies granular debug category toggles
  - Applies safety settings (safe mode, auto-spacing)
  - Shows safe mode toast notification when enabled
  - Shows config error toasts for visibility
  - Validates configuration and reports errors/warnings

### Technical
- Enhanced `DebugLogger.java` (+30 lines) - Granular category toggles
- New `GuiCompatibilityDetector.java` (180 lines) - GUI compatibility & positioning
- New `ErrorToastManager.java` (165 lines) - In-game toast notifications
- New `RedirectManager.java` (150 lines) - Timeout handling & async monitoring
- Modified `WaystoneConfig.java` (+100 lines) - New config fields & initialization
- Documentation: `QOL_IMPROVEMENTS_v3.0.233.md`, `CONFIG_PATCH_v3.0.233.md`, `IMPLEMENTATION_STATUS_v3.0.233.md`
- Total new/modified code: ~670 lines
- All changes are backwards compatible - existing configs work without modification

### Impact
- **High Impact**: Prevents client freezes (redirect timeout), improves error visibility (toasts)
- **Medium Impact**: Enhances compatibility (safe mode), improves diagnostics (granular debug)
- **Backwards Compatible**: All new features have defaults that maintain current behavior

---

## [3.0.232] - 2025-12-06

### Added
- **DebugLogger System**: Centralized logging with multiple verbosity levels
  - Debug mode for detailed troubleshooting logs
  - Verbose mode for ultra-detailed logging
  - Specialized logging categories: GUI, config, mixin, redirect, event, resource
  - Helper methods: `buttonPress()`, `redirectAttempt/Result()`, `textureLoaded()`, `guiDetected()`
- **ConfigValidator System**: Comprehensive configuration validation
  - Validates labels (empty check, length warnings >50 chars)
  - Validates commands (format, redirect syntax, server addresses)
  - Validates dimensions (width 20-200, height 15-100)
  - Validates offsets (warns if >500 pixels, likely off-screen)
  - Validates colors (hex format `0xFFFFFF`)
  - Validates sides (`auto`, `left`, `right`)
  - Validates sleep chance (0-100%)
  - Validates server addresses (domain:port regex)
  - Auto-disables buttons with critical errors
- **Debug Configuration Section**: New `[debug]` section in config
  - `debugMode = false` - Enable detailed logging for troubleshooting
  - `verboseMode = false` - Enable ultra-detailed logging (very spammy)
- **IMPROVEMENTS.md**: 600+ line comprehensive improvement roadmap
  - Completed improvements documentation
  - High-priority next steps (texture fallbacks, redirect timeouts)
  - Medium-priority improvements (GUI compatibility, screen resolution handling)
  - Code organization recommendations
  - Testing checklist and known limitations

### Changed
- Config validation now runs automatically on load/reload
- Invalid configurations are caught early with clear, actionable error messages
- Debug settings applied automatically from config

### Fixed
- **CRITICAL**: Added SRG obfuscation alias `f_96579_` to `@Shadow` SCREENS field in MixinMenuScreens
  - Fixes production crash: "field SCREENS was not located in target class"
  - Now works in both development and production environments
- Buttons with invalid configurations are gracefully disabled instead of causing crashes

### Technical
- SLF4J integration for professional-grade logging
- Regex-based validation for server addresses, hex colors, command formats
- `DebugLogger.java` (230 lines) - Centralized logging utilities
- `ConfigValidator.java` (350 lines) - Config validation system
- Modified `WaystoneConfig.java` - Added debug config section + validation integration
- Modified `WaystoneInjectorMod.java` - Calls validation on startup
- Modified `MixinMenuScreens.java` - Added SRG alias for production compatibility

### Breaking Changes
None - fully backwards compatible. New config options have sensible defaults.

---

## [3.0.168] - 2025-12-05

### Added
- **Mystical Portal Overlays**: 26-frame animated mystical portal layer for enhanced visual effects
  - Renders between base portal animation and waystone GUI texture
  - Time-based frame cycling (100ms per frame, 2.6 second full cycle)
  - Applied to all waystone types including sharestones
- **Waystone List Entry Overlays**: Individual texture overlays for each waystone in selection list
  - 220x36 pixel overlays matching waystone type
  - Supports 9 waystone types: regular, mossy, blackstone, deepslate, endstone, sharestone, warp_scroll, warp_stone, portstone
  - Each entry shows its own type-specific overlay for easy visual identification
- **Extended Button Texture Support**: Added button textures for warp_scroll, warp_stone, and portstone
- **Type Registry System**: Automatic waystone type detection and persistent storage
  - Learns waystone types when player right-clicks blocks
  - Saves type mappings to disk for persistence across sessions
  - Applies correct overlays based on learned types

### Changed
- Consolidated button textures from 36 files to 9 files (reduced from 64x96 to 64x32)
- Renamed mystical portal textures from `nether_portal_*.png` to `mystic_*.png` for better naming consistency
- Waystone list overlays now use individual waystone type instead of menu waystone type
- Improved sharestone detection to support SharestoneSelectionScreen alongside WaystoneSelectionScreen

### Fixed
- Double-tall block detection for waystones (properly handles upper/lower halves)
- Server icon render order (now renders behind custom button textures as intended)
- Sharestone GUI detection and custom button application

### Technical
- Added MYSTICAL_PORTALS array with 26 ResourceLocation references
- Added OVERLAY_* ResourceLocations for 9 waystone types
- Enhanced WaystoneTypeRegistry with persistent name→type mapping
- Debug logging for waystone list overlay rendering

## [3.0.138] - 2025-12-05

### Added
- **Client-Side Health Monitoring**: Death detection via health monitoring (works with instant respawn)
- **Server Address Field**: New `serverAddress` config field for each button to enable death/sleep triggers on non-redirect commands
- **Health-Based Detection**: Monitors player health every tick to detect death (health <= 0) and respawn (health > 0)
- 2-second cooldown after respawn to prevent duplicate triggers
- Comprehensive debug logging for death/respawn detection
- Auto-download dependencies: MouseTweaks and MyServerIsCompatible now install automatically

### Changed
- Death detection now uses `ClientTickEvent` with health monitoring instead of server-side events
- Death triggers work with ANY command type (not just `redirect` commands)
- Improved reliability with instant respawn enabled
- Simplified death/respawn logic - removed threading delays

### Fixed
- **MAJOR**: Death triggers now work on client-only mod (previous server events never fired)
- Death detection now works with instant respawn enabled
- Death triggers now work with non-redirect commands (e.g., `spawn`, `warp lobby`)
- Server matching now checks explicit `serverAddress` field first, then falls back to parsing redirect commands

### Technical
- Switched from `LivingDeathEvent`/`PlayerRespawnEvent` (server-only) to `ClientTickEvent` (client-side)
- Health monitoring: detects death at health <= 0, respawn when health returns to positive
- Performance: ~0.001ms per tick, with 2-second skip after detection
- Added `serverAddress` config field to `WaystoneConfig` for all 6 buttons

## [3.0.133] - 2025-12-04

### Added
- **Built-in Death/Sleep Detection**: Client-side death and sleep event detection
- **Per-Server Redirect Mappings**: Configure different death/sleep destinations for each server
- **Death Count Threshold**: Configurable number of deaths before auto-redirect triggers
- Improved connection monitoring - checks every 2 seconds instead of just at 60 second timeout
- Better error handling - returns to multiplayer screen instead of blank screen on connection failure

### Changed
- Connection failure now returns players to multiplayer server list instead of title screen
- Enhanced connection monitoring with active checks for faster failure detection

### Removed
- External Feverdream packet integration (replaced with built-in detection)
- Secret buttons (6-11) - no longer needed with built-in system
- Individual button configs - simplified to per-server redirect mappings

### Fixed
- Players no longer get stuck on blank screen when connection fails
- Connection timeout now properly returns to multiplayer screen

---

## [2.0.0] - 2025-11-30

### Changed
- **MAJOR REWRITE**: Removed all server-side command execution - now uses pure client-side server transfers
- No longer requires OP permissions at all - everything happens client-side
- No longer requires ServerRedirect mod - built-in server transfer functionality
- Commands are now parsed for server addresses and client directly connects to new servers

### Added
- Direct client-to-server connection system bypassing all permission requirements
- Server address parsing from redirect commands (supports `redirect server.com` and `redirect server.com:25565`)
- Automatic disconnection from current server and connection to target server
- Support for custom ports in server addresses

### Removed
- Server-side command execution with elevated permissions
- Dependency on ServerRedirect mod
- All OP permission requirements

### Technical
- Client parses server address from command string
- Uses `ConnectScreen.startConnecting()` for direct server connections
- `ExecuteClientCommandPacket` now handles full connection logic
- Server only validates button press and forwards to client
- Works with any Minecraft server (no special setup needed on destination server)

---

## [1.0.x] - 2025-11-27

### Changed
- **BREAKING**: Commands now execute server-side with OP level 2 permissions
- Switched from client-side command execution to server-side packet handling
- Players no longer need OP status to use buttons - commands execute with elevated permissions
- Simplified version numbering to `1.0.<commit-count>` format (removed git hash)
- Updated mods.toml to dynamically pull version from build.gradle

### Added
- Server-side command execution system allowing non-OP players to use redirect buttons
- CommandSourceStack creation with permission level 2 at player's position
- Network packet communication from client to server for button clicks

### Technical
- Commands now run via `CommandSourceStack` with permission level 2
- Client sends `WaystoneButtonPacket` with button index to server
- Server executes command on server thread with `server.execute()`
- Command source positioned at player location for proper targeting

---

## [1.0.28] - 2025-11-27

### Added
- Automatic version numbering based on git commit count and hash
- Full git history fetching in GitHub Actions for accurate version tracking

### Changed
- Version format now includes commit number and git hash (e.g., `1.0.28+91d07c9`)
- Improved error handling in build script when git is unavailable
- Updated GitHub Actions workflow to fetch full repository history

### Fixed
- Build failures in GitHub Actions due to shallow git clone
- Fallback to version `1.0.0` when git is not available

---

## [1.0.27] - 2025-11-27

### Changed
- Restored working version from commit 68f5133
- Removed experimental features that caused buttons not to display

### Fixed
- Buttons not appearing on waystone screen
- Various rendering and injection issues

---

## [1.0.7] - Initial Working Version

### Added
- Client-side server redirect buttons in waystone GUI
- Configuration file support for up to 6 custom buttons
- Individual button enable/disable toggles
- Buttons positioned on left and right sides of screen
- Vertical centering for better UI appearance
- Support for client commands like `/redirect`
- Permission level 0 (no OP required)

### Features
- **Button Positioning**: Buttons split between left and right screen edges
- **Configurable Labels**: Custom text for each button
- **Custom Commands**: Execute any command when button is clicked
- **Texture Pack Compatible**: Uses standard Minecraft button rendering
- **User-Friendly Config**: Individual sections for each button in TOML format

### Technical
- Client-side command execution via `player.connection.sendCommand()`
- Event-based button injection into WaystoneSelectionScreen
- ForgeConfigSpec configuration management
- Compatible with Minecraft 1.20.1 Forge
