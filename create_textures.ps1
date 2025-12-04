# PowerShell script to generate button background textures
Add-Type -AssemblyName System.Drawing

$outputPath = "src\main\resources\assets\waystoneinjector\textures\gui\buttons"

# Define waystone types with their RGB colors
$waystoneColors = @{
    'regular' = @(139, 115, 85)      # Sandy brown
    'mossy' = @(85, 107, 47)          # Dark olive green
    'blackstone' = @(42, 42, 48)      # Dark gray
    'deepslate' = @(60, 60, 60)       # Dark slate gray
    'endstone' = @(221, 223, 165)     # Pale yellow
    'sharestone' = @(138, 43, 226)    # Blue violet/purple
}

$sides = @('left', 'right')
$counts = @(1, 2, 3)

foreach ($waystoneType in $waystoneColors.Keys) {
    $rgb = $waystoneColors[$waystoneType]
    
    foreach ($side in $sides) {
        foreach ($count in $counts) {
            # Create 64x96 bitmap
            $bitmap = New-Object System.Drawing.Bitmap(64, 96)
            $graphics = [System.Drawing.Graphics]::FromImage($bitmap)
            
            # Fill with transparency
            $graphics.Clear([System.Drawing.Color]::Transparent)
            
            # Create colors
            $bgColor = [System.Drawing.Color]::FromArgb(200, $rgb[0], $rgb[1], $rgb[2])
            $borderColor = [System.Drawing.Color]::FromArgb(255, $rgb[0], $rgb[1], $rgb[2])
            $highlightColor = [System.Drawing.Color]::FromArgb(80, 255, 255, 255)
            
            # Create brushes and pens
            $bgBrush = New-Object System.Drawing.SolidBrush($bgColor)
            $borderPen = New-Object System.Drawing.Pen($borderColor, 2)
            $highlightBrush = New-Object System.Drawing.SolidBrush($highlightColor)
            
            # Draw 3 button slots
            for ($i = 0; $i -lt 3; $i++) {
                $yStart = $i * 32
                $yEnd = $yStart + 30
                
                # Background fill
                $graphics.FillRectangle($bgBrush, 2, $yStart + 2, 59, 28)
                
                # Border
                $graphics.DrawRectangle($borderPen, 0, $yStart, 63, 31)
                
                # Inner highlight
                $graphics.FillRectangle($highlightBrush, 4, $yStart + 4, 55, 2)
            }
            
            # Save as PNG
            $filename = "$outputPath\$($waystoneType)_$($side)_$($count).png"
            $bitmap.Save($filename, [System.Drawing.Imaging.ImageFormat]::Png)
            
            Write-Host "Created $($waystoneType)_$($side)_$($count).png"
            
            # Cleanup
            $graphics.Dispose()
            $bitmap.Dispose()
            $bgBrush.Dispose()
            $borderPen.Dispose()
            $highlightBrush.Dispose()
        }
    }
}

Write-Host "`nAll 36 textures created successfully!" -ForegroundColor Green
