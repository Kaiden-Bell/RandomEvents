package me.kaidenbell.randomEvents;

import java.util.UUID;

public class PlayerScore{
    private UUID playerUUID;
    private int score;


    public PlayerScore(UUID playerUUID, int score) {
        this.playerUUID = playerUUID;
        this.score = score;
    }

    public UUID getPlayerUUID() {
        return playerUUID;
    }

    public int getScore() {
        return score;
    }

    public void setScore(int score) {
        this.score = score;
    }
}
