declare namespace oep = "http://xmlns.oracle.com/OEP/OrderManagement/Order/V1";

declare variable $in.headers.OrderIndex as xs:string external;

let $input := fn:root(.)
let $order := $input/oep:OrderEntryRequest/*
let $orderIndex := xs:integer($in.headers.OrderIndex)

let $serviceIdentifier := 
						 if(fn:exists(.//oep:MsIsdn) and .//oep:MsIsdn[$orderIndex]/text()!='' 
						 and not(fn:exists(.//oep:BulkSMSService))
						 and (not(fn:exists(.//oep:OldMsIsdn) and .//oep:OldMsIsdn[$orderIndex]/text()!=''))) then
					(
						
						.//oep:MsIsdn[$orderIndex]/text()
					
					)
					
					else if(fn:exists(.//oep:OldMsIsdn) and .//oep:OldMsIsdn[$orderIndex]/text()!='') then
					(
						
						.//oep:OldMsIsdn[$orderIndex]/text()
					
					)
					else if(fn:exists(.//oep:ServiceIdentifier) and .//oep:ServiceIdentifier[$orderIndex]/text()!='') then
					(
						.//oep:ServiceIdentifier[$orderIndex]/text()
						
					)
					
					else if(fn:exists(.//oep:ServiceNo) and .//oep:ServiceNo[$orderIndex]/text()!='') then
					(
						.//oep:ServiceNo[$orderIndex]/text()
						
					)
					else if((fn:exists(.//oep:ServiceNumber) and .//oep:ServiceNumber[$orderIndex]/text()!='') and fn:exists(.//oep:ProcessCugOrder)) then
					(
						.//oep:ServiceNumber[$orderIndex]/text()
						
					)
					else if(fn:exists(.//oep:ShortCodeNumber) and .//oep:ShortCodeNumber[$orderIndex]/text()!='') then
					(
						.//oep:ShortCodeNumber[$orderIndex]/text()
						
					)					
					else if(fn:exists(.//oep:Id) and .//oep:Id[$orderIndex]/text()!='') then
					(
						
						.//oep:Id[$orderIndex]/text()
					
					)
									
					else ()

return
	<ServiceIdentifier>{$serviceIdentifier}</ServiceIdentifier>

