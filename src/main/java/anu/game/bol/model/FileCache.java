package anu.game.bol.model;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;
import java.util.stream.Collectors;

public class FileCache implements BoardCache {

	public static final String MOVE_SEPARATOR = "_";
    public static final String COUNT_SEPARATOR = ",";
    private final File directory;

    public FileCache(File directory) {
        this.directory = directory;
    }

    @Override
    public boolean contains(List<Move> moves) {
        File f = new File(directory, format(moves) + ".board");
        return f.exists();
    }

    @Override
    public void put(List<Move> moves, Board board) {
        final String movesString = format(moves);
        final File boardFile = new File(directory, movesString + ".board");
        try (PrintStream printStream = new PrintStream(new FileOutputStream(boardFile))) {
            writeTo(printStream, board);
        }
        catch(IOException e) {
            throw new RuntimeException(e);
        }
    }

    public static void writeTo(PrintStream printStream, Board board) {
        printStream.println(board.getNextPlayer().id);
        printStream.println(board.getScore(Player.ONE));
        printStream.println(board.getScore(Player.TWO));
        printStream.println(cupCounts(board));
        printStream.println(format(board.nextMoves()));
    }

    public static String cupCounts(Board board) {
        StringBuilder sb = new StringBuilder();
        Iterator<Box> iter = Box.iterator();
        while(iter.hasNext()) {
            Box cup = iter.next();
            sb.append(board.getBeadCount(cup));
            if (iter.hasNext()) {
                sb.append(COUNT_SEPARATOR);
            }
        }
        return sb.toString();
    }

    public static String format(List<Move> moves) {
        return moves.stream()
                .map(move -> move.getBox())
                .map(cup -> cup.toString())
                .collect(Collectors.joining(MOVE_SEPARATOR));
    }

    @Override
    public Board get(List<Move> moves) {
        final File boardFile = new File(directory, format(moves) + ".board");
        try (BufferedReader reader = new BufferedReader(new InputStreamReader(new FileInputStream(boardFile))))
        {
            return readFrom(reader);
        }
        catch(IOException e) {
            throw new RuntimeException(e);
        }
    }

    public static Board readFrom(BufferedReader reader) throws IOException {
        char playerId = reader.readLine().charAt(0);
        int[] scores = new int[]{
                Integer.parseInt(reader.readLine()),
                Integer.parseInt(reader.readLine()),
        };
        int[] beads = parseCupCounts(reader.readLine());
        List<Box> nextMoves = parseNextMoves(reader.readLine());
        return new Board(beads, Player.parse(playerId), scores, nextMoves);
    }

    public static List<Box> parseNextMoves(String line) {
        String[] split = line.split(MOVE_SEPARATOR);
        return Arrays.asList(split).stream()
                .map(s -> Box.parse(s))
                .collect(Collectors.toList());
    }

    public static int[] parseCupCounts(String line) {
        String[] split = line.split(COUNT_SEPARATOR);
        int[] beads = new int[split.length];
        for(int i=0; i < 14; i++) {
            beads[i] = Integer.parseInt(split[i]);
        }
        return beads;
    }

}
