package br.ufba.dcc.disciplinas.mate08.resolver;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileFilter;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URISyntaxException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Properties;

import javax.annotation.PostConstruct;
import javax.faces.context.FacesContext;
import javax.inject.Singleton;

@Singleton
public class PropertyResolver {

	private Map<String, Object> properties = new HashMap<>();
	
	
	public String getValue(String key) {
        Object value = properties.get(key);

        return (value != null) ? String.valueOf(value) : null;
    }
	
	private List<File> getPropertyFiles(ClassLoader classLoader) throws IOException {
        List<File> result = new ArrayList<>();

        InputStream inputStream = classLoader.getResourceAsStream("/");
        BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(inputStream));
        String line = "";
        
        FileFilter filter =  new PropertyFileFilter();
        
        while ((line = bufferedReader.readLine()) != null) {
        	File resource = getFileFromURL(classLoader.getResource(line));
        	if (resource.isDirectory()) {
        		File[] files = resource.listFiles(filter);
        		result.addAll(Arrays.asList(files));
        	} else if (resource.isFile() && resource.canRead() && filter.accept(resource)) {
        		result.add(resource);
        	}
        }
        
        return result;
    }
	
	private File getFileFromURL(URL url) {
        File result;

        try {
            result = new File(url.toURI());
        } catch (URISyntaxException e) {
            result = new File(url.getPath());
        }

        return result;
    }

	
	@PostConstruct
	public void init() throws IOException {
		//ClassLoader classLoader = Thread.currentThread().getContextClassLoader();
		ClassLoader classLoader = getClass().getClassLoader();
		
		List<File> propertyFiles = getPropertyFiles(classLoader);
		
		for (File file : propertyFiles) {
			Properties p = new Properties();
			p.load(new FileInputStream(file));
			
			// TODO: If required - notify if added key was already present in
			// the map
			properties.putAll(new HashMap<String, Object>((Map) p));
		}
	}

}
