package com.threedsoft.packing.endpoint.rest;

import java.io.IOException;
import java.time.LocalDateTime;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;

import com.threedsoft.packing.dto.requests.PackConfirmRequestDTO;
import com.threedsoft.packing.dto.requests.PackSearchRequestDTO;
import com.threedsoft.packing.service.PackingService;
import com.threedsoft.util.dto.ErrorResourceDTO;

import io.swagger.annotations.Api;
import lombok.extern.slf4j.Slf4j;
@Controller
@RequestMapping("/packing/v1")
@Api(value="Pack Service", description="Operations pertaining to packing")
@RefreshScope
@Slf4j
public class PackingRestEndPoint {

    @Autowired
	PackingService packingService;
	
    @Value("${wms.service.health.msg: Packing Service - Config Server is not working..please check}")
    private String healthMsg;
    
    @Value("${wms.service.ready.msg: Packing Service - Not ready yet}")
    private String readyMsg;

	@GetMapping("/ready")
	public ResponseEntity ready() throws Exception {
		return ResponseEntity.ok(readyMsg);
	}
	
	@GetMapping("/health")
	public ResponseEntity health() throws Exception {
		return ResponseEntity.ok(healthMsg);
	}
	
	@GetMapping("/{busName}/{locnNbr}/packs/{id}")
	public ResponseEntity getByPackId(@PathVariable("busName") String busName, @PathVariable("locnNbr") Integer locnNbr, @PathVariable("id") Long packId) throws IOException {
		try {
			return ResponseEntity.ok(packingService.findByPackId(busName, locnNbr, packId));
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return ResponseEntity.badRequest().body(new ErrorRestResponse(HttpStatus.INTERNAL_SERVER_ERROR.value(), "Error occured while getting next pick task"));
		}
	}
	@GetMapping("/{busName}/{locnNbr}/packs")
	public ResponseEntity getPicks(@PathVariable("busName") String busName, @PathVariable("locnNbr") Integer locnNbr)
			throws IOException {
		try {
			return ResponseEntity.ok(packingService.findByBusNameAndLocnNbr(busName, locnNbr));
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return ResponseEntity.badRequest().body(new ErrorResourceDTO(HttpStatus.INTERNAL_SERVER_ERROR.value(),
					"Error occured while getting picks for busName:" + busName + ",locnNbr:" + locnNbr));
		}
	}
	@GetMapping("/{busName}/{locnNbr}/packs/order/{id}")
	public ResponseEntity getPacksByOrderId(@PathVariable("busName") String busName, @PathVariable("locnNbr") Integer locnNbr, @PathVariable("id") Long orderId) throws IOException {
		try {
			return ResponseEntity.ok(packingService.findByOrderId(busName, locnNbr, orderId));
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return ResponseEntity.badRequest().body(new ErrorRestResponse(HttpStatus.INTERNAL_SERVER_ERROR.value(), "Error occured while getting next pick task"));
		}
	}

	@GetMapping("/{busName}/{locnNbr}/packs/container/{containerNbr}")
	public ResponseEntity getPacksByContainerNbr(@PathVariable("busName") String busName, @PathVariable("locnNbr") Integer locnNbr, @PathVariable("containerNbr") String containerNbr) throws IOException {
		try {
			return ResponseEntity.ok(packingService.findByContainerNbr(busName, locnNbr, containerNbr));
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return ResponseEntity.badRequest().body(new ErrorRestResponse(HttpStatus.INTERNAL_SERVER_ERROR.value(), "Error occured while getting next pick task"));
		}
	}

	@PostMapping("/{busName}/{locnNbr}/packs/{id}")
	public ResponseEntity confirmPack(@PathVariable("busName") String busName,@PathVariable("locnNbr") Integer locnNbr, @PathVariable("id") Long id, @RequestBody PackConfirmRequestDTO pickReq) throws IOException {
		try {
			return ResponseEntity.ok(packingService.confirmPack(pickReq));
		} catch (Exception e) {
			
			// TODO Auto-generated catch block
			e.printStackTrace();
			return ResponseEntity.badRequest().body(new ErrorRestResponse(HttpStatus.INTERNAL_SERVER_ERROR.value(), "Error Occured for GET request busName:" + busName + ", id:" + id + " : " + e.getMessage()));
		}
	}	
	
	@PostMapping("/{busName}/{locnNbr}/packs/search")
	public ResponseEntity searchInventory(@PathVariable("busName") String busName, @PathVariable("locnNbr") Integer locnNbr,
			@RequestBody PackSearchRequestDTO packSearchReq) throws IOException {
		long startTime = System.currentTimeMillis();
		log.info("Received Pack search request for : " + packSearchReq.toString() + ": at :" + LocalDateTime.now());
		ResponseEntity resEntity = null;
		try {
			resEntity = ResponseEntity.ok().contentType(MediaType.APPLICATION_JSON)
					.body(packingService.searchPacks(packSearchReq));
		} catch (Exception e) {
			e.printStackTrace();
			resEntity = ResponseEntity.badRequest()
					.body(new ErrorResourceDTO(HttpStatus.INTERNAL_SERVER_ERROR.value(),
							"Error occured while searching for Packs:" + e.getMessage(), packSearchReq));
		}
		long endTime = System.currentTimeMillis();
		log.info("Completed Pack search request for : " + packSearchReq.toString() + ": at :" + LocalDateTime.now()
				+ " : total time:" + (endTime - startTime) / 1000.00 + " secs");
		return resEntity;
	}
	
}
