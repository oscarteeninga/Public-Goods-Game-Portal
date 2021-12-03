//package pl.edu.agh.gma.model;
//
//import lombok.AllArgsConstructor;
//import lombok.Data;
//import lombok.NoArgsConstructor;
//
//import javax.persistence.*;
//import javax.validation.constraints.NotBlank;
//
//import static javax.persistence.GenerationType.IDENTITY;
//
//@Table(name = "GameMeta")
//@Data
//@AllArgsConstructor
//@NoArgsConstructor
//@Entity
//public class GameMeta {
//    @Id
//    @GeneratedValue(strategy = IDENTITY)
//    private Long gameId;
//    @NotBlank(message = "Game name is required")
//    private String gameName;
//    private String description;
//
//    private String linkToGame;
//    private String status;
//    private String connectedPlayers;
//}