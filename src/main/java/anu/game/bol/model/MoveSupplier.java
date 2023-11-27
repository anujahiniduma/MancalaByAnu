package anu.game.bol.model;

public interface MoveSupplier {
	
	 /**
     * Given a board - choose a move
     * @param board the board state to choose a move from
     * @return the selected move
     */
    Move selectFrom(Board board);

    /**
     * A display name for this move supplier
     */
    String getDisplayName();

}
