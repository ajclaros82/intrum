package com.ajclaros.payoutsapp.file;

import java.io.File;
import java.io.FileNotFoundException;
import java.nio.file.Path;

import static java.util.Objects.isNull;

public class PathFinder {

    public static Path getPath(String filePath, String startsWith) throws FileNotFoundException {
        File[] foundFiles = new File(filePath).listFiles((dir, name) -> name.startsWith(startsWith));

        if (isNull(foundFiles) || foundFiles.length == 0)
            throw new FileNotFoundException("File does not exist.");

        if (foundFiles.length > 1) {
            throw new FileNotFoundException("More than one file to process.");
        }

        return foundFiles[0].toPath();
    }

}
