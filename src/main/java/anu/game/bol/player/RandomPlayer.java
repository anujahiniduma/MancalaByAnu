package anu.game.bol.player;

import java.util.List;

import anu.game.bol.model.Board;
import anu.game.bol.model.Move;
import anu.game.bol.model.MoveSupplier;

public class RandomPlayer implements MoveSupplier {

    @Override
    public Move selectFrom(Board board) {
        List<Move> moves = board.nextMoves();
        int selection = (int) (Math.random() * moves.size());
        return (Move) moves.get(selection);
    }

    @Override
    public String getDisplayName() {
        return "Random player";
    }
}