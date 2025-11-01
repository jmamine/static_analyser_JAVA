package com.example.ASTDemo;

import java.io.File;
import java.io.IOException;
import java.util.List;
import java.util.Map;

import junit.framework.TestCase;

/**
 * Tests unitaires pour l'application ASTDemo
 */
public class AppTest extends TestCase {
    
    private CodeAnalyzer analyzer;
    private MetricsCalculator calculator;
    
    @Override
    protected void setUp() throws Exception {
        super.setUp();
        analyzer = new CodeAnalyzer();
    }
    
    /**
     * Test 1: Analyser du code simple depuis une chaîne
     */
    public void testAnalyzeSimpleCode() {
        String code = "public class Test {\n" +
                     "  private int x;\n" +
                     "  public void method() {}\n" +
                     "}";
        
        analyzer.analyzeCode(code, "Test.java");
        
        assertEquals(1, analyzer.getClasses().size());
        assertEquals("Test", analyzer.getClasses().get(0).getName());
        assertEquals(1, analyzer.getClasses().get(0).getMethodCount());
        assertEquals(1, analyzer.getClasses().get(0).getAttributeCount());
    }
    
    /**
     * Test 2: Analyser un fichier de test existant
     */
    public void testAnalyzeTestFile() throws IOException {
        File testFile = new File("src/test/java/com/example/ASTDemo/TestInput1.java");
        assertTrue("Le fichier de test doit exister", testFile.exists());
        
        analyzer.analyzeFile(testFile);
        
        List<ClassInfo> classes = analyzer.getClasses();
        assertTrue("Au moins une classe doit être trouvée", classes.size() > 0);
        
        ClassInfo testClass = classes.get(0);
        assertEquals("TestInput1", testClass.getName());
        assertTrue("La classe doit avoir plusieurs méthodes", testClass.getMethodCount() > 5);
    }
    
    /**
     * Test 3: Calcul des métriques de base
     */
    public void testBasicMetrics() {
        String code = "public class Test {\n" +
                     "  private int a, b;\n" +
                     "  public void m1() {}\n" +
                     "  public void m2() {}\n" +
                     "  public void m3() {}\n" +
                     "}";
        
        analyzer.analyzeCode(code, "Test.java");
        calculator = new MetricsCalculator(analyzer.getClasses(), analyzer.getAllMethods());
        
        assertEquals(1, calculator.getNumberOfClasses());
        assertEquals(3, calculator.getTotalNumberOfMethods());
        assertEquals(2, calculator.getAverageAttributesPerClass(), 0.01);
        assertEquals(3.0, calculator.getAverageMethodsPerClass(), 0.01);
    }
    
    /**
     * Test 4: Test du nombre de packages
     */
    public void testPackageCount() {
        String code1 = "package pkg1; public class A {}";
        String code2 = "package pkg1; public class B {}";
        String code3 = "package pkg2; public class C {}";
        
        analyzer.analyzeCode(code1, "A.java");
        analyzer.analyzeCode(code2, "B.java");
        analyzer.analyzeCode(code3, "C.java");
        
        calculator = new MetricsCalculator(analyzer.getClasses(), analyzer.getAllMethods());
        assertEquals(2, calculator.getTotalNumberOfPackages());
    }
    
    /**
     * Test 5: Test du nombre maximal de paramètres
     */
    public void testMaxParameters() {
        String code = "public class Test {\n" +
                     "  public void m1() {}\n" +
                     "  public void m2(int a) {}\n" +
                     "  public void m3(int a, int b, int c) {}\n" +
                     "  public void m4(int a, int b, int c, int d, int e) {}\n" +
                     "}";
        
        analyzer.analyzeCode(code, "Test.java");
        calculator = new MetricsCalculator(analyzer.getClasses(), analyzer.getAllMethods());
        
        assertEquals(5, calculator.getMaxParametersInMethods());
    }
    
    /**
     * Test 6: Test des classes avec plus de X méthodes
     */
    public void testClassesWithMoreThanXMethods() {
        String code1 = "public class A { public void m1() {} public void m2() {} public void m3() {} }";
        String code2 = "public class B { public void m1() {} }";
        
        analyzer.analyzeCode(code1, "A.java");
        analyzer.analyzeCode(code2, "B.java");
        
        calculator = new MetricsCalculator(analyzer.getClasses(), analyzer.getAllMethods());
        
        List<ClassInfo> result = calculator.getClassesWithMoreThanXMethods(2);
        assertEquals(1, result.size());
        assertEquals("A", result.get(0).getName());
    }
    
    /**
     * Test 7: Test du top 10% des classes par méthodes
     */
    public void testTop10PercentClassesByMethods() {
        // Créer 10 classes avec différents nombres de méthodes
        for (int i = 1; i <= 10; i++) {
            StringBuilder code = new StringBuilder("public class Class" + i + " {");
            for (int j = 0; j < i; j++) {
                code.append("public void m").append(j).append("() {} ");
            }
            code.append("}");
            analyzer.analyzeCode(code.toString(), "Class" + i + ".java");
        }
        
        calculator = new MetricsCalculator(analyzer.getClasses(), analyzer.getAllMethods());
        List<ClassInfo> top10 = calculator.getTop10PercentClassesByMethods();
        
        assertTrue("Au moins une classe doit être dans le top 10%", top10.size() >= 1);
    }
    
    /**
     * Test 8: Test de l'analyse d'un dossier
     */
    public void testAnalyzeDirectory() throws IOException {
        File testDir = new File("src/test/java/com/example/ASTDemo");
        assertTrue("Le dossier de test doit exister", testDir.exists() && testDir.isDirectory());
        
        analyzer.analyzeDirectory(testDir);
        
        assertTrue("Plusieurs classes doivent être trouvées dans le dossier", 
                   analyzer.getClasses().size() >= 3);
    }
    
    /**
     * Test 9: Test des lignes de code
     */
    public void testTotalLinesOfCode() {
        String code = "public class Test {\n" +
                     "  public void method() {\n" +
                     "    System.out.println(\"Hello\");\n" +
                     "  }\n" +
                     "}";
        
        analyzer.analyzeCode(code, "Test.java");
        
        int lines = analyzer.getTotalLinesOfCode();
        assertTrue("Le nombre de lignes doit être > 0", lines > 0);
    }
    
    /**
     * Test 10: Test des méthodes avec beaucoup de lignes
     */
    public void testTop10PercentMethodsByLines() {
        String code = "public class Test {\n" +
                     "  public void shortMethod() {}\n" +
                     "  public void longMethod() {\n" +
                     "    System.out.println(\"Line 1\");\n" +
                     "    System.out.println(\"Line 2\");\n" +
                     "    System.out.println(\"Line 3\");\n" +
                     "    System.out.println(\"Line 4\");\n" +
                     "    System.out.println(\"Line 5\");\n" +
                     "  }\n" +
                     "}";
        
        analyzer.analyzeCode(code, "Test.java");
        calculator = new MetricsCalculator(analyzer.getClasses(), analyzer.getAllMethods());
        
        Map<String, List<MethodInfo>> topMethods = calculator.getTop10PercentMethodsByLinesPerClass();
        assertTrue("Au moins une méthode doit être trouvée", topMethods.size() > 0);
    }
}
