import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.AfterEach;
import java.io.ByteArrayOutputStream;
import java.io.PrintStream;
import java.util.ArrayList;
import static org.junit.jupiter.api.Assertions.*;

public class P4LocalTest {
    private final ByteArrayOutputStream outContent = new ByteArrayOutputStream();
    private final PrintStream originalOut = System.out;

    @BeforeEach
    public void setUpStreams() {
        System.setOut(new PrintStream(outContent));
    }

    @AfterEach
    public void restoreStreams() {
        System.setOut(originalOut);
    }


    @Test
    void testAddBattleLogic() {
        Arena arena = new Arena();
        Sprite s1 = new Sprite("S1", "01", ElementType.WATER, 100, new AttackSkill(10), new AttackSkill(10));
        Sprite s2 = new Sprite("S2", "02", ElementType.WATER, 100, new AttackSkill(10), new AttackSkill(10));
        Sprite s3 = new Sprite("S3", "03", ElementType.WATER, 100, new AttackSkill(10), new AttackSkill(10));

        arena.getSprites().add(s1);
        arena.getSprites().add(s2);

        Battle validBattle = new Battle(s1, s2);
        boolean resultValid = arena.addBattle(validBattle);
        assertTrue(resultValid);
        assertEquals(1, arena.getBattles().size());
        assertTrue(arena.getBattles().contains(validBattle));

        Battle invalidBattle1 = new Battle(s1, s3);
        boolean resultInvalid1 = arena.addBattle(invalidBattle1);
        assertFalse(resultInvalid1);
        assertEquals(1, arena.getBattles().size());

        Battle invalidBattle2 = new Battle(s3, s2);
        boolean resultInvalid2 = arena.addBattle(invalidBattle2);
        assertFalse(resultInvalid2);
        assertEquals(1, arena.getBattles().size());
    }

    @Test
    void testFreeForAllSameElement() {
        Arena arena = new Arena();

        Sprite s1 = new Sprite("S1", "01", ElementType.WATER, 200, new AttackSkill(50), new AttackSkill(50));
        Sprite s2 = new Sprite("S2", "02", ElementType.WATER, 150, new AttackSkill(30), new AttackSkill(30));
        Sprite s3 = new Sprite("S3", "03", ElementType.WATER, 100, new AttackSkill(20), new AttackSkill(20));
        Sprite s4 = new Sprite("S4", "04", ElementType.WATER, 50, new AttackSkill(10), new AttackSkill(10));

        arena.getSprites().add(s1);
        arena.getSprites().add(s2);
        arena.getSprites().add(s3);
        arena.getSprites().add(s4);

        arena.addBattle(new Battle(s1, s2));
        arena.addBattle(new Battle(s3, s4));
        arena.addBattle(new Battle(s1, s3));
        arena.addBattle(new Battle(s2, s4));

        arena.beginFreeForAllBattle();
        arena.show();

        String[] lines = outContent.toString().trim().split("\\r?\\n");
        assertEquals(36, lines.length);

        assertTrue(lines[32].matches("S1 01 4 2"));
        assertTrue(lines[33].matches("S2 02 2 2"));
        assertTrue(lines[34].matches("S3 03 2 2"));
        assertTrue(lines[35].matches("S4 04 0 2"));
    }
}