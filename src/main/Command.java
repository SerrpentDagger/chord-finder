package main;

import java.util.ArrayList;
import java.util.HashMap;

public class Command
{
	private static final HashMap<String, Command> COMMANDS = new HashMap<String, Command>();
	
	private static final Command CLEAR = new Command("clear", "Clears all selected chords.", (set) -> { set.clear(); } );
	private static final Command ADD = new Command("add", "Adds the given chord to the set.", (set) -> {  } );
	
	///////////////////////
	
	public static ArrayList<Chordset> parse(String commands)
	{
		ArrayList<Chordset> set = new ArrayList<Chordset>();
		
		
		
		return set;
	}
	
	///////////////////////
	
	private final CmdAction action;
	private final String name;
	private final String tooltip;
	
	/////////////////////////
	
	public Command(String name, String tooltip, CmdAction action)
	{
		this.action = action;
		this.name = name;
		this.tooltip = tooltip;
		
		COMMANDS.put(name, this);
	}
	
	//////
	
	@Override
	public String toString()
	{
		return name + ": " + tooltip;
	}
	
	//////
	
	public static Command getCommand(String name)
	{
		return COMMANDS.get(name);
	}
	
	/////////////////////
	
	@FunctionalInterface
	public static interface CmdAction
	{
		public void action(ArrayList<Chordset> set);
	}
}
