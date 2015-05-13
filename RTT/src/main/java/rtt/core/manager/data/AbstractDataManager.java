package rtt.core.manager.data;

import java.io.InputStream;
import java.io.OutputStream;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBElement;
import javax.xml.bind.Marshaller;
import javax.xml.bind.Unmarshaller;
import javax.xml.transform.Result;
import javax.xml.transform.Source;
import javax.xml.transform.stream.StreamResult;
import javax.xml.transform.stream.StreamSource;

import rtt.core.exceptions.RTTException;
import rtt.core.exceptions.RTTException.Type;
import rtt.core.loader.ArchiveLoader;
import rtt.core.loader.fetching.IFileFetching;
import rtt.core.utils.RTTLogging;

/**
 * This abstract data manager provides a basic set of operations for loading and
 * storing informations in the rtt archive.
 * 
 * @author Christian Oelsner <C.Oelsner@gmail.com>
 * 
 * @param <T>
 *            The main type of data used within this data manager.
 */
public abstract class AbstractDataManager<T> {

	/**
	 * The current data of the manager.
	 */
	protected T data;

	/**
	 * The current {@link ArchiveLoader} of the manager.
	 */
	protected ArchiveLoader loader;

	/**
	 * The current {@link IFileFetching} of the manager
	 */
	protected IFileFetching strategy;

	/**
	 * Creates a data manager with an archive loader and a strategy for loading
	 * a file, which contains all data of this manager.
	 * 
	 * @param loader
	 *            the {@link ArchiveLoader}
	 * @param strategy
	 *            the {@link IFileFetching}
	 * @see AbstractDataManager
	 * @see ArchiveLoader
	 * @see IFileFetching
	 */
	public AbstractDataManager(ArchiveLoader loader,
			IFileFetching strategy) {
		this.loader = loader;
		setFetchingStrategy(strategy);

		data = getEmptyData();
	}

	/**
	 * Creates a data manager without a {@link IFileFetching}. This
	 * strategy must be set before using this manager.
	 * 
	 * @param loader
	 *            the {@link ArchiveLoader}
	 * @see AbstractDataManager
	 * @see ArchiveLoader
	 * @see IFileFetching
	 * @see #setFetchingStrategy(IFileFetching)
	 */
	public AbstractDataManager(ArchiveLoader loader) {
		this(loader, null);
	}

	/**
	 * Sets the strategy for loading a file, which contains all data of this
	 * manager.
	 * 
	 * @param strategy
	 *            the {@link IFileFetching}
	 * @see AbstractDataManager
	 * @see IFileFetching
	 */
	protected void setFetchingStrategy(IFileFetching strategy) {
		this.strategy = strategy;
	}
	
	protected InputStream getInputStream() {
		InputStream result = null;
		if (strategy != null) {
			result = loader.getInputStream(strategy.getFileName(), strategy.getFolders());
		}
		
		return result;
	}
	
	protected OutputStream getOutputStream() {
		OutputStream result = null;
		if (strategy != null) {
			result = loader.getOutputStream(strategy.getFileName(), strategy.getFolders());
		}
		
		return result;
	}

	/**
	 * Returns the data of the file given by the current
	 * {@link IFileFetching}. The conversion of the data will be made
	 * through JAXB.
	 * 
	 * @param clazz
	 *            the class of the data
	 * @return a object containing the data
	 * @throws Exception
	 *             thrown, if any errors occur during loading
	 * @see AbstractDataManager
	 * @see IFileFetching
	 * @see #unmarshall(Class, InputStream)
	 * @see Unmarshaller#unmarshal(Source, Class)
	 */
	@SuppressWarnings("unchecked")
	protected T unmarshall(Class<T> clazz) throws Exception {
		return (T) unmarshall(clazz, getInputStream());
	}

