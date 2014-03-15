/**
 * Jogo dos Oito
 * Daniel Filipe - 201106486
 * Tiago Duarte - 201105251
 **/

import java.util.LinkedList;
import java.util.List;
import java.util.Scanner;

/*
    - Criar Boards com a configuração inicil e final
    - Verificar se existe solução (com paridade)
    - Resolver através dos algoritmos:
        - NÃO INFORMADAS
            - Busca em profundidade (BP)
            - Busca em largura (BL)
            - Busca em profundidade iterativa (BPI)
        - INFORMADAS
            - Busca gulosa (GULOSA)
            - Busca A* (ASTAR)
*/
public class Game {
    public static Board configInicial, configFinal;
    public static int maximumNodes;

    public static void main (String [] args) {
        Scanner kb = new Scanner(System.in);

        initializeData();

        if ((configInicial.checkParity() % 2 == 0 && configFinal.checkParity() % 2 == 0) || (configInicial.checkParity() % 2 != 0 && configFinal.checkParity() % 2 != 0)) {
            Board solution = null;

            System.out.println("Escolha um algoritmo");
            System.out.println("1: Busca em largura");
            System.out.println("2: Busca em profundidade");
            System.out.println("3: Busca em profundidade iterativa");
            System.out.println("4: Busca A estrela (A*)");
            System.out.println("5: Busca gulosa");

            int chosenAlgorithm = kb.nextInt();
            long startTime = System.currentTimeMillis();

            System.out.println("A calcular, aguarde por favor...");
            if (chosenAlgorithm == 1) {
                solution = generalSearchAlgorithm("BL", -1);
            } else if (chosenAlgorithm == 2) {
                solution = generalSearchAlgorithm("BP", -1);
            } else if (chosenAlgorithm == 3) {
                int limit = 0;
                while (solution == null) {
                    solution = generalSearchAlgorithm("BPI", limit);
                    limit++;
                }
            } else if (chosenAlgorithm == 4) {
                solution = generalSearchAlgorithm("ASTAR", -1);
            } else if (chosenAlgorithm == 5) {
                solution = generalSearchAlgorithm("GULOSA", -1);
            }

            long stopTime = System.currentTimeMillis();
            long elapsedTime = stopTime - startTime;

            if (solution == null)
                System.out.println("ERRO : Algo correu mal, solução não foi encontrada.");
            else {
                printSolution(solution);
                System.out.println("Tempo de execução: " + elapsedTime + " ms");
                System.out.println("Número máximo de nós em memória: " + maximumNodes);
            }
        } else {
            System.out.println("ERRO : Não é possível chegar da configuração inicial dada à configuração final.");
        }
    }

    public static void initializeData() {
        int [][] config1 = {
                {3, 2, 4},
                {5, 1, 8},
                {0, 6, 7}
        };

        int [][] config2 = {
                {5, 7, 2},
                {6, 0, 3},
                {8, 4, 1}
        };

        /* Para manter as nossa sanidade mental, criamos uma configuração em que a
           solução se encontra no nível 8 da árvore de resolução. */
        int [][] config3 = {
                {1, 3, 0},
                {7, 2, 8},
                {6, 5, 4}
        };

        configInicial = new Board(config3);
        configFinal = new Board();

        System.out.println("Configuração inicial");
        configInicial.print();
        System.out.println();
        System.out.println("Configuração final");
        configFinal.print();

        System.out.println();
        System.out.println("Paridade (configInicial): " + configInicial.checkParity());
        System.out.println("Paridade (configFinal): " + configFinal.checkParity());
    }

    public static Board generalSearchAlgorithm(String queueingFunction, int limit) {
        maximumNodes = 0;
        LinkedList<Board> queue = new LinkedList<Board>();
        queue.push(configInicial);

        while (!queue.isEmpty()) {
            Board node = queue.pop();

            if (node.equals(configFinal)) {
                return node;
            }

            if (limit != -1) {
                if (node.nodeLevel <= limit) {
                    List<Board> possiblePlays = generatePlays(node);
                    SolFinder.enqueue(queue, possiblePlays, queueingFunction);
                }
            } else {
                List<Board> possiblePlays = generatePlays(node);
                SolFinder.enqueue(queue, possiblePlays, queueingFunction);
            }

            if (queue.size() > maximumNodes) maximumNodes = queue.size();
        }

        return null;
    }

    public static List<Board> generatePlays (Board obj) {
        LinkedList<Board> plays = new LinkedList<Board>();
        int [][] cells = obj.cells;

        Board movedUp = new Board(cells);
        movedUp.parent = obj;
        movedUp.nodeLevel = obj.nodeLevel + 1;
        Board movedDown = new Board(cells);
        movedDown.parent = obj;
        movedDown.nodeLevel = obj.nodeLevel + 1;
        Board movedLeft = new Board(cells);
        movedLeft.parent = obj;
        movedLeft.nodeLevel = obj.nodeLevel + 1;
        Board movedRight = new Board(cells);
        movedRight.parent = obj;
        movedRight.nodeLevel = obj.nodeLevel + 1;

        if (movedUp.moveUp() != null) plays.push(movedUp);
        if (movedDown.moveDown() != null) plays.push(movedDown);
        if (movedLeft.moveLeft() != null) plays.push(movedLeft);
        if (movedRight.moveRight() != null) plays.push(movedRight);

        return plays;
    }

    public static void printSolution (Board obj) {
        int steps = 0;
        String path = "";
        Board parentNode = obj.parent;

        path = " -> CONFIG_FINAL".concat(path);
        while (parentNode != null) {
            if (parentNode.parent != null) {
                path = (" -> " + parentNode.operation).concat(path);
            } else {
                path = "CONFIG_INICIAL".concat(path);
            }
            parentNode = parentNode.parent;
            steps++;
        }

        System.out.println(path);
        System.out.println("Número de passos: " + steps);
    }
}