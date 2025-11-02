package com.example.ASTDemo;

import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Line;
import javafx.scene.text.Text;
import javafx.stage.DirectoryChooser;
import javafx.stage.Stage;

import java.io.File;
import java.io.IOException;
import java.util.*;

public class AppUI extends Application {

    private CodeAnalyzer analyzer = new CodeAnalyzer();

    @Override
    public void start(Stage primaryStage) {
        primaryStage.setTitle("Analyseur Statique de Code Java");

        // Logo de l'application pour la barre de titre
        try {
            Image logo = new Image(getClass().getResourceAsStream("/logo.png"));
            primaryStage.getIcons().add(logo);
        } catch (Exception e) {
            System.out.println("Logo non trouvé !");
        }

        // Créer une fenêtre principale avec en-tête et icône
        VBox mainRoot = new VBox();
        mainRoot.setSpacing(20);
        mainRoot.setPadding(new Insets(30));
        mainRoot.setStyle("-fx-background-color: #f8f9fa;");

        // En-tête avec icône et titre
        HBox headerBox = new HBox(15);
        headerBox.setAlignment(javafx.geometry.Pos.CENTER_LEFT);
        headerBox.setPadding(new Insets(0, 0, 20, 0));
        
        try {
            Image headerIcon = new Image(getClass().getResourceAsStream("/logo.png"), 48, 48, true, true);
            ImageView iconView = new ImageView(headerIcon);
            headerBox.getChildren().add(iconView);
        } catch (Exception e) {
            // Si l'icône n'est pas trouvée, on continue sans
        }
        
        VBox titleBox = new VBox(5);
        Label mainTitle = new Label("Analyseur Statique de Code Java");
        mainTitle.setStyle("-fx-font-size: 24px; -fx-font-weight: bold; -fx-text-fill: #2c3e50;");
        Label subtitle = new Label("Analyse de métriques et génération de graphes d'appel");
        subtitle.setStyle("-fx-font-size: 14px; -fx-text-fill: #7f8c8d;");
        titleBox.getChildren().addAll(mainTitle, subtitle);
        headerBox.getChildren().add(titleBox);

        // Panneau de sélection d'options
        VBox optionsBox = new VBox(15);
        optionsBox.setPadding(new Insets(20));
        optionsBox.setStyle("-fx-background-color: white; -fx-background-radius: 8; -fx-effect: dropshadow(gaussian, rgba(0,0,0,0.1), 10, 0, 0, 2);");
        
        Label optionsTitle = new Label("Sélectionnez une fonctionnalité :");
        optionsTitle.setStyle("-fx-font-size: 16px; -fx-font-weight: bold; -fx-text-fill: #34495e;");
        
        Button btn1 = createOptionButton("📊 Analyser les métriques d'un projet", 
            "Analyse complète d'un répertoire de code source Java");
        Button btn2 = createOptionButton("📝 Analyser du code saisi", 
            "Analyse métrique d'un extrait de code Java");
        Button btn3 = createOptionButton("🔗 Générer un graphe d'appel (projet)", 
            "Création d'un graphe d'appel depuis un répertoire");
        Button btn4 = createOptionButton("📈 Générer un graphe d'appel (code)", 
            "Création d'un graphe d'appel depuis du code saisi");

        btn1.setOnAction(e -> analyzeProject(primaryStage));
        btn2.setOnAction(e -> analyzeText(primaryStage));
        btn3.setOnAction(e -> analyzeCallGraph(primaryStage));
        btn4.setOnAction(e -> analyzeCallGraphFromText(primaryStage));

        optionsBox.getChildren().addAll(optionsTitle, btn1, btn2, btn3, btn4);
        
        mainRoot.getChildren().addAll(headerBox, optionsBox);

        Scene mainScene = new Scene(mainRoot, 600, 650);
        primaryStage.setScene(mainScene);
        primaryStage.setResizable(false);
        primaryStage.show();
    }

