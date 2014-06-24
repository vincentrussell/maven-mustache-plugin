package com.github.vincentrussell.mustache;


import com.github.mustachejava.Mustache;
import com.github.mustachejava.MustacheFactory;

import java.io.*;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;

public class Mustacheize {

    private final File propertiesFile;
    private final File sourceFile;
    private final File destinationFile;

    private Mustacheize(String propertiesFileLoc, String sourceFileLoc, String destinationFileLoc) throws IOException {
        this.propertiesFile = new File(propertiesFileLoc);
        this.sourceFile = new File(sourceFileLoc);
        this.destinationFile = new File(destinationFileLoc);

        validateFileExistence(propertiesFile);
        validateFileExistence(sourceFile);


    }

    private void validateFileExistence(File file) throws FileNotFoundException {
        if (!file.exists()) {
            throw new FileNotFoundException(file.getAbsolutePath() + " file not found");
        }
    }

    private void doMustache() throws IOException {
        Writer writer = new FileWriter(destinationFile);
        MustacheFactory mf = new NonHtmlEscapingDefaultMustacheFactory();
        Mustache mustache = mf.compile(new FileReader(sourceFile), "mustacheize");

        Properties props = getPropertiesFromPropertiesFile(propertiesFile);
        Map<String,String> propertiesMap = propertiesToMap(props);

        mustache.execute(writer, propertiesMap).flush();

    }

    private Properties getPropertiesFromPropertiesFile(File propertiesFile) throws IOException {
        Properties properties = new Properties();
        properties.load(new FileInputStream(propertiesFile));
        return properties;
    }

    public static Map<String, String> propertiesToMap(Properties props) {
        HashMap<String, String> hm = new HashMap<String,String>();
        Enumeration<Object> e = props.keys();
        while (e.hasMoreElements()) {
            String s = (String)e.nextElement();
            hm.put(s, props.getProperty(s));
        }
        return hm;
    }

    public static void main(String[] args) {
        String propertiesFileLoc = System.getProperty("propertiesFile");
        String sourceFileLoc = System.getProperty("sourceFile");
        String destinationFileLoc = System.getProperty("destinationFile");

        try {
            Mustacheize mustacheize = new Mustacheize(propertiesFileLoc, sourceFileLoc, destinationFileLoc);
            mustacheize.doMustache();

        } catch (IOException e) {
            e.printStackTrace();
            System.exit(1);
        }



    }
}
