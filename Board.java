/**
 * Jogo dos Oito
 * Daniel Filipe - 201106486
 * Tiago Duarte - 201105251
 **/

public class Board {
    int [][] cells;
    int [] zeroAt;
    Board parent;
    int cost;
    int nodeLevel;
    char operation;

    // Construtores
    public Board() {
        this.cells = new int[3][3];
        this.zeroAt = new int[2];
        this.parent = null;
        cost = 0;
        nodeLevel = 0;
        operation = ' ';

        this.cells[0][0] = 1;
        this.cells[0][1] = 2;
        this.cells[0][2] = 3;
        this.cells[1][0] = 8;
        this.cells[1][1] = 0;
        this.zeroAt[0] = 1;
        this.zeroAt[1] = 1;
        this.cells[1][2] = 4;
        this.cells[2][0] = 7;
        this.cells[2][1] = 6;
        this.cells[2][2] = 5;
    }

    public Board(int[][] configInicial) {
        this.cells = new int[3][3];
        this.zeroAt = new int[2];
        this.parent = null;
        cost = 0;
        nodeLevel = 0;
        operation = ' ';

        for (int row = 0; row < 3; row++) {
            for (int column = 0; column < 3; column++) {
                this.cells[row][column] = configInicial[row][column];

                if (this.cells[row][column] == 0) {
                    this.zeroAt[0] = row;
                    this.zeroAt[1] = column;
                }
            }
        }
    }

    // Verificar paridade
    public int checkParity() {
        int parity = 0;

        for (int row = 0; row < 3; row++) {
            for (int column = 0; column < 3; column++) {
                parity += getHigher(row, column);
            }
        }

        return parity;
    }
    private int getHigher(int targetRow, int targetCol) {
        int parity = 0;
        int val = cells[targetRow][targetCol];

        if (val == 0) return 0;

        for (int row = 0; row < 3; row++) {
            for (int column = 0; column < 3; column++) {
                if (cells[row][column] > val)
                    parity++;
                else if (cells[row][column] == val)
                    return parity;
            }
        }

        return parity;
    }

    public boolean equals(Board obj) {
        for (int row = 0; row < 3; row++) {
            for (int column = 0; column < 3; column++) {
                if (this.cells[row][column] != obj.cells[row][column])
                    return false;
            }
        }

        return true;
    }

    /*
        Operações:
            N -> Mover para cima (Norte)
            E -> Mover para a esquerda (Este)
            O -> Mover para a direita (Oeste)
            S -> Mover para baixo (Sul)
     */

    public Board moveUp() {
        if (this.zeroAt[0] == 0) return null;

        int oldVal = this.cells[this.zeroAt[0]-1][this.zeroAt[1]];
        this.cells[this.zeroAt[0]-1][this.zeroAt[1]] = 0;
        this.cells[this.zeroAt[0]][this.zeroAt[1]] = oldVal;
        this.zeroAt[0] = this.zeroAt[0] - 1;
        this.operation = 'N';

        return this;
    }

    public Board moveDown() {
        if (this.zeroAt[0] == 2) return null;

        int oldVal = this.cells[this.zeroAt[0]+1][this.zeroAt[1]];
        this.cells[this.zeroAt[0]+1][this.zeroAt[1]] = 0;
        this.cells[this.zeroAt[0]][this.zeroAt[1]] = oldVal;
        this.zeroAt[0] = this.zeroAt[0] + 1;
        this.operation = 'S';

        return this;
    }

    public Board moveLeft() {
        if (this.zeroAt[1] == 0) return null;

        int oldVal = this.cells[this.zeroAt[0]][this.zeroAt[1]-1];
        this.cells[this.zeroAt[0]][this.zeroAt[1]-1] = 0;
        this.cells[this.zeroAt[0]][this.zeroAt[1]] = oldVal;
        this.zeroAt[1] = this.zeroAt[1] - 1;
        this.operation = 'W';

        return this;
    }

    public Board moveRight() {
        if (this.zeroAt[1] == 2) return null;

        int oldVal = this.cells[this.zeroAt[0]][this.zeroAt[1]+1];
        this.cells[this.zeroAt[0]][this.zeroAt[1]+1] = 0;
        this.cells[this.zeroAt[0]][this.zeroAt[1]] = oldVal;
        this.zeroAt[1] = this.zeroAt[1] + 1;
        this.operation = 'E';

        return this;
    }

    // TESTS ONLY
    public void print() {
        for (int line = 0; line < 3; line++) {
            for (int column = 0; column < 3; column++) {
                System.out.print("[" + cells[line][column] + "]");
            }

            System.out.println();
        }
    }
}
