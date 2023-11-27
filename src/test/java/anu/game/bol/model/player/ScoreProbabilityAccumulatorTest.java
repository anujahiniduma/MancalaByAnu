package anu.game.bol.model.player;

import anu.game.bol.model.Board;
import anu.game.bol.model.Player;
import anu.game.bol.player.ScoreProbabilityAccumulator;

import org.junit.Test;
import static org.junit.Assert.*;

public class ScoreProbabilityAccumulatorTest {

	private ScoreProbabilityAccumulator subject = new ScoreProbabilityAccumulator(Player.ONE);

    @Test
    public void visit() {
        Board.initialBoard().visit(subject, 3);
        assertEquals(226, subject.getTotalPopulationSize());
        assertEquals(-1.1858407079646018, subject.getAverageLead(), 0.00001);
    }
	
}
