package anu.game.bol.model;

import lombok.ToString;

import java.util.Iterator;


@ToString(exclude = "after")
public class Move implements Iterable<Box> {
	
	/**
     * The start cup for this move.
     */
    private final Box startBox;
    /**
     * The board before this move (this move is one of the moves on this board)
     */
    private final Board before;
    /**
     * A cache of the calculated "after" board state
     */
    private transient Board after = null;

    public Move(Box startBox, Board before) {
        this.startBox = startBox;
        this.before = before;
    }

    public Board getBefore() {
        return before;
    }

    public Box getBox() {
        return startBox;
    }

    /**
     * The player whose move this is
     */
    public Player getPlayer() {
        return before.getNextPlayer();
    }

    public Board getAfter() {
        if (after == null) {
            MoveOutcome outcome = calcMoveOutcome();
            after = new Board(outcome.getBeadCount(), outcome.isAnotherGo() ? getPlayer() : getPlayer().opponent());
        }
        return after;
    }

    private MoveOutcome calcMoveOutcome() {
        int[] boxes = before.cloneBeads();
        Iterator<Box> moveIterator = iterator();
        Box box = null;
        do {
            if (box == null) {
                box = moveIterator.next();
                // scoop up the beads
                boxes[box.index()] = 0;
            } else {
                box = moveIterator.next();
                // drop a bead in that cup
                boxes[box.index()]++;
            }
        } while (moveIterator.hasNext());
        // is this another go for "this" player
        boolean anotherGo = box.isPlayerEndCup(getPlayer());

        // special "bead opposite" stealing logic...
        // only applies when we did not finish in our end cup
        if (!anotherGo) {
            // Did we put the last bead in an empty cup? (i.e. count == 1)
            if (boxes[box.index()] == 1) {
                // and does it belongs to us..
                if (box.getPlayer().equals(getPlayer())) {
                    // take that bead + the beads opposite into our end cup
                    Box opposite = box.getOpposite();
                    int steal = boxes[opposite.index()] + 1;
                    boxes[opposite.index()] = 0;
                    boxes[box.index()] = 0;
                    boxes[Box.endCup(getPlayer()).index()] += steal;
                }
            }
        }
        return new MoveOutcome(boxes,anotherGo);
                
    }

    @Override
    public Iterator<Box> iterator() {
        int beads = before.getBeadCount(startBox);
        return new BoxIterator(startBox, beads + 1, getPlayer());
    }
    
    /*
    @Override
    public String toString() {
		return this.startBox.toString();
    	
    }
   */
}
