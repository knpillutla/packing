package com.threedsoft.packing.dto.converter;

import com.threedsoft.packing.db.Pack;
import com.threedsoft.packing.dto.requests.PackCreationRequestDTO;
import com.threedsoft.packing.dto.requests.PackSearchRequestDTO;
import com.threedsoft.packing.dto.responses.PackResourceDTO;

public class PackDTOConverter {

	public static PackResourceDTO getPackDTO(Pack packEntity) {
		PackResourceDTO packDTO = new PackResourceDTO(packEntity.getId(), packEntity.getPickId(), packEntity.getOrderId(),
				packEntity.getOrderLineId(), packEntity.getOrderLineNbr(), packEntity.getBatchNbr(), 
				packEntity.getBusName(), packEntity.getLocnNbr(),
				packEntity.getBusUnit(), packEntity.getCompany(), packEntity.getDivision(), packEntity.getItemBrcd(),
				packEntity.getQty(), packEntity.getPackedQty(), packEntity.getStatus(), packEntity.getFromContainer(),
				packEntity.getToContainer(), packEntity.getOrderNbr(),
				packEntity.getPackageNbr(),  packEntity.getTransName(),
				packEntity.getSource(), packEntity.getHostName(), packEntity.getUserId());
		return packDTO;
	}

	public static Pack getPackEntity(PackCreationRequestDTO packCreationReq) {
		Pack packEntity = new Pack();
		packEntity.setBatchNbr(packCreationReq.getBatchNbr());
		packEntity.setBusName(packCreationReq.getBusName());
		packEntity.setBusUnit(packCreationReq.getBusUnit());
		packEntity.setCompany(packCreationReq.getCompany());
		packEntity.setDivision(packCreationReq.getDivision());
		packEntity.setFromContainer(packCreationReq.getFromContainer());
		packEntity.setToContainer(packCreationReq.getToContainer());
		packEntity.setHostName("");
		packEntity.setUserId(packCreationReq.getUserId());
		packEntity.setItemBrcd(packCreationReq.getItemBrcd());
		packEntity.setQty(packCreationReq.getQty());
		packEntity.setOrderId(packCreationReq.getOrderId());
		packEntity.setOrderLineId(packCreationReq.getOrderLineId());
		packEntity.setOrderLineNbr(packCreationReq.getOrderLineNbr());
		packEntity.setPickId(packCreationReq.getPickId());
		packEntity.setLocnNbr(packCreationReq.getLocnNbr());
		return packEntity;
	}

	public static Pack getPackEntityForSeach(PackSearchRequestDTO packSearchReq) {
		Pack packEntity = new Pack();
		packEntity.setBatchNbr(packSearchReq.getBatchNbr());
		packEntity.setBusName(packSearchReq.getBusName());
		packEntity.setBusUnit(packSearchReq.getBusUnit());
		packEntity.setCompany(packSearchReq.getCompany());
		packEntity.setDivision(packSearchReq.getDivision());
		packEntity.setFromContainer(packSearchReq.getFromContainer());
		packEntity.setToContainer(packSearchReq.getToContainer());
		packEntity.setItemBrcd(packSearchReq.getItemBrcd());
		packEntity.setOrderId(packSearchReq.getOrderId());
		packEntity.setOrderLineNbr(packSearchReq.getOrderLineNbr());
		packEntity.setPickId(packSearchReq.getPickId());
		packEntity.setLocnNbr(packSearchReq.getLocnNbr());
		return packEntity;
	}
}
