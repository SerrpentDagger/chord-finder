package main;

import java.io.PrintStream;
import java.util.Arrays;
import java.util.Comparator;
import java.util.Scanner;

public class Main
{
	static String[] notes = new String[] { "A", "A#", "B", "C", "C#", "D", "D#", "E", "F", "F#", "G", "G#" };
	
	public static String digitize(String raw)
	{
		raw = raw.toUpperCase().replaceAll("M", "m").replaceAll(" ", "");
		for (int i = notes.length - 1; i >= 0; i--)
			raw = raw.replaceAll(notes[i], "" + i);
		return raw;
	}
	
	public static String minLen(String str, int min)
	{
		for (int i = str.length(); i < min; i++)
			str += " ";
		return str;
	}
	
	public static Integer[] fillAscending(Integer[] array)
	{
		for (int i = 0; i < array.length; i++)
			array[i] = i;
		return array;
	}
	
	public static void main(String[] args)
	{
		Scanner in = new Scanner(System.in);
		PrintStream out = System.out;
		
		int[] indicies = null;
		boolean[] minor = null;
		
		boolean invalid = true;
		while (invalid)
		{
			invalid = false;
			
			out.println("Input the relevant chords as a comma-separated list of names/indicies from those below:");		
			out.println("0  1  2  3  4  5  6  7  8  9  10 11");
			out.println("A  A# B  C  C# D  D# E  F  F# G  G#");
			out.println("Example: 'A,2,m 3,DM' includes the chords A, B, Cm, and Dm.");
			out.print("Chord Indicies: ");
			
			String[] inputs = digitize(in.nextLine()).split(",");
			indicies = new int[inputs.length];
			minor = new boolean[inputs.length];
			
			for (int i = 0; i < inputs.length; i++)
			{
				String str = inputs[i];
				minor[i] = str.contains("m");
				str = str.replaceAll("m", "");
				try
				{
					indicies[i] = Integer.parseInt(str);
					if (indicies[i] > 11 || indicies[i] < 0)
						throw new NumberFormatException();
				}
				catch (NumberFormatException e)
				{
					out.println("Invalid input for token \"" + inputs[i].trim() + "\".");
					out.println("Every comma-separated value must be an integer between 0 and 11 inclusive or a chord name selected from the list, with the optional inclusion of 'm' or 'M' to signal a minor chord.");
					out.println();
					invalid = true;
					break;
				}
			}
		}
		assert indicies != null && minor != null;
		
		Integer[] order = fillAscending(new Integer[indicies.length]);
		
		final int[] ind = indicies; 
		Comparator<Integer> inSort = (a, b) -> { return Integer.compare(ind[a], ind[b]); };
		Arrays.sort(order, inSort);
		int[] tmp1 = new int[indicies.length];
		boolean[] tmp2 = new boolean[indicies.length];
		for (int i = 0; i < order.length; i++)
		{
			tmp1[i] = indicies[order[i]];
			tmp2[i] = minor[order[i]];
		}
		indicies = tmp1;
		minor = tmp2;
		
		out.print("Chords for when capo is at fret: ");
		
		int capo = in.nextInt();
		
		out.println();
		out.println("Chord Letter -> " + capo + "th-Fret Shapes:");
		
		int ln = indicies.length;
		Chordset[] chordsets = new Chordset[ln];
		for (int i = 0; i < ln; i++)
		{
			chordsets[i] = new Chordset(indicies[i], capo, 1, minor[i]);
			chordsets[i].print();
		}
		
		out.println();
		out.println("Assign priorities to the chords you selected by inputting a list of comma-separated values.");
		out.println("The position of the priority value in the list corresponds to that of the chord letter in your selection.");
		out.println();
		out.println("Example:");
		out.println("A  B  C  D");
		out.println("1, 2, 3, 4");
		out.println("This configuration assigns a priority of 1 to A, 2 to B, 3 to C, and 4 to D.");
		out.println();
		out.println("Higher priority chords will shift the capo recommendation towards placements facilitating the playing of those chords.");
		out.println("Lower priority chords will have less of an impact on the recommendation.");
		out.println("Input a single comma to assign equal priority to all chords in your selection.");
		
		
		double[] priorities = new double[indicies.length];
		Arrays.fill(priorities, 1);
		
		invalid = true;
		while (invalid)
		{
			out.println();
			out.println("Input a priority list for your selection:");
			for (Chordset set : chordsets)
				out.print(minLen(set.note, 4));
			out.println();
			
			String[] inputs = in.next().split(",");
			for (int i = 0; i < inputs.length && i < priorities.length; i++)
			{
				try
				{
					priorities[i] = Double.parseDouble(inputs[i].trim());
				}
				catch (NumberFormatException e)
				{
					out.println("Invalid input for token \"" + inputs[i].trim() + "\".");
					out.println("Every comma-separated value must be an number.");
					continue;
				}
			}
			
			invalid = false;
		}
		
		for (int i = 0; i < chordsets.length; i++)
			chordsets[i].priority = priorities[i];
		
		double[] scores = Chordset.capoScores(chordsets);
		Integer[] frets = new Integer[scores.length];
		frets = fillAscending(frets);
		
		Comparator<Integer> comp = (a, b) -> { return Double.compare(scores[b], scores[a]); };
		Arrays.sort((Integer[]) frets, comp);

		out.println();
		out.println("Fret scores:");
		for (int i = 0; i < scores.length; i++)
		{
			out.print(minLen("" + frets[i], 3) + ": ");
			out.println(scores[frets[i]]);
		}
		
		out.println();
		out.println("Chord Letter -> " + frets[0] + "th-Fret Shapes:");
		
		for (int i = 0; i < ln; i++)
		{
			chordsets[i] = new Chordset(chordsets[i].note_index, frets[0], 1, chordsets[i].minor);
			chordsets[i].print();
		}
		
		in.close();
		
	}
}
