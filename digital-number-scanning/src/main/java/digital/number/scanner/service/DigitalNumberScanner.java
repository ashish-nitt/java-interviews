package digital.number.scanner.service;

import java.util.List;
import java.util.stream.Stream;

public interface DigitalNumberScanner {
    Stream<String> performScanning(Stream<String> inputLines, int startIndex, int endIndex);
}
