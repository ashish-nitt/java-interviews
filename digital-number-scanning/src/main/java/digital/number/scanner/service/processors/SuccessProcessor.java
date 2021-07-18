package digital.number.scanner.service.processors;

import digital.number.scanner.service.model.OutputChar;
import org.apache.camel.Exchange;
import org.apache.camel.Processor;

import java.util.ArrayList;
import java.util.List;

public class SuccessProcessor implements Processor {
    List<String> output = new ArrayList<>();
    StringBuilder nextStringBuilder = new StringBuilder("");

    @Override
    public void process(Exchange exchange) throws Exception {
        OutputChar outputChar = exchange.getMessage().getBody(OutputChar.class);
        if (outputChar.isEOL()) {
            output.add(nextStringBuilder.toString());
            nextStringBuilder.setLength(0);
        } else {
            nextStringBuilder.append(outputChar.getCharacter());
        }
    }

    public List<String> getOutput() {
        return output;
    }
}
