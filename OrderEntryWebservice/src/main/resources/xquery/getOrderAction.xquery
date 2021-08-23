declare namespace oep = "http://xmlns.oracle.com/OEP/OrderManagement/Order/V1";

declare variable $in.headers.OrderIndex as xs:string external;

let $input := fn:root(.)
let $order := $input/oep:OrderEntryRequest/*
let $orderIndex := xs:integer($in.headers.OrderIndex)

let $orderAction := if(fn:local-name($order)='ProcessAddOrder') then
					(
						if (fn:exists($order/oep:ListOfOEPOrder/oep:OEPOrder/oep:MobileService)) then (
							$order/oep:ListOfOEPOrder/oep:OEPOrder/oep:MobileService/oep:OrderAction/text()
						)
						else if(fn:exists($order/oep:ListOfOEPOrder/oep:OEPOrder/oep:MobileBBService)) then (
							$order/oep:ListOfOEPOrder/oep:OEPOrder/oep:MobileBBService/oep:OrderAction/text()
						)
						else if(fn:exists($order/oep:ListOfOEPOrder/oep:OEPOrder/oep:BulkSMSService)) then (
							$order/oep:ListOfOEPOrder/oep:OEPOrder/oep:BulkSMSService/oep:OrderAction/text()
						)	
						else if(fn:exists($order/oep:ListOfOEPOrder/oep:OEPOrder/oep:ShortCodeService)) then (
							$order/oep:ListOfOEPOrder/oep:OEPOrder/oep:ShortCodeService/oep:OrderAction/text()
						)
						
						else ()
					)
					else if(fn:local-name($order)='ProcessModifyOrder') then
					(
						if(fn:exists($order/oep:ListOfOEPOrder/oep:OEPOrder/oep:MobileService/oep:AddRemoveAddon)) then (
							$order/oep:ListOfOEPOrder/oep:OEPOrder/oep:MobileService/oep:AddRemoveAddon/oep:OrderAction/text()
						)
						
						else if(fn:exists($order/oep:ListOfOEPOrder/oep:OEPOrder/oep:MobileBBService/oep:AddRemoveAddon)) then (
							
							$order/oep:ListOfOEPOrder/oep:OEPOrder/oep:MobileBBService/oep:AddRemoveAddon/oep:OrderAction/text()
						)

						
						else if(fn:exists($order/oep:ListOfOEPOrder/oep:OEPOrder/oep:MobileService/oep:ChangePlan)) then (
							$order/oep:ListOfOEPOrder/oep:OEPOrder/oep:MobileService/oep:ChangePlan/oep:OrderAction/text()
						)
						
						else if(fn:exists($order/oep:ListOfOEPOrder/oep:OEPOrder/oep:MobileBBService/oep:ChangePlan)) then (							
							$order/oep:ListOfOEPOrder/oep:OEPOrder/oep:MobileBBService/oep:ChangePlan/oep:OrderAction/text()
						)							

							
						else if(fn:exists($order/oep:ListOfOEPOrder/oep:OEPOrder/oep:MobileService/oep:ChangeSim)) then (
							$order/oep:ListOfOEPOrder/oep:OEPOrder/oep:MobileService/oep:ChangeSim/oep:OrderAction/text()
						)	
						
						else if(fn:exists($order/oep:ListOfOEPOrder/oep:OEPOrder/oep:MobileBBService/oep:ChangeSim)) then (
							$order/oep:ListOfOEPOrder/oep:OEPOrder/oep:MobileBBService/oep:ChangeSim/oep:OrderAction/text()
						)
						
						else if(fn:exists($order/oep:ListOfOEPOrder/oep:OEPOrder/oep:MobileService/oep:ChangeMsIsdn)) then (
							$order/oep:ListOfOEPOrder/oep:OEPOrder/oep:MobileService/oep:ChangeMsIsdn/oep:OrderAction/text()
						)	
						
						else if(fn:exists($order/oep:ListOfOEPOrder/oep:OEPOrder/oep:ShortCodeService/oep:ChangeShortCodeNumber)) then (
							$order/oep:ListOfOEPOrder/oep:OEPOrder/oep:ShortCodeService/oep:ChangeShortCodeNumber/oep:OrderAction/text()
						)	
						
						else if(fn:exists($order/oep:ListOfOEPOrder/oep:OEPOrder/oep:ShortCodeService/oep:ChangePlan)) then (
							$order/oep:ListOfOEPOrder/oep:OEPOrder/oep:ShortCodeService/oep:ChangePlan/oep:OrderAction/text()
						)	
						
						else if(fn:exists($order/oep:ListOfOEPOrder/oep:OEPOrder/oep:ShortCodeService/oep:AddRemoveAddon)) then (
							$order/oep:ListOfOEPOrder/oep:OEPOrder/oep:ShortCodeService/oep:AddRemoveAddon/oep:OrderAction/text()
						)	
						
						else if(fn:exists($order/oep:ListOfOEPOrder/oep:OEPOrder/oep:BulkSMSService/oep:ChangePlan)) then (
							$order/oep:ListOfOEPOrder/oep:OEPOrder/oep:BulkSMSService/oep:ChangePlan/oep:OrderAction/text()
						)	
						else if(fn:exists($order/oep:ListOfOEPOrder/oep:OEPOrder/oep:BulkSMSService/oep:ChangeDetails)) then (
							$order/oep:ListOfOEPOrder/oep:OEPOrder/oep:BulkSMSService/oep:ChangeDetails/oep:OrderAction/text()
						)	
						
						else if(fn:exists($order/oep:ListOfOEPOrder/oep:OEPOrder/oep:MobileBBService/oep:ChangeMsIsdn)) then (
							$order/oep:ListOfOEPOrder/oep:OEPOrder/oep:MobileBBService/oep:ChangeMsIsdn/oep:OrderAction/text()
						)						
					
						else if(fn:exists($order/oep:ListOfOEPOrder/oep:OEPOrder/oep:MobileService/oep:Fca)) then (
						
							if($order/oep:ListOfOEPOrder/oep:OEPOrder/oep:MobileService/oep:Fca/oep:OrderAction = 'FUA') then (
								'FirstCallActivation'
							)
							else()
						)
						
						else ()
					)
					else if(fn:local-name($order)='ProcessDisconnectOrder') then
					(
						if(fn:exists($order/oep:ListOfOEPOrder/oep:OEPOrder/oep:MobileService)) then (
							$order/oep:ListOfOEPOrder/oep:OEPOrder/oep:MobileService/oep:ServiceDetails/oep:OrderAction/text()
						)
						else if(fn:exists($order/oep:ListOfOEPOrder/oep:OEPOrder/oep:MobileBBService)) then (
							$order/oep:ListOfOEPOrder/oep:OEPOrder/oep:MobileBBService/oep:ServiceDetails/oep:OrderAction/text()
						)		
						else if(fn:exists($order/oep:ListOfOEPOrder/oep:OEPOrder/oep:MobileShortCodeService)) then (
							$order/oep:ListOfOEPOrder/oep:OEPOrder/oep:MobileShortCodeService/oep:ServiceDetails/oep:OrderAction/text()
						)					
						else if(fn:exists($order/oep:ListOfOEPOrder/oep:OEPOrder/oep:BulkSMSService)) then (
							$order/oep:ListOfOEPOrder/oep:OEPOrder/oep:BulkSMSService/oep:ServiceDetails/oep:OrderAction/text()
						)
												
						else ()
					)
					else if(fn:local-name($order)='ProcessSuspendOrResumeOrder') then
					(
						if(fn:exists($order/oep:ListOfOEPOrder/oep:OEPOrder/oep:MobileService)) then (					
						$order/oep:ListOfOEPOrder/oep:OEPOrder[$orderIndex]/oep:MobileService/oep:ServiceDetails/oep:OrderAction/text()
					)
					
						else if(fn:exists($order/oep:ListOfOEPOrder/oep:OEPOrder/oep:MobileBBService)) then (					
							$order/oep:ListOfOEPOrder/oep:OEPOrder[$orderIndex]/oep:MobileBBService/oep:ServiceDetails/oep:OrderAction/text()
						)	
							
						else ()					
					)
					
					else if(fn:local-name($order)='ProcessAddOnManagment') then
					(
						'DHIOAddOnManagement'
					)
					else if((fn:local-name($order)='ProcessManagePortInOrder') 
							or (fn:local-name($order)='ProcessStartCancelOrder') 
							or (fn:local-name($order)='ProcessUpdateServiceSuspensionDate')) then
					(
						$order/oep:OrderAction/text()
						
					)

					else if(fn:local-name($order)='ProcessNumberReleaseOrder') then
					(
						'MobileService-NumberRelease'
						
					)
					else if(fn:local-name($order)='ProcessChangePlan') then
					(
						'DHIOChangePlan'
						
					)

					else if(fn:local-name($order)='ProcessCallDivertManagment') then
					(
						if($order/oep:ServiceAction = 'Activate') then (
							'MobileService-CallDivert-Activate'
						)						
						else if($order/oep:ServiceAction = 'DeActivate') then (
							'MobileService-CallDivert-DeActivate'
						)							
						else ()

						
					)					
					else if(fn:local-name($order)='ProcessCugOrder') then
					(
						'MobileService-CUG'
					) 
					else if(fn:local-name($order)='ProcessCollectionBarring') then
					(
						if($order/oep:OrderAction = 'PrepaidLifecycleBarring') then (
							'PrepaidLifecycleBarring'
							) 
							else if($order/oep:Services/oep:BarringType = '0') then (
							'CollectionsActionBarring'
							)
							else if($order/oep:Services/oep:BarringType = '1') then (
							'SpendingCapAction'
							) else if($order/oep:Services/oep:BarringType = '2') then (
							'CollectionsActionBarring'
							)
							else()
					)
					else
					(
						'ModifyOrder'
					)

return
	<OrderAction>{$orderAction}</OrderAction>

