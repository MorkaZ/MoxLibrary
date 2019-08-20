package com.morkaz.moxlibrary.other.commandusagebuilder;

import java.util.List;

public class CommandUsageBuilder {

	private String command;
	private List<Argument> arguments;

	public CommandUsageBuilder(String command){
		this.command = command;
	}

	public CommandUsageBuilder(String command, List<Argument> arguments){
		this.command = command;
		this.arguments = arguments;
	}

	public String getCommand() {
		return command;
	}

	public void setCommand(String command) {
		this.command = command;
	}

	public List<Argument> getArguments() {
		return arguments;
	}

	public void setArguments(List<Argument> arguments) {
		this.arguments = arguments;
	}

	public void addArgument(Argument argument){
		arguments.add(argument);
	}

}
