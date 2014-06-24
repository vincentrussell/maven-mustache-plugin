package com.github.vincentrussell.mustache;

import com.github.mustachejava.DefaultMustacheFactory;
import com.github.mustachejava.MustacheException;

import java.io.IOException;
import java.io.Writer;

public class NonHtmlEscapingDefaultMustacheFactory extends DefaultMustacheFactory {
    @Override
    public void encode(String value, Writer writer) {
        try {
            writer.append(value);
        } catch (IOException e) {
            throw new MustacheException("Failed to encode value: " + value);
        }
    }
}
