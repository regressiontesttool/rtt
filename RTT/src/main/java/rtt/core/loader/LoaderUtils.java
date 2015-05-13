package rtt.core.loader;

import java.io.File;

/**
 * This class contains some static methods to assist an {@link ArchiveLoader}.
 * 
 * @author Christian Oelsner <C.Oelsner@gmail.com>
 * @see ArchiveLoader
 */
public class LoaderUtils {
	/**
	 * Returns a string containing a path, with all given elements.
	 * <p>
	 * For example "elements, of, path" will result in "elements/of/path"
	 * 
	 * @param elements
	 *            elements of the path
	 * @return string containing the path
	 */
	public static final String getPath(String... elements) {
		return getFilePath(null, elements);
	}

	/**
	 * Returns a string containing a path to the given file, with all given
	 * elements
	 * <p>
	 * For example "test.txt, elements, of, path" will result in
	 * "elements/of/path/text.txt".
	 * 
	 * @param fileName
	 *            the name of the file (with extension, if needed)
	 * @param pathElements
	 *            elements of the path
	 * @return a string containing the path to the the file
	 */
	public static final String getFilePath(String fileName,
			String... pathElements) {
		StringBuilder path = new StringBuilder();

		for (String element : pathElements) {
			if (element != null && element.equals("") == false) {
				path.append(element);

				if (element.endsWith(File.separator) == false) {
					path.append(File.separator);
				}
			}
		}

		if (fileName != null && fileName.equals("") == false) {
			path.append(fileName);
		}

		return path.toString();
	}
}
