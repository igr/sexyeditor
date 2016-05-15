package net.intellij.plugins.sexyeditor;

import java.util.WeakHashMap;

/**
 * Weak set implementation build on WeakHashMap.
 */
public class WeakSet<E> extends SetMapAdapter<E> {

	public WeakSet() {
		super(new WeakHashMap<>());
	}
}
