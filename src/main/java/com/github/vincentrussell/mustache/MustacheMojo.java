package com.github.vincentrussell.mustache;

import com.github.mustachejava.Mustache;
import com.github.mustachejava.MustacheFactory;
import org.apache.maven.plugin.AbstractMojo;
import org.apache.maven.plugin.MojoExecutionException;
import org.apache.maven.plugin.MojoFailureException;
import org.apache.maven.plugins.annotations.LifecyclePhase;
import org.apache.maven.plugins.annotations.Mojo;
import org.apache.maven.plugins.annotations.Parameter;
import org.apache.maven.plugins.annotations.ResolutionScope;

import java.io.*;
import java.util.*;

@Mojo(name = "mustache", defaultPhase = LifecyclePhase.PRE_INTEGRATION_TEST, requiresDependencyResolution = ResolutionScope.TEST)
public class MustacheMojo extends AbstractMojo {

    @Parameter(property = "mustacheConfig", required = true)
    private List<MustacheConfig> mustacheConfigList;

    private void doMustache(String propertiesFile, String sourceFile, String destinationFile) throws IOException {
        Writer writer = new FileWriter(destinationFile);
        MustacheFactory mf = new NonHtmlEscapingDefaultMustacheFactory();
        Mustache mustache = mf.compile(new FileReader(sourceFile), "mustacheize");

        Properties props = getPropertiesFromPropertiesFile(new File(propertiesFile));
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

    @Override
    public void execute() throws MojoExecutionException, MojoFailureException {
        for (MustacheConfig mustacheConfig : mustacheConfigList) {
            try {
                doMustache(mustacheConfig.getPropertiesFile(),mustacheConfig.getSourceFile(),mustacheConfig.getDestinationFile());
            } catch (IOException e) {
                throw new MojoExecutionException(e.getMessage(),e);
            }
        }

    }
}
