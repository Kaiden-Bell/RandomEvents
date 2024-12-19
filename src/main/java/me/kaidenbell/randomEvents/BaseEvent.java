package me.kaidenbell.randomEvents;

public interface BaseEvent {
    void start();
    void end();
    boolean isRunning();
}
