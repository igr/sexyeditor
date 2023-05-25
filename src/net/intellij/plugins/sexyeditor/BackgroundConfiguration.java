package net.intellij.plugins.sexyeditor;

import com.intellij.openapi.fileTypes.WildcardFileNameMatcher;

import java.util.Random;
import java.util.StringTokenizer;

/**
 * Configuration data object for one file group.
 */
public class BackgroundConfiguration {

	public static final int POSITION_TOP_LEFT = 0;
	public static final int POSITION_TOP_MIDDLE = 1;
	public static final int POSITION_TOP_RIGHT = 2;
	public static final int POSITION_MIDDLE_LEFT = 3;
	public static final int POSITION_CENTER = 4;
	public static final int POSITION_MIDDLE_RIGHT = 5;
	public static final int POSITION_BOTTOM_LEFT = 6;
	public static final int POSITION_BOTTOM_MIDDLE = 7;
	public static final int POSITION_BOTTOM_RIGHT = 8;

	protected String name = "All editors";

	/**
	 * List of matching editor file names for this background configuration.
	 */
	protected String editorGroup = "*";

	/**
	 * Opacity value.
	 */
	protected float opacity = 0.10f;


	/**
	 * Image position 0 - 8.
	 */
	protected int position = POSITION_TOP_RIGHT;

	/**
	 * Position offset from the edges in pixels.
	 */
	protected int positionOffset = 10;

	/**
	 * Is image shrunk.
	 */
	protected boolean shrink;

	/**
	 * Amount of image shrinking in percents. 100% percent means shrink to fit
	 * the screen dimensions.
	 */
	protected int shrinkValue = 90;

	/**
	 * List of images.
	 */
	protected String[] fileNames;

	/**
	 * Is the next image to load random one from the list.
	 */
	protected boolean random;

	/**
	 * Slide-show mode.
	 */
	protected boolean slideshow;

	/**
	 * Pause in milliseconds between two slides.
	 */
	protected int slideshowPause = 3000;

	/**
	 * If background is located at fixed position and does not move with scrolling.
	 */
	protected boolean fixedPosition = true;

	protected boolean shrinkToFit = false;

	// ---------------------------------------------------------------- access

	public String getName() {
		return name;
	}

	public void setName(final String name) {
		this.name = name;
	}

	public String getEditorGroup() {
		return editorGroup;
	}

	public void setEditorGroup(final String editorGroup) {
		this.editorGroup = editorGroup;
	}

	public float getOpacity() {
		return opacity;
	}

	public void setOpacity(final float opacity) {
		this.opacity = opacity;
	}

	public int getPosition() {
		return position;
	}

	public void setPosition(final int position) {
		this.position = position;
	}

	public int getPositionOffset() {
		return positionOffset;
	}

	public void setPositionOffset(final int positionOffset) {
		this.positionOffset = positionOffset;
	}

	public String[] getFileNames() {
		return fileNames;
	}

	public void setFileNames(final String[] fileNames) {
		this.fileNames = fileNames;
	}

	public boolean isRandom() {
		return random;
	}

	public void setRandom(final boolean random) {
		this.random = random;
	}

	public synchronized boolean isSlideshow() {
		return slideshow;
	}

	public synchronized void setSlideshow(final boolean slideshow) {
		this.slideshow = slideshow;
		if (slideshow) {
			createThread();
		} else {
			closeThread();
		}
	}

	public int getSlideshowPause() {
		return slideshowPause;
	}

	public void setSlideshowPause(final int slideshowPause) {
		this.slideshowPause = slideshowPause;
	}

	public boolean isShrink() {
		return shrink;
	}

	public void setShrink(final boolean shrink) {
		this.shrink = shrink;
	}

	public int getShrinkValue() {
		return shrinkValue;
	}

	public void setShrinkValue(final int shrinkValue) {
		this.shrinkValue = shrinkValue;
	}

	public boolean isFixedPosition() {
		return fixedPosition;
	}

	public void setFixedPosition(final boolean fixedPosition) {
		this.fixedPosition = fixedPosition;
	}

	public boolean isShrinkToFit() {
		return shrinkToFit;
	}

	public void setShrinkToFit(final boolean shrinkToFit) {
		this.shrinkToFit = shrinkToFit;
	}

	// ---------------------------------------------------------------- runtime

	private static final Random rnd = new Random();

	/**
	 * Matches file name with editor group wildcard list.
	 */
	public boolean matchFileName(final String fileName) {
		final StringTokenizer st = new StringTokenizer(editorGroup, ";");
		while (st.hasMoreTokens()) {
			final String token = st.nextToken().trim();
			final var wfnm = new WildcardFileNameMatcher(token);
			if (wfnm.acceptsCharSequence(fileName)) {
				return true;
			}
		}
		return false;
	}

	private int imageIndex =  -1;     // index of current image

	/**
	 * Returns the file name of the next image. If random mode is not enabled,
	 * returns the very next image, otherwise, the random one.
	 */
	public String getNextImage() {
		if (fileNames == null) {
			return null;
		}
		final int total = fileNames.length;
		if (total == 0) {
			return null;
		}

		if (random) {
			imageIndex = rnd.nextInt(total);
		} else {
			imageIndex++;
			if (imageIndex >= total) {
				imageIndex = 0;
			}
		}
		return fileNames[imageIndex];
	}



	// ---------------------------------------------------------------- borders and thread

	private final WeakSet<BackgroundBorder> allBorders = new WeakSet<>();

	/**
	 * Registers a border to its configuration.
	 */
	public synchronized void registerBorder(final BackgroundBorder border) {
		allBorders.add(border);
	}

	/**
	 * Unregisters border from its configuration.
	 */
	public synchronized void unregisterBorder(final BackgroundBorder border) {
		allBorders.remove(border);
	}

	/**
	 * Repaints components of all registered borders.
	 */
	public synchronized void repaintAllEditors() {
		for (final BackgroundBorder bb : allBorders) {
			bb.getComponent().repaint();
		}
	}


	private Thread slideshowThread;

	/**
	 * Creates new slideshow thread if it doesn't exist. Thread waits for specified time
	 * and then loads the next image in all borders with this configuration.
	 * Under the synchronized lock.
	 */
	private void createThread() {
		if (slideshowThread != null) {
			return;
		}
		slideshowThread = new Thread(() -> {
			while (slideshow) {
				try {
					Thread.sleep(slideshowPause);
				} catch (final InterruptedException iex) {
					if (!slideshow) {
						break;
					}
				}
				for (final BackgroundBorder border : allBorders) {
					final String nextImage = getNextImage();
					border.loadImage(nextImage);
				}
			}
		});
		slideshowThread.setDaemon(true);
		slideshowThread.setPriority(Thread.MIN_PRIORITY);
		slideshowThread.start();
	}

	/**
	 * Closes the thread. Under the synchronized lock.
	 */
	private void closeThread() {
		if (slideshowThread == null) {
			return;
		}
		slideshowThread.interrupt();
		while (slideshowThread.isAlive()) {
			try {
				Thread.sleep(100);
			} catch (final InterruptedException iex) {
				//ignore
			}
		}
		slideshowThread = null;
	}

	// ---------------------------------------------------------------- toString

	/**
	 * String representation, used for UI presentation.
	 */
	@Override
	public String toString() {
		return name + " (" + editorGroup + ')';
	}

}
