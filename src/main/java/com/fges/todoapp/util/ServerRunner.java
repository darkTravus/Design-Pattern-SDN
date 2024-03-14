package com.fges.todoapp.util;

import com.fges.todoapp.dto.TodoDTO;
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
        DummyCrudEndpoint<TodoDTO> endpoint = new DummyCrudEndpoint<>(this.domainName, provider, TodoDTO.class);

        endpoint.run(port);
        System.err.println("Serveur démarré sur le port " + port + ".");
        System.err.println("'stop' pour arrêter le serveur");

        while (!stopRequested) {
            String input = scanner.nextLine();
            if (input.equals("stop")) {
                stopRequested = true;
            }
        }

        System.err.println("Serveur arrêté.");
    }
}

