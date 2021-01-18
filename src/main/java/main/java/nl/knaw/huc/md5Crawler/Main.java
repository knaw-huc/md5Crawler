package main.java.nl.knaw.huc.md5Crawler;


import com.twmacinta.util.MD5;

import java.io.FileWriter;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Iterator;

public class Main {

    public static void main(String[] args) throws Exception {

        System.err.println("determine dir");
        String directory = ".";
        if(args.length>1) {
            if("-d".equals(args[0].toString())) {
                directory = args[1];
            }
        }
        System.err.println(directory);

        System.err.println("crawl dir and subdirs");
        ArrayList allFiles = new ArrayList();
        Files.walk(Paths.get(directory))
             .forEach(allFiles::add);

        System.err.println("compute md5 for all files");
        System.err.println("write results to csv");
        String outputFilename = "listHash.csv";
        FileWriter myWriter = new FileWriter(outputFilename);

        for (Iterator iter = allFiles.iterator(); iter.hasNext(); ) {
            String filename = iter.next().toString();
            System.err.println(filename);
            if(!Files.isDirectory(Paths.get(filename))) {
                String hash = MD5.asHex(MD5.getHash(new java.io.File(filename)));
                myWriter.write("\"" + filename + "\",\"" + hash + "\"\n");
                myWriter.flush();
            } else {
                System.err.println("is dir?");
            }
        }
        myWriter.close();
    }

}