    private Button createOptionButton(String title, String description) {
        VBox buttonContent = new VBox(5);
        buttonContent.setAlignment(javafx.geometry.Pos.CENTER_LEFT);
        Label titleLabel = new Label(title);
        titleLabel.setStyle("-fx-font-size: 14px; -fx-font-weight: bold; -fx-text-fill: #2c3e50;");
        Label descLabel = new Label(description);
        descLabel.setStyle("-fx-font-size: 11px; -fx-text-fill: #7f8c8d;");
        buttonContent.getChildren().addAll(titleLabel, descLabel);
        
        Button button = new Button();
        button.setGraphic(buttonContent);
        button.setContentDisplay(javafx.scene.control.ContentDisplay.LEFT);
        button.setMaxWidth(Double.MAX_VALUE);
        button.setStyle("-fx-alignment: center-left; -fx-padding: 15px; -fx-background-color: #ecf0f1; -fx-background-radius: 5;");
        button.setOnMouseEntered(e -> button.setStyle("-fx-alignment: center-left; -fx-padding: 15px; -fx-background-color: #d5dbdb; -fx-background-radius: 5;"));
        button.setOnMouseExited(e -> button.setStyle("-fx-alignment: center-left; -fx-padding: 15px; -fx-background-color: #ecf0f1; -fx-background-radius: 5;"));
        
        return button;
    }

    private void analyzeProject(Stage stage) {
        DirectoryChooser chooser = new DirectoryChooser();
        chooser.setTitle("Sélectionner le répertoire du projet Java");
        File projectDir = chooser.showDialog(stage);

        if (projectDir == null || !projectDir.exists()) {
            Alert alert = new Alert(Alert.AlertType.ERROR, "Aucun répertoire sélectionné ou répertoire introuvable.");
            alert.setTitle("Erreur de sélection");
            alert.showAndWait();
            return;
        }

        try {
            analyzer.analyzeDirectory(projectDir);
            MetricsCalculator calculator = new MetricsCalculator(
                    analyzer.getClasses(), analyzer.getAllMethods()
            );
            showResults(calculator, analyzer);
        } catch (IOException e) {
            Alert alert = new Alert(Alert.AlertType.ERROR, "Erreur lors de l'analyse : " + e.getMessage());
            alert.setTitle("Erreur d'analyse");
            alert.showAndWait();
        }
    }

    private void analyzeText(Stage stage) {
        Stage textStage = new Stage();
        textStage.setTitle("Analyse de Code - Saisie Manuelle");
        try {
            Image logo = new Image(getClass().getResourceAsStream("/logo.png"));
            textStage.getIcons().add(logo);
        } catch (Exception e) {
            // Ignore
        }

        VBox root = new VBox(15);
        root.setPadding(new Insets(20));
        root.setStyle("-fx-background-color: #f8f9fa;");

        Label titleLabel = new Label("Analyse de Métriques - Code Saisi");
        titleLabel.setStyle("-fx-font-size: 18px; -fx-font-weight: bold; -fx-text-fill: #2c3e50;");

        Label instructionLabel = new Label("Saisissez ou collez le code Java à analyser :");
        instructionLabel.setStyle("-fx-font-size: 12px; -fx-text-fill: #7f8c8d;");

        TextArea codeArea = new TextArea();
        codeArea.setWrapText(true);
        codeArea.setPrefSize(800, 500);
        codeArea.setPromptText("package com.example;\n\npublic class Exemple {\n    public void methode() {\n        // Votre code ici\n    }\n}");
        codeArea.setStyle("-fx-font-family: 'Consolas', 'Courier New', monospace; -fx-font-size: 11px;");

        Button analyzeButton = new Button("Lancer l'analyse");
        analyzeButton.setStyle("-fx-font-size: 14px; -fx-padding: 10px 20px; -fx-background-color: #3498db; -fx-text-fill: white; -fx-background-radius: 5;");
        analyzeButton.setOnMouseEntered(e -> analyzeButton.setStyle("-fx-font-size: 14px; -fx-padding: 10px 20px; -fx-background-color: #2980b9; -fx-text-fill: white; -fx-background-radius: 5;"));
        analyzeButton.setOnMouseExited(e -> analyzeButton.setStyle("-fx-font-size: 14px; -fx-padding: 10px 20px; -fx-background-color: #3498db; -fx-text-fill: white; -fx-background-radius: 5;"));
        
        analyzeButton.setOnAction(e -> {
            String code = codeArea.getText();
            if (code.isEmpty()) {
                Alert alert = new Alert(Alert.AlertType.ERROR, "Veuillez saisir du code Java à analyser.");
                alert.setTitle("Code manquant");
                alert.showAndWait();
                return;
            }
            analyzer.analyzeCode(code, "input.java");
            MetricsCalculator calculator = new MetricsCalculator(
                    analyzer.getClasses(), analyzer.getAllMethods()
            );
            showResults(calculator, analyzer);
            textStage.close();
        });

        root.getChildren().addAll(titleLabel, instructionLabel, codeArea, analyzeButton);

        Scene scene = new Scene(root);
        textStage.setScene(scene);
        textStage.show();
    }

