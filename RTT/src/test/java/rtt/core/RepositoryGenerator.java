/**
 * <copyright>
 *
 * This program and the accompanying materials are made available under the
 * terms of the MIT license (X11 license) which accompanies this distribution.
 *
 * </copyright>
 */
package rtt.core;

import java.io.*;
import java.util.*;

import rtt.annotations.*;

/**
 * A generator that given an input stream {@link #nextChunk() splices it into
 * chunks of words} and stores them in a --- depending on the first word
 * randomly --- {@link #createRepository() generated tree structured
 * repository}.
 * @author C. Bürger
 */
@Node
public final class RepositoryGenerator {
	private InputStream is;
	
	/**
	 * Initialize a new repository generator configured with the given input
	 * stream as source for splicing and repository generation.
	 * @param is Input consumed while and needed for repository generation.
	 */
	@Node.Initialize(acceptedExceptions=RuntimeException.class)
	public RepositoryGenerator(InputStream is) {this.is = is;}
	
	/**
	 * Simple slicer, returning the next word chunk of the configured input
	 * each time it is called.
	 * @return The next word of the input.
	 * @throws IOException Thrown, iff the input cannot be read.
	 */
	public Value nextChunk() throws IOException {
		Value result = new Value();
		int read;
		for (read = is.read();
				read == ' ' || read == '\t' || read == '\n' || read == '\r';
				read = is.read())
			;
		if (read >= 0)
			result.value = result.value + String.valueOf((char)read);
		for (read = is.read();
				read >= 0 && read != ' ' && read != '\t' &&
					read != '\n' && read != '\r';
				read = is.read())
			result.value = result.value + String.valueOf((char)read);
		return result;
	}
	
	/**
	 * Generate a tree structured repository for the configured input. The
	 * generated repository has a random tree structure, iff the first input
	 * chunk is not a number. Iff it is a negative number a {@link
	 * RuntimeException runtime exception} is thrown. Otherwise, the positive
	 * number is used to determine the random generator.
	 * @return A (randomly) generated repository.
	 * @throws IOException Thrown, iff the input cannot be read.
	 * @throws RuntimeException Thrown, if the first input chunk is a negative
	 * number.
	 */
	@Node.Value public Repository createRepository() throws IOException {
		// Initialize the repository:
		Value value = nextChunk();
		Repository repository = new Repository(value.value, new Repository[2]);
		
		// Try to initialize the random generator with the first chunk as seed:
		Random rand = new Random();
		try {
			long seed = Long.parseLong(value.value);
			if (seed < 0)
				throw new RuntimeException(
						"Test exception to check RTT's exception behavior.");
			rand.setSeed(seed);
		} catch (NumberFormatException exc) {}
		
		// Do a breadth-first search to construct a random repository:
		List<Repository[]> fronts = new LinkedList<Repository[]>();
		fronts.add(repository.children);
		int frontPosition = 0;
		while(!value.isEOF()) { // Expand the tree as long as there is input:
			Repository[] frontFragment = fronts.remove(frontPosition);
			int newFrontsCount = 0;
			for (int i = 0; i < frontFragment.length; i++) {
				value = nextChunk();
				switch (i == frontFragment.length - 1 && newFrontsCount == 0 ?
						0 : Math.abs(rand.nextInt()) % 3) {
				case 0:
					frontFragment[i] = new Repository(
							value.value,
							new Repository[Math.abs(rand.nextInt()) % 4 + 2]);
					fronts.add(frontPosition++,
							frontFragment[i].children);
					newFrontsCount++;
					break;
				default: frontFragment[i] = new Repository(value.value);
				}
			}
			if (frontPosition == fronts.size())
				frontPosition = 0;
		}
		while (!fronts.isEmpty()) { // Finish the tree:
			Repository[] frontFragment = fronts.remove(0);
			for (int i = 0; i < frontFragment.length; i++)
				frontFragment[i] = new Repository(value.value);
		}
		
		// Done; return the generated repository:
		return repository;
	}
	
	public static final class Value {
		public String value = "";
		public boolean isEOF() {return value.length() == 0;}
	}
	
	@Node public static final class Repository {
		public Repository[] children = new Repository[0];
		@Node.Value public List<Repository> children() {
			return Arrays.asList(children);
		}
		@Node.Value public String value = "";
		public Repository(String value, Repository ...children) {
			this.value = value;
			this.children = children;
		}
		public Repository(Repository ...children) {this.children = children;}
		public Repository(String value) {this.value = value;}
	}
}
