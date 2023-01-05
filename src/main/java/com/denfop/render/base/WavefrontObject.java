package com.denfop.render.base;

import com.denfop.api.render.IModelCustom;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.vertex.DefaultVertexFormats;
import net.minecraft.client.resources.IResource;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.Vec3d;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class WavefrontObject implements IModelCustom {

    private static final Pattern vertexPattern = Pattern.compile(
            "(v( (\\-) {0,1}\\d+\\.\\d+){3,4} *\\n)|(v( (\\-){0,1}\\d+\\.\\d+){3,4} *$)");
    private static final Pattern vertexNormalPattern = Pattern.compile(
            "(vn( (\\-){0,1}\\d+\\.\\d+){3,4} *\\n)|(vn( (\\-){0,1}\\d+\\.\\d+){3,4} *$)");
    private static final Pattern textureCoordinatePattern = Pattern.compile(
            "(vt( (\\-){0,1}\\d+\\.\\d+){2,3} *\\n)|(vt( (\\-){0,1}\\d+\\.\\d+){2,3} *$)");
    private static final Pattern face_V_VT_VN_Pattern = Pattern.compile(
            "(f( \\d+/\\d+/\\d+){3,4} *\\n)|(f( \\d+/\\d+/\\d+){3,4} *$)");
    private static final Pattern face_V_VT_Pattern = Pattern.compile("(f( \\d+/\\d+){3,4} *\\n)|(f( \\d+/\\d+){3,4} *$)");
    private static final Pattern face_V_VN_Pattern = Pattern.compile("(f( \\d+//\\d+){3,4} *\\n)|(f( \\d+//\\d+){3,4} *$)");
    private static final Pattern face_V_Pattern = Pattern.compile("(f( \\d+){3,4} *\\n)|(f( \\d+){3,4} *$)");
    private static final Pattern groupObjectPattern = Pattern.compile("([go]( [\\w\\d\\.]+) *\\n)|([go]( [\\w\\d\\.]+) *$)");
    private static Matcher vertexMatcher;
    private static Matcher vertexNormalMatcher;
    private static Matcher textureCoordinateMatcher;
    private static Matcher face_V_VT_VN_Matcher;
    private static Matcher face_V_VT_Matcher;
    private static Matcher face_V_VN_Matcher;
    private static Matcher face_V_Matcher;
    private static Matcher groupObjectMatcher;
    private final String fileName;
    public ArrayList<Vertex> vertices = new ArrayList<>();
    public ArrayList<Vertex> vertexNormals = new ArrayList<>();
    public ArrayList<WavefrontObject.TextureCoordinate> textureCoordinates = new ArrayList<>();
    public ArrayList<WavefrontObject.GroupObject> groupObjects = new ArrayList<>();
    private WavefrontObject.GroupObject currentGroupObject;

    public WavefrontObject(ResourceLocation resource) throws WavefrontObject.ModelFormatException {
        this.fileName = resource.toString();

        try {
            IResource res = Minecraft.getMinecraft().getResourceManager().getResource(resource);
            this.loadObjModel(res.getInputStream());
        } catch (IOException var3) {
            throw new ModelFormatException("IO Exception reading model format", var3);
        }
    }

    private static boolean isValidFaceLine(String line) {
        return isValidFace_V_VT_VN_Line(line) || isValidFace_V_VT_Line(line) || isValidFace_V_VN_Line(line) || isValidFace_V_Line(
                line);
    }

    private static boolean isValidFace_V_VN_Line(String line) {
        if (face_V_VN_Matcher != null) {
            face_V_VN_Matcher.reset();
        }

        face_V_VN_Matcher = face_V_VN_Pattern.matcher(line);
        return face_V_VN_Matcher.matches();
    }

    private static boolean isValidFace_V_Line(String line) {
        if (face_V_Matcher != null) {
            face_V_Matcher.reset();
        }

        face_V_Matcher = face_V_Pattern.matcher(line);
        return face_V_Matcher.matches();
    }

    private static boolean isValidFace_V_VT_Line(String line) {
        if (face_V_VT_Matcher != null) {
            face_V_VT_Matcher.reset();
        }

        face_V_VT_Matcher = face_V_VT_Pattern.matcher(line);
        return face_V_VT_Matcher.matches();
    }

    private static boolean isValidGroupObjectLine(String line) {
        if (groupObjectMatcher != null) {
            groupObjectMatcher.reset();
        }

        groupObjectMatcher = groupObjectPattern.matcher(line);
        return groupObjectMatcher.matches();
    }

    private static boolean isValidVertexLine(String line) {
        if (vertexMatcher != null) {
            vertexMatcher.reset();
        }

        vertexMatcher = vertexPattern.matcher(line);
        return vertexMatcher.matches();
    }

    private static boolean isValidVertexNormalLine(String line) {
        if (vertexNormalMatcher != null) {
            vertexNormalMatcher.reset();
        }

        vertexNormalMatcher = vertexNormalPattern.matcher(line);
        return vertexNormalMatcher.matches();
    }

    private static boolean isValidFace_V_VT_VN_Line(String line) {
        if (face_V_VT_VN_Matcher != null) {
            face_V_VT_VN_Matcher.reset();
        }

        face_V_VT_VN_Matcher = face_V_VT_VN_Pattern.matcher(line);
        return face_V_VT_VN_Matcher.matches();
    }

    private static boolean isValidTextureCoordinateLine(String line) {
        if (textureCoordinateMatcher != null) {
            textureCoordinateMatcher.reset();
        }

        textureCoordinateMatcher = textureCoordinatePattern.matcher(line);
        return textureCoordinateMatcher.matches();
    }

    private void loadObjModel(InputStream inputStream) throws WavefrontObject.ModelFormatException {
        BufferedReader reader = null;
        String currentLine;
        int lineCount = 0;

        try {
            reader = new BufferedReader(new InputStreamReader(inputStream));

            while ((currentLine = reader.readLine()) != null) {
                ++lineCount;
                currentLine = currentLine.replaceAll("\\s+", " ").trim();
                if (!currentLine.startsWith("#") && currentLine.length() != 0) {
                    Vertex vertex;
                    if (currentLine.startsWith("v ")) {
                        vertex = this.parseVertex(currentLine, lineCount);
                        if (vertex != null) {
                            this.vertices.add(vertex);
                        }
                    } else if (currentLine.startsWith("vn ")) {
                        vertex = this.parseVertexNormal(currentLine, lineCount);
                        if (vertex != null) {
                            this.vertexNormals.add(vertex);
                        }
                    } else if (currentLine.startsWith("vt ")) {
                        WavefrontObject.TextureCoordinate textureCoordinate = this.parseTextureCoordinate(currentLine, lineCount);
                        if (textureCoordinate != null) {
                            this.textureCoordinates.add(textureCoordinate);
                        }
                    } else if (currentLine.startsWith("f ")) {
                        if (this.currentGroupObject == null) {
                            this.currentGroupObject = new GroupObject("Default");
                        }

                        WavefrontObject.Face face = this.parseFace(currentLine, lineCount);
                        this.currentGroupObject.faces.add(face);
                    } else if (currentLine.startsWith("g ") | currentLine.startsWith("o ")) {
                        WavefrontObject.GroupObject group = this.parseGroupObject(currentLine, lineCount);
                        if (group != null && this.currentGroupObject != null) {
                            this.groupObjects.add(this.currentGroupObject);
                        }

                        this.currentGroupObject = group;
                    }
                }
            }

            this.groupObjects.add(this.currentGroupObject);
        } catch (IOException var16) {
            throw new ModelFormatException("IO Exception reading model format", var16);
        } finally {
            try {
                reader.close();
            } catch (IOException ignored) {
            }

            try {
                inputStream.close();
            } catch (IOException ignored) {
            }

        }
    }

    private WavefrontObject.Face parseFace(String line, int lineCount) {
        Face face;
        if (isValidFaceLine(line)) {
            face = new Face();
            String trimmedLine = line.substring(line.indexOf(" ") + 1);
            String[] tokens = trimmedLine.split(" ");
            String[] subTokens;
            if (tokens.length == 3) {
                this.currentGroupObject.glDrawingMode = 4;
            } else if (tokens.length == 4) {
                this.currentGroupObject.glDrawingMode = 7;
            }

            int i;
            if (isValidFace_V_VT_VN_Line(line)) {
                face.vertices = new Vertex[tokens.length];
                face.textureCoordinates = new TextureCoordinate[tokens.length];
                face.vertexNormals = new Vertex[tokens.length];

                for (i = 0; i < tokens.length; ++i) {
                    subTokens = tokens[i].split("/");
                    face.vertices[i] = this.vertices.get(Integer.parseInt(subTokens[0]) - 1);
                    face.textureCoordinates[i] = this.textureCoordinates.get(Integer.parseInt(subTokens[1]) - 1);
                    face.vertexNormals[i] = this.vertexNormals.get(Integer.parseInt(subTokens[2]) - 1);
                }

                face.faceNormal = face.calculateFaceNormal();
            } else if (isValidFace_V_VT_Line(line)) {
                face.vertices = new Vertex[tokens.length];
                face.textureCoordinates = new TextureCoordinate[tokens.length];

                for (i = 0; i < tokens.length; ++i) {
                    subTokens = tokens[i].split("/");
                    face.vertices[i] = this.vertices.get(Integer.parseInt(subTokens[0]) - 1);
                    face.textureCoordinates[i] = this.textureCoordinates.get(Integer.parseInt(subTokens[1]) - 1);
                }

                face.faceNormal = face.calculateFaceNormal();
            } else if (isValidFace_V_VN_Line(line)) {
                face.vertices = new Vertex[tokens.length];
                face.vertexNormals = new Vertex[tokens.length];

                for (i = 0; i < tokens.length; ++i) {
                    subTokens = tokens[i].split("//");
                    face.vertices[i] = this.vertices.get(Integer.parseInt(subTokens[0]) - 1);
                    face.vertexNormals[i] = this.vertexNormals.get(Integer.parseInt(subTokens[1]) - 1);
                }

                face.faceNormal = face.calculateFaceNormal();
            } else {
                if (!isValidFace_V_Line(line)) {
                    throw new ModelFormatException("Error parsing entry ('" + line + "', line " + lineCount + ") in file '" + this.fileName + "' - Incorrect format");
                }

                face.vertices = new Vertex[tokens.length];

                for (i = 0; i < tokens.length; ++i) {
                    face.vertices[i] = this.vertices.get(Integer.parseInt(tokens[i]) - 1);
                }

                face.faceNormal = face.calculateFaceNormal();
            }

            return face;
        } else {
            throw new ModelFormatException("Error parsing entry ('" + line + "', line " + lineCount + ") in file '" + this.fileName + "' - Incorrect format");
        }
    }

    private GroupObject parseGroupObject(String line, int lineCount) throws
            thaumcraft.client.lib.obj.WavefrontObject.ModelFormatException {
        GroupObject group = null;
        if (isValidGroupObjectLine(line)) {
            String trimmedLine = line.substring(line.indexOf(" ") + 1);
            if (trimmedLine.length() > 0) {
                group = new GroupObject(trimmedLine);
            }

            return group;
        } else {
            throw new ModelFormatException("Error parsing entry ('" + line + "', line " + lineCount + ") in file '" + this.fileName + "' - Incorrect format");
        }
    }

    @SideOnly(Side.CLIENT)
    public void renderAll() {
        Tessellator tessellator = Tessellator.getInstance();
        if (this.currentGroupObject != null) {
            tessellator.getBuffer().begin(this.currentGroupObject.glDrawingMode, DefaultVertexFormats.POSITION_TEX_NORMAL);
        } else {
            tessellator.getBuffer().begin(4, DefaultVertexFormats.POSITION_TEX_NORMAL);
        }

        this.tessellateAll(tessellator);
        tessellator.draw();
    }

    @SideOnly(Side.CLIENT)
    public void tessellateAll(Tessellator tessellator) {

        for (final GroupObject groupObject : this.groupObjects) {
            groupObject.render(tessellator);
        }

    }

    @SideOnly(Side.CLIENT)
    public void renderOnly(String... groupNames) {

        for (final GroupObject groupObject : this.groupObjects) {

            for (String groupName : groupNames) {
                if (groupName.equalsIgnoreCase(groupObject.name)) {
                    groupObject.render();
                }
            }
        }

    }

    @SideOnly(Side.CLIENT)
    public String[] getPartNames() {
        ArrayList<String> l = new ArrayList<>();

        for (final GroupObject groupObject : this.groupObjects) {
            l.add(groupObject.name);
        }

        return l.toArray(new String[0]);
    }

    @SideOnly(Side.CLIENT)
    public void renderPart(String partName) {

        for (final GroupObject groupObject : this.groupObjects) {
            if (partName.equalsIgnoreCase(groupObject.name)) {
                groupObject.render();
            }
        }

    }

    @SideOnly(Side.CLIENT)
    public void renderAllExcept(String... excludedGroupNames) {

        for (final GroupObject groupObject : this.groupObjects) {
            boolean skipPart = false;

            for (String excludedGroupName : excludedGroupNames) {
                if (excludedGroupName.equalsIgnoreCase(groupObject.name)) {
                    skipPart = true;
                    break;
                }
            }

            if (!skipPart) {
                groupObject.render();
            }
        }

    }

    private Vertex parseVertex(String line, int lineCount) throws WavefrontObject.ModelFormatException {
        if (isValidVertexLine(line)) {
            line = line.substring(line.indexOf(" ") + 1);
            String[] tokens = line.split(" ");

            try {
                if (tokens.length == 2) {
                    return new Vertex(Float.parseFloat(tokens[0]), Float.parseFloat(tokens[1]));
                } else {
                    return tokens.length == 3 ? new Vertex(
                            Float.parseFloat(tokens[0]),
                            Float.parseFloat(tokens[1]),
                            Float.parseFloat(tokens[2])
                    ) : null;
                }
            } catch (NumberFormatException var6) {
                throw new ModelFormatException(
                        String.format("Number formatting error at line %d", lineCount),
                        var6
                );
            }
        } else {
            throw new ModelFormatException("Error parsing entry ('" + line + "', line " + lineCount + ") in file '" + this.fileName + "' - Incorrect format");
        }
    }

    private Vertex parseVertexNormal(String line, int lineCount) throws WavefrontObject.ModelFormatException {
        if (isValidVertexNormalLine(line)) {
            line = line.substring(line.indexOf(" ") + 1);
            String[] tokens = line.split(" ");

            try {
                return tokens.length == 3 ? new Vertex(
                        Float.parseFloat(tokens[0]),
                        Float.parseFloat(tokens[1]),
                        Float.parseFloat(tokens[2])
                ) : null;
            } catch (NumberFormatException var6) {
                throw new ModelFormatException(
                        String.format("Number formatting error at line %d", lineCount),
                        var6
                );
            }
        } else {
            throw new ModelFormatException("Error parsing entry ('" + line + "', line " + lineCount + ") in file '" + this.fileName + "' - Incorrect format");
        }
    }

    private WavefrontObject.TextureCoordinate parseTextureCoordinate(String line, int lineCount) throws
            WavefrontObject.ModelFormatException {
        if (isValidTextureCoordinateLine(line)) {
            line = line.substring(line.indexOf(" ") + 1);
            String[] tokens = line.split(" ");

            try {
                if (tokens.length == 2) {
                    return new TextureCoordinate(Float.parseFloat(tokens[0]), 1.0F - Float.parseFloat(tokens[1]));
                } else {
                    return tokens.length == 3
                            ? new TextureCoordinate(
                            Float.parseFloat(tokens[0]),
                            1.0F - Float.parseFloat(tokens[1]),
                            Float.parseFloat(tokens[2])
                    )
                            : null;
                }
            } catch (NumberFormatException var6) {
                throw new ModelFormatException(
                        String.format("Number formatting error at line %d", lineCount),
                        var6
                );
            }
        } else {
            throw new ModelFormatException("Error parsing entry ('" + line + "', line " + lineCount + ") in file '" + this.fileName + "' - Incorrect format");
        }
    }

    public String getType() {
        return "obj";
    }

    public static class ModelFormatException extends RuntimeException {

        private static final long serialVersionUID = 2023547503969671835L;

        public ModelFormatException(String message, Throwable cause) {
            super(message, cause);
        }

        public ModelFormatException(String message) {
            super(message);
        }

    }

    public static class GroupObject {

        public String name;
        public ArrayList<WavefrontObject.Face> faces;
        public int glDrawingMode;

        public GroupObject(String name) {
            this(name, -1);
        }

        public GroupObject(String name, int glDrawingMode) {
            this.faces = new ArrayList<>();
            this.name = name;
            this.glDrawingMode = glDrawingMode;
        }

        @SideOnly(Side.CLIENT)
        public void render() {
            if (this.faces.size() > 0) {
                Tessellator tessellator = Tessellator.getInstance();
                tessellator.getBuffer().begin(this.glDrawingMode, DefaultVertexFormats.POSITION_TEX_NORMAL);
                this.render(tessellator);
                tessellator.draw();
            }

        }

        @SideOnly(Side.CLIENT)
        public void render(Tessellator tessellator) {
            if (this.faces.size() > 0) {

                for (final Face face : this.faces) {
                    face.addFaceForRender(tessellator);
                }
            }

        }

    }

    public static class Face {

        public Vertex[] vertices;
        public Vertex[] vertexNormals;
        public Vertex faceNormal;
        public WavefrontObject.TextureCoordinate[] textureCoordinates;

        public Face() {
        }

        @SideOnly(Side.CLIENT)
        public void addFaceForRender(Tessellator tessellator) {
            this.addFaceForRender(tessellator, 5.0E-4F);
        }

        @SideOnly(Side.CLIENT)
        public void addFaceForRender(Tessellator tessellator, float textureOffset) {
            if (this.faceNormal == null) {
                this.faceNormal = this.calculateFaceNormal();
            }

            float averageU = 0.0F;
            float averageV = 0.0F;
            if (this.textureCoordinates != null && this.textureCoordinates.length > 0) {
                for (final TextureCoordinate textureCoordinate : this.textureCoordinates) {
                    averageU += textureCoordinate.u;
                    averageV += textureCoordinate.v;
                }

                averageU /= (float) this.textureCoordinates.length;
                averageV /= (float) this.textureCoordinates.length;
            }

            for (int i = 0; i < this.vertices.length; ++i) {
                if (this.textureCoordinates != null && this.textureCoordinates.length > 0) {
                    float offsetU = textureOffset;
                    float offsetV = textureOffset;
                    if (this.textureCoordinates[i].u > averageU) {
                        offsetU = -textureOffset;
                    }

                    if (this.textureCoordinates[i].v > averageV) {
                        offsetV = -textureOffset;
                    }

                    tessellator
                            .getBuffer()
                            .pos(
                                    this.vertices[i].x,
                                    this.vertices[i].y,
                                    this.vertices[i].z
                            )
                            .tex(
                                    this.textureCoordinates[i].u + offsetU,
                                    this.textureCoordinates[i].v + offsetV
                            )
                            .normal(this.faceNormal.x, this.faceNormal.y, this.faceNormal.z)
                            .endVertex();
                } else {
                    tessellator.getBuffer().pos(
                            this.vertices[i].x,
                            this.vertices[i].y,
                            this.vertices[i].z
                    ).normal(this.faceNormal.x, this.faceNormal.y, this.faceNormal.z).endVertex();
                }
            }

        }

        public Vertex calculateFaceNormal() {
            Vec3d v1 = new Vec3d(
                    this.vertices[1].x - this.vertices[0].x,
                    this.vertices[1].y - this.vertices[0].y,
                    this.vertices[1].z - this.vertices[0].z
            );
            Vec3d v2 = new Vec3d(
                    this.vertices[2].x - this.vertices[0].x,
                    this.vertices[2].y - this.vertices[0].y,
                    this.vertices[2].z - this.vertices[0].z
            );
            Vec3d normalVector;
            normalVector = v1.crossProduct(v2).normalize();
            return new Vertex((float) normalVector.x, (float) normalVector.y, (float) normalVector.z);
        }

    }

    public static class TextureCoordinate {

        public float u;
        public float v;
        public float w;

        public TextureCoordinate(float u, float v) {
            this(u, v, 0.0F);
        }

        public TextureCoordinate(float u, float v, float w) {
            this.u = u;
            this.v = v;
            this.w = w;
        }

    }

}
