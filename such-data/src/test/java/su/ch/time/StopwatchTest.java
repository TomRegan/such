package su.ch.time;

import org.junit.jupiter.api.Test;
import su.ch.time.Stopwatch.*;

import java.time.Duration;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.*;
import static org.junit.jupiter.api.Assertions.*;
import static su.ch.time.Stopwatch.*;

class StopwatchTest {

    @Test void elapsedTime() {
        ReadyStopwatch ready = readyStopwatch();
        RunningStopwatch running = ready.start();
        StoppedStopwatch stopped = running.stop();
        assertAll(
                () -> assertThat(ready.elapsed(), is(equalTo(Duration.ZERO))),
                () -> assertThat(stopped.elapsed(), is(greaterThan(ready.elapsed()))),
                () -> assertThat(running.elapsed(), is(greaterThan(stopped.elapsed()))));
    }

    @Test void runningState() {
        assertAll(
                () -> assertFalse(readyStopwatch().isRunning()),
                () -> assertTrue(runningStopwatch().isRunning()),
                () -> assertFalse(runningStopwatch().stop().isRunning()),
                () -> assertFalse(runningStopwatch().reset().isRunning()));

    }

    @Test void mapping() {
        runningStopwatch().apply((duration) -> assertThat(duration, greaterThanOrEqualTo(Duration.ZERO)));
        runningStopwatch().stop().apply((duration) -> assertThat(duration, greaterThanOrEqualTo(Duration.ZERO)));
    }

    @Test void monoid() {
        assertThat(readyStopwatch()
                        .append(offsetStopwatch(Duration.ofMillis(100)))
                        .append(readyStopwatch().id()).elapsed(),
                is(equalTo(Duration.ofMillis(100))));

    }

}
