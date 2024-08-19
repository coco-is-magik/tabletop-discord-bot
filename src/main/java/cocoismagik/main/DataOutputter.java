package cocoismagik.main;

import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardOpenOption;
import java.util.Collections;

public class DataOutputter {
    public static boolean writeToFile(Object object){
        try{
            String entry = object.toString();
            String filename = "test.txt";
            Path dirPath = Path.of(DataOutputter.class.getProtectionDomain().getCodeSource().getLocation().toURI());
            Path filePath = dirPath.resolve(filename);
            if(!Files.exists(filePath)){
                Files.createFile(filePath);
            }
            Files.write(filePath, Collections.singletonList(entry), StandardCharsets.UTF_8, StandardOpenOption.APPEND);
            System.out.println(entry);
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            System.out.println(e.getMessage());
            return false;
        }
    }
}
