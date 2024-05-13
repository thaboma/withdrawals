package za.co.sanlam.fintech.withdrawal.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;
import lombok.*;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;
import java.util.UUID;

@Entity
@Table(name = "transaction")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode
@Builder
public class Transaction implements Serializable {

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(
			name = "account_id", referencedColumnName = "id", updatable = false, insertable = false

	)
	Account account;

	@Id
	@GeneratedValue(strategy = GenerationType.UUID)
	private UUID id;

	@Column(name = "account_id", nullable = false)
	private Long accountId;

	@Column(name = "amount", nullable = false)
	private BigDecimal amount;

	@Column(name = "transaction_date")
	private Date transactionDate;

	@Column(name = "type")
	@Enumerated(EnumType.STRING)
	private Type type;

	public enum Type {ADD, DEPOSIT,WITHDRAWAL}
}


