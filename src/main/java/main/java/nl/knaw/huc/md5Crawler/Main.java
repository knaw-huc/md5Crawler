package main.java.nl.knaw.huc.md5Crawler;


import joptsimple.OptionParser;
import joptsimple.OptionSet;

public class Main {

    public static void main(String[] args) throws Exception {

        System.err.println("determine dir");
        String directory = ".";
        OptionParser parser = new OptionParser("d");
        OptionSet options = parser.parse(args);
        if (options.has("f"))
            directory = (String)options.valueOf("f");

        System.err.println("crawl dir and subdirs");
        FileWalker fileWalker = new FileWalker();


        System.err.println("compute md5 for all files");
        System.err.println("write results to csv");
    }

}

