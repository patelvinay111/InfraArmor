package org.infraarmor.app;

import java.io.File;
import java.io.IOException;

public class ShotsLoader {
    public static String loadShotsContent(String shotsPath) throws IOException {
        StringBuilder shots = new StringBuilder();
        File shotsDir = new File(shotsPath);
        File[] listOfShots = shotsDir.listFiles();
        if (listOfShots != null) {
            for (File shot : listOfShots) {
                if (shot.isFile()) {
                    shots.append(FileLoader.loadFileContent(shot.getPath())).append("\n");
                }
            }
        }
        return shots.toString();
    }
}