package digital.number.scanner.service.integration;

import digital.number.scanner.service.model.CustomCamelException;
import digital.number.scanner.service.processors.*;
import org.apache.camel.CamelContext;
import org.apache.camel.ConsumerTemplate;
import org.apache.camel.ProducerTemplate;
import org.apache.camel.builder.RouteBuilder;
import org.apache.camel.impl.DefaultCamelContext;

import java.util.List;

public class DigitalNumberScannerContext {
    final CamelContext camelContext;
    final Chunker chunker;
    final ChunkProcessor chunkProcessor;
    final SymbolReader symbolReader;
    final SymbolProcessor symbolProcessor;
    final ErrorProcessor errorProcessor;
    final SuccessProcessor successProcessor;
    final ProducerTemplate startProducer;

    public DigitalNumberScannerContext() throws Exception {
        camelContext = new DefaultCamelContext();
        chunker = new Chunker(camelContext, "direct:chunkProcessor");
        chunkProcessor = new ChunkProcessor(camelContext, "direct:symbolReader");
        symbolReader = new SymbolReader(camelContext, "direct:symbolProcessor");
        symbolProcessor = new SymbolProcessor(camelContext, "direct:successProcessor");
        errorProcessor = new ErrorProcessor();
        successProcessor = new SuccessProcessor();
        camelContext.addRoutes(new RouteBuilder() {
            @Override
            public void configure() {
                onException(CustomCamelException.class).process(errorProcessor).to("stub:nowhere");
                from("direct:dnsStart").process(chunker).to("stub:nowhere");
                from("direct:chunkProcessor").process(chunkProcessor).to("stub:nowhere");
                from("direct:symbolReader").process(symbolReader).to("stub:nowhere");
                from("direct:symbolProcessor").process(symbolProcessor).to("stub:nowhere");
                from("direct:successProcessor").process(successProcessor).to("stub:nowhere");
            }
        });
        startProducer = camelContext.createProducerTemplate();
    }

    public void start() {
        camelContext.start();
    }

    public void produce(String line) {
        if (camelContext.isStarted()) {
            startProducer.sendBody("direct:dnsStart", line);
        }
    }

    public void clear() {
        try {
            camelContext.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public List<String> getOutput() {
        return successProcessor.getOutput();
    }
}