    private void analyzeCallGraph(Stage stage) {
        DirectoryChooser chooser = new DirectoryChooser();
        chooser.setTitle("Sélectionner le répertoire du projet pour le graphe d'appel");
        File projectDir = chooser.showDialog(stage);

        if (projectDir == null || !projectDir.exists()) {
            Alert alert = new Alert(Alert.AlertType.ERROR, "Aucun répertoire sélectionné ou répertoire introuvable.");
            alert.setTitle("Erreur de sélection");
            alert.showAndWait();
            return;
        }

        try {
            CallGraphAnalyzer callGraphAnalyzer = new CallGraphAnalyzer();
            callGraphAnalyzer.analyzeDirectory(projectDir);
            showCallGraph(callGraphAnalyzer);
        } catch (IOException e) {
            Alert alert = new Alert(Alert.AlertType.ERROR, "Erreur lors de l'analyse : " + e.getMessage());
            alert.setTitle("Erreur d'analyse");
            alert.showAndWait();
        }
    }

    private void analyzeCallGraphFromText(Stage stage) {
        Stage textStage = new Stage();
        textStage.setTitle("Graphe d'Appel - Saisie Manuelle");
        try {
            Image logo = new Image(getClass().getResourceAsStream("/logo.png"));
            textStage.getIcons().add(logo);
        } catch (Exception e) {
            // Ignore
        }

        VBox root = new VBox(15);
        root.setPadding(new Insets(20));
        root.setStyle("-fx-background-color: #f8f9fa;");

        Label titleLabel = new Label("Génération de Graphe d'Appel - Code Saisi");
        titleLabel.setStyle("-fx-font-size: 18px; -fx-font-weight: bold; -fx-text-fill: #2c3e50;");

        Label instructionLabel = new Label("Saisissez ou collez le code Java pour générer le graphe d'appel :");
        instructionLabel.setStyle("-fx-font-size: 12px; -fx-text-fill: #7f8c8d;");

        TextArea codeArea = new TextArea();
        codeArea.setWrapText(false);
        codeArea.setPrefSize(800, 500);
        codeArea.setPromptText("package com.example;\n\npublic class Exemple {\n    public void methode1() {\n        methode2();\n    }\n    public void methode2() {\n        // Code\n    }\n}");
        codeArea.setStyle("-fx-font-family: 'Consolas', 'Courier New', monospace; -fx-font-size: 11px;");

        Button analyzeButton = new Button("Générer le graphe d'appel");
        analyzeButton.setStyle("-fx-font-size: 14px; -fx-padding: 10px 20px; -fx-background-color: #3498db; -fx-text-fill: white; -fx-background-radius: 5;");
        analyzeButton.setOnMouseEntered(e -> analyzeButton.setStyle("-fx-font-size: 14px; -fx-padding: 10px 20px; -fx-background-color: #2980b9; -fx-text-fill: white; -fx-background-radius: 5;"));
        analyzeButton.setOnMouseExited(e -> analyzeButton.setStyle("-fx-font-size: 14px; -fx-padding: 10px 20px; -fx-background-color: #3498db; -fx-text-fill: white; -fx-background-radius: 5;"));
        
        analyzeButton.setOnAction(e -> {
            String code = codeArea.getText();
            if (code.isEmpty()) {
                Alert alert = new Alert(Alert.AlertType.ERROR, "Veuillez saisir du code Java à analyser.");
                alert.setTitle("Code manquant");
                alert.showAndWait();
                return;
            }
            try {
                CallGraphAnalyzer callGraphAnalyzer = new CallGraphAnalyzer();
                callGraphAnalyzer.analyzeCode(code, "input.java");
                showCallGraph(callGraphAnalyzer);
                textStage.close();
            } catch (Exception ex) {
                Alert alert = new Alert(Alert.AlertType.ERROR, "Erreur lors de l'analyse : " + ex.getMessage());
                alert.setTitle("Erreur d'analyse");
                alert.showAndWait();
            }
        });
        
        root.getChildren().addAll(titleLabel, instructionLabel, codeArea, analyzeButton);

        Scene scene = new Scene(root);
        textStage.setScene(scene);
        textStage.show();
    }

