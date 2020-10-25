package com.advenoh.streams;

import lombok.extern.slf4j.Slf4j;
import org.junit.Test;

import java.util.Arrays;
import java.util.List;
import java.util.NoSuchElementException;

import static org.assertj.core.api.Assertions.assertThat;

@Slf4j
public class MinMaxValueFromListTest {
    /**
     * https://www.baeldung.com/java-collection-min-max
     */
    @Test
    public void test_max_값_얻기() {
        List<Integer> intList = Arrays.asList(2, 3, 6, 4, 10, 23);
        Integer minValue = intList.stream()
                .mapToInt(x -> x)
                .max()
                .orElseThrow(NoSuchElementException::new);

        assertThat(minValue).isEqualTo(2);
    }

}
