package com.example.ASTDemo;

import java.util.ArrayList;
import java.util.List;

public class ClassInfo {
    private String name;
    private String packageName;
    private int lineCount;
    private List<MethodInfo> methods;
    private List<String> attributes;
    private int startLine;
    private int endLine;

    public ClassInfo(String name, String packageName) {
        this.name = name;
        this.packageName = packageName;
        this.methods = new ArrayList<>();
        this.attributes = new ArrayList<>();
    }

    public String getName() {
        return name;
    }

    public String getPackageName() {
        return packageName;
    }

    public int getLineCount() {
        return lineCount;
    }

    public void setLineCount(int lineCount) {
        this.lineCount = lineCount;
    }

    public List<MethodInfo> getMethods() {
        return methods;
    }

    public void addMethod(MethodInfo method) {
        this.methods.add(method);
    }

    public List<String> getAttributes() {
        return attributes;
    }

    public void addAttribute(String attribute) {
        this.attributes.add(attribute);
    }

    public int getMethodCount() {
        return methods.size();
    }

    public int getAttributeCount() {
        return attributes.size();
    }

    public int getStartLine() {
        return startLine;
    }

    public void setStartLine(int startLine) {
        this.startLine = startLine;
    }

    public int getEndLine() {
        return endLine;
    }

    public void setEndLine(int endLine) {
        this.endLine = endLine;
    }

    public String getFullName() {
        return packageName.isEmpty() ? name : packageName + "." + name;
    }
}

