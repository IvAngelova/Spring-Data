package com.example.game_store.service;

import com.example.game_store.model.dto.GameAddDto;

public interface GameService {
    void addGame(GameAddDto gameAddDto);

    void editGame(Long gameId, String[] values);

    void deleteGame(Long gameId);

    void getAllGamesInfo();

    void getGameInfo(String title);
}
