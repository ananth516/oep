declare namespace oep = "http://xmlns.oracle.com/OEP/OrderManagement/Order/V1";

let $input := fn:root(.)
let $order := $input/oep:OrderEntryRequest/*
let $serviceType := if(fn:local-name($order)='ProcessAddOrder') then
					(
						if (fn:exists($order/oep:ListOfOEPOrder/oep:OEPOrder/oep:MobileService)) then (
							'Mobile'
						)
						else if(fn:exists($order/oep:ListOfOEPOrder/oep:OEPOrder/oep:MobileBBService)) then (
							'BB'
						)
						else if(fn:exists($order/oep:ListOfOEPOrder/oep:OEPOrder/oep:BulkSMSService)) then (
							'Bulk SMS'
						)
						else if(fn:exists($order/oep:ListOfOEPOrder/oep:OEPOrder/oep:ShortCodeService)) then (
							'SC'
						)
						
						else ()
					)
					else if(fn:local-name($order)='ProcessModifyOrder') then
					(
						if (fn:exists($order/oep:ListOfOEPOrder/oep:OEPOrder/oep:MobileService)) then (
							'Mobile'
						)
						else if(fn:exists($order/oep:ListOfOEPOrder/oep:OEPOrder/oep:MobileBBService)) then (
							'BB'
						)
						else if(fn:exists($order/oep:ListOfOEPOrder/oep:OEPOrder/oep:BulkSMSService)) then (
							'Bulk SMS'
						)
						else if(fn:exists($order/oep:ListOfOEPOrder/oep:OEPOrder/oep:ShortCodeService)) then (
							'SC'
						)
						else ()
					)
					else if(fn:local-name($order)='ProcessDisconnectOrder') then
					(
						if (fn:exists($order/oep:ListOfOEPOrder/oep:OEPOrder/oep:MobileService)) then (
							'Mobile'
						)
						else if(fn:exists($order/oep:ListOfOEPOrder/oep:OEPOrder/oep:MobileBBService)) then (
							'BB'
						)
						else if(fn:exists($order/oep:ListOfOEPOrder/oep:OEPOrder/oep:BulkSMSService)) then (
							'Bulk SMS'
						)
						else if(fn:exists($order/oep:ListOfOEPOrder/oep:OEPOrder/oep:MobileShortCodeService)) then (
							'SC'
						)
						else ()
					)
					else if(fn:local-name($order)='ProcessSuspendOrResumeOrder') then
					(
						if (fn:exists($order/oep:ListOfOEPOrder/oep:OEPOrder/oep:MobileService)) then (
							'Mobile'
						)
						else if(fn:exists($order/oep:ListOfOEPOrder/oep:OEPOrder/oep:MobileBBService)) then (
							'BB'
						)
						else if(fn:exists($order/oep:ListOfOEPOrder/oep:OEPOrder/oep:BulkSMSService)) then (
							'Bulk SMS'
						)
						else if(fn:exists($order/oep:ListOfOEPOrder/oep:OEPOrder/oep:ShortCodeService)) then (
							'SC'
						)
						else ()					
					)					
					else if(fn:local-name($order)=('ProcessAddOnManagment','ProcessChangePlan','ProcessUpdateServiceSuspensionDate')) then
					(
						$order/oep:ServiceType/text()
					)
					else if(fn:local-name($order)=('ProcessManagePortInOrder',
													'ProcessNumberReleaseOrder',
													'ProcessCollectionBarring',
													'ProcessCugOrder',
													'ProcessCallDivertManagment')) then
					(
						'Mobile'
					)					
					
					else ()

return
	<ServiceType>{$serviceType}</ServiceType>