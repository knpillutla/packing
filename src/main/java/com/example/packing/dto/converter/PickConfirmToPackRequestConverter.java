package com.example.packing.dto.converter;

import org.springframework.stereotype.Component;

import com.example.packing.dto.requests.PackCreationRequestDTO;
import com.example.picking.dto.events.PickConfirmationEvent;
import com.example.picking.dto.responses.PickDTO;

@Component
public class PickConfirmToPackRequestConverter {
	public static PackCreationRequestDTO createPackCreationRequest(PickConfirmationEvent pickConfirmEvent) {
		PickDTO pickDTO = pickConfirmEvent.getPickDTO();
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
