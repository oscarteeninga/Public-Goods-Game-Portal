package pl.edu.agh.gma.controller;

import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("sing/api/v1/admin/games")
@AllArgsConstructor
public class GameAdminController {

  //    private final GameService gameService;
  //
  //    @GetMapping
  //    public ResponseEntity<List<GameResponse>> getAllGames() {
  //        List<GameResponse> gamesData = gameService.getAllGames();
  //
  //        if (gamesData.isEmpty())
  //            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
  //
  //        return ResponseEntity
  //                        .status(HttpStatus.OK)
  //                        .body(gamesData);
  //    }
  //
  //    @GetMapping("/{gameId}")
  //    public ResponseEntity<GameResponse> getGames(@PathVariable Long gameId) {
  //        GameMeta gameMetaData = gameService.getGame(gameId);
  //
  //        if (gameMetaData == null)
  //            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
  //        else
  //            return ResponseEntity
  //                .status(HttpStatus.OK)
  //                .body(gameMetaData);
  //    }
  //
  //    @PostMapping("/configure/{gameId}")
  //    public ResponseEntity<Void> configureGame(@PathVariable Long gameId, @RequestBody GameConfiguration
  //    gameConfiguration) {
  //        gameService.configure(gameConfiguration);
  //        return new ResponseEntity<>(HttpStatus.OK);
  //    }
}