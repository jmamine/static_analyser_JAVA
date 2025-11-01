package com.example.ASTDemo;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.eclipse.jdt.core.dom.AST;
import org.eclipse.jdt.core.dom.ASTParser;
import org.eclipse.jdt.core.dom.ASTVisitor;
import org.eclipse.jdt.core.dom.CompilationUnit;
import org.eclipse.jdt.core.dom.FieldDeclaration;
import org.eclipse.jdt.core.dom.MethodDeclaration;
import org.eclipse.jdt.core.dom.TypeDeclaration;
import org.eclipse.jdt.core.dom.VariableDeclarationFragment;

public class CodeAnalyzer {
    private List<ClassInfo> classes;
    private List<MethodInfo> allMethods;
    private Map<String, String> sourceCodeMap;
    private int totalLinesOfCode;

    public CodeAnalyzer() {
        this.classes = new ArrayList<>();
        this.allMethods = new ArrayList<>();
        this.sourceCodeMap = new HashMap<>();
        this.totalLinesOfCode = 0;
    }

    public void analyzeCode(String sourceCode, String fileName) {
        sourceCodeMap.put(fileName, sourceCode);
        
        ASTParser parser = ASTParser.newParser(AST.JLS11);
        parser.setSource(sourceCode.toCharArray());
        parser.setKind(ASTParser.K_COMPILATION_UNIT);
        parser.setResolveBindings(false);

        CompilationUnit cu = (CompilationUnit) parser.createAST(null);
        
        String packageName = "";
        if (cu.getPackage() != null) {
            packageName = cu.getPackage().getName().toString();
        }

        CodeVisitor visitor = new CodeVisitor(packageName, sourceCode);
        cu.accept(visitor);
        
        classes.addAll(visitor.getClasses());
        allMethods.addAll(visitor.getAllMethods());
        totalLinesOfCode += countLines(sourceCode);
    }

    public void analyzeFile(File file) throws IOException {
        StringBuilder sourceCode = new StringBuilder();
        try (BufferedReader reader = new BufferedReader(new FileReader(file))) {
            String line;
            while ((line = reader.readLine()) != null) {
                sourceCode.append(line).append("\n");
            }
        }
        analyzeCode(sourceCode.toString(), file.getName());
    }

    public void analyzeDirectory(File directory) throws IOException {
        if (!directory.isDirectory()) {
            throw new IllegalArgumentException("Not a directory: " + directory);
        }

        File[] files = directory.listFiles();
        if (files != null) {
            for (File file : files) {
                if (file.isDirectory()) {
                    analyzeDirectory(file);
                } else if (file.getName().endsWith(".java")) {
                    analyzeFile(file);
                }
            }
        }
    }

    private int countLines(String code) {
        return code.split("\n").length;
    }

    public List<ClassInfo> getClasses() {
        return classes;
    }

    public List<MethodInfo> getAllMethods() {
        return allMethods;
    }

    public int getTotalLinesOfCode() {
        return totalLinesOfCode;
    }

    private class CodeVisitor extends ASTVisitor {
        private String packageName;
        private List<ClassInfo> classList;
        private List<MethodInfo> methodList;
        private ClassInfo currentClass;

        public CodeVisitor(String packageName, String sourceCode) {
            this.packageName = packageName;
            this.classList = new ArrayList<>();
            this.methodList = new ArrayList<>();
        }

        @Override
        public boolean visit(TypeDeclaration node) {
            String className = node.getName().toString();
            currentClass = new ClassInfo(className, packageName);
            
            CompilationUnit cu = (CompilationUnit) node.getRoot();
            int startLine = cu.getLineNumber(node.getStartPosition());
            int endLine = cu.getLineNumber(node.getStartPosition() + node.getLength() - 1);
            currentClass.setStartLine(startLine);
            currentClass.setEndLine(endLine);
            currentClass.setLineCount(endLine - startLine + 1);
            
            classList.add(currentClass);
            return true;
        }

        @Override
        public void endVisit(TypeDeclaration node) {
            currentClass = null;
        }

        @Override
        public boolean visit(MethodDeclaration node) {
            if (currentClass == null) {
                return true;
            }

            String methodName = node.getName().toString();
            MethodInfo methodInfo = new MethodInfo(methodName, currentClass.getName());
            
            int parameterCount = node.parameters().size();
            methodInfo.setParameterCount(parameterCount);

            CompilationUnit cu = (CompilationUnit) node.getRoot();
            int startLine = cu.getLineNumber(node.getStartPosition());
            int endLine = cu.getLineNumber(node.getStartPosition() + node.getLength() - 1);
            methodInfo.setStartLine(startLine);
            methodInfo.setEndLine(endLine);
            
            int methodLineCount = endLine - startLine + 1;
            methodInfo.setLineCount(methodLineCount);

            currentClass.addMethod(methodInfo);
            methodList.add(methodInfo);
            
            return true;
        }

        @Override
        public boolean visit(FieldDeclaration node) {
            if (currentClass == null) {
                return true;
            }

            for (Object obj : node.fragments()) {
                if (obj instanceof VariableDeclarationFragment) {
                    VariableDeclarationFragment fragment = (VariableDeclarationFragment) obj;
                    String attributeName = fragment.getName().toString();
                    currentClass.addAttribute(attributeName);
                }
            }
            
            return true;
        }

        public List<ClassInfo> getClasses() {
            return classList;
        }

        public List<MethodInfo> getAllMethods() {
            return methodList;
        }
    }
}

