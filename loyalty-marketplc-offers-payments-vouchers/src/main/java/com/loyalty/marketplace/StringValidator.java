package com.loyalty.marketplace;


import java.util.function.Predicate;

public enum StringValidator implements Predicate<String> {

	VALIDATE_STRING(input -> input != null && !input.isEmpty());

	private final Predicate<String> predicate;

	StringValidator(Predicate<String> predicate) {
		this.predicate = predicate;
	}

	public boolean test(String s) {
		return predicate.test(s);
	}

	public Predicate<String> getPredicate() {
		return predicate;
	}

	

	
}