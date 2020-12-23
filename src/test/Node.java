package test;

import java.util.ArrayList;

public class Node {
    private Node parent; // the parent of the node
    public ArrayList<Node> children; // the children of the node
    private int nodeDepth; // the level at this node is
    private int[] nodeMove; // an array that contains the new x, y, of the Player and the dice
    private Board nodeBoard; // a copy of the board
    private double nodeEvaluation; // the points the player gets if he make the move in the direction the dice says.

    //constructors
    public Node() {
        parent = null;
        children = new ArrayList<Node>();
        nodeEvaluation = 0;
        nodeDepth = 0;
        nodeMove = new int[3];
    }

    public Node(Node parent, Board nodeBoard, int x, int y, int dice) {
        this.parent = parent;
        children = new ArrayList<Node>();
        this.nodeBoard = nodeBoard;
        nodeMove = new int[] {x, y, dice};

        nodeEvaluation = 0;
        nodeDepth = 0;
    }

    //Getters & Setters
    public Node getParent() {
        return parent;
    }

    public void setParent(Node parent) {
        this.parent = parent;
    }

    public int getNodeDepth() {
        return nodeDepth;
    }

    public void setNodeDepth(int nodeDepth) {
        this.nodeDepth = nodeDepth;
    }

    public int[] getNodeMove() {
        return nodeMove;
    }

    public void setNodeMove(int[] nodeMove) {
        this.nodeMove = nodeMove;
    }

    public Board getNodeBoard() {
        return nodeBoard;
    }

    public void setNodeBoard(Board nodeBoard) {
        this.nodeBoard = nodeBoard;
    }

    public double getNodeEvaluation() {
        return nodeEvaluation;
    }

    public void setNodeEvaluation(double nodeEvaluation) {
        this.nodeEvaluation = nodeEvaluation;
    }
}
