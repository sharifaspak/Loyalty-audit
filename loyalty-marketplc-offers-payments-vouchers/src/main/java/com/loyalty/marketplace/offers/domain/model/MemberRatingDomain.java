package com.loyalty.marketplace.offers.domain.model;

import java.util.List;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;

/**
 * 
 * @author jaya.shukla
 *
 */
@Getter
@ToString
@NoArgsConstructor
public class MemberRatingDomain {

	private String accountNumber;
	private String membershipCode;
	private String firstName;
	private String lastName;
	private List<MemberCommentDomain> comments;

	public MemberRatingDomain(MemberRatingBuilder memberRating) {

		this.accountNumber = memberRating.accountNumber;
		this.membershipCode = memberRating.membershipCode;
		this.firstName = memberRating.firstName;
		this.lastName = memberRating.lastName;
		this.comments = memberRating.comments;

	}

	public static class MemberRatingBuilder {

		private String accountNumber;
		private String membershipCode;
		private String firstName;
		private String lastName;
		private List<MemberCommentDomain> comments;

		public MemberRatingBuilder(String accountNumber, String membershipCode, String firstName, String lastName,
				List<MemberCommentDomain> comments) {

			this.accountNumber = accountNumber;
			this.membershipCode = membershipCode;
			this.firstName = firstName;
			this.lastName = lastName;
			this.comments = comments;

		}

		public MemberRatingDomain build() {
			return new MemberRatingDomain(this);
		}

	}

}
