package rtt.ui.utils;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.Collections;

import org.eclipse.core.resources.IFile;
import org.eclipse.core.resources.IProject;
import org.eclipse.core.runtime.IPath;
import org.eclipse.core.runtime.Path;
import org.eclipse.emf.common.command.BasicCommandStack;
import org.eclipse.emf.common.notify.AdapterFactory;
import org.eclipse.emf.common.util.URI;
import org.eclipse.emf.ecore.provider.EcoreItemProviderAdapterFactory;
import org.eclipse.emf.ecore.resource.Resource;
import org.eclipse.emf.ecore.resource.ResourceSet;
import org.eclipse.emf.edit.domain.AdapterFactoryEditingDomain;
import org.eclipse.emf.edit.provider.ComposedAdapterFactory;
import org.eclipse.emf.edit.provider.ReflectiveItemProviderAdapterFactory;
import org.eclipse.emf.edit.provider.resource.ResourceItemProviderAdapterFactory;

import regression.test.provider.TestItemProviderAdapterFactory;
import rtt.core.archive.configuration.Configuration;
import rtt.core.exceptions.RTTException;
import rtt.core.exceptions.RTTException.Type;
import rtt.core.manager.Manager;

public class RttPluginUtil {

	public static Manager getArchiveManager(IFile file, Configuration config)
			throws RTTException {
		if (file != null && file.exists()) {
			File archiveFile = file.getLocation().toFile();
			Manager manager = new Manager(archiveFile, true);
			if (config != null) {
				manager.loadArchive(archiveFile, config.getName());
			} else {
				manager.loadArchive(archiveFile);
			}

			return manager;
		}

		return null;
	}
	
	public static IPath getArchivePath(IProject project) {
		String archivePath = RttPreferenceStore.get(project, RttPreferenceStore.PREF_ARCHIVE_PATH, "");
		if (archivePath != null && !archivePath.equals("")) {
			IPath path = Path.fromPortableString(archivePath);
			return path;
		}
		
		return null;
	}
	
	public static final void loadResource(ResourceSet resourceSet, URI uri, InputStream inputStream) throws RTTException {
		Resource resource = resourceSet.createResource(uri);
		if (resource == null) {
			throw new RTTException(Type.OPERATION_FAILED, "Could not create new resource.");
		}
		
		try {
			resource.load(inputStream, Collections.EMPTY_MAP);
			inputStream.close();
		} catch (IOException e) {
			throw new RTTException(Type.OPERATION_FAILED, e);
		}	
	}
	
	public static final ComposedAdapterFactory createFactory() {
		ComposedAdapterFactory adapterFactory = new ComposedAdapterFactory(
				ComposedAdapterFactory.Descriptor.Registry.INSTANCE);

		adapterFactory.addAdapterFactory(
				new TestItemProviderAdapterFactory());
		
		adapterFactory.addAdapterFactory(
				new ResourceItemProviderAdapterFactory());

		adapterFactory.addAdapterFactory(
				new EcoreItemProviderAdapterFactory());

		adapterFactory.addAdapterFactory(
				new ReflectiveItemProviderAdapterFactory());		 

		return adapterFactory;
	}
	
	public static final AdapterFactoryEditingDomain createEditingDomain(AdapterFactory factory) {
		return new AdapterFactoryEditingDomain(factory, new BasicCommandStack());
	}
	
	public static final AdapterFactoryEditingDomain createEditingDomain(AdapterFactory factory, ResourceSet resourceSet) {
		return new AdapterFactoryEditingDomain(factory, new BasicCommandStack(), resourceSet);
	}
}
