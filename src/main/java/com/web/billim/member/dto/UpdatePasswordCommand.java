package com.web.billim.member.dto;

import com.web.billim.member.dto.request.UpdatePasswordRequest;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class UpdatePasswordCommand {

	private long memberId;
	private String password;
	private String newPassword;

	public UpdatePasswordCommand(long memberId, UpdatePasswordRequest req) {
		this.memberId = memberId;
		this.password = req.getPassword();
		this.newPassword = req.getNewPassword();
	}
}
