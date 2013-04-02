package br.ufba.dcc.disciplinas.mate08.resolver;

import java.io.File;
import java.io.FileFilter;


public class PropertyFileFilter implements FileFilter {

    @Override
    public boolean accept(File pathname) {

        if (pathname == null) {
            return false;
        }

        boolean isDirectory = pathname.isDirectory();

        boolean isWebInfResource = pathname.getAbsolutePath().contains("classes" + System.getProperty("file.separator"));

        if (isDirectory || !isWebInfResource) {
            return false;
        }

        String extension = getExtension(pathname.getName());

        if (extension.equals("properties")) {
            return true;
        } else {
            return false;
        }
    }


    protected String getExtension(String filename) {
        if (filename == null) {
            return "";
        }

        int lastDotIdx = filename.lastIndexOf(".");

        if (lastDotIdx == -1) {
            return "";
        }

        return filename.substring(lastDotIdx + 1);
    }
}