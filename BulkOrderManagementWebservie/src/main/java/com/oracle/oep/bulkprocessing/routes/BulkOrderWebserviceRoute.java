package com.oracle.oep.bulkprocessing.routes;

import org.apache.camel.LoggingLevel;
import org.apache.camel.builder.RouteBuilder;
import org.apache.camel.model.dataformat.JaxbDataFormat;
import org.apache.camel.support.builder.Namespaces;
import org.springframework.beans.factory.annotation.Autowired;
import org.w3c.dom.Document;

import com.oracle.oep.bulkordermanagement.order.v1.BulkOrderEntryRequest;
import com.oracle.oep.bulkprocessing.processors.BulkOrderEntryWebserviceProcessor;
import com.oracle.oep.bulkprocessing.services.BulkOrderWebservice;

public class BulkOrderWebserviceRoute extends RouteBuilder{
	
	
	@Autowired
	private BulkOrderWebservice bulkOrderWebservice;
	
    public void configure() throws Exception {
        JaxbDataFormat jaxb = new JaxbDataFormat();
        jaxb.setContextPath(BulkOrderEntryRequest.class.getPackage().getName());

        BulkOrderEntryWebserviceProcessor webServiceProcessor = new BulkOrderEntryWebserviceProcessor();

        

        
        errorHandler(deadLetterChannel("weblogicJMS:OEP_DEADLETTER_CHANNEL").useOriginalMessage());
        
        Namespaces ns = new Namespaces();
		ns.add("oep", "http://xmlns.oracle.com.mv/OEP/BulkOrderManagement/Order/V1");
        
        from("spring-ws:rootqname:{http://xmlns.oracle.com.mv/OEP/BulkOrderManagement/Order/V1}BulkOrderEntryRequest?endpointMapping=#endpointMapping")
        .log(LoggingLevel.INFO, "spring-ws payload bulkOrderRequest : \n ${in.body}")
        .unmarshal(jaxb)
        .choice()
		.when(xpath("local-name(./oep:BulkOrderEntryRequest/*) = 'ProcessBulkOrderRequest'", ns))
		.bean(bulkOrderWebservice,"processOrder")
		.when(xpath("local-name(./oep:BulkOrderEntryRequest/*) = 'QueryWithFileOrderIdRequest'", ns))
		.bean(bulkOrderWebservice,"queryByFileOrderId")
		.when(xpath("local-name(./oep:BulkOrderEntryRequest/*) = 'QueryWithBatchIdRequest'", ns))
		.bean(bulkOrderWebservice,"queryByBatchId")
		.when(xpath("local-name(./oep:BulkOrderEntryRequest/*) = 'QueryFailedRecordsRequest'", ns))
		.bean(bulkOrderWebservice,"queryFailedRecords")
		.otherwise()
        	.process(webServiceProcessor)
        .marshal(jaxb);
        

        from("direct:jmsQueueChannel")
        .log(LoggingLevel.INFO, "bulkOrderRequest: jmsQueueChannel : \n ${in.body}")
        .to("weblogicJMS:OEP_BULKORDER_JMS_QUEUE?exchangePattern=InOnly");
        
        from("direct:queryBulkJobMaster")
        .to("jpa:com.oracle.oep.bulkprocessing.model.BulkJobMaster?query=select b from com.oracle.oep.bulkprocessing.model.BulkJobMaster b where b.fileOrderId=:param&parameters=\"param\":${in.body}");
        
        from("direct:queryBulkBatchMasterByJobId")
        .to("jpa:com.oracle.oep.bulkprocessing.model.BulkBatchMaster?query=select ba from com.oracle.oep.bulkprocessing.model.BulkBatchMaster ba join ba.bulkJobMaster b where b.jobId=:param&parameters=\"param\":${in.body}");

        from("direct:queryBulkBatchMaster")
        .to("jpa:com.oracle.oep.bulkprocessing.model.BulkBatchMaster?query=select b from com.oracle.oep.bulkprocessing.model.BulkBatchMaster b where b.batchId=:param&parameters=\"param\":${in.body}");
        
        from("direct:queryOrdersByBatchId")
        .to("jpa:com.oracle.oep.bulkprocessing.model.OepOrder?query=select o from com.oracle.oep.bulkprocessing.model.OepOrder o join o.bulkBatchMaster ba where ba.batchId=:param&parameters=param:${in.body}");
        
        from("direct:convertBodyToDOM").convertBodyTo(Document.class);

        
    }
}
