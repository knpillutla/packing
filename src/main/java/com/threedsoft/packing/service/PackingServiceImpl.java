package com.threedsoft.packing.service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import javax.transaction.Transactional;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.threedsoft.packing.db.Pack;
import com.threedsoft.packing.db.PackingRepository;
import com.threedsoft.packing.dto.converter.EntityDTOConverter;
import com.threedsoft.packing.dto.events.PackConfirmationEvent;
import com.threedsoft.packing.dto.events.PackCreatedEvent;
import com.threedsoft.packing.dto.requests.PackConfirmRequestDTO;
import com.threedsoft.packing.dto.requests.PackCreationRequestDTO;
import com.threedsoft.packing.dto.responses.PackResourceDTO;
import com.threedsoft.packing.util.PackingConstants;
import com.threedsoft.util.service.EventPublisher;

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
	public PackResourceDTO confirmPack(PackConfirmRequestDTO packConfirmRequest) throws Exception{
		logger.info("confirmPack Start, :" + packConfirmRequest);
		PackResourceDTO packDTO = null;
		Optional<Pack> packDtl = packDAO.findById(packConfirmRequest.getId());
		if(packDtl.isPresent()) {
			Pack packEntity = packDtl.get();
			packEntity.setPackedQty((packEntity.getPackedQty()==null?0:packEntity.getPackedQty()) + packConfirmRequest.getQtyPacked());
			packEntity.setUserId(packConfirmRequest.getUserId());
			packEntity.setStatCode(PackStatus.PACKED.getStatCode());
			Pack updatedPackObj = packDAO.save(packEntity);
			packDTO = EntityDTOConverter.getPackDTO(updatedPackObj);
			PackConfirmationEvent packConfirmEvent = new PackConfirmationEvent(packDTO, PackingConstants.PACKING_SERVICE_NAME);
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
	public PackResourceDTO createPack(PackCreationRequestDTO packCreationReq) throws Exception {
		Pack newPackEntity = EntityDTOConverter.getPackEntity(packCreationReq);
		newPackEntity.setStatCode(PackStatus.RELEASED.getStatCode());
		PackResourceDTO packDTO = EntityDTOConverter.getPackDTO(packDAO.save(newPackEntity));
		PackCreatedEvent packCreatedEvent = new PackCreatedEvent(packDTO, PackingConstants.PACKING_SERVICE_NAME);
		eventPublisher.publish(packCreatedEvent);
		logger.info("createPack End, created new pack:" + packDTO);
		return packDTO;
	}

	@Override
	public List<PackResourceDTO> findByOrderId(String busName, Integer locnNbr, Long orderId) throws Exception {
		List<Pack> pickEntityList = packDAO.findByBusNameAndLocnNbrAndOrderId(busName, locnNbr, orderId);
		List<PackResourceDTO> pickDTOList = new ArrayList();
		if(pickEntityList!=null) {
			for(Pack packEntity : pickEntityList) {
				pickDTOList.add(EntityDTOConverter.getPackDTO(packEntity));
			}
		}
		return pickDTOList;
	}

	@Override
	public PackResourceDTO findByPackId(String busName, Integer locnNbr, Long pickId) throws Exception {
		Pack packEntity = packDAO.findByPickId(busName, locnNbr, pickId);
		return EntityDTOConverter.getPackDTO(packEntity);
	}

	@Override
	public List<PackResourceDTO> findByOrderNbr(String busName, Integer locnNbr, String orderNbr) throws Exception {
		List<Pack> pickEntityList = packDAO.findByBusNameAndLocnNbrAndOrderNbr(busName, locnNbr, orderNbr);
		List<PackResourceDTO> pickDTOList = new ArrayList();
		if(pickEntityList!=null) {
			for(Pack packEntity : pickEntityList) {
				pickDTOList.add(EntityDTOConverter.getPackDTO(packEntity));
			}
		}
		return pickDTOList;
	}

	@Override
	public List<PackResourceDTO> findByBatchNbr(String busName, Integer locnNbr, String batchNbr) throws Exception {
		List<Pack> pickEntityList = packDAO.findByBusNameAndLocnNbrAndBatchNbr(busName, locnNbr, batchNbr);
		List<PackResourceDTO> pickDTOList = new ArrayList();
		if(pickEntityList!=null) {
			for(Pack packEntity : pickEntityList) {
				pickDTOList.add(EntityDTOConverter.getPackDTO(packEntity));
			}
		}
		return pickDTOList;
	}

	@Override
	public List<PackResourceDTO> findByContainerNbr(String busName, Integer locnNbr, String containerNbr) throws Exception {
		List<Pack> pickEntityList = packDAO.findByBusNameAndLocnNbrAndContainerNbr(busName, locnNbr, containerNbr);
		List<PackResourceDTO> pickDTOList = new ArrayList();
		if(pickEntityList!=null) {
			for(Pack packEntity : pickEntityList) {
				pickDTOList.add(EntityDTOConverter.getPackDTO(packEntity));
			}
		}
		return pickDTOList;
	}
}
