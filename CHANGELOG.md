# Changelog

All notable changes to the Waystone Button Injector mod will be documented in this file.

## [Unreleased] - 2025-12-03

### Added
- **Secret Buttons (IDs 6-11)**: Hidden buttons that mirror the 6 visible buttons for Feverdream integration
- Feverdream mod can now send button IDs (6-11) to trigger configured buttons programmatically
- Secret button 6 executes the same command as visible button 1, button 7 = button 2, etc.
- Improved connection monitoring - checks every 2 seconds instead of just at 60 second timeout
- Better error handling - returns to multiplayer screen instead of blank screen on connection failure

### Changed
- Connection failure now returns players to multiplayer server list instead of title screen
- FeverdreamHandler now supports both server names and button IDs (6-11)
- Enhanced connection monitoring with active checks for faster failure detection

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
