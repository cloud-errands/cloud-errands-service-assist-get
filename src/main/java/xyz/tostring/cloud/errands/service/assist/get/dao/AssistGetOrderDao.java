package xyz.tostring.cloud.errands.service.assist.get.dao;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import xyz.tostring.cloud.errands.service.assist.get.entity.AssistGetOrderDO;

import java.util.List;

@Repository
public interface AssistGetOrderDao extends JpaRepository<AssistGetOrderDO, Long> {
    List<AssistGetOrderDO> findAllByOrderByLatestUpdateTimeDesc();

    List<AssistGetOrderDO> findAllByUserOpenIdOrderByLatestUpdateTimeDesc(String userOpenId);

    List<AssistGetOrderDO> findAllByPayStatusOrderByLatestUpdateTimeAsc(Integer buyStatus);

    List<AssistGetOrderDO> findAllByPayStatusOrderByLatestUpdateTimeDesc(Integer buyStatus);

    List<AssistGetOrderDO> findAllByUserOpenIdAndPayStatusOrderByLatestUpdateTimeDesc(String userOpenId, Integer buyStatus);
}
