package digital.number.scanner.service.model;

import lombok.Value;

@Value
public class SymbolInfo {
    final Chunk chunk;
    final int index;
    final boolean isEOL;
}
