package za.co.sanlam.fintech.withdrawal.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import za.co.sanlam.fintech.withdrawal.dto.WithdrawalRequestDto;
import za.co.sanlam.fintech.withdrawal.dto.WithdrawalResponseDto;
import za.co.sanlam.fintech.withdrawal.entity.Account;
import za.co.sanlam.fintech.withdrawal.service.AccountService;

@RestController
@RequestMapping("/v1/account")
@RequiredArgsConstructor
public class AccountsController {

	private final AccountService service;

	@PostMapping("/withdraw")
	public ResponseEntity<WithdrawalResponseDto> withdraw(@RequestBody WithdrawalRequestDto withdrawalRequestDto) {
		var withdrawalResponseDto = service.withdraw(withdrawalRequestDto);
		return new ResponseEntity<>(withdrawalResponseDto, HttpStatus.OK);
	}

	@PostMapping("/save")
	public ResponseEntity<Void> save(@RequestBody Account account) {
		service.saveOrUpdateAccount(account);
		return new ResponseEntity<>(HttpStatus.OK);
	}
}