# Changelog

All notable changes to the Waystone Button Injector mod will be documented in this file.

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
