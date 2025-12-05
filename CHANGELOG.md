# Changelog

All notable changes to the Waystone Button Injector mod will be documented in this file.

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
