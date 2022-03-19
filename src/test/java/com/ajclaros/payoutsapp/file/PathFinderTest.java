package com.ajclaros.payoutsapp.file;

import lombok.SneakyThrows;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;

import java.io.FileNotFoundException;
import java.nio.file.Path;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

public class PathFinderTest {

    @SneakyThrows
    @ParameterizedTest
    @CsvSource(value = {
            "null, null",
            "null, any-value",
            "any-value, null"},
            nullValues = {"null"})
    void givenANullPathWhenGetPathIsCallShouldReturnException(String path, String startWith) {
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () ->
                PathFinder.getPath(path, startWith));

        assertEquals("Inputs should not be null.", exception.getMessage());
    }

    @Test
    @SneakyThrows
    void givenABadPathWhenGetPathIsCallShouldReturnException() {
        FileNotFoundException exception = assertThrows(FileNotFoundException.class, () ->
                PathFinder.getPath("wrong-path", "path-finder-first"));

        assertEquals("File does not exist.", exception.getMessage());
    }

    @Test
    @SneakyThrows
    void givenABadFileNameStartWhenGetPathIsCallShouldReturnException() {
        FileNotFoundException exception = assertThrows(FileNotFoundException.class, () ->
                PathFinder.getPath("src/test/resources/path-finder/", "wrong-file-start"));

        assertEquals("File does not exist.", exception.getMessage());
    }

    @Test
    @SneakyThrows
    void givenGoodPathAndFileNameAnd2FilesMatchingTheCriteriaWhenGetPathIsCallShouldReturnException() {
        FileNotFoundException exception = assertThrows(FileNotFoundException.class, () ->
                PathFinder.getPath("src/test/resources/path-finder/", "test-"));

        assertEquals("More than one file to process.", exception.getMessage());
    }

    @Test
    @SneakyThrows
    void givenGoodPathAndFileNameAndWhenGetPathIsCallShouldReturnException() {
        Path path = PathFinder.getPath("src/test/resources/path-finder/", "test-first");

        assertEquals(Path.of("src/test/resources/path-finder/", "test-first-file.txt"), path);
    }
}
