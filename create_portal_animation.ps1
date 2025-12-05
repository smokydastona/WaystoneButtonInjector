# PowerShell script to create an animated portal texture (optimized version)
# This creates a vertical strip of 16 frames (4096x256) with gradient animation

Add-Type -AssemblyName System.Drawing

# Portal animation parameters
$frameWidth = 256
$frameHeight = 256
$frameCount = 16  # Reduced for faster generation
$totalHeight = $frameHeight * $frameCount

Write-Host "Creating animated portal texture ($frameWidth x $totalHeight)..."

# Create the full animation strip
$bitmap = New-Object System.Drawing.Bitmap($frameWidth, $totalHeight)
$graphics = [System.Drawing.Graphics]::FromImage($bitmap)
$graphics.SmoothingMode = [System.Drawing.Drawing2D.SmoothingMode]::AntiAlias

# Portal colors (purple/magenta gradient)
for ($frame = 0; $frame -lt $frameCount; $frame++) {
    $frameY = $frame * $frameHeight
    $animProgress = $frame / [float]$frameCount
    
    # Create rotating gradient for this frame
    $centerX = $frameWidth / 2
    $centerY = $frameHeight / 2
    
    # Calculate rotation angle for this frame
    $rotation = $animProgress * 360
    
    # Create radial gradient brush
    $path = New-Object System.Drawing.Drawing2D.GraphicsPath
    $path.AddEllipse(0, $frameY, $frameWidth, $frameHeight)
    
    $brush = New-Object System.Drawing.Drawing2D.PathGradientBrush($path)
    
    # Center color (lighter purple with transparency)
    $brush.CenterColor = [System.Drawing.Color]::FromArgb(180, 180, 82, 255)
    
    # Edge colors (darker purple/indigo with transparency)
    $edgeColors = @(
        [System.Drawing.Color]::FromArgb(100, 75, 0, 130),
        [System.Drawing.Color]::FromArgb(120, 138, 43, 226)
    )
    $brush.SurroundColors = $edgeColors
    
    # Fill this frame
    $graphics.FillRectangle($brush, 0, $frameY, $frameWidth, $frameHeight)
    
    # Add some swirl effects
    $pen = New-Object System.Drawing.Pen([System.Drawing.Color]::FromArgb(50, 200, 100, 255), 2)
    $angle1 = ($rotation + 0) * [Math]::PI / 180
    $angle2 = ($rotation + 120) * [Math]::PI / 180
    $angle3 = ($rotation + 240) * [Math]::PI / 180
    
    $graphics.DrawArc($pen, 64, $frameY + 64, 128, 128, $rotation, 180)
    $graphics.DrawArc($pen, 64, $frameY + 64, 128, 128, $rotation + 120, 180)
    
    $brush.Dispose()
    $path.Dispose()
    $pen.Dispose()
    
    Write-Host "Frame $($frame + 1)/$frameCount completed"
}

# Create output directory
$outputDir = "src\main\resources\assets\waystoneinjector\textures\gui"
New-Item -ItemType Directory -Force -Path $outputDir | Out-Null

# Save as PNG
$outputPath = Join-Path $outputDir "portal_animation.png"
$bitmap.Save($outputPath, [System.Drawing.Imaging.ImageFormat]::Png)

Write-Host "`nAnimated portal texture created: $outputPath"
Write-Host "Dimensions: $frameWidth x $totalHeight ($frameCount frames)"

$graphics.Dispose()
$bitmap.Dispose()

Write-Host "`nDone!"

