package xyz.tostring.cloud.errands.service.assist.get.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;
import xyz.tostring.cloud.errands.service.assist.get.entity.TableOrderDO;

import java.util.List;

@Repository
public interface TableOrderRepository extends JpaRepository<TableOrderDO, Long> , JpaSpecificationExecutor<TableOrderDO> {
    List<TableOrderDO> findAllByUserOpenId(String userOpenId);

    List<TableOrderDO> findAllByOrderStatus(Integer buyStatus);

    List<TableOrderDO> findAllByUserOpenIdAndOrderStatus(String userOpenId, Integer buyStatus);

}
