package pl.edu.agh.gma.model.mongo;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
@Document
public class GameConfiguration {

    @Id
    private String id;

    private int roundTime;
    private int numberOfRounds;
    private double initialMoneyAmount;
    private double poolMultiplierFactor;

    private int freeriders;
    private int cooperators;
    private int casuals;

    private int reinforcers;
    private int sensitives;
    private int neural;

    private int presidents;

    public GameConfiguration(
            int roundTime,
            int numberOfRounds,
            double initialMoneyAmount,
            double poolMultiplierFactor,
            int freeriders,
            int cooperators,
            int casuals,
            int reinforcers,
            int sensitives,
            int neural,
            int presidents) {
        this.roundTime = roundTime;
        this.numberOfRounds = numberOfRounds;
        this.initialMoneyAmount = initialMoneyAmount;
        this.poolMultiplierFactor = poolMultiplierFactor;
        this.freeriders = freeriders;
        this.cooperators = cooperators;
        this.casuals = casuals;
        this.reinforcers = reinforcers;
        this.sensitives = sensitives;
        this.neural = neural;
        this.presidents = presidents;
    }
}
