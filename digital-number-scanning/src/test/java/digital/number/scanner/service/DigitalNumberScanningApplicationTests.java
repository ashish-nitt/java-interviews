package digital.number.scanner.service;

import org.junit.jupiter.api.BeforeEach;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

@SpringBootTest
public class DigitalNumberScanningApplicationTests extends BaseScannerServiceIntegrationTest {
	@Autowired
	DigitalNumberScannerFacade digitalNumberScanner;
	int startIndex = 0;
	int maxIndex = 400;

	public DigitalNumberScanningApplicationTests() {
		this.digitalNumberScanner = new DigitalNumberScannerFacade();
	}

	@Override
	protected List<String> performScanning(String inputFilePath) {
		try {
			return digitalNumberScanner
					.performScanning(Files.readAllLines(Paths.get(inputFilePath)).stream(), 0, 400)
					.collect(Collectors.toList());
		} catch (IOException e) {
			e.printStackTrace();
		}
		return Collections.emptyList();
	}
}
