package com.loyalty.marketplace.gifting.service.dto;

import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Setter
@Getter
@ToString
@NoArgsConstructor
@AllArgsConstructor
public class GetListMemberResponse {
	
	private List<GetListMemberResponseDto> listMember;

}
