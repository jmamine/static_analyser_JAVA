package com.example.ASTDemo;

import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.layout.*;
import javafx.stage.DirectoryChooser;
import javafx.stage.Stage;

import java.io.File;
import java.io.IOException;
import java.util.List;
import java.util.Map;

public class AppUI extends Application {

    private CodeAnalyzer analyzer = new CodeAnalyzer();

    @Override
    public void start(Stage primaryStage) {
        primaryStage.setTitle("Analyseur de Métriques Java");

        // Logo de l'application
        try {
            Image logo = new Image(getClass().getResourceAsStream("/logo.png"));
            primaryStage.getIcons().add(logo);
        } catch (Exception e) {
            System.out.println("Logo non trouvé !");
        }

        // Choix initial 1 ou 2
        ChoiceDialog<String> choiceDialog = new ChoiceDialog<>("1", "1", "2");
        choiceDialog.setTitle("Analyse du projet");
        choiceDialog.setHeaderText("Choisissez l'option:");
        choiceDialog.setContentText("1. Analyser un projet\n2. Coller du code Java");
        choiceDialog.showAndWait().ifPresent(choice -> {
            if (choice.equals("1")) {
                analyzeProject(primaryStage);
            } else if (choice.equals("2")) {
                analyzeText(primaryStage);
            } else {
                System.out.println("Option invalide !");
            }
        });
    }

    private void analyzeProject(Stage stage) {
        DirectoryChooser chooser = new DirectoryChooser();
        chooser.setTitle("Choisir le dossier du projet Java");
        File projectDir = chooser.showDialog(stage);

        if (projectDir == null || !projectDir.exists()) {
            Alert alert = new Alert(Alert.AlertType.ERROR, "Projet non sélectionné ou introuvable !");
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
            Alert alert = new Alert(Alert.AlertType.ERROR, "Erreur : " + e.getMessage());
            alert.showAndWait();
        }
    }

    private void analyzeText(Stage stage) {
        Stage textStage = new Stage();
        textStage.setTitle("Coller le code Java à analyser");

        TextArea codeArea = new TextArea();
        codeArea.setWrapText(true);
        codeArea.setPrefSize(800, 600);

        Button analyzeButton = new Button("Analyser");
        analyzeButton.setOnAction(e -> {
            String code = codeArea.getText();
            if (code.isEmpty()) {
                Alert alert = new Alert(Alert.AlertType.ERROR, "Veuillez coller du code !");
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

        VBox root = new VBox(10, codeArea, analyzeButton);
        root.setPadding(new Insets(15));

        Scene scene = new Scene(root);
        textStage.setScene(scene);
        textStage.show();
    }

    private void showResults(MetricsCalculator calculator, CodeAnalyzer analyzer) {
        StringBuilder sb = new StringBuilder();
        sb.append("=== RÉSULTATS DE L'ANALYSE ===\n\n");

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
        dialog.setTitle("Question 11");
        dialog.setHeaderText("Entrez la valeur de X pour trouver les classes avec plus de X méthodes");
        dialog.setContentText("X :");
        dialog.showAndWait().ifPresent(xStr -> {
            try {
                int x = Integer.parseInt(xStr);
                List<ClassInfo> classesWithMoreThanX = calculator.getClassesWithMoreThanXMethods(x);
                sb.append("\n=== Classes avec plus de ").append(x).append(" méthodes ===\n");
                if (classesWithMoreThanX.isEmpty()) sb.append("Aucune classe trouvée.\n");
                else classesWithMoreThanX.forEach(c -> sb.append(" - ").append(c.getFullName())
                        .append(" (").append(c.getMethodCount()).append(" méthodes)\n"));
            } catch (NumberFormatException ex) {
                sb.append("\nValeur X invalide.\n");
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

        // Pop-up finale
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Résultats de l'analyse");
        alert.setHeaderText(null);

        TextArea textArea = new TextArea(sb.toString());
        textArea.setEditable(false);
        textArea.setWrapText(true);
        textArea.setPrefWidth(600);
        textArea.setPrefHeight(400);

        alert.getDialogPane().setContent(textArea);
        alert.showAndWait();
    }

    public static void main(String[] args) {
        launch(args);
    }
}
