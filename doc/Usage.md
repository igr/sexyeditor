# Usage

## Configuration screen

![](SexyEditorSettings.png)

## Editor

`Editors` is a list of Editor groups. Each Editor group is defined by its _name_ and files matching _wildcard patterns_. The order is important: it proceeds from top to bottom. An editors' file name is matched against each group starting from the top of the list.

## Name and Match

`Name` is simply a user-friendly label. `Match` is semi-column (`;`) separated list of _wildcard_ (no regex) file patterns that define which files will be associated with a currently selected group.

## Position and Offset

Images' relative position in the editor window. `Offset` is a gap in pixels between the image and the nearest editors' edge.

## Fixed position

If checked, the background image will not follow scrolling of the editor.

## Fit to editor

Various fitting strategies, on how background image is resized for the editor size.

## Opacity

Controls the image transparency.

## Resize on load (adv.)

If this option is set, large images will be resized to fit the _desktop_ size. Note it is a desktop size, not IDEA size, therefore, images will be still a bit larger than the editor. 100% means images will fit to desktop, 50% means images will fit the one quarter of the screen.

Once again, this is not a simple resizing of the images. This is a definition of the max image size relative to OS display. The small images stay the same, large images get shrunk.

## Random

Enables the random mode, where next background image is chosen randomly from the file list.

## Slideshow

Slideshow enables image changes in runtime, during user work. Value defines the number of seconds between image change.

## Backgrounds

List of all images. Each file is on a separate row. Invalid files (not images) are simply ignored. Drag-and-drop is supported.

There is a context menu that displays a preview of the image.