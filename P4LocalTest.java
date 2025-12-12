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

    private String normalize(String output) {
        return output.replace("\r\n", "\n").replace('\r', '\n').trim();
    }
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
        assertEquals(20, lines.length);

        assertTrue(lines[16].matches("S1 01 4 2"));
        assertTrue(lines[17].matches("S2 02 2 2"));
        assertTrue(lines[18].matches("S3 03 2 2"));
        assertTrue(lines[19].matches("S4 04 0 2"));
    }

    
    @Test
    void testFreeForAllAwayDrawAndDefeat() {
        Arena arena = new Arena();

        Sprite s1 = new Sprite("S1", "01", ElementType.WATER, 200, new AttackSkill(50), new AttackSkill(50));
        Sprite s2 = new Sprite("S2", "02", ElementType.WATER, 150, new HealingSkill(30), new HealingSkill(30));
        Sprite s3 = new Sprite("S3", "03", ElementType.WATER, 100, new HealingSkill(20), new HealingSkill(20));

        arena.getSprites().add(s1);
        arena.getSprites().add(s2);
        arena.getSprites().add(s3);

        arena.addBattle(new Battle(s1, s2));
        arena.addBattle(new Battle(s3, s2));

        arena.beginFreeForAllBattle();
        arena.show();

        String[] lines = outContent.toString().trim().split("\\r?\\n");
        assertEquals(54, lines.length);

        assertTrue(lines[51].matches("S1 01 2 1"));
        assertTrue(lines[52].matches("S3 03 1 1"));
        assertTrue(lines[53].matches("S2 02 1 2"));
    }
    
    @Test
    void test() {
        Arena arena = new Arena();
        arena.setSprites(new ArrayList<>());

        Sprite s1 = new Sprite("S1", "0002", ElementType.WATER, 200, new AttackSkill(50), new AttackSkill(50));
        Sprite s2 = new Sprite("S2", "0001", ElementType.WATER, 150, new AttackSkill(30), new AttackSkill(30));
        Sprite s3 = new Sprite("S3", "0003", ElementType.WATER, 100, new AttackSkill(20), new AttackSkill(20));
        Sprite s4 = new Sprite("S4", "0004", ElementType.WATER, 50, new AttackSkill(10), new AttackSkill(10));

        arena.getSprites().add(s1);
        arena.getSprites().add(s2);
        arena.getSprites().add(s3);
        arena.getSprites().add(s4);

        arena.addBattle(new Battle(s1, s2));
        arena.addBattle(new Battle(s1, s3));
        arena.addBattle(new Battle(s2, s4));
        arena.addBattle(new Battle(s3, s4));

        arena.beginFreeForAllBattle();
        arena.show();

        String expectedOutput =
                """
                S2 hp(150) - 50
                S1 hp(200) - 30
                S2 hp(100) - 50
                S1 hp(170) - 30
                S2 hp(50) - 50
                S3 hp(100) - 50
                S1 hp(200) - 20
                S3 hp(50) - 50
                S4 hp(50) - 30
                S2 hp(150) - 10
                S4 hp(20) - 30
                S4 hp(50) - 20
                S3 hp(100) - 10
                S4 hp(30) - 20
                S3 hp(90) - 10
                S4 hp(10) - 20
                S1 0002 4 2
                S2 0001 2 2
                S3 0003 2 2
                S4 0004 0 2
                """;


        assertEquals(normalize(expectedOutput), normalize(outContent.toString()));
    }
}

