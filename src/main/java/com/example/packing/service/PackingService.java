package com.example.packing.service;

import java.util.List;

import com.example.packing.dto.requests.PackConfirmRequestDTO;
import com.example.packing.dto.requests.PackCreationRequestDTO;
import com.example.packing.dto.responses.PackDTO;

public interface PackingService {
	public PackDTO createPack(PackCreationRequestDTO pickCreationRequest) throws Exception;
	
	public PackDTO confirmPack(PackConfirmRequestDTO pickConfirmRequest) throws Exception;

	public List<PackDTO> findByOrderId(String busName, Integer locnNbr, Long orderId) throws Exception;

	public List<PackDTO> findByOrderNbr(String busName, Integer locnNbr, String orderNbr) throws Exception;

	public List<PackDTO> findByBatchNbr(String busName, Integer locnNbr, String batchNbr) throws Exception;
	
	public List<PackDTO> findByContainerNbr(String busName, Integer locnNbr, String containerNbr) throws Exception;

	PackDTO findByPackId(String busName, Integer locnNbr, Long pickDtlId) throws Exception;
}