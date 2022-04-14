package com.loyalty.marketplace.offers.domain.model;

import java.util.Date;

import lombok.AllArgsConstructor;
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
@AllArgsConstructor
public class MemberCommentDomain {

	private Integer rating;
	private String comment;
	private Date reviewDate;
	
	public MemberCommentDomain(MemberCommentBuilder memberComment) {
		this.rating = memberComment.rating;
		this.comment = memberComment.comment;
		this.reviewDate = memberComment.reviewDate;
	}

	public static class MemberCommentBuilder {

		private Integer rating;
		private String comment;
		private Date reviewDate;
		
		public MemberCommentBuilder(Date reviewDate) {
			this.reviewDate = reviewDate;
		}

		public MemberCommentBuilder rating(Integer rating) {
			this.rating = rating;
			return this;
		}
		
		public MemberCommentBuilder comment(String comment) {
			this.comment = comment;
			return this;
		}
		
		public MemberCommentDomain build() {
			return new MemberCommentDomain(this);
		}

	}
	
}
