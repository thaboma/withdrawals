package za.co.sanlam.fintech.withdrawal.entity;

import jakarta.persistence.*;
import lombok.*;
import za.co.sanlam.fintech.withdrawal.enums.Status;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;
import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name = "account")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode
@Builder
public class Account implements Serializable {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	@Column(name = "account_name", nullable = false)
	private String accountName;

	@Column(name = "balance", nullable = false)
	private BigDecimal balance;

	@Column(name = "created_date")
	private Date createdDate;

	@Enumerated(EnumType.STRING)
	private Status status = Status.ACTIVE;

	@OneToMany(mappedBy = "account", fetch = FetchType.EAGER, cascade = { CascadeType.ALL }, orphanRemoval = true)
	@EqualsAndHashCode.Exclude
	private Set<Transaction> transactions = new HashSet<>();

	public Set<Transaction> getTransactions() {
		return transactions;
	}

	public void setTransactions(Set<Transaction> transactions) {
		if(this.transactions==null){
			this.transactions=new HashSet<>();
		}
		this.transactions.addAll(transactions);
	}

}
