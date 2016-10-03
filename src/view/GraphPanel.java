package view;

import interfaces.Observable;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Paint;
import java.awt.event.ComponentEvent;
import java.awt.event.ComponentListener;
import java.awt.geom.Point2D;

import javax.swing.BorderFactory;
import javax.swing.JPanel;
import javax.swing.border.TitledBorder;

import org.apache.commons.collections15.Transformer;

import edu.uci.ics.jung.algorithms.layout.CircleLayout;
import edu.uci.ics.jung.algorithms.layout.Layout;
import edu.uci.ics.jung.graph.Graph;
import edu.uci.ics.jung.graph.SparseMultigraph;
import edu.uci.ics.jung.graph.util.EdgeType;
import edu.uci.ics.jung.visualization.VisualizationViewer;
import edu.uci.ics.jung.visualization.control.ViewScalingControl;
import edu.uci.ics.jung.visualization.decorators.ToStringLabeller;
import edu.uci.ics.jung.visualization.renderers.Renderer.VertexLabel.Position;

import object.AttackGraph;
import object.OrientedEdge;
import object.Vertex;

public class GraphPanel extends JPanel implements Observable{
	
	public GraphPanel(int width, int height, AttackGraph ag){
		this.setPreferredSize(new Dimension(width, height));
		this.setSize(width, height);
		TitledBorder title= new TitledBorder(BorderFactory.createMatteBorder(
                1, 5, 1, 1, Color.GRAY), "");
		
		
		if(ag!=null){
			title= new TitledBorder(BorderFactory.createMatteBorder(
	                1, 5, 1, 1, Color.GRAY), "Attack graph");
			
			Graph<String, String> g = new SparseMultigraph<String, String>();
			
			for (Vertex v : ag.getVertexes()) {
				
				g.addVertex((String)v.getTag());			
			}
			
			for (OrientedEdge edge : ag.getEdges()) {
				
				g.addEdge(""+edge.getFrom().getTag()+edge.getTo().getTag(), edge.getFrom().getTag(),edge.getTo().getTag(), EdgeType.DIRECTED);
				
			}
			
	        Layout<String, String> layout = new CircleLayout(g);		
			layout.setSize(new Dimension((width/4)*3, (height/4)*3));
			
			VisualizationViewer<String,String> vv =
					new VisualizationViewer<String,String>(layout);
			vv.setPreferredSize(new Dimension((width/4)*3, (height/4)*3));
			vv.setBackground(Color.white);
			// Setup up a new vertex to paint transformer...
			Transformer<String, Paint> vertexPaint = new Transformer<String,Paint>() {
			public Paint transform(String i) {
			return Color.GREEN;
			}

			
			};
			Transformer<String,Paint> vertexPaint2 = new Transformer<String,Paint>() {
						
						@Override
						public Paint transform(String arg0) {
							// TODO Auto-generated method stub
							return Color.RED;
						}
						};
			
			vv.getRenderContext().setVertexFillPaintTransformer(vertexPaint);	
			vv.getRenderContext().setEdgeDrawPaintTransformer(vertexPaint2);
			vv.getRenderContext().setVertexLabelTransformer(new ToStringLabeller());
			vv.getRenderer().getVertexLabelRenderer().setPosition(Position.CNTR);
			ViewScalingControl scales =new ViewScalingControl();
			scales.scale(vv, (float) 1.25, new Point2D.Double(3, 3));
			
			this.setLayout(new BorderLayout());
			add(BorderLayout.CENTER,vv);
		}
		setBorder(title);
	}

	@Override
	public void setView(int compl) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void setView(String request) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void setView(AttackGraph g) {
		// TODO Auto-generated method stub
		
	}

	

}
