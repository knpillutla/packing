package com.threedsoft.packing.service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import javax.transaction.Transactional;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Example;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import com.threedsoft.packing.db.Pack;
import com.threedsoft.packing.db.PackingRepository;
import com.threedsoft.packing.dto.converter.PackDTOConverter;
import com.threedsoft.packing.dto.events.PackConfirmationEvent;
import com.threedsoft.packing.dto.events.PackCreatedEvent;
import com.threedsoft.packing.dto.requests.PackConfirmRequestDTO;
import com.threedsoft.packing.dto.requests.PackCreationRequestDTO;
import com.threedsoft.packing.dto.requests.PackSearchRequestDTO;
import com.threedsoft.packing.dto.responses.PackResourceDTO;
import com.threedsoft.packing.util.PackStatus;
import com.threedsoft.packing.util.PackingConstants;
import com.threedsoft.util.service.EventPublisher;

@Service
public class PackingServiceImpl implements PackingService {
	private static final Logger logger = LoggerFactory.getLogger(PackingServiceImpl.class);
	
	@Autowired
	PackingRepository packDAO;
	
	@Autowired
	EventPublisher eventPublisher;
	
	@Autowired
	PackDTOConverter packDTOConverter;

	@Override
	@Transactional
	public PackResourceDTO confirmPack(PackConfirmRequestDTO packConfirmRequest) throws Exception{
		logger.info("confirmPack Start, :" + packConfirmRequest);
		PackResourceDTO packDTO = null;
		Optional<Pack> packDtl = packDAO.findById(packConfirmRequest.getId());
		if(packDtl.isPresent()) {
			Pack packEntity = packDtl.get();
			packEntity.setPackedQty((packEntity.getPackedQty()==null?0:packEntity.getPackedQty()) + packConfirmRequest.getQtyPacked());
			packEntity.setUserId(packConfirmRequest.getUserId());
			packEntity.setStatus(PackStatus.PACKED.getStatus());
			Pack updatedPackObj = packDAO.save(packEntity);
			packDTO = PackDTOConverter.getPackDTO(updatedPackObj);
			PackConfirmationEvent packConfirmEvent = new PackConfirmationEvent(packDTO, PackingConstants.PACKING_SERVICE_NAME);
			eventPublisher.publish(packConfirmEvent);
		}
		logger.info("confirmPack End, updated pack obj:" + packDTO);
		return packDTO;
	}

	/* (non-Javadoc)
	 * @see com.example.demo.PackingService#createNew(com.example.AvroPackTask)
	 */
	@Override
	@Transactional
	public PackResourceDTO createPack(PackCreationRequestDTO packCreationReq) throws Exception {
		Pack newPackEntity = PackDTOConverter.getPackEntity(packCreationReq);
		newPackEntity.setStatus(PackStatus.RELEASED.getStatus());
		PackResourceDTO packDTO = PackDTOConverter.getPackDTO(packDAO.save(newPackEntity));
		PackCreatedEvent packCreatedEvent = new PackCreatedEvent(packDTO, PackingConstants.PACKING_SERVICE_NAME);
		eventPublisher.publish(packCreatedEvent);
		logger.info("createPack End, created new pack:" + packDTO);
		return packDTO;
	}

	@Override
	public List<PackResourceDTO> findByOrderId(String busName, Integer locnNbr, Long orderId) throws Exception {
		List<Pack> packEntityList = packDAO.findByBusNameAndLocnNbrAndOrderId(busName, locnNbr, orderId);
		List<PackResourceDTO> pickDTOList = new ArrayList();
		if(packEntityList!=null) {
			for(Pack packEntity : packEntityList) {
				pickDTOList.add(PackDTOConverter.getPackDTO(packEntity));
			}
		}
		return pickDTOList;
	}

	@Override
	public PackResourceDTO findByPackId(String busName, Integer locnNbr, Long pickId) throws Exception {
		Pack packEntity = packDAO.findByPickId(busName, locnNbr, pickId);
		return PackDTOConverter.getPackDTO(packEntity);
	}

	@Override
	public List<PackResourceDTO> findByOrderNbr(String busName, Integer locnNbr, String orderNbr) throws Exception {
		List<Pack> packEntityList = packDAO.findByBusNameAndLocnNbrAndOrderNbr(busName, locnNbr, orderNbr);
		List<PackResourceDTO> pickDTOList = new ArrayList();
		if(packEntityList!=null) {
			for(Pack packEntity : packEntityList) {
				pickDTOList.add(PackDTOConverter.getPackDTO(packEntity));
			}
		}
		return pickDTOList;
	}

	@Override
	public List<PackResourceDTO> findByBatchNbr(String busName, Integer locnNbr, String batchNbr) throws Exception {
		List<Pack> packEntityList = packDAO.findByBusNameAndLocnNbrAndBatchNbr(busName, locnNbr, batchNbr);
		List<PackResourceDTO> pickDTOList = new ArrayList();
		if(packEntityList!=null) {
			for(Pack packEntity : packEntityList) {
				pickDTOList.add(PackDTOConverter.getPackDTO(packEntity));
			}
		}
		return pickDTOList;
	}

	@Override
	public List<PackResourceDTO> findByContainerNbr(String busName, Integer locnNbr, String containerNbr) throws Exception {
		List<Pack> packEntityList = packDAO.findByBusNameAndLocnNbrAndContainerNbr(busName, locnNbr, containerNbr);
		List<PackResourceDTO> pickDTOList = new ArrayList();
		if(packEntityList!=null) {
			for(Pack packEntity : packEntityList) {
				pickDTOList.add(PackDTOConverter.getPackDTO(packEntity));
			}
		}
		return pickDTOList;
	}

	@Override
	public List<PackResourceDTO> findByBusNameAndLocnNbr(String busName, Integer locnNbr)  throws Exception{
		PageRequest pageRequest = new PageRequest(0, 20);
		List<Pack> packEntityList = packDAO.findByBusNameAndLocnNbr(busName, locnNbr, pageRequest);
		List<PackResourceDTO> pickDTOList = new ArrayList();
		if(packEntityList!=null) {
			for(Pack packEntity : packEntityList) {
				pickDTOList.add(PackDTOConverter.getPackDTO(packEntity));
			}
		}
		return pickDTOList;
	}

	@Override
	public List<PackResourceDTO> searchPacks(PackSearchRequestDTO packSearchReq) throws Exception {
		PageRequest pageRequest = new PageRequest(0, 50);
		Pack searchPacks = packDTOConverter.getPackEntityForSeach(packSearchReq);
		Page<Pack> packEntityPage = packDAO.findAll(Example.of(searchPacks), pageRequest);
		List<PackResourceDTO> packDTOList = new ArrayList();
		for (Pack packEntity : packEntityPage) {
			packDTOList.add(packDTOConverter.getPackDTO(packEntity));
		}
		return packDTOList;
	}	
}
