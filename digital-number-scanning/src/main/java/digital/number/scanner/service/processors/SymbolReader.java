package digital.number.scanner.service.processors;

import digital.number.scanner.service.model.Chunk;
import digital.number.scanner.service.model.Symbol;
import digital.number.scanner.service.model.SymbolInfo;
import org.apache.camel.CamelContext;
import org.apache.camel.Exchange;
import org.apache.camel.Processor;
import org.apache.camel.ProducerTemplate;

public class SymbolReader implements Processor {
    final CamelContext camelContext;
    final ProducerTemplate producerTemplate;
    final String nextStage;

    public SymbolReader(CamelContext camelContext, String nextStage) {
        this.camelContext = camelContext;
        producerTemplate = camelContext.createProducerTemplate();
        this.nextStage = nextStage;
    }

    @Override
    public void process(Exchange exchange) throws Exception {
        SymbolInfo symbolInfo = exchange.getMessage().getBody(SymbolInfo.class);
        String[] lines = symbolInfo.getChunk().getLines();
        int i = symbolInfo.getIndex();
        if (symbolInfo.isEOL()) {
            producerTemplate.sendBody(nextStage, new Symbol(null, true));
        } else {
            if (i < 9) {
                Symbol symbol = new Symbol(String.valueOf(new char[] {
                        lines[0].charAt(i * 3), lines[0].charAt(i * 3 + 1), lines[0].charAt(i * 3 + 2),
                        lines[1].charAt(i * 3), lines[1].charAt(i * 3 + 1), lines[1].charAt(i * 3 + 2),
                        lines[2].charAt(i * 3), lines[2].charAt(i * 3 + 1), lines[2].charAt(i * 3 + 2)
                }), false);
                producerTemplate.sendBody(nextStage, symbol);
            }
        }
    }
}
