package com.threedsoft.packing.dto.converter;

import org.springframework.stereotype.Component;

import com.threedsoft.packing.dto.requests.PackCreationRequestDTO;
import com.threedsoft.picking.dto.events.PickConfirmationEvent;
import com.threedsoft.picking.dto.responses.PickResourceDTO;
import com.threedsoft.util.dto.events.EventResourceConverter;

@Component
public class PickConfirmToPackRequestConverter {
	public static PackCreationRequestDTO createPackCreationRequest(PickConfirmationEvent pickConfirmEvent) {
		PickResourceDTO pickDTO = (PickResourceDTO) EventResourceConverter
				.getObject(pickConfirmEvent.getEventResource(), pickConfirmEvent.getEventResourceClassName());
		PackCreationRequestDTO packReq = new PackCreationRequestDTO();
		packReq.setPickId(pickDTO.getId());
		packReq.setBatchNbr(pickDTO.getBatchNbr());
		packReq.setBusName(pickDTO.getBusName());
		packReq.setLocnNbr(pickDTO.getLocnNbr());
		packReq.setBusUnit(pickDTO.getBusUnit());
		packReq.setItemBrcd(pickDTO.getItemBrcd());
		packReq.setOrderNbr(pickDTO.getOrderNbr());
		packReq.setOrderLineNbr(pickDTO.getOrderLineNbr());
		packReq.setQty(pickDTO.getQty());
		packReq.setPackedQty(0);
		packReq.setOrderId(pickDTO.getOrderId());
		packReq.setOrderLineId(pickDTO.getOrderLineId());
		packReq.setOrderLineNbr(pickDTO.getOrderLineNbr());
		packReq.setFromContainer(pickDTO.getToContainer());

		//pickReq.setCompany(invnAllocatedEvent);
		//pickReq.setDivision(division);
		packReq.setUserId(pickDTO.getUserId());
		
		return packReq;
	}
}
