# Guide de Test - ASTDemo

Ce guide vous explique comment tester l'application ASTDemo de différentes manières.

> **Note importante** : Si vous n'avez pas Maven installé ou accessible en ligne de commande, vous pouvez tester directement depuis Eclipse sans Maven. Consultez **[GUIDE_TEST_ECLIPSE.md](GUIDE_TEST_ECLIPSE.md)** pour les instructions détaillées.

**Solution rapide sans Maven :**
1. Dans Eclipse, clic droit sur `App.java`
2. **Run As** → **Java Application**
3. Choisissez Option 1 ou 2 pour tester

## Méthode 1 : Test via l'application principale (recommandé)

### Étape 1 : Compiler le projet

Ouvrez un terminal dans le répertoire du projet et exécutez :

```bash
mvn clean compile
```

### Étape 2 : Exécuter l'application

```bash
mvn exec:java -Dexec.mainClass="com.example.ASTDemo.App"
```

Ou depuis Eclipse :
1. Clic droit sur `App.java`
2. **Run As** → **Java Application**

### Étape 3 : Choisir une option de test

#### Option 1 : Analyser depuis un fichier/dossier

1. Choisissez `1` dans le menu
2. Entrez un chemin, par exemple :
   - `src/test/java/com/example/ASTDemo/TestInput1.java` (un seul fichier)
   - `src/test/java/com/example/ASTDemo` (tous les fichiers du dossier)
   - `src/main/java` (analyser tout le code source principal)

#### Option 2 : Analyser depuis une chaîne de caractères

1. Choisissez `2` dans le menu
2. Collez votre code Java
3. Terminez par `END` sur une ligne séparée
4. Appuyez sur Entrée

Exemple :
```
2
package com.example.test;

public class Test {
    public void method() {
        System.out.println("Hello");
    }
}
END
```

---

## Méthode 2 : Tests unitaires avec JUnit

### Exécuter tous les tests

```bash
mvn test
```

### Exécuter un test spécifique

```bash
mvn test -Dtest=AppTest
```

### Depuis Eclipse

1. Clic droit sur `AppTest.java`
2. **Run As** → **JUnit Test**

---

## Méthode 3 : Test avec des scripts automatisés

### Script PowerShell (Windows)

Créez un fichier `test.ps1` dans le répertoire du projet :

```powershell
Write-Host "=== Test ASTDemo ===" -ForegroundColor Green

# Compiler
Write-Host "`n1. Compilation..." -ForegroundColor Yellow
mvn clean compile
if ($LASTEXITCODE -ne 0) {
    Write-Host "Erreur de compilation!" -ForegroundColor Red
    exit 1
}

# Exécuter les tests unitaires
Write-Host "`n2. Exécution des tests unitaires..." -ForegroundColor Yellow
mvn test
if ($LASTEXITCODE -ne 0) {
    Write-Host "Des tests ont échoué!" -ForegroundColor Red
    exit 1
}

Write-Host "`n=== Tests réussis! ===" -ForegroundColor Green
```

Exécutez avec :
```powershell
.\test.ps1
```

---

## Exemples de fichiers de test disponibles

Le projet contient déjà plusieurs fichiers de test dans `src/test/java/com/example/ASTDemo/` :

1. **TestInput1.java** : Classe avec plusieurs méthodes et attributs
2. **TestInput2.java** : Classe avec des méthodes de calcul
3. **TestInput3.java** : Classe avec beaucoup d'attributs et méthodes

---

## Vérification des métriques calculées

L'application calcule et affiche :

1. ✅ Nombre de classes
2. ✅ Nombre de lignes de code
3. ✅ Nombre total de méthodes
4. ✅ Nombre total de packages
5. ✅ Nombre moyen de méthodes par classe
6. ✅ Nombre moyen de lignes par méthode
7. ✅ Nombre moyen d'attributs par classe
8. ✅ Top 10% des classes par nombre de méthodes
9. ✅ Top 10% des classes par nombre d'attributs
10. ✅ Classes dans les deux catégories précédentes
11. ✅ Classes avec plus de X méthodes (interactif)
12. ✅ Top 10% des méthodes par lignes (par classe)
13. ✅ Nombre maximal de paramètres dans les méthodes

---

## Dépannage

### Erreur : "UnsupportedClassVersionError"
- Vérifiez que vous utilisez Java 8 ou supérieur
- Configurez Eclipse pour utiliser la bonne version de Java (voir README.md)

### Erreur : "FileNotFoundException"
- Vérifiez que le chemin du fichier est correct
- Utilisez des chemins relatifs depuis la racine du projet

### Les métriques ne s'affichent pas correctement
- Vérifiez que le code Java analysé est syntaxiquement correct
- Assurez-vous que les fichiers contiennent du code valide (pas seulement des commentaires)

---

## Astuces de test

1. **Testez avec différents types de code** :
   - Code simple avec peu de méthodes
   - Code complexe avec beaucoup de méthodes et attributs
   - Code avec beaucoup de paramètres

2. **Testez les cas limites** :
   - Classe vide
   - Classe avec une seule méthode
   - Méthode très longue
   - Méthode avec beaucoup de paramètres

3. **Vérifiez la cohérence** :
   - Le nombre total de méthodes doit correspondre à la somme des méthodes par classe
   - Les moyennes doivent être cohérentes avec les données

