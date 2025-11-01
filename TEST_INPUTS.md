# Exemples de test pour ASTDemo

## Option 1 : Analyser du code depuis un fichier/dossier

### Chemins à utiliser lors du test :

1. **Analyser un fichier spécifique** :
   ```
   src/test/java/com/example/ASTDemo/TestInput1.java
   ```

2. **Analyser tout le dossier de test** :
   ```
   src/test/java/com/example/ASTDemo
   ```

3. **Analyser tout le projet** :
   ```
   src/main/java
   ```

---

## Option 2 : Analyser du code depuis une chaîne de caractères

### Exemple 1 : Code simple avec plusieurs classes

```
package com.example.test;

public class Calculator {
    private int result;
    
    public Calculator() {
        this.result = 0;
    }
    
    public int add(int a, int b) {
        result = a + b;
        return result;
    }
    
    public int subtract(int a, int b) {
        result = a - b;
        return result;
    }
    
    public int multiply(int a, int b) {
        result = a * b;
        return result;
    }
    
    public int divide(int a, int b) {
        if (b != 0) {
            result = a / b;
        }
        return result;
    }
    
    public void reset() {
        result = 0;
    }
}

class MathUtils {
    public static int max(int a, int b) {
        return a > b ? a : b;
    }
    
    public static int min(int a, int b) {
        return a < b ? a : b;
    }
}

class Geometry {
    private double pi = 3.14159;
    
    public double circleArea(double radius) {
        return pi * radius * radius;
    }
    
    public double rectangleArea(double width, double height) {
        return width * height;
    }
    
    public double triangleArea(double base, double height) {
        return 0.5 * base * height;
    }
}
END
```

### Exemple 2 : Code avec beaucoup de méthodes et attributs

```
package com.example.test;

public class ComplexClass {
    private String attribute1;
    private String attribute2;
    private String attribute3;
    private int attribute4;
    private int attribute5;
    private boolean attribute6;
    private double attribute7;
    private double attribute8;
    
    public void method1() {
        System.out.println("Method 1");
    }
    
    public void method2() {
        System.out.println("Method 2 - Line 1");
        System.out.println("Method 2 - Line 2");
        System.out.println("Method 2 - Line 3");
    }
    
    public void method3() {
        System.out.println("Method 3");
    }
    
    public void method4() {
        System.out.println("Method 4");
    }
    
    public void method5() {
        System.out.println("Method 5");
    }
    
    public void method6() {
        System.out.println("Method 6");
    }
    
    public void method7() {
        System.out.println("Method 7");
    }
    
    public void method8() {
        System.out.println("Method 8");
    }
    
    public void method9() {
        System.out.println("Method 9");
    }
    
    public void method10() {
        System.out.println("Method 10");
    }
    
    public void methodWithManyParams(int a, int b, int c, int d, int e, int f, int g, int h) {
        System.out.println("Method with 8 parameters");
    }
    
    public void longMethod() {
        System.out.println("Line 1");
        System.out.println("Line 2");
        System.out.println("Line 3");
        System.out.println("Line 4");
        System.out.println("Line 5");
        System.out.println("Line 6");
        System.out.println("Line 7");
        System.out.println("Line 8");
        System.out.println("Line 9");
        System.out.println("Line 10");
    }
}
END
```

### Exemple 3 : Code avec méthodes avec plusieurs paramètres

```
package com.example.test;

public class ParameterTest {
    public void noParams() {
        System.out.println("No parameters");
    }
    
    public void oneParam(int a) {
        System.out.println(a);
    }
    
    public void twoParams(int a, int b) {
        System.out.println(a + b);
    }
    
    public void threeParams(int a, int b, int c) {
        System.out.println(a + b + c);
    }
    
    public void fourParams(int a, int b, int c, int d) {
        System.out.println(a + b + c + d);
    }
    
    public void fiveParams(int a, int b, int c, int d, int e) {
        System.out.println(a + b + c + d + e);
    }
    
    public void sixParams(int a, int b, int c, int d, int e, int f) {
        System.out.println(a + b + c + d + e + f);
    }
    
    public void sevenParams(int a, int b, int c, int d, int e, int f, int g) {
        System.out.println(a + b + c + d + e + f + g);
    }
}
END
```

---

## Instructions pour tester

### Test Option 1 :
1. Lancez l'application
2. Choisissez `1`
3. Entrez : `src/test/java/com/example/ASTDemo/TestInput1.java`
4. Ou entrez : `src/test/java/com/example/ASTDemo` (pour analyser les 3 fichiers)

### Test Option 2 :
1. Lancez l'application
2. Choisissez `2`
3. Copiez-collez l'un des exemples ci-dessus (y compris le `END` final)
4. Appuyez sur Entrée

---

## Fichiers de test créés

Les fichiers suivants ont été créés dans `src/test/java/com/example/ASTDemo/` :
- `TestInput1.java` : Classe avec plusieurs méthodes et attributs
- `TestInput2.java` : Classe avec des méthodes de calcul
- `TestInput3.java` : Classe avec beaucoup d'attributs (10) et plusieurs méthodes

Ces fichiers peuvent être utilisés avec l'Option 1.

