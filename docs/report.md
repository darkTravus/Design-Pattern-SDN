# L3 design pattern report

- **Firstname**: DJOUE
- **Lastname**: ARIEL EMMANUEL KOUADIO


> Add your thoughts on every TP bellow, everything is interresting but no need to right a book.
> 
> Keep it short simple and efficient:
> 
> - What you did and why
> - What helped you and why
> - What did you find difficult
> - What did not help you
> - What did you need to change
> - Anything relevant
> 
> Add a link to schemas describing your architecture (UML or not but add a legend)
> 
> Remember: it is ok to make mistakes, you will have time to spot them later.
> 
> Fill free to contact me if needed.

---

>  ### TP1
> - La première tâche du script est de s'occuper des options passer en ligne de commande et de renvoyer une erreur 
> en fonction du résultat. On va d'abord séparer l'analyse des arguments de la gestion des erreurs avec la classe
> ```java
> static class CommandLineProcessor implements CommandProcessor {}
> ```
> - On va maintenant implémenter une fonction spécifique pour récupérer l'argument passer en ligne de commande
> ```java
> static class MyCommandProcessor {}
> ```
> - Plus bas dans le code, je me suis rendu compte qu'il fallait aussi récupérer le reste des arguments de la ligne de
> commande 😅. J'ai donc ajouté l'interface
> ```java
> interface ArgumentValidator {}
> ```
> et une classe
> ```java
> static class PositionalArgumentValidator implements ArgumentValidator {}
> ```
> afin de respecter le principe SOLID. Le but est de vérifier s'il y'a des arguments présents.
> 
>   J'ai par conséquent ajouté quelqus instructions à la méthode
> ```java
> public int processCommand(CommandLine cmd) {}
> ```
> de la classe
> ```java
> static class CommandLineProcessor implements CommandProcessor {}
> ```
> pour gérer les erreurs.
> 
> - Pour ce qui est de la récupération du contenu du fichier, j'ai suivi le même principe en séparant la vérification
> du chemin de la lecture du fichier respectivement avec les classes suivantes :
> ```java
> static class PathValidator {}
> ```
> ```java
> static class FileReader {}
> ```
