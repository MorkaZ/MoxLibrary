package com.morkaz.moxlibrary.other.commandusagebuilder;

public class Argument {

	private String argumentName, givenArgument;
	private boolean required;

	public Argument(String argumentName, String givenArgument, boolean required){
		this.argumentName = argumentName;
		if (givenArgument == null || givenArgument.equals("")){
			this.givenArgument = "?";
		} else {
			this.givenArgument = givenArgument;
		}
		this.required = required;
	}

	public String getArgumentName() {
		return argumentName;
	}

	public void setArgumentName(String argumentName) {
		this.argumentName = argumentName;
	}

	public String getGivenArgument() {
		return givenArgument;
	}

	public void setGivenArgument(String givenArgument) {
		this.givenArgument = givenArgument;
	}

	public boolean isRequired() {
		return required;
	}

	public void setRequired(boolean required) {
		this.required = required;
	}
}
