package de.unibremen.swp;

import de.unibremen.swp.data.Converter;

import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

import static java.nio.charset.StandardCharsets.UTF_8;

public class Main {

    public static void main(final String[] args) {
        if (args.length != 2) {
            System.out.println("Usage: <input> <output>");
            System.exit(0);
        }

        final Path in = Paths.get(args[0]);
        final Path out = Paths.get(args[1]);

        // Check input and output formats.
        if (!args[0].endsWith(".csv") && !args[0].endsWith(".json")) {
            System.err.println("Unsupported input format: "
                    + in.getFileName());
            System.exit(-1);
        }
        if (!args[1].endsWith(".csv") && !args[1].endsWith(".json")) {
            System.err.println("Unsupported output format: "
                    + out.getFileName());
            System.exit(-1);
        }

        // Copy or convert input to output.
        try {
            if (args[0].endsWith(".csv") && args[1].endsWith(".csv") ||
                    args[0].endsWith(".json") && args[1].endsWith(".json")) {
                Files.copy(in, out);
            } else {
                final Converter converter = new Converter();
                final String input = Files.readString(in, UTF_8);
                final String output = args[0].endsWith(".csv")
                        ? converter.csvToJSON(input)
                        : converter.jsonToCSV(input);
                Files.writeString(out, output, UTF_8);
            }
        } catch (final Exception e) {
            System.err.println("Error: " + e.getMessage());
            System.exit(-1);
        }
    }
}
