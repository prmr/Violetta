package ca.mcgill.cs.stg.jetuml.commands;

import static org.junit.Assert.*;

import java.awt.geom.Point2D;
import java.lang.reflect.Field;
import java.util.ArrayList;

import org.junit.Before;
import org.junit.Test;

import ca.mcgill.cs.stg.jetuml.diagrams.ClassDiagramGraph;
import ca.mcgill.cs.stg.jetuml.graph.ClassNode;
import ca.mcgill.cs.stg.jetuml.graph.Graph;
import ca.mcgill.cs.stg.jetuml.graph.Node;

public class TestDeleteNodeCommand {
    private Graph aGraph;
    private Field aNeedsLayout;
    private Field aNodesToBeRemoved;
    private Node aNode;
    private ArrayList<Node> aRootNodes;
    private DeleteNodeCommand aDeleteNodeCommand;
    private boolean hasExecuted = false;
    
    @Before
    public void setUp() throws Exception
    {
        aGraph = new ClassDiagramGraph();
        aRootNodes = (ArrayList<Node>) aGraph.getRootNodes();
        aNode = new ClassNode();
        aGraph.addNode(aNode, new Point2D.Double());
        aNeedsLayout = aGraph.getClass().getSuperclass().getDeclaredField("aNeedsLayout");
        aNeedsLayout.setAccessible(true);
        aNodesToBeRemoved = aGraph.getClass().getSuperclass().getDeclaredField("aNodesToBeRemoved");
        aNodesToBeRemoved.setAccessible(true);
        aDeleteNodeCommand = new DeleteNodeCommand(aGraph, aNode);  
    }
    
    @Test
    public void testExecute() {
        aDeleteNodeCommand.execute();
        hasExecuted = true;
        try {
            ArrayList<Node> aListNodesToBeRemoved = (ArrayList<Node>)(aNodesToBeRemoved.get(aGraph));
            assertTrue(aListNodesToBeRemoved.contains((Node)aNode));
        } catch (IllegalArgumentException e1) {
            e1.printStackTrace();
        } catch (IllegalAccessException e1) {
            e1.printStackTrace();
        }
        try {
            assertTrue((boolean)aNeedsLayout.get(aGraph));
        } catch (IllegalArgumentException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        } 
    }
    
    @Test
    public void testUndo() {
        if (!hasExecuted)
        {
            aDeleteNodeCommand.execute();
        }
        int numOfNodes = aRootNodes.size();
        aDeleteNodeCommand.undo();
        assertEquals(numOfNodes+1, aRootNodes.size());

        try {
            assertTrue((boolean)aNeedsLayout.get(aGraph));
        } catch (IllegalArgumentException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }
    }
}