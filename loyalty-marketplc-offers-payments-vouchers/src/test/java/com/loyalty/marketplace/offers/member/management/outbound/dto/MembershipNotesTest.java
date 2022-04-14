package com.loyalty.marketplace.offers.member.management.outbound.dto;

import static org.junit.Assert.assertNotNull;

import java.util.Date;

import org.junit.Before;
import org.junit.Test;
import org.mockito.MockitoAnnotations;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

@SpringBootTest(classes = MembershipNotes.class)
@ActiveProfiles("unittest")
public class MembershipNotesTest {

	private MembershipNotes membershipNotes;
	
	@Before
	public void setUp(){
		MockitoAnnotations.initMocks(this);
		membershipNotes = new MembershipNotes();
		
		membershipNotes.setNoteID("");
		membershipNotes.setNote("");
		membershipNotes.setCreatedUser("");
		membershipNotes.setCreatedDate(new Date());
		
		MembershipNotes member = new MembershipNotes("", "", "", new Date());
		member.setNoteID("");
	}
	
	/**
	 * 
	 * Test Getters
	 * 
	 */
	@Test
	public void testGetters() {
		assertNotNull(membershipNotes.getNoteID());
		assertNotNull(membershipNotes.getNote());
		assertNotNull(membershipNotes.getCreatedUser());
		assertNotNull(membershipNotes.getCreatedDate());
	}
	/**
	 * 
	 * Test ToString
	 * 
	 */
	@Test
	public void testToString() {
		assertNotNull(membershipNotes.toString());
	}
	
}
