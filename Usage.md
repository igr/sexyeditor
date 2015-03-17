# Configuration screen #

![http://idea-sexyeditor.googlecode.com/files/config.jpg](http://idea-sexyeditor.googlecode.com/files/config.jpg)

# Details #

## Editor ##

Editors is a list of editor groups. Each group is defined by its **name** and files matching **wildcard patterns**. Order of the list is important, and it goes from top to bottom. File name is matched upon each group from the top, until filename matching is positive.

## Name and Match ##

Name is just user-friendly group label. Match is semi-column (;) separated list of **wildcard** file patterns that defines which files will be associated with currently selected group.

## Position and Offest ##

Image relative position in editor window. Offset is a gap in pixels between image and nearest editor edges.

## Opacity ##

Makes images transparent.

## Shrink to fit ##

If this option is set, large images will be shrink to fit the **desktop** size. Note it is a desktop size, not IDEA size, therefore, images will be still a bit larger then editor. Therefore it is possible to set amount of shrinking: 100% means images will fit to desktop, 50% means images will fit the one quarter of the screen.

## Random ##

Enables random mode, where next image to load is chosen randomly from the file list.

## Slideshow ##

Slideshow enables runtime image changes, during user work. Value defines number of milliseconds between image change. Quite sexy;)

## File list ##

Editable list of all images files. Each file has to be in one row. Invalid files are simply ignored.