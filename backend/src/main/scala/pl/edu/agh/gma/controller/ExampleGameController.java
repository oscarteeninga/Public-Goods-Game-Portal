package pl.edu.agh.gma.controller;

import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("sing/api/v1/example/game")
@AllArgsConstructor
public class ExampleGameController {

  //    private final ExampleGameService exampleGameService;
  //
  //    @GetMapping("/get")
  //    public ResponseEntity<List<Round>> getAllHistory() {
  //        return ResponseEntity
  //                .status(HttpStatus.OK)
  //                .body(exampleGameService.getAllHistory());
  //    }
  //
  //    @GetMapping("/getlast")
  //    public ResponseEntity<Round> getLastRoundHistory() {
  //
  //        return ResponseEntity
  //                .status(HttpStatus.OK)
  //                .body(exampleGameService.getLastRoundResult());
  //    }
  //
  //    @GetMapping("/save")
  //    public ResponseEntity<Round> saveExampleRound() {
  //
  //        return ResponseEntity
  //                .status(HttpStatus.OK)
  //                .body(exampleGameService.saveExampleRound());
  //    }
}
