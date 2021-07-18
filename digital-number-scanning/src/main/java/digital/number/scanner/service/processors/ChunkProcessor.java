package digital.number.scanner.service.processors;

import digital.number.scanner.service.model.Chunk;
import digital.number.scanner.service.model.CustomCamelException;
import digital.number.scanner.service.model.Symbol;
import digital.number.scanner.service.model.SymbolInfo;
import org.apache.camel.CamelContext;
import org.apache.camel.Exchange;
import org.apache.camel.Processor;
import org.apache.camel.ProducerTemplate;

import java.util.concurrent.SynchronousQueue;

public class ChunkProcessor implements Processor {
    final CamelContext camelContext;
    final ProducerTemplate producerTemplate;
    final String nextStage;

    public ChunkProcessor(CamelContext camelContext, String nextStage) {
        this.camelContext = camelContext;
        producerTemplate = camelContext.createProducerTemplate();
        this.nextStage = nextStage;
    }

    @Override
    public void process(Exchange exchange) throws Exception {
        Chunk chunk = exchange.getMessage().getBody(Chunk.class);
        String[] lines = chunk.getLines();
        if (lines.length == 3 && lines[0].length() == 27 && lines[1].length() == 27 && lines[2].length() == 27) {
            for (int i = 0; i < 9; i++) {
                producerTemplate.sendBody(nextStage, new SymbolInfo(chunk, i, false));
            }
            producerTemplate.sendBody(nextStage, new SymbolInfo(chunk, lines.length/3, true));
        } else {
            throw new CustomCamelException("Couldn't process chunk");
        }
    }
}
