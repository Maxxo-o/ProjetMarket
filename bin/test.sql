DROP TABLE Produit CASCADE CONSTRAINTS;
DROP TABLE Client CASCADE CONSTRAINTS;
DROP TABLE Magasin CASCADE CONSTRAINTS;
DROP TABLE TypesDeProfil CASCADE CONSTRAINTS;
DROP TABLE Commande CASCADE CONSTRAINTS;
DROP TABLE Preferer CASCADE CONSTRAINTS;
DROP TABLE Stocker CASCADE CONSTRAINTS;
DROP TABLE Appartenir_Type CASCADE CONSTRAINTS;
DROP TABLE Delivrer CASCADE CONSTRAINTS;
DROP TABLE Repartir CASCADE CONSTRAINTS;
DROP TABLE Composer CASCADE CONSTRAINTS;
DROP TABLE Correspondre CASCADE CONSTRAINTS;




-- Table Produit
CREATE TABLE Produit (
ProduitId VARCHAR(50) PRIMARY KEY,
NomProd VARCHAR(100) NOT NULL,
PrixAuKg DECIMAL(10, 2) NULL,
PrixUnitaire DECIMAL(10, 2) ,
Poids DECIMAL(10, 2) NULL,
Nutriscore CHAR(1) NOT NULL CHECK (Nutriscore IN ('A', 'B', 'C', 'D', 'E')),
Categorie VARCHAR(50) NOT NULL,
Marque VARCHAR(50) NOT NULL
);
-- Table Client
CREATE TABLE Client (
ClientId VARCHAR(50) PRIMARY KEY,
Nom VARCHAR(100) NOT NULL,
Prenom VARCHAR(100) NOT NULL,
Age INT NOT NULL,
Sexe CHAR(1) NOT NULL CHECK (Sexe IN ('M', 'F', 'O')),
Adresse VARCHAR(200) NOT NULL
);
-- Table Magasin
CREATE TABLE Magasin (
MagId INT PRIMARY KEY,
NomMag VARCHAR(100) NOT NULL,
AdresseMag VARCHAR(200) NOT NULL,
CodePostalMag CHAR(5) NOT NULL,
VilleMag VARCHAR(100) NOT NULL
);
-- Table TypesDeProfil
CREATE TABLE TypesDeProfil (
ProfilId INT PRIMARY KEY ,
NomProfil VARCHAR(50) NOT NULL
);
-- Table Commande
CREATE TABLE Commande (
CommandeId VARCHAR(50) PRIMARY KEY,
DateCommande DATE NOT NULL,
MagId INT NOT NULL,
ClientId VARCHAR(50) NOT NULL,
CONSTRAINT FK_Commande_Magasin FOREIGN KEY (MagId) REFERENCES Magasin(MagId),
CONSTRAINT FK_Commande_Client FOREIGN KEY (ClientId) REFERENCES Client(ClientId)
);
-- Table Preferer
CREATE TABLE Preferer (
ProduitId VARCHAR(50) NOT NULL,
ClientId VARCHAR(50) NOT NULL,
PRIMARY KEY (ProduitId, ClientId),
CONSTRAINT FK_Preferer_Produit FOREIGN KEY (ProduitId) REFERENCES Produit(ProduitId),
CONSTRAINT FK_Preferer_Client FOREIGN KEY (ClientId) REFERENCES Client(ClientId)
);
-- Table Stocker
CREATE TABLE Stocker (
ProduitId VARCHAR(50) NOT NULL,
MagId INT NOT NULL,
QteStock INT NOT NULL,
PRIMARY KEY (ProduitId, MagId),
CONSTRAINT FK_Stocker_Produit FOREIGN KEY (ProduitId) REFERENCES Produit(ProduitId),
CONSTRAINT FK_Stocker_Magasin FOREIGN KEY (MagId) REFERENCES Magasin(MagId)
);
-- Table Appartenir_Type
CREATE TABLE Appartenir_Type (
ClientId VARCHAR(50) NOT NULL,
ProfilId INT NOT NULL,
PRIMARY KEY (ClientId, ProfilId),
CONSTRAINT FK_Appartenir_Type_Client FOREIGN KEY (ClientId) REFERENCES Client(ClientId),
CONSTRAINT FK_Appartenir_Type_Profil FOREIGN KEY (ProfilId) REFERENCES TypesDeProfil(ProfilId)
);
-- Table Delivrer
CREATE TABLE Delivrer (
ProduitId VARCHAR(50) NOT NULL,
CommandeId VARCHAR(50) NOT NULL,
QuantiteLivraison INT NOT NULL,
ModeLivraison VARCHAR(50) NOT NULL,
DateLivraison DATE NOT NULL,
PRIMARY KEY (ProduitId, CommandeId),
CONSTRAINT FK_Delivrer_Produit FOREIGN KEY (ProduitId) REFERENCES Produit(ProduitId),
CONSTRAINT FK_Delivrer_Commande FOREIGN KEY (CommandeId) REFERENCES Commande(CommandeId)
);
-- Table Repartir
CREATE TABLE Repartir (
ProduitId VARCHAR(50) NOT NULL,
CommandeId VARCHAR(50) NOT NULL,
MagId INT NOT NULL,
QteRep INT NOT NULL,
PRIMARY KEY (ProduitId, CommandeId, MagId),
CONSTRAINT FK_Repartir_Produit FOREIGN KEY (ProduitId) REFERENCES Produit(ProduitId),
CONSTRAINT FK_Repartir_Commande FOREIGN KEY (CommandeId) REFERENCES Commande(CommandeId),
CONSTRAINT FK_Repartir_Magasin FOREIGN KEY (MagId) REFERENCES Magasin(MagId)
);
-- Table Composer
CREATE TABLE Composer (
ProduitId VARCHAR(50) NOT NULL,
CommandeId VARCHAR(50) NOT NULL,
QteCom INT NOT NULL,
PRIMARY KEY (ProduitId, CommandeId),
CONSTRAINT FK_Composer_Produit FOREIGN KEY (ProduitId) REFERENCES Produit(ProduitId),
CONSTRAINT FK_Composer_Commande FOREIGN KEY (CommandeId) REFERENCES Commande(CommandeId)
);
-- Table Correspondre
CREATE TABLE Correspondre (
ProduitId VARCHAR(50) NOT NULL,
ProfilId INT NOT NULL,
PRIMARY KEY (ProduitId, ProfilId),
CONSTRAINT FK_Correspondre_Produit FOREIGN KEY (ProduitId) REFERENCES Produit(ProduitId),
CONSTRAINT FK_Correspondre_Profil FOREIGN KEY (ProfilId) REFERENCES TypesDeProfil(ProfilId)
);


