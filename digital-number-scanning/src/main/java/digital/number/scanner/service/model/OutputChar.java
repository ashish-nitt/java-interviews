package digital.number.scanner.service.model;

import lombok.Value;

@Value
public class OutputChar {
    final Character character;
    final boolean isEOL;
}
