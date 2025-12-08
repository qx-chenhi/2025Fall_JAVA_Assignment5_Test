import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class P1LocalTest {

    @Test
    void testConstructorAndGetters() {
        Student s1 = new Student("123456", "s1", 88);
        assertEquals("123456", s1.getId());
        assertEquals("s1", s1.getName());
        assertEquals(88, s1.getScore());
    }

    @Test
    void testSetters() {
        Student s1 = new Student("123456", "s1", 88);

        s1.setName("NewName");
        s1.setId("654321");
        s1.setScore(99);

        assertEquals("NewName", s1.getName());
        assertEquals("654321", s1.getId());
        assertEquals(99, s1.getScore());
    }

    @Test
    void testToString() {
        Student s1 = new Student("123456", "s1", 88);
        String expected = "s1 - id: 123456, score: 88";
        assertEquals(expected, s1.toString());
    }

    @Test
    void testCompareToDifferentScores() {
        Student s1 = new Student("000001", "s1", 90);
        Student s2 = new Student("000002", "s2", 80);

        assertTrue(s1.compareTo(s2) < 0);
        assertTrue(s2.compareTo(s1) > 0);
    }

    @Test
    void testCompareToSameScore() {
        Student s1 = new Student("123456", "s1", 90);
        Student s2 = new Student("456789", "s2", 90);

        assertTrue(s1.compareTo(s2) < 0);
        assertTrue(s2.compareTo(s1) > 0);
    }
}