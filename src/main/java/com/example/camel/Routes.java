package com.example.camel;

import org.apache.camel.builder.RouteBuilder;

public class Routes extends RouteBuilder {

    public void configure() {

        onException()
                .useOriginalMessage()
                .convertBodyTo(String.class)
                .handled(true);

        from("direct:convertBodyWithStreamCache").streamCaching()
                .convertBodyTo(String.class)
                .throwException(new IllegalArgumentException());

        from("direct:convertBodyWithoutStreamCache").noStreamCaching()
                .convertBodyTo(String.class)
                .throwException(new IllegalArgumentException());

        from("direct:unmarshallWithStreamCache").streamCaching()
                .unmarshal().json()
                .throwException(new IllegalArgumentException());

        from("direct:unmarshallWithoutStreamCache").noStreamCaching()
                .unmarshal().json()
                .throwException(new IllegalArgumentException());

        from("direct:convertBodyInvalidUnmarshallWithoutStreamCache").noStreamCaching()
                .convertBodyTo(String.class)
                .unmarshal().json()
                .throwException(new IllegalArgumentException());

        from("direct:noStreamReading").streamCaching()
                .throwException(new IllegalArgumentException());
    }
}
