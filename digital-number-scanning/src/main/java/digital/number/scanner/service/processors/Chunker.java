package digital.number.scanner.service.processors;

import digital.number.scanner.service.model.Chunk;
import lombok.RequiredArgsConstructor;
import org.apache.camel.*;
import org.springframework.util.ObjectUtils;

import java.util.LinkedList;
import java.util.List;

import static org.springframework.util.ObjectUtils.isEmpty;

public class Chunker implements Processor {
    static final int MAX_SIZE = 3;
    enum ChunkerState {INITIAL, DELIM, DATA};
    ChunkerState chunkerState = ChunkerState.INITIAL;
    List<String> lines = new LinkedList<>();

    final CamelContext camelContext;
    final ProducerTemplate producerTemplate;
    final String nextStage;

    public Chunker(CamelContext camelContext, String nextStage) {
        this.camelContext = camelContext;
        producerTemplate = camelContext.createProducerTemplate();
        this.nextStage = nextStage;
    }

    @Override
    public void process(Exchange exchange) throws Exception {
        String line = exchange.getMessage().getBody(String.class);
        if (chunkerState == ChunkerState.INITIAL) {
            if (isEmpty(line)) {
                chunkerState = ChunkerState.DELIM;
            } else {
                chunkerState = ChunkerState.DATA;
            }
        } else if (lines.size() < 3) {
            chunkerState = ChunkerState.DATA;
        } else {
            lines.clear();
            chunkerState = ChunkerState.DELIM;
        }
        if (chunkerState == ChunkerState.DATA) {
            lines.add(line);
            if (lines.size() == 3) {
                producerTemplate.sendBody(nextStage, new Chunk(new String[] {lines.get(0), lines.get(1), lines.get(2)}));
            }
        }
    }
}
