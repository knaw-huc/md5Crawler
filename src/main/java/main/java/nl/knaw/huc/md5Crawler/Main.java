package main.java.nl.knaw.huc.md5Crawler;


import com.twmacinta.util.MD5;
import joptsimple.OptionParser;
import joptsimple.OptionSet;

import java.io.FileWriter;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Iterator;

public class Main {

    public static void main(String[] args) throws Exception {

        System.err.println("determine dir");
        String directory = ".";
        String outputFilename = "listHash.csv";

        OptionParser parser = new OptionParser("d:o:?*");
        OptionSet options = parser.parse(args);
        if(options.has("d")) {
            directory = (String)options.valueOf("d");
        }
        if(options.has("o")) {
           outputFilename = (String)options.valueOf("o");
        }

        ArrayList allFiles = new ArrayList();
        Files.walk(Paths.get(directory))
             .forEach(allFiles::add);

        FileWriter myWriter = new FileWriter(outputFilename);

        for (Iterator iter = allFiles.iterator(); iter.hasNext(); ) {
            String filename = iter.next().toString();
            if(!Files.isDirectory(Paths.get(filename))) {
                String hash = MD5.asHex(MD5.getHash(new java.io.File(filename)));
                myWriter.write("\"" + filename + "\",\"" + hash + "\"\n");
                myWriter.flush();
            }
        }
        myWriter.close();
    }

}

