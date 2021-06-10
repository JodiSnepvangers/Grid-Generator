# Grid Generator
This application will generate a input and output folder in the place it is ran, and will open any JPG or PNG images found inside the input folder, and any images saved are found inside the output folder. Images can be selected by the selection box above.

Refresh will check the input folder for any new images, and Load will reload the current copy of the map (in case it has been edited from outside).

## Preview window
The preview window is found on the left side of the application, and shows what the grid looks like on top of the selected image, and the area shown in red is what will be cut off when saved to keep the grid central.

## Grid Parameters
the parameters can be found on the right side of the program, and will affect the grid shown in the preview. 
### Grid Size
Grid Spacing affects how big each square of the grid is in pixels, while Grid Thickness affects how thick the grid lines actually are.
### Filter Type
Filters determine where and when to place labels for the coordinates, each affecting them differently:

Cross Grid draws lines both horizontally and vertically, placing either numbers or letters respectively, and both where the two lines intersect.

Battleship works the same as  Cross Grid, but only draws labels where the lines intersect.

Letter Line only draws the vertical letter lines.

No Filter applies no filter, and will place a label on every grid space.

Off is as you would expect, and the labels are removed entirely.

### Label Sliders
The Label Parameters affect the labels placed by the filters, such as their opacity or their size. Label Skip determines how many spaces sit between each line placed by the filters.

## Color Picker
The Color Picker lets you select different colors for the lines, labels, and the font inside the labels, in case the map makes them difficult to see.

## Auto Cut & Direct Input
these last two checkboxes are special settings. Auto Cut will enable or disable the red area. When enabled, it will cut the image in such a way to make the grid fit perfectly. Direct Input will swap out all sliders for a direct number input to allow for bigger or smaller values. this is a very dangerous setting!