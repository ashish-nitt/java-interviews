package digital.number.scanner.service.processors;

import org.apache.camel.ErrorHandlerFactory;
import org.apache.camel.Exchange;
import org.apache.camel.Processor;

public class ErrorProcessor implements Processor, ErrorHandlerFactory {
    @Override
    public void process(Exchange exchange) throws Exception {
        String errorMessage = exchange.getMessage().getBody(String.class);
        System.out.println("ERROR " + errorMessage);
    }
}
