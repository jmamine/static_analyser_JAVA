package com.example.ASTDemo;

import java.io.File;
import java.io.IOException;
import java.util.List;
import java.util.Map;
import java.util.Scanner;

public class App {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        
        System.out.println("=== Analyseur de Métriques Java ===");
        System.out.println();
        System.out.println("Choisissez une option:");
        System.out.println("1. Analyser du code depuis un fichier/dossier");
        System.out.println("2. Analyser du code depuis une chaîne de caractères");
        
        int choice = scanner.nextInt();
        scanner.nextLine(); // consommer le retour à la ligne
        
        CodeAnalyzer analyzer = new CodeAnalyzer();
        
        try {
            if (choice == 1) {
                System.out.print("Entrez le chemin du fichier ou dossier: ");
                String path = scanner.nextLine().trim();
                
                // Supprimer les guillemets si présents
                if ((path.startsWith("\"") && path.endsWith("\"")) || 
                    (path.startsWith("'") && path.endsWith("'"))) {
                    path = path.substring(1, path.length() - 1);
                }
                
                File file = new File(path);
                
                // Si le fichier n'existe pas, essayer depuis le répertoire de travail du projet
                if (!file.exists()) {
                    String currentDir = System.getProperty("user.dir");
                    System.out.println("Répertoire de travail actuel: " + currentDir);
                    
                    // Essayer avec le chemin depuis le répertoire de travail
                    File altFile = new File(currentDir, path);
                    if (altFile.exists()) {
                        file = altFile;
                        System.out.println("Fichier trouvé avec le chemin: " + file.getAbsolutePath());
                    }
                }
                
                if (file.isDirectory()) {
                    analyzer.analyzeDirectory(file);
                } else if (file.isFile()) {
                    analyzer.analyzeFile(file);
                } else {
                    System.out.println("Fichier ou dossier introuvable!");
                    System.out.println("Chemin essayé: " + file.getAbsolutePath());
                    System.out.println("Répertoire de travail: " + System.getProperty("user.dir"));
                    return;
                }
            } else if (choice == 2) {
                System.out.println("Entrez le code Java (terminez par 'END' sur une ligne séparée):");
                StringBuilder codeBuilder = new StringBuilder();
                String line;
                while (!(line = scanner.nextLine()).equals("END")) {
                    codeBuilder.append(line).append("\n");
                }
                analyzer.analyzeCode(codeBuilder.toString(), "input.java");
            } else {
                System.out.println("Option invalide!");
                return;
            }
            
            // Calculer toutes les métriques
            MetricsCalculator calculator = new MetricsCalculator(
                analyzer.getClasses(), 
                analyzer.getAllMethods()
            );
            
            // Afficher tous les résultats
            displayAllMetrics(calculator, analyzer);
            
            // Question 11: Classes avec plus de X méthodes
            System.out.println("\n=== Question 11 ===");
            System.out.print("Entrez la valeur de X pour trouver les classes avec plus de X méthodes: ");
            int x = scanner.nextInt();
            List<ClassInfo> classesWithMoreThanX = calculator.getClassesWithMoreThanXMethods(x);
            System.out.println("Classes avec plus de " + x + " méthodes:");
            for (ClassInfo classInfo : classesWithMoreThanX) {
                System.out.println("  - " + classInfo.getFullName() + " (" + classInfo.getMethodCount() + " méthodes)");
            }
            if (classesWithMoreThanX.isEmpty()) {
                System.out.println("  Aucune classe trouvée.");
            }
            
        } catch (IOException e) {
            System.out.println("Erreur lors de la lecture du fichier: " + e.getMessage());
        } finally {
            scanner.close();
        }
    }
    
    private static void displayAllMetrics(MetricsCalculator calculator, CodeAnalyzer analyzer) {
        System.out.println("\n=== RÉSULTATS DE L'ANALYSE ===\n");
        
        // Question 1
        System.out.println("1. Nombre de classes de l'application: " + calculator.getNumberOfClasses());
        
        // Question 2
        int totalLines = analyzer.getTotalLinesOfCode();
        System.out.println("2. Nombre de lignes de code de l'application: " + totalLines);
        
        // Question 3
        System.out.println("3. Nombre total de méthodes de l'application: " + calculator.getTotalNumberOfMethods());
        
        // Question 4
        System.out.println("4. Nombre total de packages de l'application: " + calculator.getTotalNumberOfPackages());
        
        // Question 5
        double avgMethods = calculator.getAverageMethodsPerClass();
        System.out.println("5. Nombre moyen de méthodes par classe: " + String.format("%.2f", avgMethods));
        
        // Question 6
        double avgLinesPerMethod = calculator.getAverageLinesPerMethod();
        System.out.println("6. Nombre moyen de lignes de code par méthode: " + String.format("%.2f", avgLinesPerMethod));
        
        // Question 7
        double avgAttributes = calculator.getAverageAttributesPerClass();
        System.out.println("7. Nombre moyen d'attributs par classe: " + String.format("%.2f", avgAttributes));
        
        // Question 8
        System.out.println("\n8. Les 10% des classes qui possèdent le plus grand nombre de méthodes:");
        List<ClassInfo> top10PercentMethods = calculator.getTop10PercentClassesByMethods();
        for (ClassInfo classInfo : top10PercentMethods) {
            System.out.println("   - " + classInfo.getFullName() + " (" + classInfo.getMethodCount() + " méthodes)");
        }
        if (top10PercentMethods.isEmpty()) {
            System.out.println("   Aucune classe trouvée.");
        }
        
        // Question 9
        System.out.println("\n9. Les 10% des classes qui possèdent le plus grand nombre d'attributs:");
        List<ClassInfo> top10PercentAttributes = calculator.getTop10PercentClassesByAttributes();
        for (ClassInfo classInfo : top10PercentAttributes) {
            System.out.println("   - " + classInfo.getFullName() + " (" + classInfo.getAttributeCount() + " attributs)");
        }
        if (top10PercentAttributes.isEmpty()) {
            System.out.println("   Aucune classe trouvée.");
        }
        
        // Question 10
        System.out.println("\n10. Les classes qui font partie en même temps des deux catégories précédentes:");
        List<ClassInfo> classesInBoth = calculator.getClassesInBothCategories();
        for (ClassInfo classInfo : classesInBoth) {
            System.out.println("    - " + classInfo.getFullName() + 
                            " (" + classInfo.getMethodCount() + " méthodes, " + 
                            classInfo.getAttributeCount() + " attributs)");
        }
        if (classesInBoth.isEmpty()) {
            System.out.println("    Aucune classe trouvée.");
        }
        
        // Question 12
        System.out.println("\n12. Les 10% des méthodes qui possèdent le plus grand nombre de lignes de code (par classe):");
        Map<String, List<MethodInfo>> topMethodsPerClass = calculator.getTop10PercentMethodsByLinesPerClass();
        if (topMethodsPerClass.isEmpty()) {
            System.out.println("    Aucune méthode trouvée.");
        } else {
            for (Map.Entry<String, List<MethodInfo>> entry : topMethodsPerClass.entrySet()) {
                System.out.println("    Classe: " + entry.getKey());
                for (MethodInfo method : entry.getValue()) {
                    System.out.println("      - " + method.getName() + " (" + method.getLineCount() + " lignes)");
                }
            }
        }
        
        // Question 13
        System.out.println("\n13. Le nombre maximal de paramètres par rapport à toutes les méthodes de l'application: " + 
                          calculator.getMaxParametersInMethods());
    }
}