INSERT INTO Produit (ProduitId, NomProd, PrixAuKg, PrixUnitaire, Poids, Nutriscore, Categorie, Marque) VALUES ('1', 'Spaghetti', NULL, 1.50, 500, 'B', 'Pâtes', 'Panzani');
INSERT INTO Produit (ProduitId, NomProd, PrixAuKg, PrixUnitaire, Poids, Nutriscore, Categorie, Marque) VALUES ('2', 'Piles AA', NULL, 4.99, NULL, 'C', 'Électronique', 'Duracell');
INSERT INTO Produit (ProduitId, NomProd, PrixAuKg, PrixUnitaire, Poids, Nutriscore, Categorie, Marque) VALUES ('3', 'Couches Bébé Taille 3', NULL, 18.99, NULL, 'D', 'Hygiène', 'Pampers');
INSERT INTO Produit (ProduitId, NomProd, PrixAuKg, PrixUnitaire, Poids, Nutriscore, Categorie, Marque) VALUES ('4', 'Sauce Ketchup', 3.98, 2.50, 750, 'C', 'Condiments', 'Heinz');
INSERT INTO Produit (ProduitId, NomProd, PrixAuKg, PrixUnitaire, Poids, Nutriscore, Categorie, Marque) VALUES ('5', 'Saumon Fumé', 35.00, NULL, 200, 'B', 'Poisson', 'Labeyrie');
INSERT INTO Produit (ProduitId, NomProd, PrixAuKg, PrixUnitaire, Poids, Nutriscore, Categorie, Marque) VALUES ('6', 'Banane', 1.50, NULL, 120, 'A', 'Fruits', 'Chiquita');
INSERT INTO Produit (ProduitId, NomProd, PrixAuKg, PrixUnitaire, Poids, Nutriscore, Categorie, Marque) VALUES ('7', 'Pomme', 2.20, NULL, 150, 'A', 'Fruits', 'Pink Lady');
INSERT INTO Produit (ProduitId, NomProd, PrixAuKg, PrixUnitaire, Poids, Nutriscore, Categorie, Marque) VALUES ('8', 'Liquide Vaisselle', NULL, 2.75, NULL, 'B', 'Entretien', 'Paic');
INSERT INTO Produit (ProduitId, NomProd, PrixAuKg, PrixUnitaire, Poids, Nutriscore, Categorie, Marque) VALUES ('9', 'Yaourt Nature', NULL, 2.99, 500, 'A', 'Produits Laitiers', 'Danone');
INSERT INTO Produit (ProduitId, NomProd, PrixAuKg, PrixUnitaire, Poids, Nutriscore, Categorie, Marque) VALUES ('10', 'Eau Minérale 1L', NULL, 0.60, 1000, 'A', 'Boissons', 'Evian');
INSERT INTO Produit (ProduitId, NomProd, PrixAuKg, PrixUnitaire, Poids, Nutriscore, Categorie, Marque) VALUES ('11', 'Céréales Chocolatées', NULL, 4.50, 500, 'C', 'Petit-Déjeuner', 'Kellogg''s');
INSERT INTO Produit (ProduitId, NomProd, PrixAuKg, PrixUnitaire, Poids, Nutriscore, Categorie, Marque) VALUES ('12', 'Poulet Rôti', 8.50, NULL, 1000, 'C', 'Viandes', 'Le Gaulois');
INSERT INTO Produit (ProduitId, NomProd, PrixAuKg, PrixUnitaire, Poids, Nutriscore, Categorie, Marque) VALUES ('13', 'Pain de Mie', NULL, 1.85, 500, 'B', 'Boulangerie', 'Harry''s');
INSERT INTO Produit (ProduitId, NomProd, PrixAuKg, PrixUnitaire, Poids, Nutriscore, Categorie, Marque) VALUES ('14', 'Beurre Demi-Sel', NULL, 2.30, 250, 'A', 'Produits Laitiers', 'Président');
INSERT INTO Produit (ProduitId, NomProd, PrixAuKg, PrixUnitaire, Poids, Nutriscore, Categorie, Marque) VALUES ('15', 'Fromage Râpé', NULL, 3.40, 200, 'B', 'Produits Laitiers', 'Emmental');
INSERT INTO Produit (ProduitId, NomProd, PrixAuKg, PrixUnitaire, Poids, Nutriscore, Categorie, Marque) VALUES ('16', 'Pizza Margherita', NULL, 4.99, 400, 'D', 'Plats Préparés', 'Buitoni');
INSERT INTO Produit (ProduitId, NomProd, PrixAuKg, PrixUnitaire, Poids, Nutriscore, Categorie, Marque) VALUES ('17', 'Chips Nature', NULL, 1.80, 200, 'C', 'Snacks', 'Lay''s');
INSERT INTO Produit (ProduitId, NomProd, PrixAuKg, PrixUnitaire, Poids, Nutriscore, Categorie, Marque) VALUES ('18', 'Lait UHT 1L', NULL, 0.95, 1000, 'A', 'Produits Laitiers', 'Candia');
INSERT INTO Produit (ProduitId, NomProd, PrixAuKg, PrixUnitaire, Poids, Nutriscore, Categorie, Marque) VALUES ('19', 'Oeufs x12', NULL, 3.20, NULL, 'A', 'Produits Frais', 'Matines');
INSERT INTO Produit (ProduitId, NomProd, PrixAuKg, PrixUnitaire, Poids, Nutriscore, Categorie, Marque) VALUES ('20', 'Riz Basmati', NULL, 2.75, 1000, 'A', 'Céréales', 'Taureau Ailé');
INSERT INTO Produit (ProduitId, NomProd, PrixAuKg, PrixUnitaire, Poids, Nutriscore, Categorie, Marque) VALUES ('21', 'Farine de Blé', NULL, 1.20, 1000, 'A', 'Épicerie', 'Francine');
INSERT INTO Produit (ProduitId, NomProd, PrixAuKg, PrixUnitaire, Poids, Nutriscore, Categorie, Marque) VALUES ('22', 'Jambon Blanc', 12.00, NULL, 150, 'B', 'Charcuterie', 'Herta');
INSERT INTO Produit (ProduitId, NomProd, PrixAuKg, PrixUnitaire, Poids, Nutriscore, Categorie, Marque) VALUES ('23', 'Crème Fraîche', NULL, 2.50, 200, 'B', 'Produits Laitiers', 'Elle et Vire');
INSERT INTO Produit (ProduitId, NomProd, PrixAuKg, PrixUnitaire, Poids, Nutriscore, Categorie, Marque) VALUES ('24', 'Lentilles Vertes', NULL, 2.10, 500, 'A', 'Céréales', 'Celnat');
INSERT INTO Produit (ProduitId, NomProd, PrixAuKg, PrixUnitaire, Poids, Nutriscore, Categorie, Marque) VALUES ('25', 'Sucre en Poudre', NULL, 1.50, 1000, 'C', 'Épicerie', 'Daddy');
INSERT INTO Produit (ProduitId, NomProd, PrixAuKg, PrixUnitaire, Poids, Nutriscore, Categorie, Marque) VALUES ('26', 'Chocolat Noir', NULL, 2.99, 200, 'B', 'Snacks', 'Lindt');
INSERT INTO Produit (ProduitId, NomProd, PrixAuKg, PrixUnitaire, Poids, Nutriscore, Categorie, Marque) VALUES ('27', 'Huile d Olive', NULL, 5.99, 750, 'A', 'Condiments', 'Puget');
INSERT INTO Produit (ProduitId, NomProd, PrixAuKg, PrixUnitaire, Poids, Nutriscore, Categorie, Marque) VALUES ('28', 'Cornichons', NULL, 2.75, 400, 'A', 'Condiments', 'Amora');
INSERT INTO Produit (ProduitId, NomProd, PrixAuKg, PrixUnitaire, Poids, Nutriscore, Categorie, Marque) VALUES ('29', 'Thé Vert', NULL, 3.99, NULL, 'A', 'Boissons', 'Lipton');
INSERT INTO Produit (ProduitId, NomProd, PrixAuKg, PrixUnitaire, Poids, Nutriscore, Categorie, Marque) VALUES ('30', 'Yaourt aux Fruits', NULL, 3.50, 500, 'B', 'Produits Laitiers', 'Activia');
INSERT INTO Produit (ProduitId, NomProd, PrixAuKg, PrixUnitaire, Poids, Nutriscore, Categorie, Marque) VALUES ('31', 'Glace à la Vanille', NULL, 4.50, 500, 'C', 'Desserts', 'Haagen-Dazs');
INSERT INTO Produit (ProduitId, NomProd, PrixAuKg, PrixUnitaire, Poids, Nutriscore, Categorie, Marque) VALUES ('32', 'Viande Hachée', 10.50, NULL, 500, 'C', 'Viandes', 'Charal');
INSERT INTO Produit (ProduitId, NomProd, PrixAuKg, PrixUnitaire, Poids, Nutriscore, Categorie, Marque) VALUES ('33', 'Saucisson Sec', 14.00, NULL, 250, 'D', 'Charcuterie', 'Cochonou');
INSERT INTO Produit (ProduitId, NomProd, PrixAuKg, PrixUnitaire, Poids, Nutriscore, Categorie, Marque) VALUES ('34', 'Haricots Verts Surgelés', NULL, 2.50, 1000, 'A', 'Surgelés', 'Bonduelle');
INSERT INTO Produit (ProduitId, NomProd, PrixAuKg, PrixUnitaire, Poids, Nutriscore, Categorie, Marque) VALUES ('35', 'Confiture de Fraises', NULL, 3.20, 370, 'B', 'Épicerie', 'Bonne Maman');
INSERT INTO Produit (ProduitId, NomProd, PrixAuKg, PrixUnitaire, Poids, Nutriscore, Categorie, Marque) VALUES ('36', 'Poivrons Rouges', 3.00, NULL, 300, 'A', 'Légumes', 'Bio');
INSERT INTO Produit (ProduitId, NomProd, PrixAuKg, PrixUnitaire, Poids, Nutriscore, Categorie, Marque) VALUES ('37', 'Café Moulu', NULL, 4.99, 250, 'B', 'Boissons', 'Lavazza');
INSERT INTO Produit (ProduitId, NomProd, PrixAuKg, PrixUnitaire, Poids, Nutriscore, Categorie, Marque) VALUES ('38', 'Miel', NULL, 6.50, 500, 'A', 'Épicerie', 'Lune de Miel');
INSERT INTO Produit (ProduitId, NomProd, PrixAuKg, PrixUnitaire, Poids, Nutriscore, Categorie, Marque) VALUES ('39', 'Pâte à Tartiner', NULL, 3.90, 750, 'C', 'Épicerie', 'Nutella');
INSERT INTO Produit (ProduitId, NomProd, PrixAuKg, PrixUnitaire, Poids, Nutriscore, Categorie, Marque) VALUES ('40', 'Savon Liquide', NULL, 2.75, 300, 'B', 'Hygiène', 'Le Petit Marseillais');
INSERT INTO Produit (ProduitId, NomProd, PrixAuKg, PrixUnitaire, Poids, Nutriscore, Categorie, Marque) VALUES ('41', 'Eau Gazeuse 1L', NULL, 0.80, 1000, 'A', 'Boissons', 'Perrier');
INSERT INTO Produit (ProduitId, NomProd, PrixAuKg, PrixUnitaire, Poids, Nutriscore, Categorie, Marque) VALUES ('42', 'Blanc de Dinde', 12.00, NULL, 150, 'B', 'Charcuterie', 'Fleury Michon');
INSERT INTO Produit (ProduitId, NomProd, PrixAuKg, PrixUnitaire, Poids, Nutriscore, Categorie, Marque) VALUES ('43', 'Carottes', 1.20, NULL, 1000, 'A', 'Légumes', 'Bio');
INSERT INTO Produit (ProduitId, NomProd, PrixAuKg, PrixUnitaire, Poids, Nutriscore, Categorie, Marque) VALUES ('44', 'Fromage Blanc', NULL, 2.50, 500, 'A', 'Produits Laitiers', 'Yoplait');
INSERT INTO Produit (ProduitId, NomProd, PrixAuKg, PrixUnitaire, Poids, Nutriscore, Categorie, Marque) VALUES ('45', 'Biscottes Complètes', NULL, 2.99, 500, 'B', 'Petit-Déjeuner', 'Heudebert');
INSERT INTO Produit (ProduitId, NomProd, PrixAuKg, PrixUnitaire, Poids, Nutriscore, Categorie, Marque) VALUES ('46', 'Gel Douche', NULL, 3.20, 300, 'B', 'Hygiène', 'Dove');
INSERT INTO Produit (ProduitId, NomProd, PrixAuKg, PrixUnitaire, Poids, Nutriscore, Categorie, Marque) VALUES ('47', 'Pommes de Terre', 0.95, NULL, 2000, 'A', 'Légumes', 'Agriculteurs Locaux');
INSERT INTO Produit (ProduitId, NomProd, PrixAuKg, PrixUnitaire, Poids, Nutriscore, Categorie, Marque) VALUES ('48', 'Epinards Surgelés', NULL, 2.10, 1000, 'A', 'Surgelés', 'Bonduelle');
INSERT INTO Produit (ProduitId, NomProd, PrixAuKg, PrixUnitaire, Poids, Nutriscore, Categorie, Marque) VALUES ('49', 'Saucisses de Francfort', 9.00, NULL, 300, 'C', 'Charcuterie', 'Herta');
INSERT INTO Produit (ProduitId, NomProd, PrixAuKg, PrixUnitaire, Poids, Nutriscore, Categorie, Marque) VALUES ('50', 'Coca-Cola 1.5L', NULL, 1.50, 1500, 'D', 'Boissons', 'Coca-Cola');
