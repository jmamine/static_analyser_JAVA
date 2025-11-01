package com.example.ASTDemo;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

public class MetricsCalculator {
    private List<ClassInfo> classes;
    private List<MethodInfo> allMethods;

    public MetricsCalculator(List<ClassInfo> classes, List<MethodInfo> allMethods) {
        this.classes = classes;
        this.allMethods = allMethods;
    }

    // 1. Nombre de classes de l'application
    public int getNumberOfClasses() {
        return classes.size();
    }

    // 2. Nombre de lignes de code de l'application
    public int getTotalLinesOfCode(int totalLines) {
        return totalLines;
    }

    // 3. Nombre total de méthodes de l'application
    public int getTotalNumberOfMethods() {
        return allMethods.size();
    }

    // 4. Nombre total de packages de l'application
    public int getTotalNumberOfPackages() {
        Set<String> packages = classes.stream()
            .map(ClassInfo::getPackageName)
            .filter(pkg -> pkg != null && !pkg.isEmpty())
            .collect(Collectors.toSet());
        
        // Add default package if any class has no package
        boolean hasDefaultPackage = classes.stream()
            .anyMatch(c -> c.getPackageName() == null || c.getPackageName().isEmpty());
        
        return packages.size() + (hasDefaultPackage ? 1 : 0);
    }

    // 5. Nombre moyen de méthodes par classe
    public double getAverageMethodsPerClass() {
        if (classes.isEmpty()) {
            return 0.0;
        }
        int totalMethods = classes.stream()
            .mapToInt(ClassInfo::getMethodCount)
            .sum();
        return (double) totalMethods / classes.size();
    }

    // 6. Nombre moyen de lignes de code par méthode
    public double getAverageLinesPerMethod() {
        if (allMethods.isEmpty()) {
            return 0.0;
        }
        int totalLines = allMethods.stream()
            .mapToInt(MethodInfo::getLineCount)
            .sum();
        return (double) totalLines / allMethods.size();
    }

    // 7. Nombre moyen d'attributs par classe
    public double getAverageAttributesPerClass() {
        if (classes.isEmpty()) {
            return 0.0;
        }
        int totalAttributes = classes.stream()
            .mapToInt(ClassInfo::getAttributeCount)
            .sum();
        return (double) totalAttributes / classes.size();
    }

    // 8. Les 10% des classes qui possèdent le plus grand nombre de méthodes
    public List<ClassInfo> getTop10PercentClassesByMethods() {
        if (classes.isEmpty()) {
            return new ArrayList<>();
        }
        
        int top10PercentCount = Math.max(1, (int) Math.ceil(classes.size() * 0.1));
        
        return classes.stream()
            .sorted(Comparator.comparingInt(ClassInfo::getMethodCount).reversed())
            .limit(top10PercentCount)
            .collect(Collectors.toList());
    }

    // 9. Les 10% des classes qui possèdent le plus grand nombre d'attributs
    public List<ClassInfo> getTop10PercentClassesByAttributes() {
        if (classes.isEmpty()) {
            return new ArrayList<>();
        }
        
        int top10PercentCount = Math.max(1, (int) Math.ceil(classes.size() * 0.1));
        
        return classes.stream()
            .sorted(Comparator.comparingInt(ClassInfo::getAttributeCount).reversed())
            .limit(top10PercentCount)
            .collect(Collectors.toList());
    }

    // 10. Les classes qui font partie en même temps des deux catégories précédentes
    public List<ClassInfo> getClassesInBothCategories() {
        List<ClassInfo> topMethods = getTop10PercentClassesByMethods();
        List<ClassInfo> topAttributes = getTop10PercentClassesByAttributes();
        
        Set<String> topMethodsNames = topMethods.stream()
            .map(ClassInfo::getFullName)
            .collect(Collectors.toSet());
        
        return topAttributes.stream()
            .filter(c -> topMethodsNames.contains(c.getFullName()))
            .collect(Collectors.toList());
    }

    // 11. Les classes qui possèdent plus de X méthodes (la valeur de X est donnée)
    public List<ClassInfo> getClassesWithMoreThanXMethods(int x) {
        return classes.stream()
            .filter(c -> c.getMethodCount() > x)
            .collect(Collectors.toList());
    }

    // 12. Les 10% des méthodes qui possèdent le plus grand nombre de lignes de code (par classe)
    public Map<String, List<MethodInfo>> getTop10PercentMethodsByLinesPerClass() {
        Map<String, List<MethodInfo>> methodsByClass = allMethods.stream()
            .collect(Collectors.groupingBy(MethodInfo::getClassName));
        
        Map<String, List<MethodInfo>> result = new HashMap<>();
        
        for (Map.Entry<String, List<MethodInfo>> entry : methodsByClass.entrySet()) {
            List<MethodInfo> classMethods = entry.getValue();
            if (classMethods.isEmpty()) {
                continue;
            }
            
            int top10PercentCount = Math.max(1, (int) Math.ceil(classMethods.size() * 0.1));
            
            List<MethodInfo> topMethods = classMethods.stream()
                .sorted(Comparator.comparingInt(MethodInfo::getLineCount).reversed())
                .limit(top10PercentCount)
                .collect(Collectors.toList());
            
            result.put(entry.getKey(), topMethods);
        }
        
        return result;
    }

    // 13. Le nombre maximal de paramètres par rapport à toutes les méthodes de l'application
    public int getMaxParametersInMethods() {
        if (allMethods.isEmpty()) {
            return 0;
        }
        return allMethods.stream()
            .mapToInt(MethodInfo::getParameterCount)
            .max()
            .orElse(0);
    }
}

