### Ajout d'une nouvelle commande :

1. **Création d'une nouvelle classe implémentant l'interface `Command`.**

   Tout d'abord, pour ajouter une nouvelle commande à notre application, nous devons créer une classe qui implémente l'interface `Command`. Cette classe représentera la logique spécifique de la nouvelle commande. Elle doit implémenter la méthode `execute` qui prend en paramètre une liste d'arguments positionnels et retourne un entier. Voici un exemple de la structure de cette classe :

    ```java
    public class NewCommand implements Command {
        @Override
        public int execute(List<String> positionalArgs) throws IOException {
            // Logique de la nouvelle commande
            return 0;
        }
    }
    ```

2. **Ajout d'une entrée dans la classe `CommandFactory` pour associer le nom de la commande à sa création.**

   Ensuite, pour que notre application puisse reconnaître et exécuter cette nouvelle commande, nous devons ajouter une entrée dans la classe `CommandFactory`. Cette entrée associe le nom de la commande (par exemple, "newcommand") à une fonction lambda qui crée une instance de la classe `NewCommand`. Voici comment ajouter cette entrée :

    ```java
    commandMap.put(
        "newcommand", 
        (fileHandler, filePath, outputFileHandler, outputPath, taskState) -> new NewCommand()
    );
    ```

3. **Ajout de l'option de ligne de commande pour la nouvelle commande dans la méthode `buildOptions` de la classe `App`.**

   Enfin, pour que l'utilisateur puisse utiliser cette nouvelle commande en ligne de commande, nous devons ajouter une option correspondante dans la méthode `buildOptions` de la classe `App`. Cette option spécifie le nom de la commande ainsi qu'une description facultative. Voici comment ajouter cette option :

    ```java
    cliOptions.addOption(
        "n", 
        "newcommand", false, "Description de la nouvelle commande");
    ```

### 📜 Ajout d'une nouvelle source de données basée sur un fichier :

1. **Création d'une nouvelle classe implémentant l'interface `StorageHandler`** :
   - Tout d'abord, l'utilisateur crée une nouvelle classe qui implémente l'interface `StorageHandler`.
   - Cette classe devrait fournir une implémentation pour les méthodes de lecture et d'écriture des données 
   dans un fichier.
   - Par exemple :
     ```java
     public class NewFileDataSourceHandler implements StorageHandler {
         @Override
         public void write(List<Todo> todos, Path filePath) throws IOException {
             // Implémente la logique pour écrire les todos dans un fichier
         }

         @Override
         public List<Todo> read(Path filePath) throws IOException {
             // Implémente la logique pour lire les todos à partir d'un fichier
             return null;
         }
     }
     ```

2. **Ajout de l'implémentation dans la classe `StorageFactory`** :
   - Dans la classe `StorageFactory`, l'utilisateur ajoute une nouvelle entrée dans la map pour associer le type de la nouvelle
   source de données à son gestionnaire.
   - Par exemple :
     ```java
     registry.put("newfiledatasource", new NewFileDataSourceHandler());
     ```

3. **Utilisation de la nouvelle source de données** :
   - Lorsque l'utilisateur souhaite utiliser la nouvelle source de données dans l'application, il peut l'obtenir à 
   partir de la `StorageFactory` en spécifiant son type.
   - Par exemple :
     ```java
     StorageHandler inputHandler = fileRegistry.detectHandler("newfiledatasource");
     ```

### 🗂️ Ajout d'une nouvelle source de données non basée sur un fichier :

1. **Création d'une nouvelle classe implémentant l'interface `StorageHandler`** :
   - De la même manière que pour une source de données basée sur un fichier, l'utilisateur crée une nouvelle classe implémentant 
   l'interface `StorageHandler`.
   - Cette classe devrait fournir une implémentation pour les méthodes de lecture et d'écriture des données dans 
   une source de données non basée sur un fichier.
   - Par exemple :
     ```java
     public class NewNonFileDataSourceHandler implements StorageHandler {
         @Override
         public void write(List<Todo> todos, Path filePath) throws IOException {
             // Implémente la logique pour écrire les todos dans une source de données non basée sur un fichier
         }

         @Override
         public List<Todo> read(Path filePath) throws IOException {
             // Implémente la logique pour lire les todos à partir d'une source de données non basée sur un fichier
             return null;
         }
     }
     ```

2. **Ajout de l'implémentation dans la classe `StorageFactory`** :
   - l'utilisateur ajoute une nouvelle entrée dans la map de la classe `StorageFactory` pour associer le type de la nouvelle 
   source de données à son gestionnaire.
   - Par exemple :
     ```java
     registry.put("newnondatasource", new NewNonFileDataSourceHandler());
     ```

3. **Utilisation de la nouvelle source de données** :
   - Lors de l'utilisation de la nouvelle source de données dans l'application, l'utilisateur la récupère à partir 
   de la `StorageFactory` en spécifiant son type.
   - Par exemple :
     ```java
     StorageHandler dataSourceHandler = dataSourceFactory.detectHandler("newnondatasource");
     ```

### 📝 Ajout d'un nouvel attribut à un Todo :

1. **Ajout d'un champ dans la classe `Todo`** :
   - L'utilisateur modifie la classe `Todo` en ajoutant un nouveau champ pour le nouvel attribut.
   - Par exemple :
     ```java
     private String newAttribute;
     ```

2. **Mise à jour des constructeurs** :
   - Met à jour les constructeurs de la classe `Todo` pour prendre en compte le nouvel attribut.
   - Par exemple :
     ```java
     public Todo(String name, TaskState taskState, String newAttribute) {
         this.name = name;
         this.taskState = taskState;
         this.newAttribute = newAttribute;
     }
     ```

3. **Utilisation du nouvel attribut** :
   - L'utilisateur utilise le nouvel attribut dans le reste du code où nécessaire.
   - Par exemple, dans la classe `TodoProvider`, si l'utilisateur veut manipuler ce nouvel attribut lors de l'ajout d'une 
   nouvelle tâche à faire :
     ```java
     public void add(TodoDTO todoDTO) throws Exception {
         Todo todo = new Todo(todoDTO.getContent(), todoDTO.getStatus(), todoDTO.getNewAttribute());
         // Logique pour ajouter la nouvelle tâche à faire
     }
     ```
