# Waystone Button Injector

Forge 1.20.1 mod that injects two custom server redirect buttons into the Waystones GUI.

## Features
- Adds "Chaos Town" and "The Undergrown" buttons to every Waystone menu
- Sends packets to server to execute redirect commands
- Works alongside normal Waystone functionality
- No configuration needed - server addresses are hardcoded

## Build
1. Install Java 17
2. Run `gradlew.bat build`
3. Find jar in `build/libs/`

## Installation
1. Install on both client and server
2. Requires Waystones mod installed
3. Server needs `redirect` command (ServerRedirect or similar mod)

## Server Addresses
- Chaos Town: `chaostowntest.modrinth.gg`
- The Undergrown: `51.222.244.61:10020`

Edit `WaystoneButtonHandler.java` to change addresses.
