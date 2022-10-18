package com.example.camel;

import com.fasterxml.jackson.core.JsonParseException;
import org.apache.camel.builder.RouteBuilder;

public class Routes extends RouteBuilder {

    public void configure() {

        onException(ExceptionOne.class, JsonParseException.class)
                .useOriginalMessage()
                .convertBodyTo(String.class)
                .handled(true);

        onException(ExceptionTwo.class)
                .setBody(exchangeProperty("OriginalBody"))
                .convertBodyTo(String.class)
                .handled(true);

        from("direct:convertBodyWithStreamCache").streamCaching()
                .convertBodyTo(String.class)
                .throwException(new ExceptionOne());

        from("direct:convertBodyWithoutStreamCache").noStreamCaching()
                .convertBodyTo(String.class)
                .throwException(new ExceptionOne());

        from("direct:unmarshallWithStreamCache").streamCaching()
                .unmarshal().json()
                .throwException(new ExceptionOne());

        from("direct:unmarshallWithoutStreamCache").noStreamCaching()
                .unmarshal().json()
                .throwException(new ExceptionOne());

        from("direct:convertBodyInvalidUnmarshallWithoutStreamCache").noStreamCaching()
                .convertBodyTo(String.class)
                .unmarshal().json();

        from("direct:noStreamReading").streamCaching()
                .throwException(new ExceptionOne());

        from("direct:withoutOriginalMessage").noStreamCaching()
                .convertBodyTo(String.class)
                .setProperty("OriginalBody", body())
                .throwException(new ExceptionTwo());
    }
}
