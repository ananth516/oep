package com.oracle.oep.order.service.impl;

import java.util.Optional;

import javax.transaction.Transactional;
import javax.xml.datatype.DatatypeConfigurationException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.oracle.oep.order.constants.OEPConstants;
import com.oracle.oep.order.model.ListOfOEPQueryOrderDetailsResponseType;
import com.oracle.oep.order.model.ListOfOEPTaskHistory;
import com.oracle.oep.order.model.OEPHistoryType;
import com.oracle.oep.order.model.OEPOrderTaskResponseType;
import com.oracle.oep.order.model.OrderEntryRequest;
import com.oracle.oep.order.model.OrderEntryResponse;
import com.oracle.oep.order.model.ProcessQueryOrderDetailsResponseType;
import com.oracle.oep.order.presistence.model.OepOrder;
import com.oracle.oep.order.presistence.model.OepOrderLine;
import com.oracle.oep.order.presistence.repository.OepOrderRepository;
import com.oracle.oep.order.service.QueryOrderDetailsService;
import com.oracle.oep.order.utils.OrderDataUtils;

@Service
public class QueryOrderDetailsServiceImpl implements QueryOrderDetailsService {
	
	private static final Logger log = LoggerFactory.getLogger(QueryOrderDetailsServiceImpl.class);
	
	@Autowired
	private OepOrderRepository oepOrderRepo;

	@Override
	@Transactional
	public OrderEntryResponse processQueryOrderDetailsRequest(OrderEntryRequest request) {
		
		OrderEntryResponse response = new OrderEntryResponse();
		ProcessQueryOrderDetailsResponseType queryOrderDetailsResponse = new ProcessQueryOrderDetailsResponseType();
		
		try{
			
			Optional<OepOrder> oepOrderOptional = Optional.ofNullable(oepOrderRepo.findByOrderId(request.getProcessQueryOrderDetails().getOrderId()));
			
			if(oepOrderOptional.isPresent())
				createOrderDetailsResponse(queryOrderDetailsResponse,oepOrderOptional.get());
			
			else
				queryOrderDetailsResponse.setStatus(OEPConstants.NO_RECORDS_FOUND);	
			
		}
		catch(Exception e){
			
			queryOrderDetailsResponse.setStatus(OEPConstants.UNABLE_TO_FETCH_RECORDS);
			log.error("Unable to fetch order line details : {}",e.getMessage());
		}
		
		response.setProcessQueryOrderDetailsResponse(queryOrderDetailsResponse);
		return response;
	}

	private void createOrderDetailsResponse(ProcessQueryOrderDetailsResponseType queryOrderDetailsResponse,OepOrder oepOrder) {
		
		if(oepOrder.getOepOrderLines().isEmpty())
			queryOrderDetailsResponse.setStatus(OEPConstants.NO_RECORDS_FOUND);			
		else{				
			
			ListOfOEPQueryOrderDetailsResponseType tasks = new ListOfOEPQueryOrderDetailsResponseType();
			
			oepOrder.getOepOrderLines()
			.stream()
			.forEach(oepOrderLine -> createOrderLinesDeatils(oepOrderLine,tasks));
			
			queryOrderDetailsResponse.setListOfTasks(tasks);
			
		}
	}

	private void createOrderLinesDeatils(OepOrderLine oepOrderLine,
			ListOfOEPQueryOrderDetailsResponseType tasks) {
		
		OEPOrderTaskResponseType planLineItem = new OEPOrderTaskResponseType();
		planLineItem.setTaskName(oepOrderLine.getPlanName());
		planLineItem.setTaskId(oepOrderLine.getOrderLineId());
		
		createOrderLineMileStoneHistory(oepOrderLine,planLineItem);	
		
		tasks.getTask().add(planLineItem);
		
	}

	private void createOrderLineMileStoneHistory(OepOrderLine oepOrderLine, OEPOrderTaskResponseType planLineItem) {
		
		ListOfOEPTaskHistory listOfTaskHistory =  new ListOfOEPTaskHistory();
		
		oepOrderLine.getOepMilestoneHistories()
		.stream()
		.sorted((mileStoneHist1,mileStoneHist2)->mileStoneHist2.getLastModifiedDate().compareTo(mileStoneHist1.getLastModifiedDate()))
		.forEach(mileStoneHist -> {
			
			OEPHistoryType mileStoneHistory = new OEPHistoryType();	
			mileStoneHistory.setTaskStatus(mileStoneHist.getMilestoneCode());
			
			try {
				mileStoneHistory.setTaskCreatedDate(OrderDataUtils.getXMLGregorianCalendarFromTimeStamp(mileStoneHist.getLastModifiedDate()));
			} catch (DatatypeConfigurationException e) {
				log.error("Error while Parsing the date {}",e.getMessage());
			}
			
			listOfTaskHistory.getHistory().add(mileStoneHistory);	
		});
		
		planLineItem.setListOfTaskHistory(listOfTaskHistory);
	}
	
	

}
