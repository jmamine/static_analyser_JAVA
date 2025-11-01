# Solution : Problème de Chemin dans Eclipse

## Problème

Lors de l'exécution depuis Eclipse, vous obtenez : `"Fichier ou dossier introuvable!"`

Cela se produit parce que le **répertoire de travail** (working directory) d'Eclipse peut être différent du répertoire du projet.

---

## Solutions

### Solution 1 : Utiliser un chemin absolu (Le plus simple)

Au lieu d'entrer un chemin relatif, utilisez le chemin absolu complet du fichier.

**Exemple :**
```
C:\Users\dell\eclipse-workspace\ASTDemo\src\test\java\com\example\ASTDemo\TestInput1.java
```

**Comment obtenir le chemin absolu :**
1. Dans Eclipse, clic droit sur `TestInput1.java`
2. **Properties** (Propriétés)
3. Copiez le chemin complet affiché dans "Location" ou "Path"

---

### Solution 2 : Configurer le répertoire de travail dans Eclipse

1. **Clic droit sur `App.java`** → **Run As** → **Run Configurations...**
2. Dans la section **Arguments**, trouvez **Working directory**
3. Sélectionnez **Other:**
4. Cliquez sur **Workspace...**
5. Sélectionnez le projet **ASTDemo**
6. Cliquez **OK**
7. Cliquez **Run**

Maintenant, les chemins relatifs devraient fonctionner !

---

### Solution 3 : Utiliser un chemin relatif depuis le répertoire racine du projet

Essayez ces chemins (sans guillemets) :

```
src\test\java\com\example\ASTDemo\TestInput1.java
```

ou avec des slash :

```
src/test/java/com/example/ASTDemo/TestInput1.java
```

---

### Solution 4 : Naviguer manuellement au répertoire

1. Ouvrez un explorateur de fichiers Windows
2. Naviguez jusqu'à : `C:\Users\dell\eclipse-workspace\ASTDemo`
3. Copiez le chemin complet d'un fichier (clic droit → Propriétés)
4. Collez ce chemin dans l'application

---

### Solution 5 : Utiliser l'Option 2 (Code depuis chaîne)

Au lieu d'utiliser Option 1, utilisez **Option 2** et copiez-collez directement le contenu du fichier :

1. Dans Eclipse, ouvrez `TestInput1.java`
2. Sélectionnez tout le code (`Ctrl+A`)
3. Copiez (`Ctrl+C`)
4. Dans l'application, choisissez **Option 2**
5. Collez le code
6. Tapez `END` sur une nouvelle ligne
7. Appuyez sur Entrée

---

## Code amélioré

J'ai modifié le code pour :
- ✅ Supprimer automatiquement les guillemets autour du chemin
- ✅ Afficher le répertoire de travail actuel pour vous aider
- ✅ Essayer automatiquement de trouver le fichier depuis le répertoire de travail

**Recompilez et réexécutez** l'application pour utiliser ces améliorations.

---

## Vérification rapide

1. Réexécutez l'application (`App.java` → Run As → Java Application)
2. Choisissez **Option 1**
3. Entrez le chemin **sans guillemets** : 
   ```
   src/test/java/com/example/ASTDemo/TestInput1.java
   ```
4. Si ça ne marche pas, l'application vous affichera maintenant :
   - Le répertoire de travail actuel
   - Le chemin complet essayé
   - Cela vous aidera à trouver le bon chemin !

---

## Recommandation

Pour éviter ce problème à l'avenir, utilisez **Solution 2** (configurer le répertoire de travail dans Eclipse). C'est une configuration unique et ensuite tous les chemins relatifs fonctionneront correctement.



