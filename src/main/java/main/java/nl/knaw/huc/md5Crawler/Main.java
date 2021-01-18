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
        String outputFilename = "listHash.csv";

        for(int i=0; i< args.length; i++) {
            if("-d".equals(args[i].toString())) {
                if(args[i+1]!=null) {
                    directory = args[i + 1];
                }
            }
            if("-o".equals(args[i].toString())) {
                if(args[i+1]!=null) {
                    outputFilename = args[i + 1];
                }
            }
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

