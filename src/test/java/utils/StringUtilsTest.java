package utils;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

import static org.junit.jupiter.api.Assertions.*;

class StringUtilsTest {

    @ParameterizedTest
    @ValueSource(strings = {"4", "40", "-50"})
    public void should_return_true_for_ints(String v) {
        assertTrue(StringUtils.estEntier(v));
    }

    @Test
    void should_return_true_for_int() {
        assertTrue(StringUtils.estEntier("10"));
    }

    @Test
    void should_return_false_for_char() {
        assertFalse(StringUtils.estEntier("-"));
    }

    @Test
    void should_return_ok_for_number_of_occurrence() {
        assertEquals(2, StringUtils.nbOccurrence("aabbdkjnfreljne", 'e'));
    }

    @Test
    void should_not_found_the_char() {
        assertEquals(0, StringUtils.nbOccurrence("aabbdkjnfreljne", 'z'));
    }
}