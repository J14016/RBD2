package com.example.j14016_m.framework;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

/**
 * Created by J14016_M on 2016/01/20.
 */
public interface FileIO {
    public InputStream readAsset(String fileName) throws IOException;
    public InputStream readFile(String fileName) throws IOException;
    public OutputStream writeFile(String fileName) throws IOException;
}
