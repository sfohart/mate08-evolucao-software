package br.ufba.dcc.disciplinas.mate08.resolver;

import java.io.File;
import java.io.IOException;
import java.net.URISyntaxException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.PostConstruct;
import javax.inject.Singleton;

@Singleton
public class PropertyResolver {

	private Map<String, Object> properties = new HashMap<>();
	
	
	private List<File> getPropertyFiles(ClassLoader cl) throws IOException {
        List<File> result = new ArrayList<>();

        Enumeration<URL> resources = cl.getResources("");

        while (resources.hasMoreElements()) {
            File resource = getFileFromURL(resources.nextElement());

            File[] files = resource.listFiles(new PropertyFileFilter());
            result.addAll(Arrays.asList(files));
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
	private void init() {
		ClassLoader classLoader = Thread.currentThread().getContextClassLoader();
		
		List<File> fileList = getPropertyFiles(classLoader);
	}

}
