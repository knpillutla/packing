package com.example.packing.db;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface PackingRepository extends JpaRepository<Pack, Long>{
	@Query("select s from Pack s where s.busName=:busName and s.locnNbr=:locnNbr and s.orderId=:orderId")
	public List<Pack> findByBusNameAndLocnNbrAndOrderId(@Param("busName") String busName, @Param("locnNbr") Integer locnNbr, @Param("orderId") Long orderId);

	@Query("select s from Pack s where s.busName=:busName and s.locnNbr=:locnNbr and s.orderNbr=:orderNbr")
	public List<Pack> findByBusNameAndLocnNbrAndOrderNbr(@Param("busName") String busName, @Param("locnNbr") Integer locnNbr, @Param("orderNbr") String orderNbr);

	@Query("select s from Pack s where s.busName=:busName and s.locnNbr=:locnNbr and s.batchNbr=:batchNbr")
	public List<Pack> findByBusNameAndLocnNbrAndBatchNbr(@Param("busName") String busName, @Param("locnNbr") Integer locnNbr, @Param("batchNbr") String batchNbr);

	@Query("select s from Pack s where s.busName=:busName and s.locnNbr=:locnNbr and s.fromContainer=:containerNbr")
	public List<Pack> findByBusNameAndLocnNbrAndContainerNbr(@Param("busName") String busName, @Param("locnNbr") Integer locnNbr, @Param("containerNbr") String containerNbr);

	@Query("select s from Pack s where s.busName=:busName and s.locnNbr=:locnNbr and s.id=:pickId")
	public Pack findByPickId(@Param("busName") String busName, @Param("locnNbr") Integer locnNbr, @Param("pickId") Long pickId);
}
