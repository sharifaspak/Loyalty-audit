package com.loyalty.marketplace.outbound.events.eventobject;

import java.io.Serializable;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
@NoArgsConstructor
public class Event implements Serializable {

	private static final long serialVersionUID = 5357238694787330241L;
	private String channelId;
	private String systemId;
	private String systemPassword;
	private String token;
	private String eventType;

}
