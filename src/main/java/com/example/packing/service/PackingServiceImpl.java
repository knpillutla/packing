package com.example.packing.service;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Optional;

import javax.transaction.Transactional;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.packing.db.Pack;
import com.example.packing.db.PackingRepository;
import com.example.packing.dto.converter.EntityDTOConverter;
import com.example.packing.dto.events.PackConfirmationEvent;
import com.example.packing.dto.events.PackCreatedEvent;
import com.example.packing.dto.requests.PackConfirmRequestDTO;
import com.example.packing.dto.requests.PackCreationRequestDTO;
import com.example.packing.dto.responses.PackDTO;

@Service
public class PackingServiceImpl implements PackingService {
	private static final Logger logger = LoggerFactory.getLogger(PackingServiceImpl.class);
	
	@Autowired
	PackingRepository packDAO;
	
	@Autowired
	EventPublisher eventPublisher;
	
	public enum PackStatus {
		CREATED(100), RELEASED(110), PACKED(120), SHORTED(140), CANCELLED(199);
		PackStatus(Integer statCode) {
			this.statCode = statCode;
		}

		private Integer statCode;

		public Integer getStatCode() {
			return statCode;
		}
	}
	
	/* (non-Javadoc)
	 * @see com.example.demo.PickingService#confirmPick(com.example.AvroPickTask)
	 */
	@Override
	@Transactional
	public PackDTO confirmPack(PackConfirmRequestDTO packConfirmRequest) throws Exception{
		logger.info("confirmPack Start, :" + packConfirmRequest);
		PackDTO packDTO = null;
		Optional<Pack> packDtl = packDAO.findById(packConfirmRequest.getId());
		if(packDtl.isPresent()) {
			Pack packEntity = packDtl.get();
			packEntity.setPackedQty((packEntity.getPackedQty()==null?0:packEntity.getPackedQty()) + packConfirmRequest.getQtyPacked());
			packEntity.setUserId(packConfirmRequest.getUserId());
			packEntity.setStatCode(PackStatus.PACKED.getStatCode());
			packEntity.setUpdatedDttm(new java.util.Date());
			Pack updatedPackObj = packDAO.save(packEntity);
			packDTO = EntityDTOConverter.getPackDTO(updatedPackObj);
			PackConfirmationEvent packConfirmEvent = new PackConfirmationEvent(packDTO);
			eventPublisher.publish(packConfirmEvent);
		}
		logger.info("confirmPack End, updated pack obj:" + packDTO);
		return packDTO;
	}

	/* (non-Javadoc)
	 * @see com.example.demo.PickingService#createNew(com.example.AvroPickTask)
	 */
	@Override
	@Transactional
	public PackDTO createPack(PackCreationRequestDTO packCreationReq) throws Exception {
		Pack newPackEntity = EntityDTOConverter.getPackEntity(packCreationReq);
		Date createdDttm = new java.util.Date();
		newPackEntity.setCreatedDttm(createdDttm);
		newPackEntity.setUpdatedDttm(createdDttm);
		newPackEntity.setStatCode(PackStatus.RELEASED.getStatCode());
		PackDTO packDTO = EntityDTOConverter.getPackDTO(packDAO.save(newPackEntity));
		PackCreatedEvent pickCreatedEvent = new PackCreatedEvent(packDTO);
		eventPublisher.publish(pickCreatedEvent);
		logger.info("createPack End, created new pack:" + packDTO);
		return packDTO;
	}

	@Override
	public List<PackDTO> findByOrderId(String busName, Integer locnNbr, Long orderId) throws Exception {
		List<Pack> pickEntityList = packDAO.findByBusNameAndLocnNbrAndOrderId(busName, locnNbr, orderId);
		List<PackDTO> pickDTOList = new ArrayList();
		if(pickEntityList!=null) {
			for(Pack packEntity : pickEntityList) {
				pickDTOList.add(EntityDTOConverter.getPackDTO(packEntity));
			}
		}
		return pickDTOList;
	}

	@Override
	public PackDTO findByPackId(String busName, Integer locnNbr, Long pickId) throws Exception {
		Pack packEntity = packDAO.findByPickId(busName, locnNbr, pickId);
		return EntityDTOConverter.getPackDTO(packEntity);
	}

	@Override
	public List<PackDTO> findByOrderNbr(String busName, Integer locnNbr, String orderNbr) throws Exception {
		List<Pack> pickEntityList = packDAO.findByBusNameAndLocnNbrAndOrderNbr(busName, locnNbr, orderNbr);
		List<PackDTO> pickDTOList = new ArrayList();
		if(pickEntityList!=null) {
			for(Pack packEntity : pickEntityList) {
				pickDTOList.add(EntityDTOConverter.getPackDTO(packEntity));
			}
		}
		return pickDTOList;
	}

	@Override
	public List<PackDTO> findByBatchNbr(String busName, Integer locnNbr, String batchNbr) throws Exception {
		List<Pack> pickEntityList = packDAO.findByBusNameAndLocnNbrAndBatchNbr(busName, locnNbr, batchNbr);
		List<PackDTO> pickDTOList = new ArrayList();
		if(pickEntityList!=null) {
			for(Pack packEntity : pickEntityList) {
				pickDTOList.add(EntityDTOConverter.getPackDTO(packEntity));
			}
		}
		return pickDTOList;
	}

	@Override
	public List<PackDTO> findByContainerNbr(String busName, Integer locnNbr, String containerNbr) throws Exception {
		List<Pack> pickEntityList = packDAO.findByBusNameAndLocnNbrAndContainerNbr(busName, locnNbr, containerNbr);
		List<PackDTO> pickDTOList = new ArrayList();
		if(pickEntityList!=null) {
			for(Pack packEntity : pickEntityList) {
				pickDTOList.add(EntityDTOConverter.getPackDTO(packEntity));
			}
		}
		return pickDTOList;
	}
}
