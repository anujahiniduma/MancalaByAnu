package anu.game.bol.model;

import org.junit.Test;

import java.util.Iterator;

import static org.junit.Assert.*;

public class BoxTest {
	
	@Test
    public void testFirstAndEndCups() {
        final Box p1first = Box.firstCup(Player.ONE);
        final Box p2first = Box.firstCup(Player.TWO);
        assertEquals(0, p1first.index());
        assertEquals(7, p2first.index());

        final Box p1End = Box.endCup(Player.ONE);
        final Box p2End = Box.endCup(Player.TWO);
        assertEquals(6, p1End.index());
        assertEquals(13, p2End.index());
        assertEquals(p1End, p2End.getOpposite());
        assertEquals(p2End, p1End.getOpposite());

        assertEquals(p1first, p2End.getNext());
        assertEquals(p2first, p1End.getNext());
    }

    @Test
    public void testPlayerCupIterator() {
        int count = 0;
        for (int i=0; i < 2; i++) {
            Player player = Player.byValue(i);
            Iterable<Box> subject = Box.playerCups(player);
            assertNotNull(subject);
            Iterator<Box> iter = subject.iterator();
            for (int j = 0; j < 7; j++) {
                assertTrue(iter.hasNext());
                Box box = iter.next();
                assertNotNull(box);
                assertEquals(i, box.getPlayer().number);
                assertEquals(j, box.getCupNumber());
                assertEquals(count, box.index());

                Box opposite = box.getOpposite();
                assertNotNull(opposite);
                assertEquals(box, opposite.getOpposite());
                count++;
            }
            assertFalse(iter.hasNext());
        }
        assertEquals(14, count);
    }

    @Test
    public void testFullIterator() {
        Iterator<Box> iterator = Box.iterator();
        assertNotNull(iterator);
        for(int i=0; i < 14; i++) {
            assertTrue(iterator.hasNext());
            Box box = iterator.next();
            assertEquals(i, box.index());
        }
        assertFalse(iterator.hasNext());
    }



}
