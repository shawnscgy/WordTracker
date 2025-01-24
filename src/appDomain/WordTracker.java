package appDomain;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.util.List;
import java.util.Map;

import exceptions.NoSpecifiedFilenameException;
import exceptions.NotEnoughArgumentsException;
import implementations.BSTree;
import implementations.BSTreeNode;
import utilities.Iterator;

public class WordTracker implements Serializable
{
	private static final long serialVersionUID = -5636044286998407308L;
	private static final String REPOSITORY_FILE = "repository.ser";
	private BSTree<WordMetadata> wordTree;
	public WordTracker()
	{
		wordTree = new BSTree<WordMetadata>();
		loadRepository();
	}
	/**
	 * Load the existing data of the words with their occurrences(metadata) into a tree.
	 * One metadata contains a word and a hashmap containing the occurrences to be the properties.
	 * These metadatas constitute a BSTree
	 * <p>This method reads the serialized BSTree from {@link #REPOSITORY_FILE}.</p>
	 */
	@SuppressWarnings("unchecked")
	private void loadRepository()
	{
		File repoFile = new File(REPOSITORY_FILE);
		if(repoFile.exists())
		{
			System.out.println(repoFile.getPath());
			try(ObjectInputStream ois = new ObjectInputStream(
					new BufferedInputStream(new FileInputStream(repoFile))))
			{
				this.wordTree = (BSTree<WordMetadata>) ois.readObject();
			}
			catch(IOException | ClassNotFoundException e)
			{
				System.err.println("Loading Error: " + e.getMessage());
			}
		}
		else
		{
			this.wordTree = new BSTree<WordMetadata>();
		}
	}
	
