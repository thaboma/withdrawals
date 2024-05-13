package za.co.sanlam.fintech.withdrawal.dao;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import za.co.sanlam.fintech.withdrawal.entity.AccountAuditEvent;

@Repository("accountAuditEventRepository")
public interface AccountAuditEventRepository extends JpaRepository<AccountAuditEvent, Long> {
}
