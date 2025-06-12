package Ihm;
import Metier.CheminCritique;
import Metier.Tache;
import exFinal.Controleur;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.ArrayList;
import java.util.Collections;
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
		private boolean enDate;

		/**
		 * Constructeur principal prenant un contrôleur.
		 *
		 * @param ctrl le contrôleur qui fournit les tâches
		 */
		public MPMGrapheAuto(Controleur ctrl)
		{
			this.nbCol           =  0;
			this.etapePlusTarMax = -1;
			this.etapePlusTotMax =  1;
			this.ctrl            =  ctrl;
			this.enDate          = false;
			this.cheminsCritiques = this.ctrl.getCheminCritiques();

			this.majIhm();

			resetEtape();
			
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
				if (x >= n.getX() && x <= n.getX() + boxSize &&
					y >= n.getY() && y <= n.getY() + boxSize)
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

			dicTaches.clear();

			while(dicTaches.size() != taches.size())//boucle infini
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

				Noeud n = new Noeud(t.getNom(), t.getDatePlusTot(), t.getDatePlusTard(), dicTaches.get(t.getNom()), false, ctrl);
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
			int startX = 5;
			int startY = 375; // <- Ta demande
			int distX = boxSize + 100;
			int distY = boxSize;

			// Construction du dictionnaire colonne -> nœuds
			HashMap<Integer, ArrayList<Noeud>> dicColNoeud = new HashMap<>();

			for (int col = 0; col < nbCol; col++)
			{
				ArrayList<Noeud> ListNoeudDsCol = new ArrayList<>();
				for (Noeud n : noeuds) 
				{
					if (n.getCol() == col)
						ListNoeudDsCol.add(n);
				}
				dicColNoeud.put(col,ListNoeudDsCol);	
			}

			// Calcul des 'lig' pour chaque colonne
			for (Integer col : dicColNoeud.keySet())
			{
				ArrayList<Noeud> lstCol       = dicColNoeud.get(col);
				ArrayList<Noeud> lstNGrpFait  = new ArrayList<>();
				
				if (col == 0)
						lstCol.get(0).setLig(0);
				else
				{
					// tri des nœuds de la colonne col
        			ArrayList<Noeud> lstColOrga = triCol(col, dicColNoeud);
					int cptSup = 0;
					int cptInf = 0;

					for (Noeud n : lstColOrga)
					{
						System.out.println(n.getNom());
            			ArrayList<Noeud> lstNSvtDePre = new ArrayList<>();

						ArrayList<Noeud> lstNoeudPreActuel  = getPrecedents(n);

						for (Noeud nPreActuel : lstNoeudPreActuel)
							lstNSvtDePre = getNoeudsMemeSvt(nPreActuel, lstNSvtDePre, lstNoeudPreActuel);
						
						if(estNewGrp(lstNSvtDePre,lstNGrpFait,col))
						{
							cptSup = -1;
							for (Noeud nGrp : lstNSvtDePre)
							{
								if (nGrp.getCol() == col)
									cptInf ++;
							}
							cptInf = (int) Math.ceil(cptInf/2);
							
						}
						
						System.out.println("cptInf : " + cptInf);

						if (!lstNSvtDePre.isEmpty()) 
						{
							int milieu = 0;

							 // 4) position de n dans la liste
							int emplacementN = lstNSvtDePre.indexOf(n);
							double tailleGrpDeN = lstNSvtDePre.size();

							int lig = 0;
							if (lstNSvtDePre.size()%2 != 0)
							{
								if ( n == lstNSvtDePre.get((int)tailleGrpDeN/2))
								{
									if (n.getNbPre()%2 == 0)
										milieu += getMil(n);
									else
										milieu += getMilieu(getPrecedents(n));
									lig += milieu;
									System.out.println("Noeud : " + n.getNom());
									System.out.println("Lig = " + lig + " = " + milieu +"/2\n");
								}
								else
								{
									if (emplacementN > Math.ceil(tailleGrpDeN/2))
									{
										System.out.println("hihihihihih");
										lig += getMilieu(getPrecedents(n)) + (((emplacementN+1) - Math.ceil(tailleGrpDeN/2)) + cptSup++);
									}
									else
									{
										System.out.println("ohohoohohoho");
										lig += getMilieu(getPrecedents(n)) + (((emplacementN+1) - Math.ceil(tailleGrpDeN/2)) - cptInf--);
									}
									System.out.println("Noeud : " + n.getNom());
									System.out.println("Lig = " + lig + " = " + getMilieu(getPrecedents(n)) + "+ (((" + emplacementN+"+1)" +" - "+ Math.ceil(tailleGrpDeN/2)+") - "+ cptInf-- +")");
									System.out.println("cptInf : "+ cptInf);
									System.out.println("cptSup : "+ cptSup  + "\n");
								}
							}
							else
							{
								if (emplacementN >= tailleGrpDeN/2)
								{	
									lig += getMil(n) + ((emplacementN - (tailleGrpDeN/2)) + ++cptSup) + 1;
								}
								else
								{
									lig += getMil(n) + ((emplacementN - (tailleGrpDeN/2)) - --cptInf) ;
								}
								System.out.println("Noeud : " + n.getNom());
								System.out.println("Lig = " + lig + " = " + getMil(n) + " - (" + emplacementN +" - ("+ tailleGrpDeN/2 + "))");
							}

							lstNGrpFait.add(n);
							
							n.setLig(lig);
						}
						
					}
				}
			}

			// Affectation des coordonnées x/y pour l'affichage
			for (Noeud n : noeuds) 
			{
				int x = startX + n.getCol() * distX;
				int y = startY + n.getLig() * distY;
				n.setX(x);
				n.setY(y);
			}

		}

		//doit renvoyer vrais si dans lstGrpFait il y a tous les noeuds de lstNGrp dans la colonne indiqué
		public boolean estNewGrp(ArrayList<Noeud> lstNGrp, ArrayList<Noeud> lstNGrpFait, int col) 
		{
			for (Noeud n : lstNGrp) 
			{
				if (n.getCol() == col && lstNGrpFait.contains(n)) 
					return false;
			}
			return true;
		}

		public ArrayList<Noeud> triCol(int colActuel, HashMap<Integer, ArrayList<Noeud>> dicColNoeud)
		{
			ArrayList<Noeud> noeudsActuels    = dicColNoeud.get(colActuel);

			for (int i = 0; i < noeudsActuels.size() - 1; i++)
			{
				if (i % 2 == 0)
				{
					// Si position paire, list[i] doit être < list[i+1]
					if (noeudsActuels.get(i).getNbPre() > noeudsActuels.get(i + 1).getNbPre())
						Collections.swap(noeudsActuels, i, i + 1);
				}
				else
				{
					// Si position impaire, list[i] doit être > list[i+1]
					if (noeudsActuels.get(i).getNbPre() < noeudsActuels.get(i + 1).getNbPre())
						Collections.swap(noeudsActuels, i, i + 1);
				}
			}

			for (int i = 0; i < noeudsActuels.size() - 1; i++)
			{

				int milieu1 = getMil(noeudsActuels.get(i));
				int milieu2 = getMil(noeudsActuels.get(i + 1));

				if (milieu2 < milieu1)
					Collections.swap(noeudsActuels, i, i + 1);
			}

			return noeudsActuels;
		}

		private int getMil(Noeud n)
		{
			int sumLig  = 0;
			for (Noeud nPreActuel : getPrecedents(n))
				sumLig  += nPreActuel.getLig();
			return sumLig/getPrecedents(n).size();
		}

		private int getMilieu(ArrayList<Noeud> lstNoeudPreActuel)
		{
			if (lstNoeudPreActuel == null || lstNoeudPreActuel.isEmpty())
				return 0;

			// Étape 1 : copier et trier manuellement la liste par insertion
			ArrayList<Noeud> copieTriee = new ArrayList<>();

			for (Noeud n : lstNoeudPreActuel)
			{
				int i = 0;
				while (i < copieTriee.size() && n.getLig() > copieTriee.get(i).getLig()) 
				{
					i++;
				}
				copieTriee.add(i, n); // insertion à la bonne position
			}

			// Étape 2 : trouver l'élément du milieu
			int indexMilieu = copieTriee.size() / 2;

			int i = 0;
			for (Noeud n : copieTriee) 
			{
				if (i == indexMilieu) 
				{
					return n.getLig();
				}
				i++;
			}

			return 0; // fallback (ne devrait jamais arriver)
		}


		private ArrayList<Noeud> getSuivants(Noeud n)
		{
			Tache t = ctrl.chercherTacheParNom(n.getNom());
			ArrayList<Tache> lstTSvt = t.getSuivants();
			ArrayList<Noeud> lstNSvt = new ArrayList<>();

			for (Tache tSvt : lstTSvt)
			{
				lstNSvt.add(getNoeud(tSvt.getNom()));
			}

			return lstNSvt;
		}

		private ArrayList<Noeud> getPrecedents(Noeud n)
		{
			Tache t = ctrl.chercherTacheParNom(n.getNom());
			ArrayList<Tache> lstTPre = t.getPrecedents();
			ArrayList<Noeud> lstNPre = new ArrayList<>();

			for (Tache tSvt : lstTPre)
			{
				lstNPre.add(getNoeud(tSvt.getNom()));
			}

			return lstNPre;
		}

		/**
		 * Retourne le nœud par son nom.
		 */
		private Noeud getNoeud(String nom)
		{
			for (Noeud n : noeuds)
			{
				if (n.getNom().equals(nom))
					return n;
			}
			return null;
		}

		public ArrayList<Noeud> getNoeudsMemeSvt(Noeud n, ArrayList<Noeud> lstNSvt, ArrayList<Noeud> lstPre)
		{
			ArrayList<Tache> lstTSvt = ctrl.chercherTacheParNom(n.getNom()).getSuivants();

			for (Tache tacheSvt : lstTSvt) 
			{
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

				int x1 = noeudDepart .getX()  + boxSize;
				int y1 = noeudDepart .getY()  + boxSize / 2;

				int x2 = noeudArrivee.getX();
				int y2 = noeudArrivee.getY()  + boxSize / 2;

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
				/*
				 * Dessin du rectangle
				 */
				g2.setColor(Color.WHITE);
				g2.fillRect(n.getX(), n.getY(), boxSize, boxSize); // fond blanc
				g2.setColor(Color.BLACK);

				//Affichage du Chemin Critique
				if (n.getEstChemin() && this.cheminActif)
				{
					g2.setColor(Color.GRAY);
					g2.fillRect(n.getX(), n.getY(), boxSize, boxSize);
				}
				g2.setColor(Color.BLACK);

				g2.drawRect(n.getX(), n.getY(), boxSize, boxSize);
				g2.drawLine(n.getX(), n.getY() + boxSize / 2, n.getX() + boxSize, n.getY() + boxSize / 2);
				g2.drawLine(n.getX() + boxSize / 2, n.getY() + boxSize / 2, n.getX() + boxSize / 2, n.getY() + boxSize);

				//Dessin du Nom
				FontMetrics fm = g2.getFontMetrics();
				int textWidth = fm.stringWidth(n.getNom());
				g2.drawString(n.getNom(), n.getX() + (boxSize - textWidth) / 2, n.getY() + 20);

				
			}

			
				for (Noeud n : this.noeuds)
				{
					Tache tache = ctrl.chercherTacheParNom(n.getNom());


					String dtPlusTot = this.enDate ? tache.ajouterJours(ctrl.getDateDebut(), tache.getDatePlusTot())  : n.getTot() + "" ;
					String dtPlusTar = this.enDate ? tache.ajouterJours(ctrl.getDateDebut(), tache.getDatePlusTard()) :n.getTard() + "" ;
					int ecartTot     = this.enDate ? 3 : 15;
					int ecartTar     = this.enDate ? 38 : 25;

					//Affichage au Plus tot
					if (n.getCol() < this.etapePlusTotMax)
					{
						g2.setColor  (new Color(30, 189, 120));
						g2.drawString(dtPlusTot, n.getX() + ecartTot, n.getY() + boxSize - 10);
					}

					//Affichage au Plus tard
					g2.setColor  (Color.RED);
					if (n.getCol() > this.etapePlusTarMax )
					{
						g2.drawString(dtPlusTar, n.getX() + boxSize - ecartTar, n.getY() + boxSize - 10);

					}
				}
					
				g2.setColor  (Color.BLACK);


		}

		public void activerChemin() 
		{
			this.cheminActif = !cheminActif;
			repaint(); // Redessine le panel
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
			if (etapePlusTarMax > this.nbCol) this.etapePlusTarMax = this.nbCol;
			this.etapePlusTarMax--;
			repaint();
			
			if (this.etapePlusTarMax < 0) 
					return false;
				
			return true;
		}

		public void resetEtape() 
		{
			this.etapePlusTarMax = this.nbCol;
			this.etapePlusTotMax = 1;
			this.cheminActif     = false;
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


		private void afficherInfosNoeud(Noeud n) 
		{
			Tache tache = null;
			for (Tache t : ctrl.getTaches()) {
				if (t.getNom().equals(n.getNom())) {
					tache = t;
					break;
				}
			}
			if (tache == null) return;

			FrameNoeudInfo frameInfo = new FrameNoeudInfo(tache, this.ctrl);
		}

		public String getInfos() 
		{
			String sRet = "";
			for (Noeud n : this.noeuds)
			{
				sRet += n.getNom();
				//Ajouter les précedents des noeuds 
				
				for (Arc a : this.arcs)
				{
					if (a.from.equals(n.getNom()))
					{
						sRet += "|" + a.poids;
						break;
					}
				}
				sRet += "|";
				//les precedents
				for (Tache t : ctrl.getTaches())
				{
					if (t.getNom().equals(n.getNom()))
					{
						for (Tache tPre : t.getPrecedents())
						{
							sRet += tPre.getNom()  + ",";
						}
					}
				}
				//enlèver le dernier ,
				sRet = sRet.substring(0, sRet.length() - 1);
				sRet += "|" + n.getX() + "," + n.getY() + "\n";
			}
			return sRet;
        }

		public void setEnDate()
		{
			if (!this.enDate)
            {
                this.enDate = true;
                this.majIhm();
            }
            else
            {
                this.enDate = false;
                this.majIhm();
            }
            this.majIhm();
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
						offsetX = e.getX() - noeudSelectionne.getX();
						offsetY = e.getY() - noeudSelectionne.getY();
					}
				}
			}
				
			public void mouseReleased(MouseEvent e)
			{
				noeudSelectionne = null;
			}

			public void mouseDragged(MouseEvent e)
			{
				if (noeudSelectionne != null && SwingUtilities.isLeftMouseButton(e))
				{
					// Mettre à jour les coordonnées du nœud
					noeudSelectionne.setX(e.getX() - offsetX);
					noeudSelectionne.setY(e.getY() - offsetY);

					repaint();
				}
			}
		}
}