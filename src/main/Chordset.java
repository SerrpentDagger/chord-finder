package main;

import java.util.LinkedHashMap;

public class Chordset
{
	//////////////////////
	
	private LinkedHashMap<String, Integer> chordset = new LinkedHashMap<String, Integer>();
	public final int note_index;
	public final String note;
	public final int capo;
	public final boolean minor;
	public double priority;
	
	public Chordset(int note_index, int capo, double priority, boolean minor)
	{
		this.note_index = note_index;
		this.note = Main.notes[note_index] + (minor ? "m" : "");
		this.capo = capo;
		this.priority = priority;
		this.minor = minor;
		
		put("A", 0).put("C", 9).put("D", 7).put("E", 5).put("F", 4).put("G", 2);
	}
	
	public Chordset(int note_index, int capo, double priority)
	{
		this(note_index, capo, priority, false);
	}
	
	public Chordset(int note_index, int capo)
	{
		this(note_index, capo, 1);
	}
	
	/////////////////////////////////
	
	private Chordset put(String barshape, int offset)
	{
		if (minor && (barshape.equals("C") || barshape.equals("F") || barshape.equals("G")))
				return this;
		chordset.put(barshape + (minor ? "m" : "") + "bar", (12 + note_index + offset - capo)%12);
		return this;
	}
	
	///////////////////////////////
	
	public void print()
	{
		System.out.print(Main.minLen(note, 3) + "  ->  ");
		chordset.forEach((st, in) ->
		{
			System.out.print(Main.minLen((st + in).replaceAll("bar0", ""), (minor && st.equals("Ambar") ? 2 : 1) * 8));
		});
		System.out.println();
	}
	
	//////////////////////////////
	
	public static double[] capoScores(Chordset[] sets)
	{
		double[] scores = new double[12];
		for (int i = 0; i < sets.length; i++)
		{
			Chordset set = sets[i];
			if (set != null)
			{
				set.chordset.forEach((shape, fret) ->
				{
					scores[(fret + set.capo)%12] += set.priority;
				});
			}
		}
		
		return scores;
	}
	
}
