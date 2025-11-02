# Analyseur Statique de Code Java

Un outil d'analyse statique pour le code Java permettant d'analyser les métriques de code et de générer des graphes d'appel visuels.

## 📋 Table des matières

- [Aperçu](#aperçu)
- [Description](#Description)
- [Pattern Visitor](#Pattern_Visitor)
- [Fonctionnalités](#fonctionnalités)
- [Configuration requise](#configuration-requise)
- [Installation](#installation)
- [Utilisation](#utilisation)
- [Architecture](#architecture)

## 🎯 Aperçu

Cet analyseur statique permet de :
- Analyser les métriques de code Java (nombre de classes, méthodes, lignes de code, etc.)
- Générer des graphes d'appel visuels pour visualiser les relations entre les méthodes
- Analyser à la fois des projets complets et des extraits de code

## Description

Cet outil d'analyse statique de code Java permet d'extraire et visualiser des métriques de code ainsi que de générer des graphes d'appel. Le projet est basé sur l'analyse de l'**AST (Abstract Syntax Tree)** en utilisant le **design pattern Visitor**.

### Architecture basée sur l'AST

Le projet utilise la bibliothèque **Eclipse JDT** pour parser le code Java source et construire un arbre de syntaxe abstraite. L'AST représente la structure hiérarchique du code (packages, classes, méthodes, déclarations, etc.) de manière arborescente.

### Pattern_Visitor

Le **design pattern Visitor** est utilisé pour parcourir l'AST de manière flexible :
- **CodeVisitor** : Visiteur qui parcourt l'AST pour extraire les informations sur les classes, méthodes et attributs
- **CallGraphVisitor** : Visiteur spécialisé qui détecte les appels de méthodes entre les différents éléments du code
- Chaque visiteur implémente `ASTVisitor` et surcharge les méthodes `visit()` pour traiter les nœuds spécifiques (TypeDeclaration, MethodDeclaration, MethodInvocation, etc.)

Cette approche permet de séparer la structure des données (l'AST) des opérations effectuées sur celles-ci (analyse, calcul de métriques, génération de graphes).


## ✨ Fonctionnalités

### 1. Analyse de Métriques
- Nombre de classes, méthodes et packages
- Nombre de lignes de code
- Statistiques moyennes (méthodes par classe, lignes par méthode, etc.)
- Identification des classes avec le plus grand nombre de méthodes/attributs
- Analyse des méthodes avec le plus de lignes de code

### 2. Graphe d'Appel
- Visualisation graphique interactive des relations entre méthodes
- Génération de graphes depuis un répertoire complet ou du code saisi
- Affichage des méthodes appelantes et appelées avec des nœuds et flèches
- Panneau détaillé listant toutes les relations

## 🔧 Configuration requise

- **Java** : 17 ou supérieur
- **Maven** : 3.6 ou supérieur
- **Eclipse IDE** : Optionnel (pour le développement)

## 📦 Installation

### Méthode 1 : Depuis Eclipse (Recommandé)

1. Clonez le projet dans votre workspace Eclipse
2. Ouvrez Eclipse et importez le projet existant
3. Configurez le projet pour utiliser Java 17 :
   - Clic droit sur le projet → **Properties**
   - **Java Build Path** → **Libraries**
   - Sélectionnez **Execution environment** → **JavaSE-17**
4. Maven mettra automatiquement à jour les dépendances

### Méthode 2 : Compilation via Maven

```bash
# Cloner le projet
git clone https://github.com/jmamine/static_analyser_JAVA.git
cd static_analyser

# Compiler le projet
mvn clean compile

# Exécuter les tests
mvn test
```

## 🚀 Utilisation

### Exécution de l'application

#### Option A : Depuis Eclipse
1. Ouvrez `src/main/java/com/example/ASTDemo/AppUI.java`
2. Clic droit → **Run As** → **Java Application**

#### Option B : Via Maven
```bash
mvn javafx:run
```

### Guide d'utilisation

Une fois l'application lancée, vous verrez une fenêtre principale avec 4 options :

#### 1️⃣ Analyser les métriques d'un projet
- **Fonction** : Analyse complète d'un répertoire de code source Java
- **Utilisation** :
  1. Cliquez sur le bouton
  2. Sélectionnez le répertoire contenant vos fichiers `.java`
  3. Les résultats s'affichent dans une nouvelle fenêtre avec :
     - Nombre de classes, méthodes, packages
     - Moyennes statistiques
     - Top 10% des classes par métriques
     - Analyse personnalisée du nombre de méthodes

#### 2️⃣ Analyser du code saisi
- **Fonction** : Analyse métrique d'un extrait de code Java
- **Utilisation** :
  1. Cliquez sur le bouton
  2. Saisissez ou collez votre code Java dans la zone de texte
  3. Cliquez sur "Lancer l'analyse"
  4. Visualisez les métriques

#### 3️⃣ Générer un graphe d'appel (projet)
- **Fonction** : Création d'un graphe d'appel depuis un répertoire
- **Utilisation** :
  1. Cliquez sur le bouton
  2. Sélectionnez le répertoire à analyser
  3. Une fenêtre de visualisation s'ouvre avec :
     - **Gauche** : Graphe visuel (nœuds = méthodes, flèches = appels)
     - **Droite** : Liste textuelle détaillée des relations

#### 4️⃣ Générer un graphe d'appel (code)
- **Fonction** : Création d'un graphe d'appel depuis du code saisi
- **Utilisation** :
  1. Cliquez sur le bouton
  2. Saisissez ou collez votre code Java
  3. Cliquez sur "Générer le graphe d'appel"
  4. Explorez le graphe interactif

### Exemple de code à analyser

```java
package com.example;

public class Calculatrice {
    private int resultat;
    
    public Calculatrice() {
        this.resultat = 0;
    }
    
    public int additionner(int a, int b) {
        resultat = a + b;
        return resultat;
    }
    
    public int multiplier(int a, int b) {
        resultat = a * b;
        afficherResultat();
        return resultat;
    }
    
    private void afficherResultat() {
        System.out.println("Résultat : " + resultat);
    }
}
```

## 🏗️ Architecture

### Structure du projet
```
static_analyser/
├── src/
│   ├── main/
│   │   ├── java/com/example/ASTDemo/
│   │   │   ├── AppUI.java              # Interface utilisateur principale
│   │   │   ├── CodeAnalyzer.java       # Analyseur AST principal
│   │   │   ├── CallGraphAnalyzer.java  # Analyseur de graphe d'appel
│   │   │   ├── MetricsCalculator.java  # Calcul des métriques
│   │   │   ├── ClassInfo.java          # Informations sur les classes
│   │   │   └── MethodInfo.java         # Informations sur les méthodes
│   │   └── resources/
│   │       └── logo.png                # Logo de l'application
│   └── test/
│       └── java/com/example/ASTDemo/
│           ├── AppTest.java            # Tests unitaires
│           ├── TestInput1.java         # Exemples de test
│           ├── TestInput2.java
│           └── TestInput3.java
├── pom.xml                             # Configuration Maven
└── README.md                           # Ce fichier
```

### Technologies utilisées
- **JavaFX** : Interface graphique moderne
- **Eclipse JDT** : Parsing et analyse du code Java (AST)
- **Maven** : Gestion des dépendances et compilation

### Méthodes d'analyse
- Utilisation de l'AST (Abstract Syntax Tree) d'Eclipse JDT
- Visiteurs AST pour parcourir les structures de code
- Extraction de métriques à travers le modèle de données

## 🐛 Dépannage

### Erreur : Java version non supportée
```
UnsupportedClassVersionError
```
**Solution** : Assurez-vous d'utiliser Java 17 ou supérieur
```bash
java -version  # Vérifier la version
```

### L'application ne se lance pas
**Solution** : Vérifiez que toutes les dépendances Maven sont téléchargées
```bash
mvn clean install
```

### Le logo ne s'affiche pas
**Solution** : Le logo est optionnel. L'application fonctionne sans problème sans celui-ci.

## 📝 Notes

- L'analyse est basée sur la syntaxe du code, pas sur sa compilation complète
- Les appels de méthodes vers des bibliothèques externes ne sont pas toujours résolus
- Le graphe d'appel est interactif : vous pouvez scroller pour explorer de grands graphes

## 📄 Licence

Ce projet est fourni à des fins éducatives et de démonstration.

## 🤝 Contribution

Pour contribuer au projet, veuillez :
1. Fork le projet
2. Créer une branche pour votre feature
3. Committez vos changements
4. Push vers la branche
5. Ouvrir une Pull Request

---

**Développé avec ❤️ pour l'analyse de code Java**
