package com.chaotichippos.finalproject.app.util;

import com.chaotichippos.finalproject.app.App;
import com.squareup.otto.Bus;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * See https://gist.github.com/JakeWharton/3057437
 */
public class ScopedBus {
	// See Otto's sample application for how BusProvider works. Any mechanism
	// for getting a singleton instance will work.
	private final Bus bus = App.getEventBus();
	private final Set<Object> objects = new HashSet<Object>();
	private final List<Object> events = new ArrayList<Object>();
	private boolean active;

	public void register(Object obj) {
		objects.add(obj);
		if (active) {
			bus.register(obj);
		}
	}

	public void unregister(Object obj) {
		objects.remove(obj);
		if (active) {
			bus.unregister(obj);
		}
	}

	public void post(Object event) {
		if (active) {
			bus.post(event);
		} else {
			events.add(event);
		}
	}

	public void paused() {
		active = false;
		for (Object obj : objects) {
			bus.unregister(obj);
		}
	}

	public void resumed() {
		active = true;
		for (Object obj : objects) {
			bus.register(obj);
		}
		for (Object event : events) {
			bus.post(event);
		}
		events.clear();
	}
}
