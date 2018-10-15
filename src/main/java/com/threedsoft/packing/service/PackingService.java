package com.threedsoft.packing.service;

import java.util.List;

import com.threedsoft.packing.dto.requests.PackConfirmRequestDTO;
import com.threedsoft.packing.dto.requests.PackCreationRequestDTO;
import com.threedsoft.packing.dto.responses.PackResourceDTO;

public interface PackingService {
	public PackResourceDTO createPack(PackCreationRequestDTO pickCreationRequest) throws Exception;
	
	public PackResourceDTO confirmPack(PackConfirmRequestDTO pickConfirmRequest) throws Exception;

	public List<PackResourceDTO> findByOrderId(String busName, Integer locnNbr, Long orderId) throws Exception;

	public List<PackResourceDTO> findByOrderNbr(String busName, Integer locnNbr, String orderNbr) throws Exception;

	public List<PackResourceDTO> findByBatchNbr(String busName, Integer locnNbr, String batchNbr) throws Exception;
	
	public List<PackResourceDTO> findByContainerNbr(String busName, Integer locnNbr, String containerNbr) throws Exception;

	PackResourceDTO findByPackId(String busName, Integer locnNbr, Long pickDtlId) throws Exception;
}