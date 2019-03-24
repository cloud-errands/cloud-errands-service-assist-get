package xyz.tostring.cloud.errands.service.assist.get.dao;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import xyz.tostring.cloud.errands.service.assist.get.entity.AssistGetOrderDO;

import java.util.List;

@Repository
public interface AssistGetOrderDao extends JpaRepository<AssistGetOrderDO, Long> {
    List<AssistGetOrderDO> findAllByOrderByLatestUpdateTimeDesc();

    List<AssistGetOrderDO> findAllByUserOpenIdOrderByLatestUpdateTimeDesc(String userOpenId);

    List<AssistGetOrderDO> findAllByOrderStatusOrderByLatestUpdateTimeAsc(Integer buyStatus);

    List<AssistGetOrderDO> findAllByOrderStatusOrderByLatestUpdateTimeDesc(Integer buyStatus);

    List<AssistGetOrderDO> findAllByUserOpenIdAndOrderStatusOrderByLatestUpdateTimeDesc(String userOpenId, Integer buyStatus);

    List<AssistGetOrderDO> findAllByUserOpenIdAndOrderStatusOrderByLatestUpdateTimeAsc(String userOpenId, Integer buyStatus);

}
