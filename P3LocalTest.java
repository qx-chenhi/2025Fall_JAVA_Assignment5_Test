import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;

import static org.junit.jupiter.api.Assertions.*;

public class P3LocalTest {
    private final ByteArrayOutputStream outContent = new ByteArrayOutputStream();
    private final PrintStream originalOut = System.out;
    private String normalize(String output) {
        return output.replace("\r\n", "\n").replace('\r', '\n').trim();
    }

    @BeforeEach
    void setUp() {
        System.setOut(new PrintStream(outContent));
    }

    @Test
    void tearDown() {
        System.setOut(originalOut);
    }

    @Test
    void testBattleOutcomeEnumValues() {
        assertEquals(0, BattleOutcome.DEFEAT.getValue());
        assertEquals(1, BattleOutcome.DRAW.getValue());
        assertEquals(2, BattleOutcome.VICTORY.getValue());
        assertEquals(3, BattleOutcome.values().length);
        assertNotNull(BattleOutcome.valueOf("DEFEAT"));
        assertNotNull(BattleOutcome.valueOf("DRAW"));
        assertNotNull(BattleOutcome.valueOf("VICTORY"));
    }

    @Test
    void testBegin1v1BattleVictory() {
        Skill homePrimary = new AttackSkill(60);
        Skill homeSecondary = new AttackSkill(40);
        Sprite home = new Sprite("Home", "H01", ElementType.FIRE, 80, homePrimary, homeSecondary);

        Skill awayPrimary = new AttackSkill(10);
        Skill awaySecondary = new AttackSkill(10);
        Sprite away = new Sprite("Away", "A01", ElementType.WOOD, 40, awayPrimary, awaySecondary);

        Battle battle = new Battle(home, away);
        assertEquals(BattleOutcome.VICTORY, battle.begin1v1Battle());
    }

    @Test
    void testBegin1v1BattleDefeat() {
        Skill homePrimary = new AttackSkill(10);
        Skill homeSecondary = new AttackSkill(10);
        Sprite home = new Sprite("Home", "H02", ElementType.WOOD, 40, homePrimary, homeSecondary);

        Skill awayPrimary = new AttackSkill(50);
        Skill awaySecondary = new AttackSkill(50);
        Sprite away = new Sprite("Away", "A02", ElementType.FIRE, 120, awayPrimary, awaySecondary);

        Battle battle = new Battle(home, away);
        assertEquals(BattleOutcome.DEFEAT, battle.begin1v1Battle());
        String expectedOutput =
                """
                Away hp(120) - 10
                Home hp(40) - 50
                """;

        assertEquals(normalize(expectedOutput), normalize(outContent.toString()));
    }

    @Test
    void testBegin1v1BattleDraw() {
        Skill homePrimary = new HealingSkill(15);
        Skill homeSecondary = new HealingSkill(15);
        Sprite home = new Sprite("Home", "H03", ElementType.WATER, 200, homePrimary, homeSecondary);

        Skill awayPrimary = new HealingSkill(15);
        Skill awaySecondary = new HealingSkill(15);
        Sprite away = new Sprite("Away", "A03", ElementType.WATER, 200, awayPrimary, awaySecondary);

        Battle battle = new Battle(home, away);
        assertEquals(BattleOutcome.DRAW, battle.begin1v1Battle());

    }

    @Test
    void testBegin1v1Battle() {
        Skill homePrimary = new AttackSkill(25);
        Skill homeSecondary = new StrengthenSkill(10);
        Sprite home = new Sprite("BuffedFire", "H04", ElementType.FIRE, 80, homePrimary, homeSecondary);

        Skill awayPrimary = new AttackSkill(30);
        Skill awaySecondary = new StrengthenSkill(8);
        Sprite away = new Sprite("DebuffedMetal", "A04", ElementType.METAL, 80, awayPrimary, awaySecondary);

        Battle battle = new Battle(home, away);
        BattleOutcome outcome = battle.begin1v1Battle();
/*
        R0  Home Attack 30   -> Away HP 80 - 30 = 50
        R1  Away Attack 25   -> Home HP 80 - 25 = 55
        R2  Home Strengthen 15  -> Home 主技能 30 + 15 = 45
        R3  Away Strengthen 3   -> Away 主技能 25 + 3 = 28
        R4  Home Attack 45   -> Away HP 50 - 45 = 5
        R5  Away Attack 28   -> Home HP 55 - 28 = 27
        R6  Home Strengthen 15 -> Home 主技能 45 + 15 = 60
        R7  Away Strengthen 3  -> Away 主技能 28 + 3 = 31
        R8  Home Attack 60   -> Away HP 5 - 60 = 0

*/
        assertEquals(BattleOutcome.VICTORY, outcome);
        String expectedOutput =
                """
                DebuffedMetal hp(80) - 30
                BuffedFire hp(80) - 25
                BuffedFire skillStrength(30) + 15
                DebuffedMetal skillStrength(25) + 3
                DebuffedMetal hp(50) - 45
                BuffedFire hp(55) - 28
                BuffedFire skillStrength(45) + 15
                DebuffedMetal skillStrength(28) + 3
                DebuffedMetal hp(5) - 60
                """;
        assertEquals(normalize(expectedOutput), normalize(outContent.toString()));
    }
}