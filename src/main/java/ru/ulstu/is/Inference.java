package ru.ulstu.is;

import java.io.IOException;
import java.nio.file.Path;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import ru.ulstu.is.swrlf.SwrlfEngine;

public class Inference {
    public static void main(String[] args) {
        final Logger log = LoggerFactory.getLogger(Inference.class);
        if (args.length != 1) {
            log.error("Ontology is not specified");
            log.error("Usage: swrlf.jar ontology.owx");
            System.exit(1);
        }

        try (final SwrlfEngine swrlfEngine = new SwrlfEngine(Path.of(args[0]))) {
            swrlfEngine.run();
        } catch (IOException e) {
            log.error(e.getMessage());
            e.printStackTrace();
            System.exit(1);
        }
    }
}
