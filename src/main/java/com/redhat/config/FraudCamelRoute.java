package com.redhat.config;

import org.apache.camel.builder.RouteBuilder;

public class FraudCamelRoute extends RouteBuilder {

	private String route;
	
	public FraudCamelRoute(String route) {
		super();
		this.route = route;
	}

	@Override
	public void configure() throws Exception {
		from("direct:fraudRoute").to(this.route);
	}

}
