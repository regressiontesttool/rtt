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
import rtt.core.loader.fetching.FileFetchingStrategy;

public abstract class DataManager<T> {

	protected T data;
	protected ArchiveLoader loader;
	protected FileFetchingStrategy strategy;

	public DataManager(ArchiveLoader loader, FileFetchingStrategy strategy) {
		this.loader = loader;
		setFetchingStrategy(strategy);

		data = getEmptyData();
	}

	public DataManager(ArchiveLoader loader) {
		this(loader, null);
	}

	protected void setFetchingStrategy(FileFetchingStrategy strategy) {
		this.strategy = strategy;
	}

	protected InputStream getInputStream(String fileName, String folderName) {
		return loader.getInputStream(fileName, folderName);
	}

	protected OutputStream getOutputStream(String fileName, String folderName) {
		return loader.getOutputStream(fileName, folderName);
	}

	@SuppressWarnings("unchecked")
	protected T unmarshall(Class<T> clazz) throws Exception {
		return (T) unmarshall(clazz,
				getInputStream(strategy.getFileName(), strategy.getFolders()));
	}

	protected Object unmarshall(Class<?> clazz, InputStream inputStream)
			throws Exception {
		JAXBContext jc = JAXBContext.newInstance(clazz);
		Unmarshaller unmarshaller = jc.createUnmarshaller();
		Source source = new StreamSource(inputStream);
		JAXBElement<?> element = unmarshaller.unmarshal(source, clazz);
		return element.getValue();
	}

	protected void marshall(Class<T> clazz, T data) {
		marshall(clazz, data,
				getOutputStream(strategy.getFileName(), strategy.getFolders()));
	}

	protected void marshall(Class<?> clazz, Object data,
			OutputStream outputStream) {
		marshall(clazz, data, outputStream, null);
	}

	protected void marshall(Class<?> clazz, Object data,
			OutputStream outputStream, String xsltPath) {
		if (outputStream != null) {
			try {
				JAXBContext jc = JAXBContext.newInstance(clazz);

				Marshaller marshaller = jc.createMarshaller();
				marshaller.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, true);
				if (xsltPath != null && !xsltPath.equals("")) {
					marshaller.setProperty(Marshaller.JAXB_FRAGMENT, true);
					outputStream
							.write("<?xml version='1.0' encoding='UTF-8' standalone='yes'?>"
									.getBytes());
					outputStream
							.write(("<?xml-stylesheet type='text/xsl' href='"
									+ xsltPath + "' ?>").getBytes());
				}

				Result result = new StreamResult(outputStream);

				marshaller.marshal(data, result);

				outputStream.close();
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	}

	public T getData() {
		return data;
	}

	public final void load() throws RTTException {
		try {
			data = doLoad();
		} catch (Exception e) {
			throw new RTTException(Type.ARCHIVE, "Could not load data for '"
					+ this.getClass() + "'");
		}
	}

	public final void save() throws RTTException {
		try {
			doSave(data);
		} catch (Exception e) {
			throw new RTTException(Type.ARCHIVE, "Could not load data for '"
					+ this.getClass() + "'");
		}
	}

	protected abstract T getEmptyData();

	protected abstract T doLoad() throws Exception;

	protected abstract void doSave(T data) throws Exception;
}
