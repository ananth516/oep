package com.oracle.oep.troubleticket.route;

import org.apache.camel.builder.RouteBuilder;
import org.apache.camel.converter.jaxb.JaxbDataFormat;

import com.oracle.oep.troubleticket.xsd.CreateTroubleTicketRequest;

public class TroubleTicketServiceRoute extends RouteBuilder {

	@Override
	public void configure() throws Exception {
		
		JaxbDataFormat jaxb = new JaxbDataFormat(CreateTroubleTicketRequest.class.getPackage().getName());
		
		from("spring-ws:rootqname:{http://xmlns.oracle.com/OEP/TroubleTicketService/V1}CreateTroubleTicketRequest?endpointMapping=#ttEndpointMapping")		
			.unmarshal(jaxb)
			.process("troubleTicketServiceProcessor")
			.marshal(jaxb);
		
		from("direct:queryDispositionParams")
			.to("sql:classpath:sql/queryDispositionParams.sql?dataSource=#oapDataSource");
		
		from("direct:saveLogMaster")
        	.to("oapJpa:com.oracle.oep.troubleticket.model.OapCallLogMasterT");
		
			
	}

}
