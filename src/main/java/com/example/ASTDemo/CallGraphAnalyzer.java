package com.example.ASTDemo;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.*;

import org.eclipse.jdt.core.dom.AST;
import org.eclipse.jdt.core.dom.ASTParser;
import org.eclipse.jdt.core.dom.ASTVisitor;
import org.eclipse.jdt.core.dom.CompilationUnit;
import org.eclipse.jdt.core.dom.MethodDeclaration;
import org.eclipse.jdt.core.dom.MethodInvocation;
import org.eclipse.jdt.core.dom.TypeDeclaration;

public class CallGraphAnalyzer {
    private Map<String, Set<String>> callGraph; // caller -> set of callees
    private Map<String, String> methodToClass; // method full name -> class name
    
    public CallGraphAnalyzer() {
        this.callGraph = new HashMap<>();
        this.methodToClass = new HashMap<>();
    }
    
    public void analyzeCode(String sourceCode, String fileName) {
        ASTParser parser = ASTParser.newParser(AST.JLS11);
        parser.setSource(sourceCode.toCharArray());
        parser.setKind(ASTParser.K_COMPILATION_UNIT);
        parser.setResolveBindings(false);
        
        CompilationUnit cu = (CompilationUnit) parser.createAST(null);
        
        String packageName = "";
        if (cu.getPackage() != null) {
            packageName = cu.getPackage().getName().toString();
        }
        
        CallGraphVisitor visitor = new CallGraphVisitor(packageName);
        cu.accept(visitor);
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
    
    public Map<String, Set<String>> getCallGraph() {
        return callGraph;
    }
    
    public Map<String, String> getMethodToClass() {
        return methodToClass;
    }
    
    private class CallGraphVisitor extends ASTVisitor {
        private String packageName;
        private String currentClass;
        private String currentMethod;
        private Set<String> currentCallees;
        
        public CallGraphVisitor(String packageName) {
            this.packageName = packageName;
        }
        
        @Override
        public boolean visit(TypeDeclaration node) {
            currentClass = node.getName().toString();
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
            String fullClassName = packageName.isEmpty() ? currentClass : packageName + "." + currentClass;
            currentMethod = fullClassName + "." + methodName;
            currentCallees = new HashSet<>();
            
            // Register this method
            methodToClass.put(currentMethod, fullClassName);
            callGraph.put(currentMethod, currentCallees);
            
            return true;
        }
        
        @Override
        public void endVisit(MethodDeclaration node) {
            currentMethod = null;
            currentCallees = null;
        }
        
        @Override
        public boolean visit(MethodInvocation node) {
            if (currentMethod != null && currentCallees != null) {
                String methodName = node.getName().toString();
                String callee;
                
                // Try to resolve the full method name if possible
                if (node.getExpression() != null) {
                    String expr = node.getExpression().toString();
                    // Check if expression is a qualified name (contains dot)
                    if (expr.contains(".")) {
                        // It's a method call on another object
                        callee = expr + "." + methodName;
                    } else {
                        // It's a method call on a variable - use the method name only
                        // We'll try to match it with methods in the current class
                        String fullClassName = packageName.isEmpty() ? currentClass : packageName + "." + currentClass;
                        callee = fullClassName + "." + methodName;
                        // Also add the simple version
                        currentCallees.add(methodName);
                    }
                } else {
                    // If no expression, assume it's a method in the current class
                    String fullClassName = packageName.isEmpty() ? currentClass : packageName + "." + currentClass;
                    callee = fullClassName + "." + methodName;
                }
                
                currentCallees.add(callee);
                
                // Also add methods from standard library that are commonly called
                // These are just the method name without the full qualification
                if (methodName.equals("println") || methodName.equals("print") || 
                    methodName.equals("length") || methodName.equals("size") ||
                    methodName.equals("add") || methodName.equals("get") ||
                    methodName.equals("put") || methodName.equals("contains")) {
                    currentCallees.add("System." + methodName);
                }
            }
            return true;
        }
    }
}

