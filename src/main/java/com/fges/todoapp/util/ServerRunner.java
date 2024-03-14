package com.fges.todoapp.util;

import com.fges.todoapp.todo.Todo;
import com.fges.todoapp.todo.TodoProvider;
import fr.anthonyquere.dumbcrud.DummyCrudEndpoint;

import java.io.IOException;
import java.util.Scanner;
public class ServerRunner {
    private final int port ;
    private final String domainName ;

    public ServerRunner(int port, String domainName) {
        this.port = port;
        this.domainName = domainName;
    }

    public void startServer(TodoProvider provider) throws IOException {
        boolean stopRequested = false;
        Scanner scanner = new Scanner(System.in);

        // Lancement du serveur
        DummyCrudEndpoint<Todo> endpoint = new DummyCrudEndpoint<>(this.domainName, provider, Todo.class);

        endpoint.run(port);
        System.out.println("Serveur démarré sur le port " + port + ".");
        System.out.println("'stop' pour arrêter le serveur");

        while (!stopRequested) {
            String input = scanner.nextLine();
            if (input.equals("stop")) {
                stopRequested = true;
            }
        }

        System.out.println("Serveur arrêté.");
    }
}

