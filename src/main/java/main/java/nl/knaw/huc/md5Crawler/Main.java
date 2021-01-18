package main.java.nl.knaw.huc.md5Crawler;


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
        for (Iterator iter = allFiles.iterator(); iter.hasNext(); ) {
            System.err.println(iter.next().toString());
        }

        System.err.println("compute md5 for all files");
        System.err.println("write results to csv");
    }

}

