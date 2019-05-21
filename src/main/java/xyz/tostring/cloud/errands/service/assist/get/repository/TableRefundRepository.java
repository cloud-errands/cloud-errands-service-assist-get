package xyz.tostring.cloud.errands.service.assist.get.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import xyz.tostring.cloud.errands.service.assist.get.entity.TableRefundDO;

@Repository
public interface TableRefundRepository extends JpaRepository<TableRefundDO, Long> {
    TableRefundDO findAllByOrderId(Long orderId);
}
