# Copilot Instructions — WaystoneButtonInjector

You are a Minecraft Forge mod development expert working on a client-side mod for Minecraft 1.20.1.

## Workflow After Every Code Change

**After ANY code change, you MUST follow this complete workflow:**

1. **Scan all files first** - Run error checking across the entire codebase
Do not stop after fixing a single file. Treat the mod as a single system:

Scan all files first and list all errors and inconsistencies.

Fix all errors systematically.

Re-validate after each fix to ensure no new errors are introduced.

Only stop when all files are completely correct and the pack would load in Minecraft without any errors or warnings.

For every fix, explain what was wrong, what you changed, and why. Once the mod passes 100% validation, summarize all changes and confirm it is fully functional.

Only consider the job done when the entire mod works as a system, not when just one file is fixed.

2. **Fix all errors systematically** - Address every error found, not just one file
3. **Re-validate after each fix** - Ensure no new errors were introduced
4. **Explain every change** - What was wrong, what you changed, and why
5. **Push to GitHub Actions** - Commit, push, and tag for compilation
6. **Only stop when 100% validated** - Continue until all files are completely correct and compile without errors

## Compilation Workflow

**NEVER build locally.** Always use GitHub Actions:

```bash
git add -A
git commit -m "descriptive message"
git push
git tag v3.0.X
git push origin v3.0.X
```

This triggers GitHub Actions to compile the mod. The workspace must stay clean - no `build/` or `.gradle/` directories.

## Project Architecture

- **Client-side only** - No server installation required
- **Built-in death/sleep detection** - `DeathSleepEvents.java` handles client-side detection
- **Per-server redirect mappings** - Config uses `death:source->dest` and `sleep:source->dest` format
- **Direct server connections** - No packets, no OP permissions needed
- **Waystone menu buttons** - Manual redirects via visible buttons in GUI
- **Mixin compatibility bypass** - `MixinClientPacketListener` forces vanilla connection to bypass mod checks

## Critical Rules

- ✅ **Scan → Fix → Validate → Push** after every change
- ✅ Treat the entire codebase as a system, not individual files
- ✅ Always push to GitHub Actions for compilation
- ❌ NEVER auto-tag releases without explicit user request
- ❌ NEVER build locally (no `gradlew build`)
- ❌ NEVER skip error validation before pushing

## Version Management

Version format: `3.0.{git-commit-count}`
- Auto-increments from git commit history
- Tags trigger GitHub Actions release workflow
- User decides when to tag, not the AI
