package anu.game.bol.player;

import java.util.List;

import anu.game.bol.model.Board;
import anu.game.bol.model.Move;
import anu.game.bol.model.MoveSupplier;
import static org.slf4j.LoggerFactory.getLogger;
import org.slf4j.Logger;

public class ComputerPlayer implements MoveSupplier {

    /**
     * Logger (SLF4J)
     */
    private static final Logger LOG = getLogger(ComputerPlayer.class);

    private int depth = 5;

    @Override
    public Move selectFrom(Board board) {
        return doBestScoreMove(board);
    }

    /**
     * Selects the move to give this player the best score possible after the move.
     *
     * @param board The board to pick the best score from
     * @return The last move with the highest score
     */
    public Move doBestScoreMove(Board board) {
        List<Move> moves = board.nextMoves();
        double bestScore = Double.NEGATIVE_INFINITY;
        Move bestMove = null;
        for(Move m : moves) {
            ScoreProbabilityAccumulator accumulator = new ScoreProbabilityAccumulator(board.getNextPlayer());
            m.getAfter().visit(accumulator, depth);
            double score = accumulator.getAverageLead();
            if (LOG.isDebugEnabled()){
                LOG.debug(m.getBox().toString() + ":"+score);
            }

            if (bestMove == null || score > bestScore) {
                bestMove = m;
                bestScore = score;
            }
        }
        return bestMove;
    }

    @Override
    public String getDisplayName() {
        return "Computer AI Player";
    }
}