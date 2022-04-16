package com.example.football.models.dto;

import com.google.gson.annotations.Expose;

import javax.validation.constraints.*;

public class TeamSeedDto {
    @Expose
    @NotBlank
    @Size(min = 3)
    private String name;

    @Expose
    @NotBlank
    @Size(min = 3)
    private String stadiumName;

    @Expose
    @Min(1000)
    @NotNull
    private Integer fanBase;

    @Expose
    @NotBlank
    @Size(min = 10)
    private String history;

    @Expose
    @NotBlank
    @Size(min = 2)
    private String townName;

    public TeamSeedDto() {
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getStadiumName() {
        return stadiumName;
    }

    public void setStadiumName(String stadiumName) {
        this.stadiumName = stadiumName;
    }

    public Integer getFanBase() {
        return fanBase;
    }

    public void setFanBase(Integer fanBase) {
        this.fanBase = fanBase;
    }

    public String getHistory() {
        return history;
    }

    public void setHistory(String history) {
        this.history = history;
    }

    public String getTownName() {
        return townName;
    }

    public void setTownName(String townName) {
        this.townName = townName;
    }
}
