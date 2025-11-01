# Script de test pour ASTDemo
# Usage: .\test.ps1

Write-Host "=== Test ASTDemo ===" -ForegroundColor Green
Write-Host ""

# Étape 1: Compilation
Write-Host "1. Compilation du projet..." -ForegroundColor Yellow
mvn clean compile
if ($LASTEXITCODE -ne 0) {
    Write-Host "❌ Erreur de compilation!" -ForegroundColor Red
    exit 1
}
Write-Host "✅ Compilation réussie" -ForegroundColor Green
Write-Host ""

# Étape 2: Exécution des tests unitaires
Write-Host "2. Exécution des tests unitaires..." -ForegroundColor Yellow
mvn test
if ($LASTEXITCODE -ne 0) {
    Write-Host "❌ Des tests ont échoué!" -ForegroundColor Red
    exit 1
}
Write-Host "✅ Tous les tests ont réussi" -ForegroundColor Green
Write-Host ""

# Étape 3: Génération du rapport de test
Write-Host "3. Rapport de test généré dans target/surefire-reports/" -ForegroundColor Cyan
Write-Host ""

Write-Host "=== Tests terminés avec succès! ===" -ForegroundColor Green
Write-Host ""
Write-Host "Pour exécuter l'application manuellement:" -ForegroundColor Cyan
Write-Host "  mvn exec:java -Dexec.mainClass=`"com.example.ASTDemo.App`"" -ForegroundColor White
Write-Host ""



