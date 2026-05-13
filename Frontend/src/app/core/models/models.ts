// ===== API Response wrapper =====
export interface ApiResponse<T> {
  success: boolean;
  message: string;
  data: T;
}

// ===== Vehicule =====
export interface Vehicule {
  id?: number;
  immatriculation: string;
  modele: string;
  type: string;
  kilometrage: number;
  statut: StatutVehicule;
  nombreMissions?: number;
  distanceTotale?: number;
  coutCarburantTotal?: number;
}

export type StatutVehicule = 'DISPONIBLE' | 'EN_MISSION' | 'EN_MAINTENANCE' | 'EN_PANNE';

// ===== Chauffeur =====
export interface Chauffeur {
  id?: number;
  nom: string;
  permis: string;
  experience: number;
  nombreMissions?: number;
  distanceTotale?: number;
}

// ===== TrajetMission =====
export interface TrajetMission {
  id?: number;
  vehiculeId: number;
  vehiculeImmatriculation?: string;
  chauffeurId: number;
  chauffeurNom?: string;
  pointDepart: string;
  destination: string;
  distance: number;
  dateMission?: string;
  statut?: StatutMission;
}

export type StatutMission = 'EN_COURS' | 'TERMINEE' | 'ANNULEE';

// ===== Consommation =====
export interface Consommation {
  id?: number;
  vehiculeId: number;
  vehiculeImmatriculation?: string;
  date: string;
  quantiteCarburant: number;
  coutTotal: number;
}

// ===== Dashboard =====
export interface DashboardData {
  totalVehicules: number;
  vehiculesDisponibles: number;
  vehiculesEnMission: number;
  vehiculesEnMaintenance: number;
  totalChauffeurs: number;
  missionsEnCours: number;
  totalMissions: number;
  coutCarburantTotal: number;
  distanceTotale: number;
  alertesMaintenance: AlerteMaintenance[];
}

export interface AlerteMaintenance {
  vehiculeId: number;
  immatriculation: string;
  modele: string;
  kilometrageActuel: number;
  seuilMaintenance: number;
  depassement: number;
  niveauAlerte: 'CRITIQUE' | 'ATTENTION' | 'INFO';
}

// ===== Reporting =====
export interface VehiculeActif {
  vehiculeId: number;
  immatriculation: string;
  modele: string;
  type: string;
  kilometrage: number;
  nombreMissions: number;
  distanceTotale: number;
  coutCarburant: number;
}

export interface CoutCarburant {
  vehiculeId: number;
  immatriculation: string;
  modele: string;
  coutTotal: number;
  quantiteTotale: number;
  nombrePleins: number;
}

export interface ConsommationMensuelle {
  annee: number;
  mois: number;
  moisLibelle: string;
  coutTotal: number;
  quantiteTotale: number;
}
