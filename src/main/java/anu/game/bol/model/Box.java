package anu.game.bol.model;

import java.util.Iterator;

public class Box {
	
    /**
     * The index of the end cup in a players cups
     */
    private static final int END_CUP = 6;
    /**
     * The number of cups for a player
     */
    private static final int NUM_CUPS = END_CUP + 1;
    /**
     * Statically represents the cups on the board. Indexed by player (0/1) then by cup number (0-6).
     */
    public static final Box[][] BOARD_LAYOUT = initialCups();
    /**
     * Which player does this belong to
     */
    private final Player player;
    /**
     * The cup number (0 - 7)
     */
    private final int cupNumber;
    /**
     * The next cup around the board
     */
    private Box next;
    /**
     * The cup opposite (null for end cups)
     */
    private Box opposite;

    public Box(Player player, int cupNumber) {
        this.player = player;
        this.cupNumber = cupNumber;
    }

    /**
     * Creates the initial state of the cups on the board
     *
     * @return
     */
    static final Box[][] initialCups() {
        Box[][] cups = new Box[2][7];
        Box last = null;
        for (int playerNum = 0; playerNum < 2; playerNum++) {
            Player player = Player.byValue(playerNum);
            for (int cup = 0; cup < 7; cup++) {
                Box built = new Box(player, cup);
                cups[playerNum][cup] = built;

                // add link to this as "next" for the last cup
                if (last != null) {
                    last.next = built;
                }
                last = built;
            }
        }
        // make board "next" a loop
        last.next = cups[0][0];
        // add opposites
        for (int i = 0; i < 6; i++) {
            // 0,0 > 1,5
            // 0,5 > 1,0
            cups[0][i].opposite = cups[1][5 - i];
            cups[1][5 - i].opposite = cups[0][i];
        }
        // opposites for end cups
        cups[0][6].opposite = cups[1][6];
        cups[1][6].opposite = cups[0][6];
        return cups;
    }

    /**
     * An iterator once around the board from player 1 cup 0
     * @return a new iterator
     */
    public static Iterator<Box> iterator() {
        return new BoxIterator(BOARD_LAYOUT[0][0], 14, null);
    }

    public static Box firstCup(Player p) {
        return BOARD_LAYOUT[p.number][0];
    }

    public static Box endCup(Player p) {
        return BOARD_LAYOUT[p.number][END_CUP];
    }

    public static Iterable<Box> playerCups(Player player) {
        return new Iterable<Box>() {
            @Override
            public Iterator<Box> iterator() {
                return new BoxIterator(firstCup(player), NUM_CUPS, player);
            }
        };
    }

    public static Iterable<Box> playerMoveCups(Player player) {
        return new Iterable<Box>() {
            @Override
            public Iterator<Box> iterator() {
                return new BoxIterator(firstCup(player), END_CUP, player);
            }
        };
    }

    public static Box parse(String s) {
        Player p = Player.parse(s.charAt(0));
        int index = Integer.parseInt(s.substring(1));
        return BOARD_LAYOUT[p.number][index];
    }


    public Player getPlayer() {
        return player;
    }

    public Box getOpposite() {
        return opposite;
    }

    public int getCupNumber() {
        return cupNumber;
    }

    public Box getNext() {
        return next;
    }

    public String toString() {
        return player.id + Integer.toString(cupNumber);
    }

    public boolean isEndCup() {
        return cupNumber == END_CUP;
    }

    public boolean isPlayerEndCup(Player player) {
        return isEndCup() && player.equals(this.player);
    }

    public Box next(Player p) {
        if (p != null && next.isEndCup() && !next.getPlayer().equals(p)) {
            // skip other player's end cup
            return next.next;
        }
        return next;
    }

    public int index() {
        return player.number * 7 + cupNumber;
    }

}
