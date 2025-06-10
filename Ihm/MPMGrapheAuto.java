package exFinal.Ihm;

import exFinal.Controleur;
import exFinal.Metier.CheminCritique;
import exFinal.Metier.Tache;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import javax.swing.*;

/**
 * Cette classe représente un graphe MPM affiché avec Swing,
 * basé sur les tâches du contrôleur. Les positions sont calculées
 * selon les dates au plus tôt des tâches.
 */
	public class MPMGrapheAuto extends JPanel
	{
		private boolean cheminActif = false;
		private ArrayList<CheminCritique> cheminsCritiques = new ArrayList<>();

		private final List<Noeud> noeuds = new ArrayList<>();
		private final List<Arc> arcs = new ArrayList<>();
		private final int boxSize = 80;
		private final Controleur ctrl;
		
		// Variables pour le glisser-déposer
		private Noeud noeudSelectionne = null;
		private int offsetX, offsetY;

		private int etapePlusTotMax;
		private int etapePlusTarMax;
		private int nbCol;

		/**
		 * Constructeur principal prenant un contrôleur.
		 *
		 * @param ctrl le contrôleur qui fournit les tâches
		 */
		public MPMGrapheAuto(Controleur ctrl)
		{
			this.etapePlusTarMax = -1;
			this.etapePlusTotMax =  1;
			this.ctrl            =  ctrl;
			this.cheminsCritiques = this.ctrl.getCheminCritiques();

			this.majIhm();

			this.etapePlusTarMax = this.nbCol;
			
			// Ajoute des listeners
			GereSouris GereSouris = new GereSouris();

			this.addMouseListener      ( GereSouris );
			this.addMouseMotionListener( GereSouris );
		}
		
		/**
		 * Retourne le nœud à la position donnée, ou null si aucun.
		 */
		private Noeud getNoeudAPosition(int x, int y)
		{
			for (Noeud n : noeuds)
			{
				if (x >= n.x && x <= n.x + boxSize && 
					y >= n.y && y <= n.y + boxSize)
				{
					return n;
				}
			}
			return null;
		}

		public void majIhm()
		{
			this.cheminsCritiques = this.ctrl.getCheminCritiques();
			this.initialiserNoeudsArcs();
			this.creerGraphe();

			if (this.etapePlusTotMax == this.nbCol )
				this.etapePlusTotMax = this.nbCol + 1;

			repaint();
		}

		/**
		 * Crée les nœuds et arcs à partir des tâches du contrôleur.
		 */
		private void initialiserNoeudsArcs()
		{
			noeuds.clear();
			arcs.clear();

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
						 this.nbCol = colPlusGrd;
					}
				}
			}


			for (Tache t : taches)
			{
				Noeud n = new Noeud(t.getNom(), t.getDatePlusTot(), t.getDatePlusTard(), dicTaches.get(t.getNom()), false);
				noeuds.add(n);
				for (CheminCritique ch : this.cheminsCritiques)
				{
					for (Tache tacheCh : ch.getTachesCritiques())
					{
						if (tacheCh.getNom().equals(t.getNom()))
						{
							n.setEstChemin(true);
						}
					}
				}

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
			int distX  = boxSize + 60;
			int distY  = boxSize + 40;

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


		public ArrayList<Noeud> getNoeudsMemeSvt(Noeud n, ArrayList<Noeud> lstNSvt, ArrayList<Noeud> lstPre) 
		{
			ArrayList<Tache> lstTSvt = ctrl.chercherTacheParNom(n.getNom()).getSuivants();

			for (Tache tacheSvt : lstTSvt) {
				Noeud noeudSvt = getNoeud(tacheSvt.getNom());

				// Obtenir les prédécesseurs du suivant
				ArrayList<Tache> tachesPrecedentes = ctrl.chercherTacheParNom(tacheSvt.getNom()).getPrecedents();
				ArrayList<Noeud> noeudsPrecedents = new ArrayList<>();
				for (Tache t : tachesPrecedentes) {
					noeudsPrecedents.add(getNoeud(t.getNom()));
				}

				// Vérifier si tous les noeuds de lstPre sont dans les prédécesseurs du suivant
				if (contientTous(noeudsPrecedents, lstPre)) {
					if (!lstNSvt.contains(noeudSvt)) {
						lstNSvt.add(noeudSvt);
					}
				}
			}

			return lstNSvt;
		}


		private boolean contientTous(ArrayList<Noeud> conteneur, ArrayList<Noeud> contenu) 
		{
			for (Noeud n : contenu) {
				if (!conteneur.contains(n)) {
					return false;
				}
			}
			return true;
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
                Noeud noeudDepart  = getNoeud(arc.from);
                Noeud noeudArrivee = getNoeud(arc.to);        

                int x1 = noeudDepart.x  + boxSize;
                int y1 = noeudDepart.y  + boxSize / 2;

                int x2 = noeudArrivee.x;
                int y2 = noeudArrivee.y + boxSize / 2;

                // Point du milieu
                int milieuX = (x1 + x2) / 2;
                int milieuY = (y1 + y2) / 2;
                
                // Calculer la direction de la ligne
                double deltaX = x2 - x1;
                double deltaY = y2 - y1;
                double longueur = Math.sqrt(deltaX * deltaX + deltaY * deltaY);
                
                // Direction unitaire
                double directionX = deltaX / longueur;
                double directionY = deltaY / longueur;
                
                // Espace avant et après le texte
                int espace = 10;
                
                // Premier bout de ligne (s'arrête avant le texte)
                int arretX = (int)(milieuX - espace * directionX);
                int arretY = (int)(milieuY - espace * directionY);
                
                // Deuxième bout de ligne (reprend après le texte)
                int repriseX = (int)(milieuX + espace * directionX);
                int repriseY = (int)(milieuY + espace * directionY);

                g2.setColor(Color.BLUE);
                // Première ligne
                g2.drawLine(x1, y1, arretX, arretY);
                // Deuxième ligne
                g2.drawLine(repriseX, repriseY, x2, y2);
                
                // Texte au milieu
                g2.setColor(new Color(201, 87, 30));
                g2.drawString(String.valueOf(arc.poids), milieuX - 5, milieuY + 5);
                
                // Flèche
                drawArrowHead(g2, repriseX, repriseY, x2, y2);
                g2.setColor(Color.BLACK);
            }
            for (Noeud n : this.noeuds)
            {
                if (n.estChemin && this.cheminActif)
                {
                    g2.setColor(Color.GRAY);
                    g2.fillRect(n.x, n.y, boxSize, boxSize);
                }
                g2.setColor(Color.BLACK);

                g2.drawRect(n.x, n.y, boxSize, boxSize);
                g2.drawLine(n.x, n.y + boxSize / 2, n.x + boxSize, n.y + boxSize / 2);
                g2.drawLine(n.x + boxSize / 2, n.y + boxSize / 2, n.x + boxSize / 2, n.y + boxSize);

                FontMetrics fm = g2.getFontMetrics();
                int textWidth = fm.stringWidth(n.nom);
                g2.drawString(n.nom, n.x + (boxSize - textWidth) / 2, n.y + 20);
                
            }

            
                for (Noeud n : this.noeuds) 
                {
                    
                    //Affichage au Plus tot
                    if (n.col < this.etapePlusTotMax) 
                    {
                        g2.setColor  (new Color(30, 189, 120));
                        g2.drawString(String.valueOf(n.tot), n.x + 15, n.y + boxSize - 10);
                    }
                    
                    //Affichage au Plus tard
                    g2.setColor  (Color.RED);
                    if (n.col > this.etapePlusTarMax ) 
                    {
                        g2.drawString(String.valueOf(n.tard), n.x + boxSize - 25, n.y + boxSize - 10);
                        
                    }                    
                }
                
                g2.setColor  (Color.BLACK);

        }

		public void activerChemin() 
		{
			this.cheminActif = !cheminActif;
			repaint(); // Redessine le panell
		}

		public boolean  AugmenterEtapePlusTotMax()
		{
			++this.etapePlusTotMax;
			repaint();

			if (this.etapePlusTotMax >  this.nbCol) 
				return false;
				
			
			return true;
		}
		
		public boolean AugmenterEtapePlusTarMax()
		{
			this.etapePlusTarMax--;
			repaint();
			
			if (this.etapePlusTarMax < 0) 
					return false;
				
			return true;
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
			int barb   = 10;
			double dy  = y2 - y1, dx = x2 - x1;
			double theta = Math.atan2(dy, dx);
			for (int j = 0; j < 2; j++)
			{
				double rho = theta + (j == 0 ? phi : -phi);
				int x = (int) (x2 - barb * Math.cos(rho));
				int y = (int) (y2 - barb * Math.sin(rho));
				g2.setColor(Color.BLUE);
				g2.drawLine(x2, y2, x, y);

			}
		}

		private ArrayList<Noeud> getNoeudsSvtDePreActuel(Noeud nPreActuel, ArrayList<Noeud> lstNSvtDePre, List<Noeud> lstNoeudPreActuel) {
			throw new UnsupportedOperationException("Not supported yet.");
		}


		private void afficherInfosNoeud(Noeud n) 
		{
			Tache tache = null;
			for (Tache t : ctrl.getTaches()) {
				if (t.getNom().equals(n.nom)) {
					tache = t;
					break;
				}
			}
			if (tache == null) return;

			FrameNoeudInfo frameInfo = new FrameNoeudInfo(tache, this.ctrl);
			frameInfo.setVisible(true);
		}

		public String getInfos() 
		{
			String sRet = "";
			for (Noeud n : this.noeuds)
			{
				sRet += n.nom;
				//Ajouter les précedents des noeuds 
				
				for (Arc a : this.arcs)
				{
					if (a.from.equals(n.nom))
					{
						sRet += "|" + a.poids;
						break;
					}
				}
				sRet += "|";
				//les precedents
				for (Tache t : ctrl.getTaches())
				{
					if (t.getNom().equals(n.nom))
					{
						for (Tache tPre : t.getPrecedents())
						{
							sRet += tPre.getNom()  + ",";
						}
					}
				}
				//enlèver le dernier ,
				sRet = sRet.substring(0, sRet.length() - 1);
				sRet += "|" + n.x + "," + n.y + "\n";
			}
			return sRet;
        }

		private class GereSouris extends MouseAdapter
		{
			public void mousePressed(MouseEvent e)
			{
				// Chercher le nœud cliqué
				noeudSelectionne = getNoeudAPosition(e.getX(), e.getY());
				
				if (noeudSelectionne != null)
				{
					if (SwingUtilities.isRightMouseButton(e)) {
						afficherInfosNoeud(noeudSelectionne);
					} else {
						// Calculer l'offset pour un déplacement fluide
						offsetX = e.getX() - noeudSelectionne.x;
						offsetY = e.getY() - noeudSelectionne.y;
					}
				}
			}
				
			public void mouseReleased(MouseEvent e)
			{
				noeudSelectionne = null;
			}
			
			public void mouseDragged(MouseEvent e)
			{
				if (noeudSelectionne != null)
				{
					// Mettre à jour les coordonnées du nœud
					noeudSelectionne.x = e.getX() - offsetX;
					noeudSelectionne.y = e.getY() - offsetY;
					
					repaint();
				}
			}
	}
}
