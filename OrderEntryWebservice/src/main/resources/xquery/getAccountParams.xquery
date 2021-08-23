declare namespace oep = "http://xmlns.oracle.com/OEP/OrderManagement/Order/V1";
declare namespace batch = "http://xmlns.oracle.com/OEP/batch/provisioning";

declare variable $in.headers.param as xs:string external;

let $input := fn:root(.)
let $order := $input/oep:OrderEntryRequest/*
let $param := xs:string($in.headers.param)

let $output := 
					if($param='BillingAccountId') then 
					(
						if(fn:exists(.//oep:BillingAccountId) and .//oep:BillingAccountId/text()!='') then
						(
							
							.//oep:BillingAccountId/text()
						
						)
						
						else if(fn:exists(.//batch:billingAccountId) and .//batch:billingAccountId/text()!='') then
						(
							.//batch:billingAccountId/text()
							
						)
						else ()
					)
					
					else if($param='CustomerAccountId') then 
					(
						if(fn:exists(.//oep:CustomerAccountId) and .//oep:CustomerAccountId/text()!='') then
						(
							
							.//oep:CustomerAccountId/text()
						
						)
						
						else if(fn:exists(.//batch:customerAccountId) and .//batch:customerAccountId/text()!='') then
						(
							.//batch:customerAccountId/text()
							
						)
						else ()
					)
					
					else if($param='AccountName') then 
					(
						if(fn:exists(.//oep:AccountName) and .//oep:AccountName/text()!='') then
						(
							
							.//oep:AccountName/text()
						
						)
						
						else if(fn:exists(.//batch:accountName) and .//batch:accountName/text()!='') then
						(
							.//batch:accountName/text()
							
						)
						else ()
					)
					
					else if($param='AccountNo') then 
					(
						if(fn:exists(.//oep:AccountNo) and .//oep:AccountNo/text()!='') then
						(
							
							.//oep:AccountNo/text()
						
						)
						
						else if(fn:exists(.//batch:accountNo) and .//batch:accountNo/text()!='') then
						(
							.//batch:accountNo/text()
							
						)
						else ()
					)
					else if($param='ChannelName') then 
					(
						if(fn:exists(.//oep:ChannelName) and .//oep:ChannelName/text()!='') then
						(
							
							.//oep:ChannelName/text()
						
						)
						
						else ()
					)
					else ()

return
	<Param>{$output}</Param>