    private void showCallGraph(CallGraphAnalyzer analyzer) {
        Map<String, Set<String>> callGraph = analyzer.getCallGraph();
        
        if (callGraph.isEmpty()) {
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle("Graphe d'Appel");
            alert.setHeaderText("Aucun appel détecté");
            alert.setContentText("Aucun appel de méthode n'a été trouvé dans le code analysé.");
            alert.showAndWait();
            return;
        }

        // Créer une fenêtre avec visualisation graphique
        Stage graphStage = new Stage();
        graphStage.setTitle("Visualisation du Graphe d'Appel");
        try {
            Image logo = new Image(getClass().getResourceAsStream("/logo.png"));
            graphStage.getIcons().add(logo);
        } catch (Exception e) {
            // Ignore
        }
        graphStage.setWidth(1200);
        graphStage.setHeight(800);

        // Liste des méthodes (nœuds)
        List<String> allMethods = new ArrayList<>(callGraph.keySet());
        Set<String> allCallees = new HashSet<>();
        for (Set<String> callees : callGraph.values()) {
            allCallees.addAll(callees);
        }
        // Ajouter les méthodes appelées qui ne sont pas dans les appelantes
        for (String callee : allCallees) {
            if (!allMethods.contains(callee)) {
                allMethods.add(callee);
            }
        }
        Collections.sort(allMethods);

        // Calculer les colonnes pour la disposition en grille
        int cols = (int) Math.ceil(Math.sqrt(allMethods.size()));

        // Panneau principal avec ScrollPane
        ScrollPane scrollPane = new ScrollPane();
        Pane graphPane = new Pane();
        // Ajuster la taille du panneau selon le nombre de méthodes
        int graphWidth = Math.max(1500, cols * 300);
        int graphHeight = Math.max(1000, (int)Math.ceil((double)allMethods.size() / cols) * 150);
        graphPane.setPrefSize(graphWidth, graphHeight);
        graphPane.setStyle("-fx-background-color: #ffffff;"); // Fond blanc pour plus de clarté
        scrollPane.setContent(graphPane);
        scrollPane.setFitToWidth(true);
        scrollPane.setFitToHeight(true);

        // Positionner les nœuds en grille
        Map<String, Double[]> nodePositions = new HashMap<>();
        double nodeRadius = 25; // Cercles plus petits
        double spacingX = 250; // Espacement horizontal
        double spacingY = 120; // Espacement vertical
        double startX = 80;
        double startY = 80;

        // Dessiner les nœuds
        Map<String, Circle> nodeCircles = new HashMap<>();
        Map<String, Text> nodeLabels = new HashMap<>();
        
        for (int i = 0; i < allMethods.size(); i++) {
            String method = allMethods.get(i);
            int row = i / cols;
            int col = i % cols;
            double x = startX + col * spacingX;
            double y = startY + row * spacingY;
            
            nodePositions.put(method, new Double[]{x, y});
            
            // Cercle pour le nœud
            Circle circle = new Circle(x, y, nodeRadius);
            
            // Colorier différemment selon si c'est un appelant ou non
            boolean hasCalls = callGraph.containsKey(method) && 
                             callGraph.get(method) != null && 
                             !callGraph.get(method).isEmpty();
            if (hasCalls) {
                circle.setFill(Color.LIGHTBLUE);
                circle.setStroke(Color.BLUE);
                circle.setStrokeWidth(2);
            } else {
                circle.setFill(Color.WHITE);
                circle.setStroke(Color.GRAY);
                circle.setStrokeWidth(1.5);
            }
            
            // Texte pour le nom de la méthode (simplifié et centré)
            String shortName = method.length() > 25 ? method.substring(0, 22) + "..." : method;
            // Extraire juste le nom de la méthode (dernier mot après le point)
            String methodName = shortName.contains(".") ? shortName.substring(shortName.lastIndexOf(".") + 1) : shortName;
            Text label = new Text(x - 45, y + nodeRadius + 20, methodName);
            label.setStyle("-fx-font-size: 9px; -fx-font-weight: bold;");
            label.setWrappingWidth(90);
            
            // Ajouter un texte pour la classe (optionnel, en plus petit)
            if (shortName.contains(".")) {
                String className = shortName.substring(0, shortName.lastIndexOf("."));
                className = className.length() > 20 ? "..." + className.substring(className.length() - 17) : className;
                Text classLabel = new Text(x - 45, y + nodeRadius + 35, className);
                classLabel.setStyle("-fx-font-size: 7px; -fx-fill: gray;");
                classLabel.setWrappingWidth(90);
                graphPane.getChildren().add(classLabel);
            }
            
            nodeCircles.put(method, circle);
            nodeLabels.put(method, label);
            graphPane.getChildren().add(circle);
            graphPane.getChildren().add(label);
        }

        // Dessiner les flèches (lignes) entre les nœuds
        List<Line> edges = new ArrayList<>();
        for (Map.Entry<String, Set<String>> entry : callGraph.entrySet()) {
            String caller = entry.getKey();
            Set<String> callees = entry.getValue();
            
            if (callees == null || callees.isEmpty()) continue;
            
            Double[] callerPos = nodePositions.get(caller);
            if (callerPos == null) continue;
            
            for (String callee : callees) {
                Double[] calleePos = nodePositions.get(callee);
                
                // Si la méthode appelée n'est pas dans notre liste, créer un nœud virtuel
                if (calleePos == null) {
                    // Chercher une position proche ou créer un nouveau nœud
                    continue;
                }
                
                // Dessiner une ligne de l'appelant à l'appelé
                double x1 = callerPos[0];
                double y1 = callerPos[1];
                double x2 = calleePos[0];
                double y2 = calleePos[1];
                
                // Calculer la direction et positionner les extrémités sur les cercles
                double dx = x2 - x1;
                double dy = y2 - y1;
                double distance = Math.sqrt(dx * dx + dy * dy);
                
                if (distance > 0) {
                    double ratio = nodeRadius / distance;
                    double lineStartX = x1 + dx * ratio;
                    double lineStartY = y1 + dy * ratio;
                    double lineEndX = x2 - dx * ratio;
                    double lineEndY = y2 - dy * ratio;
                    
                    Line line = new Line(lineStartX, lineStartY, lineEndX, lineEndY);
                    line.setStroke(Color.STEELBLUE);
                    line.setStrokeWidth(1.5); // Lignes plus fines pour plus de clarté
                    line.setOpacity(0.7); // Opacité légèrement augmentée pour meilleure visibilité
                    edges.add(line);
                }
            }
        }
        
        // Ajouter les lignes en arrière-plan (derrière les cercles)
        graphPane.getChildren().addAll(0, edges);

        // Créer un SplitPane avec le graphe et un panneau texte
        SplitPane splitPane = new SplitPane();
        
        // Panneau de texte avec la liste
        VBox textPanel = new VBox(10);
        textPanel.setPadding(new Insets(10));
        textPanel.setPrefWidth(350);
        
        Label textLabel = new Label("Relations d'appel détectées :");
        textLabel.setStyle("-fx-font-weight: bold; -fx-font-size: 14px; -fx-text-fill: #2c3e50;");
        
        TextArea textArea = new TextArea();
        textArea.setEditable(false);
        textArea.setWrapText(true);
        
        StringBuilder sb = new StringBuilder();
        sb.append("GRAPHE D'APPEL\n");
        sb.append("═══════════════\n\n");
        sb.append("Nombre total de méthodes : ").append(allMethods.size()).append("\n\n");
        
        for (String caller : allMethods) {
            Set<String> callees = callGraph.get(caller);
            if (callees != null && !callees.isEmpty()) {
                sb.append("• ").append(caller).append("\n");
                for (String callee : callees) {
                    sb.append("  → ").append(callee).append("\n");
                }
                sb.append("\n");
            }
        }
        
        textArea.setText(sb.toString());
        
        textPanel.getChildren().addAll(textLabel, textArea);
        
        splitPane.getItems().addAll(scrollPane, textPanel);
        splitPane.setDividerPositions(0.7);

        Scene scene = new Scene(splitPane);
        graphStage.setScene(scene);
        graphStage.show();
    }

