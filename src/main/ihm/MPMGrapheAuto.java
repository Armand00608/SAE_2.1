package main.ihm;
import java.awt.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import javax.swing.*;

import main.metier.Tache;
import main.Controleur;

/**
 * Cette classe représente un graphe MPM affiché avec Swing,
 * basé sur les tâches du contrôleur. Les positions sont calculées
 * selon les dates au plus tôt des tâches.
 */
public class MPMGrapheAuto extends JPanel
{

    /**
     * Représente un nœud du graphe.
     */
    static class Noeud
    {
        String nom;
        int tot, tard;
        int x, y;
		int col;

        Noeud(String nom, int tot, int tard, int col)
        {
            this.nom = nom;
            this.tot = tot;
            this.tard = tard;
			this.col = col;
        }

		public int getCol()    {return col;}
		public String getNom() {return nom;}
    }

    /**
     * Représente un arc (dépendance) entre deux tâches.
     */
    static class Arc
    {
        String from, to;
        int poids;

        Arc(String from, String to, int poids)
        {
            this.from = from;
            this.to = to;
            this.poids = poids;
        }
    }

    private final List<Noeud> noeuds = new ArrayList<>();
    private final List<Arc> arcs = new ArrayList<>();
    private final int boxSize = 80;
    private final Controleur ctrl;

    /**
     * Constructeur principal prenant un contrôleur.
     *
     * @param ctrl le contrôleur qui fournit les tâches
     */
    public MPMGrapheAuto(Controleur ctrl)
    {
        this.ctrl = ctrl;
        initialiserNoeudsArcs();
        creerGraphe();
    }

    /**
     * Crée les nœuds et arcs à partir des tâches du contrôleur.
     */
    private void initialiserNoeudsArcs()
    {
		ArrayList<Tache> taches  = new ArrayList<>(this.ctrl.getTaches());
		HashMap<String, Integer> dicTaches = new HashMap<>();

		while(dicTaches.size() != taches.size())
		{
			for (Tache t : taches)
			{
				int colPlusGrd = -1;

				if (t.getPrecedents().isEmpty())
				{
					dicTaches.put(t.getNom(), 0);
				}
				else
				{
					for (Tache pred : t.getPrecedents())
					{
						String nomTache = pred.getNom();
						if(dicTaches.containsKey(nomTache) && dicTaches.get(nomTache)+1 > colPlusGrd)
							colPlusGrd = dicTaches.get(nomTache)+1;
					}
					if(colPlusGrd != -1)
					{	
						dicTaches.put(t.getNom(), colPlusGrd);
					}
				}
			}
		}

		for (Tache t : taches)
        {
			noeuds.add(new Noeud(t.getNom(), t.getDatePlusTot(), t.getDatePlusTard(), dicTaches.get(t.getNom())));

			for (Tache tachePrc : t.getPrecedents())
			{
				arcs.add(new Arc(tachePrc.getNom(), t.getNom(), tachePrc.getDuree()));
			}
        }
    }

    /**
     * Place les nœuds horizontalement selon leur date au plus tôt,
     * et verticalement pour éviter les chevauchements.
     */
    private void creerGraphe()
	{
		int startX = 100;
		int startY = 100;
		int distX = boxSize + 60;
		int distY = boxSize + 40;

		// 1. Trouver la colonne maximale
		int maxCol = 0;
		for (Noeud n : noeuds)
			if (n.col > maxCol)
				maxCol = n.col;

		// 2. Initialiser compteur de lignes par colonne
		List<Integer> compteurLignes = new ArrayList<>();
		for (int i = 0; i <= maxCol; i++)
			compteurLignes.add(0);

		// 3. Positionner les noeuds
		for (Noeud n : noeuds)
		{
			int col = n.col;
			int ligne = compteurLignes.get(col);

			n.x = startX + col * distX;
			n.y = startY + ligne * distY;

			compteurLignes.set(col, ligne + 1); // mettre à jour le compteur
		}
	}





    /**
     * Affiche le graphe (arcs et nœuds) dans la fenêtre graphique.
     */
    protected void paintComponent(Graphics g)
    {
        super.paintComponent(g);
        Graphics2D g2 = (Graphics2D) g;
        g2.setFont(new Font("SansSerif", Font.PLAIN, 14));

        for (Arc arc : arcs)
        {
            Noeud from = getNoeud(arc.from);
            Noeud to = getNoeud(arc.to);

            if (from == null || to == null)
            {
                continue;
            }

            int x1 = from.x + boxSize;
            int y1 = from.y + boxSize / 2;
            int x2 = to.x;
            int y2 = to.y + boxSize / 2;

            g2.drawLine(x1, y1, x2, y2);
            g2.drawString(String.valueOf(arc.poids), (x1 + x2) / 2, (y1 + y2) / 2 - 5);
            drawArrowHead(g2, x1, y1, x2, y2);
        }

        for (Noeud n : noeuds)
        {
            g2.drawRect(n.x, n.y, boxSize, boxSize);
            g2.drawLine(n.x, n.y + boxSize / 2, n.x + boxSize, n.y + boxSize / 2);
            g2.drawLine(n.x + boxSize / 2, n.y + boxSize / 2, n.x + boxSize / 2, n.y + boxSize);

            FontMetrics fm = g2.getFontMetrics();
            int textWidth = fm.stringWidth(n.nom);
            g2.drawString(n.nom, n.x + (boxSize - textWidth) / 2, n.y + 20);
            g2.drawString(String.valueOf(n.tot), n.x + 15, n.y + boxSize - 10);
            g2.drawString(String.valueOf(n.tard), n.x + boxSize - 25, n.y + boxSize - 10);
        }
    }

    /**
     * Retourne le nœud par son nom.
     */
    private Noeud getNoeud(String nom)
    {
        for (Noeud n : noeuds)
        {
            if (n.nom.equals(nom))
                return n;
        }
        return null;
    }

    /**
     * Dessine une flèche orientée entre deux points.
     */
    private void drawArrowHead(Graphics2D g2, int x1, int y1, int x2, int y2)
    {
        double phi = Math.toRadians(25);
        int barb = 10;
        double dy = y2 - y1, dx = x2 - x1;
        double theta = Math.atan2(dy, dx);
        for (int j = 0; j < 2; j++)
        {
            double rho = theta + (j == 0 ? phi : -phi);
            int x = (int) (x2 - barb * Math.cos(rho));
            int y = (int) (y2 - barb * Math.sin(rho));
            g2.drawLine(x2, y2, x, y);
        }
    }
}
