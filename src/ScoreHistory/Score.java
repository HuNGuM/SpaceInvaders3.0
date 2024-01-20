package ScoreHistory;

import java.io.Serializable;

public record Score(String name, Integer score, Integer distance, Integer leftMatch, Integer rightMatch, Integer totalMatch) implements Serializable, Comparable<Score> {
    @Override
    public int compareTo(Score o) {
        return Integer.compare(this.score(), o.score());
    }
}