	/**
	 * Process the input file which is intended to be read,
	 * populating the BSTree with words and corresponding occurrences.
	 * 
	 * @param inputFile
	 * @throws IOException
	 */
	public void processFile(String inputFile) throws IOException
	{
		// Resolve file name/path
		File file = new File(inputFile);
		String resolvedFile = inputFile;
		if(!file.exists())
		{
			if(!inputFile.startsWith("res\\"))
			{
				resolvedFile = "res\\" + inputFile;
				file = new File(resolvedFile);
			}
			if(!inputFile.endsWith(".txt"))
			{
				resolvedFile = resolvedFile + ".txt";
				file = new File(resolvedFile);
			}
			if(!file.exists())
			{
				throw new FileNotFoundException(String.format("File: \'%s\' not found.", inputFile));
			}
		}
		
		// Read the file line by line(loop of lines in the file),
		// then word by word(loop of words in each line),
		// and populate tree with words that occur in the file
		try(BufferedReader reader = new BufferedReader(new FileReader(file)))
		{
			// Line loop
			String line;
			int lineNumber = 1;
			while((line = reader.readLine()) != null)
			{
				// Remove everything leaving only words and spaces
				String[] words = line.replaceAll("[^a-zA-Z\\s]", "").split("\\s+");
				// word loop
				for(String word : words)
				{
					if(!word.equals(""))
					{
						WordMetadata wordMetadata = new WordMetadata(word);
						// Search the node with the word in the existing tree
						WordMetadata searchKey = new WordMetadata(word.toLowerCase());
						BSTreeNode<WordMetadata> node = wordTree.search(searchKey);
						// If the node doesn't exist, which means the word doesn't exist in repo file, 
						// then create new node with the word and add it to the tree
						if(node == null)
						{
							wordMetadata.addOccurrence(inputFile, lineNumber);
							wordTree.add(wordMetadata);
						}
						// If the node does exist, add the new occurrence
						else
						{
							node.getElement().addOccurrence(inputFile, lineNumber);
						}
					}
				}
				lineNumber++;
			}
		}
	}
	/**
	 * Serialize the updated wordTree and save it to {@link #REPOSITORY_FILE}
	 */
	private void saveRepositry()
	{
		try(ObjectOutputStream oos = new ObjectOutputStream(
				new BufferedOutputStream(new FileOutputStream(REPOSITORY_FILE))))
		{
			oos.writeObject(wordTree);
		}
		catch(IOException e)
		{
			System.err.println("Saving error: " + e.getMessage());
		}
	}
	/**
	 * Print the report displaying:
	 * <ul>
	 * 	<li>-pf: words and the corresponding files they are in</li>
	 * 	<li>-pl: words and the corresponding lines and files they are in</li>
	 * 	<li>-po: words and the corresponding occurrence information</li>
	 * </ul>
	 * @param outputOption
	 * @param outputFile
	 * @throws IOException 
	 */
	public void generateReport(String outputOption, String outputFile) throws IOException
	{
		// Print Report
		StringBuilder report = new StringBuilder();
		report.append("Displaying " + outputOption + " format\n");
		Iterator<WordMetadata> it = wordTree.inorderIterator();
		WordMetadata wm;
		switch(outputOption)
		{
			case "-pf":
				while(it.hasNext())
				{
					wm = it.next();
					report.append("Key : ===" + wm.getWord() + "=== " + "found in file: " 
							+ String.join(", ", wm.getOccurrences().keySet()) + "\n");
				}
				break;
			case "-pl":
				while(it.hasNext())
				{
					wm = it.next();
					report.append("Key : ===" + wm.getWord() + "=== " + buildFilesAndLines(wm) + "\n");
				}
				break;
			case "-po":
				while(it.hasNext())
				{
					wm = it.next();
					int occurNumber = 0;
					for(List<Integer> lines : wm.getOccurrences().values())
					{
						occurNumber += lines.size();
					}
					report.append("Key : ===" + wm.getWord() + "=== " 
							+ "number of entries: " + occurNumber + " " + buildFilesAndLines(wm) + "\n");
				}
				break;
			default:
				System.out.println("Invalid option: " + outputOption);
				return;
		}
		System.out.println(report.toString());
		
		// Write report into file
		if(outputFile != null)
		{
			// Resolve filename
			File file = new File(outputFile);
			String resolvedOutputFile = outputFile;
			//   1. Add suffix ensuring the file type
			if(!resolvedOutputFile.endsWith(".txt"))
			{
				resolvedOutputFile = outputFile + ".txt";
				file = new File(resolvedOutputFile);
			}
			//   2. Make parent directory if doesn't exist
			File parentDirectory = new File(resolvedOutputFile).getParentFile();
			// null object cannot call function. If it is null, the file will create in the root dir
			if(parentDirectory != null && !parentDirectory.exists()) 
			{
				parentDirectory.mkdir();
			}
			try(BufferedWriter writer = new BufferedWriter(new FileWriter(resolvedOutputFile)))
			{
				writer.write(report.toString());
			}
			System.out.println("\n Report is exporting to: " + resolvedOutputFile);
		}
		else
		{
			System.out.println("Report is NOT saved without specified Exporting File.");
		}
	}
	// A method for print files and lines in the report
	private String buildFilesAndLines(WordMetadata wm)
	{
		StringBuilder filesAndLines = new StringBuilder();
		int filesCount = 0;
		for(Map.Entry<String, List<Integer>> entry :  wm.getOccurrences().entrySet())
		{
			filesAndLines.append("found in file: " + entry.getKey() + " on lines: ");
			List<Integer> lines = entry.getValue();// value
			int linesSize = lines.size();
			// add numbers in the List using for loop 
			for(int i = 0; i < linesSize; i++)
			{
				filesAndLines.append(lines.get(i));
				// Determine if it's the last line number
				if(i < linesSize - 1)
				{
					filesAndLines.append(", ");
				}
			}
			// Determine if it's the last file: if last append '.', if not ','
			filesCount++;
			if(filesCount < wm.getOccurrences().entrySet().size())
			{
				filesAndLines.append(", ");
			}
			else
			{
				filesAndLines.append(".");
			}
		}
		return filesAndLines.toString();
	}
	
	// MAIN
	public static void main(String[] args)
	{
		try
		{
			// Check right count of arguments 
			if(args.length < 2)
			{
				throw new NotEnoughArgumentsException();
			}
			// Parse the arguments
			String inputFile = args[0];
			String outputOption = args[1];
			String outputFile = null;
			// Check output file name
			if(args.length >= 3)
			{
				String outputArg = args[2];
				// Check empty
				if(outputArg.startsWith("-f"))
				{
					outputFile = outputArg.substring(2);
					if(outputFile.isEmpty())
					{
						throw new NoSpecifiedFilenameException();
					}
				}
				// Check invalid
				else
				{
					throw new NotEnoughArgumentsException();
				}
			}
			// Compute
			WordTracker tracker = new WordTracker();
			
			tracker.processFile(inputFile);
			tracker.saveRepositry();
			tracker.generateReport(outputOption, outputFile);
		}
		catch(Exception e)
		{
			System.err.println(e.getMessage());
		}
	}
}