    private void showResults(MetricsCalculator calculator, CodeAnalyzer analyzer) {
        // Créer une fenêtre dédiée pour les résultats
        Stage resultsStage = new Stage();
        resultsStage.setTitle("Résultats de l'Analyse");
        try {
            Image logo = new Image(getClass().getResourceAsStream("/logo.png"));
            resultsStage.getIcons().add(logo);
        } catch (Exception e) {
            // Ignore
        }

        VBox root = new VBox(15);
        root.setPadding(new Insets(20));
        root.setStyle("-fx-background-color: #f8f9fa;");

        // En-tête avec icône et titre
        HBox headerBox = new HBox(15);
        headerBox.setAlignment(javafx.geometry.Pos.CENTER_LEFT);
        headerBox.setPadding(new Insets(0, 0, 15, 0));
        
        try {
            Image headerIcon = new Image(getClass().getResourceAsStream("/logo.png"), 32, 32, true, true);
            ImageView iconView = new ImageView(headerIcon);
            headerBox.getChildren().add(iconView);
        } catch (Exception e) {
            // Si l'icône n'est pas trouvée, on continue sans
        }
        
        Label titleLabel = new Label("Résultats de l'Analyse");
        titleLabel.setStyle("-fx-font-size: 20px; -fx-font-weight: bold; -fx-text-fill: #2c3e50;");
        headerBox.getChildren().add(titleLabel);

        StringBuilder sb = new StringBuilder();
        sb.append("RÉSULTATS DE L'ANALYSE\n");
        sb.append("═══════════════════════\n\n");

        sb.append("1. Nombre de classes : ").append(calculator.getNumberOfClasses()).append("\n");
        sb.append("2. Nombre de lignes de code : ").append(analyzer.getTotalLinesOfCode()).append("\n");
        sb.append("3. Nombre total de méthodes : ").append(calculator.getTotalNumberOfMethods()).append("\n");
        sb.append("4. Nombre total de packages : ").append(calculator.getTotalNumberOfPackages()).append("\n");
        sb.append("5. Moyenne de méthodes par classe : ").append(String.format("%.2f", calculator.getAverageMethodsPerClass())).append("\n");
        sb.append("6. Moyenne de lignes par méthode : ").append(String.format("%.2f", calculator.getAverageLinesPerMethod())).append("\n");
        sb.append("7. Moyenne d'attributs par classe : ").append(String.format("%.2f", calculator.getAverageAttributesPerClass())).append("\n");

        // Question 8
        sb.append("\n8. Les 10% des classes avec le plus grand nombre de méthodes:\n");
        List<ClassInfo> topMethods = calculator.getTop10PercentClassesByMethods();
        if (topMethods.isEmpty()) sb.append("  Aucune classe trouvée.\n");
        else topMethods.forEach(c -> sb.append("  - ").append(c.getFullName())
                .append(" (").append(c.getMethodCount()).append(" méthodes)\n"));

        // Question 9
        sb.append("\n9. Les 10% des classes avec le plus grand nombre d'attributs:\n");
        List<ClassInfo> topAttributes = calculator.getTop10PercentClassesByAttributes();
        if (topAttributes.isEmpty()) sb.append("  Aucune classe trouvée.\n");
        else topAttributes.forEach(c -> sb.append("  - ").append(c.getFullName())
                .append(" (").append(c.getAttributeCount()).append(" attributs)\n"));

        // Question 10
        sb.append("\n10. Classes présentes dans les deux catégories précédentes:\n");
        List<ClassInfo> inBoth = calculator.getClassesInBothCategories();
        if (inBoth.isEmpty()) sb.append("  Aucune classe trouvée.\n");
        else inBoth.forEach(c -> sb.append("  - ").append(c.getFullName())
                .append(" (").append(c.getMethodCount()).append(" méthodes, ")
                .append(c.getAttributeCount()).append(" attributs)\n"));

        // Question 11: valeur X
        TextInputDialog dialog = new TextInputDialog("3");
        dialog.setTitle("Analyse avancée");
        dialog.setHeaderText("Classes avec un nombre spécifique de méthodes");
        dialog.setContentText("Entrez le nombre minimum de méthodes :");
        dialog.showAndWait().ifPresent(xStr -> {
            try {
                int x = Integer.parseInt(xStr);
                List<ClassInfo> classesWithMoreThanX = calculator.getClassesWithMoreThanXMethods(x);
                sb.append("\n=== Classes avec plus de ").append(x).append(" méthodes ===\n");
                if (classesWithMoreThanX.isEmpty()) sb.append("Aucune classe trouvée.\n");
                else classesWithMoreThanX.forEach(c -> sb.append(" - ").append(c.getFullName())
                        .append(" (").append(c.getMethodCount()).append(" méthodes)\n"));
            } catch (NumberFormatException ex) {
                sb.append("\nValeur invalide.\n");
            }
        });

        // Question 12: top 10% méthodes par lignes
        sb.append("\n12. Les 10% des méthodes avec le plus de lignes par classe:\n");
        Map<String, List<MethodInfo>> topMethodsPerClass = calculator.getTop10PercentMethodsByLinesPerClass();
        if (topMethodsPerClass.isEmpty()) sb.append("  Aucune méthode trouvée.\n");
        else topMethodsPerClass.forEach((cls, methods) -> {
            sb.append("  Classe: ").append(cls).append("\n");
            methods.forEach(m -> sb.append("    - ").append(m.getName())
                    .append(" (").append(m.getLineCount()).append(" lignes)\n"));
        });

        // Question 13: max paramètres
        sb.append("\n13. Nombre maximal de paramètres parmi toutes les méthodes: ").append(calculator.getMaxParametersInMethods()).append("\n");

        TextArea textArea = new TextArea(sb.toString());
        textArea.setEditable(false);
        textArea.setWrapText(true);
        textArea.setStyle("-fx-font-family: 'Consolas', 'Courier New', monospace; -fx-font-size: 11px;");
        
        // Faire en sorte que le TextArea prenne tout l'espace disponible
        VBox.setVgrow(textArea, javafx.scene.layout.Priority.ALWAYS);

        root.getChildren().addAll(headerBox, textArea);

        Scene scene = new Scene(root, 800, 600);
        resultsStage.setScene(scene);
        resultsStage.show();
    }

    public static void main(String[] args) {
        launch(args);
    }
}
