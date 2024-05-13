package za.co.sanlam.fintech.withdrawal.entity;

import jakarta.persistence.*;
import lombok.*;

import java.io.Serializable;
import java.util.Date;

@Entity
@Table(name = "account_audit_event")
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode
@ToString
@Getter
@Setter
@Builder
public class AccountAuditEvent implements Serializable {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	@Column(name = "account_id", nullable = false)
	private Long accountId;

	@Column(name = "created_date", nullable = false)
	private Date createddate;

	private String status;

	@Column(name = "payload", nullable = false)
	private String payload;

}
