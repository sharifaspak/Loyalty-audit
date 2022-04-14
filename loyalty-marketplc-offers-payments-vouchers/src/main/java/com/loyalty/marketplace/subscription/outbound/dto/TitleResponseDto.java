package com.loyalty.marketplace.subscription.outbound.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@JsonInclude(Include.NON_NULL)
@Setter
@Getter
@ToString
@NoArgsConstructor
@AllArgsConstructor
public class TitleResponseDto {

	private String id;
	private String english;
	private String arabic;
	
	@Override
    public boolean equals(Object obj) {
        if(obj instanceof TitleResponseDto)
        {
        	TitleResponseDto temp = (TitleResponseDto) obj;
            if(this.id == temp.id)
                return true;
        }
        return false;

    }
    @Override
    public int hashCode() {
        
        return this.id.hashCode();        
    }
}
