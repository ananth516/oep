declare namespace oep = "http://xmlns.oracle.com/OEP/OrderManagement/Order/V1";

declare function local:getOrderType($order as element){
	
	if(fn:local-name($order)='ProcessAddOrder') then
	(
		'AddOrder'
	)
	else
	(
		'ModifyOrder'
	)

};

let $input := fn:root(.)
let $order:= $input/oep:OrderEntryRequest/*

let $orderType := local:getOrderType($order)
