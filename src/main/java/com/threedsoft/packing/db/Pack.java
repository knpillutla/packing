package com.threedsoft.packing.db;

import java.io.Serializable;
import java.time.LocalDateTime;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EntityListeners;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Version;

import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import lombok.Data;

@Entity
@Data
@Table(name="packs")
@EntityListeners(AuditingEntityListener.class)
public class Pack  implements Serializable{
	@Column(name="ID")
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	Long id;

	@Column(name="PICK_ID")
	Long pickId;

	@Column(name="ORDER_ID")
	Long orderId;

	@Column(name="ORDER_LINE_ID")
	Long orderLineId;

	@Column(name="ORDER_NBR")
	String orderNbr;

	@Column(name="ORDER_LINE_NBR")
	Integer orderLineNbr;

	@Column(name="BATCH_NBR")
	String batchNbr;

	@Column(name="BUS_NAME")
	String busName;

	@Column(name="LOCN_NBR")
	Integer locnNbr;

	@Column(name="BUS_UNIT")
	String busUnit;

	@Column(name="COMPANY")
	String company;
	
	@Column(name="DIVISION")
	String division;
	
	@Column(name="ITEM_BRCD")
	String itemBrcd;

	@Column(name="QTY")
	Integer qty;

	@Column(name="PACKED_QTY")
	Integer packedQty;

	@Column(name="STATUS")
	String status;

	@Column(name="FROM_CONTAINER_NBR")
	String fromContainer;

	@Column(name="TO_CONTAINER_NBR")
	String toContainer;

	@Column(name="SINGLES")
	String singles;

	@Column(name="PACKAGE_NBR")
	String packageNbr;

	@Column(name="TRANSACTION_NAME")
	String transName;
	
	@Column(name="SOURCE")
	String source;

	@Column(name="HOST_NAME")
	String hostName;

    @CreatedDate
	@Column(name="CREATED_DTTM", nullable = false, updatable = false)
    LocalDateTime createdDttm;
	
    @Column(name = "UPDATED_DTTM", nullable = false)
    @LastModifiedDate
	LocalDateTime updatedDttm;
	
	@Column(name="USER_ID")
	String userId;
	
	@Version
 	@Column(name="VERSION")
	Integer version; 	
}
