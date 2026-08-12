# WhatsDiagnostic

Application Android minimale qui collecte des informations locales et prépare un e‑mail pour contacter le support WhatsApp. Ne tente pas d'accéder aux serveurs WhatsApp ni d'automatiser des vérifications côté serveur.

Build / run
1. Ouvre le projet dans Android Studio (ou importe le répertoire).
2. Android Studio va synchroniser Gradle. Si nécessaire, mets à jour le plugin Gradle / Kotlin dans les dialogues.
3. Run -> app pour installer sur un appareil connecté ou un émulateur.

Générer un APK release
1. Crée un keystore (Build -> Generate Signed Bundle / APK).
2. Dans app/build.gradle, configure signingConfigs si tu veux assembler via ./gradlew assembleRelease.
3. Signe l’APK via Android Studio ou apksigner.

Fonctionnement
- "Collecter diagnostic" écrit un fichier diagnostic dans cacheDir/diagnostic.txt.
- "Préparer e‑mail..." ouvre un chooser d’e‑mail avec support@whatsapp.com, prérempli et le fichier en pièce jointe (l’utilisateur doit confirmer l’envoi).
- "Ouvrir page WhatsApp Play Store" ouvre Play Store.
- "Lancer WhatsApp" tente de lancer l’app si installée.

Respect de la vie privée
- L’app ne collecte pas de messages ni de tokens d’utilisateur. Évite d’inclure des données sensibles avant d’envoyer un e‑mail.

Notes légales
- N’utilise pas cette app pour contourner des blocages ou pour des activités contraires aux conditions d’utilisation de WhatsApp.
