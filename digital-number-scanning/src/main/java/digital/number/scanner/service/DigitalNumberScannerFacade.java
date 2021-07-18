package digital.number.scanner.service;

import digital.number.scanner.service.integration.DigitalNumberScannerContext;

import java.util.Iterator;
import java.util.stream.Stream;

public class DigitalNumberScannerFacade implements DigitalNumberScanner {
    @Override
    public Stream<String> performScanning(Stream<String> inputLines, int startIndex, int endIndex) {
        DigitalNumberScannerContext digitalNumberScannerContext = null;
        try {
            digitalNumberScannerContext = new DigitalNumberScannerContext();
            digitalNumberScannerContext.start();
            int count = 0;
            Iterator<String> iterator = inputLines.iterator();
            while (iterator.hasNext()) {
                if (count < startIndex) {
                    continue;
                }
                if (count >= startIndex && count <= endIndex) {
                    digitalNumberScannerContext.produce(iterator.next());
                    count++;
                } else {
                    break;
                }
            }
            return digitalNumberScannerContext.getOutput().stream();
        } catch (Exception e) {
            return Stream.empty();
        } finally {
            if (digitalNumberScannerContext != null) {
                digitalNumberScannerContext.clear();
            }
        }
    }
}
