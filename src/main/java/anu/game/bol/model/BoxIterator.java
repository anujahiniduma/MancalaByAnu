package anu.game.bol.model;

import java.util.Iterator;

public class BoxIterator implements Iterator<Box> {
	
	private final Box origin;
    private final Player player;
    private int counter;
    private Box current;

    public BoxIterator(Box origin, int cups, Player player) {
        if (origin == null) {
            throw new IllegalArgumentException("Origin Box cannot be null");
        }
        this.origin = origin;
        this.counter = cups;
        this.player = player;
        this.current = null;
    }

    @Override
    public boolean hasNext() {
        return counter != 0;
    }

    @Override
    public Box next() {
        if (current == null) {
            current = origin;
        }
        else {
            current = current.next(player);
        }
        // update counter
        if (counter > 0) {
            counter--;
        }
        return current;
    }

}
