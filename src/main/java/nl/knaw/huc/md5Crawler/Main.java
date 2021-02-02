package nl.knaw.huc.md5Crawler;


import com.twmacinta.util.MD5;
import joptsimple.OptionParser;
import joptsimple.OptionSet;

import java.io.PrintWriter;
import java.nio.file.Files;
import java.nio.file.NoSuchFileException;
import java.nio.file.Path;
import java.nio.file.Paths;

public class Main {

    private static PrintWriter myWriter;

    public static void main(String[] args) throws Exception {
        String directory = ".";
        String outputFilename = "";
        OptionParser parser = new OptionParser("d:o:?*");
        OptionSet options = parser.parse(args);
        if(options.has("d")) {
            directory = (String)options.valueOf("d");
        }
        if(options.has("o")) {
           outputFilename = (String)options.valueOf("o");
        }
        myWriter = null;
        if(outputFilename!="") {
            myWriter = new PrintWriter(outputFilename);
        } else {
            myWriter = new PrintWriter(System.out);
        }
        try {
            Files.walk(Paths.get(directory))
                 .forEach(Main::calcAndPrint);
            myWriter.close();
        } catch(NoSuchFileException e) {
            System.err.println("directory " + directory + " does not exists.");
        }
    }

    public static void calcAndPrint(Path filename) {
        if(!Files.isDirectory(filename)) {
            String hash = null;
            try {
                hash = MD5.asHex(MD5.getHash(new java.io.File(String.valueOf(filename))));
                myWriter.println("\"" + String.valueOf(filename).replace("\"","\"\"") + "\",\"" + hash + "\"");
            } catch (Exception e) {
                System.err.println(e.getMessage());
                System.err.println(String.valueOf(filename)+" caused an Exception");
            } catch (Error e) {
                System.err.println(e.getMessage());
                System.err.println(String.valueOf(filename)+" caused an Error");
            }
        }
    }
}

