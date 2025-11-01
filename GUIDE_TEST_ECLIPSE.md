# Guide de Test depuis Eclipse (Sans Maven en ligne de commande)

Ce guide vous explique comment tester l'application ASTDemo directement depuis Eclipse, sans avoir besoin d'installer Maven en ligne de commande.

## Option 1 : Exécuter l'application principale

### Étape 1 : Compiler le projet

1. Dans Eclipse, faites un clic droit sur le projet **ASTDemo**
2. Sélectionnez **Maven** → **Update Project...**
3. Cochez **Force Update of Snapshots/Releases**
4. Cliquez **OK**

Ou simplement :
1. Clic droit sur le projet → **Build Project** (ou appuyez sur `Ctrl+B`)

### Étape 2 : Exécuter l'application

1. Ouvrez le fichier `App.java` dans `src/main/java/com/example/ASTDemo/`
2. Clic droit sur le fichier → **Run As** → **Java Application**

### Étape 3 : Tester avec différentes options

#### Test avec Option 1 (Fichier/Dossier)

Lorsque l'application démarre :
1. Tapez `1` et appuyez sur Entrée
2. Entrez un chemin relatif depuis la racine du projet :
   - `src/test/java/com/example/ASTDemo/TestInput1.java` (un fichier)
   - `src/test/java/com/example/ASTDemo` (tous les fichiers du dossier)

#### Test avec Option 2 (Code depuis chaîne)

Lorsque l'application démarre :
1. Tapez `2` et appuyez sur Entrée
2. Collez votre code Java
3. Terminez par `END` sur une ligne séparée
4. Appuyez sur Entrée

**Exemple :**
```
2
public class Test {
    private int value;
    public void method() {
        System.out.println("Hello");
    }
}
END
```

---

## Option 2 : Exécuter les tests unitaires

### Méthode 1 : Depuis la vue Package Explorer

1. Ouvrez `AppTest.java` dans `src/test/java/com/example/ASTDemo/`
2. Clic droit sur le fichier → **Run As** → **JUnit Test**

### Méthode 2 : Depuis la vue JUnit

1. Exécutez une première fois avec la Méthode 1 ci-dessus
2. Une fois les tests terminés, vous verrez la vue JUnit s'ouvrir
3. Vous pouvez réexécuter les tests en cliquant sur le bouton ▶️ dans la vue JUnit

### Méthode 3 : Exécuter tous les tests du projet

1. Clic droit sur le projet **ASTDemo**
2. **Run As** → **JUnit Test**

---

## Option 3 : Exécuter via la console Eclipse

Si vous souhaitez utiliser Maven depuis Eclipse :

1. **Installer Maven dans Eclipse** :
   - Ouvrez **Help** → **Eclipse Marketplace**
   - Recherchez "M2Eclipse" ou "Maven Integration for Eclipse"
   - Installez-le si ce n'est pas déjà fait (la plupart des versions d'Eclipse le contiennent déjà)

2. **Créer une configuration Maven** :
   - Clic droit sur le projet → **Run As** → **Run Configurations...**
   - Cliquez sur **Maven Build** → **New Configuration**
   - Nom : `test`
   - Goals : `test`
   - Base directory : `${workspace_loc:/ASTDemo}`
   - Cliquez **Run**

---

## Vérification rapide

### Test rapide depuis Eclipse :

1. **Compiler** : `Ctrl+B` ou clic droit → **Build Project**
2. **Exécuter App** : Clic droit sur `App.java` → **Run As** → **Java Application**
3. **Choisir Option 1** et entrer : `src/test/java/com/example/ASTDemo/TestInput1.java`
4. Vérifier que les métriques s'affichent correctement

---

## Installation de Maven (Optionnel)

Si vous souhaitez utiliser Maven en ligne de commande :

### Étape 1 : Télécharger Maven

1. Allez sur https://maven.apache.org/download.cgi
2. Téléchargez l'archive ZIP (par exemple `apache-maven-3.9.x-bin.zip`)
3. Extrayez l'archive dans un dossier (par exemple `C:\Program Files\Apache\maven`)

### Étape 2 : Configurer les variables d'environnement

1. Ouvrez **Paramètres Windows** → **Système** → **Informations système** → **Paramètres système avancés**
2. Cliquez sur **Variables d'environnement**
3. Sous **Variables système**, trouvez `Path` et cliquez **Modifier**
4. Cliquez **Nouveau** et ajoutez le chemin vers le dossier `bin` de Maven
   - Par exemple : `C:\Program Files\Apache\maven\bin`
5. Cliquez **OK** sur toutes les fenêtres

### Étape 3 : Vérifier l'installation

Ouvrez un nouveau PowerShell et tapez :
```powershell
mvn -version
```

Vous devriez voir la version de Maven s'afficher.

---

## Dépannage

### Erreur : "Project is not a Maven project"

**Solution** :
1. Clic droit sur le projet → **Configure** → **Convert to Maven Project**
2. Ou créez un nouveau projet Maven et copiez les fichiers

### Erreur : "UnsupportedClassVersionError"

**Solution** : Vérifiez que vous utilisez Java 8 ou supérieur
1. Clic droit sur le projet → **Properties**
2. **Java Build Path** → **Libraries**
3. Vérifiez que le JRE System Library est Java 8 ou supérieur

### Erreur : "Cannot resolve Eclipse JDT Core"

**Solution** : Maven doit télécharger les dépendances
1. Clic droit sur le projet → **Maven** → **Update Project...**
2. Cochez **Force Update of Snapshots/Releases**
3. Cliquez **OK**

### Les tests ne s'exécutent pas

**Solution** :
1. Vérifiez que JUnit est présent dans le projet
2. Clic droit sur le projet → **Properties** → **Java Build Path** → **Libraries**
3. Si JUnit n'est pas présent, ajoutez-le via **Add Library** → **JUnit** → **JUnit 3** (ancienne version compatible)

---

## Conseils

1. **Pour tester rapidement** : Utilisez l'Option 1 (Exécuter App) directement depuis Eclipse
2. **Pour des tests automatisés** : Utilisez l'Option 2 (Tests unitaires)
3. **Pour une utilisation en ligne de commande** : Installez Maven (Option 3)

---

## Exemples de fichiers de test

Le projet contient déjà ces fichiers de test :
- `TestInput1.java` : Classe avec plusieurs méthodes et attributs
- `TestInput2.java` : Classe avec des méthodes de calcul
- `TestInput3.java` : Classe avec beaucoup d'attributs et méthodes

Utilisez-les avec l'Option 1 de l'application principale !



