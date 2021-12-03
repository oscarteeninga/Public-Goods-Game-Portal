package pl.edu.agh.gma.mapper;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import pl.edu.agh.gma.dto.ConfigurationGameDto;
import pl.edu.agh.gma.model.GameSessionToken;
import pl.edu.agh.gma.model.mongo.GameConfiguration;
import pl.edu.agh.gma.service.util.GameState;

@Component
public class GameConfigurationMapper {

    @Value("${app.url.frontendBase}")
    private String frontendUrl;

    public GameSessionToken mapToGameSessionToken(ConfigurationGameDto configurationGameDto) {
        return new GameSessionToken(configurationGameDto.getGameSessionToken());
    }

    public GameConfiguration mapToGameConf(ConfigurationGameDto configurationGameDto) {
        return new GameConfiguration(
                configurationGameDto.getRoundTime(),
                configurationGameDto.getNumberOfRounds(),
                configurationGameDto.getInitialMoneyAmount(),
                configurationGameDto.getPoolMultiplierFactor(),
                configurationGameDto.getFreeriders(),
                configurationGameDto.getCooperators(),
                configurationGameDto.getCasuals()
        );
    }

    // TODO refactor => as temporary solution game is configured true/false
    public ConfigurationGameDto mapToDto(GameState gameState, Optional<GameConfiguration> gameConfiguration) {
        return gameConfiguration.map(configuration ->
                new ConfigurationGameDto(
                        gameState.getSessionToken().getStrToken(),
                        gameState.getGameName(),
                        gameState.getDescription(),
                        configuration.getRoundTime(),
                        configuration.getNumberOfRounds(),
                        configuration.getInitialMoneyAmount(),
                        configuration.getPoolMultiplierFactor(),
                        true,
                        gameState.getStatus(),
                        frontendUrl + "/user/game/" + gameState.getSessionToken().getStrToken(),
                        configuration.getFreeriders(),
                        configuration.getCooperators(),
                        configuration.getCasuals())
        ).orElseGet(() ->
                new ConfigurationGameDto(
                        gameState.getSessionToken().getStrToken(),
                        gameState.getGameName(),
                        gameState.getDescription(),
                        5,
                        5,
                        10,
                        2,
                        false,
                        gameState.getStatus(),
                        "...",
                        0,
                        0,
                        0
                )
        );
    }

}
