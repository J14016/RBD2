package com.example.j14016_m.framework.gl;

import android.util.Log;

import com.example.j14016_m.framework.impl.GLGame;
import com.example.j14016_m.framework.math.Vector3;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by J14016_M on 2016/01/21.
 */
public class MqoLoader {

    public static Mesh load(GLGame game, String file, boolean hasNormals) {
        InputStream in = null;
        try {
            in = game.getFileIO().readAsset(file);
            List<String> lines = readLines(in);


            Vertex vertices[] = new Vertex[lines.size() * 2];
            int numVertices = 0;

            Face faces[] = new Face[lines.size()];
            int numFaces = 0;

            MqoMaterial materials[] = null;
            int numMaterials = 0;

            for(int i = 0; i < lines.size(); i++) {
                String line = lines.get(i);
                String tokens[] =line.split("[ ]+");

                if(tokens[0].matches("Material")) {
                    int numMtlIndex = Integer.parseInt(tokens[1]);
                    materials = new MqoMaterial[numMtlIndex];
                    for(int index = 0; index < numMtlIndex; index++) {
                        line = lines.get(++i);
                        line = line.replaceAll("[(|)]", " ");
                        tokens = line.split("[ ]+");

                        materials[index] = new MqoMaterial();

                        if(line.indexOf("tex") != -1) {
                            tokens[19] = tokens[19].replaceAll("\"", "");
                            Texture texture = new Texture(game, tokens[19], false);
                            materials[index].setTexture(texture);
                        } else {
                            materials[index].setTexture(null);
                        }
                    }
                    continue;
                }

                if(tokens[0].matches("\tvertex")) {
                    int numVertexIndex = Integer.parseInt(tokens[1]);
                    for(int index = 0; index < numVertexIndex; index++) {
                        line = lines.get(++i);
                        line = line.replaceFirst("\t\t", "");
                        tokens = line.split("[ ]+");
                        vertices[numVertices++] = new Vertex(Float.parseFloat(tokens[0]), Float.parseFloat(tokens[1]), Float.parseFloat(tokens[2]), 0, 0, 0, 0, 0);
                    }
                    continue;
                }

                if(tokens[0].matches("\tface")) {
                    int numFacesIndex = Integer.parseInt(tokens[1]);
                    for(int index = 0; index < numFacesIndex; index++) {
                        line = lines.get(++i);
                        line = line.replaceFirst("\t\t", "");
                        line = line.replaceAll("[(|)]", " ");
                        tokens = line.split("[ ]+");
                        int i0 = Integer.parseInt(tokens[3]);
                        int i1 = Integer.parseInt(tokens[2]);
                        int i2 = Integer.parseInt(tokens[4]);

                        int mtlNum = -1;
                        if(tokens[5].compareTo("M") == 0) {
                            mtlNum = Integer.parseInt(tokens[6]);
                        }

                        float u0, u1, u2, v0, v1, v2;
                        u0 = Float.parseFloat(tokens[10]);
                        v0 = Float.parseFloat(tokens[11]);
                        u1 = Float.parseFloat(tokens[8]);
                        v1 = Float.parseFloat(tokens[9]);
                        u2 = Float.parseFloat(tokens[12]);
                        v2 = Float.parseFloat(tokens[13]);

                        Vector3 norm = Vector3.normal(vertices[i0].p, vertices[i1].p, vertices[i2].p);
                        vertices[i0].u = u0;
                        vertices[i0].v = v0;
                        vertices[i0].norm.add(norm);

                        vertices[i1].u = u1;
                        vertices[i1].v = v1;
                        vertices[i1].norm.add(norm);

                        vertices[i2].u = u2;
                        vertices[i2].v = v2;
                        vertices[i2].norm.add(norm);

                        faces[numFaces++] = new Face(i0, i1, i2, u0, v0, u1, v1, u2, v2, norm);
                    }
                    continue;
                }
            }

            for(int i = 0; i < numVertices; i++) {
                vertices[i].norm.nor();
            }


            for(int i = 0; i < numFaces; i++) {
                Vertex v = vertices[faces[i].i0];
                if(v.u != faces[i].u0 || v.v != faces[i].v0) {
                    faces[i].i0 = numVertices;
                    vertices[numVertices++] = new Vertex(v.p, v.norm, faces[i].u0, faces[i].v0);
                }

                v = vertices[faces[i].i1];
                if(v.u != faces[i].u1 || v.v != faces[i].v1) {
                    faces[i].i1 = numVertices;
                    vertices[numVertices++] = new Vertex(v.p, v.norm, faces[i].u1, faces[i].v1);
                }

                v = vertices[faces[i].i2];
                if(v.u != faces[i].u2 || v.v != faces[i].v2) {
                    faces[i].i2 = numVertices;
                    vertices[numVertices++] = new Vertex(v.p, v.norm, faces[i].u2, faces[i].v2);
                }
            }

            Mesh mesh = new Mesh(vertices, numVertices, faces, numFaces, materials, numMaterials, hasNormals);

            return mesh;

        } catch (Exception ex) {
            throw new RuntimeException("couldn't load '" + file + "'", ex);
        } finally {
            if(in != null) {
                try {
                    in.close();
                } catch (Exception ex) {

                }
            }
        }
    }

    static List<String> readLines(InputStream in) throws IOException {
        List<String> lines = new ArrayList<String>();

        BufferedReader reader = new BufferedReader(new InputStreamReader(in));
        String line = null;
        while((line = reader.readLine()) != null) {
            lines.add(line);
        }
        return lines;
    }



}
