package anu.game.bol.model;

import lombok.EqualsAndHashCode;
import lombok.ToString;
import org.slf4j.Logger;

import java.util.*;
import java.util.stream.Collectors;

import static org.slf4j.LoggerFactory.getLogger;

public class Board {
	
	/**
     * Logger (SLF4J)
     */
    private static final Logger LOG = getLogger(Board.class);
    /**
     * The bead counts in the Box. Indexed by {@link Box#index()}
     */
    private final int[] beads;
    /**
     * The scores for each player
     */
    private final int[] scores;
    /**
     * The player who makes the next move
     */
    private final Player nextPlayer;
    /**
     * The cups which are valid moves for the {@link #nextPlayer}
     */
    private final List<Box> nextMoves;

    public Board(int[] beads, Player nextPlayer) {
        this(beads,
                nextPlayer,
                new int[]{
                        calcScore(Player.ONE, beads),
                        calcScore(Player.TWO, beads)
                },
                calcNextMoves(nextPlayer, beads));
        checksum();
    }

    public Board(int[] beads, Player nextPlayer, int[] scores, List<Box> nextMoves) {
        if(beads == null || beads.length != 14) {
            throw new IllegalArgumentException("Beads should be 14");
        }
        this.beads = beads;
        if (nextPlayer == null) {
            throw new IllegalArgumentException("Player cannot be null");
        }
        this.nextPlayer = nextPlayer;
        if(scores.length != 2) {
            throw new IllegalArgumentException("Scores must be 2 in length");
        }
        this.scores = scores;
        this.nextMoves = nextMoves;
        checksum();
    }

    private void checksum() {
        assert beadCount() == 48: "Count of beads is not 48";
        assert scores[0] + scores[1] == 48: "Count of scores is not 48";
    }

    private int beadCount() {
        int beadCount = 0;
        for(int i=0; i < 14; i++) {
            beadCount += beads[i];
        }
        return beadCount;
    }

    public static Board initialBoard() {
        return new Board(initialBeads(), Player.ONE);
    }

    private static int[] initialBeads() {
        return new int[]{4,4,4,4,4,4,0,4,4,4,4,4,4,0};
    }

    private static int calcScore(Player player, int[] beads) {
        if(beads == null || beads.length != 14) {
            throw new IllegalArgumentException("Beads should be 14");
        }
        int score = 0;
        for (Box cup : Box.playerCups(player)) {
            score += beads[cup.index()];
        }
        return score;
    }

    public Player getNextPlayer() {
        return nextPlayer;
    }

    public int getScore(Player player) {
        return scores[player.number];
    }

    public int getSafeScore(Player player) {
        return beads[Box.endCup(player).index()];
    }

    public int getLead(Player player) {
        return scores[player.number] - scores[player.otherNumber];
    }

    public int getBeadCount(Box cup) {
        return beads[cup.index()];
    }

    public int[] cloneBeads() {
        int[] clone = new int[14];
        System.arraycopy(beads, 0, clone, 0, clone.length);
        return clone;
    }

    public List<Move> nextMoves() {
        return nextMoves.stream()
                .map(cup -> new Move(cup, this))
                .collect(Collectors.toList());
    }

    private static List<Box> calcNextMoves(Player nextPlayer, int[] beads) {
        ArrayList<Box> moves = new ArrayList<>();
        for(Box playerCup : Box.playerMoveCups(nextPlayer)) {
            if (beads[playerCup.index()] > 0) {
                moves.add(playerCup);
            }
        }
        return Collections.unmodifiableList(moves);
    }

    public boolean isGameOver() {
        return nextMoves.isEmpty();
    }

    /**
     * Which player (if either) is in the lead.
     *
     * @return the lead player or NULL if tied
     */
    public Player getLeader() {
        int lead = getLead(Player.ONE);
        if (lead > 0) {
            return Player.ONE;
        } else if (lead < 0) {
            return Player.TWO;
        } else {
            return null;
        }
    }

    /**
     * Depth first recursive visting pattern on all the moves (and subsequent boards)
     * Up to a certain depth (if > 0)
     * @param visitor the visitor to the boards
     * @param depth the depth of recursion (0 or less implies "infinite")
     */
    public void visit(final MoveVisitor visitor, final int depth) {
        if (visitor != null) {
            if (depth > 0) {
                int nextDepth = depth - 1;
                for (Move move : nextMoves()) {
                    visitor.visit(move);
                    Board next = move.getAfter();
                    // recurse - depth first
                    next.visit(visitor, nextDepth);
                }
            }
            else {
                LOG.trace("Visitor recursion limit reached");
            }
        }
    }
    
    


}
