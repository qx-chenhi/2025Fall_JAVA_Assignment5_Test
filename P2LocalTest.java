import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

public class P2LocalTest {
    @Test
    void testElementType() {
        assertNotNull(ElementType.METAL);
        assertNotNull(ElementType.WOOD);
        assertNotNull(ElementType.WATER);
        assertNotNull(ElementType.FIRE);
        assertNotNull(ElementType.EARTH);
        assertEquals(5, ElementType.values().length);
    }

    @Test
    void testSpriteConstructorAndGetters() {
        Skill attack = new AttackSkill(10);
        Skill heal = new HealingSkill(20);
        Sprite s = new Sprite("S1", "001", ElementType.FIRE, 100, attack, heal);

        assertEquals("S1", s.getName());
        assertEquals("001", s.getId());
        assertEquals(ElementType.FIRE, s.getElementType());
        assertEquals(100, s.getHp());
        assertEquals(attack, s.getPrimarySkill());
        assertEquals(heal, s.getSecondarySkill());
    }

    @Test
    void testSpriteSetters() {
        Skill attack = new AttackSkill(10);
        Skill heal = new HealingSkill(20);
        Sprite s = new Sprite("S1", "001", ElementType.FIRE, 100, attack, heal);

        s.setName("S2");
        s.setId("002");
        s.setElementType(ElementType.WATER);
        s.setHp(200);
        Skill newPrimary = new AttackSkill(30);
        Skill newSecondary = new StrengthenSkill(5);
        s.setPrimarySkill(newPrimary);
        s.setSecondarySkill(newSecondary);

        assertEquals("S2", s.getName());
        assertEquals("002", s.getId());
        assertEquals(ElementType.WATER, s.getElementType());
        assertEquals(200, s.getHp());
        assertEquals(newPrimary, s.getPrimarySkill());
        assertEquals(newSecondary, s.getSecondarySkill());
    }

    @Test
    void testEffectIntensityValues() {
        assertEquals(5, EffectIntensity.STRENGTHENED.getValue());
        assertEquals(0, EffectIntensity.UNCHANGED.getValue());
        assertEquals(-5, EffectIntensity.WEAKENED.getValue());
    }

    @Test
    void testCompareType() {
        Sprite fire = new Sprite("Fire", "1", ElementType.FIRE, 100, null, null);
        Sprite metal = new Sprite("Metal", "2", ElementType.METAL, 100, null, null);

        assertEquals(EffectIntensity.STRENGTHENED, fire.compareType(metal));
        assertEquals(EffectIntensity.WEAKENED, metal.compareType(fire));
        assertEquals(EffectIntensity.UNCHANGED, fire.compareType(fire));
    }

    @Test
    void testSpriteToString() {
        Skill attack = new AttackSkill(10);
        Skill heal = new HealingSkill(20);
        Sprite s = new Sprite("S1", "001", ElementType.WOOD, 100, attack, heal);
        String expected = "S1 - id: 001, elementType: WOOD, hp: 100, primarySkill: AttackSkill, secondarySkill: HealingSkill";
        assertEquals(expected, s.toString());
    }

    @Test
    void testAttackSkillGetterSetter() {
        AttackSkill skill = new AttackSkill(10);
        assertEquals(10, skill.getSkillStrength());

        skill.setSkillStrength(20);
        assertEquals(20, skill.getSkillStrength());
    }

    @Test
    void testHealingSkillGetterSetter() {
        HealingSkill skill = new HealingSkill(10);
        assertEquals(10, skill.getSkillStrength());

        skill.setSkillStrength(20);
        assertEquals(20, skill.getSkillStrength());
    }

    @Test
    void testStrengthenSkillGetterSetter() {
        StrengthenSkill skill = new StrengthenSkill(10);
        assertEquals(10, skill.getSkillStrength());

        skill.setSkillStrength(20);
        assertEquals(20, skill.getSkillStrength());
    }

    @Test
    void testReleaseSkill() {
        Skill attack = new AttackSkill(50);
        Sprite s1 = new Sprite("Attacker", "001", ElementType.METAL, 100, attack, null);
        assertEquals("Attacker hp(100) - 50", attack.releaseSkill(s1));

        Skill heal = new HealingSkill(30);
        Sprite s2 = new Sprite("Healer", "002", ElementType.WOOD, 80, null, heal);
        assertEquals("Healer hp(80) + 30", heal.releaseSkill(s2));

        Skill primary = new AttackSkill(40);
        Skill buff = new StrengthenSkill(10);
        Sprite s3 = new Sprite("Buffer", "003", ElementType.EARTH, 120, primary, buff);
        assertEquals("Buffer skillStrength(40) + 10", buff.releaseSkill(s3));
    }
}