	/**
	 * Returns the data of the given {@link InputStream}. The conversion of the
	 * data will be made through JAXB.
	 * 
	 * @param clazz
	 *            the class of the data
	 * @param inputStream
	 *            the {@link InputStream} of the data
	 * @return a object containing the data
	 * @throws Exception
	 *             thrown, if any errors occur during loading
	 * @see AbstractDataManager
	 * @see InputStream
	 * @see Unmarshaller#unmarshal(Source, Class)
	 */
	protected Object unmarshall(Class<?> clazz, InputStream inputStream)
			throws Exception {
		// create new context and an unmarshaller
		JAXBContext jc = JAXBContext.newInstance(clazz);
		Unmarshaller unmarshaller = jc.createUnmarshaller();

		// unmarshall data from stream source
		Source source = new StreamSource(inputStream);
		JAXBElement<?> element = unmarshaller.unmarshal(source, clazz);

		// return value of jaxb element
		return element.getValue();
	}

	/**
	 * Saves the given data to the file given by the current
	 * {@link IFileFetching}. The conversion of the data will be made
	 * through JAXB.
	 * 
	 * @param clazz
	 *            the class of the data
	 * @param data
	 *            the data, which should be saved
	 * @see AbstractDataManager
	 * @see IFileFetching
	 * @see #marshall(Class, Object, OutputStream)
	 * @see Marshaller#marshal(Object, Result)
	 */
	protected void marshall(Class<T> clazz, T data) {
		marshall(clazz, data, getOutputStream());
	}

	/**
	 * Saves the given data to the given {@link OutputStream}.
	 * 
	 * @param clazz
	 *            the class of the data
	 * @param data
	 *            the data, which should be saved
	 * @param outputStream
	 *            the {@link OutputStream}
	 * @see AbstractDataManager
	 * @see OutputStream
	 * @see Marshaller#marshal(Object, Result)
	 */
	protected void marshall(Class<?> clazz, Object data,
			OutputStream outputStream) {
		if (outputStream != null) {
			try {
				// create new context and marshaller
				JAXBContext jc = JAXBContext.newInstance(clazz);

				Marshaller marshaller = jc.createMarshaller();
				marshaller.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, true);
				
				// TODO add CDATA support for values
//				XMLOutputFactory xof = XMLOutputFactory.newInstance();
//				XMLStreamWriter streamWriter = xof.createXMLStreamWriter(outputStream, "UTF-8");				

				// create stream result and marshall data
				Result result = new StreamResult(outputStream);
				marshaller.marshal(data, result);

				outputStream.close();
			} catch (Exception e) {
				RTTLogging.error("Could not marshall output data", e);
			}
		}
	}
	
	/**
	 * Gets data from manager. This function will return empty data, but not
	 * {@code null}, if called before {@link #load()}.
	 * 
	 * @return the data of the manager
	 * @see AbstractDataManager
	 * @see #load()
	 */
	public T getData() {
		return data;
	}

	/**
	 * Loads all data into the manager.
	 * 
	 * @throws RTTException
	 *             thrown, if any error occur during loading
	 * @see AbstractDataManager
	 */
	public final void load() throws RTTException {
		try {
			data = doLoad();
		} catch (Exception e) {
			throw new RTTException(Type.DATA_NOT_FOUND, "Could not load data for '"
					+ this.getClass() + "'");
		}
	}

	/**
	 * Saves all data from the manager.
	 * 
	 * @throws RTTException
	 *             thrown, if any error occur during saving
	 * @see AbstractDataManager
	 */
	public final void save() throws RTTException {
		try {
			doSave(data);
		} catch (Exception e) {
			throw new RTTException(Type.DATA_NOT_FOUND, "Could not load data for '"
					+ this.getClass() + "'");
		}
	}

	/**
	 * Returns an empty data object. 
	 * @return an empty data object.
	 */
	protected abstract T getEmptyData();

	/**
	 * Loads the data from archive.
	 * 
	 * @return the loaded data
	 * @throws Exception
	 *             thrown, if any error occur during loading
	 */
	protected abstract T doLoad() throws Exception;

	/**
	 * Saves the data to the archive.
	 * 
	 * @param data
	 *            the data, which should be saved
	 * @throws Exception
	 *             thrown, if any error occur during saving
	 */
	protected abstract void doSave(T data) throws Exception;
}
