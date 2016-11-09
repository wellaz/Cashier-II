package com.sign.duration;

import java.time.Duration;
import java.time.Instant;

import com.sign.in.Monitor;

/**
 *
 * @author Wellington
 */

public class HeartBeat {

	private static String getHeartBeat() {
		Instant end = Instant.now();
		Instant start = Monitor.startTime;
		Duration timeElapsed = Duration.between(start, end);
		return "" + timeElapsed.toMillis();
	}

	public static String getDuration() {
		return getHeartBeat().trim();
	}
}
