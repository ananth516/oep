package com.oracle.oep.viewusage.route;

import org.apache.camel.builder.RouteBuilder;
import org.apache.camel.converter.jaxb.JaxbDataFormat;

import com.oracle.oep.viewusage.xsd.CUSOPSUBSCRIPTIONVIEWUSAGEInputFlistRequest;

public class ViewUsageServiceRoute extends RouteBuilder {

	@Override
	public void configure() throws Exception {
		// TODO Auto-generated method stub
		JaxbDataFormat jaxb = new JaxbDataFormat(CUSOPSUBSCRIPTIONVIEWUSAGEInputFlistRequest.class.getPackage().getName());
		
		from("spring-ws:rootqname:{http://xmlns.oracle.com/OEP/ViewUsage/V1}CUS_OP_SUBSCRIPTION_VIEW_USAGE_inputFlistRequest?endpointMapping=#uvEndpointMapping")
		.unmarshal(jaxb)
		.process("viewUsageServiceProcessor")
		.marshal(jaxb);
		
		//from("direct:callViewUsageProc").to("sql-stored:classpath:sql/viewUsage.sql?dataSource=#brmDataSource");
	}

}
