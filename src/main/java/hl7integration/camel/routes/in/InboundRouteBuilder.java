package hl7integration.camel.routes.in;

import hl7integration.camel.Processor;
import org.apache.camel.spring.SpringRouteBuilder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

@Component
public class InboundRouteBuilder extends SpringRouteBuilder {
    private static final Logger log = LoggerFactory.getLogger(InboundRouteBuilder.class);

    @Override
    public void configure() throws Exception {

        Processor processor = new Processor();
        String triggerStartDir = processor.getPropValues("trigger-start-dir");
        String triggerEndDir = processor.getPropValues("trigger-end-dir");
        from("file:" + triggerStartDir + "?delete=true")
                .to("file:" + triggerEndDir).routeId("Transformer-Camel-Route")
                .to("bean:processor?method=generateCDAFiles")
//                .to("bean:respondACK?method=process")
                .end();
    }
}
