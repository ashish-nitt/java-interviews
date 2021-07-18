package digital.number.scanner.service.processors;

import digital.number.scanner.service.model.OutputChar;
import digital.number.scanner.service.model.Symbol;
import digital.number.scanner.service.model.SymbolInfo;
import org.apache.camel.CamelContext;
import org.apache.camel.Exchange;
import org.apache.camel.Processor;
import org.apache.camel.ProducerTemplate;

public class SymbolProcessor implements Processor {
    final CamelContext camelContext;
    final ProducerTemplate producerTemplate;
    final String nextStage;

    public SymbolProcessor(CamelContext camelContext, String nextStage) {
        this.camelContext = camelContext;
        producerTemplate = camelContext.createProducerTemplate();
        this.nextStage = nextStage;
    }

    @Override
    public void process(Exchange exchange) throws Exception {
        Symbol symbol = exchange.getMessage().getBody(Symbol.class);
        if (symbol.isEOL()) {
            producerTemplate.sendBody(nextStage, new OutputChar('\n', true));
        } else {
            producerTemplate.sendBody(nextStage, new OutputChar(parse(symbol.getCharMatrix()), false));
        }
    }

    private char parse(String charMatrix) {
        switch(charMatrix) {
            case " - | ||_|":
                return '0';
            case "     |  |":
                return '1';
            case " _  _||_ ":
                return '2';
            case " _  _| _|":
                return '3';
            case "   |_|  |":
                return '4';
            case " _ |_  _|":
                return '5';
            case " _ |_ |_|":
                return '6';
            case " _   |  |":
                return '7';
            case " _ |_||_|":
                return '8';
            case " _ |_| _|":
                return '9';
            default:
                return '?';
        }
    }
}
